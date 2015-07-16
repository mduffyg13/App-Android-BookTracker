package uni.android.md.muc_coursework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //Icon buttons
    ImageButton btnNews;
    ImageButton btnStats;
    ImageButton btnLibrary;
    ImageButton btnMap;

    //Reading library controls
    Button btnUpdate;
    Button btnFinished;
    TextView txtCurrentlyReading;
    ImageButton ibtnPrev;
    ImageButton ibtnNext;
    SeekBar pageSeekBar;
    TextView tvPageNo;

    //Database access
    Library userLibrary;
    CommitDBMgr cdbMgr;
    Book[] currentlyReading;
    Commit[] commits;
    int bookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load default preferences, defined in preference menu layout file into shared preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        //SharedPreferences b = PreferenceManager.getDefaultSharedPreferences(this);
        //String bRSSFeedURL = b.getString("PREF_LIST", "default choice");
        //String cRSSFeedURL = b.getString("PREF_LIST", "default choice");


        //Icon buttons
        btnNews = (ImageButton) findViewById(R.id.btnNews);
        btnNews.setOnClickListener(this);
        btnStats = (ImageButton) findViewById(R.id.btnStats);
        btnStats.setOnClickListener(this);
        btnLibrary = (ImageButton) findViewById(R.id.btnLibrary);
        btnLibrary.setOnClickListener(this);
        btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);

        //Commit buttons
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnFinished = (Button) findViewById(R.id.btnFinished);
        btnFinished.setOnClickListener(this);

        //Book cycle buttons
        ibtnPrev = (ImageButton) findViewById(R.id.ibtnPrev);
        ibtnPrev.setOnClickListener(this);
        ibtnNext = (ImageButton) findViewById(R.id.ibtnNext);
        ibtnNext.setOnClickListener(this);

        //Display currently reading selection
        txtCurrentlyReading = (TextView) findViewById(R.id.txtCurrentlyReading);

        //Current page objects
        pageSeekBar = (SeekBar) findViewById(R.id.pageSeekBar);
        tvPageNo = (TextView) findViewById(R.id.txtPageNo);


        //Attach listener to seek bar
        pageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //When seek bar is altered
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Display current value of seek bar
                tvPageNo.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


//Book database
        userLibrary = new Library(this);

        //Load commit database
        cdbMgr = new CommitDBMgr(this, "sqlLibrary2.s3db", null, 1);

        try {
            cdbMgr.dbCreate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Load Commits(Dont need?)
        commits = cdbMgr.loadCommits();

        userLibrary.loadLibrary();

        //Load currently reading books from database
        currentlyReading = userLibrary.getReading();


        bookId = 0;
        displayCurrentlyReading();


    }

@Override
    public void onResume(){
    super.onResume();
    currentlyReading = userLibrary.getReading();
        displayCurrentlyReading();
    }
    //Update screen component to reflect currently selected book
    public void displayCurrentlyReading() {
        txtCurrentlyReading.setText(currentlyReading[bookId].getTitle());
        //Set max seek bar value to total number of book pages
        pageSeekBar.setMax(currentlyReading[bookId].getNumberOfPages());
        //Set seek bar progress to current page
        pageSeekBar.setProgress(currentlyReading[bookId].getCurrentPage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.isLoggable("TEST", item.getItemId());
        if (id == R.id.action_settings) {


            //Log.e("PREF", "SETTINGS SELECTED");
            //Load settings screen
            Intent settingsScreen = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsScreen);

        }
        if (id == R.id.action_about) {
            //Display about dialog box
            showAbout();

        }
        if (id == R.id.quit) {
            //Exit application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    //public void onDestroy(){
    //int i = 0;
    //   i ++;
    //}
    @Override
    public void onClick(View view) {
        //screen object clicked
        switch (view.getId()) {
            //Icon buttons, move to new activity
            case R.id.btnNews:
                Intent newsScreen = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(newsScreen);
                break;
            case R.id.btnStats:
                Intent statsScreen = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(statsScreen);
                break;
            case R.id.btnLibrary:
                Intent libraryScreen = new Intent(getApplicationContext(), LibraryActivity.class);
                startActivity(libraryScreen);
                break;
            case R.id.btnMap:
                Intent mapScreen = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapScreen);
                break;


//Buttons to cycle through currently reading books
            case R.id.ibtnNext:
                if (bookId < currentlyReading.length - 1) {
                    bookId++;
                } else {
                    bookId = 0;
                }
                //update screen
                displayCurrentlyReading();
                break;
            case R.id.ibtnPrev:
                if (bookId == 0) {
                    bookId = currentlyReading.length - 1;
                } else {
                    bookId--;
                }
                //update screen
                displayCurrentlyReading();
                break;

            //Update commit buttons
            case R.id.btnUpdate:
                //add commit to database
                createCommit();
                //update loaded commits
                commits = cdbMgr.loadCommits();
                break;
            case R.id.btnFinished:
                //add commit to database
                finishBook();
                //update book state in database
                currentlyReading = userLibrary.getReading();
                //reload commits
                commits = cdbMgr.loadCommits();
                break;
            default:
                break;
        }

    }

    private void createCommit() {
//function that constructs new commit object that is sent for entry to the database

        //Calculate pages read
        int current_page = currentlyReading[bookId].getCurrentPage();
        int pages_read = pageSeekBar.getProgress() - current_page;

        //If pages read a positive value
        if (pages_read > 0) {

            //Create new commit
            Date date = new Date();
            Commit c = new Commit();
            c.setDate(date);
            c.setPages(pages_read);

            //Add to database
            cdbMgr.addCommit(c);

            //Update currently reading page
            Book b = currentlyReading[bookId];
            b.setCurrentPage(current_page + pages_read);

            //Update database
            userLibrary.AddUpdateBook(b);
            b = currentlyReading[bookId];

//display progress to user
            Toast.makeText(this, "New current page: " + b.getCurrentPage(), Toast.LENGTH_SHORT).show();

        } else {
//display error to user
            Toast.makeText(this, "Commit not made( negative value )", Toast.LENGTH_SHORT).show();
        }
        //make new commit
        //update library

        //update pages read
        //c.setPages();


    }

    private void finishBook() {
        //Same as update book, added info send to book database

        //Calculate pages read
        int current_page = currentlyReading[bookId].getCurrentPage();
        int pages_read = currentlyReading[bookId].getNumberOfPages() - current_page;

        //If pages read a positive value
        if (pages_read > 0) {

            //Create new commit
            Date date = new Date();
            Commit c = new Commit();
            c.setDate(date);
            c.setPages(pages_read);

            //Add to database
            cdbMgr.addCommit(c);

            //Update current book
            Book b = currentlyReading[bookId];
            //Reading info reset
            b.setReading(false);
            b.setCurrentPage(0);

            //Update database
            userLibrary.AddUpdateBook(b);
            b = currentlyReading[bookId];


            Toast.makeText(this, "Back on shelf", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "Commit not made( negative value )", Toast.LENGTH_SHORT).show();
        }

    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        //Build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.abc_ic_search); //set box icon
        builder.setTitle(R.string.app_name);    //set title
        builder.setView(messageView);
        builder.create();
        builder.show();
    }
}

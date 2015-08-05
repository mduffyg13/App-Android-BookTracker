package uni.android.md.muc_coursework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class LibraryActivity extends ActionBarActivity implements DialogInterface.OnClickListener, View.OnClickListener {
//Activity class for library interface


    //Current users library
    Library userLibrary;

    //List view
    ListView lvLibrary;
    ArrayAdapter_Library lAdapter;

    Book[] b;
    Button btnAddBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        //Load full library
        userLibrary = new Library(this);
        userLibrary.loadLibrary();

        btnAddBook = (Button) findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(this);

        //Create adapter for listview, takes array of books and layout id
        lAdapter = new ArrayAdapter_Library(this, R.layout.listview_item_book, userLibrary.getLibrary());

        lvLibrary = (ListView) findViewById(R.id.lvLibrary);

        //fill list view with adapter result
        lvLibrary.setAdapter(lAdapter);

        lvLibrary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Log.e("TEST", Integer.toString(i));
            }
        });

        //Attach listview for context menu use
        registerForContextMenu(lvLibrary);
    }

    @Override
    public void onResume(){
        super.onResume();
        LoadLibray();
    }
    private void LoadLibray() {
        //Loads full library, used to refresh screen on updates

        userLibrary.loadLibrary();
        lAdapter = new ArrayAdapter_Library(this, R.layout.listview_item_book, userLibrary.getLibrary());

        lvLibrary = (ListView) findViewById(R.id.lvLibrary);
        lvLibrary.setAdapter(lAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //displays context menu on long click on list view item
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        //use book menu layout
        inflater.inflate(R.menu.book, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Listener for context menu interaction
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            //Edit button
            case R.id.edit:

                //Call dialog, pass currently selected item
                showEditDialog(info.position);

                return true;
            //Delete button
            case R.id.delete:
                //Log.e("CONTEXT TEST", Integer.toString(info.position));
                //Send currently selected item to database for deletion
                this.deleteBook(info.position);
                //Reload altered library
                this.LoadLibray();

                return true;
            //Read button
            case R.id.read:
                //Update selected item
                this.readBook(info.position);
                this.LoadLibray();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    //Components of dialog
    EditText edTitle;
    EditText edAuthor;
    EditText edPages;
    Book aBook;

    private void showEditDialog(int position) {
        // Get currently selected book from database
        aBook = userLibrary.getLibrary()[position];

        //Build and display dialog
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("EDIT BOOK");
        ab.setPositiveButton("SAVE", this);
        ab.setNegativeButton("CANCEL", this);

        LayoutInflater lf = getLayoutInflater();
        View dialogView = lf.inflate(R.layout.dialog_book, null);


        ab.setView(dialogView);

        //Add selected book to edit box
        edTitle = (EditText) dialogView.findViewById(R.id.edTitle);
        edTitle.setText(aBook.getTitle());
        edAuthor = (EditText) dialogView.findViewById(R.id.edAuthor);
        edAuthor.setText(aBook.getAuthor());
        edPages = (EditText) dialogView.findViewById(R.id.edPages);
        edPages.setText(Integer.toString(aBook.getNumberOfPages()));


        //Show completed dialog
        AlertDialog alertDialog = ab.create();
        alertDialog.show();


    }

    private void showAddDialog() {
        //Same function as above, loads with no book selected, new book instance


        aBook = new Book();

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("ADD BOOK");
        ab.setPositiveButton("SAVE", this);
        ab.setNegativeButton("CANCEL", this);

        LayoutInflater lf = getLayoutInflater();
        View dialogView = lf.inflate(R.layout.dialog_book, null);


        ab.setView(dialogView);

        //Add selected book to edit box
        edTitle = (EditText) dialogView.findViewById(R.id.edTitle);
        edTitle.setText(aBook.getTitle());
        edAuthor = (EditText) dialogView.findViewById(R.id.edAuthor);
        edAuthor.setText(aBook.getAuthor());
        edPages = (EditText) dialogView.findViewById(R.id.edPages);
        edPages.setText(Integer.toString(aBook.getNumberOfPages()));


        AlertDialog alertDialog = ab.create();
        alertDialog.show();


    }

    private void readBook(int id) {
        userLibrary.ReadBook(id);
    }

    private boolean deleteBook(int id) {
        userLibrary.DeleteBook(id);
        // lAdapter.notifyDataSetChanged();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        //Dialog button listener

        //Save entered book instance
        if (i == -1) {
            updateBook();

        }

    }

    private void updateBook() {
//Construct book instance for entry to database
        aBook.setTitle(edTitle.getText().toString());
        aBook.setAuthor(edAuthor.getText().toString());
        int pages = Integer.parseInt(edPages.getText().toString());

        aBook.setNumberOfPages(pages);

        //Database update entry
        userLibrary.AddUpdateBook(aBook);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Add book button
            case R.id.btnAddBook:
                //Display add book dialog
                showAddDialog();
                Toast.makeText(this, "Add Book", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }
}

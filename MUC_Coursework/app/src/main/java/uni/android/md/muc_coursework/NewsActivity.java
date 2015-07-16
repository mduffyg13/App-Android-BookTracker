package uni.android.md.muc_coursework;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.concurrent.ExecutionException;


public class NewsActivity extends ActionBarActivity {

    ListView lvNews;
    ArrayAdapter_News nAdapter;
    RSSDataItem[] di;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        lvNews = (ListView) findViewById(R.id.lvNews);

        //Load pref, rss feed to read
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sRSSFeedURL = sharedPreferences.getString("PREF_LIST", "default choice");


        String RSSFeedURL = sRSSFeedURL;
        //Start reader thread
        AsyncRSSParser aParse = new AsyncRSSParser(this, RSSFeedURL);

        try {
            //Get results of parse when complete
            di = aParse.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Send parsed into to list view adapter
        nAdapter = new ArrayAdapter_News(this, R.layout.listview_item_book, di);

        //Fill listview
        lvNews = (ListView) findViewById(R.id.lvNews);
        lvNews.setAdapter(nAdapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//Load web link for selected item
                String weblink = di[i].getItemLink();
                Log.e("LIST TEST", weblink);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(weblink));
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);
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


}

package uni.android.md.muc_coursework;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;

/**
 * Created by Mark on 22/11/2014.
 */
public class AsyncRSSParser extends AsyncTask<String, Integer, RSSDataItem[]> {
    //Class starts thread which using an instance of RSSParser to load items from
//an rss feed.	


    private Context appContext;
    private String urlRSSToParse;

    public AsyncRSSParser(Context currentAppContext, String urlRSS) {
        appContext = currentAppContext;
        urlRSSToParse = urlRSS;
    }

    // A callback method executed on UI thread on starting the task
    @Override
    protected void onPreExecute() {
        // Message to indicate start of parsing
        Toast.makeText(appContext, "Parsing started!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected RSSDataItem[] doInBackground(String... params) {
//method runs and returns info to calling activity when complete    
        RSSDataItem parsedData;
        RSSDataItem[] parsedDatas;
        RSSParser rssParser = new RSSParser();
        try {
            rssParser.parseRSSData(urlRSSToParse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // parsedData = rssParser.getRSSDataItem();
        // return parsedData;
        parsedDatas = rssParser.getRSSDataItems();
        return parsedDatas;
    }


    // A callback method executed on UI thread, invoked after the completion of the task
    // When doInbackground has completed, the return value from that method is passed into this event
    // handler.
    @Override
    protected void onPostExecute(RSSDataItem[] result) {
        // Message to indicate end of parsing
        Toast.makeText(appContext, "Parsing finished!", Toast.LENGTH_SHORT).show();
    }


}

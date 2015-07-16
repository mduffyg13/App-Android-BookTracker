package uni.android.md.muc_coursework;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Mark on 22/11/2014.
 */
public class RSSParser {
//Class which connects to rss feed
//Reads in xml string and converts to RSSDataItem instances
//which are returned to calling activity in an array

    private RSSDataItem RSSDataItem;

    private RSSDataItem[] items;
    int i;
    int itemLimit = 50;

    public void setRSSDataItem(String sItemData) {
        RSSDataItem.setItemTitle(sItemData);
        RSSDataItem.setItemDesc(sItemData);
        RSSDataItem.setItemLink(sItemData);
    }

    public RSSDataItem getRSSDataItem() {
        return this.RSSDataItem;
    }

    public RSSDataItem[] getRSSDataItems() {

        //Trim empty space from array
        int j = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                j++;

            }
        }
        int result = j;

        RSSDataItem[] returned;
        returned = Arrays.copyOf(items, result);

        return returned;
    }

    public RSSParser() {
        items = new RSSDataItem[itemLimit];
        this.RSSDataItem = new RSSDataItem();
        setRSSDataItem(null);
        i = 0;
    }

    public void parseRSSDataItem(XmlPullParser theParser, int theEventType) throws IOException, XmlPullParserException {
        try {
            while (theEventType != XmlPullParser.END_DOCUMENT) {
                // Found a start tag
                if (theEventType == XmlPullParser.START_TAG) {
                    // Check which Tag has been found
                    if (theParser.getName().equalsIgnoreCase("item")) {
                        RSSDataItem = new RSSDataItem();
                    }
                    if (theParser.getName().equalsIgnoreCase("title")) {
                        // Now just get the associated text
                        String temp = theParser.nextText();
                        // store data in class
                        RSSDataItem.setItemTitle(temp);
                    } else
                        // Check which Tag we have
                        if (theParser.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = theParser.nextText();
                            // store data in class
                            RSSDataItem.setItemDesc(temp);
                        } else
                            // Check which Tag we have
                            if (theParser.getName().equalsIgnoreCase("link")) {
                                // Now just get the associated text
                                String temp = theParser.nextText();
                                // store data in class
                                RSSDataItem.setItemLink(temp);
                            }

                    if (RSSDataItem.getItemDesc() != "" && RSSDataItem.getItemTitle() != "" && RSSDataItem.getItemLink() != ""
                            && RSSDataItem.getItemDesc() != null && RSSDataItem.getItemDesc().toLowerCase() != "index") {

                        if (i != 0) {
                            items[i - 1] = RSSDataItem;
                        }
                        i++;
                        RSSDataItem = new RSSDataItem();
                    }
                }

                // Get the next event
                theEventType = theParser.next();

            } // End of while


        } catch (XmlPullParserException parserExp1) {
            Log.e("MyTag", "Parsing error" + parserExp1.toString());
        } catch (IOException parserExp1) {
            Log.e("MyTag", "IO error during parsing");
        }

    }

    public void parseRSSData(String RSSItemsToParse) throws MalformedURLException {
        URL rssURL = new URL(RSSItemsToParse);
        InputStream rssInputStream;
        try {
            XmlPullParserFactory parseRSSfactory = XmlPullParserFactory.newInstance();
            parseRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parseRSSfactory.newPullParser();
            String xmlRSS = getStringFromInputStream(getInputStream(rssURL), "UTF-8");
            RSSxmlPP.setInput(new StringReader(xmlRSS));
            int eventType = RSSxmlPP.getEventType();

            parseRSSDataItem(RSSxmlPP, eventType);

        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.e("MyTag", "End document");
    }

    public InputStream getInputStream(URL url) throws IOException {
        return url.openConnection().getInputStream();
    }

    public static String getStringFromInputStream(InputStream stream, String charsetName) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}

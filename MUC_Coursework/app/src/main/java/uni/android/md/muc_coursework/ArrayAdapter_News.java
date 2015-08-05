package uni.android.md.muc_coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Mark on 10/12/2014.
 */
public class ArrayAdapter_News extends ArrayAdapter<RSSDataItem> {
    //Used for loading news items into a list view
    //Works same way as library list view
    //Formatting of news to be improved


    private final Context context;
    private final RSSDataItem[] values;

    public ArrayAdapter_News(Context context, int resource, RSSDataItem[] values) {
        super(context, R.layout.listview_item_book, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_item_news, parent, false);

        TextView tv1 = (TextView) rowView.findViewById(R.id.txtTitle);
        TextView tv2 = (TextView) rowView.findViewById(R.id.txtDesc);

        final RSSDataItem thisItem = values[position];
        tv1.setText(values[position].getItemTitle());

        //XMl text formatted for display in list
        String desc = values[position].getItemDesc();
        desc = desc.substring(0, 150) + "....";
        desc = desc.replace("<p>", "");
        desc = desc.replace("<strong>", "");
        tv2.setText(desc);

        return rowView;
    }
}



package uni.android.md.muc_coursework;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mark on 22/11/2014.
 */
public class ArrayAdapter_Library extends ArrayAdapter<Book> {

    private final Context context; //App context adapter called from
    private final Book[] values;    //values used to fill list view


    public ArrayAdapter_Library(Context context, int resource, Book[] values) {
        super(context, R.layout.listview_item_book, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //For each item in array, list item is created using layout

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Load layout for list item
        View rowView = inflater.inflate(R.layout.listview_item_book, parent, false);

        //Add current items information to to layout objects
        TextView tv1 = (TextView) rowView.findViewById(R.id.textView);
        TextView tv2 = (TextView) rowView.findViewById(R.id.textView2);
        ImageView imBook = (ImageView) rowView.findViewById(R.id.imBook);
        // ImageButton ibtnBook = (ImageButton)rowView.findViewById(R.id.ibtnBook);

        final Book thisBook = values[position];
        tv1.setText(values[position].getTitle());
        tv2.setText(values[position].getAuthor());

        //Display appropriate icon for reading state of book
        if (values[position].isReading()) {
            imBook.setImageResource(R.drawable.ic_book_open);
        } else {
            imBook.setImageResource(R.drawable.ic_book_closed);
        }

//return completed list item
        return rowView;
    }
}

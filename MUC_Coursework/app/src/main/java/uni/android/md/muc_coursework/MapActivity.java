package uni.android.md.muc_coursework;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity {
    //TO ADD - Get current location, perform search for key words

    //Map instance to display.
    private GoogleMap map;
    //Current location.
    private LatLng GLASGOW = new LatLng(55.8580, -4.2590);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SetupMap();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.map, menu);
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

    public void SetupMap() {

        //Create and attach google map to map fragment in layout file
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map != null) {
            //Move and zoom map to current location.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(GLASGOW, 14));
            //Enable user controls on map.
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
        }
        //Define marker locations.
        LatLng w1 = new LatLng(55.864703, -4.258084);
        LatLng w2 = new LatLng(55.858468, -4.256433);
        LatLng fb = new LatLng(55.862469, -4.253208);
        LatLng ml = new LatLng(55.865110, -4.271820);

        //Attach markers to map
        Marker m = map.addMarker(new MarkerOptions()
                        .position(w1)
                        .title("Waterstones")
        );
        m = map.addMarker(new MarkerOptions()
                        .position(w2)
                        .title("Waterstones")
        );
        m = map.addMarker(new MarkerOptions()
                        .position(fb)
                        .title("Forbidden Planet")
        );
        m = map.addMarker(new MarkerOptions()
                        .position(ml)
                        .title("Mitchell Library")

        );

    }
}

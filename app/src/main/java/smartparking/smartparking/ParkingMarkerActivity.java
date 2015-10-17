package smartparking.smartparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ParkingMarkerActivity extends Activity {
    private Button saveParking;
    private Button releaseParking;
    private LatLng latLng;
    private String id;
    private String title;
    private ParseObject obj;
    private TextView price;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_marker2);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        saveParking = (Button) (findViewById(R.id.saveParkingSlot));
        releaseParking = (Button) (findViewById(R.id.releaseParking));


        latLng = (LatLng) getIntent().getExtras().get("position");
        id = getIntent().getExtras().getString("id");
        title = getIntent().getExtras().getString("title");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AvailableParking");
        try {
            obj = query.get(title);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(obj.get("status").toString().equals("booked")){
            releaseParking.setEnabled(false);
            text1.setText("This parking is not available");
        }
        RatingBar rate_bar =  (RatingBar)findViewById(R.id.ratingBar1);
        rate_bar.setRating(4.0f);
        price = (TextView) (findViewById(R.id.price));
        price.setText(obj.get("Cost").toString());
        text1= (TextView) (findViewById(R.id.textView));

        saveParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.put("isAvailable", false);
                obj.put("status", "booked");
                obj.saveInBackground();
                Toast.makeText(getApplicationContext(), latLng.latitude + ""+latLng.longitude, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Parking Booked", Toast.LENGTH_LONG).show();
                releaseParking.setEnabled(true);
                saveParking.setEnabled(false);
                text1.setText("This parking is not available");

                // ParseObject testObject = new ParseObject("AvailableParking");
                // testObject.put("Longitude", longiStr);
                // testObject.put("Latitude", latiStr);
                // testObject.saveInBackground();
                // Toast.makeText(getApplicationContext(), latitude + "", Toast.LENGTH_LONG).show();
                // Toast.makeText(getApplicationContext(), "Parking freed", Toast.LENGTH_LONG).show();
            }
        });
        releaseParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.put("isAvailable",true);
                obj.put("status","free");
                obj.saveInBackground();
                Toast.makeText(getApplicationContext(), latLng.latitude + ""+latLng.longitude, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Parking Released", Toast.LENGTH_LONG).show();
                releaseParking.setEnabled(false);
                saveParking.setEnabled(true);
                text1.setText("This parking is available");
                // ParseObject testObject = new ParseObject("AvailableParking");
                // testObject.put("Longitude", longiStr);
                // testObject.put("Latitude", latiStr);
                // testObject.saveInBackground();
                // Toast.makeText(getApplicationContext(), latitude + "", Toast.LENGTH_LONG).show();
                // Toast.makeText(getApplicationContext(), "Parking freed", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parking_marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}

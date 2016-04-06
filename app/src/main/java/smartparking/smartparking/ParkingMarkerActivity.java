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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import smartparking.smartparking.model.ParkingSpot;
import smartparking.smartparking.model.User;
import smartparking.smartparking.util.AppConstants;
import smartparking.smartparking.util.ImageDownloader;


public class ParkingMarkerActivity extends Activity {
    private LatLng latLng;
    private String id;
    private String spot_name;
    private TextView parking_name, priceText, descText;
    private ImageView parkingImage;
    private ImageDownloader imageDownloader;
    private ParkingSpot spot;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserved_parking_layout);
        //etActionBar().setDisplayHomeAsUpEnabled(true);
        parking_name = (TextView) (findViewById(R.id.parking_name));
        priceText = (TextView) (findViewById(R.id.price));
        descText = (TextView) (findViewById(R.id.desc));
        parkingImage = (ImageView) findViewById(R.id.parking_image);

        imageDownloader = new ImageDownloader();

        spot = (ParkingSpot) getIntent().getSerializableExtra(AppConstants.SPOT);
        user = (User) getIntent().getSerializableExtra(AppConstants.USER);
        spot_name = spot.getName();
        parking_name.setText(spot_name);
        priceText.setText("Price: " + spot.getPriceDesc());
        descText.setText("Parking Spot was reserved on " +  user.getReservationDate().toString());

        if(spot.getImageUrl() != null && !spot.getImageUrl().equalsIgnoreCase(""))
            imageDownloader.download(spot.getImageUrl(), parkingImage);

/*
        //
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AvailableParking");
        try {
            obj = query.get(title);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(obj.get("status").toString().equals("booked")){
            saveParking.setEnabled(false);
            text1.setText("This parking is not available");
        }
        RatingBar rate_bar =  (RatingBar)findViewById(R.id.ratingBar1);
        rate_bar.setRating(4.0f);
        price = (TextView) (findViewById(R.id.price));
        price.setText(obj.get("Cost").toString());


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
*/
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

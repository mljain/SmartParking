package smartparking.smartparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.ParseUser;

import java.util.Date;

import smartparking.smartparking.model.ParkingSpot;
import smartparking.smartparking.model.User;
import smartparking.smartparking.util.AppConstants;
import smartparking.smartparking.util.ImageDownloader;


public class ParkingMarkerActivity extends Activity {
    private LatLng latLng;
    private String id;
    private Button releaseButton;
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
        releaseButton = (Button) findViewById(R.id.releaseParkingButton);

        imageDownloader = new ImageDownloader();

        user = (User) getIntent().getSerializableExtra(AppConstants.USER);
        spot = (ParkingSpot) getIntent().getSerializableExtra(AppConstants.SPOT);

        if( spot == null)
            spot = user.getParkingSpot();


        parking_name.setText(spot.getName());
        priceText.setText("Price: " + spot.getPriceDesc());
        descText.setText("Parking Spot was reserved on " + user.getReservationDate().toString());

        if(spot.getImageUrl() != null && !spot.getImageUrl().equalsIgnoreCase(""))
            imageDownloader.download(spot.getImageUrl(), parkingImage);


        releaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    ParseUser pu = ParseUser.getCurrentUser();
                    pu.put(AppConstants.HAS_PARKING, false);
                    pu.put(AppConstants.PARKING_ID, "");
                    pu.remove(AppConstants.RESERVATION_DATE);
                    pu.save();

                    Toast.makeText(ParkingMarkerActivity.this, "Spot Released",Toast.LENGTH_SHORT).show();

                    Date currentDate = new Date();
                    long timeInMilli = currentDate.getTime() - user.getReservationDate().getTime();
                    int hours =  (int) timeInMilli / AppConstants.HOURS_IN_MILLI;
                    int minutes =  (int) timeInMilli / AppConstants.MINUTES_IN_MILLI;

                    double cost = (hours  + minutes/AppConstants.SIXTY) * spot.getPrice();
                    Log.i("cost", cost + "");

                    Intent paymentActivity = new Intent(ParkingMarkerActivity.this, PaymentActivity.class);
                    paymentActivity.putExtra(AppConstants.USER, user);
                    paymentActivity.putExtra(AppConstants.AMOUNT, cost);
                    //startActivity(paymentActivity);

                    //Intent intent = new Intent(ParkingMarkerActivity.this, MapsActivity.class);
                    //intent.putExtra(AppConstants.USER, user);
                    user.setParkingSpot(null);
                    user.setHasParking(false);
                    user.setReservationDate(null);
                    user.setParkingID("");
                    startActivity(paymentActivity);
                    //startActivity(intent);
                    finish();
                }catch(ParseException e){
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
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

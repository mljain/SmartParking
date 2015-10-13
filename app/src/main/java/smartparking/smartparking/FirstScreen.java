package smartparking.smartparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;


public class FirstScreen extends Activity {
    double latitude, longitude;
    String longiStr, latiStr;
    private Button findParking,releaseParking,saveParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "klpklpmZUYYtPoxDNP9TdEAbllMW1jvdAl922KDPH02", "JUJUtb9s5hA6ZxaITMpUGEYwP7Bu4ifW2GGMhK9m0f");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        findParking = (Button)(findViewById(R.id.findParking));
        releaseParking = (Button)(findViewById(R.id.releaseParking));
        saveParking = (Button)(findViewById(R.id.releaseParking));
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
        findParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findParkingActivity = new Intent(FirstScreen.this, MapsActivity.class);
                startActivity(findParkingActivity);
            }
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        // Get latitude of the current location
         latitude = myLocation.getLatitude();
        latiStr = String.valueOf(latitude);

        // Get longitude of the current location
        longitude = myLocation.getLongitude();
         longiStr = String.valueOf(longitude);
//          ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("andrey", "gal");
//        testObject.saveInBackground();
        releaseParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject testObject = new ParseObject("AvailableParking");
                testObject.put("Longitude", longiStr);
                testObject.put("Latitude", latiStr);
                testObject.saveInBackground();
                Toast.makeText(getApplicationContext(), latitude+"", Toast.LENGTH_LONG).show();
//                Intent releaseParkingActivity = new Intent(FirstScreen.this, ReleaseParking.class);
//                startActivity(releaseParkingActivity);
            }
        });
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();


//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put(latitude +"", longitude+"");
//        testObject.saveInBackground();


//        saveParking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseObject testObject = new ParseObject("TestObject");
//                testObject.put(latitude +"", longitude+"");
//                testObject.saveInBackground();
//                Toast.makeText(getApplicationContext(), "Parking freed", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

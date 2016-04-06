package smartparking.smartparking;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import smartparking.smartparking.model.ParkingSpot;
import smartparking.smartparking.model.User;
import smartparking.smartparking.util.AppConstants;
import smartparking.smartparking.util.ImageDownloader;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Dialog parkingDialog;
    private ImageDownloader imagedl;
    private HashMap<String, ParkingSpot> parkingList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        user = (User) getIntent().getSerializableExtra(AppConstants.USER);

        ActionBar actionBar = getActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        zoomOnMyLocation();
        parkingList = new HashMap<>();
        imagedl = new ImageDownloader();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ParkingSpot spot = new ParkingSpot();
            /*    Intent releaseParkingActivity = new Intent(MapsActivity.this, ParkingMarkerActivity.class);
                releaseParkingActivity.putExtra("position",marker.getPosition());
                releaseParkingActivity.putExtra("title",marker.getTitle());
                releaseParkingActivity.putExtra("id",marker.getId());
                releaseParkingActivity.putExtra("snipped",marker.getSnippet());
                startActivity(releaseParkingActivity);



                spot.setLatitude(marker.getPosition().latitude);
                spot.setLongitude(marker.getPosition().longitude);
                spot.setName("San Jose Parking Garage");
                spot.setImageUrl("http://www.co.berks.pa.us/Dept/Courts/Jury/PublishingImages/7th_Penn_Parking_Lot.jpg");
                spot.setQuantity(14);
                spot.setPrice(14.00);
                */

                spot = parkingList.get(marker.getTitle());
                showDialog(MapsActivity.this, spot);
                return false;
            }
        });
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AvailableParking");
        List<ParseObject> ob = null;
        try {
            ob = query.find();
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        if (ob!=null && ob.size() > 0) {
            // IS INSTALLED ON DEVICE..!!!

            ParkingSpot sp;
            for (ParseObject mediObject : ob) {
                String Longitude = mediObject.get("Longitude").toString();
                String Latitude = mediObject.get("Latitude").toString();
                double longi = Double.parseDouble(Longitude);
                double lati = Double.parseDouble(Latitude);
                sp = new ParkingSpot();
                sp.setName(mediObject.get("Name").toString());
                sp.setLatitude(lati);
                sp.setLongitude(longi);
                sp.setQuantity(Integer.parseInt(mediObject.get("quantity").toString()));
                sp.setPriceDesc(mediObject.get("Cost").toString());
                sp.setImageUrl(mediObject.get("garageImage").toString());

                parkingList.put(sp.getName(), sp);





                if (!mediObject.get("status").toString().equals("free")) {
                    IconGenerator factory = new IconGenerator(this);
                    factory.setStyle(IconGenerator.STYLE_RED);
                    factory.setBackground(getResources().getDrawable(R.drawable.parkme1));
                    Bitmap icon =factory.makeIcon(mediObject.get("Cost").toString());
                    mMap.addMarker(new MarkerOptions().draggable(true)
                            .position(new LatLng(lati, longi))
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
                            .title(sp.getName()));
                } else {
                    IconGenerator factory = new IconGenerator(this);
                    factory.setStyle(IconGenerator.STYLE_GREEN);
                    Bitmap icon =factory.makeIcon(mediObject.get("Cost").toString());
                    mMap.addMarker(new MarkerOptions().draggable(true)
                            .position(new LatLng(lati, longi))
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
                            .title(sp.getName()));
                }
            }
        }

    }

    public void showDialog(Context context, final ParkingSpot spot){
        parkingDialog = new Dialog(context);
        TextView parkingName, parkingQuantity, parkingPrice;
        ImageView parkingImage;
        Button parkingButton;

        //set layout view
        parkingDialog.setContentView(R.layout.parking_spot_view);
        //parkingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Initialize your widgets from layout here
        parkingName = (TextView) parkingDialog.findViewById(R.id.parking_name);
        parkingQuantity = (TextView) parkingDialog.findViewById(R.id.parking_quantity);
        parkingPrice = (TextView) parkingDialog.findViewById(R.id.parking_price);
        parkingImage = (ImageView) parkingDialog.findViewById(R.id.parking_image);
        parkingButton = (Button) parkingDialog.findViewById(R.id.book_button);

        //set variables
        parkingDialog.setTitle(spot.getName());
        parkingName.setText(spot.getName());
        parkingQuantity.setText("Spots Available: " + spot.getQuantity());
        parkingPrice.setText("Price: " + spot.getPriceDesc());

        if(spot.getImageUrl() != null && !spot.getImageUrl().equalsIgnoreCase(""))
            imagedl.download(spot.getImageUrl(), parkingImage);

        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Spot Booked",Toast.LENGTH_SHORT).show();
                spot.setBooked();
                user.setParkingSpot(spot);
                Intent intent = new Intent(MapsActivity.this, ParkingMarkerActivity.class);
                intent.putExtra(AppConstants.USER, user);
                intent.putExtra(AppConstants.SPOT, spot);
                startActivity(intent);
            }
        });

        parkingDialog.show();
    }

    private void zoomOnMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        double latitude = 37.391508, longitude = -121.980040;
        if (myLocation != null) {
            // Get latitude of the current location
            latitude = myLocation.getLatitude();
            // Get longitude of the current location
            longitude = myLocation.getLongitude();
            myLocation = new Location(provider);
            myLocation.setLatitude(latitude);
            myLocation.setLongitude(longitude);
        }

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        double latitude = 37.391508, longitude = -121.980040;
        if (myLocation != null) {
            // Get latitude of the current location
            latitude = myLocation.getLatitude();
            // Get longitude of the current location
            longitude = myLocation.getLongitude();
        }

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        LatLng myCoordinates = new LatLng(latitude, longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 12);
        mMap.animateCamera(yourLocation);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      //  for (int i = 0; i < yourArrayList.size(); i++) {

       //     double lati=Double.parseDouble(pins.get(i).latitude);
        //    double longLat=Double.parseDouble(pins.get(i).longitude);

     //   }
    }

    /*
    private void addMarkerOnCurrentLocation(){
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

    }*/
}

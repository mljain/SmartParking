package smartparking.smartparking;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class FirstScreen extends Activity {
    double latitude, longitude;
    String longiStr, latiStr;
    private Button findParking,releaseParking,saveParking;
    private ImageButton fpimgButton;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "", "");
       // ParseInstallation.getCurrentInstallation().saveInBackground();
        setContentView(R.layout.activity_first_screen);
      //  findParking = (Button)(findViewById(R.id.findParking));
        releaseParking=(Button)(findViewById(R.id.releaseParking));
     /*   findParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findParkingActivity = new Intent(FirstScreen.this, MapsActivity.class);
                startActivity(findParkingActivity);
            }
        });*/

        fpimgButton = (ImageButton) (findViewById(R.id.findParkingImageButton));
        fpimgButton.setOnClickListener(new View.OnClickListener() {
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
        if (myLocation != null) {
            latitude = myLocation.getLatitude();
            latiStr = String.valueOf(latitude);

            // Get longitude of the current location
            longitude = myLocation.getLongitude();
            longiStr = String.valueOf(longitude);
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
           Log.i("adapter", "Read an NFC tag");
        } else {
            Log.i("adapter", "This phone is not NFC enabled.");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        Intent first_intent = getIntent();
        if(first_intent != null && first_intent.getAction().equals("android.nfc.action.NDEF_DISCOVERED"))
            onNewIntent(first_intent);

        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // menu.setShadowWidthRes(R.dimen.shadow_width);
        // menu.setShadowDrawable(R.drawable.shadow);
        // menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
       // menu.setMenu(R.layout.);

    }

    public void learnParking(View v) {
        Intent learnParkingActivity = new Intent(FirstScreen.this, LearnParkingActivity.class);
        startActivity(learnParkingActivity);
    }
    @Override
    public void onNewIntent(Intent intent) {
        String nfc_data="";
        if(intent.getAction().equals("android.nfc.action.NDEF_DISCOVERED")){
            // parse through all NDEF messages and their records and pick text type only
            Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (data != null) {
                try {
                    for (int i = 0; i < data.length; i++) {
                        NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                        for (int j = 0; j < recs.length; j++) {
                            if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                    Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                                byte[] payload = recs[j].getPayload();
                                String textEncoding ;
                                if ((payload[0] & 0200) == 0)
                                    textEncoding = "UTF-8" ;
                                else
                                    textEncoding = "UTF-16";
                                int langCodeLen = payload[0] & 0077;

                                nfc_data = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                        textEncoding);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("TagDispatch", e.toString());
                }
            }

            Log.i("data",nfc_data);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AvailableParking");
            List<ParseObject> ob = null;
            ParseObject po = null;
            try {
                ob = query.find();
                Iterator iter = ob.iterator();
                while(iter.hasNext()){
                    po=  (ParseObject) iter.next();
                    // Log.i(" po.getObjectId()", po.getObjectId());
                    //Log.i(" po.nfc_data()", nfc_data);

                    Log.i(" po", po.getObjectId());
                    if( po.getObjectId().equalsIgnoreCase(nfc_data)){
                        Intent releaseParkingActivity = new Intent(FirstScreen.this, ParkingMarkerActivity.class);
                        releaseParkingActivity.putExtra("id", nfc_data);
                        releaseParkingActivity.putExtra("position", new LatLng(latitude, longitude));
                        releaseParkingActivity.putExtra("title",nfc_data);
                        startActivity(releaseParkingActivity);
                        break;
                    }
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
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

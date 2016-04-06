package smartparking.smartparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import smartparking.smartparking.model.ParkingSpot;
import smartparking.smartparking.model.User;
import smartparking.smartparking.util.AppConstants;

/**
 * Activity which starts an intent for either the logged in (MainActivity) or logged out
 * (SignUpOrLoginActivity) activity.
 */
public class DispatchActivity extends Activity {
  private User user;

  public DispatchActivity() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    // Check if there is current user info
    Parse.initialize(this, "", "");
    if (ParseUser.getCurrentUser() != null) {
      // Start an intent for the logged in activity
      //startActivity(new Intent(this, MainActivity.class));

      ParseUser pu = ParseUser.getCurrentUser();
      if(pu != null){
        user = new User();
        user.setId(pu.getUsername());
        user.setHasParking((boolean) pu.get(AppConstants.HAS_PARKING));
        Log.i("uname", user.hasParking() + "");
      }

      Intent intent;
      if(user.hasParking()){
        intent = new Intent(this,ParkingMarkerActivity.class);
        user.setParkingID((String) pu.get(AppConstants.PARKING_ID));

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AvailableParking");
        query.whereEqualTo(AppConstants.NAME, user.getParkingID());
        try {
          ParseObject po = query.getFirst();

          if (po != null){
            ParkingSpot spot = new ParkingSpot();
            spot.setName(po.get("Name").toString());
            spot.setImageUrl(po.get("garageImage").toString());
            String p = po.get("Cost").toString();
            spot.setPriceDesc(p);
            String temp = p.split("/day")[0];
            spot.setPrice(Double.parseDouble(temp.substring(1)));
            spot.setQuantity(Integer.parseInt(po.get("quantity").toString()));
            user.setParkingSpot(spot);
          }
        }catch(ParseException e){
          Log.e("Error", e.getMessage());
          e.printStackTrace();
        }

      }else{

        intent = new Intent(this, FirstScreen.class);
      }

      intent.putExtra(AppConstants.USER, user);
      startActivity(intent);
    } else {
      // Start and intent for the logged out activity
      startActivity(new Intent(this, WelcomeActivity.class));
    }
  }

}

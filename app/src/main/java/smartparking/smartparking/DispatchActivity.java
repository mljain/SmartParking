package smartparking.smartparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Activity which starts an intent for either the logged in (MainActivity) or logged out
 * (SignUpOrLoginActivity) activity.
 */
public class DispatchActivity extends Activity {

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
      startActivity(new Intent(this, FirstScreen.class));
    } else {
      // Start and intent for the logged out activity
      startActivity(new Intent(this, WelcomeActivity.class));
    }
  }

}

package smartparking.smartparking;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Parnit on 10/16/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                "", "");
    }
}
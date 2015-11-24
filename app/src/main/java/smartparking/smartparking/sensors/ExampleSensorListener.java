package smartparking.smartparking.sensors;

import android.util.Log;

import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

/**
 * Created by souachar on 11/24/15.
 */
public class ExampleSensorListener implements SensorDataListener
{
    public void onDataSensed(SensorData data)
    {
        // This method will be called by the ES Sensor Manager when it has new data to publish
        // and lets you decide what actions to take with that data.
        //Log.d("Received Sensor Data:::",data.toString());

    }

    public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
    {
        // This method will be called by the ES Sensor Manager when the phone's battery falls
        // below a pre-defined, configurable threshold, and again when the phone has been charged
        // above that threshold.
    }
}
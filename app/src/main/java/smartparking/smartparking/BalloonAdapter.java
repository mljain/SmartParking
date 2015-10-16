package smartparking.smartparking;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class BalloonAdapter extends Activity implements  InfoWindowAdapter {
    LayoutInflater inflater = null;
    private TextView textViewTitle;
    private  View myContentsView=null;
    Button saveParking;
    Button releaseParking;

    public BalloonAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        //myContentsView =inflate(R.layout.activity_balloon_adapter, null);
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        myContentsView = inflater.inflate(R.layout.activity_balloon_adapter, null);
        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
        //tvTitle.setText(marker.getTitle());
        saveParking=(Button) myContentsView.findViewById(R.id.saveParkingSlot);
        releaseParking=(Button) myContentsView.findViewById(R.id.releaseParking);
        RatingBar rate_bar =  (RatingBar)myContentsView.findViewById(R.id.ratingBar);
        rate_bar.setNumStars(4);
        setContentView(myContentsView);
        saveParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveParkingActivity = new Intent(BalloonAdapter.this, ParkingMarkerActivity.class);
                saveParkingActivity.putExtra("position",marker.getPosition());
                saveParkingActivity.putExtra("title",marker.getTitle());
                saveParkingActivity.putExtra("id",marker.getId());
                saveParkingActivity.putExtra("snipped",marker.getSnippet());
                startActivity(saveParkingActivity);
            }

        });

        releaseParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
        return (myContentsView);
    }



    @Override
    public View getInfoContents(Marker marker) {
        return (null);
    }
}
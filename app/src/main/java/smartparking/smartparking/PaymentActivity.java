package smartparking.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import smartparking.smartparking.model.User;
import smartparking.smartparking.util.AppConstants;

public class PaymentActivity extends ActionBarActivity {

    final String TAG = getClass().getName();
    private Button scanButton, payButton;
    private TextView resultTextView;
    private EditText cardName, cardNumber, expiryDate, payment, CVVNumber;
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int
    private CreditCard scanResult;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getSerializableExtra(AppConstants.USER);

        setContentView(R.layout.activity_payment);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        scanButton = (Button) findViewById(R.id.scanButton);
        payButton = (Button) findViewById(R.id.pay);
        cardName = (EditText) findViewById(R.id.cardName);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        expiryDate = (EditText) findViewById(R.id.expiryDate);
        CVVNumber = (EditText) findViewById(R.id.cvv);
        payment = (EditText) findViewById(R.id.amount);

        resultTextView.setText("card.io library version: " + CardIOActivity.sdkVersion() + "\nBuilt: " + CardIOActivity.sdkBuildDate());

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultStr;
                if(scanResult != null) {
                    resultStr = "Payment Successfull. Thank you.";
                    resultTextView.setText(resultStr);
                    Toast.makeText(PaymentActivity.this, resultStr, Toast.LENGTH_LONG).show();
                    Intent findParkingActivity = new Intent(PaymentActivity.this, FirstScreen.class);
                    findParkingActivity.putExtra(AppConstants.USER, user);
                    startActivity(findParkingActivity);
                }else{
                    resultStr = "Please provide payment details.";
                    resultTextView.setText(resultStr);
                    Toast.makeText(PaymentActivity.this, resultStr, Toast.LENGTH_LONG).show();
                    resultTextView.setText(resultStr);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CardIOActivity.canReadCardWithCamera()) {
            scanButton.setText("Scan a credit card");
        } else {
            scanButton.setText("Enter credit card information");
        }
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

    public void startScan(View v) {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String resultStr;
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
            cardNumber.setText(scanResult.getRedactedCardNumber());

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                expiryDate.setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                CVVNumber.setText(scanResult.cvv);
            }

            if (scanResult.postalCode != null) {
                resultStr += "Postal Code: " + scanResult.postalCode + "\n";
            }

            if (scanResult.cardholderName != null) {
                resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
                cardName.setText(scanResult.cardholderName);
            }
        } else {
            resultStr = "Scan was canceled.";
        }
        resultTextView.setText(resultStr);
        Toast.makeText(this, resultStr, Toast.LENGTH_LONG).show();

    }
}

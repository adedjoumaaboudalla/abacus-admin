package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private static final String TAG = "SCANNER";
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.setAutoFocus(true);          // Set Autofaucus
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

        Intent result = new Intent();
        result.putExtra(AccueilActivity.CODEBAR, rawResult.getText());
        result.putExtra("Find", true);
        setResult(RESULT_OK, result);
        finish();
    }

}

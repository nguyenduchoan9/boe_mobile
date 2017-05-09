package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.repacked.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRCodeScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private final String TAG_SCANNER = "SCANNER_RESULT";
    public final String SCANNER_INPUT = "SCANNER_INPUT";
    public final String SCANNER_OUTPUT = "SCANNER_OUTPUT";

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFormats(getBarcodeFormat());
        setContentView(mScannerView);
    }

    private List<BarcodeFormat> getBarcodeFormat() {
        List<BarcodeFormat> barcodeFormatList = new ArrayList<>();
        barcodeFormatList.add(BarcodeFormat.QRCODE);
        barcodeFormatList.add(BarcodeFormat.CODABAR);
        barcodeFormatList.add(BarcodeFormat.UPCA);
        barcodeFormatList.add(BarcodeFormat.UPCE);
        barcodeFormatList.add(BarcodeFormat.EAN8);
        barcodeFormatList.add(BarcodeFormat.EAN13);
        barcodeFormatList.add(BarcodeFormat.ISBN13);
        barcodeFormatList.add(BarcodeFormat.ISBN10);
        barcodeFormatList.add(BarcodeFormat.I25);
        barcodeFormatList.add(BarcodeFormat.DATABAR);
        barcodeFormatList.add(BarcodeFormat.DATABAR_EXP);
        barcodeFormatList.add(BarcodeFormat.CODE39);
        barcodeFormatList.add(BarcodeFormat.PDF417);
        barcodeFormatList.add(BarcodeFormat.CODE93);
        barcodeFormatList.add(BarcodeFormat.CODE128);

        return barcodeFormatList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v(TAG_SCANNER, result.getContents()); // Prints scan results
        Log.v(TAG_SCANNER, result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
        returnResult(result.getContents(), result.getBarcodeFormat().getName());
    }

    public static Intent newInstace(Context context) {
        Intent i = new Intent(context, QRCodeScanActivity.class);
        return i;
    }

    private void returnResult(String content, String barcodeFormat) {
        Intent data = new Intent();
        data.putExtra(Constant.SCANNER_RESULT_CONTENT, content);
        data.putExtra(Constant.SCANNER_RESULT_QRCODE, barcodeFormat);

        setResult(RESULT_OK, data);
        finish();
    }


}

package com.frutosajniahperez.libreria.ui.libreria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class LectorCodigoBarras extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 1;
    ZBarScannerView zBarScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zBarScannerView = new ZBarScannerView(this);
        setContentView(zBarScannerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        zBarScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zBarScannerView.setResultHandler(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        } else {
            zBarScannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        Dialogo_busqueda_google.searchView.setQuery(rawResult.getContents(), true);
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                zBarScannerView.startCamera();
            } else {
                onBackPressed();
            }
        }
    }
}

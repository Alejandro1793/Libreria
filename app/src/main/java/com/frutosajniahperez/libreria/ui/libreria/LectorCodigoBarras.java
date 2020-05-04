package com.frutosajniahperez.libreria.ui.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.frutosajniahperez.libreria.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class LectorCodigoBarras extends AppCompatActivity implements ZBarScannerView.ResultHandler {

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
        zBarScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Dialogo_busqueda_google.searchView.setQuery(rawResult.getContents(), true);
        onBackPressed();
    }
}

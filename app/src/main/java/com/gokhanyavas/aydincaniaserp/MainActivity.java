package com.gokhanyavas.aydincaniaserp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private CustomWebViewClient webViewClient;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide(); // barı gizle


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Yükleniyor...");
        mProgressDialog.setMessage("Lütfen Bekleyiniz.");

        webViewClient = new CustomWebViewClient();
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true); //zoom yapılmasına izin verir
       // webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);

        // hızlandırma - başlangıç
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // hızlandırma - bitiş

        webView.setWebViewClient(webViewClient);
        webView.loadUrl(getString(R.string.web_link));


    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl){
           // webview.loadUrl("file:///android_asset/error.html");

        //    Intent errorIntent = new Intent(getApplicationContext(), ErrorPage.class);
        //    startActivity(errorIntent);

            try {
                webView.stopLoading();
            } catch (Exception e) {
            }
            try {
                webView.clearView();
            } catch (Exception e) {
            }
            if (webView.canGoBack()) {
                webView.goBack();
            }
            webView.loadUrl("file:///android_asset/error.html");
            super.onReceivedError(webView, errorCode, description, failingUrl);

        }


    }

    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            //Sayfa yoksa uygulamadan çık
            super.onBackPressed();
        }
    }
}

package com.example.reformfitapp.serviceInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.reformfitapp.R;

import io.perfmark.Link;

public class LinkWebview extends AppCompatActivity {

    private String url;
    private WebView webView;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);



        view = getLayoutInflater().inflate(R.layout.activity_link_webview, null);


        url = String.valueOf(getIntent().getSerializableExtra("Url"));


        webView = (WebView) view.findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
        setContentView(view);


    }
}
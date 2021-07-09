package com.example.reformfitapp.expandedFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.R;

public class YongjiuReportEx extends AppCompatActivity {

    WebView webView;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);



        view = getLayoutInflater().inflate(R.layout.activity_yongjiu_report_ex, null);


        String measurementId = (String) getIntent().getStringExtra("MeasurementId");


        String phoneNum;
                //TODO: get phone number
        /*if(((GlobalVariableApplication)getApplication()).getLogIn()){
            phoneNum = ((GlobalVariableApplication)getApplication()).getMindbodyClientResponseModel().getMobilePhone();
        *///}
        //else{

        phoneNum = "14379876631";
        //}


        YongjiuReportDetail yongjiuReportDetail = new YongjiuReportDetail(getApplicationContext(), phoneNum, measurementId);

        webView = (WebView) view.findViewById(R.id.webview);
        yongjiuReportDetail.reportRequest(webView, new YongjiuReportDetail.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setStandardFontFamily((String) "Time New Roman");
                webView.loadUrl(response);
                setContentView(view);

            }
        });

    }
}
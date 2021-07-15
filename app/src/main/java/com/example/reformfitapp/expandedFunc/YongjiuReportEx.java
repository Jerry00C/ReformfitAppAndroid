package com.example.reformfitapp.expandedFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.R;

public class YongjiuReportEx extends AppCompatActivity {

    WebView webView;
    View view;


    ImageView initBack;
    ImageView initHome;

    String measurementId;
    String phoneNum;

    TextView initLangChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.progress_bar);



        view = getLayoutInflater().inflate(R.layout.activity_yongjiu_report_ex, null);



        initBack = view.findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YongjiuReportEx.this.finish();
            }
        });

        initHome = view.findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((GlobalVariableApplication) getApplication()).setHome(true);

                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(switchActivityIntent);


            }
        });


        initLangChange = view.findViewById(R.id.init_langChange);
        initLangChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initLangChange.getText().equals(getResources().getString(R.string.yjEx_init_text1))){
                    initLangChange.setText(getResources().getString(R.string.yjEx_init_text2));
                    refresh(false);

                }
                else{
                    initLangChange.setText(getResources().getString(R.string.yjEx_init_text1));
                    refresh(true);

                }
            }
        });

        measurementId = (String) getIntent().getStringExtra("MeasurementId");


                //TODO: get phone number
        /*if(((GlobalVariableApplication)getApplication()).getLogIn()){
            phoneNum = ((GlobalVariableApplication)getApplication()).getMindbodyClientResponseModel().getMobilePhone();
        *///}
        //else{

        phoneNum = "14379876631";
        //}


        YongjiuReportDetail yongjiuReportDetail = new YongjiuReportDetail(getApplicationContext(), phoneNum, measurementId);

        webView = (WebView) view.findViewById(R.id.webview);
        yongjiuReportDetail.reportRequest(new YongjiuReportDetail.VolleyResponseListener() {
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
        }, true);

    }


    private void refresh(boolean english){

        YongjiuReportDetail yongjiuReportDetail = new YongjiuReportDetail(getApplicationContext(), phoneNum, measurementId);

        webView = (WebView) view.findViewById(R.id.webview);
        yongjiuReportDetail.reportRequest(new YongjiuReportDetail.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {


                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setStandardFontFamily((String) "Time New Roman");

                Log.d("url", response);
                webView.loadUrl(response);
                setContentView(view);

            }
        }, english);





    }
}
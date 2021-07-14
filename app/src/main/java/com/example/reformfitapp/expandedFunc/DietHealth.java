package com.example.reformfitapp.expandedFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.reformfitapp.ClassInfo;
import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.R;

public class DietHealth extends AppCompatActivity {



    WebView webView;
    View view;


    ImageView initBack;
    ImageView initHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.progress_bar);

        view = getLayoutInflater().inflate(R.layout.activity_diet_health, null);



        initBack = view.findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DietHealth.this.finish();
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



        webView = (WebView) view.findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://eats.reformfit.ca");
        setContentView(view);

    }
}
package com.example.reformfitapp.expandedFunc.blogNews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.R;

public class BlogEx2 extends AppCompatActivity {


    ScrollView scrollView;
    ConstraintLayout constraintLayout;

    TextView thresh;

    ImageView initBack;
    ImageView initHome;



    TextView initBack2;
    TextView initTop;

    TextView blgText1;
    TextView blgText2;
    TextView blgText3;
    TextView blgText4;
    TextView blgText5;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_blog_ex2);


        initBack = findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogEx2.this.finish();
            }
        });

        initHome = findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((GlobalVariableApplication) getApplication()).setHome(true);
                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(switchActivityIntent);


            }
        });


        scrollView = (ScrollView) findViewById(R.id.scrollView);
        thresh = findViewById(R.id.thresh);
        constraintLayout = findViewById(R.id.constraintLayout);


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY >= thresh.getTop() - 85){
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
                }
                else{
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
            }
        });


        initBack2 = findViewById(R.id.init_back2);
        initBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogEx2.this.finish();
            }
        });


        initTop = findViewById(R.id.init_top);
        initTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                scrollView.post(new Runnable() {
                    public void run() {


                        scrollView.smoothScrollTo(0, 0);
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                });

            }
        });


        blgText1 = findViewById(R.id.blg_text1);
        blgText2 = findViewById(R.id.blg_text2);
        blgText3 = findViewById(R.id.blg_text3);
        blgText4 = findViewById(R.id.blg_text4);
        blgText5 = findViewById(R.id.blg_text5);

        blgText1.setText(Html.fromHtml(getString(R.string.blg2_text1)));
        blgText2.setText(Html.fromHtml(getString(R.string.blg2_text2)));
        blgText3.setText(Html.fromHtml(getString(R.string.blg2_text3)));
        //blgText4.setText(Html.fromHtml(getString(R.string.blg2_text5)));
        blgText5.setText(Html.fromHtml(getString(R.string.blg2_text6)));









    }
}
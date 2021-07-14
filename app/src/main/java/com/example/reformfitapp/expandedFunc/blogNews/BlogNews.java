package com.example.reformfitapp.expandedFunc.blogNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.R;

public class BlogNews extends AppCompatActivity {


    ImageView initBack;
    ImageView initHome;

    ConstraintLayout initBlog1;
    ConstraintLayout initBlog2;
    ConstraintLayout initBlog3;
    ConstraintLayout initBlog4;
    ConstraintLayout initBlog5;
    ConstraintLayout initBlog6;
    ConstraintLayout initBlog7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_blog_news);


        initBack = findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogNews.this.finish();
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

        initBlog1 = findViewById(R.id.init_blog1);
        initBlog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx1.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });


        initBlog2 = findViewById(R.id.init_blog2);
        initBlog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx2.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });

        initBlog3 = findViewById(R.id.init_blog3);
        initBlog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx3.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });


        initBlog4 = findViewById(R.id.init_blog4);
        initBlog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx4.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });


        initBlog5 = findViewById(R.id.init_blog5);
        initBlog5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx5.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });


        initBlog6 = findViewById(R.id.init_blog6);
        initBlog6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx6.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });


        initBlog7 = findViewById(R.id.init_blog7);
        initBlog7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), BlogEx7.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });




    }
}
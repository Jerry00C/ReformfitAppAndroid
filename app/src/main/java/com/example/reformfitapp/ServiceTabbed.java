package com.example.reformfitapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.reformfitapp.databinding.ActivityServiceTabbedBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.ui.main.SectionsPagerAdapter;
import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowErrorType;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ServiceTabbed extends AppCompatActivity {

    private ActivityServiceTabbedBinding binding;

    ViewPager viewPager;
    TabLayout tabs;

    View view1;
    FrameLayout frameLayout;

    int startPagePos;


    ImageView initBack;
    ImageView initHome;

    TextView initPurchase;
    TextView initLiveChat;
    private ChatWindowView fullScreenChatWindow;
    private String licenceNumber = "12951837";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_progress_bar);

        view1 = getLayoutInflater().inflate(R.layout.activity_service_tabbed, null);

        initBack = view1.findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceTabbed.this.finish();
            }
        });

        initHome = view1.findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((GlobalVariableApplication) getApplication()).setHome(true);
                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(switchActivityIntent);


            }
        });

        frameLayout = findViewById(R.id.container);


        startPagePos = (int) getIntent().getSerializableExtra("startPagePos");


        viewPager = view1.findViewById(R.id.view_pager);
        tabs = view1.findViewById(R.id.tabs);



        initPurchase = view1.findViewById(R.id.init_purchase);
        initPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivity = new Intent(getApplicationContext(), TabbedActivityPurchase.class);
                switchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(switchActivity);
            }
        });


        initLiveChat = view1.findViewById(R.id.init_liveChat);
        initLiveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent switchActivity = new Intent(getApplicationContext(), LiveChat.class);
                switchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(switchActivity);*/

                startFullScreenChat();
            }
        });

        initialize();

    }


    private void initialize(){
        Calendar cal = Calendar.getInstance();

        String start_date = "";
        String end_date = "";


        int curr_month = cal.get(Calendar.MONTH) + 1;
        int curr_date = cal.get(Calendar.DAY_OF_MONTH);
        int curr_year = cal.get(Calendar.YEAR);

        String curr_month_final;
        String curr_date_final;
        if(curr_month < 10){
            curr_month_final = "0" + curr_month;
        }
        else{
            curr_month_final = String.valueOf(curr_month);
        }

        if(curr_date < 10){
            curr_date_final = "0" + curr_date;
        }
        else{
            curr_date_final = String.valueOf(curr_date);
        }



        start_date = curr_year + "-" + curr_month_final + "-" + curr_date_final;

        cal.add(Calendar.DAY_OF_MONTH, 13);
        curr_month = cal.get(Calendar.MONTH) + 1;
        curr_date = cal.get(Calendar.DAY_OF_MONTH);
        curr_year = cal.get(Calendar.YEAR);

        if(curr_month < 10){
            curr_month_final = "0" + curr_month;
        }
        else{
            curr_month_final = String.valueOf(curr_month);
        }

        if(curr_date < 10){
            curr_date_final = "0" + curr_date;
        }
        else{
            curr_date_final = String.valueOf(curr_date);
        }

        end_date = curr_year + "-" + curr_month_final + "-" + curr_date_final;

        System.out.println("start_date: " + start_date);
        System.out.println("end_date: " + end_date);


        MindbodyClass mindbodyClass = new MindbodyClass(ServiceTabbed.this);

        String finalStart_date = start_date;
        String finalEnd_date = end_date;
        mindbodyClass.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(ServiceTabbed.this, message, Toast.LENGTH_SHORT).show();
                Toast.makeText(ServiceTabbed.this, "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                ServiceTabbed.this.finish();

            }

            @Override
            public void onResponse(String response) {
                mindbodyClass.getClassInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(ServiceTabbed.this, message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ServiceTabbed.this, "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                        ServiceTabbed.this.finish();

                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ServiceTabbed.this, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        ArrayList<ArrayList<MindbodyClassModel>> models = mindbodyClass.getMindbodyClassModelArray().getModelAll();
                        ArrayList<ArrayList<MindbodyClassModel>> modelsOnline = mindbodyClass.getMindbodyClassModelArray().getModelOnlineAll();




                        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(ServiceTabbed.this, getSupportFragmentManager(), models, modelsOnline, false);
                        viewPager.setAdapter(sectionsPagerAdapter);
                        viewPager.setCurrentItem(startPagePos);
                        tabs.setupWithViewPager(viewPager);

                        frameLayout.removeAllViews();
                        frameLayout.addView(view1);

                    }
                }, finalStart_date, finalEnd_date, -1);
            }
        });
    }

    public void startFullScreenChat() {
        String visitorName = "";
        String visitorEmail = "";
        if(((GlobalVariableApplication) getApplication()).getLogIn()){

            MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();
            visitorName = mindbodyClientResponseModel.getFirstName();
            visitorEmail = mindbodyClientResponseModel.getEmail();
        }

        HashMap<String, String> customParamsMap = null;
        ChatWindowConfiguration configuration = new ChatWindowConfiguration(
                licenceNumber,
                "",
                visitorName,
                visitorEmail,
                customParamsMap
        );

        if (fullScreenChatWindow == null) {
            fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(ServiceTabbed.this);
            fullScreenChatWindow.setUpWindow(configuration);
            fullScreenChatWindow.onBackPressed();
            fullScreenChatWindow.setUpListener(new ChatWindowView.ChatWindowEventsListener() {
                @Override
                public void onChatWindowVisibilityChanged(boolean visible) {

                }

                @Override
                public void onNewMessage(NewMessageModel message, boolean windowVisible) {

                }

                @Override
                public void onStartFilePickerActivity(Intent intent, int requestCode) {

                }

                @Override
                public boolean onError(ChatWindowErrorType errorType, int errorCode, String errorDescription) {
                    return false;
                }

                @Override
                public boolean handleUri(Uri uri) {
                    return false;
                }
            });
            fullScreenChatWindow.initialize();
        }
        fullScreenChatWindow.showChatWindow();
    }
    @Override
    public void onBackPressed() {

        if(fullScreenChatWindow != null && fullScreenChatWindow.onBackPressed()){

        }
        else{
            super.onBackPressed();
        }
    }

}
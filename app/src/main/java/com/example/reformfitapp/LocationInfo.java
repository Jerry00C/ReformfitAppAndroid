package com.example.reformfitapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowErrorType;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;

import java.util.Calendar;
import java.util.HashMap;

public class LocationInfo extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;

    Integer[] imageId = {R.mipmap.location1,R.mipmap.location1,R.mipmap.location1,R.mipmap.location1};
    private ImageView[] dots = new ImageView[imageId.length];
    private int custom_position = 0;

    TextView description;

    TextView phoneNum;

    TextView password_textView;
    TextView init_copy_password;

    TextView address;

    TextView init_map;

    ScrollView scrollView;
    ConstraintLayout constraintLayout;

    TextView info_tab1;
    TextView warning_tab1;

    TextView info_tab;
    TextView warning_tab;

    CardView info_thresh;
    CardView warning_thresh;

    Button group_service;
    Button private_service;
    Button online_training;


    MindbodyLocationModel mindbodyLocationModel;

    View view;
    FrameLayout frameLayout;

    ImageView initHome;
    ImageView initBack;

    TextView initPurchase;
    TextView initLiveChat;
    private ChatWindowView fullScreenChatWindow;
    private String licenceNumber = "12951837";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_progress_bar);



        frameLayout = findViewById(R.id.container);

        view = getLayoutInflater().inflate(R.layout.activity_location_info, null);

        initBack = view.findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationInfo.this.finish();
            }
        });

        initHome = view.findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((GlobalVariableApplication) getApplication()).setHome(true);


                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(switchActivityIntent);


            }
        });







        //image gallery construction
        sliderDotspanel = view.findViewById(R.id.SliderDots);

        viewPager = view.findViewById(R.id.viewpager);





        //location description
        description = view.findViewById(R.id.des);
        //Toast.makeText(getApplicationContext(), mindbodyLocationModel.getDescription(), Toast.LENGTH_SHORT).show();


        //password copy
        init_copy_password = view.findViewById(R.id.copy_password);
        password_textView = view.findViewById(R.id.wifi_password);




        phoneNum = view.findViewById(R.id.phoneNum);

        address = view.findViewById(R.id.address_text);


        init_map = view.findViewById(R.id.init_map);




        scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        constraintLayout = view.findViewById(R.id.constraintLayout);



        info_tab = view.findViewById(R.id.info_tab);
        warning_tab = view.findViewById(R.id.warning_tab);

        info_tab1 = view.findViewById(R.id.info_tab1);
        warning_tab1 = view.findViewById(R.id.warning_tab1);

        info_thresh = view.findViewById(R.id.service);
        warning_thresh = view.findViewById(R.id.step);






        group_service = view.findViewById(R.id.group_service);


        private_service = view.findViewById(R.id.private_service);

        online_training = view.findViewById(R.id.online_servie);


        initPurchase = view.findViewById(R.id.init_purchase);
        initLiveChat = view.findViewById(R.id.init_liveChat);


        fetchInfo();

    }

    //TODO: clear Toast message and Log.d
    private void fetchInfo(){
        MindbodyLocation mindbodyLocation = new MindbodyLocation(getApplicationContext());
        mindbodyLocation.getUserToken(new MindbodyLocation.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "something wrong try again later", Toast.LENGTH_SHORT).show();
                LocationInfo.this.finish();
            }

            @Override
            public void onResponse(String response) {
                mindbodyLocation.getLocationInfo(new MindbodyLocation.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "something wrong try again later", Toast.LENGTH_SHORT).show();
                        LocationInfo.this.finish();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);

                        mindbodyLocationModel = mindbodyLocation.getMindbodyLocationModel();

                        initialize();
                    }
                });
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initialize(){
              //swipeRefreshLayout.canChildScrollUp();


        PagerAdapter adapter = new CustomAdapter(this,imageId);
        viewPager.setAdapter(adapter);
        prepareDots(custom_position++);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(custom_position > imageId.length-1) custom_position = 0;
                prepareDots(custom_position++);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        description.setText(mindbodyLocationModel.getDescription());

        init_copy_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String password = (String) password_textView.getText();
                ClipData clip = ClipData.newPlainText("simple text", password);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Password has been copied", Toast.LENGTH_SHORT).show();
            }
        });

        init_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Map point based on address
                //Uri location = Uri.parse("geo:0,0?q=/ReformFit+fitness");
                // Or map point based on latitude/longitude
                //Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                Uri location = Uri.parse("geo:" + mindbodyLocationModel.getLat() + "," + mindbodyLocationModel.getLon() + "?z=14"); // z param is zoom level
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

                String title = getResources().getString(R.string.chooser_title);
                // Create intent to show chooser
                Intent chooser = Intent.createChooser(mapIntent, title);


                // Try to invoke the intent.
                if(chooser.resolveActivity(getPackageManager()) != null) startActivity(chooser);
            }
        });



        info_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    public void run() {


                        scrollView.smoothScrollTo(0, sliderDotspanel.getBottom());
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));

                        info_tab.setClickable(false);
                        warning_tab.setClickable(false);
                        info_tab1.setVisibility(View.VISIBLE);
                        warning_tab1.setVisibility(View.VISIBLE);

                        info_tab1.setClickable(true);
                        warning_tab1.setClickable(true);
                        info_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+136);

                                    }

                                });
                            }
                        });

                        warning_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+136);

                                    }
                                });
                            }
                        });

                    }

                });
            }
        });

        warning_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+136);

                        constraintLayout.setBackgroundColor(getColor(R.color.black));
                        info_tab.setClickable(false);
                        warning_tab.setClickable(false);
                        info_tab1.setVisibility(View.VISIBLE);
                        warning_tab1.setVisibility(View.VISIBLE);

                        info_tab1.setClickable(true);
                        warning_tab1.setClickable(true);
                        info_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+136);

                                    }

                                });
                            }
                        });

                        warning_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+136);

                                    }
                                });
                            }
                        });

                    }
                });
            }
        });





        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //above warning thresh

                if(scrollY >= sliderDotspanel.getBottom()){

                    constraintLayout.setBackgroundColor(getColor(R.color.black));

                    info_tab.setClickable(false);
                    warning_tab.setClickable(false);
                    info_tab1.setVisibility(View.VISIBLE);
                    warning_tab1.setVisibility(View.VISIBLE);

                    info_tab1.setClickable(true);
                    warning_tab1.setClickable(true);
                    info_tab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+136);

                                }

                            });
                        }
                    });

                    warning_tab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+136);

                                }
                            });
                        }
                    });


                }
                else{

                    constraintLayout.setBackgroundColor(getColor(R.color.transparent));
                    info_tab1.setVisibility(View.INVISIBLE);
                    warning_tab1.setVisibility(View.INVISIBLE);

                    info_tab.setClickable(true);
                    warning_tab.setClickable(true);

                    info_tab1.setClickable(false);
                    warning_tab1.setClickable(false);
                }
                if(scrollY >= warning_thresh.getTop()+constraintLayout.getBottom()+136){
                    info_tab1.setTextColor(getResources().getColor(R.color.grey));
                    warning_tab1.setTextColor(getResources().getColor(R.color.white));
                }
                else{
                    info_tab1.setTextColor(getResources().getColor(R.color.white));
                    warning_tab1.setTextColor(getResources().getColor(R.color.grey));

                }

                //below warning thresh
            }
        });



        group_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceTabCall(0);
            }
        });


        private_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceTabCall(1);

            }
        });

        online_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceTabCall(2);
            }
        });



        phoneNum.setText(mindbodyLocationModel.getPhone());

        address.setText(mindbodyLocationModel.getAddress());



        initPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivity = new Intent(getApplicationContext(), TabbedActivityPurchase.class);
                switchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(switchActivity);
            }
        });

        initLiveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent switchActivity = new Intent(getApplicationContext(), LiveChat.class);
                switchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(switchActivity);*/

                startFullScreenChat();
            }
        });


        frameLayout.removeAllViews();
        frameLayout.addView(view);





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
            fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(LocationInfo.this);
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


    private void serviceTabCall(int pos){



        Intent switchActivityIntent = new Intent(getApplicationContext(), ServiceTabbed.class);

        switchActivityIntent.putExtra("startPagePos", pos);

        switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(switchActivityIntent);

    }

    private void prepareDots(int currPosition){

        if(sliderDotspanel.getChildCount() > 0)
            sliderDotspanel.removeAllViews();


        for(int i = 0; i < imageId.length; i++){

            dots[i] = new ImageView(this);
            if(i == currPosition){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

    }


}
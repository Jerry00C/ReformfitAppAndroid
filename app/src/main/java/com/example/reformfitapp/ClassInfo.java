package com.example.reformfitapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.reformfitapp.main.GroupClassPlaceholderFragment;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowErrorType;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClassInfo extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;

    Integer[] imageId = {R.mipmap.location1,R.mipmap.location1,R.mipmap.location1,R.mipmap.location1};
    private ImageView[] dots = new ImageView[imageId.length];
    private int custom_position = 0;

    TextView classDes;
    ImageView classDesImage;

    TextView classEffectDes;
    ImageView classEffectDesImage;

    TextView faqDes;
    ImageView faqDesImage;

    TextView init_map;

    ScrollView scrollView;
    ConstraintLayout constraintLayout;

    TextView info_tab1;
    TextView warning_tab1;


    TextView info_tab;
    TextView warning_tab;

    CardView info_thresh;
    CardView warning_thresh;

    TextView init_add_client;


    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;

    ImageView initBack;
    ImageView initHome;



    TextView className;
    TextView staffName;
    ImageView staffImage;
    TextView classTime;
    TextView classAddress;
    TextView classDescription;


    int maxCapacity;
    int totalBooked;
    int totalWaitlist;
    boolean isWaitlistAvailable;
    boolean isAavilable;
    boolean isCanceled;
    boolean isOver;


    boolean isLateCancel;
    MindbodyClassModel classEx;
    String classId;


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


        setContentView(R.layout.activity_class_info);


        initBack = findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassInfo.this.finish();
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





        classId = String.valueOf(getIntent().getSerializableExtra("ClassId"));
        classEx = (MindbodyClassModel) getIntent().getParcelableExtra("MindbodyClassModel");

        Log.d("class info", classEx.toString());
        sliderDotspanel = findViewById(R.id.SliderDots);

        viewPager = findViewById(R.id.viewpager);


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




        scrollView = (ScrollView) findViewById(R.id.scrollview);
        constraintLayout = findViewById(R.id.constraintLayout);



        info_tab = findViewById(R.id.info_tab);
        warning_tab = findViewById(R.id.warning_tab);

        info_tab1 = findViewById(R.id.info_tab1);
        warning_tab1 = findViewById(R.id.warning_tab1);

        info_thresh = findViewById(R.id.service);
        warning_thresh = findViewById(R.id.step);

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


        className = findViewById(R.id.class_name);
        className.setText(classEx.getClassName());

        staffImage = findViewById(R.id.staff_image);
        String staff_imageUrl = classEx.getStaff_mageUrl();

        if(staff_imageUrl != null){
            Glide.with(ClassInfo.this).load(staff_imageUrl).into(staffImage);
        }


        staffName = findViewById(R.id.staff_name);
        staffName.setText(classEx.getStaff_name());



        classTime = findViewById(R.id.class_time);
        classTime.setText(classEx.getStartDateCut() + " " + classEx.getStartTimeCut() + "-" + classEx.getEndTimeCut());

        classAddress = findViewById(R.id.class_address);
        classAddress.setText(classEx.getAddress());







        init_map = findViewById(R.id.init_map);
        init_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Map point based on address
                Uri location = Uri.parse("geo:0,0?q=/ReformFit+fitness");
                // Or map point based on latitude/longitude
                //Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                //Uri location = Uri.parse("geo:" + mindbodyLocationModel.getLat() + "," + mindbodyLocationModel.getLon() + "?z=14"); // z param is zoom level
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

                String title = getResources().getString(R.string.chooser_title);
                // Create intent to show chooser
                Intent chooser = Intent.createChooser(mapIntent, title);


                // Try to invoke the intent.
                if(chooser.resolveActivity(getPackageManager()) != null) startActivity(chooser);
            }
        });

        classDes = findViewById(R.id.class_des);
        classDes.setText(classEx.getDescription());

        classDesImage = findViewById(R.id.class_des_image);

        classDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classDes.getMaxLines() != 3){
                    classDes.setMaxLines(3);
                }
                else{
                    classDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        classDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classDes.getMaxLines() != 3){
                    classDes.setMaxLines(3);
                }
                else{
                    classDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });


        classEffectDes = findViewById(R.id.class_effect_text);
        classEffectDesImage = findViewById(R.id.class_effect_image);

        classEffectDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classEffectDes.getMaxLines() != 3){
                    classEffectDes.setMaxLines(3);
                }
                else{
                    classEffectDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        classEffectDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classEffectDes.getMaxLines() != 3){
                    classEffectDes.setMaxLines(3);
                }
                else{
                    classEffectDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });


        faqDes = findViewById(R.id.faq_des);
        faqDesImage = findViewById(R.id.faq_des_image);

        faqDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(faqDes.getMaxLines() != 3){
                    faqDes.setMaxLines(3);
                }
                else{
                    faqDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        faqDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(faqDes.getMaxLines() != 3){
                    faqDes.setMaxLines(3);
                }
                else{
                    faqDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });


        TextView warnDes;
        ImageView warnDesImage;


        warnDes = findViewById(R.id.warn_des);
        warnDesImage = findViewById(R.id.warn_des_image);

        warnDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warnDes.getMaxLines() != 3){
                    warnDes.setMaxLines(3);
                }
                else{
                    warnDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        warnDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warnDes.getMaxLines() != 3){
                    warnDes.setMaxLines(3);
                }
                else{
                    warnDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });



        init_add_client = findViewById(R.id.init_add_client);

        maxCapacity = classEx.getMaxCapacity();
        totalBooked = classEx.getTotalBooked();
        totalWaitlist = classEx.getTotalBookedWaitlist();
        isWaitlistAvailable = classEx.isWaitlistAvailable();
        isAavilable = classEx.isAvailable();
        isCanceled = classEx.isCancel();

        isOver = classEx.isOver();


        isLateCancel = checkLateCancel(classEx.getCancelOffset(), classEx.getStartTimestamp());



        initPurchase = findViewById(R.id.init_purchase);
        initLiveChat = findViewById(R.id.init_liveChat);

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



        initialize();
    }

    private void startFullScreenChat() {
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
            fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(ClassInfo.this);
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

    private boolean checkLateCancel(int cancelOffset, long startTimestamp){

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(today);
        calendar.add(Calendar.HOUR, cancelOffset);
        long compared = calendar.getTimeInMillis();


        if(compared > startTimestamp){
            return true;
        }
        else{
            return false;
        }
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


    private void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(ClassInfo.this);
        dialog.setContentView(R.layout.sign_in_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);


        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView createAccountClickable = dialog.findViewById(R.id.create_account_clickable);


        EditText email = dialog.findViewById(R.id.editTextTextEmailAddress2);
        EditText password = dialog.findViewById(R.id.editTextTextPassword2);

        popupTitle.setText(title);


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });


        createAccountClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCreateAccountDialog("新用户注册", dialog);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text;
                String password_text;


                email_text = email.getText().toString();
                password_text = password.getText().toString();



                firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.signInWithEmailAndPassword(email_text, password_text)
                        .addOnCompleteListener((Activity) ClassInfo.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "success");

                                    dialog.setContentView(R.layout.progress_bar);
                                    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    dialog.setCanceledOnTouchOutside(false);

                                    userID = firebaseAuth.getCurrentUser().getUid();
                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    DocumentReference docRef = firebaseFirestore.collection("clientId").document(userID);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    String clientId = (String) document.getData().get("ClientId");

                                                    MindbodyClient mindbodyClient = new MindbodyClient(getApplicationContext());

                                                    mindbodyClient.getUserToken(new MindbodyLocation.VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();


                                                            dialog.setCancelable(true);
                                                            dialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            mindbodyClient.getClientInfo(new MindbodyLocation.VolleyResponseListener() {
                                                                @Override
                                                                public void onError(String message) {
                                                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();


                                                                    dialog.setCancelable(true);
                                                                    dialog.dismiss();
                                                                }

                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                                    Log.d("mindbody_response", response);

                                                                    MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyClient.getMindbodyClientResponseModel();

                                                                    ((GlobalVariableApplication) (Application)getApplicationContext()).setClientId(clientId);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setLogIn(true);


                                                                    initialize();
                                                                    dialog.setCancelable(true);
                                                                    dialog.dismiss();
                                                                }
                                                            }, clientId);
                                                        }
                                                    });

                                                    Log.d("response", "DocumentSnapshot data: " + document.getData().get("ClientId"));


                                                } else {
                                                    Log.d("response", "No such document");


                                                    Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();

                                                    dialog.setCancelable(true);
                                                    dialog.dismiss();
                                                }
                                            } else {
                                                Log.d("response", "get failed with ", task.getException());

                                                Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();

                                                dialog.setCancelable(true);
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("login", task.getException().toString());

                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();


                                    dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);
                                }
                            }
                        });


            }
        });

        dialog.show();
    }


    private void showCreateAccountDialog(String title,Dialog previousDialog){
        final Dialog dialog = new Dialog(ClassInfo.this);

        dialog.setContentView(R.layout.signup_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height2 = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.92), WindowManager.LayoutParams.WRAP_CONTENT);


        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView have_read_clickable = dialog.findViewById(R.id.have_read_clickable);
        CheckBox agreement_check_box = dialog.findViewById(R.id.agreement_checkbox);




        EditText email = dialog.findViewById(R.id.editTextTextEmailAddress);
        EditText password = dialog.findViewById(R.id.editTextTextPassword);
        EditText confirm_password = dialog.findViewById(R.id.editTextTextPassword3);
        EditText height = dialog.findViewById(R.id.editTextNumberDecimal);
        EditText weight = dialog.findViewById(R.id.editTextNumberDecimal2);
        EditText phoneNum = dialog.findViewById(R.id.editTextPhone);
        EditText postalCode = dialog.findViewById(R.id.editTextTextPostalAddress);
        EditText first_name = dialog.findViewById(R.id.editTextTextPersonName2);
        EditText last_name = dialog.findViewById(R.id.editTextTextPersonName);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();




        popupTitle.setText(title);

        have_read_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgreementDialog("Group Training Agreement","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ullamcorper velit sed hendrerit. Suspendisse erat arcu, molestie quis est sed, vehicula luctus tellus. Quisque ultrices non justo nec ultricies. In posuere nisi vel nunc lobortis, ac sollicitudin quam pulvinar. Donec blandit augue id orci vehicula, eget semper est tempus. Integer auctor dictum justo, fringilla suscipit ligula suscipit at. Cras eget suscipit turpis. Maecenas sit amet nisl sagittis, hendrerit metus vitae, ornare purus. Curabitur sem ligula, imperdiet non nunc ut, luctus volutpat est. Suspendisse condimentum felis vitae nibh semper sollicitudin.");
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreement_check_box.isChecked()==true) {

                    String email_text;
                    String password_text;
                    String confirm_password_text;
                    String height_text;
                    String weight_text;
                    String phoneNum_text;
                    String postalCode_text;
                    String firstname_text;
                    String lastname_text;


                    email_text = email.getText().toString();
                    password_text = password.getText().toString();
                    confirm_password_text = confirm_password.getText().toString();
                    height_text = height.getText().toString();
                    weight_text = weight.getText().toString();
                    phoneNum_text = phoneNum.getText().toString();
                    postalCode_text = postalCode.getText().toString();
                    firstname_text = first_name.getText().toString();
                    lastname_text = last_name.getText().toString();


                    if(confirm_password_text.equals(password_text)){

                        if(password_text.length() >= 6){


                            HashMap<String, Object> params = new HashMap<>();
                            params.put("FirstName", firstname_text);
                            params.put("LastName", lastname_text);
                            params.put("Email", height_text);


                            //TODO:find correct Custom Client Field Info
                            HashMap<String, Object> params_customClientField = new HashMap<>();
                            params_customClientField.put("Id", 1);
                            params_customClientField.put("Value", height_text);
                            params_customClientField.put("DataType", "String");
                            params_customClientField.put("Name", "Employer");

                            HashMap<String, Object> params_customClientField2 = new HashMap<>();
                            params_customClientField2.put("Id", 2);
                            params_customClientField2.put("Value", weight_text);
                            params_customClientField2.put("DataType", "String");
                            params_customClientField2.put("Name", "Health Preferences");

                            ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                            hashMapArrayList.add(params_customClientField);
                            hashMapArrayList.add(params_customClientField2);

                            params.put("CustomClientFields", hashMapArrayList);



                            //params.put("Height", weight_text);
                            //params.put("Weight", firstname_text);
                            params.put("MobilePhone", phoneNum_text);
                            params.put("PostalCode", postalCode_text);


                            firebaseAuth.createUserWithEmailAndPassword(email_text, password_text)
                                    .addOnCompleteListener(ClassInfo.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Log.d("register", "sucessfull");



                                                userID = firebaseAuth.getCurrentUser().getUid();

                                                previousDialog.dismiss();
                                                dialog.setContentView(R.layout.progress_bar);
                                                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                                                dialog.setCancelable(false);




                                                DocumentReference documentReference = firebaseFirestore.collection("clientId").document(userID);

                                                MindbodyAddClient mindbodyAddClient = new MindbodyAddClient(getApplicationContext());

                                                mindbodyAddClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                                                    @Override
                                                    public void onError(String message) {
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_SHORT).show();
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        user.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.d("firebase", "User account deleted.");
                                                                        }
                                                                    }
                                                                });


                                                        dialog.setCancelable(true);
                                                        dialog.dismiss();



                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        mindbodyAddClient.addClient(new MindbodyClass.VolleyResponseListener() {
                                                            @Override
                                                            public void onError(String message) {
                                                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_SHORT).show();
                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                                user.delete()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Log.d("firebase", "User account deleted.");
                                                                                }
                                                                            }
                                                                        });

                                                                dialog.setCancelable(true);
                                                                dialog.dismiss();

                                                            }

                                                            @Override
                                                            public void onResponse(String response) {
                                                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                                Log.d("mindbody_response", response);

                                                                String clientId = mindbodyAddClient.getClientId();
                                                                MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyAddClient.getMindbodyClientResponseModel();


                                                                ((GlobalVariableApplication)  (Application)getApplicationContext()).setClientId(clientId);
                                                                ((GlobalVariableApplication)  (Application)getApplicationContext()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                ((GlobalVariableApplication) (Application)getApplicationContext()).setLogIn(true);


                                                                Map<String, Object> user = new HashMap<>();
                                                                user.put("ClientId", clientId);

                                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d("database", "sucuess");

                                                                    }
                                                                });

                                                                initialize();

                                                                //nonSwipeableViewPager.setCurrentItem(currPos);

                                                                dialog.setCancelable(true);
                                                                dialog.dismiss();

                                                            }
                                                        }, params);
                                                    }
                                                });

                                            }
                                            else{
                                                Log.d("register", task.getException().toString());
                                                if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.")){

                                                    Toast.makeText(getApplicationContext(), "The email address is badly formatted", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        }
                                    });

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "password is too short, need more than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "password does not matched", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "check to proceed", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


    private void showAgreementDialog(String title, String text){
        final Dialog dialog = new Dialog(ClassInfo.this);
        dialog.setContentView(R.layout.agreement_page);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView main_text = dialog.findViewById(R.id.main_text);

        popupTitle.setText(title);
        main_text.setText(text);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sentToEmail();
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    private void initialize(){


        isOver = classEx.getEndTImeStamp() < System.currentTimeMillis();


        isLateCancel = checkLateCancel(classEx.getCancelOffset(), classEx.getStartTimestamp());

        if(isOver || isLateCancel){

            init_add_client.setText("Over");
            init_add_client.setClickable(false);
        }

        else if(isCanceled){
            init_add_client.setText("Canceled");
            init_add_client.setClickable(false);
        }

        else if(totalBooked==maxCapacity && isWaitlistAvailable){
            init_add_client.setText("Add to waitlist");
            init_add_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String clientId = ((GlobalVariableApplication) getApplication()).getClientId();
                    Boolean registered = ((GlobalVariableApplication) getApplication()).getLogIn();

                    Toast.makeText(getApplicationContext(), registered.toString(), Toast.LENGTH_LONG).show();

                    if(registered){


                        Dialog dialog = new Dialog(ClassInfo.this);
                        dialog.setContentView(R.layout.progress_bar);
                        dialog.show();

                        MindbodyAddClientToClass mindbodyAddClientToClass = new MindbodyAddClientToClass(getApplicationContext());
                        mindbodyAddClientToClass.addClientToClass(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                                if(message.equals("{\"Error\":{\"Message\":\"Client is already booked at this time\",\"Code\":\"ClientIsAlreadyBooked\"}}")){
                                    Toast.makeText(getApplicationContext(), "You have booked already", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Something wrong, please try again", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("add_client", response);

                                Toast.makeText(ClassInfo.this, "added to waitlist", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }, classId, clientId);

                    }
                    else{

                        showLoginDialog("登录");
                    }
                }
            });
        }
        else if(totalBooked==maxCapacity && !isWaitlistAvailable){
            init_add_client.setText("full");
            init_add_client.setClickable(false);
        }
        else{
            init_add_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String clientId = ((GlobalVariableApplication) getApplication()).getClientId();
                    Boolean registered = ((GlobalVariableApplication) getApplication()).getLogIn();

                    Toast.makeText(getApplicationContext(), registered.toString(), Toast.LENGTH_LONG).show();

                    if(registered){

                        Dialog dialog = new Dialog(ClassInfo.this);
                        dialog.setContentView(R.layout.progress_bar);
                        dialog.show();


                        MindbodyAddClientToClass mindbodyAddClientToClass = new MindbodyAddClientToClass(getApplicationContext());
                        mindbodyAddClientToClass.addClientToClass(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                //Log.d("error", message);
                                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                if(message.equals("{\"Error\":{\"Message\":\"Client is already booked at this time\",\"Code\":\"ClientIsAlreadyBooked\"}}")){
                                    Toast.makeText(getApplicationContext(), "You have booked already", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Something wrong, please try again", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("add_client", response);
                                dialog.dismiss();

                                Toast.makeText(ClassInfo.this, "added to class", Toast.LENGTH_SHORT).show();

                                Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
                                Calendar beginTime = Calendar.getInstance();
                                beginTime.setTimeInMillis(classEx.getStartTimestamp());
                                Calendar endTime = Calendar.getInstance();
                                endTime.setTimeInMillis(classEx.getEndTImeStamp());
                                String title = classEx.getClassName();
                                String location = classEx.getAddress();

                                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                                calendarIntent.putExtra(CalendarContract.Events.TITLE, title);
                                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);

                                startActivity(calendarIntent);
                            }
                        }, classId, clientId);

                    }
                    else{

                        showLoginDialog("登录");
                    }
                }
            });
        }
    };

}
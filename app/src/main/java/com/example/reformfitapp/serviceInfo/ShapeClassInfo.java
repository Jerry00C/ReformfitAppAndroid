package com.example.reformfitapp.serviceInfo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.ClassInfo;
import com.example.reformfitapp.CustomAdapter;
import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.R;

public class ShapeClassInfo extends AppCompatActivity {


    ViewPager viewPager;
    LinearLayout sliderDotspanel;

    Integer[] imageId = {R.mipmap.location1, R.mipmap.location1, R.mipmap.location1, R.mipmap.location1};
    private ImageView[] dots = new ImageView[imageId.length];
    private int custom_position = 0;

    TextView classDes;
    ImageView classDesImage;

    TextView classEffectDes;
    ImageView classEffectDesImage;

    TextView faqDes;
    ImageView faqDesImage;


    ScrollView scrollView;
    ConstraintLayout constraintLayout;

    TextView info_tab1;
    TextView warning_tab1;

    TextView info_tab;
    TextView warning_tab;

    CardView info_thresh;
    CardView warning_thresh;


    TextView heartRateDes;
    ImageView heartRateDesImage;


    ImageView initBack;
    ImageView initHome;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_shape_class_info);



        initBack = findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShapeClassInfo.this.finish();
            }
        });

        initHome = findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((GlobalVariableApplication) getApplication()).setHome(true);

                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(switchActivityIntent);


            }
        });


        sliderDotspanel = findViewById(R.id.SliderDots);

        viewPager = findViewById(R.id.viewpager);


        PagerAdapter adapter = new CustomAdapter(this, imageId);
        viewPager.setAdapter(adapter);
        prepareDots(custom_position++);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (custom_position > imageId.length - 1) custom_position = 0;
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


                        scrollView.smoothScrollTo(0, sliderDotspanel.getBottom()-85);
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
                                        scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+85);

                                    }

                                });
                            }
                        });

                        warning_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+85);

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
                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+85);

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
                                        scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+85);

                                    }

                                });
                            }
                        });

                        warning_tab1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.post(new Runnable() {
                                    public void run() {
                                        scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+85);

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

                if(scrollY >= sliderDotspanel.getBottom()-85){

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
                                    scrollView.smoothScrollTo(0, info_thresh.getTop()+constraintLayout.getBottom()+85);

                                }

                            });
                        }
                    });

                    warning_tab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.smoothScrollTo(0, warning_thresh.getTop()+constraintLayout.getBottom()+85);

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
                if(scrollY >= warning_thresh.getTop()+constraintLayout.getBottom()+85){
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




        classDes = findViewById(R.id.class_des);
        classDesImage = findViewById(R.id.class_des_image);

        classDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classDes.getMaxLines() != 3) {
                    classDes.setMaxLines(3);
                } else {
                    classDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        classDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classDes.getMaxLines() != 3) {
                    classDes.setMaxLines(3);
                } else {
                    classDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });


        classEffectDes = findViewById(R.id.class_effect_text);
        classEffectDesImage = findViewById(R.id.class_effect_image);

        classEffectDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classEffectDes.getMaxLines() != 3) {
                    classEffectDes.setMaxLines(3);
                } else {
                    classEffectDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        classEffectDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classEffectDes.getMaxLines() != 3) {
                    classEffectDes.setMaxLines(3);
                } else {
                    classEffectDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });

        heartRateDes = findViewById(R.id.heartRateDes);
        heartRateDesImage = findViewById(R.id.init_heartRateDesImage);


        heartRateDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heartRateDes.getMaxLines() != 3){
                    heartRateDes.setMaxLines(3);
                }
                else{
                    heartRateDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        heartRateDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heartRateDes.getMaxLines() != 3){
                    heartRateDes.setMaxLines(3);
                }
                else{
                    heartRateDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });



        faqDes = findViewById(R.id.faq_des);
        faqDesImage = findViewById(R.id.faq_des_image);

        faqDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faqDes.getMaxLines() != 3) {
                    faqDes.setMaxLines(3);
                } else {
                    faqDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        faqDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faqDes.getMaxLines() != 3) {
                    faqDes.setMaxLines(3);
                } else {
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
                if (warnDes.getMaxLines() != 3) {
                    warnDes.setMaxLines(3);
                } else {
                    warnDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });
        warnDesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (warnDes.getMaxLines() != 3) {
                    warnDes.setMaxLines(3);
                } else {
                    warnDes.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });




    }


    private void prepareDots(int currPosition) {

        if (sliderDotspanel.getChildCount() > 0)
            sliderDotspanel.removeAllViews();


        for (int i = 0; i < imageId.length; i++) {

            dots[i] = new ImageView(this);
            if (i == currPosition) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

    }

}
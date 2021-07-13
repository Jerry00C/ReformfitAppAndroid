package com.example.reformfitapp.expandedFunc;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MainBottomNaviService;
import com.example.reformfitapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class YongjiuHealth extends AppCompatActivity {

    View view;

    ImageView initBack;
    ImageView initHome;

    LinearLayout reportList;


    Integer[] imageId = {R.mipmap.location1,R.mipmap.location1,R.mipmap.location1,R.mipmap.location1};
    private ImageView[] dots = new ImageView[imageId.length];
    private int custom_position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.progress_bar);


        view = getLayoutInflater().inflate(R.layout.activity_yongjiu_health, null);


        initBack = view.findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YongjiuHealth.this.finish();
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

        reportList = view.findViewById(R.id.report_list);


        String phoneNum = "";

        //TODO: get phone number
        /*if(((GlobalVariableApplication)getApplication()).getLogIn()){
            phoneNum = ((GlobalVariableApplication)getApplication()).getMindbodyClientResponseModel().getMobilePhone();
        *///}
        //else{
            phoneNum = "14379876631";
        //}

        YongjiuReport yongjiuReport = new YongjiuReport(getApplicationContext(), phoneNum);

        yongjiuReport.reportRequest(new YongjiuReport.VolleyResponseListener3() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "something wrong, try this later", Toast.LENGTH_SHORT).show();

                YongjiuHealth.this.finish();

            }


            @Override
            public void onResponse(ArrayList<YongjiuReportModel> yongjiuReportModelArrayList) {
                for(int i = 0; i < yongjiuReportModelArrayList.size(); i++){

                    YongjiuReportModel yongjiuReportModelEx = yongjiuReportModelArrayList.get(i);




                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 10, 0, 10);


                    final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.yougjiu_health_report_container, null);
                    cardView.setId(i);



                    LinearLayout starList;
                    TextView reportTime;
                    TextView muscleIndex;
                    TextView weight;
                    TextView muscleAmt;
                    TextView bodyFat;

                    ConstraintLayout constraintLayout;

                    starList = cardView.findViewById(R.id.star_list);
                    reportTime = cardView.findViewById(R.id.report_time);
                    muscleIndex = cardView.findViewById(R.id.muscle_index);
                    weight = cardView.findViewById(R.id.weight);
                    muscleAmt = cardView.findViewById(R.id.muscle_amt);
                    bodyFat = cardView.findViewById(R.id.body_fat);


                    double musInd = Double.parseDouble(yongjiuReportModelEx.getMuscleIndex());
                    DecimalFormat format = new DecimalFormat("0.0#");

                    reportTime.setText(yongjiuReportModelEx.getReportTime());
                    muscleIndex.setText(format.format(musInd));
                    weight.setText(yongjiuReportModelEx.getWeight());
                    muscleAmt.setText(yongjiuReportModelEx.getMuscleAmt());
                    bodyFat.setText(yongjiuReportModelEx.getBodyFat() + "%");


                    if(starList.getChildCount() > 0)
                        starList.removeAllViews();



                    double musIndConvert = 0;
                    int gender = yongjiuReportModelEx.getGender();
                    if(gender == 1){

                        musIndConvert = Math.floor((musInd - 16.6)*4.0)/4.0;
                    }
                    else{
                        musIndConvert =  Math.floor((musInd - 12.6)*4.0)/4.0;
                    }


                    for(int index = 0; index < 5; index++){

                        ImageView imageView = new ImageView(getApplicationContext());

                        if(musIndConvert < 1 && musIndConvert > 0){

                            imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_half_24));
                            musIndConvert = 0;

                        }
                        else if(musIndConvert == 0.0){


                            imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_empty_24));
                        }
                        else{


                            imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_24));

                            musIndConvert -= 1;
                        }

                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params2.setMargins(10, 0, 10, 0);


                        starList.addView(imageView, params2);




                    }

                    constraintLayout = cardView.findViewById(R.id.constraintLayout);
                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent switchActivityIntent = new Intent(getApplicationContext(), YongjiuReportEx.class);


                            switchActivityIntent.putExtra("MeasurementId",yongjiuReportModelEx.getId());

                            switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                            startActivity(switchActivityIntent);

                        }
                    });


                    reportList.addView(cardView, params);


                }

                setContentView(view);

            }
        });



    }
}
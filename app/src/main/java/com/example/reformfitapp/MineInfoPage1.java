package com.example.reformfitapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.expandedFunc.BMICalculator;
import com.example.reformfitapp.expandedFunc.BMRCalculator;
import com.example.reformfitapp.expandedFunc.BlogNews;
import com.example.reformfitapp.expandedFunc.DietHealth;
import com.example.reformfitapp.expandedFunc.TDEECalculator;
import com.example.reformfitapp.expandedFunc.YongjiuHealth;
import com.example.reformfitapp.expandedFunc.YongjiuReport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineInfoPage1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineInfoPage1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //ImageView initWristbandBrand;
    TextView textWristbandBrand;
    //ProgressBar progressBarBrand;


    ImageView initWristbandNum;
    TextView textWristbandNum;
    ProgressBar progressBarNum;


    ImageView BMIImage;
    TextView BMIText;


    ImageView TDEEImage;
    TextView TDEEText;

    ImageView BMRImage;
    TextView BMRText;

    ImageView purchaseImage;
    TextView purchaseText;

    ImageView healthImage;
    TextView healthText;

    ImageView dietImage;
    TextView dietText;

    ImageView blogImage;
    TextView blogText;



    public MineInfoPage1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineInfoPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static MineInfoPage1 newInstance(String param1, String param2) {
        MineInfoPage1 fragment = new MineInfoPage1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine_info_page1, container, false);


        //initWristbandBrand = view.findViewById(R.id.init_wristbandBrand);
        textWristbandBrand = view.findViewById(R.id.wristbandBrand);
        //progressBarBrand = view.findViewById(R.id.progressBarBrand);



        initWristbandNum = view.findViewById(R.id.init_wristbandNum);
        textWristbandNum = view.findViewById(R.id.wristbandNum);
        progressBarNum = view.findViewById(R.id.progressBarNum);

        BMIImage = view.findViewById(R.id.BMI_image);
        BMIText = view.findViewById(R.id.BMI_text);

        TDEEImage = view.findViewById(R.id.tdee_image);
        TDEEText = view.findViewById(R.id.tdee_text);


        BMRImage = view.findViewById(R.id.bmr_image);
        BMRText = view.findViewById(R.id.bmr_text);

        purchaseImage = view.findViewById(R.id.purchase_image);
        purchaseText = view.findViewById(R.id.purchase_text);


        healthImage = view.findViewById(R.id.health_image);
        healthText = view.findViewById(R.id.health_text);

        dietImage = view.findViewById(R.id.diet_image);
        dietText = view.findViewById(R.id.diet_text);

        blogImage = view.findViewById(R.id.blog_image);
        blogText = view.findViewById(R.id.blog_text);


        BMIImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BMICalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        BMIText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BMICalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        TDEEImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), TDEECalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        TDEEText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), TDEECalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        BMRImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BMRCalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        BMRText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BMRCalculator.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });


        purchaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), TabbedActivityPurchase.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        purchaseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), TabbedActivityPurchase.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });


        healthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), YongjiuHealth.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        healthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), YongjiuHealth.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });



        dietImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), DietHealth.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        dietText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), DietHealth.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BlogNews.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });

        blogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), BlogNews.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });




        initalized_profile();



        initWristbandNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumRequestDialog();
            }
        });
        return view;
    }




    private void showNumRequestDialog(){


        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("Wristband Number");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initWristbandNum.setVisibility(View.GONE);
                initWristbandNum.setClickable(false);
                progressBarNum.setVisibility(View.VISIBLE);

                String wristbandNum_inserted = textView.getText().toString();

                Log.d("wristbandNum_inserted", wristbandNum_inserted);


                HashMap<String, Object> params = new HashMap<String, Object>();


                HashMap<String, Object> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getActivity().getApplication()).getClientId();

                params_client.put("Id", clientId);

                //TODO:find correct Custom Client Field Info
                HashMap<String, Object> params_customClientField = new HashMap<>();
                params_customClientField.put("Id", 4);
                params_customClientField.put("Value", wristbandNum_inserted);
                params_customClientField.put("DataType", "String");
                params_customClientField.put("Name", "Progressive Swim Level"); //weight

                ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                hashMapArrayList.add(params_customClientField);


                params_client.put("CustomClientFields", hashMapArrayList);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());



                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Something wrong, try it later", Toast.LENGTH_SHORT).show();
                        progressBarNum.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                Toast.makeText(getContext(), "Something wrong, try it later", Toast.LENGTH_SHORT).show();
                                progressBarNum.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getActivity().getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setWristBandNum(wristbandNum_inserted);
                                ((GlobalVariableApplication) getActivity().getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textWristbandNum.setText(wristbandNum_inserted);
                                progressBarNum.setVisibility(View.INVISIBLE);

                                textWristbandNum.setClickable(true);
                                textWristbandNum.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showNumRequestDialog();
                                    }
                                });
                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




        dialog.show();




    }
    public void refresh(){
        initalized_profile();
    }


    private void initalized_profile(){

        Log.d("page1","called");

        MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getActivity().getApplication()).getMindbodyClientResponseModel();

        String wristBandBrandText = mindbodyClientResponseModel.getWristBandBrand();

        if(wristBandBrandText != null){
            textWristbandBrand.setText(wristBandBrandText);
        }





        String wristBandNumText = mindbodyClientResponseModel.getWristBandNum();
        if(wristBandNumText != null) Log.d("wristBandNumText",wristBandNumText);
        if(wristBandNumText != null && !wristBandNumText.equals("null")){
            textWristbandNum.setText(wristBandNumText);
            initWristbandNum.setVisibility(View.GONE);
            initWristbandNum.setClickable(false);
            textWristbandNum.setClickable(true);
            textWristbandNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNumRequestDialog();
                }
            });
        }


    }
}
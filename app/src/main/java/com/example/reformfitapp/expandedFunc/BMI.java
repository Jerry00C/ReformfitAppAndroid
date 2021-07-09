package com.example.reformfitapp.expandedFunc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BMI#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BMI extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText editAge;
    EditText editHeight;
    EditText editWeight;

    TextView ageWarning;
    TextView heightWarning;
    TextView weightWarning;

    TextView resultText;
    TextView resultTitle;
    View resultBar;


    TextView initCancel;
    TextView initSubmit;

    ImageView initCancel2;

    public BMI() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BMI.
     */
    // TODO: Rename and change types and number of parameters
    public static BMI newInstance(String param1, String param2) {
        BMI fragment = new BMI();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bmi, container, false);

        editAge = view.findViewById(R.id.edit_age);
        editHeight = view.findViewById(R.id.edit_height);
        editWeight = view.findViewById(R.id.edit_weight);

        ageWarning = view.findViewById(R.id.age_warning);
        heightWarning = view.findViewById(R.id.height_warning);
        weightWarning = view.findViewById(R.id.weight_warning);

        editAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals("")){
                    if(Integer.parseInt(s.toString()) < 18){
                        ageWarning.setVisibility(View.VISIBLE);

                    }
                    else{
                        ageWarning.setVisibility(View.GONE);
                    }
                }


            }
        });


        editWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ageWarning.setVisibility(View.GONE);


            }
        });

        editHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ageWarning.setVisibility(View.GONE);

            }
        });

        resultBar = view.findViewById(R.id.result_bar);
        resultText = view.findViewById(R.id.result_text);
        resultTitle = view.findViewById(R.id.result_title);


        initCancel = view.findViewById(R.id.init_cancel);
        initCancel2 = view.findViewById(R.id.init_cancel2);
        initSubmit = view.findViewById(R.id.init_submit);

        initCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down,R.anim.sliding_down).remove(BMI.this).commit();

            }
        });
        initCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down,R.anim.sliding_down).remove(BMI.this).commit();

            }
        });

        initSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checkValid = true;

                if(editAge.getText().toString().equals("") || Integer.parseInt(editAge.getText().toString())<18){

                    checkValid = false;
                    ageWarning.setVisibility(View.VISIBLE);

                };
                if(editHeight.getText().toString().equals("")){
                    checkValid = false;
                    heightWarning.setVisibility(View.VISIBLE);

                }
                if(editWeight.getText().toString().equals("")){
                    checkValid = false;
                    weightWarning.setVisibility(View.VISIBLE);
                }


                if(checkValid){

                    float weight = Float.parseFloat(editWeight.getText().toString());
                    float height = Float.parseFloat(editHeight.getText().toString());

                    float result = (float) (Math.round((float) (weight /( Math.pow((height/100),2)))*100)/100.0);

                    resultText.setText(String.valueOf(result));
                    resultText.setVisibility(View.VISIBLE);
                    resultTitle.setVisibility(View.VISIBLE);
                    resultBar.setVisibility(View.VISIBLE);




                }



            }
        });






        return view;
    }
}
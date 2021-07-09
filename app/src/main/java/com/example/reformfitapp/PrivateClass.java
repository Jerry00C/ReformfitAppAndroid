package com.example.reformfitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivateClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateClass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imageView;
    private TextView des;

    private boolean fragment;

    private Button initCheck;

    public PrivateClass(boolean fragment){
        this.fragment = fragment;

    }

    public PrivateClass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateClass.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivateClass newInstance(String param1, String param2) {
        PrivateClass fragment = new PrivateClass();
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
        View view = inflater.inflate(R.layout.fragment_private_class, container, false);
        imageView = view.findViewById(R.id.text_collaps_btn);
        des = view.findViewById(R.id.des);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(des.getMaxLines() != 3){
                    des.setMaxLines(3);
                }
                else{
                    des.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });

        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(des.getMaxLines() != 3){
                    des.setMaxLines(3);
                }
                else{
                    des.setMaxLines(Integer.MAX_VALUE);
                }

            }
        });

        initCheck = view.findViewById(R.id.group_service);
        initCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getContext(), PrivateTrainingInfo.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);

            }
        });





        return view;
    }
}
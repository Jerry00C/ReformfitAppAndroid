package com.example.reformfitapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryBSDF extends BottomSheetDialogFragment {


    private Context context;


    MaterialButton initClassInfo;
    MaterialButton initCancel;

    private int classId;
    private MindbodyClassModel mindbodyClassModel;

    public HistoryBSDF() {
    }

    public HistoryBSDF(int classId, MindbodyClassModel mindbodyClassModel, Context context) {
        this.classId = classId;
        this.mindbodyClassModel = mindbodyClassModel;
        this.context = context;

    }

    public static HistoryUpcomingBottomSheetDialogFragment newInstance() {
        return new HistoryUpcomingBottomSheetDialogFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.bottom_sheet_dialog_history, container,
                false);


        initClassInfo = view.findViewById(R.id.init_classInfo);
        initCancel = view.findViewById(R.id.cancel);



        initClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent switchActivityIntent = new Intent(getContext(), ClassInfo.class);

                switchActivityIntent.putExtra("ClassId", classId);
                switchActivityIntent.putExtra("MindbodyClassModel", mindbodyClassModel);

                Log.d("before class info", mindbodyClassModel.toString());

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(switchActivityIntent);
            }
        });



        initCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;

    }

}


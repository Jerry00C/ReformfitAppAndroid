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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryUpcomingOnlineBSDF extends BottomSheetDialogFragment {


    private Context context;

    MaterialButton initClassCancel;
    MaterialButton initClassInfo;
    MaterialButton initLiveStream;
    MaterialButton initCancel;


    private int classId;
    private boolean lateCancel;
    private String clientId;
    private MindbodyClassModel mindbodyClassModel;

    public HistoryUpcomingOnlineBSDF() {
    }

    public HistoryUpcomingOnlineBSDF(int classId, boolean lateCancel, String clientId, MindbodyClassModel mindbodyClassModel, Context context) {
        this.classId = classId;
        this.lateCancel = lateCancel;
        this.clientId = clientId;
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


        View view = inflater.inflate(R.layout.bottom_sheet_dialog_upcoming_online, container,
                false);


        initClassCancel = view.findViewById(R.id.init_classCancel);
        initClassInfo = view.findViewById(R.id.init_classInfo);
        initLiveStream = view.findViewById(R.id.init_liveStream);
        initCancel = view.findViewById(R.id.cancel);


        if(lateCancel){
            initClassCancel.setText("Late Cancel");
        }

        initClassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                removeClient();

            }
        });

        initClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent switchActivityIntent = new Intent(getContext(), ClassInfo.class);

                switchActivityIntent.putExtra("ClassId", classId);
                switchActivityIntent.putExtra("MindbodyClassModel", mindbodyClassModel);

                Log.d("before class info", mindbodyClassModel.toString());

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(switchActivityIntent);
            }
        });


        initLiveStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String streamLink = mindbodyClassModel.getVirtualStreamLink();
                Log.d("stream link", streamLink);
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


    private void removeClient(){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_bar);
        dialog.show();

        MindbodyRemoveClientFromClass mindbodyRemoveClientFromClass = new MindbodyRemoveClientFromClass(context);

        mindbodyRemoveClientFromClass.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                Log.d("mindbody_response", response);


                HashMap<String, Object> params = new HashMap<>();
                params.put("ClientId", clientId);
                params.put("ClassId", classId);
                params.put("Test", false);
                params.put("SendEmail", true);
                params.put("LateCancel", lateCancel);



                mindbodyRemoveClientFromClass.removeClientFromClass(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);

                        dialog.dismiss();

                    }
                }, params);
            }
        });

    }




}


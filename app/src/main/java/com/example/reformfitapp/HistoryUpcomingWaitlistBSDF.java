package com.example.reformfitapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

public class HistoryUpcomingWaitlistBSDF extends BottomSheetDialogFragment {


    private Context context;

    MaterialButton initWaitlistRemove;
    MaterialButton initClassInfo;
    MaterialButton initCancel;


    private int classId;
    private MindbodyClassModel mindbodyClassModel;
    private int waitlistId;

    public HistoryUpcomingWaitlistBSDF() {
    }

    public HistoryUpcomingWaitlistBSDF(int classId, MindbodyClassModel mindbodyClassModel, Context context, int waitlistId) {
        this.classId = classId;
        this.mindbodyClassModel = mindbodyClassModel;
        this.context = context;
        this.waitlistId = waitlistId;

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


        View view = inflater.inflate(R.layout.bottom_sheet_dialog_upcoming_waitlist, container,
                false);


        initWaitlistRemove = view.findViewById(R.id.init_wailistRemove);
        initClassInfo = view.findViewById(R.id.init_classInfo);
        initCancel = view.findViewById(R.id.cancel);



        initWaitlistRemove.setOnClickListener(new View.OnClickListener() {
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



        MindbodyRemoveFromWaitlist mindbodyRemoveFromWaitlist = new MindbodyRemoveFromWaitlist(context);
        mindbodyRemoveFromWaitlist.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                Log.d("mindbody_response", response);

                mindbodyRemoveFromWaitlist.removeWaitlist(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                    }
                }, waitlistId);

            }
        });


    }

}


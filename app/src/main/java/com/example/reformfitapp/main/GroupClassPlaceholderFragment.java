package com.example.reformfitapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.reformfitapp.ClassInfo;
import com.example.reformfitapp.LocationInfo;
import com.example.reformfitapp.MindbodyClass;
import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.R;
import com.example.reformfitapp.ServiceTabbed;
import com.example.reformfitapp.databinding.GroupClassViewpageContainerBinding;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupClassPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GroupClassViewpageContainerBinding binding;
    private int indexEx;
    ArrayList<ArrayList<MindbodyClassModel>> models;
    LinearLayout linearLayout;

    private boolean fragment;

    public GroupClassPlaceholderFragment() {

    }

    public GroupClassPlaceholderFragment(ArrayList<ArrayList<MindbodyClassModel>> modelsFrom, int index, boolean fragment) {
        // Required empty public constructor
        models = modelsFrom;
        indexEx = index;
        this.fragment = fragment;
    }


    public static GroupClassPlaceholderFragment newInstance(int index) {
        GroupClassPlaceholderFragment fragment = new GroupClassPlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        //int index = savedInstanceState.getInt(ARG_SECTION_NUMBER);




        binding = GroupClassViewpageContainerBinding.inflate(inflater, container, false);
        linearLayout = binding.classAll;

        linearLayout.removeAllViews();
        //System.out.println(indexEx);

        ArrayList<MindbodyClassModel> modelEx = models.get(indexEx);
        for(int indexArray = 0; indexArray < modelEx.size(); indexArray++){


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            if(fragment && indexArray==modelEx.size()-1){

                params.setMargins(3, 10, 3, 250);
            }
            else{

                params.setMargins(3, 10, 3, 10);
            }

            MindbodyClassModel mindbodyClassModelEx = modelEx.get(indexArray);
           // Log.d("inner_tab" , mindbodyClassModelEx.toString());

            final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.group_class_cardview, null);
            cardView.setId(indexArray);

            TextView className;
            TextView classTime;
            TextView classDes;
            ImageView staffImage;

            className = cardView.findViewById(R.id.class_name);
            classTime = cardView.findViewById(R.id.class_time);
            staffImage = cardView.findViewById(R.id.staff_image);


            className.setText(mindbodyClassModelEx.getClassName());
            classTime.setText(mindbodyClassModelEx.getStartTimeCut() + "-" + mindbodyClassModelEx.getEndTimeCut());

            String staff_imageUrl = mindbodyClassModelEx.getStaff_mageUrl();

            if(staff_imageUrl != null){
                Glide.with(GroupClassPlaceholderFragment.this).load(staff_imageUrl).into(staffImage);
            }



            ConstraintLayout constraintLayout = cardView.findViewById(R.id.init_detail_info);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent switchActivityIntent = new Intent(getContext(), ClassInfo.class);

                    switchActivityIntent.putExtra("ClassId", mindbodyClassModelEx.getClassId());
                    switchActivityIntent.putExtra("MindbodyClassModel", mindbodyClassModelEx);

                    //Log.d("before class info", mindbodyClassModelEx.toString());

                    switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(switchActivityIntent);
                }
            });


            linearLayout.addView(cardView,params);


        }



        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

   /* private void refreshPage(){

        getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                getClassInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);




                        Intent switchActivityIntent = new Intent(context, ServiceTabbed.class);

                        switchActivityIntent.putExtra("MindbodyCLassModelArray", mindbodyClassModelArray);
                        switchActivityIntent.putExtra("startPagePos", start_page_pos);

                        switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(switchActivityIntent);

                    }
                }, start_date, end_date, -1);
            }
        });
    }*/



}
package com.example.reformfitapp;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.reformfitapp.main.GroupClassViewpagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupClass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupClass extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ArrayList<ArrayList<MindbodyClassModel>> models;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    // Inflate the layout for this fragment
    ViewPager viewPager;
    LinearLayout sliderDotspanel;

    Integer[] imageId = {R.mipmap.location1,R.mipmap.location1,R.mipmap.location1,R.mipmap.location1};

    ImageView[] dots = new ImageView[imageId.length];
    int custom_position = 0;

    ViewPager viewPagerClasses;

    TabLayout tabLayout;

    boolean fragment;

    //ArrayList<ArrayList<MindbodyClassModel>> models;

    public GroupClass(){

    }

    public GroupClass(ArrayList<ArrayList<MindbodyClassModel>> modelsFrom, boolean fragmentFrom) {
        // Required empty public constructor
        models = modelsFrom;
        fragment = fragmentFrom;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupClass.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupClass newInstance(String param1, String param2) {
        GroupClass fragment = new GroupClass();
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


        //Log.d("group_class", models.get(0).get(0).toString());



        View view = inflater.inflate(R.layout.fragment_group_class, container, false);


        sliderDotspanel = view.findViewById(R.id.SliderDots);
        viewPager = view.findViewById(R.id.viewpager);

        PagerAdapter adapter = new CustomAdapter(getActivity(),imageId);
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


        Calendar cal = Calendar.getInstance();

        ArrayList <String> dates = new ArrayList<String>();
        ArrayList <String> dates_for_request = new ArrayList<String>();
        for(int index = 0; index < 14; index++){

            int curr_month = cal.get(Calendar.MONTH) + 1;
            int curr_date = cal.get(Calendar.DAY_OF_MONTH);
            int curr_year = cal.get(Calendar.YEAR);
            dates.add(curr_month + "." + curr_date);
            dates_for_request.add(curr_year + "-" + curr_month + "-" + curr_date);
            cal.add(Calendar.DAY_OF_MONTH, 1);


        }

        Log.d("dates", dates.toString());


        viewPagerClasses = view.findViewById(R.id.viewpagerclasses);

        PagerAdapter adapter1 = new GroupClassViewpagerAdapter(getActivity(),  getChildFragmentManager(), dates, models, fragment);
        viewPagerClasses.setAdapter(adapter1);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPagerClasses);



        return view;
    }



    private void prepareDots(int currPosition){

        if(sliderDotspanel.getChildCount() > 0)
            sliderDotspanel.removeAllViews();


        for(int i = 0; i < imageId.length; i++){

            dots[i] = new ImageView(getContext());
            if(i == currPosition){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

    }


}
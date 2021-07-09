package com.example.reformfitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.reformfitapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayout tabs;
    ViewPager viewPager;



    View view;
    View view1;

    public ServiceTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceTabFragment newInstance(String param1, String param2) {
        ServiceTabFragment fragment = new ServiceTabFragment();
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
        view =  inflater.inflate(R.layout.fragment_progress_bar, container, false);

        view1 = inflater.inflate(R.layout.fragment_service_tab, container, false);


        viewPager = view1.findViewById(R.id.view_pager);
        tabs = view1.findViewById(R.id.tabs);


        initialized();

        return view;
    }

    private void initialized(){


        Calendar cal = Calendar.getInstance();

        String start_date = "";
        String end_date = "";


        int curr_month = cal.get(Calendar.MONTH) + 1;
        int curr_date = cal.get(Calendar.DAY_OF_MONTH);
        int curr_year = cal.get(Calendar.YEAR);

        String curr_month_final;
        String curr_date_final;
        if(curr_month < 10){
            curr_month_final = "0" + curr_month;
        }
        else{
            curr_month_final = String.valueOf(curr_month);
        }

        if(curr_date < 10){
            curr_date_final = "0" + curr_date;
        }
        else{
            curr_date_final = String.valueOf(curr_date);
        }



        start_date = curr_year + "-" + curr_month_final + "-" + curr_date_final;

        cal.add(Calendar.DAY_OF_MONTH, 13);
        curr_month = cal.get(Calendar.MONTH) + 1;
        curr_date = cal.get(Calendar.DAY_OF_MONTH);
        curr_year = cal.get(Calendar.YEAR);

        if(curr_month < 10){
            curr_month_final = "0" + curr_month;
        }
        else{
            curr_month_final = String.valueOf(curr_month);
        }

        if(curr_date < 10){
            curr_date_final = "0" + curr_date;
        }
        else{
            curr_date_final = String.valueOf(curr_date);
        }

        end_date = curr_year + "-" + curr_month_final + "-" + curr_date_final;

        System.out.println("start_date: " + start_date);
        System.out.println("end_date: " + end_date);


        MindbodyClass mindbodyClass = new MindbodyClass(getActivity());

        String finalStart_date = start_date;
        String finalEnd_date = end_date;
        mindbodyClass.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                mindbodyClass.getClassInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);

                        ArrayList<ArrayList<MindbodyClassModel>> models = mindbodyClass.getMindbodyClassModelArray().getModelAll();
                        ArrayList<ArrayList<MindbodyClassModel>> modelsOnline = mindbodyClass.getMindbodyClassModelArray().getModelOnlineAll();



                        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getChildFragmentManager(), models, modelsOnline, true);

                        viewPager.setAdapter(sectionsPagerAdapter);
                        tabs.setupWithViewPager(viewPager);

                        FrameLayout frameLayout = view.findViewById(R.id.container);
                        frameLayout.removeAllViews();
                        frameLayout.addView(view1);

                    }
                }, finalStart_date, finalEnd_date, -1);
            }
        });
    }
}
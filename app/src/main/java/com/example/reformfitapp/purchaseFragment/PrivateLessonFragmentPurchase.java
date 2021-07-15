package com.example.reformfitapp.purchaseFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reformfitapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivateLessonFragmentPurchase#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateLessonFragmentPurchase extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View pack_100_session;
    private View pack_75_session;
    private View pack_50_session;
    private View pack_1_session;

    private View pack_20_week;
    private View pack_16_week;
    private View pack_12_week;
    private View pack_4_week;
    private Context context;
    private View current_view;




    public PrivateLessonFragmentPurchase() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateLessonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivateLessonFragmentPurchase newInstance(String param1, String param2) {
        PrivateLessonFragmentPurchase fragment = new PrivateLessonFragmentPurchase();
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

        current_view = inflater.inflate(R.layout.fragment_private_lesson_purchase, container, false);
        context = getActivity();

        pack_100_session = current_view.findViewById(R.id.one_on_one_option1_clickable);
        pack_75_session = current_view.findViewById(R.id.one_on_one_option2_clickable);
        pack_50_session = current_view.findViewById(R.id.one_on_one_option3_clickable);
        pack_1_session = current_view.findViewById(R.id.one_on_one_option4_clickable);



        pack_100_session.setOnClickListener(this);
        pack_75_session.setOnClickListener(this);
        pack_50_session.setOnClickListener(this);
        pack_1_session.setOnClickListener(this);


        return current_view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.one_on_one_option1_clickable:
                initializeChosenOptionMembership(R.id.info_first,R.id.purchase_info_1,R.id.purchase_info_2,R.id.info_first_display);
                break;
            case  R.id.one_on_one_option2_clickable:
                initializeChosenOptionMembership(R.id.one_on_one_option2, R.id.purchase_info_2_1,R.id.purchase_info_2_2,R.id.one_on_one_option2_price);
                break;
            case  R.id.one_on_one_option3_clickable:
                initializeChosenOptionMembership(R.id.one_on_one_option3, R.id.purchase_info_3_1,R.id.purchase_info_3_2,R.id.one_on_one_option3_price);
                break;
            case  R.id.one_on_one_option4_clickable:
                initializeChosenOptionMembership(R.id.one_on_one_option4, R.id.purchase_info_4_1,R.id.purchase_info_4_2,R.id.one_on_one_option4_price);
                break;
            default:
                break;
        }

    }

    ////////////////////////////// switch to pass purchasing page
    private void switchToPassPurchasePage(String title, ArrayList<String> infos,String price) {

        Intent switchActivityIntent = new Intent(getActivity(), PrivateLessonMemberPurchasePage.class);


        switchActivityIntent.putExtra("name",title);
        for(int it=0; it<infos.size(); it++){
            switchActivityIntent.putExtra("description"+ (it + 1), infos.get(it));
        }
        switchActivityIntent.putExtra("price",price);
        startActivity(switchActivityIntent);
    }

    private void initializeChosenOptionPass(int purchase_title, int purchase_info1, int purchase_info2, int purchase_info3, int price){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(purchase_info1);
        TextView description2_view = current_view.findViewById(purchase_info2);
        TextView description3_view = current_view.findViewById(purchase_info3);
        TextView price_view = current_view.findViewById(price);


        String pass_name = pass_name_view.getText().toString();
        String description1 = description1_view.getText().toString();
        String description2 = description2_view.getText().toString();
        String description3 = description3_view.getText().toString();
        String priceText = price_view.getText().toString();
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(description1);
        descriptions.add(description2);
        descriptions.add(description3);
        switchToPassPurchasePage(pass_name,descriptions,priceText);
    }

    //////////////////////////// switch to membership purchasing page

    private void switchToMembershipPurchasePage(String title, ArrayList<String> infos, String price) {

        Intent switchActivityIntent = new Intent(getActivity(), PrivateLessonMemberPurchasePage.class);


        switchActivityIntent.putExtra("name",title);
        for(int it=0; it<infos.size(); it++){
            switchActivityIntent.putExtra("description"+ (it + 1), infos.get(it));
        }
        switchActivityIntent.putExtra("price",price);
        startActivity(switchActivityIntent);
    }

    private void initializeChosenOptionMembership(int purchase_title, int info1, int info2,int price){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(info1);
        TextView description2_view = current_view.findViewById(info2);
        TextView price_view = current_view.findViewById(price);



        String pass_name = pass_name_view.getText().toString();


        String description1 = description1_view.getText().toString();
        String description2 = description2_view.getText().toString();
        String description3 = "";
        String priceText = price_view.getText().toString();

        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(description1);
        descriptions.add(description2);
        descriptions.add(description3);

        switchToMembershipPurchasePage(pass_name, descriptions,priceText);

    }
}
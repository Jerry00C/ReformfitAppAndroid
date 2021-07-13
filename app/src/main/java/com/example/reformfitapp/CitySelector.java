package com.example.reformfitapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitySelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CitySelector extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button city1;
    Button city2;
    Button city3;
    Button city4;
    Button city5;
    Button city6;
    Button city7;

    LinearLayout city_list;
    LinearLayout location_list;



    String [][] location_list_All = {{"location1", "location1", "location1","location1", "location1", "location1","location1"},
            {"location1", "location1"},
            {"location1", "location1"},
            {"location1", "location1"},
            {"location1", "location1"},
            {"location1", "location1"},
            {"location1", "location1"}};

    Button location_selected;
    Button city_selected;

    TextView cityList;


    public CitySelector(TextView cityList) {
        // Required empty public constructor
        this.cityList = cityList;
    }

    public CitySelector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CitySelector.
     */
    // TODO: Rename and change types and number of parameters
    public static CitySelector newInstance(String param1, String param2) {
        CitySelector fragment = new CitySelector();
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

        View view = inflater.inflate(R.layout.fragment_city_selector, container, false);


        city1 = (Button) view.findViewById(R.id.button);
        city2 = (Button) view.findViewById(R.id.button1);
        city3 = (Button) view.findViewById(R.id.button2);
        city4 = (Button) view.findViewById(R.id.button3);
        city5 = (Button) view.findViewById(R.id.button4);
        city6 = (Button) view.findViewById(R.id.button5);
        city7 = (Button) view.findViewById(R.id.button6);

        city_list = view.findViewById(R.id.city_list);
        location_list = (LinearLayout) view.findViewById(R.id.location_list);


        city1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onClick(View v) {

                //after city1 is selected
                city1.setBackgroundColor(getResources().getColor(R.color.yellow));
                city1.setTextColor(getResources().getColor(R.color.black));

                if(city_selected != null){

                    city1.setBackgroundColor(getResources().getColor(R.color.black4));
                    city1.setTextColor(getResources().getColor(R.color.white));
                }
                city1.setClickable(false);
                city_selected = city1;

                Toast.makeText(getContext(), "all is clicked", Toast.LENGTH_SHORT).show();
                Log.d("all","clicked");
                location_list.removeAllViews();

                LinearLayout linearLayout1 = new LinearLayout(getContext());

                int pos = city_list.indexOfChild(v);
                String [] location_list_text = location_list_All[pos];
                Toast.makeText(getContext(), "loop start", Toast.LENGTH_SHORT).show();

                 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(3, 10, 3, 10);

                    //ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(),R.style.Default_Button);


                    //Button btn = new Button(getApplicationContext());
                    final Button btn = (Button) getLayoutInflater().inflate(R.layout.default_button, null);
                    btn.setId(1);
                    final int id_ = btn.getId();
                    btn.setText("1");
                    btn.setBackgroundColor(getResources().getColor(R.color.black4));

                    location_list.addView(btn, params);
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Toast.makeText(view.getContext(),
                                    "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                    .show();

                            btn.setBackgroundColor(getResources().getColor(R.color.yellow));
                            btn.setTextColor(getResources().getColor(R.color.black));

                            if(location_selected != null){
                                location_selected.setBackgroundColor(getResources().getColor(R.color.black4));
                                location_selected.setTextColor(getResources().getColor(R.color.white));
                                location_selected.setClickable(true);

                            }
                            btn.setClickable(false);
                            location_selected = btn;

                        }
                    });
                }

        });
        return view;
    }
}
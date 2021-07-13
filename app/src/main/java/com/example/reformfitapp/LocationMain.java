package com.example.reformfitapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.reformfitapp.serviceInfo.FatBurnInfo;
import com.example.reformfitapp.serviceInfo.PrivateClassInfo;
import com.example.reformfitapp.serviceInfo.ShapeClassInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationMain extends Fragment {

    View city_list_init;
    VideoView simpleVideoView;

    View dialogView;


    CardView location_cardView;



    CitySelector citySelector;


    RelativeLayout init_burn_group_service;
    RelativeLayout init_shape_group_service;
    RelativeLayout init_private_service;

    String fab = "";

    TextView cityListText;

    View view2;
    FloatingActionButton floatingActionButton;

    public VideoView getSimpleVideoView() {
        return simpleVideoView;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocationMain() {
        // Required empty public constructor
    }

    public LocationMain(String fab_input){
        fab = fab_input;

    }


    public void disableAllClickable() {
        if(dialogView != null && location_cardView!=null &&  simpleVideoView != null && init_burn_group_service != null && init_shape_group_service != null && init_private_service != null ){
            dialogView.setClickable(false);
            location_cardView.setClickable(false);
            simpleVideoView.stopPlayback();

            init_burn_group_service.setClickable(false);
            init_shape_group_service.setClickable(false);
            init_private_service.setClickable(false);

        }

    }
    public void enableAllClickable() {
        dialogView.setClickable(true);
        location_cardView.setClickable(true);
        init_burn_group_service.setClickable(true);
        init_shape_group_service.setClickable(true);
        init_private_service.setClickable(true);

        startVideo();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location_Main.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationMain newInstance(String param1, String param2) {
        LocationMain fragment = new LocationMain();
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

        View view = inflater.inflate(R.layout.fragment_location_main, container, false);

        simpleVideoView = (VideoView) view.findViewById(R.id.startoff_video); // initiate a video view


        startVideo();


        init_burn_group_service = view.findViewById(R.id.burn_group_service);
        init_shape_group_service = view.findViewById(R.id.shape_group_service);
        init_private_service = view.findViewById(R.id.private_service);

        init_burn_group_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent switchActivityIntent = new Intent(getContext(), FatBurnInfo.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(switchActivityIntent);
            }
        });

        init_shape_group_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent switchActivityIntent = new Intent(getContext(), ShapeClassInfo.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(switchActivityIntent);
            }
        });


        init_private_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent switchActivityIntent = new Intent(getContext(), PrivateClassInfo.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(switchActivityIntent);
            }
        });




        cityListText = view.findViewById(R.id.city_list_text);


        citySelector = new CitySelector(cityListText);

        dialogView = (View) view.findViewById(R.id.city_list);
        dialogView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_LONG).show();

                /*if (getActivity().getSupportFragmentManager().getFragments().contains(citySelector)) {

                   startVideo();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down, R.anim.sliding_down).remove(citySelector).commitNow();
                }
                else {

                    simpleVideoView.stopPlayback();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_up, R.anim.sliding_up).add(R.id.fragment_container, citySelector).commitNow();

                }
                *//*openDialog();*/
            }
        });






        location_cardView = view.findViewById(R.id.location1);

        location_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), LocationInfo.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);



            }
        });

        /*if(fab.equals("fab")){
            Log.d("fab", "setted");
            disableAllClickable();
        }*/


        view2 = view.findViewById(R.id.view);

        View view3 = inflater.inflate(R.layout.activity_main_bottom_navi_service, container, false);

        floatingActionButton = view3.findViewById(R.id.floatingActionButton);
        int top = floatingActionButton.getTop();

        Log.d("top", String.valueOf(top));

        view3.setLayoutParams(new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, top));



        return view;
    }




    private void startVideo(){
        Uri uri = Uri.parse("https://dcffvbxhml043.cloudfront.net/7d87d189-98b4-47ee-a4b4-1c3fa4df15ad/mp4/60ad9036c346e300011b78e0_Mp4_Avc_Aac_16x9_1280x720p_30Hz_4.5Mbps.mp4");
        simpleVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */
                        // create an object of media controller
                        MediaController mediaController = new MediaController(getContext());
                        // set media controller object for a video view
                        simpleVideoView.setMediaController(mediaController);
                        /*
                         * and set its position on screen
                         */
                        mediaController.setAnchorView(simpleVideoView);
                    }
                });
            }
        });
        simpleVideoView.setVideoURI(uri);
        simpleVideoView.start();
    }


}
package com.example.reformfitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class MainBottomNaviService extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    LocationMain location_main;

    FloatingActionButton fab;

    MineInfo mineInfo;
    MineInfoPage1 mineInfoPage1;
    VideoMain videoMain;
    ServiceTabFragment serviceTabFragment;

    Animation slide_up;

    BottomAppBar bottomAppBar;
    String fabReponse;

    Fragment prevFragment;

    ConstraintLayout constraintLayout;

    View view;

    TextView textView;
    int currPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("main", "create");

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main_bottom_navi_service);


        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        location_main = new LocationMain();

        mineInfo = new MineInfo(location_main.getSimpleVideoView());
        mineInfoPage1 = new MineInfoPage1();
        videoMain = new VideoMain();
        serviceTabFragment = new ServiceTabFragment();

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sliding_up);

        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navi_view);
        bottomNavigationView.setBackground(null);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fabReponse = (String) getIntent().getSerializableExtra("Fab");



        constraintLayout = findViewById(R.id.constraintLayout);
        view = findViewById(R.id.view);

        textView = findViewById(R.id.textView63);


        if(fabReponse != null){
            if(fabReponse.equals("fab")){
                Toast.makeText(this, "fab", Toast.LENGTH_LONG).show();
                int startPagePos = (int) getIntent().getSerializableExtra("CurrPos");
                mineInfo.setCurrPos(startPagePos);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mineInfo).commitNow();
                //fab.performClick();
               /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, location_main).commitNow();



               location_main.disableAllClickable();
                bottomAppBar.performHide();
                //mineInfo.setClickable(false);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_up,R.anim.sliding_down).add(R.id.fragment_container,mineInfo).commitNow();
*/
            }

        }
        else{
            bottomNavigationView.setSelectedItemId(R.id.navigation_location);

        }




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "fab clicked", Toast.LENGTH_SHORT).show();


                if (getSupportFragmentManager().getFragments().contains(mineInfo)){

                    if(getSupportFragmentManager().getFragments().size() == 1){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, location_main).commitNow();
                        bottomAppBar.performShow();
                        location_main.enableAllClickable();
                        textView.setAlpha((float) 1);

                    }
                    else {


                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down, R.anim.sliding_down).remove(mineInfo).commitNow();
                        bottomAppBar.performShow();
                        location_main.enableAllClickable();
                        textView.setAlpha((float) 1);

                    }
                }
                else{
                    location_main.disableAllClickable();
                    bottomAppBar.performHide();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_up,R.anim.sliding_down).add(R.id.fragment_container,mineInfo).commitNow();

                    textView.setAlpha((float) 0.4);
                    //view.setVisibility(View.VISIBLE);

                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.navigation_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, location_main).commitNow();
                return true;

            case R.id.navigation_class:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, serviceTabFragment).commitNow();
                return true;



            case R.id.navigation_video:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, videoMain).commitNow();
                return true;
        }


        return false;
    }



    @Override
    public void onBackPressed() {

        List<Fragment> fragment = getSupportFragmentManager().getFragments();

        if (fragment.size() > 1){
            if (getSupportFragmentManager().getFragments().contains(mineInfo)){

                bottomAppBar.performShow();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down,R.anim.sliding_down).remove(mineInfo).commitNow();

                location_main.enableAllClickable();

                textView.setAlpha((float) 1);
            }

            else{
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.sliding_down,R.anim.sliding_down).remove(fragment.get(fragment.size()-1)).commitNow();
            }


        }
        else if(fragment.size() == 1 && fragment.get(0) == mineInfo){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, location_main).commitNow();
            bottomAppBar.performShow();
            location_main.enableAllClickable();

            textView.setAlpha((float) 1);

        }
        else{
            super.onBackPressed();
        }


    }


    @Override
    protected void onResume() {

        Log.d("main", "resume");
       if(((GlobalVariableApplication) getApplication()).isHome()){

           super.onResume();
           List<Fragment> fragments = getSupportFragmentManager().getFragments();
           Log.d("main", "fragment " + fragments.size());

           bottomAppBar.performShow();
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, location_main).commitNow();

           location_main.enableAllClickable();

           textView.setAlpha((float) 1);

           ((GlobalVariableApplication) getApplication()).setHome(false);
       }
       else{

           super.onResume();
       }


    }
}
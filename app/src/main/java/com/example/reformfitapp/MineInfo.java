package com.example.reformfitapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.reformfitapp.mine.main.MineClassViewpagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    NonSwipeableViewPager nonSwipeableViewPager;

    TabLayout tabLayout;

    TextView initLogin;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;

    private ImageView initProfile;

    private int currPos;
    ProgressBar progressBar;

    VideoView videoView;

    MineClassViewpagerAdapter adapter1;


    public void setCurrPos(int currPos) {
        this.currPos = currPos;
    }

    public MineInfo() {
        currPos = 0;
    }

    public MineInfo(VideoView videoViewEx) {
        // Required empty public constructor
        currPos = 0;
        videoView = videoViewEx;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static MineInfo newInstance(String param1, String param2) {
        MineInfo fragment = new MineInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mine_info, container, false);


        if(videoView != null){

            videoView.suspend();
        }

        nonSwipeableViewPager = view.findViewById(R.id.view_pager);



        adapter1 = new MineClassViewpagerAdapter(getActivity(), getChildFragmentManager(), nonSwipeableViewPager, (GlobalVariableApplication) getActivity().getApplication());
        nonSwipeableViewPager.setAdapter(adapter1);
        nonSwipeableViewPager.setCurrentItem(currPos);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(nonSwipeableViewPager);




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                Log.d("tab_selected", String.valueOf(tab.getPosition()));
                switch (tab.getPosition()){
                    case 1:
                    case 2:
                        if(!((GlobalVariableApplication) getActivity().getApplication()).getLogIn()){
                            showLoginDialog("登录");

                        }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        initLogin = view.findViewById(R.id.init_login);



        if(((GlobalVariableApplication) getActivity().getApplication()).getLogIn()){
            initLogin.setClickable(false);

            MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getActivity().getApplication()).getMindbodyClientResponseModel();
            initLogin.setText(mindbodyClientResponseModel.getFirstName() + " " + mindbodyClientResponseModel.getLastName());
        }

        else {
            initLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showLoginDialog("登录");

                    //Toast.makeText(getContext(), "has login", Toast.LENGTH_LONG).show();
                }
            });
        }


        initProfile = view.findViewById(R.id.init_profile);
        initProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getContext(), ProfilePage.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(switchActivityIntent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((GlobalVariableApplication) getActivity().getApplication()).getLogIn()){
            initLogin.setClickable(false);

            MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getActivity().getApplication()).getMindbodyClientResponseModel();
            initLogin.setText(mindbodyClientResponseModel.getFirstName() + " " + mindbodyClientResponseModel.getLastName());
        }

        else {
            initLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showLoginDialog("登录");

                    //Toast.makeText(getContext(), "has login", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(getContext()){
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP ){

                    dismiss();
                    nonSwipeableViewPager.setCurrentItem(0);
                }
                return false;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                super.onCreate(savedInstanceState);
            }

            @Override
            public void create() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                super.create();
            }
        };
        dialog.setContentView(R.layout.sign_in_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);



/*


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.);
        window.setAttributes(wlp);
*/



        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView createAccountClickable = dialog.findViewById(R.id.create_account_clickable);


        EditText email = dialog.findViewById(R.id.editTextTextEmailAddress2);
        EditText password = dialog.findViewById(R.id.editTextTextPassword2);

        popupTitle.setText(title);


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    nonSwipeableViewPager.setCurrentItem(0);
                }
                return false;
            }
        });


        createAccountClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCreateAccountDialog("新用户注册", dialog);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                nonSwipeableViewPager.setCurrentItem(0);

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text;
                String password_text;


                email_text = email.getText().toString();
                password_text = password.getText().toString();



                firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.signInWithEmailAndPassword(email_text, password_text)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "success");

                                    dialog.setContentView(R.layout.progress_bar);
                                    dialog.setCanceledOnTouchOutside(false);

                                    userID = firebaseAuth.getCurrentUser().getUid();
                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    DocumentReference docRef = firebaseFirestore.collection("clientId").document(userID);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    String clientId = (String) document.getData().get("ClientId");

                                                    MindbodyClient mindbodyClient = new MindbodyClient(getContext());

                                                    mindbodyClient.getUserToken(new MindbodyLocation.VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            mindbodyClient.getClientInfo(new MindbodyLocation.VolleyResponseListener() {
                                                                @Override
                                                                public void onError(String message) {
                                                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                                                    Log.d("mindbody_response", response);

                                                                    MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyClient.getMindbodyClientResponseModel();

                                                                    ((GlobalVariableApplication) getActivity().getApplication()).setClientId(clientId);
                                                                    ((GlobalVariableApplication) getActivity().getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                    ((GlobalVariableApplication) getActivity().getApplication()).setLogIn(true);

                                                                    initLogin.setClickable(false);
                                                                    initLogin.setText(mindbodyClientResponseModel.getFirstName() + " " + mindbodyClientResponseModel.getLastName());


                                                                    adapter1.refreshAll();
                                                                    dialog.setCancelable(true);
                                                                    dialog.dismiss();
                                                                }
                                                            }, clientId);
                                                        }
                                                    });

                                                    Log.d("response", "DocumentSnapshot data: " + document.getData().get("ClientId"));

                                                } else {
                                                    Log.d("response", "No such document");
                                                }
                                            } else {
                                                Log.d("response", "get failed with ", task.getException());
                                            }
                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("login", task.getException().toString());

                                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

        dialog.show();
    }


    public void showCreateAccountDialog(String title,Dialog previousDialog){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.signup_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height2 = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.92), WindowManager.LayoutParams.WRAP_CONTENT);



        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView have_read_clickable = dialog.findViewById(R.id.have_read_clickable);
        CheckBox agreement_check_box = dialog.findViewById(R.id.agreement_checkbox);








        EditText email = dialog.findViewById(R.id.editTextTextEmailAddress);
        EditText password = dialog.findViewById(R.id.editTextTextPassword);
        EditText confirm_password = dialog.findViewById(R.id.editTextTextPassword3);
        EditText height = dialog.findViewById(R.id.editTextNumberDecimal);
        EditText weight = dialog.findViewById(R.id.editTextNumberDecimal2);
        EditText phoneNum = dialog.findViewById(R.id.editTextPhone);
        EditText postalCode = dialog.findViewById(R.id.editTextTextPostalAddress);
        EditText first_name = dialog.findViewById(R.id.editTextTextPersonName2);
        EditText last_name = dialog.findViewById(R.id.editTextTextPersonName);



        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();




        popupTitle.setText(title);

        have_read_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgreementDialog("Group Training Agreement","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ullamcorper velit sed hendrerit. Suspendisse erat arcu, molestie quis est sed, vehicula luctus tellus. Quisque ultrices non justo nec ultricies. In posuere nisi vel nunc lobortis, ac sollicitudin quam pulvinar. Donec blandit augue id orci vehicula, eget semper est tempus. Integer auctor dictum justo, fringilla suscipit ligula suscipit at. Cras eget suscipit turpis. Maecenas sit amet nisl sagittis, hendrerit metus vitae, ornare purus. Curabitur sem ligula, imperdiet non nunc ut, luctus volutpat est. Suspendisse condimentum felis vitae nibh semper sollicitudin.");
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreement_check_box.isChecked()==true) {

                    String email_text;
                    String password_text;
                    String confirm_password_text;
                    String height_text;
                    String weight_text;
                    String phoneNum_text;
                    String postalCode_text;
                    String firstname_text;
                    String lastname_text;


                    email_text = email.getText().toString();
                    password_text = password.getText().toString();
                    confirm_password_text = confirm_password.getText().toString();
                    height_text = height.getText().toString();
                    weight_text = weight.getText().toString();
                    phoneNum_text = phoneNum.getText().toString();
                    postalCode_text = postalCode.getText().toString();
                    firstname_text = first_name.getText().toString();
                    lastname_text = last_name.getText().toString();


                    if(confirm_password_text.equals(password_text)){

                        if(password_text.length() >= 6){


                            HashMap<String, Object> params = new HashMap<>();
                            params.put("FirstName", firstname_text);
                            params.put("LastName", lastname_text);
                            params.put("Email", email_text);


                            //TODO:find correct Custom Client Field Info
                            HashMap<String, Object> params_customClientField = new HashMap<>();
                            params_customClientField.put("Id", 1);
                            params_customClientField.put("Value", height_text);
                            params_customClientField.put("DataType", "String");
                            params_customClientField.put("Name", "Employer");

                            HashMap<String, Object> params_customClientField2 = new HashMap<>();
                            params_customClientField2.put("Id", 2);
                            params_customClientField2.put("Value", weight_text);
                            params_customClientField2.put("DataType", "String");
                            params_customClientField2.put("Name", "Health Preferences");

                            HashMap<String, Object> params_customClientField3 = new HashMap<>();
                            params_customClientField3.put("Id", 3);
                            params_customClientField3.put("Value", "reformfit");
                            params_customClientField3.put("DataType", "String");
                            params_customClientField3.put("Name", "Contract Canceled");
                            ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                            hashMapArrayList.add(params_customClientField);
                            hashMapArrayList.add(params_customClientField2);
                            hashMapArrayList.add(params_customClientField3);

                            params.put("CustomClientFields", hashMapArrayList);



                            //params.put("Height", weight_text);
                            //params.put("Weight", firstname_text);
                            params.put("MobilePhone", phoneNum_text);
                            params.put("PostalCode", postalCode_text);

                            Log.d("param", params.toString());


                            firebaseAuth.createUserWithEmailAndPassword(email_text, password_text)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Log.d("register", "sucessfull");

                                                int viewPage_pos = nonSwipeableViewPager.getCurrentItem();


                                                userID = firebaseAuth.getCurrentUser().getUid();

                                                previousDialog.dismiss();
                                                dialog.setContentView(R.layout.progress_bar);

                                                dialog.setCancelable(false);




                                                DocumentReference documentReference = firebaseFirestore.collection("clientId").document(userID);

                                                MindbodyAddClient mindbodyAddClient = new MindbodyAddClient(getContext());

                                                mindbodyAddClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                                                                                   @Override
                                                                                   public void onError(String message) {
                                                                                       Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                                                                   }

                                                                                   @Override
                                                                                   public void onResponse(String response) {
                                                                                       mindbodyAddClient.addClient(new MindbodyClass.VolleyResponseListener() {
                                                                                           @Override
                                                                                           public void onError(String message) {
                                                                                               Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                                                                               Toast.makeText(getContext(), "something wrong", Toast.LENGTH_LONG).show();
                                                                                           }

                                                                                           @Override
                                                                                           public void onResponse(String response) {
                                                                                               Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                                                                               Log.d("mindbody_response", response);

                                                                                               String clientId = mindbodyAddClient.getClientId();
                                                                                               MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyAddClient.getMindbodyClientResponseModel();


                                                                                               ((GlobalVariableApplication) getActivity().getApplication()).setClientId(clientId);
                                                                                               ((GlobalVariableApplication) getActivity().getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                                               ((GlobalVariableApplication) getActivity().getApplication()).setLogIn(true);


                                                                                               Map<String, Object> user = new HashMap<>();
                                                                                               user.put("ClientId", clientId);

                                                                                               documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                   @Override
                                                                                                   public void onSuccess(Void unused) {
                                                                                                       Log.d("database", "sucuess");

                                                                                                   }
                                                                                               });

                                                                                               initLogin.setClickable(false);
                                                                                               initLogin.setText(mindbodyClientResponseModel.getFirstName() + " " + mindbodyClientResponseModel.getLastName());

                                                                                               //nonSwipeableViewPager.setCurrentItem(currPos);


                                                                                               adapter1.refreshAll();
                                                                                               dialog.setCancelable(true);
                                                                                               dialog.dismiss();
                                                                                               //nonSwipeableViewPager.getCurrentItem();

                                                                                           }
                                                                                       }, params);
                                                                                   }
                                                                               });

                                            }
                                            else{
                                                Log.d("register", task.getException().toString());
                                            }
                                        }
                                    });

                        }
                        else{
                            Toast.makeText(getContext(), "password is too short, need more than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "password does not matched", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "check to proceed", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


    public void showAgreementDialog(String title, String text){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.agreement_page);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextView main_text = dialog.findViewById(R.id.main_text);

        popupTitle.setText(title);
        main_text.setText(text);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sentToEmail();
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
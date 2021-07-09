package com.example.reformfitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {


    TextView name;

    //ImageView initEmail;
    TextView textEmail;
    //ProgressBar progressBarEmail;

    ImageView initPhoneNum;
    TextView textPhoneNum;
    ProgressBar progressBarPhoneNum;

    ImageView initGender;
    TextView textGender;
    ProgressBar progressBarGender;

    ImageView initBirthDate;
    TextView textBirthDate;
    ProgressBar progressBarBirthDate;

    ImageView initHeight;
    TextView textHeight;
    ProgressBar progressBarHeight;

    ImageView initWeight;
    TextView textWeight;
    ProgressBar progressBarWeight;

    ImageView initPostalCode;
    TextView textPostalCode;
    ProgressBar progressBarPostalCode;


    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;


    ImageView initBack;
    ImageView initHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile_page);



        initBack = findViewById(R.id.init_back);
        initBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePage.this.finish();
            }
        });

        initHome = findViewById(R.id.init_home);
        initHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent switchActivityIntent = new Intent(getApplicationContext(), MainBottomNaviService.class);

                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(switchActivityIntent);


            }
        });

        name = findViewById(R.id.name);

        //initEmail = findViewById(R.id.init_email);
        textEmail = findViewById(R.id.email);
        //progressBarEmail = findViewById(R.id.progressBar_email);



        initPhoneNum = findViewById(R.id.init_phoneNum);
        textPhoneNum = findViewById(R.id.phoneNum);
        progressBarPhoneNum = findViewById(R.id.progressBar_phoneNum);


        initGender = findViewById(R.id.init_gender);
        textGender = findViewById(R.id.gender);
        progressBarGender = findViewById(R.id.progressBar_gender);


        initBirthDate = findViewById(R.id.inti_birthdate);
        textBirthDate = findViewById(R.id.birthdate);
        progressBarBirthDate = findViewById(R.id.progressBar_birthdate);


        initHeight = findViewById(R.id.init_height);
        textHeight = findViewById(R.id.height);
        progressBarHeight = findViewById(R.id.progressBar_height);


        initWeight = findViewById(R.id.init_weight);
        textWeight = findViewById(R.id.weight);
        progressBarWeight = findViewById(R.id.progressBar_weight);


        initPostalCode = findViewById(R.id.init_postalCode);
        textPostalCode = findViewById(R.id.postalCode);
        progressBarPostalCode = findViewById(R.id.progressBar_postalCode);



        initPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneNumRequestDialog();
            }
        });

        initGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderRequestDialog();
            }
        });

        initBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthDateRequestDialog();
            }
        });

        initHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeightRequestDialog();
            }
        });

        initWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeightRequestDialog();
            }
        });

        initPostalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostalCodeRequestDialog();
            }
        });


        if(((GlobalVariableApplication)getApplication()).getLogIn()){
            loadingDialog();
        }
        else{
            showLoginDialog("登录");
        }

    }


    /*private void showEmailRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("Email");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initEmail.setVisibility(View.GONE);
                initEmail.setClickable(false);
                progressBarEmail.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String email_inserted = textView.getText().toString();

                Log.d("email_inserted", email_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();

                HashMap<String, String> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);

                params_client.put("Email", email_inserted);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setEmail(email_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.updateEmail(email_inserted)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("database email", "User email address updated.");

                                                    textEmail.setText(email_inserted);
                                                    progressBarEmail.setVisibility(View.INVISIBLE);

                                                    textEmail.setClickable(true);
                                                    textEmail.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            showEmailRequestDialog();
                                                        }
                                                    });
                                                }
                                            }
                                        });


                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }*/

    private void showPhoneNumRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("PhoneNumber");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initPhoneNum.setVisibility(View.GONE);
                initPhoneNum.setClickable(false);
                progressBarPhoneNum.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String phoneNum_inserted = textView.getText().toString();

                Log.d("phoneNum_inserted", phoneNum_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();

                HashMap<String, String> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);

                params_client.put("MobilePhone", phoneNum_inserted);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setMobilePhone(phoneNum_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textPhoneNum.setText(phoneNum_inserted);
                                progressBarPhoneNum.setVisibility(View.INVISIBLE);

                                textPhoneNum.setClickable(true);
                                textPhoneNum.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showPhoneNumRequestDialog();
                                    }
                                });



                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void showGenderRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("Gender");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initGender.setVisibility(View.GONE);
                initGender.setClickable(false);
                progressBarGender.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String gender_inserted = textView.getText().toString();

                Log.d("gender_inserted", gender_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();

                HashMap<String, String> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);
                params_client.put("Gender", gender_inserted);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setGender(gender_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textGender.setText(gender_inserted);
                                progressBarGender.setVisibility(View.INVISIBLE);

                                textGender.setClickable(true);
                                textGender.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showGenderRequestDialog();
                                    }
                                });



                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void showBirthDateRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("BirthDate");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initBirthDate.setVisibility(View.GONE);
                initBirthDate.setClickable(false);
                progressBarBirthDate.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String birthDate_inserted = textView.getText().toString();

                Log.d("birthDate_inserted", birthDate_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();
                HashMap<String, String> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);
                params_client.put("BirthDate", birthDate_inserted);


                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);


                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setBirthDate(birthDate_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textBirthDate.setText(birthDate_inserted);
                                progressBarBirthDate.setVisibility(View.INVISIBLE);

                                textBirthDate.setClickable(true);
                                textBirthDate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showBirthDateRequestDialog();
                                    }
                                });



                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void showHeightRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("Height");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initHeight.setVisibility(View.GONE);
                initHeight.setClickable(false);
                progressBarHeight.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String height_inserted = textView.getText().toString();

                Log.d("height_inserted", height_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();


                HashMap<String, Object> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);

                //TODO:find correct Custom Client Field Info
                HashMap<String, Object> params_customClientField = new HashMap<>();
                params_customClientField.put("Id", 1);
                params_customClientField.put("Value", height_inserted);
                params_customClientField.put("DataType", "String");
                params_customClientField.put("Name", "Employer"); //height

                ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                hashMapArrayList.add(params_customClientField);


                params_client.put("CustomClientFields", hashMapArrayList);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setHeight(height_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textHeight.setText(height_inserted);
                                progressBarHeight.setVisibility(View.INVISIBLE);

                                textHeight.setClickable(true);
                                textHeight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showHeightRequestDialog();
                                    }
                                });



                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showWeightRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("Weight");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initWeight.setVisibility(View.GONE);
                initWeight.setClickable(false);
                progressBarWeight.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String weight_inserted = textView.getText().toString();

                Log.d("weight_inserted", weight_inserted);


                HashMap<String, Object> params = new HashMap<String, Object>();


                HashMap<String, Object> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);

                //TODO:find correct Custom Client Field Info
                HashMap<String, Object> params_customClientField = new HashMap<>();
                params_customClientField.put("Id", 2);
                params_customClientField.put("Value", weight_inserted);
                params_customClientField.put("DataType", "String");
                params_customClientField.put("Name", "Health Preferences"); //weight

                ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                hashMapArrayList.add(params_customClientField);


                params_client.put("CustomClientFields", hashMapArrayList);

                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());



                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setWeight(weight_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textWeight.setText(weight_inserted);
                                progressBarWeight.setVisibility(View.INVISIBLE);

                                textWeight.setClickable(true);
                                textWeight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showWeightRequestDialog();
                                    }
                                });
                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




        dialog.show();




    }

    private void showPostalCodeRequestDialog(){


        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.profile_update_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = dialog.findViewById(R.id.popup_title);
        title.setText("PostalCode");

        MaterialButton confirm = dialog.findViewById(R.id.confirm_button);
        Button cancel = dialog.findViewById(R.id.cancel_button);

        TextInputEditText textView = dialog.findViewById(R.id.input_text);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initPostalCode.setVisibility(View.GONE);
                initPostalCode.setClickable(false);
                progressBarPostalCode.setVisibility(View.VISIBLE);

                dialog.dismiss();
                String postalCode_inserted = textView.getText().toString();

                Log.d("postalCode_inserted", postalCode_inserted);

                HashMap<String, Object> params = new HashMap<String, Object>();

                HashMap<String, String> params_client = new HashMap<>();
                String clientId = ((GlobalVariableApplication) getApplication()).getClientId();

                params_client.put("Id", clientId);

                params_client.put("PostalCode", postalCode_inserted);
                params.put("Client", params_client);
                params.put("CrossRegionalUpdate", false);
                params.put("Test", false);

                Log.d("params", params.toString());


                MindbodyUpdateClient mindbodyUpdateClient = new MindbodyUpdateClient(getApplicationContext());
                mindbodyUpdateClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);


                        mindbodyUpdateClient.updateClient(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);



                                MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();

                                mindbodyClientResponseModel.setPostalCode(postalCode_inserted);
                                ((GlobalVariableApplication) getApplication()).setMindbodyClientResponseModel(mindbodyClientResponseModel);


                                textPostalCode.setText(postalCode_inserted);
                                progressBarPostalCode.setVisibility(View.INVISIBLE);

                                textPostalCode.setClickable(true);
                                textPostalCode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showPostalCodeRequestDialog();
                                    }
                                });



                            }
                        }, params);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void initalized_profile(Dialog dialog){

        MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();


        String nameText = mindbodyClientResponseModel.getFirstName() + " " + mindbodyClientResponseModel.getLastName();
        name.setText(nameText);


        String emailText = mindbodyClientResponseModel.getEmail();
        if(emailText != "null"){
            textEmail.setText(emailText);
            /*initEmail.setVisibility(View.GONE);
            initEmail.setClickable(false);
            textEmail.setClickable(true);
            textEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmailRequestDialog();
                }
            });*/
        }

        String phoneNumText = mindbodyClientResponseModel.getMobilePhone();
        if(phoneNumText != "null"){
            textPhoneNum.setText(phoneNumText);
            initPhoneNum.setVisibility(View.GONE);
            initPhoneNum.setClickable(false);
            textPhoneNum.setClickable(true);
            textPhoneNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhoneNumRequestDialog();
                }
            });
        }

        String genderText = mindbodyClientResponseModel.getGender();
        if(genderText != "null"){
            textGender.setText(genderText);
            initGender.setVisibility(View.GONE);
            initGender.setClickable(false);
            textGender.setClickable(true);
            textGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGenderRequestDialog();
                }
            });
        }

        String birthDateText = mindbodyClientResponseModel.getBirthDate();
        if(birthDateText != "null"){
            textBirthDate.setText(birthDateText);
            initBirthDate.setVisibility(View.GONE);
            initBirthDate.setClickable(false);
            textBirthDate.setClickable(true);
            textBirthDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBirthDateRequestDialog();
                }
            });
        }

        String heightText = mindbodyClientResponseModel.getHeight();
        if(heightText != "null"){
            textHeight.setText(heightText);
            initHeight.setVisibility(View.GONE);
            initHeight.setClickable(false);
            textHeight.setClickable(true);
            textHeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHeightRequestDialog();
                }
            });
        }

        String weightText = mindbodyClientResponseModel.getWeight();
        if(weightText != "null"){
            textWeight.setText(weightText);
            initWeight.setVisibility(View.GONE);
            initWeight.setClickable(false);
            textWeight.setClickable(true);
            textWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWeightRequestDialog();
                }
            });
        }

        String postalCodeText = mindbodyClientResponseModel.getPostalCode();
        if(postalCodeText != "null"){
            textPostalCode.setText(postalCodeText);
            initPostalCode.setVisibility(View.GONE);
            initPostalCode.setClickable(false);
            textPostalCode.setClickable(true);
            textPostalCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostalCodeRequestDialog();
                }
            });
        }

        dialog.dismiss();

    }




    public void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.sign_in_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                ProfilePage.this.finish();

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
                        .addOnCompleteListener((Activity) ProfilePage.this, new OnCompleteListener<AuthResult>() {
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

                                                    MindbodyClient mindbodyClient = new MindbodyClient(getApplicationContext());

                                                    mindbodyClient.getUserToken(new MindbodyLocation.VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            mindbodyClient.getClientInfo(new MindbodyLocation.VolleyResponseListener() {
                                                                @Override
                                                                public void onError(String message) {
                                                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                                    Log.d("mindbody_response", response);

                                                                    MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyClient.getMindbodyClientResponseModel();

                                                                    ((GlobalVariableApplication) (Application)getApplicationContext()).setClientId(clientId);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setLogIn(true);


                                                                    dialog.setCancelable(true);

                                                                    initalized_profile(dialog);

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







                                    /*
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);*/
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("login", task.getException().toString());

                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
        });

        dialog.show();
    }


    public void showCreateAccountDialog(String title,Dialog previousDialog){
        final Dialog dialog = new Dialog(ProfilePage.this);

        dialog.setContentView(R.layout.signup_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                            params.put("Email", height_text);


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

                            ArrayList<HashMap<String, Object>> hashMapArrayList = new ArrayList<>();
                            hashMapArrayList.add(params_customClientField);
                            hashMapArrayList.add(params_customClientField2);

                            params.put("CustomClientFields", hashMapArrayList);



                            //params.put("Height", weight_text);
                            //params.put("Weight", firstname_text);
                            params.put("MobilePhone", phoneNum_text);
                            params.put("PostalCode", postalCode_text);


                            firebaseAuth.createUserWithEmailAndPassword(email_text, password_text)
                                    .addOnCompleteListener(ProfilePage.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Log.d("register", "sucessfull");



                                                userID = firebaseAuth.getCurrentUser().getUid();

                                                previousDialog.dismiss();
                                                dialog.setContentView(R.layout.progress_bar);

                                                dialog.setCancelable(false);




                                                DocumentReference documentReference = firebaseFirestore.collection("clientId").document(userID);

                                                MindbodyAddClient mindbodyAddClient = new MindbodyAddClient(getApplicationContext());

                                                mindbodyAddClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                                                    @Override
                                                    public void onError(String message) {
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        mindbodyAddClient.addClient(new MindbodyClass.VolleyResponseListener() {
                                                            @Override
                                                            public void onError(String message) {
                                                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onResponse(String response) {
                                                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                                Log.d("mindbody_response", response);

                                                                String clientId = mindbodyAddClient.getClientId();
                                                                MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyAddClient.getMindbodyClientResponseModel();


                                                                ((GlobalVariableApplication)  (Application)getApplicationContext()).setClientId(clientId);
                                                                ((GlobalVariableApplication)  (Application)getApplicationContext()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                ((GlobalVariableApplication) (Application)getApplicationContext()).setLogIn(true);


                                                                Map<String, Object> user = new HashMap<>();
                                                                user.put("ClientId", clientId);

                                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d("database", "sucuess");

                                                                    }
                                                                });


                                                                //nonSwipeableViewPager.setCurrentItem(currPos);

                                                                dialog.setCancelable(true);
                                                               // dialog.dismiss();
                                                                initalized_profile(dialog);

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
                            Toast.makeText(getApplicationContext(), "password is too short, need more than 6 characters", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "password does not matched", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "check to proceed", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


    public void showAgreementDialog(String title, String text){
        final Dialog dialog = new Dialog(ProfilePage.this);
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


    private void loadingDialog(){
        Dialog dialog = new Dialog(ProfilePage.this);
        dialog.setContentView(R.layout.progress_bar);

        dialog.show();
        initalized_profile(dialog);

    }




}
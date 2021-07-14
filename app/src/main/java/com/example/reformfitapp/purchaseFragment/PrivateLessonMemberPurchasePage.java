package com.example.reformfitapp.purchaseFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.MindbodyAddClient;
import com.example.reformfitapp.MindbodyClass;
import com.example.reformfitapp.MindbodyClient;
import com.example.reformfitapp.MindbodyClientResponseModel;
import com.example.reformfitapp.MindbodyLocation;
import com.example.reformfitapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.round;

public class PrivateLessonMemberPurchasePage extends AppCompatActivity implements View.OnClickListener{
    private TextView purchaseTitle;
    private TextView purchaseInfo1, purchaseInfo2,purchaseInfo3;
    private ImageView infoIcon3;
    private ImageView  checkIcon3;
    private TextView subtotal;
    private TextView taxAmount;
    private TextView total;
    private TextView agreementClickable;
    private CheckBox agreementCheckBox;
    private EditText fullNameInput;
    private MaterialButton confirmButton;
    private boolean confirmedAgreement = false;
    private String purchaseProgramName;

    private float taxRate = (float) 0.13;

    private String clientId;


    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.private_lesson_purchase_page);

        Intent intent = getIntent();

        String purchaseName = intent.getStringExtra("name");
        String purchaseDescription1 = intent.getStringExtra("description1");
        String purchaseDescription2 = intent.getStringExtra("description2");
        String purchaseDescription3 = intent.getStringExtra("description3");
        String price = intent.getStringExtra("price");
        float priceInFloat = Float.parseFloat(price.substring(1));


        purchaseTitle = findViewById(R.id.purchase_title);
        purchaseInfo1 = findViewById(R.id.purchase_info_1);
        purchaseInfo2 = findViewById(R.id.purchase_info_2);
        purchaseInfo3 = findViewById(R.id.purchase_info_3);
        infoIcon3 = findViewById(R.id.info_icon_1_3);
        checkIcon3 = findViewById(R.id.check3);
        agreementClickable = findViewById(R.id.have_read_clickable);
        agreementCheckBox = findViewById(R.id.agreement_checkbox);
        fullNameInput = findViewById(R.id.full_name);
        agreementClickable = findViewById(R.id.have_read_clickable);
        confirmButton = findViewById(R.id.confirm_purchase_button);
        subtotal = findViewById(R.id.first_subtotal);
        taxAmount = findViewById(R.id.first_tax);
        total = findViewById(R.id.overall_total);

        if(((GlobalVariableApplication)getApplication()).getLogIn()){

            clientId = ((GlobalVariableApplication)getApplication()).getClientId();
            

        }
        else{
            showLoginDialog("会员登录");
        }







        purchaseProgramName = purchaseName;
        purchaseTitle.setText(purchaseName);
        purchaseInfo1.setText(purchaseDescription1);
        purchaseInfo2.setText(purchaseDescription2);

        if ( purchaseDescription3.equals("")) {
            purchaseInfo3.setVisibility(View.GONE);
            infoIcon3.setVisibility(View.GONE);
            checkIcon3.setVisibility(View.GONE);
        }
        else{
            purchaseInfo3.setText(purchaseDescription3);
        }


        float tax_amount = calculateTax(priceInFloat,taxRate);
        float total_amount = calculateTotal(priceInFloat,tax_amount);

        String subtotalInString ="$"+ priceInFloat;
        String taxInString = "$"+tax_amount;
        String totalInString = "$"+total_amount;

        subtotal.setText(subtotalInString);
        taxAmount.setText(taxInString);
        total.setText(totalInString);

        agreementClickable.setOnClickListener(this);
        confirmButton.setOnClickListener(this);



    }


    private float calculateTax(float subtotal, float taxRate){

        return (float) (round(subtotal*taxRate*100.0)/100.0);
    }
    private float calculateTotal(float subtotal, float taxAmount){
        return subtotal+taxAmount;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.have_read_clickable:
                showAgreementDialog("Group Training Agreement","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ullamcorper velit sed hendrerit. Suspendisse erat arcu, molestie quis est sed, vehicula luctus tellus. Quisque ultrices non justo nec ultricies. In posuere nisi vel nunc lobortis, ac sollicitudin quam pulvinar. Donec blandit augue id orci vehicula, eget semper est tempus. Integer auctor dictum justo, fringilla suscipit ligula suscipit at. Cras eget suscipit turpis. Maecenas sit amet nisl sagittis, hendrerit metus vitae, ornare purus. Curabitur sem ligula, imperdiet non nunc ut, luctus volutpat est. Suspendisse condimentum felis vitae nibh semper sollicitudin.");
                break;
            case R.id.confirm_purchase_button:

                if (!agreementCheckBox.isChecked()){
                    Toast.makeText(this, "Please check the agreement", Toast.LENGTH_SHORT).show();
                }
                else if (fullNameInput.getText().toString().equals("")){
                    Toast.makeText(this, "Please enter your full name to confirm ", Toast.LENGTH_SHORT).show();
                }
                else {
                    proceedToWebPage();
                }
        }
    }

    private void proceedToWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (purchaseProgramName){
            case "100-Session Pack":
                showPurchaseWebViewActivity("https://checkout.square.site/buy/KNS3QQKFNOC32TGZ2LCBMHR6");
                break;
            case "75-Session Pack":
                showPurchaseWebViewActivity("https://checkout.square.site/merchant/SGVJ1ZDZQ005H/checkout/2MEEAMLUSSRV2PQ6LQVGH4QE");
                break;
            case "50-Session Pack":
                showPurchaseWebViewActivity("https://checkout.square.site/merchant/SGVJ1ZDZQ005H/checkout/2MEEAMLUSSRV2PQ6LQVGH4QE");
                break;
            case "Single Session":
                showPurchaseWebViewActivity("https://google.com");
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    private void showAgreementDialog(String title, String text) {
        final Dialog dialog = new Dialog(PrivateLessonMemberPurchasePage.this);
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

    private void showPurchaseWebViewActivity(String url){
        Intent newIntent = new Intent(this,PrivateLessonPurchaseWebview.class);
        newIntent.putExtra("url",url);
        startActivity(newIntent);

    }
    public void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(PrivateLessonMemberPurchasePage.this){
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP ){

                    Rect r = new Rect(0,0,0,0);
                    this.getWindow().getDecorView().getHitRect(r);
                    boolean intersects = r.contains((int)event.getX(), (int)event.getY());
                    if(!intersects) {
                        this.dismiss();
                        PrivateLessonMemberPurchasePage.this.finish();
                        return true;
                    }

                }
                return false;

            }

        };
        dialog.setContentView(R.layout.sign_in_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);


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
                PrivateLessonMemberPurchasePage.this.finish();

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
                        .addOnCompleteListener((Activity) PrivateLessonMemberPurchasePage.this, new OnCompleteListener<AuthResult>() {
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

                                                    clientId = (String) document.getData().get("ClientId");

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
        final Dialog dialog = new Dialog(PrivateLessonMemberPurchasePage.this);

        dialog.setContentView(R.layout.signup_dialog_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
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
                if (agreement_check_box.isChecked()) {

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
                                    .addOnCompleteListener(PrivateLessonMemberPurchasePage.this, new OnCompleteListener<AuthResult>() {
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

                                                                clientId = mindbodyAddClient.getClientId();
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
                                                                dialog.dismiss();

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


}

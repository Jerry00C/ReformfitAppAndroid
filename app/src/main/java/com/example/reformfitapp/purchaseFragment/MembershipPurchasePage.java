package com.example.reformfitapp.purchaseFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.SaveInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.reformfitapp.ClassInfo;
import com.example.reformfitapp.ClientUpdateElement;
import com.example.reformfitapp.CreditCardInfo;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.round;

public class MembershipPurchasePage extends AppCompatActivity implements View.OnClickListener, PurchaseBottomSheetDialogFragment.OnDataPass{

    private TextView purchaseOptionName;
    private TextView purchaseInfo1;
    private TextView purchaseInfo2;
    private TextView purchaseInfo3;
    private TextView purchaseInfo4;
    private ImageView info_icon_1_3;
    private ImageView info_icon_1_4;
    private ImageView checkmark_4;
    private ImageView checkmark_3;

    private TextView payment_date_title, payment_date_display;
    private ImageView datePicker, addPromoCode, addGiftCard;
    private TextView entered_activation_date, enteredPromoCode, have_read_clickable;

    private ProgressBar grandTotalProgress, promoCodeProgress;

    private TextView recurring_total_textView, recurring_tax_textView, recurring_subtotal_textView, first_total_textView, first_tax_textView, first_subtotal_textView;
    private TextView promoDeductedAmount;
    private MaterialButton confirmButton;

    private ContractData contractData;
    private double first_subtotal;
    private double first_tax;
    private double first_total;
    private double recurring_subtotal;
    private double recurring_tax;
    private double recurring_total;
    private int serviceId;
    private int contractId;

    private Context context = this;

    private String selectedDate;
    private String currentTime;
    private String currentDateTime;

    private String clientId;

    private boolean finishedSetUpPayment;
    private PurchaseSucceededPassBackData passedBackPaymentData;

    private String storedCardLastFour;
    private String storedCardNumber;
    private String storedExpMonth;
    private String storedExpYear;

    private String dDirectBranchingNumber;
    private String dDirectTransitNumber;
    private String dDirectAccountNumber;

    private LinearLayout paymentMethodDisplay;

    private MaterialCardView storedCreditCard;
    private MaterialCardView directDebitDisplayer;

    private Dialog loadingDialog;

    private ContractElement initialContractElement;

    private CheckBox agreementCheckBox;
    private EditText nameInput;

    private PurchaseBottomSheetDialogFragment purchaseBottomSheetDialogFragment;
    MindbodyService mindbodyService;



    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;


    private boolean validForCreatePaymentMethod;
    private boolean debitTypeSelected;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.promo_code_icon:
            case R.id.entered_promo_code:
                showDialog("请输入Promo Code #:",0);
                break;
            case R.id.have_read_clickable:
                showAgreementDialog("Group Training Agreement","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ullamcorper velit sed hendrerit. Suspendisse erat arcu, molestie quis est sed, vehicula luctus tellus. Quisque ultrices non justo nec ultricies. In posuere nisi vel nunc lobortis, ac sollicitudin quam pulvinar. Donec blandit augue id orci vehicula, eget semper est tempus. Integer auctor dictum justo, fringilla suscipit ligula suscipit at. Cras eget suscipit turpis. Maecenas sit amet nisl sagittis, hendrerit metus vitae, ornare purus. Curabitur sem ligula, imperdiet non nunc ut, luctus volutpat est. Suspendisse condimentum felis vitae nibh semper sollicitudin.");

                break;

            case R.id.confirm_purchase_button:

                if (!finishedSetUpPayment) {
                    if(selectedDate.equals("")){

                        Toast.makeText(this, "Please selecte a date",Toast.LENGTH_SHORT).show();
                    }

                    else if (!agreementCheckBox.isChecked()){
                        Toast.makeText(this, "Please check the agreement", Toast.LENGTH_SHORT).show();
                    }
                    else if (nameInput.getText().toString().equals("")){
                        Toast.makeText(this, "Please enter your full name to confirm ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        purchaseBottomSheetDialogFragment = PurchaseBottomSheetDialogFragment.newInstance();

                        purchaseBottomSheetDialogFragment.show(ft, null);
                    }
                }

                else {
                    // set up a final shopping cart to be processed to post request
                    if ( passedBackPaymentData.getPaymentOption()==PurchaseSucceededPassBackData.typeStoredCredit) {
                        constructFinalCart(PurchaseSucceededPassBackData.typeStoredCredit);
                    }
                    else if (passedBackPaymentData.getPaymentOption()==PurchaseSucceededPassBackData.typeDirectDebit){
                        constructFinalCart(PurchaseSucceededPassBackData.typeDirectDebit);
                    }

                    showLoadingBar();
                    mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                        @Override
                        public void onError(String errorMessage) {

                        }

                        @Override
                        public void onResponse(String authToken) {
                            mindbodyService.purchaseContract(new MindbodyService.PurchaseContractListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    Log.d("PaymentError",errorMessage);
                                    stopLoadingBar();
                                }

                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(MembershipPurchasePage.this, "Purchase successful", Toast.LENGTH_SHORT).show();
                                    stopLoadingBar();
                                    finish();
                                }
                            },initialContractElement);
                        }
                    });
                }
            default:
                break;
        }
    }

    @Override
    public void onDataPass(PurchaseSucceededPassBackData data) {
        passedBackPaymentData = data;
        if ( data.getPaymentOption()==PurchaseSucceededPassBackData.typeStoredCredit){

            setUpCreditCardInfoDisplay(data);
            setUpFinalPurchaseButton();

        }

        else if (data.getPaymentOption()==PurchaseSucceededPassBackData.typeDirectDebit){
            setUpDirectDebitInfoDisplay(data);
            setUpFinalPurchaseButton();



        }
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        /*getSupportActionBar().hide(); //hide the title bar*/
        setContentView(R.layout.membership_purchase_page);





        mindbodyService = new MindbodyService(MembershipPurchasePage.this);

        finishedSetUpPayment = false;



        paymentMethodDisplay = findViewById(R.id.chosen_payment_method);
        purchaseOptionName = findViewById(R.id.membership_purchase_title);
        purchaseInfo1 = findViewById(R.id.purchase_info_1);
        purchaseInfo2 = findViewById(R.id.purchase_info_2);
        purchaseInfo3 = findViewById(R.id.purchase_info_3);
        purchaseInfo4 = findViewById(R.id.purchase_info_4);
        info_icon_1_3 = findViewById(R.id.info_icon_1_3);
        info_icon_1_4 = findViewById(R.id.info_icon_1_4);
        checkmark_3 = findViewById(R.id.checkmark3);
        checkmark_4 = findViewById(R.id.checkmark4);
        first_subtotal_textView = findViewById(R.id.first_subtotal);
        first_tax_textView = findViewById(R.id.first_tax);
        first_total_textView = findViewById(R.id.first_total);
        recurring_subtotal_textView = findViewById(R.id.recurring_subtotal);
        recurring_tax_textView = findViewById(R.id.recurring_tax);
        recurring_total_textView = findViewById(R.id.recurring_total);
        promoDeductedAmount = findViewById(R.id.promo_deducted_amount);
        confirmButton = findViewById(R.id.confirm_purchase_button);
        agreementCheckBox = findViewById(R.id.agreement_checkbox);
        nameInput = findViewById(R.id.full_name);

        Intent intent = getIntent();

        String purchaseName = intent.getStringExtra("membership_name");
        String description1 = intent.getStringExtra("description1");
        String description2 = intent.getStringExtra("description2");
        String description3 = intent.getStringExtra("description3");
        String description4 = intent.getStringExtra("description4");
        contractData = intent.getParcelableExtra("contract_data");

        Log.d("contract_data", contractData.toString());


        first_subtotal = roundToTwoDecimalPlace(contractData.getFirst_payment_subtotal());
        Log.d("display_issue", String.valueOf(contractData.getFirst_payment_subtotal()));
        first_tax = roundToTwoDecimalPlace(contractData.getFirst_payment_tax());
        first_total = roundToTwoDecimalPlace(contractData.getFirst_payment_total());
        recurring_subtotal = roundToTwoDecimalPlace(contractData.getRecurring_payment_subtotal());
        recurring_tax = roundToTwoDecimalPlace(contractData.getRecurring_payment_tax());
        recurring_total = roundToTwoDecimalPlace(contractData.getRecurring_payment_total());
        String fSubtotalS = "$"+first_subtotal;
        String fTaxS = "$"+first_tax;
        String fTotalS = "$"+first_total;
        String rSubTotalsS = "$"+recurring_subtotal;
        String rTaxS = "$"+recurring_tax;
        String rTotalS = "$"+recurring_total;

        first_subtotal_textView.setText(fSubtotalS);
        first_tax_textView.setText(fTaxS);
        first_total_textView.setText(fTotalS);
        recurring_subtotal_textView.setText(rSubTotalsS);
        recurring_tax_textView.setText(rTaxS);
        recurring_total_textView.setText(rTotalS);


        purchaseOptionName.setText(purchaseName);
        purchaseInfo1.setText(description1);
        purchaseInfo2.setText(description2);

        if ( description3.equals("")) {
            purchaseInfo3.setVisibility(View.GONE);
            info_icon_1_3.setVisibility(View.GONE);
            checkmark_3.setVisibility(View.GONE);
        }
        else{
            purchaseInfo3.setText(description3);
        }


        if (description4.equals("")){
            purchaseInfo4.setVisibility(View.GONE);
            info_icon_1_4.setVisibility(View.GONE);
            checkmark_4.setVisibility(View.GONE);
        }
        else{
            purchaseInfo4.setText(description4);
        }
        //////////////////////////////

        debitTypeSelected = false;
        /////////////////// date picker
        datePicker = findViewById(R.id.activation_date_icon);
        entered_activation_date = findViewById(R.id.entered_activation_date);

        selectedDate = "";

        final MaterialDatePicker<Long> materialDatePicker = initializeDatePicker();

//        MaterialDateRan
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat onlyDate = new SimpleDateFormat("dd");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                onlyDate.setTimeZone(TimeZone.getTimeZone("GMT"));


                selectedDate = simpleDateFormat.format(selection);
                payment_date_title = findViewById(R.id.payment_start_date);
                payment_date_display = findViewById(R.id.entered_payment_start_date);
                payment_date_title.setVisibility(View.VISIBLE);
                payment_date_display.setVisibility(View.VISIBLE);
                String dayInMonth = onlyDate.format(selection);
                dayInMonth = dayInMonth.replace("0","");
                String messageDate = "每月第"+dayInMonth+"天";
                payment_date_display.setText(messageDate);

                entered_activation_date.setText(materialDatePicker.getHeaderText());

                Log.d("Selection",selection.toString());
                Log.d("Selection",selectedDate);
            }
        });

        ////////////// pop up active button



        ///////////////////promo code button
        addPromoCode = findViewById(R.id.promo_code_icon);
        enteredPromoCode = findViewById(R.id.entered_promo_code);
        addPromoCode.setOnClickListener(this);
        enteredPromoCode.setOnClickListener(this);

        /////////
        have_read_clickable = findViewById(R.id.have_read_clickable);
        have_read_clickable.setOnClickListener(this);





        confirmButton.setOnClickListener(this);

        if(((GlobalVariableApplication)getApplication()).getLogIn()){

            clientId = ((GlobalVariableApplication)getApplication()).getClientId();

        }
        else{
            showLoginDialog("会员登录");
        }









    }




    public void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(MembershipPurchasePage.this){
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP ){

                    Rect r = new Rect(0,0,0,0);
                    this.getWindow().getDecorView().getHitRect(r);
                    boolean intersects = r.contains((int)event.getX(), (int)event.getY());
                    if(!intersects) {
                        this.dismiss();
                        MembershipPurchasePage.this.finish();
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
                MembershipPurchasePage.this.finish();

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
                        .addOnCompleteListener((Activity) MembershipPurchasePage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "success");

                                    dialog.setContentView(R.layout.progress_bar);
                                    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
                                                    Log.d("client Id",clientId);

                                                    MindbodyClient mindbodyClient = new MindbodyClient(getApplicationContext());

                                                    mindbodyClient.getUserToken(new MindbodyLocation.VolleyResponseListener() {
                                                        @Override
                                                        public void onError(String message) {
                                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();


                                                            dialog.setCancelable(true);
                                                            dialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            mindbodyClient.getClientInfo(new MindbodyLocation.VolleyResponseListener() {
                                                                @Override
                                                                public void onError(String message) {
                                                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();


                                                                    dialog.setCancelable(true);
                                                                    dialog.dismiss();
                                                                }

                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                                                    Log.d("mindbody_response", response);

                                                                    MindbodyClientResponseModel mindbodyClientResponseModel = mindbodyClient.getMindbodyClientResponseModel();

                                                                    ((GlobalVariableApplication) (Application)getApplicationContext()).setClientId(clientId);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                                                                    ((GlobalVariableApplication)  (Application)getApplicationContext()).setLogIn(true);


                                                                    contractId = contractData.getContract_id();
                                                                    serviceId = contractData.getServiceId();

                                                                    initialContractElement = new ContractElement();
                                                                    initialContractElement.setClientId(clientId);
                                                                    initialContractElement.setContractId(contractId);
                                                                    initialContractElement.setLocation(1);
                                                                    initialContractElement.setTest(false);








                                                                    dialog.setCancelable(true);
                                                                    dialog.dismiss();
                                                                }
                                                            }, clientId);
                                                        }
                                                    });

                                                    Log.d("response", "DocumentSnapshot data: " + document.getData().get("ClientId"));
                                                    Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();

                                                    dialog.setCancelable(true);
                                                    dialog.dismiss();


                                                } else {
                                                    Log.d("response", "No such document");


                                                    Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();

                                                    dialog.setCancelable(true);
                                                    dialog.dismiss();
                                                }
                                            } else {
                                                Log.d("response", "get failed with ", task.getException());

                                                Toast.makeText(getApplicationContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();

                                                dialog.setCancelable(true);
                                                dialog.dismiss();
                                            }
                                        }
                                    });
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
        final Dialog dialog = new Dialog(MembershipPurchasePage.this);

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
                                    .addOnCompleteListener(MembershipPurchasePage.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Log.d("register", "sucessfull");



                                                userID = firebaseAuth.getCurrentUser().getUid();

                                                previousDialog.dismiss();
                                                dialog.setContentView(R.layout.progress_bar);
                                                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

                                                dialog.setCancelable(false);




                                                DocumentReference documentReference = firebaseFirestore.collection("clientId").document(userID);

                                                MindbodyAddClient mindbodyAddClient = new MindbodyAddClient(getApplicationContext());

                                                mindbodyAddClient.getUserToken(new MindbodyClass.VolleyResponseListener() {
                                                    @Override
                                                    public void onError(String message) {
                                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_SHORT).show();
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        user.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.d("firebase", "User account deleted.");
                                                                        }
                                                                    }
                                                                });


                                                        dialog.setCancelable(true);
                                                        dialog.dismiss();



                                                    }

                                                    @Override
                                                    public void onResponse(String response) {
                                                        mindbodyAddClient.addClient(new MindbodyClass.VolleyResponseListener() {
                                                            @Override
                                                            public void onError(String message) {
                                                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_SHORT).show();
                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                                user.delete()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Log.d("firebase", "User account deleted.");
                                                                                }
                                                                            }
                                                                        });

                                                                dialog.setCancelable(true);
                                                                dialog.dismiss();

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

                                                                contractId = contractData.getContract_id();
                                                                serviceId = contractData.getServiceId();

                                                                initialContractElement = new ContractElement();
                                                                initialContractElement.setClientId(clientId);
                                                                initialContractElement.setContractId(contractId);
                                                                initialContractElement.setLocation(1);
                                                                initialContractElement.setTest(false);

                                                                dialog.setCancelable(true);
                                                                dialog.dismiss();

                                                            }
                                                        }, params);
                                                    }
                                                });

                                            }
                                            else{
                                                Log.d("register", task.getException().toString());
                                                if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.")){

                                                    Toast.makeText(getApplicationContext(), "The email address is badly formatted", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "Something wrong, try again", Toast.LENGTH_LONG).show();

                                                }
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
        final Dialog dialog = new Dialog(MembershipPurchasePage.this);
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


    public void showDialog(String title, int type){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.promo_gift_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);

        popupTitle.setText(title);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputEditText inputText = dialog.findViewById(R.id.input_text);
                String enteredCode = inputText.getText().toString();

                if (!enteredCode.equals("")) {

                    initializeProgressBars();
                    ShoppingCartElement cartElement =ShoppingCartElement.generateTestShoppingCartElement(enteredCode,serviceId,clientId);

                    mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String authToken) {
                            Toast.makeText(MembershipPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();

                            mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    String actualPrice = getActualPriceByErrorMessage(errorMessage);

                                    if (actualPrice!=null){
                                        validPromoCodeDisplay(enteredCode,actualPrice);
                                    }
                                    else{
                                        Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                        Log.d("shoppingCart",errorMessage);
                                        setProgressBarsInvisible();
                                    }
                                }

                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            },cartElement);


                        }
                    });

                }
                else {
                    resetOriginalPriceDisplay();
                }

                dialog.dismiss();
            }
        });

        dialog.show();


    }




    void sentToEmail(){}

    private void setUpCreditCardInfoDisplay(PurchaseSucceededPassBackData data) {
        storedCardLastFour = data.getStoredCreditInfo().getLastFour();
        storedCardNumber = data.getStoredCreditInfo().getCardNumber();
        storedExpMonth = data.getStoredCreditInfo().getExpMonth();
        storedExpYear = data.getStoredCreditInfo().getExpYear();

        paymentMethodDisplay.setVisibility(View.VISIBLE);
        storedCreditCard = (MaterialCardView) getLayoutInflater().inflate(R.layout.stored_credit_card_info_display,null);

        TextView cardNumberDisplayer = storedCreditCard.findViewById(R.id.info_first_display);
        cardNumberDisplayer.setText(storedCardNumber);

        TextView dateDisplayer = storedCreditCard.findViewById(R.id.info_second_display);
        String yearMonth = storedExpYear+"/"+storedExpMonth;
        dateDisplayer.setText(yearMonth);

        ImageView payment_modifier = storedCreditCard.findViewById(R.id.payment_modifier);
        payment_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateCreditCard("请输入借记卡信息");
            }
        });
        paymentMethodDisplay.addView(storedCreditCard);
    }

    private void setUpDirectDebitInfoDisplay(PurchaseSucceededPassBackData data) {
        dDirectBranchingNumber = data.getDirectDebitInfo().getBranchNumber();
        dDirectTransitNumber = data.getDirectDebitInfo().getTransitNumber();
        dDirectAccountNumber = data.getDirectDebitInfo().getAccountNumber();

        paymentMethodDisplay.setVisibility(View.VISIBLE);
        directDebitDisplayer = (MaterialCardView) getLayoutInflater().inflate(R.layout.direct_debit_info_display,null);

        TextView branchingNumberDisplay = directDebitDisplayer.findViewById(R.id.info_third_display);
        branchingNumberDisplay.setText(dDirectBranchingNumber);

        TextView transitNumberDisplay = directDebitDisplayer.findViewById(R.id.info_fourth_display);
        transitNumberDisplay.setText(dDirectTransitNumber);

        TextView accountNumberDisplay = directDebitDisplayer.findViewById(R.id.info_fifth_display);
        accountNumberDisplay.setText(dDirectAccountNumber);

        ImageView payment_modifier = directDebitDisplayer.findViewById(R.id.payment_modifier);
        payment_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDebitCard("请输入借记卡信息");
            }
        });
        paymentMethodDisplay.addView(directDebitDisplayer);

    }

    private void setUpFinalPurchaseButton(){
        finishedSetUpPayment = true ;
        MaterialButton confirmButton = findViewById(R.id.confirm_purchase_button);
        confirmButton.setText(R.string.ppp_activity_proceed_to_purchase);
    }


    private double roundToTwoDecimalPlace(double amount){
        return round(amount*100.0)/100.0;
    }

    private void initializeProgressBars(){

        grandTotalProgress = findViewById(R.id.grand_total_progress_bar);
        promoCodeProgress = findViewById(R.id.promo_code_progress_bar);
        grandTotalProgress.setVisibility(View.VISIBLE);
        promoCodeProgress.setVisibility(View.VISIBLE);
    }

    @NotNull
    private MaterialDatePicker<Long> initializeDatePicker(){
        /////////////////////////// get long int value of date 6 month away from today in milliseconds
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar  = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.setTimeInMillis(today);
        calendar.add(Calendar.MONTH, 6);
        long halfYear = calendar.getTimeInMillis();


        /////////////////////////// make a materialDatePicker with calendar constraints
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.now();
        CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(halfYear);

//        CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(halfYear);
//        CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.now();

        ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
        listOfValidators.add(dateValidator_start);
        listOfValidators.add(dateValidator_end);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
        constraintsBuilder.setValidator(validators);
        builder.setCalendarConstraints(constraintsBuilder.build());
        builder.setTitleText("选择会员激活日期");
        builder.setTheme(R.style.MaterialCalendarTheme);

        return builder.build();
    }

    private String getActualPriceByErrorMessage(String errorMessage){
        Pattern p = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher m = p.matcher(errorMessage);
        String actualPrice = null;
        while(m.find()){
            actualPrice = m.group();
        }
        return actualPrice;
    }

    private void validPromoCodeDisplay(String enteredCode,String actualPrice){
        String subtotalS = "$"+actualPrice;
        first_total_textView.setText(subtotalS);
        enteredPromoCode.setText(enteredCode);
        enteredPromoCode.setVisibility(View.VISIBLE);
        addPromoCode.setVisibility(View.INVISIBLE);
        setProgressBarsInvisible();
        float deducted_amount =(float)first_total- Float.parseFloat(actualPrice);
        String deductedS = "-$"+round(deducted_amount*100.0)/100.0;
        promoDeductedAmount.setText(deductedS);
        promoDeductedAmount.setVisibility(View.VISIBLE);

    }

    private void setProgressBarsInvisible() {
        promoCodeProgress.setVisibility(View.INVISIBLE);
        grandTotalProgress.setVisibility(View.INVISIBLE);
    }

    private void resetOriginalPriceDisplay(){
        String subtotalS = "$"+first_total;
        first_total_textView.setText(subtotalS);
        enteredPromoCode.setText("");
        enteredPromoCode.setVisibility(View.GONE);

        promoDeductedAmount.setText("");
        promoDeductedAmount.setVisibility(View.GONE);


        addPromoCode.setVisibility(View.VISIBLE);

    }

//    public void showCreateDebitCard(String title){
//        final Dialog dialog = new Dialog(MembershipPurchasePage.this);
//        dialog.setContentView(R.layout.debit_card_registration_pop_up);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
//        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
//        TextView popupTitle = dialog.findViewById(R.id.popup_title);
//
//        popupTitle.setText(title);
//
//        RadioButton checking = dialog.findViewById(R.id.checking);
//        RadioButton saving = dialog.findViewById(R.id.saving);
//        TextInputEditText branch_number = dialog.findViewById(R.id.input_text_branch_number);
//        TextInputEditText transit_number = dialog.findViewById(R.id.input_text_transit_number);
//        TextInputEditText account_number = dialog.findViewById(R.id.input_text_account_number);
//        TextInputEditText client_name = dialog.findViewById(R.id.input_text_name);
//
//
//
//
//
//
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
//                    @Override
//                    public void onError(String errorMessage) {
//                        Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(String authToken) {
//                        Toast.makeText(MembershipPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();
//                        String accountType = new String();
//                        if (checking.isChecked()){
//                            accountType = "Checking";
//
//                        }
//                        else if (saving.isChecked()){
//                            accountType = "Savings";
//                        }
//
//                        HashMap<String,Object> params = new HashMap<>();
//                        params.put("Test",false);
//                        params.put("ClientId",clientId);
//                        params.put("NameOnAccount",client_name.getText().toString());
//                        params.put("RoutingNumber",branch_number.getText().toString()+transit_number.getText().toString());
//                        params.put("AccountNumber",account_number.getText().toString());
//                        params.put("AccountType", accountType);
//                        mindbodyService.postAddClientDirectDebit(new MindbodyService.AddClientDirectDebitInfoListener() {
//                            @Override
//                            public void onError(String errorMessage) {
//                                Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//
//                                String branchNumber = branch_number.getText().toString();
//                                String transitNumber = transit_number.getText().toString();
//                                String accountNumber = account_number.getText().toString();
//
//                                dDirectBranchingNumber = branchNumber;
//                                dDirectTransitNumber = transitNumber;
//                                dDirectAccountNumber =accountNumber;
//                                dialog.dismiss();
//
//                            }
//                        },params);
//
//
//
//
//
//                    }
//                });
//
//            }
//        });
//
//        dialog.show();
//
//
//    }

    public void showCreateDebitCard(String title){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.debit_card_registration_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);


        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);

        popupTitle.setText(title);

        RadioGroup buttons = dialog.findViewById(R.id.buttons);
        RadioButton checking = dialog.findViewById(R.id.checking);
        RadioButton saving = dialog.findViewById(R.id.saving);
        EditText branch_number = dialog.findViewById(R.id.input_text_branch_number);
        EditText transit_number = dialog.findViewById(R.id.input_text_transit_number);
        EditText account_number = dialog.findViewById(R.id.input_text_account_number);
        EditText client_name = dialog.findViewById(R.id.input_text_name);

        TextView branchWarning = dialog.findViewById(R.id.branching_warning);
        TextView transitWarning = dialog.findViewById(R.id.transit_warning);
        TextView accountWarning = dialog.findViewById(R.id.account_warning);
        TextView nameWarning = dialog.findViewById(R.id.name_warning);
        TextView typeWarning = dialog.findViewById(R.id.type_warning);


        branch_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    branchNumberChecker(branch_number,branchWarning);

                }
            }
        });

        transit_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (transitWarning.getVisibility()== View.VISIBLE && !transitWarning.getText().toString().isEmpty()){
                    transitWarning.setVisibility(View.GONE);
                }
            }
        });
        account_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (accountWarning.getVisibility()== View.VISIBLE && !accountWarning.getText().toString().isEmpty()){
                    accountWarning.setVisibility(View.GONE);
                }
            }
        });
        client_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nameWarning.getVisibility()== View.VISIBLE && !nameWarning.getText().toString().isEmpty()){
                    nameWarning.setVisibility(View.GONE);
                }
            }
        });

        buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                debitTypeSelected = true;
                if (typeWarning.getVisibility()== View.VISIBLE){
                    typeWarning.setVisibility(View.GONE);
                }
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

                if(branch_number.getText().toString().equals("")){
                    validForCreatePaymentMethod = false;
                    branchWarning.setVisibility(View.VISIBLE);
                    branchWarning.setText("请输入此选项");
                }
                if( transit_number.getText().toString().equals("") ){

                    validForCreatePaymentMethod = false;
                    transitWarning.setVisibility(View.VISIBLE);
                    transitWarning.setText("请输入此选项");


                };
                if(account_number.getText().toString().equals("")){
                    validForCreatePaymentMethod = false;
                    accountWarning.setVisibility(View.VISIBLE);
                    accountWarning.setText("请输入此选项");

                }

                if(client_name.getText().toString().equals("")){
                    validForCreatePaymentMethod = false;
                    nameWarning.setVisibility(View.VISIBLE);
                    nameWarning.setText("请输入此选项");

                }

                if (!debitTypeSelected){
                    validForCreatePaymentMethod = false;
                    typeWarning.setVisibility(View.VISIBLE);
                    typeWarning.setText("请输入此选项");
                }
                if(validForCreatePaymentMethod) {
                    showLoadingBar();
                    mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String authToken) {
                            Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();
                            String accountType = new String();
                            if (checking.isChecked()) {
                                accountType = "Checking";

                            } else if (saving.isChecked()) {
                                accountType = "Savings";
                            }

                            HashMap<String, Object> params = new HashMap<>();
                            params.put("Test", false);
                            params.put("ClientId", clientId);
                            params.put("NameOnAccount", client_name.getText().toString());
                            params.put("RoutingNumber", branch_number.getText().toString() + transit_number.getText().toString());
                            params.put("AccountNumber", account_number.getText().toString());
                            params.put("AccountType", accountType);
                            mindbodyService.postAddClientDirectDebit(new MindbodyService.AddClientDirectDebitInfoListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(JSONObject response) {

                                    String branchNumber = branch_number.getText().toString();
                                    String transitNumber = transit_number.getText().toString();
                                    String accountNumber = account_number.getText().toString();

                                    dDirectBranchingNumber = branchNumber;
                                    dDirectTransitNumber = transitNumber;
                                    dDirectAccountNumber =accountNumber;
                                    stopLoadingBar();
                                    dialog.dismiss();

                                }
                            }, params);


                        }
                    });
                }

            }
        });


        dialog.show();


    }

    private void branchNumberChecker(EditText input, TextView message) {

        if(!input.getText().toString().equals("")){
            if(input.getText().toString().length()< 5 ){
                message.setVisibility(View.VISIBLE);
                message.setText(R.string.ppp_activity_dialog_enter_valid_branchn);
                validForCreatePaymentMethod = false;
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")){
                            if(s.toString().length()<5 ){
                                message.setVisibility(View.VISIBLE);
                                message.setText(R.string.ppp_activity_dialog_enter_valid_branchn);
                                validForCreatePaymentMethod = false;

                            }
                            else{
                                message.setVisibility(View.GONE);
                                validForCreatePaymentMethod = true;
                            }
                        }
                    }
                });


            }
            else{
                message.setVisibility(View.GONE);
                validForCreatePaymentMethod = true;
            }
        }
    }
//    public void showCreateCreditCard(String title){
//        final Dialog dialog = new Dialog(MembershipPurchasePage.this);
//        dialog.setContentView(R.layout.credit_card_registration_pop_up);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
//        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
//
//
//        TextView popupTitle = dialog.findViewById(R.id.popup_title);
//        TextInputEditText credit_card_number = dialog.findViewById(R.id.input_text_credit_card);
//        TextInputEditText year = dialog.findViewById(R.id.input_text_date_valid);
//        TextInputEditText month = dialog.findViewById(R.id.input_text_cvv);
//        TextInputEditText client_name = dialog.findViewById(R.id.input_text_name);
//        TextInputEditText address = dialog.findViewById(R.id.input_text_address);
//        TextInputEditText type = dialog.findViewById(R.id.input_text_line_2);
//        TextInputEditText city = dialog.findViewById(R.id.input_text_city);
//        TextInputEditText state = dialog.findViewById(R.id.input_text_province);
//        TextInputEditText postal_code = dialog.findViewById(R.id.input_text_postal_code);
//
//
//
//
//        popupTitle.setText(title);
//
//        //cardNumber = "";
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
//                    @Override
//                    public void onError(String errorMessage) {
//                        Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(String authToken) {
//                        Toast.makeText(MembershipPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();
//
//                        CreditCardInfo creditCardInfo = new CreditCardInfo(
//                                credit_card_number.getText().toString(),
//                                Integer.parseInt(year.getText().toString()),
//                                Integer.parseInt(month.getText().toString()),
//                                type.getText().toString(),
//                                client_name.getText().toString(),
//                                address.getText().toString(),
//                                city.getText().toString(),
//                                state.getText().toString(),
//                                postal_code.getText().toString()
//                        );
//
//                        ClientUpdateElement clientUpdateElement = new ClientUpdateElement(
//                                creditCardInfo.toHashMap_update(),
//                                clientId,
//                                false,
//                                false
//
//                        );
//
//                        mindbodyService.postUpdateClientCreditCard(new MindbodyService.UpdateClientCreditCardListener(){
//                            @Override
//                            public void onError(String errorMessage) {
//                                Toast.makeText(MembershipPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Toast.makeText(MembershipPurchasePage.this, "Succeed", Toast.LENGTH_SHORT).show();
//                                String lastFour = "";
//                                try {
//                                    JSONObject creditCardInfo = response.getJSONObject("ClientCreditCard");
//                                    lastFour = creditCardInfo.getString("LastFour");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                String cardNumber =  credit_card_number.getText().toString();
//                                String expYear = year.getText().toString();
//                                String expMonth = month.getText().toString();
//
//
//                                storedCardLastFour = lastFour;
//                                storedCardNumber = cardNumber;
//                                storedExpMonth = expMonth;
//                                storedExpYear = expYear;
//
//
//                                TextView cardNumberDisplayer = storedCreditCard.findViewById(R.id.info_first_display);
//                                storedCardNumber = replaceByStar(storedCardNumber);
//                                cardNumberDisplayer.setText(storedCardNumber);
//
//                                TextView dateDisplayer = storedCreditCard.findViewById(R.id.info_second_display);
//                                dateDisplayer.setText(storedExpYear+"/"+storedExpMonth);
//
//                                dialog.dismiss();
//
//                            }
//                        },clientUpdateElement.toHashmap());
//                    }
//                });
//
//            }
//        });
//
//        dialog.show();
//
//
//    }
    public void showCreateCreditCard(String title){
    final Dialog dialog = new Dialog(context);
    dialog.setContentView(R.layout.credit_card_registration_pop_up);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity)context).getWindowManager()
            .getDefaultDisplay()
            .getMetrics(displayMetrics);
    int height = displayMetrics.heightPixels;
    int width = displayMetrics.widthPixels;

    dialog.getWindow().setLayout((int) ((int)width*0.8), WindowManager.LayoutParams.WRAP_CONTENT);


    MaterialButton apply = dialog.findViewById(R.id.confirm_button);
    MaterialButton cancel = dialog.findViewById(R.id.cancel_button);


    TextView popupTitle = dialog.findViewById(R.id.popup_title);
    EditText credit_card_number = dialog.findViewById(R.id.input_text_credit_card);
    EditText year = dialog.findViewById(R.id.input_text_date_valid);
    EditText month = dialog.findViewById(R.id.input_text_cvv);
    EditText client_name = dialog.findViewById(R.id.input_text_name);
    EditText address = dialog.findViewById(R.id.input_text_address);
    Spinner type = dialog.findViewById(R.id.input_text_line_2);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
            R.array.credit_card_type_list, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    type.setAdapter(adapter);
    EditText city = dialog.findViewById(R.id.input_text_city);
    EditText state = dialog.findViewById(R.id.input_text_province);
    EditText postal_code = dialog.findViewById(R.id.input_text_postal_code);

    TextView creditCardWarning = dialog.findViewById(R.id.card_number_warning);
    TextView yearWarning = dialog.findViewById(R.id.year_warning);
    TextView monthWarning = dialog.findViewById(R.id.month_warning);
    TextView nameWarning = dialog.findViewById(R.id.name_warning);
    TextView addressWarning = dialog.findViewById(R.id.address_warning);
    TextView cityWarning = dialog.findViewById(R.id.city_warning);
    TextView stateWarning = dialog.findViewById(R.id.province_warning);
    TextView postalWarning = dialog.findViewById(R.id.postal_warning);




    credit_card_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                creditCardNumberChecker(credit_card_number,creditCardWarning);

            }
        }
    });

    year.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                yearChecker(year,yearWarning);

            }
        }
    });

    month.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus){
                monthChecker(month,monthWarning);

            }
        }
    });

    client_name.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (nameWarning.getVisibility()== View.VISIBLE && !nameWarning.getText().toString().isEmpty()){
                nameWarning.setVisibility(View.GONE);
            }
        }
    });

    address.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (addressWarning.getVisibility()== View.VISIBLE && !addressWarning.getText().toString().isEmpty()){
                addressWarning.setVisibility(View.GONE);
            }
        }
    });
    city.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (cityWarning.getVisibility()== View.VISIBLE && !cityWarning.getText().toString().isEmpty()){
                cityWarning.setVisibility(View.GONE);
            }
        }
    });
    state.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (stateWarning.getVisibility()== View.VISIBLE && !stateWarning.getText().toString().isEmpty()){
                stateWarning.setVisibility(View.GONE);
            }
        }
    });
    postal_code.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (postalWarning.getVisibility()== View.VISIBLE && !postalWarning.getText().toString().isEmpty()){
                postalWarning.setVisibility(View.GONE);
            }
        }
    });


    popupTitle.setText(title);

        //cardNumber = "";

    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

    apply.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if( credit_card_number.getText().toString().equals("") ){

                validForCreatePaymentMethod = false;
                creditCardWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");

            };
            if(year.getText().toString().equals("")){
                validForCreatePaymentMethod = false;
                yearWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");

            }
            if(month.getText().toString().equals("")){
                validForCreatePaymentMethod = false;
                monthWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");
            }
            if( client_name.getText().toString().equals("") ){

                validForCreatePaymentMethod = false;
                nameWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");

            };
            if(address.getText().toString().equals("")){
                validForCreatePaymentMethod = false;
                addressWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");

            }
            if(city.getText().toString().equals("")){
                validForCreatePaymentMethod = false;
                cityWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");
            }
            if( state.getText().toString().equals("") ){

                validForCreatePaymentMethod = false;
                stateWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");


            };
            if(postal_code.getText().toString().equals("")){
                validForCreatePaymentMethod = false;
                postalWarning.setVisibility(View.VISIBLE);
                creditCardWarning.setText("请输入此选项");

            }

            if (validForCreatePaymentMethod) {
                showLoadingBar();
                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String authToken) {
                        Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();

                        CreditCardInfo creditCardInfo = new CreditCardInfo(
                                credit_card_number.getText().toString(),
                                Integer.parseInt(year.getText().toString()),
                                Integer.parseInt(month.getText().toString()),
                                type.getSelectedItem().toString(),
                                client_name.getText().toString(),
                                address.getText().toString(),
                                city.getText().toString(),
                                state.getText().toString(),
                                postal_code.getText().toString()
                        );

                        ClientUpdateElement clientUpdateElement = new ClientUpdateElement(
                                creditCardInfo.toHashMap_update(),
                                clientId,
                                false,
                                false

                        );

                        mindbodyService.postUpdateClientCreditCard(new MindbodyService.UpdateClientCreditCardListener() {
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
                                String lastFour = "";
                                try {
                                    JSONObject client = response.getJSONObject("Client");
                                    JSONObject creditCardInfo = client.getJSONObject("ClientCreditCard");
                                    lastFour = creditCardInfo.getString("LastFour");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String cardNumber = credit_card_number.getText().toString();
                                String expYear = year.getText().toString();
                                String expMonth = month.getText().toString();

/////////////////////////////////////// different from the one in bottom sheet dialog fragment
                                storedCardLastFour = lastFour;
                                storedCardNumber = cardNumber;
                                storedExpMonth = expMonth;
                                storedExpYear = expYear;


                                TextView cardNumberDisplayer = storedCreditCard.findViewById(R.id.info_first_display);
                                storedCardNumber = replaceByStar(storedCardNumber);
                                cardNumberDisplayer.setText(storedCardNumber);

                                TextView dateDisplayer = storedCreditCard.findViewById(R.id.info_second_display);
                                String yearMonth = storedExpYear+"/"+storedExpMonth;
                                dateDisplayer.setText(yearMonth);
                                ///////////////////////////////////////////////////////////

                                stopLoadingBar();
                                dialog.dismiss();


                            }
                        }, clientUpdateElement.toHashmap());
                    }
                });

            }
            else{
                Toast.makeText(context, "Please enter valid fields", Toast.LENGTH_SHORT).show();
            }
        }
    });

    dialog.show();


}

    private void monthChecker(EditText input, TextView message) {
        if(!input.getText().toString().equals("")){
            if(Integer.parseInt(input.getText().toString()) >12 || Integer.parseInt(input.getText().toString())<1 ){
                message.setVisibility(View.VISIBLE);
                message.setText(R.string.ppp_activity_dialog_enter_valid_month);
                validForCreatePaymentMethod = false;
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")){
                            if(Integer.parseInt(input.getText().toString()) >12 || Integer.parseInt(input.getText().toString())<1 ){
                                message.setVisibility(View.VISIBLE);
                                message.setText(R.string.ppp_activity_dialog_enter_valid_month);
                                validForCreatePaymentMethod = false;

                            }
                            else{
                                message.setVisibility(View.GONE);
                                validForCreatePaymentMethod = true;
                            }
                        }
                    }
                });


            }
            else{
                message.setVisibility(View.GONE);
                validForCreatePaymentMethod = true;
            }
        }
    }

    private void yearChecker(EditText input, TextView message) {

        if(!input.getText().toString().equals("")){
            if(Integer.parseInt(input.getText().toString()) < Calendar.getInstance().get(Calendar.YEAR)){
                message.setVisibility(View.VISIBLE);
                message.setText(R.string.ppp_activity_dialog_enter_valid_year);
                validForCreatePaymentMethod = false;
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")){
                            if(Integer.parseInt(input.getText().toString()) < Calendar.getInstance().get(Calendar.YEAR)){
                                message.setVisibility(View.VISIBLE);
                                message.setText(R.string.ppp_activity_dialog_enter_valid_year);
                                validForCreatePaymentMethod = false;

                            }
                            else{
                                message.setVisibility(View.GONE);
                                validForCreatePaymentMethod = true;
                            }
                        }
                    }
                });


            }
            else{
                message.setVisibility(View.GONE);
                validForCreatePaymentMethod = true;
            }
        }
    }


    public void creditCardNumberChecker(EditText input, TextView message){
        if(!input.getText().toString().equals("")){
            if(input.getText().toString().length()< 16 || input.getText().toString().length()> 19  ){
                message.setVisibility(View.VISIBLE);
                message.setText(R.string.ppp_activity_dialog_enter_valid_number);
                validForCreatePaymentMethod = false;
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!s.toString().equals("")){
                            if(s.toString().length()< 16 ||s.toString().length()> 19 ){
                                message.setVisibility(View.VISIBLE);
                                message.setText(R.string.ppp_activity_dialog_enter_valid_number);
                                validForCreatePaymentMethod = false;

                            }
                            else{
                                message.setVisibility(View.GONE);
                                validForCreatePaymentMethod = true;
                            }
                        }
                    }
                });


            }
            else{
                message.setVisibility(View.GONE);
                validForCreatePaymentMethod = true;
            }
        }

    }

    public String replaceByStar(String str) {
        return  "************"+ str.substring(str.length()-4);
    }

    public void showLoadingBar() {
        loadingDialog = new Dialog(MembershipPurchasePage.this);
        loadingDialog.setContentView(R.layout.progress_bar);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }
    public void stopLoadingBar(){
        loadingDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void constructFinalCart(int cardType) {

        if (cardType == PurchaseSucceededPassBackData.typeStoredCredit){
            initialContractElement.setStoredLastFour(storedCardLastFour);
            selectedDate = selectedDate+"T"+java.time.LocalTime.now();
            initialContractElement.setStartDate(selectedDate);
            initialContractElement.setFirstPaymentOccurs("StartDate");


        }
        else if (cardType == PurchaseSucceededPassBackData.typeDirectDebit){
            initialContractElement.setUseDirectDebit(true);
            selectedDate = selectedDate+"T"+java.time.LocalTime.now();
            initialContractElement.setStartDate(selectedDate);
            initialContractElement.setFirstPaymentOccurs("StartDate");


        }
    }



//    public void setStartDateandTime(String startDate) {
//        String[] parts = startDate.split("T");
//        String date = parts[0];
//        String time = parts[1];
//
//        this.startDate = date;
//        this.startTime = time;
//
//        Date localTime = null;
//        try {
//            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date+time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        startTimestamp = localTime.getTime();
//
//
//        startTimeCut = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(localTime);
//        startDateCut = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(localTime);
//
//
//        // Log.d("endTimeCut", startTimeCut);
//        //Log.d("endDateCut", startDateCut);
//
//
//    }


}

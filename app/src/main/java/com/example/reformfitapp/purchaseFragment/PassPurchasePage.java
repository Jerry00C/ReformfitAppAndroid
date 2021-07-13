package com.example.reformfitapp.purchaseFragment;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.round;

public class PassPurchasePage extends AppCompatActivity implements View.OnClickListener, PurchaseBottomSheetDialogFragment.OnDataPass {
    TextView purchaseOptionName;
    TextView purchaseInfo1;
    TextView purchaseInfo2;
    ImageView info_icon_1_2;
    ImageView checkmark1_2;
    TextView preTaxTotal, tax, grandTotal;
    ImageView addPromoCode, addGiftCard;
    TextView enteredPromoCode, enteredGiftCode;
    TextView DeductedAmount, giftCardDeductedAmount;
    TextView have_read_clickable;
    ProgressBar grandTotalProgress;
    ProgressBar promoCodeProgress ;
    ProgressBar giftCardProgress;
    MaterialButton confirmButton;
    Dialog loadingDialog;

    MaterialCardView storedCreditCard;
    MaterialCardView directDebitDisplayer;

    PurchaseBottomSheetDialogFragment purchaseBottomSheetDialogFragment;
    double price ;
    double tax_amount ;
    double inTotal ;
    static final int giftCard = 1;
    static final int promoCode = 0;
    ServiceData serviceData;
    int serviceId;
    MindbodyService mindbodyService;
    String enteredCode;
    String enteredGiftCard;
    String validPromoCode = "";
    String validGiftCard="";
    double giftCardBalance;
    boolean coverableByGiftCard;
    double totalAfterDeduction,promoCodeDeductedAmount, onlyPromoDeductionTotal, giftCardDeductionAmount, onlyGiftDeductionTotal ;
    ShoppingCartElement cartWithoutPayment;
    ShoppingCartElement shoppingCartElement;
    float finalPrice;
    float giftCardPaidAmount;

    String storedCardLastFour;
    String storedCardNumber;
    String storedExpMonth;
    String storedExpYear;

    String dDirectBranchingNumber;
    String dDirectTransitNumber;
    String dDirectAccountNumber;

    LinearLayout paymentMethodDisplay;

    CheckBox agreementCheckBox;
    EditText nameInput;

    boolean finishedSetUpPayment;
    PurchaseSucceededPassBackData passedBackPaymentData;



    String clientId;


    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;




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

    private void constructFinalCart(int cardType) {

        if (cardType == PurchaseSucceededPassBackData.typeStoredCredit){
            if (finalPrice!=0) {
                shoppingCartElement = cartWithoutPayment;
                shoppingCartElement.addStoredCard(storedCardLastFour, finalPrice );
            }
            shoppingCartElement.setTest(false);
            String checkoutHere = shoppingCartElement.toJsonObject().toString();
            Log.d("shoppingCartCheckPoint1",checkoutHere);

        }
        else if (cardType == PurchaseSucceededPassBackData.typeDirectDebit){
            if (finalPrice!=0) {
                shoppingCartElement = cartWithoutPayment;
                shoppingCartElement.addDirectDebit(finalPrice);
            }
            shoppingCartElement.setTest(false);
        }
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

    private void setUpCreditCardInfoDisplay(PurchaseSucceededPassBackData data) {
        storedCardLastFour = data.getStoredCreditInfo().getLastFour();
        storedCardNumber = data.getStoredCreditInfo().getCardNumber();
        storedExpMonth = data.getStoredCreditInfo().getExpMonth();
        storedExpYear = data.getStoredCreditInfo().getExpYear();

        paymentMethodDisplay.setVisibility(View.VISIBLE);
        storedCreditCard = (MaterialCardView) getLayoutInflater().inflate(R.layout.stored_credit_card_info_display,null);

        TextView cardNumberDisplayer = storedCreditCard.findViewById(R.id.info_first_display);
        cardNumberDisplayer.setText(replaceByStar(storedCardNumber));

        TextView dateDisplayer = storedCreditCard.findViewById(R.id.info_second_display);
        dateDisplayer.setText(storedExpYear+"/"+storedExpMonth);

        ImageView payment_modifier = storedCreditCard.findViewById(R.id.payment_modifier);
        payment_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateCreditCard("请输入借记卡信息");
            }
        });
        paymentMethodDisplay.addView(storedCreditCard);
    }

    private void setUpFinalPurchaseButton(){
        finishedSetUpPayment = true ;
        MaterialButton confirmButton = findViewById(R.id.confirm_purchase_button);
        confirmButton.setText("Proceed to purchase");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.promo_code_icon:
            case R.id.entered_promo_code:
                showDialog("请输入Promo Code #:",0);

                break;
            case R.id.gift_card_icon:
            case R.id.entered_gift_card:
                showDialog("请输入Gift Card #:",1);
                break;
            case R.id.have_read_clickable:
                showAgreementDialog("Group Training Agreement","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dignissim ullamcorper velit sed hendrerit. Suspendisse erat arcu, molestie quis est sed, vehicula luctus tellus. Quisque ultrices non justo nec ultricies. In posuere nisi vel nunc lobortis, ac sollicitudin quam pulvinar. Donec blandit augue id orci vehicula, eget semper est tempus. Integer auctor dictum justo, fringilla suscipit ligula suscipit at. Cras eget suscipit turpis. Maecenas sit amet nisl sagittis, hendrerit metus vitae, ornare purus. Curabitur sem ligula, imperdiet non nunc ut, luctus volutpat est. Suspendisse condimentum felis vitae nibh semper sollicitudin.");

                break;
            case R.id.confirm_purchase_button:
                if (!finishedSetUpPayment) {
//                    Log.d("Check nameInput",nameInput.getText())
                    if (!agreementCheckBox.isChecked()){
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
                        purchaseBottomSheetDialogFragment.setFinalShoppingCart(shoppingCartElement);
                        purchaseBottomSheetDialogFragment.setAmountToBePaid(finalPrice);

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
                            mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    Log.d("PaymentError",errorMessage);
                                    shoppingCartElement.resetPaymentMethods();
                                    stopLoadingBar();
                                }

                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(PassPurchasePage.this, "Purchase successful", Toast.LENGTH_SHORT).show();
                                    stopLoadingBar();
                                    finish();
                                }
                            },shoppingCartElement);
                        }
                    });
                }
            default:
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.pass_purchase_page);
        mindbodyService = new MindbodyService(PassPurchasePage.this);
        /////////////////
        /////////////confirm button boolean  initialize
        finishedSetUpPayment = false;




        //////////////// initial look
        purchaseOptionName = findViewById(R.id.pass_purchase_title);
        purchaseInfo1 = findViewById(R.id.purchase_info_1);
        purchaseInfo2 = findViewById(R.id.purchase_info_2);
        info_icon_1_2= findViewById(R.id.info_icon_1_2);
        checkmark1_2 = findViewById(R.id.check2);
        preTaxTotal = findViewById(R.id.first_subtotal);
        tax = findViewById(R.id.first_tax);
        grandTotal = findViewById(R.id.overall_total);
        confirmButton = findViewById(R.id.confirm_purchase_button);
        paymentMethodDisplay = findViewById(R.id.chosen_payment_method);
        agreementCheckBox = findViewById(R.id.agreement_checkbox);
        nameInput = findViewById(R.id.full_name);

        Intent intent = getIntent();

        String purchaseName = intent.getStringExtra("pass_name");
        String purchaseDescription1 = intent.getStringExtra("description1");
        String purchaseDescription2 = intent.getStringExtra("description2");
        serviceData =  intent.getParcelableExtra("service_data");
        serviceId = serviceData.getServiceId();



        price = serviceData.getPrice();
        tax_amount = taxRateToValue(serviceData.getTax_rate(),serviceData.getPrice());




        purchaseOptionName.setText(purchaseName);
        purchaseInfo1.setText(purchaseDescription1);
        if (purchaseDescription2.equals("")){
            purchaseInfo2.setVisibility(View.GONE);
            info_icon_1_2.setVisibility(View.GONE);
            checkmark1_2.setVisibility(View.GONE);
        }
        else{
            purchaseInfo2.setText(purchaseDescription2);
        }





        DeductedAmount = findViewById(R.id.promo_deducted_amount);

        ///////////////////promo code button
        addPromoCode = findViewById(R.id.promo_code_icon);
        enteredPromoCode = findViewById(R.id.entered_promo_code);
        addPromoCode.setOnClickListener(this);
        enteredPromoCode.setOnClickListener(this);



        ///////////////
        addGiftCard = findViewById(R.id.gift_card_icon);
        enteredGiftCode = findViewById(R.id.entered_gift_card);
        addGiftCard.setOnClickListener(this);
        enteredGiftCode.setOnClickListener(this);

        //////////////agreement text
        have_read_clickable = findViewById(R.id.have_read_clickable);
        have_read_clickable.setOnClickListener(this);



        ////////////// confirm button
        confirmButton.setOnClickListener(this);
        if(((GlobalVariableApplication)getApplication()).getLogIn()){

            clientId = ((GlobalVariableApplication)getApplication()).getClientId();
            cartWithoutPayment = ShoppingCartElement.generateDefaultCartElement(serviceId,clientId);
            mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                @Override
                public void onError(String errorMessage) {

                }

                @Override
                public void onResponse(String authToken) {
                    ShoppingCartElement cart = ShoppingCartElement.generateTestShoppingCartElement("",serviceId,clientId);
                    mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                        @Override
                        public void onError(String errorMessage) {
                            String actualPrice = getActualPriceByErrorMessage(errorMessage);
                            finalPrice = Float.parseFloat(actualPrice);
                        }

                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },cart);
                }
            });
            inTotal = finalPrice;
            inTotal= price + tax_amount;


            preTaxTotal.setText("$"+ price);
            tax.setText("$"+tax_amount);
            grandTotal.setText("$"+inTotal);

        }
        else{
            showLoginDialog("会员登录");
        }



    }




    public void showLoginDialog(String title) {
        final Dialog dialog = new Dialog(PassPurchasePage.this){
            @Override
            public boolean onTouchEvent(@NonNull MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP ){

                    Rect r = new Rect(0,0,0,0);
                    this.getWindow().getDecorView().getHitRect(r);
                    boolean intersects = r.contains((int)event.getX(), (int)event.getY());
                    if(!intersects) {
                        this.dismiss();
                        PassPurchasePage.this.finish();
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
                PassPurchasePage.this.finish();

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
                        .addOnCompleteListener((Activity) PassPurchasePage.this, new OnCompleteListener<AuthResult>() {
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

                                                                    cartWithoutPayment = ShoppingCartElement.generateDefaultCartElement(serviceId,clientId);
                                                                    mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                                                                        @Override
                                                                        public void onError(String errorMessage) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(String authToken) {
                                                                            ShoppingCartElement cart = ShoppingCartElement.generateTestShoppingCartElement("",serviceId,clientId);
                                                                            mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                                                                @Override
                                                                                public void onError(String errorMessage) {
                                                                                    String actualPrice = getActualPriceByErrorMessage(errorMessage);
                                                                                    finalPrice = Float.parseFloat(actualPrice);
                                                                                }

                                                                                @Override
                                                                                public void onResponse(JSONObject response) {

                                                                                }
                                                                            },cart);
                                                                        }
                                                                    });
                                                                    inTotal = finalPrice;
                                                                    inTotal= price + tax_amount;


                                                                    preTaxTotal.setText("$"+ price);
                                                                    tax.setText("$"+tax_amount);
                                                                    grandTotal.setText("$"+inTotal);
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
        final Dialog dialog = new Dialog(PassPurchasePage.this);

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
                                    .addOnCompleteListener(PassPurchasePage.this, new OnCompleteListener<AuthResult>() {
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
                                                                cartWithoutPayment = ShoppingCartElement.generateDefaultCartElement(serviceId,clientId);
                                                                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                                                                    @Override
                                                                    public void onError(String errorMessage) {

                                                                    }

                                                                    @Override
                                                                    public void onResponse(String authToken) {
                                                                        ShoppingCartElement cart = ShoppingCartElement.generateTestShoppingCartElement("",serviceId,clientId);
                                                                        mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                                                            @Override
                                                                            public void onError(String errorMessage) {
                                                                                String actualPrice = getActualPriceByErrorMessage(errorMessage);
                                                                                finalPrice = Float.parseFloat(actualPrice);
                                                                            }

                                                                            @Override
                                                                            public void onResponse(JSONObject response) {

                                                                            }
                                                                        },cart);
                                                                    }
                                                                });
                                                                inTotal = finalPrice;
                                                                inTotal= price + tax_amount;


                                                                preTaxTotal.setText("$"+ price);
                                                                tax.setText("$"+tax_amount);
                                                                grandTotal.setText("$"+inTotal);

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


    public void showAgreementDialog(String title, String text){
        final Dialog dialog = new Dialog(PassPurchasePage.this);
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

                enteredCode = inputText.getText().toString();

                if (type==PassPurchasePage.promoCode){
                    if (!enteredCode.equals("")) {

                        ///////////////////////////////////////////set up test checkoutcart info

                        //paymentMetadata.put("Amount",130);

                        grandTotalProgress = findViewById(R.id.grand_total_progress_bar);
                        promoCodeProgress = findViewById(R.id.promo_code_progress_bar);
                        grandTotalProgress.setVisibility(View.VISIBLE);
                        promoCodeProgress.setVisibility(View.VISIBLE);
                        ShoppingCartElement cart;
                        if (validGiftCard.equals("")){
                            cart = ShoppingCartElement.generateTestShoppingCartElement(enteredCode, serviceId,clientId);
                        }

                        else{
                            cart = ShoppingCartElement.generateTestShoppingCartElement(enteredCode,validGiftCard,giftCardBalance, serviceId,clientId);
                        }

                        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String authToken) {
                                Toast.makeText(PassPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();
                                mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                    @Override
                                    public void onError(String errorMessage) {
                                        Log.d("shoopingCartError",errorMessage);
                                        Pattern p = Pattern.compile("[0-9]*\\.?[0-9]+");
                                        Matcher m = p.matcher(errorMessage);
                                        String actualPrice = null;
                                        while(m.find()){
                                            actualPrice = m.group();
                                        }
                                        if(actualPrice!=null) {
                                            if( validGiftCard.equals("")) {
                                                Toast.makeText(PassPurchasePage.this, actualPrice, Toast.LENGTH_SHORT).show();
                                                validPromoCode = enteredCode;
                                                onlyPromoDeductionTotal = Double.parseDouble(actualPrice);
                                                finalPrice = (float) onlyPromoDeductionTotal;
                                                grandTotal.setText("$" + onlyPromoDeductionTotal);
                                                enteredPromoCode.setText(enteredCode);
                                                enteredPromoCode.setVisibility(View.VISIBLE);
                                                addPromoCode.setVisibility(View.INVISIBLE);
                                                promoCodeProgress.setVisibility(View.INVISIBLE);
                                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                                float deducted_amount = (float) inTotal - Float.parseFloat(actualPrice);
                                                promoCodeDeductedAmount = round(deducted_amount * 100.0) / 100.0;
                                                DeductedAmount.setText("-$" + promoCodeDeductedAmount);
                                                DeductedAmount.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                double actualPriceInNumber = Double.parseDouble(actualPrice);
                                                if (giftCardBalance>=actualPriceInNumber){
                                                    totalAfterDeduction = 0; // to be shown on the display
                                                    cart.setGiftCardAmount((float)actualPriceInNumber);

                                                }
                                                else {
                                                    totalAfterDeduction = actualPriceInNumber-giftCardBalance;// to be shown on display
                                                    totalAfterDeduction = roundTwoDecimal(totalAfterDeduction);

                                                }
                                                validPromoCode = enteredCode;
                                                finalPrice = (float)totalAfterDeduction;
                                                grandTotal.setText("$"+totalAfterDeduction);
                                                enteredPromoCode.setText(enteredCode);
                                                enteredPromoCode.setVisibility(View.VISIBLE);
                                                addPromoCode.setVisibility(View.INVISIBLE);
                                                promoCodeProgress.setVisibility(View.INVISIBLE);
                                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                                float deducted_amount =(float)inTotal- (float)totalAfterDeduction;
                                                DeductedAmount.setText("-$"+deducted_amount);
                                                DeductedAmount.setVisibility(View.VISIBLE);
                                            }
                                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);
                                            cartWithoutPayment = cart;

                                        }
                                        else{
                                            Toast.makeText(PassPurchasePage.this, "Promo code is not valid ", Toast.LENGTH_SHORT).show();
                                            promoCodeProgress.setVisibility(View.INVISIBLE);
                                            grandTotalProgress.setVisibility(View.INVISIBLE);


                                        }
//                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Toast.makeText(PassPurchasePage.this, response.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }, cart);
                            }
                        });




                    }

                    else {
                        validPromoCode = "";
                        if (validGiftCard.equals("")) {

                            finalPrice =(float)inTotal;
                            grandTotal.setText("$" + inTotal);
                            enteredPromoCode.setText("");
                            enteredPromoCode.setVisibility(View.GONE);

                            DeductedAmount.setText("");
                            DeductedAmount.setVisibility(View.GONE);


                            addPromoCode.setVisibility(View.VISIBLE);
                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);

                        }
                        else {
                            enteredCode = validGiftCard;
                            applyGiftCard();
                            enteredPromoCode.setText("");
                            enteredPromoCode.setVisibility(View.GONE);
                            addPromoCode.setVisibility(View.VISIBLE);
                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);



                        }

                    }

                }
                else if (type == PassPurchasePage.giftCard){
                    if (!enteredCode.equals("")) {

                        grandTotalProgress = findViewById(R.id.grand_total_progress_bar);
                        giftCardProgress = findViewById(R.id.gift_card_progress_bar);
                        grandTotalProgress.setVisibility(View.VISIBLE);
                        giftCardProgress.setVisibility(View.VISIBLE);

                        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String authToken) {
                                mindbodyService.getGiftCardBalance(new MindbodyService.GetGiftCardBalanceListener() {
                                    @Override
                                    public void onError(String errorMessage) {
                                        Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            giftCardBalance = response.getDouble("RemainingBalance");

                                            if (giftCardBalance!=0) {
                                                ShoppingCartElement cartElement = ShoppingCartElement.generateTestShoppingCartElement(validPromoCode, enteredCode, giftCardBalance, serviceId,clientId);
                                                String checkoutHere1 = cartElement.toJsonObject().toString();
                                                Log.d("shoppingCartCheckPoint5",checkoutHere1);
                                                mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                                    @Override
                                                    public void onError(String errorMessage) {
                                                        String actualPrice = getActualPriceByErrorMessage(errorMessage);// price after promocode
                                                        Log.d("actualPrice", actualPrice);
                                                        if(actualPrice!=null){



                                                                double actualPriceInNumber = Double.parseDouble(actualPrice);
                                                                validGiftCard = enteredCode;
                                                                Log.d("amount_compare", (actualPrice + "&" + giftCardBalance));
                                                                if (giftCardBalance >= actualPriceInNumber) {
                                                                    totalAfterDeduction = 0; // to be shown on the display
                                                                    cartElement.setGiftCardAmount((float)actualPriceInNumber);

                                                                } else {
                                                                    totalAfterDeduction = actualPriceInNumber - giftCardBalance;// to be shown on display
                                                                    totalAfterDeduction = roundTwoDecimal(totalAfterDeduction);

                                                                }
                                                                finalPrice = (float)totalAfterDeduction;
                                                                grandTotal.setText("$" + totalAfterDeduction);
                                                                enteredGiftCode.setText(enteredCode);
                                                                enteredGiftCode.setVisibility(View.VISIBLE);
                                                                addGiftCard.setVisibility(View.INVISIBLE);
                                                                giftCardProgress.setVisibility(View.INVISIBLE);
                                                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                                                float deducted_amount = (float) inTotal - (float) totalAfterDeduction;
                                                                Log.d("displayAmounts", (deducted_amount + " & " + inTotal + " & " + totalAfterDeduction ));
                                                                giftCardDeductionAmount = (double)round(deducted_amount * 100.0) / 100.0;
                                                                Log.d("gfda", String.valueOf(giftCardDeductionAmount));
                                                                DeductedAmount.setText("-$" + giftCardDeductionAmount);
                                                                DeductedAmount.setVisibility(View.VISIBLE);
                                                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);

                                                            String checkoutHere1 = cartElement.toJsonObject().toString();
                                                            Log.d("shoppingCartCheckPoint5",checkoutHere1);

                                                            cartWithoutPayment = cartElement;
                                                            String checkoutHere = cartWithoutPayment.toJsonObject().toString();
                                                            Log.d("shoppingCartCheckPoint2",checkoutHere);

                                                        }

                                                        else {
                                                            Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                                            giftCardProgress.setVisibility(View.INVISIBLE);
                                                            grandTotalProgress.setVisibility(View.INVISIBLE);
                                                        }

                                                    }

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        //it means that the gift card balance equal to the service price --> set directly total to 0, amount deducted to -$giftCardBalance
                                                        finalPrice = 0;
                                                        grandTotal.setText("$0");
                                                        enteredGiftCode.setText(enteredCode);
                                                        enteredGiftCode.setVisibility(View.VISIBLE);
                                                        addGiftCard.setVisibility(View.INVISIBLE);
                                                        giftCardProgress.setVisibility(View.INVISIBLE);
                                                        grandTotalProgress.setVisibility(View.INVISIBLE);
                                                        float deducted_amount =(float)inTotal;
                                                        DeductedAmount.setText("-$"+round(deducted_amount*100.0)/100.0);
                                                        DeductedAmount.setVisibility(View.VISIBLE);
                                                        cartWithoutPayment = cartElement;
                                                    }
                                                },cartElement);


                                            }

                                            else {
                                                Toast.makeText(PassPurchasePage.this, "Invalid gift card", Toast.LENGTH_SHORT).show();
                                                giftCardProgress.setVisibility(View.INVISIBLE);
                                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },enteredCode);
                            }
                        });


                    }

                    else{
                        validGiftCard = "";
                        if (validPromoCode.equals("")) {
                            grandTotal.setText("$" + inTotal);
                            enteredGiftCode.setText("");
                            enteredGiftCode.setVisibility(View.GONE);

                            DeductedAmount.setText("");
                            DeductedAmount.setVisibility(View.GONE);


                            addGiftCard.setVisibility(View.VISIBLE);
                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);

                        }

                        else
                        {
                            enteredCode= validPromoCode;
                            applyPromoCode();
                            enteredGiftCode.setText("");
                            enteredGiftCode.setVisibility(View.GONE);
                            addGiftCard.setVisibility(View.VISIBLE);
                            Log.d("stored code state", "Valid giftcard: "+validGiftCard+", Valid promocode: "+validPromoCode+", enteredcode: "+enteredCode);



                        }
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void sentToEmail() {
    }
    private double taxRateToValue(double tax_rate,double price ){

        return round(tax_rate*price*100.0)/100.0;
    }

    private double roundTwoDecimal(double price ){

        return round(price*100.0)/100.0;
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

    private void applyGiftCard(){
        grandTotalProgress = findViewById(R.id.grand_total_progress_bar);
        giftCardProgress = findViewById(R.id.gift_card_progress_bar);
        grandTotalProgress.setVisibility(View.VISIBLE);
        giftCardProgress.setVisibility(View.VISIBLE);

        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String authToken) {
                mindbodyService.getGiftCardBalance(new MindbodyService.GetGiftCardBalanceListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            giftCardBalance = response.getDouble("RemainingBalance");

                            if (giftCardBalance!=0) {
                                ShoppingCartElement cartElement = ShoppingCartElement.generateTestShoppingCartElement(validPromoCode, enteredCode, giftCardBalance, serviceId,clientId);
                                mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                                    @Override
                                    public void onError(String errorMessage) {
                                        String actualPrice = getActualPriceByErrorMessage(errorMessage);// price after promocode

                                        if(actualPrice!=null){



                                                double actualPriceInNumber = Double.parseDouble(actualPrice);
                                                validGiftCard = enteredCode;
                                                Log.d("amount_compare", (actualPrice + "&" + giftCardBalance));
                                                if (giftCardBalance >= actualPriceInNumber) {
                                                    totalAfterDeduction = 0; // to be shown on the display

                                                } else {
                                                    totalAfterDeduction = actualPriceInNumber - giftCardBalance;// to be shown on display
                                                    totalAfterDeduction = roundTwoDecimal(totalAfterDeduction);

                                                }
                                                finalPrice = (float)totalAfterDeduction;
                                                grandTotal.setText("$" + totalAfterDeduction);
                                                enteredGiftCode.setText(enteredCode);
                                                enteredGiftCode.setVisibility(View.VISIBLE);
                                                addGiftCard.setVisibility(View.INVISIBLE);
                                                giftCardProgress.setVisibility(View.INVISIBLE);
                                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                                float deducted_amount = (float) inTotal - (float) totalAfterDeduction;
                                                giftCardDeductionAmount = round(deducted_amount * 100.0) / 100.0;
                                                DeductedAmount.setText("-$" + giftCardDeductionAmount);
                                                DeductedAmount.setVisibility(View.VISIBLE);
                                                cartWithoutPayment = cartElement;


                                        }

                                        else {
                                            Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            giftCardProgress.setVisibility(View.INVISIBLE);
                                            grandTotalProgress.setVisibility(View.INVISIBLE);
                                        }

                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //it means that the gift card balance equal to the service price --> set directly total to 0, amount deducted to -$giftCardBalance
                                        finalPrice = 0;
                                        grandTotal.setText("$0");
                                        enteredGiftCode.setText(enteredCode);
                                        enteredGiftCode.setVisibility(View.VISIBLE);
                                        addGiftCard.setVisibility(View.INVISIBLE);
                                        giftCardProgress.setVisibility(View.INVISIBLE);
                                        grandTotalProgress.setVisibility(View.INVISIBLE);
                                        float deducted_amount =(float)inTotal;
                                        DeductedAmount.setText("-$"+round(deducted_amount*100.0)/100.0);
                                        DeductedAmount.setVisibility(View.VISIBLE);
                                        cartWithoutPayment = cartElement;
                                    }
                                },cartElement);


                            }

                            else {
                                Toast.makeText(PassPurchasePage.this, "Invalid gift card", Toast.LENGTH_SHORT).show();
                                giftCardProgress.setVisibility(View.INVISIBLE);
                                grandTotalProgress.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },validGiftCard);
            }
        });

    }

    private void applyPromoCode(){
        grandTotalProgress = findViewById(R.id.grand_total_progress_bar);
        promoCodeProgress = findViewById(R.id.promo_code_progress_bar);
        grandTotalProgress.setVisibility(View.VISIBLE);
        promoCodeProgress.setVisibility(View.VISIBLE);
        ShoppingCartElement cart;
        if (validGiftCard.equals("")){
            cart = ShoppingCartElement.generateTestShoppingCartElement(enteredCode, serviceId,clientId);
        }

        else{
            cart = ShoppingCartElement.generateTestShoppingCartElement(enteredCode,validGiftCard,giftCardBalance, serviceId,clientId);
        }

        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String authToken) {
                Toast.makeText(PassPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();
                mindbodyService.postCheckoutShoppingCart(new MindbodyService.CheckoutShoppingCartListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Log.d("shoopingCartError",errorMessage);
                        Pattern p = Pattern.compile("[0-9]*\\.?[0-9]+");
                        Matcher m = p.matcher(errorMessage);
                        String actualPrice = null;
                        while(m.find()){
                            actualPrice = m.group();
                        }
                        if(actualPrice!=null) {
                            if( validGiftCard.equals("")) {
                                Toast.makeText(PassPurchasePage.this, actualPrice, Toast.LENGTH_SHORT).show();
                                validPromoCode = enteredCode;
                                onlyPromoDeductionTotal = Double.parseDouble(actualPrice);
                                finalPrice = (float)onlyPromoDeductionTotal;
                                grandTotal.setText("$" + onlyPromoDeductionTotal);
                                enteredPromoCode.setText(enteredCode);
                                enteredPromoCode.setVisibility(View.VISIBLE);
                                addPromoCode.setVisibility(View.INVISIBLE);
                                promoCodeProgress.setVisibility(View.INVISIBLE);
                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                float deducted_amount = (float) inTotal - Float.parseFloat(actualPrice);
                                promoCodeDeductedAmount = round(deducted_amount * 100.0) / 100.0;
                                DeductedAmount.setText("-$" + promoCodeDeductedAmount);
                                DeductedAmount.setVisibility(View.VISIBLE);
                            }
                            else{
                                double actualPriceInNumber = Double.parseDouble(actualPrice);
                                if (giftCardBalance>=actualPriceInNumber){
                                    totalAfterDeduction = 0; // to be shown on the display

                                }
                                else {
                                    totalAfterDeduction = actualPriceInNumber-giftCardBalance;// to be shown on display
                                    totalAfterDeduction = roundTwoDecimal(totalAfterDeduction);

                                }
                                validPromoCode = enteredCode;
                                finalPrice = (float) totalAfterDeduction;
                                grandTotal.setText("$"+totalAfterDeduction);
                                enteredPromoCode.setText(enteredCode);
                                enteredPromoCode.setVisibility(View.VISIBLE);
                                addPromoCode.setVisibility(View.INVISIBLE);
                                promoCodeProgress.setVisibility(View.INVISIBLE);
                                grandTotalProgress.setVisibility(View.INVISIBLE);
                                float deducted_amount =(float)inTotal- (float)totalAfterDeduction;
                                DeductedAmount.setText("-$"+deducted_amount);
                                DeductedAmount.setVisibility(View.VISIBLE);
                            }
                            cartWithoutPayment = cart;
                        }
                        else{
                            Toast.makeText(PassPurchasePage.this, "Promo code is not valid ", Toast.LENGTH_SHORT).show();
                            promoCodeProgress.setVisibility(View.INVISIBLE);
                            grandTotalProgress.setVisibility(View.INVISIBLE);


                        }
//                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(PassPurchasePage.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, cart);
            }
        });


    }

    public void showCreateDebitCard(String title){
        final Dialog dialog = new Dialog(PassPurchasePage.this);
        dialog.setContentView(R.layout.debit_card_registration_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);
        TextView popupTitle = dialog.findViewById(R.id.popup_title);

        popupTitle.setText(title);

        RadioButton checking = dialog.findViewById(R.id.checking);
        RadioButton saving = dialog.findViewById(R.id.saving);
        TextInputEditText branch_number = dialog.findViewById(R.id.input_text_branch_number);
        TextInputEditText transit_number = dialog.findViewById(R.id.input_text_transit_number);
        TextInputEditText account_number = dialog.findViewById(R.id.input_text_account_number);
        TextInputEditText client_name = dialog.findViewById(R.id.input_text_name);







        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingBar();
                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String authToken) {
                        Toast.makeText(PassPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();
                        String accountType = new String();
                        if (checking.isChecked()){
                            accountType = "Checking";

                        }
                        else if (saving.isChecked()){
                            accountType = "Savings";
                        }

                        HashMap<String,Object> params = new HashMap<>();
                        params.put("Test",false);
                        params.put("ClientId",clientId);
                        params.put("NameOnAccount",client_name.getText().toString());
                        params.put("RoutingNumber",branch_number.getText().toString()+transit_number.getText().toString());
                        params.put("AccountNumber",account_number.getText().toString());
                        params.put("AccountType", accountType);
                        mindbodyService.postAddClientDirectDebit(new MindbodyService.AddClientDirectDebitInfoListener() {
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                        },params);





                    }
                });

            }
        });

        dialog.show();


    }

    public void showCreateCreditCard(String title){
        final Dialog dialog = new Dialog(PassPurchasePage.this);
        dialog.setContentView(R.layout.credit_card_registration_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);


        TextView popupTitle = dialog.findViewById(R.id.popup_title);
        TextInputEditText credit_card_number = dialog.findViewById(R.id.input_text_credit_card);
        TextInputEditText year = dialog.findViewById(R.id.input_text_date_valid);
        TextInputEditText month = dialog.findViewById(R.id.input_text_cvv);
        TextInputEditText client_name = dialog.findViewById(R.id.input_text_name);
        TextInputEditText address = dialog.findViewById(R.id.input_text_address);
        TextInputEditText type = dialog.findViewById(R.id.input_text_line_2);
        TextInputEditText city = dialog.findViewById(R.id.input_text_city);
        TextInputEditText state = dialog.findViewById(R.id.input_text_province);
        TextInputEditText postal_code = dialog.findViewById(R.id.input_text_postal_code);




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


                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String authToken) {
                        Toast.makeText(PassPurchasePage.this, authToken, Toast.LENGTH_SHORT).show();

                        CreditCardInfo creditCardInfo = new CreditCardInfo(
                                credit_card_number.getText().toString(),
                                Integer.parseInt(year.getText().toString()),
                                Integer.parseInt(month.getText().toString()),
                                type.getText().toString(),
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

                        mindbodyService.postUpdateClientCreditCard(new MindbodyService.UpdateClientCreditCardListener(){
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(PassPurchasePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(PassPurchasePage.this, "Succeed", Toast.LENGTH_SHORT).show();
                                String lastFour = "";
                                try {
                                    JSONObject client = response.getJSONObject("Client");
                                    JSONObject creditCardInfo = client.getJSONObject("ClientCreditCard");
                                    lastFour = creditCardInfo.getString("LastFour");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String cardNumber =  credit_card_number.getText().toString();
                                String expYear = year.getText().toString();
                                String expMonth = month.getText().toString();


                                storedCardLastFour = lastFour;
                                storedCardNumber = cardNumber;
                                storedExpMonth = expMonth;
                                storedExpYear = expYear;


                                TextView cardNumberDisplayer = storedCreditCard.findViewById(R.id.info_first_display);
                                storedCardNumber = replaceByStar(storedCardNumber);
                                cardNumberDisplayer.setText(storedCardNumber);

                                TextView dateDisplayer = storedCreditCard.findViewById(R.id.info_second_display);
                                dateDisplayer.setText(storedExpYear+"/"+storedExpMonth);

                                dialog.dismiss();

                            }
                        },clientUpdateElement.toHashmap());
                    }
                });

            }
        });

        dialog.show();


    }
    public String replaceByStar(String str) {
        return  "************"+ str.substring(str.length()-4);
    }

    public void showLoadingBar() {
        loadingDialog = new Dialog(PassPurchasePage.this);
        loadingDialog.setContentView(R.layout.progress_bar);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }
    public void stopLoadingBar(){
        loadingDialog.dismiss();
    }

}

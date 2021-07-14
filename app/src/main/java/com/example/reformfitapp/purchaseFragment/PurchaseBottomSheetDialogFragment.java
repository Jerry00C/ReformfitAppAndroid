package com.example.reformfitapp.purchaseFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.reformfitapp.ClientUpdateElement;
import com.example.reformfitapp.CreditCardInfo;
import com.example.reformfitapp.GlobalVariableApplication;
import com.example.reformfitapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PurchaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private MindbodyService mindbodyService;


    private String clientId;

    private boolean needManualCreditCard = false;

    private ShoppingCartElement finalShoppingCart;

    private float amountToBePaid;

    private ContractElement contractElement;

    private Dialog loadingDialog;

    private boolean validForCreatePaymentMethod;

    private boolean debitTypeSelected;



    public static PurchaseBottomSheetDialogFragment newInstance() {
        return new PurchaseBottomSheetDialogFragment();
    }

    public void setFinalShoppingCart(ShoppingCartElement finalShoppingCart) {
        this.finalShoppingCart = finalShoppingCart;
    }

    public void setAmountToBePaid(float amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    ////////////////////////////// data passing back to activity
    public interface OnDataPass {
        void onDataPass(PurchaseSucceededPassBackData data);
    }

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(PurchaseSucceededPassBackData data) {
        dataPasser.onDataPass(data);
    }

//////////////////////////////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        context = this.getActivity();

        clientId =((GlobalVariableApplication)getActivity().getApplication()).getClientId();

        View view = inflater.inflate(R.layout.purchase_bottom_sheet_dialog_fragment_layout, container,
                false);
        mindbodyService = new MindbodyService(context);

        MaterialButton creditCard = view.findViewById(R.id.credit_card);
        MaterialButton debitCard = view.findViewById(R.id.debit_card);
        MaterialButton onCancel = view.findViewById(R.id.cancel);

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingBar();
                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onResponse(String authToken) {
                        mindbodyService.getClientCreditCard(new MindbodyService.GetClientCreditCardListener() {
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(context, "get credit card"+errorMessage, Toast.LENGTH_SHORT).show();
                                String cardNumber = "";
                                String expMonth = "";
                                String expYear = "";
                                String lastFour = "";
                                PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(cardNumber,lastFour,expMonth,expYear);
                                passData(data);
                                stopLoadingBar();
                                //dismiss();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String cardNumber = response.getString("CardNumber");
                                    String expMonth = response.getString("ExpMonth");
                                    String expYear = response.getString("ExpYear");
                                    String lastFour = response.getString("LastFour");
                                    PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(cardNumber,lastFour,expMonth,expYear);
                                    passData(data);
                                    stopLoadingBar();
                                    //dismiss();





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }

                            @Override
                            public void onNullResponse() {
                                needManualCreditCard = true;

                                stopLoadingBar();
                                showCreateCreditCard("请输入行用卡信息");

                                dismiss();

                            }
                        },clientId);
                    }
                });
                dismiss();
            }
        });
        debitTypeSelected = false;
        debitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingBar();
                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onResponse(String authToken) {
                        mindbodyService.getClientDirectDebit(new MindbodyService.GetDirectDebitInfoListener() {
                            @Override
                            public void onError(String errorMessage) {
                                stopLoadingBar();
                                showCreateDebitCard("请输入行用卡信息");

                                dismiss();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String routingNumber = response.getString("RoutingNumber");
                                    String branchingNumber = routingNumber.substring(0,5);
                                    String transitNumber = routingNumber.substring(5);

                                    String accountNumber = response.getString("AccountNumber");

                                    PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(branchingNumber,transitNumber,accountNumber);
                                    passData(data);
                                    stopLoadingBar();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },clientId);
                    }
                });
                dismiss();

            }
        });

        onCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // get the views and attach the listener

        ////////// no use
//        Window window = getDialog().getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;

    }

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

                                    PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(branchNumber, transitNumber, accountNumber);
                                    passData(data);

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
                message.setText("Please enter valid card number");
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
                                message.setText("Please enter valid branching number");
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


                                    PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(cardNumber, lastFour, expMonth, expYear);
                                    passData(data);

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
                message.setText("Please enter valid card number");
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
                                message.setText("Please enter valid month");
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
                message.setText("Please enter valid year");
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
                                message.setText("Please enter valid card number");
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
                message.setText("Please enter valid card number");
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
                                message.setText("Please enter valid card number");
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



    public void showLoadingBar() {
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.progress_bar);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }
    public void stopLoadingBar(){
        loadingDialog.dismiss();
    }


}

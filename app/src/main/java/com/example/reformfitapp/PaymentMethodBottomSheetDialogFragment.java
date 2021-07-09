package com.example.reformfitapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reformfitapp.purchaseFragment.MindbodyService;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentMethodBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private MindbodyService mindbodyService;
    private String cardNumber,date;

    private String clientId;

    public String getCardNumber() {
        return cardNumber;
    }

    public static PaymentMethodBottomSheetDialogFragment newInstance() {
        return new PaymentMethodBottomSheetDialogFragment();
    }



/////////////////////////////////////// child to parent data transfer
    public interface OnInputSelected{
        void sendInput(String cardType, ArrayList<String> data);
    }

    public OnInputSelected onInputSelected;/* a reference to the parent fragment*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onAttachToParentFragment(getParentFragment());
    }

    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            onInputSelected = (OnInputSelected)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }
//////////////////////////////////////////////////////////////////////////////////////


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        context = getActivity();

        clientId = "100013640";

        View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment_layout, container,
                false);
        mindbodyService = new MindbodyService(context);

        MaterialButton creditCard = view.findViewById(R.id.credit_card);
        MaterialButton debitCard = view.findViewById(R.id.debit_card);
        MaterialButton onCancel = view.findViewById(R.id.cancel);

        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                showCreateCreditCard("请输入行用卡信息");
            }
        });

        debitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDebitCard("请输入行用卡信息");

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

        return view;

    }



    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        return super.show(transaction, tag);
    }
    //    @Override
//    public void onStart()
//    {
//        if (getDialog() == null)
//        {
//            return;
//        }
//
//        getDialog().getWindow().setWindowAnimations(
//                R.style.DialogAnimation);
//
//        super.onStart();
//    }

//    @Override
//    public int getTheme() {
//        return R.style.DialogAnimation;
//    }
    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//    }





    public void showCreateCreditCard(String title){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.credit_card_registration_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton apply = dialog.findViewById(R.id.confirm_button);
        MaterialButton cancel = dialog.findViewById(R.id.cancel_button);


        TextView popupTitle = dialog.findViewById(R.id.popup_title_real);
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

        cardNumber = "";

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
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String authToken) {
                        Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
                                String cardNumber =  credit_card_number.getText().toString();
                                String date = year.getText().toString()+"/"+month.getText().toString();

                                ArrayList<String> data = new ArrayList<>();
                                data.add(cardNumber);
                                data.add(date);

                                onInputSelected.sendInput("CreditCard",data);
                                dialog.dismiss();

                            }
                        },clientUpdateElement.toHashmap());
                    }
                });

            }
        });

        dialog.show();


    }




    public void showCreateDebitCard(String title){
        final Dialog dialog = new Dialog(getActivity());
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
                mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String authToken) {
                        Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(JSONObject response) {

                                String branchNumber = branch_number.getText().toString();
                                String transitNumber = transit_number.getText().toString();
                                String accountNumber = account_number.getText().toString();

                                ArrayList<String> data = new ArrayList<>();
                                data.add(branchNumber);
                                data.add(transitNumber);
                                data.add(accountNumber);


                                onInputSelected.sendInput("DebitCard",data);
                                dialog.dismiss();

                            }
                        },params);





                    }
                });

            }
        });

        dialog.show();


    }
}


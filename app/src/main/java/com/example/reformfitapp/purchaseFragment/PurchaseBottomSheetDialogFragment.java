package com.example.reformfitapp.purchaseFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
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
import java.util.HashMap;

public class PurchaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private MindbodyService mindbodyService;


    private String clientId;

    private boolean needManualCreditCard = false;

    private ShoppingCartElement finalShoppingCart;

    private float amountToBePaid;

    private ContractElement contractElement;

    private Dialog loadingDialog;



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

                                PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(branchNumber,transitNumber,accountNumber);
                                passData(data);

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
        final Dialog dialog = new Dialog(context);
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


                                PurchaseSucceededPassBackData data = new PurchaseSucceededPassBackData(cardNumber,lastFour,expMonth,expYear);
                                passData(data);

                                stopLoadingBar();
                                dialog.dismiss();

                            }
                        },clientUpdateElement.toHashmap());
                    }
                });

            }
        });

        dialog.show();


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

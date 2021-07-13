package com.example.reformfitapp.purchaseFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupClassFragmentPurchase#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupClassFragmentPurchase extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View current_view;
    private View class_20_clickable;
    private View class_10_clickable;
    private View class_1_month_clickable;
    private View class_single_clickable;

    private View PB_clickable;
    private View EB_clickable;
    private View MB_clickable;

    private Context context;
    private MindbodyService mindbodyService;
    private String service_category;
    private int programId;
    private int locationId;

    private int contractId;

    private String test_contract_name = "month 1 unlimited";


    private HashMap<String,ServiceData> name_serviceId_pair;
    private HashMap<String,ContractData> name_contractId_pair;

    private Dialog loadingDialog;

    private boolean synchronizeBoolean = false;

    public GroupClassFragmentPurchase() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupClassFragmentPurchase newInstance(String param1, String param2) {
        GroupClassFragmentPurchase fragment = new GroupClassFragmentPurchase();
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

        locationId = 1;
        contractId = 354;
        name_serviceId_pair =new HashMap<>();
        name_contractId_pair=new HashMap<>();
        service_category = "Classes"; /* correspond to the two type of class format */
        context = getActivity();
        mindbodyService = new MindbodyService(context);
        showLoadingBar();
        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String authToken) {
                //Toast.makeText(context, "Token id: "+authToken, Toast.LENGTH_SHORT).show();

                mindbodyService.getContracts(new MindbodyService.GetContractsListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        synchronizedCount();
                    }

                    @Override
                    public void onResponse(JSONArray arrayResponse) {
                        for (int it = 0; it< arrayResponse.length(); it++){

                            try {
                                JSONObject one_contract_from_array = (JSONObject) arrayResponse.get(it);
                                String name = one_contract_from_array.getString("Name");
                                /* official version: must include a check for keyword "PASS"

                                if (name.contains("BURN"){


                                */
                                Log.d("ContractNames",name);
                                if (name.contains("unlimited")) {



                                    ContractData contractData = new ContractData();
                                    contractData.setContract_id(one_contract_from_array.getInt("Id"));
                                    contractData.setFirst_payment_subtotal(one_contract_from_array.getDouble("FirstPaymentAmountSubtotal"));
                                    Log.d("display_issue", String.valueOf(one_contract_from_array.getDouble("FirstPaymentAmountSubtotal")));
                                    contractData.setFirst_payment_tax(one_contract_from_array.getDouble("FirstPaymentAmountTax"));
                                    contractData.setFirst_payment_total(one_contract_from_array.getDouble("FirstPaymentAmountTotal"));
                                    contractData.setRecurring_payment_subtotal(one_contract_from_array.getDouble("RecurringPaymentAmountSubtotal"));
                                    contractData.setRecurring_payment_tax(one_contract_from_array.getDouble("RecurringPaymentAmountTax"));
                                    contractData.setRecurring_payment_total(one_contract_from_array.getDouble("RecurringPaymentAmountTotal"));
                                    contractData.setAgreement_terms(one_contract_from_array.getString("AgreementTerms"));
                                    contractData.setName(one_contract_from_array.getString("Name"));

                                    //get the service id
                                    JSONObject contractItem_for_service = (JSONObject) one_contract_from_array.getJSONArray("ContractItems").get(0);
                                    String serviceId_inString = contractItem_for_service.getString("Id");
                                    contractData.setServiceId(Integer.parseInt(serviceId_inString));

                                    name_contractId_pair.put(contractData.getName(), contractData);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        Log.d("name_contractId_pair",name_contractId_pair.toString());
                        synchronizedCount();


                    }
                },locationId,contractId);

                mindbodyService.getPrograms(new MindbodyService.GetProgramListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        synchronizedCount();
                    }

                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        try{
                            JSONArray programs_json_array = response.getJSONArray("Programs");
                            for (int it = 0; it<programs_json_array.length(); it++){
                                JSONObject program_for_this_fragment = (JSONObject)programs_json_array.get(it);
                                /* check if this fragment deals with the specific program*/
                                if(service_category.equals(program_for_this_fragment.getString("Name"))){
                                    programId = program_for_this_fragment.getInt("Id");
                                    break;
                                }

                            }
                            mindbodyService.getServicesOfProgram(new MindbodyService.GetServiceListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                    synchronizedCount();
                                }

                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    JSONArray list_of_services = response.getJSONArray("Services");
                                    for (int it = 0; it< list_of_services.length(); it++){
                                        JSONObject one_service = (JSONObject) list_of_services.get(it);

                                        /* official version: must include a check for keyword "PASS"
                                        String name = one_service.getString("Name");
                                        if (name.contains("PASS"){


                                         */

                                        ServiceData serviceData = new ServiceData();
                                        serviceData.setPrice((double)one_service.getInt("Price"));
                                        serviceData.setProgramId(one_service.getInt("ProgramId"));
                                        serviceData.setTax_rate(one_service.getDouble("TaxRate"));
                                        serviceData.setServiceId(one_service.getInt("Id"));
                                        serviceData.setServiceName(one_service.getString("Name"));
                                        serviceData.setCount(one_service.getInt("Count"));
                                        serviceData.setExpirationType(one_service.getString("ExpirationType"));
                                        serviceData.setExpirationUnit(one_service.getString("ExpirationUnit"));
                                        serviceData.setExpirationLength(one_service.getInt("ExpirationLength"));
                                        serviceData.setProgram_name(one_service.getString("Program"));
                                        name_serviceId_pair.put(one_service.getString("Name"),serviceData);
                                        /*
                                        }
                                         */
                                    }
                                    synchronizedCount();

                                }
                            },programId);



                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        current_view= inflater.inflate(R.layout.fragment_group_class_purchase, container, false);

        PB_clickable =current_view.findViewById(R.id.PBclickable);
        EB_clickable = current_view.findViewById(R.id.EBclickable);
        MB_clickable = current_view.findViewById(R.id.MBclickable);

        PB_clickable.setOnClickListener(this);
        EB_clickable.setOnClickListener(this);
        MB_clickable.setOnClickListener(this);


        class_20_clickable = current_view.findViewById(R.id.class20clickable);
        class_10_clickable = current_view.findViewById(R.id.class10clickable);
        class_1_month_clickable = current_view.findViewById(R.id.month1clickable);
        class_single_clickable = current_view.findViewById(R.id.class1clickable);

        class_20_clickable.setOnClickListener(this);
        class_10_clickable.setOnClickListener(this);
        class_1_month_clickable.setOnClickListener(this);
        class_single_clickable.setOnClickListener(this);
        // Inflate the layout for this fragment
        return current_view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.class20clickable:

                initializeChosenOptionPass(R.id.passOption1, R.id.class20info1, R.id.class20info2);
                break;
            case R.id.class10clickable:

                initializeChosenOptionPass(R.id.passOption2, R.id.class10info1, R.id.class10info2);
                break;
            case R.id.month1clickable:

                initializeChosenOptionPass(R.id.passOption3, R.id.month1info1, R.id.month1info2);


                break;
            case R.id.class1clickable:

                initializeChosenOptionPass(R.id.passOption4, R.id.class1info1, R.id.class1info2);


                break;
            case R.id.PBclickable:
                initializeChosenOptionMembership(R.id.memOption1, R.id.PBinfo1, R.id.PBinfo2, R.id.PBinfo3);
                break;
            case R.id.EBclickable:
                initializeChosenOptionMembership(R.id.memOption2, R.id.EBinfo1, R.id.EBinfo2, R.id.EBinfo3);
                break;
            case R.id.MBclickable:
                initializeChosenOptionMembership(R.id.memOption3, R.id.MBinfo1, R.id.MBinfo3);
                break;


            default:
                break;
        }
    }



    /////////////////////////////// switch to pass purchasing page
    private void switchToPassPurchasePage(String title, ArrayList<String> infos) {

        Intent switchActivityIntent = new Intent(getActivity(), PassPurchasePage.class);

        switchActivityIntent.putExtra("service_data",name_serviceId_pair.get(title));
        switchActivityIntent.putExtra("pass_name",title);
        for(int it=0; it<infos.size(); it++){
            switchActivityIntent.putExtra("description"+ (it + 1), infos.get(it));
        }
        startActivity(switchActivityIntent);
    }

    private void initializeChosenOptionPass(int purchase_title, int purchase_info1, int purchase_info2){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(purchase_info1);
        TextView description2_view = current_view.findViewById(purchase_info2);

        //   TODO: official version must set the title passed-in as the displayed name, pass name from view must equal to the existing pass nam ein the system in order for it to work
        String pass_name = pass_name_view.getText().toString();
        String description1 = description1_view.getText().toString();
        String description2 = description2_view.getText().toString();
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(description1);
        descriptions.add(description2);
        switchToPassPurchasePage(pass_name,descriptions);
    }

    //////////////////////////// switch to membership purchasing page

    private void switchToMembershipPurchasePage(String title, ArrayList<String> infos) {

        Intent switchActivityIntent = new Intent(getActivity(), MembershipPurchasePage.class);

        switchActivityIntent.putExtra("contract_data",name_contractId_pair.get(title));
        switchActivityIntent.putExtra("membership_name",title);
        for(int it=0; it<infos.size(); it++){
            switchActivityIntent.putExtra("description"+ (it + 1), infos.get(it));
        }
        startActivity(switchActivityIntent);
    }
    private void initializeChosenOptionMembership(int purchase_title, int purchase_info1, int purchase_info2){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(purchase_info1);
        TextView description2_view = current_view.findViewById(purchase_info2);


        //   TODO: official version must set the title passed-in as the displayed name
//        String pass_name = pass_name_view.getText().toString();
        String pass_name = test_contract_name;

        String description1 = description1_view.getText().toString();
        String description2 = description2_view.getText().toString();
        String description3 = "";
        String description4 = "";
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(description1);
        descriptions.add(description2);
        descriptions.add(description3);
        descriptions.add(description4);
        switchToMembershipPurchasePage(pass_name,descriptions);
    }
    private void initializeChosenOptionMembership(int purchase_title, int info1, int info2, int info3){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(info1);
        TextView description2_view = current_view.findViewById(info2);
        TextView description3_view = current_view.findViewById(info3);


        //   TODO: official version must set the title passed-in as the displayed name
//        String pass_name = pass_name_view.getText().toString();
        String pass_name = test_contract_name;

        String description1 = description1_view.getText().toString();
        String description2 = description2_view.getText().toString();
        String description3 = description3_view.getText().toString();
        String description4 = "";
        ArrayList<String> descriptions = new ArrayList<>();
        descriptions.add(description1);
        descriptions.add(description2);
        descriptions.add(description3);
        descriptions.add(description4);
        switchToMembershipPurchasePage(pass_name, descriptions);

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


    private void synchronizedCount(){
        if (!synchronizeBoolean){
            synchronizeBoolean = true;
        }
        else {
            Log.d("contracts : ", name_contractId_pair.toString());
            Log.d("services : ", name_serviceId_pair.toString());
            stopLoadingBar();
        }
    }


}
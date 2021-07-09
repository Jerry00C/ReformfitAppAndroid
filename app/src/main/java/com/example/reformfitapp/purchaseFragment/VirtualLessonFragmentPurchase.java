package com.example.reformfitapp.purchaseFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.reformfitapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VirtualLessonFragmentPurchase extends Fragment implements View.OnClickListener{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View current_view;

    private View virtualUnlimited;

    private View virtual_10_clickable;
    private View virtual_5_clickable;

    private Context context;
    private MindbodyService mindbodyService;
    private String service_category;
    private int programId;
    private int locationId;
    private int contractId;

    private HashMap<String,ServiceData> name_serviceId_pair;
    private HashMap<String,ContractData> name_contractId_pair;
    private String test_contract_name = "yoga contract";
    private String test_service_name = "10 yoga Card PASS";


    public VirtualLessonFragmentPurchase() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateLessonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VirtualLessonFragmentPurchase newInstance(String param1, String param2) {
        VirtualLessonFragmentPurchase fragment = new VirtualLessonFragmentPurchase();
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
        // Inflate the layout for this fragment
        locationId = 1;
        contractId = 353;
        name_serviceId_pair =new HashMap<>();
        name_contractId_pair=new HashMap<>();
        service_category = "Yoga"; /* correspond to the two type of class format */
        context = getActivity();
        mindbodyService = new MindbodyService(context);

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
                                if (name.contains("yoga")) {

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


                    }
                },locationId,contractId);

                mindbodyService.getPrograms(new MindbodyService.GetProgramListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
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
                                    Log.d("VirtualProgramId", String.valueOf(programId));
                                    break;
                                }

                            }
                            mindbodyService.getServicesOfProgram(new MindbodyService.GetServiceListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    JSONArray list_of_services = response.getJSONArray("Services");
                                    for (int it = 0; it< list_of_services.length(); it++){
                                        Log.d("virtualServiceCount", String.valueOf(it));
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

                                }
                            },programId);

                            Log.d("name_serviceId_pair",name_serviceId_pair.toString());

                        } catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

        current_view = inflater.inflate(R.layout.fragment_virtual_lesson_purchase, container, false);

        virtualUnlimited =current_view.findViewById(R.id.virtual_unlimited_clickable);


        virtualUnlimited.setOnClickListener(this);



        virtual_5_clickable = current_view.findViewById(R.id.virtual_5_clickable);
        virtual_10_clickable = current_view.findViewById(R.id.virtual_10_clickable);


        virtual_5_clickable.setOnClickListener(this);
        virtual_10_clickable.setOnClickListener(this);





        return current_view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.virtual_5_clickable:

                initializeChosenOptionPass(R.id.virtual_5_title, R.id.virtual_5_info_1);
                break;
            case R.id.virtual_10_clickable:

                initializeChosenOptionPass(R.id.virtual_10_title, R.id.virtual_10_info_1);
                break;
            case R.id.virtual_unlimited_clickable:

                initializeChosenOptionMembership(R.id.virtual_unlimited_title, R.id.virtual_unlimited_info_1, R.id.virtual_unlimited_info_2,R.id.virtual_unlimited_info_3);


            default:
                break;
        }
    }

    ////////////////////////////// switch to pass purchasing page
    private void switchToPassPurchasePage(String title, ArrayList<String> infos) {

        Intent switchActivityIntent = new Intent(getActivity(), PassPurchasePage.class);

        switchActivityIntent.putExtra("service_data",name_serviceId_pair.get(title));
        switchActivityIntent.putExtra("pass_name",title);
        for(int it=0; it<infos.size(); it++){
            switchActivityIntent.putExtra("description"+ (it + 1), infos.get(it));
        }
        startActivity(switchActivityIntent);
    }

    private void initializeChosenOptionPass(int purchase_title, int purchase_info1){
        TextView pass_name_view = current_view.findViewById(purchase_title);
        TextView description1_view = current_view.findViewById(purchase_info1);


        // TODO: official version must set the title passed-in as the displayed name
        //String pass_name = pass_name_view.getText().toString();
        String pass_name =test_service_name;
        String description1 = description1_view.getText().toString();
        String description2 = "";
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

}

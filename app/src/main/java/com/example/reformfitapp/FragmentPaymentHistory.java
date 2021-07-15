package com.example.reformfitapp;

import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reformfitapp.purchaseFragment.MindbodyService;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPaymentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPaymentHistory extends Fragment implements PaymentMethodBottomSheetDialogFragment.OnInputSelected{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View current_view;
    private ImageView payment_modifer;

    private TextView first_info_title, second_info_title,third_info_title, fourth_info_title, fifth_info_title;
    private TextView first_info, second_info, third_info, fourth_info, fifth_info;

    private SwipeRefreshLayout swipeView;

    private RecyclerView payment_history_viewer;

    private MindbodyService mindbodyService;

    private PaymentMethodBottomSheetDialogFragment paymentMethodBottomSheetDialogFragment;

    private Context context;

    private String clientId;

    private String startDate, endDate;

    private ImageView history_date_selector;

    private String routing_number, branch_number,transit_number, account_number,year_month, cardNumber;
    private int synchronizeOnResponseCount;

    private ImageView arrowDownModifier;


    public FragmentPaymentHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPaymentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPaymentHistory newInstance(String param1, String param2) {
        FragmentPaymentHistory fragment = new FragmentPaymentHistory();
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

        clientId =((GlobalVariableApplication)getActivity().getApplication()).getClientId();
        context = getActivity();
        current_view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        swipeView = current_view.findViewById(R.id.swipView);
        payment_modifer = current_view.findViewById(R.id.payment_modifier);

        first_info = current_view.findViewById(R.id.info_first_display);
        second_info = current_view.findViewById(R.id.info_second_display);
        third_info = current_view.findViewById(R.id.info_third_display);
        fourth_info = current_view.findViewById(R.id.info_fourth_display);
        fifth_info = current_view.findViewById(R.id.info_fifth_display);


        payment_history_viewer = current_view.findViewById(R.id.history_recycler_view);



        history_date_selector = current_view.findViewById(R.id.history_date_selector);

        history_date_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long today = MaterialDatePicker.todayInUtcMilliseconds();
                Calendar calendar  = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.clear();
                calendar.setTimeInMillis(today);
                calendar.roll(Calendar.MONTH, -1);
                long halfYear = calendar.getTimeInMillis();
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.roll(Calendar.DATE,1);
                long tomorrow = calendar.getTimeInMillis();

                //String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);



                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(halfYear);
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(tomorrow);
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);


        /*Pair<Long, Long> pair = new Pair<>(startDate_timestamp, System.currentTimeMillis());

        builder.setSelection(pair);*/
                builder.setTitleText("选择会员激活日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);
                        startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


                        Date date2 = new Date(selection.second);
                        endDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeView.setRefreshing(true);
                        refreshPurchaseHistory(startDate,endDate);




                    }
                });




                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");
            }
        });

        arrowDownModifier = current_view.findViewById(R.id.downArrowModifier);

        arrowDownModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long today = MaterialDatePicker.todayInUtcMilliseconds();
                Calendar calendar  = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.clear();
                calendar.setTimeInMillis(today);
                calendar.roll(Calendar.MONTH, -1);
                long halfYear = calendar.getTimeInMillis();
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.roll(Calendar.DATE,1);
                long tomorrow = calendar.getTimeInMillis();

                //String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);



                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(halfYear);
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(tomorrow);
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);


        /*Pair<Long, Long> pair = new Pair<>(startDate_timestamp, System.currentTimeMillis());

        builder.setSelection(pair);*/
                builder.setTitleText("选择会员激活日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);
                        startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


                        Date date2 = new Date(selection.second);
                        endDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeView.setRefreshing(true);
                        refreshPurchaseHistory(startDate,endDate);




                    }
                });




                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");

            }
        });



        payment_modifer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getChildFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                paymentMethodBottomSheetDialogFragment = PaymentMethodBottomSheetDialogFragment.newInstance();

                paymentMethodBottomSheetDialogFragment.show(ft,null);



            }
        });

        mindbodyService = new MindbodyService(getActivity());


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPurchaseHistory();

            }
        });

        if(((GlobalVariableApplication)getActivity().getApplication()).getLogIn()){


            swipeView.setRefreshing(true);
            refreshPurchaseHistory();
        }

        // Inflate the layout for this fragment
        return current_view;
    }

    @Override
    public void sendInput(String cardType, ArrayList<String> data) {
        if (cardType.equals("CreditCard")){
            first_info.setText(replaceByStar(data.get(0)));
            second_info.setText(data.get(1));

        }
        else if(cardType.equals("DebitCard")){
            third_info.setText(data.get(0));
            fourth_info.setText(data.get(1));
            fifth_info.setText(data.get(2));


        }

    }

    public void refreshPurchaseHistory(){
        if(((GlobalVariableApplication)getActivity().getApplication()).getLogIn()){
            clientId = ((GlobalVariableApplication)getActivity().getApplication()).getClientId();
        }
        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String authToken) {
//                Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();

                synchronizeOnResponseCount = 0;

                mindbodyService.getClientDirectDebit(new MindbodyService.GetDirectDebitInfoListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
//                        boolean equalMessage = errorMessage.equals("got null: no debit card stored on this client Id");
//                        Log.d("onErrorResponseDebit", errorMessage);
//                        Log.d("onErrorResponseDebit", String.valueOf(equalMessage));

                        // the logic reaches here most likely because the response body is a null
                        synchronizeCount("debit");
                    }

                    @Override
                    public void onResponse(JSONObject response)  {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        try {

                            routing_number = response.getString("RoutingNumber");
                            Log.d("onResponseDbitInfo", routing_number);
                            Log.d("onResponseDbitInfo",response.toString());
                            branch_number = routing_number.substring(0,5);
                            transit_number = routing_number.substring(5);


                            account_number = response.getString("AccountNumber");
                            account_number = account_number.substring(account_number.length()-4);
                            synchronizeCount("debit");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONobject", e.toString());
                        }



                    }
                },clientId);


                mindbodyService.getClientCreditCard(new MindbodyService.GetClientCreditCardListener() {
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, "get credit card"+errorMessage, Toast.LENGTH_SHORT).show();
                        cardNumber = "";
                        year_month = "";
                        synchronizeCount("credit");
                        // when error is "something went wrong, please try again", the logic reaches here
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cardNumber = response.getString("CardNumber");
                            String year = response.getString("ExpYear");
                            String month = response.getString("ExpMonth");
                            if (month.length()==2){
                                month = month.substring(1);
                            }
                            year_month = year+"/"+month;
                            synchronizeCount("credit");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onNullResponse() {
                        synchronizeCount("credit");
                        // when there is no credit card registered in this client id, showing null at that field
                    }
                },clientId);


                mindbodyService.extractClientPurchasedItems(clientId,"now", "end_of_day", new MindbodyService.ExtractClientPaymentHistoryListener(){

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.d("History error", errorMessage);
                        synchronizeCount("history");

                    }

                    @Override
                    public void onResponse(List<PaymentHistoryElement> paymentHistoryList) {
                        TextView emptyMessage = current_view.findViewById(R.id.empty_message);

                        if (paymentHistoryList.isEmpty()) {
                            emptyMessage.setVisibility(View.VISIBLE);
                        }
                        else{
                            emptyMessage.setVisibility(View.GONE);

                            PaymentHistoryRecViewAdapter adapter = new PaymentHistoryRecViewAdapter();
                            adapter.setPayment_his_element_list(paymentHistoryList);
                            payment_history_viewer.setAdapter(adapter);

                        }

                        synchronizeCount("history");

                    }

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                });


            }
        });
    }

    public void refreshPurchaseHistory(String startDate, String endDate){
        mindbodyService.getAuthToken(new MindbodyService.AuthTokenResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String authToken) {
//                Toast.makeText(context, authToken, Toast.LENGTH_SHORT).show();
                mindbodyService.extractClientPurchasedItems(clientId,startDate, endDate, new MindbodyService.ExtractClientPaymentHistoryListener(){

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(List<PaymentHistoryElement> paymentHistoryList) {

                        PaymentHistoryRecViewAdapter adapter = new PaymentHistoryRecViewAdapter();
                        adapter.setPayment_his_element_list(paymentHistoryList);
                        payment_history_viewer.setAdapter(adapter);
                        payment_history_viewer.setLayoutManager(new LinearLayoutManager(context));
                        swipeView.setRefreshing(false);


                    }

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                });


            }
        });
    }


    public String replaceByStar(String str) {
        return  "************"+ str.substring(str.length()-4);
    }

    public void synchronizeCount(String name){

        if (synchronizeOnResponseCount==0){
            synchronizeOnResponseCount++;
            Log.d("step", name);
        }
        else if (synchronizeOnResponseCount==1){
            synchronizeOnResponseCount++;
            Log.d("step", name);
        }
        else if (synchronizeOnResponseCount==2){
            Log.d("step", name);
            synchronizeOnResponseCount=0;

            third_info.setText(branch_number);
            fourth_info.setText(transit_number);
            fifth_info.setText(account_number);
            Log.d("DirectDebit",branch_number+"/"+transit_number+"/"+account_number);

            first_info.setText(cardNumber);
            second_info.setText(year_month);

            payment_history_viewer.setLayoutManager(new LinearLayoutManager(context));
            swipeView.setRefreshing(false);




        }
    }

    public void setUpDatePicker() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.setTimeInMillis(today);
        calendar.roll(Calendar.MONTH, -1);
        long halfYear = calendar.getTimeInMillis();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.roll(Calendar.DATE, 1);
        long tomorrow = calendar.getTimeInMillis();

        //String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(halfYear);
        CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(tomorrow);
        ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
        listOfValidators.add(dateValidator_start);
        listOfValidators.add(dateValidator_end);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
        constraintsBuilder.setValidator(validators);


        /*Pair<Long, Long> pair = new Pair<>(startDate_timestamp, System.currentTimeMillis());

        builder.setSelection(pair);*/
        builder.setTitleText("选择会员激活日期");
        builder.setTheme(R.style.MaterialCalendarTheme);
        builder.setCalendarConstraints(constraintsBuilder.build());


        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date date = new Date(selection.first);
                startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


                Date date2 = new Date(selection.second);
                endDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date2);


                Log.d("startDate", startDate);
                Log.d("endDate", endDate);

                materialDatePicker.dismiss();

                swipeView.setRefreshing(true);
                refreshPurchaseHistory(startDate, endDate);


            }
        });
    }
}
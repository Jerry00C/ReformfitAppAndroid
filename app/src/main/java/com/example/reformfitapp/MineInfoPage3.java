package com.example.reformfitapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineInfoPage3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineInfoPage3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout linearLayout;
    LinearLayout linearLayout2;

    SwipeRefreshLayout swipeRefreshLayout;


    private boolean stoppedOnce = false;

    ImageView initCalendarH;
    ImageView initCalendarH2;
    ImageView initCalendarU;
    ImageView initCalendarU2;

    Context context;



    private HistoryUpcomingBottomSheetDialogFragment historyUpcomingBottomSheetDialogFragment;
    private HistoryUpcomingOnlineBSDF historyUpcomingOnlineBSDF;
    private HistoryBSDF historyBSDF;
    private HistoryUpcomingWaitlistBSDF historyUpcomingWaitlistBSDF;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String userID;


    public MineInfoPage3() {
        // Required empty public constructor
        context = getContext();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineInfoPage3.
     */
    // TODO: Rename and change types and number of parameters
    public static MineInfoPage3 newInstance(String param1, String param2) {
        MineInfoPage3 fragment = new MineInfoPage3();
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
        View view = inflater.inflate(R.layout.fragment_mine_info_page3, container, false);

        linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout2 = view.findViewById(R.id.linearLayout2);

        swipeRefreshLayout = view.findViewById(R.id.init_refresh);

        initCalendarH = view.findViewById(R.id.init_calendarH);
        initCalendarH2 = view.findViewById(R.id.init_calendarH2);
        initCalendarU = view.findViewById(R.id.init_calendarU);
        initCalendarU2 = view.findViewById(R.id.init_calendarU2);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
        initCalendarU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("clicked", "clicked");

                long today = MaterialDatePicker.todayInUtcMilliseconds();
                Calendar calendar  = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.clear();
                calendar.setTimeInMillis(today);
                calendar.add(Calendar.DAY_OF_MONTH, 13);
                long upper_range = calendar.getTimeInMillis();

                //String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


                firebaseAuth = FirebaseAuth.getInstance();
                long startDate_timestamp = firebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp();


                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.now();
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(upper_range);
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);


                /*Pair<Long, Long> pair = new Pair<>(startDate_timestamp, System.currentTimeMillis());

                builder.setSelection(pair);*/
                builder.setTitleText("选择查看日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String startDate = (String) sdf.format(date);


                        Date date2 = new Date(selection.second);

                        String endDate = (String) (String) sdf.format(date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeRefreshLayout.setRefreshing(true);
                        refreshProgressing(startDate, endDate);



                    }
                });
                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");

            }
        });

        initCalendarU2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("clicked", "clicked");

                long today = MaterialDatePicker.todayInUtcMilliseconds();
                Calendar calendar  = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.clear();
                calendar.setTimeInMillis(today);
                calendar.add(Calendar.DAY_OF_MONTH, 13);
                long upper_range = calendar.getTimeInMillis();

                //String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


                firebaseAuth = FirebaseAuth.getInstance();
                long startDate_timestamp = firebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp();


                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.now();
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.before(upper_range);
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);


                /*Pair<Long, Long> pair = new Pair<>(startDate_timestamp, System.currentTimeMillis());

                builder.setSelection(pair);*/
                builder.setTitleText("选择查看日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String startDate = (String) sdf.format(date);


                        Date date2 = new Date(selection.second);

                        String endDate = (String) (String) sdf.format(date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeRefreshLayout.setRefreshing(true);
                        refreshProgressing(startDate, endDate);



                    }
                });
                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");

            }
        });

        initCalendarH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("clicked", "clicked");


                firebaseAuth = FirebaseAuth.getInstance();
                long startDate_timestamp = firebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp();

                Date date2 = new Date(startDate_timestamp);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String startDate = (String) sdf.format(date2);

                Log.d("calendar", startDate);



                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(startDate_timestamp);
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.now();
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);

                builder.setTitleText("选择查看日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String startDate = (String) sdf.format(date);


                        Date date2 = new Date(selection.second);

                        String endDate = (String) (String) sdf.format(date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeRefreshLayout.setRefreshing(true);
                        refreshHistory(startDate, endDate);



                    }
                });
                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");

            }
        });

        initCalendarH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("clicked", "clicked");



                firebaseAuth = FirebaseAuth.getInstance();
                long startDate_timestamp = firebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp();


                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                CalendarConstraints.DateValidator dateValidator_start = DateValidatorPointForward.from(startDate_timestamp);
                CalendarConstraints.DateValidator dateValidator_end = DateValidatorPointBackward.now();
                ArrayList<CalendarConstraints.DateValidator> listOfValidators = new ArrayList<>();
                listOfValidators.add(dateValidator_start);
                listOfValidators.add(dateValidator_end);
                CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listOfValidators);
                constraintsBuilder.setValidator(validators);


                builder.setTitleText("选择查看日期");
                builder.setTheme(R.style.MaterialCalendarTheme);
                builder.setCalendarConstraints(constraintsBuilder.build());


                final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Date date = new Date(selection.first);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String startDate = (String) sdf.format(date);


                        Date date2 = new Date(selection.second);

                        String endDate = (String) (String) sdf.format(date2);



                        Log.d("startDate", startDate);
                        Log.d("endDate", endDate);

                        materialDatePicker.dismiss();

                        swipeRefreshLayout.setRefreshing(true);
                        refreshHistory(startDate, endDate);



                    }
                });
                materialDatePicker.show(getChildFragmentManager(),"DATE_PICKER");

            }
        });


        if(((GlobalVariableApplication) getActivity().getApplication()).getLogIn()){
            loginSetup();
        }
        return view;
    }


    public void loginSetup(){


        swipeRefreshLayout.setRefreshing(true);
        refreshPage();




    }


    private void refreshProgressing(String endDate, String endDate2){

        String clientId = ((GlobalVariableApplication) getActivity().getApplication()).getClientId();


        MindbodyVisitHistory mindbodyVisitHistory = new MindbodyVisitHistory(context);
        mindbodyVisitHistory.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                mindbodyVisitHistory.getVisitHistoryInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);




                        mindbodyVisitHistory.getWaitlistEntries(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);


                                mindbodyVisitHistory.getWaitlistEntriesOrder(new MindbodyClass.VolleyResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                        Log.d("mindbody_response", response);


                                        if(!mindbodyVisitHistory.getClassIdArrayList().isEmpty() || !mindbodyVisitHistory.getClassIdWailistEntriesArrayList().isEmpty()){
                                            mindbodyVisitHistory.getClassInfo(new MindbodyClass.VolleyResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                                    Log.d("mindbody_response", response);

                                                    ArrayList<MindbodyClassModel> mindbodyClassModelArrayList = mindbodyVisitHistory.getMindbodyClassModelArrayList();

                                                    if(!mindbodyClassModelArrayList.isEmpty()) {

                                                        linearLayout.removeAllViews();

                                                        for(int index = 0; index < mindbodyClassModelArrayList.size(); index++) {

                                                            MindbodyClassModel mindbodyClassModelEx = mindbodyClassModelArrayList.get(index);

                                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            params.setMargins(0, 10, 0, 10);


                                                            final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.class_history_template, null);
                                                            cardView.setId(index);

                                                            TextView className;
                                                            TextView classTime;
                                                            TextView classDate;

                                                            RelativeLayout relativeLayout;

                                                            className = cardView.findViewById(R.id.class_name);
                                                            classTime = cardView.findViewById(R.id.class_time);
                                                            classDate = cardView.findViewById(R.id.class_date);

                                                            relativeLayout = cardView.findViewById(R.id.virtual_tag);

                                                            className.setText(mindbodyClassModelEx.getClassName());
                                                            classDate.setText(mindbodyClassModelEx.getStartDateCut());
                                                            classTime.setText(mindbodyClassModelEx.getStartTimeCut() + "-" + mindbodyClassModelEx.getEndTimeCut());




                                                            ConstraintLayout waitlistCondContainer = cardView.findViewById(R.id.waitlist_cond_container);
                                                            TextView waitlistCond = cardView.findViewById(R.id.waitlist_cond);

                                                            ConstraintLayout constraintLayout = cardView.findViewById(R.id.init_detail_info);


                                                            if(mindbodyClassModelEx.isWaitlist()){


                                                                constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        historyUpcomingWaitlistBSDF = new HistoryUpcomingWaitlistBSDF(mindbodyClassModelEx.getClassId(), mindbodyClassModelEx, getContext(), mindbodyClassModelEx.getWailistEntryId());
                                                                        historyUpcomingWaitlistBSDF.show(getChildFragmentManager().beginTransaction(), null);
                                                                    }
                                                                });


                                                                if(mindbodyClassModelEx.getProgramName().equals("Classes")){
                                                                    relativeLayout.setVisibility(View.INVISIBLE);
                                                                }

                                                                int waitlistOrder = mindbodyClassModelEx.getWailistOrder();
                                                                waitlistCond.setText(waitlistOrder + "/4");



                                                            }
                                                            else{

                                                                waitlistCondContainer.setVisibility(View.GONE);
                                                                if(mindbodyClassModelEx.getProgramName().equals("Classes")){

                                                                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            boolean lateCancel = checkLateCancel(mindbodyClassModelEx.getCancelOffset(), mindbodyClassModelEx.getStartTimestamp());
                                                                            historyUpcomingBottomSheetDialogFragment = new HistoryUpcomingBottomSheetDialogFragment(mindbodyClassModelEx.getClassId(), lateCancel, clientId, mindbodyClassModelEx, getContext());
                                                                            historyUpcomingBottomSheetDialogFragment.show(getChildFragmentManager().beginTransaction(), null);

                                                                        }
                                                                    });
                                                                    relativeLayout.setVisibility(View.INVISIBLE);
                                                                }
                                                                else{

                                                                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            boolean lateCancel = checkLateCancel(mindbodyClassModelEx.getCancelOffset(), mindbodyClassModelEx.getStartTimestamp());
                                                                            historyUpcomingOnlineBSDF = new HistoryUpcomingOnlineBSDF(mindbodyClassModelEx.getClassId(), lateCancel, clientId, mindbodyClassModelEx, getContext());
                                                                            historyUpcomingOnlineBSDF.show(getChildFragmentManager().beginTransaction(), null);

                                                                        }
                                                                    });
                                                                }
                                                            }



                                                            linearLayout.addView(cardView,params);
                                                            Log.d("child view", String.valueOf(linearLayout.getChildCount()));

                                                        }


                                                    }


                                                    swipeRefreshLayout.setRefreshing(false);


                                                }
                                            },endDate, endDate2, false);
                                        }


                                    }
                                }, clientId);
                            }
                        }, clientId);
                    }
                }, clientId, endDate, endDate2);
            }
        });

    }

    private void refreshHistory(String startDate,String endDate){

        String clientId = ((GlobalVariableApplication) getActivity().getApplication()).getClientId();

        MindbodyVisitHistory mindbodyVisitHistory1 = new MindbodyVisitHistory(context);
        mindbodyVisitHistory1.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                mindbodyVisitHistory1.getVisitHistoryInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);

                        mindbodyVisitHistory1.getClassInfoHistory(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                Log.d("HISTORY_response", response);

                                ArrayList<MindbodyClassModel> mindbodyClassModelArrayList = mindbodyVisitHistory1.getMindbodyClassModelArrayList();

                                if(!mindbodyClassModelArrayList.isEmpty()) {

                                    linearLayout2.removeAllViews();

                                    for(int index = 0; index < mindbodyClassModelArrayList.size(); index++) {

                                        MindbodyClassModel mindbodyClassModelEx = mindbodyClassModelArrayList.get(index);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(0, 10, 0, 10);


                                        final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.class_history_template, null);
                                        cardView.setId(index);

                                        TextView className;
                                        TextView classTime;
                                        TextView classDate;

                                        RelativeLayout relativeLayout;

                                        className = cardView.findViewById(R.id.class_name);
                                        classTime = cardView.findViewById(R.id.class_time);
                                        classDate = cardView.findViewById(R.id.class_date);

                                        relativeLayout = cardView.findViewById(R.id.virtual_tag);

                                        className.setText(mindbodyClassModelEx.getClassName());
                                        classDate.setText(mindbodyClassModelEx.getStartDateCut());
                                        classTime.setText(mindbodyClassModelEx.getStartTimeCut() + "-" + mindbodyClassModelEx.getEndTimeCut());

                                        ConstraintLayout waitlistCondContainer = cardView.findViewById(R.id.waitlist_cond_container);
                                        waitlistCondContainer.setVisibility(View.GONE);

                                        ConstraintLayout constraintLayout = cardView.findViewById(R.id.init_detail_info);

                                        constraintLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                historyBSDF = new HistoryBSDF(mindbodyClassModelEx.getClassId(), mindbodyClassModelEx, getContext());
                                                historyBSDF.show(getChildFragmentManager().beginTransaction(), null);

                                            }
                                        });

                                        if(mindbodyClassModelEx.getProgramName().equals("Classes")){
                                            relativeLayout.setVisibility(View.INVISIBLE);
                                        }

                                        linearLayout2.addView(cardView,params);
                                        Log.d("child view", String.valueOf(linearLayout2.getChildCount()));

                                    }

                                }

                                swipeRefreshLayout.setRefreshing(false);


                            }
                        },startDate, endDate, false);


                    }
                }, clientId, startDate, endDate);
            }
        });
    }



    public void refreshPage(){


        String clientId = ((GlobalVariableApplication) getActivity().getApplication()).getClientId();

        firebaseAuth = FirebaseAuth.getInstance();


        long startDate_timestamp = 0;

        if(firebaseAuth.getCurrentUser() != null){
            startDate_timestamp = firebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp();
        }

        Date date = new Date(startDate_timestamp);
        String startDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date);


        Date date2 = new Date(System.currentTimeMillis());
        String endDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", date2);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 13);
        int curr_month = calendar.get(Calendar.MONTH) + 1;
        int curr_date = calendar.get(Calendar.DAY_OF_MONTH);
        int curr_year = calendar.get(Calendar.YEAR);


        String endDate2 = curr_year + "-" + curr_month + "-" + curr_date;




        Log.d("startDate", startDate);
        Log.d("endDate", endDate);
        Log.d("endDate2", endDate2);



        MindbodyVisitHistory mindbodyVisitHistory = new MindbodyVisitHistory(context);
        mindbodyVisitHistory.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                mindbodyVisitHistory.getVisitHistoryInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);




                        mindbodyVisitHistory.getWaitlistEntries(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                Log.d("mindbody_response", response);


                                mindbodyVisitHistory.getWaitlistEntriesOrder(new MindbodyClass.VolleyResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                        Log.d("mindbody_response", response);


                                        if(!mindbodyVisitHistory.getClassIdArrayList().isEmpty() || !mindbodyVisitHistory.getClassIdWailistEntriesArrayList().isEmpty()){
                                            mindbodyVisitHistory.getClassInfo(new MindbodyClass.VolleyResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getActivity(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                                    Log.d("mindbody_response", response);

                                                    ArrayList<MindbodyClassModel> mindbodyClassModelArrayList = mindbodyVisitHistory.getMindbodyClassModelArrayList();

                                                    if(!mindbodyClassModelArrayList.isEmpty()) {

                                                        linearLayout.removeAllViews();

                                                        for(int index = 0; index < mindbodyClassModelArrayList.size(); index++) {

                                                            MindbodyClassModel mindbodyClassModelEx = mindbodyClassModelArrayList.get(index);

                                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                                            params.setMargins(0, 10, 0, 10);


                                                            final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.class_history_template, null);
                                                            cardView.setId(index);

                                                            TextView className;
                                                            TextView classTime;
                                                            TextView classDate;

                                                            RelativeLayout relativeLayout;

                                                            className = cardView.findViewById(R.id.class_name);
                                                            classTime = cardView.findViewById(R.id.class_time);
                                                            classDate = cardView.findViewById(R.id.class_date);

                                                            relativeLayout = cardView.findViewById(R.id.virtual_tag);

                                                            className.setText(mindbodyClassModelEx.getClassName());
                                                            classDate.setText(mindbodyClassModelEx.getStartDateCut());
                                                            classTime.setText(mindbodyClassModelEx.getStartTimeCut() + "-" + mindbodyClassModelEx.getEndTimeCut());




                                                            ConstraintLayout waitlistCondContainer = cardView.findViewById(R.id.waitlist_cond_container);
                                                            TextView waitlistCond = cardView.findViewById(R.id.waitlist_cond);

                                                            ConstraintLayout constraintLayout = cardView.findViewById(R.id.init_detail_info);


                                                            if(mindbodyClassModelEx.isWaitlist()){


                                                                constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        historyUpcomingWaitlistBSDF = new HistoryUpcomingWaitlistBSDF(mindbodyClassModelEx.getClassId(), mindbodyClassModelEx, getContext(), mindbodyClassModelEx.getWailistEntryId());
                                                                        historyUpcomingWaitlistBSDF.show(getChildFragmentManager().beginTransaction(), null);
                                                                    }
                                                                });


                                                                if(mindbodyClassModelEx.getProgramName().equals("Classes")){
                                                                    relativeLayout.setVisibility(View.INVISIBLE);
                                                                }

                                                                int waitlistOrder = mindbodyClassModelEx.getWailistOrder();
                                                                waitlistCond.setText(waitlistOrder + "/4");



                                                            }
                                                            else{

                                                                waitlistCondContainer.setVisibility(View.GONE);
                                                                if(mindbodyClassModelEx.getProgramName().equals("Classes")){

                                                                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            boolean lateCancel = checkLateCancel(mindbodyClassModelEx.getCancelOffset(), mindbodyClassModelEx.getStartTimestamp());
                                                                            historyUpcomingBottomSheetDialogFragment = new HistoryUpcomingBottomSheetDialogFragment(mindbodyClassModelEx.getClassId(), lateCancel, clientId, mindbodyClassModelEx, getContext());
                                                                            historyUpcomingBottomSheetDialogFragment.show(getChildFragmentManager().beginTransaction(), null);

                                                                        }
                                                                    });
                                                                    relativeLayout.setVisibility(View.INVISIBLE);
                                                                }
                                                                else{

                                                                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            boolean lateCancel = checkLateCancel(mindbodyClassModelEx.getCancelOffset(), mindbodyClassModelEx.getStartTimestamp());
                                                                            historyUpcomingOnlineBSDF = new HistoryUpcomingOnlineBSDF(mindbodyClassModelEx.getClassId(), lateCancel, clientId, mindbodyClassModelEx, getContext());
                                                                            historyUpcomingOnlineBSDF.show(getChildFragmentManager().beginTransaction(), null);

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            linearLayout.addView(cardView,params);
                                                            Log.d("child view", String.valueOf(linearLayout.getChildCount()));

                                                        }


                                                    }

                                                    if(!stoppedOnce){
                                                        stoppedOnce = true;
                                                    }
                                                    else{
                                                        swipeRefreshLayout.setRefreshing(false);
                                                        stoppedOnce = false;
                                                    }
                                                }
                                            },endDate, endDate2, true);
                                        }

                                        else{
                                            if(!stoppedOnce){
                                                stoppedOnce = true;
                                            }
                                            else{
                                                swipeRefreshLayout.setRefreshing(false);
                                                stoppedOnce = false;
                                            }
                                        }


                                    }
                                }, clientId);
                            }
                        }, clientId);
                    }
                }, clientId, endDate, endDate2);
            }
        });



        MindbodyVisitHistory mindbodyVisitHistory1 = new MindbodyVisitHistory(context);
        mindbodyVisitHistory1.getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                mindbodyVisitHistory1.getVisitHistoryInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);

                        mindbodyVisitHistory1.getClassInfoHistory(new MindbodyClass.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Something wrong, try this later", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                Log.d("HISTORY_response", response);

                                ArrayList<MindbodyClassModel> mindbodyClassModelArrayList = mindbodyVisitHistory1.getMindbodyClassModelArrayList();

                                if(!mindbodyClassModelArrayList.isEmpty()) {

                                    linearLayout2.removeAllViews();

                                    for(int index = 0; index < mindbodyClassModelArrayList.size(); index++) {

                                        MindbodyClassModel mindbodyClassModelEx = mindbodyClassModelArrayList.get(index);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params.setMargins(0, 10, 0, 10);


                                        final CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.class_history_template, null);
                                        cardView.setId(index);

                                        TextView className;
                                        TextView classTime;
                                        TextView classDate;

                                        RelativeLayout relativeLayout;

                                        className = cardView.findViewById(R.id.class_name);
                                        classTime = cardView.findViewById(R.id.class_time);
                                        classDate = cardView.findViewById(R.id.class_date);

                                        relativeLayout = cardView.findViewById(R.id.virtual_tag);

                                        className.setText(mindbodyClassModelEx.getClassName());
                                        classDate.setText(mindbodyClassModelEx.getStartDateCut());
                                        classTime.setText(mindbodyClassModelEx.getStartTimeCut() + "-" + mindbodyClassModelEx.getEndTimeCut());

                                        ConstraintLayout waitlistCondContainer = cardView.findViewById(R.id.waitlist_cond_container);
                                        waitlistCondContainer.setVisibility(View.GONE);

                                        ConstraintLayout constraintLayout = cardView.findViewById(R.id.init_detail_info);

                                        constraintLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                historyBSDF = new HistoryBSDF(mindbodyClassModelEx.getClassId(), mindbodyClassModelEx, getContext());
                                                historyBSDF.show(getChildFragmentManager().beginTransaction(), null);

                                            }
                                        });


                                        if(mindbodyClassModelEx.getProgramName().equals("Classes")){
                                            relativeLayout.setVisibility(View.INVISIBLE);
                                        }

                                        linearLayout2.addView(cardView,params);
                                        Log.d("child view", String.valueOf(linearLayout2.getChildCount()));

                                    }


                                }

                                if(!stoppedOnce){
                                    stoppedOnce = true;
                                }
                                else{
                                    swipeRefreshLayout.setRefreshing(false);
                                    stoppedOnce = false;
                                }
                            }
                        },startDate, endDate, true);


                    }
                }, clientId, startDate, endDate);
            }
        });
    }


    private boolean checkLateCancel(int cancelOffset, long startTimestamp){

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(today);
        calendar.add(Calendar.HOUR, cancelOffset);
        long compared = calendar.getTimeInMillis();


        if(compared > startTimestamp){
            return true;
        }
        else{
            return false;
        }
    }}
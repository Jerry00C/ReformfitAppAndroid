package com.example.reformfitapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MindbodyVisitHistory {


    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";


    private ArrayList<Integer> classIdArrayList;

    private ArrayList<Integer> classIdWailistEntriesArrayList;

    private HashMap<Integer, Long> classIdWrequestTimestamp;

    private HashMap<Integer, Integer> classIdWOrderWaitlist;

    private HashMap<Integer, Integer> classIdWWaitlistId;


    public ArrayList<Integer> getClassIdWailistEntriesArrayList() {
        return classIdWailistEntriesArrayList;
    }

    ArrayList<MindbodyClassModel> mindbodyClassModelArrayList;

    public ArrayList<MindbodyClassModel> getMindbodyClassModelArrayList() {
        return mindbodyClassModelArrayList;
    }

    public MindbodyVisitHistory(Context context) {
        this.context = context;
        classIdArrayList = new ArrayList<>();


        mindbodyClassModelArrayList = new ArrayList<>();
        classIdWailistEntriesArrayList = new ArrayList<>();

        classIdWOrderWaitlist = new HashMap<>();
        classIdWrequestTimestamp = new HashMap<>();
        classIdWWaitlistId = new HashMap<>();

    }

    public ArrayList<Integer> getClassIdArrayList() {
        return classIdArrayList;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String response);
    }

    public void getUserToken(MindbodyClass.VolleyResponseListener volleyResponseListener){
        String url = baseUrl + "usertoken/issue";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Username", "_ReformFIT");
        params.put("Password", "WEBdeveloper123!");

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userToken = response.getString("AccessToken");
                } catch (JSONException e) {
                    Log.d("ERROR",e.toString());
                }
                volleyResponseListener.onResponse(userToken);
                //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError(error.toString());
                //Log.d("ERROR",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Api-Key", api_key);
                headers.put("SiteId", "-99");
                return headers;
            }
        };
        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }

    public void getVisitHistoryInfo(MindbodyClass.VolleyResponseListener volleyResponseListener, String clientId, String startDate, String endDate){

        String url = baseUrl + "client/clientvisits?request.clientId=" + clientId + "&request.crossRegionalLookup=false&request.endDate=" + endDate + "&request.startDate=" + startDate;


        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject> () {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                JSONArray visit = null;
                try {
                    visit = response.getJSONArray("Visits");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int index = 0; index < visit.length(); index++){

                    JSONObject visitEx = null;
                    try {
                        visitEx = visit.getJSONObject(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        classIdArrayList.add(visitEx.getInt("ClassId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                volleyResponseListener.onResponse(response.toString());
                //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError(error.toString());
                //Log.d("ERROR",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Api-Key", api_key);
                headers.put("SiteId", "-99");
                headers.put("Authorization",userToken);
                return headers;
            }
        };
        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }



    public void getWaitlistEntries(MindbodyClass.VolleyResponseListener volleyResponseListener, String clientId){

        //https://api.mindbodyonline.com/public/v6/class/waitlistentries?request.clientIds=100015726
        String url = baseUrl + "class/waitlistentries?request.clientIds=" + clientId;



        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray waitlistEntries = null;
                try {
                    waitlistEntries = response.getJSONArray("WaitlistEntries");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int index = 0; index < waitlistEntries.length(); index++){
                    JSONObject waitlistEntriesEx = null;
                    try {
                        waitlistEntriesEx = waitlistEntries.getJSONObject(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int classId = 0;

                    try {
                        classId = waitlistEntriesEx.getInt("ClassId");
                        classIdWailistEntriesArrayList.add(classId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String requestDateTime = null;
                    try {
                        requestDateTime = waitlistEntriesEx.getString("RequestDateTime");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    long requestTimestamp = getTimestamp(requestDateTime);


                    int waitlistId = 0;
                    try {
                        waitlistId = waitlistEntriesEx.getInt("Id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    classIdWrequestTimestamp.put(classId, requestTimestamp);
                    classIdWOrderWaitlist.put(classId, 0);
                    classIdWWaitlistId.put(classId, waitlistId);
                }



                volleyResponseListener.onResponse(response.toString());
                //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError(error.toString());
                //Log.d("ERROR",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Api-Key", api_key);
                headers.put("SiteId", "-99");
                headers.put("Authorization",userToken);
                return headers;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }

    public void getWaitlistEntriesOrder(MindbodyClass.VolleyResponseListener volleyResponseListener, String clientId){


        if(classIdWailistEntriesArrayList.isEmpty()){
            volleyResponseListener.onResponse("No WaitlistEntries");
        }
        else{
            String url_2 = "";
            for(int index = 0; index < classIdWailistEntriesArrayList.size(); index++){
                //ClassIds={ClassId1}
                url_2 += "request.classIds=" + classIdWailistEntriesArrayList.get(index);

                if(index != classIdWailistEntriesArrayList.size()-1){
                    url_2 += "&";
                }

            }

            String url = baseUrl + "class/waitlistentries?" + url_2;



            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {


                    JSONArray waitlistEntries = null;
                    try {
                        waitlistEntries = response.getJSONArray("WaitlistEntries");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for(int index = 0; index < waitlistEntries.length(); index++){
                        JSONObject waitlistEntriesEx = null;
                        try {
                            waitlistEntriesEx = waitlistEntries.getJSONObject(index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int classIdEx = 0;
                        try {
                            classIdEx = waitlistEntriesEx.getInt("ClassId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject clientInfo = null;
                        try {
                            clientInfo = waitlistEntriesEx.getJSONObject("Client");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String clientIdEx = null;
                        try {
                            clientIdEx = clientInfo.getString("Id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String requestDateTime = null;
                        try {
                            requestDateTime = waitlistEntriesEx.getString("RequestDateTime");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if(clientIdEx != clientId){

                            long timestampThresh = classIdWrequestTimestamp.get(classIdEx);

                            long timestampCompare = getTimestamp(requestDateTime);

                            if(timestampThresh > timestampCompare){

                                classIdWOrderWaitlist.put(classIdEx, classIdWOrderWaitlist.get(classIdEx)+1);

                            }
                        }
                    }

                    volleyResponseListener.onResponse(response.toString());
                    //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError(error.toString());
                    //Log.d("ERROR",error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Api-Key", api_key);
                    headers.put("SiteId", "-99");
                    headers.put("Authorization",userToken);
                    return headers;
                }
            };

            req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // add the request object to the queue to be executed
            MySingleton.getInstance(context).addToRequestQueue(req);
        }


    }



    public void getClassInfo(MindbodyClass.VolleyResponseListener volleyResponseListener, String startDate, String endDate, boolean limited){

        classIdArrayList.addAll(classIdWailistEntriesArrayList);

        Log.d("classIds", classIdArrayList.toString());

        String url_2 = "";


        for(int index = 0; index < classIdArrayList.size(); index++){
            //ClassIds={ClassId1}
            url_2 += "request.classIds=" + classIdArrayList.get(index);

            if(index != classIdArrayList.size()-1){
                url_2 += "&";
            }

        }

        if(classIdArrayList.isEmpty()){
            volleyResponseListener.onResponse("No WaitlistEntries");
        }
        else {

            String url = baseUrl + "class/classes?" + url_2 + "&request.endDateTime=" + endDate + "&request.startDateTime=" + startDate;
            Log.d("class url", url);

            JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(JSONObject response) {


                    JSONArray classes = null;
                    try {
                        classes = response.getJSONArray("Classes");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int index = 0; index < classes.length(); index++) {



                        JSONObject classEx = null;
                        try {
                            classEx = classes.getJSONObject(index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String endDate = "";
                        try {
                            endDate = classEx.getString("EndDateTime");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String[] parts = endDate.split("T");
                        String date = parts[0];
                        String time = parts[1];
                        long timestamp = System.currentTimeMillis();

                        Date localTime = null;
                        try {
                            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long timestamp1 = localTime.getTime();


                        Log.d("curr time", String.valueOf(timestamp));
                        Log.d("compared time", String.valueOf(timestamp1));

                        if (timestamp1 > timestamp) {

                            MindbodyClassModel mindbodyClassModel = new MindbodyClassModel();

                            try {
                                mindbodyClassModel.setClassScheduleId(classEx.getInt("ClassScheduleId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int classIdEx = 0;
                            try {
                                classIdEx = classEx.getInt("Id");
                                mindbodyClassModel.setClassId(classIdEx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setStartDateandTime(classEx.getString("StartDateTime"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setEndDateandTime(classEx.getString("EndDateTime"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setMaxCapacity(classEx.getInt("MaxCapacity"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setTotalBookedWaitlist(classEx.getInt("TotalBookedWaitlist"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setTotalBooked(classEx.getInt("TotalBooked"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setWaitlistAvailable(classEx.getBoolean("IsWaitlistAvailable"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setAvailable(classEx.getBoolean("IsAvailable"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setVirtualStreamLink(classEx.getString("VirtualStreamLink"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JSONObject descriptions = null;
                            try {
                                descriptions = classEx.getJSONObject("ClassDescription");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setDescription(descriptions.getString("Description"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setClassName(descriptions.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JSONObject staffEx = null;
                            try {
                                staffEx = classEx.getJSONObject("Staff");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setStaff_name(staffEx.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                mindbodyClassModel.setStaff_des(staffEx.getString("Bio"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                mindbodyClassModel.setStaff_mageUrl(staffEx.getString("ImageUrl"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            String programName = null;
                            try {
                                programName = descriptions.getJSONObject("Program").getString("Name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            int cancelOffset = 0;
                            try {
                                cancelOffset = descriptions.getJSONObject("Program").getInt("CancelOffset");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mindbodyClassModel.setCancelOffset(cancelOffset);



                            mindbodyClassModel.setProgramName(programName);




                            JSONObject location = null;
                            try {
                                location = classEx.getJSONObject("Location");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setAddress(location.getString("Address") + location.getString("Address2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (classIdWailistEntriesArrayList.contains(classIdEx)) {
                                Log.d("history", "waitlist");
                                mindbodyClassModel.setWaitlist(true);
                                mindbodyClassModel.setWailistEntryId(classIdWWaitlistId.get(classIdEx));
                                mindbodyClassModel.setWailistOrder(classIdWOrderWaitlist.get(classIdEx));

                            } else {
                                mindbodyClassModel.setWaitlist(false);
                            }


                            addInOrder(mindbodyClassModel, limited);

                            Log.d("model" + index, mindbodyClassModelArrayList.toString());


                        }
                    }

                    volleyResponseListener.onResponse(response.toString());
                    //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError(error.toString());
                    //Log.d("ERROR",error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Api-Key", api_key);
                    headers.put("SiteId", "-99");
                    headers.put("Authorization", userToken);
                    return headers;
                }
            };
            // add the request object to the queue to be executed
            MySingleton.getInstance(context).addToRequestQueue(req);
        }
    }
    public void getClassInfoHistory(MindbodyClass.VolleyResponseListener volleyResponseListener, String startDate, String endDate, boolean limited){

        classIdArrayList.addAll(classIdWailistEntriesArrayList);

        Log.d("classIds", classIdArrayList.toString());

        String url_2 = "";
        for(int index = 0; index < classIdArrayList.size(); index++){
            //ClassIds={ClassId1}
            url_2 += "request.classIds=" + classIdArrayList.get(index);

            if(index != classIdArrayList.size()-1){
                url_2 += "&";
            }

        }

        if(classIdArrayList.isEmpty()){
            volleyResponseListener.onResponse("No WaitlistEntries");
        }
        else {

            String url = baseUrl + "class/classes?" + url_2 + "&request.endDateTime=" + endDate + "&request.startDateTime=" + startDate;
            Log.d("class url", url);

            JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(JSONObject response) {


                    JSONArray classes = null;
                    try {
                        classes = response.getJSONArray("Classes");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int index = 0; index < classes.length(); index++) {




                        JSONObject classEx = null;
                        try {
                            classEx = classes.getJSONObject(index);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String endDate = "";
                        try {
                            endDate = classEx.getString("EndDateTime");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String[] parts = endDate.split("T");
                        String date = parts[0];
                        String time = parts[1];
                        long timestamp = System.currentTimeMillis();

                        Date localTime = null;
                        try {
                            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long timestamp1 = localTime.getTime();


                        Log.d("curr time", String.valueOf(timestamp));
                        Log.d("compared time", String.valueOf(timestamp1));

                        if (timestamp1 < timestamp) {

                            MindbodyClassModel mindbodyClassModel = new MindbodyClassModel();

                            try {
                                mindbodyClassModel.setClassScheduleId(classEx.getInt("ClassScheduleId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int classIdEx = 0;
                            try {
                                classIdEx = classEx.getInt("Id");
                                mindbodyClassModel.setClassId(classIdEx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setStartDateandTime(classEx.getString("StartDateTime"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setEndDateandTime(classEx.getString("EndDateTime"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setMaxCapacity(classEx.getInt("MaxCapacity"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setTotalBookedWaitlist(classEx.getInt("TotalBookedWaitlist"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setTotalBooked(classEx.getInt("TotalBooked"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setWaitlistAvailable(classEx.getBoolean("IsWaitlistAvailable"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setAvailable(classEx.getBoolean("IsAvailable"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setVirtualStreamLink(classEx.getString("VirtualStreamLink"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            JSONObject descriptions = null;
                            try {
                                descriptions = classEx.getJSONObject("ClassDescription");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setDescription(descriptions.getString("Description"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                mindbodyClassModel.setClassName(descriptions.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JSONObject staffEx = null;
                            try {
                                staffEx = classEx.getJSONObject("Staff");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setStaff_name(staffEx.getString("Name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                mindbodyClassModel.setStaff_des(staffEx.getString("Bio"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                mindbodyClassModel.setStaff_mageUrl(staffEx.getString("ImageUrl"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            String programName = null;
                            try {
                                programName = descriptions.getJSONObject("Program").getString("Name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            int cancelOffset = 0;
                            try {
                                cancelOffset = descriptions.getJSONObject("Program").getInt("CancelOffset");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mindbodyClassModel.setCancelOffset(cancelOffset);



                            mindbodyClassModel.setProgramName(programName);




                            JSONObject location = null;
                            try {
                                location = classEx.getJSONObject("Location");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                mindbodyClassModel.setAddress(location.getString("Address") + location.getString("Address2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                            addInOrder(mindbodyClassModel, limited);

                            Log.d("model" + index, mindbodyClassModelArrayList.toString());


                        }
                    }

                    volleyResponseListener.onResponse(response.toString());
                    //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyResponseListener.onError(error.toString());
                    //Log.d("ERROR",error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Api-Key", api_key);
                    headers.put("SiteId", "-99");
                    headers.put("Authorization", userToken);
                    return headers;
                }
            };
            // add the request object to the queue to be executed
            MySingleton.getInstance(context).addToRequestQueue(req);
        }
    }





    private void addInOrder(MindbodyClassModel mindbodyClassModel, boolean limited){

        long timestamp = mindbodyClassModel.getStartTimestamp();

        for(int index = 0; index < mindbodyClassModelArrayList.size(); index++){

            long timestamp2 = mindbodyClassModelArrayList.get(index).getStartTimestamp();



            //Log.d("model time", String.valueOf(timestamp));
            //Log.d("sample time", String.valueOf(timestamp2));
            if(timestamp <= timestamp2){
                mindbodyClassModelArrayList.add(index, mindbodyClassModel);
                if(limited) {
                    if (mindbodyClassModelArrayList.size() > 3) {
                        mindbodyClassModelArrayList.remove(mindbodyClassModelArrayList.size() - 1);
                    }
                }
                return;
            }


        }

        mindbodyClassModelArrayList.add(mindbodyClassModel);

        if(limited) {
            if (mindbodyClassModelArrayList.size() > 3) {
                mindbodyClassModelArrayList.remove(mindbodyClassModelArrayList.size() - 1);
            }
        }




    }



    private long getTimestamp(String dateTime){
        String[] parts = dateTime.split("T");
        String date = parts[0];
        String time = parts[1];


        Date localTime = null;
        try {
            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localTime.getTime();

    }




}

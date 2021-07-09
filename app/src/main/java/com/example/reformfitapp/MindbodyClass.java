package com.example.reformfitapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class MindbodyClass {

    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";




    private MindbodyClassModelArray mindbodyClassModelArray = null;

    public MindbodyClassModelArray getMindbodyClassModelArray() {
        return mindbodyClassModelArray;
    }

    public MindbodyClass(Context context) {
        mindbodyClassModelArray = new MindbodyClassModelArray();
        this.context = context;


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

    public void getClassInfo(MindbodyClass.VolleyResponseListener volleyResponseListener, String start_date,String end_date, int classId){

        String url;
        /*if(classId == -1){
            url =
        }
        else{*/
            url = baseUrl + "class/classes?request.endDateTime=" + end_date+ "&request.limit=200&request.startDateTime=" + start_date;

        //}

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject> () {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray classes = response.getJSONArray("Classes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                Toast.makeText(context, response.toString(),Toast.LENGTH_SHORT).show();
                Log.d("Mindbody response", response.toString());

                JSONArray classes = null;
                try {
                    classes = response.getJSONArray("Classes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for(int index = 0; index < classes.length(); index++){
                    JSONObject classEx = null;
                    try {
                        classEx = classes.getJSONObject(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MindbodyClassModel mindbodyClassModel = new MindbodyClassModel();

                    try {
                        mindbodyClassModel.setClassScheduleId(classEx.getInt("ClassScheduleId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        mindbodyClassModel.setClassId(classEx.getInt("Id"));
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
                        mindbodyClassModel.setCancel(classEx.getBoolean("IsCanceled"));

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


                    //Parsing the date
                    LocalDate dateBefore = LocalDate.parse(start_date);
                    LocalDate dateAfter = LocalDate.parse(mindbodyClassModel.getStartDate());

                    //calculating number of days in between
                    int noOfDaysBetween = (int) ChronoUnit.DAYS.between(dateBefore, dateAfter);



                    if(programName.equals("Classes")){
                        MindbodyClassModelArray.setModels(noOfDaysBetween, mindbodyClassModel);

                    }
                    else if(programName.equals("Yoga")){
                        MindbodyClassModelArray.setModelsOnline(noOfDaysBetween, mindbodyClassModel);

                    }
                    else{
                        Log.d("class filter", programName);
                    }
                }




                Log.d("Mindbody response", mindbodyClassModelArray.toString());




                volleyResponseListener.onResponse(mindbodyClassModelArray.toString());
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


    public void classInfo(String start_date, String end_date, int start_page_pos){
        getUserToken(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                getClassInfo(new MindbodyClass.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);




                        Intent switchActivityIntent = new Intent(context, ServiceTabbed.class);

                        switchActivityIntent.putExtra("MindbodyCLassModelArray", mindbodyClassModelArray);
                        switchActivityIntent.putExtra("startPagePos", start_page_pos);

                        switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(switchActivityIntent);

                    }
                }, start_date, end_date, -1);
            }
        });
    }
}

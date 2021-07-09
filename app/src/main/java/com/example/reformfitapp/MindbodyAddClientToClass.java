package com.example.reformfitapp;

import android.content.Context;
import android.content.Intent;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class MindbodyAddClientToClass {


    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";





    public MindbodyAddClientToClass(Context context) {
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

    public void addClientToClass(MindbodyClass.VolleyResponseListener volleyResponseListener, String classId, String clientIdEx){

        String url = baseUrl + "class/addclienttoclass";

        String clientId = clientIdEx;

        HashMap<String, Object> params = new HashMap<String, Object>();


        params.put("ClientId", clientId);
        params.put("ClassId", classId);
        params.put("Test", false);
        params.put("RequirePayment", true);
        params.put("Waitlist", true);
        params.put("WaitlistEntryId", 0);
        params.put("ClientServiceId", 0);
        params.put("CrossRegionalBooking", false);
        params.put("CrossRegionalBookingClientServiceSiteId", 0);

        Log.d("params", new JSONObject(params).toString());


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject> () {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {


                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                Log.d("add_client", response.toString());


                volleyResponseListener.onResponse(response.toString());
                //Toast.makeText(context, userToken, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("ERROR",error.toString());
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                //final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                volleyResponseListener.onError(body);

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

        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public void addClient(String classId, MindbodyClassModel classEx, String clientId){

        addClientToClass(new MindbodyClass.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                Log.d("add_client", response);




                Intent switchActivityIntent = new Intent(context, ClassInfo.class);



                switchActivityIntent.putExtra("ClassId", classId);
                switchActivityIntent.putExtra("MindbodyClassModel", classEx);


                switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(switchActivityIntent);


            }
        }, classId, clientId);
    }
}

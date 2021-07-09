package com.example.reformfitapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

public class MindbodyRemoveFromWaitlist {


    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";



    public MindbodyRemoveFromWaitlist(Context context) {
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

    public void removeWaitlist(MindbodyClass.VolleyResponseListener volleyResponseListener, int waitlistIds){

        String url = baseUrl + "class/removefromwaitlist?request.waitlistEntryIds=" + waitlistIds;



        //}

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject> () {

            @Override
            public void onResponse(JSONObject response) {




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
}

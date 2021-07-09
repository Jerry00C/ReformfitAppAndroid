package com.example.reformfitapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;

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
import java.util.Map;

public class MindbodyRemoveClientFromClass {


    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";

    public MindbodyRemoveClientFromClass(Context context) {
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





    public void removeClientFromClass(MindbodyClass.VolleyResponseListener volleyResponseListener,HashMap<String, Object> params){



        String url = baseUrl + "class/removeclientfromclass";
        Log.d("remove from class", new JSONObject(params).toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                headers.put("Authorization", userToken);
                return headers;
            }
        };
        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);

    }





}

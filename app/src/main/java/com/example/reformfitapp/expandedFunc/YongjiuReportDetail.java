package com.example.reformfitapp.expandedFunc;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.reformfitapp.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YongjiuReportDetail {
    private static final String baseUrl = "https://open.youjiuhealth.com/api";
    private static final String baseurl_report = "https://op.youjiuhealth.com/report/detail/";
    private static final String app_id = "977771291791745";
    private static final String app_secret = "ZTkyMWU3ODljZWViZmI0NTA0MzA0MTcxNTRkMzM2OTY1ODg0N2UyZQ";


    Context context;

    String userToken;
    protected String phoneNum;
    protected String measurementId;



    public YongjiuReportDetail(Context context, String phoneNum, String measurementId) {
        this.context = context;
        this.phoneNum = phoneNum;
        this.measurementId = measurementId;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String response);
    }


    public void getUserToken(VolleyResponseListener volleyResponseListener){
        String url = baseUrl + "/session";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app_id", app_id);
        params.put("app_secret", app_secret);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userToken = response.getString("access_token");
                } catch (JSONException e) {
                    Log.d("ERROR",e.toString());
                }
                volleyResponseListener.onResponse(userToken);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError(error.toString());
            }
        });

        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }





    public String getReport2(String measurementIdEx, boolean english){

        long timestamp = System.currentTimeMillis() / 1000L;
        String userToken2 = "third." + measurementIdEx+ "." + app_id + "."+ timestamp + "."+ md5(app_id + app_secret + timestamp + measurementIdEx);


        String url =  null;

        if(english){
            url = "https://c.youjiuhealth.com/index.html#/pages/report/show/show?id=" + measurementIdEx + "&token=" + userToken2 + "&lang=en_CA";
        }
        else{
            url = "https://c.youjiuhealth.com/index.html#/pages/report/show/show?id=" + measurementIdEx + "&token=" + userToken2 + "&lang=zh_CN";
        }


       return url;

    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void reportRequest(VolleyResponseListener volleyResponseListener, boolean english){

        getUserToken(new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError(message);
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                Log.d("response",response.toString());


                String url = getReport2(measurementId, english);


                volleyResponseListener.onResponse(url);


            }
        });

    }
}

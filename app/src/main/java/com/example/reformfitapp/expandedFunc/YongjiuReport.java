package com.example.reformfitapp.expandedFunc;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.reformfitapp.MindbodyClassModel;
import com.example.reformfitapp.MySingleton;
import com.example.reformfitapp.R;

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

public class YongjiuReport {
    private static final String baseUrl = "https://open.youjiuhealth.com/api";
    private static final String baseurl_report = "https://op.youjiuhealth.com/report/detail/";
    private static final String app_id = "977771291791745";
    private static final String app_secret = "ZTkyMWU3ODljZWViZmI0NTA0MzA0MTcxNTRkMzM2OTY1ODg0N2UyZQ";


    Context context;

    String userToken;
    protected String phoneNum;
    protected ArrayList<String> measurementIdList;

    private ArrayList<YongjiuReportModel> yongjiuReportModelArrayList;

    private int check;


    public YongjiuReport(Context context, String phoneNum) {
        this.context = context;
        this.phoneNum = phoneNum;
        measurementIdList = new ArrayList<>();
        yongjiuReportModelArrayList = new ArrayList<>();
        check = 0;
    }

    public ArrayList<String> getMeasurementIdListA() {
        return measurementIdList;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String response);
    }
    public interface VolleyResponseListener2{
        void onError(String message);

        void onResponse(YongjiuReportModel yongjiuReportModel);
    }
    public interface VolleyResponseListener3{
        void onError(String message);

        void onResponse(ArrayList<YongjiuReportModel> yongjiuReportModelArrayList);
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
        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public void getMeasurementIdList(VolleyResponseListener volleyResponseListener){
        String url = baseUrl + "/reports?phone=" + phoneNum;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                // volleyResponseListener.onResponse(response.toString());

                JSONArray data = null;
                try {
                    data = response.getJSONArray("data");
                } catch (JSONException e) {
                    volleyResponseListener.onError(e.toString());
                }


                for(int i = 0; i < data.length(); i++){

                    JSONObject dataEx = null;
                    try {
                        dataEx = data.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {
                        measurementIdList.add(dataEx.getJSONObject("measurement").getString("id"));
                    } catch (JSONException e) {
                        volleyResponseListener.onError(e.toString());
                    }
                }

                volleyResponseListener.onResponse(measurementIdList.toString());
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
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + userToken);
                return headers;
            }
        };
        // add the request object to the queue to be executed
        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(req);
    }

    public void getMeasurementDetail(VolleyResponseListener2 volleyResponseListener, String measurementId){
        String url = baseUrl + "/reports/" + measurementId ;

        Log.d("detail", url);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                // volleyResponseListener.onResponse(response.toString());

                JSONObject data = null;
                try {
                    data = response.getJSONObject("data");
                } catch (JSONException e) {
                    volleyResponseListener.onError(e.toString());
                }

                JSONObject measurement = null;
                try {
                    measurement = data.getJSONObject("measurement");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                YongjiuReportModel yongjiuReportModel = new YongjiuReportModel();


                yongjiuReportModel.setId(measurementId);



                try {
                    yongjiuReportModel.setReportTime(measurement.getString("start_time"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    yongjiuReportModel.setWeight(measurement.getString("weight"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject outline = null;
                try {
                    outline = measurement.getJSONObject("outline");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    yongjiuReportModel.setBodyFat(outline.getString("pbf"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    yongjiuReportModel.setMuscleAmt(outline.getString("smm"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject composition = null;
                try {
                    composition = data.getJSONObject("composition");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    yongjiuReportModel.setMuscleIndex(composition.getString("ffmi"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                volleyResponseListener.onResponse(yongjiuReportModel);
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
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + userToken);
                return headers;
            }
        };
        // add the request object to the queue to be executed
//        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(req);
    }



    public void getReport2(String measurementIdEx, WebView webView){

        long timestamp = System.currentTimeMillis() / 1000L;
        String userToken2 = "third." + measurementIdEx+ "." + app_id + "."+ timestamp + "."+ md5(app_id + app_secret + timestamp + measurementIdEx);

        String url =  "https://c.youjiuhealth.com/index.html#/pages/report/show/show?id=" + measurementIdEx + "&token=" + userToken2 + "&lang=en_CA";


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setStandardFontFamily((String) "Time New Roman");
        webView.loadUrl(url);

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


    public void reportRequest(VolleyResponseListener3 volleyResponseListener){


        getUserToken(new YongjiuReport.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();

                volleyResponseListener.onError(1+message.toString());
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                Log.d("response",response.toString());


                getMeasurementIdList(new YongjiuReport.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onError(2+message.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("response",response.toString());

;

                        for(int i = 0; i < measurementIdList.size(); i++){

                            String measurementIdEx = measurementIdList.get(i);

                            getMeasurementDetail(new YongjiuReport.VolleyResponseListener2() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                                    volleyResponseListener.onError(3+message.toString());
                                }
                                @Override
                                public void onResponse(YongjiuReportModel yongjiuReportModel) {

                                    Toast.makeText(context, yongjiuReportModel.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("response",yongjiuReportModel.toString());



                                    addToArray(yongjiuReportModel);



                                    check++;
                                    if(check == measurementIdList.size()){
                                        volleyResponseListener.onResponse(yongjiuReportModelArrayList);
                                        check = 0;
                                    }

                                }
                            }, measurementIdEx);

                        }

                    }
                });

            }
        });

    }



    private void addToArray(YongjiuReportModel yongjiuReportModel){


        Log.d("compared id", yongjiuReportModel.getId());
        int id = Integer.parseInt(yongjiuReportModel.getId());

        for(int index = 0; index < yongjiuReportModelArrayList.size(); index++){

            long id2 = Integer.parseInt(yongjiuReportModelArrayList.get(index).getId());


            //Log.d("model time", String.valueOf(timestamp));
            //Log.d("sample time", String.valueOf(timestamp2));
            if(id > id2){
                yongjiuReportModelArrayList.add(index, yongjiuReportModel);

                return;
            }

        }
        yongjiuReportModelArrayList.add(yongjiuReportModel);

    }

}
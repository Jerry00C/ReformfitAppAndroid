package com.example.reformfitapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MindbodyLocation {

    private static MindbodyLocation instance;
    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";

    private MindbodyLocationModel mindbodyLocationModel = new MindbodyLocationModel();

    public MindbodyLocationModel getMindbodyLocationModel() {
        return mindbodyLocationModel;
    }


    public MindbodyLocation(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String response);
    }

    public void getUserToken(VolleyResponseListener volleyResponseListener){
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

    public void getLocationInfo(VolleyResponseListener volleyResponseListener){

        String url = baseUrl + "site/locations";

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray locations = null;
                try {
                    locations = response.getJSONArray("Locations");
                } catch (JSONException e) {
                    Log.d("ERROR",e.toString());
                }

               //for(int locationEx = 0; locationEx < locations.length(); locationEx++){
               for(int locationEx = 0; locationEx < 1; locationEx++){
                   JSONObject locationInfoEx = null;
                   try {
                       locationInfoEx = locations.getJSONObject(locationEx);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }



                   /*JSONArray locationImages = null;
                   try {
                       locationImages = locationInfoEx.getJSONArray("AdditionalImageURLs");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   ArrayList<String> imageUrls = new ArrayList<String>();
                   for(int imageEx = 0; imageEx < locationImages.length(); imageEx++){
                       try {
                           imageUrls.add(locationImages.getString(imageEx));
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }

                   mindbodyLocationModel.setAdditionalImageURLs(imageUrls);
*/
                   try {
                       mindbodyLocationModel.setAddress(locationInfoEx.getString("Address"));
                   } catch (JSONException e) {
                       e.printStackTrace();

                   }

                   try {
                       mindbodyLocationModel.setAddress2(locationInfoEx.getString("Address2"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   try {
                       mindbodyLocationModel.setDescription(locationInfoEx.getString("Description"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   try {
                       mindbodyLocationModel.setLat(locationInfoEx.getDouble("Latitude"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   try {
                       mindbodyLocationModel.setLon(locationInfoEx.getDouble("Longitude"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try {
                       mindbodyLocationModel.setName(locationInfoEx.getString("Name"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try {
                       mindbodyLocationModel.setPhone(locationInfoEx.getString("Phone"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   try {
                       mindbodyLocationModel.setPostalCode(locationInfoEx.getString("PostalCode"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }


                volleyResponseListener.onResponse(mindbodyLocationModel.toString());
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


    public void locationInfo(){
        getUserToken(new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                getLocationInfo(new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);




                        Intent switchActivityIntent = new Intent(context, LocationInfo.class);

                        switchActivityIntent.putExtra("MindbodyLocationModel", mindbodyLocationModel);

                        switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(switchActivityIntent);

                    }
                });
            }
        });
    }

}

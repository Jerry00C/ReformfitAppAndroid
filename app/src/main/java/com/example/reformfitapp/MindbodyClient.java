package com.example.reformfitapp;

import android.app.Application;
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

import java.util.HashMap;
import java.util.Map;

public class MindbodyClient {
    private static MindbodyLocation instance;
    private static final String baseUrl = "https://api.mindbodyonline.com/public/v6/";

    private Context context;
    private String userToken;

    private static final String api_key = "75d68925737844f4ac6a7d990ac11414";

    MindbodyClientResponseModel mindbodyClientResponseModel = null;

    public MindbodyClientResponseModel getMindbodyClientResponseModel() {
        return mindbodyClientResponseModel;
    }



    public MindbodyClient(Context context) {
        this.context = context;
        mindbodyClientResponseModel = new MindbodyClientResponseModel();
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String response);
    }

    public void getUserToken(MindbodyLocation.VolleyResponseListener volleyResponseListener){
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

    public void getClientInfo(MindbodyLocation.VolleyResponseListener volleyResponseListener, String clientId){

        String url = baseUrl + "client/clients?request.clientIDs=" + clientId;

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject client = null;
                try {
                    client = response.getJSONArray("Clients").getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    mindbodyClientResponseModel.setBirthDate(client.getString("BirthDate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setCountry(client.getString("Country"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setCreationDate(client.getString("CreationDate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setFirstName(client.getString("FirstName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setClientId(client.getString("Id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setLastName(client.getString("LastName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setUniqueId(String.valueOf(client.getInt("UniqueId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setEmail(client.getString("Email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setMobilePhone(client.getString("MobilePhone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setAddressLine1(client.getString("AddressLine1"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setAddressLine2(client.getString("AddressLine2"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setCity(client.getString("City"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setPostalCode(client.getString("PostalCode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setPhoteUrl(client.getString("PhotoUrl"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    mindbodyClientResponseModel.setGender(client.getString("Gender"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray customField = null;
                try {
                    customField = client.getJSONArray("CustomClientFields");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int index = 0; index < customField.length(); index++){
                    JSONObject customFieldEx = null;
                    try {
                        customFieldEx = customField.getJSONObject(index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String fieldName = null;
                    try {
                        fieldName = customFieldEx.getString("Name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //TODO: need to change later
                    //Height
                    if(fieldName.equals("Employer")){

                        try {
                            mindbodyClientResponseModel.setHeight(customFieldEx.getString("Value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //Wieght
                    else if(fieldName.equals("Health Preferences")){

                        try {
                            mindbodyClientResponseModel.setWeight(customFieldEx.getString("Value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //wristband brand
                    else if(fieldName.equals("Contract Canceled")){

                        try {
                            mindbodyClientResponseModel.setWristBandBrand(customFieldEx.getString("Value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //wristband number
                    else if(fieldName.equals("Progressive Swim Level")){

                        try {
                            mindbodyClientResponseModel.setWristBandNum(customFieldEx.getString("Value"));
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        // add the request object to the queue to be executed
        MySingleton.getInstance(context).addToRequestQueue(req);
    }


    public void clientInfo(String clientId, Application activity, int currpos, boolean classInfo){
        getUserToken(new MindbodyLocation.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                getClientInfo(new MindbodyLocation.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        Log.d("mindbody_response", response);



                        ((GlobalVariableApplication) activity).setClientId(clientId);
                        ((GlobalVariableApplication) activity).setMindbodyClientResponseModel(mindbodyClientResponseModel);
                        ((GlobalVariableApplication) activity).setLogIn(true);


                        if(!classInfo){
                            Intent switchActivityIntent = new Intent(context, MainBottomNaviService.class);

                            switchActivityIntent.putExtra("Fab", "fab");
                            switchActivityIntent.putExtra("CurrPos", currpos);

                            switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(switchActivityIntent);

                        }
                        else{
                            Intent switchActivityIntent = new Intent(context, Class.class);


                            switchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(switchActivityIntent);

                        }


                    }
                }, clientId);
            }
        });
    }
}

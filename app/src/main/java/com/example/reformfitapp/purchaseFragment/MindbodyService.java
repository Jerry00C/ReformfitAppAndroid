package com.example.reformfitapp.purchaseFragment;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.reformfitapp.MySingleton;
import com.example.reformfitapp.PaymentHistoryElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MindbodyService {

    public static final String base_url = "https://api.mindbodyonline.com/public/v6/";

    String accessToken;
    Context context;

    public MindbodyService(Context context) {
        this.context = context;
    }

    ///////////////////////////////////////////////////// interfaces //////////////////////////////////////////////////

    public interface RequestListener{
        void onError(String errorMessage);

        void onResponse(String authToken);

        void onResponse(JSONObject response);

        void onResponse(JSONArray jsonArrayResponse) ;

        void onResponse(List<PaymentHistoryElement> paymentHistoryList);


    }
    public interface AuthTokenResponseListener {
        void onError(String errorMessage);

        void onResponse(String authToken);
    }// combined

    public interface CheckoutShoppingCartListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response);
    }// combined

    public interface ExtractClientContractsListener {
        void onError(String errorMessage);

        void onResponse(JSONArray jsonArrayResponse) throws JSONException;
    }// combined

    public interface PurchaseContractListener {
        void onError(String errorMessage);

        void onResponse(JSONObject jsonArrayResponse) throws JSONException;
    }

    public interface ExtractClientServicesListener {
        void onError(String errorMessage);

        void onResponse(JSONArray jsonArrayResponse) throws JSONException;
    }

    public interface GetProgramListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response) throws JSONException;
    }

    public interface GetServiceListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response) throws JSONException;
    }

    public interface GetPromoCodeListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response) throws JSONException;
    }

    public interface UpdateClientCreditCardListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response);
    }

    public interface GetClientCreditCardListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response);

        void onNullResponse();
    }

    public interface AddClientDirectDebitInfoListener {
        void onError(String errorMessage);

        void onResponse(JSONObject response);

    }

    public interface ExtractClientPaymentHistoryListener {
        void onError(String errorMessage);

        void onResponse(List<PaymentHistoryElement> paymentHistoryList);

        void onResponse(JSONObject response);

    }

    public interface GetDirectDebitInfoListener {
        void onError(String errorMessage);


        void onResponse(JSONObject response);

    }

    public interface GetContractsListener{
        void onError(String errorMessage);


        void onResponse(JSONArray arrayResponse);
    }

    public interface GetGiftCardBalanceListener {
        void onError(String errorMessage);


        void onResponse(JSONObject response);
    }



//    public interface GetClientCreditCardListener
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////// methods /////////////////////////////////////////////////////////////


    public void getAuthToken(AuthTokenResponseListener authTokenResponseListener) {
        String url = base_url + "usertoken/issue";
        HashMap<String, String> params = new HashMap<>();
        params.put("Username", "_ReformFIT");
        params.put("Password", "WEBdeveloper123!");


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = "";
                String tokenid = "";
                try {
                    token = response.getString("TokenType");
                    tokenid = response.getString("AccessToken");
                    accessToken = tokenid;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                authTokenResponseListener.onResponse(tokenid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                authTokenResponseListener.onError("Token not get ");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
    /* this version is for test purpose, since snadbox site requires a contract id for it to work */
    public void getContracts(GetContractsListener getContractsListener, int locationId, int contractId){
        String url = base_url +"sale/contracts?request.locationId="+locationId+"&request.contractIds="+contractId;



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getContractsListener.onResponse(response.getJSONArray("Contracts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                getContractsListener.onError(body);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getContracts(GetContractsListener getContractsListener, int locationId){
        String url = base_url +"sale/contracts?request.locationId="+locationId+"&request.contractIds=353";



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getContractsListener.onResponse(response.getJSONArray("Contracts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                getContractsListener.onError(body);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    /////////////// routine to get programs with schedule type of Class////////////
    public void getPrograms(GetProgramListener getProgramListener) {
        String url = base_url + "site/programs?request.scheduleType=Class";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getProgramListener.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                getProgramListener.onError(body);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

/////////////// routine to get the services with a specific program id ///////////

    public void getServicesOfProgram(GetServiceListener getServiceListener, int programId) {
        String url = base_url + "sale/services?request.programIds=" + String.valueOf(programId);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getServiceListener.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                getServiceListener.onError(body);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    public void postCheckoutShoppingCart(CheckoutShoppingCartListener checkoutShoppingCartListener, ShoppingCartElement shoppingCartElement) {
        JSONObject paramsInJson = shoppingCartElement.toJsonObject();
        Log.d("ShoppingCartElem",paramsInJson.toString());
        String url = base_url + "sale/checkoutshoppingcart";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramsInJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkoutShoppingCartListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                checkoutShoppingCartListener.onError(body);
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;

            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getGiftCardBalance(GetGiftCardBalanceListener getGiftCardBalanceListener, String barcodeId){
        String url = base_url + "sale/giftcardbalance?barcodeId="+barcodeId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("GiftCardBalance", response.toString());
                getGiftCardBalanceListener.onResponse(response);





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    getGiftCardBalanceListener.onError("got null: no debit card stored on this client Id ");
                    Log.d("GiftCardBalanceError", error.toString());
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                Log.d("JSONobject", body);
                getGiftCardBalanceListener.onError(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);



    }

    public void getClientDirectDebit(GetDirectDebitInfoListener getDirectDebitInfoListener, String clientId){
        String url = base_url + "client/clientdirectdebitinfo?clientId="+clientId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    Log.d("JSONobject", response.toString());
                    getDirectDebitInfoListener.onResponse(response);





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    getDirectDebitInfoListener.onError("got null: no debit card stored on this client Id ");
                    Log.d("Volle", error.toString());
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                Log.d("JSONobject", body);
                getDirectDebitInfoListener.onError(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);






    }

    public void getClientCreditCard(GetClientCreditCardListener getClientCreditCardListener, String clientId){
        String url = base_url + "client/clients?request.clientIDs=" + clientId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject client = null;
                try {
                    client = (JSONObject) response.getJSONArray("Clients").get(0);
                    Log.d("clientCreCard",client.getString("ClientCreditCard"));
                    if (client.getString("ClientCreditCard")=="null"){
                        getClientCreditCardListener.onNullResponse();
                    }
                    else {
                        JSONObject clientCreditCardInfo = client.getJSONObject("ClientCreditCard");
//                    Log.d("JSONobject", clientCreditCardInfo.toString());

                        getClientCreditCardListener.onResponse(clientCreditCardInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                getClientCreditCardListener.onError(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);



    }


    public void postUpdateClientCreditCard(UpdateClientCreditCardListener updateClientCreditCardListener, HashMap<String, Object> params) {
        JSONObject paramsInJson = new JSONObject(params);
        String url = base_url + "client/updateclient";

        Log.d("JSONobject", paramsInJson.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramsInJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateClientCreditCardListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
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
                updateClientCreditCardListener.onError(body);
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;

            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    public void postAddClientDirectDebit(AddClientDirectDebitInfoListener addClientDirectDebitInfo, HashMap<String, Object> params) {

        JSONObject paramsInJson = new JSONObject(params);
        String url = base_url + "client/addclientdirectdebitinfo";

        Log.d("JSONobject", paramsInJson.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramsInJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addClientDirectDebitInfo.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
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
                addClientDirectDebitInfo.onError(body);
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;

            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }

//todays access token: 97505e98fece4f57a2acb9f715fee3a0a74c1e43e3cb4519971deacf72ea6677
// class id: 19365, service product id: 1357, client id :100015484
// second token : 45d1685b3aa4475ca2a71b51d75908e1e2cdb350f3554bf4a864d4697e90f152


    public void extractClientContracts(ExtractClientContractsListener extractClientContractsListener, String clientId) {
        String url = base_url + "client/clientcontracts?request.clientId=" + clientId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray contracts = response.getJSONArray("Contracts");
                    extractClientContractsListener.onResponse(contracts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                extractClientContractsListener.onError(body);

            }
        }) {

            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;

            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    public boolean getAvailableContract(String clientId, int contractId) {
        ArrayList<Integer> clientContracts = new ArrayList<>();
        this.extractClientContracts(new ExtractClientContractsListener() {
            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }

            @Override
            public void onResponse(JSONArray jsonArrayResponse) throws JSONException {
                for (int i = 0; i < jsonArrayResponse.length(); i++) {

                    JSONObject oneContract = (JSONObject) jsonArrayResponse.get(i);

                    clientContracts.add(oneContract.getInt("Id"));
                }
            }
        }, clientId);

        return clientContracts.contains(contractId);
        // client already purchased the contract

    }


    public void purchaseContract(PurchaseContractListener purchaseContractListener, ContractElement contractElement) {
        String url = base_url + "sale/purchasecontract";

        JSONObject paramsInJson = contractElement.toJSONObject();
        Log.d("JSONobject", paramsInJson.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramsInJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    purchaseContractListener.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body = "";
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "PP";
                    // exception
                }
                purchaseContractListener.onError(body);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    public void extractClientPurchasedItems(String clientId, String startDate, String endDate, ExtractClientPaymentHistoryListener extractClientPaymentHistoryListener) {
        String url = base_url + "client/clientpurchases?request.clientId=" + clientId + "&request.endDate=" + endDate + "&request.startDate=" + startDate;
        List<PaymentHistoryElement> paymentHistoryList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    extractClientPaymentHistoryListener.onResponse(response);

                    JSONArray purchases = response.getJSONArray("Purchases");
                    for (int i = 0; i < purchases.length(); i++) {

                        // get one purchase at a time
                        JSONObject one_purchase_from_api = (JSONObject) purchases.get(i);

                        String date = one_purchase_from_api.getJSONObject("Sale").getString("SaleDate");


                        JSONArray purchasedItems = one_purchase_from_api.getJSONObject("Sale").getJSONArray("PurchasedItems");

                        for (int it = 0; it < purchasedItems.length(); it++) {
                            PaymentHistoryElement oneElement = new PaymentHistoryElement();

                            JSONObject onePurchasedItem = (JSONObject) purchasedItems.get(it);


                            String title = onePurchasedItem.getString("Description");
                            String total_amount = "";
                            double amount = onePurchasedItem.getDouble("TotalAmount");
                            if (amount < 0) {
                                total_amount = "-$" + amount;
                            } else if (amount >= 0) {
                                total_amount = "$" + amount;
                            }
                            String purchaseDay = changeDateFormat(date);
                            oneElement.setTitle(title);
                            oneElement.setAmount(total_amount);
                            oneElement.setPurchase_date(purchaseDay);
                            paymentHistoryList.add(oneElement);
                            Log.d("ONE_PURCHASE",oneElement.toString());

                        }


                    }
                    extractClientPaymentHistoryListener.onResponse(paymentHistoryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                extractClientPaymentHistoryListener.onError(body);

            }
        }) {

            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;

            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }

    ////////////////////////////// to extract all services purchased by the client
    public void extractClientPurchasedServices(ExtractClientServicesListener extractClientServiceListener, String clientId) {
        String url = base_url + "client/clientservices?request.clientId=" + clientId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray services = null;
                try {
                    services = response.getJSONArray("ClientServices");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    extractClientServiceListener.onResponse(services);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = "";
                    // exception
                }
                extractClientServiceListener.onError(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("API-KEY", "75d68925737844f4ac6a7d990ac11414");
                headers.put("SiteId", "-99");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public String changeDateFormat(String endDate) {
        String[] parts = endDate.split("T");
        String date = parts[0];


        Date localTime = null;

        try {
            localTime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(localTime);

    }
}
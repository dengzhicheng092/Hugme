package com.qboxus.hugme.Volley_Package;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.qboxus.hugme.Code_Classes.Functions;
import com.qboxus.hugme.Code_Classes.Variables;
import com.qboxus.hugme.R;

import java.util.HashMap;
import java.util.Map;


public class ApiRequest {


    public static void Call_Api(final Context context, final String API_link,
                                final JSONObject jsonObject, final CallBack callBack ){


        Functions.Log_d_msg(context,API_link);
        if(jsonObject!=null)
            Functions.Log_d_msg(context,jsonObject.toString());

        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObj = new JsonObjectRequest(API_link,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Functions.Log_d_msg(context,response.toString());

                    if(callBack != null)
                        callBack.Get_Response("Post",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(callBack != null)
                        callBack.Get_Response("Post",error.toString());

                }
            });

            queue.add(jsonObj);
            jsonObj.setRetryPolicy(new DefaultRetryPolicy(
                    Variables.MY_API_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }catch(Exception e){

        }

    }


    public static void Send_Notification(final Context context, final JSONObject jsonObject, final CallBack callback ){

        Log.d(Variables.tag,jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(Variables.tag,response.toString());

                        if(callback!=null)
                            callback.Get_Response("Post",response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if(callback!=null)
                    callback.Get_Response("Post",error.toString());

                Log.d(Variables.tag+" error",error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization","key="+context.getResources().getString(R.string.firebase_server_key));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        if(Functions.isConnectedToInternet(context)) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjReq);
        }
        else {

        }
    }


}

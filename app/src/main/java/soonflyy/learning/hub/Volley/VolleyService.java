package soonflyy.learning.hub.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VolleyService {
    VolleyResponseListener callback ;
    Activity mContext;
    ProgressDialog progressBar;

    //region(Constructor)
    public VolleyService(VolleyResponseListener listener,Activity context){
        callback = listener;
        mContext = context;
        progressBar = new ProgressDialog(context);
    }

    public void postDataVolley(final int requestType, final String path,
                               final Map<String,String> getParams){
        try {
            progressBar.show();
            progressBar.setMessage("Loading...");
            progressBar.setCanceledOnTouchOutside(false);
            StringRequest request = new StringRequest(Request.Method.POST,
                    path,
                    response -> {
                        CommonMethods.toPrettyFormat(response,
                                0);
                        progressBar.dismiss();
                        if(callback != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                               // if (jsonObject.getBoolean("status")) {
                                    callback.onResponse(requestType, jsonObject.toString());
                              //  }else {
                                    //CommonMethods.generalAlert(mContext,jsonObject.getString("message"));
                               // }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("clientimgError", e.getMessage());
                            }
                        }
                    }, error -> {
                progressBar.dismiss();
                if(callback != null)
                    CommonMethods.generalAlert(mContext,"Internal Server Error");
            }) {
                @Override
                protected Map<String, String> getParams() {
                    CommonMethods.toPrettyFormat(getParams.toString(),
                            1);
                    return getParams;
                }
            };
            CommonMethods.requestQueue(mContext,request);
        }catch(Exception e){
            progressBar.dismiss();
            Log.e("EXCEPTION", e.toString());
        }
    }

    public  static  <T> Object response(String response,Class<T> clazz){
        Object model = null;
        try {
            JSONObject jsonObject  = new JSONObject(response);
            model = new Gson().fromJson(jsonObject.toString(),clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public  static  <T> List<T> stringToArray(String s, Class<T> clazz)  {
        T arr = new Gson().fromJson(s, clazz);
        return  Arrays.asList(arr);
    }
    //endregion
}

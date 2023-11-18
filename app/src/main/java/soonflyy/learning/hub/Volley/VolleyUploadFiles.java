package soonflyy.learning.hub.Volley;

import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_COMPLETE;
import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_ERROR;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import soonflyy.learning.hub.Common.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyUploadFiles {
    Context context;
    private String upload_URL ;
    private RequestQueue rQueue;
    private HashMap<String,String>params=new HashMap<>();
    private ArrayList<Uri>fileUriList=new ArrayList<>();
    private ArrayList<HashMap<String, String>> arraylist;
    String url = "https://www.google.com";


    public VolleyUploadFiles(Context context, String upload_URL, HashMap<String, String> params) {
        this.context = context;
        this.upload_URL = upload_URL;
        this.params = params;
       // this.fileUriList = fileUriList;
    }
    public void upload(){
        Toast.makeText(context,""+fileUriList.size(),Toast.LENGTH_SHORT).show();
       // uploadFiles("file.mp4",fileUriList.get(0));
    }

    public  void uploadFiles(final String fileName, Uri pdffile , ResultReceiver resultReceiver){//Uri pdffile
        Log.e("upload_methods","running");
        InputStream iStream = null;
        try {

            iStream = context.getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));

                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                if (jsonObject.getBoolean("status")) {
                                    CommonMethods.showSuccessToast(context, "Video Uploaded Successfully");
                                  if (resultReceiver!=null) {
                                      Bundle b = new Bundle();
                                      resultReceiver.send(FILE_UPLOAD_COMPLETE, b);
                                  }
                                }

                              /*  jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {
                                    Log.d("come::: >>>  ","yessssss");
                                    arraylist = new ArrayList<HashMap<String, String>>();
                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                        url = dataobj.optString("pathToFile");
                                       // tv.setText(url);
                                    }

                               */


                              //  }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Bundle b = new Bundle();
                                resultReceiver.send(FILE_UPLOAD_ERROR, b);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CommonMethods.showSuccessToast(context, error.getMessage());
                            Bundle b = new Bundle();
                            resultReceiver.send(FILE_UPLOAD_ERROR, b);
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                   // Map<String, String> prm = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                  // prm.put("course_id","2");
                    //prm.put("type","demo");
                    //
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Log.e("upload_methods","uploading videos");
                    Map<String, DataPart> prm = new HashMap<>();
                    prm.put("file", new DataPart(fileName ,inputData));//filename
                    return prm;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(context);
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



}

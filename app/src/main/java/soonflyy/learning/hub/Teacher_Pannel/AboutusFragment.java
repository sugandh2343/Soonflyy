package soonflyy.learning.hub.Teacher_Pannel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AboutusFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    ImageView arrow_back_img;
    TextView tv_title,tv_data;
    String title,type;


    public AboutusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_aboutus, container, false);
        initview(view);
        title=getArguments ().getString ("title");
        type=getArguments ().getString ("type");

        if(title.equals ("About Us")){
            title="About Us";
        }
        else if(title.equals ("condition"))
        {
            title="Terms and Condition";
        }
        else if(title.equals ("contact"))
        {
            title="Contact Us";
        }
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.FRONTEND_CODE);
        }

        return  view;
    }

    private void initview(View v) {
        tv_data=v.findViewById (R.id.tv_data);
        tv_title=v.findViewById (R.id.tv_title);
        arrow_back_img =v.findViewById(R.id.arrow_back_img);
        arrow_back_img.setOnClickListener(this);

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.FRONTEND_CODE :
                callApi(ApiCode.FRONTEND_CODE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback,getActivity());
        switch (request){
            case ApiCode.FRONTEND_CODE:
                service.postDataVolley(ApiCode.FRONTEND_CODE,
                        BaseUrl.URL_FRONTEND_SETTING, params);
                break;

        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.arrow_back_img){
            if(type.equals("student")){
                Intent intent = new Intent (requireActivity ( ), MainActivity.class);
                startActivity (intent);
            }
            else {
                Intent intent = new Intent (requireActivity ( ), TeacherMainActivity.class);
                startActivity (intent);
            }
        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.FRONTEND_CODE:
                Log.d("front edn setting : ",response);
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                        try {
                            boolean res = jsonObject.getBoolean("status") ;
                            if (res)
                            {
                                JSONObject object = jsonObject.getJSONObject("data");

                               if (title.equals("About Us")){
                                   tv_data.setText(object.getString("about_us"));
                                }else if (title.equals("Terms and Condition")) {
                                    tv_data.setText(object.getString("terms_and_condition"));
                                }else if (title.equals("Contact Us")) {
                                   tv_data.setText(object.getString("contact_us"));
                               }


                            }
                        }
                         catch (JSONException e) {
                             e.printStackTrace();
                         }
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(type.equals("teacher"))
        //( (TeacherMainActivity)getActivity()).setChildActionBar(title);
            ((TeacherMainActivity)getActivity()).setTeacherActionBar(title,false);
        else
            ((MainActivity)getActivity()).setStudentChildActionBar(title,false);
    }
}
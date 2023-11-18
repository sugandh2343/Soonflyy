package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TeacherHomeFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    CardView card_create_course, card_mycourse;

   ArrayList<Notification_model> notificationList = new ArrayList<>();

    public TeacherHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_home, container, false);
        // ((TeacherMainActivity)getActivity()).drawerToggle.setDrawerIndicatorEnabled(true);
        initview(view);
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_NOTIFICATION);
        }
        return view;
    }

    private void initview(View view) {

        card_create_course = view.findViewById(R.id.card_create_course);
        card_mycourse = view.findViewById(R.id.card_mycourse);

        card_create_course.setOnClickListener(this);
        card_mycourse.setOnClickListener(this);

    }




    public void onClick(View v) {

        if (v.getId() == R.id.card_create_course) {

            Create_Course_MainFragment fragment = new Create_Course_MainFragment();



            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        }
        if (v.getId() == R.id.card_mycourse) {
            //Intent intent=new Intent (getContext (), CreateCourseFragment.class);
            // startActivity (intent);
            MyCourseFragment fragment = new MyCourseFragment();
            Bundle arg = new Bundle();
            arg.putString("type", "add");
            fragment.setArguments(arg);
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        }
//
    }
//


    @Override
    public void onResume() {
        super.onResume();


       ((TeacherMainActivity) getActivity()).drawerToggle.setDrawerIndicatorEnabled(false);
      ((TeacherMainActivity) getActivity()).setTeacherActionBar("", true);

//        ((TeacherMainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        ((TeacherMainActivity) getActivity()).drawerToggle.setDrawerIndicatorEnabled(true);
//        ((TeacherMainActivity) getActivity()).drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.primary_color));
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_NOTIFICATION:
                params.put("user_id", new SessionManagement(getActivity()).getString(USER_ID));
//                callApi(ApiCode.GET_NOTIFICATION, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {

            case ApiCode.GET_NOTIFICATION:
                service.postDataVolley(ApiCode.GET_NOTIFICATION,
                        BaseUrl.URL_GET_NOTIFICATION, params);
                break;

        }
    }


    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.GET_NOTIFICATION:
                Log.e("notification", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        List<Notification_model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Notification_model>>() {
                                        }.getType());
                   notificationList.clear();
                        notificationList.addAll(psearch);
                        try {
                            ((TeacherMainActivity) getActivity()).setNotifications(notificationList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        setNotificationCount(notificationList.size());
                    } else {
                        notificationList.clear();
                        try {
                            ((TeacherMainActivity) getActivity()).setNotifications(notificationList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
//                        setNotificationCount(notificationList.size());
                    }
                } else {
                    notificationList.clear();
                    try {
                        ((TeacherMainActivity) getActivity()).setNotifications(notificationList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    setNotificationCount(notificationList.size());
                }
                break;

        }
    }

}
package soonflyy.learning.hub.Common_Package.Common_Package;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.NotificationAdapter;
import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;


public class NotificationFragment extends Fragment implements VolleyResponseListener {
    SwipeRefreshLayout refreshLayout;
    RecyclerView rec_notification;
    LinearLayout lin_header;

    ArrayList<Notification_model>notificationList=new ArrayList<>();
    NotificationAdapter notificationAdapter;


    String from;

    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        bindViewId(view);
        getArgumentData();
        refresh();
        return view;

    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        notificationList.addAll(getArguments().getParcelableArrayList("notifications"));
      //  setNotification();
    }

    private void bindViewId(View view) {
        rec_notification=view.findViewById(R.id.rec_notification);
        refreshLayout=view.findViewById(R.id.refresh_notification);
        lin_header=view.findViewById(R.id.lin_notice_title);


        rec_notification.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                refresh();
            }
        });

    }

    private void refresh() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            //call api
            sendRequest(ApiCode.GET_NOTIFICATION);

        }
    }

    private void setNotification(){
        notificationAdapter=new NotificationAdapter(getActivity(), notificationList,
                new NotificationAdapter.OnNotificationClickListener() {
                    @Override
                    public void onNotificationClick(int position, Notification_model model) {
                        showMessageDialog(model.getTitle());
                    }
                });
        rec_notification.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();

        if (notificationAdapter.getItemCount()==0){
            CommonMethods.showSuccessToast(getActivity(),"No Notification");
            rec_notification.setVisibility(View.GONE);
            lin_header.setVisibility(View.GONE);
        }else{
            lin_header.setVisibility(View.VISIBLE);
            rec_notification.setVisibility(View.VISIBLE);

        }

    }
    private void showMessageDialog(String msg) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_show_notice_msg);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_title =dialog.findViewById(R.id.tv_title);
        TextView tv_msg =dialog.findViewById(R.id.tv_msg);
        tv_title.setText("Notification");
        tv_msg.setText(msg);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_NOTIFICATION:
                params.put("user_id", new SessionManagement(getActivity()).getString(USER_ID));
                callApi(ApiCode.GET_NOTIFICATION, params);
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
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_NOTIFICATION:
                Log.e("notification",response);
                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        List<Notification_model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Notification_model>>() {
                                        }.getType());
                        notificationList.clear();
                        notificationList.addAll(psearch);
                        setNotification();

                    }else{
                        notificationList.clear();
                        setNotification();

                    }
                }else{
                    notificationList.clear();
                    setNotification();
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_STUDENT)){
            ((MainActivity)getActivity()).setStudentChildActionBar("Notification",false);
        }else if (from.equals(SIMPLEE_HOME_TUTOR)){
            ((TeacherMainActivity)getActivity()).setTeacherActionBar("Notification",false);
        }
    }
}
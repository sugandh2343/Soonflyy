package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.StudentTimeTableAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.StudentTimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentTimeTableFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {

    RecyclerView rec_timeTable;
    SwipeRefreshLayout refreshLayout;
    ImageView ivBack;
    TextView tvTitleDate;

    StudentTimeTableAdapter timeTableAdapter;
    ArrayList<StudentTimeTable>timeList=new ArrayList<>();
    String from,student_type,id;

    public StudentTimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_time_table, container, false);
        initView(view);
        getArgumentData();
        callRereshApi();
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        id=getArguments().getString("id");
        student_type=getArguments().getString("student_type");

    }

    private void initView(View view) {
        ivBack=view.findViewById(R.id.iv_back);
        tvTitleDate=view.findViewById(R.id.tv_test_date);
        rec_timeTable = view.findViewById(R.id.rec_timetable);
        refreshLayout = view.findViewById(R.id.refresh_layout);

        tvTitleDate.setText("Date : "+CommonMethods.getCurrentTime("dd/MM/yyyy, EEEE"));
        ivBack.setOnClickListener(this);
        rec_timeTable.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                callRereshApi();
            }
        });
    }

    private void callRereshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())){
sendRequest(ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT);
        }
    }
    private void setTimeTable(){
        timeTableAdapter=new StudentTimeTableAdapter(getActivity(),timeList);
        rec_timeTable.setAdapter(timeTableAdapter);
        timeTableAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
      //  ((SchoolMainActivity)getActivity()).setActionBarTitle("Time Table");
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
    }

    //----------API CALL---------//
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){

            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT:
              //  params.put("student_id",new Session_management(getActivity()).getString(SCHOOL_STUDENT_ID));
              if (student_type.equals("school")){
                  params.put("school_id", id);
                  params.put("type", "0");
              }else if (student_type.equals("itutor")) {
                  params.put("teacher_id", id);
                  params.put("type", "1");
              }
                callApi(ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_TIME_TABLE_BY_STUDENT, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_STUDENT:
                Log.e("time_table",response);
                if(jsonObject.getBoolean("status")){
                    JSONArray array=jsonObject.getJSONArray("data");
                    if(array.length()>0){
                        ArrayList<StudentTimeTable> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<ArrayList<StudentTimeTable>>() {
                                        }.getType());
                        timeList.clear();
                        timeList.addAll(psearch);
                        setTimeTable();
                    }else{
                        timeList.clear();
                        setTimeTable();
                    }
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ((SchoolMainActivity)getActivity()).onBackPressed();
                break;
        }
    }
}
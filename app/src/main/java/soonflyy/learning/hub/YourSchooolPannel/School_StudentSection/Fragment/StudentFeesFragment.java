package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.FeesAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.StudentFees;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentFeesFragment extends Fragment implements VolleyResponseListener {
    RecyclerView rec_fees;
    SwipeRefreshLayout refreshLayout;

    ArrayList<StudentFees>feesList=new ArrayList<>();
    FeesAdapter feesAdapter;
    String from,id,student_type;

    public StudentFeesFragment() {
        // Required empty public constructor
    }

   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_student_fees, container, false);
        initview(view);
        getArgumentData();
        callRefreshApi();
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        id=getArguments().getString("id");
        student_type=getArguments().getString("student_type");
        Log.e("studentType",""+student_type);
    }

    private void initview(View view) {
        rec_fees=view.findViewById(R.id.rec_fees);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        rec_fees.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                callRefreshApi();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle("Fees");
    }

    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            //
            sendRequest(ApiCode.SCHOOL_GET_FEES_BY_STUDENT);
        }
    }
    private void setFees(){
        feesAdapter=new FeesAdapter(getActivity(),feesList);
        rec_fees.setAdapter(feesAdapter);
        feesAdapter.notifyDataSetChanged();
    }



    //----------API CALL---------//
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){

            case ApiCode.SCHOOL_GET_FEES_BY_STUDENT:
                params.put("student_id",new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                if (student_type.equals("school")){
                     params.put("type","0");
                }else if (student_type.equals("itutor")){
                     params.put("type","1");
                }
                callApi(ApiCode.SCHOOL_GET_FEES_BY_STUDENT, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_GET_FEES_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_FEES_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_FEES_BY_STUDENT, params);
                Log.e("url",""+  BaseUrl.URL_SCHOOL_GET_FEES_BY_STUDENT);
                Log.e("params",""+params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_GET_FEES_BY_STUDENT:
                Log.e("FREES",response);
                if(jsonObject.getBoolean("status")){
                    JSONArray array=jsonObject.getJSONArray("data");
                    if(array.length()>0){
                        ArrayList<StudentFees> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<ArrayList<StudentFees>>() {
                                        }.getType());
                        feesList.clear();
                        feesList.addAll(psearch);
                        setFees();
                    }else{
                        feesList.clear();
                        setFees();
                    }
                }else{
                    feesList.clear();
                    setFees();
                }
                break;
        }

    }
}
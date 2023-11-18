package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;

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
import soonflyy.learning.hub.Student_Pannel.Adapter.DppAapter;
import soonflyy.learning.hub.Student_Pannel.Model.Dpp_Model;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DppList_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, VolleyResponseListener {

    RecyclerView rv_dpp;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Dpp_Model>dppList=new ArrayList<>();
    DppAapter dppAdpter;
    String chapter_id,subject_id,course_id,from;

    public DppList_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dpp_list_, container, false);
        bindIdView(view);
        getArgumentData();
        sendApiRequest();
       // setDpp();
        refreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        course_id=getArguments().getString("course_id");
    }

    private void setDpp() {
        rv_dpp.setLayoutManager(new LinearLayoutManager(getContext()));
//        dppList.add(new Dpp_Model());
//        dppList.add(new Dpp_Model());
//        dppList.add(new Dpp_Model());
//        dppList.add(new Dpp_Model());
                dppAdpter=new DppAapter(getContext(), dppList, new DppAapter.OnDppClickListener() {
                    @Override
                    public void onDppClick(int position) {
                        if (from.equals(SIMPLEE_HOME_STUDENT)) {
                            Dpp_Fragment fragment = new Dpp_Fragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("dpp", dppList.get(position));
                            fragment.setArguments(bundle);
                            ((Subject_Chapter_Details_Fragment) getParentFragment()).switchFragment(fragment);
                        }
                    }
                });
                rv_dpp.setAdapter(dppAdpter);
                dppAdpter.notifyDataSetChanged();
    }

    private void bindIdView(View view) {
        rv_dpp=view.findViewById(R.id.rec_dpp);
        refreshLayout=view.findViewById(R.id.refresh_dpp);

    }

    @Override
    public void onRefresh() {
        sendApiRequest();
    }

    private void sendApiRequest() {
        if(ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_DPP);
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_DPP:
                // if (liveType.equals("course_wise")) {
                params.put("course_id",course_id );//course_id
                params.put("section_id",subject_id);//subject_id
                params.put("lesson_id",chapter_id);//chapter_id
                //}
               // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                // Log.e("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_DPP, params);
                break;
        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_DPP:
                service.postDataVolley(ApiCode.GET_DPP,
                        BaseUrl.URL_GET_DPP, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        Log.e("dpplist ",response);
        switch (requestType){
            case ApiCode.GET_DPP:
                Log.e("dpplist ",response);
                JSONObject jsonObject=new JSONObject(response);
              if (jsonObject.getBoolean("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        ArrayList<Dpp_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<Dpp_Model>>() {
                                        }.getType());
                        dppList.clear();
                        dppList.addAll(psearch);
                       setDpp();
                    }else{
                        dppList.clear();
                       setDpp();
                        CommonMethods.showSuccessToast(getContext(),"No Dpp");
                    }
               }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).setDppBackground();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).showAssignToProfile();
    }
}
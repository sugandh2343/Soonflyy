package soonflyy.learning.hub.Teacher_Pannel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_DPPTopicAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_DppStudent;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class T_DPPTopicDeatilFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, VolleyResponseListener {

    RecyclerView rec_student;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<T_DppStudent>dppStudentList=new ArrayList<>();
    T_DPPTopicAdapter studentAdapter;
    String course_id,section_id,chapter_id,dpp_id,title;
    TextView tv_title;
    String pageTitle;




    public T_DPPTopicDeatilFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_t__d_p_p_topic_deatil, container, false);
        bindView(view);
        getArgumentData();
        sendApiCall();
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;

    }

    private void getArgumentData() {
        course_id=getArguments().getString("course_id");
        chapter_id=getArguments().getString("chapter_id");
        section_id=getArguments().getString("section_id");
        dpp_id=getArguments().getString("dpp_id");
        title=getArguments().getString("title");
        pageTitle=getArguments().getString("course_title");

        tv_title.setText("DPP Topic: "+title);
    }


    private void bindView(View view) {
        rec_student=view.findViewById(R.id.rec_dpp_student);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        tv_title=view.findViewById(R.id.tv_title);
        rec_student.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private  void setStudentLis(){
        studentAdapter=new T_DPPTopicAdapter(getContext(), dppStudentList, new T_DPPTopicAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                DPPDeatailFragment fragment=new DPPDeatailFragment();
                Bundle bundle=new Bundle();
                bundle.putString("course_id",course_id);
                bundle.putString("dpp_id",dpp_id);
                bundle.putString("section_id",section_id);
                bundle.putString("chapter_id",chapter_id);
                bundle.putString("title",title);
                bundle.putString("course_title",pageTitle);
                bundle.putString("student_id",dppStudentList.get(postion).getStudent_id());
                fragment.setArguments(bundle);
                ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
            }
        });
        rec_student.setAdapter(studentAdapter);
        studentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        sendApiCall();
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()){
            //send api
            sendRequest(ApiCode.GET_STUDENT_LIST_DPP);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }



    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_STUDENT_LIST_DPP:
                params.put("dpp_id", dpp_id);
                params.put("lesson_id", chapter_id);
               // params.put("section_id", section_id);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_STUDENT_LIST_DPP, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_STUDENT_LIST_DPP:
                service.postDataVolley(ApiCode.GET_STUDENT_LIST_DPP,
                        BaseUrl.URL_GET_STUDENT_LIST_DPP, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {

            case ApiCode.GET_STUDENT_LIST_DPP:
                Log.e("student_list", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<T_DppStudent> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<T_DppStudent>>() {
                                        }.getType());
                        dppStudentList.clear();
                        dppStudentList.addAll(psearch);
                        setStudentLis();
                        // notesAdapter.notifyDataSetChanged();
                    } else {
                        dppStudentList.clear();
                        setStudentLis();
                        //notesAdapter.notifyDataSetChanged();
                        //  CommonMethods.showSuccessToast(getContext(),"Notes not available");
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle,false);
        ((T_ChapterDetailFragment)getParentFragment()).setDppBgColor();
        ((T_ChapterDetailFragment)getParentFragment()).hideAssignProfile();

    }
}
package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

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
import soonflyy.learning.hub.Student_Pannel.Adapter.Test_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.Test_Model;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Test_Fragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rv_test;
    SwipeRefreshLayout refresh_layout;

    Test_Adapter testAdapter;
    ArrayList<Test_Model>testList=new ArrayList<>();

    String course_id,subject_id,type,chapter_id;
    String teacher_id,teacher_name,class_id,section_id,from;

    public Test_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_test_, container, false);
        bindViewId(view);
        getArgumentsData();
        requestApi();
        setTest();

        refresh_layout.setOnRefreshListener(this);
//        rv_test.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_test, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
        return  view;
    }

    private void getArgumentsData() {
        from=getArguments().getString("from");
        type=getArguments().getString("type");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
            course_id = getArguments().getString("course_id");
        }else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            teacher_name=getArguments().getString("teacher_name");
            class_id =getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");
            teacher_id=getArguments().getString("teacher_id");
        }

       // }

    }
    private void requestApi() {
        if (ConnectivityReceiver.isConnected()){
            if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_TEST_LIST);
            }else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_TEST);
            }
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }


    private void setTest() {
        //testList.clear();
        testAdapter=new Test_Adapter(getContext(), from,testList, new Test_Adapter.OnTestClickListener() {
            @Override
            public void onItemClick(int position) {
                Test_Model model=testList.get(position);
                if (from.equals(SCHOOL_COACHING)|| from.equals(SIMPLEE_HOME_TUTOR)){
                    Start_Test_Fragment fragment = new Start_Test_Fragment();
                    Bundle arg = new Bundle();
                    arg.putString("test_id", model.getId());
                    arg.putString("from",from);
                    fragment.setArguments(arg);
                    if (from.equals(SCHOOL_COACHING))
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                    else
                        ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
                }else {
                    if (model.getAttempted().equals("0")) {
                        Start_Test_Fragment fragment = new Start_Test_Fragment();
                        Bundle arg = new Bundle();
                        arg.putString("from",from);
                        arg.putString("test_id", model.getId());
                        fragment.setArguments(arg);
                        if (from.equals(SCHOOL_STUDENT)){
                            ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);

                        }else {
                            ((MainActivity) getActivity()).SwitchFragment(fragment);
                        }
                        Log.e("test", "click");
                    } else {
                        //show result go
                        ResultFragment fragment = new ResultFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("from",from);
                        bundle.putString("test_id", model.getId());
                        bundle.putString("fromPage","TestList");
                        fragment.setArguments(bundle);
                        // bundle.putString("course_id",course_id);
                        if (from.equals(SCHOOL_STUDENT)){
                            ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);

                        }else {
                            ((MainActivity) getActivity()).SwitchFragment(fragment);
                        }
                    }
                }
            }
        });
        rv_test.setLayoutManager( new LinearLayoutManager(getContext()));
        rv_test.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();

    }

    private void bindViewId(View view) {
        rv_test=view.findViewById(R.id.rv_test);
        refresh_layout=view.findViewById(R.id.refresh_test);

    }

    @Override
    public void onResume() {
        super.onResume();
       // ((MainActivity)getActivity()).setStudentChildActionBar("Internet of Things(IOT)",false);
        if (getArguments().getString("type").equals("course")) {
            ((Subscribed_Course_Details) getParentFragment()).setTestBgColor();
            ((Subscribed_Course_Details) getParentFragment()).showAssignToProfile();
        }
        else {
            ((Subject_Chapter_Details_Fragment) getParentFragment()).setTestBackground();
            ((Subject_Chapter_Details_Fragment) getParentFragment()).showAssignToProfile();
        }

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_TEST_LIST:
               // params.put("user_id", new Session_management(getContext()).getString(USER_ID));
                params.put("type", type);//course or series
                params.put("course_id", course_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                if (from.equals(SIMPLEE_HOME_STUDENT)) {
                    //for home student
                    params.put("is_student","1");
                    params.put("student_id",new SessionManagement(getContext())
                            .getString(USER_ID));

                }else{
                    //for home  tutor
                    params.put("is_student","0");
                    params.put("student_id","");

                }

                callApi(ApiCode.GET_TEST_LIST, params);
                break;
            case ApiCode.SCHOOL_GET_TEST:
                if(from.equals(SCHOOL_STUDENT)){
                   params.put("type","0");
                    params.put("user_id",new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                }else{
                    //for school coaching
                    params.put("type","1");
                    params.put("user_id","");
                }
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_TEST, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_TEST_LIST:
                service.postDataVolley(ApiCode.GET_TEST_LIST,
                        BaseUrl.URL_GET_TEST_LIST, params);
                break;
            case ApiCode.SCHOOL_GET_TEST:
                service.postDataVolley(ApiCode.SCHOOL_GET_TEST,
                        BaseUrl.URL_SCHOOL_GET_TEST, params);
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);

        switch (requestType) {
            case ApiCode.GET_TEST_LIST:
            case ApiCode.SCHOOL_GET_TEST:
                Log.e("test_data ", response);
                if(jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Test_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<Test_Model>>() {
                                        }.getType());
                        testList.clear();
                        testList.addAll(psearch);
                        setTest();
                       // testAdapter.notifyDataSetChanged();
                    } else {
                        testList.clear();
                        setTest();
                        CommonMethods.showSuccessToast(getContext(), "No Test Available");
                    }

                }
//                JSONObject object = new JSONObject(response);
//                if (object.getBoolean("status")) {
//                    JSONArray jsonArray = object.getJSONArray("data");
//                    if (jsonArray.length()>0) {
//                        List<TestSeries> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new TypeToken<List<TestSeries>>() {
//                                        }.getType());
//                        testList.clear();
//                        testList.addAll(psearch);
//                        testAdapter.notifyDataSetChanged();
//                    }else{
//                        CommonMethods.showSuccessToast(getContext(),"No Test Available");
//                    }
//                }
                break;

        }
    }

    @Override
    public void onRefresh() {
        refresh_layout.setRefreshing(false);
        requestApi();
    }


}
package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolDiscustionAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolDiscustionModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SchoolTutorDiscussionFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {

    RecyclerView rec_student;
    ImageView arrow_back_img;
    SwipeRefreshLayout refreshLayout;
    String type;
    TextView tv_title;
    SearchView searchView;
    SchoolDiscustionAdapter adapter;
    ArrayList<SchoolDiscustionModel> list=new ArrayList<>();
    String school_id,from,school_name;

    public SchoolTutorDiscussionFragment() {
        // Required empty public constructor
    }


    public static SchoolTutorDiscussionFragment newInstance(String param1, String param2) {
        SchoolTutorDiscussionFragment fragment = new SchoolTutorDiscussionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school_tutor_discussion, container, false);
        initview(view);
        getArgumentData();
        callRefreshApi();


        return view;
    }


    private void getArgumentData() {
        from=getArguments().getString("from");
        school_id=getArguments().getString("school_id");
        school_name=getArguments().getString("school_name");
    }

    private void initview(View view) {
        refreshLayout=view.findViewById(R.id.refresh_layout);
        rec_student = view.findViewById(R.id.rec_discussions);
        rec_student.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rec_student.setLayoutManager(linearLayoutManager);
        rec_student.setKeepScreenOn(true);
        list = new ArrayList<>();
        searchView = view.findViewById(R.id.search_view);
        CommonMethods.setSearchViewColor(getActivity(), searchView);
        tv_title = view.findViewById(R.id.tv_title);
        arrow_back_img = view.findViewById(R.id.back_btn);
        arrow_back_img.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                callRefreshApi();
            }
        });

    }

    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION);
        }
    }

    private void initRecyclerview(){
        adapter = new SchoolDiscustionAdapter(getContext(), list, new SchoolDiscustionAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
                SchoolDiscussionSectionFragment fragment = new SchoolDiscussionSectionFragment();
                Bundle bundle=new Bundle();
                bundle.putString("from",from);
                bundle.putString("school_id",school_id);
                bundle.putString("class_id",list.get(postion).getClass_id());
                bundle.putString("class_name",list.get(postion).getClass_name());
                bundle.putString("teacher_id",new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
               fragment.setArguments(bundle);
               // SwitchFragment(fragment);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }


            @Override
            public void onDelete(int position) {

            }


        });
        rec_student.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back_btn:
//                Log.e("back_click","clicked");
//                gotoBack();
//                break;
//        }
//
//    }

    private void gotoBack() {
        Log.e("back_click","clicked");
        try {
            ((SchoolMainActivity)getActivity()).onBackPressed();
//            if (type.equals("teacher")) {
//                ((TeacherMainActivity) getActivity()).onBackPressed();
//
//            } else if (type.equals("student")) {
//                ((MainActivity) getActivity()).onBackPressed();
//            }
        } catch (Exception e) {

        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment);//, ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION:
                params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
                callApi(ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION:
                service.postDataVolley(ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION,
                        BaseUrl.URL_SCHOOL_GET_CLASS_FOR_DISCUSSION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CLASS_FOR_DISCUSSION);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_CLASS_FOR_DISCUSSION:
                Log.e("sc_login", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolDiscustionModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolDiscustionModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                    }else{
                        list.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                Log.e("back_click","clicked");
                gotoBack();
                break;
        }
    }
}
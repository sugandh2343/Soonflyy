package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.os.Bundle;
import android.text.TextUtils;
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
import soonflyy.learning.hub.Teacher_Pannel.MessageFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolDiscussionChatAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolDiscussionChatModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class School_DiscustionChatDetailFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {

    SwipeRefreshLayout swipe;
    RecyclerView rec_discussion;
    ImageView arrow_back_img;
    TextView tv_title,tv_class_name;
    String type = "";
    SearchView searchView;
    SchoolDiscussionChatAdapter ada;
    ArrayList<SchoolDiscussionChatModel> list=new ArrayList<>();
    String from,class_id,section_id,teacher_id,class_name,section_name,student_type;

    public School_DiscustionChatDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static School_DiscustionChatDetailFragment newInstance(String param1, String param2) {
        School_DiscustionChatDetailFragment fragment = new School_DiscustionChatDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_discustion_detail, container, false);
        initView(view);
        getArgumentData();
        callRefreshApi();
      //  initRecyclerview();
        init_swipe_method();
        return view;
    }
    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            if (from.equals(SCHOOL_TUTOR)) {
                sendRequest(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION);
            }else if (from.equals(INDEPENDENT_TUTOR)){
                sendRequest(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR);
            }else if (from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_CHAT_BY_ID);
            }
        }
    }


    private void getArgumentData() {
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_TUTOR)) {
            section_id = getArguments().getString("section_id");
            class_id = getArguments().getString("class_id");
            teacher_id = getArguments().getString("teacher_id");
            class_name = getArguments().getString("class_name");
            section_name = getArguments().getString("section_name");
            tv_class_name.setText("Class - " + class_name + " (" + section_name + ")");
        }else if (from.equals(INDEPENDENT_TUTOR)){
            teacher_id=getArguments().getString("itutor_id");
            tv_class_name.setVisibility(View.GONE);
        }else if (from.equals(SCHOOL_STUDENT)){
            tv_class_name.setVisibility(View.GONE);
            student_type=getArguments().getString("student_type");

        }



    }

    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        searchView = view.findViewById(R.id.search_view);
        tv_class_name = view.findViewById(R.id.tv_class);
        CommonMethods.setSearchViewColor(getActivity(), searchView);
        tv_title = view.findViewById(R.id.tv_title);
        arrow_back_img = view.findViewById(R.id.btn_back);
        arrow_back_img.setOnClickListener(this);

        rec_discussion = view.findViewById(R.id.rec_discussion);
        rec_discussion.hasFixedSize();
        rec_discussion.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_discussion.setLayoutManager(layoutManager);
        rec_discussion.setKeepScreenOn(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (ada!=null){
                    ada.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (ada!=null){
                    ada.getFilter().filter(newText);
                }
                return false;
            }
        });

    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callRefreshApi();
            //    initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }


    private void initRecyclerview() {

        ada = new SchoolDiscussionChatAdapter(getActivity(), list, new SchoolDiscussionChatAdapter.OnChatClickListener() {
            @Override
            public void onItemClick(int postion) {
                SchoolDiscussionChatModel model = list.get(postion);
                if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR)) {

                    Fragment fragment = new MessageFragment();
                    Bundle args = new Bundle();
                    args.putString("from", from);
                    args.putString("type", "teacher");
                    // args.putParcelable("studentData", allStudentList.get(position));
                    args.putString("teacher_id", teacher_id);
                    args.putString("student_id", model.getId());
                    if (!TextUtils.isEmpty(model.getName())){
                        args.putString("name",model.getName());
                    }else{
                        args.putString("name","Unknown");
                    }
                    //args.putString("name", model.getName());
                    args.putString("profile_image", "");
                    fragment.setArguments(args);
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                }else if (from.equals(SCHOOL_STUDENT)){
                    MessageFragment chatFragment = new MessageFragment();
                    Bundle args = new Bundle();
                    args.putString("from",from);
                    args.putString("type", "student");
                    args.putString("student_id",new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                    args.putString("teacher_id",model.getId());
                    if (!TextUtils.isEmpty(model.getName())){
                        args.putString("name",model.getName());
                    }else{
                        args.putString("name","Unknown");
                    }

                    args.putString("profile_image","");
                    Log.e("s_id",""+new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                    Log.e("tutor_id",model.getId());
                    chatFragment.setArguments(args);
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(chatFragment);
                }

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_discussion.setAdapter(ada);
        ada.notifyDataSetChanged();


    }


    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.arrow_back_img:
//                gotoBack();
//                break;
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
    }

    private void gotoBack() {

        try {
            ((SchoolMainActivity)getActivity()).onBackPressed();
        } catch (Exception e) {

        }
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION:
                params.put("section_id", section_id);
                params.put("tutor_id",new SessionManagement(getContext())
                        .getString(SCHOOL_TEACHER_ID));
                callApi(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION, params);
                break;
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR:
                params.put("teacher_id", teacher_id);
                callApi(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_GET_CHAT_BY_ID:
                params.put("student_id", new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                if (student_type.equals("itutor")){
                    params.put("type","1");
                    params.put("teacher_id",getArguments().getString("id"));
                }else{
                    params.put("type","0");
                    params.put("school_id",getArguments().getString("id"));
                }
                callApi(ApiCode.SCHOOL_GET_CHAT_BY_ID, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION,
                        BaseUrl.URL_SCHOOL_GET_STUDENT_LIST_BY_SECTION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_STUDENT_LIST_BY_SECTION);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_CHAT_BY_ID:
                service.postDataVolley(ApiCode.SCHOOL_GET_CHAT_BY_ID,
                        BaseUrl.URL_SCHOOL_GET_CHAT_BY_ID, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CHAT_BY_ID);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_SECTION:
            case ApiCode.SCHOOL_GET_STUDENT_LIST_BY_INDEPENDENT_TUTOR:
         //   case ApiCode.SCHOOL_GET_CHAT_BY_ID:

                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolDiscussionChatModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolDiscussionChatModel>>() {
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
//
                break;
            case ApiCode.SCHOOL_GET_CHAT_BY_ID:

                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("users");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolDiscussionChatModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolDiscussionChatModel>>() {
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
//
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                gotoBack();
                break;
        }
    }
}
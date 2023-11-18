package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel;

import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyNoticeFragment;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter.MyTeacherSubjectCourseAdapter;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.MyTeacherSubjectCourseModel;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyTeacherHomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, VolleyResponseListener {


    TextView tv_course,tv_subject,tvTitle;//,tvBoldTitle
    LinearLayout lin_course, lin_subject,linNotice;
    ImageView iv_course,iv_subject,ivBack;
    RecyclerView recProfile;
    SwipeRefreshLayout refreshLayout;
    SearchView searchView;
    String type="course";
    MyTeacherSubjectCourseAdapter tProfileAdapter;
    ArrayList<MyTeacherSubjectCourseModel> profileList = new ArrayList<>();



    public MyTeacherHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_teacher_home, container, false);
        initView(view);
        initControl();
        setTeacherList();
        setAboutBgColor();
        return view;

    }



    private void initView(View view) {

        tvTitle=view.findViewById(R.id.tv_title);
      //  tvBoldTitle=view.findViewById(R.id.tv_bold_title);
        ivBack=view.findViewById(R.id.iv_back);
        searchView=view.findViewById(R.id.search_view);
        lin_course=view.findViewById(R.id.lin_course);
        iv_course= view.findViewById(R.id.iv_course);
        tv_course=view.findViewById(R.id.tv_course);
        lin_subject=view.findViewById(R.id.lin_subject);
        iv_subject=view.findViewById(R.id.iv_subject);
        tv_subject=view.findViewById(R.id.tv_subject);
        refreshLayout=view.findViewById(R.id.refresh);
        linNotice=view.findViewById(R.id.lin_assign_notice);
        recProfile=view.findViewById(R.id.rec_profile);
        recProfile.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonMethods.setSearchViewColor(getActivity(),searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (tProfileAdapter!=null){
                   tProfileAdapter.getFilter().filter(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (tProfileAdapter!=null){
                    tProfileAdapter.getFilter().filter(newText.trim());
                }
                return false;
            }
        });


        //chat_btn.setOnTouchListener(this);



    }
    private void initControl() {
        //tvTitle.setVisibility(View.GONE);
        tvTitle.setText("My Teacher's");
      //  tvBoldTitle.setVisibility(View.VISIBLE);

        lin_course.setOnClickListener(this);
        lin_subject.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        ivBack.setOnClickListener(this);
        linNotice.setOnClickListener(this);

    }
    private void setTeacherList() {
        tProfileAdapter=new MyTeacherSubjectCourseAdapter(getActivity(), type,profileList, new MyTeacherSubjectCourseAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion,MyTeacherSubjectCourseModel model) {
                Bundle bundle=new Bundle();
                bundle.putString("id",model.getId());
                bundle.putString("name",model.getName());
                Fragment fragment=new AssigntTProfileDetailsFragment();
                fragment.setArguments(bundle);
                ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
//                if (type.equalsIgnoreCase("course")){
//
//                    //go on next fragemetn
////                    ((TeacherMainActivity)getActivity()).SwitchFragment(new DemoFragment());
//
//                   ((TeacherMainActivity)getActivity()).SwitchFragment(new MyTeacherSubCourseProfileFragment());
//                }
//                else if (type.equalsIgnoreCase("subject")){
//                    //go on next fragment
//                    ((TeacherMainActivity)getActivity()).SwitchFragment(new MyTeacherSubCourseProfileFragment());
//
//
//
//                }
            }
        });
        recProfile.setAdapter(tProfileAdapter);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (v.getId()){
            case R.id.lin_course:
                showAboutFragment();
                break;
            case R.id.lin_subject:
                showSubjectFragment();
                break;
            case R.id.iv_back:
                ((TeacherMainActivity)getActivity()).onBackPressed();
                break;
            case R.id.lin_assign_notice:
                if (type.equals("course"))
                gotoNoticeFragment("Course Notice");
                else if (type.equals("subject"))
                    gotoNoticeFragment("Subject Notice");
                break;

        }
    }

    private void gotoNoticeFragment(String title) {
        MyNoticeFragment noticeFragment=new MyNoticeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("notice","tutor");
        bundle.putString("title",title);
        bundle.putString("type",type);
        noticeFragment.setArguments(bundle);
        ((TeacherMainActivity)getActivity()).SwitchFragment(noticeFragment);
    }

    private void showAboutFragment() {
        profileList.clear();
        type="course";
        setAboutBgColor();
        setTeacherList();
        sendApiCall();

    }



    private void showSubjectFragment() {
        profileList.clear();
        type="subject";
        showSubjectBg();
        setTeacherList();
        sendApiCall();

    }



    public void setAboutBgColor() {

        lin_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_course.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_course.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


    }




    public void showSubjectBg() {
        lin_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_course.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_course.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

    }


    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
        if (type.equalsIgnoreCase("course")){
            showAboutFragment();

        }else if (type.equalsIgnoreCase("subject")){
            showSubjectFragment();
        }

        //((TeacherMainActivity) getActivity()).setTeacherActionBar("My Teacher's",false);
    }

    @Override
    public void onRefresh() {
        setTeacherList();
        refreshLayout.setRefreshing(false);
        sendApiCall();
    }

    private void sendApiCall() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.GET_MY_ASSIGN_TEACHER);
        }
    }
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        switch (request){
            case ApiCode.GET_MY_ASSIGN_TEACHER :
                params.put("tutor_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("type",type);
//                callApi(ApiCode.GET_MY_ASSIGN_TEACHER, params);
                if(type.equals("course")){
                    reference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            profileList.clear();
                            for(DataSnapshot ds:snapshot.getChildren()){
                                if(ds.child("creatorId").getValue(String.class).equals(firebaseAuth.getUid())){
                                    if(ds.child("assign_to_id").exists()){
                                        MyTeacherSubjectCourseModel myTeacherSubjectCourseModel=new MyTeacherSubjectCourseModel();
                                        myTeacherSubjectCourseModel.setId(ds.child("assign_to_id").getValue(String.class));
                                        myTeacherSubjectCourseModel.setName(ds.child("assign_to_name").getValue(String.class));
                                        myTeacherSubjectCourseModel.setCourse_name(ds.child("title").getValue(String.class));
                                        profileList.add(myTeacherSubjectCourseModel);

                                    }

                                }
                            }
                            Log.e("ProfileListjk",""+profileList.size());
                            setTeacherList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                break;

        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_MY_ASSIGN_TEACHER:
                service.postDataVolley(ApiCode.GET_MY_ASSIGN_TEACHER,
                        BaseUrl.URL_GET_MY_ASSIGN_TEACHER, params);
                Log.e("url",BaseUrl.URL_GET_MY_ASSIGN_TEACHER);
                Log.e("params",""+params);
                break;


        }
    }


    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.GET_MY_ASSIGN_TEACHER:
                Log.e("teachers ",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0) {
                            ArrayList<MyTeacherSubjectCourseModel>list=new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<MyTeacherSubjectCourseModel>>() {
                                            }.getType());
                            profileList.clear();
                            profileList.addAll(list);
                            setTeacherList();
                        }else{
                            profileList.clear();
                            setTeacherList();
                            CommonMethods.showSuccessToast(getContext(),"You have not created any course");
                        }

                    }else{
                        profileList.clear();
                        setTeacherList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

}
package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_TO;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import soonflyy.learning.hub.Student_Pannel.Student_Notice_Fragment;
import soonflyy.learning.hub.Student_Pannel.Subscribed_Course_Details;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.TeacherSubject_Adapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.AssigntTProfileDetailsFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.MyCourseAdapter;
import soonflyy.learning.hub.adapter.PendingCourseAdapter;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import soonflyy.learning.hub.model.TeacherSubject_Model;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyCourseFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = MyCourseFragment.class.getName();
    RecyclerView rec_course;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout linMyCourse, linAssignedCourse, linAssignedSubject, linNotice, linAssignNotice, lin_accepted_course, lin_request;
    TextView tvMyCourse, tvAssignedCourse, tvAssignedSubject,tv_request,tv_accepted,tv_notice;
    ImageView ivMyCourse, ivAssignedCourse, ivAssignedSubject,iv_accepted,iv_request,iv_notice;

    MyCourseAdapter adapter;
    ArrayList<MyCourseDetailModel> clist = new ArrayList<>();
    String from;
    String type = "MyCourse";
    String subType="Accepted";
    String subjectSubType="Accepted";
    private SessionManagement management;

    //--------for assign subject-----//
    TeacherSubject_Adapter subject_adapter;
    ArrayList<TeacherSubject_Model> subjectList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    PendingCourseAdapter pendingCourseAdapter;
    //------------------------------//

    public MyCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Inflate" , "Inflate");
        View view = inflater.inflate(R.layout.fragment_my_course , container , false);
        management = new SessionManagement(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        pendingCourseAdapter=new PendingCourseAdapter(getContext(),clist);
        init(view);

        // setMyCorseTabColor();
        sendREquest();
        initControl();
        return view;



    }

    private void sendREquest() {
        Log.e("SendRequest" , "Called");
        if (ConnectivityReceiver.isConnected()) {
            if (type.equals("AssignCourse")) {
                sendGetCourseRequest(ApiCode.GET_ASSIGN_BY_COURSES , null);
            } else if (type.equals("AssignSub")) {
                sendGetCourseRequest(ApiCode.GET_ASSIGNED_SUBJECT , null);
            } else {
                sendGetCourseRequest(ApiCode.GET_COURSE , null);
            }
            //  showStatic_DAta();
        } else {
            CommonMethods.showSuccessToast(getContext() , "No Internet Connection");
        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left , R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout , fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void init(View view) {
        rec_course = view.findViewById(R.id.rec_course);
        swipeRefreshLayout = view.findViewById(R.id.refresh_list);

        linNotice = view.findViewById(R.id.lin_notice);
        linMyCourse = view.findViewById(R.id.lin_my_course);
        linAssignedCourse = view.findViewById(R.id.lin_assigned_course);
        linAssignedSubject = view.findViewById(R.id.lin_assigned_subject);
        linAssignNotice = view.findViewById(R.id.lin_assign_notice);
        lin_accepted_course = view.findViewById(R.id.lin_accepted_course);
        lin_request = view.findViewById(R.id.lin_request);
        iv_accepted=view.findViewById(R.id.iv_accepted);
        tv_accepted=view.findViewById(R.id.tv_accepted);
        iv_request=view.findViewById(R.id.iv_request);
        tv_notice=view.findViewById(R.id.tv_notice);
        iv_notice=view.findViewById(R.id.iv_notice);
        tv_request=view.findViewById(R.id.tv_request);

        ivMyCourse = view.findViewById(R.id.iv_my_course);
        ivAssignedCourse = view.findViewById(R.id.iv_assigned_course);
        ivAssignedSubject = view.findViewById(R.id.iv_assigned_subject);

        tvMyCourse = view.findViewById(R.id.tv_my_course);
        tvAssignedCourse = view.findViewById(R.id.tv_assigned_course);
        tvAssignedSubject = view.findViewById(R.id.tv_assigned_sub);


        rec_course.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void initControl() {

        swipeRefreshLayout.setOnRefreshListener(this);
        linMyCourse.setOnClickListener(this);
        linAssignedCourse.setOnClickListener(this);
        linAssignedSubject.setOnClickListener(this);
        linAssignNotice.setOnClickListener(this);
        lin_request.setOnClickListener(this);
        lin_accepted_course.setOnClickListener(this);


    }

    private void setDataOnList() {
        adapter = new MyCourseAdapter(getContext() , type , clist , new MyCourseAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                MyCourseDetailModel model = clist.get(postion);
                if (type.equals("MyCourse")) {
                    if (model.getAssigned_value().equals("1")) {
                        //go for view only mode
                        fragment = new Subscribed_Course_Details();

                        bundle.putString("from" , SIMPLEE_HOME_TUTOR);
                        String image = BaseUrl.BASE_URL_MEDIA + model.getAssign_to().getImage();
                        String profileId = model.getAssign_to().getId();
                        String name = model.getAssign_to().getName();
                        String mobile = "+91-" + model.getAssign_to().getMobile();
                        AssignProfile assignProfile = new AssignProfile(profileId , name , mobile , image);
                        bundle.putSerializable("profileData" , assignProfile);
                        bundle.putString("course_name" , model.getTitle());
                        bundle.putString("course_id" , model.getId());
                        bundle.putString("course_creator_id" , model.getCreatorId());
                    } else {
                        // go for not assigned course
                        fragment = new Mycourse_deailFragment();
                        bundle.putString("course_id" , model.getId());
                        bundle.putString("course_title" , model.getTitle());
                        bundle.putString("course_creator_id" , model.getCreatorId());
                        bundle.putString("live" , "0");
                    }
                } else {
                    fragment = new Mycourse_deailFragment();
                    bundle.putString("course_id" , model.getId());
                    bundle.putString("course_title" , model.getTitle());
                    bundle.putString("live" , "0");
                    bundle.putString("course_creator_id" , model.getCreatorId());
                    if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                        bundle.putSerializable("profileData" , clist.get(postion).getAssign_by());
                    }
                }
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    SwitchFragment(fragment);
                }
//

            }

            @Override
            public void onSubscriptionClick(int position) {
//                StudentListFragment fragment=new StudentListFragment();
//                Bundle args = new Bundle();
//                args.putString("type","teacher");
//                args.putString("listType","subscription");
//                args.putString("user_id",clist.get(position).getUser_id());
//                args.putString("course_id",clist.get(position).getId());
//                fragment.setArguments(args);
//                SwitchFragment(fragment);
            }

            @Override
            public void onDelete(int position) {
                MyCourseDetailModel model = clist.get(position);
//                if (model.getStatus().equals("active") && model.getIs_subscriptions().equals("0"))
                if (!TextUtils.isEmpty(model.getIs_subscriptions())) {
                    if (Integer.parseInt(model.getIs_subscriptions()) > 0) {
                        CommonMethods.showSuccessToast(getContext() , "You can't delete this course.");
                    } else {
                        showAler(position , 1);
                    }
                } else {
                    showAler(position , 1);
                }
//                else
//                    CommonMethods.showSuccessToast(getContext(),"You can't delete this course.");
            }

            @Override
            public void onEdit(int position) {
                if (clist.get(position).getStatus().equals("active"))
                    showAler(position , 0);
                else
                    CommonMethods.showSuccessToast(getContext() , "You can't edit this course.");
            }

            @Override
            public void onGoLive(int position) {
                Mycourse_deailFragment fragment = new Mycourse_deailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("course_id" , clist.get(position).getId());
                bundle.putString("course_title" , clist.get(position).getTitle());
                bundle.putString("live" , "1");
                fragment.setArguments(bundle);
                SwitchFragment(fragment);
//                MyGoLiveTeacherFragment fragment = new MyGoLiveTeacherFragment();
//                SwitchFragment(fragment);

            }

            @Override
            public void onProfileClick(int position) {
                //------
                String assignValue = "";
                //------------
                Bundle bundle = new Bundle();
                AssignProfile profile = null;
                if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                    profile = clist.get(position).getAssign_by();
                    assignValue = "1";
                } else {
                    profile = clist.get(position).getAssign_to();
                    assignValue = "0";
                }
                if (profile != null) {
                    AssigntTProfileDetailsFragment fragment = new AssigntTProfileDetailsFragment();
                    bundle.putString("id" , profile.getId());
                    bundle.putString("name" , profile.getName());
                    bundle.putString("assignValue" , assignValue);
                    fragment.setArguments(bundle);
                    ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                }
                //  CommonMethods.showSuccessToast(getContext(),"Profile Click");
            }
        });
        rec_course.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void showAler(int position , int value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setCancelable(false);
        if (value == 0) {
            builder.setMessage("Are you sure to edit course ?");
        } else {
            builder.setMessage("Are you sure,you want to delete course ?");
        }
        builder.setNegativeButton("NO" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {

            }
        }).setPositiveButton("YES" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog , int which) {

                if (value == 0) {
                    gotoEditPage(position);
                } else {
                    //write code for delete couse
                    if (ConnectivityReceiver.isConnected()) {
                        sendGetCourseRequest(ApiCode.DELETE_COURSE , clist.get(position).getId());
                    }
                }

            }
        }).show();
    }

    private void gotoEditPage(int position) {
        CreateCourseFragment fragment = new CreateCourseFragment();
        Bundle arg = new Bundle();
        arg.putString("type" , "update");
        arg.putString("course_name" , clist.get(position).getTitle());
        arg.putString("course_id" , clist.get(position).getCourse_id());
        arg.putString("course_creator_id" , clist.get(position).getCreatorId());
        arg.putParcelable("courseData" , clist.get(position));
        fragment.setArguments(arg);
        ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
    }

    private void setSubjectList() {
        subject_adapter = new TeacherSubject_Adapter(getContext() , type , subjectList , new TeacherSubject_Adapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                TeacherSubject_Model model = subjectList.get(postion);
                // Mycourse_deailFragment fragment = new Mycourse_deailFragment();
                T_Subject_ChaptersFragment fragment = new T_Subject_ChaptersFragment();
                Bundle bundle = new Bundle();
                bundle.putString("course_id" , model.getCourse_id());
                bundle.putString("subject_id" , model.getId());
                bundle.putString("course_title" , model.getCourse_name());//title+
                bundle.putString("subName" , model.getTitle());//model.getTitle()
                if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                    // AssignProfile profileData= (AssignProfile) getArguments().getSerializable("profileData");
                    bundle.putSerializable("profileData" , model.getAssigned_by());
                }
                fragment.setArguments(bundle);

                // Bundle bundle=new Bundle();
                //bundle.putString("subject_id",cli);
                // SwitchFragment(fragment);
                ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
            }

            @Override
            public void onDelete(int position) {
                CommonMethods.showSuccessToast(getContext() , "Deleted");
            }

            @Override
            public void onEdit(int position) {
                CommonMethods.showSuccessToast(getContext() , "Updated");
            }

            @Override
            public void onProfileClick(int position) {
                String assignValue = "";
                Bundle bundle = new Bundle();
                AssignProfile profile = null;
                Log.e("management",management.getString(ASSIGN));
                if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                    profile = subjectList.get(position).getAssigned_by();
                    profile.setId(subjectList.get(position).getAssign_by_id());
                    assignValue = "1";
                }
                else{
                    profile.setId(subjectList.get(position).getAssigm_to_id());
                }

                if (profile.getId() != null) {
                    AssigntTProfileDetailsFragment fragment = new AssigntTProfileDetailsFragment();
                    bundle.putString("id" , profile.getId());
                    fragment.setArguments(bundle);
                    ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                }
                // CommonMethods.showSuccessToast(getContext(),"Profile Clicked");
            }
        });
        rec_course.setAdapter(subject_adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
//        ( (TeacherMainActivity)getActivity()).setChildActionBar("My Course");
        ((TeacherMainActivity) getActivity()).setTeacherActionBar("My Courses" , false);
        if (type.equals("MyCourse")) {
            setMyCorseTabColor();

        } else if (type.equals("AssignSub")) {
            setAssignSubTabColor();

        } else if (type.equals("AssignCourse")) {
            setAssignCourseTabColor();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_my_course:
                setMyCorseTabColor();
                //setDataOnList();
                break;
            case R.id.lin_assigned_course:
                setAssignCourseTabColor();
                //setDataOnList();
                break;
            case R.id.lin_assigned_subject:
                setAssignSubTabColor();
                break;
            case R.id.lin_assign_notice:
                subType="Notice";
                //go for notice
                gotoNoticePage();
                break;
            case R.id.lin_accepted_course:
                subType="Accepted";
                //go for notice
                if(type.equals("AssignCourse")){
                    setAssignCourseTabColor();

                }else{
                    setAssignSubTabColor();
                }

                break;
            case R.id.lin_request:
                subType="Request";
                //go for notice
                if(type.equals("AssignCourse")){
                    setAssignCourseTabColor();

                }else{
                    setAssignSubTabColor();
                }

                break;


        }
    }

    private void gotoNoticePage() {
        Student_Notice_Fragment fragment = new Student_Notice_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("from" , SIMPLEE_HOME_TUTOR);
        bundle.putString("type" , "AssignBy");
        if (type.equals("AssignSub")) {
            bundle.putString("noticeTitle" , "Assigned Subject Notice");
            bundle.putString("noticeType" , "subject");
        } else {
            bundle.putString("noticeTitle" , "Assigned Course Notice");
            bundle.putString("noticeType" , "course");
        }
        fragment.setArguments(bundle);
        ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);

    }

    private void setAssignSubTabColor() {
        type = "AssignSub";
        management.setString(ASSIGN , ASSIGN_BY);
        linNotice.setVisibility(View.VISIBLE);

        if(subType.equals("Accepted")){
            lin_accepted_course.setBackground(getContext().getDrawable(R.drawable.bg_selected));
            lin_request.setBackground(getContext().getDrawable(R.drawable.bg_rounded_corner));
            tv_accepted.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
            lin_request.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            tv_request.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            linNotice.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            iv_notice.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
            tv_notice.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        }else if(subType.equals("Request")){
            lin_accepted_course.setBackground(getContext().getDrawable(R.drawable.bg_rounded_corner));
            lin_request.setBackground(getContext().getDrawable(R.drawable.bg_selected));
            tv_request.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
            lin_accepted_course.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));

            tv_accepted.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            linNotice.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            iv_notice.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
            tv_notice.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        }
        linAssignedSubject.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.graient2));
        ivAssignedSubject.setColorFilter(ContextCompat.getColor(getContext() , R.color.white));
        tvAssignedSubject.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
        setOtherTabColor("AssignSub");
        sendGetCourseRequest(ApiCode.GET_ASSIGNED_SUBJECT , null);
        setSubjectList();
    }

    private void setAssignCourseTabColor() {

        type = "AssignCourse";
        management.setString(ASSIGN , ASSIGN_BY);
        linNotice.setVisibility(View.VISIBLE);
        clist.clear();
        setDataOnList();
        if(subType.equals("Accepted")){
            lin_accepted_course.setBackground(getContext().getDrawable(R.drawable.bg_selected));
            lin_request.setBackground(getContext().getDrawable(R.drawable.bg_rounded_corner));
            tv_accepted.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
            lin_request.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            tv_request.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            linNotice.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            iv_notice.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
            tv_notice.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        }else if(subType.equals("Request")){
            lin_accepted_course.setBackground(getContext().getDrawable(R.drawable.bg_rounded_corner));
            lin_request.setBackground(getContext().getDrawable(R.drawable.bg_selected));
            tv_request.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
            lin_accepted_course.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));

            tv_accepted.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
            linNotice.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
            iv_notice.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
            tv_notice.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
        }
        linAssignedCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.graient2));
        ivAssignedCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.white));
        tvAssignedCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
        setOtherTabColor("AssignCourse");
        sendREquest();
    }

    private void setMyCorseTabColor() {


        type = "MyCourse";
        clist.clear();
        setDataOnList();
        management.setString(ASSIGN , ASSIGN_TO);
        linNotice.setVisibility(View.GONE);
        linMyCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.graient2));
        ivMyCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.white));
        tvMyCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.white));
        setOtherTabColor("MyCourse");
        sendREquest();
    }

    private void setOtherTabColor(String type) {
        switch (type) {
            case "MyCourse":
                linAssignedCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivAssignedCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvAssignedCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.black));

                linAssignedSubject.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivAssignedSubject.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvAssignedSubject.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
                break;
            case "AssignCourse":
                linMyCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivMyCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvMyCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.black));

                linAssignedSubject.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivAssignedSubject.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvAssignedSubject.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
                break;
            case "AssignSub":
                linMyCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivMyCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvMyCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.black));

                linAssignedCourse.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.white));
                ivAssignedCourse.setColorFilter(ContextCompat.getColor(getContext() , R.color.black));
                tvAssignedCourse.setTextColor(ContextCompat.getColor(getContext() , R.color.black));
                break;
        }

    }

    private void goToMainScreen() {
        Intent intent = new Intent(requireActivity() , TeacherMainActivity.class);
        startActivity(intent);

    }

    private void sendGetCourseRequest(int request , String delPosition) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        switch (request) {
            case ApiCode.GET_COURSE:
                params.put("user_id" , management.getString(USER_ID));
                params.put("course_id" , " ");
                reference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clist.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            MyCourseDetailModel myCourseDetailModel = ds.getValue(MyCourseDetailModel.class);
                            clist.add(myCourseDetailModel);








                        }
                        Log.e("vdfHGVD" , "" + clist.size());

                        adapter = new MyCourseAdapter(getContext() , type , clist , new MyCourseAdapter.OnCourseClickListener() {
                            @Override
                            public void onItemClick(int postion) {
                                Fragment fragment = null;
                                Bundle bundle = new Bundle();
                                MyCourseDetailModel model = clist.get(postion);
                                Log.e("NFNDJS",type);
                                Log.e("NFNDJS",model.getAssigned_value());
                                if (type.equals("MyCourse")) {
                                    if (model.getAssigned_value().equals("1")) {
                                        //go for view only mode
                                        fragment = new Subscribed_Course_Details();

                                        bundle.putString("from" , SIMPLEE_HOME_TUTOR);
                                        String image = model.getCourse_thumbnail();
                                        String profileId = model.getAssign_to().getId();
                                        String name = model.getAssign_to().getName();
                                        String mobile = "+91-" + model.getAssign_to().getMobile();
                                        AssignProfile assignProfile = new AssignProfile(profileId , name , mobile , image);
                                        bundle.putSerializable("profileData" , assignProfile);
                                        bundle.putString("course_name" , model.getTitle());
                                        bundle.putString("course_id" , model.getId());
                                    }
                                    else {
                                        // go for not assigned course
                                        fragment = new Mycourse_deailFragment();
                                        Log.e("AdapterClocked" , model.getCourse_id());

                                        bundle.putString("course_id" , model.getCourse_id());
                                        bundle.putString("course_name" , model.getTitle());
                                        bundle.putString("course_creator_id" , model.getCreatorId());
                                        bundle.putString("live" , "0");
                                    }
                                } else {
                                    fragment = new Mycourse_deailFragment();
                                    bundle.putString("course_id" , model.getId());
                                    bundle.putString("course_title" , model.getTitle());
                                    bundle.putString("live" , "0");

                                    if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                                        bundle.putSerializable("profileData" , clist.get(postion).getAssign_by());
                                    }
                                }
                                if (fragment != null) {
                                    fragment.setArguments(bundle);
                                    SwitchFragment(fragment);
                                }
//

                            }

                            @Override
                            public void onSubscriptionClick(int position) {
//                StudentListFragment fragment=new StudentListFragment();
//                Bundle args = new Bundle();
//                args.putString("type","teacher");
//                args.putString("listType","subscription");
//                args.putString("user_id",clist.get(position).getUser_id());
//                args.putString("course_id",clist.get(position).getId());
//                fragment.setArguments(args);
//                SwitchFragment(fragment);
                            }

                            @Override
                            public void onDelete(int position) {
                                MyCourseDetailModel model = clist.get(position);
//                if (model.getStatus().equals("active") && model.getIs_subscriptions().equals("0"))
                                if (!TextUtils.isEmpty(model.getIs_subscriptions())) {
                                    if (Integer.parseInt(model.getIs_subscriptions()) > 0) {
                                        CommonMethods.showSuccessToast(getContext() , "You can't delete this course.");
                                    } else {
                                        showAler(position , 1);
                                    }
                                } else {
                                    showAler(position , 1);
                                }
//                else
//                    CommonMethods.showSuccessToast(getContext(),"You can't delete this course.");
                            }

                            @Override
                            public void onEdit(int position) {
                                if (clist.get(position).getStatus().equals("active"))
                                    showAler(position , 0);
                                else
                                    CommonMethods.showSuccessToast(getContext() , "You can't edit this course.");
                            }

                            @Override
                            public void onGoLive(int position) {
                                Mycourse_deailFragment fragment = new Mycourse_deailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("course_id" , clist.get(position).getId());
                                bundle.putString("course_title" , clist.get(position).getTitle());
                                bundle.putString("live" , "1");
                                fragment.setArguments(bundle);
                                SwitchFragment(fragment);
//                MyGoLiveTeacherFragment fragment = new MyGoLiveTeacherFragment();
//                SwitchFragment(fragment);

                            }

                            @Override
                            public void onProfileClick(int position) {
                                //------
                                String assignValue = "";
                                //------------
                                Bundle bundle = new Bundle();
                                AssignProfile profile = null;
                                if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                                    profile = clist.get(position).getAssign_by();
                                    assignValue = "1";
                                } else {
                                    profile = clist.get(position).getAssign_to();
                                    assignValue = "0";
                                }
                                if (profile != null) {
                                    AssigntTProfileDetailsFragment fragment = new AssigntTProfileDetailsFragment();
                                    bundle.putString("id" , profile.getId());
                                    bundle.putString("name" , profile.getName());
                                    bundle.putString("assignValue" , assignValue);
                                    fragment.setArguments(bundle);
                                    ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                                }
                                //  CommonMethods.showSuccessToast(getContext(),"Profile Click");
                            }
                        });
                        rec_course.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//               if (type.equalsIgnoreCase("MyCourse")) {
//                    params.put("assign_value","1" );
//                }
//                callApi(ApiCode.GET_COURSE, params);
                break;
            case ApiCode.GET_ASSIGN_BY_COURSES:
                params.put("user_id" , management.getString(USER_ID));
                params.put("assign_value" , "2");//pre 2
                if(subType.equals("Accepted")){
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            clist.clear();
                            for(DataSnapshot ds: snapshot.getChildren()){
                                for(DataSnapshot ds1:ds.getChildren()) {
                                    Log.e("DSFR" , ds1.getKey());
                                    if (ds1.child("assign_to_id").getValue(String.class) != null) {

                                        if (ds1.child("assign_to_id").getValue(String.class).equals(firebaseAuth.getUid())) {
                                            if (ds1.child("status").getValue(String.class).equals("Accepted")) {
                                                MyCourseDetailModel myCourseDetailModel = ds1.getValue(MyCourseDetailModel.class);
                                                Log.e("CLISHFYYD","MCM::::"+myCourseDetailModel.getCourse_id());
                                                clist.add(myCourseDetailModel);

                                            }
                                        }
                                    }
                                }
                            }
//                            Log.e("CLISHFYYD",""+clist.get(0).getCourse_id());

                            adapter = new MyCourseAdapter(getContext() , type , clist , new MyCourseAdapter.OnCourseClickListener() {
                                @Override
                                public void onItemClick(int postion) {
                                    Fragment fragment = null;
                                    Bundle bundle = new Bundle();
                                    MyCourseDetailModel model = clist.get(postion);
                                    Log.e("course_creator_id",type);
//                                    Log.e("course_creator_id",courseId);
                                    if (type.equals("MyCourse")) {
                                        if (model.getAssigned_value().equals("1")) {
                                            //go for view only mode
                                            fragment = new Subscribed_Course_Details();

                                            bundle.putString("from" , SIMPLEE_HOME_TUTOR);
                                            String image = BaseUrl.BASE_URL_MEDIA + model.getAssign_to().getImage();
                                            String profileId = model.getAssign_to().getId();
                                            String name = model.getAssign_to().getName();
                                            String mobile = "+91-" + model.getAssign_to().getMobile();
                                            AssignProfile assignProfile = new AssignProfile(profileId , name , mobile , image);
                                            bundle.putSerializable("profileData" , assignProfile);
                                            bundle.putString("course_name" , model.getTitle());
                                            bundle.putString("course_id" , model.getCourse_id());
                                            bundle.putString("course_creator_id" , model.getCreatorId());
//                                            Log.e("Assjhvhgdjyhgcvujgcjdyxm",model.getCreatorId());
                                        } else {
                                            // go for not assigned course
                                            fragment = new Mycourse_deailFragment();
                                            Log.e("AdapterClocked" , model.getCourse_id());

                                            bundle.putString("course_id" , model.getCourse_id());
                                            bundle.putString("course_creator_id" , model.getCreatorId());



                                            bundle.putString("course_title" , model.getTitle());
                                            bundle.putString("live" , "0");
                                        }
                                    } else {
                                        fragment = new Mycourse_deailFragment();
                                        bundle.putString("course_id" , model.getCourse_id());
                                        bundle.putString("course_title" , model.getTitle());
                                        bundle.putString("course_creator_id" , model.getCreatorId());
                                        Log.e("Assjhvhgdjyhgcvujgcjdyxm",model.getCreatorId());


                                        bundle.putString("live" , "0");

                                        if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                                            bundle.putSerializable("profileData" , clist.get(postion).getAssign_by());
                                        }
                                    }
                                    if (fragment != null) {
                                        fragment.setArguments(bundle);
                                        SwitchFragment(fragment);
                                    }
//

                                }

                                @Override
                                public void onSubscriptionClick(int position) {
//                StudentListFragment fragment=new StudentListFragment();
//                Bundle args = new Bundle();
//                args.putString("type","teacher");
//                args.putString("listType","subscription");
//                args.putString("user_id",clist.get(position).getUser_id());
//                args.putString("course_id",clist.get(position).getId());
//                fragment.setArguments(args);
//                SwitchFragment(fragment);
                                }

                                @Override
                                public void onDelete(int position) {
                                    MyCourseDetailModel model = clist.get(position);
//                if (model.getStatus().equals("active") && model.getIs_subscriptions().equals("0"))
                                    if (!TextUtils.isEmpty(model.getIs_subscriptions())) {
                                        if (Integer.parseInt(model.getIs_subscriptions()) > 0) {
                                            CommonMethods.showSuccessToast(getContext() , "You can't delete this course.");
                                        } else {
                                            showAler(position , 1);
                                        }
                                    } else {
                                        showAler(position , 1);
                                    }
//                else
//                    CommonMethods.showSuccessToast(getContext(),"You can't delete this course.");
                                }

                                @Override
                                public void onEdit(int position) {
                                    if (clist.get(position).getStatus().equals("active"))
                                        showAler(position , 0);
                                    else
                                        CommonMethods.showSuccessToast(getContext() , "You can't edit this course.");
                                }

                                @Override
                                public void onGoLive(int position) {
                                    Mycourse_deailFragment fragment = new Mycourse_deailFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("course_id" , clist.get(position).getId());
                                    bundle.putString("course_title" , clist.get(position).getTitle());
                                    bundle.putString("live" , "1");
                                    fragment.setArguments(bundle);
                                    SwitchFragment(fragment);
//                MyGoLiveTeacherFragment fragment = new MyGoLiveTeacherFragment();
//                SwitchFragment(fragment);

                                }

                                @Override
                                public void onProfileClick(int position) {
                                    //------
                                    String assignValue = "";
                                    //------------
                                    Bundle bundle = new Bundle();
                                    AssignProfile profile = null;
                                    if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                                        profile = clist.get(position).getAssign_by();
                                        assignValue = "1";
                                    } else {
                                        profile = clist.get(position).getAssign_to();
                                        assignValue = "0";
                                    }
                                    if (profile != null) {
                                        AssigntTProfileDetailsFragment fragment = new AssigntTProfileDetailsFragment();
                                        bundle.putString("id" , profile.getId());
                                        bundle.putString("name" , profile.getName());
                                        bundle.putString("assignValue" , assignValue);
                                        fragment.setArguments(bundle);
                                        ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                                    }
                                    //  CommonMethods.showSuccessToast(getContext(),"Profile Click");
                                }
                            });
                            rec_course.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else if(subType.equals("Request")){
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            clist.clear();
                            for(DataSnapshot ds: snapshot.getChildren()){
                                for(DataSnapshot ds1:ds.getChildren()) {
                                    Log.e("DSFR" , ds1.getKey());
                                    if (ds1.child("assign_to_id").getValue(String.class) != null) {

                                    if (ds1.child("assign_to_id").getValue(String.class).equals(firebaseAuth.getUid())) {
                                        if (ds1.child("status").getValue(String.class).equals("Pending")) {
                                            MyCourseDetailModel myCourseDetailModel = ds1.getValue(MyCourseDetailModel.class);
                                            Log.e("CLISHFYYD","MCM::::"+myCourseDetailModel.getCourse_id());
                                            clist.add(myCourseDetailModel);

                                        }
                                    }
                                }
                                }
                            }
//                            Log.e("CLISHFYYD",""+clist.get(0).getCourse_id());

                            pendingCourseAdapter=new PendingCourseAdapter(getContext(),clist);
                            rec_course.setAdapter(pendingCourseAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
//                callApi(ApiCode.GET_ASSIGN_BY_COURSES , params);
                break;
            case ApiCode.DELETE_COURSE:
                params.put("course_id" , delPosition);
                callApi(ApiCode.DELETE_COURSE , params);
                break;
            case ApiCode.GET_ASSIGNED_SUBJECT:
                params.put("teacher_id" , management.getString(USER_ID));
//                callApi(ApiCode.GET_ASSIGNED_SUBJECT , params);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subjectList.clear();
                        Log.e("SUVGFYGDF",subType);
                        for(DataSnapshot ds:snapshot.getChildren()){
                            for(DataSnapshot ds1:ds.getChildren()){
                                for(DataSnapshot ds2:ds1.child("Subject").getChildren()){
                                    if(ds2.child("assign_to_id").exists()){
                                        if(ds2.child("assign_to_id").getValue(String.class).equals(firebaseAuth.getUid())){
                                            TeacherSubject_Model teacherSubject_model=new TeacherSubject_Model();
                                            Log.e("jkgjhfghcyhnsjyrnhdmuyjd",ds2.child("subject_id").getValue(String.class));
                                            Log.e("jkgjhfghcyhnsjyrnhdmuyjd",subType);
                                            Log.e("jkgjhfghcyhnsjyrnhdmuyjd",ds2.child("status").getValue(String.class));
                                            teacherSubject_model.setTitle(ds2.child("title").getValue(String.class));
                                            teacherSubject_model.setId(ds2.child("subject_id").getValue(String.class));
                                            teacherSubject_model.setCover_image(ds2.child("section_thumbnail").getValue(String.class));
                                            teacherSubject_model.setAssign_by_id(ds2.child("assign_by_id").getValue(String.class));
                                            teacherSubject_model.setAssign_to_name(ds2.child("assign_to_name").getValue(String.class));
                                            teacherSubject_model.setAssigm_to_id(ds2.child("assign_to_id").getValue(String.class));
                                            teacherSubject_model.setCourse_id(ds2.child("course_id").getValue(String.class));
                                            if(subType.equals("Accepted") && ds2.child("status").getValue(String.class).equals("Accepted")){
                                                teacherSubject_model.setStatus(ds2.child("status").getValue(String.class));
                                                subjectList.add(teacherSubject_model);

                                            }else if(subType.equals("Request")&&ds2.child("status").getValue(String.class).equals("Pending")){
                                                teacherSubject_model.setStatus(ds2.child("status").getValue(String.class));
                                                subjectList.add(teacherSubject_model);
                                            }





                                        }
                                    }
                                }
                            }
                            if(subjectList.size()>0){
                                setSubjectList();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;

        }
    }

    private void callApi(int request , HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback , getActivity());
        switch (request) {
            case ApiCode.GET_COURSE:
                service.postDataVolley(ApiCode.GET_COURSE ,
                        BaseUrl.URL_GET_COURSE , params);
                break;
            case ApiCode.GET_ASSIGN_BY_COURSES:
                service.postDataVolley(ApiCode.GET_ASSIGN_BY_COURSES ,
                        BaseUrl.URL_GET_ASSIGN_BY_COURSES , params);
                break;
            case ApiCode.DELETE_COURSE:
                service.postDataVolley(ApiCode.DELETE_COURSE ,
                        BaseUrl.URL_DELETE_COURSE , params);
                break;
            case ApiCode.GET_ASSIGNED_SUBJECT:
                service.postDataVolley(ApiCode.GET_ASSIGNED_SUBJECT ,
                        BaseUrl.URL_GET_ASSIGNED_SUBJECT , params);
                break;

        }
    }


    @Override
    public void onResponse(int requestType , String response) {
        switch (requestType) {
            case ApiCode.GET_COURSE:
            case ApiCode.GET_ASSIGN_BY_COURSES:
                Log.e("courses " , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            ArrayList<MyCourseDetailModel> list = new Gson().
                                    fromJson(jsonArray.toString() ,
                                            new TypeToken<List<MyCourseDetailModel>>() {
                                            }.getType());
                            clist.clear();
                            clist.addAll(list);
                            setDataOnList();
                        } else {
                            clist.clear();
                            setDataOnList();
                            CommonMethods.showSuccessToast(getContext() , "You have not created any course");
                        }

                    } else {
                        clist.clear();
                        setDataOnList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.DELETE_COURSE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getContext() , "Deleted Successfully");
                        if (ConnectivityReceiver.isConnected()) {
                            sendGetCourseRequest(ApiCode.GET_COURSE , null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.GET_ASSIGNED_SUBJECT:
                Log.e("assSub " , response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            ArrayList<TeacherSubject_Model> list = new Gson().
                                    fromJson(jsonArray.toString() ,
                                            new TypeToken<List<TeacherSubject_Model>>() {
                                            }.getType());
                            subjectList.clear();
                            subjectList.addAll(list);
                            setSubjectList();
                        } else {
                            subjectList.clear();
                            setSubjectList();
                            //CommonMethods.showSuccessToast(getContext(),"You have not created any course");
                        }

                    } else {
                        subjectList.clear();
                        setSubjectList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        sendREquest();
    }
}
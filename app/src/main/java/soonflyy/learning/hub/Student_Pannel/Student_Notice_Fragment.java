package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Notice_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.Notice;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Student_Notice_Fragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rv_notice;
    LinearLayout lin_notice_title;
    SwipeRefreshLayout refresh_notice;

    Notice_Adapter noticeAdapter;
    ArrayList<Notice>noticeArrayList=new ArrayList<>();
    String chapter_id,subject_id,course_id;
    String teacher_id,teacher_name,class_id,section_id,from;
    String noticeType="",assignType="",homeNoticeType="";

    //---------assign to profile--------//
    View assignToLayout;
    CircleImageView ivProfileImg;
    TextView tvProfileName,tvType;
    ImageView tvMobileNo;
    //-------------------//


    public Student_Notice_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_student__notice_, container, false);

        bindViewId(view);
        getArgumentData();
        requestApi();
        refresh_notice.setOnRefreshListener(this);
       // setNotice();
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");

        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            teacher_name=getArguments().getString("teacher_name");
            class_id =getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");
            teacher_id=getArguments().getString("teacher_id");
            subject_id=getArguments().getString("subject_id");
            chapter_id=getArguments().getString("chapter_id");
        }else if (from.equals(SIMPLEE_HOME_STUDENT)) {
            homeNoticeType=getArguments().getString("notice");
            course_id = getArguments().getString("course_id");
            if (homeNoticeType.equals("subject")) {
                subject_id = getArguments().getString("subject_id");
            }
        }else if (from.equals(SIMPLEE_HOME_TUTOR)){
            //get argument data for assigned course notice---------//
            assignType=getArguments().getString("type");
            if (assignType.equals("AssignBy")){
                noticeType=getArguments().getString("noticeType");
            }else {
                course_id = getArguments().getString("course_id");
                homeNoticeType=getArguments().getString("notice");
                if (homeNoticeType.equals("subject")) {
                    subject_id = getArguments().getString("subject_id");
                    AssignProfile assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
                    setProfileData(assignProfile);
                }

            }
            //-------------------------------------------------------//
        }

    }

    private void requestApi() {
        if (ConnectivityReceiver.isConnected()){
            if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_NOTICE);
            }else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                if (from.equals(SIMPLEE_HOME_TUTOR)){
                    if (assignType.equals("AssignBy")){
                        //assign for
                        sendRequest(ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS);
                    }else{
                        //for assign to
                        if (homeNoticeType.equals("subject")){
                            //foe subject wise notice
                            sendRequest(ApiCode.GET_SUBJECT_NOTICE);
                        }else{
                            //for course wise notice
                            sendRequest(ApiCode.GET_NOTICE);
                        }

                    }
                }else {
                    if (homeNoticeType.equals("subject")){
                        //student subject wise notice
                        sendRequest(ApiCode.GET_SUBJECT_NOTICE);
                    }else{
                        //for student course wise notice

                        sendRequest(ApiCode.GET_NOTICE);
                    }

                }
            }

        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void setNotice() {
        rv_notice.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeAdapter=new Notice_Adapter(getContext(), noticeArrayList, new Notice_Adapter.OnNoticeMsgClickListener() {
            @Override
            public void onClick(int position) {
                showMessageDialog(noticeArrayList.get(position).getMsg());
            }
        });
        rv_notice.setAdapter(noticeAdapter);
        noticeAdapter.notifyDataSetChanged();

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
        TextView tv_msg =dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void bindViewId(View view) {
        //----------//
        tvProfileName=view.findViewById(R.id.assign_tv_name);
        tvMobileNo=view.findViewById(R.id.assign_tv_mobile);
        ivProfileImg=view.findViewById(R.id.assign_iv_profile_img);
        assignToLayout=view.findViewById(R.id.include_assign_to);
        tvType=view.findViewById(R.id.tv_type);
        tvType.setText("Assigned to");
        //--------------//
        rv_notice=view.findViewById(R.id.student_notice_rv);
        lin_notice_title=view.findViewById(R.id.lin_notice_title);
        refresh_notice=view.findViewById(R.id.refresh_notice);
    }
    private void setProfileData(AssignProfile profile) {
        if (profile!=null) {
            String link = BaseUrl.BASE_URL_MEDIA + profile.getImage();
            Log.e("image", link);
            Picasso.get().load(link).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            tvProfileName.setText(profile.getName());
//            tvMobileNo.setText("+91-" + profile.getMobile());
            tvMobileNo.setVisibility(View.GONE);
            assignToLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // ((MainActivity)getActivity()).setTitle("Internet Of Things (IOT)");
      //  ((MainActivity)getActivity()).setStudentChildActionBar("Internet Of Things (IOT)",false);
       if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
           ((Subject_Chapter_Details_Fragment) getParentFragment()).setDppBackground();
       }else if (from.equals(SIMPLEE_HOME_TUTOR)){
        ///for assign by tutor notice
           if (getArguments().getString("type").equals("AssignBy")) {
               //for assigned by course & subject notice in tutor section
               ((TeacherMainActivity) getActivity()).setTeacherActionBar(getArguments().getString("noticeTitle"), false);
           }else {
               //for assign to teachre
               if (homeNoticeType.equals("subject")){
                   //for subject wise notice assigned to
                   ((TeacherMainActivity)getActivity()).setTeacherActionBar(getArguments().getString("subTitle"),false);

               }else {
                   //for assign to course notice
                   ((Subscribed_Course_Details) getParentFragment()).setNoticeBgColor();
                   ((Subscribed_Course_Details) getParentFragment()).showAssignToProfile();
               }
           }

       } else {
           if (homeNoticeType.equals("subject")){
               //for student subject wise notice assigned to
               ((MainActivity)getActivity()).setStudentChildActionBar(getArguments().getString("subTitle"),false);

           }else {
               //student course wise notice
               ((Subscribed_Course_Details) getParentFragment()).setNoticeBgColor();
           }
       }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_NOTICE:
             //   params.put("type","0");
                params.put("course_id",course_id);
                if (from.equals(SIMPLEE_HOME_TUTOR)){
                    //don't post user_id
                }else {
                  //  params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                }
                callApi(ApiCode.GET_NOTICE, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE:
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_NOTICE, params);
                break;
            case ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS:
                params.put("type",noticeType);
                params.put("tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS, params);
                break;
            case ApiCode.GET_SUBJECT_NOTICE:
                params.put("course_id",course_id);
                params.put("subject_id",course_id);
                callApi(ApiCode.GET_SUBJECT_NOTICE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_NOTICE:
                service.postDataVolley(ApiCode.GET_NOTICE,
                        BaseUrl.URL_GET_NOTICE, params);
                Log.e("api", BaseUrl.URL_GET_NOTICE);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_NOTICE:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE,
                        BaseUrl.URL_SCHOOL_GET_NOTICE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTICE);
                Log.e("params", params.toString());
                break;
            case ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS:
                service.postDataVolley(ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS,
                        BaseUrl.URL_GET_NOTICE_BY_ASSIGNED_TUTORS, params);
                Log.e("api", BaseUrl.URL_GET_NOTICE_BY_ASSIGNED_TUTORS);
                Log.e("params", params.toString());
                break;
            case ApiCode.GET_SUBJECT_NOTICE:
                service.postDataVolley(ApiCode.GET_SUBJECT_NOTICE,
                        BaseUrl.URL_GET_SUBJECT_NOTICE, params);
                Log.e("url:", BaseUrl.URL_GET_SUBJECT_NOTICE);
                Log.e("params:", "" + params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_NOTICE:
            case ApiCode.SCHOOL_GET_NOTICE:
            case ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS:
            case ApiCode.GET_SUBJECT_NOTICE:
                Log.e("notice_list",response);
                if(jsonObject.getBoolean("status")){
                    JSONArray array=jsonObject.getJSONArray("data");
                    if(array.length()>0){
                        ArrayList<Notice> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<ArrayList<Notice>>() {
                                        }.getType());
                        noticeArrayList.clear();
                        noticeArrayList.addAll(psearch);
                        setNotice();
                    }else{
                        noticeArrayList.clear();
                        setNotice();
                    }
                }
                break;
//            case ApiCode.GET_NOTICE_BY_ASSIGNED_TUTORS:
//                Log.e("ASSIGNbY",response);
//                break;
        }

    }

    @Override
    public void onRefresh() {
        refresh_notice.setRefreshing(false);
        requestApi();
    }
}
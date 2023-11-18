package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Subscribed_Course_Details;
import soonflyy.learning.hub.Student_Pannel.TopicFragment;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter.AssignedCourseAdapter;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter.AssignedSubjectAdapter;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AssignedCourses;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AssignedSubjects;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssigntTProfileDetailsFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    LinearLayout lin_about, lin_course, lin_subject;
    ImageView iv_course_down, iv_about_down, iv_subject_down;
    LinearLayout lin_about_detail, lin_course_detail, lin_sub_detail;
    RecyclerView rec_profile_course, rec_profile_subject;

    CircleImageView ivProfileImg;
    TextView tvName, tvMobile, tvTotalSubscription;
    Button btnUpdateAbout;

    AssignedCourseAdapter assignCourse;
    ArrayList<AssignedCourses> assignCourseList = new ArrayList<>();
    AssignedSubjectAdapter assignSubAdapter;
    ArrayList<AssignedSubjects> assignSubList = new ArrayList<>();
    RelativeLayout rel_profile;
    RelativeLayout rel_show_image;
    RoundedImageView rvProfileImage;
    String imageString = "", profileId,courseId="",subjectId="",type="",about="",assignValue="";
    EditText et_about_message;

    AssignProfile assignProfile;



    public AssigntTProfileDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_teacher_sub_course_profile, container, false);

        initView(view);
        getArgumentData();
        setAssignSubjects();
        setAssignCourses();
        setApiCall();
        return view;
    }

    private void getArgumentData() {

        profileId = getArguments().getString("id");
        assignValue=getArguments().getString("assignValue");
    }

    private void initView(View view) {
        rel_profile = view.findViewById(R.id.rel_profile);
        lin_about = view.findViewById(R.id.lin_about);
        ivProfileImg = view.findViewById(R.id.assign_iv_profile_img);
        tvName = view.findViewById(R.id.assign_tv_name);
        tvMobile = view.findViewById(R.id.assign_tv_mobile);
        tvTotalSubscription = view.findViewById(R.id.tv_subcriber);

        lin_course = view.findViewById(R.id.lin_course);
        lin_about_detail = view.findViewById(R.id.lin_about_detail);
        lin_course_detail = view.findViewById(R.id.lin_course_detail);
        lin_sub_detail = view.findViewById(R.id.lin_sub_detail);
        lin_subject = view.findViewById(R.id.lin_subject);
        rec_profile_course = view.findViewById(R.id.rec_profile_course);
        rec_profile_subject = view.findViewById(R.id.rec_profile_subject);
        rec_profile_course.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_profile_subject.setLayoutManager(new LinearLayoutManager(getActivity()));


        iv_about_down = view.findViewById(R.id.iv_about_down);
        iv_subject_down = view.findViewById(R.id.iv_subject_down);
        iv_course_down = view.findViewById(R.id.iv_course_down);
        et_about_message = view.findViewById(R.id.et_about_message);
        btnUpdateAbout=view.findViewById(R.id.btn_update);

        initGo();
    }

    private void initGo() {
        btnUpdateAbout.setOnClickListener(this);
        rel_profile.setOnClickListener(this);
        iv_subject_down.setOnClickListener(this);
        iv_course_down.setOnClickListener(this);
        iv_about_down.setOnClickListener(this);

    }

    private void setAssignCourses() {

        assignCourse = new AssignedCourseAdapter(getActivity(), assignCourseList,
                new AssignedCourseAdapter.OnCourseClickListener() {
                    @Override
                    public void onItemClick(int postion) {
                        Subscribed_Course_Details course_details = new Subscribed_Course_Details();
                        Bundle bundle = new Bundle();
                        bundle.putString("from", SIMPLEE_HOME_TUTOR);
                        bundle.putSerializable("profileData",assignProfile);
                        bundle.putString("course_name",assignCourseList.get(postion).getCourse_name());
                        bundle.putString("course_id",assignCourseList.get(postion).getCourse_id());
                        course_details.setArguments(bundle);
                        ((TeacherMainActivity) getActivity()).SwitchFragment(course_details);
                    }

                    @Override
                    public void onUnassignCourse(int position, AssignedCourses model) {
                        type="course";
                        courseId=model.getCourse_id();
                        if (CommonMethods.checkInternetConnection(getActivity())){
                            sendRequest(ApiCode.UNASSIGN_COURSE_SUBJECT);
                        }
                    }
                });
        rec_profile_course.setAdapter(assignCourse);
    }

    private void setAssignSubjects() {
        assignSubAdapter = new AssignedSubjectAdapter(getActivity(), assignSubList, new AssignedSubjectAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int position,AssignedSubjects model) {
                TopicFragment fragment = new TopicFragment();
                //Subject_Chapter_Details_Fragment fragment=new Subject_Chapter_Details_Fragment();
                //  FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
                Bundle arg = new Bundle();
                arg.putString("from", SIMPLEE_HOME_TUTOR);
                arg.putString("subject_id", model.getSubject_id());
                arg.putString("course_id", model.getCourse_id());
                arg.putString("course_name", model.getCourse_name());
                arg.putString("subject_name", model.getSubject_name());
                arg.putInt("subjectPosition", position + 1);
                arg.putSerializable("profileData",assignProfile);
                fragment.setArguments(arg);
                ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
            }

            @Override
            public void onUnassignSubject(int position, AssignedSubjects model) {
                type="subject";
                courseId=model.getCourse_id();
                subjectId=model.getSubject_id();
                if (CommonMethods.checkInternetConnection(getActivity())){
                    sendRequest(ApiCode.UNASSIGN_COURSE_SUBJECT);
                }
            }

        });
        rec_profile_subject.setAdapter(assignSubAdapter);

    }


    public void onClick(View view) {
        if (ConnectivityReceiver.isConnected()) {
            switch (view.getId()) {
                case R.id.iv_about_down:
                    //Toast.makeText(getActivity(), "about", Toast.LENGTH_SHORT).show();
                    if (lin_about_detail.getVisibility() == View.VISIBLE) {
                        iv_about_down.setRotation(Float.parseFloat("360"));
                        lin_about_detail.setVisibility(View.GONE);
                    } else {
                        iv_about_down.setRotation(Float.parseFloat("180"));
                        lin_about_detail.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_course_down:
                    dismissUnassignWindow();
                    if (lin_course_detail.getVisibility() == View.VISIBLE) {
                        iv_course_down.setRotation(Float.parseFloat("360"));
                        lin_course_detail.setVisibility(View.GONE);
                    } else {
                        iv_course_down.setRotation(Float.parseFloat("180"));
                        lin_course_detail.setVisibility(View.VISIBLE);

                    }
                    break;
                case R.id.iv_subject_down:
                    dismissUnassignWindow();
                    if (lin_sub_detail.getVisibility() == View.VISIBLE) {
                        iv_subject_down.setRotation(Float.parseFloat("360"));
                        lin_sub_detail.setVisibility(View.GONE);
                    } else {
                        iv_subject_down.setRotation(Float.parseFloat("180"));
                        lin_sub_detail.setVisibility(View.VISIBLE);

                    }
                    break;
                case R.id.rel_profile:
                    showEditProfileDailoge();
                    break;
                case R.id.btn_update:
                    updateAbout();
                    break;

            }
        } else {
        }
    }

    private void updateAbout() {
        about=et_about_message.getText().toString().trim();
        if (TextUtils.isEmpty(about)) {
            et_about_message.setError("Pleas Write here");
            et_about_message.requestFocus();
        }else{
            if (CommonMethods.checkInternetConnection(getActivity())){
                sendRequest(ApiCode.UPDATE_ABOUT_PROFILE);
            }
        }
    }

    private void showEditProfileDailoge() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailoge_upload_profile_image);
        Button btn_upload = dialog.findViewById(R.id.btn_upload);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        ImageView ivCancel = dialog.findViewById(R.id.iv_cancel);
        LinearLayout lin_add_image = dialog.findViewById(R.id.lin_add_image);
        rel_show_image = dialog.findViewById(R.id.rel_show_image);
        ImageView ivChoose = dialog.findViewById(R.id.iv_sub);
        rvProfileImage = dialog.findViewById(R.id.iv_upload_img);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_show_image.setVisibility(View.GONE);
                imageString = "";
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        ivChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(imageString)) {
                    CommonMethods.showSuccessToast(getContext(), "Choose Profile Image");
                } else {
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.UPDATE_ASSIGN_TUTOR_IMG);
                        dialog.dismiss();
                    }
                    //

                }


            }
        });
        dialog.show();

    }

    private void chooseProfileImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                Glide.with(getActivity()).load(imgUri).into(rvProfileImage);

                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                CommonMethods.showSuccessToast(getContext(), "Image Selected");
                rel_show_image.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(getArguments().getString("name"), false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      dismissUnassignWindow();
    }

    private void dismissUnassignWindow() {
        try {
            if (assignCourse != null) {
                PopupWindow window = assignCourse.getPopUpWindow();
                if (window != null) {
                    if (window.isShowing()) {
                        window.dismiss();
                    }
                }
            }
            if (assignSubAdapter != null) {
                PopupWindow window = assignSubAdapter.getPopUpWindow();
                if (window != null) {
                    if (window.isShowing()) {
                        window.dismiss();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setApiCall() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.GET_ASSIGN_PROFILE_DETAILS);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_ASSIGN_PROFILE_DETAILS:
                if (assignValue.equals("0")) {
                    params.put("assigned_by_id", new SessionManagement(getActivity()).getString(USER_ID));
                    params.put("assigned_tutor_id", profileId);
                }else {
                    params.put("assigned_tutor_id", new SessionManagement(getActivity()).getString(USER_ID));
                    params.put("assigned_by_id", profileId);
                }
                    //-----------
                    params.put("type", assignValue);
                    //---------

                callApi(ApiCode.GET_ASSIGN_PROFILE_DETAILS, params);
                break;
            case ApiCode.UPDATE_ASSIGN_TUTOR_IMG:
                // params.put("tutor_id",new Session_management(getActivity()).getString(USER_ID));
                params.put("tutor_id", profileId);
                params.put("profile_image", imageString);
                callApi(ApiCode.UPDATE_ASSIGN_TUTOR_IMG, params);
                break;
            case ApiCode.UNASSIGN_COURSE_SUBJECT:

                params.put("assign_by_id", new SessionManagement(getActivity()).getString(USER_ID));
                params.put("assign_to_id", profileId);
                params.put("type", type);
                if (type.equals("course")) {
                    params.put("course_id", courseId);
                }else if (type.equals("subject")){
                    params.put("course_id", courseId);
                    params.put("subject_id", subjectId);
                }
                callApi(ApiCode.UNASSIGN_COURSE_SUBJECT, params);
                break;
            case ApiCode.UPDATE_ABOUT_PROFILE:
                // params.put("tutor_id",new Session_management(getActivity()).getString(USER_ID));
                params.put("tutor_id", profileId);
                params.put("about", about);
                callApi(ApiCode.UPDATE_ABOUT_PROFILE, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_ASSIGN_PROFILE_DETAILS:
                service.postDataVolley(ApiCode.GET_ASSIGN_PROFILE_DETAILS,
                        BaseUrl.URL_GET_ASSIGN_PROFILE_DETAILS, params);
                Log.e("url", BaseUrl.URL_GET_ASSIGN_PROFILE_DETAILS);
                Log.e("params", "" + params);
                break;
            case ApiCode.UPDATE_ASSIGN_TUTOR_IMG:
                service.postDataVolley(ApiCode.UPDATE_ASSIGN_TUTOR_IMG,
                        BaseUrl.URL_UPDATE_ASSIGN_TUTOR_IMG, params);
                Log.e("url", BaseUrl.URL_UPDATE_ASSIGN_TUTOR_IMG);
                Log.e("params", "" + params);
                break;
            case ApiCode.UNASSIGN_COURSE_SUBJECT:
                service.postDataVolley(ApiCode.UNASSIGN_COURSE_SUBJECT,
                        BaseUrl.URL_UNASSIGN_COURSE_SUBJECT, params);
                Log.e("url", BaseUrl.URL_UNASSIGN_COURSE_SUBJECT);
                Log.e("params", "" + params);
                break;
            case ApiCode.UPDATE_ABOUT_PROFILE:
                service.postDataVolley(ApiCode.UPDATE_ABOUT_PROFILE,
                        BaseUrl.URL_UPDATE_ABOUT_PROFILE, params);
                Log.e("url", BaseUrl.URL_UPDATE_ABOUT_PROFILE);
                Log.e("params", "" + params);
                break;


        }
    }


    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType) {
            case ApiCode.GET_ASSIGN_PROFILE_DETAILS:
                Log.e("profileDtls ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            JSONObject object = jsonArray.getJSONObject(0);
                            setProfileData(object);
                        }
                    }else{
                        assignCourseList.clear();
                        assignSubList.clear();
                        setAssignCourses();
                        setAssignSubjects();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.UPDATE_ASSIGN_TUTOR_IMG:
                Log.e("imgUpdate ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getContext(), "Updated Successfully");
                        String imgUrl = BaseUrl.BASE_URL_MEDIA + jsonObject.getString("data");
                        Log.e("profileImg", "" + imgUrl);
                        Picasso.get().load(imgUrl).into(ivProfileImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case ApiCode.UNASSIGN_COURSE_SUBJECT:
                Log.e("unassign ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                        setApiCall();
                    }else{
                        CommonMethods.showSuccessToast(getActivity(),"Update Error");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case ApiCode.UPDATE_ABOUT_PROFILE:
                Log.e("about", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getActivity(),"Updated Successfully");
                    }else{
                        CommonMethods.showSuccessToast(getActivity(),"Update Error");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;



        }
    }

    private void setProfileData(JSONObject object) {
        try {

            String imageLink = BaseUrl.BASE_URL_MEDIA + object.getString("image");
            Log.e("imgLink", "" + imageLink);
            Picasso.get().load(imageLink).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            String name=object.getString("name");
            tvName.setText(name);
            String mobile="+91-" + object.getString("mobile");
            tvMobile.setText(mobile);
            //-------set assign to profile data -----------//
            assignProfile=new AssignProfile(profileId,name,mobile,imageLink);

            //------------------------------//
            tvTotalSubscription.setText("Total Subscribers:"+object.getString("total_subscriptions"));
            about=object.getString("about");
            et_about_message.setText(about);
            setAssignedCourse(object.getJSONArray("assinedCourses"));
            setAssignedSubject(object.getJSONArray("assignedsSubject"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAssignedCourse(JSONArray assiCourseArray) {
        if (assiCourseArray.length() > 0) {

            ArrayList<AssignedCourses> list = new Gson().
                    fromJson(assiCourseArray.toString(),
                            new TypeToken<List<AssignedCourses>>() {
                            }.getType());
            assignCourseList.clear();
            assignCourseList.addAll(list);
            setAssignCourses();

        } else {
            assignCourseList.clear();
            setAssignCourses();
        }
    }

    private void setAssignedSubject(JSONArray assiSubArray) {
        if (assiSubArray.length() > 0) {
            ArrayList<AssignedSubjects> list = new Gson().
                    fromJson(assiSubArray.toString(),
                            new TypeToken<List<AssignedSubjects>>() {
                            }.getType());
            assignSubList.clear();
            assignSubList.addAll(list);
            setAssignSubjects();
        } else {
            assignSubList.clear();
            setAssignSubjects();
        }

    }


}
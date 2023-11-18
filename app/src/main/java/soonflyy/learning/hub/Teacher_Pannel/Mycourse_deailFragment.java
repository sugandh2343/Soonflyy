package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Mycourse_deailFragment extends Fragment implements View.OnClickListener {


    TextView tv_about,tv_test,tv_subject,tv_notice;
    LinearLayout lin_about_course, lin_notices,ll_pdf,ll_test,lin_subject;
    ImageView iv_about,iv_subject,iv_notice,iv_test;
    String course_id,course_title,course_creator_id;
    String is_for_live;
    TextView tvName;
    ImageView tvMobile;
    CircleImageView ivProfileImg;

    SessionManagement sessionManagement;
    AssignProfile profileData;

    //------profile manage-------------//
   View profileLayoutView;
    //-------------------------//

    public Mycourse_deailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mycourse_deail, container, false);
        sessionManagement=new SessionManagement(getActivity());
        initView(view);
       showAssignProfile();
       if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
           profileData= (AssignProfile) getArguments().getSerializable("profileData");
           setProfileData(profileData);
       }

        course_id=getArguments().getString("course_id");
        course_title=getArguments().getString("course_title");
        course_creator_id=getArguments().getString("course_creator_id");
        is_for_live=getArguments().getString("live");
        initControl();
        return view;
    }

    private void initView(View view) {

        profileLayoutView=view.findViewById(R.id.include_assign_profile);
        profileLayoutView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white_smoke));

        lin_about_course=view.findViewById(R.id.lin_about);
        iv_about= view.findViewById(R.id.iv_about);
        tv_about=view.findViewById(R.id.tv_about);
        lin_subject=view.findViewById(R.id.lin_subject);
        iv_subject=view.findViewById(R.id.iv_subject);
        tv_subject=view.findViewById(R.id.tv_subject);

        lin_notices=view.findViewById(R.id.lin_note);
        iv_notice=view.findViewById(R.id.iv_note);
        tv_notice=view.findViewById(R.id.tv_note);

        ll_test=view.findViewById(R.id.lin_test);
        iv_test=view.findViewById(R.id.iv_test);
        tv_test=view.findViewById(R.id.tv_test);

        tvName=view.findViewById(R.id.assign_tv_name);
        tvMobile=view.findViewById(R.id.assign_tv_mobile);
        ivProfileImg=view.findViewById(R.id.assign_iv_profile_img);

        //chat_btn.setOnTouchListener(this);

        lin_about_course.setOnClickListener(this);
        lin_notices.setOnClickListener(this);
        lin_subject.setOnClickListener(this);
        ll_test.setOnClickListener(this);

    }

    private void initControl() {

        if (getChildFragmentManager().getBackStackEntryCount()==0){
            if (is_for_live.equals("1")){
                showSubjectFragment();
            }else {
                showAboutFragment();
            }
        }

    }
    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.frame_layout_container, fragment);//, ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void showAssignProfile(){
        String assignValue=sessionManagement.getString(ASSIGN);
        if (assignValue !=null){
            if (assignValue.equals(ASSIGN_BY)) {
                profileLayoutView.setVisibility(View.VISIBLE);

            } else
                profileLayoutView.setVisibility(View.GONE);
        }else{
            profileLayoutView.setVisibility(View.GONE);
        }
    }

    public void setProfileData(AssignProfile profileModel) {
        if (profileModel!=null){
            String link= BaseUrl.BASE_URL_MEDIA+profileModel.getImage();
            Log.e("link",link);
            Picasso.get().load(link).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            tvName.setText(profileModel.getName());

        }

    }

    public  void hideAssignProfile(){
        profileLayoutView.setVisibility(View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (v.getId()){
            case R.id.lin_about:
                showAboutFragment();
                break;
            case R.id.lin_subject:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showSubjectFragment();
                break;
            case R.id.lin_note:
                 // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showNoticeFragment();
                break;
            case R.id.lin_test:
              //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showTestFragment();
                // Log.e("testclick","click");
                break;
        }
    }

    private void showAboutFragment() {
       setAboutBgColor();
//chat_btn.setVisibility(View.GONE);
        //MyCurse_AboutFragment fragment = new MyCurse_AboutFragment();
        CreateCourseFragment fragment=new CreateCourseFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type","update");
        bundle.putString("course_id",course_id);
        bundle.putString("course_title",course_title);
        bundle.putString("course_creator_id",course_creator_id);

        fragment.setArguments(bundle);
//        Bundle bundle=new Bundle();
//        bundle.putString("type",type);
//        if (type.equals("subscription")) {
//            bundle.putParcelable("sCourse", sub_course);
//        }
//        if (type.equals("bookmark")){
//            bundle.putParcelable("sCourse", bookMarkCourse);
//        }
//        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }


    private void showTestFragment() {
        setTestColor();
       // chat_btn.setVisibility(View.VISIBLE);

        MyTeacherManageTestFragment fragment = new MyTeacherManageTestFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",SIMPLEE_HOME_TUTOR);
        bundle.putString("type","course");
        bundle.putString("course_id",course_id);
        bundle.putString("subject_id","");
        bundle.putString("chapter_id"," ");
        bundle.putString("course_title",course_title);
        fragment.setArguments(bundle);
//
        SwitchFragment(fragment);
    }
    private void showNoticeFragment() {
        setNoticeBgColor();
        MyNoticeFragment fragment = new MyNoticeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("course_title",course_title);
        bundle.putString("notice","course");
        fragment.setArguments(bundle);
//
        SwitchFragment(fragment);
    }

    private void showSubjectFragment() {
        showSubjectBg();
        //Toast.makeText(getActivity(), "subject fragment", Toast.LENGTH_SHORT).show();
        MyCourseSubjectFragment fragment = new MyCourseSubjectFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("course_title",course_title);
        if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)) {
            bundle.putSerializable("profileData",profileData);
        }
//
        fragment.setArguments(bundle);
        SwitchFragment(fragment);

    }

    public void setTestColor() {
        lin_about_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_about.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_about.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notices.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        ll_test.requestFocus();
    }

    public void setAboutBgColor() {

       lin_about_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_about.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_about.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notices.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
  }

    public void setNoticeBgColor() {

        lin_about_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_about.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_about.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notices.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }


    public void showSubjectBg() {
        lin_about_course.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_about.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_about.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_subject.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_subject.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_subject.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_notices.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }



}
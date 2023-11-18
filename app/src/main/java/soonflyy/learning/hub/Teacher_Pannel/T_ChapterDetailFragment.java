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


public class T_ChapterDetailFragment extends Fragment implements View.OnClickListener {

    TextView tv_liveclass,tv_test, tv_video, tv_note,tv_dpp,tvProfileName;
    CircleImageView ivProfileImg;
    LinearLayout lin_live_class, lin_note,lin_dpp,ll_test, lin_video;
    ImageView iv_liveclass, iv_video, iv_note,iv_test,iv_dpp,tvProfileMobile;

    String course_id,subject_id,chapter_id;
    String title;

    View profileLayoutView;
    SessionManagement sessionManagement;
    AssignProfile assignProfile;
    public T_ChapterDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teacher_live_class, container, false);
        initView(view);
        sessionManagement=new SessionManagement(getActivity());
        showAssignProfile();
        getArgumentData();
        initControl();

        return view;
    }

    private void getArgumentData() {
        course_id=getArguments().getString("course_id");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        title=getArguments().getString("title");
        if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
            setProfileData(assignProfile);
        }
    }

    private void setProfileData(AssignProfile model) {
        String link= BaseUrl.BASE_URL_MEDIA+model.getImage();
        Log.e("imge",link);
        Picasso.get().load(link).placeholder(R.drawable.logoo)
                .into(ivProfileImg);
        tvProfileName.setText(model.getName());
//        tvProfileMobile.setText("+91-"+model.getMobile());
    }

    private void initView(View view) {

        ivProfileImg=view.findViewById(R.id.assign_iv_profile_img);
        tvProfileMobile=view.findViewById(R.id.assign_tv_mobile);
        tvProfileName=view.findViewById(R.id.assign_tv_name);
        profileLayoutView=view.findViewById(R.id.include_assign_profile);
        profileLayoutView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white_smoke));

        lin_live_class =view.findViewById(R.id.lin_live_class);
        iv_liveclass = view.findViewById(R.id.iv_liveclass);
        tv_liveclass =view.findViewById(R.id.tv_liveclass);
        lin_video =view.findViewById(R.id.lin_video);
        iv_video =view.findViewById(R.id.iv_video);
        tv_video =view.findViewById(R.id.tv_video);

        lin_note =view.findViewById(R.id.lin_note);
        iv_note =view.findViewById(R.id.iv_note);
        tv_note =view.findViewById(R.id.tv_note);

        ll_test=view.findViewById(R.id.lin_test);
        iv_test=view.findViewById(R.id.iv_test);
        tv_test=view.findViewById(R.id.tv_test);

        lin_dpp=view.findViewById(R.id.lin_notice);
        iv_dpp=view.findViewById(R.id.iv_notice);
        tv_dpp=view.findViewById(R.id.tv_notice);
        //chat_btn.setOnTouchListener(this);

        lin_live_class.setOnClickListener(this);
        lin_note.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        ll_test.setOnClickListener(this);
        lin_dpp.setOnClickListener(this);

    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount()==0){
            showLiveFragment();
        }

    }
    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.frame_layout_live, fragment);//, ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showAssignProfile(){
        String assignValue=sessionManagement.getString(ASSIGN);
        if (assignValue !=null){
            if (assignValue.equals(ASSIGN_BY))
                profileLayoutView.setVisibility(View.VISIBLE);
            else
                profileLayoutView.setVisibility(View.GONE);
        }else{
            profileLayoutView.setVisibility(View.GONE);
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
            case R.id.lin_live_class:
                showLiveFragment();
                break;
            case R.id.lin_video:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showVideoFragment();
                break;
            case R.id.lin_note:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showNoteFragment();
                break;
            case R.id.lin_test:
                //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showTestFragment();
                // Log.e("testclick","click");
                break;
            case R.id.lin_notice:
                showDppFragment();
                break;
        }
    }

    private void showDppFragment() {
        setDppBgColor();
        TeacherDPPFragment fragment = new TeacherDPPFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("section_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("course_title",title);
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

    public void setDppBgColor() {
        lin_live_class.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_liveclass.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_liveclass.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_note.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_note.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_note.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_dpp.requestFocus();
    }

    private void showLiveFragment() {
        setLiveBgColor();
//chat_btn.setVisibility(View.GONE);
        ScheduleLiveFragment fragment = new ScheduleLiveFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("section_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("course_title",title);
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


    public void showTestFragment() {
        setTestBgColor();
        // chat_btn.setVisibility(View.VISIBLE);

        MyTeacherManageTestFragment fragment = new MyTeacherManageTestFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",SIMPLEE_HOME_TUTOR);
        bundle.putString("type","chapter");
        bundle.putString("course_id",course_id);
        bundle.putString("subject_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
      bundle.putString("course_title",title);
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
    private void showNoteFragment() {
        setNoteBgColor();

        TeacherNotesFragment fragment = new TeacherNotesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("section_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("course_title",title);
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

    private void showVideoFragment() {
        showVideoBg();
        MyTeacherVideoFragment fragment = new MyTeacherVideoFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",SIMPLEE_HOME_TUTOR);//SIMPLEE_HOME_STUDENT
        bundle.putString("course_id",course_id);
        bundle.putString("section_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("course_title",title);
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

    public void setTestBgColor() {
        lin_live_class.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_liveclass.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_liveclass.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_note.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_note.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_note.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        ll_test.requestFocus();
    }

    public void setLiveBgColor() {

        lin_live_class.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_liveclass.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_liveclass.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_note.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_note.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_note.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }

    public void setNoteBgColor() {

        lin_live_class.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_liveclass.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_liveclass.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_note.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_note.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_note.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }


    public void showVideoBg() {
        lin_live_class.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_liveclass.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_liveclass.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_note.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_note.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_note.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }


}
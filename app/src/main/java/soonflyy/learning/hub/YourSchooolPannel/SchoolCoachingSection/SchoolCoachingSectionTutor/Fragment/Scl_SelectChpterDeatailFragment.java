package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherManageTestFragment;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherVideoFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;


public class Scl_SelectChpterDeatailFragment extends Fragment implements View.OnClickListener {
    TextView tv_liveclass,tv_test, tv_video, tv_note, tv_notice,tv_teacher_name;
    LinearLayout lin_live_class, lin_note, lin_notice,ll_test, lin_video;
    ImageView iv_liveclass, iv_video, iv_note,iv_test, iv_notice;

    String subject_id,chapter_id,teacher_id,from,class_id,section_id,teacher_name,school_id;
    String title;
    public Scl_SelectChpterDeatailFragment() {
        // Required empty public constructor
    }



    public static Scl_SelectChpterDeatailFragment newInstance(String param1, String param2) {
        Scl_SelectChpterDeatailFragment fragment = new Scl_SelectChpterDeatailFragment();


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
        View view= inflater.inflate(R.layout.fragment_scl__select_chpter_deatail, container, false);
        initView(view);
        getArgumentData();
        initControl();

        return view;
    }

    private void getArgumentData() {
        teacher_name=getArguments().getString("teacher_name");
        title=getArguments().getString("chapter_name");
        subject_id=getArguments().getString("subject_id");
        teacher_id=getArguments().getString("teacher_id");
        chapter_id=getArguments().getString("chapter_id");
        class_id =getArguments().getString("class_id");
        section_id=getArguments().getString("section_id");
        school_id=getArguments().getString("school_id");
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING)){
            tv_teacher_name.setText(teacher_name);
            tv_teacher_name.setVisibility(View.VISIBLE);
        }else{
            tv_teacher_name.setVisibility(View.GONE);
        }

    }

    private void initView(View view) {
        tv_teacher_name =view.findViewById(R.id.tv_teacher_name);
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

        lin_notice =view.findViewById(R.id.lin_notice);
        iv_notice =view.findViewById(R.id.iv_notice);
        tv_notice =view.findViewById(R.id.tv_notice);
        //chat_btn.setOnTouchListener(this);

        lin_live_class.setOnClickListener(this);
        lin_note.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        ll_test.setOnClickListener(this);
        lin_notice.setOnClickListener(this);

    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount()==0){
            showLiveFragment(setBundle());
        }

    }
    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.frame_layout_live, fragment);// ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Bundle bundle=setBundle();
        switch (v.getId()){
            case R.id.lin_live_class:
                showLiveFragment(bundle);
                break;
            case R.id.lin_video:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showVideoFragment(bundle);
                break;
            case R.id.lin_note:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showNoteFragment(bundle);
                break;
            case R.id.lin_test:
                //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showTestFragment(bundle);
                // Log.e("testclick","click");
                break;
            case R.id.lin_notice:
                showDppFragment(bundle);
                break;
        }
    }

    private Bundle setBundle() {
        Bundle bundle=new Bundle();
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("from",from);
        bundle.putString("subject_id",subject_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("chapter_name",title);
        bundle.putString("section_id",section_id);
        bundle.putString("class_id",class_id);
        bundle.putString("school_id",school_id);
        return bundle;
    }

    private void showDppFragment(Bundle  bundle) {
        setDppBgColor();
        Scl_SelectChpNoticeFragment fragment = new Scl_SelectChpNoticeFragment();
        fragment.setArguments(bundle);
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


        lin_notice.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_notice.requestFocus();
    }

    private void showLiveFragment(Bundle bundle) {
        setLiveBgColor();
//chat_btn.setVisibility(View.GONE);
        Scl_SelectChpLiveClassesFragment fragment = new Scl_SelectChpLiveClassesFragment();
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }


    public void showTestFragment(Bundle bundle) {
        setTestBgColor();
        // chat_btn.setVisibility(View.VISIBLE);

      //  Scl_SelectChpTestFragment fragment = new Scl_SelectChpTestFragment();
        MyTeacherManageTestFragment fragment=new MyTeacherManageTestFragment();

        fragment.setArguments(bundle);

        SwitchFragment(fragment);
    }
    private void showNoteFragment(Bundle bundle) {
        setNoteBgColor();
        Scl_SelectChpNotesFragment fragment = new Scl_SelectChpNotesFragment();
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }

    private void showVideoFragment(Bundle bundle) {
        showVideoBg();
      //  Scl_SelectChpVideosFragment fragment = new Scl_SelectChpVideosFragment();
        MyTeacherVideoFragment fragment=new MyTeacherVideoFragment();
        fragment.setArguments(bundle);
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

        lin_notice.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


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
        lin_notice.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

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

        lin_notice.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));


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

        lin_notice.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notice.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        ll_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle(title);
    }
}
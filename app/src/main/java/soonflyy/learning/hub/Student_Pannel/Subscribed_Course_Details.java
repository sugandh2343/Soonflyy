package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.utlis.AppConstant.FROM;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.base.BaseFragment;
import soonflyy.learning.hub.studentModel.BookMarkCourse;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Subscribed_Course_Details extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
 public static final String TAG = Subscribed_Course_Details.class.getName();

    float dX;
    float dY;
    int lastAction;

 TextView tv_about,tv_test,tv_subject,tv_notice;
 private ImageView chat_btn;
    String from;
    boolean isSelected=true;
    LinearLayout lin_about_course, lin_notices,ll_pdf,ll_test,lin_subject;
    ImageView iv_about,iv_subject,iv_notice,iv_test;

    private SessionManagement sessionManagement;
    private  SubscribedCourse sub_course;
    private BookMarkCourse bookMarkCourse;
    private String type,courseName,courseId,courseCreatorId;
    ///------------profile Assign to-----------//
    View assignToLayoutView;
    TextView tvType,tvProfileName,tvMobile;
    CircleImageView profileImg;
    //RelativeLayout relProfile;

    AssignProfile assignProfile;

    //--------------------------------//

    public Subscribed_Course_Details() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        from = args.getString(FROM);
        courseCreatorId=args.getString("course_creator_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_subscibed_course_details, container, false);
        sessionManagement=new SessionManagement(getContext());

        return view;
    }

    private void getArgumentsData() {
        from=getArguments().getString(FROM);
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
            sub_course = getArguments().getParcelable("sCourse");
            courseName=sub_course.getTitle();
            courseId=sub_course.getCourse_id();
            if (sub_course.getScreenshot_enable().equals("1")) {
                CommonMethods.enableScreenshot(getActivity().getWindow());

            } else {
                CommonMethods.disableScreenshot(getActivity().getWindow());

            }
        }else if (from.equals(SIMPLEE_HOME_TUTOR)){
            //get argument for tutor assignedto in tutorhome
            //courseName=getArguments().getString("title");
            courseName=getArguments().getString("course_name");
            courseId=getArguments().getString("course_id");
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
            showAssignToProfile();
            setProfileData(assignProfile);
        }


    }

    private void setProfileData(AssignProfile model) {
        if (model!=null){
            String imaglink= model.getImage();
            Log.e("image",""+imaglink);
            Picasso.get().load(imaglink).placeholder(R.drawable.logoo)
                    .into(profileImg);
            tvProfileName.setText(model.getName());
            tvMobile.setText(model.getMobile());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getArgumentsData();
        initControl();
       // checkStatus();
    }



    @Override
    public void init() {
        lin_about_course=getView().findViewById(R.id.lin_live_class);
        iv_about=getView().findViewById(R.id.iv_liveclass);
        tv_about=getView().findViewById(R.id.tv_liveclass);

        lin_subject=getView().findViewById(R.id.lin_video);
        iv_subject=getView().findViewById(R.id.iv_video);
        tv_subject=getView().findViewById(R.id.tv_video);

        lin_notices=getView().findViewById(R.id.lin_note);
        iv_notice=getView().findViewById(R.id.iv_note);
        tv_notice=getView().findViewById(R.id.tv_note);

        ll_test=getView().findViewById(R.id.lin_test);
        iv_test=getView().findViewById(R.id.iv_test);
        tv_test=getView().findViewById(R.id.tv_test);

        chat_btn= getView().findViewById(R.id.floatingActionButton);

        //-------------------------------------//
       // relProfile=getView().findViewById(R.id.);
        tvProfileName=getView().findViewById(R.id.assign_tv_name);
        tvMobile=getView().findViewById(R.id.assign_tv_mobile);
        tvType=getView().findViewById(R.id.tv_type);
        profileImg=getView().findViewById(R.id.assign_iv_profile_img);
        assignToLayoutView=getView().findViewById(R.id.include_assign_to);


        //-------------------------------------//

    }

    @Override
    public void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount()==0){
            showAboutFragment();
        }
        lin_about_course.setOnClickListener(this);
        lin_notices.setOnClickListener(this);
        lin_subject.setOnClickListener(this);
        ll_test.setOnClickListener(this);
        chat_btn.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (v.getId()){
            case R.id.lin_live_class:
                showAboutFragment();
                break;
            case R.id.lin_video:
              //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showSubjectFragment();
                break;
            case R.id.lin_note:
              //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showNoticeFragment();
                break;
            case R.id.lin_test:
               // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
               showTestFragment();
               // Log.e("testclick","click");
                break;
        }
    }

    private void showSubjectFragment() {
        showSubjectBg();
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",from);
        bundle.putString("course_id",courseId);//sub_course.getCourse_id()
        bundle.putString("course_name",courseName);//sub_course.getTitle()
        bundle.putSerializable("profileData",assignProfile);
//        bundle.putString("type",type);
//        if (type.equals("subscription")) {
//            bundle.putParcelable("sCourse", sub_course);
//        }
//        if (type.equals("bookmark")){
//            bundle.putParcelable("sCourse", bookMarkCourse);
//        }
        fragment.setArguments(bundle);
        SwitchFragment(fragment);

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

    public  void showAssignToProfile(){
        if (from.equals(SIMPLEE_HOME_TUTOR)){
            tvType.setText("Assigned to");
            assignToLayoutView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white_smoke));
            assignToLayoutView.setVisibility(View.VISIBLE);
        }else {
            assignToLayoutView.setVisibility(View.GONE);
        }
    }
    public  void  hideProfile(){
        assignToLayoutView.setVisibility(View.GONE);
    }



    private void showTestFragment() {
        setTestBgColor();
       // CourseTestFragment fragment=new CourseTestFragment();
        Test_Fragment fragment=new Test_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",from);//SIMPLEE_HOME_STUDENT
        bundle.putString("type","course");
        bundle.putString("subject_id"," ");
        bundle.putString("chapter_id"," ");
        bundle.putString("course_id",courseId);//sub_course.getCourse_id()
       /* if (type.equals("subscription")) {
            bundle.putString("course_id",sub_course.getCourse_id());
        }
        if (type.equals("bookmark")){
            bundle.putString("course_id",bookMarkCourse.getCourse_id());
        }

        */
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }

    public void setTestBgColor() {
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



    private void showNoticeFragment() {
        setNoticeBgColor();
       // LiveSessionFragment liveSessionFragment = new LiveSessionFragment();
        Student_Notice_Fragment fragment=new Student_Notice_Fragment();
       // FragmentManager fragmentManager = getChildFragmentManager();
        Bundle bundle=new Bundle();
        bundle.putString("type","AssignTo");
        bundle.putString("from",from);//SIMPLEE_HOME_STUDENT
        bundle.putString("course_id",courseId);//sub_course.getCourse_id()
        bundle.putString("notice","course");

        fragment.setArguments(bundle);
        SwitchFragment(fragment);

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

    private void showAboutFragment() {
        setAboutBgColor();
        AboutCourseFragment videoFragment = new AboutCourseFragment();
        Bundle arg=new Bundle();
       // arg.putString("video_type","subscription");
        arg.putString("from",from);
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
            arg.putParcelable("sCourse", sub_course);
        }else{
            //for tutor course
            Log.e("CJNIJDIBD",courseId);
            Log.e("CJNIJDIBD",assignProfile.getId());
            Log.e("CJNIJDIBD",courseName);
            arg.putString("course_id",courseId);
            arg.putString("tutor_id",assignProfile.getId());
            arg.putString("course_name",courseName);
            arg.putString("course_creator_id",courseCreatorId);

           // arg.putSerializable("profileData",assignProfile);
        }

        videoFragment.setArguments(arg);
        SwitchFragment (videoFragment);
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


    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
       fragmentTransaction.replace(R.id.frame_layout_container, fragment);//, ProfileFragment.TAG
       fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
      //  tv_title.setText("Railway completed");
        if (from.equals(SIMPLEE_HOME_STUDENT))
        ((MainActivity)getActivity()).setStudentChildActionBar(sub_course.getTitle(),false);
        else if (from.equals(SIMPLEE_HOME_TUTOR))
            ((TeacherMainActivity)getActivity()).setTeacherActionBar(courseName,false);//courseName
     //   getAndSetBackStackData();

    }

    //private void getAndSetBackStackData() {}

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    // Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
//                    MessageFragment fragment = new MessageFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("type","student");
//                    bundle.putString("student_id",new Session_management(getContext()).getString(USER_ID));
//                    if (type.equals("subscription")){
//                        bundle.putString("teacher_id",sub_course.getTeacher_id());
//                        bundle.putString("name",sub_course.getTeacher_name());
//                        bundle.putString("profile_image",sub_course.getTeacher_image());
//                    }if (type.equals("bookmark")){
//                        bundle.putString("teacher_id",bookMarkCourse.getTeacher_id());
//                        bundle.putString("name",bookMarkCourse.getTeacher_name());
//                        bundle.putString("profile_image"," ");//bookMarkCourse.getTeacher_image()
//                    }
//
//                    fragment.setArguments(bundle);
//                    ((MainActivity)getActivity()).SwitchFragment(fragment);
                }
                break;

            default:
                return false;
        }
        return true;
    }

}
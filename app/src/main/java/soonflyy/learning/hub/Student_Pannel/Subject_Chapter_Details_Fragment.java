package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

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

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Subject_Chapter_Details_Fragment extends Fragment implements View.OnClickListener {

     TextView tv_live,tv_video,tv_notes,tv_dpp,tv_test,tv_teacher_name;
     ImageView iv_live,iv_video,iv_notes,iv_dpp,iv_test,tvMobile;
     LinearLayout lin_live,lin_video,lin_notes,lin_dpp,lin_test;
     View lin_tutorName;

     String chapter_id,subject_id,course_id;
    String teacher_id,from,class_id,section_id,teacher_name,subName,courseName,chapterName;

    ///------------profile Assign to-----------//
    View assignToLayoutView;
    TextView tvType,tvProfileName;
    CircleImageView profileImg;
    AssignProfile assignProfile;
    //RelativeLayout relProfile;

    //--------------------------------//
    public Subject_Chapter_Details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subject__chapter__details_, container, false);
        bindViewId(view);
        getArgumentData();
        listener();
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            chapterName=getArguments().getString("chapter_name");

            teacher_name=getArguments().getString("teacher_name");
            class_id =getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");
            teacher_id=getArguments().getString("teacher_id");
            tv_teacher_name.setText("Teacher - "+teacher_name);
            tv_dpp.setText("Notice");
           // tv_teacher_name.setVisibility(View.VISIBLE);
            lin_tutorName.setVisibility(View.VISIBLE);
        }else if (from.equals(SIMPLEE_HOME_STUDENT)|| from.equals(SIMPLEE_HOME_TUTOR)) {
            tv_dpp.setText("DPP");
            course_id = getArguments().getString("course_id");
            courseName=getArguments().getString("course_name");
            subName=getArguments().getString("subName");
            chapterName=getArguments().getString("chapterName");
            if (from.equals(SIMPLEE_HOME_TUTOR)){
                assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
                showAssignToProfile();
                setProfileData(assignProfile);
            }
        }
    }

    private void setProfileData(AssignProfile model) {
        if (model!=null){
            String imaglink= model.getImage();
            Log.e("image",""+imaglink);
            Picasso.get().load(imaglink).placeholder(R.drawable.logoo)
                    .into(profileImg);
            tvProfileName.setText(model.getName());

        }
    }

    private void listener() {
         if (getChildFragmentManager().getBackStackEntryCount()==0){
             showLiveClasess();
         }
         lin_test.setOnClickListener(this);
         lin_video.setOnClickListener(this);
         lin_live.setOnClickListener(this);
         lin_dpp.setOnClickListener(this);
         lin_notes.setOnClickListener(this);
     }

     private void bindViewId(View view) {
        lin_tutorName=view.findViewById(R.id.lin_t_name);
        tv_teacher_name=view.findViewById(R.id.tv_teacher_name);
         tv_live=view.findViewById(R.id.tv_live_text);
         tv_video=view.findViewById(R.id.tv_video_text);
         tv_notes=view.findViewById(R.id.tv_notes_text);
         tv_dpp=view.findViewById(R.id.tv_dpp_icon);
         tv_test=view.findViewById(R.id.tv_test_text);

         iv_live=view.findViewById(R.id.video_img);
         iv_video=view.findViewById(R.id.iv_video_icon);
         iv_notes=view.findViewById(R.id.iv_notes_icon);
         iv_dpp=view.findViewById(R.id.iv_dpp_icon);
         iv_test=view.findViewById(R.id.iv_test_icon);

         lin_live=view.findViewById(R.id.lin_live_classes);
         lin_video=view.findViewById(R.id.lin_video);
         lin_notes=view.findViewById(R.id.lin_note);
         lin_dpp=view.findViewById(R.id.lin_notice);
         lin_test=view.findViewById(R.id.lin_test);


         //-------------------------------------//
         // relProfile=getView().findViewById(R.id.);
         tvProfileName=view.findViewById(R.id.assign_tv_name);
         tvMobile=view.findViewById(R.id.assign_tv_mobile);
         tvType=view.findViewById(R.id.tv_type);
         profileImg=view.findViewById(R.id.assign_iv_profile_img);
         assignToLayoutView=view.findViewById(R.id.include_assign_to);


         //-------------------------------------//

     }

    public  void showAssignToProfile(){
        if (from.equals(SIMPLEE_HOME_TUTOR)){
            tvType.setText("Assign to");
            assignToLayoutView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white_smoke));
            assignToLayoutView.setVisibility(View.VISIBLE);
        }else {
            assignToLayoutView.setVisibility(View.GONE);
        }
    }
    public  void  hideProfile(){
        assignToLayoutView.setVisibility(View.GONE);
    }

     @Override
     public void onClick(View v) {
         getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
         switch (v.getId()){
             case R.id.lin_live_classes:
                 showLiveClasess();
                 break;
             case R.id.lin_video:
                 //getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                 showVideo();
                 break;
             case R.id.lin_note:
                 showNotes();
                 break;
             case R.id.lin_notice:
                 showDpp();
                 break;
             case R.id.lin_test:
                 showTest();
                 break;

         }
     }

    private void showLiveClasess() {
        setLiveBackground();
        Live_Classes_Fragment fragment=new Live_Classes_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("subject_id",subject_id);


        bundle.putString("from",from);
        bundle.putString("section_id",section_id);
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("class_id",class_id);
        bundle.putString("courseName",courseName);
        bundle.putString("subName",subName);
        fragment.setArguments(bundle);
        switchFragment(fragment);
    }
    private void showVideo() {
        setVideoBackground();
        Video_Fragment fragment=new Video_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("subject_id",subject_id);

        bundle.putString("from",from);
        bundle.putString("section_id",section_id);
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("class_id",class_id);
        bundle.putString("courseName",courseName);
        bundle.putString("subName",subName);
        fragment.setArguments(bundle);
        switchFragment(fragment);
    }
    private void showNotes() {
        setNotesBackground();

        Notes_Fragment fragment=new Notes_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("courseName",courseName);
        bundle.putString("subName",subName);
        bundle.putString("course_id",course_id);
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("subject_id",subject_id);

        bundle.putString("from",from);
        bundle.putString("section_id",section_id);
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("class_id",class_id);
        fragment.setArguments(bundle);
        switchFragment (fragment);
    }
    private void showDpp() {setTestBackground();
        setDppBackground();
        Fragment fragment=null;
        if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
             fragment = new DppList_Fragment();
        }else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            fragment=new Student_Notice_Fragment();
        }
        Bundle bundle=new Bundle();
       // bundle.putString("type","note");
        bundle.putString("chapter_id",chapter_id);
        bundle.putString("subject_id",subject_id);
        bundle.putString("course_id",course_id);
        bundle.putString("courseName",courseName);
        bundle.putString("subName",subName);

        bundle.putString("from",from);
        bundle.putString("section_id",section_id);
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("class_id",class_id);
        fragment.setArguments(bundle);
        switchFragment(fragment);

    }
    private void showTest() {
        setTestBackground();
        Test_Fragment fragment=new Test_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("type","chapter");
        bundle.putString("subject_id",subject_id);
        bundle.putString("course_id",course_id);
        bundle.putString("chapter_id",chapter_id);

        bundle.putString("from",from);
        bundle.putString("section_id",section_id);
        bundle.putString("teacher_id",teacher_id);
        bundle.putString("teacher_name",teacher_name);
        bundle.putString("class_id",class_id);
        fragment.setArguments(bundle);
        switchFragment(fragment);
    }
    public void setLiveBackground() {
        lin_live.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_live.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_live.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notes.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notes.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notes.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }
    public void setVideoBackground() {
        lin_live.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_live.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_live.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_notes.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notes.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notes.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }
    public void setNotesBackground() {
        lin_live.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_live.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_live.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notes.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_notes.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_notes.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }
    public void setDppBackground() {
        lin_live.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_live.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_live.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notes.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notes.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notes.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }
    public void setTestBackground() {
        lin_live.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_live.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_live.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_video.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_video.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_video.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_notes.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_notes.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_notes.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_dpp.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_dpp.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_dpp.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_test.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_test.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_test.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

    }



    @Override
     public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
          ((MainActivity)getActivity()).setStudentChildActionBar(courseName+" ("+chapterName+")",false);
        } else if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity)getActivity()).setTeacherActionBar(courseName+" ("+chapterName+")",false);
        }else if (from.equals(SCHOOL_STUDENT)||from.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).setActionBarTitle(chapterName);
        }
    }

    public void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.sub_layout_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
 }
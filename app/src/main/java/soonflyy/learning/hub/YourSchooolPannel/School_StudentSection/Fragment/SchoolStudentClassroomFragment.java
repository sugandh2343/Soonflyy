package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.MyPerformance_Fragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolAllSubjectsFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolApplyForLeaveFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.School_DiscustionChatDetailFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.Scl_SelectChpNoticeFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.StudentClassroomAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.StudentClassroomModel;

import java.util.ArrayList;


public class SchoolStudentClassroomFragment extends Fragment {

    SwipeRefreshLayout swipe;
    RecyclerView rec_classroom;
    StudentClassroomAdapter classAdapter;
    ArrayList<StudentClassroomModel> classRoomlist;
    ImageView iv_subject;
    String id,student_type,from,section_id,class_id,page_title;
    public SchoolStudentClassroomFragment() {
        // Required empty public constructor
    }


    public static SchoolStudentClassroomFragment newInstance(String param1, String param2) {
        SchoolStudentClassroomFragment fragment = new SchoolStudentClassroomFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_student_classroom, container, false);
        initView(view);
        getArgumentData();
        initRecyclerview();
        init_swipe_method();
       // SchoolStudentHomeFragment fragm = new SchoolStudentHomeFragment();

         //   fragm.makeRelGon ("gone");
        //}

        return  view;
    }

    private void getArgumentData() {
        id=getArguments().getString("id");
        from=getArguments().getString("from");
        student_type=getArguments().getString("student_type");
        page_title=getArguments().getString("page_title");
        if (student_type.equals("school")){
            class_id=getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");

        }
    }


    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        iv_subject = view.findViewById(R.id.iv_subject);
        rec_classroom = view.findViewById(R.id.rec_classroom);
        rec_classroom.hasFixedSize();
        rec_classroom.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_classroom.setLayoutManager(layoutManager);
        rec_classroom.setKeepScreenOn(true);
        iv_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Scl_StudentAllSubjectFragment fragment = new Scl_StudentAllSubjectFragment ();
                SchoolAllSubjectsFragment fragment=new SchoolAllSubjectsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("from",from);
                bundle.putString("student_type",student_type);
                if (student_type.equals("school")) {
                    bundle.putString("class_id", class_id);
                    bundle.putString("section_id", section_id);
                    bundle.putString("school_id", id);
                    bundle.putString("school_name",page_title);
                }else if (student_type.equals("itutor")){
                    bundle.putString("itutor_id",id);
                }
                fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
               // SwitchFragment (fragment);
            }
        });

    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }





    private void initRecyclerview() {

        classRoomlist = new ArrayList<>();
        classRoomlist.add(new StudentClassroomModel("Notice",R.drawable.ic_notification));
        classRoomlist.add(new StudentClassroomModel("Time Table",R.drawable.ic_baseline_calendar_month_24));
        classRoomlist.add(new StudentClassroomModel("Attendance",R.drawable.attendance));
        classRoomlist.add(new StudentClassroomModel("Apply for Leave", R.drawable.apply_leave));
        classRoomlist.add(new StudentClassroomModel("Discussion",R.drawable.ic_baseline_chat_24));
        classRoomlist.add(new StudentClassroomModel("Fee",R.drawable.fee));
        classRoomlist.add(new StudentClassroomModel("Performance",R.drawable.s_perfomance_icon));
        classAdapter= new StudentClassroomAdapter(getActivity(), classRoomlist, new StudentClassroomAdapter.OnClassClickListener() {
            @Override
            public void onItemClick(int postion) {
                String classroom_name = classRoomlist.get(postion).getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("from", SCHOOL_STUDENT);
                bundle.putString("id", id);//school_id
                bundle.putString("student_type", student_type);
                switch (classroom_name) {
                    case "Notice":
                        Scl_SelectChpNoticeFragment fragment = new Scl_SelectChpNoticeFragment();
                       fragment.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);

                        //  SwitchFragment(fragment);
                        break;
                    case "Time Table":
                     //   SchoolTutotTimeTableFragment fragment_table = new SchoolTutotTimeTableFragment();
                       // SwitchFragment(fragment_table);
                        StudentTimeTableFragment fragment_table=new StudentTimeTableFragment();
                        fragment_table.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_table);
                        break;
                    case "Attendance":
                        MyPerformance_Fragment fragment1 = new MyPerformance_Fragment();
                        fragment1.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment1);
//
//                        SchoolTutotAttendenceFragment fragment_attendence = new SchoolTutotAttendenceFragment();
//
//                     //   SwitchFragment(fragment_attendence);
//                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_attendence);
                        break;

                    case "Apply for Leave":
                        SchoolApplyForLeaveFragment fragment_leave = new SchoolApplyForLeaveFragment();
                        fragment_leave.setArguments(bundle);
                       // SwitchFragment(fragment_leave);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_leave);
                        break;
                    case "Discussion":
                     //   SchoolTutorDiscussionFragment fragment_discuss = new SchoolTutorDiscussionFragment();

                       // SwitchFragment(fragment_discuss);
                        School_DiscustionChatDetailFragment fragment_discuss = new School_DiscustionChatDetailFragment();
                        fragment_discuss.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_discuss);
                        break;
                    case "Fee":
                       // SchoolTutorFeeFragment fragment_Fee = new SchoolTutorFeeFragment();

                       // SwitchFragment(fragment_Fee);
                        StudentFeesFragment fragment_Fee=new StudentFeesFragment();
                        fragment_Fee.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_Fee);
                        break;
                    case "Performance":
                        Scl_StudentPerformanceFragment fragment_per = new Scl_StudentPerformanceFragment();
                        fragment_per.setArguments(bundle);
                       // SwitchFragment(fragment_per);

                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_per);
                        break;

                }
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_classroom.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();


    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle(page_title);
    }
}
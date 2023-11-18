package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.StudentAttendencdeFragment;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.ScholProfileFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.Sc_Notice_Fragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.SchoolCoachingFeeFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.SchoolTutorLeaveApprovedFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.ClassRoomAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolAllSubjectsFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.School_DiscustionChatDetailFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.ClassRoomModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import java.util.ArrayList;


public class SchoolIndependentTutorHomeFragment extends Fragment implements View.OnClickListener {

    SwipeRefreshLayout swipe;
    RecyclerView rec_class;
    ImageView arrow_back_img;
    TextView tv_title;
    String type= "";
    ImageView iv_setting;
    LinearLayout lin_mobile;
    ClassRoomAdapter classAdapter;
    ArrayList<ClassRoomModel> classRoomlist;
    ImageView iv_class;

    public SchoolIndependentTutorHomeFragment() {
        // Required empty public constructor
    }


    public static SchoolIndependentTutorHomeFragment newInstance(String param1, String param2) {
        SchoolIndependentTutorHomeFragment fragment = new SchoolIndependentTutorHomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_school_tutor_home, container, false);
        initView(view);
        tv_title.setText(("Independent Tutor Section"));
        initRecyclerview();
        init_swipe_method();
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("type","1");
                bundle.putString("from",INDEPENDENT_TUTOR);
                bundle.putString("id",new SessionManagement(getActivity()).getString(SCHOOL_IT_ID));
                ScholProfileFragment fragment=new ScholProfileFragment();
                fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }
        });
        return  view;
    }

    private void initView(View view) {
        tv_title=view.findViewById(R.id.tv_t_hometitle);
        iv_setting=view.findViewById(R.id.iv_setting);
        swipe = view.findViewById(R.id.swipe);
        lin_mobile= view.findViewById(R.id.lin_mobile);
        lin_mobile.setVisibility(View.GONE);

        iv_class = view.findViewById(R.id.iv_class);
        rec_class= view.findViewById(R.id.rec_class);
        rec_class.hasFixedSize();
        rec_class.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_class.setLayoutManager(layoutManager);
        rec_class.setKeepScreenOn(true);
        iv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Scl_Ind_TutorSubjectFragment fragment = new Scl_Ind_TutorSubjectFragment ();

                //SwitchFragment (fragment);
                SchoolAllSubjectsFragment fragment=new SchoolAllSubjectsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("from",INDEPENDENT_TUTOR);
                bundle.putString("itutor_id",new SessionManagement(getActivity()).getString(SCHOOL_IT_ID));
                fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
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
        classRoomlist.add(new ClassRoomModel("Notice",R.drawable.ic_notification));
        classRoomlist.add(new ClassRoomModel("Manage\nStudents",R.drawable.ic_person));
        classRoomlist.add(new ClassRoomModel("Fee",R.drawable.fee));
        classRoomlist.add(new ClassRoomModel("Leave\nApproval",R.drawable.apply_leave));

        classRoomlist.add(new ClassRoomModel("Time Table",R.drawable.ic_baseline_calendar_month_24));
        classRoomlist.add(new ClassRoomModel("Attendance",R.drawable.attendance));
        classRoomlist.add(new ClassRoomModel("Discussion",R.drawable.ic_baseline_chat_24));
        classAdapter= new ClassRoomAdapter(getActivity(), classRoomlist, new ClassRoomAdapter.OnClassClickListener() {
            @Override
            public void onItemClick(int postion) {
                String classroom_name=classRoomlist.get(postion).getTitle();
                Bundle bundle=new Bundle();
                bundle.putString("from",INDEPENDENT_TUTOR);
                bundle.putString("itutor_id",new SessionManagement(getActivity()).getString(SCHOOL_IT_ID));
                switch (classroom_name){
                    case "Notice":
//                        Scl_SelectChpNoticeFragment fragment = new Scl_SelectChpNoticeFragment ();
//                        fragment.setArguments(bundle);
                        Sc_Notice_Fragment notice_fragment=new Sc_Notice_Fragment();
                        notice_fragment.setArguments(bundle);
//                        SwitchFragment (fragment);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(notice_fragment);
                        break;
                    case "Manage\nStudents":
//                        Scl_Indp_TutorManageStudentFragment fragment_manage = new Scl_Indp_TutorManageStudentFragment ();
//                        SwitchFragment (fragment_manage);
                        Scl_Indp_TutorManageStudentFragment fragment_mange = new Scl_Indp_TutorManageStudentFragment ();
                        fragment_mange.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_mange);
                        break;
                    case "Fee":

                       // SchoolTutorFeeFragment fragment_Fee = new SchoolTutorFeeFragment ();

                       //// SwitchFragment (fragment_Fee);
                        SchoolCoachingFeeFragment fragment_Fee=new SchoolCoachingFeeFragment();
                        fragment_Fee.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_Fee);
                        break;

                    case "Time Table":
                      Scl_Ind_TutorTimeTableFragment fragment_table = new Scl_Ind_TutorTimeTableFragment ();
//                        //SwitchFragment (fragment_table);
                        fragment_table.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_table);

                        break;
                    case "Attendance":
                        //SchoolTutotAttendenceFragment fragment_attendence = new SchoolTutotAttendenceFragment ();
                        StudentAttendencdeFragment fragment_attendence=new StudentAttendencdeFragment();
                        fragment_attendence.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_attendence);

//                        SwitchFragment (fragment_attendence);
                        break;

                    case "Leave\nApproval":
//                        Scl_Ind_TutorLeaveApprovalFragment fragment_leave = new Scl_Ind_TutorLeaveApprovalFragment ();
//
//                        SwitchFragment (fragment_leave);
                        SchoolTutorLeaveApprovedFragment fragment_leave_apv = new SchoolTutorLeaveApprovedFragment();
                       fragment_leave_apv.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_leave_apv);
                        break;
                    case "Discussion":
                        School_DiscustionChatDetailFragment fragment_discuss = new School_DiscustionChatDetailFragment  ();
                       // SwitchFragment (fragment_discuss);
                        fragment_discuss.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_discuss);

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
        rec_class.setAdapter(classAdapter);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back_img:
                gotoBack();
                break;
        }

    }

    private void gotoBack() {
        try {
            if (type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).onBackPressed();

            } else if (type.equals("student")) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
    }
}
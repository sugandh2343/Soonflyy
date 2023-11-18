package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.ClassRoomAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.AllClassesFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolTutorDiscussionFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolTutotTimeTableFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.ClassRoomModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment.Scl_Indp_TutorManageStudentFragment;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import java.util.ArrayList;


public class SchoolCoachingHomeFragment extends Fragment {
    SwipeRefreshLayout swipe;
    RecyclerView rec_class;
    LinearLayout lin_mobile;
    String type = "";
    RelativeLayout rel_no_live;
    ImageView iv_setting, iv_icon;
    TextView tv_school_name;
    ClassRoomAdapter classAdapter;
    ArrayList<ClassRoomModel> classRoomlist;
    ImageView iv_class, iv_tutor;

    public SchoolCoachingHomeFragment() {
        // Required empty public constructor
    }


    public static SchoolCoachingHomeFragment newInstance(String param1, String param2) {
        SchoolCoachingHomeFragment fragment = new SchoolCoachingHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_coaching_home2, container, false);
        initView(view);
        initRecyclerview();
        init_swipe_method();
        // ((SchoolLoginMainActivity)getActivity ()).makeBottom_gone ("gone");
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "2");
                bundle.putString("from", SCHOOL_COACHING);
                bundle.putString("id", new SessionManagement(getActivity()).getString(SCHOOL_ID));
                ScholProfileFragment fragment = new ScholProfileFragment();
                fragment.setArguments(bundle);
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }
        });
        return view;
    }

    private void initView(View view) {
        iv_icon = view.findViewById(R.id.iv_icon);
        tv_school_name = view.findViewById(R.id.tv_t_hometitle);
        iv_setting = view.findViewById(R.id.iv_setting);
        swipe = view.findViewById(R.id.swipe);
        lin_mobile = view.findViewById(R.id.lin_mobile);
        lin_mobile.setVisibility(View.GONE);
        iv_class = view.findViewById(R.id.iv_class);
        iv_tutor = view.findViewById(R.id.iv_tutor);
        rel_no_live = view.findViewById(R.id.rel_no_live);
        rec_class = view.findViewById(R.id.rec_class);
        rec_class.hasFixedSize();
        rec_class.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_class.setLayoutManager(layoutManager);
        rec_class.setKeepScreenOn(true);

        iv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllClassesFragment fragment = new AllClassesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "school");
                bundle.putString("school_id", new SessionManagement(getActivity()).getString(SCHOOL_ID));
                fragment.setArguments(bundle);
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                //SwitchFragment (fragment);
            }
        });
        iv_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SchoolCoachingAsignTutorFragment fragment_asign = new SchoolCoachingAsignTutorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "school");
                bundle.putString("school_id", new SessionManagement(getActivity()).getString(SCHOOL_ID));
                fragment_asign.setArguments(bundle);
                //  SwitchFragment(fragment_asign);
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_asign);

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

        classRoomlist.add(new ClassRoomModel("New\nProfiles", R.drawable.ic_baseline_people_24));
        classRoomlist.add(new ClassRoomModel("Manage\nStudents", R.drawable.ic_person));
        classRoomlist.add(new ClassRoomModel("Fee", R.drawable.fee));
        classRoomlist.add(new ClassRoomModel("Leave\nApproval", R.drawable.apply_leave));
        classRoomlist.add(new ClassRoomModel("Time Table", R.drawable.ic_baseline_calendar_month_24));
        classRoomlist.add(new ClassRoomModel("Attendance", R.drawable.attendance));

        // classRoomlist.add(new ClassRoomModel("Discussion",R.drawable.ic_baseline_chat_24));

        classRoomlist.add(new ClassRoomModel("Notice", R.drawable.ic_notification));
        classAdapter = new ClassRoomAdapter(getActivity(), classRoomlist, new ClassRoomAdapter.OnClassClickListener() {
            @Override
            public void onItemClick(int postion) {
                String classroom_name = classRoomlist.get(postion).getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("from", "school");
                bundle.putString("school_id", new SessionManagement(getActivity()).getString(SCHOOL_ID));
                switch (classroom_name) {
                    //classroom adapter m school k new tye se open hoga///bna diya h aap dekh lena///
                    case "New\nProfiles":
                        SchoolCoachingTutorNewProfileFragment fragment = new SchoolCoachingTutorNewProfileFragment();

                        // SwitchFragment (fragment);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        fragment.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);

                        break;
                    case "Manage\nStudents":
                        Scl_Indp_TutorManageStudentFragment fragment_mange = new Scl_Indp_TutorManageStudentFragment();

                        // SwitchFragment (fragment);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        fragment_mange.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_mange);

                        break;
                    case "Notice":
                        ////two notice fragment open honge   "SchoolCoachingStudentTutorHomeFragment" school coaching ka h and iske andar jo oepn hoga vo main h /////
                        // SchoolCoachingStudentTutorHomeFragment fragment_profile = new SchoolCoachingStudentTutorHomeFragment ();

                        // SwitchFragment (fragment);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        Sc_Notice_Fragment notice_fragment = new Sc_Notice_Fragment();
                        notice_fragment.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(notice_fragment);

                        break;
                    case "Time Table":
                        SchoolTutotTimeTableFragment fragment_table = new SchoolTutotTimeTableFragment();
                        // SwitchFragment (fragment_table);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        fragment_table.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_table);

                        break;
                    case "Attendance":
                        //  SchoolTutotAttendenceFragment fragment_attendence = new SchoolTutotAttendenceFragment ();
                        // SwitchFragment (fragment_attendence);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        //((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_attendence);
                        StudentAttendencdeFragment fragment_attendence = new StudentAttendencdeFragment();
                        fragment_attendence.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_attendence);

                        break;

//                    case "Leave Approval":
//                        SchoolApplyForLeaveFragment fragment_leave = new SchoolApplyForLeaveFragment ();
//                        //SwitchFragment (fragment_leave);
//                        //comment by swati---- comment switchfragment method because its crash on this fragment
//                        fragment_leave.setArguments(bundle);
//                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_leave);
//
//                        break;
                    case "Leave\nApproval":
                        //// school Tutor k liye "  SchoolLeaveApprovedFragment" and studednt k liye "Scl_Ind_TutorLeaveApprovalFragment" ye fragment use hua h ////
                        //  Scl_Ind_TutorLeaveApprovalFragment fragment_leave_apv = new Scl_Ind_TutorLeaveApprovalFragment ();
                        SchoolTutorLeaveApprovedFragment fragment_leave_apv = new SchoolTutorLeaveApprovedFragment();
                        //SwitchFragment (fragment_leave);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        fragment_leave_apv.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_leave_apv);

                        break;
                    case "Discussion":
                        SchoolTutorDiscussionFragment fragment_discuss = new SchoolTutorDiscussionFragment();
                        //  SwitchFragment (fragment_discuss);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_discuss);

                        break;
                    case "Fee":
                        SchoolCoachingFeeFragment fragment_Fee = new SchoolCoachingFeeFragment();
                        //  SchoolTutorFeeFragment fragment_Fee = new SchoolTutorFeeFragment ();
                        //SwitchFragment (fragment_Fee);
                        //comment by swati---- comment switchfragment method because its crash on this fragment
                        fragment_Fee.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_Fee);

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

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).showHideHomeActionBar(false);
        tv_school_name.setText(new SessionManagement(getActivity()).getString(SCHOOL_NAME));
        iv_icon.setVisibility(View.VISIBLE);


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
}
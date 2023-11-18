package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.MyPerformance_Fragment;
import soonflyy.learning.hub.Teacher_Pannel.StudentAttendencdeFragment;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.SchoolCoachingFeeFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.ClassRoomAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.ClassRoomModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;

import java.util.ArrayList;


public class SchoolTutorClassRoomFragment extends Fragment implements View.OnClickListener {
    SwipeRefreshLayout swipe;
    RecyclerView rec_class;
    ImageView arrow_back_img;
    TextView tv_title;
    String type = "";
    ClassRoomAdapter classAdapter;
    ArrayList<ClassRoomModel> classRoomlist;
    ImageView iv_class;
    String school_id, school_name;

    public SchoolTutorClassRoomFragment() {
        // Required empty public constructor
    }


    public static SchoolTutorClassRoomFragment newInstance(String param1, String param2) {
        SchoolTutorClassRoomFragment fragment = new SchoolTutorClassRoomFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_details, container, false);
        initView(view);
        getArgumentData();
        initRecyclerview();
        init_swipe_method();
        return view;
    }

    private void getArgumentData() {
        school_id = getArguments().getString("school_id");
        school_name = getArguments().getString("school_name");
    }

    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("Class");
        arrow_back_img = view.findViewById(R.id.arrow_back_img);
        arrow_back_img.setOnClickListener(this);
        iv_class = view.findViewById(R.id.iv_class);
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
                bundle.putString("from", "s_tutor");
                bundle.putString("school_name", school_name);
                bundle.putString("school_id", school_id);
                fragment.setArguments(bundle);
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
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

        classRoomlist.add(new ClassRoomModel("Notice", R.drawable.ic_notification));
        classRoomlist.add(new ClassRoomModel("Time Table", R.drawable.ic_baseline_calendar_month_24));
        classRoomlist.add(new ClassRoomModel("Attendance", R.drawable.attendance));
        classRoomlist.add(new ClassRoomModel("Apply for Leave", R.drawable.apply_leave));
        classRoomlist.add(new ClassRoomModel("Discussion", R.drawable.ic_baseline_chat_24));
        classRoomlist.add(new ClassRoomModel("Fee", R.drawable.fee));
        classRoomlist.add(new ClassRoomModel("Manage Attendance", R.drawable.attendance));
        classAdapter = new ClassRoomAdapter(getActivity(), classRoomlist, new ClassRoomAdapter.OnClassClickListener() {
            @Override
            public void onItemClick(int postion) {
                String classroom_name = classRoomlist.get(postion).getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("from", SCHOOL_TUTOR);
                bundle.putString("school_id", school_id);
                bundle.putString("school_name", school_name);
                switch (classroom_name) {
                    case "Notice":
                        Scl_SelectChpNoticeFragment fragment = new Scl_SelectChpNoticeFragment();
                        bundle.putString("notice_type","school_notice");
                        bundle.putString("chapter_name","Notice");
                        fragment.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                        //SwitchFragment (fragment);
                        break;
                    case "Time Table":
                        SchoolTutotTimeTableFragment fragment_table = new SchoolTutotTimeTableFragment();

                        fragment_table.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_table);
                        // SwitchFragment (fragment_table);
                        break;
                    case "Attendance":
                        // SchoolTutotAttendenceFragment fragment_attendence = new SchoolTutotAttendenceFragment ();
                        MyPerformance_Fragment fragment1 = new MyPerformance_Fragment();
                        fragment1.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment1);
                        //SwitchFragment (fragment_attendence);
                        break;

                    case "Apply for Leave":
                        SchoolApplyForLeaveFragment fragment_leave = new SchoolApplyForLeaveFragment();
                        fragment_leave.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_leave);
                        // SwitchFragment (fragment_leave);
                        break;
                    case "Discussion":
                        SchoolTutorDiscussionFragment fragment_discuss = new SchoolTutorDiscussionFragment();
                        fragment_discuss.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_discuss);
                        // SwitchFragment (fragment_discuss);
                        break;
                    case "Fee":
                        // SchoolTutorFeeFragment fragment_Fee = new SchoolTutorFeeFragment ();
                        SchoolCoachingFeeFragment fragment_Fee = new SchoolCoachingFeeFragment();
                        fragment_Fee.setArguments(bundle);
                        ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_Fee);

                        // SwitchFragment (fragment_Fee);
                        break;
                    case "Manage Attendance":
                        StudentAttendencdeFragment fragment_attendence=new StudentAttendencdeFragment();
                        fragment_attendence.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment_attendence);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back_img:
                gotoBack();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).setActionBarTitle(school_name);
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
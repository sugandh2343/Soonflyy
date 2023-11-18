package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter.AttendenceSpinnerAdapter;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AttendenceSpinnerModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorManageStudentModel;
import soonflyy.learning.hub.adapter.StudentAttendenceAdapter;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import soonflyy.learning.hub.model.StudentAttendenceModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class StudentAttendencdeFragment extends Fragment implements VolleyResponseListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    RecyclerView rec_studentlist;
    LinearLayout lin_course,lin_school_tutor_title,lin_classSection,linChooseCourse;
    LinearLayout linCourse,linSubject,linCategory;
    View viewLineCourse,viewLineSubject;

    TextView tv_title;
    SwipeRefreshLayout swipe;
    StudentAttendenceAdapter adapter;
    ArrayList<SchoolCoachingTutorAsginModel> sTutorList=new ArrayList<>();
    ArrayList<Indp_TutorManageStudentModel> itstudentList=new ArrayList<>();
    ArrayList<StudentAttendenceModel> list = new ArrayList<>();

    ArrayList<MyCourseDetailModel> clist = new ArrayList<>();
    ArrayAdapter<MyCourseDetailModel> courseAdapter;
    Spinner course_spinner;
    TextView tv_courseName;
    String course_id,date,class_id = "", section_id = "";
    Spinner class_spinner, section_spinner;
    TextView tv_class, tv_section;
    ArrayList<AllClassesModel> classList = new ArrayList<>();
    ArrayList<AllClassesModel> sectionList = new ArrayList<>();
    ArrayAdapter<AllClassesModel> class_adapter, section_adapter;


    //----------------attandance spinner-------------//
    RecyclerView recAttendanceType;
    ArrayList<AttendenceSpinnerModel>typeList=new ArrayList<>();
    AttendenceSpinnerAdapter attendanceTypeAdapter;
    TextView tvType;


    //---------------------------------------------//


    // Spinner spiner_attendence;
    Button btn_submit;
    int day, month, year;

    CalendarView calendar;

    String from, itutor_id,school_id;
    String assignValue="",type="course";

    public StudentAttendencdeFragment() {
        // Required empty public ccustom_calenederonstructor
    }


    public static StudentAttendencdeFragment newInstance(String param1, String param2) {
        StudentAttendencdeFragment fragment = new StudentAttendencdeFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_attendencde, container, false);
        initView(view);

        getArgumentData();
        init_swipe_method();
        setCourseSpinner();
        setClassSectionSpinner();
        // initRecyclerView();

        setCalendar();
        initRecyclerView();
        tv_courseName.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_section.setOnClickListener(this);
        tv_class.setOnClickListener(this);
        class_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);
        tvType.setOnClickListener(this);
        linCourse.setOnClickListener(this);
        linSubject.setOnClickListener(this);

        return view;
    }

    private void setClassSectionSpinner() {
        //for class spinner
        classList.add(new AllClassesModel());
        class_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, classList);
        class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        class_spinner.setAdapter(class_adapter);

        //for section spinner
        sectionList.add(new AllClassesModel());
        section_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, sectionList);
        section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        section_spinner.setAdapter(section_adapter);
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            //manage for home tuttor
            setAttendanceType();
          //  sendApiCall();

        } else if (from.equals(INDEPENDENT_TUTOR)) {
            //manage for independent tutor
            itutor_id = getArguments().getString("itutor_id");
            lin_course.setVisibility(View.GONE);
            lin_classSection.setVisibility(View.GONE);
            if (CommonMethods.checkInternetConnection(getActivity())) {
                    sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);

                }
        } else if (from.equals(SCHOOL_COACHING)) {
            tv_title.setVisibility(View.GONE);
            lin_school_tutor_title.setVisibility(View.VISIBLE);
            lin_classSection.setVisibility(View.GONE);
            school_id = getArguments().getString("school_id");
            lin_course.setVisibility(View.GONE);
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_TUTOR);

            }

        }else if (from.equals(SCHOOL_TUTOR)){
            school_id = getArguments().getString("school_id");
            lin_school_tutor_title.setVisibility(View.GONE);
            lin_classSection.setVisibility(View.VISIBLE);
            lin_course.setVisibility(View.GONE);
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_CLASS);
            }


        }
    }

    private void setAttendanceType() {
        type="course";
        typeList.clear();
        typeList.add(new AttendenceSpinnerModel("My Course(Created by myself)",false));
        typeList.add(new AttendenceSpinnerModel("My Teachers(Assigned to)",false));
        typeList.add(new AttendenceSpinnerModel("Assigned Course(Assigned by)",false));
        attendanceTypeAdapter=new AttendenceSpinnerAdapter(getActivity(), typeList, new AttendenceSpinnerAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Log.e("type",""+position);
                tvType.setText(typeList.get(position).getTitle());
                recAttendanceType.setVisibility(View.GONE);
                typeList.get(position).setChecked(true);
                setOtherSelection(position);
                setCourseList(position);
                attendanceTypeAdapter.notifyDataSetChanged();
            }
        });
        recAttendanceType.setAdapter(attendanceTypeAdapter);
    }

    private void setCourseList(int position) {
        if (CommonMethods.checkInternetConnection(getActivity())){
            switch (position){
                case 0:
                    //for create by my self
                    assignValue="-1";
                    tv_title.setText("Student List");
                    linChooseCourse.setVisibility(View.VISIBLE);
                    notifyCourseSpinner();
                    sendRequest(ApiCode.GET_COURSE);
                    break;
                case 1:
                    // for assigned to teachers
                    tv_title.setText("Teacher List");
                    assignValue="1";
                    linChooseCourse.setVisibility(View.GONE);
                    sendRequest(ApiCode.GET_MY_ASSIGN_TEACHER);
                    //-------------tem testing data----------//
                    sendRequest(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST);

                    //-------------------------------
                    break;
                case 2:
                    //for assigned by
                    assignValue="2";
                    tv_title.setText("Student List");
                    notifyCourseSpinner();
                    linChooseCourse.setVisibility(View.VISIBLE);
                    sendRequest(ApiCode.GET_ASSIGN_BY_COURSES);

                    break;
            }
        }

    }

//    private void setTempData() {
//        list.clear();
//        for ( int i=0;i<5;i++){
//            StudentAttendenceModel model=new StudentAttendenceModel();
//            model.setFirst_name("Name "+i);
//            model.setAttendanceStatus(true);
//            list.add(model);
//        }
//        initRecyclerView();
//
//    }

    private void notifyCourseSpinner() {
        tv_courseName.setText("");
        clist.clear();
        courseAdapter.notifyDataSetChanged();
    }

    private void setOtherSelection(int position) {
        for (int i=0;i<typeList.size();i++){
            if (i!=position){
                typeList.get(i).setChecked(false);
            }
        }
        attendanceTypeAdapter.notifyDataSetChanged();
    }

    private void setCalendar() {
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);

        calendar.setDate(System.currentTimeMillis(), false, true);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int dayOfMonth) {
                day = dayOfMonth;
                month = m;
                year = y;
                String sd=dayOfMonth+"/"+(month+1)+"/"+year;
                date=CommonMethods.changeDateTimeFmt("dd/MM/yyyy","dd-MMM-yyyy",sd);
                //
                callAttendanceListApi();
            }
        });
    }

    private void callAttendanceListApi() {
        if (ConnectivityReceiver.isConnected()){
            if (from.equals(SIMPLEE_HOME_TUTOR)){
                if (type.equals("course")){
                    //api call for course case
                    if (!TextUtils.isEmpty(assignValue)) {
                        if (assignValue.equals("1")) {
                            // call for assigned to case
                            sendRequest(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST);
                            list.clear();
                            initRecyclerView();
                        }else{
                            if (course_id!=null && !TextUtils.isEmpty(course_id)) {
                                list.clear();
                                initRecyclerView();
                                sendRequest(ApiCode.GET_STUDENT_LIST_ATTENDANCE);
                            }
                        }
                    }

                }else {
                    //call for subject case
                    sendRequest(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST);
                }
            }
        }
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_COURSE);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void initView(View view) {
        linCategory=view.findViewById(R.id.lin_category);
        linCourse=view.findViewById(R.id.lin_att_course);
        linSubject=view.findViewById(R.id.lin_att_subject);
        viewLineCourse=view.findViewById(R.id.view_course);
        viewLineSubject=view.findViewById(R.id.view_subject);
        linChooseCourse=view.findViewById(R.id.lin_choose_course);
        lin_classSection=view.findViewById(R.id.lin_class_section);
        tv_class = view.findViewById(R.id.tv_s_class);
        tv_section = view.findViewById(R.id.tv_section);
        class_spinner = view.findViewById(R.id.class_spinner);
        section_spinner = view.findViewById(R.id.section_spinner);

        lin_school_tutor_title = view.findViewById(R.id.lin_s_tutor_title);
        swipe = view.findViewById(R.id.swipe);
        tv_courseName = view.findViewById(R.id.tv_course_name);
        course_spinner = view.findViewById(R.id.course_spinner);
        rec_studentlist = view.findViewById(R.id.rec_studentlist);
        btn_submit = view.findViewById(R.id.btn_submit);
        calendar = view.findViewById(R.id.custom_caleneder);
        lin_course = view.findViewById(R.id.lin_course);
        tv_title = view.findViewById(R.id.tv_title);

        tvType=view.findViewById(R.id.tv_type);
        recAttendanceType=view.findViewById(R.id.rec_attendance_type);
        recAttendanceType.setLayoutManager(new LinearLayoutManager(getContext()));



        calendar.setMaxDate(System.currentTimeMillis());
        date=CommonMethods.getCurrentDate();
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                initControl();
                if (CommonMethods.checkInternetConnection(getActivity())) {

                    if (from.equals(SIMPLEE_HOME_TUTOR)) {
                      //  sendApiCall();

                    } else if (from.equals(INDEPENDENT_TUTOR)) {
                      sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);

                    }else if (from.equals(SCHOOL_TUTOR)){
                        if (!TextUtils.isEmpty(class_id) && !TextUtils.isEmpty(section_id)){
                            //call get student api
                                sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);
                        }else if (!TextUtils.isEmpty(section_id)){
                            sendRequest(ApiCode.SCHOOL_GET_SECTION);
                        }else {
                            sendRequest(ApiCode.SCHOOL_GET_CLASS);
                        }
                    }
                }
               // initRecyclerView();
                // init_CustomCalender();
                swipe.setRefreshing(false);
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

//    private void init_CustomCalender() {
//        HashMap<Object, Property> descHashMap = new HashMap<>();
//
//        Property propDefault = new Property();
//        propDefault.layoutResource = R.layout.defould_calenderview;
//        propDefault.dateTextViewResource = R.id.text_view;
//        descHashMap.put("default", propDefault);
////////////for current date
//        Property currentProperty = new Property();
//        currentProperty.layoutResource = R.layout.current_view;
//        propDefault.dateTextViewResource = R.id.text_view;
//        descHashMap.put("current", currentProperty);
//        /////////for present date
//        Property presentProperty = new Property();
//        presentProperty.layoutResource = R.layout.present_date;
//        propDefault.dateTextViewResource = R.id.text_view;
//        descHashMap.put("present", presentProperty);
//        /////////for absent date
//        Property absentProperty = new Property();
//        absentProperty.layoutResource = R.layout.absent_date;
//        propDefault.dateTextViewResource = R.id.text_view;
//        descHashMap.put("present", absentProperty);
//        /////holiday date
//        Property propHoliday = new Property();
//        propHoliday.layoutResource = R.layout.holiday_view;
//        propHoliday.dateTextViewResource = R.id.text_view;
//        descHashMap.put("holiday", propHoliday);
//        ////set desc hashmap///
//        custom_caleneder.setMapDescToProp(descHashMap);
//        ///initialize hashmap///
//          //HashMap<Integer,Object> mapDescToProp = new HashMap<>();
//        HashMap<Integer, Object> dateHashMap = new HashMap<>();
//
//
//        Calendar calendar = Calendar.getInstance();
//        ///put values..
//        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");
//        dateHashMap.put(1,"present");
//        dateHashMap.put(2,"holiday");
//        dateHashMap.put(3,"present");
//        dateHashMap.put(4,"absent");
//        dateHashMap.put(20,"present");
//        dateHashMap.put(23,"absent");
//        ////set date
//        custom_caleneder.setDate(calendar,dateHashMap);
//        custom_caleneder.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
//                String date = selectedDate.get(calendar.DAY_OF_MONTH)
//                        +"/"+(selectedDate.get(calendar.MONTH)+1)
//                        +"/" +selectedDate.get(calendar.YEAR);
//                //// display date in toast
//                Toast.makeText(getContext(),date, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private void initRecyclerView() {
        Log.e("listsize",""+list.size());
        rec_studentlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_studentlist.setLayoutManager(linearLayoutManager);
        rec_studentlist.setKeepScreenOn(true);
        adapter = new StudentAttendenceAdapter(getContext(), new String[]{from,type,assignValue},list, new StudentAttendenceAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onStudentAttendenceClick(int position, int sposition) {
                Log.e("position ", "" + position);
                Log.e("selected ", "" + sposition);
                if (sposition > 0 && position>=0) {
                    if (sposition == 2) {//type.equals("A")
                        list.get(position).setAttendanceStatus(false);
                        list.get(position).setAttenadnce_value("A");

                    } else {
                        list.get(position).setAttendanceStatus(true);
                        list.get(position).setAttenadnce_value("P");
                    }

                    adapter.notifyDataSetChanged();
                }
            }


        });
        rec_studentlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (adapter.getItemCount()>0){
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                tv_title.setVisibility(View.VISIBLE);
            }
            rec_studentlist.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
        }else{
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                tv_title.setVisibility(View.GONE);
            }
            rec_studentlist.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        }


    }

    private void showSpinerAttendence() {

    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    private void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setCourseSpinner() {
        clist.add(new MyCourseDetailModel(""));
        courseAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, clist);
        courseAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        course_spinner.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_courseName.setText(parent.getSelectedItem().toString());
                    course_id = clist.get(position).getId();
                    if (ConnectivityReceiver.isConnected()) {
                        sendRequest(ApiCode.GET_STUDENT_LIST_ATTENDANCE);
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                    }
                    // category_name = subCategoryList.get(position).getName();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void sendRequest(int request) {
        SessionManagement management=new SessionManagement(getActivity());
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_TUTOR:
                params.put("school_id", school_id);
                callApi(ApiCode.SCHOOL_GET_TUTOR, params);
                break;
            case ApiCode.ADD_ATTENDANCE:
                params.put("type",type);
                if (type.equals("course")){
                    params.put("assigned_value",assignValue);
                    if (assignValue.equals("1")){
                        params.put("tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                    }else{
                        params.put("course_id", course_id);
                    }
                }else  if (type.equals("subject")){
                    params.put("tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                }

               // params.put("teacher_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("year", String.valueOf(year));
                params.put("month", String.valueOf(month + 1));
                params.put("day", String.valueOf(day));
                params.put("data", new Gson().toJson(getAttendance()));//A,P
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.ADD_ATTENDANCE, params);
                break;
            case ApiCode.SCHOOL_ADD_ATTENDANCE:
                if (from.equals(INDEPENDENT_TUTOR)) {
                    params.put("teacher_id", new SessionManagement(getContext()).getString(SCHOOL_IT_ID));
                    params.put("type", "1");
                    params.put("date", date);
                    params.put("data", new Gson().toJson(getAttendance()));//A,P
                } else if (from.equals(SCHOOL_COACHING)) {
                params.put("school_id", new SessionManagement(getContext()).getString(SCHOOL_ID));
                params.put("type", "0");
                params.put("date", date);
                params.put("data", new Gson().toJson(getAttendance()));//A,P
            }

                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.SCHOOL_ADD_ATTENDANCE, params);
                break;
            case ApiCode.GET_STUDENT_LIST_ATTENDANCE:
                params.put("course_id", course_id);
                params.put("year", String.valueOf(year));
                params.put("month", String.valueOf(month + 1));
                params.put("day", String.valueOf(day));
               // params.put("teacher_id", new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_STUDENT_LIST_ATTENDANCE, params);
                break;
            case ApiCode.GET_COURSE:
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("course_id", " ");
                callApi(ApiCode.GET_COURSE, params);
                break;
            case ApiCode.GET_ASSIGN_BY_COURSES :
                params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("assign_value","2" );
                callApi(ApiCode.GET_ASSIGN_BY_COURSES, params);
                break;
            case ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST :
                params.put("assigned_by_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("type",type);
                params.put("year", String.valueOf(year));
                params.put("month", String.valueOf(month + 1));
                params.put("day", String.valueOf(day));
                callApi(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST, params);
                break;
            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                if (from.equals(SCHOOL_TUTOR)) {
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("school_id", school_id);
                    params.put("type", "0");
                }else if (from.equals(INDEPENDENT_TUTOR)) {
                    ////--------for independent tutor---/////
                    params.put("teacher_id",itutor_id);
                    params.put("type","1");
                }
                callApi(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS, params);
                break;
            case ApiCode.SCHOOL_GET_CLASS:
                params.put("school_id", school_id);
                if (from.equals(SCHOOL_TUTOR)){
                    params.put("teacher_id",management.getString(SCHOOL_TEACHER_ID));
                }else{
                    params.put("teacher_id","");
                }
                callApi(ApiCode.SCHOOL_GET_CLASS, params);
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                params.put("school_id", school_id);
                params.put("class_id", class_id);
                if (from.equals(SCHOOL_TUTOR)){
                    params.put("teacher_id",management.getString(SCHOOL_TEACHER_ID));
                }else{
                    params.put("teacher_id","");
                }
                callApi(ApiCode.SCHOOL_GET_SECTION, params);
                break;
            case ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR:
                params.put("school_id",school_id);
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
                params.put("date", date);
                params.put("data", new Gson().toJson(getAttendance()));
                callApi(ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR, params);
                break;

        }
    }


    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_TUTOR);
                Log.e("params", params.toString());
                break;
            case ApiCode.GET_STUDENT_LIST_ATTENDANCE:
                service.postDataVolley(ApiCode.GET_STUDENT_LIST_ATTENDANCE,
                        BaseUrl.URL_GET_STUDENT_LIST_ATTENDANCE, params);
                break;
            case ApiCode.SCHOOL_ADD_ATTENDANCE:
                service.postDataVolley(ApiCode.SCHOOL_ADD_ATTENDANCE,
                        BaseUrl.URL_SCHOOL_ADD_ATTENDANCE, params);
                break;
            case ApiCode.ADD_ATTENDANCE:
                service.postDataVolley(ApiCode.ADD_ATTENDANCE,
                        BaseUrl.URL_ADD_ATTENDANCE, params);
                break;

            case ApiCode.GET_COURSE:
                service.postDataVolley(ApiCode.GET_COURSE,
                        BaseUrl.URL_GET_COURSE, params);
                break;
            case ApiCode.GET_ASSIGN_BY_COURSES:
                service.postDataVolley(ApiCode.GET_ASSIGN_BY_COURSES,
                        BaseUrl.URL_GET_ASSIGN_BY_COURSES, params);
                break;
            case ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST:
                service.postDataVolley(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST,
                        BaseUrl.URL_GET_ASSIGN_TO_ATTENDANCE_LIST, params);
                Log.e("url",BaseUrl.URL_GET_ASSIGN_TO_ATTENDANCE_LIST);
                Log.e("params",""+params);
                break;
            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                service.postDataVolley(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS,
                        BaseUrl.URL_SCHOOL_GET_SCHOOL_STUDENTS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SCHOOL_STUDENTS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_GET_CLASS,
                        BaseUrl.URL_SCHOOL_GET_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION,
                        BaseUrl.URL_SCHOOL_GET_SECTION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SECTION);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR,
                        BaseUrl.URL_SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR);
                Log.e("params", params.toString());
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.ADD_ATTENDANCE:
            case ApiCode.SCHOOL_ADD_ATTENDANCE:
            case ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR:
                Log.e("attendence ", response);
                if (jsonObject.getBoolean("status")) {
                    //  pdfFileString = "";
                    CommonMethods.showSuccessToast(getContext(), "Attendance Added Successfully");
                    //sendAPicCAll();
                }else{
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;
            case ApiCode.GET_STUDENT_LIST_ATTENDANCE:
                if (jsonObject.getBoolean("status")) {
                    String submissionStatus=jsonObject.getString("submission_status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<StudentAttendenceModel> plist = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<StudentAttendenceModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(plist);
                        initRecyclerView();
                    } else {
                        list.clear();
                        initRecyclerView();
                        ///CommonMethods.showSuccessToast(getContext(), "");
                    }

                }else{
                    list.clear();
                    initRecyclerView();
                }
                break;
            case ApiCode.GET_COURSE:
            case ApiCode.GET_ASSIGN_BY_COURSES:
                Log.e("courses ", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<MyCourseDetailModel> list = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<MyCourseDetailModel>>() {
                                        }.getType());
                        clist.clear();
                        if (requestType==ApiCode.GET_COURSE) {
                            clist.add(new MyCourseDetailModel(""));
                            for (int i = 0; i < list.size(); i++) {
                                MyCourseDetailModel model=list.get(i);
                                if (model.getAssigned_value().equals("-1")){
                                    clist.add(model);
                                }
                            }
                        }else {
                            clist.add(new MyCourseDetailModel(""));
                            clist.addAll(list);
                        }
                        courseAdapter.notifyDataSetChanged();
                        //setDataOnList();
                    } else {
                        clist.clear();
                        clist.add(new MyCourseDetailModel(""));
                        courseAdapter.notifyDataSetChanged();
                        //CommonMethods.showSuccessToast(getContext(), "You have not created any course");
                    }

                }else{
                    clist.clear();
                    clist.add(new MyCourseDetailModel(""));
                    courseAdapter.notifyDataSetChanged();
                    CommonMethods.showSuccessToast(getContext(), "You have not created any course");
                }
                break;
            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                Log.e("sc_students",response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Indp_TutorManageStudentModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Indp_TutorManageStudentModel>>() {
                                        }.getType());
                        itstudentList.clear();
                        list.clear();
                        itstudentList.addAll(psearch);
                        for (int i=0;i<itstudentList.size();i++){
                            list.add(new StudentAttendenceModel());
                            list.get(i).setFirst_name(itstudentList.get(i).getName());
                            list.get(i).setStudent_id(itstudentList.get(i).getStudent_id());
                            list.get(i).setAttendanceStatus(false);
                        }
                        initRecyclerView();
                    } else {
                        itstudentList.clear();
                        list.clear();
                        initRecyclerView();

                    }
                }else{
                    itstudentList.clear();
                    list.clear();
                    initRecyclerView();
                  //  CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;

            case ApiCode.SCHOOL_GET_TUTOR:
                Log.e("sc_tutor", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolCoachingTutorAsginModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolCoachingTutorAsginModel>>() {
                                        }.getType());
                        sTutorList.clear();
                        list.clear();
                        sTutorList.addAll(psearch);
                        for (int i=0;i<sTutorList.size();i++){
                            list.add(new StudentAttendenceModel());
                            list.get(i).setFirst_name(sTutorList.get(i).getName());
                            list.get(i).setStudent_id(sTutorList.get(i).getId());
                            list.get(i).setAttendanceStatus(false);
                        }
                        initRecyclerView();


                    }else{
                        sTutorList.clear();
                        list.clear();
                        initRecyclerView();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    sTutorList.clear();
                    list.clear();
                    initRecyclerView();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_CLASS:
                Log.e("sc_login", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("classes");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        classList.clear();
                        classList.add(new AllClassesModel());
                        classList.addAll(psearch);
                        class_adapter.notifyDataSetChanged();

                    } else {
                        classList.clear();
                        classList.add(new AllClassesModel());
                        class_adapter.notifyDataSetChanged();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    classList.clear();
                    classList.add(new AllClassesModel());
                    class_adapter.notifyDataSetChanged();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        sectionList.addAll(psearch);
                        section_adapter.notifyDataSetChanged();
                    } else {
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        section_adapter.notifyDataSetChanged();

                    }
                } else {
                    sectionList.clear();
                    sectionList.add(new AllClassesModel());
                    section_adapter.notifyDataSetChanged();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST:
                Log.e("asToteachers ",response);
                try {
                    if (jsonObject.getBoolean("status")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0) {
                            list.clear();
                          for (int i=0;i<jsonArray.length();i++){
                              JSONObject object=jsonArray.getJSONObject(i);
                              StudentAttendenceModel model=new StudentAttendenceModel();
                              model.setStudent_id(object.getString("id"));
                              model.setFirst_name(object.getString("name"));
                              model.setImage(object.getString("image"));
                              model.setAttenadnce_value(object.getString("attandance_value"));
                              if (type.equals("course")) {
                                  model.setTotalStudent(object.getString("total_student"));
                                  model.setAbsent(object.getString("absent"));
                                  model.setPresent(object.getString("present"));
                              }
                              list.add(model);
                          }
                          initRecyclerView();
                        }else{
                            list.clear();
                            initRecyclerView();
                          //  CommonMethods.showSuccessToast(getContext(),"You have not created any course");
                        }

                    }else{
                        list.clear();
                        initRecyclerView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_att_course:
                setDataForCourse();
                break;
            case R.id.lin_att_subject:
                setDataForSubject();
                    break;

            case R.id.tv_course_name:
                course_spinner.performClick();
                break;
            case R.id.btn_submit:
                if (from.equals(SIMPLEE_HOME_TUTOR)) {
                    if (type.equals("course")&& !assignValue.equals("1")
                            && TextUtils.isEmpty(course_id)) {
                        CommonMethods.showSuccessToast(getContext(), "Choose Course");
                    } else if (list.size() == 0) {
                        CommonMethods.showSuccessToast(getContext(), "There is no student in this course");
                    } else {
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.ADD_ATTENDANCE);
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                        }
                    }
                } else if (from.equals(INDEPENDENT_TUTOR)||from.equals(SCHOOL_COACHING)) {
                    if (TextUtils.isEmpty(date)){
                        CommonMethods.showSuccessToast(getContext(),"Select Date");
                    }else{
                        if (list.size() > 0) {
                            if (CommonMethods.checkInternetConnection(getActivity())) {
                                sendRequest(ApiCode.SCHOOL_ADD_ATTENDANCE);
                            }
                        }
                    }
                }else if (from.equals(SCHOOL_TUTOR)){
                    if (TextUtils.isEmpty(date)){
                        CommonMethods.showSuccessToast(getContext(),"Select Date");
                    }else if (TextUtils.isEmpty(class_id)){
                        CommonMethods.showSuccessToast(getContext(),"Select Class");
                    }else if (TextUtils.isEmpty(section_id)){
                        CommonMethods.showSuccessToast(getContext(),"Select Section");
                    }else{
                        if (list.size() > 0) {
                            if (CommonMethods.checkInternetConnection(getActivity())) {
                                sendRequest(ApiCode.SCHOOL_ADD_ATTENDANCE_BY_SCHOOL_TUTOR);
                            }
                        }
                    }
                }

                break;
            case R.id.tv_s_class:
                class_spinner.performClick();
                break;
            case R.id.tv_section:
                section_spinner.performClick();
                break;
            case R.id.tv_type:
                if (recAttendanceType.getVisibility()==View.GONE) {
                    recAttendanceType.setVisibility(View.VISIBLE);
                }else if (recAttendanceType.getVisibility()==View.VISIBLE){
                    recAttendanceType.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setDataForSubject() {
        type="subject";
        assignValue="";
        tv_title.setText("Teacher List");
        linCategory.setVisibility(View.GONE);
        linChooseCourse.setVisibility(View.GONE);
        viewLineSubject.setVisibility(View.VISIBLE);
        viewLineCourse.setVisibility(View.GONE);
        list.clear();
        initRecyclerView();
        sendRequest(ApiCode.GET_ASSIGN_TO_ATTENDANCE_LIST);

    }

    private void setDataForCourse() {
        type="course";
        linCategory.setVisibility(View.VISIBLE);
        viewLineSubject.setVisibility(View.GONE);
        linChooseCourse.setVisibility(View.VISIBLE);
        clist.clear();
        clist.add(new MyCourseDetailModel(""));
        courseAdapter.notifyDataSetChanged();
        tv_courseName.setText("");
        setAttendanceType();
        viewLineCourse.setVisibility(View.VISIBLE);
        list.clear();
        initRecyclerView();
        tvType.setText("");
    }

    private ArrayList<Attendance> getAttendance() {
        ArrayList<Attendance> attendancelist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            StudentAttendenceModel model = list.get(i);
         /*   if (model.isAttendanceStatus()) {
                attendancelist.add(new Attendance(model.getStudent_id(), "P"));
            } else {
                attendancelist.add(new Attendance(model.getStudent_id(), "A"));
            }

          */
            attendancelist.add(new Attendance(model.getStudent_id(), model.getAttenadnce_value()));
        }
        return attendancelist;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity) getActivity()).getSupportActionBar().show();
            ((TeacherMainActivity) getActivity()).setTeacherActionBar("Student's Attendance", false);
        } else if (from.equals(INDEPENDENT_TUTOR)||from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_TUTOR)) {
            ((SchoolMainActivity) getActivity()).setActionBarTitle("Attendance");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            switch (parent.getId()) {
                case R.id.class_spinner:
                    AllClassesModel model = classList.get(position);
                    tv_class.setText(model.getName());
                    class_id = model.getClass_id();
                    section_id = "";
                    sectionList.clear();
                    sectionList.add(new AllClassesModel());
                    section_adapter.notifyDataSetChanged();
                    tv_section.setText("");
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);

                    }
                    break;

            }
           // callGetTableApi();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //

    private class Attendance {
        String student_id;
        String type;

        public Attendance(String student_id, String type) {
            this.student_id = student_id;
            this.type = type;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String Student_Id) {
            this.student_id = student_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
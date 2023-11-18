package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolTimeTableAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolTimeTableModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.adapter.DayFilterAdapter;
import soonflyy.learning.hub.model.DayModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SchoolTutotTimeTableFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, VolleyResponseListener, PopupWindow.OnDismissListener {
    RecyclerView rec_time_table;

    TextView tv_student, tvTitleDate;
    ImageView ivBack;
    View view_student, view_tutor;
    LinearLayout lin_student, lin_teacher, lin_s_header, lin_t_header, lin_st_table_header;
    View lin_s_t_layout;

    CardView cv_class,cvDateFilter;
    Spinner class_spinner, section_spinner;
    TextView tv_class, tv_section, tv_date;
    SchoolTimeTableAdapter adapter;
    ArrayList<SchoolTimeTableModel> list = new ArrayList<>();
    SwipeRefreshLayout swipe;
    ArrayList<AllClassesModel> classList = new ArrayList<>();
    ArrayList<AllClassesModel> sectionList = new ArrayList<>();
    ArrayAdapter<AllClassesModel> class_adapter, section_adapter;
    String from, school_id, class_id = "", section_id = "", itutor_id;
    String type = "0";

    //--day filter-----//
    ArrayList<DayModel>dayList=new ArrayList<>();
    TextView tvAttachedDayPopup;
    String dayId="";
    DayFilterAdapter dayFilterAdapter;
    PopupWindow dayPopupWindow;

    public SchoolTutotTimeTableFragment() {
        // Required empty public constructor
    }


    public static SchoolTutotTimeTableFragment newInstance(String param1, String param2) {
        SchoolTutotTimeTableFragment fragment = new SchoolTutotTimeTableFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_tutot_time_table, container, false);
        initView(view);
        getArgumentData();

        initRecyclerview();
        init_swipe_method();
        return view;
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING) || from.equals(SCHOOL_TUTOR)) {
            school_id = getArguments().getString("school_id");
            setSpinner();
            sendApiRequest();
            if (from.equals(SCHOOL_COACHING)) {
                manageViewData("0");
            } else if (from.equals(SCHOOL_TUTOR)) {
                type = "1";
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        lin_s_t_layout.setVisibility(View.GONE);
                        lin_st_table_header.setVisibility(View.VISIBLE);
                        lin_t_header.setVisibility(View.GONE);
                    }
                });

            }

        }
    }

    private void initView(View view) {
        cvDateFilter=view.findViewById(R.id.cv_month);
        ivBack = view.findViewById(R.id.iv_back);
        tvTitleDate = view.findViewById(R.id.tv_test_date);
        lin_st_table_header = view.findViewById(R.id.lin_st_table_header);
        lin_s_t_layout = view.findViewById(R.id.school_tutor_tab);
        view_student = view.findViewById(R.id.view_school);
        view_tutor = view.findViewById(R.id.view_tutor);
        lin_student = view.findViewById(R.id.lin_school);
        lin_teacher = view.findViewById(R.id.lin_tutor);
        tv_student = view.findViewById(R.id.tv_school);
        lin_s_header = view.findViewById(R.id.lin_s_table_header);
        lin_t_header = view.findViewById(R.id.lin_t_table_header);

        class_spinner = view.findViewById(R.id.class_spinner);
        section_spinner = view.findViewById(R.id.section_spinner);
        tv_date = view.findViewById(R.id.tv_date);

        tv_class = view.findViewById(R.id.tv_s_class);
        tv_section = view.findViewById(R.id.tv_section);


        swipe = view.findViewById(R.id.swipe);
        rec_time_table = view.findViewById(R.id.rec_time_table);

        tvTitleDate.setText("Date : " + CommonMethods.getCurrentTime("dd/MM/yyyy, EEEE"));
        rec_time_table.hasFixedSize();
        rec_time_table.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_time_table.setLayoutManager(layoutManager);
        rec_time_table.setKeepScreenOn(true);


        ivBack.setOnClickListener(this);
        tv_section.setOnClickListener(this);
        tv_class.setOnClickListener(this);
        tv_date.setOnClickListener(this);

        tv_student.setText("Student");
        lin_teacher.setOnClickListener(this);
        lin_student.setOnClickListener(this);

        class_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);

        /*tv_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callGetTableApi();

            }
        });

         */


    }

    private void sendApiRequest() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_CLASS);
        }
    }

    private void setSpinner() {
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
//
//        list = new ArrayList<>();
//
//        list.add(new SchoolTimeTableModel());
//        list.add(new SchoolTimeTableModel());
//        list.add(new SchoolTimeTableModel());
//        list.add(new SchoolTimeTableModel());

        adapter = new SchoolTimeTableAdapter(getActivity(), from, type, list, new SchoolTimeTableAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
//                Scl_SelectChpterDeatailFragment fragment = new Scl_SelectChpterDeatailFragment();
//
//                SwitchFragment(fragment);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_time_table.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.e("size", "" + adapter.getItemCount());


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
        //  ((SchoolMainActivity) getActivity()).setActionBarTitle("Time Table");
        ((SchoolMainActivity) getActivity()).showHideHomeActionBar(false);
    }

    ///---api call--//
    private void callGetTableApi() {
        if (from.equals(SCHOOL_TUTOR)) {
            if (!TextUtils.isEmpty(class_id) && !TextUtils.isEmpty(section_id)
                    && !TextUtils.isEmpty(tv_date.getText().toString())) {
                sendRequest(ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR);
            }
        } else {
            if (type.equals(0)) {
                if (!TextUtils.isEmpty(class_id) && !TextUtils.isEmpty(section_id)
                        && !TextUtils.isEmpty(tv_date.getText().toString())) {
                    sendRequest(ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL);
                }
            } else {
                if (!TextUtils.isEmpty(class_id) && !TextUtils.isEmpty(section_id)
                        && !TextUtils.isEmpty(tv_date.getText().toString())) {
                    sendRequest(ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL);
                }
            }
        }
    }

    private void sendRequest(int request) {
        SessionManagement management=new SessionManagement(getActivity());
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
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
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL:
                if (type.equals("0")) {
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("school_id", school_id);
                    params.put("type", "0");
                   // params.put("date", tv_date.getText().toString());
                    params.put("days", dayId);

                } else if (type.equals("1")) {
                    params.put("type", "1");
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("school_id", school_id);
                    //params.put("date", tv_date.getText().toString());
                    params.put("days", dayId);
                }
                callApi(ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL, params);
                break;
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR:
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("school_id", school_id);
                params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
              //  params.put("date", tv_date.getText().toString());
                params.put("days", dayId);
                callApi(ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
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
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL:
                service.postDataVolley(ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL,
                        BaseUrl.URL_SCHOOL_GET_TIME_TABLE_BY_SCHOOL, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_TIME_TABLE_BY_SCHOOL);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_TIME_TABLE_BY_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_TIME_TABLE_BY_TUTOR);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
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
                    // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
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
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;

//            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL:
//                Log.e("sc_tutor_time",response.toString());
            //break;
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_TUTOR:
            case ApiCode.SCHOOL_GET_TIME_TABLE_BY_SCHOOL:
                Log.e("sc_tutor", response.toString());
                list.clear();
                initRecyclerview();
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<SchoolTimeTableModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolTimeTableModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();

                    } else {
                        list.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    list.clear();
                    initRecyclerview();
                    // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////

                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                ((SchoolMainActivity) getActivity()).onBackPressed();
                break;
            case R.id.tv_s_class:
                class_spinner.performClick();
                break;
            case R.id.tv_section:
                section_spinner.performClick();
                break;
            case R.id.tv_date:
                showFilterDayPopupDialog(tv_date);
                /*String predate = tv_date.getText().toString().trim();
                showDatePicker(tv_date);
                // CommonMethods.showDatePicker(getActivity(), tv_date);
                if (!TextUtils.isEmpty(tv_date.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(predate)) {
                        if (!tv_date.getText().toString().trim().equals(predate)) {
                            list.clear();
                            initRecyclerview();
                            callGetTableApi();
                        }
                    }
                }


                 */
                break;
            case R.id.lin_school:
                type = "0";
                view_tutor.setVisibility(View.GONE);
                view_student.setVisibility(View.VISIBLE);
                manageViewData(type);

                break;
            case R.id.lin_tutor:
                type = "1";
                view_student.setVisibility(View.GONE);
                view_tutor.setVisibility(View.VISIBLE);
                manageViewData(type);
                // callGetTableApi();
                break;

        }
    }
    private void showFilterDayPopupDialog(TextView attachedToView){
        tvAttachedDayPopup=attachedToView;
        CommonMethods.hideSoftKeyboard(getActivity());
        dayList.clear();
        dayList= CommonMethods.getDayList();
        // int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int width=cvDateFilter.getMeasuredWidth();
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_day_popup, null);
        RecyclerView recItem = view.findViewById(R.id.rec_item);
        recItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        attachedToView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_filter_list_24,0,R.drawable.ic_baseline_arrow_drop_up_gray_24,0);

//
        dayPopupWindow = new PopupWindow(view, width, height);
        //window.setContentView(view);
        dayPopupWindow.setFocusable(true);
        dayPopupWindow.setOutsideTouchable(true);
        dayPopupWindow.setOnDismissListener(this);
        dayPopupWindow.showAsDropDown(cvDateFilter);
        dayFilterAdapter=new DayFilterAdapter(getActivity(),dayList, new DayFilterAdapter.OnChangeSelectionListener() {
            @Override
            public void onChangeSelection(int position, DayModel dayModel) {
                dayId=dayModel.getId();
                attachedToView.setText(dayModel.getName());
                dayPopupWindow.dismiss();
                callGetTableApi();
            }
        });
        recItem.setAdapter(dayFilterAdapter);

    }
    private void showDatePicker(TextView tvView) {
//
        boolean isValidDate = false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_divider_color));


        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month, int day, int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());

                tvView.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {

            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }


    private void manageViewData(String type) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {


                if (type.equals("0")) {
                    lin_t_header.setVisibility(View.GONE);
                    lin_s_header.setVisibility(View.VISIBLE);
                    tv_section.setEnabled(true);
                    tv_class.setEnabled(true);

                    //also call api and mange data here
                } else {
                    lin_t_header.setVisibility(View.VISIBLE);
                    lin_s_header.setVisibility(View.GONE);
                    tv_section.setEnabled(true);
                    tv_class.setEnabled(true);
                    //also call api and mange data here
                }
            }
        });
        list.clear();
        initRecyclerview();
        callGetTableApi();


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
                    tv_section.setText("");
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    break;

            }
            list.clear();
            initRecyclerview();
            callGetTableApi();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onDismiss() {
        tvAttachedDayPopup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_filter_list_24, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
    }
}
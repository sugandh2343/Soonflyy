package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllSubjectModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter.Indp_TutorTimeTableAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorTimeTableModel;
import soonflyy.learning.hub.adapter.DayFilterAdapter;
import soonflyy.learning.hub.adapter.MultiDayAdapter;
import soonflyy.learning.hub.model.DayModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Scl_Ind_TutorTimeTableFragment extends Fragment implements VolleyResponseListener, View.OnClickListener, PopupWindow.OnDismissListener {
    RecyclerView rec_time_table;
    Indp_TutorTimeTableAdapter adapter;
    ArrayList<Indp_TutorTimeTableModel> list=new ArrayList<>();
    ArrayList<Indp_TutorTimeTableModel> allTableList=new ArrayList<>();
    TextView tv_f_date,tvTitleDate;
    ImageView ivBack;

    ArrayList<AllSubjectModel> subject_list = new ArrayList<>();
    ArrayAdapter<AllSubjectModel>subjectAdapter;
    private FloatingActionButton feb_addtimetable;
    String subject_id,date,sTime,eTime,period;

    String from, itutor_id,f_date,period_id,subject_name;

    //------day model----//
    ArrayList<DayModel>dayList=new ArrayList<>();
    MultiDayAdapter multiDayAdapter;
    PopupWindow dayPopupWindow,filterDayPopup;
    ArrayList<String>selectedDayIdList=new ArrayList<>();
    TextView tvAttachedDayPopup;
    String dayPopupType="",dayId="";
    DayFilterAdapter dayFilterAdapter;
    ImageView ivDayIcon;
    LinearLayout linDayFilter;

    Dialog addDialog;

    public Scl_Ind_TutorTimeTableFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Scl_Ind_TutorTimeTableFragment newInstance(String param1, String param2) {
        Scl_Ind_TutorTimeTableFragment fragment = new Scl_Ind_TutorTimeTableFragment();
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
        View view = inflater.inflate(R.layout.fragment_scl__ind__tutor_time_table, container, false);

        initview(view);
        getArgumentData();
        if (CommonMethods.checkInternetConnection(getActivity())){
            getAllTimePeriodDataApi();
            sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
            sendRequest(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT);
        }
        setSubjectSpinner();
        init_RecyclerView();
        initControl();
        ivBack.setOnClickListener(this);
        //-------initialize daylist------/
        dayList.clear();
        dayList.addAll(CommonMethods.getDayList());
        //------------------//
        return view;
    }

    private void setSubjectSpinner() {
            subject_list.add(new AllSubjectModel("",""));
            subjectAdapter=new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder,subject_list);
            subjectAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        }

    private void getArgumentData() {
        from=getArguments().getString("from");
        itutor_id=getArguments().getString("itutor_id");
    }

    private void initview(View view) {
        linDayFilter=view.findViewById(R.id.lin_date_filter);
        ivDayIcon=view.findViewById(R.id.iv_day_icon);
        ivBack=view.findViewById(R.id.iv_back);
        tvTitleDate=view.findViewById(R.id.tv_test_date);
        rec_time_table = view.findViewById(R.id.rec_time_table);
        feb_addtimetable = view.findViewById(R.id.feb_addtimetable);
        tv_f_date=view.findViewById(R.id.tv_fdate);
        rec_time_table.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_time_table.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_time_table.hasFixedSize();
        rec_time_table.setHasFixedSize(true);
        //row_ind_timetable

        tvTitleDate.setText("Date : "+CommonMethods.getCurrentTime("dd/MM/yyyy, EEEE"));
        feb_addtimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTimeTableDailoge("add");
            }
        });
       f_date=CommonMethods.getCurrentDate();

        String dName=CommonMethods.getCurrentDayName();
        Log.e("daYName",dName);
        dayId=CommonMethods.getDayIdFromName(dName);
        //tv_f_date.setText(f_date);
        tv_f_date.setText(dName);
        tv_f_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  CommonMethods.showDatePicker(getActivity(),tv_f_date,true,false);
               /* showDatePicker("filter",tv_f_date);
                f_date=tv_f_date.getText().toString().trim();

                */
                showFilterDayPopupDialog(tv_f_date);



            }
        });
       /*tv_f_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                f_date=s.toString();
                sendRequest(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT);
            }
        });*/
    }
    private  void showDatePicker(String type,TextView tvView){
//
        boolean isValidDate=false;
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
                if (type.equals("add")){
                    String d=tvView.getText().toString();
                    if (!TextUtils.isEmpty(d)) {
                        if (!validateDate(d)) {
                            tvView.setText("");
//                            spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                            CommonMethods.showSuccessToast(getActivity(),"You can't select a date earlier than the current date");
                        }
                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private void showAddTimeTableDailoge(String type) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_tutor_time_table);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        Button btn_create = dialog.findViewById(R.id.btn_create);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_sTime = dialog.findViewById(R.id.tv_s_time);
        TextView tv_eTime = dialog.findViewById(R.id.tv_e_time);
        TextView tv_date = dialog.findViewById(R.id.tv_date);
        EditText et_peroid = dialog.findViewById(R.id.et_period);
        TextView tv_suject = dialog.findViewById(R.id.tv_subject);
        Spinner subject_spinner = dialog.findViewById(R.id.spinner_subject);

        //initialize here selected day id list
        updateDay(type,selectedDayIdList);
        subject_spinner.setAdapter(subjectAdapter);
        if (type.equals("update")){
            tv_suject.setText(subject_name);
           et_peroid.setText(period);
           tv_date.setText(date);
           tv_sTime.setText(sTime);
           tv_eTime.setText(eTime);
        }

        tv_suject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject_spinner.performClick();
            }
        });

        dialog.show();
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTime=tv_sTime.getText().toString().trim();
                eTime=tv_eTime.getText().toString().trim();
                period=et_peroid.getText().toString().trim();
                date=tv_date.getText().toString().trim();
                if (TextUtils.isEmpty(subject_id)){
                   CommonMethods.showSuccessToast(getContext(),"Select Subject");
                }else if (TextUtils.isEmpty(period)){
                    et_peroid.setError("Enter period");
                    et_peroid.requestFocus();
                }else if (selectedDayIdList.size()==0){
                    CommonMethods.showSuccessToast(getActivity(),"Select day");
                }
//                else if (TextUtils.isEmpty(date)){
//                    CommonMethods.showSuccessToast(getContext(),"Select Date");
//                }
                else if (TextUtils.isEmpty(sTime)){
                    CommonMethods.showSuccessToast(getContext(),"Select Start Time");
                }else if (TextUtils.isEmpty(eTime)){
                    CommonMethods.showSuccessToast(getContext(),"Select End Time");
                }else if (!CommonMethods.isValidTimePeriod(sTime,eTime)){//CommonMethods.isValidTimePeriod(sTime,eTime,date)
                    CommonMethods.showSuccessToast(getContext(),"Invalid time");
                }
                else{
                    if (allTableList.size()>0) {
                        addDialog=dialog;
                       new ValidateDatabase().execute(type);
                    }else {

                        if (CommonMethods.checkInternetConnection(getActivity())) {
                            if (type.equals("add")) {
                            sendRequest(ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT);
                            dialog.dismiss();
                            }else if (type.equals("update")){
                                dialog.dismiss();
                                sendRequest(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT);
                            }
                        }
                    }


                }
            }

        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showDatePicker("add",tv_date);
                showDayPopupDialog(tv_date,dayList);
                //CommonMethods.showDatePicker(getActivity(),tv_date,true,false);
            }
        });
        tv_sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.showTimePicker(getActivity(),"Start Time",tv_sTime);
            }
        });
        tv_eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.showTimePicker(getActivity(),"End Time",tv_eTime);
            }
        });


        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    tv_suject.setText(parent.getSelectedItem().toString());
                    AllSubjectModel model=subject_list.get(parent.getSelectedItemPosition());
                    subject_id=model.getSubject_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.setCanceledOnTouchOutside(false);

    }

    private void updateDay(String type, ArrayList<String> idList) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dayList.clear();
                dayList=CommonMethods.getDayList();
                if (type.equals("update")){
                    if (idList.size()>0){
                        for (int i=0;i<idList.size();i++){
                            dayList.get(Integer.parseInt(idList.get(i))-1).setSelected(true);
                        }
                    }
                }
                try {
                    if (dayPopupWindow!=null && dayPopupWindow.isShowing()){
                        if (multiDayAdapter!=null){
                            multiDayAdapter.notifyDataSetChanged();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void showDayPopupDialog(TextView attachedToView,ArrayList<DayModel>dList){
        dayPopupType="add";
        CommonMethods.hideSoftKeyboard(getActivity());
       // int width = LinearLayout.LayoutParams.MATCH_PARENT;
        tvAttachedDayPopup=attachedToView;
        int width=attachedToView.getMeasuredWidth();
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_day_popup, null);
        RecyclerView recItem = view.findViewById(R.id.rec_item);
        recItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        attachedToView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_up_gray_24,0);

//
        dayPopupWindow = new PopupWindow(view, width, height);
        //window.setContentView(view);
        dayPopupWindow.setFocusable(true);
        dayPopupWindow.setOutsideTouchable(true);
        dayPopupWindow.setOnDismissListener(this);
        dayPopupWindow.showAsDropDown(attachedToView);
        multiDayAdapter=new MultiDayAdapter(getActivity(), dList, new MultiDayAdapter.OnChangeSelectionListener() {
            @Override
            public void onChangeSelection(int position, boolean checked) {
                multiDayAdapter.notifyItemChanged(position);
                selectedDayIdList.clear();
                selectedDayIdList=multiDayAdapter.getSelectedDay();
                //dayPopupWindow.dismiss();
            }
        });
        recItem.setAdapter(multiDayAdapter);

    }


    private void showFilterDayPopupDialog(TextView attachedToView){

        dayPopupType="filter";
        CommonMethods.hideSoftKeyboard(getActivity());
        dayList.clear();
        dayList= CommonMethods.getDayList();
        // int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int width=linDayFilter.getMeasuredWidth();
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_day_popup, null);
        RecyclerView recItem = view.findViewById(R.id.rec_item);
        recItem.setLayoutManager(new LinearLayoutManager(getActivity()));
       ivDayIcon.setRotation(Float.parseFloat("180"));

//
        filterDayPopup = new PopupWindow(view, width, height);
        //window.setContentView(view);
        filterDayPopup.setFocusable(true);
        filterDayPopup.setOutsideTouchable(true);
        filterDayPopup.setOnDismissListener(this);
        filterDayPopup.showAsDropDown(linDayFilter);
       dayFilterAdapter=new DayFilterAdapter(getActivity(),dayList, new DayFilterAdapter.OnChangeSelectionListener() {
           @Override
           public void onChangeSelection(int position, DayModel dayModel) {
               dayId=dayModel.getId();
               attachedToView.setText(dayModel.getName());
               filterDayPopup.dismiss();
               if (CommonMethods.checkReadPermission(getActivity())){
                   sendRequest(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT);
               }

           }
       });
       recItem.setAdapter(dayFilterAdapter);

    }


    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidLiveDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void init_RecyclerView() {

//        list = new ArrayList<>();
//        list.add(new Indp_TutorTimeTableModel());
//        list.add(new Indp_TutorTimeTableModel());
//        list.add(new Indp_TutorTimeTableModel());
//        list.add(new Indp_TutorTimeTableModel());

        adapter = new Indp_TutorTimeTableAdapter(getActivity(), list, new Indp_TutorTimeTableAdapter.OnClickListener() {


            @Override
            public void onSubjectClick(int postion) {

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }

            @Override
            public void onItemClick(int position) {
                String[]options=new String[]{"Update","Delete"};

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Select")
                        .setCancelable(false)
                        .setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    //update period here
                                    dialog.dismiss();
                                    updatePeriod(position);

                                }else{
                                    //delete period here
                                    dialog.dismiss();
                                    showDeleteConfirmation(position);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


            }
        });
        rec_time_table.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void showDeleteConfirmation(int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete period?")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Indp_TutorTimeTableModel model=list.get(position);
                        period_id=model.getId();
                        subject_id=model.getSubject_id();
                        dialog.dismiss();
                        //call delete api here
                        if (CommonMethods.checkInternetConnection(getActivity())){
                            sendRequest(ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR);
                        }
                    }
                }).show();
    }

    private void updatePeriod(int position) {
        Indp_TutorTimeTableModel model=list.get(position);
        period_id=model.getId();
        date=model.getDate();
        period=model.getPeroid();
        subject_id=model.getSubject_id();
        sTime=model.getStart_time();
        subject_name=model.getSubject();
        eTime=model.getEnd_time();
        selectedDayIdList=getDayArray(model.getDays());
        showAddTimeTableDailoge("update");


    }

    private ArrayList<String> getDayArray(String days) {
        ArrayList<String>list=new ArrayList<>();
        JsonParser jsonParser=new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(days);
        if(jsonArray.size()>0){
            for (int i=0;i<jsonArray.size();i++){
                list.add(jsonArray.get(i).getAsString());
            }
        }
        return list;
    }


    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      //  ((SchoolMainActivity) getActivity()).setActionBarTitle("Time Table");
        ((SchoolMainActivity) getActivity()).showHideHomeActionBar(false);
    }


    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_SUBJECT:
                params.put("teacher_id", itutor_id);
                params.put("type", "1");
                callApi(ApiCode.SCHOOL_GET_SUBJECT, params);
                break;
            case ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT:
                ArrayList<Integer>dayIdList=getSelectId(selectedDayIdList);
                params.put("teacher_id", itutor_id);
                params.put("peroid", period);
                params.put("subject_id", subject_id);
                params.put("date", date);
                params.put("days", new Gson().toJson(dayIdList));
                params.put("start_time", sTime);
                params.put("end_time", eTime);
                callApi(ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT, params);
                break;
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT:
                ArrayList<Integer>dIdList=getSelectId(selectedDayIdList);
                params.put("teacher_id", itutor_id);
                params.put("peroid", period);
                params.put("peroid_id", period_id);
                params.put("subject_id", subject_id);
               params.put("date", date);
                params.put("days", new Gson().toJson(dIdList));
                params.put("start_time", sTime);
                params.put("end_time", eTime);
                callApi(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT, params);
                break;
            case ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT:
                params.put("teacher_id", itutor_id);
               // params.put("date", f_date);
                params.put("days", dayId);
                callApi(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT, params);
                break;
            case ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR:
                params.put("teacher_id", itutor_id);
                params.put("period_id", period_id);
                params.put("subject_id", subject_id);
                callApi(ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR, params);
                break;
        }
    }
    private ArrayList<Integer> getSelectId(ArrayList<String> idList) {
        ArrayList<Integer>list=new ArrayList<>();
        for(String value:idList){
            list.add(Integer.parseInt(value));
        }
        return list;
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_GET_SUBJECT,
                        BaseUrl.URL_SCHOOL_GET_SUBJECT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SUBJECT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT:
                service.postDataVolley(ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT,
                        BaseUrl.URL_SCHOOL_ASSIGN_PERIOD_BY_IT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ASSIGN_PERIOD_BY_IT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT:
                service.postDataVolley(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT,
                        BaseUrl.URL_SCHOOL_GET_TIME_PERIOD_BY_IT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_TIME_PERIOD_BY_IT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT,
                        BaseUrl.URL_SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR,
                        BaseUrl.URL_DELETE_TIME_TABLE_BY_ITUTOR, params);
                Log.e("api", BaseUrl.URL_DELETE_TIME_TABLE_BY_ITUTOR);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_SUBJECT:
                Log.e("sc_subject", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllSubjectModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllSubjectModel>>() {
                                        }.getType());
                        subject_list.clear();
                        subject_list.add(new AllSubjectModel("",""));
                        subject_list.addAll(psearch);
                        subjectAdapter.notifyDataSetChanged();
                    } else {
                        subject_list.clear();
                        subject_list.add(new AllSubjectModel("",""));
                        subjectAdapter.notifyDataSetChanged();
                    }
                } else {
                    subject_list.clear();
                    subject_list.add(new AllSubjectModel("",""));
                    subjectAdapter.notifyDataSetChanged();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;
            case ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT:
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT:
                Log.e("sc_assign", response);
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                if (jsonObject.getBoolean("response")){
                    getAllTimePeriodDataApi();
                    sendRequest(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT);
                }
                break;
            case ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT:
                Log.e("sc_getTime", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Indp_TutorTimeTableModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Indp_TutorTimeTableModel>>() {
                                        }.getType());
                        list.clear();

                        list.addAll(psearch);
                        init_RecyclerView();
                    } else {
                        list.clear();
                        init_RecyclerView();
                    }
                } else {
                    list.clear();
                    init_RecyclerView();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;
            case ApiCode.SCHOOL_DELETE_TIME_TABLE_BY_ITUTOR:
                Log.e("delete_period", response);
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                if (jsonObject.getBoolean("response")){
                    getAllTimePeriodDataApi();
                    sendRequest(ApiCode.SCHOOL_GET_TIME_PERIOD_BY_IT);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ((SchoolMainActivity)getActivity()).onBackPressed();
                break;
        }
    }


    @Override
    public void onDismiss() {
        //on day popup listener
        if (dayPopupType.equals("filter")){
            ivDayIcon.setRotation(Float.parseFloat("360"));
        }else {
            tvAttachedDayPopup.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
        }
    }


    //--------------/
    private  class ValidateDatabase extends AsyncTask<String, Boolean, Boolean> {
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        String error="";
        String type="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            type=strings[0];
            boolean result=true;
            ArrayList<Indp_TutorTimeTableModel>tableList=new ArrayList<>();
            tableList.addAll(allTableList);

            if (type.equals("update")){
               for (int i=0;i<tableList.size();i++){
                   if (period_id.equals(  tableList.get(i).getId())) {
                       tableList.remove(i);
                       break;
                   }
               }
                Log.e("removeId",""+period_id);
                Log.e("remmovedData",""+new Gson().toJson(tableList));
            }


            if (tableList.size()>0) {
                for (int i = 0; i < tableList.size(); i++) {
                    Indp_TutorTimeTableModel model = tableList.get(i);
                    if (period.equals(model.getPeroid())) {
                        ArrayList<String> dList = getDayArray(model.getDays());
                        Log.e("dayFromDb", "" + dList);
                        Log.e("selectedDay", "" + selectedDayIdList);

                        dList.retainAll(selectedDayIdList);
                        Log.e("commonDay", "" + dList);
                        if (dList.size() > 0) {
                            error = "Day in selected day already assigned";
                            result = false;
                            break;
                        }

                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            progressDialog.dismiss();
            if (status){
                if (addDialog!=null) {
                    addDialog.dismiss();
                }
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    if (type.equals("add")) {
                        sendRequest(ApiCode.SCHOOL_ASSIGN_PERIOD_BY_IT);
                    }else if (type.equals("update")){
                        sendRequest(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_IT);
                    }
                }
            }else{
                CommonMethods.showSuccessToast(getActivity(),error);
            }
        }
    }

    //------------get all time period-------------//
    private void getAllTimePeriodDataApi() {
        String sendurl = BaseUrl.URL_SCHOOL_GET_TIME_PERIOD_BY_IT;
        HashMap<String, String> params = new HashMap<>();
        params.put("teacher_id", itutor_id);
        params.put("days", "");
        if (sendurl != null) {
            CommonMethods.postRequest(sendurl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("allTimeTable ", response);
                    try {
                            JSONObject jsonObject=new JSONObject(response);
                       // Log.e("sc_getTime", response);
                        allTableList.clear();
                        if (jsonObject.getBoolean("status")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                ArrayList<Indp_TutorTimeTableModel> psearch = new Gson().
                                        fromJson(jsonArray.toString(),
                                                new com.google.gson.reflect.TypeToken<ArrayList<Indp_TutorTimeTableModel>>() {
                                                }.getType());
                                allTableList.addAll(psearch);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // CommonMethods.showSuccessToast(getContext(), error.getMessage());
                }
            });
        } else {

           // CommonMethods.showSuccessToast(getContext(), "Something  Went Wrong");
        }


    }
    //------------------//
}
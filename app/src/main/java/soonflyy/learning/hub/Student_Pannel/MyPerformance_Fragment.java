package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyPerformance_Fragment extends Fragment implements VolleyResponseListener, OnNavigationButtonClickedListener, View.OnClickListener {

    // URL_GET_STUDENT_PERFORMANCE
    PieChart pieChart;
    SessionManagement sessionManagement;
    LinearLayout lin_days;
    String user_id = "";
    LinearLayout lin_chart;
    TextView tv_total, tv_present, tv_absent, tv_course;
    Spinner spinner;
    LinearLayout rel_spinner;
    //CalendarView calender;
    List<SubscribedCourse> courseList = new ArrayList<>();
     CustomCalendar customCalendar;
    float[] yData = {80.0f, 20.0f};
    String[] xData = {"Present", "Absent"};
    String course_id, course_name;
    int year = 0, month = 0, current_month_days = 0, total;

    String from, school_id, school_name,studentType,id;

    public MyPerformance_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_performance_, container, false);
        bindId(view);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        current_month_days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        getInttentData();
        setAbsentDate();
        setPiChart();
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
            getCourse();
        }
        tv_course.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //selectStateTV.setText(AppUtlis.StateList[position]);
                if (position>0) {
                    tv_course.setText(parent.getSelectedItem().toString());
                    course_id = courseList.get(position).getCourse_id();
                    Log.e("gg", "onItemSelected: " + courseList.get(position).getTitle());
                    getPerformance();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void setAbsentDate() {
        // Initialize description hashmap
        HashMap<Object, Property> descHashMap=new HashMap<>();

        // Initialize default property
        Property defaultProperty=new Property();

        // Initialize default resource
        defaultProperty.layoutResource=R.layout.default_date_view;

        // Initialize and assign variable
        defaultProperty.dateTextViewResource=R.id.text_view;

        // Put object and property
        descHashMap.put("default",defaultProperty);

        // for current date
//        Property currentProperty=new Property();
//        currentProperty.layoutResource=R.layout.default_date_view;
//        currentProperty.dateTextViewResource=R.id.text_view;
//        descHashMap.put("current",currentProperty);

//        // for present date
//        Property presentProperty=new Property();
//        presentProperty.layoutResource=R.layout.present_view;
//        presentProperty.dateTextViewResource=R.id.text_view;
//        descHashMap.put("present",presentProperty);

        // For absent
        Property absentProperty =new Property();
        absentProperty.layoutResource=R.layout.absent_date_view;
        absentProperty.dateTextViewResource=R.id.text_view;
        descHashMap.put("absent",absentProperty);

        // set desc hashmap on custom calendar
        customCalendar.setMapDescToProp(descHashMap);

        // Initialize date hashmap
        HashMap<Integer,Object> dateHashmap=new HashMap<>();

        // initialize calendar
        Calendar calendar=  Calendar.getInstance();

        // Put values
      //  dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");
//        dateHashmap.put(1,"absent");
//        dateHashmap.put(5,"absent");
//        dateHashmap.put(10,"absent");
        //dateHashmap.put(12,"absent");
//        dateHashmap.put(20,"absent");
//        dateHashmap.put(30,"absent");

        // set date
        customCalendar.setDate(calendar,dateHashmap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

//        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
//                // get string date
//                String sDate=selectedDate.get(Calendar.DAY_OF_MONTH)
//                        +"/" +(selectedDate.get(Calendar.MONTH)+1)
//                        +"/" + selectedDate.get(Calendar.YEAR);
//
//                // display date in toast
//                Toast.makeText(getContext(),sDate, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void getInttentData() {
        from = getArguments().getString("from");
        if (from.equals(SCHOOL_TUTOR)) {
            school_id = getArguments().getString("school_id");
            school_name = getArguments().getString("school_name");
            rel_spinner.setVisibility(View.GONE);
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_ATTENDANCE);
            }
        }else if (from.equals(SCHOOL_STUDENT)){
            studentType=getArguments().getString("student_type");
            id=getArguments().getString("id");
            rel_spinner.setVisibility(View.GONE);
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_ATTENDANCE);
            }
        }

    }

    private void getCourse() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        CommonMethods.postRequest(BaseUrl.URL_SUBSCRIBED_COURSES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("course_performannce ", response);
                JSONObject jsonObject = null;


                try {
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean("status");
                    if (res) {
                        courseList.add(new SubscribedCourse(""));
                        JSONArray array = object.getJSONArray("data");
                        if (array.length() > 0) {
                            List<SubscribedCourse> psearch = new Gson().
                                    fromJson(array.toString(),
                                            new TypeToken<List<SubscribedCourse>>() {
                                            }.getType());
//                            courseList.clear();
                            courseList.addAll(psearch);
//                            UtilSpinnerAdapter CourseSpinnerAdapter = new UtilSpinnerAdapter(getActivity(), R.layout.spinner_drop_down_single_row, R.id.name, courseList);
                            ArrayAdapter<SubscribedCourse> courseAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, courseList);
                            courseAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
                            spinner.setAdapter(courseAdapter);
                            courseAdapter.notifyDataSetChanged();
//                            if (!(courseList.size() == 0)) {
//                                getPerformance(courseList.get(0).getCourse_id());
//                            }


                        }
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "You have not subscribed any course");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonMethods.showSuccessToast(getContext(), error.getMessage());
            }
        });
    }

    public void getPerformance() {
        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", course_id);
        params.put("student_id", user_id);
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
//        params.put("course_id","5");
//        params.put("student_id","6");
//        params.put("year", "2022");
//        params.put("month", "4");
        Log.e("get_performance", ": " + params);
        CommonMethods.postRequest(BaseUrl.URL_GET_STUDENT_PERFORMANCELIST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("performance ", response);

                try {
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean("response");
                    if (res) {
                        setData(object.getJSONObject("data"));
                        lin_days.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.VISIBLE);
                        lin_chart.setVisibility(View.VISIBLE);
//                        JSONObject jsonObject = object.getJSONObject("data");
//
//
//                        tv_absent.setText("Absent : " + String.valueOf(jsonObject.getString("absentper")) + " days");
//                        tv_present.setText("Present : " + String.valueOf(jsonObject.getString("presentper")) + " days");
//                        Float total = Float.parseFloat(jsonObject.getString("absentper")) + Float.parseFloat(jsonObject.getString("presentper"));
//                        tv_total.setText("Total : " + String.valueOf(Math.round(total)) + " days");
//                        yData = new float[]{Float.parseFloat(jsonObject.getString("presentper")), Float.parseFloat(jsonObject.getString("absentper"))};
//                        addDataSet();
                    } else {
                        lin_days.setVisibility(View.GONE);
                        pieChart.setVisibility(View.GONE);
                        lin_chart.setVisibility(View.GONE);
                       // tv_total.setText("Total : " + String.valueOf(current_month_days) + "days");
                      //  CommonMethods.showSuccessToast(getContext(), "No data found");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonMethods.showSuccessToast(getContext(), error.getMessage());
            }
        });
    }

    private void setPiChart() {
        pieChart.setHoleRadius(1f);
        pieChart.setHoleColor(R.color.white);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setUsePercentValues(true);
        //pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        //addDataSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void addDataSet(float[]valueData,String [] typeData) {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for (int i = 0; i < valueData.length; i++) {
            yEntry.add(new PieEntry(valueData[i], i));
        }
        for (int i = 0; i < typeData.length; i++) {
            xEntry.add(typeData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);
        // pieDataSet.getValueLinePart1OffsetPercentage();


        ///
        ArrayList<Integer> colors = new ArrayList<>();
        if (typeData.length==2) {
            colors.add(Color.GREEN);
            colors.add(Color.RED);
        }else {
            if (typeData[0].equals("Present")){
                colors.add(Color.GREEN);
            }else if (typeData[0].equals("Absent")){
                colors.add(Color.RED);
            }
        }

        pieDataSet.setColors(colors);

        //legends to cart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        // legend.set

        //create pai data object\\
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    private void bindId(View view) {
        tv_course=view.findViewById(R.id.tv_course);
        lin_days=view.findViewById(R.id.lin_days);
        pieChart = view.findViewById(R.id.attendance_chart);
        // calendar=view.findViewById(R.id.calender);
        lin_chart = view.findViewById(R.id.lin_chart);
        spinner = view.findViewById(R.id.spinner);
       // spinner.performClick();
        rel_spinner = view.findViewById(R.id.rel_spinner);
        rel_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

        tv_total = view.findViewById(R.id.tv_total);
        tv_present = view.findViewById(R.id.tv_present);
        tv_absent = view.findViewById(R.id.tv_absent);
        customCalendar = view.findViewById(R.id.calender);
        sessionManagement = new SessionManagement(getContext());
        user_id = sessionManagement.getString(USER_ID);
//        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
            ((MainActivity) getActivity()).setStudentChildActionBar("My Performance", false);
            ((MainActivity) getActivity()).getSupportActionBar().show();
        } else if (from.equals(SCHOOL_TUTOR)||from.equals(SCHOOL_STUDENT)) {
            ((SchoolMainActivity) getActivity()).setActionBarTitle("Attendance");
        }
    }


    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_ATTENDANCE:
                if (from.equals(SCHOOL_TUTOR)) {
                    params.put("teacher_id", sessionManagement.getString(SCHOOL_TEACHER_ID));
                    params.put("type", "1");
                    params.put("school_id",school_id);
                    params.put("year",String.valueOf(year));//
                    params.put("month",String.valueOf(month));// String.valueOf(month)

                }else if (from.equals(SCHOOL_STUDENT)){
                    params.put("student_id", sessionManagement.getString(SCHOOL_STUDENT_ID));
                    params.put("type", "0");
                    if (studentType.equals("school")) {
                        params.put("key","0");
                        params.put("school_id",id);//teacher
                    }else if (studentType.equals("itutor")){
                        params.put("key","1");
                        params.put("teacher_id",id);
                    }
                    params.put("year",String.valueOf(year));//
                    params.put("month",String.valueOf(month));// String.valueOf(month)

                }
                callApi(ApiCode.SCHOOL_GET_ATTENDANCE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_ATTENDANCE:
                service.postDataVolley(ApiCode.SCHOOL_GET_ATTENDANCE,
                        BaseUrl.URL_SCHOOL_GET_ATTENDANCE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_ATTENDANCE);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_ATTENDANCE:
                Log.e("sc_attendance", response.toString());
                if (jsonObject.getBoolean("response")) {
                    setData(jsonObject.getJSONObject("data"));
                    lin_days.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.VISIBLE);
                    lin_chart.setVisibility(View.VISIBLE);
                } else {
                    lin_days.setVisibility(View.GONE);
                    pieChart.setVisibility(View.GONE);
                    lin_chart.setVisibility(View.GONE);
                  //  tv_total.setText("Total : " + String.valueOf(current_month_days) + "days");
                  //  CommonMethods.showSuccessToast(getContext(), "No data found");
                }

                break;
        }
    }

    private void setData(JSONObject data) {
        int absentDay=0;
        HashMap<Integer,Object> dateHashmap=new HashMap<>();
        Calendar calendar=  Calendar.getInstance();
        try {
            float presentData=Float.parseFloat(data.getString("presentper"));
            float absentData=Float.parseFloat(data.getString("absentper"));
            if (presentData ==0 && absentData !=0){
                addDataSet(new float[]{absentData},new String[]{"Absent"});
            }else if (presentData !=0 && absentData==0){
                addDataSet(new float[]{presentData},new String[]{"Present"});
            }else if (presentData !=0 && absentData !=0){
                addDataSet(new float[]{presentData,absentData},new String[]{"Present","Absent"});
            }

//            else {
//                yData = new float[]{Float.parseFloat(data.getString("presentper")), Float.parseFloat(data.getString("absentper"))};
//            }
           // addDataSet();
            JSONArray absentArray=data.getJSONArray("absentdate");
            absentDay=absentArray.length();
            if (absentArray.length()>0){
                dateHashmap.clear();
                for (int i=0;i<absentArray.length();i++){
                    dateHashmap.put(Integer.parseInt(absentArray.getString(i)),"absent");
                }
            }
            customCalendar.setDate(calendar,dateHashmap);
            String total=data.getString("totalday");
                    String present=data.getString("presentday");
                    String absent=data.getString("absentday");
            tv_total.setText("Total: "+ total+" Days");
            tv_present.setText("Present: "+ present+" Days");
            tv_absent.setText("Absent: "+ absent+" Days");

//            tv_present.setText("Present "+ presentDay+" Days");
//            tv_absent.setText("Absent "+ absentDay+" Days");
//            int totalDays=CommonMethods.getTotalDaysOfMonth(month,year);
//
//            int total=CommonMethods.remainDaysInMonth(month,year,totalDays);
//            int presentDay=total-absentDay;
//
//            tv_total.setText("Total "+ total+" Days");
//            tv_present.setText("Present "+ presentDay+" Days");
//            tv_absent.setText("Absent "+ absentDay+" Days");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        year=newMonth.get(Calendar.YEAR);
        month=newMonth.get(Calendar.MONTH)+1;
        Log.e("month",""+newMonth.get(Calendar.MONTH)+" "+newMonth.get(Calendar.YEAR));
        if (CommonMethods.checkInternetConnection(getActivity())) {
            if (from.equals(SCHOOL_TUTOR) || from.equals(SCHOOL_STUDENT)) {
                sendRequest(ApiCode.SCHOOL_GET_ATTENDANCE);

            }else if (from.equals(SIMPLEE_HOME_STUDENT)){
                if (!TextUtils.isEmpty(course_id)){
                    getPerformance();
                }

            }
        }
        arr[0] = new HashMap<>();
        //arr[0].put(1,"absent");
//        arr[0].put(5,"absent");
//        arr[0].put(10,"absent")
        arr[0]=null;
        return arr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_course:
                spinner.performClick();
                break;
        }
    }
}
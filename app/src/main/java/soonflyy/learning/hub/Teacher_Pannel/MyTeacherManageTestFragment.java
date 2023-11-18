package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.Scl_SelectChpterDeatailFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.adapter.TManageTestAdapter;
import soonflyy.learning.hub.model.TManageTestModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MyTeacherManageTestFragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

 RecyclerView rec_managetest;
 SwipeRefreshLayout refreshLayout;
 TManageTestAdapter adapter;
 FloatingActionButton fab_add_test;
 CardView create_test;
 ImageView iv_test;
//CardView card_create_test;
    TextView tv_duration,tv_startTime,tv_endTime,tv_date;

    EditText et_test_title;
    String test_title,sTime,eTime,date;
    int dMin=0;
  ArrayList<TManageTestModel> list=new ArrayList<>();
  String course_id,type,subject_id=" ",chapter_id=" ";
  String pageTitle;
  String from,teacher_id,class_id,section_id,school_id;
    public MyTeacherManageTestFragment() {
        // Required empty public constructor
    }

    public static MyTeacherManageTestFragment newInstance(String param1, String param2) {
        MyTeacherManageTestFragment fragment = new MyTeacherManageTestFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_manage_test, container, false);
       init_View(view);
       getArgumentData();
     //  setTest();
       initControl();
       sendApiRequest();
       refreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void sendApiRequest() {
        if(ConnectivityReceiver.isConnected()){
            if (from.equals(SCHOOL_TUTOR) || from.equals(INDEPENDENT_TUTOR)) {
                sendRequest(ApiCode.SCHOOL_GET_TEST);
            }else if (from.equals(SIMPLEE_HOME_TUTOR)){
                sendRequest(ApiCode.GET_TEST_LIST);

            }
        }else {
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        Log.e("ChapterID",chapter_id);
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            course_id = getArguments().getString("course_id");
            type = getArguments().getString("type");
            CommonMethods.changeImageViewTintColor(iv_test,0,R.color.red);
            pageTitle = getArguments().getString("course_title");
            if (type.equals("course"))
                ((Mycourse_deailFragment)getParentFragment()).showAssignProfile();
            else
                ((T_ChapterDetailFragment)getParentFragment()).showAssignProfile();


        }else if (from.equals(SCHOOL_TUTOR)){
            pageTitle = getArguments().getString("chapter_name");
            teacher_id = getArguments().getString("teacher_id");
            class_id = getArguments().getString("class_id");
            section_id = getArguments().getString("section_id");
            school_id = getArguments().getString("school_id");



        }else if (from.equals(INDEPENDENT_TUTOR)){
            pageTitle = getArguments().getString("chapter_name");
            teacher_id=new SessionManagement(getActivity()).getString(SCHOOL_IT_ID);
        }

    }

    private void setTest() {

        rec_managetest.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
         linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
       // rec_managetest.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rec_managetest.setLayoutManager(linearLayoutManager);
        rec_managetest.setKeepScreenOn(true);

        adapter = new TManageTestAdapter(getContext(), list, new TManageTestAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                TeacherTestFragment fragment = new TeacherTestFragment ();
                Bundle bundle =new Bundle();
                bundle.putString("test_id",list.get(postion).getId());
                bundle.putString("from",from);
                if (from.equals(SIMPLEE_HOME_TUTOR)) {
                    bundle.putString("course_id", course_id);
                    bundle.putString("type",type);
                    bundle.putString("subject_id",subject_id);
                    fragment.setArguments(bundle);

                    if (type.equals("course"))
                        ((Mycourse_deailFragment) getParentFragment()).SwitchFragment(fragment);
                    else
                        ((T_ChapterDetailFragment) getParentFragment()).SwitchFragment(fragment);
                }else{
                    if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR)){
                        bundle.putString("teacher_id", teacher_id);
                        bundle.putString("chapter_id", chapter_id);
                        bundle.putString("subject_id", subject_id);
                        fragment.setArguments(bundle);
                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                    }
                }
               // SwitchFragment (fragment);
            }

            @Override
            public void onSubscriptionClick(int position) {

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_managetest.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount()==0){
            fab_add_test.setVisibility(View.GONE);
            create_test.setVisibility(View.VISIBLE);
            rec_managetest.setVisibility(View.GONE);
        }else{
            create_test.setVisibility(View.GONE);
            if (from.equals(SCHOOL_TUTOR) || from.equals(INDEPENDENT_TUTOR)){//from.equals(SIMPLEE_HOME_TUTOR)
                fab_add_test.setVisibility(View.VISIBLE);
            }else{
                fab_add_test.setVisibility(View.GONE);
            }
            rec_managetest.setVisibility(View.VISIBLE);
        }



    }
    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }



//    private void SwitchFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
//        fragmentTransaction.replace(R.id.frame_layout_container, fragment, ProfileFragment.TAG);
//        //fragmentTransaction.replace(R.id.frame_layout_container, fragment);
//
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

    private void init_View(View view) {
        rec_managetest= view.findViewById(R.id.rec_managetest);
        refreshLayout=view.findViewById(R.id.swipe);
        create_test=view.findViewById(R.id.card_create_test);
        fab_add_test = view.findViewById(R.id.fab_add_test);
        iv_test=view.findViewById(R.id.img_test);
        create_test.setOnClickListener(this);
        fab_add_test.setOnClickListener(this);

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.ADD_TEST:
                String timestamp=""+System.currentTimeMillis();
                params.put("title", test_title);
                params.put("duration", String.valueOf(dMin));
                params.put("start_time", sTime);
                params.put("end_time", eTime);//endTime
                params.put("date", date);
                params.put("type", type);//chapter/course
                params.put("is_paid", "0");//String.valueOf(isPaid)
                params.put("amount", "0");//paidAmount
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                params.put("test_description", "test");
                params.put("course_id", course_id);
                params.put("test_id",timestamp);
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Courses");
                reference.child(new SessionManagement(getContext()).getString(USER_ID)).child(course_id).child("Subject")
                        .child(subject_id).child("Chapters").child(chapter_id).child("Tests").child(timestamp)
                        .setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CommonMethods.showSuccessToast(getContext(), "Test Added Successfully");
                                sendApiRequest();
                            }
                        });
                break;
            case ApiCode.GET_TEST_LIST:
              //  params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
//                if(type.equals("chapter")){
//                    params.put("type", "subject");
//                }else {
                    params.put("type", type);//course or series
                //}
                params.put("course_id", course_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                reference1.child(new SessionManagement(getContext()).getString(USER_ID)).child(course_id).child("Subject")
                        .child(subject_id).child("Chapters").child(chapter_id).child("Tests").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for(DataSnapshot ds:snapshot.getChildren()){
                                    TManageTestModel tManageTestModel=new TManageTestModel();
                                    tManageTestModel.setId(ds.child("test_id").getValue(String.class));
                                    tManageTestModel.setTitle(ds.child("title").getValue(String.class));
                                    list.add(tManageTestModel);
                                }

                                setTest();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                //------------//
                params.put("is_student","0");
                params.put("student_id","");
                //---------------//
//                callApi(ApiCode.GET_TEST_LIST, params);
                break;

            case ApiCode.SCHOOL_ADD_TEST:
                params.put("title", test_title);
                params.put("duration", String.valueOf(dMin));
                params.put("start_time", sTime);
                params.put("end_time", eTime);//endTime
                params.put("date", date);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                if(from.equals(SCHOOL_TUTOR)){
                    params.put("school_id", school_id);
                    params.put("teacher_id", teacher_id);
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("type", "0");
                }else if (from.equals(INDEPENDENT_TUTOR)){
                    params.put("section_id", "");
                    teacher_id=new SessionManagement(getActivity()).getString(SCHOOL_IT_ID);
                    params.put("teacher_id", teacher_id);
                    params.put("type", "1");
                }

                callApi(ApiCode.SCHOOL_ADD_TEST, params);
                break;
            case ApiCode.SCHOOL_GET_TEST:
                params.put("type","1");
                params.put("user_id","");
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_TEST, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_TEST_LIST:
                service.postDataVolley(ApiCode.GET_TEST_LIST,
                        BaseUrl.URL_GET_TEST_LIST, params);
                break;
            case ApiCode.ADD_TEST:
                service.postDataVolley(ApiCode.ADD_TEST,
                        BaseUrl.URL_ADD_TEST, params);
                break;
            case ApiCode.SCHOOL_GET_TEST:
                service.postDataVolley(ApiCode.SCHOOL_GET_TEST,
                        BaseUrl.URL_SCHOOL_GET_TEST, params);
                break;
            case ApiCode.SCHOOL_ADD_TEST:
                service.postDataVolley(ApiCode.SCHOOL_ADD_TEST,
                        BaseUrl.URL_SCHOOL_ADD_TEST, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);

        switch (requestType) {
            case ApiCode.GET_TEST_LIST:
            case ApiCode.SCHOOL_GET_TEST:
                Log.e("test_data ", response);
                if(jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<TManageTestModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<TManageTestModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        setTest();
                        // testAdapter.notifyDataSetChanged();
                    } else {
                        list.clear();
                        setTest();
                     //   CommonMethods.showSuccessToast(getContext(), "No Test Available");
                    }

                }
//
                break;
//            case ApiCode.SCHOOL_GET_TEST:
//                Log.e("test_data ", response);
//                if(jsonObject.getBoolean("status")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//                    if (jsonArray.length() > 0) {
//                        ArrayList<TManageTestModel> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new TypeToken<ArrayList<TManageTestModel>>() {
//                                        }.getType());
//                        list.clear();
//                        list.addAll(psearch);
//                        setTest();
//                        // testAdapter.notifyDataSetChanged();
//                    } else {
//                        list.clear();
//                        setTest();
//                        CommonMethods.showSuccessToast(getContext(), "No Test Available");
//                    }
//
//                }
////
//                break;
            case ApiCode.ADD_TEST:
            case   ApiCode.SCHOOL_ADD_TEST:
                boolean status = jsonObject.getBoolean("status");
                if (status) {
                    //Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    CommonMethods.showSuccessToast(getContext(), "Test Added Successfully");
                    sendApiRequest();
                }
                break;

        }
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_create_test:
            case R.id.fab_add_test:
                showCreatTestDialog();
                break;
        }
    }

    private void showCreatTestDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_test);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        et_test_title = dialog.findViewById(R.id.et_name);
        tv_date=dialog.findViewById(R.id.et_date);
        tv_duration=dialog.findViewById(R.id.tv_duration);
        tv_startTime=dialog.findViewById(R.id.et_time1);
        tv_endTime=dialog.findViewById(R.id.et_time2);
        LinearLayout marklayout = dialog.findViewById(R.id.mark_layout);
        marklayout.setVisibility(View.GONE);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        TextView tv_back = dialog.findViewById(R.id.tv_back);
        et_test_title.setEnabled(true);

        dialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatField()){
                    if (ConnectivityReceiver.isConnected()){
                        if (from.equals(SIMPLEE_HOME_TUTOR)) {
                            sendRequest(ApiCode.ADD_TEST);
                        }else{
                            sendRequest(ApiCode.SCHOOL_ADD_TEST);
                        }
                        dialog.dismiss();

                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDuration();
            }
        });
        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoTimePicker(1);

            }
        });
        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoTimePicker(0);

            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  chooseDate();
                showDatePicker(tv_date);

            }
        });
        dialog.setCanceledOnTouchOutside(false);
    }

    private boolean validatField() {
        test_title=et_test_title.getText().toString().trim();
        date=tv_date.getText().toString().trim();
        sTime=tv_startTime.getText().toString().trim();
        eTime=tv_endTime.getText().toString().trim();

        if(TextUtils.isEmpty(test_title)){
            et_test_title.setError("Enter title");
            et_test_title.requestFocus();
            return false;
        }
        if (dMin==0){
            CommonMethods.showSuccessToast(getContext(),"Choose duration");
            return false;
        }
        if (TextUtils.isEmpty(date)){
            CommonMethods.showSuccessToast(getContext(),"Select date");
            return false;
        }

        if (CommonMethods.isBeforeDate(date)){
            CommonMethods.showSuccessToast(getContext(),"Invalid Date");
            return  false;
        }

        if (TextUtils.isEmpty(sTime)){
            CommonMethods.showSuccessToast(getContext(),"Select start time");
            return false;
        }
        if (TextUtils.isEmpty(eTime)){
            CommonMethods.showSuccessToast(getContext(),"Select end time");
            return false;
        }

        if (!CommonMethods.isValidTime(sTime, eTime,date,dMin)){
            CommonMethods.showSuccessToast(getContext(),"Invalid time");
            return false;
        }

        return true;
    }

    private void shoTimePicker(int value) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinut = c.get(Calendar.MINUTE);
        int mSec = c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity()
                , android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        Calendar cl = Calendar.getInstance();
                        cl.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cl.set(Calendar.MINUTE, minute);
                        String time = sdf.format(cl.getTime());
                        if (value == 1) {
                            tv_startTime.setText(time);
                        } else {
                            tv_endTime.setText(time);
                        }

                    }
                }, mHour, mMinut, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (value == 1) {
            timePickerDialog.setTitle("Start Time");
        } else {
            timePickerDialog.setTitle("End Time");
        }
        timePickerDialog.show();
    }
    private void chooseDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());
                tv_date.setText(date);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
    private  void showDatePicker(TextView tvView){
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
                String d=tvView.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tvView.setText("");
                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"You can't select a date earlier than the current date");
                    }
                }
            }

        });
        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }
    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidLiveDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void chooseDuration() {
        View view = getLayoutInflater().inflate(R.layout.dialog_test_duration_chooser, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText min = view.findViewById(R.id.min_edit);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView ok = view.findViewById(R.id.tv_ok);
        cancel.setOnClickListener(v12 -> dialog.dismiss());
        ok.setOnClickListener(v1 -> {
            String m = min.getText().toString().trim();
            if (TextUtils.isEmpty(m)) {
                min.setError("Invalid");
                min.requestFocus();
            } else {
                dMin = Integer.parseInt(m);
                if (dMin > 0) {
                    if (m.length() == 1) {
                        m = "0" + m;
                    }
                    tv_duration.setText(m + " Min");
                    dialog.dismiss();
                } else {
                    min.setError("Invalid");
                    min.requestFocus();
                }
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);
            if (type.equals("course"))
                ((Mycourse_deailFragment) getParentFragment()).setTestColor();
            else
                ((T_ChapterDetailFragment) getParentFragment()).setTestBgColor();
        }else if (from.equals(SCHOOL_TUTOR) || from.equals(INDEPENDENT_TUTOR)){
            ((Scl_SelectChpterDeatailFragment)getParentFragment()).setTestBgColor();
        }

    }
}
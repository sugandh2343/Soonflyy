package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.CreateTestAdapter;
import soonflyy.learning.hub.model.CreateTestModel;
import soonflyy.learning.hub.model.Question;
import soonflyy.learning.hub.studentModel.TestQuestionModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class  TeacherTestFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    float dX;
    float dY;
    int lastAction;

    NestedScrollView nestedScrollView;
    RecyclerView rec_main;
    SwipeRefreshLayout refreshLayout;
    TextView create_test_btn;
    CreateTestAdapter testAdapter;
    ImageView arrow_back_img;
    FloatingActionButton addQuestion_btn;

    EditText et_test_title;
    TextView tv_duration,tv_startTime,tv_endTime,tv_date;

    TextView tv_title;
    int dMin;

    ArrayList<CreateTestModel> tlist = new ArrayList<>();
    ImageButton add_Question_btn;
    SessionManagement session_management;
    ArrayList<CreateTestModel> testmodel;


    ArrayList<TestQuestionModel> quesArrayList = new ArrayList<>();
    ArrayList<String> removeIdList=new ArrayList<>();
    int totalMark=0;


    String answer;

    String user_id;
    String test_id;
    String type, pageTitle;//,passingMark
    String  courseId;
    String total_mark, passing_mark,test_title,duration,sTime,eTime,date;


    private Question questionModel;
    int preMarkValue = 0;
    private boolean isCheckedEmptyField = true;

    String teacher_id,subject_id,chapter_id,from;

    public TeacherTestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_test, container, false);
        initView(view);
        getArgumentData();
        setQuestionToList();
        sendApiRequest();
       //
        // sendRequestCreateTest(ApiCode.GET_TEST_PAPER, null);
        return view;
    }

    private void getArgumentData() {
       from=getArguments().getString("from");
        test_id = getArguments().getString("test_id");
       if (from.equals(SIMPLEE_HOME_TUTOR)) {
           courseId = getArguments().getString("course_id");
           subject_id = getArguments().getString("subject_id");
           String type=getArguments().getString("type");
           if(type.equals("course")) {
               ((Mycourse_deailFragment) getParentFragment()).hideAssignProfile();
           }
           else {
               ((T_ChapterDetailFragment) getParentFragment()).hideAssignProfile();
           }

       }else if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR)){
           teacher_id=getArguments().getString("teacher_id");
           chapter_id=getArguments().getString("chapter_id");
           subject_id=getArguments().getString("subject_id");
       }
       Log.e("HGGfvghv","1::"+test_id);
       Log.e("HGGfvghv","2::"+courseId);
       Log.e("HGGfvghv","3::"+subject_id);


    }

    private void initView(View view) {
        testmodel = new ArrayList<>();
        nestedScrollView=view.findViewById(R.id.scroll_nested);
        refreshLayout=view.findViewById(R.id.refresh_layout);
        rec_main = view.findViewById(R.id.rec_main);
        et_test_title=view.findViewById(R.id.et_name);
        tv_duration=view.findViewById(R.id.et_duration);
        tv_date=view.findViewById(R.id.et_date);
        tv_startTime=view.findViewById(R.id.et_time1);
        tv_endTime=view.findViewById(R.id.et_time2);
        create_test_btn = view.findViewById(R.id.create_test_btn);
        create_test_btn.setOnClickListener(this);
        tv_title = view.findViewById(R.id.tv_title);
        addQuestion_btn = view.findViewById(R.id.add_new);
       // addQuestion_btn.setOnTouchListener(this);
        addQuestion_btn.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        tv_duration.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);

        //  add_Question_btn = view.findViewById(R.id.add_new_btn);
        arrow_back_img = view.findViewById(R.id.arrow_back_img);
        //et_passing_mark=view.findViewById(R.id.passing_mark_edit);
        // lin_passing_mark=view.findViewById(R.id.linearLayout);
        // hz_line=view.findViewById(R.id.divider);

        session_management = new SessionManagement(getContext());

        rec_main.setLayoutManager(new LinearLayoutManager(getContext()));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_test_btn:
                if (validateField()){
                    Log.e("question",""+new Gson().toJson(quesArrayList));
                    show_button_dailoge();
                }


                break;
            case R.id.arrow_back_img:
                //finish ();
                break;


            case R.id.et_duration:
                chooseDuration();
                break;
            case R.id.et_date:
                showDatePicker(tv_date);
             //   chooseDate();
                break;
            case R.id.et_time1:
                shoTimePicker(1);
                break;
            case R.id.et_time2:
                shoTimePicker(0);
                break;
            case R.id.add_new:
                addNewQuestion();
                break;



        }

    }
    private void addNewQuestion(){
        TestQuestionModel model = new TestQuestionModel();
        model.setAnswer("-1");
        model.setSelected_option("-1");
        model.setOptions(new String[]{"","","",""});
        quesArrayList.add(model);
        testAdapter.notifyDataSetChanged();
        // testAdapter.notifyItemInserted(questionList.size() - 1);//tlist
        Log.e("size ", "" + quesArrayList.size());
        // rec_main.smoothScrollToPosition(quesArrayList.size() - 1);
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private boolean validateField() {
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
        if (quesArrayList.size()==0){
            CommonMethods.showSuccessToast(getContext(),"Create question");
            return false;
        }
        if (!setquestionList()){
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
//                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"You can't select a date earlier than the current date");
                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
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



    private void show_button_dailoge() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_test);
        dialog.getWindow();
        dialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        TextView tv_test_date=dialog.findViewById(R.id.et_date);
        TextView tv_test_duration=dialog.findViewById(R.id.tv_duration);
        TextView tv_sTime=dialog.findViewById(R.id.et_time1);
        TextView tv_etime=dialog.findViewById(R.id.et_time2);
        TextView tv_total_mark = dialog.findViewById(R.id.tv_total_mark);
        EditText et_pasing_mark = dialog.findViewById(R.id.et_passing);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        TextView tv_back = dialog.findViewById(R.id.tv_back);



        et_name.setText(et_test_title.getText().toString().trim());
        tv_test_duration.setText(tv_duration.getText().toString().trim());
        tv_test_date.setText(tv_date.getText().toString().trim());
        tv_sTime.setText(tv_startTime.getText().toString().trim());
        tv_etime.setText(tv_endTime.getText().toString().trim());
        tv_total_mark.setText(String.valueOf(totalMark));
        if (passing_mark !=null) {
            if (!passing_mark.equals("null")) {
                et_pasing_mark.setText(passing_mark);
            }
        }

        btn_save.setText("Create");

        dialog.show();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        et_pasing_mark.requestFocus();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passing_mark=et_pasing_mark.getText().toString().trim();
                if (TextUtils.isEmpty(passing_mark)){
                    et_pasing_mark.setError("Enter passing mark");
                    et_pasing_mark.requestFocus();
                }else if(Integer.parseInt(tv_total_mark.getText().toString())<Integer.parseInt(passing_mark)||
                        Integer.parseInt(passing_mark)==0){
                    et_pasing_mark.setError("Invalid passing mark");
                    et_pasing_mark.requestFocus();
                }else{
                   total_mark=String.valueOf(totalMark);
                    //call api
                    if (ConnectivityReceiver.isConnected()){
                        if (from.equals(SIMPLEE_HOME_TUTOR)) {
                            sendRequestCreateTest(ApiCode.UPDATE_TEST, null);
                        }else if (from.equals(SCHOOL_TUTOR)|| from.equals(INDEPENDENT_TUTOR)){
                            sendRequestCreateTest(ApiCode.SCHOOL_UPDATE_TEST, null);
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

        dialog.setCanceledOnTouchOutside(false);
    }

    private boolean setquestionList() {
        isCheckedEmptyField=true;
        totalMark=0;

        Log.e("question_size ", "" + testAdapter.getItemCount());//testAdapter.getItemCount())
       // testmodel.clear();
        for (int i = 0; i < testAdapter.getItemCount(); i++) {
            Log.e("qpostion", "" + i);
            RecyclerView.ViewHolder viewHolder = rec_main.findViewHolderForAdapterPosition(i);
            if (viewHolder == null) {
                viewHolder = testAdapter.holderHasMap.get(i);
            }
            //  assert viewHolder != null;
            View view = viewHolder.itemView;
            TextView answerkey = view.findViewById(R.id.answer_key_tv);
            EditText quetion = view.findViewById(R.id.question_edit);
            EditText marks = view.findViewById(R.id.mark_edit);
            EditText option1 = view.findViewById(R.id.option_1_edit);
            EditText option2 = view.findViewById(R.id.option_2_edit);
            EditText option3 = view.findViewById(R.id.option_3_edit);
            EditText option4 = view.findViewById(R.id.option_4_edit);
            RadioButton radio_1 = view.findViewById(R.id.option_1_radio_btn);
            RadioButton radio_2 = view.findViewById(R.id.option_2_radio_btn);
            RadioButton radio_3 = view.findViewById(R.id.option_3_radio_btn);
            RadioButton radio_4 = view.findViewById(R.id.option_4_radio_btn);
            //EditText description = view.findViewById(R.id.description_edit);

            if (TextUtils.isEmpty(marks.getText().toString())) {
                marks.setError("Enter mark");
                marks.requestFocus();
                isCheckedEmptyField = false;
                break;
            } else if (TextUtils.isEmpty(quetion.getText().toString())) {
                quetion.setError("Enter question");
                quetion.requestFocus();
                isCheckedEmptyField = false;
                break;
            }
            else if (TextUtils.isEmpty(option1.getText().toString())) {
                option1.setError("Enter option 1");
                option1.requestFocus();
                isCheckedEmptyField = false;
                break;
            }
            else if (TextUtils.isEmpty(option2.getText().toString())) {
                option2.setError("Enter option 2");
                option2.requestFocus();
                isCheckedEmptyField = false;
                break;
            }
            else if (TextUtils.isEmpty(option3.getText().toString())) {
                option3.setError("Enter option 3");
                option3.requestFocus();
                isCheckedEmptyField = false;
                break;
            }
            else if (TextUtils.isEmpty(option4.getText().toString())) {
                option4.setError("Enter option 4");
                option4.requestFocus();
                isCheckedEmptyField = false;
                break;
            }
//            else if (TextUtils.isEmpty(description.getText().toString())) {
//                description.setError("Enter Comment");
//                description.requestFocus();
//                isCheckedEmptyField = false;
//                break;
           // }
            else if (TextUtils.isEmpty(answerkey.getText().toString().trim())) {
                DynamicToast.make(getContext(), "select answer", 2000).show();
                rec_main.scrollToPosition(i);
                isCheckedEmptyField = false;
                break;
            } else {
                if (radio_1.isChecked()) {
                    answer = "0";

                } else if (radio_2.isChecked()) {
                    answer = "1";

                } else if (radio_3.isChecked()) {
                    answer = "2";

                } else if (radio_4.isChecked()) {
                    answer = "3";

                }
                String[] option = {option1.getText().toString().trim(), option2.getText().toString().trim(),
                        option3.getText().toString().trim(), option4.getText().toString().trim()};
                TestQuestionModel model=quesArrayList.get(i);
                model.setTitle(quetion.getText().toString().trim());
                model.setMarks(marks.getText().toString().trim());
                model.setOptions(option);
                if (model.getId()==null){
                    model.setId(" ");
                }
                model.setDetails("");
                model.setAnswer(answer);
                totalMark=totalMark+Integer.parseInt(marks.getText().toString().trim());

//                testmodel.add(new TestQuestionModel(quetion.getText().toString().trim(),
//                        marks.getText().toString().trim(), option, answer));//description.getText().toString().trim()answerkey.getText().toString().trim()
//                Log.e("test question", quetion.getText().toString().trim());
//                isCheckedEmptyField = true;
                // Toast.makeText(this, ""+qNo.getText().toString(), Toast.LENGTH_SHORT).s
            }
        }
       // return testmodel;
        Log.e("data",new Gson().toJson(quesArrayList));
        return isCheckedEmptyField;
    }




    private void sendRequestCreateTest(int request, ArrayList<CreateTestModel> questionList) {

        switch (request) {
            case ApiCode.GET_TEST_PAPER:
                HashMap<String, String> prms = new HashMap<>();
                // params.put("type","0");

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
                reference.child(new SessionManagement(getContext()).getString(USER_ID)).child(courseId).child("Subject")
                        .child(subject_id).child("Chapters").child(chapter_id).child("Tests")
                        .child(test_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                et_test_title.setText(snapshot.child("title").getValue(String.class));
                                tv_duration.setText(snapshot.child("duration").getValue(String.class));
                                tv_date.setText(snapshot.child("date").getValue(String.class));
                                tv_startTime.setText(snapshot.child("start_time").getValue(String.class));
                                tv_endTime.setText(snapshot.child("end_time").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                break;
            case ApiCode.UPDATE_TEST:
                HashMap<String, String> pr = new HashMap<>();
                // params.put("type","0");
                pr.put("test_id", test_id);
                pr.put("remove_id", new Gson().toJson(removeIdList));//remove id in array
                pr.put("title", test_title);
                pr.put("duration", String.valueOf(dMin));
                pr.put("start_time", sTime);
                pr.put("end_time", eTime);
                pr.put("date", date);
                pr.put("course_id", courseId);
                pr.put("passing_mark", passing_mark);
                pr.put("total_mark", String.valueOf(totalMark));
                pr.put("data", new Gson().toJson(quesArrayList));
                //pr.put("test_id", test_id);
                 pr.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.UPDATE_TEST, pr);
                break;

            case ApiCode.SCHOOL_GET_TEST_DETAILS:
                HashMap<String, String> sprm = new HashMap<>();
                sprm.put("test_id", test_id);
                sprm.put("user_id","");
//                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
//                reference.child(new SessionManagement(getContext()).getString(USER_ID)).child(courseId).child("Subject")
//                        .child(subject_id).child("Chapters").child(chapter_id).child("Tests")
//                        .child(test_id).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                et_test_title.setText(snapshot.child("title").getValue(String.class));
//                                tv_duration.setText(snapshot.child("duration").getValue(String.class));
//                                tv_date.setText(snapshot.child("date").getValue(String.class));
//                                tv_startTime.setText(snapshot.child("start_time").getValue(String.class));
//                                tv_endTime.setText(snapshot.child("end_time").getValue(String.class));
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                break;
            case ApiCode.SCHOOL_UPDATE_TEST:
                HashMap<String, String> spr = new HashMap<>();
                // params.put("type","0");
                spr.put("test_id", test_id);
                spr.put("teacher_id", teacher_id);
                spr.put("chapter_id", chapter_id);
                spr.put("subject_id", subject_id);
                spr.put("remove_id", new Gson().toJson(removeIdList));//remove id in array
                spr.put("title", test_title);
                spr.put("duration", String.valueOf(dMin));
                spr.put("start_time", sTime);
                spr.put("end_time", eTime);
                spr.put("date", date);
               spr.put("passing_mark", passing_mark);
                spr.put("total_mark", String.valueOf(totalMark));
                spr.put("data", new Gson().toJson(quesArrayList));
                //pr.put("test_id", test_id);
                spr.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.SCHOOL_UPDATE_TEST, spr);
                break;

            case ApiCode.CREATE_TEST:
                HashMap<String, String> params = new HashMap<>();
                Gson gson = new Gson();
                String data = "";//gson.toJson(setquestionList());

                Log.e("data ", data);
                params.put("data", data);
                params.put("user_id", user_id);
                params.put("test_id", test_id);
                // params.put("passing_mark", "passingMark");
                callApi(ApiCode.CREATE_TEST, params);
                //  }
                //}
                break;
            case ApiCode.UPDATE_QUESTION:
                int passingMark = getArguments().getInt("passingMark");
                int totalMark = getArguments().getInt("totalMark");

                Log.e("passing", String.valueOf(passingMark));
                Log.e("total", String.valueOf(totalMark));
                HashMap<String, String> prm = new HashMap<>();
                CreateTestModel question = questionList.get(0);
                int mark = Integer.parseInt(question.getMarks());

                prm.put("title", question.getTitle());
                JSONArray jsonArray = new JSONArray(Arrays.asList(question.getOptions()));
                prm.put("options", jsonArray.toString());
                prm.put("answer", question.getAnswer());
                prm.put("details", question.getDetails());
                prm.put("mark", question.getMarks());
                prm.put("question_id", questionModel.getId());
                prm.put("test_id", test_id);


                if (questionModel.isIs_selected()) {

                    Log.e("premark", String.valueOf(preMarkValue));
                    Log.e("mark", String.valueOf(mark));
                    if (mark == preMarkValue) {
                        prm.put("passing_mark", String.valueOf(passingMark));
                        prm.put("total_mark", String.valueOf(totalMark));
                        callApi(ApiCode.UPDATE_QUESTION, prm);
                    } else {
                        int diff = passingMark - mark;
                        Log.e("diff", String.valueOf(diff));
                        int newTotal = totalMark + diff;
                        Log.e("new total", String.valueOf(newTotal));
                        if (newTotal >= passingMark) {
                            prm.put("passing_mark", String.valueOf(passingMark));
                            prm.put("total_mark", String.valueOf(newTotal));
                            callApi(ApiCode.UPDATE_QUESTION, prm);
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "Invalid Question Mark");
                        }
                    }
                } else {
                    prm.put("passing_mark", String.valueOf(passingMark));
                    prm.put("total_mark", String.valueOf(totalMark));
                    callApi(ApiCode.UPDATE_QUESTION, prm);
                }

                break;
//
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback =  this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {

            case ApiCode.GET_TEST_PAPER:
                service.postDataVolley(ApiCode.GET_TEST_PAPER,
                        BaseUrl.URL_GET_TEST_PAPER, params);
                break;
            case ApiCode.UPDATE_TEST:
                service.postDataVolley(ApiCode.UPDATE_TEST,
                        BaseUrl.URL_UPDATE_TEST, params);
                break;

            case ApiCode.SCHOOL_GET_TEST_DETAILS:
            service.postDataVolley(ApiCode.SCHOOL_GET_TEST_DETAILS,
                    BaseUrl.URL_SCHOOL_GET_TEST_DETAILS, params);
            break;
            case ApiCode.SCHOOL_UPDATE_TEST:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_TEST,
                        BaseUrl.URL_SCHOOL_UPDATE_TEST, params);
                break;

            case ApiCode.CREATE_TEST:
                service.postDataVolley(ApiCode.CREATE_TEST,
                        BaseUrl.URL_CREATE_TEST, params);
                break;
            case ApiCode.UPDATE_QUESTION:
                service.postDataVolley(ApiCode.UPDATE_QUESTION,
                        BaseUrl.URL_UPDATE_QUESTION, params);
                break;
            case ApiCode.ADD_QUIZ_QUESTION:
                service.postDataVolley(ApiCode.ADD_QUIZ_QUESTION,
                        BaseUrl.URL_ADD_QUIZ_QUESTION, params);
                break;
            case ApiCode.UPDATE_QUIZ_QUESTION:
                service.postDataVolley(ApiCode.UPDATE_QUIZ_QUESTION,
                        BaseUrl.URL_UPDATE_QUIZ_QUESTION, params);
                break;

        }
    }

    public void onResponse(int requestType, String response) throws JSONException {

        switch (requestType) {
            case ApiCode.CREATE_TEST:

                Log.d("qution_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        // Toast.makeText(this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        CommonMethods.showSuccessToast(getContext(), "Added Successfully");
                        if (type.equals("addMore")) {
                            getActivity().onBackPressed();
                        } else {
//                            ChooseQuestionFragement courseDetails = new ChooseQuestionFragement();
//                            Bundle args = new Bundle();
//                            args.putString("test_id", test_id);
//                            args.putString("user_id", user_id);
//                            courseDetails.setArguments(args);
//                            SwitchFragment(courseDetails);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.UPDATE_QUESTION:
            case ApiCode.ADD_QUIZ_QUESTION:
            case ApiCode.UPDATE_QUIZ_QUESTION:
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("status")) {
                        if (requestType == ApiCode.ADD_QUIZ_QUESTION) {
                            CommonMethods.showSuccessToast(getContext(), "Quiz Added Successfully");
                            getActivity().onBackPressed();
                        }
                        if (requestType == ApiCode.UPDATE_QUESTION) {
                            CommonMethods.showSuccessToast(getContext(), "Question Updated Successfully");
                            getActivity().onBackPressed();
                        }
                        if (requestType == ApiCode.UPDATE_QUIZ_QUESTION) {
                            CommonMethods.showSuccessToast(getContext(), "Question Updated Successfully");
                            getActivity().onBackPressed();
                        }


                        // getActivity().onBackPressed();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.UPDATE_TEST:
            case ApiCode.SCHOOL_UPDATE_TEST:
                Log.e("update", response);
                JSONObject ob = new JSONObject(response);
                if (ob.getBoolean("status")) {
                    dMin=0;
                    totalMark=0;
                    passing_mark="0";
                    CommonMethods.showSuccessToast(getContext(),"Test Updated Successfully");
                    sendApiRequest();
                }else{
                    CommonMethods.showSuccessToast(getContext(),ob.getString("message"));
                    sendApiRequest();
                }
                break;

            case ApiCode.GET_TEST_PAPER:
            case ApiCode.SCHOOL_GET_TEST_DETAILS:
                Log.e("test_question", response);
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
//                    int dtime = Integer.parseInt(object.getJSONObject("data").getString("duration"));
//
//                    total_mark = object.getJSONObject("data").getString("total_mark");
//                    passing_mark = object.getJSONObject("data").getString("passing_mark");
                    JSONArray jsonArray = (object.getJSONObject("data")).getJSONArray("questions");
                    setViewData(object.getJSONObject("data"));
                    if (jsonArray.length() > 0) {
                        List<TestQuestionModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<TestQuestionModel>>() {
                                        }.getType());
                        quesArrayList.clear();
                        quesArrayList.addAll(psearch);
                        setQuestionToList();
                        setQuestionListField(quesArrayList);

//                        startTimer();

                    }

                }
                break;



        }
    }

    private void setViewData(JSONObject data) {
        try {
            test_title=data.getString("title");
            duration=data.getString("duration");
            dMin=Integer.parseInt(duration);
            sTime=data.getString("start_time");
            eTime=data.getString("end_time");
            date=data.getString("date");
            total_mark=data.getString("total_mark");
            passing_mark=data.getString("passing_mark");

            et_test_title.setText(test_title);
            tv_duration.setText(duration+" min");
            tv_date.setText(date);
            tv_startTime.setText(sTime);
            tv_endTime.setText(eTime);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setQuestionToList() {

        testAdapter = new CreateTestAdapter(getContext(), quesArrayList, quesArrayList.size(), "test", new CreateTestAdapter.OnSelectAnswerListener() {
            @Override
            public void onAnswerSelect(String option) {

            }

            @Override
            public void onRemoveQuestion(int position, String id) {
                if (!TextUtils.isEmpty(id)){
                    removeIdList.add(id);
                }
//                quesArrayList.remove(position);
//
                testAdapter.notifyDataSetChanged();
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }
        });
        rec_main.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        Log.e("araysize"," "+quesArrayList.size());
    }

    public void setQuestionListField(List<TestQuestionModel> questionModelList) {
        for (int i = 0; i < questionModelList.size(); i++) {
            TestQuestionModel model = questionModelList.get(i);
            model.setSelected_option("-1");
            // total_mark=total_mark+Integer.parseInt(model.getMarks());
        }
        testAdapter.notifyDataSetChanged();

    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
      //  fragmentTransaction.replace(R.id.container_layout, fragment, TeacherProfileFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
       // ((TeacherMainActivity) getActivity()).setChildActionBar(pageTitle);
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity) getActivity()).setTeacherActionBar("Create Test", false);
        }

    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    TestQuestionModel model = new TestQuestionModel();
                    model.setAnswer("-1");
                    model.setSelected_option("-1");
                    model.setOptions(new String[]{"","","",""});
                    quesArrayList.add(model);
                    testAdapter.notifyDataSetChanged();
                    // testAdapter.notifyItemInserted(questionList.size() - 1);//tlist
                    Log.e("size ", "" + quesArrayList.size());
                  // rec_main.smoothScrollToPosition(quesArrayList.size() - 1);
                    nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;

            default:
                return false;
        }
        return true;

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }

    private void sendApiRequest() {
        if (ConnectivityReceiver.isConnected()){
            if (from.equals(SIMPLEE_HOME_TUTOR))
            sendRequestCreateTest(ApiCode.GET_TEST_PAPER,null);
            else if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR))
                sendRequestCreateTest(ApiCode.SCHOOL_GET_TEST_DETAILS,null);

        }else {
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }
}
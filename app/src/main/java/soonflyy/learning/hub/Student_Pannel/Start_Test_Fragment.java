package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.TestWithMultiChoiceAdapter;
import soonflyy.learning.hub.studentModel.TestQuestionModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Start_Test_Fragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    RecyclerView rv_question;
    TextView tv_title,tv_date,tv_duration, tv_remain_time;
    TextView tvTotalQuest,tvExamDuration,tvTotalMark,tvPassingMark;
    Button btn_submit;
    ImageView iv_backIcon;
    LinearLayout lin_timer;
    RelativeLayout relTestInfo;

    private List<TestQuestionModel> questionList = new ArrayList<>();
    TestWithMultiChoiceAdapter testAdapter;

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis ;
    private boolean mTimerRunning;
    String test_id,total_mark,passing_mark,pageTitle,from;

    View assignToTestInfo;


    public Start_Test_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_start__test_, container, false);
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bindIdView(view);
        test_id=getArguments().getString("test_id");
        from=getArguments().getString("from");
        setView();
        if (ConnectivityReceiver.isConnected()){
            if (from.equals(SCHOOL_COACHING)|| from.equals(SCHOOL_STUDENT)){
                sendRequest(  ApiCode.SCHOOL_GET_TEST_DETAILS);
            }else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_TEST_PAPER);
            }
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
       // setQuestion();
        btn_submit.setOnClickListener(this);
        iv_backIcon.setOnClickListener(this);
        return  view;
    }

    private void setView() {
        if (from.equals(SCHOOL_COACHING)||from.equals(SIMPLEE_HOME_TUTOR)){
            relTestInfo.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            if (from.equals(SIMPLEE_HOME_TUTOR)){
                assignToTestInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setQuestion() {
       // questionList.add(new TestQuestionModel());
        //questionList.add(new TestQuestionModel());
        testAdapter = new TestWithMultiChoiceAdapter(getContext(), questionList);
        LinearLayoutManager lm = new LinearLayoutManager(requireActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_question.setLayoutManager(lm);
        rv_question.setAdapter(testAdapter);
       // startTimer();

    }

    private void bindIdView(View view) {
        btn_submit = view.findViewById(R.id.btn_submit);
        rv_question = view.findViewById(R.id.rv_question);
        tv_duration = view.findViewById(R.id.exam_duration_tv);
        tv_remain_time = view.findViewById(R.id.remain_time);
        tv_title = view.findViewById(R.id.tv_title_test);
        tv_date = view.findViewById(R.id.tv_test_date);
        iv_backIcon = view.findViewById(R.id.iv_back);
        lin_timer = view.findViewById(R.id.lin_timer);
        relTestInfo=view.findViewById(R.id.relativeLayout4);




        //---------//
        assignToTestInfo=view.findViewById(R.id.assign_mark_details);
        tvTotalQuest = view.findViewById(R.id.tv_total_qstn);
        tvExamDuration = view.findViewById(R.id.tv_exam_duration);
        tvTotalMark = view.findViewById(R.id.total_mark);
        tvPassingMark = view.findViewById(R.id.passing_score);

    }

    private void startTimer() {
        mTimerRunning = true;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer.start();

        } else {
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();

                }

                @Override
                public void onFinish() {
                    //Toast.makeText(getContext(), "time over", Toast.LENGTH_SHORT).show();
                    CommonMethods.showSuccessToast(getContext(),"Time Over");
                    mTimerRunning = false;
                    tv_remain_time.setTextColor(ContextCompat.getColor(getContext(),R.color.light_black));
                    updateCountDownText();
                    mCountDownTimer.cancel();
                    if (ConnectivityReceiver.isConnected()){
                        sendRequest(ApiCode.SUBMIT_TEST);
                    }
                }
            }.start();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tv_remain_time.setText(timeLeftFormatted);
        if (timeLeftFormatted.equalsIgnoreCase("00:10")) {
            tv_remain_time.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                if (!from.equals(SCHOOL_COACHING) && !from.equals(SIMPLEE_HOME_TUTOR)){
                    gotoBack();
                }else{
                    backMedhod();
                }

                break;
            case R.id.btn_submit:
                submitTest();
                break;

        }
    }

    private void gotoBack() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert")
                .setMessage("Are you sure to exit?")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectivityReceiver.isConnected()){
                            sendRequest(ApiCode.SUBMIT_TEST);
                            backMedhod();
                            dialog.dismiss();
                        }

                    }
                }).show();


    }

    private void backMedhod() {
        if (from.equals(SCHOOL_COACHING)|| from.equals(SCHOOL_STUDENT)){
            ((SchoolMainActivity)getActivity()).onBackPressed();
        }else{
            if (from.equals(SIMPLEE_HOME_STUDENT))
                ((MainActivity)getActivity()).onBackPressed();
            else  if (from.equals(SIMPLEE_HOME_TUTOR)){
                //--for assigned tutor--//
                ((TeacherMainActivity)getActivity()).onBackPressed();
            }
        }
    }

    private void submitTest() {
        if (mTimerRunning) {
            //show alert
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Alert")
                    .setMessage("Are you sure to submit test !")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ConnectivityReceiver.isConnected()){
                                sendRequest(ApiCode.SUBMIT_TEST);
                            }

                        }
                    }).show();
        }
    }

    private void sendRequest(int request) {
        SessionManagement sMgt=new SessionManagement(getActivity());
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_TEST_PAPER:
               // params.put("type","0");
                if (from.equals(SIMPLEE_HOME_STUDENT)){
                    params.put("user_id",sMgt.getString(USER_ID));
                }else{
                    params.put("user_id","");
                }
                params.put("test_id",test_id);
               // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_TEST_PAPER, params);
                break;
            case ApiCode.SCHOOL_GET_TEST_DETAILS:
                HashMap<String, String> sprm = new HashMap<>();
                if (from.equals(SCHOOL_STUDENT)){
                    sprm.put("user_id",sMgt.getString(SCHOOL_STUDENT_ID));
                }else{
                    sprm.put("user_id","");
                }
                sprm.put("test_id", test_id);
                callApi(ApiCode.SCHOOL_GET_TEST_DETAILS, sprm);

                break;
            case ApiCode.SUBMIT_TEST:
                String data=getDataForSubmit();
                Log.e("data",data);
                params.put("test_id", test_id);
                if (from.equals(SCHOOL_STUDENT)){
                    params.put("user_id", new SessionManagement(getContext()).getString(SCHOOL_STUDENT_ID));
                }else {
                    params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                }
                params.put("type", "course");//series
                params.put("total_mark", String.valueOf(total_mark));
                params.put("passing_mark", String.valueOf(passing_mark));
                params.put("data", data);
                callApi(ApiCode.SUBMIT_TEST, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_TEST_PAPER:
                service.postDataVolley(ApiCode.GET_TEST_PAPER,
                        BaseUrl.URL_GET_TEST_PAPER, params);
                break;
            case ApiCode.SUBMIT_TEST:
                service.postDataVolley(ApiCode.SUBMIT_TEST,
                        BaseUrl.URL_SUBMIT_TEST, params);
                break;
            case ApiCode.SCHOOL_GET_TEST_DETAILS:
                service.postDataVolley(ApiCode.SCHOOL_GET_TEST_DETAILS,
                        BaseUrl.URL_SCHOOL_GET_TEST_DETAILS, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject object=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_TEST_PAPER:
            case ApiCode.SCHOOL_GET_TEST_DETAILS:
                Log.e("test_question",response);
                if (object.getBoolean("status")) {
                    String attempt=object.getJSONObject("data").getString("attempted");

                    if (!TextUtils.isEmpty(attempt)) {

                        if (attempt.equals("1") && !from.equals(SCHOOL_COACHING)&& !from.equals(SIMPLEE_HOME_TUTOR)){
                            gotToResultPage();
                        }else{
                            int dtime= Integer.parseInt(object.getJSONObject("data").getString("duration"));
                            mTimeLeftInMillis=(dtime*60)*1000;
                            tv_duration.setText(dtime+" Min");
                            setTimerTime(object.getJSONObject("data").getString("end_time"));
                            total_mark=object.getJSONObject("data").getString("total_mark");
                            passing_mark=object.getJSONObject("data").getString("passing_mark");
                            pageTitle=object.getJSONObject("data").getString("title");
                            tv_title.setText("Test On "+pageTitle);
                            tv_date.setText("Date: "+object.getJSONObject("data").getString("date"));
                            JSONArray jsonArray = (object.getJSONObject("data")).getJSONArray("questions");
                            if (jsonArray.length()>0) {
                                List<TestQuestionModel> psearch = new Gson().
                                        fromJson(jsonArray.toString(),
                                                new TypeToken<List<TestQuestionModel>>() {
                                                }.getType());
                                questionList.clear();
                                questionList.addAll(psearch);
                                Log.e("qsize",""+questionList.size());
                                setQuestionListField();
                                setQuestion();
                                if (!from.equals(SCHOOL_COACHING) && !from.equals(SIMPLEE_HOME_TUTOR)) {
                                    startTimer();
                                }
                                //--for assign to home tutor data--------//
                                tvTotalQuest.setText("Total Questions : "+questionList.size());
                                tvExamDuration.setText("Exam Duration : "+dtime+" Min");
                                tvTotalMark.setText("Total Marks : "+total_mark);
                                tvPassingMark.setText("Passing score : "+passing_mark);

                                //--------------------------------//

                            }

                        }
                    }

                }
                break;
            case ApiCode.SUBMIT_TEST:
                Log.e("submission ", response);
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.getBoolean("status")){
                    try {
                        CommonMethods.showSuccessToast(getContext(),"Test Submitted Successfully");
                        gotToResultPage();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
        }

    }

    private void setTimerTime(String endTime) {
        long milSecond = CommonMethods.getTimeGapBetweenTime(CommonMethods.getCurrentTime("h:mm:ss a"),
                CommonMethods.changeDateTimeFmt("h:mm a", "h:mm:ss a", endTime));
        Log.e("left", "" + milSecond);
        mTimeLeftInMillis=milSecond;
    }

    private void gotToResultPage() {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("test_id", test_id);
        bundle.putString("from",from);
        bundle.putString("fromPage","StartTest");
        //bundle.putString("course_id",course_id);
//        bundle.putSt("total_mark",total_mark);
//        bundle.putInt("passing_mark",passing_mark);
//        bundle.putString("title",test_name);
//        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) testAdapter.getStudentTestData());
        fragment.setArguments(bundle);
        if (from.equals(SCHOOL_STUDENT)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else {
            ((MainActivity) getActivity()).SwitchFragment(fragment);
        }
        try {
            if (mCountDownTimer!=null){
                mCountDownTimer.cancel();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getDataForSubmit(){
        List<TestQuestionModel> questionModelList=new ArrayList<>();
        questionModelList.addAll(testAdapter.getStudentTestData());
        return new Gson().toJson(questionModelList);
    }
    public void setQuestionListField(){
        for(int i=0;i<questionList.size();i++){
            TestQuestionModel model=questionList.get(i);
            model.setSelected_option("-1");
           // total_mark=total_mark+Integer.parseInt(model.getMarks());
        }
       // testAdapter.notifyDataSetChanged();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SCHOOL_COACHING)|| from.equals(SCHOOL_STUDENT)){
            ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
        }else{
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                //for assigned to tutor
                ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            }else {
                ((MainActivity) getActivity()).getSupportActionBar().hide();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            ((SchoolMainActivity)getActivity()).showHideHomeActionBar(true);
        }else{
            if (from.equals(SIMPLEE_HOME_TUTOR)){
                ((TeacherMainActivity) getActivity()).getSupportActionBar().show();
            }else {
                ((MainActivity) getActivity()).getSupportActionBar().show();
            }
        }

    }


}
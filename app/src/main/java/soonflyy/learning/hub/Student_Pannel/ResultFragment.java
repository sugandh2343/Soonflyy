package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ResultFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    private TextView tv_examDuration,tv_test_title,tv_score,tv_passing_mark,tv_correct,tv_incorrect
            ,tv_totalQuestion,tv_attempt,tv_non_attempt;
    SwipeRefreshLayout refreshLayout;
    Button btn_result_status,btn_download_test,btn_back;
    private String passingMark;
    private String totalMark;
    private String title,from;
    private String obtianed_mark,correct_answer,inCorrectAnswer,result;
    String testId,downloadLink;
    String fromPage;
    ImageView ivToolbarBackBtn;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_result, container, false);

        testId=getArguments().getString("test_id");
        from=getArguments().getString("from");
        fromPage=getArguments().getString("fromPage");

        bindId(view);

        if (ConnectivityReceiver.isConnected()) {
           sendRequest(ApiCode.GET_TEST_RESULT);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }

        btn_download_test.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        setBackButtonEvent(view);
        return  view;
    }

    private void setDataToView(JSONObject dataObject) throws JSONException {

         downloadLink=dataObject.getString("download_link");
        String duration=dataObject.getString("duration");
        tv_examDuration.setText("Exam Duration: "+duration+" Min");
        title=dataObject.getString("test_title");
        totalMark=dataObject.getString("total_mark");
        passingMark=dataObject.getString("passing_mark");
        obtianed_mark=dataObject.getString("obtain_mark");
        result=dataObject.getString("result");

        tv_totalQuestion.setText(dataObject.getJSONObject("basic_result").getString("total_questions"));
        correct_answer=dataObject.getJSONObject("basic_result").getString("correct_answer");
        inCorrectAnswer=dataObject.getJSONObject("basic_result").getString("incorrect_answer");
        String total=dataObject.getJSONObject("basic_result").getString("total_questions");
        int totalAttempt=Integer.parseInt(correct_answer)+Integer.parseInt(inCorrectAnswer);
        int totalNonAttempt=Integer.parseInt(total)-totalAttempt;
        tv_attempt.setText(String.valueOf(totalAttempt));
        tv_non_attempt.setText(String.valueOf(totalNonAttempt));
        tv_test_title.setText(title);
       // tv_score.setText(obtianed_mark+" / "+totalMark);
        tv_passing_mark.setText(passingMark);
        tv_correct.setText(correct_answer);
        tv_incorrect.setText(inCorrectAnswer);//(questionModelList.size()-correct_answer)
        tv_score.setText(""+obtianed_mark);//obtianed_mark
        if (result.equals("1")||result.equalsIgnoreCase("Pass")){
            btn_result_status.setText("Pass");
            btn_result_status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.green_100)));

        }else{
            btn_result_status.setText("Fail");
            btn_result_status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.primary_color)));

        }

    }




    private void bindId(View view) {

        if (from.equals(SCHOOL_STUDENT)){
            //for school student
            ivToolbarBackBtn=((SchoolMainActivity)getActivity()).getToolbarBackBtn();
        }else{
            //for home student
            ivToolbarBackBtn=((MainActivity)getActivity()).getToolbarBackBtn();
        }
        tv_test_title=view.findViewById(R.id.titel_tv);
        tv_score=view.findViewById(R.id.tv_ur_score);
        tv_passing_mark=view.findViewById(R.id.tv_passing);
        tv_correct=view.findViewById(R.id.tv_correct);
        tv_incorrect=view.findViewById(R.id.tv_incorrect);

        tv_examDuration=view.findViewById(R.id.tv_exm_duration);
        refreshLayout=view.findViewById(R.id.refresh_result);
        btn_result_status=view.findViewById(R.id.btn_result);
        tv_totalQuestion=view.findViewById(R.id.tv_total_question);
        tv_attempt=view.findViewById(R.id.tv_attempt);
        tv_non_attempt=view.findViewById(R.id.tv_non_attempt);
        btn_download_test=view.findViewById(R.id.tv_download_test);
        btn_back=view.findViewById(R.id.btn_back_course);

        //-----toolbar back btn----------//
        ivToolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotTobackMethod();
            }
        });
        //---------------//

    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SCHOOL_STUDENT)) {
          //  ((SchoolMainActivity)getActivity()).showHideHomeActionBar(true);
            ((SchoolMainActivity)getActivity()).setActionBarTitle("Result");

        }else {
            ((MainActivity) getActivity()).setStudentChildActionBar("Result", false);
            ((MainActivity) getActivity()).getSupportActionBar().show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_download_test:
                dwnloadTest();
                break;
            case R.id.btn_back_course:
                gotTobackMethod();

                break;
        }
    }

    private void gotTobackMethod() {
        Log.e("resultbackstack: ", ": " + fromPage);
        if (from.equals(SCHOOL_STUDENT)) {
           if (fromPage.equalsIgnoreCase("StartTest")) {
               ((SchoolMainActivity) getActivity()).onBackPressed();
               ((SchoolMainActivity) getActivity()).onBackPressed();
           }else{
               ((SchoolMainActivity) getActivity()).onBackPressed();
           }
        }else{
            if (fromPage.equalsIgnoreCase("StartTest")) {
                ((MainActivity) getActivity()).onBackPressed();
                ((MainActivity) getActivity()).onBackPressed();
            }else{
                ((MainActivity) getActivity()).onBackPressed();
            }
        }
    }

    private void dwnloadTest() {
        if (downloadLink !=null){
            new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(),downloadLink,
                    "Test Pdf",true,"");
        }else{
            CommonMethods.showSuccessToast(getContext(),"Something went wrong");
        }

    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_TEST_RESULT:
                params.put("test_id", testId);
                if (from.equals(SCHOOL_STUDENT)){
                    params.put("user_id", new SessionManagement(getContext()).getString(SCHOOL_STUDENT_ID));
                }else {
                    params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                }
                callApi(ApiCode.GET_TEST_RESULT, params);

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_TEST_RESULT:
                service.postDataVolley(ApiCode.GET_TEST_RESULT,
                        BaseUrl.URL_GET_TEST_RESULT, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType){
            case ApiCode.GET_TEST_RESULT:
                Log.e("result ",response);
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.getBoolean("status")){
                    JSONObject dataObject=jsonObject.getJSONObject("data");
                    setDataToView(dataObject);
                }else if (!jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Something went wrong. You can view result later");
                    if (from.equals(SCHOOL_STUDENT)) {
                        ((SchoolMainActivity) getActivity()).onBackPressed();
                    }else{
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                }
                break;
        }
    }


    //-------get back event---------------//

    private void setBackButtonEvent(View view) {

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.e("resultbacke", " clicked");
                        gotTobackMethod();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
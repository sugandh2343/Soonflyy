package soonflyy.learning.hub.YourSchooolPannel;

import static soonflyy.learning.hub.Common.Constant.DEVICE_BRAND;
import static soonflyy.learning.hub.Common.Constant.DEVICE_ID;
import static soonflyy.learning.hub.Common.Constant.DEVICE_MODEL;
import static soonflyy.learning.hub.Common.Constant.DEVICE_VERSION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGGED_IN_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ADDRESS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_DOB;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_FATHER;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_S_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_S_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_ADDRESS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_DOB;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_FATHER;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolCoachingTutorHomeFragment;
import soonflyy.learning.hub.activity.LoginActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import in.aabhasjindal.otptextview.OtpTextView;


public class SchoolstudentOptVeriFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    public String phoneNumber;
    TextView  tv_timer, tv_resend;
    OtpTextView otp_view;
    Button btn_submit;
    String otp,login_type;
    LinearLayout lin_resend_otp;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis= 30000;
    private boolean mTimerRunning;

    View header_student,header_tutor;
    String typValue="";
    SessionManagement session_management;

    public SchoolstudentOptVeriFragment() {
        // Required empty public constructor
    }

    public static SchoolstudentOptVeriFragment newInstance(String param1, String param2) {
        SchoolstudentOptVeriFragment fragment = new SchoolstudentOptVeriFragment();

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
        View view=  inflater.inflate(R.layout.fragment_schoolstudent_opt_veri, container, false);
        session_management=new SessionManagement(getActivity());
        init(view);
        initControl();
        myObserve();
        getFromIntentData();
        startTimer();
        return view;
    }

    private void getFromIntentData() {
        phoneNumber=getArguments().getString("mobile");
        otp=getArguments().getString("otp");
        login_type=getArguments().getString("login_type");
        typValue=getArguments().getString("typeValue");
        Log.e("typeValue",""+typValue);
        if (login_type.equals(SCHOOL_TUTOR)){
            header_student.setVisibility(View.GONE);
            header_tutor.setVisibility(View.VISIBLE);
            typValue="1";
        }else if (login_type.equals(SCHOOL_STUDENT)){
            header_tutor.setVisibility(View.GONE);
            header_student.setVisibility(View.VISIBLE);
            typValue="0";
        }
        if (!otp.isEmpty()){
            setOtp(otp);
        }

    }
    private void startTimer() {
       // tv_resend.setVisibility(View.GONE);
        tv_resend.setEnabled(false);
        mTimerRunning = true;
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer.start();

        }else {
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();

                }

                @Override
                public void onFinish() {

                    mTimerRunning = false;
                    tv_resend.setEnabled(true);
                    tv_timer.setTextColor(getResources().getColor(R.color.light_black));
                    updateCountDownText();
                    mCountDownTimer.cancel();
                    lin_resend_otp.setVisibility(View.VISIBLE);
                }
            }.start();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tv_timer.setText(timeLeftFormatted);
        if(timeLeftFormatted.equalsIgnoreCase("00:10"))
        {
            tv_timer.setTextColor(getActivity().getResources().getColor(R.color.red));
        }


    }



    public void init(View view) {
        header_student=view.findViewById(R.id.layout_schoolstudent);
        header_tutor=view.findViewById(R.id.layout_schooltutor);
        tv_timer = view.findViewById(R.id.tv_timer);
        tv_resend =view. findViewById(R.id.tv_resend);
        btn_submit =view. findViewById(R.id.btn_submit);
        otp_view =view. findViewById(R.id.otp_view);
        lin_resend_otp=view.findViewById(R.id.lin_resend_otp);

    }

    public void initControl() {
        btn_submit.setOnClickListener(this);
        tv_resend.setOnClickListener(this);

    }


    public void myObserve() {

    }



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (onValidation()) {
                    if (CommonMethods.checkInternetConnection(getContext())) {
                        sendRequest(ApiCode.SCHOOL_VERIFY_OTP);
                    }
                }
               // showCustomDialog();
                break;
            case R.id.tv_resend:
                if (CommonMethods.checkInternetConnection(getContext())) {
                    startTimer();
                    otp_view.setOTP("");
                    //lin_resend_otp.setVisibility(View.GONE);
                    sendRequest(ApiCode.SCHOOL_SEND_OTP);
                }
                break;
        }
    }

    private void showCustomDialog() {
//        if(onValidation()){
            Fragment fragment = new SchoolCoachingTutorHomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
            // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
          //  fragmentTransaction.replace(R.id.frame_school, fragment);
          fragmentTransaction.replace(R.id.container_layout, fragment);

        fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
       // }
    }



    public boolean onValidation(){
        String otp = otp_view.getOTP().trim();

        if(otp.equals("")){
            CommonMethods.commonToast(getActivity(),"Please enter OTP number");
            return false;
        }if(otp.length()!=4){
            CommonMethods.commonToast(getActivity(),"Please enter Valid OTP number");
            return false;
        }if (!this.otp.equals(otp)){
            CommonMethods.commonToast(getActivity(),"Please enter Valid OTP number");
            return false;
        }
        return true;

    }
    public void onBackPressed() {
        CommonMethods.intentAlert(getActivity(), login_type,"Are you sure you want to cancel the process?", LoginActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("gone",login_type);

    }

    public void setOtp(String otp){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                otp_view.setOTP(otp);
            }
        },5000);

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.SCHOOL_SEND_OTP:
                params.put("mobile",phoneNumber);
                params.put("type",typValue);
                callApi(ApiCode.SCHOOL_SEND_OTP, params);
                break;
            case ApiCode.SCHOOL_VERIFY_OTP:
                params.put("mobile",phoneNumber);
                params.put("type",typValue);
                params.put("otp",otp_view.getOTP().trim());
                setDeviceInfo(params);
                callApi(ApiCode.SCHOOL_VERIFY_OTP, params);
                break;
        }
    }
    private void setDeviceInfo(HashMap<String,String> params){
        params.put("device_id",session_management.getString(DEVICE_ID));
        params.put("device_model",session_management.getString(DEVICE_MODEL));
        params.put("device_brand",session_management.getString(DEVICE_BRAND));
        params.put("device_version",session_management.getString(DEVICE_VERSION));
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_SEND_OTP:
                service.postDataVolley(ApiCode.SCHOOL_SEND_OTP,
                        BaseUrl.URL_SCHOOL_SEND_OTP, params);
                Log.e("api",BaseUrl.URL_SCHOOL_SEND_OTP);
                Log.e("params",params.toString());
                break;
            case ApiCode.SCHOOL_VERIFY_OTP:
                service.postDataVolley(ApiCode.SCHOOL_VERIFY_OTP,
                        BaseUrl.URL_SCHOOL_VERIFY_OTP, params);
                Log.e("api",BaseUrl.URL_SCHOOL_VERIFY_OTP);
                Log.e("params",params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_SEND_OTP:
                Log.e("send_otp",response);
                if (jsonObject.getBoolean("status")){
                    otp=jsonObject.getString("otp");
                    setOtp(otp);
                }else{
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                    tv_resend.setEnabled(true);
                    lin_resend_otp.setVisibility(View.VISIBLE);
                }
                break;
            case ApiCode.SCHOOL_VERIFY_OTP:
                Log.e("verify_otp",response);
                if (jsonObject.getBoolean("status")){
                    if(login_type.equals("student")){
                        //go for student student home page
                        createStudentLoginSession(jsonObject.getJSONObject("data"));
                        gotSchoolTutorHomePage();
                    }else{
                        //go for schoo tutor home page
                        createLoginSession(jsonObject.getJSONObject("data"));
                        gotSchoolTutorHomePage();
                    }
                 //go for school stu
                }else{
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                    tv_resend.setEnabled(true);
                    lin_resend_otp.setVisibility(View.VISIBLE);
                }
                break;
        }
    }



    private void createLoginSession(JSONObject data) {
        //SessionManagement session_management=new SessionManagement(getActivity());
        try {
            session_management.setString(SCHOOL_TEACHER_ID,data.getString("id"));
            session_management.setString(SCHOOL_T_NAME,data.getString("name"));
            session_management.setString(SCHOOL_T_MOBILE,data.getString("mobile"));
            session_management.setString(SCHOOL_T_EMAIL,data.getString("email"));
            session_management.setString(SCHOOL_T_FATHER,data.getString("father_name"));
            session_management.setString(SCHOOL_T_DOB,data.getString("dob"));
            session_management.setString(SCHOOL_T_ADDRESS,data.getString("address"));
            session_management.setString(SCHOOL_T_IMAGE,data.getString("image"));
            session_management.setString(SCHOOL_TUTOR_ID_SECTION,
                    data.getString("is_id_section_created"));
            session_management.setBoolean(SCHOOL_T_LOGIN_STATUS,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void createStudentLoginSession(JSONObject data) {

        try {
            session_management.setString(SCHOOL_STUDENT_ID,data.getString("id"));
            session_management.setString(SCHOOL_LOGGED_IN_STUDENT_ID,data.getString("id"));
            session_management.setString(SCHOOL_STUDENT_NAME,data.getString("name"));
            session_management.setString(SCHOOL_STUDENT_MOBILE,data.getString("mobile"));
            session_management.setString(SCHOOL_STUDENT_EMAIL,data.getString("email"));
            session_management.setString(SCHOOL_STUDENT_FATHER,data.getString("father_name"));
            session_management.setString(SCHOOL_STUDENT_DOB,data.getString("dob"));
            session_management.setString(SCHOOL_STUDENT_ADDRESS,data.getString("address"));
            session_management.setString(SCHOOL_S_IMAGE,data.getString("image"));
            session_management.setString(SCHOOL_STUDENT_ID_SECTION,data.getString("is_id_section_created"));

            session_management.setBoolean(SCHOOL_S_LOGIN_STATUS,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gotSchoolTutorHomePage() {
        if (mTimerRunning){
            mCountDownTimer.cancel();
        }
        Intent intent=new Intent(getActivity(),SchoolMainActivity.class);

        if (login_type.equals("student")){
            intent.putExtra("login_type","s_student");
        }else {
            intent.putExtra("login_type", login_type);
        }

        startActivity(intent);
       // getActivity().finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    ((SchoolLoginMainActivity) getActivity()).clearSchoolBackstack();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mTimerRunning){
                mCountDownTimer.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
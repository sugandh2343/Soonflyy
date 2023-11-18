package soonflyy.learning.hub.activity;

import static soonflyy.learning.hub.utlis.AppConstant.FROM_RESET_PASSWORD;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;
import static soonflyy.learning.hub.utlis.AppConstant.SCHOOL_SECTION_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolLoginMainActivity;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import in.aabhasjindal.otptextview.OtpTextView;

public class VerificationActivity extends BaseActivity implements View.OnClickListener, VolleyResponseListener {
    public String phoneNumber;
    TextView phone_tv, tv_timer, tv_resend;
    OtpTextView otp_view;
    ImageView arrow_back_img;
    ImageView iv_right_top_img;
    Button btn_submit;
    String otp,user_type;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis= 30000;
    private boolean mTimerRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        init();
        initControl();
        myObserve();
        getFromIntentData();
        startTimer();
        user_type =getIntent ().getStringExtra ("user_type");

    }

    private void getFromIntentData() {
        Intent intent = getIntent();
        phoneNumber=intent.getStringExtra("number");
        otp=intent.getStringExtra("otp");
        user_type=intent.getStringExtra("user_type");
        if (!otp.isEmpty()){
            setOtp(otp);
        }

    }
    private void startTimer() {
        tv_resend.setVisibility(View.GONE);
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
                    tv_resend.setVisibility(View.VISIBLE);
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
            tv_timer.setTextColor(getResources().getColor(R.color.red));
        }


    }


    @Override
    public void init() {
        iv_right_top_img = findViewById(R.id.iv_top_right_img);
        phone_tv = findViewById(R.id.phone_tv);
        arrow_back_img = findViewById(R.id.arrow_back_img);
        tv_timer = findViewById(R.id.tv_timer);
        tv_resend = findViewById(R.id.tv_resend);
        btn_submit = findViewById(R.id.btn_submit);
        otp_view = findViewById(R.id.otp_view);
         Picasso.get().load(R.drawable.otp_verification).into(iv_right_top_img);

    }

    @Override
    public void initControl() {
        arrow_back_img.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_resend.setOnClickListener(this);

    }

    @Override
    public void myObserve() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back_img:
                onBackPressed();
                break;
            case R.id.btn_submit:
                showCustomDialog();
                break;
            case R.id.tv_resend:
                startTimer();
                otp_view.setOTP("");
                if(user_type.equals("Teacher")||user_type.equals("Student"))
                sendRequest(ApiCode.MOBILEVARIFICATION);
                else
                    sendRequest(ApiCode.SCHOOL_MOBILE_VERIFICATION);
                break;
        }
    }

    private void showCustomDialog() {
        if(onValidation()){
            if(user_type.equals("Teacher")||user_type.equals("Student"))
            sendRequest(ApiCode.OTP_VERIFICATION);
            else
                sendRequest(ApiCode.SCHOOL_OTP_VERIFICATION);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.OTP_VERIFICATION :
                params.put("mobile",phoneNumber);
                params.put("otp",otp);
                if (user_type.equals("Teacher"))
                    params.put("type","1");
                else
                    params.put("type","0");
                callApi(ApiCode.OTP_VERIFICATION, params);
                break;
            case ApiCode.MOBILEVARIFICATION:
                params.put("mobile",phoneNumber);
                if (user_type.equals("Teacher"))
                    params.put("type","1");
                else
                    params.put("type","0");
                callApi(ApiCode.MOBILEVARIFICATION, params);
                break;
            case ApiCode.SCHOOL_OTP_VERIFICATION :
                params.put("mobile",phoneNumber);
                params.put("otp",otp);
                if (user_type.equals("school"))
                    params.put("type","0");
                else
                    params.put("type","1");
                callApi(ApiCode.SCHOOL_OTP_VERIFICATION, params);
                break;
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                params.put("mobile",phoneNumber);
                if (user_type.equals("school"))
                    params.put("type","0");
                else
                    params.put("type","1");
                callApi(ApiCode.SCHOOL_MOBILE_VERIFICATION, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback,VerificationActivity.this);
        switch (request){
            case ApiCode.OTP_VERIFICATION:
                service.postDataVolley(ApiCode.OTP_VERIFICATION,
                        BaseUrl.URL_MOBILE_VERIFICATION, params);
                break;
            case ApiCode.MOBILEVARIFICATION:
                service.postDataVolley(ApiCode.MOBILEVARIFICATION,
                        BaseUrl.URL_MOBILE_VERIFICATION, params);
                break;
            case ApiCode.SCHOOL_OTP_VERIFICATION:
                service.postDataVolley(ApiCode.SCHOOL_OTP_VERIFICATION,
                        BaseUrl.URL_SCHOOL_MOBILE_VERIFICATION, params);
                break;
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                service.postDataVolley(ApiCode.SCHOOL_MOBILE_VERIFICATION,
                        BaseUrl.URL_SCHOOL_MOBILE_VERIFICATION, params);
                break;
        }
    }

    public boolean onValidation(){
        String otp = otp_view.getOTP().trim();

        if(otp.equals("")){
            CommonMethods.commonToast(this,"Please enter OTP number");
            return false;
        }if(otp.length()!=6){
            CommonMethods.commonToast(this,"Please enter Valid OTP number");
            return false;
        }if (!this.otp.equals(otp)){
            CommonMethods.commonToast(this,"Please enter Valid OTP number");
            return false;
        }
        return true;

    }
    @Override
    public void onBackPressed() {
        Class tclass=null;
        if(user_type.equals("Teacher")||user_type.equals("Student")) {
            tclass = LoginActivity.class;
        } else {
            new SessionManagement(this).setString(LOGIN_PAGE_TYPE,FROM_RESET_PASSWORD);
            new SessionManagement(this).setString(SCHOOL_SECTION_TYPE,user_type);
            tclass = SchoolLoginMainActivity.class;
        }
        CommonMethods.intentAlert(this, user_type,"Are you sure you want to cancel the process?",tclass);// LoginActivity.class
    }

    @Override
    protected void onResume() {
        super.onResume();
        phone_tv.setText("+91- "+phoneNumber);
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

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.OTP_VERIFICATION:
            case ApiCode.SCHOOL_OTP_VERIFICATION:
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    boolean res=jsonObject.getBoolean("status");
                    if (res) {
                      // JSONArray array = jsonObject.getJSONArray("data");
                        Intent intent = new Intent(VerificationActivity.this, ChangePasswordActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user_type", user_type);
                        intent.putExtra("user_id", (jsonObject.getJSONObject("data")).getString("user_id"));
                        otp_view.setOTP("");
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ApiCode.MOBILEVARIFICATION:
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    otp = jsonObject.getString("data");
                    if (!otp.isEmpty()) {
                        tv_resend.setEnabled(false);
                        setOtp(otp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
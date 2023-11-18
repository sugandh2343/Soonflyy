package soonflyy.learning.hub.activity;

import static soonflyy.learning.hub.Common.Constant.CITY;
import static soonflyy.learning.hub.Common.Constant.DEVICE_BRAND;
import static soonflyy.learning.hub.Common.Constant.DEVICE_ID;
import static soonflyy.learning.hub.Common.Constant.DEVICE_MODEL;
import static soonflyy.learning.hub.Common.Constant.DEVICE_VERSION;
import static soonflyy.learning.hub.Common.Constant.DISTRICT;
import static soonflyy.learning.hub.Common.Constant.DOB;
import static soonflyy.learning.hub.Common.Constant.EMAIL;
import static soonflyy.learning.hub.Common.Constant.FATHER;
import static soonflyy.learning.hub.Common.Constant.GENDER;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.INSTITUTE;
import static soonflyy.learning.hub.Common.Constant.IS_INSTRUCTOR;
import static soonflyy.learning.hub.Common.Constant.LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.PROFILE_IMAGE;
import static soonflyy.learning.hub.Common.Constant.STATE;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.Common.Constant.WORKPLACE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.model.LoginModel;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.AppUtlis;
import soonflyy.learning.hub.utlis.SessionManagement;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener, VolleyResponseListener {

    EditText et_mobile_num,et_password;
    TextView forget_password_tv,login_tittle;//,tv_register
    TextView tvRegister;
    Button btnLogin;
    RelativeLayout rel_register;
    ImageView toggle_img;
    String user_type;
    boolean isPasswordVisible;
    String mobile_num;
    SessionManagement session_management;

    FirebaseAuth firebaseAuth;
    String dont_account="<p>If you don't have an account? <b><font color='blue'>Register</font></b></p>";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initControl();
        myObserve();
        addTextWatcherListener();
        getFromIntentData();

        firebaseAuth=FirebaseAuth.getInstance();

    }

    private void getFromIntentData() {
        Intent intent = getIntent();
        user_type = intent.getStringExtra(AppConstant.USER_TYPE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        login_tittle.setText("Login Please");
    }

    @Override
    public void init() {
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_password = findViewById(R.id.et_password);
        forget_password_tv = findViewById(R.id.forget_password_tv);
        btnLogin = findViewById(R.id.btnLogin);
        rel_register = findViewById(R.id.rel_register);
        toggle_img = findViewById(R.id.toggle_cofm_img);
        login_tittle = findViewById(R.id.login_tittle);
        tvRegister=findViewById(R.id.tv_reg);
      //tv_register=findViewById(R.id.tv_register);
        //tv_register.setText(Html.fromHtml(dont_account));
        session_management = new SessionManagement(this);


    }
    private void addTextWatcherListener() {
        et_mobile_num.addTextChangedListener(new TextWatcher(et_mobile_num));
        et_password.addTextChangedListener(new TextWatcher(et_password));
    }

    @Override
    public void initControl() {
        rel_register.setOnClickListener(this);
        forget_password_tv.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        toggle_img.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void myObserve() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           // case R.id.rel_register:
            case R.id.tv_reg:
                goToSignUpScreen();
                break;
            case R.id.forget_password_tv:
                goToChangePassword();
                //goToVerificationScreen ();
                break;
            case R.id.btnLogin:
              goToVerificationScreen ();
                break;
            case R.id.toggle_cofm_img:
                showHidePassword();
                break;
        }
    }

    private void showHidePassword() {
        if (!isPasswordVisible) {
            AppUtlis.hideKeyboard(this);
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggle_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_black_24dp));
            isPasswordVisible = true;
        } else {
            AppUtlis.hideKeyboard(this);
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggle_img.setImageDrawable(getResources().getDrawable(R.drawable.hidden_password));
            isPasswordVisible = false;
        }
    }

    private void goToVerificationScreen() {
        if(onValidation()){
          //  if (user_type.equals("Student")) {
                sendRequest(ApiCode.LOGIN_CODE);
          //  }


        }

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.LOGIN_CODE :
                params.put("mobile",et_mobile_num.getText().toString());
                params.put("password",et_password.getText().toString());
                params.put("device_id",session_management.getString(DEVICE_ID));
                params.put("device_model",session_management.getString(DEVICE_MODEL));
                params.put("device_brand",session_management.getString(DEVICE_BRAND));
                params.put("device_version",session_management.getString(DEVICE_VERSION));
                if (user_type.equals("Teacher"))
                params.put("type","1");
                else
                    params.put("type","0");
                callApi(ApiCode.LOGIN_CODE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
//        VolleyResponseListener callback = this;
//        VolleyService service = new VolleyService(callback,LoginActivity.this);
//        switch (request){
//            case ApiCode.LOGIN_CODE:
//                service.postDataVolley(ApiCode.LOGIN_CODE,
//                        BaseUrl.URL_LOGIN, params);
//                Log.e("apiName",""+BaseUrl.URL_LOGIN);
//                Log.e("postParam",""+params);
//                break;

//        }
        if(!isMobile(et_mobile_num.getText().toString())){
            firebaseAuth.signInWithEmailAndPassword(et_mobile_num.getText().toString(),et_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                            reference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    session_management.setString(USER_ID, firebaseAuth.getUid());
                                    session_management.setString(EMAIL, snapshot.child("email").getValue(String.class));
                                    session_management.setString(MOBILE, snapshot.child("mobile").getValue(String.class));
                                    session_management.setString(CITY, snapshot.child("city").getValue(String.class));
                                    session_management.setString(STATE, snapshot.child("state").getValue(String.class));
                                    session_management.setString(DISTRICT, snapshot.child("district").getValue(String.class));
                                    session_management.setString(INSTITUTE, snapshot.child("institute_name").getValue(String.class));
                                    session_management.setString(DOB, snapshot.child("institute_name").getValue(String.class));
                                    session_management.setString(NAME, snapshot.child("name").getValue(String.class));
                                    session_management.setString(GENDER, snapshot.child("gender").getValue(String.class));
                                    session_management.setString(WORKPLACE, snapshot.child("workplace").getValue(String.class));
                                    session_management.setString(PROFILE_IMAGE, "");
                                    session_management.setString(FATHER, "");
                                    session_management.setString(ID_SECTION,""+snapshot.hasChild("id_section"));
                                    session_management.setInt(LOGIN_STATUS, 1);
                                    session_management.setBoolean(LOGIN_STATUS, true);
                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("TAG", "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }

                                                    // Get new FCM registration token
                                                    String token = task.getResult();

                                                    // Log and toast
                                                    //String msg = getString(R.string.msg_token_fmt, token);
                                                    Log.e("TAG", token);
                                                    HashMap<String,Object> hashMap=new HashMap<>();
                                                    hashMap.put("token",token);
                                                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
                                                    reference1.child(firebaseAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            // session_management.setString(M);
                                                            Intent intent;
                                                            if (user_type.equals("Teacher")) {
                                                                session_management.setString(IS_INSTRUCTOR, "1");

                                                                intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                                                            } else {
                                                                session_management.setString(IS_INSTRUCTOR, "0");

                                                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            }
                                                            intent.putExtra(AppConstant.MOBILE_NUMBER, mobile_num);
                                                            intent.putExtra(AppConstant.USER_TYPE, user_type);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    });
//                        Toast.makeText(RegistrationActivity.this, "" + token, Toast.LENGTH_SHORT).show();
                                                }
                                            });

//                            session_management.setString(FIRST_NAME, model.data.first_name);
//                            session_management.setString(LAST_NAME, model.data.last_name);
//                            session_management.setString(EMAIL, model.data.email);
//                            session_management.setString(ROLL, model.data.role);
//                            session_management.setInt(VALIDITY, model.data.validity);

                                }





                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            CommonMethods.showSuccessToast(LoginActivity.this,e.getMessage());

                        }
                    });
        }
    }

    private void goToChangePassword() {
        Intent intent = new Intent(LoginActivity.this,MobileVerificationActivity.class);
        intent.putExtra ("user_type", user_type);
        //Toast.makeText (this, ""+user_type, Toast.LENGTH_SHORT).show ( );
        startActivity(intent);
    }

    private void goToSignUpScreen() {
        Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
        intent.putExtra(AppConstant.USER_TYPE,user_type);
        startActivity(intent);
    }

    public boolean onValidation(){
        mobile_num = et_mobile_num.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(TextUtils.isEmpty(mobile_num)){
            et_mobile_num.setError("Enter your mobile number or email");
            et_mobile_num.requestFocus();
            //Toast.makeText(this,"Please fill mobile or email",Toast.LENGTH_LONG).show();
            return false; }

        if (isMobile(mobile_num)) {
            if (mobile_num.length() != 10) {
               et_mobile_num.setError("Invalid mobile number");
               et_mobile_num.requestFocus();
                return false;
            }
            if (Integer.parseInt(String.valueOf(mobile_num.charAt(0))) < 6) {
                et_mobile_num.setError("Invalid mobile number");
                et_mobile_num.requestFocus();
                return false;
            }
        }
        if (!isMobile(mobile_num)) {
            if (!(Patterns.EMAIL_ADDRESS.matcher(mobile_num).matches())) {
                et_mobile_num.setError("Invalid email");
                et_mobile_num.requestFocus();
                return false;

            }
        }


//        if(mobile_num.length()!=10){
//            Toast.makeText(this,"Please fill valid mobile number",Toast.LENGTH_LONG).show();
//            return false; }
//        if(Integer.parseInt (String.valueOf (mobile_num.charAt (0))) < 6){
//            Toast.makeText(this,"Mobile number should be start 6 or greater 6",Toast.LENGTH_LONG).show();
//            return false;
//        }
       if(TextUtils.isEmpty(password)){
           et_password.setError("Enter password");
           et_password.requestFocus();
            return false;
        }if (password.length()<4){
           et_password.setError("Invalid password");
           et_password.requestFocus();
           return false;
       }
        return true;
    }
    public boolean isMobile(String value) {
        boolean result = false;
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }


    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.LOGIN_CODE:
                Log.d("login_data",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        //Toast.makeText(this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                       CommonMethods.showSuccessToast(this,jsonObject.getString("message"));
                        LoginModel model = (LoginModel) VolleyService.response(response, LoginModel.class);
                        if (model.status) {
                            session_management.setString(USER_ID, model.data.id);
                            session_management.setString(EMAIL, model.data.email);
                            session_management.setString(MOBILE, model.data.mobile);
                            session_management.setString(CITY, model.data.city);
                            session_management.setString(STATE, model.data.state);
                            session_management.setString(DISTRICT, model.data.district);
                            session_management.setString(INSTITUTE, model.data.institute_name);
                            session_management.setString(DOB, model.data.dob);
                            session_management.setString(NAME, model.data.name);
                            session_management.setString(GENDER, model.data.gender);
                            session_management.setString(WORKPLACE, model.data.workplace);
                            session_management.setString(PROFILE_IMAGE, model.data.image);
                          session_management.setString(FATHER, model.data.father_name);
                          session_management.setString(ID_SECTION,model.data.is_id_section_created);

//                            session_management.setString(FIRST_NAME, model.data.first_name);
//                            session_management.setString(LAST_NAME, model.data.last_name);
//                            session_management.setString(EMAIL, model.data.email);
//                            session_management.setString(ROLL, model.data.role);
//                            session_management.setInt(VALIDITY, model.data.validity);
                            session_management.setInt(LOGIN_STATUS, model.data.login_status);
                            session_management.setBoolean(LOGIN_STATUS, true);
                            session_management.setString(IS_INSTRUCTOR, model.data.is_instructor);
                            // session_management.setString(M);
                            Intent intent;
                            if (user_type.equals("Teacher")) {

                                intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                            } else {

                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            }
                            intent.putExtra(AppConstant.MOBILE_NUMBER, mobile_num);
                            intent.putExtra(AppConstant.USER_TYPE, user_type);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                    else{
                        CommonMethods.showSuccessToast(this,jsonObject.getString("message"));

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
}
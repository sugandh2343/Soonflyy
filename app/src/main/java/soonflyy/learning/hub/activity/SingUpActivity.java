package soonflyy.learning.hub.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.AppUtlis;
import soonflyy.learning.hub.utlis.SessionManagement;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SingUpActivity extends BaseActivity implements View.OnClickListener, VolleyResponseListener, RadioGroup.OnCheckedChangeListener {
   EditText et_name,et_mobile_num,et_email,et_password,et_confm_password,et_father_name,et_workplace,et_city,et_state,et_district;
   LinearLayout lin_student_info,lin_teaher_info,lin_gender;
   TextView signup_title,tv_dob,tvLoginNow;
   ImageView toggle_img,toggle_cofm_img;
   Button btn_signup;
   RadioGroup gender_group;
   RelativeLayout rel_login_now;
   String user_type;
   Button btn_send_code;
   int gender =-1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean isPasswordVisible;

    PinView MobileOtp_Pinview;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        init();
        initControl();
        myObserve();
        getFromIntentData();
        addTextchangeListener();
        firebaseAuth=FirebaseAuth.getInstance();
        btn_send_code.setVisibility(View.GONE);
        MobileOtp_Pinview.setVisibility(View.GONE);
        et_mobile_num.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence , int i , int i1 , int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence , int i , int i1 , int i2) {
                if(charSequence.toString().length()==10){
                    btn_send_code.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void getFromIntentData() {
        Intent intent = getIntent();
        user_type = intent.getStringExtra(AppConstant.USER_TYPE);
        if (user_type.equals("Teacher")){
            et_name.setHint("School/Teacher/Institute Name");
            lin_student_info.setVisibility(View.GONE);
            lin_teaher_info.setVisibility(View.VISIBLE);
            lin_gender.setVisibility(View.GONE);

        }else if (user_type.equals("Student")){
            et_name.setHint("Your Name");
            lin_student_info.setVisibility(View.VISIBLE);
            lin_teaher_info.setVisibility(View.GONE);
            lin_gender.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        signup_title.setText("Register Please");
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
        finish();
    }

    @Override
    public void init() {
        tvLoginNow=findViewById(R.id.tv_login_now);
        lin_gender=findViewById(R.id.lin_gender);
        signup_title = findViewById(R.id.signup_title);

        et_name = findViewById(R.id.et_name);
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confm_password = findViewById(R.id.et_confm_password);
        rel_login_now=findViewById(R.id.rel_register);
        toggle_img = findViewById(R.id.toggle_img);
        toggle_cofm_img = findViewById(R.id.toggle_cofm_img);
        btn_signup = findViewById(R.id.btn_signup);
        gender_group =findViewById(R.id.coruse_radio_group);
        et_father_name=findViewById(R.id.et_father);
        et_workplace=findViewById(R.id.et_workplace);
        tv_dob=findViewById(R.id.tv_dob);

        lin_student_info=findViewById(R.id.lin_student_info);
        lin_teaher_info=findViewById(R.id.lin_teacher_info);
        et_city=findViewById(R.id.et_city);
        et_state=findViewById(R.id.et_state);
        et_district=findViewById(R.id.et_district);
        btn_send_code=findViewById(R.id.btn_get_code);
        MobileOtp_Pinview=findViewById(R.id.MobileOtp_Pinview);




        //setTint color
      //  CommonMethods.setEditTextDrawerColor(this,et_name,R.color.primary_color,true);
        //CommonMethods.setEditTextDrawerColor(this,et_mobile_num,R.color.primary_color,true);

    }
    private void addTextchangeListener() {
        et_name.addTextChangedListener(new TextWatcher(et_name));
        et_mobile_num.addTextChangedListener(new TextWatcher(et_mobile_num));
        et_email.addTextChangedListener(new TextWatcher(et_email));
        et_password.addTextChangedListener(new TextWatcher(et_password));
        et_confm_password.addTextChangedListener(new TextWatcher(et_confm_password));
    }
    @Override
    public void initControl() {
        btn_signup.setOnClickListener(this);
        toggle_img.setOnClickListener(this);
        toggle_cofm_img.setOnClickListener(this);
        gender_group.setOnCheckedChangeListener(this);
        rel_login_now.setOnClickListener(this);
        tv_dob.setOnClickListener(this);
        tvLoginNow.setOnClickListener(this);
        btn_send_code.setOnClickListener(this);
    }

    @Override
    public void myObserve() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
          case R.id.btn_signup:
                goToLoginActivity();
                break;
            case R.id.toggle_img:
                showHidePassword();
                break;
            case R.id.toggle_cofm_img:
                showHideConfirmPassword();
                break;
            //case R.id.rel_register:
            case R.id.tv_login_now:
                onBackPressed();
                break;
            case R.id.tv_dob:
             //   showDateChooser();
                showDatePicker();
                break;
            case R.id.btn_get_code:
                //   showDateChooser();
                MobileOtp_Pinview.setVisibility(View.VISIBLE);
                sendOtpCode("+91"+et_mobile_num.getText().toString());
                break;

        }
    }

    private void sendOtpCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                        signInWithCredential(phoneAuthCredential);
                        Log.e("OTPcode",phoneAuthCredential.getSmsCode());
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showDateChooser() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String month = String.valueOf(i1 + 1);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = String.valueOf(i2);
                if (day.length() == 1) {
                    day = "0" + day;
                }
               String dob = day + "/" + month + "/" + i;
                tv_dob.setText(dob);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void showDatePicker() {
        boolean isValidDate=false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(this);
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(SingUpActivity.this,
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(SingUpActivity.this,
                R.color.calendar_divider_color));

        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month, int day, int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());

                tv_dob.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                String d=tv_dob.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tv_dob.setText("");
//TODO                        spinnerPickerDialog.show(SingUpActivity.this.getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(SingUpActivity.this,"Please select a date before  current date");
                    }
                }
            }

        });
//TODO       spinnerPickerDialog.show(SingUpActivity.this.getSupportFragmentManager(), "");
    }

    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidDOB(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showHideConfirmPassword() {
        if (!isPasswordVisible) {
            AppUtlis.hideKeyboard(this);
            et_confm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggle_cofm_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_black_24dp));
            isPasswordVisible = true;
        } else {
            AppUtlis.hideKeyboard(this);
            et_confm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggle_cofm_img.setImageDrawable(getResources().getDrawable(R.drawable.hidden_password));
            isPasswordVisible = false;
        }
        toggle_cofm_img.setColorFilter(ContextCompat.getColor(this,R.color.black));
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
        toggle_img.setColorFilter(ContextCompat.getColor(this,R.color.black));
    }

    public boolean onValidation(){
        String name = et_name.getText().toString().trim();
        String mobile_num = et_mobile_num.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirm_password = et_confm_password.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
           et_name.setError("Enter name");
           et_name.requestFocus();
            return false; }
        if(TextUtils.isEmpty(mobile_num)){
           et_mobile_num.setError("Enter mobile number");
           et_mobile_num.requestFocus();
            return false; }
        if(mobile_num.length()!=10){
            et_mobile_num.setError("Invalid mobile number");
            et_mobile_num.requestFocus();
            return false; }

        if(Integer.parseInt (String.valueOf (mobile_num.charAt (0))) < 6){
            et_mobile_num.setError("Invalid mobile number");
            et_mobile_num.requestFocus();
             return false;
        }
        if(TextUtils.isEmpty(email)){
            et_email.setError("Enter email");
            et_email.requestFocus();
            return false;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Invalid email");
            et_email.requestFocus();
            return false;
        }

        if (user_type.equals("Student")) {
            if (TextUtils.isEmpty(et_father_name.getText().toString().trim())) {
                et_father_name.setError("Enter father name");
                et_father_name.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(tv_dob.getText().toString().trim())) {
                Toast.makeText(this, "Choose your date of birth", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (user_type.equals("Teacher")){
            if (TextUtils.isEmpty(et_city.getText().toString().trim())) {
                et_city.setError("Enter city");
                et_city.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(et_state.getText().toString().trim())) {
                et_state.setError("Enter state");
                et_state.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(et_district.getText().toString().trim())) {
                et_district.setError("Enter district");
                et_district.requestFocus();
                return false;
            }
        }
        
        if(TextUtils.isEmpty(et_workplace.getText().toString().trim())){
            et_workplace.setError("Enter workplace");
            et_workplace.requestFocus();
            return false; }

        if(TextUtils.isEmpty(password)){
            et_password.setError("Enter password");
            et_password.requestFocus();
            return false;
        }
        if (password.length()<4){
            et_password.setError("Minimum password length should be 4");
            et_password.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(confirm_password)){
            et_confm_password.setError("Enter password again to confirm ");
            et_confm_password.requestFocus();
            return false;
        }if(!password.matches(confirm_password)){
            et_confm_password.setError("Password doesn't match");
            et_confm_password.requestFocus();
            return false;
        }
        if (user_type.equals("Student") && gender <0){
           CommonMethods.showSuccessToast(this,"Please select your gender");
            return false;
        }
        if(TextUtils.isEmpty(MobileOtp_Pinview.getText().toString())){
            CommonMethods.showSuccessToast(this,"Please Enter the OTP received");

        }
       /* if(!AppUtlis.isValidPassword(password)){
            Toast.makeText(this,"Please fill valid password",Toast.LENGTH_LONG).show();
            return false;
        }*/
        return true;
  }
    private void goToLoginActivity() {
        if( onValidation()){
            sendRequest(ApiCode.SIGN_UP);
        }

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.SIGN_UP :
                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("password",et_password.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("institute_name",et_name.getText().toString().trim());
                params.put("city",et_city.getText().toString().trim());
                params.put("state",et_state.getText().toString().trim());
                params.put("district",et_district.getText().toString().trim());
                params.put("father_name",et_father_name.getText().toString().trim());
                params.put("gender",String.valueOf(gender));
                params.put("workplace",et_workplace.getText().toString().trim());
                params.put("dob",tv_dob.getText().toString().trim());
                params.put("token",new SessionManagement(this).getPushToken());
                params.put("device_id",new SessionManagement(this).getPushDeviceId());
                params.put("uid",firebaseAuth.getUid());

                if (user_type.equals("Teacher"))
                    params.put("type","1");
                else
                    params.put("type","0");
                callApi(ApiCode.SIGN_UP, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
//        Log.e("params",params.toString());
//        VolleyResponseListener callback = this;
//        VolleyService service = new VolleyService(callback,SingUpActivity.this);
//        switch (request){
//            case ApiCode.SIGN_UP:
//                service.postDataVolley(ApiCode.SIGN_UP,
//                        BaseUrl.URL_SIGN_UP, params);
//                break;
//
//        }
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean validate=false;
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(ds.child("mobile").getValue(String.class).equals(et_mobile_num.getText().toString())){
                        validate=true;
                    }
                    if(validate){
                        Toast.makeText(SingUpActivity.this , "Mobile Number Already Exist" , Toast.LENGTH_SHORT).show();
                    }else{

                        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                                        reference.child(firebaseAuth.getUid()).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                CommonMethods.showSuccessToast(SingUpActivity.this, "Register Successfully");
                                                Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                                                intent.putExtra(AppConstant.USER_TYPE, user_type);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SingUpActivity.this , ""+e.getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(firebaseAuth.getUid()).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CommonMethods.showSuccessToast(SingUpActivity.this, "Register Successfully");
                                Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                                intent.putExtra(AppConstant.USER_TYPE, user_type);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.SIGN_UP:
                try {
                    Log.e("register_response",response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    boolean res=jsonObject.getBoolean("response");
                    if (res) {
                        CommonMethods.showSuccessToast(this, "Register Successfully");
                        Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                        intent.putExtra(AppConstant.USER_TYPE, user_type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        CommonMethods.showSuccessToast(this,jsonObject.getString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
      /*  RadioButton checked_btn=(RadioButton)group.findViewById(checkedId);
        boolean isChecked=checked_btn.isChecked();
        if (isChecked){
            Log.e("checked course: ",checked_btn.getText().toString());
        }

       */
        switch (checkedId){
            case R.id.male_btn:
                gender =0;
                Log.e("gender: ","male");
                break;
            case R.id.female_radio:
                gender =1;
                Log.e("gender : ","female");
                break;
        }


    }
}
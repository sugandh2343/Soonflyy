package soonflyy.learning.hub.YourSchooolPannel;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchooRegisterMainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class SchoolStudentLoginFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    EditText et_mobile_num;
    TextView login_tittle,tvSign;//,tv_register
    Button btnLogin;
    RelativeLayout rel_register;
    String login_type;
    View header_school,header_student;
    String typeValue;
    PinView MobileOtp_Pinview;
    Button btnSendOtp;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private String verificationId;
    SessionManagement session_management;
    public SchoolStudentLoginFragment() {
        // Required empty public constructor
    }


    public static SchoolStudentLoginFragment newInstance(String param1, String param2) {
        SchoolStudentLoginFragment fragment = new SchoolStudentLoginFragment();

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
        View view = inflater.inflate(R.layout.fragment_school_student_login, container, false);
        session_management=new SessionManagement(getActivity());

        init(view);
        getArgumentData();
        initControl();
        //myObserve();
        addTextWatcherListener();

        return view;
    }

    private void getArgumentData() {
        login_type=getArguments().getString("login_type");
    }

    public void init (View view) {
        tvSign=view.findViewById(R.id.tv_sign);
        header_school=view.findViewById(R.id.layout_school);
        header_student=view.findViewById(R.id.layout_student);
        MobileOtp_Pinview=view.findViewById(R.id.MobileOtp_Pinview);
        MobileOtp_Pinview.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();

        btnSendOtp=view.findViewById(R.id.btnSendOtp);
            et_mobile_num = view.findViewById(R.id.et_mobile_num);
            btnLogin = view.findViewById(R.id.btnLogin);
            rel_register = view.findViewById(R.id.rel_register);
        btnLogin.setVisibility(View.GONE);

            login_tittle = view.findViewById(R.id.login_tittle);
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            //tv_register=findViewById(R.id.tv_register);
            //tv_register.setText(Html.fromHtml(dont_account));



        }
        private void addTextWatcherListener () {
            et_mobile_num.addTextChangedListener(new TextWatcher(et_mobile_num));
        }
        public void initControl () {
            rel_register.setOnClickListener(this);
            tvSign.setOnClickListener(this);
            btnLogin.setOnClickListener(this);
            btnSendOtp.setOnClickListener(this);

        }


        @Override
        public void onClick (View v){
            switch (v.getId()) {
                //case R.id.rel_register:
                case R.id.tv_sign:
                    goToSignUpScreen();
                    break;
                case R.id.forget_password_tv:

                    break;
                case R.id.btnLogin:

                  if (onValidation()) {
                        if (ConnectivityReceiver.isConnected()) {
                            callLoginApi();                        }
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }


                    break;
                case R.id.btnSendOtp:

                   if(onValidation()){
                       if (ConnectivityReceiver.isConnected()) {
                           progressDialog.setMessage("Sending SMS Code");
                           progressDialog.show();

                           sendVerificationCode("+91"+et_mobile_num.getText().toString());             }
                   }else{
                       CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                   }





                    break;


            }
        }
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    progressDialog.dismiss();
                    btnSendOtp.setVisibility(View.GONE);
                    MobileOtp_Pinview.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    verificationId=s;
                    CommonMethods.showSuccessToast(getActivity(),"Code Sent to "+et_mobile_num.getText().toString());

                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        Log.e("Verification","Completed");
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        progressDialog.dismiss();
                        CommonMethods.showSuccessToast(getActivity(),e.getMessage());

                    }
                });
    }


    private void callLoginApi() {
       // if (login_type.equals("s_tutor")){
            //call api for tutor login
            sendRequest(ApiCode.SCHOOL_SEND_OTP);

     //   }else{
            //call login api for student

       //}
    }

    @Override
    public void onResume() {
        super.onResume();
        if (login_type.equals("student")){
            ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("gone","student");
            header_school.setVisibility(View.GONE);
            header_student.setVisibility(View.VISIBLE);
            typeValue="0";
        }else if (login_type.equals("s_tutor")){
            login_tittle.setText("Tutor Login");
            ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("visible","s_tutor");
            header_student.setVisibility(View.GONE);
            header_school.setVisibility(View.VISIBLE);
            typeValue="1";


        }
        checkLoginStatus();
    }
    private void checkLoginStatus(){
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Checking your login credential\nPlease wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (login_type.equals(SCHOOL_TUTOR)) {
                    if (new SessionManagement(getActivity()).getBoolean(SCHOOL_T_LOGIN_STATUS)) {

                        gotSchoolTutorHomePage();
                        progressDialog.dismiss();
                    } else{
                        progressDialog.dismiss();
                    }
                }else{
                    //for student login
                    if (new SessionManagement(getActivity()).getBoolean(SCHOOL_S_LOGIN_STATUS)) {

                        gotSchoolTutorHomePage();
                        progressDialog.dismiss();
                    }else {
                      progressDialog.dismiss();
                  }
                }
            }
        },2000);

    }

    private void gotSchoolTutorHomePage() {
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
                    ((SchoolLoginMainActivity)getActivity()).clearSchoolBackstack();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },200);

    }

    private void goToVerificationScreen (Bundle bundle) {
           // if (onValidation()) {
            Log.e("hjbkmkl", "goToSignUpScreen:  "+rel_register );

            Fragment fragment = new SchoolstudentOptVeriFragment();
            fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
                fragmentTransaction.replace(R.id.container_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//}


        }
        private void goToSignUpScreen () {
            Log.e("cghvjbk", "goToSignUpScreen:  "+rel_register );
            Intent intent=new Intent(getActivity(), SchooRegisterMainActivity.class);
            if (login_type.equals("s_tutor")) {
                intent.putExtra("selected_tab", "tutor");
                intent.putExtra("register_type", "s_tutor");
            }else if (login_type.equals("student")){
                intent.putExtra("selected_tab", "student");
                intent.putExtra("register_type", "student");
            }
           // startActivityForResult(intent,1234);
            ((SchoolLoginMainActivity)getActivity()).launchSomeActivity.launch(intent);
//            Fragment fragment = new SchoolStudentSignupFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
//            // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
//            fragmentTransaction.replace(R.id.container_layout, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
        }

        public boolean onValidation () {
            String   mobile_num = et_mobile_num.getText().toString().trim();
            if (TextUtils.isEmpty(mobile_num)) {
                et_mobile_num.setError("Enter mobile number");
                et_mobile_num.requestFocus();
                return false;
            }
            return true;
        }



    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request){
            case ApiCode.SCHOOL_SEND_OTP:
//                params.put("type",typeValue);//modification here last
//                params.put("mobile",et_mobile_num.getText().toString().trim());
//                callApi(ApiCode.SCHOOL_SEND_OTP, params);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean loginValue=false;
                        String schoolUid="",name="",mobile="",email="",father="",dob="",address="",image="";
                        for(DataSnapshot ds:snapshot.getChildren()){
                            if(ds.child("type").exists()){
                                if(ds.child("type").getValue(String.class).equals("school")){
                                    if(ds.child("Tutors").child(et_mobile_num.getText().toString()).exists()){
                                        loginValue=true;
                                        schoolUid=ds.getKey();
                                        name=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("name").getValue(String.class);
                                        mobile=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("mobile").getValue(String.class);
                                        email=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("email").getValue(String.class);
                                        father=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("father").getValue(String.class);
                                        dob=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("dob").getValue(String.class);
                                        address=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("address").getValue(String.class);
                                        image=ds.child("Tutors").child(et_mobile_num.getText().toString()).child("image").getValue(String.class);
                                    }
                                }
                            }
                        }
                        if(loginValue){
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, MobileOtp_Pinview.getText().toString());
                            String finalName = name;
                            String finalMobile = mobile;
                            String finalEmail = email;
                            String finalFather = father;
                            String finalDob = dob;
                            String finalAddress = address;
                            String finalImage = image;
                            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    session_management.setString(SCHOOL_TEACHER_ID,firebaseAuth.getUid());
                                    session_management.setString(SCHOOL_T_NAME, finalName);
                                    session_management.setString(SCHOOL_T_MOBILE, finalMobile);
                                    session_management.setString(SCHOOL_T_EMAIL, finalEmail);
                                    session_management.setString(SCHOOL_T_FATHER, finalFather);
                                    session_management.setString(SCHOOL_T_DOB, finalDob);
                                    session_management.setString(SCHOOL_T_ADDRESS, finalAddress);
                                    session_management.setString(SCHOOL_T_IMAGE, finalImage);
                                    session_management.setString(SCHOOL_TUTOR_ID_SECTION,
                                            "0");
                                    session_management.setBoolean(SCHOOL_T_LOGIN_STATUS,true);
                                    gotSchoolTutorHomePage();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                            // after getting credential we are
                            // calling sign in method.

                        }else{
                            CommonMethods.showSuccessToast(getActivity(),"No User Record Found. Please sign up.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
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

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_SEND_OTP:
                Log.e("sc_tutor_login",response.toString());
                if (jsonObject.getBoolean("status")){
                    String otp=jsonObject.getString("otp");
                    Bundle bundle=new Bundle();
                    bundle.putString("mobile",et_mobile_num.getText().toString().trim());
                    bundle.putString("login_type",login_type);
                    bundle.putString("otp",otp);
                    bundle.putString("typeValue",typeValue);
                  goToVerificationScreen(bundle);
                }else{
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                }

//                    boolean res=jsonObject.getBoolean("response");
//                    if (res) {
//                        CommonMethods.showSuccessToast(this, "Register Successfully");
//                        Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
//                        intent.putExtra(AppConstant.USER_TYPE, user_type);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        CommonMethods.showSuccessToast(this,jsonObject.getString("message"));
//                    }
                break;
        }
    }




}
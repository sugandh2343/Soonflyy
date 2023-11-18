package soonflyy.learning.hub.YourSchooolPannel;

import static soonflyy.learning.hub.Common.Constant.DEVICE_BRAND;
import static soonflyy.learning.hub.Common.Constant.DEVICE_ID;
import static soonflyy.learning.hub.Common.Constant.DEVICE_MODEL;
import static soonflyy.learning.hub.Common.Constant.DEVICE_VERSION;
import static soonflyy.learning.hub.Common.Constant.ITUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_CITY;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_CITY;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_PINCODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_STATE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_UNIQUE_CODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGIN;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_PINCODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STATE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_UNIQUE_CODE;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;
import static soonflyy.learning.hub.utlis.AppConstant.SCHOOL_SECTION_TYPE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchooRegisterMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.School_CoaachingMainActivity;
import soonflyy.learning.hub.activity.MobileVerificationActivity;
import soonflyy.learning.hub.utlis.AppUtlis;
import soonflyy.learning.hub.utlis.SessionManagement;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SchoolLoginFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    EditText et_user_id, et_password;
    TextView forget_password_tv, login_tittle, tv_register;//,tv_register
    Button btnLogin;
    RelativeLayout rel_register;
    ImageView toggle_img;
    String login_type;
    boolean isPasswordVisible;
    View lin_it_header,lin_school_header;
    String login_id;
    SessionManagement session_management;

    String dont_account = "<p>If you don't have an account? <b><font color='blue'>Register</font></b></p>";


    public SchoolLoginFragment() {
        // Required empty public constructor
    }


    public static SchoolLoginFragment newInstance(String param1, String param2) {
        SchoolLoginFragment fragment = new SchoolLoginFragment();

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
        View view = inflater.inflate(R.layout.fragment_school_login, container, false);
        init(view);
        getArgumentData();
        initControl();
        //myObserve();
        addTextWatcherListener();
        return view;
    }

    private void getArgumentData() {
        login_type = getArguments().getString("login_type");
        if (login_type.equals("school")) {
            lin_school_header.setVisibility(View.VISIBLE);
            lin_it_header.setVisibility(View.GONE);
            ((SchoolLoginMainActivity) getActivity()).makeBottom_gone("visible","school");
        } else if (login_type.equals("i_tutor")) {
            lin_school_header.setVisibility(View.GONE);
            lin_it_header.setVisibility(View.VISIBLE);
            et_user_id.setHint("Enter your Email");
            ((SchoolLoginMainActivity) getActivity()).makeBottom_gone("gone","i_tutor");
        }
    }


    public void init(View view) {
        lin_it_header=view.findViewById(R.id.layout_idp_tutor);
        lin_school_header=view.findViewById(R.id.layout_scool_coaching);
        et_user_id = view.findViewById(R.id.et_id);
        et_password = view.findViewById(R.id.et_password);
        forget_password_tv = view.findViewById(R.id.forget_password_tv);
        btnLogin = view.findViewById(R.id.btnLogin);
        rel_register = view.findViewById(R.id.rel_register);
        toggle_img = view.findViewById(R.id.toggle_cofm_img);
        login_tittle = view.findViewById(R.id.login_tittle);
        tv_register = view.findViewById(R.id.tv_register);
        //tv_register.setText(Html.fromHtml(dont_account));
        session_management = new SessionManagement(getActivity());


    }

    private void addTextWatcherListener() {
        et_user_id.addTextChangedListener(new TextWatcher(et_user_id));
        et_password.addTextChangedListener(new TextWatcher(et_password));
    }


    public void initControl() {
        tv_register.setOnClickListener(this);
        forget_password_tv.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        toggle_img.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                goToSignUpScreen();
                break;
            case R.id.forget_password_tv:
                goToChangePassword();
                //goToVerificationScreen ();
                break;
            case R.id.btnLogin:
                //  goToVerificationScreen ();
                if (onValidation()) {
                    callLoginApi();
                }
                break;
            case R.id.toggle_cofm_img:
                showHidePassword();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (login_type.equals("school")){
        session_management.setString(SCHOOL_SECTION_TYPE,"");
        session_management.setString(LOGIN_PAGE_TYPE,"");
            checkLoginStatus();
       // }else{
            //check login status for school tutor


       // }
    }

    private void callLoginApi() {
        Log.e("SJVHGDHTXY",login_type);
        if (CommonMethods.checkInternetConnection(getActivity())) {
            if (login_type.equals("school")) {
                //call school login api
                sendRequest(ApiCode.SCHOOL_LOGIN);
            } else {
                //call for independent_tutor
                sendRequest(ApiCode.INDEPENDENT_TUTOR_LOGIN);
            }
        }
    }

    private void showHidePassword() {
        if (!isPasswordVisible) {
            AppUtlis.hideKeyboard(getActivity());
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggle_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_black_24dp));
            isPasswordVisible = true;
        } else {
            AppUtlis.hideKeyboard(getActivity());
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggle_img.setImageDrawable(getResources().getDrawable(R.drawable.hidden_password));
            isPasswordVisible = false;
        }
    }

    private void goToVerificationScreen() {
        if (onValidation()) {
            Intent i_login = new Intent(getActivity(), School_CoaachingMainActivity.class);
            startActivity(i_login);

        }

    }


    private void goToChangePassword() {
        Intent intent = new Intent(getActivity(), MobileVerificationActivity.class);
        intent.putExtra ("user_type", login_type);
        //Toast.makeText (this, ""+user_type, Toast.LENGTH_SHORT).show ( );
        startActivity(intent);

    }

    private void goToSignUpScreen() {
        Intent intent = new Intent(getActivity(), SchooRegisterMainActivity.class);
        if (login_type.equals("school")) {
            intent.putExtra("selected_tab", "school");
            intent.putExtra("register_type", "school");
        } else if (login_type.equals("i_tutor")) {
            intent.putExtra("selected_tab", " ");
            intent.putExtra("register_type", "i_tutor");
        }
        //startActivityForResult(intent, 1234);
        ((SchoolLoginMainActivity)getActivity()).launchSomeActivity.launch(intent);
//        Fragment fragment = new SchoolRegisterFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
//        // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
//        fragmentTransaction.replace(R.id.container_layout, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    public boolean onValidation() {
        String id = et_user_id.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            et_user_id.setError("Enter ID");
            et_user_id.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            et_password.setError("Enter password");
            et_password.requestFocus();
            return false;
        }
        if (password.length()<4){
            et_password.setError("Invalid password");
            et_password.requestFocus();
            return false;
        }
        return true;
    }


    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_LOGIN:
//                params.put("id", et_user_id.getText().toString().trim());
//                params.put("password", et_password.getText().toString().trim());
//                setDeviceInfo(params);
//                callApi(ApiCode.SCHOOL_LOGIN, params);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean loginValue=false;
                        String loginEmail="";
                        String loginPassword="";
                        String schoolId="";
                        String name="";
                        String mobile="";
                        String email="";
                        String state="";
                        String city="";
                        String image="";
                        String pincode="";
                        String uniqueCode="";

                        for(DataSnapshot ds:snapshot.getChildren()){
                            Log.e("LSNMNH",""+snapshot.getChildrenCount());
                            if(ds.child("type").exists()){
                                Log.e("LSNMNH",""+ds.child("type").getValue(String.class));

                                if(ds.child("type").getValue(String.class).equals("school")){
                                    if(ds.child("SchoolId").getValue(String.class).equals(et_user_id.getText().toString())){
                                        loginValue=true;
                                        loginEmail=ds.child("email").getValue(String.class);
                                        loginPassword=ds.child("password").getValue(String.class);
                                        schoolId=ds.child("SchoolId").getValue(String.class);
                                        name=ds.child("name").getValue(String.class);
                                        mobile=ds.child("mobile").getValue(String.class);
                                        state=ds.child("state").getValue(String.class);
                                        city=ds.child("city").getValue(String.class);
                                        image=ds.child("profile").getValue(String.class);
                                        uniqueCode=schoolId;

                                    }
                                }
                            }
                        }
                        if(loginValue){
                            if(loginPassword.equals(et_password.getText().toString())) {
                                String finalLoginEmail = loginEmail;
                                String finalSchoolId = schoolId;
                                String finalName = name;
                                String finalMobile = mobile;
                                String finalState = state;
                                String finalCity = city;
                                String finalImage = image;
                                String finalUniqueCode = uniqueCode;
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail ,
                                                et_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                createSessionForSchool(finalLoginEmail , finalSchoolId ,
                                                        finalName , finalMobile ,
                                                        finalState , finalCity , finalImage , finalUniqueCode,pincode);
                                                gotSchoolHomePage();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }else{
                                CommonMethods.showSuccessToast(getActivity(),"Incorrect Password...");
                            }
                        }else{
                            CommonMethods.showSuccessToast(getActivity(),"Id not found...");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.INDEPENDENT_TUTOR_LOGIN:
                params.put("id", et_user_id.getText().toString().trim());
                params.put("password", et_password.getText().toString().trim());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds:snapshot.getChildren()){
                            if(ds.child("type").exists()){
                                if(ds.child("type").getValue(String.class).equals("itutor")&&
                                        ds.child("email").getValue(String.class).equals(et_user_id.getText().toString())){
                                    if(ds.child("password").getValue(String.class).equals(et_password.getText().toString())){
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(et_user_id.getText().toString(),
                                                et_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                String loginEmail="";
                                                String loginPassword="";
                                                String schoolId="";
                                                String name="";
                                                String mobile="";
                                                String email="";
                                                String state="";
                                                String city="";
                                                String image="";
                                                String pincode="";
                                                String uniqueCode="";
                                              HashMap<String,Object> hashMap=new HashMap<>();
                                              hashMap.put("online",true);
                                              reference.child(FirebaseAuth.getInstance().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void unused) {

                                                      createLoginSessionForITuttor(ds.child("email").getValue(String.class),
                                                              ds.child("name").getValue(String.class),
                                                              ds.child("mobile").getValue(String.class),
                                                              ds.child("state").getValue(String.class),
                                                              ds.child("city").getValue(String.class),
                                                              ds.child("pincode").getValue(String.class),
                                                              ds.child("profile").getValue(String.class));
                                                      gotSchoolHomePage();
                                                  }
                                              });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                CommonMethods.showSuccessToast(getActivity(),e.getMessage());
                                            }
                                        });

                                    }else{
                                        CommonMethods.showSuccessToast(getActivity(),"Incorrect Password");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                callApi(ApiCode.INDEPENDENT_TUTOR_LOGIN, params);
                setDeviceInfo(params);
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
        switch (request) {
            case ApiCode.SCHOOL_LOGIN:
                service.postDataVolley(ApiCode.SCHOOL_LOGIN,
                        BaseUrl.URL_SCHOOL_LOGIN, params);
                Log.e("api", BaseUrl.URL_SCHOOL_LOGIN);
                Log.e("params", params.toString());
                break;
            case ApiCode.INDEPENDENT_TUTOR_LOGIN:
                service.postDataVolley(ApiCode.INDEPENDENT_TUTOR_LOGIN,
                        BaseUrl.URL_INDEPENDENT_TUTOR_LOGIN, params);
                Log.e("api", BaseUrl.URL_INDEPENDENT_TUTOR_LOGIN);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_LOGIN:
            case ApiCode.INDEPENDENT_TUTOR_LOGIN:
                Log.e("sc_login", response.toString());

                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                    if (login_type.equals("school")) {
                        //got to school home page
//                        createSessionForSchool(finalLoginEmail , finalSchoolId , finalName , finalMobile , finalState , finalCity , finalImage , finalUniqueCode);
                    gotSchoolHomePage();
//                        SchoolCoachingHomeFragment fragment_Fee = new SchoolCoachingHomeFragment();
//
//                        SwitchFragment(fragment_Fee);

                    } else {
                        //go to independet tutor page
//                        createLoginSessionForITuttor(jsonObject.getJSONObject("data"));
//                        gotSchoolHomePage();
//                        SchoolIndependentTutorHomeFragment fragment_indp_tutor = new SchoolIndependentTutorHomeFragment();
//                            SwitchFragment(fragment_indp_tutor);

                    }

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
        }
    }

    private void createLoginSessionForITuttor(String email,String name,String mobile,String state,String city,String pinCode,String profile) {

            session_management.setString(SCHOOL_IT_ID,FirebaseAuth.getInstance().getUid());
            session_management.setString(SCHOOL_IT_NAME,name);
            session_management.setString(SCHOOL_IT_MOBILE,mobile);
            session_management.setString(SCHOOL_IT_EMAIL,email);
            session_management.setString(SCHOOL_IT_CITY,city);
            session_management.setString(SCHOOL_IT_STATE,state);
            session_management.setString(SCHOOL_IT_PINCODE,pinCode);
            session_management.setString(SCHOOL_IT_IMAGE,profile);
            session_management.setString(SCHOOL_IT_UNIQUE_CODE,FirebaseAuth.getInstance().getUid());
            session_management.setBoolean(SCHOOL_IT_LOGIN_STATUS,true);
            session_management.setString(ITUTOR_ID_SECTION,"");


    }

    private void gotSchoolHomePage() {
        Intent intent=new Intent(getActivity(),SchoolMainActivity.class);
        intent.putExtra("login_type",login_type);
        startActivity(intent);
        //getActivity().finish();
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

    private void createSessionForSchool(String finalLoginEmail , String finalSchoolId , String finalName , String finalMobile , String finalState , String finalCity , String finalImage , String finalUniqueCode , String pincode) {

            session_management.setString(SCHOOL_ID,finalSchoolId);
            session_management.setString(SCHOOL_NAME,finalName);
            session_management.setString(SCHOOL_MOBILE,finalMobile);
            session_management.setString(SCHOOL_EMAIL,finalLoginEmail);
            session_management.setString(SCHOOL_STATE,finalState);
            session_management.setString(SCHOOL_CITY,finalCity);
            session_management.setString(SCHOOL_IMAGE,finalImage);
            session_management.setString(SCHOOL_PINCODE,pincode);
            session_management.setString(SCHOOL_UNIQUE_CODE,finalUniqueCode);
            session_management.setString(SCHOOL_LOGIN_STATUS,"1");
            session_management.setBoolean(SCHOOL_LOGIN,true);
            session_management.setString(SCHOOL_COACHING_ID_SECTION,"0");

    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void checkLoginStatus(){
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Checking your login credential\nPlease wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (login_type.equals("school")) {
                    if (session_management.getBoolean(SCHOOL_LOGIN)) {

                        gotSchoolHomePage();
                        progressDialog.dismiss();
                    }else {

                            progressDialog.dismiss();
                    }
                }else{
                    //for tutor login
                    if (session_management.getBoolean(SCHOOL_IT_LOGIN_STATUS)){
                        gotSchoolHomePage();
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                    }
                }
            }
        },2000);

    }




}
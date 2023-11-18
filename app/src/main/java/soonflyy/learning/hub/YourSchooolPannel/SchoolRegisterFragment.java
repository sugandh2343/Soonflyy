package soonflyy.learning.hub.YourSchooolPannel;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class SchoolRegisterFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    EditText et_name,et_mobile_num,et_email,et_city,et_state,et_pincode,et_password,et_confm_password;
    LinearLayout lin_student_info,lin_teaher_info;
    TextView signup_title,tv_login_now;
    Button btn_signup;
    RelativeLayout rel_login_now;
    String register_type;
    View lin_header_school,lin_header_it;
    private static final int Location_Request_code = 100;
    private final int REQUEST_IMAGE = 400;
    private String[] locationPermissions;
    private LocationManager locationManager;
    Boolean result;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth firebaseAuth;
    int countLoop=0;

    public SchoolRegisterFragment() {
        // Required empty public constructor
    }


    public static SchoolRegisterFragment newInstance(String param1, String param2) {
        SchoolRegisterFragment fragment = new SchoolRegisterFragment();

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
        View view = inflater.inflate(R.layout.fragment_school_register, container, false);
        init(view);
        if (checkLocationPermission()) {
            detectLocation();
        } else {
            requestLocationPermission();
        }
        getArgumentData();

        initControl();

        addTextchangeListener();
        return view;
    }



    private void getArgumentData() {
register_type=getArguments().getString("register_type");

    }
    private Boolean checkLocationPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
        return result;

    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), locationPermissions, Location_Request_code);
        checkLocationPermission();

    }
    private void detectLocation() {

        locationManager = (LocationManager)getActivity(). getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        try{
                            Location location = task.getResult();
                            if (location != null) {

                                try {
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1


                                    );

                                   double latitude = addresses.get(0).getLatitude();
                                   double longitude = addresses.get(0).getLongitude();
                                    if(addresses.get(0).getLocality()!=null){
                                        et_city.setText(addresses.get(0).getLocality());
                                        et_state.setText(addresses.get(0).getAdminArea());
                                        et_pincode.setText(addresses.get(0).getPostalCode());


                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                            }
                        }catch (Exception e){



                        }



                    }
                });
            }
        }
    }





    public void onResume() {
        super.onResume();
        signup_title.setText("Register Please");
        if (register_type.equals("school")){
            lin_header_school.setVisibility(View.VISIBLE);
            lin_header_it.setVisibility(View.GONE);
            et_name.setHint("School/Coaching Name");

        }else{
            et_name.setHint("Name");
            lin_header_school.setVisibility(View.GONE);
            lin_header_it.setVisibility(View.VISIBLE);
        }
    }


    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        Fragment fragment = new SchoolLoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void init(View view) {
        lin_header_it = view.findViewById(R.id.lin_indp_tutor);
        lin_header_school = view.findViewById(R.id.layout_school_header);
        signup_title = view.findViewById(R.id.signup_title);
        et_name =  view.findViewById(R.id.et_name);
        et_mobile_num =  view.findViewById(R.id.et_mobile_num);
        et_email =  view.findViewById(R.id.et_email);
        rel_login_now = view.findViewById(R.id.rel_login_now);
        tv_login_now= view.findViewById(R.id.tv_login_now);
        btn_signup =  view.findViewById(R.id.btn_signup);
        lin_student_info= view.findViewById(R.id.lin_student_info);
        lin_teaher_info= view.findViewById(R.id.lin_teacher_info);
        et_city= view.findViewById(R.id.et_city);
        et_state= view.findViewById(R.id.et_state);
        et_pincode= view.findViewById(R.id.pincode);
        et_confm_password= view.findViewById(R.id.et_confm_password);
        et_password= view.findViewById(R.id.et_password);
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        firebaseAuth=FirebaseAuth.getInstance();





    }
    private void addTextchangeListener() {
        et_name.addTextChangedListener(new TextWatcher(et_name));
        et_mobile_num.addTextChangedListener(new TextWatcher(et_mobile_num));
        et_email.addTextChangedListener(new TextWatcher(et_email));
    }

    public void initControl() {
        btn_signup.setOnClickListener(this);
        tv_login_now.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                if (validateEmptyField()){
                    callRegisterApi();
                }
               // goToLoginActivity();
                break;

            case R.id.tv_login_now:
                goForLogin();
              //  onBackPressed();
              //  onBackPressed();
                break;

        }
    }
    private void goForLogin(){
        Intent intent=new Intent();
        intent.putExtra("regType",register_type);
        getActivity().setResult(Activity.RESULT_OK,intent);
        ((SchooRegisterMainActivity)getActivity()).finish();
    }

    private void callRegisterApi() {
        if (ConnectivityReceiver.isConnected()) {
            Log.e("RegisterType",register_type);
            if (register_type.equals("school")) {
                //call api for school coaching registration
                HashMap<String, String> params = new HashMap<>();
                String name=et_name.getText().toString();
                String mobile=et_mobile_num.getText().toString();
                String schoolName="",mobileNum="";
                int countSchool=0,countMobile=0;
                for(int i=0;i<name.length();i++){
                    if(countSchool<4){
                        char c=name.charAt(i);
                        if(c!=' '){
                            countSchool++;
                            schoolName=schoolName+c;
                        }
                    }
                }
                for(int j=mobile.length()-4;j<mobile.length();j++){
                    char c=mobile.charAt(j);
                    if(c!=' '){
                        mobileNum=mobileNum+c;
                    }
                }
                String schoolid=schoolName+mobileNum;
                Log.e("SchoolId",schoolid);
                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("SchoolId",schoolid);
                params.put("email",et_email.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("city",et_city.getText().toString().trim());
                params.put("state",et_state.getText().toString().trim());
                params.put("pincode",et_pincode.getText().toString().trim());
                params.put("password",et_password.getText().toString().trim());
                params.put("type","school");

                checkUserIdPresent(schoolid,schoolName,mobileNum,name,mobile,params);


//                sendRequest(ApiCode.SCHOOL_REGISTRATION);

            } else {
                //call api for independent tutor registeration
                sendRequest(ApiCode.INDEPENDENT_TUTOR_REGISTRATION);
            }
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void checkUserIdPresent(String schoolid , String schoolName , String mobileNum , String name , String mobile , HashMap<String, String> params) {
        countLoop++;
        final String[] schoolId = {schoolid};
         result=false;
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(ds.child("SchoolId").getValue(String.class)!=null){
                        if(ds.child("SchoolId").getValue(String.class).equals(schoolId[0])){
                            if(ds.child("name").getValue(String.class).equals(et_name.getText().toString())
                                    && ds.child("mobile").getValue(String.class).equals(et_mobile_num.getText().toString() )){
                                result=true;
                            }else{
                                String newMobile=mobileNum+countLoop;
                                schoolId[0] =schoolName+mobileNum;
                                checkUserIdPresent(schoolId[0],schoolName,newMobile,name,mobile , params);
                            }
                        }
                    }
                }
                if(result){
                    CommonMethods.showSuccessToast(getActivity(),"School with same name and mobile already exists");

                }else{
                    firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                params.put("uid",task.getResult().getUser().getUid());
                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(task.getResult().getUser().getUid()).setValue(params)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                CommonMethods.showSuccessToast(getActivity(),"School Registration Successful");
                                                firebaseAuth.signOut();
                                                goForLogin();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getActivity() , ""+e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public boolean validateEmptyField(){
        String name = et_name.getText().toString().trim();
        String mobile_num = et_mobile_num.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String city=et_city.getText().toString().trim();
        String state=et_state.getText().toString().trim();
        String pincode=et_pincode.getText().toString().trim();
        String password=et_password.getText().toString().trim();
        String confirmPassword=et_confm_password.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
           et_name.setError("Enter name");
           et_name.requestFocus();
            return false; }
        if(name.length()<5){
            et_name.setError("Name must contain atleast 4 characters");
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
            et_email.setError("Enter email-ID");
            et_email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Invalid email-ID");
            et_email.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(city)){
            et_city.setError("Enter city");
            et_city.requestFocus();
            return false; }
        if(TextUtils.isEmpty(state)){
            et_state.setError("Enter state");
            et_state.requestFocus();
            return false; }
        if(TextUtils.isEmpty(pincode)){
            et_pincode.setError("Enter pin code");
            et_pincode.requestFocus();
            return false; }
        if(pincode.length()!=6 || pincode.equals("000000")||pincode.charAt(0)=='0'){
            et_pincode.setError("Invalid pin code");
            et_pincode.requestFocus();
            return false; }
        if(TextUtils.isEmpty(password)){
            et_password.setError("Enter Password");
            et_password.requestFocus();
        }
        if(TextUtils.isEmpty(confirmPassword)){
            et_confm_password.setError("Enter Confirm Password");
            et_confm_password.requestFocus();
        }
        if(!password.equals(confirmPassword)){
            et_confm_password.setError("Passwords do not match");
            et_confm_password.requestFocus();
        }

        return true;
    }
    private void goToLoginActivity() {
//        if( onValidation()){
            showUniqueIDDailoge();
      //  }

    }

    private void showUniqueIDDailoge() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_unique_id);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);

        Button btn_continue = dialog.findViewById(R.id.btn_continue);

        dialog.show();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment = new SchoolLoginFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
//                // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
//                fragmentTransaction.replace(R.id.container_layout, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                dialog.dismiss();
                goForLogin();
                //((SchooRegisterMainActivity)getActivity()).finish();

            }
        });



        dialog.setCanceledOnTouchOutside(false);
    }



    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.SCHOOL_REGISTRATION:
                String name=et_name.getText().toString();
                String mobile=et_mobile_num.getText().toString();
                String schoolName="",mobileNum="";
                int countSchool=0,countMobile=0;
                for(int i=0;i<name.length();i++){
                    if(countSchool<3){
                        char c=name.charAt(i);
                        if(c!=' '){
                            countSchool++;
                            schoolName=schoolName+c;
                        }
                    }
                }
                for(int j=mobile.length()-4;j<mobile.length();j++){
                    char c=name.charAt(j);
                    if(c!=' '){
                        mobileNum=mobileNum+c;
                    }
                }
                String schoolid=schoolName+mobileNum;
                Log.e("SchoolId",schoolid);
                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("city",et_city.getText().toString().trim());
                params.put("state",et_state.getText().toString().trim());
                params.put("pincode",et_pincode.getText().toString().trim());
                params.put("password",et_password.getText().toString().trim());
                params.put("type","school");
//                firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_password.getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
//                                reference.child(firebaseAuth.getUid()).setValue(params)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                CommonMethods.showSuccessToast(getActivity(),"School Registration Successful");
//                                                goForLogin();
//                                            }
//                                        });
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                                Toast.makeText(getActivity() , ""+e.getMessage() , Toast.LENGTH_SHORT).show();
//                            }
//                        });
                



                break;
            case ApiCode.INDEPENDENT_TUTOR_REGISTRATION:
                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("city",et_city.getText().toString().trim());
                params.put("state",et_state.getText().toString().trim());
                params.put("pincode",et_pincode.getText().toString().trim());
                params.put("password",et_password.getText().toString().trim());
                params.put("type","itutor");
//                callApi(ApiCode.INDEPENDENT_TUTOR_REGISTRATION, params);
                firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString().trim(),et_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                       firebaseAuth.signOut();
                                       goForLogin();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                             CommonMethods.showSuccessToast(getActivity(),e.getMessage());
                            }
                        });
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_REGISTRATION:
                service.postDataVolley(ApiCode.SCHOOL_REGISTRATION,
                        BaseUrl.URL_SCHOOL_REGISTRATION, params);
                Log.e("api",BaseUrl.URL_SCHOOL_REGISTRATION);
                Log.e("params",params.toString());
                break;
            case ApiCode.INDEPENDENT_TUTOR_REGISTRATION:
                service.postDataVolley(ApiCode.INDEPENDENT_TUTOR_REGISTRATION,
                        BaseUrl.URL_INDEPENDENT_TUTOR_REGISTRATION, params);
                Log.e("api",BaseUrl.URL_INDEPENDENT_TUTOR_REGISTRATION);
                Log.e("params",params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_REGISTRATION:
            case ApiCode.INDEPENDENT_TUTOR_REGISTRATION:
                    Log.e("sc_register",response.toString());
                    if (jsonObject.getBoolean("response")){
                        showUniqueIDDailoge();
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

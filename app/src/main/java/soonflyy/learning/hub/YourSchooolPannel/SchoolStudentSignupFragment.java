package soonflyy.learning.hub.YourSchooolPannel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchooRegisterMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.TextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class SchoolStudentSignupFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, AdapterView.OnItemSelectedListener {

    EditText et_name,et_mobile_num,et_email,et_father_name,et_address,et_unique_id;
    LinearLayout lin_student_info,lin_teaher_info,linClass_section;
    TextView signup_title,tv_dob,tvVerify,tv_class, tv_section,tvLogin;
    Spinner section_spinner, class_spinner;
    Button btn_signup;
    RelativeLayout rel_login_now;
    String register_type;
    View header_school,header_student;

    //--class and section-----------//
    ArrayList<AllClassesModel> classList = new ArrayList<>();
    ArrayList<AllClassesModel> sectionList = new ArrayList<>();
    ArrayAdapter<AllClassesModel> class_adapter, section_adapter;
    String  school_id, class_id, section_id;
    boolean isIdVerified=false;
    //----------------------------//

    public SchoolStudentSignupFragment() {
        // Required empty public constructor
    }


    public static SchoolStudentSignupFragment newInstance(String param1, String param2) {
        SchoolStudentSignupFragment fragment = new SchoolStudentSignupFragment();

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
        View view= inflater.inflate(R.layout.fragment_school_student_signup, container, false);
        init(view);
        getFromIntentData();
        initControl();
        setSpinnerData();
        addTextchangeListener();
        return view;
    }



    private void getFromIntentData() {
        register_type=getArguments().getString("register_type");
        if (register_type.equals("s_tutor")){
            et_unique_id.setVisibility(View.VISIBLE);
            tvVerify.setVisibility(View.GONE);
            linClass_section.setVisibility(View.GONE);
            header_student.setVisibility(View.GONE);
            header_school.setVisibility(View.VISIBLE);

        }else{
            et_unique_id.setVisibility(View.VISIBLE);
            //tvVerify.setVisibility(View.VISIBLE);
            linClass_section.setVisibility(View.VISIBLE);
            header_school.setVisibility(View.GONE);
            header_student.setVisibility(View.VISIBLE);
        }
    }


    public void onResume() {
        super.onResume();
        signup_title.setText("Register Please");
    }

    public void init(View view) {
        tvLogin=view.findViewById(R.id.tv_s_login);
        linClass_section=view.findViewById(R.id.lin_class_section);
        tvVerify=view.findViewById(R.id.tv_verify);
        header_school=view.findViewById(R.id.layout_school);
        header_student=view.findViewById(R.id.layout_student);
        signup_title = view.findViewById(R.id.signup_title);
        et_father_name =  view.findViewById(R.id.et_father);
        et_unique_id =  view.findViewById(R.id.et_unique_id);

        et_name =  view.findViewById(R.id.et_name);
        et_mobile_num =  view.findViewById(R.id.et_mobile_num);
        et_email =  view.findViewById(R.id.et_email);
        rel_login_now= view.findViewById(R.id.rel_login_now);

        btn_signup =  view.findViewById(R.id.btn_signup);

        lin_student_info= view.findViewById(R.id.lin_student_info);
        lin_teaher_info= view.findViewById(R.id.lin_teacher_info);
        et_address= view.findViewById(R.id.et_address);
        tv_dob= view.findViewById(R.id.tv_dob);

        tv_class = view.findViewById(R.id.tv_class);
        tv_section = view.findViewById(R.id.tv_section);
        class_spinner = view.findViewById(R.id.class_spinner);
        section_spinner = view.findViewById(R.id.section_spinner);

    }
    private void setSpinnerData() {
        //for class spinner
        classList.add(new AllClassesModel());
        class_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, classList);
        class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        class_spinner.setAdapter(class_adapter);

        //for section spinner
        sectionList.add(new AllClassesModel());
        section_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, sectionList);
        section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        section_spinner.setAdapter(section_adapter);


    }
    private void addTextchangeListener() {
        et_name.addTextChangedListener(new TextWatcher(et_name));
        et_mobile_num.addTextChangedListener(new TextWatcher(et_mobile_num));
        et_email.addTextChangedListener(new TextWatcher(et_email));

        et_unique_id.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!register_type.equals("s_tutor")) {
                            /// for student
                            if (!TextUtils.isEmpty(s.toString())) {
                                if (s.toString().length()==6) {
                                    tvVerify.setVisibility(View.VISIBLE);
                                    isIdVerified=false;
                                    setClassSection();
                                }else{
                                    tvVerify.setVisibility(View.GONE);
                                    isIdVerified=false;
                                    setClassSection();
                                }
                            }else{
                                tvVerify.setVisibility(View.GONE);
                                isIdVerified=false;
                                setClassSection();
                            }
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClassSection() {
        classList.clear();
        sectionList.clear();
        tv_class.setText("");
        tv_section.setText("");
        class_id="";
        section_id="";
        setSpinnerData();
    }

    public void initControl() {
        btn_signup.setOnClickListener(this);
        rel_login_now.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tv_dob.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
        tv_class.setOnClickListener(this);
        tv_section.setOnClickListener(this);
        class_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                if (validateEmptyField()){
                    if (ConnectivityReceiver.isConnected()){
                        callRegistrationApi();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }
               // goToLoginActivity();
                break;
            case R.id.tv_dob:
               // showDateChooser();
                showDatePicker();
                break;

            //case R.id.rel_login_now:
            case R.id.tv_s_login:
              goForLogin();
              break;
              /*  Fragment fragment = new SchoolLoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
                // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
               // fragmentTransaction.replace(R.id.frame_school, fragment);
                 fragmentTransaction.replace(R.id.container_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

               */
            case R.id.tv_verify:
                //call verify button
                verifyId();
                break;
            case R.id.tv_class:
                class_spinner.performClick();
                break;
            case R.id.tv_section:
                section_spinner.performClick();
                break;

        }
    }

    private void verifyId() {
        String id=et_unique_id.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            et_unique_id.requestFocus();
            et_unique_id.setError("Enter ID");
        }else if (id.length()!=6){
            et_unique_id.setError("Invalid ID");
            et_unique_id.requestFocus();
        }else{
            //call api for verify
            if (CommonMethods.checkInternetConnection(getActivity())){
                sendRequest(ApiCode.VERIFY_UNIQUE_ID);
            }
        }
    }

    private void goForLogin(){
        Intent intent=new Intent();
        intent.putExtra("regType",register_type);
        getActivity().setResult(Activity.RESULT_OK,intent);
        ((SchooRegisterMainActivity)getActivity()).finish();
    }

    private void callRegistrationApi() {
        if (register_type.equals("student")){
            //call api for student registration
            sendRequest(ApiCode.SCHOOL_STUDENT_REGISTER);
        }else{
            //call api for school tutor registration
            sendRequest(ApiCode.SCHOOL_TUTOR_REGISTER);
        }
    }


    public boolean validateEmptyField(){
        String id = et_unique_id.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String mobile_num = et_mobile_num.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String father=et_father_name.getText().toString().trim();
        String dob=tv_dob.getText().toString().trim();
        String address=et_address.getText().toString().trim();

       // if (register_type.equals("s_tutor")) {
            if (TextUtils.isEmpty(id)) {
                et_unique_id.setError("Enter ID");
                et_unique_id.requestFocus();
                return false;
            }
      //  }


        if (!register_type.equals("s_tutor")){
            //for student
            if (!isIdVerified){
                CommonMethods.showSuccessToast(getContext(),"Please verify ID");
                tvVerify.requestFocus();
                return false;
            }
        }

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
            et_email.setError("Enter email-ID");
            et_email.requestFocus();
            return false;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Invalid email-ID");
            et_email.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(father)){
            et_father_name.setError("Enter father name");
            et_father_name.requestFocus();
            return false; }
        if(TextUtils.isEmpty(dob)){
            CommonMethods.showSuccessToast(getContext(),"Select DOB");
            return false; }

        if (!register_type.equals("s_tutor")){
            //for student
            if (TextUtils.isEmpty(tv_class.getText().toString().trim())){
                CommonMethods.showSuccessToast(getContext(),"Select Class");
               tv_class.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(tv_section.getText().toString().trim())){
                CommonMethods.showSuccessToast(getContext(),"Select Section");
                tv_section.requestFocus();
                return false;
            }
        }

        if(TextUtils.isEmpty(address)){
            et_address.setError("Enter address");
            et_address.requestFocus();
            return false; }

        return true;
    }
    private void goToLoginActivity() {
        if( validateEmptyField()){
        showUniqueIDDailoge();
          }

    }
    private void showDatePicker() {
        boolean isValidDate=false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getContext(),
                R.color.graient2));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getContext(),
                R.color.graient2));

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
                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"Please select a date before current date");
                    }
                }
            }

        });
        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidDOB(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDateChooser() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                String dob = day + "-" + month + "-" + i;
                tv_dob.setText(dob);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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
//                Fragment fragment = new SchoolTutorHomeFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
//                // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
//                //fragmentTransaction.replace(R.id.frame_school, fragment);
//                fragmentTransaction.replace(R.id.container_layout, fragment);
//
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                dialog.dismiss();
                ((SchooRegisterMainActivity)getActivity()).finish();

            }
        });



        dialog.setCanceledOnTouchOutside(false);
    }




    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        switch (request){
            case ApiCode.SCHOOL_TUTOR_REGISTER:

                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("father_name",et_father_name.getText().toString().trim());
                params.put("dob",tv_dob.getText().toString().trim());
                params.put("address",et_address.getText().toString().trim());
                params.put("unique_code",et_unique_id.getText().toString().trim());
                params.put("type", "1");
                params.put("status","Pending");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String schoolUid="";
                        for(DataSnapshot ds:snapshot.getChildren()){
                            if(ds.child("SchoolId").exists()){
                                if(ds.child("SchoolId").getValue(String.class).toLowerCase(Locale.ENGLISH)
                                        .equals(et_unique_id.getText().toString().toLowerCase(Locale.ENGLISH))){
                                    schoolUid=ds.getKey();
                                }
                            }
                        }
                        Log.e("SchoolSignUp",schoolUid);
                        if(schoolUid.equals("")){
                            CommonMethods.showSuccessToast(getActivity(),"School Id not found");
                        }else{
                            String finalSchoolUid = schoolUid;
                            reference.child(schoolUid).child("Tutors").child(et_mobile_num.getText().toString())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                if(snapshot.child("status").getValue(String.class).equals("Pending")){
                                                    CommonMethods.showSuccessToast(getActivity(),"You have already requested to this school. Wait for their Approval");
                                                }else if(snapshot.child("status").getValue(String.class).equals("Approved")){
                                                    CommonMethods.showSuccessToast(getActivity(),"You are already a tutor in this school. Login with your mobile number");
                                                }else{
                                                    reference.child(finalSchoolUid).child("Tutors").child(et_mobile_num.getText().toString()).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            CommonMethods.showSuccessToast(getActivity(),"Request has been sent to school successfully for Approval.");
                                                        }
                                                    });
                                                }
                                            }else{
                                                reference.child(finalSchoolUid).child("Tutors").child(et_mobile_num.getText().toString()).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        CommonMethods.showSuccessToast(getActivity(),"Request has been sent to school successfully for Approval.");
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                callApi(ApiCode.SCHOOL_TUTOR_REGISTER, params);
                break;
            case ApiCode.SCHOOL_STUDENT_REGISTER:
                params.put("mobile",et_mobile_num.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("father_name",et_father_name.getText().toString().trim());
                params.put("dob",tv_dob.getText().toString().trim());
                params.put("address",et_address.getText().toString().trim());
                params.put("type", "0");
                params.put("class",class_id);
                params.put("section",section_id);
                params.put("school_id",school_id);
                callApi(ApiCode.SCHOOL_STUDENT_REGISTER, params);
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                params.put("class_id", class_id);

                callApi(ApiCode.SCHOOL_GET_SECTION, params);
                break;
            case ApiCode.VERIFY_UNIQUE_ID:
                params.put("uniqueid", et_unique_id.getText().toString().trim());
                callApi(ApiCode.VERIFY_UNIQUE_ID, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_TUTOR_REGISTER:
                service.postDataVolley(ApiCode.SCHOOL_TUTOR_REGISTER,
                        BaseUrl.URL_SCHOOL_TUTOR_REGISTER, params);
                Log.e("api",BaseUrl.URL_SCHOOL_TUTOR_REGISTER);
                Log.e("params",params.toString());
                break;
            case ApiCode.SCHOOL_STUDENT_REGISTER:
                service.postDataVolley(ApiCode.SCHOOL_STUDENT_REGISTER,
                        BaseUrl.URL_SCHOOL_STUDENT_REGISTER, params);
                Log.e("api",BaseUrl.URL_SCHOOL_STUDENT_REGISTER);
                Log.e("params",params.toString());
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION,
                        BaseUrl.URL_SCHOOL_GET_SECTION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SECTION);
                Log.e("params", params.toString());
                break;
            case ApiCode.VERIFY_UNIQUE_ID:
                service.postDataVolley(ApiCode.VERIFY_UNIQUE_ID,
                        BaseUrl.URL_VERIFY_UNIQUE_ID, params);
                Log.e("api", BaseUrl.URL_VERIFY_UNIQUE_ID);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_TUTOR_REGISTER:
            case ApiCode.SCHOOL_STUDENT_REGISTER:
                Log.e("sc_tutor_regi",response.toString());
                if (jsonObject.getBoolean("response")){
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                    //((SchooRegisterMainActivity)getActivity()).finish();
                    goForLogin();
                }else{
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));

                }

//
                break;

            case ApiCode.SCHOOL_GET_SECTION:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        sectionList.addAll(psearch);
                        section_adapter.notifyDataSetChanged();
                    }
                }
////
                break;
            case ApiCode.VERIFY_UNIQUE_ID:
                setClassSection();
                Log.e("uniqueResp", response.toString());
                if(jsonObject.getBoolean("response")){
                    CommonMethods.showSuccessToast(getActivity(),"ID Verified");
                    isIdVerified=true;
                    tvVerify.setVisibility(View.GONE);
                    et_unique_id.clearFocus();
                   // JSONObject dataObject=jsonObject.getJSONObject("data");
                    school_id=jsonObject.getString("school_id");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        classList.clear();
                        classList.add(new AllClassesModel());
                        classList.addAll(psearch);
                        class_adapter.notifyDataSetChanged();
                    }
                }else{
                    isIdVerified=true;
                   // CommonMethods.showSuccessToast(getActivity(),"Invalid ID");
                    et_unique_id.requestFocus();
                    et_unique_id.setError("Invalid ID");
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            switch (parent.getId()) {
                case R.id.class_spinner:
                    AllClassesModel model = classList.get(position);
                    tv_class.setText(model.getName());
                    class_id = model.getClass_id();
                    section_id = "";
                    sectionList.clear();
                    sectionList.add(new AllClassesModel());
                    tv_section.setText("");
                    section_adapter.notifyDataSetChanged();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

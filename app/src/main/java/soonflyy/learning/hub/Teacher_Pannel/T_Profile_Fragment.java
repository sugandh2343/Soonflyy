package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.CITY;
import static soonflyy.learning.hub.Common.Constant.DISTRICT;
import static soonflyy.learning.hub.Common.Constant.EMAIL;
import static soonflyy.learning.hub.Common.Constant.INSTITUTE;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.PROFILE_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.STATE;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.Common.Constant.WORKPLACE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.SplashActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class T_Profile_Fragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {

    LinearLayout lin_personalDetails,lin_performance,lin_subscriptions;
    ImageView iv_back;
    SwipeRefreshLayout refreshLayout;
    EditText et_name,et_mobile,et_email,et_fatherName,et_workPlace,et_city,et_dist,et_state;
    TextView tv_dob,tv_name,tv_mobile,tv_sub_count;
    CircleImageView iv_profile,iv_edit;
    Button update_btn,logout_btn;
    RadioGroup gender_group;
    RadioButton btn_male,btn_female;
    SessionManagement management ;
    int gender=-1;
    String imageString="";
    FirebaseAuth firebaseAuth;
    Uri imgUri;
    Uri downloadImageUri;

    public T_Profile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_t__profile_, container, false);
        bindViewId(view);
        management = new SessionManagement(getContext());
        setHeader();

        if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.GET_USER_DETAILS);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
        iv_back.setOnClickListener(this);
        lin_subscriptions.setOnClickListener(this);
        lin_personalDetails.setOnClickListener(this);
        lin_performance.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        iv_edit.setOnClickListener(this);
        return view;
    }


    private void setHeader() {
        Log.e("MMAASS",""+management.getString(PROFILE_IMAGE));
        if(management.getString(PROFILE_IMAGE)!=null &&!management.getString(PROFILE_IMAGE).equals("")){
            Picasso.get().load(management.getString(PROFILE_IMAGE))
                    .placeholder(R.drawable.profile)
                    .into(iv_profile);
        }else{
            iv_profile.setImageResource(R.drawable.profile);
        }

        tv_name.setText(management.getString(INSTITUTE));
        tv_mobile.setText("+91-"+management.getString(MOBILE));
    }

    private void bindViewId(View view) {
        iv_edit=view.findViewById(R.id.iv_edit);
        lin_performance=view.findViewById(R.id.lin_performance);
        lin_personalDetails=view.findViewById(R.id.lin_personal_details);
        lin_subscriptions=view.findViewById(R.id.lin_subscription);
        logout_btn=view.findViewById(R.id.btn_logout);
        iv_back=view.findViewById(R.id.iv_back_icon);
        iv_profile=view.findViewById(R.id.iv_profile);
        tv_name=view.findViewById(R.id.assign_tv_name);
        tv_mobile=view.findViewById(R.id.assign_tv_mobile);
        tv_sub_count=view.findViewById(R.id.tv_sub_count);
        refreshLayout=view.findViewById(R.id.refresh_layout);
        firebaseAuth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_personal_details:
                //
//                if (ConnectivityReceiver.isConnected()){
//                    sendRequest(ApiCode.GET_USER_DETAILS);
//                }else{
//                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
//                }
                showPsDetailsDialog();
                break;
            case R.id.lin_performance:
                gotoPerformance();
                break;
            case R.id.lin_subscription:
                gotoSubcription();
                break;
            case R.id.iv_back_icon:
                ((TeacherMainActivity)getActivity()).onBackPressed();
                break;
            case R.id.iv_profile:
            case R.id.iv_edit:
                chooseProfileImage();
                break;
            case R.id.btn_logout:
//                showLogoutAlert();
                CommonMethods.showLogoutDialog(getActivity(),SIMPLEE_HOME_TUTOR);
                break;
        }
    }
    private void showLogoutAlert(){
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to logout ?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent ( getActivity(), SplashActivity.class);
                management.logoutSession ();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity (intent);
                getActivity().finish ();
            }
        }).show();
    }
    private void chooseProfileImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode==RESULT_OK){
            try {
                imgUri = data.getData();
                Glide.with(getActivity()).load(imgUri).into(iv_profile);

                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                //byte[] imgData = stream.toByteArray();
                // byte[] encoded_data = Base64.encode(imgData,0);
                // imgString = new String(encoded_data);
                imageString= Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.UPDATE_PROFILE_IMAGE);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((TeacherMainActivity)getActivity()).getSupportActionBar().show();
    }

    private void gotoSubcription() {
    }

    private void gotoPerformance() {
        ((TeacherMainActivity)getActivity()).SwitchFragment(new ScreenshotPolicyFragment());
    }

    private void showPsDetailsDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.dialog_student_profile,null);
        builder.setView(view);
        ImageView cancel=view.findViewById(R.id.cancel_btn);
        LinearLayout lin_studentfield=view.findViewById(R.id.layout_stdentfield);
        LinearLayout lin_teacherField=view.findViewById(R.id.lin_teacher_info);
        LinearLayout lin_gender=view.findViewById(R.id.lin_gender);
        et_name=view.findViewById(R.id.et_name);
        et_mobile=view.findViewById(R.id.et_mobile_num);
        et_email=view.findViewById(R.id.et_email);
        et_fatherName=view.findViewById(R.id.et_father);
        et_city=view.findViewById(R.id.et_city);
        et_dist=view.findViewById(R.id.et_district);
        et_state=view.findViewById(R.id.et_state);

        et_workPlace=view.findViewById(R.id.et_workplace);
        tv_dob=view.findViewById(R.id.tv_dob);
        update_btn=view.findViewById(R.id.btn_update);
//        btn_male=view.findViewById(R.id.competitive_rbtn);
//        btn_female=view.findViewById(R.id.academic_rbtn);
        gender_group=view.findViewById(R.id.coruse_radio_group);
        lin_studentfield.setVisibility(View.GONE);
        lin_gender.setVisibility(View.GONE);
        lin_teacherField.setVisibility(View.VISIBLE);
        setData();

//        gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.academic_rbtn:
//                        gender =0;
//                        Log.e("checked course: ","Academic");
//                        break;
//                    case R.id.competitive_rbtn:
//                        gender =1;
//                        Log.e("checked course: ","Competitive");
//                        break;
//                }
//            }
//        });
//        tv_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePicker();
//            }
//        });

        builder.setCancelable(false);
        Dialog dialog=builder.create();
        dialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    //call update profile api
                    if (ConnectivityReceiver.isConnected()){
                        sendRequest(ApiCode.UPDATE_PROFILE);
                        dialog.dismiss();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }
            }
        });


    }

    private void setData() {
        et_name.setText(management.getString(INSTITUTE));
        et_mobile.setText(management.getString(MOBILE));
        et_email.setText(management.getString(EMAIL));
        et_city.setText(management.getString(CITY));
        et_dist.setText(management.getString(DISTRICT));
        et_state.setText(management.getString(STATE));
       // et_fatherName.setText(management.getString(FATHER));
        et_workPlace.setText(management.getString(WORKPLACE));
       // tv_dob.setText(management.getString(DOB));
      //  gender = Integer.parseInt(management.getString(GENDER));
//        if (gender==0){
//            btn_male.setChecked(true);
//        }else{
//            btn_female.setChecked(true);
//        }
    }
    private void showDatePicker() {
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
                String dob = day + "/" + month + "/" + i;
                tv_dob.setText(dob);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private boolean validateData() {
        String name=et_name.getText().toString().trim();
        String mobile=et_mobile.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        String city=et_city.getText().toString().trim();
        String dist=et_dist.getText().toString().trim();
        String state=et_state.getText().toString().trim();
        String workplace=et_workPlace.getText().toString().trim();

        boolean result=true;
        if (TextUtils.isEmpty(name)){
            et_name.setError("Enter name");
            et_name.requestFocus();
            result=false;
        }else  if (TextUtils.isEmpty(mobile)){
            et_mobile.setError("Enter mobile");
            et_mobile.requestFocus();
            result=false;
        }else  if (mobile.length()!=10 ||
                Integer.parseInt(String.valueOf(mobile.charAt (0))) < 6) {
            et_mobile.setError("Enter valid mobile number");
            et_mobile.requestFocus();
            result = false;
        }else if (TextUtils.isEmpty(email)){
            et_email.setError("Enter email");
            et_email.requestFocus();
            result=false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Invalid email");
            et_email.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(city)){
            et_city.setError("Enter city");
            et_city.requestFocus();
            result=false;
        }else if (TextUtils.isEmpty(dist)){
            et_dist.setError("Enter district");
            et_dist.requestFocus();
            result=false;
        }else if (TextUtils.isEmpty(city)){
            et_state.setError("Enter state");
            et_state.requestFocus();
            result=false;
        }
        else if (TextUtils.isEmpty(workplace)){
            et_workPlace.setError("Enter workplace");
            et_workPlace.requestFocus();
            result=false;
        }else {
            if (name.equals(management.getString(INSTITUTE))
                    && mobile.equals(management.getString(MOBILE))
                    && email.equals(management.getString(EMAIL))
                    && city.equals(management.getString(CITY))
                    && dist.equals(management.getString(DISTRICT))
                    && state.equals(management.getString(STATE))
                    && workplace.equals(management.getString(WORKPLACE))
                    // && gender==Integer.parseInt(management.getString(GENDER))
                   ){
                result=false;
                CommonMethods.showSuccessToast(getContext(),"You haven't change any data");
            }
        }
        return result;
    }

    private void sendRequest(int request){
        HashMap<String, Object> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        StorageReference storageReference=null;
        String filePathAndName="";
        switch (request){
            case ApiCode.GET_USER_DETAILS:
                params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("type","1");
                reference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("subs_count").exists()){
                            String subsCount=snapshot.child("subs_count").getValue(String.class);
                            tv_sub_count.setText(subsCount);

                        }else{
                            tv_sub_count.setText("0");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                callApi(ApiCode.GET_USER_DETAILS,params);
                break;
            case ApiCode.UPDATE_PROFILE_IMAGE:
                params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("profile_image",imageString);
                filePathAndName = "UserDetails/" +firebaseAuth.getUid()+"/" + "Image";
                storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
                if(imgUri!=null){
                    storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            downloadImageUri = uriTask.getResult();
                            HashMap<String,Object> params=new HashMap<>();
                            params.put("image", ""+downloadImageUri);

                            reference.child(firebaseAuth.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    management.setString(PROFILE_IMAGE,""+downloadImageUri);

                                    setHeader();
                                    ((TeacherMainActivity)getActivity()).setProfile();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          CommonMethods.showSuccessToast(getActivity(),e.getMessage());
                        }
                    });

                }



//                callApi(ApiCode.UPDATE_PROFILE_IMAGE,params);
                break;
            case ApiCode.UPDATE_PROFILE:
//                params.put("user_id",management.getString(USER_ID));
                params.put("mobile",et_mobile.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                // params.put("password",et_password.getText().toString().trim());
               // params.put("name",et_name.getText().toString().trim());
                params.put("institute_name",et_name.getText().toString().trim());
                params.put("city",et_city.getText().toString().trim());
                 params.put("state",et_state.getText().toString().trim());
                params.put("district",et_dist.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                //params.put("father_name",et_fatherName.getText().toString().trim());
                //params.put("gender",String.valueOf(gender));
                params.put("workplace",et_workPlace.getText().toString().trim());
                //params.put("dob",tv_dob.getText().toString().trim());
                params.put("type","1");
                reference.child(firebaseAuth.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        management.setString(INSTITUTE,et_name.getText().toString().trim());
                        management.setString(NAME,et_name.getText().toString().trim());
                        management.setString(EMAIL,et_email.getText().toString().trim());
                        management.setString(MOBILE,et_mobile.getText().toString().trim());
                        management.setString(CITY,et_city.getText().toString().trim());
                        management.setString(DISTRICT,et_dist.getText().toString().trim());
                        management.setString(STATE,et_state.getText().toString().trim());
                        // management.setString(GENDER,object.getString("gender"));
                        management.setString(WORKPLACE,et_workPlace.getText().toString().trim());
                        //management.setString(FATHER,object.getString("father_name"));
                        setHeader();
                        ((TeacherMainActivity)getActivity()).setProfile();
                    }
                });
//                callApi(ApiCode.UPDATE_PROFILE, params);
                break;
        }

    }

    public void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_USER_DETAILS:
                service.postDataVolley(ApiCode.GET_USER_DETAILS
                        , BaseUrl.URL_GET_USER_DETAILS, params);
                break;
            case ApiCode.UPDATE_PROFILE_IMAGE:
                service.postDataVolley(ApiCode.UPDATE_PROFILE_IMAGE
                        , BaseUrl.URL_UPDATE_PROFILE_IMAGE, params);
                break;
            case ApiCode.UPDATE_PROFILE:
                service.postDataVolley(ApiCode.UPDATE_PROFILE
                        , BaseUrl.URL_UPDATE_PROFILE, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_USER_DETAILS:
                Log.e("profile_details",response);
                if(jsonObject.getBoolean("status")){
                    tv_sub_count.setText(jsonObject.getJSONObject("data").getString("subscriber_count"));
                }
                break;
            case ApiCode.UPDATE_PROFILE:
                Log.e("uddate_profile",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Updated Successfully");
                    JSONObject object=jsonObject.getJSONObject("data");
                    management.setString(INSTITUTE,object.getString("institute_name"));
                    management.setString(EMAIL,object.getString("email"));
                    management.setString(MOBILE,object.getString("mobile"));
                    management.setString(CITY,object.getString("city"));
                    management.setString(DISTRICT,object.getString("district"));
                    management.setString(STATE,object.getString("state"));
                   // management.setString(GENDER,object.getString("gender"));
                    management.setString(WORKPLACE,object.getString("workplace"));
                    //management.setString(FATHER,object.getString("father_name"));
                    setHeader();
                    ((TeacherMainActivity)getActivity()).setProfile();

                }
                break;
            case ApiCode.UPDATE_PROFILE_IMAGE:
                Log.e("uddate_image",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Profile Image Updated Successfully");
                    management.setString(PROFILE_IMAGE,jsonObject.getString("path"));
                    setHeader();
                    ((TeacherMainActivity)getActivity()).setProfile();
                }

                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.GET_USER_DETAILS);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }
}
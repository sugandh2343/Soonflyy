package soonflyy.learning.hub.activity;

import static soonflyy.learning.hub.utlis.AppConstant.FROM_RESET_PASSWORD;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;
import static soonflyy.learning.hub.utlis.AppConstant.SCHOOL_SECTION_TYPE;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolLoginMainActivity;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.AppUtlis;
import soonflyy.learning.hub.utlis.SessionManagement;
import soonflyy.learning.hub.utlis.TextWatcher;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, VolleyResponseListener {
  EditText et_new_password,et_confm_password;
  ImageView arrow_back_img,iv_password,iv_confirmPassword,iv_right_top_img;
  String user_type, user_id;
  Button btn_change_password;
  boolean is_password_visible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        getIntentData();
        initControl();
        myObserve();
        addTextChangeListener();
        user_type =getIntent ().getStringExtra ("user_type");
    }

    private void getIntentData() {
        user_id = getIntent().getStringExtra("user_id");
        user_type = getIntent().getStringExtra("user_type");
    }


    @Override
    public void init() {
        iv_right_top_img=findViewById(R.id.iv_top_right_img);
        et_new_password = findViewById(R.id.et_new_password);
        et_confm_password = findViewById(R.id.et_confm_password);
        arrow_back_img = findViewById(R.id.arrow_back_img);
        btn_change_password = findViewById(R.id.btn_change_password);
        iv_confirmPassword=findViewById(R.id.toggle_cofm_img);
        iv_password=findViewById(R.id.toggle_img);

        Picasso.get().load(R.drawable.change_password).into(iv_right_top_img);
    }

    @Override
    public void initControl() {
        arrow_back_img.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        iv_confirmPassword.setOnClickListener(this);
        iv_password.setOnClickListener(this);
    }

    @Override
    public void myObserve() {

    }
    private void addTextChangeListener() {
        et_new_password.addTextChangedListener(new TextWatcher(et_new_password));
        et_confm_password.addTextChangedListener(new TextWatcher(et_confm_password));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back_img:
                onBackPressed();
                break;
            case R.id.btn_change_password:
                goToLoginScreen();
                break;
            case R.id.toggle_cofm_img:
                showHidePassword(iv_confirmPassword,et_confm_password);
                break;
            case R.id.toggle_img:
                showHidePassword(iv_password,et_new_password);
                break;
        }
    }

    private void showHidePassword(ImageView imageView, EditText etText) {

        if (!is_password_visible) {
            AppUtlis.hideKeyboard(this);
            etText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_black_24dp));
            is_password_visible = true;
        } else {
            AppUtlis.hideKeyboard(this);
            etText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.hidden_password));
            is_password_visible = false;
        }
        imageView.setColorFilter(ContextCompat.getColor(this,R.color.black));



    }

    private void goToLoginScreen() {
        if(onValidation()){
            if (user_type.equals("Teacher")||user_type.equals("Student"))
            sendRequest(ApiCode.CHANGE_PASSWORD);
            else
                sendRequest(ApiCode.SCHOOL_CHANGE_PASSWORD);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.CHANGE_PASSWORD :
                params.put("user_id",user_id);
                params.put("new_password",et_new_password.getText().toString());
                params.put("confirm_password",et_confm_password.getText().toString());
                callApi(ApiCode.CHANGE_PASSWORD, params);
                break;
            case ApiCode.SCHOOL_CHANGE_PASSWORD:
                if (user_type.equals("school")){
                    params.put("type","0");
                    //params.put("user_id",new Session_management(this).getString(SCHOOL_ID));
                }else{
                    params.put("type","1");
                    //params.put("user_id",new Session_management(this).getString(SCHOOL_IT_ID));
                }
                params.put("user_id",user_id);
                params.put("new_password",et_new_password.getText().toString());
                params.put("confirm_password",et_confm_password.getText().toString());
                callApi(ApiCode.SCHOOL_CHANGE_PASSWORD, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback,ChangePasswordActivity.this);
        switch (request){
            case ApiCode.CHANGE_PASSWORD:
                service.postDataVolley(ApiCode.CHANGE_PASSWORD,
                        BaseUrl.URL_CHANGE_PASSWORD, params);
                break;
            case ApiCode.SCHOOL_CHANGE_PASSWORD:
                service.postDataVolley(ApiCode.SCHOOL_CHANGE_PASSWORD,
                        BaseUrl.URL_SCHOOL_CHANGE_PASSWORD, params);
                break;

        }
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
      //  CommonMethods.intentAlert(this, user_type,"Are you sure you want to cancel the process?", LoginActivity.class);
    }

    public boolean onValidation(){
        String new_password = et_new_password.getText().toString().trim();
        String confirm_password = et_confm_password.getText().toString().trim();

        if(TextUtils.isEmpty(new_password)){
            Toast.makeText(this,"Please fill new password",Toast.LENGTH_LONG).show();
            return false;
        }else if(TextUtils.isEmpty(confirm_password)){
            Toast.makeText(this,"Please fill confirm password",Toast.LENGTH_LONG).show();
            return false;
        }else if(!new_password.matches(confirm_password)){
            Toast.makeText(this,"password does not match Please try again",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.CHANGE_PASSWORD:
            case ApiCode.SCHOOL_CHANGE_PASSWORD:
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    boolean res=jsonObject.getBoolean("status");
                    if (res) {
                       //CommonMethods.generalAlert(this, jsonObject.getString("message"));
                        showResetSuccessDialog("Password Change Successfully");

                    }else{
                        CommonMethods.showSuccessToast(this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void showResetSuccessDialog(String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = null;
                if (user_type.equals("Teacher") || user_type.equals("Student")){
                    //for school tutor
                    intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
            }else{
                    //for school and independent tutor
                    new SessionManagement(ChangePasswordActivity.this).setString(LOGIN_PAGE_TYPE,FROM_RESET_PASSWORD);
                    new SessionManagement(ChangePasswordActivity.this).setString(SCHOOL_SECTION_TYPE,user_type);
                    intent = new Intent(ChangePasswordActivity.this, SchoolLoginMainActivity.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(AppConstant.USER_TYPE, user_type);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        //pbutton.setBackgroundColor(Color.YELLOW);
        //Set positive button text color
        pbutton.setTextColor(ContextCompat.getColor(this,R.color.primary_color));

    }
}
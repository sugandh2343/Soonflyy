package soonflyy.learning.hub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class  MobileVerificationActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseListener {
    EditText et_mobile_num;
    String user_type;
    Button btn_submit;
    ImageView iv_right_top_img;
    public String otp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_mobile_verification);

        et_mobile_num=findViewById (R.id.et_mobile_num);
        iv_right_top_img=findViewById(R.id.iv_top_right_img);
        btn_submit=findViewById (R.id.btn_submit);
        Picasso.get().load(R.drawable.forgetpassword).into(iv_right_top_img);
        btn_submit.setOnClickListener (this);
        user_type =getIntent ().getStringExtra ("user_type");
        otp=getRandomKey(4);

    }

    private String getRandomKey(int i) {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.btn_submit){
            String m = et_mobile_num.getText().toString();

            if (m.isEmpty())
            {
               // Toast.makeText (this, "Mobile Number is Required", Toast.LENGTH_SHORT).show ( );
               et_mobile_num.setError("Enter mobile number");
               et_mobile_num.requestFocus();

            }
            else if (m.length()!=10) {
            et_mobile_num.setError ("Invalid mobile number");
               et_mobile_num.requestFocus ( );
            }
            else if (Integer.parseInt (String.valueOf (m.charAt (0))) < 6) {
              et_mobile_num.setError ("Invalid mobile number");
               // Toast.makeText(this,"Mobile number should be start 6 or greater 6",Toast.LENGTH_LONG).show();
                et_mobile_num.requestFocus ( );
            }
            else {
                if (user_type.equals("Teacher")||user_type.equals("Student")) {
                    //for home school and tutor
                    sendRequest(ApiCode.MOBILEVARIFICATION);
                }else{
                    //for school and itutor
                    sendRequest(ApiCode.SCHOOL_MOBILE_VERIFICATION);
                }
            }
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.MOBILEVARIFICATION :
                params.put("mobile",et_mobile_num.getText().toString());
                if (user_type.equals("Teacher"))
                    params.put("type","1");
                    else
                    params.put("type","0");
               //params.put("otp",otp);
                callApi(ApiCode.MOBILEVARIFICATION, params);
                break;
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                params.put("mobile",et_mobile_num.getText().toString());
                if (user_type.equals("school"))
                    params.put("type","0");
                else
                    //user_type=i_tutor
                    params.put("type","1");
                //params.put("otp",otp);
                callApi(ApiCode.SCHOOL_MOBILE_VERIFICATION, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback,MobileVerificationActivity.this);
        switch (request){
            case ApiCode.MOBILEVARIFICATION:
                service.postDataVolley(ApiCode.MOBILEVARIFICATION,
                        BaseUrl.URL_MOBILE_VERIFICATION, params);
                break;
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                service.postDataVolley(ApiCode.SCHOOL_MOBILE_VERIFICATION,
                        BaseUrl.URL_SCHOOL_MOBILE_VERIFICATION, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.MOBILEVARIFICATION:
            case ApiCode.SCHOOL_MOBILE_VERIFICATION:
                Log.e("MOBILEVARIFICATION", "onResponse: "+response );
                try {
                    JSONObject jsonObject  = new JSONObject(response);
                    boolean res=jsonObject.getBoolean("status");
                    if (res) {
                        Log.e("otp", "onResponse: "+jsonObject.getString("data"));
                        Intent intent = new Intent(MobileVerificationActivity.this, VerificationActivity.class);
                        intent.putExtra("number", et_mobile_num.getText().toString());
                        intent.putExtra("user_type", user_type);
                        intent.putExtra("otp", jsonObject.getString("data"));
                        startActivity(intent);
                    }else{
                        CommonMethods.showSuccessToast(this,jsonObject.getString("message"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }
}
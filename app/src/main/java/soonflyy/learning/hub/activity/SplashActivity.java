package soonflyy.learning.hub.activity;

import static soonflyy.learning.hub.Common.Constant.DEVICE_BRAND;
import static soonflyy.learning.hub.Common.Constant.DEVICE_ID;
import static soonflyy.learning.hub.Common.Constant.DEVICE_MODEL;
import static soonflyy.learning.hub.Common.Constant.DEVICE_VERSION;
import static soonflyy.learning.hub.Common.Constant.IS_INSTRUCTOR;
import static soonflyy.learning.hub.Common.Constant.LOGIN_STATUS;
import static soonflyy.learning.hub.utlis.AppConstant.REQUEST_PERMISSION_CODE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import soonflyy.learning.hub.Confiq.Common;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getName();
    Common common;
    SessionManagement session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        common=new Common (SplashActivity.this);
        session_management = new SessionManagement(this);
        getDeviceInfo();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ConnectivityReceiver.isConnected()) {
                    Intent intent;
                    if (session_management.getBoolean(LOGIN_STATUS)){
                        String userType=session_management.getString(IS_INSTRUCTOR);
                        if (userType.equals("1")){
                            intent = new Intent(SplashActivity.this, TeacherMainActivity.class);

                            //  intent = new Intent(SplashActivity.this, TchoiceActivity.class);
                        }else{
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }

                    }else {
                        intent = new Intent(SplashActivity.this, ChoiceActivity.class);


                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(AppConstant.FROM,SplashActivity.TAG);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    common.noInternet ();
                }


            }
        },REQUEST_PERMISSION_CODE);


    }

    private void getDeviceInfo() {
        //---------------------------/
        OSDeviceState deviceState = OneSignal.getDeviceState();
        String userId = deviceState != null ? deviceState.getUserId() : null;
        //sessionMangement.addToken(userId);

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("userdkfjld",""+userId);
        Log.e("sadfg",""+android_id);
        session_management.addPushToken(android_id,userId);
        //---------------------------//

        String androidId= Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String model=Build.MODEL;
        String version=Build.VERSION.RELEASE;
        String brand=Build.BRAND;
        session_management.setString(DEVICE_ID,androidId);
        session_management.setString(DEVICE_MODEL,model);
        session_management.setString(DEVICE_BRAND,brand);
        session_management.setString(DEVICE_VERSION,version);
        String deviceInfo="SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND: " + Build.BRAND + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: "+Build.FINGERPRINT + "\n" +
                "ANDROID_ID: "+androidId+"\n"+
                "Version Code: " + Build.VERSION.RELEASE;
        Log.e("deviceInfo",""+deviceInfo);
    }
}

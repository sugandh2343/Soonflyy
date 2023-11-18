package soonflyy.learning.hub.utlis;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import live.videosdk.rtc.android.VideoSDK;


public class AppController extends Application {
    public static final String CHANNEL_ID="video_upload_101";
    public static final String TAG =soonflyy.learning.hub.utlis.AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private static soonflyy.learning.hub.utlis.AppController mInstance;

    //-push notification--
    private static final String ONESIGNAL_APP_ID = "0cbc6bdf-61bb-4461-8e05-cadf9bb811bb";
    //------------
    @Override
    public void onCreate() {
        super.onCreate();

        //-------push notificatino initialization----------
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
       // OneSignal.promptForPushNotifications();
        //-------------------
        mInstance = this;
        List<Locale> locales = new ArrayList<> ();
        locales.add(Locale.ENGLISH);
        locales.add(new Locale ("en","ENGLISH"));
       VideoSDK.initialize(getApplicationContext());

    }

    public static synchronized soonflyy.learning.hub.utlis.AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(
                    CHANNEL_ID,
                    "Video Uploading Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}

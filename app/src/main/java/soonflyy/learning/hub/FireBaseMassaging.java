package soonflyy.learning.hub;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;

public class FireBaseMassaging extends FirebaseMessagingService {
    NotificationManager notificationManager;

    @SuppressLint("ServiceCast")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (remoteMessage != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String image = remoteMessage.getData().get("imgurl");
            Log.e("uri", String.valueOf(image));
            sendMassage(title,message,image);
            //AppData.Notification="ok";
            //notifyUser(title, message);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendMassage(String til,String msg,String img){
        Intent intent;
        intent = new Intent(FireBaseMassaging.this, TeacherMainActivity.class);



        PendingIntent pendingIntent = PendingIntent.getActivity(FireBaseMassaging.this,
                0, intent, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID = "chanelId";

        try {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.bigPicture(Glide.with(FireBaseMassaging.this).asBitmap().load(img).submit().get());
            style.setBigContentTitle("Big");
            style.setSummaryText("Small");
            style.bigLargeIcon(null);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat
                    .Builder(FireBaseMassaging.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.s_achievement)
                    .setContentTitle(til)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setLargeIcon(Glide.with(FireBaseMassaging.this).asBitmap().load(img).submit().get())
                    .setContentIntent(pendingIntent)
                    .setPriority(PRIORITY_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notihigh = NotificationManager.IMPORTANCE_DEFAULT;

            // check version of Android /// 50
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "HajiriRegister", notihigh);
                mChannel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
                mChannel.enableVibration(true);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}

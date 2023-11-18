package soonflyy.learning.hub.services;

import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_COMPLETE;
import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_ERROR;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;

import java.io.File;
import java.io.IOException;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadFileService extends Service {//IntentService

    private static final String TAG = "vidoeUploading service";
    private PowerManager.WakeLock wakeLock;
    private NotificationManager notificationManager;
    private Notification notification;

    ////---------------------------------///
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int NOTIFICATION_ID ;
    public static String CHANNEL_ID = "";//channel_02

    //--------------------------------------//

//    public UploadFileService() {
//        super("Video Uploading..");
//        setIntentRedelivery(true);
//    }

//     @Override
//    public void onCreate() {
//        super.onCreate();
//        PowerManager powerManager=(PowerManager)getSystemService(POWER_SERVICE);
//        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                "ExamHunter: wakelock");
//        wakeLock.acquire();
//        Log.d(TAG,"wakeloc acquire");
//       /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            Notification notification=new Notification.Builder(this,CHANNEL_ID)
//                    .setContentTitle("Video Upload")
//                    .setContentText("Upload in progress")
//                    .setSmallIcon(R.drawable.video_24px)
//                    .build();
//            startForeground(1,notification);
//
//        }else {
//
//
//           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                   .setSmallIcon(R.drawable.video_24px)
//                   .setContentTitle("Video Upload")
//                   .setContentText("Upload in progress")
//                   .setAutoCancel(false)
//                   //.setStyle(new NotificationCompat.BigTextStyle()
//                   //      .bigText("Much longer text that cannot fit one line..."))
//                   .setPriority(NotificationCompat.PRIORITY_HIGH);
//           NotificationManagerCompat notificationCompat=NotificationManagerCompat.from(this);
//           notificationCompat.notify(123,builder.build());
//           startForeground(1,);
//
//        */
//      //  startUploadForegroundService();
//       //}
//    }
//
   /* @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String course_id=null;
        String topic_id=null;
        String section_id=null;
        String chapter_id=null;
        String title="";
         ResultReceiver receiver = intent.getParcelableExtra("receiver");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        HashMap<String,String> params=new HashMap<>();
        Log.d(TAG,"onHandlerIntent");
        //write code to get data from intent
        VideoUploadUri urilist= (VideoUploadUri) intent.getParcelableExtra("videos");
        String from=intent.getStringExtra("from");
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            String type = intent.getStringExtra("type");
            params.put("type", type);
            course_id = intent.getStringExtra("course_id");
            section_id = intent.getStringExtra("section_id");
            chapter_id = intent.getStringExtra("chapter_id");
            title = intent.getStringExtra("title");
            params.put("course_id", course_id);
            params.put("chapter_id", chapter_id);
            params.put("section_id", section_id);
            params.put("title", title);

//        if (type.equals("paid")){
//            //get topic id
//            topic_id=intent.getStringExtra("topic_id");
//            params.put("topic_id",topic_id);
//        }
            int size = urilist.getVideoList().size();
            if (urilist.getVideoList().size() > 0) {
                for (int i = 0; i < size; i++) {
                    Log.e("videoupload", "call_" + i);
                    VolleyUploadFiles uploadFiles = new VolleyUploadFiles(this, BaseUrl.URL_UPLOAD_COURSE_VIDEO, params);
                    String fileName = type + "_video_" + sdf.format(new Date()) + ".mp4";
                    Log.e("fileName", fileName);
                    uploadFiles.uploadFiles(fileName, urilist.getVideoList().get(i),receiver);
                }
            }
        }else if (from.equals(SCHOOL_TUTOR)|| from.equals(INDEPENDENT_TUTOR)){
            params.put("chapter_id", intent.getStringExtra("chapter_id"));
            params.put("subject_id", intent.getStringExtra("subject_id"));
            params.put("teacher_id", intent.getStringExtra("teacher_id"));
            params.put("title", intent.getStringExtra("title"));
            int size = urilist.getVideoList().size();
            if (urilist.getVideoList().size() > 0) {
                for (int i = 0; i < size; i++) {
                    Log.e("videoupload", "call_" + i);
                    VolleyUploadFiles uploadFiles = new VolleyUploadFiles(this, BaseUrl.URL_SCHOOL_ADD_VIDEO, params);
                    String fileName = "chapter" + "_video_" + sdf.format(new Date()) + ".mp4";
                    Log.e("fileName", fileName);
                    uploadFiles.uploadFiles(fileName, urilist.getVideoList().get(i),receiver);
                }
            }
        }

    }

    */


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NOTIFICATION_ID= (int) System.currentTimeMillis();
        CHANNEL_ID="CHANNEL_"+NOTIFICATION_ID;
        Log.e("startId",""+startId);
        Log.e("channel",""+CHANNEL_ID);
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                "Upload Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        //  getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.createNotificationChannel(notificationChannel);
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setContentTitle("File Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.upload_24px)
                .setOnlyAlertOnce(true);

        String from = intent.getStringExtra("from");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startForeground(NOTIFICATION_ID, mBuilder.build());
                if (from.equals(SIMPLEE_HOME_TUTOR)) {
                    //upload video for home section tutor----'///
                    uploadHomeTutorVideo(intent);
                } else if (from.equals(SCHOOL_TUTOR) || from.equals(INDEPENDENT_TUTOR)) {
                    //upload video for home section tutor----'///
                    uploadSchoolTutorVideo(intent);
                }
            }
        });



        // uploadFile(intent);
        // uploadData(intent);
        return super.onStartCommand(intent, flags, startId);

    }

    private void uploadSchoolTutorVideo(Intent intent) {
        int notificaion_id=NOTIFICATION_ID;
        NotificationCompat.Builder vNotificationBuilder=mBuilder;
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String title = intent.getStringExtra("title");
        String chapterId = intent.getStringExtra("chapter_id");
        String subjectId = intent.getStringExtra("subject_id");
        String teacherId = intent.getStringExtra("teacher_id");
        File f = new File(intent.getStringExtra("fileName"));
        String content_type = getMimeType(f.getPath());
//client
        OkHttpClient okHttpClient = new OkHttpClient();


//request builder
        Request.Builder builder = new Request.Builder();
        builder.url(BaseUrl.URL_SCHOOL_ADD_VIDEO);


//your original request body
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart("file", f.getName(), RequestBody.create(MediaType.parse(content_type), f));
        bodyBuilder.addFormDataPart("chapter_id", chapterId)
                .addFormDataPart("subject_id", subjectId)
                .addFormDataPart("teacher_id", teacherId)
                .addFormDataPart("title", title);
        MultipartBody body = bodyBuilder.build();

//wrap your original request body with progress

        RequestBody requestBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Log.e("TAG", "onUIProgressStart:" + totalBytes);
            }

            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                Log.e("TAG", "=============start===============");
                Log.e("TAG", "numBytes:" + numBytes);
                Log.e("TAG", "totalBytes:" + totalBytes);
                Log.e("TAG", "percent:" + percent);
                Log.e("TAG", "speed:" + speed);
                Log.e("TAG", "============= end ===============");
                int progressValue = ((int) (100 * percent));
                vNotificationBuilder.setContentText("Upload in progress");
                vNotificationBuilder.setProgress(100, progressValue, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                //tvProgressInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + "  MB/秒");
            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                Log.e("TAG", "onUIProgressFinish:");
                //  Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();
            }

        });

//post the wrapped request body
        builder.post(requestBody);
//call
        Call call = okHttpClient.newCall(builder.build());

//enqueue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "=============onFailure===============");
                e.printStackTrace();
                stopForeground(true);
                vNotificationBuilder.setContentText("Upload  error occur")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                if (receiver != null) {
                    Bundle b = new Bundle();
                    receiver.send(FILE_UPLOAD_ERROR, b);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "=============onResponse===============");
                Log.e("TAG", "request headers:" + response.request().headers());
                Log.e("TAG", "response headers:" + response.headers());
                if (response.isSuccessful()) {
                    stopForeground(true);
                    vNotificationBuilder.setContentText("Upload Completed");
                    mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                    if (receiver != null) {
                        Bundle b = new Bundle();
                        receiver.send(FILE_UPLOAD_COMPLETE, b);
                    }
                } else {
                    stopForeground(true);
                    vNotificationBuilder.setContentText("Upload error occur");
                    mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                    if (receiver != null) {
                        Bundle b = new Bundle();
                        receiver.send(FILE_UPLOAD_ERROR, b);
                    }
                }
            }
        });
    }

    private void uploadHomeTutorVideo(Intent intent) {
        int notificaion_id=NOTIFICATION_ID;
        NotificationCompat.Builder vNotificationBuilder=mBuilder;

        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String title = intent.getStringExtra("title");
        String course_id = intent.getStringExtra("course_id");
        String section_id = intent.getStringExtra("section_id");
        String chapter_id = intent.getStringExtra("chapter_id");
        String fString = intent.getStringExtra("fileName");
        Log.e("fileName:", "" + fString);
        File f = new File(intent.getStringExtra("fileName"));
        String type = intent.getStringExtra("type");
        String content_type = getMimeType(f.getPath());
//client
        OkHttpClient okHttpClient = new OkHttpClient();
//request builder
        Request.Builder builder = new Request.Builder();
        builder.url(BaseUrl.URL_UPLOAD_COURSE_VIDEO);

//your original request body
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart("file", f.getName(), RequestBody.create(MediaType.parse(content_type), f));
        bodyBuilder.addFormDataPart("type", type)
                .addFormDataPart("course_id", course_id)
                .addFormDataPart("chapter_id", chapter_id)
                .addFormDataPart("section_id", section_id)
                .addFormDataPart("title", title);
        MultipartBody body = bodyBuilder.build();
        String post = "course_id=" + course_id + "\n"
                + "chapter_id=" + chapter_id + "\n" +
                "type=" + type + "\n"
                + "section_id=" + section_id + "\n"
                + "title=" + title + "\n"
                + "file=" + f.getName() + "\n";
        Log.e("post: ", "" + post);
//wrap your original request body with progress


        RequestBody requestBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Log.e("TAG", "onUIProgressStart:" + totalBytes);
            }

            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                Log.e("TAG", "=============start===============");
                Log.e("TAG", "numBytes:" + numBytes);
                Log.e("TAG", "totalBytes:" + totalBytes);
                Log.e("TAG", "percent:" + percent);
                Log.e("TAG", "speed:" + speed);
                Log.e("TAG", "============= end ===============");

                Log.e("TAG",""+NOTIFICATION_ID);
                Log.e("TAG",""+CHANNEL_ID);

                int progressValue = ((int) (100 * percent));
                vNotificationBuilder.setContentText("Upload in progress");
                vNotificationBuilder.setProgress(100, progressValue, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                //tvProgressInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + "  MB/秒");
            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                Log.e("TAG", "onUIProgressFinish:");
                //  Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();
            }

        });

//post the wrapped request body
        builder.post(requestBody);
//call
        Call call = okHttpClient.newCall(builder.build());
//enqueue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "=============onFailure===============");
                e.printStackTrace();
                stopForeground(true);
                vNotificationBuilder.setContentText("Upload  error occur")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                if (receiver != null) {
                    Bundle b = new Bundle();
                    receiver.send(FILE_UPLOAD_ERROR, b);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "=============onResponse===============");
                Log.e("TAG", "request headers:" + response.request().headers());
                Log.e("TAG", "response headers:" + response.headers());
                if (response.isSuccessful()) {
                    stopForeground(true);
                    vNotificationBuilder.setContentText("Upload Completed");
                    mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                    if (receiver != null) {
                        Bundle b = new Bundle();
                        receiver.send(FILE_UPLOAD_COMPLETE, b);
                    }
                } else {
                    stopForeground(true);
                    vNotificationBuilder.setContentText("Upload error occur");
                    mNotifyManager.notify(notificaion_id, vNotificationBuilder.build());
                    if (receiver != null) {
                        Bundle b = new Bundle();
                        receiver.send(FILE_UPLOAD_ERROR, b);
                    }
                }
            }
        });
    }

    /*private void uploadFile(Intent intent) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                File f = new File(intent.getStringExtra("fileName"));
                String title = intent.getStringExtra("title");
                ResultReceiver receiver = intent.getParcelableExtra("receiver");
                String from = intent.getStringExtra("from");
                if (from.equals(SIMPLEE_HOME_TUTOR)) {
                    String type = intent.getStringExtra("type");
                    String course_id = intent.getStringExtra("course_id");
                    String section_id = intent.getStringExtra("section_id");
                    String chapter_id = intent.getStringExtra("chapter_id");

                    String fileName = type + "_video_" + sdf.format(new Date()) + ".mp4";



                    String content_type = getMimeType(f.getPath());
                    String file_path = f.getAbsolutePath();

                    Log.e("contentType", "" + content_type);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);//content_type
                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            //.addFormDataPart("type", content_type)//content_type
                            .addFormDataPart("type", type)
                            .addFormDataPart("file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)

                            .addFormDataPart("course_id", course_id)
                            .addFormDataPart("chapter_id", chapter_id)
                            .addFormDataPart("section_id", section_id)
                            .addFormDataPart("title", title)
                            .build();
                    Request request = new Request.Builder()
                            .url(BaseUrl.URL_UPLOAD_COURSE_VIDEO)
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            stopForeground(true);
                            mBuilder.setContentText("Uploading Error")
                                    // Removes the progress bar
                                    .setProgress(0, 0, false);

                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                            Log.e("UploadError ",""+response);
                            throw new IOException("Error  : " + response);
                        } else {
                            if (response.isSuccessful()) {

                                stopForeground(true);
                                mBuilder.setContentText("Upload Completed")
                                        // Removes the progress bar
                                        .setProgress(0, 0, false);

                                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                                if (receiver!=null) {
                                    Bundle b = new Bundle();
                                    receiver.send(FILE_UPLOAD_COMPLETE, b);
                                }
                                Log.e("uploading", "success");

                            }
                        }


                    } catch (IOException e) {
                        stopForeground(true);
                        mBuilder.setContentText("Uploading Error")
                                // Removes the progress bar
                                .setProgress(0, 0, false);

                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());

                        Bundle b = new Bundle();
                        receiver.send(FILE_UPLOAD_ERROR, b);
                        Log.e("UploadError ",""+e.getMessage());
                        e.printStackTrace();

                    }

                }
                else if (from.equals(SCHOOL_TUTOR) || from.equals(INDEPENDENT_TUTOR)) {
                   String chapterId= intent.getStringExtra("chapter_id");
                  String subjectId= intent.getStringExtra("subject_id");
                   String teacherId= intent.getStringExtra("teacher_id");

                    String fileName = "chapter" + "_video_" + sdf.format(new Date()) + ".mp4";

                    String content_type = getMimeType(f.getPath());
                    String file_path = f.getAbsolutePath();

                    Log.e("contentType", "" + content_type);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);//content_type
                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            //.addFormDataPart("type", content_type)//content_type
                            .addFormDataPart("file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)

                            .addFormDataPart("chapter_id", chapterId)
                            .addFormDataPart("subject_id", subjectId)
                            .addFormDataPart("teacher_id", teacherId)
                            .addFormDataPart("title", title)
                            .build();
                    Request request = new Request.Builder()
                            .url(BaseUrl.URL_SCHOOL_ADD_VIDEO)
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {

                            stopForeground(true);
                            mBuilder.setContentText("Uploading Error")
                                    // Removes the progress bar
                                    .setProgress(0, 0, false);

                            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                            if (receiver!=null) {
                                Bundle b = new Bundle();
                                receiver.send(FILE_UPLOAD_ERROR, b);
                            }
                            Log.e("UploadError ",""+response);
                            throw new IOException("Error  : " + response);
                        } else {
                            if (response.isSuccessful()) {
                                stopForeground(true);
                                mBuilder.setContentText("Upload Completed")
                                        // Removes the progress bar
                                        .setProgress(0, 0, false);

                                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                                if (receiver!=null) {
                                    Bundle b = new Bundle();
                                    receiver.send(FILE_UPLOAD_COMPLETE, b);
                                }
                                Log.e("uploading", "success");
                            }
                        }


                    } catch (IOException e) {
                        stopForeground(true);
                        mBuilder.setContentText("Uploading Error")
                                // Removes the progress bar
                                .setProgress(0, 0, false);

                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                        if (receiver!=null) {
                            Bundle b = new Bundle();
                            receiver.send(FILE_UPLOAD_ERROR, b);
                        }
                        Log.e("UploadError ",""+e.getMessage());
                        e.printStackTrace();

                    }

                }
         //   }

//        }).start();




    }

    private void uploadData(Intent intent){

        File f = new File(intent.getStringExtra("fileName"));
        String title = intent.getStringExtra("title");
        String chapterId= intent.getStringExtra("chapter_id");
        String subjectId= intent.getStringExtra("subject_id");
        String teacherId= intent.getStringExtra("teacher_id");

        //client
        OkHttpClient okHttpClient = new OkHttpClient();
//request builder
        Request.Builder builder = new Request.Builder();
        builder.url(BaseUrl.URL_UPLOAD_COURSE_VIDEO);

//your original request body
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.addFormDataPart("File", f.getName(), RequestBody.create(null, f));
        bodyBuilder .addFormDataPart("chapter_id", chapterId)
                .addFormDataPart("subject_id", subjectId)
                .addFormDataPart("teacher_id", teacherId)
                .addFormDataPart("title", title);
        MultipartBody body = bodyBuilder.build();

//wrap your original request body with progress
        RequestBody requestBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
                Log.e("TAG", "onUIProgressStart:" + totalBytes);
                mBuilder.setProgress((int) PrefUtils.getBytesToMB(totalBytes),0, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
            }

            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                Log.e("TAG", "=============start===============");
                Log.e("TAG", "numBytes:" + numBytes);
                Log.e("TAG", "totalBytes:" + totalBytes);
                Log.e("TAG", "percent:" + percent);
                Log.e("TAG", "speed:" + speed);
                Log.e("TAG", "============= end ===============");
                int progressValue=((int) (100 * percent));
                mBuilder.setProgress((int) PrefUtils.getBytesToMB(totalBytes),progressValue, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
               // uploadProgress.setProgress((int) (100 * percent));
               // uploadInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + "  MB/秒");
            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
                Log.e("TAG", "onUIProgressFinish:");
                stopForeground(true);
                mBuilder.setContentText("Upload Completed")
                        // Removes the progress bar
                        .setProgress(0, 0, false);

                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
               // Toast.makeText(getApplicationContext(), "结束上传", Toast.LENGTH_SHORT).show();
            }

        });

//post the wrapped request body
        builder.post(requestBody);
//call
        Call call = okHttpClient.newCall(builder.build());
//enqueue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "=============onFailure===============");
                stopForeground(true);
                mBuilder.setContentText("Upload Error")
                        // Removes the progress bar
                        .setProgress(0, 0, false);

                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "=============onResponse===============");
                Log.e("TAG", "request headers:" + response.request().headers());
                Log.e("TAG", "response headers:" + response.headers());
                if (response.isSuccessful()) {
                    stopForeground(true);
                    mBuilder.setContentText("Download Completed")
                            // Removes the progress bar
                            .setProgress(0, 0, false);

                    mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                }else{
                    stopForeground(true);
                    mBuilder.setContentText("Download Error")
                            // Removes the progress bar
                            .setProgress(0, 0, false);

                    mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                }
            }
        });
    }

     */

    private String getMimeType(String path) {

        Uri uri = Uri.fromFile(new File(path));
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("forground", "destroy");
//        Log.e(TAG, "destroyed");
//        wakeLock.release();
//        Log.e(TAG, "wake relese");
    }



    /*private void startUploadForegroundService() {
        // Define notification channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Video Uploading", importance);

            channel.setDescription("Upload in progress..");
             notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // Build notification to be used for the foreground service.
             notification =
                    new Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle("Video Upload")
                            .setContentText("Upload in progress..")
                            .setSmallIcon(R.drawable.video_24px)
                            .build();
            // Set the service as a foreground service.
            startForeground(1, notification);
        }else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.video_24px)
                    .setContentTitle("Simplee Exclusive")
                    .setContentText("Video upload in progress..")
                    .setAutoCancel(false)
                    //.setStyle(new NotificationCompat.BigTextStyle()
                    //      .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationCompat=NotificationManagerCompat.from(this);
            notificationCompat.notify(123,builder.build());
         // startForeground(1,builder.notify());
        }

    }

     */


}

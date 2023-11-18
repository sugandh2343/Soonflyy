package soonflyy.learning.hub.Download;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import soonflyy.learning.hub.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DownloadService extends Service {
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    String fileType = "Video";
    String vUrl = "https://simplexclusive.org/uploads/demo_video/chapter_video_20220517_122434.mp4";
    String filePath;
    int NOTIFICATION_ID = 101;
    public static String CHANNEL_ID = "channel_01";
    int downloadId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent , int flags , int startId) {
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID ,
                "Download Notification" ,
                NotificationManager.IMPORTANCE_HIGH
        );
        //  getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.createNotificationChannel(notificationChannel);
        mBuilder = new NotificationCompat.Builder(this , CHANNEL_ID);
        mBuilder.setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_baseline_download_24)
                .setOnlyAlertOnce(true);


        startForeground(NOTIFICATION_ID , mBuilder.build());
        fileType = intent.getStringExtra("fileType");
        downloadVideo(intent.getStringExtra("url") , intent);
        return super.onStartCommand(intent , flags , startId);
    }


    private void downloadVideo(String url , Intent intent) {
        // Delete the old file //

        //    Log.e("urlFileName",""+filePath);
        // FileUtils.deleteDownloadedFile(this);
        String dirPath = "";
        //  filePath="pdf_"+ CommonMethods.getCurrentTime("yyyy_MM_dd_hhmmss")+".pdf";
        if (fileType.equals("Video")) {
            //for video file download
            dirPath = FileUtils.getDirPath(this);
            filePath = URLUtil.guessFileName(url , null , null);
        } else {
            //for pdf download
            dirPath = FileUtils.getPdfDirPath(this);
            // filePath="pdf_"+ CommonMethods.getCurrentTime("yyyy_MM_dd_hhmmss")+".pdf";
            filePath = intent.getStringExtra("fileName");
        }
//  TODO      downloadId = PRDownloader.download(url , dirPath , filePath).build()
//                .setOnProgressListener(new OnProgressListener() {
//                    @Override
//                    public void onProgress(Progress progress) {
//                        // updateNotification(progress,false);
//                        long progressPer = progress.currentBytes * 100 / progress.totalBytes;
//                        mBuilder.setProgress((int) PrefUtils.getBytesToMB(progress.totalBytes) , (int) progressPer , false);
//                        // Displays the progress bar for the first time.
//                        mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());
//
//
//                    }
//                })
//                .start(new OnDownloadListener() {
//                    @Override
//                    public void onDownloadComplete() {
//                        Log.e("complete" , "complete");
//
//                        if (fileType.equals("Video")) {
//                            if (encodeFile()) {
//                                // updateNotification(null,true);
//                                stopForeground(true);
//                                mBuilder.setContentText("Download Completed")
//                                        // Removes the progress bar
//                                        .setProgress(0 , 0 , false);
//
//                                mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());
//
//                            }
//                        } else {
//                            stopForeground(true);
//                            mBuilder.setContentText("Download Completed")
//                                    // Removes the progress bar
//                                    .setProgress(0 , 0 , false);
//
//                            mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Error error) {
//                        stopForeground(false);
//                        mBuilder.setContentText("Download Error Occur")
//                                .setOngoing(false)
//                                .setProgress(0 , 0 , false);
//                        mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());
//
//                    }
//
//
//                });

    }

    private class EncryptTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            //encodeFile(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    private boolean encodeFile() {
        try {
            String path = FileUtils.getDownloadedFilePath(this , filePath);
            File datafile = new File(path);
            int fileZie = (int) datafile.length();
            Log.e("filesize" , "" + fileZie);
            byte b[] = new byte[1000000];
            int j = 0;
            String s = FileUtils.getDirPath(this) + "/" + "s_" + filePath;
            FileInputStream inputStream = new FileInputStream(path);
            int read_bytes;
            while (inputStream.available() != 0) {
                j = 0;
                FileOutputStream fileOutputStream = new FileOutputStream(s);
                while (j <= fileZie && inputStream.available() != 0) {
                    Log.e("bInc_j" , "" + j);
                    read_bytes = inputStream.read(b , 0 , 1000000);
                    if (j == 0) {
                        byte[] encodedBytes = EncryptDecryptUtils.encode(EncryptDecryptUtils.getInstance(this).getSecretKey() , b);
                        // Log.e("epathLength",""+encodedBytes.length);
                        fileOutputStream.write(encodedBytes , 0 , encodedBytes.length);
                    } else {
                        fileOutputStream.write(b , 0 , read_bytes);
                    }
                    j = j + read_bytes;
                }
            }
            Log.e("split" , "File Encrypted successfully");
            inputStream.close();
            FileUtils.deleteFile(FileUtils.getDownloadedFilePath(this , filePath));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            FileUtils.deleteFile(FileUtils.getDownloadedFilePath(this , filePath));
            stopForeground(false);
            mBuilder.setContentText("Download Error Occur")
                    .setOngoing(false)
                    .setProgress(0 , 0 , false);
            mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());
            return false;
        }

    }


    /**
     * Encrypt and save to disk
     *
     * @return
     */
    private boolean encrypt() {
        // updateUI("Encrypting file...");
        try {
            byte[] fileData = FileUtils.readFile(FileUtils.getDownloadedFilePath(this , filePath));//FileUtils.getFilePath(this)
            byte[] encodedBytes = EncryptDecryptUtils.encode(EncryptDecryptUtils.getInstance(this).getSecretKey() , fileData);
            FileUtils.saveFile(encodedBytes , FileUtils.getDownloadedFilePath(this , filePath));//FileUtils.getFilePath(this)
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            FileUtils.deleteFile(FileUtils.getDownloadedFilePath(this , filePath));
            stopForeground(false);
            mBuilder.setContentText("Download Error Occur")
                    .setOngoing(false)
                    .setProgress(0 , 0 , false);
            mNotifyManager.notify(NOTIFICATION_ID , mBuilder.build());

        }
        return false;
    }

    /**
     * Decrypt and return the decoded bytes
     *
     * @return
     */
    private byte[] decrypt() {
        //updateUI("Decrypting file...");
        try {
            byte[] fileData = FileUtils.readFile(FileUtils.getFilePath(this));
            byte[] decryptedBytes = EncryptDecryptUtils.decode(EncryptDecryptUtils.getInstance(this).getSecretKey() , fileData);
            return decryptedBytes;
        } catch (Exception e) {
            // updateUI("File Decryption failed.\nException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("forground" , "destroy");
    }
}

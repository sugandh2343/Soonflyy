package soonflyy.learning.hub.Download;

import static soonflyy.learning.hub.Common.Constant.FILE_EXT;
import static soonflyy.learning.hub.Common.Constant.TEMP_FILE_NAME;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class PlayDownlaodVideoActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    PlayerView playerView;
    ExoPlayer player;
    boolean fullscreen=false;
    ImageView fullscreen_btn;
    ConcatenatingMediaSource concatenatingMediaSource;
    String video_type,title,free_video_url;
    String course_id,lesson_id,section_id;
    int position,view_status;
    ArrayList<DownloadVideo> downloadVideoArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonMethods.disableScreenshot(getWindow());
        setFullScreen();
        setContentView(R.layout.activity_play_downlaod_video);
        bindId();
        player=new SimpleExoPlayer.Builder(this).build();
        getIntentData();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case ExoPlayer.STATE_BUFFERING:
                        Log.e("status","buffering");
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ExoPlayer.STATE_ENDED:
                        Log.e("status","ended");

                        break;
                    case ExoPlayer.STATE_IDLE:
                        Log.e("status","idle");
                        break;
                    case ExoPlayer.STATE_READY:
                        Log.e("status","ready");
                        progressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        fullscreen_btn.setOnClickListener(this);
    }
    private void getIntentData() {
        try {
            video_type = getIntent().getStringExtra("video_type");
            Log.e("video_type", "" + video_type);
            /// Toast.makeText(this, "videoType : " + video_type, Toast.LENGTH_SHORT).show();
            if (video_type.equals("download")) {
                position = getIntent().getIntExtra("position", 0);
                title = getIntent().getStringExtra("title");
                downloadVideoArrayList = getIntent().getExtras().getParcelableArrayList("videoList");
                playDownloadVideo();
                // decryptFile(downloadVideoArrayList.get(position).getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void bindId() {
        progressBar=findViewById(R.id.progress);
        playerView=findViewById(R.id.exo_player_view);
        fullscreen_btn=playerView.findViewById(R.id.exo_fullscreen_icon);
    }

    private void playDownloadVideo() throws IOException {

    String path=downloadVideoArrayList.get(position).getPath();
    Log.e("eFilePath",""+path);
       /* File tempDecodedFile=FileUtils.getDecodedTempFile(getApplicationContext(),decrypt(path));
        Log.e("dFilePath",""+tempDecodedFile.getPath());
        Uri uri=Uri.parse(tempDecodedFile.getPath());
       // player=new SimpleExoPlayer.Builder(this).build();
        DefaultDataSourceFactory defaultDataSourceFactory=new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,"app"));
        concatenatingMediaSource=new ConcatenatingMediaSource();
        for (int i=0;i<downloadVideoArrayList.size();i++){
            new File(String.valueOf(downloadVideoArrayList.get(i)));
            MediaSource mediaSource=new ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playError();

        */



       GetDecryptFileTask task= new GetDecryptFileTask();
       task.execute(path);

    }



    private class GetDecryptFileTask extends AsyncTask<String,Void,File>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog=new ProgressDialog(getApplicationContext());
          //  progressDialog.setMessage("please wait...");
           // progressDialog.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            String filePath=strings[0];
            File file=null;
            try {
               //file= FileUtils.getDecodedTempFile(getApplicationContext(),decrypt(filePath));
                file=File.createTempFile(TEMP_FILE_NAME, FILE_EXT, getApplicationContext().getCacheDir());
                EncryptDecryptUtils.decodeFile(getApplicationContext(),filePath,file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
           // progressDialog.dismiss();
            if (file!=null){
                Log.e("DecodePath: ",""+file.getPath());
                Uri uri=Uri.parse(file.getPath());

                DefaultDataSourceFactory defaultDataSourceFactory=new DefaultDataSourceFactory(getApplicationContext(),
                        Util.getUserAgent(getApplicationContext(),"app"));
                concatenatingMediaSource=new ConcatenatingMediaSource();
                for (int i=0;i<downloadVideoArrayList.size();i++){
                    new File(String.valueOf(downloadVideoArrayList.get(i)));
                    MediaSource mediaSource=new ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                            .createMediaSource(Uri.parse(String.valueOf(uri)));
                    concatenatingMediaSource.addMediaSource(mediaSource);
                }
                playerView.setPlayer(player);
                playerView.setKeepScreenOn(true);
                player.prepare(concatenatingMediaSource);
                player.seekTo(position, C.TIME_UNSET);
                playError();

            }
        }
    }



    private byte[] decrypt(String path) {
        try {
            byte[] fileData = FileUtils.readFile(path);
            byte[] decryptedBytes = EncryptDecryptUtils.decode(EncryptDecryptUtils.getInstance(this).getSecretKey(), fileData);
            return decryptedBytes;
        } catch (Exception e) {
            CommonMethods.showSuccessToast(getApplicationContext(),"Video play failed.\nException: " + e.getMessage());
        }
        return null;
    }
    private void playVideo(FileDescriptor fileDescriptor) {
        if (null == fileDescriptor) {
            return;
        }



        //player.play(fileDescriptor);
    }

    private void playError() {
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("PlayError","Download Play Error: "+error.getMessage());
                CommonMethods.showSuccessToast(getApplicationContext(),"Video Playing Error");
                //CommonMethods.showSuccessToast(getApplicationContext(),"Play Error: "+error.getMessage()+"\n"+"Error Code: "+error.getErrorCodeName());
            }
        });
        player.setPlayWhenReady(true);
    }

    public void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    private void playFullScreen() {
        if (fullscreen){
            fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            // getSupportActionBar().show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            //  params.height=(int)(200*getResources().getDisplayMetrics().density);
            params.height=params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            fullscreen=false;
            // cl_content.setVisibility(View.VISIBLE);

        }else{
            fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_exit_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            fullscreen=true;
            // cl_content.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (fullscreen) {
            Log.e("screen","land");
            playFullScreen();

        }else{
            if (player.isPlaying()) {
                player.stop();
            }
            super.onBackPressed();
            finish();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            if (fullscreen) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if (player.isPlaying()){
                player.stop();
            }
            if(player!=null){
                player.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exo_fullscreen_icon:
                playFullScreen();
                break;
        }
    }
}
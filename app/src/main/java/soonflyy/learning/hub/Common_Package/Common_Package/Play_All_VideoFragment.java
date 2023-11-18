package soonflyy.learning.hub.Common_Package.Common_Package;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.Video_Adapter;
import soonflyy.learning.hub.Common_Package.Adapters.Video_Speed_Quality_Adapter;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.Common_Package.Models.Video_Quality_Speed;
import soonflyy.learning.hub.Download.DownloadService;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.exoplayer.TrackSelectionDialog;


public class Play_All_VideoFragment extends Fragment implements View.OnClickListener {

    TextView tv_videoTitle,tv_downlaod;
    RecyclerView rv_video;
  // RelativeLayout frame_player;
    //ImageView fullScreen_btn,exo_setting_btn;
    LinearLayout lin_download;
    /////-----------------------//
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    //---------------//



    //PlayerView playerView;

    ArrayList<Video_Model> videoList=new ArrayList<>();
    Video_Adapter video_adapter;
    int play_position;
    ExoPlayer simpleExoPlayer;
    PopupWindow settingWindow;
//    ProgressBar progressBar;
//
    ArrayList<Video_Quality_Speed>speedList=new ArrayList<>();
    ArrayList<Video_Quality_Speed>qualityList=new ArrayList<>();
    Video_Speed_Quality_Adapter speed_quality_adapter;
    String selected_speed="Normal";

   // boolean fullscreen=false;
    String playingVideoUrl,courseName;

    ActivityResultLauncher<Intent> activityResultLauncher;
    CountDownTimer mTimer;

    public static RelativeLayout video_container;
    // RelativeLayout frame_player;
    public  static  ImageView fullScreen_btn,exo_setting_btn;
    public  static  LinearLayout lin_relativeView;
    public static PlayerView playerView;


    public static boolean fullscreen=false;
    public static String from;
    public Play_All_VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play__all__video, container, false);
        bindViewId(view);

       // simpleExoPlayer=new SimpleExoPlayer.Builder(getContext()).build();
        trackSelector = new DefaultTrackSelector(getActivity());
        simpleExoPlayer = new SimpleExoPlayer.Builder(getActivity()).setTrackSelector(trackSelector).build();

        getArgumentData();
        setVideo();
        tv_downlaod.setOnClickListener(this);
        fullScreen_btn.setOnClickListener(this);
        exo_setting_btn.setOnClickListener(this);



        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //for android 11 and above
                        if (result.getResultCode()==RESULT_OK) {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager())
                                    CommonMethods.showSuccessToast(getActivity(),"Permission Granted");
                                  //  Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                                else
                                    CommonMethods.showSuccessToast(getActivity(),"Permission Denied");
                                    //Toast.makeText(getActivity().getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        videoList=getArguments().getParcelableArrayList("videoList");
        play_position=getArguments().getInt("position");
        courseName=getArguments().getString("courseName");

        if (from.equals(SIMPLEE_HOME_STUDENT)){
            tv_videoTitle.setText("Video on "+getArguments().getString("courseName"));
            lin_download.setVisibility(View.VISIBLE);
        }else{
            lin_download.setVisibility(View.GONE);
        }
        playVideo(videoList.get(play_position));
    }

    private void setVideo() {
        video_adapter=new Video_Adapter(getContext(), videoList, new Video_Adapter.OnVideoClickListener() {
            @Override
            public void onVideoClick(int position) {
                //wrtie code to play video
                playVideo(videoList.get(position));
            }
        });
        rv_video.setAdapter(video_adapter);
        video_adapter.notifyDataSetChanged();
    }

    private void bindViewId(View view) {
        video_container = view.findViewById(R.id.video_container);
        lin_relativeView=view.findViewById(R.id.lin_relative_view);
       // frame_player=view.findViewById(R.id.container_player);
        lin_download=view.findViewById(R.id.lin_download);
        tv_videoTitle=view.findViewById(R.id.tv_title);
        tv_downlaod=view.findViewById(R.id.tv_download);
        rv_video=view.findViewById(R.id.rec_relatedVideo);
        fullScreen_btn=view.findViewById(R.id.exo_fullscreen_icon);
        exo_setting_btn=view.findViewById(R.id.exo_settings);
        playerView=view.findViewById(R.id.player_view);
       // progressBar=view.findViewById(R.id.loading_exoplayer);
        rv_video.setLayoutManager(new GridLayoutManager(getContext(),3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_download:
                goForDownload();
                break;
            case R.id.exo_fullscreen_icon:
                Log.e("full_screen","clicked");
//                playFullScreen();
                playFullScreen(getActivity (),fullscreen);
                break;
            case R.id.exo_settings:

                if (mTimer !=null){
                    mTimer.cancel();
                }
                if (settingWindow !=null){
                    if (settingWindow.isShowing()){
                        settingWindow.dismiss();
                    }else{
                        showVideoSettingPopup();
                    }
                }else{
                    showVideoSettingPopup();
                }

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //foe android 10 and below
        switch (requestCode){
            case STORAGE_READ_WRIT_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean readper=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean writeper=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (readper && writeper){
                      //  Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        CommonMethods.showSuccessToast(getActivity(),"Permission Granted");
                    }else{
                        CommonMethods.showSuccessToast(getActivity(),"Permission Denied");
                       // Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    CommonMethods.showSuccessToast(getActivity(),"Permission needs for download video");
                   // Toast.makeText(getApplicationContext(), "You Denied Permissions", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void goForDownload() {
        if (CommonMethods.checkStoragePermission(getContext())){
            //write code to download video
            if (!TextUtils.isEmpty(playingVideoUrl)) {
                Log.e("url",""+playingVideoUrl);
                Intent intent = new Intent(getActivity(), DownloadService.class);
                intent.putExtra("url", playingVideoUrl);
                intent.putExtra("fileType","Video");
                getActivity().startForegroundService(intent);
            }

        }else{
            CommonMethods.requestStoragePermission(getActivity(),STORAGE_READ_WRIT_REQUEST_CODE,activityResultLauncher);
        }
    }

    private void showVideoSettingPopup() {
       // PopupWindow popupWindow ;
        startTimer();
        LayoutInflater layoutInflater=(LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.dailog_video_setting,null);
        LinearLayout lin_speed=customView.findViewById(R.id.lin_speed);
        LinearLayout lin_quality=customView.findViewById(R.id.lin_quality);
        TextView tv_speed=customView.findViewById(R.id.tv_speed);
        TextView tv_quality=customView.findViewById(R.id.tv_quality);
        tv_speed.setText(selected_speed);
        settingWindow=new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        settingWindow.showAtLocation(playerView,Gravity.TOP,0,200);
      // settingWindow.showAsDropDown(exo_setting_btn, 0, -exo_setting_btn.getHeight()+settingWindow.getHeight());
        //popupWindow.showAtLocation(exo_setting_btn,Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        lin_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               settingWindow.dismiss();
               if (mTimer!=null){
                   mTimer.cancel();
               }
                showSpeedQualityPopup("speed");
            }
        });
        lin_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               settingWindow.dismiss();
                if (mTimer!=null){
                    mTimer.cancel();
                }
              //  showSpeedQualityPopup("quality");
                //showQualityPopup();
                if (!isShowingTrackSelectionDialog
                        && TrackSelectionDialog.willHaveContent(trackSelector)) {
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
//                    trackSelectionDialog.show(getActivity().getSupportFragmentManager(), /* tag= */ null);


                }


            }
        });


    }

    private void showQualityPopup() {
        PopupWindow quality_popup ;
        LayoutInflater layoutInflater=(LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.dailog_video_quality,null);
        LinearLayout lin_speed=customView.findViewById(R.id.lin_two_five);

        quality_popup=new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quality_popup.showAsDropDown(exo_setting_btn,-20,-20, Gravity.END);
        //popupWindow.showAtLocation(exo_setting_btn,Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        lin_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quality_popup.dismiss();

            }
        });
    }

    private void showSpeedQualityPopup(String type) {
        startTimer();
        ArrayList<Video_Quality_Speed>itemList=new ArrayList<>();

       // PopupWindow popupWindow ;
        LayoutInflater layoutInflater=(LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.dailog_video_speed,null);
        TextView tv_title=customView.findViewById(R.id.tv_title);
        RecyclerView recyclerView=customView.findViewById(R.id.rec_quality_speed);
        settingWindow=new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (type.equals("speed")){
            //speed
            tv_title.setText("Playback Speed");
            itemList.clear();
            initalizeSpeed();
            itemList.addAll(speedList);

        }else{
            //quality
            tv_title.setText("Quality");
            itemList.clear();
            initializeQuality();
            itemList.addAll(qualityList);
        }


        speed_quality_adapter=new Video_Speed_Quality_Adapter(getContext(), itemList, new Video_Speed_Quality_Adapter.OnItemSelectionListener() {
            @Override
            public void onItemSelect(int position, Video_Quality_Speed model) {
                //--managel logic depending on type --//
                Log.e("text",""+model.getText());
                if(type.equals("speed")){
                    //set play sepeed
                    selected_speed=model.getText();
                    if (model.getText().equals("Normal")){
                        setPlaySpeed("1");
                    }else {
                        setPlaySpeed(model.getText());
                    }

                }else{
                    //quality set
                }
                settingWindow.dismiss();
                if (mTimer !=null){
                    mTimer.cancel();
                }
                //---//

            }
        });
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(speed_quality_adapter);
        speed_quality_adapter.notifyDataSetChanged();
        Log.e("itemsize",""+itemList.size());
        Log.e("adaptersize",""+speed_quality_adapter.getItemCount());
        settingWindow.showAtLocation(playerView,Gravity.CENTER,0,0);
      //  popupWindow.showAsDropDown(tvde,-20,-20, Gravity.END);






    }

    private void setPlaySpeed(String text) {
        float speed=Float.parseFloat(text);
        PlaybackParameters param = new PlaybackParameters(speed);
        if (simpleExoPlayer !=null) {
            simpleExoPlayer.setPlaybackParameters(param);
        }
    }

    private void initializeQuality() {
        qualityList.clear();
        qualityList.add(new Video_Quality_Speed("Auto",true));
        qualityList.add(new Video_Quality_Speed("720p",false));
        qualityList.add(new Video_Quality_Speed("480p",false));
        qualityList.add(new Video_Quality_Speed("360p",false));
        qualityList.add(new Video_Quality_Speed("240p",false));
        qualityList.add(new Video_Quality_Speed("180p",false));

    }

    private void initalizeSpeed() {
        speedList.clear();
        speedList.add(new Video_Quality_Speed("0.25",false));
        speedList.add(new Video_Quality_Speed("0.5",false));
        speedList.add(new Video_Quality_Speed("0.75",false));
        speedList.add(new Video_Quality_Speed("Normal",true));
        speedList.add(new Video_Quality_Speed("1.25",false));
        speedList.add(new Video_Quality_Speed("1.5",false));
        speedList.add(new Video_Quality_Speed("1.75",false));
        speedList.add(new Video_Quality_Speed("2",false));

    }


    private void playVideo(Video_Model videoModel) {
     //   progressBar.setVisibility(View.VISIBLE);
        playingVideoUrl= BaseUrl.BASE_URL_MEDIA+videoModel.getFile();
        String hlsPath2="https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.mp4/.m3u8";
      // playingVideoUrl=hlsPath2;
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem=MediaItem.fromUri(playingVideoUrl);//hlsPath2
        simpleExoPlayer.addMediaItem(mediaItem);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare();
        // simpleExoPlayer.play();
        // player.prepare(concatenatingMediaSource);
        // simpleExoPlayer.seekTo(position, C.TIME_UNSET);
        playError();
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState){
                    case SimpleExoPlayer.STATE_BUFFERING:
                        Log.e("status","buffering");
                    //    progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SimpleExoPlayer.STATE_ENDED:
                        Log.e("status","ended");
                        ///playVideoinFullScreen("complete");

                        break;
                    case SimpleExoPlayer.STATE_IDLE:
                        Log.e("status","idle");
                        break;
                    case SimpleExoPlayer.STATE_READY:
                        Log.e("status","ready");
                       // progressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void playError() {
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                DynamicToast.make(getActivity(),"Video Playing Error..",2000);
            }
        });
        simpleExoPlayer.setPlayWhenReady(true);
    }

    /*
    private void playFullScreen() {
        if (fullscreen){
            fullScreen_btn.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_baseline_fullscreen_24));
          getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

            if (from.equals(SCHOOL_COACHING) || from.equals(SCHOOL_STUDENT)) {
                ((SchoolMainActivity) getActivity()).showHideHomeActionBar(true);
                ((SchoolMainActivity) getActivity()).showHideBottomNavigation(true);
            }else {
                ((MainActivity) getActivity()).getSupportActionBar().show();
                ((MainActivity) getActivity()).hideBottomNavigation(false);
            }
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=(int)(200*getActivity().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
            fullscreen=false;
            lin_relativeView.setVisibility(View.VISIBLE);
            // cl_content.setVisibility(View.VISIBLE);

        }else{
            fullScreen_btn.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_baseline_fullscreen_exit_24));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (from.equals(SCHOOL_COACHING) || from.equals(SCHOOL_STUDENT)) {
                ((SchoolMainActivity) getActivity()).showHideHomeActionBar(false);
                ((SchoolMainActivity) getActivity()).showHideBottomNavigation(false);
            }else {
                ((MainActivity) getActivity()).getSupportActionBar().hide();
                ((MainActivity) getActivity()).hideBottomNavigation(true);
            }
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            fullscreen=true;
            lin_relativeView.setVisibility(View.GONE);
            playerView.setKeepScreenOn(true);
            // cl_content.setVisibility(View.GONE);
        }
    }

     */


    public static void playFullScreen(Activity context, Boolean fullscreens) {
        if (fullscreens) {

            fullscreen = false;

            fullScreen_btn.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_fullscreen_24));
            context.getWindow ( ).getDecorView ( ).setSystemUiVisibility (View.SYSTEM_UI_FLAG_VISIBLE);

            if (from.equals (SCHOOL_COACHING) || from.equals (SCHOOL_STUDENT)) {
                ((SchoolMainActivity)context).showHideHomeActionBar (true);
                ((SchoolMainActivity)context).showHideBottomNavigation (true);
            } else {
                ((MainActivity) context).getSupportActionBar ( ).show ( );
                ((MainActivity) context).hideBottomNavigation (false);
            }


            ((ViewGroup) playerView.getParent ( )).removeView (playerView);
            ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (200*context.getResources ().getDisplayMetrics ().density));
            video_container.addView(playerView, layout);

            context.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            playerView.setKeepScreenOn (true);
            lin_relativeView.setVisibility (View.VISIBLE);



        }


        else {


            context.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            context.getWindow ( ).getDecorView ( ).setSystemUiVisibility (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            if (playerView.getParent ( ) instanceof ViewGroup) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams ( );
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams (params);
                ((ViewGroup) playerView.getParent ( )).removeView (playerView);
                //ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ViewGroup group = context.findViewById (android.R.id.content);
                group.addView (playerView, params);
            }



            fullScreen_btn.setImageDrawable (ContextCompat.getDrawable (context, R.drawable.ic_baseline_fullscreen_exit_24));

            if (from.equals (SCHOOL_COACHING) || from.equals (SCHOOL_STUDENT)) {
                ((SchoolMainActivity) context).showHideHomeActionBar (false);
                ((SchoolMainActivity)context).showHideBottomNavigation (false);
            } else {
                ((MainActivity) context).getSupportActionBar ( ).hide ( );
                ((MainActivity) context).hideBottomNavigation (true);
            }

            fullscreen = true;
            //playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            lin_relativeView.setVisibility (View.GONE);
            playerView.setKeepScreenOn (true);



        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_STUDENT)){
            ((MainActivity)getActivity()).setStudentChildActionBar("Video on "+getArguments().getString("courseName"),false);
        }
        try {
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (simpleExoPlayer!=null){
                simpleExoPlayer.stop();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.getPlaybackState();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            if (settingWindow !=null){
                if (settingWindow.isShowing()){
                    settingWindow.dismiss();
                }
            }
            if (mTimer!=null){
                mTimer.cancel();
            }

            if (fullscreen) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                ((MainActivity) getActivity()).getSupportActionBar().show();
                // ((MainActivity) getActivity()).setBottomNavigationVisibility(true);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if (simpleExoPlayer.isPlaying()) {
                simpleExoPlayer.stop();
               // simpleExoPlayer.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    ///settting dailog dismiss---------------------//

    private void startTimer(){
        new Handler(getActivity().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                long milSecond =10000;

                    mTimer=new CountDownTimer(milSecond, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            try {
                               if (settingWindow.isShowing()){
                                   settingWindow.dismiss();
                               }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

        });
    }


    //------------------------------------------//


    ///---Video Quality speed adapter and model classes---///


    ///----------///


    //-----------



    //-----------

}
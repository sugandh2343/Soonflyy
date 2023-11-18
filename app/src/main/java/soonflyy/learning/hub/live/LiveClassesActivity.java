package soonflyy.learning.hub.live;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission_group.CAMERA;
import static android.view.WindowManager.LayoutParams.FLAG_SECURE;
import static soonflyy.learning.hub.Common.ApiCode.CREATE_LIVE_WATCH_HISTORY;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_NAME;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.VideoTrack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.Participant;
import live.videosdk.rtc.android.Stream;
import live.videosdk.rtc.android.VideoSDK;
import live.videosdk.rtc.android.VideoView;
import live.videosdk.rtc.android.listeners.MeetingEventListener;
import live.videosdk.rtc.android.listeners.ParticipantEventListener;


public class LiveClassesActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponseListener {

    ImageView arrow_back_img, ivScreenRotation;
    boolean fullscreen = false;
    LinearLayout linBottom;
    RelativeLayout relVideo;
    View viewTitle;
    TextView tv_title, tv_description, tv_duration, tv_status, tvLeave;

    String title, startTime, endTime, description, slug, type, liveType, fromPage;
    long duration;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private boolean mTimerRunning;
    Participant localParticipaint;
    int joinCount=0;


    String course_id, live_id, demo_count, user_id;
    String tocken = "", meetingId = "", hostId = "";
    public VideoView localView;
    private boolean micEnabled = false;
    private boolean webcamEnabled = false;
    int history_count;
    boolean hasHistoryApiCall = false;
    //-----meeting----------//
    private static final int PERMISSION_REQ_ID = 22;
    private Meeting meeting;
    SessionManagement sessionManagement;
    private static final String[] REQUESTED_PERMISSIONS = {RECORD_AUDIO, CAMERA, CALL_PHONE, BLUETOOTH_CONNECT};

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(FLAG_SECURE, FLAG_SECURE);
        setContentView(R.layout.activity_live_classes);
        bindId();
        sessionManagement = new SessionManagement(this);
        getArgumentData();
        setDuration();
//       VideoSDK.initialize(this);
        arrow_back_img.setOnClickListener(this);
        ivScreenRotation.setOnClickListener(this);
        tvLeave.setOnClickListener(this);
       chekPermissionsForLive();
       // startMeeting();

    }

    private void bindId() {
        localView = findViewById(R.id.localView);
        viewTitle = findViewById(R.id.include);
        linBottom = findViewById(R.id.bottom_layout);
        ivScreenRotation = findViewById(R.id.iv_screen_rotation);
        relVideo = findViewById(R.id.rel_video);

        tvLeave = findViewById(R.id.tv_leave);
        arrow_back_img = findViewById(R.id.arrow_back_img);
        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.description_tv);
        tv_duration = findViewById(R.id.tv_duration);
        tv_status = findViewById(R.id.tv_status);

    }

    private void chekPermissionsForLive() {
        requestPermissions(REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
    }

    private void startMeeting() {
        String token = TokenGeneration.getTocken();
        VideoSDK.config(token);//tocken
        String participaintName = "";
        if (fromPage.equals(SIMPLEE_HOME_STUDENT)) {
            participaintName = sessionManagement.getString(NAME);
        } else if (fromPage.equals(SIMPLEE_HOME_TUTOR)) {
            //FOR ASSIGN CASE
            participaintName = sessionManagement.getString(NAME);
        } else if (fromPage.equals(SCHOOL_STUDENT)) {
            participaintName = sessionManagement.getString(SCHOOL_STUDENT_NAME);
        } else if (fromPage.equals(SCHOOL_COACHING)) {
            participaintName = sessionManagement.getString(SCHOOL_NAME);
        }
        Log.e("meetingIddfd", "" + meetingId);
        Log.e("token", "" + tocken);
        Log.e("participaint_name", "" + participaintName);
        if (meetingId != null && token != null && participaintName != null) {
            meeting = VideoSDK.initMeeting(
                    LiveClassesActivity.this, meetingId, participaintName,
                    micEnabled, webcamEnabled, null, null);

            // 3. Add event listener for listening upcoming events
            meeting.addEventListener(new MeetingEventListener() {
                @Override
                public void onMeetingJoined() {
                    super.onMeetingJoined();
                    Log.d("#meeting", "onMeetingJoined()");


                }

                @Override
                public void onMeetingLeft() {
                    super.onMeetingLeft();
                    Log.d("#meeting", "onMeetingLeft()");
                   // meeting = null;
                    localView.releaseSurfaceViewRenderer();

                    if (!isDestroyed()) {
                        Log.e("liveDiestokldkfjld", "distroy");
                        if (meeting != null) {
                            meeting.removeAllListeners();
                            meeting.getLocalParticipant().removeAllListeners();
                            meeting.leave();
                            meeting = null;
                        }
                        finish();

                    }
                }

                @Override
                public void onParticipantJoined(Participant participant) {
                    super.onParticipantJoined(participant);
                    Log.e("ParticipaintName", "" + participant.getDisplayName());
                    Log.e("hostId", "" + hostId);
                    Log.e("participantIdfdfd", "" + participant.getId());

                        if (participant.getId().equals(hostId)) {
                            Toast.makeText(LiveClassesActivity.this, participant.getDisplayName() + " joined", Toast.LENGTH_SHORT).show();
                            setHostVideo(participant);
                        }

                }

                @Override
                public void onParticipantLeft(Participant participant) {
                    super.onParticipantLeft(participant);
                    Log.e("pLeft", "" + participant.getDisplayName() + " left");
                }

                @Override
                public void onError(JSONObject error) {
                    super.onError(error);
                    Log.e("liveError", "" + error);
                }
            });

            //4. Join VideoSDK Meeting
            meeting.join();
        } else {
            CommonMethods.showSuccessToast(this, "Tutor is not live");
            finish();
        }

    }

//    private final MeetingEventListener meetingEventListener = new MeetingEventListener() {
//        @Override
//        public void onMeetingJoined() {
//            Log.d("#meeting", "onMeetingJoined()");
//        }
//
//        @Override
//        public void onMeetingLeft() {
//            Log.d("#meeting", "onMeetingLeft()");
//            meeting = null;
//            localView.releaseSurfaceViewRenderer();
//            if (!isDestroyed()) {
//                Log.e("liveDiestokldkfjld", "distroy");
//                finish();
//
//            }
//        }
//
//        @Override
//        public void onParticipantJoined(Participant participant) {
//
//            Log.e("ParticipaintName", "" + participant.getDisplayName());
//            Log.e("hostId", "" + hostId);
//            Log.e("participantIdfdfd", "" + participant.getId());
//            if (participant.getId().equals(hostId)) {
//                Toast.makeText(LiveClassesActivity.this, participant.getDisplayName() + " joined", Toast.LENGTH_SHORT).show();
//                setHostVideo(participant);
//            }
//
//        }
//
//        @Override
//        public void onParticipantLeft(Participant participant) {
//            Log.e("pLeft", "" + participant.getDisplayName() + " left");
//            //Toast.makeText(LiveClassesActivity.this, participant.getDisplayName() + " left", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(JSONObject error) {
//            super.onError(error);
//            Log.e("liveError", "" + error);
//        }
//    };

    private void setHostVideo(Participant participant) {
        localParticipaint=participant;
        for (Map.Entry<String, Stream> entry : participant.getStreams().entrySet()) {
            Stream stream = entry.getValue();
            if (stream.getKind().equalsIgnoreCase("video")) {
                Log.e("run_liveoce", "live started");
                VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                localView.addTrack(videoTrack);
                tv_status.setVisibility(View.GONE);
                if (liveType.equals("demo")) {
                    sendRequest(CREATE_LIVE_WATCH_HISTORY);
                }
                break;

            }
        }

        participant.addEventListener(new ParticipantEventListener() {
            @Override
            public void onStreamEnabled(Stream stream) {
                Log.e("streamedEnaljdl", "true");
                try {


                    if (stream.getKind().equalsIgnoreCase("video")) {
                        Log.e("run_stream", "live....");
                        VideoTrack videoTrack = (VideoTrack) stream.getTrack();
                        localView.addTrack(videoTrack);
                        tv_status.setVisibility(View.GONE);
                        if (liveType.equals("demo")) {
                            sendRequest(CREATE_LIVE_WATCH_HISTORY);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStreamDisabled(Stream stream) {
                super.onStreamDisabled(stream);
                Log.e("streamedEnaljdl", "false");
            }
        });

    }


    private void getArgumentData() {
        title = getIntent().getStringExtra("title");
        startTime = getIntent().getStringExtra("sTime");
        endTime = getIntent().getStringExtra("eTime");
        slug = getIntent().getStringExtra("slug");
        description = getIntent().getStringExtra("description");
        liveType = getIntent().getStringExtra("live_type");
        type = getIntent().getStringExtra("type");
        tocken = getIntent().getStringExtra("token");
        meetingId = getIntent().getStringExtra("meeting_id");
        hostId = getIntent().getStringExtra("hostId");
        fromPage = getIntent().getStringExtra("from");
        Log.e("fromPage", "" + fromPage);

        if (liveType.equalsIgnoreCase("demo")) {
            course_id = getIntent().getStringExtra("course_id");
            live_id = getIntent().getStringExtra("live_id");
            user_id = getIntent().getStringExtra("user_id");
            demo_count = getIntent().getStringExtra("demo_count");
            history_count = Integer.parseInt(getIntent().getStringExtra("history_count"));
        }
    }


    private void setDuration() {
        int liveDuration = CommonMethods.getDurationBetweenTime(startTime, endTime);
        tv_duration.setText("Duration : " + liveDuration + " min");
        tv_description.setText(description);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        int leftduration = CommonMethods.getLeftDurationLiveTime(currentTime, endTime);
        Log.e("duration_setdfd", "" + leftduration);
        mTimeLeftInMillis = (long) leftduration * 60 * 1000;
        if (mTimeLeftInMillis <= 0) {
            finish();
        } else {
            startTimer();
        }

    }

    private void startTimer() {
        mTimerRunning = true;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer.start();

        } else {
            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;

                }

                @Override
                public void onFinish() {
                    mTimerRunning = false;
                    mCountDownTimer.cancel();
                    if (meeting != null) {
                        meeting.leave();
                    }

                }
            }.start();
        }
    }

    private void goLive(String slug) {

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case CREATE_LIVE_WATCH_HISTORY:
                params.put("course_id", course_id);
                params.put("user_id", user_id);
                params.put("liveclass_id", live_id);
                params.put("demo_count", demo_count);
                callApi(CREATE_LIVE_WATCH_HISTORY, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, this);
        switch (request) {
            case CREATE_LIVE_WATCH_HISTORY:
                service.postDataVolley(CREATE_LIVE_WATCH_HISTORY,
                        BaseUrl.CREATE_LIVE_WATCH_HISTORY, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType) {
            case CREATE_LIVE_WATCH_HISTORY:
                Log.e("watch_history ", response);
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    history_count = history_count + 1;
                    try {

                        showLefLiveDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
        }
    }

    private void showLefLiveDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_live_demo_left);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        Button btn_confirm_bye = dialog.findViewById(R.id.btn_confirm);
        TextView tv_msg = dialog.findViewById(R.id.tv_live_left);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        int left = Integer.parseInt(demo_count) - history_count;
        tv_msg.setText("You have only " + left + "/" + demo_count + "\ndemo left");
//        if (left==0){
//            btn_confirm_bye.setText("Purchase Course");
//           // tv_cancel.setVisibility(View.VISIBLE);
//        }
        dialog.show();
        btn_confirm_bye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onBackPressed();
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_back_img:
            case R.id.tv_leave:
                showConfirmation();
                break;
            case R.id.iv_screen_rotation:
                playFullScreen();
                break;

        }
    }

    public void showConfirmation() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_logout);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.setCancelable(false);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.tv_no);
        TextView tv_yes = dialog.findViewById(R.id.tv_yes);
        TextView tvMsg = dialog.findViewById(R.id.tv_udpate_title);
        tvMsg.setText("Are you sure to exit?");
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mCountDownTimer != null && mTimerRunning) {
                    mCountDownTimer.cancel();
                }
                if (meeting == null) {
                    finish();
                }
                if (meeting != null) {
                    meeting.leave();
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
       //super.onBackPressed();
        showConfirmation();


    }


    @Override
    public void onResume() {
        super.onResume();
        tv_title.setText("Live Class On " + title);
     //   pauseAndResumeVideo(1);

    }

//    @Override
//    protected void onDestroy() {
//
//       if (mCountDownTimer!=null && mTimerRunning) {
//            mCountDownTimer.cancel();
//        }
//       if (meeting!=null){
//           meeting.leave();
//       }
//       localView.releaseSurfaceViewRenderer();
//        super.onDestroy();
//
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void playFullScreen() {
        if (fullscreen) {
            //fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            // getSupportActionBar().show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relVideo.getLayoutParams();
            params.width = params.MATCH_PARENT;
            //  params.height=(int)(200*getResources().getDisplayMetrics().density);
            params.height = 650;
            relVideo.setLayoutParams(params);
            fullscreen = false;
            viewTitle.setVisibility(View.VISIBLE);
            linBottom.setVisibility(View.VISIBLE);
            // cl_content.setVisibility(View.VISIBLE);

        } else {
            //fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_exit_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relVideo.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            relVideo.setLayoutParams(params);
            fullscreen = true;
            viewTitle.setVisibility(View.GONE);
            linBottom.setVisibility(View.GONE);
            // cl_content.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_ID) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("countpermission","true..");
                    startMeeting();
                    // checkPermissionForLive();
                } else {
                    CommonMethods.showSuccessToast(this, "Permission require");
                    finish();
                }
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       // pauseAndResumeVideo(0);

    }

    private void pauseAndResumeVideo(int value){
        if (meeting!=null) {
            //localParticipaint = meeting.getLocalParticipant();
            if (localParticipaint!=null){
                Log.e("Pause",""+value);
            for (Map.Entry<String, Stream> entry : localParticipaint.getStreams().entrySet()) {
                Stream stream = entry.getValue();
                if (value==1) {
                    stream.resume();
                }else if (value==0){
                    stream.pause();
                }
            }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (meeting != null) {
            meeting.removeAllListeners();
            meeting.getLocalParticipant().removeAllListeners();
            meeting.leave();
            meeting = null;
        }
    }
}
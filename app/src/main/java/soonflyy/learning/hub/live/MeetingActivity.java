package soonflyy.learning.hub.live;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission_group.CAMERA;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_NAME;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.VideoTrack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.Participant;
import live.videosdk.rtc.android.Stream;
import live.videosdk.rtc.android.VideoSDK;
import live.videosdk.rtc.android.VideoView;
import live.videosdk.rtc.android.listeners.MeetingEventListener;
import live.videosdk.rtc.android.listeners.ParticipantEventListener;

public class MeetingActivity extends AppCompatActivity {
    //full screen
    boolean fullscreen = false;
    private Meeting meeting;
    public VideoView localView;
    private boolean micEnabled = true;
    private boolean webcamEnabled = true;
    ParticipantAdapter studentAdapter;
    PopupWindow settingWindow;
    ImageView ivBackBtn, ivScreenRotation;
    View viewTitle;
    TextView tvTitle, tvStatus;
    // ArrayList<Participant>participantList=new ArrayList<>();
    private String sampleToken = "", meetingId = "", fromPage = "", liveClassId = "", liveTopic = "", participaintName = "", participaintId;
    String startTime, endTime;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private boolean mTimerRunning;
    SessionManagement sessionManagement;
    private static final int PERMISSION_REQ_ID = 22;
    boolean isAwakeFromPauseState=false;

    private static final String[] REQUESTED_PERMISSIONS = {RECORD_AUDIO, CAMERA, CALL_PHONE,BLUETOOTH_CONNECT};

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
        setContentView(R.layout.activity_meeting);
        tvTitle = findViewById(R.id.tv_title);
        tvStatus = findViewById(R.id.tv_status);
        ivBackBtn = findViewById(R.id.arrow_back_img);
        viewTitle = findViewById(R.id.include);
        ivScreenRotation = findViewById(R.id.iv_screen_rotation);
        sessionManagement = new SessionManagement(this);
        getIntentData();
       VideoSDK.initialize(getApplicationContext());
        sampleToken = TokenGeneration.getTocken();
        //createMeeting(sampleToken);

        checkPermissionForLive();

        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmation();
            }
        });
        ivScreenRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFullScreen();
            }
        });
        setDuration();
    }

    private void checkPermissionForLive() {
        requestPermissions(REQUESTED_PERMISSIONS,PERMISSION_REQ_ID);
//        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
//        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);
//        // checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID);
//        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID);
//        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED) {
//            if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED) {
//                if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[2]) == PackageManager.PERMISSION_GRANTED) {
//                    Log.e("logldfjdlkf","created");
//                    createMeeting(sampleToken);
//                } else {
//                    ActivityCompat.requestPermissions(this, new String[]{REQUESTED_PERMISSIONS[2]}, PERMISSION_REQ_ID);
//                }
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{REQUESTED_PERMISSIONS[1]}, PERMISSION_REQ_ID);
//            }
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{REQUESTED_PERMISSIONS[0]}, PERMISSION_REQ_ID);
//        }
    }

    private void getIntentData() {
        liveTopic = getIntent().getStringExtra("title");
        liveClassId = getIntent().getStringExtra("live_id");
        fromPage = getIntent().getStringExtra("from");
        startTime = getIntent().getStringExtra("sTime");
        endTime = getIntent().getStringExtra("eTime");
        tvTitle.setText("Live class On" + liveTopic);

    }

    // creating the MeetingEventListener
    private void startMeeting() {
        final String token = sampleToken;//getIntent().getStringExtra("token");
        //final String meetingId = getIntent().getStringExtra("meetingId");
        localView = findViewById(R.id.localView);

        // 1. Configuration VideoSDK with Token
        VideoSDK.config(token);
        // 2. Initialize VideoSDK Meeting
        if (fromPage.equals(SIMPLEE_HOME_TUTOR)) {
            participaintId = sessionManagement.getString(USER_ID);
            participaintName = sessionManagement.getString(NAME);
        } else if (fromPage.equals(SCHOOL_TUTOR)) {
            participaintId = sessionManagement.getString(SCHOOL_TEACHER_ID);
            participaintName = sessionManagement.getString(SCHOOL_T_NAME);
        } else if (fromPage.equals(INDEPENDENT_TUTOR)) {
            participaintId = sessionManagement.getString(SCHOOL_IT_ID);
            participaintName = sessionManagement.getString(SCHOOL_IT_NAME);
        }
        meeting = VideoSDK.initMeeting(
                MeetingActivity.this, meetingId, participaintName,
                micEnabled, webcamEnabled, participaintId, null);

        // 3. Add event listener for listening upcoming events
        meeting.addEventListener(meetingEventListener);

        //4. Join VideoSDK Meeting
        meeting.join();

        // ((TextView)findViewById(R.id.tvMeetingId)).setText(meetingId);//meetingId
        studentAdapter = new ParticipantAdapter(meeting);

//        final RecyclerView rvParticipants = findViewById(R.id.rvParticipants);
//        rvParticipants.setLayoutManager(new GridLayoutManager(this, 2));
//        rvParticipants.setAdapter(studentAdapter);//new ParticipantAdapter(meeting)
        meeting.getLocalParticipant().addEventListener(new ParticipantEventListener() {
            @Override
            public void onStreamEnabled(Stream stream) {
                Log.e("streamedEnaljdl","true");
                // super.onStreamEnabled(stream);
                if (stream.getKind().equalsIgnoreCase("video")) {
                    VideoTrack track = (VideoTrack) stream.getTrack();
                    localView.addTrack(track);
                    tvStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStreamDisabled(Stream stream) {
                Log.e("streamedEnaljdl","false");
                super.onStreamDisabled(stream);

            }
        });

        localView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (settingWindow != null && settingWindow.isShowing()) {
                    settingWindow.dismiss();
                } else {
                    showPopupWindow();
                }
                return false;
            }
        });
    }

    private final MeetingEventListener meetingEventListener = new MeetingEventListener() {
        @Override
        public void onMeetingJoined() {
            Log.d("#meeting", "onMeetingJoined()");
        }

        @Override
        public void onMeetingLeft() {
            Log.d("#meeting", "onMeetingLeft()");
            meeting = null;
            if (!isDestroyed()) finish();
        }

        @Override
        public void onParticipantJoined(Participant participant) {
            Toast.makeText(MeetingActivity.this, participant.getDisplayName() + " joined", Toast.LENGTH_SHORT).show();
            //  participantList.add(participant);
            //  Log.e("participaintList",""+participantList.size());
//            Toast.makeText(MeetingActivity.this,
//                    " Total Prticipaint: "+participantList.size(), Toast.LENGTH_SHORT).show();
            // studentAdapter.notifyDataSetChanged();
        }

        @Override
        public void onParticipantLeft(Participant participant) {
            Toast.makeText(MeetingActivity.this, participant.getDisplayName() + " left", Toast.LENGTH_SHORT).show();
            studentAdapter.notifyDataSetChanged();
        }
    };


    private void showPopupWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.layout_popupp_window, null);
        TextView tvMic = customView.findViewById(R.id.tv_mic);
        TextView tvStop = customView.findViewById(R.id.tv_stop);
        TextView tvStudent = customView.findViewById(R.id.tv_students);
        TextView tvSwitchCamera = customView.findViewById(R.id.tv_switch_camera);
        TextView tvLeave = customView.findViewById(R.id.tv_leave);
        if (micEnabled) {
            tvMic.setText("Mute");
            tvMic.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_mic_on, 0, 0);
        } else {
            tvMic.setText("Unmute");
            tvMic.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_mic_off, 0, 0);
        }
        if (webcamEnabled) {
            tvStop.setText("Stop");
            tvStop.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.baseline_videocam_24, 0, 0);
        } else {
            tvStop.setText("Start");
            tvStop.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.baseline_videocam_off_24, 0, 0);
        }

        settingWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        settingWindow.showAtLocation(localView, Gravity.BOTTOM, 0, 200);
        tvMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingWindow.dismiss();
                muteUnMuteMic();
            }
        });
        tvSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch camera
                settingWindow.dismiss();
                switchCamera();
            }
        });
        tvStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show student list
                settingWindow.dismiss();
                showStudentList();
            }
        });
        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop video
                settingWindow.dismiss();
                stopCamera();
                //playFullScreen();
            }
        });
        tvLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingWindow.dismiss();
                showConfirmation();
                //meeting.end();//end session for all
                //meeting.leave();//for leave other will stay in metting
            }
        });
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
        tvMsg.setText("Are you sure to end live class?");
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
                if (mTimerRunning && mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                if (meeting != null) {
                    meeting.end();
                }else{
                    finish();
                }

            }
        });


    }


    private void stopCamera() {
        if (webcamEnabled) {
            // this will disable the local participant webcam
            meeting.disableWebcam();
            Toast.makeText(MeetingActivity.this, "Webcam Disabled", Toast.LENGTH_SHORT).show();
        } else {
            // this will enable the local participant webcam
            meeting.enableWebcam();
            Toast.makeText(MeetingActivity.this, "Webcam Enabled", Toast.LENGTH_SHORT).show();
        }
        webcamEnabled = !webcamEnabled;
    }

    private void showStudentList() {
        showStudentDialog();

    }

    private void showStudentDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_participant);
//        dialog.getWindow().
//                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

//        View view=LayoutInflater.from(this).inflate(R.layout.layout_participant,null);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        RecyclerView recStudents = dialog.findViewById(R.id.rec_students);
        recStudents.setLayoutManager(new LinearLayoutManager(this));
        //Toast.makeText(this, "Size"+participantList.size()+"\nName:"+participantList.get(0).getDisplayName() , Toast.LENGTH_SHORT).show();

        recStudents.setAdapter(studentAdapter);
        tvTitle.setText("Students(" + studentAdapter.getItemCount() + ")");
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // adapter.notifyDataSetChanged();
        dialog.show();
    }

    private void switchCamera() {
        meeting.changeWebcam();
    }

    private void muteUnMuteMic() {
        if (micEnabled) {
            // this will mute the local participant's mic
            meeting.muteMic();
            Toast.makeText(MeetingActivity.this, "Mic Disabled", Toast.LENGTH_SHORT).show();
        } else {
            // this will unmute the local participant's mic
            meeting.unmuteMic();
            Toast.makeText(MeetingActivity.this, "Mic Enabled", Toast.LENGTH_SHORT).show();
        }
        micEnabled = !micEnabled;
    }

    //-----create meeting id-------------//
    private void createMeeting(String token) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response will contain `roomId`
                            meetingId = response.getString("roomId");
                            updateTokenOnServer(meetingId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(MeetingActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTokenOnServer(String mId) {

        String sendurl = null;
        // if (fromPage.equals(SIMPLEE_HOME_TUTOR)){
        sendurl = BaseUrl.URL_GENERATE_LIVE_TOKEN;
        // }
        HashMap<String, String> params = new HashMap<>();
        params.put("liveclass_id", liveClassId);//from to
        params.put("meeting_id", mId);
        params.put("token", sampleToken);
        if (sendurl != null) {
            CommonMethods.postRequest(sendurl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("add_message ", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            startMeeting();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    CommonMethods.showSuccessToast(MeetingActivity.this
                            , error.getMessage());
                }
            });
        } else {

            CommonMethods.showSuccessToast(MeetingActivity.this, "Something  Went Wrong");
        }


    }

    @Override
    public void onBackPressed() {
        showConfirmation();
    }

    private void setDuration() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        int leftduration = CommonMethods.getLeftDurationLiveTime(currentTime, endTime);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingWindow != null && settingWindow.isShowing()) {
            settingWindow.dismiss();
        }
        if (mTimerRunning && mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (meeting != null) {
            meeting.end();
        }
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void playFullScreen() {
        if (fullscreen) {
            //fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            // getSupportActionBar().show();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            //  params.height=(int)(200*getResources().getDisplayMetrics().density);
            params.height = params.MATCH_PARENT;
            localView.setLayoutParams(params);
            fullscreen = false;
            viewTitle.setVisibility(View.VISIBLE);
            // cl_content.setVisibility(View.VISIBLE);

        } else {
            //fullscreen_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_fullscreen_exit_24));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            //getSupportActionBar().hide();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            localView.setLayoutParams(params);
            fullscreen = true;
            viewTitle.setVisibility(View.GONE);
            // cl_content.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_ID) {

            if (grantResults.length>0){
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    createMeeting(sampleToken);
                   // checkPermissionForLive();
                }else{
                    CommonMethods.showSuccessToast(this,"Permission require");
                    finish();
                }
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTitle.setText("Live class On" + liveTopic);
        if (isAwakeFromPauseState) {
            if (meeting!=null) {
                meeting.enableWebcam();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAwakeFromPauseState=true;
    }


}
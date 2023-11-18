package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import static soonflyy.learning.hub.Common.ApiCode.GET_LIVE_CLASS_DEMO;
import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_COMPLETE;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common.VideoChooser;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.RecordedDemoVideoAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter.LiveDemoClassesAdapter;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.live.MeetingActivity;
import soonflyy.learning.hub.model.VideoUploadUri;
import soonflyy.learning.hub.services.UploadFileService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LiveDemoClassesFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rec_liveclasses, recDemoVideo;
    LinearLayout linUploadRecordeVideo;
    TextView tvSelectedPath;
    SwipeRefreshLayout refreshLayout;
    ArrayList<T_LiveModel> liveList = new ArrayList<>();
    LiveDemoClassesAdapter demoClassesAdapter;


    private ArrayList<String> videoPathList = new ArrayList<>();
    String videoFileName, video_title, course_id;
    private ArrayList<Uri> videoUriList = new ArrayList<>();

    private FileUploadResultReceiver resultReceiver;
    String id = "";

    //-------Demo video---//
    ArrayList<Video_Model> demoVideoList = new ArrayList<>();
    RecordedDemoVideoAdapter videoAdapter;

    ActivityResultLauncher<Intent> activityResultLauncher;

    //-------------------//

    public LiveDemoClassesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_demo_classes, container, false);
        course_id = getArguments().getString("course_id");
        initView(view);
        sendApiRequest();
        setLiveClasses();
        setDemoVideo();

        resultReceiver = new FileUploadResultReceiver(new Handler(Looper.getMainLooper()));
       createActivityLauncher();
        return view;
    }

    private void createActivityLauncher() {
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
    }

    private void setDemoVideo() {
        videoAdapter = new RecordedDemoVideoAdapter(getContext(), demoVideoList,
                new RecordedDemoVideoAdapter.OnVide0Listener() {
                    @Override
                    public void onItemClick(int position, Video_Model model) {

                    }

                    @Override
                    public void onDelete(int position, Video_Model model) {
                        id=model.getId();
                        showDeleteDialog("demo");
                    }
                });
        recDemoVideo.setAdapter(videoAdapter);

    }

    private void sendApiRequest() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(GET_LIVE_CLASS_DEMO);
        }
    }


    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        recDemoVideo = view.findViewById(R.id.rec_demo_video);
        linUploadRecordeVideo = view.findViewById(R.id.lin_upload);
        rec_liveclasses = view.findViewById(R.id.rec_liveclasses);
        recDemoVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_liveclasses.setLayoutManager(new LinearLayoutManager(getActivity()));
        linUploadRecordeVideo.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);

    }

    private void setLiveClasses() {
        demoClassesAdapter = new LiveDemoClassesAdapter(getActivity(), liveList,
                new LiveDemoClassesAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position) {

                    }

                    @Override
                    public void onGoLive(int position, T_LiveModel model) {
                        Intent lIntent=new Intent(getActivity(), MeetingActivity.class);
                        lIntent.putExtra("title", model.getTitle());
                        lIntent.putExtra("sTime", model.getStart_time());
                        lIntent.putExtra("eTime", model.getEnd_time());
                        lIntent.putExtra("slug", model.getSlug());
                        lIntent.putExtra("live_type", "chapter");
                        lIntent.putExtra("type", "teacher");
                        lIntent.putExtra("live_id",model.getId());
                        lIntent.putExtra("from",SIMPLEE_HOME_TUTOR);
                        lIntent.putExtra("description", model.getDescription());
                        getActivity().startActivity(lIntent);
                    }

                    @Override
                    public void onDelete(int position, String liveId) {
                        id=liveId;
                        showDeleteDialog("live");
                    }
                });
        rec_liveclasses.setAdapter(demoClassesAdapter);

        // lin_shimmer.setVisibility(View.GONE);
        //  lin_rec.setVisibility(View.VISIBLE)

    }

    private void showDeleteDialog(String type) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete ?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectivityReceiver.isConnected()){
                            if (type.equals("live"))
                            sendRequest(ApiCode.DELETE_LIVE_CLASS);
                            else
                                sendRequest(ApiCode.DELETE_VIDEO);
                        }else{
                            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                        }

                        dialog.dismiss();
                    }
                }).show();
    }
    private void showUploadDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_livevideo);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        TextView tvDialogTitle = dialog.findViewById(R.id.tv_title_video);
        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        ImageView iv_sub = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_back);
        Button btn_upload = dialog.findViewById(R.id.btn_upload);
        tvSelectedPath=dialog.findViewById(R.id.tv_seleted_path);
        tvDialogTitle.setText("Recorded Demo Video");

        dialog.show();
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_title = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(video_title)) {
                    et_name.setError("Enter title");
                    et_name.requestFocus();
                } else if (videoUriList.size() == 0) {
                    CommonMethods.showSuccessToast(getContext(), "Choose video file");
                } else {
                    //upload task
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        addTopicVideo();
                        //CommonMethods.showSuccessToast(getContext(), "Upload Successfully");
                        dialog.dismiss();
                    }
                }

            }
        });
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chooseVideo();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }

    private void chooseVideo() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select videos"), 222);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setType("video/*");//video/mp4
            startActivityForResult(intent, 222);//SELECT_VIDEO_KITKAT
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 222:
                if (resultCode == RESULT_OK) {
                    videoPathList.clear();
                    videoPathList = getSelectedVideos(requestCode, data);
                    Log.e("videoList ", String.valueOf(videoPathList.size()));
                    videoFileName = videoPathList.get(0);
                    Log.e("Path",""+videoFileName);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(videoFileName);
                    Log.e("Name",""+fileName);
                    tvSelectedPath.setText("Video: "+fileName);
                }
                break;

        }

    }

    private ArrayList<String> getSelectedVideos(int requestCode, Intent data) {
        videoUriList.clear();
        ArrayList<String> result = new ArrayList<>();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                videoUriList.add(videoURI);
                String filePath = VideoChooser.getPath(getActivity(), videoURI);
                Log.e("clipPath", "" + filePath);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            // Log.e("vsize",""+videoURI.getPath());

            videoUriList.add(videoURI);
            String filePath = VideoChooser.getPath(getActivity(), videoURI);
            //  String filePath = FilePathUtils.getPathFromUri(getActivity(),videoURI);
            Path path = Paths.get(String.valueOf(videoURI));
            Log.e("pathStrng", "" + path.toString());
            result.add(filePath);
            Log.e("fPath", "" + filePath);
        }

        return result;
    }

    private void addTopicVideo() {
        Intent serviceIntent = new Intent(getContext(), UploadFileService.class);
        VideoUploadUri videoUriModel = new VideoUploadUri(videoUriList);
        // videoUriModel.setVideoList(videoUriList);
        serviceIntent.putExtra("receiver", resultReceiver);
        serviceIntent.putExtra("videos", videoUriModel);
        serviceIntent.putExtra("from", SIMPLEE_HOME_TUTOR);//from
        serviceIntent.putExtra("chapter_id", "");
        serviceIntent.putExtra("title", video_title);
        serviceIntent.putExtra("fileName", videoFileName);
        Log.e("selPath: ", "" + videoFileName);
        // if (from.equals(SIMPLEE_HOME_TUTOR)) {
        serviceIntent.putExtra("course_id", course_id);
        serviceIntent.putExtra("type", "demo");
        serviceIntent.putExtra("section_id", "");

        //  }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(serviceIntent);
        } else {
            getActivity().startService(serviceIntent);
        }

        CommonMethods.showSuccessToast(getContext(), "Video uploading started");//in background. Make sure you have better internet speed so that you can't loss your data.


    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar("Live Demo Classes", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_upload:
                Log.e("videotype", "video click");
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    //for android 11 and above
                    if (CommonMethods.checkStoragePermission(getActivity())){
                        showUploadDialog();
                    }else{
                        CommonMethods.requestStoragePermission(getActivity(),STORAGE_READ_WRIT_REQUEST_CODE,activityResultLauncher);
                    }
                } else {
                    //for android 10 or bellow 10
                    if (CommonMethods.checkReadPermission(getContext())) {
                        Log.e("permissionChecked", "trye");
                        showUploadDialog();

                    } else
                        CommonMethods.requestPermission(getActivity(), 123);
                }
//                if (CommonMethods.checkReadPermission(getContext()))
//                    showUploadDialog();
//                else
//                    CommonMethods.requestPermission(getActivity(), 245);
                break;
        }
    }

    //------------api call------------------------------//
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case GET_LIVE_CLASS_DEMO:
                //  params.put("tutor_id",new Session_management(getActivity()).getString(USER_ID));
                params.put("course_id", course_id);
                params.put("type", "all");//all or today will be used
                callApi(GET_LIVE_CLASS_DEMO, params);
                break;
            case ApiCode.DELETE_LIVE_CLASS:
                params.put("liveclass_id", id);
                callApi(ApiCode.DELETE_LIVE_CLASS, params);
                break;
            case ApiCode.DELETE_VIDEO:
                params.put("video_id", id);
                callApi(ApiCode.DELETE_VIDEO, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case GET_LIVE_CLASS_DEMO:
                service.postDataVolley(GET_LIVE_CLASS_DEMO,
                        BaseUrl.URL_GET_LIVE_CLASS_DEMO, params);
                Log.e("url", BaseUrl.URL_GET_LIVE_CLASS_DEMO);
                Log.e("params", "" + params);
                break;
            case ApiCode.DELETE_LIVE_CLASS:
                service.postDataVolley(ApiCode.DELETE_LIVE_CLASS,
                        BaseUrl.URL_DELETE_LIVE_CLASS, params);
                Log.e("url", BaseUrl.URL_DELETE_LIVE_CLASS);
                Log.e("params", "" + params);
                break;
            case ApiCode.DELETE_VIDEO:
                service.postDataVolley(ApiCode.DELETE_VIDEO,
                        BaseUrl.URL_DELETE_VIDEO, params);
                Log.e("url", BaseUrl.URL_DELETE_VIDEO);
                Log.e("params", "" + params);
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case GET_LIVE_CLASS_DEMO:
                Log.e("demoLive", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray videoArray = jsonObject.getJSONArray("demo_video");
                    setVideos(videoArray);
                    JSONArray liveArray = jsonObject.getJSONArray("live_data");
                    setLiveData(liveArray);
                } else {
                    liveList.clear();
                    setLiveClasses();
                    demoVideoList.clear();
                    setDemoVideo();
                }

                break;
            case ApiCode.DELETE_LIVE_CLASS:
                if (jsonObject.getBoolean("status")) {
                    id = "";
                    CommonMethods.showSuccessToast(getContext(), "Live Deleted Successfully");
                    sendApiRequest();

                }
                break;
            case ApiCode.DELETE_VIDEO:
                Log.e("delete", response);
                if (jsonObject.getBoolean("status")) {
                    id = "";
                    CommonMethods.showSuccessToast(getContext(), "Video Deleted successfully");
                    sendApiRequest();

                }
                break;
        }
    }

    private void setLiveData(JSONArray liveArray) {
        if (liveArray.length() > 0) {
            ArrayList<T_LiveModel> psearch = new Gson().
                    fromJson(liveArray.toString(),
                            new TypeToken<List<T_LiveModel>>() {
                            }.getType());
            liveList.clear();
            liveList.addAll(psearch);
            setLiveClasses();
        } else {
            liveList.clear();
            setLiveClasses();
        }
    }

    private void setVideos(JSONArray videoArray) {
        if (videoArray.length() > 0) {
            ArrayList<Video_Model> psearch = new Gson().
                    fromJson(videoArray.toString(),
                            new TypeToken<List<Video_Model>>() {
                            }.getType());
            demoVideoList.clear();
            demoVideoList.addAll(psearch);
            setDemoVideo();
        } else {
            demoVideoList.clear();
            setDemoVideo();
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }

    //-------------------------------//

    //--------------video upload result receiver-------------------//

    private class FileUploadResultReceiver extends ResultReceiver {

        public FileUploadResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultReceiver != null) {
                if (resultCode == FILE_UPLOAD_COMPLETE) {
                    Log.e("fUpload", "success");
                    sendApiRequest();
                    // callGetVideoApi();
                }

            }
        }
    }
    //----------------------------------------/
}
package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import static soonflyy.learning.hub.Common.BaseUrl.URL_GET_CHAPTER_VIDEO;
import static soonflyy.learning.hub.Common.BaseUrl.URL_SCHOOL_GET_VIDEO;
import static soonflyy.learning.hub.Common.Constant.FILE_UPLOAD_COMPLETE;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common.VideoChooser;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.LiveVedioAdapter;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.Scl_SelectChpterDeatailFragment;
import soonflyy.learning.hub.model.VideoUploadUri;
import soonflyy.learning.hub.services.UploadFileService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class MyTeacherVideoFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
RecyclerView rec_video;
    private FloatingActionButton feb_video;
    SwipeRefreshLayout swipe;
    CardView cv_upload;
    RelativeLayout rel_no_live,rel_showlist;
    LiveVedioAdapter adapter;
    private ArrayList<Uri>videoUriList=new ArrayList<>();
    ArrayList<Video_Model> modellist=new ArrayList<>();
    private ArrayList<String> videoPathList=new ArrayList<>();
    TextView tv_add_video,tvSelectedPath;
    String course_id,chapter_id,section_id,video_id,video_title,from;
    String teacher_id,class_id,subject_id;
    String pageTitle;
    ImageView iv_videoIcon;
    String videoFileName;
    ActivityResultLauncher<Intent> activityResultLauncher;




    private FileUploadResultReceiver resultReceiver;

    public MyTeacherVideoFragment() {
        // Required empty public constructor
    }
    public static MyTeacherVideoFragment newInstance(String param1, String param2) {
        MyTeacherVideoFragment fragment = new MyTeacherVideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_teacher_video, container, false);
        initView(view);
        getArgumentData();
        senApiRequest();
        init_swipe_method();
      //  initRecyclerView();
        tv_add_video.setOnClickListener(this);
        feb_video.setOnClickListener(this);
        cv_upload.setOnClickListener(this);

        resultReceiver=new FileUploadResultReceiver(new Handler(Looper.getMainLooper()));
        createActivityLauncher();

        return  view;
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

    private void getArgumentData() {
        from=getArguments().getString("from");
        if (from.equals(SIMPLEE_HOME_TUTOR)) {// pre if (from.equals(SIMPLEE_HOME_STUDENT)) {
            course_id = getArguments().getString("course_id");
            chapter_id = getArguments().getString("chapter_id");
            section_id = getArguments().getString("section_id");
            pageTitle = getArguments().getString("course_title");
            CommonMethods.changeImageViewTintColor(iv_videoIcon,R.color.white,R.color.red);
        }else if (from.equals(SCHOOL_TUTOR)|| from.equals(INDEPENDENT_TUTOR)){
            chapter_id = getArguments().getString("chapter_id");
            section_id = getArguments().getString("section_id");
            teacher_id = getArguments().getString("teacher_id");
            subject_id = getArguments().getString("subject_id");

        }
    }

    private void initView(View view) {
        iv_videoIcon=view.findViewById(R.id.img_live);
        cv_upload=view.findViewById(R.id.card_create_course);
        swipe=view.findViewById(R.id.swipe);
        rec_video =view.findViewById(R.id.rec_notes);
        feb_video = view.findViewById(R.id.feb_video);
        tv_add_video=view.findViewById(R.id.tv_discription);
        rel_no_live=view.findViewById(R.id.rel_no_live);
        rel_showlist=view.findViewById(R.id.rel_showlist);
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                senApiRequest();
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange,R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void senApiRequest() {
        if (ConnectivityReceiver.isConnected()){
            //call api
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_CHAPTER_VIDEO);
            }else{
                sendRequest(ApiCode.SCHOOL_GET_VIDEO);
            }

        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void initRecyclerView() {

        rec_video.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rec_video.setKeepScreenOn(true);
        adapter = new LiveVedioAdapter(getContext(), modellist, new LiveVedioAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {

            }


            @Override
            public void onDelete(int position) {
                video_id=modellist.get(position).getId();
                showDeleteDialog();

            }
        });

    if (modellist.size()==0){
        rel_no_live.setVisibility(View.VISIBLE);
        rel_showlist.setVisibility(View.GONE);

    }
    else {
        rel_no_live.setVisibility(View.GONE);
        rel_showlist.setVisibility(View.VISIBLE);
        rec_video.setAdapter(adapter);
        adapter.notifyDataSetChanged();
     }


    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setTitle("Delete")
                .setMessage("Are you sure to delete?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ConnectivityReceiver.isConnected()){
                    if (from.equals(SIMPLEE_HOME_TUTOR)){
                        sendRequest(ApiCode.DELETE_VIDEO);
                    }else{
                        sendRequest(ApiCode.SCHOOL_DELETE_VIDEO);
                    }

                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
                dialog.dismiss();
            }
        }).show();
    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }


    private void showNotesList() {

        Dialog dialog = new Dialog (getActivity());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.dailoge_livevideo);
        dialog.getWindow ();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
        dialog.show ( );

        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        ImageView iv_sub = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_back);
        Button btn_upload =dialog.findViewById(R.id.btn_upload);
        tvSelectedPath=dialog.findViewById(R.id.tv_seleted_path);

        dialog.show();
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_title=et_name.getText().toString().trim();
                if (TextUtils.isEmpty(video_title)){
                    et_name.setError("Enter title");
                    et_name.requestFocus();
                }else if (videoUriList.size()==0){
                        CommonMethods.showSuccessToast(getContext(),"Choose video file");
                }else{
                    //upload task
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        addTopicVideo();
                        dialog.dismiss();
                    }
                }

            }
        });
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("type", "video click");
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    //for android 11 and above
                    if (CommonMethods.checkStoragePermission(getActivity())){
                        chooseVideo();
                    }else{
                        CommonMethods.requestStoragePermission(getActivity(),STORAGE_READ_WRIT_REQUEST_CODE,activityResultLauncher);
                    }
                } else {
                    //for android 10 or bellow 10
                    if (CommonMethods.checkReadPermission(getContext())) {
                        Log.e("permissionChecked", "trye");
                        chooseVideo();

                    } else
                        CommonMethods.requestPermission(getActivity(), 123);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside (false);
    }

    private void chooseVideo() {

       /* new MaterialFilePicker()
                .withActivity(getActivity())
//                    .withFilter(Pattern.compile(".*"))
                .withRequestCode(222)
                .start();

        */

    if (SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select videos"),222);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setType("video/*");//video/mp4
        Log.e("chooseVideo","here...");
            startActivityForResult(intent, 222);//SELECT_VIDEO_KITKAT
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case 222:
                if ( resultCode==RESULT_OK) {
                    videoPathList.clear();
                    videoPathList =  getSelectedVideos(requestCode, data);
                    Log.e("videoList ",String.valueOf(videoPathList.size()));
                    videoFileName=videoPathList.get(0);
                    Log.e("Path",""+videoFileName);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(videoFileName);
                    Log.e("Name",""+fileName);
                    tvSelectedPath.setText("Video: "+fileName);

//                   if (data !=null) {
//                       videoFileName=data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//                       DynamicToast.make(getActivity(), "" + videoPathList.size() + " videos are selected for upload", Toast.LENGTH_SHORT).show();
//                   }
                }
                break;

        }

    }
    private ArrayList<String> getSelectedVideos(int requestCode, Intent data) {
        videoUriList.clear();
        ArrayList<String> result = new ArrayList<>();
        ClipData clipData = data.getClipData();
        if(clipData != null) {
            for(int i=0;i<clipData.getItemCount();i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                videoUriList.add(videoURI);
                String filePath = VideoChooser.getPath(getActivity(), videoURI);
                Log.e("clipPath",""+filePath);
                result.add(filePath);
            }
        }
        else {
            Uri videoURI = data.getData();
           // Log.e("vsize",""+videoURI.getPath());

            videoUriList.add(videoURI);
            String filePath = VideoChooser.getPath(getActivity(), videoURI);
          //  String filePath = FilePathUtils.getPathFromUri(getActivity(),videoURI);
            Path path= Paths.get(String.valueOf(videoURI));
           Log.e("pathStrng",""+path.toString());
                result.add(filePath);
            Log.e("fPath",""+filePath);
        }

        return result;
    }
    private  String getPathFromUri(Uri uri){
        File file = new File(uri.getPath());//create path from uri
        final String[] split = file.getPath().split(":");//split the path.
        return split[1];
    }
    private void addTopicVideo(){
        Intent serviceIntent=new Intent(getContext(), UploadFileService.class);
        VideoUploadUri videoUriModel=new VideoUploadUri(videoUriList);
        // videoUriModel.setVideoList(videoUriList);
        serviceIntent.putExtra("receiver", resultReceiver);
        serviceIntent.putExtra("videos",videoUriModel);
        serviceIntent.putExtra("from",from);
        serviceIntent.putExtra("chapter_id", chapter_id);
        serviceIntent.putExtra("title", video_title);
        serviceIntent.putExtra("fileName",videoFileName);
        Log.e("selPath: ",""+videoFileName);
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            serviceIntent.putExtra("course_id", course_id);
            serviceIntent.putExtra("type", "chapter");
            serviceIntent.putExtra("section_id", section_id);

        }else if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR)){
            serviceIntent.putExtra("teacher_id", teacher_id);
            serviceIntent.putExtra("subject_id", subject_id);
        }

        // serviceIntent.putParcelableArrayListExtra("videos",uriList);
        // serviceIntent.putExtra("inputExtra","abb");
     //   ContextCompat.startForegroundService(getContext(),serviceIntent);

        if (SDK_INT>=Build.VERSION_CODES.O){
          getActivity().startForegroundService(serviceIntent);
        }else{
            getActivity().startService(serviceIntent);
        }

        CommonMethods.showSuccessToast(getContext(),"Video uploading started");//in background. Make sure you have better internet speed so that you can't loss your data.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_discription:
            case R.id.card_create_course:
            case R.id.feb_video:
                showNotesList();
                break;
        }
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_CHAPTER_VIDEO:

                params.put("chapter_id", chapter_id);
                params.put("section_id", section_id);
                //  params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_CHAPTER_VIDEO, params);
                break;
            case ApiCode.SCHOOL_GET_VIDEO:
                params.put("chapter_id", chapter_id);
                params.put("subject_id", subject_id);
                //  params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.SCHOOL_GET_VIDEO, params);
                break;
            case ApiCode.DELETE_VIDEO:
                params.put("video_id",video_id);
                callApi(ApiCode.DELETE_VIDEO, params);
                break;
            case ApiCode.SCHOOL_DELETE_VIDEO:
                params.put("video_id",video_id);
                callApi(ApiCode.SCHOOL_DELETE_VIDEO, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.DELETE_VIDEO:
                service.postDataVolley(ApiCode.DELETE_VIDEO,
                        BaseUrl.URL_DELETE_VIDEO, params);
                break;
            case ApiCode.SCHOOL_DELETE_VIDEO:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_VIDEO,
                        BaseUrl.URL_SCHOOL_DELETE_VIDEO, params);
                break;
            case ApiCode.UPLOAD_COURSE_VIDEO:
                service.postDataVolley(ApiCode.CREATE_NOTICE,
                        BaseUrl.URL_CREATE_NOTICE, params);
                break;
            case ApiCode.GET_CHAPTER_VIDEO:
                service.postDataVolley(ApiCode.GET_CHAPTER_VIDEO,
                        URL_GET_CHAPTER_VIDEO, params);
                Log.e("params",""+params);
                Log.e("api ",""+URL_GET_CHAPTER_VIDEO);
                break;
            case ApiCode.SCHOOL_GET_VIDEO:
                service.postDataVolley(ApiCode.SCHOOL_GET_VIDEO,
                        URL_SCHOOL_GET_VIDEO, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
//
            case ApiCode.DELETE_VIDEO:
            case ApiCode.SCHOOL_DELETE_VIDEO:
                Log.e("delete",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Video Deleted successfully");
                    senApiRequest();
                }
                break;
            case ApiCode.GET_CHAPTER_VIDEO:
            case ApiCode. SCHOOL_GET_VIDEO:
                Log.e("video ", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Video_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<Video_Model>>() {
                                        }.getType());
                        modellist.clear();
                        modellist.addAll(psearch);
                        initRecyclerView();
                        //video_adapter.notifyDataSetChanged();
                    } else {
                        modellist.clear();
                        initRecyclerView();
                        // video_adapter.notifyDataSetChanged();
                      //  CommonMethods.showSuccessToast(getContext(), "Video not available");
                    }
                }else{
                    modellist.clear();
                    initRecyclerView();
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);
            ((T_ChapterDetailFragment) getParentFragment()).showVideoBg();
            ((T_ChapterDetailFragment) getParentFragment()).showAssignProfile();
        }else{
            ((Scl_SelectChpterDeatailFragment) getParentFragment()).showVideoBg();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        //resultReceiver=null;
    }

    private  class FileUploadResultReceiver extends ResultReceiver {

        public FileUploadResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultReceiver!=null) {
                if (resultCode == FILE_UPLOAD_COMPLETE) {
                    Log.e("fUpload", "success");
                   // senApiRequest();
                    callGetVideoApi();
                }

            }
        }
    }

    private void callGetVideoApi() {
        swipe.setRefreshing(true);
        String getUrl=null;
        HashMap<String, String> params = new HashMap<>();
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            getUrl=URL_GET_CHAPTER_VIDEO;
            params.put("chapter_id", chapter_id);
            params.put("section_id", section_id);
        }else{
            getUrl=URL_SCHOOL_GET_VIDEO;
            params.put("chapter_id", chapter_id);
            params.put("subject_id", subject_id);
        }

        if (getUrl!=null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("uploadVideo ", response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                ArrayList<Video_Model> psearch = new Gson().
                                        fromJson(jsonArray.toString(),
                                                new TypeToken<ArrayList<Video_Model>>() {
                                                }.getType());
                                modellist.clear();
                                modellist.addAll(psearch);
                                initRecyclerView();
                                //video_adapter.notifyDataSetChanged();
                            } else {
                                modellist.clear();
                                initRecyclerView();
                                // video_adapter.notifyDataSetChanged();
                                //  CommonMethods.showSuccessToast(getContext(), "Video not available");
                            }
                        }else{
                            modellist.clear();
                            initRecyclerView();
                        }
                        swipe.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        swipe.setRefreshing(false);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    swipe.setRefreshing(false);
                    CommonMethods.showSuccessToast(getContext(), error.getMessage());
                }
            });
        }


    }
}

package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.MessageAdpter;
import soonflyy.learning.hub.model.MessageModel;
import soonflyy.learning.hub.model.StudentAll;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    private static final int SELECT_PDF = 200;
    private static final int READ_PERMISSION_REQUEST = 301;
    private String LAST_MESSAGE="";
    RecyclerView rec_message;
    MessageAdpter adapter;
    ImageView iv_back, iv_send_msg, iv_document, iv_emoji;
    EditText et_message;
    CircleImageView profile_image;
    TextView tv_profile_text;
    TextView tv_title;

    private NestedScrollView nestedScrollView;
    // CardView actionBar_layout;

    String selecte_photo;

    String type;
    MessageModel Mmodel;
    ArrayList<MessageModel> mlist = new ArrayList<>();
    ArrayList<MessageModel> tempMsgList = new ArrayList<>();
    private ArrayList<Uri> pdfUriList = new ArrayList<>();
    private String message = "";

    String teacher_id;
    StudentAll studentModel;
    String to_id, from_id;
    ProgressBar progressBar,topProgressBar;


    private int page = 0, limit = 1;

    private long delay = 1000;//pre 1000
    private long period = 3000;//10000
    private Timer task;
    String from;

    ActivityResultLauncher<Intent>activityResultLauncher;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initview(view);
        createActivityLauncher();

        task = new Timer();

        type = getArguments().getString("type");
        from=getArguments().getString("from");
        if (type.equals("teacher")) {
            from_id = getArguments().getString("teacher_id");
            to_id = getArguments().getString("student_id");
            Log.e("tfrom",""+from_id);
            Log.e("ttoid",""+to_id);

        //    studentModel = getArguments().getParcelable("studentData");
           // to_id = studentModel.getUser_id();
        } else if (type.equals("student")) {
            from_id = getArguments().getString("student_id");
            to_id = getArguments().getString("teacher_id");
            Log.e("sfrom",""+from_id);
            Log.e("stoid",""+to_id);
            // from_id="4";
            // to_id="12";//for testing use static id of teacher
            // studentModel=getArguments().getParcelable("studentData");
        }

        if (type.equals("teacher")) {
            //tv_title.setText(studentModel.getFirst_name());
            tv_title.setText(getArguments().getString("name"));
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                iv_document.setVisibility(View.GONE);
                profile_image.setVisibility(View.VISIBLE);
                tv_profile_text.setVisibility(View.GONE);
                String profileUrl = BaseUrl.BASE_URL_MEDIA + getArguments().getString("profile_image");
                Log.e("studentImglink", profileUrl);//BaseUrl.BASE_URL_MEDIA+studentModel.getUser_image()
//            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + studentModel.getUser_image())
//                    .placeholder(R.drawable.profile)
                Picasso.get().load(profileUrl).placeholder(R.drawable.profile)
                        .into(profile_image);
                ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            }else{
                iv_document.setVisibility(View.VISIBLE);
                profile_image.setVisibility(View.GONE);
                tv_profile_text.setText(getArguments().getString("name").substring(0,1).toUpperCase(Locale.ROOT));
                tv_profile_text.setVisibility(View.VISIBLE);
                ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
            }
        }
        else {
            //for student
            tv_title.setText(getArguments().getString("name"));
            if (from.equals(SIMPLEE_HOME_STUDENT)) {
                iv_document.setVisibility(View.GONE);
                profile_image.setVisibility(View.VISIBLE);
                tv_profile_text.setVisibility(View.GONE);
                String profileUrl = BaseUrl.BASE_URL_MEDIA + getArguments().getString("profile_image");
                //write code for set image
                Log.e("teacherLink", "" + profileUrl);
                Picasso.get().load(profileUrl).placeholder(R.drawable.profile)
                        .into(profile_image);
                ((MainActivity) getActivity()).getSupportActionBar().hide();
            }else{
                profile_image.setVisibility(View.GONE);
                tv_profile_text.setText(getArguments().getString("name").substring(0,1).toUpperCase(Locale.ROOT));
                tv_profile_text.setVisibility(View.VISIBLE);
                iv_document.setVisibility(View.VISIBLE);
                ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
            }
        }
        if (ConnectivityReceiver.isConnected()) {
            //  sendRequest(ApiCode.CHAT_MESSAGE,null);
            callGetMessageApi("top");
        }
        sendSeenStatus();
        starGetMessageTask();
        //listener

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    if (page>0) {
                        page--;
                        Log.e("page", "" + page);
                        if (ConnectivityReceiver.isConnected()) {
//                            //sendRequest(ApiCode.CHAT_MESSAGE, null);

                            callGetMessageApi("bottom");
                        }
                    }

                }else if (scrollY==0){
                    Log.e("y","0");
                    if (page<limit-1) {
                        page++;
                        Log.e("page", "" + page);
                        // loadingPB.setVisibility(View.VISIBLE);
                        if (ConnectivityReceiver.isConnected()) {
//                            //sendRequest(ApiCode.CHAT_MESSAGE, null);
                            topProgressBar.setVisibility(View.VISIBLE);
                            callGetMessageApi("top");
                        }
                    }

                    // Log.e("page",""+page);
                }
            }
        });

//

        iv_back.setOnClickListener(this);
        iv_document.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_send_msg.setOnClickListener(this);

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

    private void sendSeenStatus() {
        String statusUrl=null;
        if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SIMPLEE_HOME_STUDENT)){
            statusUrl=BaseUrl.URL_SEND_STATUS;
        }else {
            //for school section
            statusUrl=BaseUrl.URL_SCHOOL_SEND_STATUS;
        }

        HashMap<String, String> params = new HashMap<>();
        if (type.equals("teacher")) {
            params.put("to", to_id);//student_id
            params.put("from", from_id);//teacher_id
           // params.put("type","from");
        }
        if (type.equals("student")) {
            params.put("from", from_id);//student_id
            params.put("to", to_id);//teacher_id
           // params.put("type","")
        }
        params.put("status", "1");


        Log.e ("seen_status", "sendSeenStatus: "+params);
        CommonMethods.postRequest(statusUrl, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("seen_status ", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonMethods.showSuccessToast(getContext(), error.getMessage());
            }
        });
    }


    private void getArgumentData() {
        teacher_id = getArguments().getString("teacher_id");
        studentModel = getArguments().getParcelable("studentData");
    }

    private void getMessage() {

        adapter = new MessageAdpter(getActivity(), mlist, from_id);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        rec_message.setLayoutManager(linearLayout);
       // rec_message.setHasFixedSize(true);
        rec_message.setAdapter(adapter);
//        nestedScrollView.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

       /* rec_message.addOnScrollListener(new RecyclerPaginationScrollListener(linearLayout) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
               // doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        */

//        rec_message.post(new Runnable() {
//            @Override
//            public void run() {
//                if (page==0) {
//                    Log.e("recbottom", "setbottom");
//
//                    rec_message.smoothScrollToPosition(adapter.getItemCount()-1);
//                }
//            }
//        });
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                if (page==0) {
                    //checkScroll();
                   // nestedScrollView.scrollTo(0, nestedScrollView.getBottom());
                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    //if (!TextUtils.isEmpty(et_message.getText().toString().trim())){
                        et_message.requestFocus();
                  //  }
                }
            }
        });
    }

    private void checkScroll() {
        if (TextUtils.isEmpty(LAST_MESSAGE)){
            if (mlist.size()>0){
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
                MessageModel model= mlist.get(mlist.size()-1);
                if (model.getMessage_type().equalsIgnoreCase("Text")) {
                    LAST_MESSAGE = model.getMessage_text();
                }else if (model.getMessage_type().equalsIgnoreCase("File")){
                    LAST_MESSAGE = model.getMessage_file();
                }
            }
        }else if (mlist.size()>0){
            String lastMsg="";
            MessageModel model= mlist.get(mlist.size()-1);
            if (model.getMessage_type().equalsIgnoreCase("Text")) {
                lastMsg = model.getMessage_text();
            }else if (model.getMessage_type().equalsIgnoreCase("File")){
                lastMsg = model.getMessage_file();
            }
            if (!lastMsg.equals(LAST_MESSAGE)) {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
            LAST_MESSAGE=lastMsg;
        }
    }

    private void initview(View view) {
       // msgRefreshProgress=view.findViewById(R.id.msg_refresh);
        topProgressBar=view.findViewById(R.id.top_progress);
        progressBar=view.findViewById(R.id.progressbar);
        tv_profile_text=view.findViewById(R.id.tv_profile_text);
        rec_message = view.findViewById(R.id.rec_message);
        mlist = new ArrayList<>();
        iv_back = view.findViewById(R.id.iv_back);

        iv_emoji = view.findViewById(R.id.iv_emoji);
        iv_send_msg = view.findViewById(R.id.iv_send_msg);
        iv_document = view.findViewById(R.id.iv_document);

        tv_title = view.findViewById(R.id.txtTitle);
        profile_image = view.findViewById(R.id.profile_image);
        et_message = view.findViewById(R.id.et_msg);
        nestedScrollView = view.findViewById(R.id.nested_scoll_msg);
        // actionBar_layout=view.findViewById(R.id.title_layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                    if (type.equals("teacher"))
                        ((TeacherMainActivity) getActivity()).onBackPressed();
                    else
                        ((MainActivity) getActivity()).onBackPressed();
                }else{
                    ((SchoolMainActivity)getActivity()).onBackPressed();
                }
                break;

            case R.id.iv_emoji:
                break;
            case R.id.iv_send_msg:
                message = et_message.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    et_message.setError("Enter message");
                    et_message.requestFocus();
                } else if (ConnectivityReceiver.isConnected()) {
                    // sendRequest(ApiCode.ADD_MESSAGE, "message");
                    callAddMessageApi("message");
                    et_message.getText().clear();
                }
                sendSeenStatus();

                break;
            case R.id.iv_document:
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    //for android 11 and above
                    if (CommonMethods.checkStoragePermission(getActivity())){
                        showDocumentDialogChooser();
                    }else{
                        CommonMethods.requestStoragePermission(getActivity(),STORAGE_READ_WRIT_REQUEST_CODE,activityResultLauncher);
                    }
                } else {
                    //for android 10 or bellow 10
                    if (CommonMethods.checkReadPermission(getContext())) {
                        showDocumentDialogChooser();
                    } else
                        CommonMethods.requestPermission(getActivity(), 123);
                }


//                if (CommonMethods.checkReadPermission(getActivity()))
//                    showDocumentDialogChooser();
//                else
//                    CommonMethods.requestPermission(getActivity(), READ_PERMISSION_REQUEST);
                break;
        }
    }

    private void showDocumentDialogChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose")
              //  .setSingleChoiceItems(new String[]{"Photo", "Document"}, -1, new DialogInterface.OnClickListener() {
                    .setSingleChoiceItems(new String[]{"Photo"}, -1, new DialogInterface.OnClickListener() {

                        @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //write code for image choser
                            chooseImage();
                            dialog.dismiss();
                        } else if (which == 1) {
                            //write code for file chooser
                            choosePdf();
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false)
                .show();
    }

    private void chooseImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    private void choosePdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("application/pdf");//*/*
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), SELECT_PDF);//intent
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {

                Uri imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);
                // Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(course_thumbnail_img);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                message = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                if (ConnectivityReceiver.isConnected()) {
                    //sendRequest(ApiCode.ADD_MESSAGE,"image");
                    callAddMessageApi("image");
                }
                // DynamicToast.make(getContext(), "Cover image selected for upload", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == SELECT_PDF && resultCode == RESULT_OK) {
            pdfUriList.clear();

            if (data != null) {
                if (data.getClipData() != null) {
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        Uri uri = data.getClipData().getItemAt(index).getUri();
                        pdfUriList.add(uri);

                    }
                } else {

                    Uri uri = data.getData();
                    pdfUriList.add(uri);
                    Log.d("fileUri: ", String.valueOf(uri));
                }

              /*  message=new Gson().toJson(CommonMethods.getBase64StringList(pdfUriList,getActivity()));
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.ADD_MESSAGE,"pdf");
                }

               */
            }

            Log.e("pdf_list ", String.valueOf(pdfUriList.size()));
        }


    }


    private void sendRequest(int request, String mtype) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.CHAT_MESSAGE:
                if (type.equals("teacher")) {
                    params.put("student_id", to_id);
                    params.put("teacher_id", from_id);
                }
                if (type.equals("student")) {
                    params.put("student_id", from_id);
                    params.put("teacher_id", to_id);
                }

                params.put("page", String.valueOf(page));
                callApi(ApiCode.CHAT_MESSAGE, params);
                break;
            case ApiCode.ADD_MESSAGE:
                params.put("from", from_id);//from to
                params.put("to", to_id);
                params.put(mtype, message);//message or image,for pdf doc add base64 with extention
                callApi(ApiCode.ADD_MESSAGE, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.CHAT_MESSAGE:
                service.postDataVolley(ApiCode.CHAT_MESSAGE,
                        BaseUrl.URL_CHAT_MESSAGE, params);
                break;
            case ApiCode.ADD_MESSAGE:
                service.postDataVolley(ApiCode.ADD_MESSAGE,
                        BaseUrl.URL_ADD_MESSAGE, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        //Log.e("message ",response);
        switch (requestType) {
            case ApiCode.CHAT_MESSAGE:
                Log.e("chat_message ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Mmodel =new MessageModel ();
                    ArrayList<MessageModel> list = new ArrayList<>();
                    if (jsonObject.getBoolean("status")) {
                        limit = Integer.parseInt(jsonObject.getString("total_page"));
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        //  for (int i = 0; i < jsonArray.length(); i++) {
                        list = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<MessageModel>>() {
                                        }.getType());
                        // mlist.clear();
                        //getMessage();
                        //adapter.notifyDataSetChanged();
                        //  }
                        String api_date= Mmodel.getCreated_at ( );
                        Log.e ("mmmmmm", "onResponse: "+ Mmodel.getCreated_at ( ));
                        String[] sep=api_date.split (" ");
                        String dates= sep[0];
                        Mmodel.setTemp_date (dates);
                       // mlist.clear();
                        mlist.add (Mmodel);
                        mlist.addAll(list);
                        getMessage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case ApiCode.ADD_MESSAGE:
                Log.e("add_message ", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("status")) {
                        sendRequest(ApiCode.CHAT_MESSAGE, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type.equals("teacher")) {
            //tv_title.setText(studentModel.getFirst_name());
            tv_title.setText(getArguments().getString("name"));
            if (from.equals(SIMPLEE_HOME_TUTOR)) {
                profile_image.setVisibility(View.VISIBLE);
                tv_profile_text.setVisibility(View.GONE);
                String profileUrl = BaseUrl.BASE_URL_MEDIA + getArguments().getString("profile_image");
                Log.e("studentImglink", "" + profileUrl);//BaseUrl.BASE_URL_MEDIA+studentModel.getUser_image()
//                Picasso.get().load(BaseUrl.BASE_URL_MEDIA + studentModel.getUser_image())
//                        .placeholder(R.drawable.profile)
                Picasso.get().load(profileUrl).placeholder(R.drawable.profile)
                        .into(profile_image);
                ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            }else{
                profile_image.setVisibility(View.GONE);
                tv_profile_text.setText(getArguments().getString("name").substring(0,1).toUpperCase(Locale.ROOT));
                tv_profile_text.setVisibility(View.VISIBLE);
                ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
                setKeyboardSoftInput();
            }
        }
        else {
            //for student
            tv_title.setText(getArguments().getString("name"));
            if (from.equals(SIMPLEE_HOME_STUDENT)) {
                profile_image.setVisibility(View.VISIBLE);
                tv_profile_text.setVisibility(View.GONE);
                String profileUrl = BaseUrl.BASE_URL_MEDIA + getArguments().getString("profile_image");
                //write code for set image
                Log.e("teacherLink", profileUrl);
                Picasso.get().load(profileUrl).placeholder(R.drawable.profile)
                        .into(profile_image);
                ((MainActivity) getActivity()).getSupportActionBar().hide();
            }else{
                profile_image.setVisibility(View.GONE);
                tv_profile_text.setText(getArguments().getString("name").substring(0,1).toUpperCase(Locale.ROOT));
                tv_profile_text.setVisibility(View.VISIBLE);
                ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
            }
        }
    }

    private void setKeyboardSoftInput() {

        ((SchoolMainActivity)getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            task.cancel();
           // task.
        }catch (Exception e){
            e.printStackTrace();
        }
        if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)){
            if (type.equals("teacher"))
                ((TeacherMainActivity) getActivity()).getSupportActionBar().show();
            else
                ((MainActivity) getActivity()).getSupportActionBar().show();
        }
    }

    private void callGetMessageApi(String type) {

       // msgRefreshProgress.setRefreshing(true);
        if (type.equals("top")){
            progressBar.setVisibility(View.GONE);
            topProgressBar.setVisibility(View.VISIBLE);
        }else{
            topProgressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }
        String getUrl=null;
        if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SIMPLEE_HOME_STUDENT)){
            getUrl=BaseUrl.URL_CHAT_MESSAGE;
        }else {
            //for school section
            getUrl=BaseUrl.URL_SCHOOL_CHAT_MESSAGE;
        }

        HashMap<String, String> params = new HashMap<>();
        if (type.equals("teacher")) {
            params.put("student_id", to_id);
            params.put("teacher_id", from_id);
        }
        if (type.equals("student")) {
            params.put("student_id", from_id);
            params.put("teacher_id", to_id);
        }
        params.put("page", String.valueOf(page));
        if (getUrl!=null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("chat_message ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ArrayList<MessageModel> list = new ArrayList<>();
                        if (jsonObject.getBoolean("status")) {
                            limit = Integer.parseInt(jsonObject.getString("total_page"));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            //  for (int i = 0; i < jsonArray.length(); i++) {
                            list = new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<MessageModel>>() {
                                            }.getType());
                            mlist.clear();
                            mlist.addAll(list);
                            if (mlist.size()<20 && page!=0 && page==limit-1){
                                mlist.addAll(tempMsgList);
                            }
                            getMessage();
                            if (page!=limit-1){
                                tempMsgList.clear();
                            tempMsgList.addAll(list);

                            }
//
                            //msgRefreshProgress.setRefreshing(false);

                        }
                        dismissProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //msgRefreshProgress.setRefreshing(false);
                        dismissProgress();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CommonMethods.showSuccessToast(getContext(), error.getMessage());
                    //msgRefreshProgress.setRefreshing(false);
                    dismissProgress();
                }
            });
        }


    }
    private void dismissProgress(){
        progressBar.setVisibility(View.GONE);
        topProgressBar.setVisibility(View.GONE);
    }
    private void callGetMessageTaskApi() {

        // msgRefreshProgress.setRefreshing(true);
        String getUrl=null;
        if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SIMPLEE_HOME_STUDENT)){
            getUrl=BaseUrl.URL_CHAT_MESSAGE;
        }else {
            //for school section
            getUrl=BaseUrl.URL_SCHOOL_CHAT_MESSAGE;
        }

        HashMap<String, String> params = new HashMap<>();
        if (type.equals("teacher")) {
            params.put("student_id", to_id);
            params.put("teacher_id", from_id);
        }
        if (type.equals("student")) {
            params.put("student_id", from_id);
            params.put("teacher_id", to_id);
        }
        params.put("page", String.valueOf(page));
        if (getUrl!=null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("chat_message ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ArrayList<MessageModel> list = new ArrayList<>();
                        if (jsonObject.getBoolean("status")) {
                            limit = Integer.parseInt(jsonObject.getString("total_page"));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            //  for (int i = 0; i < jsonArray.length(); i++) {
                            list = new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<MessageModel>>() {
                                            }.getType());
                            mlist.clear();
                            mlist.addAll(list);
                            if (mlist.size()<20 && page!=0 && page==limit-1){
                                mlist.addAll(tempMsgList);
                            }
                            getMessage();
                            if (page!=limit-1){
                                tempMsgList.clear();
                                tempMsgList.addAll(list);
                            }
//
                            //msgRefreshProgress.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //msgRefreshProgress.setRefreshing(false);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // CommonMethods.showSuccessToast(getContext(), error.getMessage());
                    //msgRefreshProgress.setRefreshing(false);
                }
            });
        }


    }

    private void callAddMessageApi(String mtype) {
        progressBar.setVisibility(View.VISIBLE);
        String sendurl=null;
        if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SIMPLEE_HOME_STUDENT)){
            sendurl=BaseUrl.URL_ADD_MESSAGE;
        }else {
            //for school section
            sendurl=BaseUrl.URL_SCHOOL_ADD_MESSAGE;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from_id);//from to
        params.put("to", to_id);
        params.put(mtype, message);
        if (sendurl!=null) {
            CommonMethods.postRequest(sendurl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("add_message ", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                           progressBar.setVisibility(View.GONE);
                           page=0;
                            callGetMessageApi("bottom");
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    CommonMethods.showSuccessToast(getContext(), error.getMessage());
                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
            CommonMethods.showSuccessToast(getContext(),"Something  Went Wrong");
        }


    }

    private void starGetMessageTask() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                task.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(getContext(),"method call",Toast.LENGTH_SHORT).show();
                                if (ConnectivityReceiver.isConnected()) {
                                    // callGetMessageApi();
                                    et_message.requestFocus();
                                    callGetMessageTaskApi();
                                }
                                else
                                    CommonMethods.showSuccessToast(getContext(), "No Internet Connection");

                            }
                        });

                    }
                }, delay, period);
            }
        });
    }



}
package soonflyy.learning.hub.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity.SCHOOL_ID_SECTION_USER_TYPE;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.Id_Message_Adapter;
import soonflyy.learning.hub.Common_Package.Models.Chat_Message;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.model.MessageModel;
import soonflyy.learning.hub.model.StudentAll;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class IDUserMessageFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    private static final int SELECT_PDF = 200;
    private static final int READ_PERMISSION_REQUEST = 301;

    private String LAST_MESSAGE="";

    RecyclerView rec_message;
    RelativeLayout rel_edit;
    //MessageAdpter adapter;
    ImageView iv_back, iv_send_msg, iv_document, iv_emoji, img_edit;
    EditText et_message;
    CircleImageView profile_image;
    CardView cv_edi;
    TextView dailog_delete, tv_block_status;
    TextView tv_title;
    ProgressBar progressBar,topProgressBar;
    private NestedScrollView nestedScrollView;
    // CardView actionBar_layout;

    String selecte_photo;

    //    String type;
    MessageModel Mmodel;
    //ArrayList<MessageModel> mlist = new ArrayList<>();
    ArrayList<Chat_Message> mlist = new ArrayList<>();
    ArrayList<Chat_Message> tempMsgList = new ArrayList<>();
    Id_Message_Adapter adapter;
    private ArrayList<Uri> pdfUriList = new ArrayList<>();
    private String message = "";

    String teacher_id;
    StudentAll studentModel;
    String to_id, from_id, profile_url, user_name, user_type, block_status = "0",blockById="";//

    String toType="",fromType="";

    private int page = 0, limit = 1;

    private long delay = 1000;
    private long period = 3000;
    private Timer task;

    boolean isKeyboardSowing=false;
    RelativeLayout rootView;

    public IDUserMessageFragment() {
        // Required empty public constructor
    }

    public static IDUserMessageFragment newInstance(String param1, String param2) {
        IDUserMessageFragment fragment = new IDUserMessageFragment();

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
        View view = inflater.inflate(R.layout.fragment_i_d_user_message, container, false);
        initview(view);
        getArgumentData();
        task = new Timer();

        if (ConnectivityReceiver.isConnected()) {
          //  sendRequest(ApiCode.UPDATE_SEEN_MESSAGE);
            //   sendRequest(ApiCode.GET_CHAT_MESSAGES);
            callGetMessageApi("top");

        }
        //getMessage();
       starGetMessageTask();


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //SCROLL_VALUE=1;
                Log.e("beforPageCount",""+page);
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (page > 0) {
                       // page--;
                        page=page-1;
                        Log.e("page", "" + page);
                        Log.e("afterDecrPgCount",""+page);
                        if (ConnectivityReceiver.isConnected()) {
//                            //sendRequest(ApiCode.CHAT_MESSAGE, null);
                            //   progressBar.setVisibility(View.VISIBLE);
                            callGetMessageApi("bottom");
                        }
                    }

                } else if (scrollY == 0) {

                    if (page < limit - 1) {
                        //page++;
                        page = page + 1;
                    }
                    Log.e("page", "" + page);
                    Log.e("afterIncrPgCount",""+page);
                    // loadingPB.setVisibility(View.VISIBLE);
                    if (ConnectivityReceiver.isConnected()) {
//                            //sendRequest(ApiCode.CHAT_MESSAGE, null);
                        // progressBar.setVisibility();
                        callGetMessageApi("top");
                    }

                    // Log.e("page",""+page);
                }
            }
        });


        iv_back.setOnClickListener(this);
        iv_document.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_send_msg.setOnClickListener(this);
        rel_edit.setOnClickListener(this);
        tv_block_status.setOnClickListener(this);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 100) { // Value should be less than keyboard's height
                   isKeyboardSowing=true;
                } else {
                    isKeyboardSowing=false;
                }
            }
        });

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }


    private void getArgumentData() {
        user_name = getArguments().getString("user_name");
        profile_url = getArguments().getString("profile_image");
        to_id = getArguments().getString("to_id");
        from_id = getArguments().getString("from_id");
        user_type = getArguments().getString("user_type");
        toType = getArguments().getString("toType");


        setActionBar();

//        teacher_id = getArguments().getString("teacher_id");
//        studentModel = getArguments().getParcelable("studentData");
    }

    private void setActionBar() {
        Picasso.get().load(profile_url).placeholder(R.drawable.profile).into(profile_image);
        tv_title.setText(user_name);
    }

    private void getMessage() {

//
        //adapter = new MessageAdpter(getActivity(), mlist, from_id);
        adapter = new Id_Message_Adapter(getActivity(), mlist, from_id);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        rec_message.setLayoutManager(linearLayout);
        rec_message.setHasFixedSize(true);
        rec_message.setAdapter(adapter);
        Log.e("mesglist: ", "" + mlist.size());

        if (mlist.size() == 0) {
            CommonMethods.showSuccessToast(getContext(), "No Messages Found");
        }

        Log.e("keyboardshowing",""+isKeyboardSowing);
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                if (page==0) {
                   // nestedScrollView.scrollTo(0, nestedScrollView.getBottom());
                    if (TextUtils.isEmpty(LAST_MESSAGE)){
                        if (mlist.size()>0){
                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                            LAST_MESSAGE=mlist.get(mlist.size()-1).getMessage();
                        }
                    }else if (mlist.size()>0){
                        String lastMsg=mlist.get(mlist.size()-1).getMessage();
                        if (!lastMsg.equals(LAST_MESSAGE)) {
                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                        }else if (isKeyboardSowing){
                           // nestedScrollView.fullScroll(View.FOCUS_DOWN);
                            et_message.requestFocus();
                        }
                            LAST_MESSAGE=lastMsg;
                    }
                        //nestedScrollView.fullScroll(View.FOCUS_DOWN);


                   // if (!TextUtils.isEmpty(et_message.getText().toString().trim())){
                       // et_message.requestFocus();
                    //}
                }
            }
        });


    }

    private void initview(View view) {
        rootView=view.findViewById(R.id.parent_view);
        progressBar = view.findViewById(R.id.progress);
        topProgressBar = view.findViewById(R.id.top_progress);
        rec_message = view.findViewById(R.id.rec_message);
        tv_block_status = view.findViewById(R.id.tv_block_status);
        //mlist = new ArrayList<>();
        iv_back = view.findViewById(R.id.iv_back);

        iv_emoji = view.findViewById(R.id.iv_emoji);
        iv_send_msg = view.findViewById(R.id.iv_send_msg);
        iv_document = view.findViewById(R.id.iv_document);

        tv_title = view.findViewById(R.id.txtTitle);
        profile_image = view.findViewById(R.id.profile_image);
        et_message = view.findViewById(R.id.et_msg);
        nestedScrollView = view.findViewById(R.id.nested_scoll_msg);
        rel_edit = view.findViewById(R.id.rel_edit);
        cv_edi = view.findViewById(R.id.cv_edi);
        dailog_delete = view.findViewById(R.id.dailog_delete);
        img_edit = view.findViewById(R.id.img_edit);

        et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                gotoBack();
                break;
            case R.id.iv_emoji:
                break;
            case R.id.iv_send_msg:
                sendMessage();
                break;
            case R.id.iv_document:

//                if (CommonMethods.checkReadPermission(getActivity()))
//                    showDocumentDialogChooser();
//                else
//                    CommonMethods.requestPermission(getActivity(), READ_PERMISSION_REQUEST);
                break;
            case R.id.rel_edit:
                if (block_status.equals("0")) {
                    BlockUserDailge();
                }
                break;
            case R.id.tv_block_status:
                ShowBlockDailoge();
                break;
        }
    }

    private void sendMessage() {
        message = et_message.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            et_message.setError("Write message here");
            et_message.requestFocus();
        } else {
            if (ConnectivityReceiver.isConnected()) {
                //sendRequest(ApiCode.SEND_MESSAGE);
                callAddMessageApi();
            }
        }
    }

    private void gotoBack() {
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).onBackPressed();
        }else if (user_type.equals("teacher")) {
            ((TeacherMainActivity) getActivity()).onBackPressed();
        } else {
            ((MainActivity) getActivity()).onBackPressed();
        }
    }

    private void BlockUserDailge() {

//        if (block_status.equals("0")){
//            dailog_delete.setText("Block");
//        }else{
//            dailog_delete.setText("Unblock");
//        }

        if (cv_edi.getVisibility() == View.VISIBLE) {
            cv_edi.setVisibility(View.GONE);
        } else {
            cv_edi.setVisibility(View.VISIBLE);

        }


        dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowBlockDailoge();
            }
        });
    }

    private void ShowBlockDailoge() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailoge_userblock);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_message = dialog.findViewById(R.id.tv_block_message);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_block);
        if (block_status.equals("0")) {
            tv_message.setText("You want to block this account");
            dialogButton.setText("Block");
        } else {
            tv_message.setText("You want to unblock this account");
            dialogButton.setText("Unblock");
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    if (block_status.equals("0")) {
                        sendRequest(ApiCode.BLOCK_USER);
                    } else {
                        sendRequest(ApiCode.UNBLOCK_USER);
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                }
                dialog.dismiss();
                cv_edi.setVisibility(View.GONE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cv_edi.setVisibility(View.GONE);
            }
        });
        dialog.show();

    }


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            task.cancel();
            // task.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
//            case ApiCode.GET_CHAT_MESSAGES:
//                params.put("to_id", to_id);
//                params.put("from_id", from_id);
//                params.put("page", "1");
//                callApi(ApiCode.GET_CHAT_MESSAGES, params);
//                break;

//            case ApiCode.SEND_MESSAGE:
//                params.put("to_id", to_id);
//                params.put("from_id", from_id);
//                params.put("message", message);
//                params.put("date", CommonMethods.getCurrentDate());
//                params.put("time", CommonMethods.getCurrentTime());
//                callApi(ApiCode.SEND_MESSAGE, params);
//                break;
            case ApiCode.UPDATE_SEEN_MESSAGE:
                params.put("to_id", to_id);
                params.put("from_id", from_id);
                params.put("is_seen", "1");
                setTypeParameter(params);
                callApi(ApiCode.UPDATE_SEEN_MESSAGE, params);
                break;
            case ApiCode.BLOCK_USER:
                params.put("to_id", to_id);
                params.put("from_id", from_id);
                setTypeParameter(params);
                callApi(ApiCode.BLOCK_USER, params);
                break;
            case ApiCode.UNBLOCK_USER:
                params.put("to_id", to_id);
                params.put("from_id", from_id);
                setTypeParameter(params);
                callApi(ApiCode.UNBLOCK_USER, params);
                break;
//
        }
    }

    private void setTypeParameter(HashMap<String, String> params) {
        if (user_type.equals(SCHOOL_COACHING)){
            if (SCHOOL_ID_SECTION_USER_TYPE.equals(SCHOOL_COACHING)){
                //params.put("type","0");
                params.put("from_type","0");
            }else{
                params.put("from_type","1");
            }

        }else {
            params.put("from_type","1");
        }
        params.put("to_type",toType);
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
//            case ApiCode.GET_CHAT_MESSAGES:
//                service.postDataVolley(ApiCode.GET_CHAT_MESSAGES,
//                        BaseUrl.URL_GET_CHAT_MESSAGES, params);
//                break;
//            case ApiCode.SEND_MESSAGE:
//                service.postDataVolley(ApiCode.SEND_MESSAGE,
//                        BaseUrl.URL_SEND_MESSAGE, params);
//                break;
            case ApiCode.UPDATE_SEEN_MESSAGE:
                service.postDataVolley(ApiCode.UPDATE_SEEN_MESSAGE,
                        BaseUrl.URL_UPDATE_SEEN_MESSAGE, params);
                break;
            case ApiCode.BLOCK_USER:
                service.postDataVolley(ApiCode.BLOCK_USER,
                        BaseUrl.URL_BLOCK_USER, params);
                break;
            case ApiCode.UNBLOCK_USER:
                service.postDataVolley(ApiCode.UNBLOCK_USER,
                        BaseUrl.URL_UNBLOCK_USER, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//
//            case ApiCode.GET_CHAT_MESSAGES:
//                Log.e("chat_message ", response);
//                if (jsonObject.getBoolean("status")) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("message");
//
//                    if (jsonArray.length() > 0) {
//                        ArrayList<Chat_Message> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new TypeToken<List<Chat_Message>>() {
//                                        }.getType());
//                        mlist.clear();
//                        mlist.addAll(psearch);
//                        getMessage();
//                    } else {
//                        mlist.clear();
//                        getMessage();
//                        // CommonMethods.showSuccessToast(getContext(),"No Users");
//                    }
//                } else {
//                    getMessage();
//                }
//                break;
//            case ApiCode.SEND_MESSAGE:
//                Log.e("send_msg ", response);
//                if (jsonObject.getBoolean("status")) {
//                    sendRequest(ApiCode.GET_CHAT_MESSAGES);
//                }
//                break;
            case ApiCode.UPDATE_SEEN_MESSAGE:
                Log.e("seen_update ", response);


                break;
            case ApiCode.BLOCK_USER:
                Log.e("block ", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Block Successfully");
                    block_status = "1";
                    tv_block_status.setVisibility(View.VISIBLE);
                    et_message.setEnabled(false);
                    iv_send_msg.setEnabled(false);

                }

                break;
            case ApiCode.UNBLOCK_USER:
                Log.e("unblock ", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Unblock Successfully");
                    block_status = "0";
                    tv_block_status.setVisibility(View.GONE);
                    et_message.setEnabled(true);
                    iv_send_msg.setEnabled(true);
                }
                break;
        }
    }

    private void callAddMessageApi() {
        progressBar.setVisibility(View.VISIBLE);
        et_message.getText().clear();
        String sendurl = BaseUrl.URL_SEND_MESSAGE;
        HashMap<String, String> params = new HashMap<>();
        params.put("to_id", to_id);
        params.put("from_id", from_id);
        params.put("message", message);
        params.put("date", CommonMethods.getCurrentDate());
        params.put("time", CommonMethods.getCurrentTime());
        setTypeParameter(params);
        if (sendurl != null) {

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
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    CommonMethods.showSuccessToast(getContext(), error.getMessage());
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            CommonMethods.showSuccessToast(getContext(), "Something  Went Wrong");
        }


    }

    private void callGetMessageApi(String type) {
        // progressBar.setVisibility(View.VISIBLE);
        if (type.equals("top")){
            progressBar.setVisibility(View.GONE);
            topProgressBar.setVisibility(View.VISIBLE);
        }else{
            topProgressBar.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }
        String getUrl = BaseUrl.URL_GET_CHAT_MESSAGES;
        HashMap<String, String> params = new HashMap<>();
        params.put("to_id", to_id);
        params.put("from_id", from_id);
        params.put("page", String.valueOf(page));
        setTypeParameter(params);
        if (getUrl != null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("chat_message ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ArrayList<MessageModel> list = new ArrayList<>();
                        if (jsonObject.getBoolean("status")) {
                            String blockBy=jsonObject.getString("blocked_by");
                            setBlockStatus(jsonObject.getString("blocked_to"),blockBy);//block_status

                            // limit = Integer.parseInt(jsonObject.getString("total_page"));
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            //  for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.length() > 0) {
                                ArrayList<Chat_Message> psearch = new Gson().
                                        fromJson(jsonArray.toString(),
                                                new TypeToken<List<Chat_Message>>() {
                                                }.getType());
                                mlist.clear();
                                mlist.addAll(psearch);
                                if (mlist.size()<20 && page!=0 && page==limit-1){
                                    mlist.addAll(tempMsgList);
                                }
//                                if (!checkSameMsg()) {
                                    getMessage();
//                                }
                                if (page!=limit-1){
                                    tempMsgList.clear();
                                    tempMsgList.addAll(psearch);
                                }
                                //progressBar.setVisibility(View.GONE);
                            } else {
                                mlist.clear();
                                tempMsgList.clear();
                                getMessage();
                                //progressBar.setVisibility(View.GONE);
                                // CommonMethods.showSuccessToast(getContext(),"No Users");
                            }
                        } else {
                            getMessage();
                            tempMsgList.clear();
                            //progressBar.setVisibility(View.GONE);
                        }
                        dismissProgress();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissProgress();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonMethods.showSuccessToast(getContext(), error.getMessage());
                   dismissProgress();
                }
            });
        }


    }

    private boolean checkSameMsg() {
        boolean result=false;
        if (TextUtils.isEmpty(LAST_MESSAGE)){
            if (mlist.size()>0){
                //nestedScrollView.fullScroll(View.FOCUS_DOWN);
                LAST_MESSAGE=mlist.get(mlist.size()-1).getMessage();
                result=false;
            }
        }else if (mlist.size()>0){
            String lastMsg=mlist.get(mlist.size()-1).getMessage();
            if (!lastMsg.equals(LAST_MESSAGE)) {
               result=false;
            }else{
                result=true;
            }
            LAST_MESSAGE=lastMsg;
        }
        return result;
    }

    private void dismissProgress(){
        progressBar.setVisibility(View.GONE);
        topProgressBar.setVisibility(View.GONE);
    }

    private void callGetMessageTaskApi() {
        // progressBar.setVisibility(View.VISIBLE);
        String getUrl = BaseUrl.URL_GET_CHAT_MESSAGES;
        HashMap<String, String> params = new HashMap<>();
        params.put("to_id", to_id);
        params.put("from_id", from_id);
        params.put("page", String.valueOf(page));
        //params.put("seen_status","1");
        setTypeParameter(params);
        if (getUrl != null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("chat_message ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ArrayList<MessageModel> list = new ArrayList<>();
                        if (jsonObject.getBoolean("status")) {
                             limit = Integer.parseInt(jsonObject.getString("total_page"));
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            //  for (int i = 0; i < jsonArray.length(); i++) {
                            String blockBy=jsonObject.getString("blocked_by");
                            setBlockStatus(jsonObject.getString("blocked_to"),blockBy);//block_status
                            if (jsonArray.length() > 0) {
                                ArrayList<Chat_Message> psearch = new Gson().
                                        fromJson(jsonArray.toString(),
                                                new TypeToken<List<Chat_Message>>() {
                                                }.getType());
                                mlist.clear();
                                mlist.addAll(psearch);
                                if (mlist.size()<20 && page!=0 && page==limit-1){
                                    mlist.addAll(tempMsgList);
                                }
                                getMessage();
                                if (page!=limit-1){
                                    tempMsgList.clear();
                                    tempMsgList.addAll(psearch);
                                }
                               // progressBar.setVisibility(View.GONE);
                            } else {
                                mlist.clear();
                                tempMsgList.clear();
                                getMessage();
                                //progressBar.setVisibility(View.GONE);
                                // CommonMethods.showSuccessToast(getContext(),"No Users");
                            }
                        } else {
                            getMessage();
                            tempMsgList.clear();
                            //progressBar.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //progressBar.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonMethods.showSuccessToast(getContext(), error.getMessage());
                    //progressBar.setVisibility(View.GONE);
                }
            });
        }


    }

    private void setBlockStatus(String bStatus,String blockBy) {
        block_status=blockBy;
        String blockToId=bStatus;
        blockById=blockBy;
        if (blockById.equals("1") && blockToId.equals("1")){
            // both block each other, so first priority is sender
            tv_block_status.setText("You blocked this account, Tap to unblock.");
            tv_block_status.setVisibility(View.VISIBLE);
            et_message.setEnabled(false);
            iv_send_msg.setEnabled(false);
        }else if (blockById.equals("1")){
            tv_block_status.setText("You blocked this account, Tap to unblock.");
           // tv_block_status.setVisibility(View.GONE);
            tv_block_status.setVisibility(View.VISIBLE);
            et_message.setEnabled(false);
            iv_send_msg.setEnabled(false);
            //receiver is block by sender
        }else if (blockToId.equals("1")){
            //sender is block by receiver
            tv_block_status.setText("You have blocked");
            tv_block_status.setVisibility(View.VISIBLE);
            et_message.setEnabled(false);
            iv_send_msg.setEnabled(false);

        }else {
            tv_block_status.setVisibility(View.GONE);
            et_message.setEnabled(true);
            iv_send_msg.setEnabled(true);
        }
        //-------------------------
       /* if (block_status.equals("0")){
            tv_block_status.setVisibility(View.GONE);
            et_message.setEnabled(true);
            iv_send_msg.setEnabled(true);
           // dailog_delete.setText("Block");
        }else{

            tv_block_status.setVisibility(View.VISIBLE);
            et_message.setEnabled(false);
            iv_send_msg.setEnabled(false);

        }

        */
        //----------------------

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


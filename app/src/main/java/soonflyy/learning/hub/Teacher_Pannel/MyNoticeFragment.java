package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Notice_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.Notice;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyNoticeFragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    RoundedImageView rvNoticeImg;
    ImageView ivCancel,ivChooseImg;
    RecyclerView rec_notice;
    RelativeLayout  rel_notice,rel_notice_layout,relShowImg;
    TextView tvHeaderMsg;
    EditText et_message;
    Button send_btn;
    CardView cv_recycler;
    LinearLayout lin_header;
    SwipeRefreshLayout refreshLayout;

    Notice_Adapter noticeAdapter;
    ArrayList<Notice> noticeArrayList = new ArrayList<>();
    String course_id,subject_id,subjectName;
    String message="",noticeType,imageString="",assignNoticeType="";

    //-----subject notice code-----------//
    View profileLayoutView;
    CircleImageView ivProfileImg;
    TextView tvProfileName;
    ImageView tvMobileNo;
    AssignProfile assignProfile;
    //----------------------//


    public MyNoticeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_notice, container, false);
        initView(view);

       getArgumentData();

        sendApiCall();
      recyclerview();
        initControl();
        refreshLayout.setOnRefreshListener(this);
        send_btn.setOnClickListener(this);
        ivChooseImg.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        return view;
    }

    private void getArgumentData() {

        noticeType=getArguments().getString("notice");
        if (noticeType.equals("course")) {
            course_id = getArguments().getString("course_id");
        }else if (noticeType.equals("tutor")){
            assignNoticeType=getArguments().getString("type");
        }else if (noticeType.equals("subject")){
            //for subject notice
            course_id=getArguments().getString("course_id");
            subject_id=getArguments().getString("subject_id");
            subjectName=getArguments().getString("title");
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
           // setProfileData(assignProfile);
            showAssignProfile();
        }
    }

    private void showMessageDialog(String msg) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_show_notice_msg);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_msg =dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void recyclerview() {
        rec_notice.setLayoutManager(new LinearLayoutManager(getContext()));
//        noticeArrayList.clear();
//        noticeArrayList.add(new Notice());
//        noticeArrayList.add(new Notice());
//        noticeArrayList.add(new Notice());
//        noticeArrayList.add(new Notice());
        if (noticeArrayList.size() > 0) {
           // rel_notice.setVisibility(View.VISIBLE);
            rel_notice_layout.setVisibility(View.GONE);
            lin_header.setVisibility(View.VISIBLE);
            cv_recycler.setVisibility(View.VISIBLE);
            rec_notice.setVisibility(View.VISIBLE);
            noticeAdapter = new Notice_Adapter(getContext(), noticeArrayList, new Notice_Adapter.OnNoticeMsgClickListener() {
                @Override
                public void onClick(int position) {
                    showMessageDialog(noticeArrayList.get(position).getMsg());
                }
            });
            rec_notice.setAdapter(noticeAdapter);
            noticeAdapter.notifyDataSetChanged();
        } else {
           // rel_notice.setVisibility(View.GONE);
            cv_recycler.setVisibility(View.GONE);
            lin_header.setVisibility(View.GONE);
            if (noticeType.equals("tutor")){
                tvHeaderMsg.setText("Create a notice for your tutor\n and keep them updated");
            }
            rel_notice_layout.setVisibility(View.VISIBLE);
            rec_notice.setVisibility(View.GONE);

        }
    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    private void SwitchFragment(Mycourse_deailFragment fragment) {
    }


    private void initView(View view) {
        //--------------------//

        tvProfileName=view.findViewById(R.id.assign_tv_name);
        tvMobileNo=view.findViewById(R.id.assign_tv_mobile);
        ivProfileImg=view.findViewById(R.id.assign_iv_profile_img);
        profileLayoutView=view.findViewById(R.id.include_assign_profile);
        //-------------------//


        relShowImg=view.findViewById(R.id.rel_show_image);
        rvNoticeImg=view.findViewById(R.id.iv_upload_img);
        ivChooseImg=view.findViewById(R.id.iv_choose);
        ivCancel=view.findViewById(R.id.iv_cancel);

        tvHeaderMsg=view.findViewById(R.id.textView17);
        rel_notice_layout=view.findViewById(R.id.rel_notice_layout);
        lin_header=view.findViewById(R.id.lin_header);
        cv_recycler=view.findViewById(R.id.cv_recycler);
        rec_notice = view.findViewById(R.id.rec_notice);
        rel_notice = view.findViewById(R.id.rel_create_notice);
        send_btn=view.findViewById(R.id.send_btn);
        et_message=view.findViewById(R.id.et_name);
        refreshLayout=view.findViewById(R.id.refresh_layout);
        //rel_rec_notice = view.findViewById(R.id.rel_rec_notice);
    }

    private void setProfileData(AssignProfile profile) {
        if (profile!=null) {
            String link = BaseUrl.BASE_URL_MEDIA + profile.getImage();
            Log.e("image", link);
            Picasso.get().load(link).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            tvProfileName.setText(profile.getName());
//            tvMobileNo.setText("+91-" + profile.getMobile());

        }
    }
    private void showAssignProfile(){
        SessionManagement sessionManagement=new SessionManagement(getActivity());
        String assignValue=sessionManagement.getString(ASSIGN);
        if (assignValue !=null){
            if (assignValue.equals(ASSIGN_BY)) {
                setProfileData(assignProfile);
                profileLayoutView.setVisibility(View.VISIBLE);
            }
            else
                profileLayoutView.setVisibility(View.GONE);
        }else{
            profileLayoutView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        // ((MainActivity)getActivity()).setTitle("Internet Of Things (IOT)");
//        ((MainActivity)getActivity()).setStudentChildActionBar("Internet Of Things (IOT)",false);
       if (noticeType.equals("course")) {
           ((Mycourse_deailFragment) getParentFragment()).setNoticeBgColor();
           ((Mycourse_deailFragment) getParentFragment()).showAssignProfile();
       }else if (noticeType.equals("tutor")){
           String title=getArguments().getString("title");
           ((TeacherMainActivity)getActivity()).setTeacherActionBar(title,false);
       }else if (noticeType.equals("subject")){
           ((TeacherMainActivity) getActivity()).setTeacherActionBar(subjectName, false);
       }
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_NOTICE:
               // params.put("type","1");
                params.put("course_id",course_id);
                //params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_NOTICE, params);
                break;
            case ApiCode.CREATE_NOTICE:
                params.put("course_id",course_id);
                params.put("msg",message);
                params.put("image",imageString);
                params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.CREATE_NOTICE, params);
                break;
            case ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R:
                params.put("type",assignNoticeType);
                params.put("tutor_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R, params);
                break;
            case ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R:
                params.put("type",assignNoticeType);
                params.put("image",imageString);
                params.put("text_link",message);
                params.put("tutor_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R, params);
                break;
            case ApiCode.SEND_SUBJECT_NOTICE:
                params.put("course_id",course_id);
                params.put("subject_id",course_id);
                params.put("image",imageString);
                params.put("msg",message);
                callApi(ApiCode.SEND_SUBJECT_NOTICE, params);
                break;
            case ApiCode.GET_SUBJECT_NOTICE:
                // params.put("type","1");
                params.put("course_id",course_id);
                params.put("subject_id",course_id);
                //params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_SUBJECT_NOTICE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_NOTICE:
                service.postDataVolley(ApiCode.GET_NOTICE,
                        BaseUrl.URL_GET_NOTICE, params);
                break;
            case ApiCode.CREATE_NOTICE:
                service.postDataVolley(ApiCode.CREATE_NOTICE,
                        BaseUrl.URL_CREATE_NOTICE, params);
                break;
            case ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R:
                service.postDataVolley(ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R,
                        BaseUrl.URL_SEND_NOTICE_FOR_ASSIGNED_TUT0R, params);
                Log.e("url:", BaseUrl.URL_SEND_NOTICE_FOR_ASSIGNED_TUT0R);
                Log.e("params:", "" + params);
                break;
            case ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R:
                service.postDataVolley(ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R,
                        BaseUrl.URL_GET_NOTICE_FOR_ASSIGNED_TUT0R, params);
                Log.e("url:", BaseUrl.URL_GET_NOTICE_FOR_ASSIGNED_TUT0R);
                Log.e("params:", "" + params);
                break;

            case ApiCode.SEND_SUBJECT_NOTICE:
                service.postDataVolley(ApiCode.SEND_SUBJECT_NOTICE,
                        BaseUrl.URL_SEND_SUBJECT_NOTICE, params);
                Log.e("url:", BaseUrl.URL_SEND_SUBJECT_NOTICE);
                Log.e("params:", "" + params);
                break;
            case ApiCode.GET_SUBJECT_NOTICE:
                service.postDataVolley(ApiCode.GET_SUBJECT_NOTICE,
                        BaseUrl.URL_GET_SUBJECT_NOTICE, params);
                Log.e("url:", BaseUrl.URL_GET_SUBJECT_NOTICE);
                Log.e("params:", "" + params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_NOTICE:
            case ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R:
            case ApiCode.GET_SUBJECT_NOTICE:
                Log.e("notice_list",response);
                if(jsonObject.getBoolean("status")){
                    JSONArray array=jsonObject.getJSONArray("data");
                    if(array.length()>0){
                        ArrayList<Notice> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<ArrayList<Notice>>() {
                                        }.getType());
                        noticeArrayList.clear();
                        noticeArrayList.addAll(psearch);
                        recyclerview();
                    }else{
                        noticeArrayList.clear();
                        recyclerview();
                    }
                }
                break;
            case ApiCode.CREATE_NOTICE:
                Log.e("notice",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Notice added successfully");
                    message="";
                    imageString="";
                    et_message.getText().clear();
                    relShowImg.setVisibility(View.GONE);
                    sendApiCall();
                }
                break;
//            case ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R:
//                Log.e("get_assign",response);
//                break;
            case ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R:
                Log.e("send_assign",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Notice added successfully");
                    message="";
                    imageString="";
                    et_message.getText().clear();
                    relShowImg.setVisibility(View.GONE);
                    sendApiCall();
                }
                break;
            case ApiCode.SEND_SUBJECT_NOTICE:
                Log.e("send_subject",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Notice added successfully");
                    message="";
                    imageString="";
                    et_message.getText().clear();
                    relShowImg.setVisibility(View.GONE);
                    sendApiCall();
                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiCall();
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()){
            if (noticeType.equals("course")) {
                sendRequest(ApiCode.GET_NOTICE);
            }else if (noticeType.equals("tutor")){
                //profile tutor notice call api
                sendRequest(ApiCode.GET_NOTICE_FOR_ASSIGNED_TUT0R);
            }else if (noticeType.equals("subject")){
                //profile tutor notice call api
                sendRequest(ApiCode.GET_SUBJECT_NOTICE);
            }
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_btn:
                message=et_message.getText().toString().trim();
                if (TextUtils.isEmpty(message)){
                    et_message.setError("Enter text/link");
                    et_message.requestFocus();
                } else{
                    if (ConnectivityReceiver.isConnected()){
                        if (noticeType.equals("course")) {
                            sendRequest(ApiCode.CREATE_NOTICE);
                        }else if (noticeType.equals("tutor")){
                            //call create notice for tutor
                            sendRequest(ApiCode.SEND_NOTICE_FOR_ASSIGNED_TUT0R);
                        }else if (noticeType.equals("subject")){
                            sendRequest(ApiCode.SEND_SUBJECT_NOTICE);
                        }
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }
                break;
            case R.id.iv_cancel:
                relShowImg.setVisibility(View.GONE);
                imageString="";
                break;
            case R.id.iv_choose:
                chooseProfileImage();
                break;

        }
    }

    private void chooseProfileImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode==RESULT_OK){
            try {
                Uri imgUri = data.getData();
                Glide.with(getActivity()).load(imgUri).into(rvNoticeImg);

                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                imageString= Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
                CommonMethods.showSuccessToast(getContext(),"Image Selected");
                relShowImg.setVisibility(View.VISIBLE);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
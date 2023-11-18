package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Download.DownloadService;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter.SchoolCoachingT_LeaveApproveAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingT_LeaveApproveModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SchoolTutorLeaveApprovedFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    RecyclerView rec_leave;
    TextView tv_student,tv_h_name;
    View view_student,view_tutor;
    LinearLayout lin_student,lin_teacher,lin_s_header,lin_t_header,lin_s_t_tutor;
    SchoolCoachingT_LeaveApproveAdapter adapter;
    ArrayList<SchoolCoachingT_LeaveApproveModel> list=new ArrayList<>();
    String from,school_id,type="0",itutor_id,leave_id,l_status;
    ActivityResultLauncher<Intent> activityResultLauncher;

    public SchoolTutorLeaveApprovedFragment() {
        // Required empty public constructor
    }


    public static SchoolTutorLeaveApprovedFragment newInstance(String param1, String param2) {
        SchoolTutorLeaveApprovedFragment fragment = new SchoolTutorLeaveApprovedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_school_leave_approved, container, false);
        initview(view);
        getArgumentData();
        callApiRequest();
       // init_RecyclerView();
        initControl();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //for android 11 and above
                        if (result.getResultCode() == RESULT_OK) {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager())
                                    CommonMethods.showSuccessToast(getActivity(), "Permission Granted");
                                    //  Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                                else
                                    CommonMethods.showSuccessToast(getActivity(), "Permission Denied");
                                //Toast.makeText(getActivity().getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
        return  view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING)) {
            school_id = getArguments().getString("school_id");
            manageViewData("0");
        }else  if (from.equals(INDEPENDENT_TUTOR)){
            tv_h_name.setText("Student");
            itutor_id = getArguments().getString("itutor_id");
            lin_s_t_tutor.setVisibility(View.GONE);
            type="1";
            manageViewData("1");
        }
    }


    private void initview(View view) {
        lin_s_t_tutor=view.findViewById(R.id.lin_tutor_student);
        view_student=view.findViewById(R.id.view_school);
        view_tutor=view.findViewById(R.id.view_tutor);
        lin_student=view.findViewById(R.id.lin_school);
        lin_teacher=view.findViewById(R.id.lin_tutor);
        tv_student=view.findViewById(R.id.tv_school);
        lin_s_header=view.findViewById(R.id.lin_s_table);
        lin_t_header=view.findViewById(R.id.lin_t_table);
        tv_h_name=view.findViewById(R.id.tv_h_name);

        rec_leave=view.findViewById (R.id.rec_leave);
        rec_leave.setLayoutManager (new LinearLayoutManager(getActivity ()));
        rec_leave.hasFixedSize();
        rec_leave.setHasFixedSize(true);

        tv_student.setText("Student");
        lin_teacher.setOnClickListener(this);
        lin_student.setOnClickListener(this);

        // row_leave_approve
    }
    private void init_RecyclerView()  {
//        list = new ArrayList<>();
//        list.add(new SchoolCoachingT_LeaveApproveModel());
//        list.add(new SchoolCoachingT_LeaveApproveModel());
//        list.add(new SchoolCoachingT_LeaveApproveModel());
//        list.add(new SchoolCoachingT_LeaveApproveModel());

        adapter= new SchoolCoachingT_LeaveApproveAdapter(getActivity(), type,from,list, new SchoolCoachingT_LeaveApproveAdapter.OnClickListener() {
            @Override
            public void onViewDownloadReason(int postion) {
                String url=BaseUrl.BASE_URL_MEDIA+list.get(postion).getReason_file();
                if (url!=null){

//                    String mimtyp = CommonMethods.getMimeType(getContext(), url);
//                    Log.e("mimetype", "" + mimtyp);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(url);
                    Log.e("Name",""+fileName);
                    //if (mimtyp == null) {
                        String   mimtyp = CommonMethods.getMimeTypeFromLastSegement(fileName);
//                    }
                    downloadFile(url,"."+mimtyp);
//                    downloadFile(url,".pdf");
                    if (mimtyp.equalsIgnoreCase("pdf")
                            ||mimtyp.equals("application/pdf")) {
                    viewPdfFile(url,false);
                    }else{
                        //show image
                        viewImageFile(url);
                    }
                }
            }

            @Override
            public void onTeacherAccept(int position) {
               // leave_id=list.get(position).getId();
                leave_id=list.get(position).getLeave_id();
                l_status="1";
                if (CommonMethods.checkInternetConnection(getActivity())) {

                    sendRequest(ApiCode.SCHOOL_APPROVE_LEAVE);
                }

            }

            @Override
            public void onTeacherCancel(int position) {
              //  leave_id=list.get(position).getId();
                leave_id=list.get(position).getLeave_id();
                l_status="2";
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    sendRequest(ApiCode.SCHOOL_APPROVE_LEAVE);
                }
            }

            @Override
            public void showMsgDetails(int position) {
                showMessageDialog(list.get(position));
            }
        });
        rec_leave.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }
    private void showMessageDialog(SchoolCoachingT_LeaveApproveModel model) {
        String msg=model.getReason_message();
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
        TextView tv_title =dialog.findViewById(R.id.tv_title);
        TextView tv_msg =dialog.findViewById(R.id.tv_msg);
        tv_title.setText("Reason");
        tv_msg.setText(msg);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_cancel);
        LinearLayout linAttachment=dialog.findViewById(R.id.lin_attachemnt);

        String fileType=model.getFiletype();
        if (TextUtils.isEmpty(fileType)) {
            linAttachment.setVisibility(View.GONE);
        }else{
            linAttachment.setVisibility(View.VISIBLE);
        }

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        linAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=BaseUrl.BASE_URL_MEDIA+model.getReason_file();
                if (fileType.equalsIgnoreCase("pdf")){
                    //for pdf

                    if (url!=null){
//                        String mimtyp = CommonMethods.getMimeType(getContext(), url);
//                        Log.e("mimetype", "" + mimtyp);
//                        String fileName=CommonMethods.getLastSegmentOfFilePath(url);
//                        Log.e("Name",""+fileName);
//                        if (mimtyp == null) {
//                            mimtyp = CommonMethods.getMimeTypeFromLastSegement(fileName);
//                        }
                        downloadFile(url,".pdf");
                        viewPdfFile(url,false);
                    }
                }else{
                    //for image
                    if (url!=null){
//                        String mimtyp = CommonMethods.getMimeType(getContext(), url);
//                        Log.e("mimetype", "" + mimtyp);
                        String fileName=CommonMethods.getLastSegmentOfFilePath(url);
                        Log.e("Name",""+fileName);
//                        if (mimtyp == null) {
                        String  mimtyp = CommonMethods.getMimeTypeFromLastSegement(fileName);
//                        }
                        downloadFile(url,"."+mimtyp);
                        viewImageFile(url);
                    }
                }
            }
        });


    }

    private void viewImageFile(String url) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_view_full_screen);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.setCancelable(false);
        dialog.show();
        PhotoView photoView =dialog.findViewById(R.id.photo_view);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_close_dialog);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Log.e("imagFile",""+url);
        Picasso.get().load(url).into(photoView);
    }

    private void viewPdfFile(String url,boolean enable) {
        //write code to view pd

        new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(),url,"Leave File",
                enable,"");
    }
    private void downloadFile(String url,String ext) {
        if (CommonMethods.checkStoragePermission(getContext())) {
            if (!TextUtils.isEmpty(url)) {
                String fileName="leave_"+System.currentTimeMillis()+ext;//".pdf";
                Log.e("url", "" + url);
                Intent intent = new Intent(getActivity(), DownloadService.class);
                intent.putExtra("url", url);
                intent.putExtra("fileType", "pdf");
                intent.putExtra("fileName", fileName);
                getActivity().startForegroundService(intent);
            }
        } else {
            CommonMethods.requestStoragePermission(getActivity(), STORAGE_READ_WRIT_REQUEST_CODE, activityResultLauncher);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //foe android 10 and below
        switch (requestCode) {
            case STORAGE_READ_WRIT_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readper = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeper = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readper && writeper) {
                        //  Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        CommonMethods.showSuccessToast(getActivity(), "Permission Granted");
                    } else {
                        CommonMethods.showSuccessToast(getActivity(), "Permission Denied");
                        // Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CommonMethods.showSuccessToast(getActivity(), "Permission needs for download video");
                    // Toast.makeText(getApplicationContext(), "You Denied Permissions", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_school:
                type = "0";
                view_tutor.setVisibility(View.GONE);
                view_student.setVisibility(View.VISIBLE);
                manageViewData(type);
                //callRefreshApi();
                break;
            case R.id.lin_tutor:
                type = "1";
                view_student.setVisibility(View.GONE);
                view_tutor.setVisibility(View.VISIBLE);
                manageViewData(type);
                //callRefreshApi();
                break;
        }
        list.clear();
        init_RecyclerView();

    }

    private void manageViewData(String type) {
        if (type.equals("0")){
            lin_t_header.setVisibility(View.GONE);
            lin_s_header.setVisibility(View.VISIBLE);

            //also call api and mange data here
        }else{
            lin_t_header.setVisibility(View.VISIBLE);
            lin_s_header.setVisibility(View.GONE);
            //also call api and mange data here
        }
        if (CommonMethods.checkInternetConnection(getContext())) {

            callApiRequest();
        }
    //    init_RecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle("Leave Approval");
    }


    private void callApiRequest(){
        if (from.equals(SCHOOL_COACHING)) {
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL);
            }
        } if (from.equals(INDEPENDENT_TUTOR)) {
            sendRequest(ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL:
                params.put("school_id", school_id);
                params.put("type", type);
                callApi(ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL, params);
                break;
            case ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR:
                    params.put("teacher_id", itutor_id);
                callApi(ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_APPROVE_LEAVE:
                params.put("leave_id", leave_id);
                params.put("status", l_status);
                callApi(ApiCode.SCHOOL_APPROVE_LEAVE, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL:
                service.postDataVolley(ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL,
                        BaseUrl.URL_SCHOOL_GET_LEAVE_FOR_SCHOOL, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LEAVE_FOR_SCHOOL);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_APPROVE_LEAVE:
                service.postDataVolley(ApiCode.SCHOOL_APPROVE_LEAVE,
                        BaseUrl.URL_SCHOOL_APPROVE_LEAVE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_APPROVE_LEAVE);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//            case ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL:
//                Log.e("sc_leave", response.toString());
//                if (jsonObject.getBoolean("response")) {
//                    JSONArray jsonArray=jsonObject.getJSONArray("classes");
//                    if (jsonArray.length()>0){
//                        ArrayList<AllClassesModel> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
//                                        }.getType());
//
//                        classList.clear();
//                        f_classList.clear();
//                        classList.add(new AllClassesModel());
//                        classList.addAll(psearch);
//                        f_classList.addAll(classList);
//                        class_adapter.notifyDataSetChanged();
//                        f_class_adapter.notifyDataSetChanged();
//
//                    }else{
//                        classList.clear();
//                        f_classList.clear();
//                        classList.add(new AllClassesModel());
//                        f_classList.addAll(classList);
//                        class_adapter.notifyDataSetChanged();
//                        f_class_adapter.notifyDataSetChanged();
//                    }
//                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
//
//                } else {
//                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
//                }
////
//                break;
            case ApiCode.SCHOOL_GET_LEAVE_FOR_SCHOOL:
            case ApiCode.SCHOOL_GET_LEAVE_FOR_INDEPENDENT_TUTOR:
                Log.e("it_tutor", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("tutor_data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolCoachingT_LeaveApproveModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolCoachingT_LeaveApproveModel>>() {
                                        }.getType());
                            list.clear();

                            list.addAll(psearch);
                            init_RecyclerView();

                    }else{
                        list.clear();
                          init_RecyclerView();

                    }
                } else {
                    list.clear();
                    init_RecyclerView();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_APPROVE_LEAVE:
                Log.e("leave_approve", response);
                CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                callApiRequest();
                break;



        }
    }

}
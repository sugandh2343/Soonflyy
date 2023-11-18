package soonflyy.learning.hub.Student_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Download.DownloadService;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Dpp_Model;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;


public class Dpp_Fragment extends Fragment implements VolleyResponseListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    TextView tv_topic_title,tv_tFileName,tv_tFileLastDate,tv_subStaus,
            tv_gradeStatus,tv_lastDae,tv_mark,tv_sFileName,tv_sFileDate,tv_noFileMsg;
    ImageView iv_download;
    LinearLayout lin_submit_file;
    SwipeRefreshLayout refreshLayout;

    CardView card_file_upload,card_tfile,card_sfile;
    Dpp_Model dppModel;
    String fileString;
    JSONObject submissionObject;
    String submission_status="0";
    String lastDate;
    ActivityResultLauncher<Intent> activityResultLauncher;

    public Dpp_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dpp_, container, false);
        bindId(view);
        getArgumentData();
        sendApiRequest();

        card_file_upload.setOnClickListener(this);
        card_sfile.setOnClickListener(this);
        card_tfile.setOnClickListener(this);
        iv_download.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);

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
        return view;
    }

    private void sendApiRequest() {
        if(ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_DPP_SUBMISSION);
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");

    }

    private void getArgumentData() {
        dppModel=getArguments().getParcelable("dpp");
    }

    private void bindId(View view) {
        refreshLayout=view.findViewById(R.id.swipe);
        tv_topic_title=view.findViewById(R.id.tv_dpp_title);
        tv_tFileName=view.findViewById(R.id.tv_tFile);
        tv_tFileLastDate=view.findViewById(R.id.tv_sub_date);
        iv_download=view.findViewById(R.id.iv_download);
        tv_subStaus=view.findViewById(R.id.tv_sub_status);
        tv_gradeStatus=view.findViewById(R.id.tv_grade_status);
        tv_lastDae=view.findViewById(R.id.tv_last_date);
        tv_mark=view.findViewById(R.id.tv_mark);
        tv_sFileName=view.findViewById(R.id.tv_student_file);
        tv_sFileDate=view.findViewById(R.id.tv_file_date);
        card_file_upload=view.findViewById(R.id.card_upload_file);
        tv_noFileMsg=view.findViewById(R.id.tv_no_upload);
        lin_submit_file=view.findViewById(R.id.lin_submit_file);
        card_tfile=view.findViewById(R.id.card_tFile);
        card_sfile=view.findViewById(R.id.card_sfile);
    }



    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_DPP_SUBMISSION:
                params.put("lesson_id", dppModel.getLesson_id());//chapter_id
                params.put("student_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("dpp_id", dppModel.getId());
                callApi(ApiCode.GET_DPP_SUBMISSION, params);
                break;
            case ApiCode.ADD_DPP_SUBMISSION:
               // params.put("course_id","12" );//course_id
                params.put("date",CommonMethods.getCurrentDateTime());
                Log.e("time",""+CommonMethods.getCurrentDateTime());
               params.put("section_id", dppModel.getSection_id() );//subject_id
                params.put("lesson_id", dppModel.getLesson_id());//chapter_id
                params.put("student_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("dpp_id", dppModel.getId());
                params.put("file",fileString);
                callApi(ApiCode.ADD_DPP_SUBMISSION, params);
                break;
        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_DPP_SUBMISSION:
                service.postDataVolley(ApiCode.GET_DPP_SUBMISSION,
                        BaseUrl.URL_GET_DPP_SUBMISSION, params);
                break;
            case ApiCode.ADD_DPP_SUBMISSION:
                service.postDataVolley(ApiCode.ADD_DPP_SUBMISSION,
                        BaseUrl.URL_ADD_DPP_SUBMISSION, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_DPP_SUBMISSION:
                Log.e("dpp_submission ",response);
                if (jsonObject.getBoolean("status")){
                    submissionObject=jsonObject.getJSONObject("data");
                    setDataToView(submissionObject);
                }

//                JSONObject jsonObject=new JSONObject(response);
//                if (jsonObject.getBoolean("status")){
//                    JSONArray jsonArray=jsonObject.getJSONArray("data");
//                    if (jsonArray.length()>0) {
//                        List<Live> psearch = new Gson().
//                                fromJson(jsonArray.toString(),
//                                        new TypeToken<List<Live>>() {
//                                        }.getType());
//                        liveList.clear();
//                        liveList.addAll(psearch);
//                        liveAdapter.notifyDataSetChanged();
//                    }else{
//                        liveList.clear();
//                        liveAdapter.notifyDataSetChanged();
//                        CommonMethods.showSuccessToast(getContext(),"Teacher is not live");
//                    }
//                }
                break;

            case ApiCode.ADD_DPP_SUBMISSION:
                Log.e("add_submission ",response);
               if (jsonObject.getBoolean("status")){
                   CommonMethods.showSuccessToast(getContext(),"File Uploaded Successfully");
                   sendApiRequest();
               }
                break;
        }
    }

    private void setDataToView(JSONObject data) throws JSONException {
        String file_name;
        String remark=data.getString("remark");
        submission_status=data.getString("submission_status");
        lastDate=data.getString("last_date");
        String convertedDate=CommonMethods.changeDateTimeFmt("dd-MMM-yyyy | hh:mm a","EEE, MMM dd, yyyy | hh:mm a",lastDate);
        String title=data.getString("topic");
        tv_topic_title.setText("Topic: "+title);
        if (title.length()>15)
            file_name=title.substring(0,16)+".pdf";
        else
            file_name=title+".pdf";
        tv_tFileName.setText(file_name);
        tv_tFileLastDate.setText(lastDate);
        if (submission_status.equals("0") && remark.equals("0")){
            //no file submitted
            tv_subStaus.setText("No attempt");
            tv_gradeStatus.setText("Not graded");
            tv_lastDae.setText(convertedDate);//lastDate
            tv_mark.setText("--");
            lin_submit_file.setVisibility(View.GONE);
            card_file_upload.setVisibility(View.VISIBLE);
            tv_noFileMsg.setVisibility(View.VISIBLE);


        }else if (submission_status.equals("1") && remark.equals("0")){
            //fiel submitted but teacher not view yet
            tv_subStaus.setText("Submitted");
            tv_gradeStatus.setText("Not graded");
            tv_lastDae.setText(convertedDate); //lastDate
            tv_mark.setText("--");
            tv_sFileName.setText(file_name);
            tv_sFileDate.setText(data.getString("date"));
            lin_submit_file.setVisibility(View.VISIBLE);
            card_file_upload.setVisibility(View.GONE);
            tv_noFileMsg.setVisibility(View.GONE);


        }else if (submission_status.equals("1") && remark.equals("1")){
            //file submitted and teacher assign marks
            tv_subStaus.setText("Submitted");
            String gradingStatus=data.getString("grading_status");
            if (gradingStatus.equals("1")){
                tv_gradeStatus.setText("Graded");
            }else if (gradingStatus.equals("0")){
                tv_gradeStatus.setText("Fail");
            }else{
                tv_gradeStatus.setText("Not Graded");
            }

            tv_lastDae.setText(convertedDate);//lastDate
            tv_mark.setText(data.getString("marks"));
            tv_sFileName.setText(file_name);
            tv_sFileDate.setText(data.getString("date"));
            lin_submit_file.setVisibility(View.VISIBLE);
            card_file_upload.setVisibility(View.GONE);
            tv_noFileMsg.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_download:
                try {
                    if (submissionObject!=null) {
                        downloadFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("techer_file"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.card_upload_file:

                if (submission_status.equals("0")){//also check last submissino date

                    try {
                        if (!CommonMethods.isDateExpired(lastDate,"dd-MMM-yyyy | hh:mm a"))
                        chooseFileSend();
                        else
                            CommonMethods.showSuccessToast(getActivity(),"Submission Date Expired");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    CommonMethods.showSuccessToast(getContext(),"Your response have already submitted");
                }

                break;
            case R.id.card_sfile:
                try {
                    if (submissionObject!=null) {

                        viewPdfFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("student_file"), false);
                    }
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.card_tFile:
                try {
                    if (submissionObject!=null) {
                        viewPdfFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("techer_file"), false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void viewPdfFile(String url,boolean enable) {
        //write code to view pd

        new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(),url,"PDF",
                enable,"");
    }
    private void downloadFile(String url) {
        if (CommonMethods.checkStoragePermission(getContext())) {
            if (!TextUtils.isEmpty(url)) {
                String pdfFileName="dpp_"+dppModel.getTopic().replace(" ","_").toLowerCase()+"_"+
                        dppModel.getId()+".pdf";
                Log.e("dppPdfName",""+pdfFileName);
                Log.e("url", "" + url);
                Intent intent = new Intent(getActivity(), DownloadService.class);
                intent.putExtra("url", url);
                intent.putExtra("fileName", pdfFileName);
                intent.putExtra("fileType", "pdf");
                getActivity().startForegroundService(intent);
            }
        } else {
            CommonMethods.requestStoragePermission(getActivity(), STORAGE_READ_WRIT_REQUEST_CODE, activityResultLauncher);
        }
    }

    private void chooseFileSend() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setType("application/pdf");//*/*
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 222);//intent

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 222:
                if (resultCode==RESULT_OK){
                   // pdfUriList.clear();
                    Uri uri=null;
                    if (data != null) {
                        if (data.getClipData() != null){
                            // Getting the length of data and logging up the logs using index
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                 uri = data.getClipData().getItemAt(index).getUri();
                                //pdfUriList.add(uri);
                                //Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );
                            }
                        }else{
                            // Getting the URI of the selected file and logging into logcat at debug level
                           uri = data.getData();
                           // pdfUriList.add(uri);
                            Log.d("fileUri: ", String.valueOf(uri));
                        }
                    }
                    CommonMethods.setFileName(getActivity(),uri,tv_sFileName);
                    fileString=CommonMethods.convertToBase64String(uri,getContext());
                    if (ConnectivityReceiver.isConnected())
                    sendRequest(ApiCode.ADD_DPP_SUBMISSION);
                    else
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                  //  DynamicToast.make(getActivity(), ""+pdfUriList.size()+"  files selected.", Toast.LENGTH_SHORT).show();
                    //Log.e("pdf_list ",String.valueOf(pdfUriList.size()));
                    //tv_selected_file.setText("Selected_file.pdf");
                }
                break;
        }
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }
}
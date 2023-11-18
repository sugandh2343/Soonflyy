package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DPPDeatailFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {

    EditText et_topic_name, et_mark, et_lastdate;
    SwipeRefreshLayout refreshLayout;
    TextView tv_update;

    TextView tv_status, tv_topic_title, tv_tFileName, tv_tFileLastDate, tv_subStaus,
            tv_gradeStatus, tv_lastDae, tv_mark, tv_sFileName, tv_sFileDate, tv_noFileMsg;
    ImageView iv_download, iv_s_download;
    LinearLayout lin_submit_file;

    CardView card_file_upload, card_tfile, card_sfile;
    Dpp_Model dppModel;
    String fileString;
    JSONObject submissionObject;
    String submission_status = "0";
    String lastDate;
    Spinner spiner_grade;
    ArrayList<String> spiner_gradeList;
    String course_id, section_id, chapter_id, dpp_id, title, student_id, gradeStatus, mark;
    String pageTitle;
    int totalMark = 0;

    ActivityResultLauncher<Intent> activityResultLauncher;

    public DPPDeatailFragment() {
        // Required empty public constructor
    }

    public static DPPDeatailFragment newInstance(String param1, String param2) {
        DPPDeatailFragment fragment = new DPPDeatailFragment();
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
        View view = inflater.inflate(R.layout.fragment_d_p_p_deatail, container, false);
        init_View(view);
        getArgumentsData();
        Spinner_grade();
        sendApiCall();
        card_file_upload.setOnClickListener(this);
        card_sfile.setOnClickListener(this);
        card_tfile.setOnClickListener(this);
        tv_status.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        tv_update.setOnClickListener(this);
        iv_s_download.setOnClickListener(this);


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

    private void getArgumentsData() {
        course_id = getArguments().getString("course_id");
        chapter_id = getArguments().getString("chapter_id");
        section_id = getArguments().getString("section_id");
        dpp_id = getArguments().getString("dpp_id");
        title = getArguments().getString("title");
        student_id = getArguments().getString("student_id");
        pageTitle = getArguments().getString("course_title");
    }

    private void Spinner_grade() {
        spiner_gradeList = new ArrayList<>();
        spiner_gradeList.add("");
        spiner_gradeList.add("Graded");
        spiner_gradeList.add("Fail");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, spiner_gradeList);
        adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        spiner_grade.setAdapter(adapter);
        spiner_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_status.setText(parent.getSelectedItem().toString());
                    if (position == 1) {
                        gradeStatus = "1";
                    } else if (position == 2) {
                        gradeStatus = "0";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void init_View(View view) {
        iv_s_download = view.findViewById(R.id.iv_s_download);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        tv_status = view.findViewById(R.id.tv_status);
        tv_update = view.findViewById(R.id.tv_update);
        et_topic_name = view.findViewById(R.id.et_topic_name);
        spiner_grade = view.findViewById(R.id.status_spinner);
        et_mark = view.findViewById(R.id.et_mark);
        et_lastdate = view.findViewById(R.id.et_lastdate);

        tv_topic_title = view.findViewById(R.id.tv_dpp_title);
        tv_tFileName = view.findViewById(R.id.tv_tFile);
        tv_tFileLastDate = view.findViewById(R.id.tv_sub_date);
        iv_download = view.findViewById(R.id.iv_download);
        tv_subStaus = view.findViewById(R.id.tv_sub_status);
        tv_gradeStatus = view.findViewById(R.id.tv_grade_status);
        tv_lastDae = view.findViewById(R.id.tv_last_date);
        tv_mark = view.findViewById(R.id.tv_mark);
        tv_sFileName = view.findViewById(R.id.tv_student_file);
        tv_sFileDate = view.findViewById(R.id.tv_file_date);
        card_file_upload = view.findViewById(R.id.card_upload_file);
        tv_noFileMsg = view.findViewById(R.id.tv_no_upload);
        lin_submit_file = view.findViewById(R.id.lin_submit_file);
        card_tfile = view.findViewById(R.id.card_tFile);
        card_sfile = view.findViewById(R.id.card_sfile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.card_sfile:

                try {
                    viewPdfFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.card_tFile:
                try {
                    viewPdfFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("techer_file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_s_download:

                try {
                    downloadFile(BaseUrl.BASE_URL_MEDIA + submissionObject.getString("file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_status:
                spiner_grade.performClick();
                break;
            case R.id.tv_update:
            case R.id.card_upload_file:
                if (submission_status.equals("1")) {
                    if (validate()) {
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.UPDATE_DPP_SUBMISSION);
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                        }
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), "File not submitted by student.");
                }
                break;
        }
    }

    private boolean validate() {
        mark = et_mark.getText().toString().trim();
        if (TextUtils.isEmpty(mark)) {
            et_mark.setError("Enter mark");
            et_mark.requestFocus();
            return false;
        }
        if (Integer.parseInt(mark) == 0 || Integer.parseInt(mark) > totalMark) {
            et_mark.setError("Mark should be less than or equal to total mark (" + totalMark + ")");
            et_mark.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(gradeStatus)) {
            CommonMethods.showSuccessToast(getContext(), "Choose Grading Status");
            return false;
        }
        return true;
    }

    private void downloadFile(String url) {
        if (CommonMethods.checkStoragePermission(getContext())) {
            if (!TextUtils.isEmpty(url)) {
                String pdfFileName="dpp_"+tv_subStaus.getText().toString().replace(" ","_").toLowerCase()+"_"+
                        et_topic_name.getText().toString().replace(" ","_").toLowerCase()+"_"+student_id+".pdf";
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

    private void viewPdfFile(String url) {
        //write code to view pdf
        new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(), url, "File",
                false, null);

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
        switch (requestCode) {
            case 222:
                if (resultCode == RESULT_OK) {
                    // pdfUriList.clear();
                    Uri uri = null;
                    if (data != null) {
                        if (data.getClipData() != null) {
                            // Getting the length of data and logging up the logs using index
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                uri = data.getClipData().getItemAt(index).getUri();
                                //pdfUriList.add(uri);
                                //Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );
                            }
                        } else {
                            // Getting the URI of the selected file and logging into logcat at debug level
                            uri = data.getData();
                            // pdfUriList.add(uri);
                            Log.d("fileUri: ", String.valueOf(uri));
                        }
                    }
                    fileString = CommonMethods.convertToBase64String(uri, getContext());
                    if (ConnectivityReceiver.isConnected())
                        sendRequest(ApiCode.ADD_DPP_SUBMISSION);
                    else
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                    //  DynamicToast.make(getActivity(), ""+pdfUriList.size()+"  files selected.", Toast.LENGTH_SHORT).show();
                    //Log.e("pdf_list ",String.valueOf(pdfUriList.size()));
                    //tv_selected_file.setText("Selected_file.pdf");
                }
                break;
        }
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_DPP_SUBMISSION_TEACHER:
                params.put("dpp_id", dpp_id);
                params.put("lesson_id", chapter_id);
                params.put("student_id", student_id);
                callApi(ApiCode.GET_DPP_SUBMISSION_TEACHER, params);
                break;
            case ApiCode.UPDATE_DPP_SUBMISSION:
                params.put("dpp_id", dpp_id);
                params.put("lesson_id", chapter_id);
                params.put("student_id", student_id);
                params.put("section_id", section_id);
                params.put("marks", mark);
                params.put("grading_status", gradeStatus);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.UPDATE_DPP_SUBMISSION, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_DPP_SUBMISSION_TEACHER:
                service.postDataVolley(ApiCode.GET_DPP_SUBMISSION_TEACHER,
                        BaseUrl.URL_GET_DPP_SUBMISSION_TEACHER, params);
                break;
            case ApiCode.UPDATE_DPP_SUBMISSION:
                service.postDataVolley(ApiCode.UPDATE_DPP_SUBMISSION,
                        BaseUrl.URL_UPDATE_DPP_SUBMISSION, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.UPDATE_DPP_SUBMISSION:
                Log.e("update_sub", response);
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getContext(), "Updated Successfully");
                    gradeStatus = "";
                    mark = "";
                    sendApiCall();
                } else {
                    CommonMethods.showSuccessToast(getContext(), "Something went wrong");
                }
                break;


            case ApiCode.GET_DPP_SUBMISSION_TEACHER:
                Log.e("get_Dpp_sub", response);
                if (jsonObject.getBoolean("status")) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    setDatatoView(object);

                }
                break;
        }
    }

    private void setDatatoView(JSONObject object) {
        try {
            submissionObject = object;

            et_topic_name.setText(object.getString("topic"));
            et_lastdate.setText(object.getString("last_date"));
            //    et_mark.setHint(object.getString("total_marks"));

            submission_status = object.getString("submission_status");
            totalMark = Integer.parseInt(object.getString("total_marks"));

            if (submission_status.equals("0")) {
                lin_submit_file.setVisibility(View.GONE);
            } else {
                tv_sFileName.setText(et_topic_name.getText().toString() + ".pdf");
                tv_sFileDate.setText(object.getString("date"));//last_date
                lin_submit_file.setVisibility(View.VISIBLE);
            }
            String remark = object.getString("remark");
            tv_subStaus.setText(object.getString("name"));
            tv_lastDae.setText(object.getString("last_date"));
            tv_gradeStatus.setText("Not Graded");
            tv_mark.setText("-");

            if (remark.equals("1")) {
                et_mark.setText(object.getString("total_marks"));
                tv_mark.setText(object.getString("marks"));
                gradeStatus = object.getString("grading_status");
                et_mark.setEnabled(false);
                tv_status.setEnabled(false);
                card_file_upload.setVisibility(View.GONE);
                if (gradeStatus.equals("0")) {
                    tv_status.setText("Fail");
                    tv_gradeStatus.setText("Fail");
                } else {
                    tv_status.setText("Graded");
                    tv_gradeStatus.setText("Graded");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiCall();
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_DPP_SUBMISSION_TEACHER);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);

        //((T_LiveClassDetailFragment)getParentFragment()).setDppBgColor();
    }
}
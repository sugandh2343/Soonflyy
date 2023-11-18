package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolApplyForLeaveAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolApplyForLeaveModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.FileHelper;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolApplyForLeaveFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    Button btn_submit;
    EditText et_leave_reason;
    LinearLayout lin_msg, lin_file,linPath;
    TextView tvSelectedPath;
    ImageView iv_upload_file,ivCancelFile;
    RelativeLayout relTop;

    RecyclerView rec_leave;
    SwipeRefreshLayout swipe;
    SchoolApplyForLeaveAdapter leaveAdapter;
    ArrayList<SchoolApplyForLeaveModel> list = new ArrayList<>();
    String school_id, school_name, from, id, student_type, pdfFileString = "";

    String docType="";
    public SchoolApplyForLeaveFragment() {
        // Required empty public constructor
    }


    public static SchoolApplyForLeaveFragment newInstance(String param1, String param2) {
        SchoolApplyForLeaveFragment fragment = new SchoolApplyForLeaveFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_select_apply_for_leave, container, false);
        initView(view);
        getArgumentData();
        callRefreshApi();

        // initRecyclerview();
        init_swipe_method();


        return view;
    }

    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            if (from.equals(SCHOOL_STUDENT)) {
                sendRequest(ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT);
            } else {
                sendRequest(ApiCode.SCHOOL_GET_LEAVE);
            }
        }
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        if (from.equals(SCHOOL_TUTOR)) {
            school_id = getArguments().getString("school_id");
            school_name = getArguments().getString("school_name");
            lin_file.setVisibility(View.VISIBLE);
            lin_msg.setVisibility(View.VISIBLE);
        } else if (from.equals(SCHOOL_STUDENT)) {
            id = getArguments().getString("id");
            student_type = getArguments().getString("student_type");
            lin_file.setVisibility(View.VISIBLE);
            lin_msg.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        linPath=view.findViewById(R.id.lin_path);
        ivCancelFile=view.findViewById(R.id.iv_cancle_doc);
        tvSelectedPath=view.findViewById(R.id.tv_seleted_path);
        relTop = view.findViewById(R.id.rel_top);
        btn_submit = view.findViewById(R.id.btn_save);
        lin_file = view.findViewById(R.id.lin_file);
        lin_msg = view.findViewById(R.id.lin_msg);
        et_leave_reason = view.findViewById(R.id.et_name);
        swipe = view.findViewById(R.id.swipe);
        rec_leave = view.findViewById(R.id.rec_leave);
        iv_upload_file = view.findViewById(R.id.iv_upload_file);
        rec_leave.hasFixedSize();
        rec_leave.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_leave.setLayoutManager(layoutManager);
        rec_leave.setKeepScreenOn(true);

        btn_submit.setOnClickListener(this);
        iv_upload_file.setOnClickListener(this);
        ivCancelFile.setOnClickListener(this);


    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callRefreshApi();
                //  initRecyclerview();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).setActionBarTitle("Apply for Leave");
    }

    private void initRecyclerview() {

//        list = new ArrayList<>();
//        list.add(new SchoolApplyForLeaveModel("01", "Polynomials"));
//        list.add(new SchoolApplyForLeaveModel("-02", "Quadratic Equations"));
//        list.add(new SchoolApplyForLeaveModel("03", "Arithmetic Progressions"));
//        list.add(new SchoolApplyForLeaveModel("-04", "Coordinate Geometry"));


        leaveAdapter = new SchoolApplyForLeaveAdapter(getContext(), list, new SchoolApplyForLeaveAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
//                TeacherTestFragment fragment = new TeacherTestFragment ();
//                SwitchFragment (fragment);
            }


            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });

        rec_leave.setAdapter(leaveAdapter);
        leaveAdapter.notifyDataSetChanged();
        if (leaveAdapter.getItemCount() > 0) {
            relTop.setVisibility(View.VISIBLE);
        } else {
            relTop.setVisibility(View.GONE);
        }


    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                sendLeaveRequest();
                break;
            case R.id.iv_upload_file:
                if (CommonMethods.checkReadPermission(getActivity())) {
                    choosePdf();
                } else {
                    CommonMethods.requestPermission(getActivity(), 999);
                }

                break;
            case R.id.iv_cancle_doc:
                pdfFileString="";
                docType="";
                tvSelectedPath.setText("");
                linPath.setVisibility(View.GONE);
                break;
        }
    }

    private void choosePdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        //intent.setType("application/pdf");//*/*
        intent.setType("*/*");
        String[] mimetypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), 222);//intent
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 222:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    pdfFileString = CommonMethods.convertToBase64String(uri, getContext());
                    String path = FileHelper.getRealPathFromURI(getActivity(),uri);
                    Log.e("pdfPath",""+path);
                    String mimtyp = CommonMethods.getMimeType(getContext(), path);
                    Log.e("mimetype", "" + mimtyp);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(path);
                    Log.e("Name",""+fileName);
                    tvSelectedPath.setText("File: "+fileName);
                    linPath.setVisibility(View.VISIBLE);
                    CommonMethods.showSuccessToast(getContext(), "File Selected");
                    if (mimtyp == null) {
                        mimtyp = CommonMethods.getMimeTypeFromLastSegement(fileName);
                    }
                    if (mimtyp != null && mimtyp.length() > 0) {
                        if (mimtyp.equalsIgnoreCase("application/pdf")
                                || mimtyp.equalsIgnoreCase("pdf")) {
                            docType = "pdf";


                        } else {
                            docType = "image";


                        }

                    }

                }
                break;
        }

    }

    private void sendLeaveRequest() {
        if (from.equals(SCHOOL_TUTOR)) {
            if (TextUtils.isEmpty(et_leave_reason.getText().toString().trim())) {
                et_leave_reason.setError("Enter reason here");
                et_leave_reason.requestFocus();
                //CommonMethods.showSuccessToast(getContext(), "Please enter message or select file for leave reason");
            } else {
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    //call api for leave Request;
                    sendRequest(ApiCode.SCHOOL_LEAVE_APPLY);
                }
            }
        } else if (from.equals(SCHOOL_STUDENT)) {
            if (TextUtils.isEmpty(pdfFileString)) {
                CommonMethods.showSuccessToast(getContext(), "Please select file");
            } else {
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    sendRequest(ApiCode.SCHOOL_LEAVE_APPLY);
                }
            }
        }
    }


    //-----api call and responses----//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT:
                params.put("student_id", new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                if (student_type.equals("school")) {
                    params.put("type", "0");
                    // params.put("teacher_id",new Session_management(getActivity()).getString(SCHOOL_TEACHER_ID));
                } else if (student_type.equals("itutor")) {
                    params.put("type", "1");
                    //params.put("student_id", new Session_management(getActivity()).getString(SCHOOL_STUDENT_ID));
                }
                callApi(ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_GET_LEAVE:
                if (from.equals(SCHOOL_TUTOR)) {
                    params.put("type", "1");
                    params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
                } else if (from.equals(SCHOOL_STUDENT)) {
                    params.put("type", "0");
                    params.put("student_id", new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                }
                callApi(ApiCode.SCHOOL_GET_LEAVE, params);
                break;
            case ApiCode.SCHOOL_LEAVE_APPLY:
                if (from.equals(SCHOOL_TUTOR)) {
                    params.put("type", "0");
                    params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
                    params.put("school_id", school_id);
                    params.put("reason_message", et_leave_reason.getText().toString().trim());
                    params.put("filetype",docType);
                    params.put("reason_file", pdfFileString);

                } else if (from.equals(SCHOOL_STUDENT)) {

                    params.put("student_id", new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));
                    params.put("reason_file", pdfFileString);
                    params.put("filetype",docType);
                    if (student_type.equals("school")) {
                        //for_schoo_student
                        params.put("type", "2");
                        params.put("school_id", id);
                        //  params.put("reason_file", "base64");
                    } else if (student_type.equals("itutor")) {
                        //for independent tutor student
                        params.put("type", "1");
                        params.put("teacher_id", id);
                        // params.put("reason_file", "base64");
                    }

                }
                params.put("date_time", CommonMethods.getCurrentTime("dd-MM-yyyy | hh:mm a"));
                callApi(ApiCode.SCHOOL_LEAVE_APPLY, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_LEAVE:
                service.postDataVolley(ApiCode.SCHOOL_GET_LEAVE,
                        BaseUrl.URL_SCHOOL_GET_LEAVE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LEAVE);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_LEAVE_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LEAVE_BY_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_LEAVE_APPLY:
                service.postDataVolley(ApiCode.SCHOOL_LEAVE_APPLY,
                        BaseUrl.URL_SCHOOL_LEAVE_APPLY, params);
                Log.e("api", BaseUrl.URL_SCHOOL_LEAVE_APPLY);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_LEAVE:
            case ApiCode.SCHOOL_GET_LEAVE_BY_STUDENT:
                Log.e("sc_leave", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("tutor_data");
                    if (jsonArray.length() > 0) {
                        ArrayList<SchoolApplyForLeaveModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolApplyForLeaveModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                    } else {
                        list.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    list.clear();
                    initRecyclerview();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
            case ApiCode.SCHOOL_LEAVE_APPLY:
                Log.e("sc_leave_aply", response);
                if (jsonObject.getBoolean("response")) {
                    callRefreshApi();
                    if (from.equals(SCHOOL_TUTOR)) {
                        et_leave_reason.getText().clear();
                        tvSelectedPath.setText("");
                        pdfFileString="";
                        docType="";
                        linPath.setVisibility(View.GONE);
                    }else{
                        tvSelectedPath.setText("");
                        pdfFileString="";
                        docType="";
                        linPath.setVisibility(View.GONE);
                    }
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));
                break;

        }
    }

    ///--------------------------------------------------//
}
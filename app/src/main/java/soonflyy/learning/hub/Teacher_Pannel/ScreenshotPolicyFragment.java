package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.BaseUrl.URL_UPDATE_SCREENSHOT_POLICY;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.ScreenshotPoliyAdapter;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import soonflyy.learning.hub.model.ScreenshotPoliyModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScreenshotPolicyFragment extends Fragment implements VolleyResponseListener {
    SwipeRefreshLayout swipe;
    RecyclerView rec_screenshot;
    ScreenshotPoliyAdapter policyadapter;
    ArrayList<ScreenshotPoliyModel> modellist=new ArrayList<>();

    String user_id,enable_value;

    TextView tvCourseSpinner;
    Spinner courseSpinner;

    ArrayList<MyCourseDetailModel> clist = new ArrayList<>();
    ArrayAdapter<MyCourseDetailModel> courseAdapter;
    SessionManagement management;
    String courseId="";
    SwitchCompat btnEnableAll;
    boolean isSelectedAll=false;
    ArrayList<String>userIdList=new ArrayList<>();
    ArrayList<String>allIdList=new ArrayList<>();
    ArrayList<Student>studentIdList=new ArrayList<>();

    public ScreenshotPolicyFragment() {
        // Required empty public constructor
    }

    public static ScreenshotPolicyFragment newInstance(String param1, String param2) {
        ScreenshotPolicyFragment fragment = new ScreenshotPolicyFragment();
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
        View view = inflater.inflate(R.layout.fragment_screenshot_policy, container, false);
       management=new SessionManagement(getActivity());
        init_view(view);
        init_swipe_method();
        sendApiCall();
        return view;
    }

    private void init_view(View view) {
        btnEnableAll=view.findViewById(R.id.policy_all_swich);
        courseSpinner = view.findViewById(R.id.course_spinner);
        tvCourseSpinner = view.findViewById(R.id.tv_course_name);
        swipe = view.findViewById(R.id.swipe);
        rec_screenshot = view.findViewById(R.id.rec_screenshot);

        tvCourseSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseSpinner.performClick();
            }
        });
        setCourseSpinner();
        btnEnableAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (!isSelectedAll){
                    if (btnEnableAll.isChecked()){
                    isSelectedAll=true;
                        showConfirmation("all","1",btnEnableAll);
                    //showConfirmation("all","Are you sure to enable screenshot policy for all student?",btnEnableAll);
                }else{
                    isSelectedAll=false;
                        showConfirmation("all","0",btnEnableAll);
                    //showConfirmation("all","Are you sure to disable screenshot policy for all student?",btnEnableAll);
                }

            }
        });
    }

    private void setEnableDisableAll() {
        if (isSelectedAll){
            Log.e("isChecked","true");
         //   btnEnableAll.setChecked(true);
            enable_value="1";
            updateList(true);
        }else {
            Log.e("isChecked","false");
            //btnEnableAll.setChecked(false);
            enable_value="0";
            updateList(false);
        }
        setSwitchColor();
    }

    private  void showConfirmation(String type,String value,SwitchCompat switchBtn){
        String message="";
        if (value.equals("0")){
            if (type.equals("all")){
                message="Are you sure to disable screenshot policy for all student?";
            }else{
                message="Are you sure to disable screenshot policy?";
            }

        }else{
            if (type.equals("all")){
                message="Are you sure to enable screenshot policy for all student?";
            }else{
                message="Are you sure to enable screenshot policy?";
            }
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type.equalsIgnoreCase("all")){
                            //update all user at time
                            setEnableDisableAll();
                            userIdList.clear();
                            userIdList.addAll(allIdList);
                        }else {
                            //update single user
                            userIdList.clear();
                            userIdList.add(user_id);
                            //call api
                        }
                        if (CommonMethods.checkInternetConnection(getActivity())){
                            sendRequest(ApiCode.UPDATE_SCREENSHOT_POLICY);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (type.equalsIgnoreCase("all")){
                            if (isSelectedAll) {
                                isSelectedAll = false;
                            }else {
                                isSelectedAll = true;
                            }
                            if (value.equals("0")){
                                switchBtn.setChecked(true);
                            }else{
                                switchBtn.setChecked(false);
                            }
                            setSwitchColor();
                        }else{
                            if (value.equals("0")){
                                switchBtn.setChecked(true);
                                setSingleSwitchColor(true,switchBtn);
                            }else{
                                switchBtn.setChecked(false);
                                setSingleSwitchColor(false,switchBtn);
                            }
                        }
                    }
                }).show();


    }

    private  void setSwitchColor(){
        if (isSelectedAll){
            btnEnableAll.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.track_color)));
            btnEnableAll.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.graient2)));
        }else{

            btnEnableAll.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.light_gray)));
            btnEnableAll.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.gray)));
        }

    }
    private  void setSingleSwitchColor(boolean checked,SwitchCompat switchBtn){
        if (checked){
            switchBtn.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.track_color)));
            switchBtn.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.graient2)));
        }else{

            switchBtn.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.light_gray)));
            switchBtn.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.gray)));
        }

    }

    private void updateList(boolean b) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (modellist.size()>0){
                    for (int i=0;i<modellist.size();i++){
                        ScreenshotPoliyModel model=modellist.get(i);
                        if (b)
                            model.setIs_enable("1");
                        else
                            model.setIs_enable("0");
                    }
                }
                if (policyadapter!=null) {
                    policyadapter.notifyDataSetChanged();
                }
            }
        });

    }


    private void setCourseSpinner() {
        clist.add(new MyCourseDetailModel(""));
        courseAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, clist);
        courseAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        courseSpinner.setAdapter(courseAdapter);
        courseAdapter.notifyDataSetChanged();
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvCourseSpinner.setText(parent.getSelectedItem().toString());
                    courseId = clist.get(position).getId();
                    if (ConnectivityReceiver.isConnected()) {
                       // sendRequest(ApiCode.GET_STUDENT_LIST_ATTENDANCE);
                        sendRequest(ApiCode.GET_SCREENSHOT_POLICY);
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                    }
                    // category_name = subCategoryList.get(position).getName();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);

                sendApiCall();
                // initControl();
//                initRecyclerView();
//
//                swipe.setRefreshing(false);
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendApiCall() {
        if(ConnectivityReceiver.isConnected()){
            //call api
          //  btnEnableAll.setChecked(false);
            isSelectedAll=false;
            if (TextUtils.isEmpty(courseId)) {
                sendRequest(ApiCode.GET_COURSE);
            }else {
                 sendRequest(ApiCode.GET_SCREENSHOT_POLICY);
            }

        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_screenshot.setLayoutManager(linearLayoutManager);
        rec_screenshot.setKeepScreenOn(true);

        policyadapter = new ScreenshotPoliyAdapter(getContext(), modellist, new ScreenshotPoliyAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int position,String value,String id,SwitchCompat switchBtn) {
                enable_value=value;
                user_id=id;

               // please remove comment while do api modification for confirmation
               if (enable_value.equals("0"))
               // showConfirmation("single","Are you sure to disable screenshot policy?",switchBtn);
                showConfirmation("single","0",switchBtn);
                else
                    //showConfirmation("single","Are you sure to enable screenshot policy?",switchBtn);
                showConfirmation("single","1",switchBtn);

                //---shoudl be remove here
//                if (!TextUtils.isEmpty(enable_value) && !TextUtils.isEmpty(user_id)) {
//                    if (ConnectivityReceiver.isConnected()) {
//                        sendRequest(ApiCode.UPDATE_SCREENSHOT_POLICY);
//                    } else {
//                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
//                    }
//                }else{
//                    CommonMethods.showSuccessToast(getContext(),"Something Went Wrong");
//                }
            }

        });
        rec_screenshot.setAdapter(policyadapter);
        policyadapter.notifyDataSetChanged();

        if (modellist.size()>0){
            btnEnableAll.setVisibility(View.VISIBLE);
            btnEnableAll.setChecked(false);
            isSelectedAll=false;
            setAllIdList();
            checkAllEnabled();
        }else{
            btnEnableAll.setVisibility(View.GONE);
            btnEnableAll.setChecked(false);
            isSelectedAll=false;
            setSwitchColor();
        }

    }

    private void checkAllEnabled() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                isSelectedAll=true;
                for (int i=0;i<modellist.size();i++){
                    ScreenshotPoliyModel model=modellist.get(i);
                    if (model.getIs_enable().equals("0")){
                        isSelectedAll=false;
                        break;
                    }
                }
                btnEnableAll.setChecked(isSelectedAll);
                setSwitchColor();
            }
        });

    }

    private void setAllIdList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (modellist.size()>0){
                    allIdList.clear();
                    for (int i=0;i<modellist.size();i++){
                        allIdList.add(modellist.get(i).getStudent_id());
                    }
                }
            }
        });
    }

    private void sendRequest(int request) {


        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
        switch (request) {
            case ApiCode.GET_SCREENSHOT_POLICY:
                params.put("teacher_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("course_id",courseId);
                callApi(ApiCode.GET_SCREENSHOT_POLICY, params);
                break;
            case ApiCode.UPDATE_SCREENSHOT_POLICY:
                setStudentId();

                params.put("teacher_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("is_enable",enable_value);
               // params.put("user_id",user_id);
                params.put("data",new Gson().toJson(studentIdList));
                //params.put("data",new Gson().toJson(userIdList));
              params.put("course_id",courseId);
                callApi(ApiCode.UPDATE_SCREENSHOT_POLICY, params);
                break;

            case ApiCode.GET_COURSE :
                params.put("user_id",management.getString(USER_ID));
                params.put("course_id"," ");
                reference.child(management.getString(USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clist.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            MyCourseDetailModel myCourseDetailModel = ds.getValue(MyCourseDetailModel.class);
                            clist.add(myCourseDetailModel);








                        }
                        courseAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//
//                callApi(ApiCode.GET_COURSE, params);
                break;

        }
    }

    private void setStudentId() {

        studentIdList.clear();
        for (int i=0;i<userIdList.size();i++){
            studentIdList.add(new Student(userIdList.get(i)));
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_SCREENSHOT_POLICY:
                service.postDataVolley(ApiCode.GET_SCREENSHOT_POLICY,
                        BaseUrl.URL_GET_SCREENSHOT_POLICY, params);

                break;
            case ApiCode.UPDATE_SCREENSHOT_POLICY:
                service.postDataVolley(ApiCode.UPDATE_SCREENSHOT_POLICY,
                        URL_UPDATE_SCREENSHOT_POLICY, params);
                Log.e("url",URL_UPDATE_SCREENSHOT_POLICY);
                Log.e("params",""+params);
                break;
            case ApiCode.GET_COURSE:
                service.postDataVolley(ApiCode.GET_COURSE,
                        BaseUrl.URL_GET_COURSE, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.UPDATE_SCREENSHOT_POLICY:
                Log.e("screenshot ", response);
                if (jsonObject.getBoolean("status")) {
                    user_id = "";
                    enable_value="";

                    CommonMethods.showSuccessToast(getContext(), "Policy Updated Successfully");
                    sendApiCall();
                }
                break;

            case ApiCode.GET_SCREENSHOT_POLICY:
                Log.e("screenshotList", response);
               // btnEnableAll.setChecked(false);
                modellist.clear();
                initRecyclerView();
                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<ScreenshotPoliyModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<ScreenshotPoliyModel>>() {
                                        }.getType());
                        modellist.clear();
                        modellist.addAll(psearch);
                        initRecyclerView();
                        // notesAdapter.notifyDataSetChanged();
                    } else {

                        //notesAdapter.notifyDataSetChanged();
                        //  CommonMethods.showSuccessToast(getContext(),"Notes not available");
                    }
                }else{

                }
                break;

            case ApiCode.GET_COURSE:
                Log.e("courses ",response);
                try {
                    clist.clear();
                    clist.add(new MyCourseDetailModel(""));
                    if (jsonObject.getBoolean("status")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0) {
                            ArrayList<MyCourseDetailModel>list=new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<MyCourseDetailModel>>() {
                                            }.getType());

                            clist.addAll(list);

                        }

                    }
                    courseAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity)getActivity()).getSupportActionBar().show();
        ((TeacherMainActivity)getActivity()).setTeacherActionBar("Screenshot Policy",false);
    }

    private class Student{
        String user_id;

        public Student(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_id() {
            return user_id;
        }
    }



}
package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Notice_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.Notice;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Scl_SelectChpNoticeFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    RecyclerView rec_notice;
    private FloatingActionButton feb_notice;
    TextView tv_add;
    SwipeRefreshLayout swipe;
    RelativeLayout rel_no_live, rel_showlist;
    CardView cv_create_notice;

   // ArrayList<SchoolChapterNoticeModel> noteList = new ArrayList<>();
    ArrayList<Notice> noteList = new ArrayList<>();
   // SchoolChapterNoticeAdapter notesAdapter;
    Notice_Adapter noticeAdapter;
    String from,teacher_id,chapter_id,subject_id,pageTitle,notice_msg,notice_id,school_id;
    String id,student_type;

    public Scl_SelectChpNoticeFragment() {
        // Required empty public constructor
    }


    public static Scl_SelectChpNoticeFragment newInstance(String param1, String param2) {
        Scl_SelectChpNoticeFragment fragment = new Scl_SelectChpNoticeFragment();
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
        View view= inflater.inflate(R.layout.fragment_scl__select_chp_notice, container, false);
        initView(view);
        getArgumentData();
        initRecyclerView();
        sendAPicCAll();
        initControl();
      //  initFloatingButton();
       // initRecyclerView();
        init_swipe_method();
        //tv_add.setOnClickListener(this);
        feb_notice.setOnClickListener(this);
        cv_create_notice.setOnClickListener(this);
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_TUTOR)) {
            school_id = getArguments().getString("school_id");
            teacher_id = getArguments().getString("teacher_id");
            chapter_id = getArguments().getString("chapter_id");
            pageTitle = getArguments().getString("chapter_name");
            subject_id = getArguments().getString("subject_id");
           // feb_notice.setVisibility(View.GONE);
        //    rel_no_live.setVisibility(View.GONE);

        }else if (from.equals(SCHOOL_STUDENT)){
            student_type=getArguments().getString("student_type");
            id=getArguments().getString("id");
            feb_notice.setVisibility(View.GONE);
            rel_no_live.setVisibility(View.GONE);
        } else {
            teacher_id = getArguments().getString("teacher_id");
            chapter_id = getArguments().getString("chapter_id");
            pageTitle = getArguments().getString("chapter_name");
            subject_id = getArguments().getString("subject_id");

        }
    }


    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        rec_notice = view.findViewById(R.id.rec_notice);
        feb_notice = view.findViewById(R.id.feb_notice);
        tv_add = view.findViewById(R.id.tv_discription);
        rel_no_live=view.findViewById(R.id.rel_no_live);
        rel_showlist=view.findViewById(R.id.rel_showlist);
        cv_create_notice=view.findViewById(R.id.card_create_course);

    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                sendAPicCAll();
                initFloatingButton();
                //initControl();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendAPicCAll() {
       if (CommonMethods.checkInternetConnection(getActivity())){
           if (from.equals(SCHOOL_TUTOR)) {
               //sendRequest(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR);
               if (subject_id==null && chapter_id==null){
                   sendRequest(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR);
               }else {
                   sendRequest(ApiCode.SCHOOL_GET_NOTICE);
               }
           }else if (from.equals(SCHOOL_STUDENT)){
               sendRequest(ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT);
           } else {
               sendRequest(ApiCode.SCHOOL_GET_NOTICE);
           }
       }
    }
    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    private void initFloatingButton() {
        rel_no_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotesList();
            }
        });
    }


    private void initRecyclerView() {

//        noteList.add(new SchoolChapterNoticeModel());
//        noteList.add(new SchoolChapterNoticeModel());
//        noteList.add(new SchoolChapterNoticeModel());

        rec_notice.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_notice.setKeepScreenOn(true);
//        notesAdapter = new SchoolChapterNoticeAdapter(getContext(), noteList, new SchoolChapterNoticeAdapter.OnNoticeClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//
//            @Override
//            public void onDelete(int position) {
//                showDeleteDialog();
//
//            }
//        });
//        rec_notes.setAdapter(notesAdapter);
//        if (noteList.size() == 0) {
//            rel_no_live.setVisibility(View.VISIBLE);
//            rel_showlist.setVisibility(View.GONE);
//
//        } else {
        noticeAdapter=new Notice_Adapter(getActivity(), noteList, new Notice_Adapter.OnNoticeMsgClickListener() {
            @Override
            public void onClick(int position) {
                showMessageDialog(noteList.get(position).getMsg());
            }
        });
        rec_notice.setAdapter(noticeAdapter);
        noticeAdapter.notifyDataSetChanged();

        if (noticeAdapter.getItemCount()>0) {

            if (from.equals(SCHOOL_TUTOR)||from.equals(INDEPENDENT_TUTOR)){
                if (subject_id==null && chapter_id==null){
                    rel_no_live.setVisibility(View.GONE);
                    rel_showlist.setVisibility(View.VISIBLE);
                    feb_notice.setVisibility(View.GONE);
                }else{
                    rel_no_live.setVisibility(View.GONE);
                    rel_showlist.setVisibility(View.VISIBLE);
                }
            }else if (from.equals(SCHOOL_STUDENT)){
                if (subject_id==null && chapter_id==null){
                    rel_no_live.setVisibility(View.GONE);
                    rel_showlist.setVisibility(View.VISIBLE);
                    feb_notice.setVisibility(View.GONE);
                }else{
                    rel_no_live.setVisibility(View.GONE);
                    rel_showlist.setVisibility(View.VISIBLE);
                }
            }
        }else{
            if (subject_id==null && chapter_id==null) {
                rel_no_live.setVisibility(View.GONE);
                rel_showlist.setVisibility(View.GONE);
            }else{
                rel_no_live.setVisibility(View.VISIBLE);
                rel_showlist.setVisibility(View.GONE);
            }
        }
        //  }


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
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
                dialog.dismiss();
            }
        }).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.feb_notice:
            case R.id.card_create_course:
                showNotesList();
                break;
        }
    }
    private void showNotesList() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_school_notice);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        LinearLayout liner = dialog.findViewById(R.id.liner);
        Button btn_upload = dialog.findViewById(R.id.btn_send);
        EditText et_message = dialog.findViewById(R.id.et_name);
        dialog.show();
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice_msg=et_message.getText().toString().trim();
                if (TextUtils.isEmpty(notice_msg)){
                    et_message.setError("Enter message");
                    et_message.requestFocus();
                }else{
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_ADD_NOTICE);
                    }
                }

                dialog.dismiss();

            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT:
                if (student_type.equals("school")){
                    params.put("school_id", id);
                    params.put("type", "0");
                }else if (student_type.equals("itutor")){
                    params.put("teacher_id", id);
                    params.put("type", "1");
                }

                callApi(ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE:
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_NOTICE, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
                params.put("school_id", school_id);
                params.put("type", "1");
                callApi(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_ADD_NOTICE:

                params.put("message", notice_msg);
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_ADD_NOTICE, params);
                break;
            case ApiCode.SCHOOL_UPDATE_NOTICE:
                params.put("message", notice_msg);
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                params.put("notice_id", notice_id);
                callApi(ApiCode.SCHOOL_UPDATE_NOTICE, params);
                break;
            case ApiCode.SCHOOL_DELETE_NOTICE:
                params.put("notice_id", notice_id);
                callApi(ApiCode.SCHOOL_DELETE_NOTICE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_NOTICE_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTICE_BY_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_NOTICE:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE,
                        BaseUrl.URL_SCHOOL_GET_NOTICE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTICE);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_NOTICE_STUDENT_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTICE_STUDENT_TUTOR);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_NOTICE:
                service.postDataVolley(ApiCode.SCHOOL_ADD_NOTICE,
                        BaseUrl.URL_SCHOOL_ADD_NOTICE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_NOTICE);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_NOTICE:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_NOTICE,
                        BaseUrl.URL_SCHOOL_UPDATE_NOTICE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_NOTICE);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_NOTICE,
                        BaseUrl.URL_SCHOOL_DELETE_NOTICE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_NOTICE);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_NOTICE:
           // case ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT:
                Log.e("sc_notce", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<Notice> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Notice>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        initRecyclerView();
                    }else{
                        noteList.clear();
                        initRecyclerView();
                    }
                } else {
                    noteList.clear();
                    initRecyclerView();
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_NOTICE_BY_STUDENT:
                Log.e("sc_notce", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<Notice> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Notice>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        initRecyclerView();
                    }else{
                        noteList.clear();
                        initRecyclerView();
                    }
                } else {
                    noteList.clear();
                    initRecyclerView();
                  //  CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_NOTICE:
                Log.e("sc_add_notcie", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTICE);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_NOTICE:
                Log.e("sc_up_notice", response);
                if (jsonObject.getBoolean("response")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTICE);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_NOTICE:
                Log.e("delete_notcie", response);
                if (jsonObject.getBoolean("response")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTICE);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
                Log.e("sc_notce", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<Notice> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Notice>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        initRecyclerView();
                    }else{
                        noteList.clear();
                        initRecyclerView();
                    }
                } else {
                    noteList.clear();
                    initRecyclerView();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equals(SCHOOL_STUDENT)) {
           ((SchoolMainActivity)getActivity()).setActionBarTitle("Notice");
        } else if (from.equals(SCHOOL_TUTOR)) {
            ((SchoolMainActivity)getActivity()).setActionBarTitle(pageTitle);

        }
    }
}
package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.app.Dialog;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Notice_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.Notice;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Sc_Notice_Fragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rec_notice;
    TextView tv_student,tv_tutor;
    View view_student,view_tutor;
    LinearLayout lin_student,lin_teacher,lin_header;
    View lin_s_t_layout;
    RelativeLayout rel_notice;
    FloatingActionButton fab_add_btn;
    SwipeRefreshLayout refreshLayout;
    String type="0";
    String notice_msg;
    String school_id,from,itutor_id;

    Notice_Adapter noticeAdapter;
    ArrayList<Notice> noticeArrayList = new ArrayList<>();





    public Sc_Notice_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sc__notice_, container, false);
        bindView(view);
        getArgument();
        callRefreshApi();
        return view;
    }

    private void getArgument() {
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING)) {
            school_id = getArguments().getString("school_id");
        }else if (from.equals(INDEPENDENT_TUTOR)){
            itutor_id=getArguments().getString("itutor_id");
            lin_s_t_layout.setVisibility(View.GONE);

        }
    }

    private void bindView(View view) {
        lin_s_t_layout=view.findViewById(R.id.bottom_school);
        refreshLayout=view.findViewById(R.id.refresh_layout);
        fab_add_btn=view.findViewById(R.id.feb_notice);
        rec_notice=view.findViewById(R.id.rec_notice);
        tv_student=view.findViewById(R.id.tv_school);
        lin_header=view.findViewById(R.id.lin_header);
        rel_notice=view.findViewById(R.id.rel_notice_layout);
        //tv_tutor=view.findViewById(R.id.);
        view_student=view.findViewById(R.id.view_school);
        view_tutor=view.findViewById(R.id.view_tutor);
        lin_student=view.findViewById(R.id.lin_school);
        lin_teacher=view.findViewById(R.id.lin_tutor);

        tv_student.setText("Student");
        lin_teacher.setOnClickListener(this);
        lin_student.setOnClickListener(this);
        fab_add_btn.setOnClickListener(this);
        rec_notice.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(this);

    }
    private void set_notice() {
//        if (noticeArrayList.size() > 0) {
            noticeAdapter = new Notice_Adapter(getContext(), noticeArrayList, new Notice_Adapter.OnNoticeMsgClickListener() {
                @Override
                public void onClick(int position) {
                    showMessageDialog(noticeArrayList.get(position).getMsg());
                }
            });
            rec_notice.setAdapter(noticeAdapter);
            noticeAdapter.notifyDataSetChanged();
            if (noticeAdapter.getItemCount()>0){
            lin_header.setVisibility(View.VISIBLE);
            rec_notice.setVisibility(View.VISIBLE);
            rel_notice.setVisibility(View.GONE);
          Log.e("ssize",""+noticeAdapter.getItemCount());
        } else {
            lin_header.setVisibility(View.GONE);
            rec_notice.setVisibility(View.GONE);
            rel_notice.setVisibility(View.VISIBLE);
            Log.e("ssize",""+noticeAdapter.getItemCount());

        }
        Log.e("ssize",""+noticeAdapter.getItemCount());
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_school:
                type="0";
                view_tutor.setVisibility(View.GONE);
                view_student.setVisibility(View.VISIBLE);
                callRefreshApi();
                break;
            case R.id.lin_tutor:
                type="1";
                view_student.setVisibility(View.GONE);
                view_tutor.setVisibility(View.VISIBLE);
                callRefreshApi();
                break;
            case R.id.feb_notice:
                addNoticeDialog();
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ((SchoolMainActivity)getActivity()).setActionBarTitle("Notice");
    }

    private void addNoticeDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_school_notice);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
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
                        if (from.equals(SCHOOL_COACHING))
                        sendRequest(ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR);
                        else if (from.equals(INDEPENDENT_TUTOR))
                            sendRequest(ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR);
                    }
                }

                dialog.dismiss();

            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }

    //----------API CALL---------//
    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR:
                params.put("type",type);
                params.put("message",notice_msg);
                params.put("date_time",CommonMethods.getCurrentTime("dd-MM-yyyy | hh:mm a"));
                params.put("school_id",school_id);
                callApi(ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
                params.put("school_id",school_id);
                params.put("type",type);
                callApi(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR:
                params.put("teacher_id",itutor_id);
                callApi(ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR:
                params.put("teacher_id",itutor_id);
                params.put("message",notice_msg);
                params.put("date_time",CommonMethods.getCurrentTime("dd-MM-yyyy | hh:mm a"));
                callApi(ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_ADD_NOTICE_STUDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_NOTICE_STUDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR, params);
                Log.e("api",""+BaseUrl.URL_SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR);
                Log.e("params",""+params);
                break;
            case ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR, params);
                Log.e("api",""+BaseUrl.URL_SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR);
                Log.e("params",""+params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR:
            case ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR:
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
                        set_notice();
                    }else{
                        noticeArrayList.clear();
                        set_notice();
                    }
                }else{
                    noticeArrayList.clear();
                    set_notice();
                }
                break;
            case ApiCode.SCHOOL_ADD_NOTICE_STUDENT_TUTOR:
            case ApiCode.SCHOOL_ADD_NOTICE_BY_INDEPENDENT_TUTOR:
                Log.e("notice",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Notice added successfully");
                    callRefreshApi();
                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        callRefreshApi();

    }

    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            //call api
            if (from.equals(SCHOOL_COACHING)) {
                sendRequest(ApiCode.SCHOOL_GET_NOTICE_STUDENT_TUTOR);
            }else if (from.equals(INDEPENDENT_TUTOR)){
                sendRequest(ApiCode.SCHOOL_GET_NOTICE_BY_INDEPENDENT_TUTOR);
            }
        }
    }

    //--------------------//
}
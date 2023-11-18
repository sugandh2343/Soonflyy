package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorStudentPerformanceModel;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.StudentPerformanceAdapter;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Scl_StudentPerformanceFragment extends Fragment implements VolleyResponseListener {
    SwipeRefreshLayout swipe;
    RecyclerView rec_performance;
    StudentPerformanceAdapter performanceAdapter;
   // ArrayList<StudentPerformanceModel> list;
    ArrayList<Indp_TutorStudentPerformanceModel> list=new ArrayList<>();
    String id,student_type,from;

    public Scl_StudentPerformanceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Scl_StudentPerformanceFragment newInstance(String param1, String param2) {
        Scl_StudentPerformanceFragment fragment = new Scl_StudentPerformanceFragment();
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
        View view= inflater.inflate(R.layout.fragment_scl__student_performance, container, false);
        initView(view);
        getArgumentData();
        callRefreshApi();
       // initRecyclerview();
        init_swipe_method();
        return  view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        id=getArguments().getString("id");
        student_type=getArguments().getString("student_type");
    }

    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        rec_performance= view.findViewById(R.id.rec_performance);
        rec_performance.hasFixedSize();
        rec_performance.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_performance.setLayoutManager(layoutManager);
        rec_performance.setKeepScreenOn(true);

    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callRefreshApi();
               // initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_PERFORMANCE);
        }
    }


    private void initRecyclerview() {

        performanceAdapter = new StudentPerformanceAdapter(getActivity(), list, new StudentPerformanceAdapter.OnSchoolDetailClickListener() {
            @Override
            public void onItemClick(int postion) {
                ShowSeeDetailDailoge(list.get(postion));
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_performance.setAdapter(performanceAdapter);
        performanceAdapter.notifyDataSetChanged();


    }

    private void ShowSeeDetailDailoge(Indp_TutorStudentPerformanceModel model) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_student_performance);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);

        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_test_title = dialog.findViewById(R.id.tv_title);
        TextView tv_total = dialog.findViewById(R.id.tv_total);
        TextView tv_obtained = dialog.findViewById(R.id.tv_obtained);
        EditText et_remark = dialog.findViewById(R.id.et_remark);
        LinearLayout lin_remark = dialog.findViewById(R.id.lin_remark);



        tv_test_title.setText("Test On "+model.getTitle());
        tv_total.setText(model.getTotal_mark());
        tv_obtained.setText(model.getObtain_mark());
        if (model.getRemark()!=null) {
            if (model.getRemark().equals("0")) {
               lin_remark.setVisibility(View.GONE);
            } else {
               et_remark.setText(model.getRemark());
                lin_remark.setVisibility(View.VISIBLE);
            }
        }else{
            lin_remark.setVisibility(View.GONE);
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle("Performance");
    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_PERFORMANCE:
                params.put("student_id", new SessionManagement(getActivity()).getString(SCHOOL_STUDENT_ID));//
                //for school student
                if (student_type.equals("school")) {
                    params.put("school_id", id);
                   params.put("type", "0");
                } else if (student_type.equals("itutor")) {
                    //for independent tutor student
                    params.put("teacher_id",id);
                    params.put("type", "1");
                }
                callApi(ApiCode.SCHOOL_GET_PERFORMANCE, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_PERFORMANCE:
                service.postDataVolley(ApiCode.SCHOOL_GET_PERFORMANCE,
                        BaseUrl.URL_SCHOOL_GET_PERFORMANCE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_PERFORMANCE);
                Log.e("params", params.toString());
                break;
//


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_PERFORMANCE:
                Log.e("sc_perfrm", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Indp_TutorStudentPerformanceModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Indp_TutorStudentPerformanceModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                    } else {
                        list.clear();
                        initRecyclerview();

                    }
                }else{
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
        }
    }
}
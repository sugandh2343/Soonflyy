package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;

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
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter.Indp_TutorStudentPerformanceAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorStudentPerformanceModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Scl_Indp_TutorStudentPerformanceFragment extends Fragment implements VolleyResponseListener {

    SwipeRefreshLayout swipe;
    ImageView arrow_back_img;
    TextView tv_title;
    RecyclerView rec_performance;
    Indp_TutorStudentPerformanceAdapter performanceAdapter;
    ArrayList<Indp_TutorStudentPerformanceModel> list=new ArrayList<>();

    String school_id,from,student_id,student_name,title,test_id;
    String remark;
    int adapterPosition;

    public Scl_Indp_TutorStudentPerformanceFragment() {
        // Required empty public constructor
    }


    public static Scl_Indp_TutorStudentPerformanceFragment newInstance(String param1, String param2) {
        Scl_Indp_TutorStudentPerformanceFragment fragment = new Scl_Indp_TutorStudentPerformanceFragment();
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
        View view= inflater.inflate(R.layout.fragment_indp__tutor_student_performance, container, false);
        initView(view);
        getARgumentData();
        callApiRequest();
       // initRecyclerview();
        init_swipe_method();
        return  view;
    }

    private void getARgumentData() {
        from=getArguments().getString("from");
        school_id=getArguments().getString("school_id");
        student_id=getArguments().getString("student_id");
        student_name=getArguments().getString("name");

    }

    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(" ");
        arrow_back_img = view.findViewById(R.id.arrow_back_img);
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
                callApiRequest();
              //  initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }





    private void initRecyclerview() {

//        list = new ArrayList<>();
//
//        list.add(new Indp_TutorStudentPerformanceModel("100","95","Mathematics"));
//        list.add(new Indp_TutorStudentPerformanceModel("100","95","Mathematics"));
//        list.add(new Indp_TutorStudentPerformanceModel("100","95","Mathematics"));
//        list.add(new Indp_TutorStudentPerformanceModel("100","95","Mathematics"));

        performanceAdapter = new Indp_TutorStudentPerformanceAdapter(getActivity(), list, new Indp_TutorStudentPerformanceAdapter.OnPerformanceClickListener() {
            @Override
            public void onItemClick(int postion) {
//                SchoolTutorAllChapterFragment fragment = new SchoolTutorAllChapterFragment();
//
//                SwitchFragment (fragment);
            }

            @Override
            public void onAddMarkClick(int postion) {
                adapterPosition=postion;
                test_id=list.get(postion).getTest_id();
                OpenAddRemarkDailoge(list.get(postion));
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

    private void OpenAddRemarkDailoge(Indp_TutorStudentPerformanceModel model)  {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_add_remark);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);

        EditText et_name= dialog.findViewById(R.id.et_name);
        TextView tv_close = dialog.findViewById(R.id.tv_close);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_add);
        if (model.getRemark().equals("0")){
            dialogButton.setVisibility(View.VISIBLE);
        }else{
            et_name.setText(model.getRemark());
            dialogButton.setVisibility(View.GONE);
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark=et_name.getText().toString().trim();
                if (TextUtils.isEmpty(remark)){
                    et_name.setError("Enter remark");
                    et_name.requestFocus();

                }else{
                    //call api here
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_ADD_REMARK);
                    }
                    dialog.dismiss();
                }


            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle(student_name);
    }

    private void callApiRequest(){
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_PERFORMANCE);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_PERFORMANCE:
                //for school
                if (from.equals(SCHOOL_COACHING)) {
                    params.put("school_id", school_id);
                    params.put("student_id", student_id);//
                    params.put("type", "0");
                } else if (from.equals(INDEPENDENT_TUTOR)) {
                    //for independent tutor
                params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_IT_ID));
                params.put("student_id", student_id);
                params.put("type", "1");
                }


                callApi(ApiCode.SCHOOL_GET_PERFORMANCE, params);
                break;
            case ApiCode.SCHOOL_ADD_REMARK:
                //for school
                if (from.equals(SCHOOL_COACHING)) {
                    params.put("school_id", school_id);
                    params.put("student_id", student_id);
                    params.put("test_id", test_id);
                    params.put("title", remark);
                    params.put("type", "0");
                }else if (from.equals(INDEPENDENT_TUTOR)){
                    params.put("teacher_id", new SessionManagement(getActivity()).getString(SCHOOL_IT_ID));
                    params.put("test_id", test_id);
                    params.put("student_id", student_id);
                    params.put("title", remark);
                    params.put("type", "1");
                }
                //for independent tutor
//                params.put("teacher_id", school_id);
//                params.put("student_id", student_id);
//                params.put("type", "1");

                callApi(ApiCode.SCHOOL_ADD_REMARK, params);

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
            case ApiCode.SCHOOL_ADD_REMARK:
                service.postDataVolley(ApiCode.SCHOOL_ADD_REMARK,
                        BaseUrl.URL_SCHOOL_ADD_REMARK, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_REMARK);
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
                break;
            case ApiCode.SCHOOL_ADD_REMARK:
                Log.e("sc_perfrm", response.toString());
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                    callApiRequest();
                }else{
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
        }
    }
}
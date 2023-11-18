package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;

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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.School_Itutor_Adapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.ShowAllSchoolAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.School_ITutor_Search;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.ShowAllSchoolModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Scl_StudentShowAllSchoolFragment extends Fragment implements VolleyResponseListener {
    FloatingActionButton feb_studentschool;
    SwipeRefreshLayout swipe;
    RecyclerView rec_studentschool;
    ShowAllSchoolAdapter adapter;
    ArrayList<ShowAllSchoolModel> clist=new ArrayList<>();
    SessionManagement session_management;
    String search_mobile;
    RecyclerView rec_sc_itutor;
    School_Itutor_Adapter searchAdaper;
    ArrayList<School_ITutor_Search>serachList=new ArrayList<>();

    public Scl_StudentShowAllSchoolFragment() {
        // Required empty public constructor
    }

    public static Scl_StudentShowAllSchoolFragment newInstance(String param1, String param2) {
        Scl_StudentShowAllSchoolFragment fragment = new Scl_StudentShowAllSchoolFragment();
        Bundle args = new Bundle();

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
        View view = inflater.inflate(R.layout.fragment_scl__sttudent_show_all_school, container, false);

        initView(view);
        session_management = new SessionManagement(getActivity());
        callRefresApi();
        initRecyclerview();
        init_swipe_method();
        return view;
    }

    private void callRefresApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT);
        }
    }


    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        rec_studentschool = view.findViewById(R.id.rec_studentschool);
        feb_studentschool = view.findViewById(R.id.feb_studentschool);
        rec_studentschool.hasFixedSize();
        rec_studentschool.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_studentschool.setLayoutManager(layoutManager);
        rec_studentschool.setKeepScreenOn(true);
        feb_studentschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSchoolSearchDailoge();
            }
        });


    }

    private void showSchoolSearchDailoge() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_student_school);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        EditText et_mobile_num = dialog.findViewById(R.id.et_mobile_num);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        Button btn_search = dialog.findViewById(R.id.btn_search);
        rec_sc_itutor=dialog.findViewById(R.id.rec_schol_tutor);
        rec_sc_itutor.setLayoutManager(new LinearLayoutManager(getActivity()));

        dialog.show();
        searchAdaper=new School_Itutor_Adapter(getActivity(), serachList, new School_Itutor_Adapter.OnAddListener() {
            @Override
            public void onAdd(int position) {

                dialog.dismiss();
            }
        });
        rec_sc_itutor.setAdapter( searchAdaper);
        searchAdaper.notifyDataSetChanged();


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_mobile=et_mobile_num.getText().toString().trim();
                if (TextUtils.isEmpty(search_mobile)) {
                    et_mobile_num.setError("Enter Mobile Number");
                    et_mobile_num.requestFocus();
                }else  if(search_mobile.length()!=10){
                    et_mobile_num.setText("Invalid mobile number");
                    et_mobile_num.requestFocus();
                } else if(Integer.parseInt (String.valueOf (search_mobile.charAt (0))) < 6){
                    et_mobile_num.setText("Invalid mobile number");
                    et_mobile_num.requestFocus();
                }else {
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT);
                       // dialog.dismiss();
                    }
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }


    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callRefresApi();
                initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }


    private void initRecyclerview() {

        adapter = new ShowAllSchoolAdapter(getActivity(), clist, new ShowAllSchoolAdapter.OnSchoolClickListener() {
            @Override
            public void onItemClick(int postion) {
                SchoolStudentClassroomFragment fragment = new SchoolStudentClassroomFragment();

                SwitchFragment(fragment);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_studentschool.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.homeies, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT:
                params.put("student_id", session_management.getString(SCHOOL_STUDENT_ID));
                //for student
                params.put("type", "0");
                //for intependent tutor
//                params.put("type","1");
                callApi(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT:
                params.put("mobile", search_mobile);
                //for student
                params.put("type", "0");
                //for intependent tutor
//                params.put("type","1");
                callApi(ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT:
                Log.e("sc_s_school", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<ShowAllSchoolModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<ShowAllSchoolModel>>() {
                                        }.getType());
                        clist.clear();
                        clist.addAll(psearch);
                        initRecyclerview();
                    }else{
                        clist.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }

                break;
            case ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT:
                Log.e("sc_search", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<School_ITutor_Search> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<School_ITutor_Search>>() {
                                        }.getType());
                        serachList.clear();
                        serachList.addAll(psearch);
                        searchAdaper.notifyDataSetChanged();
                    }else{
                        serachList.clear();
                       searchAdaper.notifyDataSetChanged();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }

                break;
        }
    }

    private void setSearchList() {


    }


}
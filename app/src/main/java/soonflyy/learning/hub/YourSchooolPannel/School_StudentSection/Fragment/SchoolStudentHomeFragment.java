package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment;

import static com.google.android.material.tabs.TabLayout.GONE;
import static com.google.android.material.tabs.TabLayout.OnClickListener;
import static com.google.android.material.tabs.TabLayout.VISIBLE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGGED_IN_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_MOBILE;

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
import android.widget.FrameLayout;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.ScholProfileFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.OtherStudentAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.School_Itutor_Adapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter.ShowAllSchoolAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.OtherStudent;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.School_ITutor_Search;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.ShowAllSchoolModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolStudentHomeFragment extends Fragment implements OnClickListener, VolleyResponseListener {
    LinearLayout lnr_top, lin_school, lin_tutor,lin_search;
    TextView tv_mobile,tv_s_dailog_title,tv_slist_title,tv_home_page_title;
    View view_tutor, view_school;
    ImageView iv_setting;
    FrameLayout homeies;
    RelativeLayout rel_bordersss;
    SessionManagement management;

    FloatingActionButton feb_studentschool;
    SwipeRefreshLayout swipe;
    RecyclerView rec_studentschool;
    ShowAllSchoolAdapter adapter;
    ArrayList<ShowAllSchoolModel> clist=new ArrayList<>();
    String search_mobile;
    RecyclerView rec_sc_itutor;
    School_Itutor_Adapter searchAdaper;
    ArrayList<School_ITutor_Search>serachList=new ArrayList<>();
    String type="0",serach_id;
    boolean isAdded=false;

    OtherStudentAdapter otherStudentAdapter;
    ArrayList<OtherStudent>otherStudentList=new ArrayList<>();

    public SchoolStudentHomeFragment() {
        // Required empty public constructor
    }

    public static SchoolStudentHomeFragment newInstance(String param1, String param2) {
        SchoolStudentHomeFragment fragment = new SchoolStudentHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_student_home, container, false);
        rel_bordersss=view.findViewById (R.id.rel_bordersss);
        management=new SessionManagement(getActivity());
        initView(view);
        tv_home_page_title.setText("Student Section");
        callRefresApi();
        initRecyclerview();
        init_swipe_method();
        makeRelGon("visible");

//        Fragment selectedFragment = new Scl_StudentShowAllSchoolFragment();
//        getFragmentManager().beginTransaction().add(R.id.homeies,
//                selectedFragment).commit();


        return view;
    }

    private void callRefresApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT);
        }
    }

    private void initView(View view) {
        tv_home_page_title=view.findViewById(R.id.tv_t_hometitle);
        iv_setting=view.findViewById(R.id.iv_setting);
        homeies=view.findViewById (R.id.homeies);
        view_tutor = view.findViewById(R.id.view_tutor);
        view_school = view.findViewById(R.id.view_school);
        lnr_top = view.findViewById(R.id.lnr_top);
        lin_school = view.findViewById(R.id.lin_school);
        lin_tutor = view.findViewById(R.id.lin_tutor);
        tv_mobile=view.findViewById(R.id.tv_mob_no);
        tv_mobile.setText("+91 "+management.getString(SCHOOL_STUDENT_MOBILE));

        swipe = view.findViewById(R.id.swipe);
        rec_studentschool = view.findViewById(R.id.rec_studentschool);
        feb_studentschool = view.findViewById(R.id.feb_studentschool);
        rec_studentschool.hasFixedSize();
        rec_studentschool.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_studentschool.setLayoutManager(layoutManager);
        rec_studentschool.setKeepScreenOn(true);
        feb_studentschool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSchoolSearchDailoge();
            }
        });

        lnr_top.setOnClickListener(this);
        lin_school.setOnClickListener(this);
        lin_tutor.setOnClickListener(this);
        iv_setting.setOnClickListener(this);

        ///initialize tab
        type="0";
        view_tutor.setVisibility(View.GONE);
        view_school.setVisibility(View.VISIBLE);


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
        lin_search=dialog.findViewById(R.id.lin_search);
        tv_slist_title=dialog.findViewById(R.id.tv_slist_title);
        tv_s_dailog_title=dialog.findViewById(R.id.tv_s_title);
        rec_sc_itutor=dialog.findViewById(R.id.rec_schol_tutor);
        rec_sc_itutor.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (type.equals("1")){
            tv_s_dailog_title.setText("Find out Tutor");
            tv_slist_title.setText("Tutor list");
        }

        dialog.show();
        searchAdaper=new School_Itutor_Adapter(getActivity(), serachList, new School_Itutor_Adapter.OnAddListener() {
            @Override
            public void onAdd(int position) {
                serach_id=serachList.get(position).getId();
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    if (type.equals("0")) {
                        sendRequest(ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT);
                    } else if (type.equals("1")) {
                        sendRequest(ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT);
                    }
                  //  dialog.dismiss();
                }

            }
        });
        rec_sc_itutor.setAdapter( searchAdaper);
        searchAdaper.notifyDataSetChanged();


        btn_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search_mobile=et_mobile_num.getText().toString().trim();
                if (TextUtils.isEmpty(search_mobile)) {
                    et_mobile_num.setError("Enter Mobile Number");
                    et_mobile_num.requestFocus();
                }else  if(search_mobile.length()!=10){
                    et_mobile_num.setError("Invalid mobile number");
                    et_mobile_num.requestFocus();
                } else if(Integer.parseInt (String.valueOf (search_mobile.charAt (0))) < 6){
                    et_mobile_num.setError("Invalid mobile number");
                    et_mobile_num.requestFocus();
                }else {
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT);
                        // dialog.dismiss();
                    }
                }

            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (serachList.size()>0){
                    serachList.clear();
                    if (isAdded) {
                        callRefresApi();
                        isAdded=false;
                    }
                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }
    private void showOtherStudentDialog(ShowAllSchoolModel model){

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_other_students);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        RecyclerView recOtherList = dialog.findViewById(R.id.rec_list);
        recOtherList.setLayoutManager(new LinearLayoutManager(getActivity()));
        otherStudentAdapter=new OtherStudentAdapter(getActivity(), otherStudentList,
                new OtherStudentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, OtherStudent otherModel) {
                        if (otherModel.getBlock_status().equals("0")){
                            management.setString(SCHOOL_STUDENT_ID,otherModel.getStudent_id());
                            Bundle bundle=new Bundle();
                            bundle.putString("page_title", model.getName());
                            bundle.putString("id", model.getId());
                            if (type.equals("0")) {
                                bundle.putString("student_type", "school");
                                bundle.putString("class_id", otherModel.getClass_id());
                                bundle.putString("section_id", otherModel.getSection_id());

                            } else {
                                bundle.putString("student_type", "itutor");
                            }
                            bundle.putString("from", SCHOOL_STUDENT);
                            SchoolStudentClassroomFragment fragment = new SchoolStudentClassroomFragment();
                            fragment.setArguments(bundle);
                            ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                            showBlockAlert();
                        }
                       // management.setString(SCHOOL_STUDENT_ID,model.getStudent_id());
                    }
                });
        recOtherList.setAdapter(otherStudentAdapter);
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

                ShowAllSchoolModel model = clist.get(postion);
                otherStudentList=model.getStudent_data();
                if (otherStudentList.size()>0){
                    showOtherStudentDialog(model);
                }
//                if (clist.get(postion).getBlock_status().equals("0")) {
//                     Bundle bundle = new Bundle();
//                   // ShowAllSchoolModel model = clist.get(postion);
//                    management.setString(SCHOOL_STUDENT_ID,model.getStudent_id());
//                    bundle.putString("page_title", model.getName());
//                    bundle.putString("id", model.getId());
//                    if (type.equals("0")) {
//                        bundle.putString("student_type", "school");
//                        bundle.putString("class_id", model.getClass_id());
//                        bundle.putString("section_id", model.getSection_id());
//
//                    } else {
//                        bundle.putString("student_type", "itutor");
//                    }
//                    bundle.putString("from", SCHOOL_STUDENT);
//                    SchoolStudentClassroomFragment fragment = new SchoolStudentClassroomFragment();
//                    fragment.setArguments(bundle);
//                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
//                }else{
//
//                    showBlockAlert();
//                }
                // SwitchFragment(fragment);
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
        Log.e("lsize",""+adapter.getItemCount());


    }

    private void showBlockAlert() {
        String studentType="";
        if (type.equals("0")){
            studentType="school";
        }else{
            studentType="teacher";
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Alert")
                .setMessage("Access denied by "+studentType+".")
              .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
       // Fragment selectedFragment = null;

        switch (id) {
            case R.id.lin_school:
                //selectedFragment = new Scl_StudentShowAllSchoolFragment();
                type="0";
                view_tutor.setVisibility(View.GONE);
                view_school.setVisibility(View.VISIBLE);
                //SwitchFragment(selectedFragment);
             //   ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(selectedFragment);
                break;
            case R.id.lin_tutor:
                type="1";
               // selectedFragment = new ScoolStudentTutorFragment();
                view_tutor.setVisibility(View.VISIBLE);
                view_school.setVisibility(View.GONE);

                //SwitchFragment(selectedFragment);
                //((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(selectedFragment);
                break;
            case R.id.iv_setting:
                Bundle bundle=new Bundle();
                bundle.putString("type","3");
                bundle.putString("from",SCHOOL_STUDENT);
                bundle.putString("id",new SessionManagement(getActivity()).getString(SCHOOL_LOGGED_IN_STUDENT_ID));//SCHOOL_STUDENT_ID
                ScholProfileFragment fragment=new ScholProfileFragment();
                fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                break;

        }
        sendRequest(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT);


    }

    private void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.homeies, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void makeRelGon(String val)
    {
       if(val.equals ("gone"))
       {
           rel_bordersss.setVisibility (GONE);
       }
       else
       {
           rel_bordersss.setVisibility (VISIBLE);
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
    }

    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT:
                    params.put("student_id", management.getString(SCHOOL_LOGGED_IN_STUDENT_ID));//SCHOOL_STUDENT_ID
                    params.put("type", type);
                callApi(ApiCode.SCHOOL_GET_SCHOOL_COACHING_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT:
                params.put("mobile", search_mobile);
                params.put("type", type);
                callApi(ApiCode.SCHOOL_GET_SEARCH_SCHOOL_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT:
                params.put("student_id", management.getString(SCHOOL_LOGGED_IN_STUDENT_ID));
                params.put("school_id", serach_id);
                if (search_mobile.equals(management.getString(SCHOOL_STUDENT_MOBILE))) {
                    params.put("mobile", "");
                }else{
                    params.put("mobile", search_mobile);
                }
                callApi(ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT, params);
                break;
            case ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT :
                params.put("student_id", management.getString(SCHOOL_LOGGED_IN_STUDENT_ID));
                params.put("tutor_id", serach_id);
                if (search_mobile.equals(management.getString(SCHOOL_STUDENT_MOBILE))) {
                    params.put("mobile", "");
                }else{
                    params.put("mobile", search_mobile);
                }
                callApi(ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT, params);
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
            case ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_ADD_SCHOOL_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_SCHOOL_BY_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_ADD_TUTOR_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_TUTOR_BY_STUDENT);
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
                    clist.clear();
                    initRecyclerview();
                 //   CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
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
                        if (searchAdaper.getItemCount()>0){
                            lin_search.setVisibility(VISIBLE);
                        }else{
                            lin_search.setVisibility(GONE);
                        }
                        Log.e("ssize",""+searchAdaper.getItemCount());
                    }else{
                        serachList.clear();
                        searchAdaper.notifyDataSetChanged();
                        lin_search.setVisibility(GONE);
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }

                break;
            case ApiCode.SCHOOL_ADD_SCHOOL_BY_STUDENT:
                Log.e("sc_add", response.toString());
                if (jsonObject.getBoolean("response")){
                    isAdded=true;
                    CommonMethods.showSuccessToast(getActivity(),"School Added Successfully");
                }else{
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
            case ApiCode.SCHOOL_ADD_TUTOR_BY_STUDENT:
                Log.e("tutor_addd", response.toString());
                if (jsonObject.getBoolean("response")){
                    isAdded=true;
                    CommonMethods.showSuccessToast(getActivity(),"Tutor Added Successfully");
                }else{
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;

        }
    }

}


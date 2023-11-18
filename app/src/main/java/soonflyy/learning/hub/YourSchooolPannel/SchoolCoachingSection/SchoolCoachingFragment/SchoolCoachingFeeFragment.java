package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolT_FreeStatusAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.Scl_SelectChpterDeatailFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolT_FreeStatusModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolCoachingFeeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, VolleyResponseListener {
    RecyclerView rec_fee_detail,rec_fee_status ;
    Spinner class_spinner,section_spinner,fee_status_spinner,month_spinner;
    TextView tv_class,tv_section,tv_month,tv_fee_status;
    Button btn_upload;


    CardView cv_class,cv_section ;
    SchoolT_FreeStatusAdapter adapter;
    ArrayList<SchoolT_FreeStatusModel> list=new ArrayList<>();
    SwipeRefreshLayout swipe;
    ArrayList<AllClassesModel> classList=new ArrayList<>();
    ArrayList<AllClassesModel> sectionList=new ArrayList<>();
    ArrayList<String> monthList=new ArrayList<>();
    ArrayList<String>statusList=new ArrayList<>();
    ArrayAdapter<String>monthAdapter,statusAdapter;
    ArrayAdapter<AllClassesModel>class_adapter,section_adapter;



    String from,school_id,class_id="",month="",fee_status="",section_id="";
    String itutor_id;

    public SchoolCoachingFeeFragment() {
        // Required empty public constructor
    }


    public static SchoolCoachingFeeFragment newInstance(String param1, String param2) {
        SchoolCoachingFeeFragment fragment = new SchoolCoachingFeeFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_coaching_fee, container, false);
        initView(view);
        getArgumentData();
        setSpinner();
     //   callFeeApiForItutor();
        //sendApiRequest();
      //  initRecyclerview();
        init_swipe_method();
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING) ||from.equals(SCHOOL_TUTOR)){
            if (from.equals(SCHOOL_TUTOR)){
                btn_upload.setVisibility(View.GONE);
            }
            school_id=getArguments().getString("school_id");

            callApiRequest();
        } else if (from.equals(INDEPENDENT_TUTOR)) {
            itutor_id=getArguments().getString("itutor_id");
            cv_class.setVisibility(View.GONE);
            cv_section.setVisibility(View.GONE);
            callFeeApiForItutor();

        }
    }

    private void setSpinner() {
        new Thread(){
            @Override
            public void run() {
                super.run();



        //for class spinner
        classList.add(new AllClassesModel());
        class_adapter= new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, classList);
        class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        class_spinner.setAdapter(class_adapter);

        //for section spinner
        sectionList.add(new AllClassesModel());
        section_adapter= new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, sectionList);
        section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        section_spinner.setAdapter(section_adapter);

        //for month spinner
        monthList.clear();
        monthList.add("");
        monthList.addAll(CommonMethods.getMonth());
        monthAdapter= new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_holder, monthList);
        monthAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        month_spinner.setAdapter(monthAdapter);

        //for status spinner
        statusList. clear();
        statusList.add("");
        if (from.equals(INDEPENDENT_TUTOR)){
            statusList.add("Paid");
            statusList.add("Unpaid");
        }else {
            statusList.addAll(CommonMethods.getFeeStatusList());
        }
        statusAdapter= new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, statusList);
        statusAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        fee_status_spinner.setAdapter(statusAdapter);
            }

    }.start();
    }


    private void initView(View view) {
        btn_upload=view.findViewById(R.id.fee_upload);
        cv_class=view.findViewById(R.id.cv_class);
        cv_section=view.findViewById(R.id.cv_section);
        class_spinner = view.findViewById(R.id.class_spinner);
        section_spinner = view.findViewById(R.id.section_spinner);
        month_spinner = view.findViewById(R.id.month_spinner);
        fee_status_spinner = view.findViewById(R.id.status_spinner);

        tv_class = view.findViewById(R.id.tv_class);
        tv_section = view.findViewById(R.id.tv_section);
        tv_month = view.findViewById(R.id.tv_month);
        tv_fee_status = view.findViewById(R.id.tv_fee_status);

        rec_fee_status= view.findViewById(R.id.rec_fee_status);
        swipe = view.findViewById(R.id.swipe);
        rec_fee_status.hasFixedSize();
        rec_fee_status.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_fee_status.setLayoutManager(layoutManager);
        rec_fee_status.setKeepScreenOn(true);

        tv_section.setOnClickListener(this);
        tv_fee_status.setOnClickListener(this);
        tv_class.setOnClickListener(this);
        tv_month.setOnClickListener(this);

        class_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);
        month_spinner.setOnItemSelectedListener(this);
        fee_status_spinner.setOnItemSelectedListener(this);
        btn_upload.setOnClickListener(this);
    }


    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callFeeApiForItutor();
               // initRecyclerview();


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
//        list.add(new SchoolT_FreeStatusModel());
//        list.add(new SchoolT_FreeStatusModel());
//        list.add(new SchoolT_FreeStatusModel());
//        list.add(new SchoolT_FreeStatusModel());

        adapter= new SchoolT_FreeStatusAdapter (getActivity(), from, "1",list, new SchoolT_FreeStatusAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
                Scl_SelectChpterDeatailFragment fragment = new Scl_SelectChpterDeatailFragment ();

                SwitchFragment (fragment);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }

            @Override
            public void onFeStatusChange(int pisition, int spinnerPistion) {
                Log.e("positionsfdfdsf",""+pisition);
                Log.e("sssspositionsfdfdsf",""+spinnerPistion);
                if(spinnerPistion >0) {

                    if (spinnerPistion == 1){//type.equals("A")
                        try {
                            list.get(pisition).setPaidOrunpaid(true);
                            list.get(pisition).setFees_status("paid");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                }else if (spinnerPistion==2) {
                        try {
                            list.get(pisition).setPaidOrunpaid(false);
                            list.get(pisition).setFees_status("unpaid");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
        rec_fee_status.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (adapter.getItemCount()>0){
            btn_upload.setVisibility(View.VISIBLE);
        }else{
            btn_upload.setVisibility(View.GONE);
        }


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
        ((SchoolMainActivity)getActivity()).setActionBarTitle("Fee");
    }

    ///---api call--//





    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_class:
                class_spinner.performClick();

                break;
            case R.id.tv_section:
                section_spinner.performClick();

                break;
            case R.id.tv_month:
                month_spinner.performClick();

                break;
            case R.id.tv_fee_status:
                fee_status_spinner.performClick();

                break;
            case R.id.fee_upload:
                //Log.e("fees",new Gson().toJson(getFeesForUpdate()));
                if (list.size()>0){
                    if (CommonMethods.checkInternetConnection(getContext())){
                        sendRequest(ApiCode.SCHOOL_UPDATE_FEES);
                    }
                }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            switch (parent.getId()) {
                case R.id.class_spinner:
                    AllClassesModel model = classList.get(position);
                    tv_class.setText(model.getName());
                    class_id = model.getClass_id();
                    section_id = "";
                    tv_section.setText("");

                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    callFeeApiForItutor();
                    break;
                case R.id.month_spinner:
                    HashMap<String ,String>value=CommonMethods.getSelectedMonth(parent.getSelectedItem().toString());
                    tv_month.setText(value.get("name"));
                    month = value.get("value");
                    Log.e("mont",""+month);
                    callFeeApiForItutor();
                    break;
                case R.id.status_spinner:
                    fee_status=parent.getSelectedItem().toString();
                    tv_fee_status.setText(fee_status);
                    callFeeApiForItutor();
                    break;


            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void callApiRequest(){
        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_TUTOR)) {
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_CLASS);
            }
        }
    }
    private void callFeeApiForItutor() {


                if (CommonMethods.checkInternetConnection(getActivity())) {
                    if (from.equals(INDEPENDENT_TUTOR)) {
                    if (!TextUtils.isEmpty(month)
                            && !TextUtils.isEmpty(fee_status)) {
                        sendRequest(ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR);
                    }

                } else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_TUTOR)) {
                        if (CommonMethods.checkInternetConnection(getActivity())) {
                            if (!TextUtils.isEmpty(class_id)
                                    && !TextUtils.isEmpty(section_id)
                                    && !TextUtils.isEmpty(month)
                                    && !TextUtils.isEmpty(fee_status)) {
                                sendRequest(ApiCode.SCHOOL_GET_FEES_BY_SCHOOL);
                            }
                        }
                    }
            }

    }

    private void sendRequest(int request) {
        SessionManagement management=new SessionManagement(getActivity());
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS:
                params.put("school_id", school_id);
                if (from.equals(SCHOOL_TUTOR)){
                    params.put("teacher_id",management.getString(SCHOOL_TEACHER_ID));
                }else{
                    params.put("teacher_id","");
                }
                callApi(ApiCode.SCHOOL_GET_CLASS, params);
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                params.put("school_id", school_id);
                    params.put("class_id", class_id);
                if (from.equals(SCHOOL_TUTOR)){
                    params.put("teacher_id",management.getString(SCHOOL_TEACHER_ID));
                }else{
                    params.put("teacher_id","");
                }
                callApi(ApiCode.SCHOOL_GET_SECTION, params);
                break;
            case ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR:
                ///---for indepennt tutor--///
                params.put("teacher_id", itutor_id);//itutor_id
                params.put("month", month);//month
                params.put("type",fee_status.toLowerCase());//fee_status.toLowerCase()
                callApi(ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR, params);
                break;
            case ApiCode.SCHOOL_GET_FEES_BY_SCHOOL:
                params.put("school_id", school_id);
                params.put("month", month);//month
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("type",fee_status.toLowerCase());//fee_status.toLowerCase()
                callApi(ApiCode.SCHOOL_GET_FEES_BY_SCHOOL, params);
                break;
            case ApiCode.SCHOOL_UPDATE_FEES:
                ///---for indepennt tutor--///
                if (from.equals(INDEPENDENT_TUTOR)) {
                    params.put("teacher_id", itutor_id);//itutor_id
                    params.put("month", month);//month
                    params.put("type","1");
                    params.put("data",new Gson().toJson(getFeesForUpdate()));
                } else if (from.equals(SCHOOL_COACHING)) {
                    params.put("school_id", school_id);
                    params.put("month", month);//month
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("type","0");
                    params.put("data",new Gson().toJson(getFeesForUpdate()));
                }
                callApi(ApiCode.SCHOOL_UPDATE_FEES, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_GET_CLASS,
                        BaseUrl.URL_SCHOOL_GET_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION,
                        BaseUrl.URL_SCHOOL_GET_SECTION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SECTION);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_FEES_BY_SCHOOL:
                service.postDataVolley(ApiCode.SCHOOL_GET_FEES_BY_SCHOOL,
                        BaseUrl.URL_SCHOOL_GET_FEES_BY_SCHOOL, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_FEES_BY_SCHOOL);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_FEES:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_FEES,
                        BaseUrl.URL_SCHOOL_UPDATE_FEES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_FEES);
                Log.e("params", params.toString());
                break;
//


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_CLASS:
                Log.e("sc_login", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("classes");
                    if (jsonArray.length()>0){
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());

                        classList.clear();
                        classList.add(new AllClassesModel());
                        classList.addAll(psearch);
                        class_adapter.notifyDataSetChanged();

                    }else{
                        classList.clear();
                        classList.add(new AllClassesModel());
                        class_adapter.notifyDataSetChanged();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("section");
                    if (jsonArray.length()>0){
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                            sectionList.clear();
                            sectionList.add(new AllClassesModel());
                            sectionList.addAll(psearch);
                            section_adapter.notifyDataSetChanged();
                    }else{
                            sectionList.clear();
                            sectionList.add(new AllClassesModel());
                            section_adapter.notifyDataSetChanged();
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;

            case ApiCode.SCHOOL_GET_FEES_BY_INDEPENDENT_TUTOR:
            case ApiCode.SCHOOL_GET_FEES_BY_SCHOOL:
                Log.e("fees",response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolT_FreeStatusModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolT_FreeStatusModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
//                        adapter.notifyDataSetChanged();
                        Log.e("size",""+adapter.getItemCount());
                    }else{
                        list.clear();
                        initRecyclerview();
//                        adapter.notifyDataSetChanged();
                    }
                } else {
                    list.clear();
                    initRecyclerview();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }

                break;
            case ApiCode.SCHOOL_UPDATE_FEES:
                Log.e("update_fee",response);
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                        callFeeApiForItutor();

                }
                break ;

        }
    }

    private ArrayList<Fees>getFeesForUpdate(){
        ArrayList<Fees>feesArrayList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            SchoolT_FreeStatusModel model=list.get(i);
            if (model.isPaidOrunpaid()){
                feesArrayList.add(new Fees(model.getStudent_id(),"paid"));
            }else{
                feesArrayList.add(new Fees(model.getStudent_id(),"unpaid"));
            }
        }
        return feesArrayList;
    }


    ///

     private class Fees{
        String student_id;
        String status;

        public Fees(String student_id, String status) {
            this.student_id = student_id;
            this.status = status;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String Student_Id) {
            this.student_id = student_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


}









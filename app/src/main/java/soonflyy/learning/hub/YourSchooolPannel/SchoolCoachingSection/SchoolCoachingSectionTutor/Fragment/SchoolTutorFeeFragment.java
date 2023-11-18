package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolT_FreeStatusModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolTutorFeeFragment extends Fragment implements VolleyResponseListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
   RecyclerView rec_fee_detail,rec_fee_status ;
   Spinner class_spinner,section_spinner,fee_status_spinner,month_spinner;
   TextView tv_class,tv_section,tv_month,tv_fee_status;

    CardView cv_class ;
    SchoolT_FreeStatusAdapter adapter;
    ArrayList<SchoolT_FreeStatusModel> list;
    SwipeRefreshLayout swipe;
    ArrayList<AllClassesModel> classList=new ArrayList<>();
    ArrayList<AllClassesModel> sectionList=new ArrayList<>();
    ArrayList<String> monthList=new ArrayList<>();
    ArrayList<String>statusList=new ArrayList<>();
    ArrayAdapter<String>monthAdapter,statusAdapter;
    ArrayAdapter<AllClassesModel>class_adapter,section_adapter;



    String from,school_id,class_id="",month="",fee_status="",section_id="";

    public SchoolTutorFeeFragment() {
        // Required empty public constructor
    }

    public static SchoolTutorFeeFragment newInstance(String param1, String param2) {
        SchoolTutorFeeFragment fragment = new SchoolTutorFeeFragment();
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
        View view= inflater.inflate(R.layout.fragment_school_tutor_fee, container, false);
        initView(view);
        getArgumentData();
        setSpinner();
        //sendApiRequest();
        sendApiRequest();
        initRecyclerview();
        init_swipe_method();

        return view;
    }

    private void setSpinner() {
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
        statusList.addAll(CommonMethods.getFeeStatusList());
        statusAdapter= new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, statusList);
        statusAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        fee_status_spinner.setAdapter(statusAdapter);
    }

    private void sendApiRequest() {
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_CLASS);
        }
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        school_id=getArguments().getString("school_id");
    }


    private void initView(View view) {
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
    }


    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                if (TextUtils.isEmpty(class_id)){
                    sendApiRequest();
                }else if (!TextUtils.isEmpty(class_id) && TextUtils.isEmpty(section_id) && sectionList.size()==0){
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                }else {
                    callApiRequest();
                }
                initRecyclerview();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }






    private void initRecyclerview() {

        list = new ArrayList<>();

        list.add(new SchoolT_FreeStatusModel());
        list.add(new SchoolT_FreeStatusModel());
        list.add(new SchoolT_FreeStatusModel());
        list.add(new SchoolT_FreeStatusModel());

        adapter= new SchoolT_FreeStatusAdapter (getActivity(), from,"0",list, new SchoolT_FreeStatusAdapter.OnClickListener() {
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

            }

        });
        rec_fee_status.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS:
                params.put("school_id", school_id);
                callApi(ApiCode.SCHOOL_GET_CLASS, params);
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                params.put("class_id", class_id);
                callApi(ApiCode.SCHOOL_GET_SECTION, params);
                break;
            case ApiCode.SCHOOL_GET_FEES_BY_TUTOR:
                params.put("class_id", class_id);
                params.put("section_id", section_id);
                params.put("type", fee_status);
                params.put("month", month);
                callApi(ApiCode.SCHOOL_GET_FEES_BY_TUTOR, params);
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
            case ApiCode.SCHOOL_GET_FEES_BY_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_FEES_BY_TUTOR,
                        BaseUrl.URL_SCHOOL_GET_FEES_BY_TUTOR, params);
                Log.e("api",BaseUrl.URL_SCHOOL_GET_FEES_BY_TUTOR);
                Log.e("params",params.toString());
                break;

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

            case ApiCode.SCHOOL_GET_FEES_BY_TUTOR:
                Log.e("sc_tutor_fees",response.toString());

        }
    }

    @Override
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
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position>0){
            switch (parent.getId()){
                case R.id.class_spinner:
                    AllClassesModel model=classList.get(position);
                    tv_class.setText(model.getName());
                    class_id=model.getClass_id();
                    section_id="";
                    if (ConnectivityReceiver.isConnected()){
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel=sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id=smodel.getSection_id();
                    break;
                case R.id.month_spinner:
                    HashMap<String,String> valueMap=CommonMethods.getSelectedMonth(parent.getSelectedItem().toString());
                    tv_month.setText(valueMap.get("name"));
                    month=valueMap.get("value");
                    break;
                case R.id.status_spinner:
                    fee_status=parent.getSelectedItem().toString();
                    tv_fee_status.setText(fee_status);
                    break;
            }
            callApiRequest();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void callApiRequest(){
        if (!TextUtils.isEmpty(class_id)
        && !TextUtils.isEmpty(section_id)
                && !TextUtils.isEmpty(month)
                && !TextUtils.isEmpty(fee_status)){
                if (CommonMethods.checkInternetConnection(getActivity())){
                    sendRequest(ApiCode.SCHOOL_GET_FEES_BY_TUTOR);
                }
        }

    }








}

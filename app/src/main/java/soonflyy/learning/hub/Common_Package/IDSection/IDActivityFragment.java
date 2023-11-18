package soonflyy.learning.hub.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity.SCHOOL_ID_SECTION_USER_TYPE;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.widget.NestedScrollView;
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
import soonflyy.learning.hub.Common_Package.Adapters.IDActivityAdapter;
import soonflyy.learning.hub.Common_Package.Models.IDActivityModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IDActivityFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    SwipeRefreshLayout swipe;
    RecyclerView rec_activity;
    Button btn_submit;
    NestedScrollView scrollView;
    FloatingActionButton add_btn;
    IDActivityAdapter adapter;
    ArrayList<IDActivityModel> list=new ArrayList<>();
    String type="add";
    ArrayList<String>removeIdList=new ArrayList<>();
    String userType="";

    public IDActivityFragment() {
        // Required empty public constructor
    }


    public static IDActivityFragment newInstance(String param1, String param2) {
        IDActivityFragment fragment = new IDActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_i_d_activity, container, false);
        initView(view);
        getArgumentData();
        init_Recyclerview();
        init_swipe_method();
        refreshApi();
        clickListenet();
        return view;
    }
    private void getArgumentData() {
        userType=getArguments().getString("user_type");
        type=getArguments().getString("type");
        if (type.equals("add")){
            btn_submit.setText("Submit");
        }else{
            btn_submit.setText("Update");
        }
    }
    private void clickListenet() {
        btn_submit.setOnClickListener(this);
        add_btn.setOnClickListener(this);
    }


    private void initView(View view) {
        scrollView=view.findViewById(R.id.scrollView);
        btn_submit=view.findViewById(R.id.fab_add);
        add_btn=view.findViewById(R.id.floatingActionButton);
        swipe = view.findViewById(R.id.swipe);
        rec_activity = view.findViewById(R.id.rec_activity);
        rec_activity.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_activity.setLayoutManager(layoutManager);


    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
               refreshApi();
//                init_Recyclerview();
//                swipe.setRefreshing(false);
            }
        });
        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void refreshApi() {
        if (ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_ACTIVITY);
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
    }

    private void init_Recyclerview() {
       /// list = new ArrayList<>();
        if (list.size()==0) {
            list.add(new IDActivityModel("", ""));
        }
//        list.add(new IDActivityModel());
//        list.add(new IDActivityModel());
        adapter = new IDActivityAdapter(getActivity(),list.size(), list, new IDActivityAdapter.OnActivityClickListener() {
            @Override
            public void OnActivityRemove(int position,IDActivityModel model) {
                if (!TextUtils.isEmpty(model.getActivity_id())){
                    removeIdList.add(model.getActivity_id());
                }
                list.remove(position);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onActivityChange(int position, String name) {
                list.get(position).setActivity_name(name);
                try {
                    adapter.notifyItemChanged(position);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rec_activity.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                submit();
                break;
            case R.id.floatingActionButton:
                list.add(new IDActivityModel("",""));
                adapter.notifyDataSetChanged();
               // rec_activity.smoothScrollToPosition(adapter.getItemCount()-1);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

                break;
        }
    }

    private void submit() {
        if (validateField()){
            if (ConnectivityReceiver.isConnected()) {
                sendRequest(ApiCode.MANAGE_ACTIVITY);
            }else {
                CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
            }
           // if (type.equals("update")){
                //call api  to update

           // }else{
             //call api for add
           // }
        }
    }

    private boolean validateField() {
        if (checkEmptyField()){

           return true;
        }
        return false;
    }

    private boolean checkEmptyField() {
        boolean checkStatus=true;
        if (list.size()==0){
            CommonMethods.showSuccessToast(getContext(),"Add your activity");
            checkStatus=false;
        }else{
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Log.e("qpostion", "" + i);
                RecyclerView.ViewHolder viewHolder = rec_activity.findViewHolderForAdapterPosition(i);
                if (viewHolder == null) {
                    viewHolder = adapter.holderHasMap.get(i);
                }
                //  assert viewHolder != null;
                View view = viewHolder.itemView;
                EditText et_activity = view.findViewById(R.id.et_name);

                if (TextUtils.isEmpty(et_activity.getText().toString().trim())) {
                    et_activity.setError("Enter activity");
                    et_activity.requestFocus();
                    checkStatus = false;
                    break;
                } else {
                    IDActivityModel model = list.get(i);
                    model.setActivity_name(et_activity.getText().toString().trim());
                }
            }
        }


        return checkStatus;
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.MANAGE_ACTIVITY:
                params.put("activity", new Gson().toJson(list));
                params.put("remove_id",new Gson().toJson(removeIdList));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                setTypeParameter(params);
                // params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.MANAGE_ACTIVITY, params);
                break;
//
            case ApiCode.GET_ACTIVITY:
                //params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
               setTypeParameter(params);
                callApi(ApiCode.GET_ACTIVITY, params);
                break;
        }
    }
    private void setTypeParameter(HashMap<String, String> params) {
        if (userType.equals(SCHOOL_COACHING)){
            if (SCHOOL_ID_SECTION_USER_TYPE.equals(SCHOOL_COACHING)){
                params.put("type","0");
            }else{
                params.put("type","1");
            }

        }else {
            params.put("type","1");
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.MANAGE_ACTIVITY:
                service.postDataVolley(ApiCode.MANAGE_ACTIVITY,
                        BaseUrl.URL_MANAGE_ACTIVITY, params);
                break;
            case ApiCode.GET_ACTIVITY:
                service.postDataVolley(ApiCode.GET_ACTIVITY,
                        BaseUrl.URL_GET_ACTIVITY, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.MANAGE_ACTIVITY:
                Log.e("activity ", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getActivity(),"Activity Updated Successfully");
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                   if (userType.equals(SCHOOL_COACHING)){
                       //for school user
                       ((SchoolMainActivity)getActivity()).setIdSectionStatus("1");
                   }else {
                       //for home user
                       new SessionManagement(getActivity()).setString(ID_SECTION, "1");
                   }
                    refreshApi();
                }

                break;
//
            case ApiCode.GET_ACTIVITY:
                Log.e("get_Activity ", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("activity");
                    if (jsonArray.length() > 0) {
                        ArrayList<IDActivityModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<IDActivityModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                       init_Recyclerview();
                        // notesAdapter.notifyDataSetChanged();
                    } else {
                        list.clear();
                        init_Recyclerview();
                          }
                }else{
                    list.clear();
                    init_Recyclerview();
                }
                break;
        }
        }
    }


//}
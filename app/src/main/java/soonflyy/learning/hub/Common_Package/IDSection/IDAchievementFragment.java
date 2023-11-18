package soonflyy.learning.hub.Common_Package.IDSection;

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
import soonflyy.learning.hub.Common_Package.Adapters.IDAchievemenAdapter;
import soonflyy.learning.hub.Common_Package.Models.IDAchievementModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IDAchievementFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    SwipeRefreshLayout swipe;
    RecyclerView rec_achievement;
    NestedScrollView nestedScrollView;
    IDAchievemenAdapter adapter;
    ArrayList<IDAchievementModel> list=new ArrayList<>();
    ArrayList<String>removeIdList=new ArrayList<>();
    FloatingActionButton add_btn;
    Button submit_btn;
    String type="add";
    String userType="";


    public IDAchievementFragment() {
        // Required empty public constructor
    }

    public static IDAchievementFragment newInstance(String param1, String param2) {
        IDAchievementFragment fragment = new IDAchievementFragment();
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
        View view= inflater.inflate(R.layout.fragment_i_d_achievement, container, false);

        initView(view);
        getArgumentData();
        init_swipe_method();
        init_Recyclerview();
        refreshApi();
        clickListener();
        return view;
    }
    private void getArgumentData() {
        userType=getArguments().getString("user_type");
        type=getArguments().getString("type");
        if (type.equals("add")){
            submit_btn.setText("Submit");
        }else{
            submit_btn.setText("Update");
        }
    }

    private void clickListener() {
        add_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }


    private void initView(View view) {
        nestedScrollView=view.findViewById(R.id.nested_scroll);
        swipe = view.findViewById(R.id.swipe);
        rec_achievement= view.findViewById(R.id.rec_achievement);
        submit_btn=view.findViewById(R.id.fab_add);
        add_btn=view.findViewById(R.id.floatingActionButton);
        rec_achievement.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_achievement.setLayoutManager(layoutManager);

    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                refreshApi();
               // init_Recyclerview();
                swipe.setRefreshing(false);
            }
        });
        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }
    private void init_Recyclerview() {

        if (list.size()==0) {
            list.add(new IDAchievementModel(" ", ""));
        }
        adapter = new IDAchievemenAdapter(getActivity(), list, new IDAchievemenAdapter.OnAchievementListener() {
            @Override
            public void OnRemove(int position, IDAchievementModel model) {
                if (!TextUtils.isEmpty(model.getId())){
                    removeIdList.add(model.getId());
                }
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        rec_achievement.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                submit();
                break;
            case R.id.floatingActionButton:
                list.add(new IDAchievementModel(" ",""));
                adapter.notifyDataSetChanged();
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;
        }
    }

    private void submit() {
        if (validateField()){
            if (ConnectivityReceiver.isConnected()) {
                sendRequest(ApiCode.MANAGE_ACHIEVEMENT);
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
            CommonMethods.showSuccessToast(getContext(),"Add your achievement");
            checkStatus=false;
        }else{
            for (int i = 0; i < adapter.getItemCount(); i++) {
                Log.e("qpostion", "" + i);
                RecyclerView.ViewHolder viewHolder = rec_achievement.findViewHolderForAdapterPosition(i);
                if (viewHolder == null) {
                    viewHolder = adapter.holderHasMap.get(i);
                }
                //  assert viewHolder != null;
                View view = viewHolder.itemView;
                EditText et_activity = view.findViewById(R.id.et_name);

                if (TextUtils.isEmpty(et_activity.getText().toString().trim())) {
                    et_activity.setError("Enter achievement");
                    checkStatus = false;
                    break;
                } else {
                    IDAchievementModel model = list.get(i);
                    model.setAchievement_name(et_activity.getText().toString().trim());
                }
            }
        }


        return checkStatus;
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.MANAGE_ACHIEVEMENT:
                params.put("achievement", new Gson().toJson(list));
                params.put("remove_id",new Gson().toJson(removeIdList));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                //params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                setTypeParameter(params);
                callApi(ApiCode.MANAGE_ACHIEVEMENT, params);
                break;
//
            case ApiCode.GET_ACHIEVEMENT:
                //params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
              setTypeParameter(params);
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                callApi(ApiCode.GET_ACHIEVEMENT, params);
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
            case ApiCode.MANAGE_ACHIEVEMENT:
                service.postDataVolley(ApiCode.MANAGE_ACHIEVEMENT,
                        BaseUrl.URL_MANAGE_ACHIEVEMENT, params);
                break;
            case ApiCode.GET_ACHIEVEMENT:
                service.postDataVolley(ApiCode.GET_ACHIEVEMENT,
                        BaseUrl.URL_GET_ACHIEVEMENT, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.MANAGE_ACHIEVEMENT:
                Log.e("achieve ", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getActivity(),"Updated Successfully");
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                    refreshApi();
                }

                break;
//
            case ApiCode.GET_ACHIEVEMENT:
                Log.e("get_Achieve ", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("achievement");
                    if (jsonArray.length() > 0) {
                        ArrayList<IDAchievementModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<IDAchievementModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        init_Recyclerview();
                        // notesAdapter.notifyDataSetChanged();
                    } else {
                        list.clear();
                        init_Recyclerview();
                        //notesAdapter.notifyDataSetChanged();
                        //  CommonMethods.showSuccessToast(getContext(),"Notes not available");
                    }
                }else{
                    list.clear();
                    init_Recyclerview();
                }
                break;
        }
    }

    private void refreshApi() {
        if (ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_ACHIEVEMENT);
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
    }
}
package soonflyy.learning.hub.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.IDViewProfileAdapter;
import soonflyy.learning.hub.Common_Package.Models.IDViewProfileModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IDUserProfileFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    RecyclerView rec_viewprofile;
    ArrayList<IDViewProfileModel> list = new ArrayList<>();
    IDViewProfileAdapter adapter;
    SwipeRefreshLayout swipe;

    ImageView iv_back, profile_image, img_message;
    SearchView et_search;
    String user_type,profile_url;

    public IDUserProfileFragment() {
        // Required empty public constructor
    }


    public static IDUserProfileFragment newInstance(String param1, String param2) {
        IDUserProfileFragment fragment = new IDUserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_i_d_user_profile, container, false);
        initView(view);
        getArgumentData();
        refreshApi();
        init_swipe_method();
        setSearch();
        //init_Recyclerview();
        return view;
    }

    private void getArgumentData() {
        user_type = getArguments().getString("user_type");
    }


    private void initView(View view) {
        et_search=view.findViewById(R.id.et_search);
        CommonMethods.setSearchViewColor(getActivity(),et_search);
        swipe = view.findViewById(R.id.swipe);
        profile_image = view.findViewById(R.id.iv_profile);
        iv_back = view.findViewById(R.id.iv_back);
        img_message = view.findViewById(R.id.img_message);
        rec_viewprofile = view.findViewById(R.id.rec_viewprofile);
        rec_viewprofile.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_viewprofile.setLayoutManager(layoutManager);

        iv_back.setOnClickListener(this);
        img_message.setOnClickListener(this);
        profile_image.setOnClickListener(this);

    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                refreshApi();
            }
        });
        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void init_Recyclerview() {
        adapter = new IDViewProfileAdapter(getActivity(), list, new IDViewProfileAdapter.OnProfileClickListener() {
            @Override
            public void OnProfileClick(String userId,String isSchool) {
//                IDUserMessageFragment fragment = new IDUserMessageFragment();
//                ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
               gotoManageProfile("view",userId,isSchool);//list.get(position).getUser_id()
//                IDUserViewProfileFragment fragment=new IDUserViewProfileFragment();
//
//                ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);


            }
        });


        rec_viewprofile.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //setSearch();


    }

    private void setSearch() {

        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               Log.e("text",""+query);
                if (list.size()>0){
                    adapter.getFilter().filter(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
              if (list.size()>0){
                    adapter.getFilter().filter(query.trim());
              }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_profile:
                gotoManageProfile("edit",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID),"");//USER_ID
                break;
            case R.id.img_message:
               gotoChatUserList();
                break;
        }

    }

    private void gotoChatUserList() {
        IDUserShowMessageFragment fragment = new IDUserShowMessageFragment();
        Bundle bundle=new Bundle();
        bundle.putString("user_type",user_type);
        bundle.putString("profile_image",profile_url);
        fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher"))
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        else
            ((MainActivity) getActivity()).SwitchFragment(fragment);
    }

    private void gotoManageProfile(String type,String user_id,String isSchoolUser) {
        IDUserViewProfileFragment fragment=new IDUserViewProfileFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("isSchoolUser",isSchoolUser);
        bundle.putString("user_type",user_type);
        bundle.putString("user_id",user_id);
        fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher"))
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        else
            ((MainActivity) getActivity()).SwitchFragment(fragment);
    }

    private void goBack() {
        try {
            if (user_type.equals(SCHOOL_COACHING)){
                ((SchoolMainActivity)getActivity()).onBackPressed();
            }else if (user_type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).onBackPressed();
            } else {
                ((MainActivity) getActivity()).onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (user_type.equals(SCHOOL_COACHING)){
                ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
            }else
            if (user_type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            } else {
                ((MainActivity) getActivity()).getSupportActionBar().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_USERS_PROFILE:
               // params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                callApi(ApiCode.GET_USERS_PROFILE, params);
                break;
//
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_USERS_PROFILE:
                service.postDataVolley(ApiCode.GET_USERS_PROFILE,
                        BaseUrl.URL_GET_USERS_PROFILE, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//
            case ApiCode.GET_USERS_PROFILE:
                Log.e("users_profile ", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray array=jsonObject.getJSONArray("my_thumbnail");
                    if (array.length()>0){
                        String url=array.getJSONObject(0).getString("photo");
                        profile_url=BaseUrl.BASE_URL_MEDIA+url;
                        Picasso.get().load(profile_url)
                                .placeholder(R.drawable.profile).into(profile_image);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("user_data");

                    if (jsonArray.length() > 0) {
                        ArrayList<IDViewProfileModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<IDViewProfileModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        init_Recyclerview();
                    } else {
                        list.clear();
                        init_Recyclerview();
                          CommonMethods.showSuccessToast(getContext(),"No Users");
                    }
                }
                    break;
                }
        }


    private void refreshApi() {
        if (ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_USERS_PROFILE);
        else
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
    }
}

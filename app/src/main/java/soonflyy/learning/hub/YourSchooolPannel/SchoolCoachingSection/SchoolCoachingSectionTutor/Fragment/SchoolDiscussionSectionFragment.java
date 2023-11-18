package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SectionForDiscussionAdaper;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SectionForDiscussion;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolDiscussionSectionFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {

    RecyclerView rec_discussions;
    ImageView arrow_back_img;
    TextView tv_title;
    String type="";
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    SectionForDiscussionAdaper adapter;
    ArrayList<SectionForDiscussion> list=new ArrayList<>();
    String from,class_id,teacher_id,school_id,class_name;
    public SchoolDiscussionSectionFragment() {
        // Required empty public constructor
    }


    public static SchoolDiscussionSectionFragment newInstance(String param1, String param2) {
        SchoolDiscussionSectionFragment fragment = new SchoolDiscussionSectionFragment ( );
        Bundle args = new Bundle ( );
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_school_discussion_section, container, false);
        initview(view);
        getArgumentData();
        callRefreshApi();
        return view;
    }


    private void callRefreshApi() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION);
        }
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        class_id=getArguments().getString("class_id");
        teacher_id=getArguments().getString("teacher_id");
        school_id=getArguments().getString("school_id");
        class_name=getArguments().getString("class_name");

    }

    private void initview(View view) {
        rec_discussions = view.findViewById(R.id.rec_discussions);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        rec_discussions.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rec_discussions.setLayoutManager(linearLayoutManager);
        rec_discussions.setKeepScreenOn(true);
        list = new ArrayList<>();
        searchView=view.findViewById(R.id.search_view);
        CommonMethods.setSearchViewColor(getActivity(),searchView);
        tv_title = view.findViewById(R.id.tv_title);
        arrow_back_img = view.findViewById(R.id.btn_back);
        arrow_back_img.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter!=null){
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter!=null){
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                callRefreshApi();
            }
        });

    }

    private void initRecyclerview() {

        adapter = new SectionForDiscussionAdaper(getContext(), list, new SectionForDiscussionAdaper.OnSectionClickListener() {
            @Override
            public void onSectionClick(int position) {
                School_DiscustionChatDetailFragment fragment = new School_DiscustionChatDetailFragment();
               Bundle bundle =new Bundle();
               bundle.putString("teacher_id",teacher_id);
               bundle.putString("from",from);
               bundle.putString("class_id",class_id);
                bundle.putString("class_name",class_name);
               bundle.putString("section_id",list.get(position).getSection_id());
                bundle.putString("section_name",list.get(position).getSection_name());
               fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }

        });
        rec_discussions.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.arrow_back_img:
//                gotoBack();
//                break;
//        }
//
//    }

    private void gotoBack() {
        try {
            ((SchoolMainActivity)getActivity()).onBackPressed();
        }catch (Exception e){

        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment);//, ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }






    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION:
                params.put("teacher_id", teacher_id);
                params.put("class_id", class_id);
                callApi(ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION,
                        BaseUrl.URL_SCHOOL_GET_SECTION_FOR_DISCUSSION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SECTION_FOR_DISCUSSION);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_SECTION_FOR_DISCUSSION:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SectionForDiscussion> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SectionForDiscussion>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                    }else{
                        list.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
        }
    }


//
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                gotoBack();
                break;
//        }
        }
    }
}
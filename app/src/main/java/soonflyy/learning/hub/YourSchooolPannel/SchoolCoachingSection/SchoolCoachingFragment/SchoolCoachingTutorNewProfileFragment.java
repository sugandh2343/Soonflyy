package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter.SchoolCoachingNewProfileAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingNewProfileModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolCoachingTutorNewProfileFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {
    RecyclerView rec_school_profile;
    SwipeRefreshLayout swipe;

    TextView tv_student;
    View view_student,view_tutor;
    LinearLayout lin_student,lin_teacher;

    RelativeLayout rel_no_live, rel_showlist;
    SchoolCoachingNewProfileAdapter adapter;
    ArrayList<SchoolCoachingNewProfileModel> modellist=new ArrayList<>();
    String from,school_id,type="0";
    FirebaseAuth firebaseAuth;

    public SchoolCoachingTutorNewProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SchoolCoachingTutorNewProfileFragment newInstance(String param1, String param2) {
        SchoolCoachingTutorNewProfileFragment fragment = new SchoolCoachingTutorNewProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_school_coaching_tutor_new_profile, container, false);
        initView(view);
        getArgumendData();
        setPage(type);
        callApiRequest();
       // initRecyclerView();
        init_swipe_method();
        initControl();
        return view;
    }

    private void getArgumendData() {
        from=getArguments().getString("from");
        school_id=getArguments().getString("school_id");
    }

    private void initView(View view) {

        view_student=view.findViewById(R.id.view_school);
        view_tutor=view.findViewById(R.id.view_tutor);
        lin_student=view.findViewById(R.id.lin_school);
        lin_teacher=view.findViewById(R.id.lin_tutor);
        tv_student=view.findViewById(R.id.tv_school);
        firebaseAuth=FirebaseAuth.getInstance();

        swipe = view.findViewById(R.id.swipe);
        rec_school_profile = view.findViewById(R.id.rec_school_profile);
        //  rel_no_live= view.findViewById(R.id.rel_no_live);


    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callApiRequest();
                initControl();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void initRecyclerView() {

        rec_school_profile.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_school_profile.setLayoutManager(linearLayoutManager);
        rec_school_profile.setKeepScreenOn(true);


        adapter = new SchoolCoachingNewProfileAdapter(getContext(), modellist, new SchoolCoachingNewProfileAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onViewProfile_Click(int postion) {
                SchoolCoachingViewProfileFragment fragment = new SchoolCoachingViewProfileFragment ();
                Bundle bundle=new Bundle();

                bundle.putString("from",from);
                bundle.putString("id",modellist.get(postion).getId());
                bundle.putString("type",type);
                bundle.putString("name",modellist.get(postion).getName());
                fragment.setArguments(bundle);
              //   SwitchFragment (fragment);
                //comment by swati---- comment switchfragment method because its crash on this fragment
              ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);

            }


            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }


        });

//        if (modellist.size() < 0) {
//            rel_no_live.setVisibility(View.VISIBLE);
//            rel_showlist.setVisibility(View.GONE);
//
//        } else {
//            rel_no_live.setVisibility(View.GONE);
        rec_school_profile.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //}


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

    private void initControl() {
        tv_student.setText("Student");
        lin_teacher.setOnClickListener(this);
        lin_student.setOnClickListener(this);
//        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
//
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setPage(type);
        ((SchoolMainActivity)getActivity()).setActionBarTitle("New Profile");
    }

    ///---api call--//
    private void callApiRequest(){
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_NEW_PROFILES);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_GET_NEW_PROFILES:
//                params.put("school_id", school_id);
//                params.put("type", type);
//                callApi(ApiCode.SCHOOL_GET_NEW_PROFILES, params);
                if(type.equals("0")){

                }else if(type.equals("1")){
                    reference.child(firebaseAuth.getUid()).child("Tutors").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            modellist.clear();
                            for(DataSnapshot ds:snapshot.getChildren()){
                                if(ds.child("status").getValue(String.class).equals("Pending")){
                                    SchoolCoachingNewProfileModel schoolCoachingNewProfileModel=new SchoolCoachingNewProfileModel();
                                    schoolCoachingNewProfileModel.setId(ds.child("mobile").getValue(String.class));
                                    schoolCoachingNewProfileModel.setName(ds.child("name").getValue(String.class));
                                    modellist.add(schoolCoachingNewProfileModel);
                                }



                            }
                            initRecyclerView();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_NEW_PROFILES:
                service.postDataVolley(ApiCode.SCHOOL_GET_NEW_PROFILES,
                        BaseUrl.URL_SCHOOL_GET_NEW_PROFILES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NEW_PROFILES);
                Log.e("params", params.toString());
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_NEW_PROFILES:
                modellist.clear();
                initRecyclerView();
                Log.e("sc_profiles", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolCoachingNewProfileModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolCoachingNewProfileModel>>() {
                                        }.getType());
                        modellist.clear();

                        modellist.addAll(psearch);
                       initRecyclerView();

                    }else{
                        modellist.clear();
                      initRecyclerView();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_school:
                type = "0";
                setPage(type);
                //callRefreshApi();
                break;
            case R.id.lin_tutor:
                type = "1";
               setPage(type);
                //callRefreshApi();
                break;
        }
    }

    private void setPage(String value) {
        if (value.equals("0")){
            //for student
            view_tutor.setVisibility(View.GONE);
            view_student.setVisibility(View.VISIBLE);
        }else{
            //for tutor
            view_student.setVisibility(View.GONE);
            view_tutor.setVisibility(View.VISIBLE);
        }
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_NEW_PROFILES);
        }

    }
}
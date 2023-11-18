package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_MOBILE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import soonflyy.learning.hub.YourSchooolPannel.ScholProfileFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolHomeAdpter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolHomeModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolCoachingTutorHomeFragment extends Fragment implements VolleyResponseListener {
FloatingActionButton feb_school;
TextView tv_mobile;
    ImageView iv_setting;
SwipeRefreshLayout swipe;
    TextView tv_t_hometitle;
   RecyclerView rec_school;
SchoolHomeAdpter adapter;
ArrayList<SchoolHomeModel> clist=new ArrayList<>();
SessionManagement session_management;
String school_id;

    public SchoolCoachingTutorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_school_home_tutor, container, false);

        initView(view);
        session_management=new SessionManagement(getActivity());
        tv_mobile.setText("+91-"+session_management.getString(SCHOOL_T_MOBILE));
        callSchoolApi();
      //  initRecyclerview();
        init_swipe_method();
        //((SchoolLoginMainActivity)getActivity ()).makeBottom_gone ("gone");
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("type","0");
                bundle.putString("from",SCHOOL_TUTOR);
                bundle.putString("id",new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID));
                ScholProfileFragment fragment=new ScholProfileFragment();
                fragment.setArguments(bundle);
                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }
        });
        return  view;
    }


    private void initView(View view) {
        iv_setting=view.findViewById(R.id.iv_setting);
        swipe = view.findViewById(R.id.swipe);
        tv_t_hometitle = view.findViewById(R.id.tv_t_hometitle);
        rec_school = view.findViewById(R.id.rec_school) ;
        feb_school= view.findViewById(R.id.feb_school) ;
        tv_mobile=view.findViewById(R.id.tv_mob_no);
        rec_school.hasFixedSize();
        rec_school.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_school.setLayoutManager(layoutManager);
        rec_school.setKeepScreenOn(true);


    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callSchoolApi();
             //   initRecyclerview();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }



    private void initRecyclerview() {

//        clist = new ArrayList<>();
//
//       clist.add(new SchoolHomeModel());
//        clist.add(new SchoolHomeModel());
//        clist.add(new SchoolHomeModel());
        Log.e("RecycleView",""+clist.size());

        adapter= new SchoolHomeAdpter(getActivity(), clist, new SchoolHomeAdpter.OnSchoolClickListener() {
            @Override
            public void onItemClick(int postion) {
                if (clist.get(postion).getBlock_status().equals("Approved")) {
                    SchoolHomeModel model = clist.get(postion);
                    SchoolTutorClassRoomFragment fragment = new SchoolTutorClassRoomFragment();
                    Bundle bundle = new Bundle();
                    Log.e("SchoolTutorIDDDDD",model.getSchool_id());
                    bundle.putString("school_id", model.getSchool_id());
                    bundle.putString("school_name", model.getSchool_name());
                    fragment.setArguments(bundle);
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                }else{
                    showBlockAlert();
                }
                //SwitchFragment (fragment);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }
        });
        rec_school.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    private void showBlockAlert() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Alert")
                .setMessage("Access denied by school.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();
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
        ((SchoolMainActivity)getActivity()).showHideHomeActionBar(false);
    }

    ///---api call--//
    private void callSchoolApi(){
        if (CommonMethods.checkInternetConnection(getActivity())) {

            sendRequest(ApiCode.GET_SCHOOL_BY_TUTOR);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_SCHOOL_BY_TUTOR:
                //params.put("mobile", session_management.getString(SCHOOL_T_MOBILE));
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clist.clear();
                        Boolean loginValue=false;
                        String loginEmail="";
                        String loginPassword="";
                        String schoolId="";
                        String name="";
                        String mobile="";
                        String email="";
                        String state="";
                        String city="";
                        String image="";
                        String pincode="";
                        String uniqueCode="";
                        Log.e("Session",session_management.getString(SCHOOL_T_MOBILE));
                        for(DataSnapshot ds:snapshot.getChildren()){

                            if(ds.child("type").exists()){
                                Log.e("LSNMNH",""+ds.child("type").getValue(String.class));

                                if(ds.child("type").getValue(String.class).equals("school")){
                                    if(ds.child("Tutors").child(session_management.getString(SCHOOL_T_MOBILE)).exists()){
                                        Log.e("Status",ds.child("Tutors").child(session_management.getString(SCHOOL_T_MOBILE)).child("status").getValue(String.class));
                                       SchoolHomeModel schoolHomeModel=new SchoolHomeModel();
                                       schoolHomeModel.setSchool_name(ds.child("name").getValue(String.class));
                                       schoolHomeModel.setSchool_id(ds.child("SchoolId").getValue(String.class));
                                       schoolHomeModel.setBlock_status(ds.child("Tutors").child(session_management.getString(SCHOOL_T_MOBILE)).child("status").getValue(String.class));
                                       schoolHomeModel.setImage("");
                                       clist.add(schoolHomeModel);

                                    }
                                }
                            }

                        }
                        initRecyclerview();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = (VolleyResponseListener) this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_SCHOOL_BY_TUTOR:
                service.postDataVolley(ApiCode.GET_SCHOOL_BY_TUTOR,
                        BaseUrl.URL_GET_SCHOOL_BY_TUTOR, params);
                Log.e("api", BaseUrl.URL_GET_SCHOOL_BY_TUTOR);
                Log.e("params", params.toString());
                break;


        }
    }


    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.GET_SCHOOL_BY_TUTOR:
                Log.e("sc_BY_MOBILE", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolHomeModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolHomeModel>>() {
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
////
                break;

        }
    }

}
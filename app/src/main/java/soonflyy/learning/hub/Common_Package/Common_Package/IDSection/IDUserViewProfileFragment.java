package soonflyy.learning.hub.Common_Package.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity.SCHOOL_ID_SECTION_USER_TYPE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.IDActivity_Achievement_Adapter;
import soonflyy.learning.hub.Common_Package.Adapters.ID_Other_Adapter;
import soonflyy.learning.hub.Common_Package.Models.IDAchievementModel;
import soonflyy.learning.hub.Common_Package.Models.IDActivityModel;
import soonflyy.learning.hub.Common_Package.Models.IDOtherModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;


public class IDUserViewProfileFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    View line_activity,line_achievement,line_other;
    LinearLayout lin_activity,lin_achievement,lin_other;
    TextView tv_education,tv_interest,tv_workplace,tv_work,
            tv_address,tv_userName,tv_pageTitle,tv_message;
    CircleImageView iv_profile;
    ImageView iv_back_btn,img_edit;
    CardView cv_edi;
    TextView dailog_delete;
    TextView tv_title;
    RecyclerView rec_tab;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rel_edit;
    IDActivity_Achievement_Adapter ac_ach_adapter;
    ID_Other_Adapter otherAdapter;
    ArrayList<IDAchievementModel>achievementList=new ArrayList<>();
    ArrayList<IDOtherModel>otherList=new ArrayList<>();
    ArrayList<IDActivityModel>activityList=new ArrayList<>();

    String user_type,view_type,user_id,user_name,profile_url,isSchool;


    public IDUserViewProfileFragment() {
        // Required empty public constructor
    }

    public static IDUserViewProfileFragment newInstance(String param1, String param2) {
        IDUserViewProfileFragment fragment = new IDUserViewProfileFragment();

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
        View view= inflater.inflate(R.layout.fragment_i_d_user_view_profile, container, false);
        initView(view);
        getArgumentData();
        refreshApi();
        clickListener();
        initSwipe();
        //showActivityOrAchievement("activity");
        line_activity.setVisibility(View.VISIBLE);
        return  view;
    }

    private void initSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshApi();
            }
        });
    }

    private void getArgumentData() {
        user_type=getArguments().getString("user_type");
        user_id=getArguments().getString("user_id");
        view_type=getArguments().getString("type");
        if (view_type.equals("view")){
            isSchool=getArguments().getString("isSchoolUser");
            rel_edit.setVisibility(View.GONE);
            tv_message.setVisibility(View.VISIBLE);

        }else{
            rel_edit.setVisibility(View.VISIBLE);
            tv_message.setVisibility(View.GONE);
        }

    }


    private void clickListener() {
        lin_activity.setOnClickListener(this);
        lin_achievement.setOnClickListener(this);
        lin_other.setOnClickListener(this);
        iv_back_btn.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        lin_activity.setOnClickListener(this);
        cv_edi.setOnClickListener(this);
    }

    private void initView(View view) {
        swipeRefreshLayout=view.findViewById(R.id.swipe);
        line_activity=view.findViewById(R.id.line_activity);
        line_achievement=view.findViewById(R.id.line_achievement);
        line_other=view.findViewById(R.id.line_other);

        lin_activity=view.findViewById(R.id.lin_activity);
        lin_achievement=view.findViewById(R.id.lin_achievement);
        lin_other=view.findViewById(R.id.lin_other);

        tv_education=view.findViewById(R.id.tv_education);
        tv_interest=view.findViewById(R.id.tv_interest);
        tv_workplace=view.findViewById(R.id.tv_workplace);
        tv_work=view.findViewById(R.id.tv_work);
        tv_address=view.findViewById(R.id.tv_address);
        tv_message=view.findViewById(R.id.tv_message);
        tv_userName=view.findViewById(R.id.tv_user_name);
        tv_pageTitle=view.findViewById(R.id.tv_title_name);

        iv_profile=view.findViewById(R.id.iv_profile);
        iv_back_btn=view.findViewById(R.id.iv_back);
        rec_tab=view.findViewById(R.id.rec_tab);

        rec_tab.setLayoutManager(new LinearLayoutManager(getContext()));
        rel_edit=view.findViewById(R.id.rel_edit);
        cv_edi=view.findViewById(R.id.cv_edi);
        dailog_delete=view.findViewById(R.id.dailog_delete);
        img_edit=view.findViewById(R.id.img_edit);
        rel_edit.setOnClickListener( this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_activity:
                line_activity.setVisibility(View.VISIBLE);
                line_achievement.setVisibility(View.GONE);
                line_other.setVisibility(View.GONE);
                showActivityOrAchievement("activity");
                break;
            case R.id.lin_achievement:
                line_activity.setVisibility(View.GONE);
                line_achievement.setVisibility(View.VISIBLE);
                line_other.setVisibility(View.GONE);
                showActivityOrAchievement("achievement");
                break;
            case R.id.lin_other:
                showOther();
                break;
            case R.id.iv_back:
                gotoBack();
                break;
            case R.id.tv_message:
                //code for message
               goForChat();

                break;
            case R.id.rel_edit:
              EditProfile();
                break;
            case R.id.cv_edi:
                gotoManageIDSection();
                break;
        }
    }

    private void goForChat() {
        IDUserMessageFragment fragment=new IDUserMessageFragment();
        Bundle bundle=new Bundle();
        bundle.putString("profile_image",profile_url);
        bundle.putString("user_name",user_name);
        bundle.putString("to_id",user_id);
        bundle.putString("user_type",user_type);
        if (isSchool.equals("1")){
            bundle.putString("toType","0");
        }else{
            bundle.putString("toType","1");
        }

       // bundle.putString("from_id",new SessionManagement(getActivity()).getString(USER_ID));
        bundle.putString("from_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
        fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher")) {
            ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        }else{
            ((MainActivity) getActivity()).SwitchFragment(fragment);
        }
    }

    private void EditProfile() {

        if(cv_edi.getVisibility ()==View.VISIBLE)
        {
            cv_edi.setVisibility (View.GONE);
            //gotoManageIDSection();
        }
        else
        {
            cv_edi.setVisibility (View.VISIBLE);

        }


        dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoManageIDSection();
                
            }
        });
    }

    private void gotoManageIDSection() {
            IDSectionFragment fragment=new IDSectionFragment();
            Bundle bundle=new Bundle();
            bundle.putString("type","update");
        bundle.putString("user_type",user_type);
            fragment.setArguments(bundle);
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
        }else if (user_type.equals("teacher")){
            ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
        }else{
            ((MainActivity)getActivity()).SwitchFragment(fragment);
        }
    }

    private void gotoBack() {
        if (user_type.equals(SCHOOL_COACHING)){
            ((SchoolMainActivity)getActivity()).onBackPressed();
        }else  if (user_type.equals("teacher")){
            ((TeacherMainActivity)getActivity()).onBackPressed();
        }else{
            ((MainActivity)getActivity()).onBackPressed();
        }

    }

    private void showOther() {
        line_activity.setVisibility(View.GONE);
        line_achievement.setVisibility(View.GONE);
        line_other.setVisibility(View.VISIBLE);
        otherAdapter=new ID_Other_Adapter(getContext(), otherList, new ID_Other_Adapter.OnLinkClickListener() {
            @Override
            public void onLinkClick(int position, String link) {

            }
        });
        rec_tab.setAdapter(otherAdapter);


    }


    private void showActivityOrAchievement(String type) {
       ac_ach_adapter=new IDActivity_Achievement_Adapter(getContext(),activityList,achievementList,type);
       rec_tab.setAdapter(ac_ach_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (user_type.equals(SCHOOL_COACHING)){
                ((SchoolMainActivity) getActivity()).showHideHomeActionBar(false);
            }else if (user_type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            } else {
                ((MainActivity) getActivity()).getSupportActionBar().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(int request) {
        HashMap<String, Object> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_ID_USER_DETAILS:
                params.put("user_id", user_id);

                if (view_type.equals("view")){
                    if (isSchool.equals("1")){
                        params.put("type","0");
                    }else{
                        params.put("type","1");
                    }
                }else {

                    setTypeParameter(params);
                }
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Id");
                reference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            profile_url=snapshot.child("photo").getValue(String.class);
                            Picasso.get().load(profile_url)
                                    .placeholder(R.drawable.profile).into(iv_profile);

                            user_name=snapshot.child("name").getValue(String.class);
                            if (view_type.equals("view")) {
                                tv_pageTitle.setText(user_name);
                            }

                            tv_userName.setText(user_name);
                            tv_education.setText(snapshot.child("education").getValue(String.class));
                            tv_interest.setText(snapshot.child("interest").getValue(String.class));
                            tv_workplace.setText(snapshot.child("interest").getValue(String.class));
                            tv_work.setText(snapshot.child("workplace").getValue(String.class));
                            tv_address.setText(snapshot.child("address").getValue(String.class));

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                callApi(ApiCode.GET_ID_USER_DETAILS, params);
                break;
//
        }
    }
    private void setTypeParameter(HashMap<String, Object> params) {
        if (user_type.equals(SCHOOL_COACHING)){
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
            case ApiCode.GET_ID_USER_DETAILS:
                service.postDataVolley(ApiCode.GET_ID_USER_DETAILS,
                        BaseUrl.URL_GET_ID_USER_DETAILS, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//
            case ApiCode.GET_ID_USER_DETAILS:
                Log.e("users_details ", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray user_array=jsonObject.getJSONArray("user_data");
                    setUserData(user_array.getJSONObject(0));
                    JSONArray activity_array=jsonObject.getJSONArray("activity");
                    if (activity_array.length()>0){
                        ArrayList<IDActivityModel> psearch = new Gson().
                                fromJson(activity_array.toString(),
                                        new TypeToken<List<IDActivityModel>>() {
                                        }.getType());
                        activityList.clear();
                        activityList.addAll(psearch);
                        //init_Recyclerview();
                    }else{
                        activityList.clear();
                    }
                    JSONArray achievement_array=jsonObject.getJSONArray("achievement");
                    if (achievement_array.length()>0){
                        ArrayList<IDAchievementModel> psearch = new Gson().
                                fromJson(achievement_array.toString(),
                                        new TypeToken<List<IDAchievementModel>>() {
                                        }.getType());
                        achievementList.clear();
                        achievementList.addAll(psearch);
                        //init_Recyclerview();
                    }else{
                        achievementList.clear();
                    }
                    JSONArray other_array=jsonObject.getJSONArray("others");
                    if (other_array.length()>0){
                        ArrayList<IDOtherModel> psearch = new Gson().
                                fromJson(other_array.toString(),
                                        new TypeToken<List<IDOtherModel>>() {
                                        }.getType());
                        otherList.clear();
                        otherList.addAll(psearch);
                        //init_Recyclerview();
                    }else{
                        otherList.clear();
                    }

                }
                showActivityOrAchievement("activity");
                break;
        }
    }

    private void setUserData(JSONObject jsonObject) {
        try {
            profile_url=BaseUrl.BASE_URL_MEDIA+jsonObject.getString("photo");

            user_name=jsonObject.getString("name");
            if (view_type.equals("view")) {
                tv_pageTitle.setText(user_name);
            }
            Picasso.get().load(profile_url)
                    .placeholder(R.drawable.profile).into(iv_profile);
            tv_userName.setText(user_name);
            tv_education.setText(jsonObject.getString("education"));
            tv_interest.setText(jsonObject.getString("interest"));
            tv_workplace.setText(jsonObject.getString("workplace"));
            tv_work.setText(jsonObject.getString("work"));
            tv_address.setText(jsonObject.getString("address"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void refreshApi() {
        if (ConnectivityReceiver.isConnected())
            sendRequest(ApiCode.GET_ID_USER_DETAILS);
        else
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
    }
}
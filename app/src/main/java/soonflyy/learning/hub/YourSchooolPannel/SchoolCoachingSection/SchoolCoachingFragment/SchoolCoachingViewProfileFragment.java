package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SchoolCoachingViewProfileFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    TextView tv_profile_text,tv_name,tv_mobile,
            tv_email,tv_address,tv_father,tv_dob,tvClass;
    ImageView iv_accept,iv_cancel,iv_block;
    LinearLayout linClass,linAddress;

    String action_type;
    String from,id,name;
    String status="",type="";
    SessionManagement sessionManagement;
    FirebaseAuth firebaseAuth;

    public SchoolCoachingViewProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SchoolCoachingViewProfileFragment newInstance(String param1, String param2) {
        SchoolCoachingViewProfileFragment fragment = new SchoolCoachingViewProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_school_coaching_view_profile, container, false);
        sessionManagement=new SessionManagement(getActivity());
        initView(view);
        getArgumentData();
        callApiRequest();
        return view;
    }

    private void initView(View view) {
        linClass=view.findViewById(R.id.lin_classes);
        linAddress=view.findViewById(R.id.linin_address);
        tvClass=view.findViewById(R.id.tv_class);

        tv_profile_text=view.findViewById(R.id.tv_profile_name);
        tv_name=view.findViewById(R.id.assign_tv_name);
        tv_mobile=view.findViewById(R.id.tv_q2);
        tv_email=view.findViewById(R.id.tv_emailid);
        tv_father=view.findViewById(R.id.tv_fathername);
        tv_dob=view.findViewById(R.id.tv_dob);
        tv_address=view.findViewById(R.id.tv_address);

        iv_accept=view.findViewById(R.id.iv_right);
        iv_block=view.findViewById(R.id.iv_block);
        iv_cancel=view.findViewById(R.id.iv_close);

        iv_accept.setOnClickListener(this);
        iv_block.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();


    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        id=getArguments().getString("id");
        name=getArguments().getString("name");
        type=getArguments().getString("type");
        tv_profile_text.setText(name.substring(0,1).toUpperCase());
        tv_name.setText(name);
        if (type.equals("0")){
            linClass.setVisibility(View.VISIBLE);
        }else{
            linClass.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_right:
                //code fro accept
                action_type="4";
                showAlert("accept");

                break;
            case R.id.iv_close:
                //code for cancel
                action_type="2";
                showAlert("remove");
                break;
            case R.id.iv_block:
                //code for block
                if (status.equals("3")){
                    action_type="1";
                    showAlert("unblock");
                }else {
                    action_type = "3";
                    showAlert("block");
                }
                break;
//            case R.id.iv_block:
//                //code for unblock
//                action_type="1";
//                break;
        }

    }
    private void showAlert(String type){


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert")
                .setCancelable(false)
                .setMessage("Are you sure to "+type+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethods.checkInternetConnection(getActivity())){
                            sendRequest(ApiCode.SCHOOL_CHANGE_USER_STATUS);
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    ///---api call--//
    private void callApiRequest(){
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES);
        }
    }

    private void sendRequest(int request) {

        HashMap<String, Object> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES:
//                params.put("id", id);
//                params.put("type", type);
//                callApi(ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES, params);
//                reference.child(firebaseAuth.getUid()).
                reference.child(firebaseAuth.getUid()).child("Tutors").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tv_name.setText(snapshot.child("name").getValue(String.class));
                        tv_mobile.setText(snapshot.child("mobile").getValue(String.class));
                        tv_email.setText(snapshot.child("email").getValue(String.class));
                        tv_father.setText(snapshot.child("father_name").getValue(String.class));
                        tv_address.setText(snapshot.child("address").getValue(String.class));
                        tv_dob.setText(snapshot.child("dob").getValue(String.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.SCHOOL_CHANGE_USER_STATUS:
//                params.put("id", id);
//                params.put("status", action_type);
//                params.put("type",type);
//                if (type.equals("1")){
//                    params.put("school_id",sessionManagement.getString(SCHOOL_ID));
//                }
//
//                callApi(ApiCode.SCHOOL_CHANGE_USER_STATUS, params);
                if(action_type.equals("4")){
                    params.put("status","Approved");
                    reference.child(firebaseAuth.getUid()).child("Tutors").child(id).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            iv_accept.setVisibility(View.VISIBLE);
                            iv_block.setVisibility(View.GONE);
                            CommonMethods.showSuccessToast(getActivity(),"Request Accepted");

                        }
                    });
                }else if(action_type.equals("2")){
                    iv_accept.setVisibility(View.GONE);
                    iv_block.setVisibility(View.GONE);
                    reference.child(firebaseAuth.getUid()).child("Tutors").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CommonMethods.showSuccessToast(getActivity(),"Request Denied");
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
            case ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES:
                service.postDataVolley(ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES,
                        BaseUrl.URL_SCHOOL_GET_SINGLE_NEW_PROFILES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SINGLE_NEW_PROFILES);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_CHANGE_USER_STATUS:
                service.postDataVolley(ApiCode.SCHOOL_CHANGE_USER_STATUS,
                        BaseUrl.URL_SCHOOL_CHANGE_USER_STATUS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_CHANGE_USER_STATUS);
                Log.e("params", params.toString());
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_SINGLE_NEW_PROFILES:
                Log.e("sc_profiles", response.toString());

                if (jsonObject.getBoolean("response")) {
                    tv_name.setText(jsonObject.getJSONObject("data").getString("name"));
                    tv_mobile.setText(jsonObject.getJSONObject("data").getString("mobile"));
                    tv_email.setText(jsonObject.getJSONObject("data").getString("email"));
                    tv_father.setText(jsonObject.getJSONObject("data").getString("father_name"));
                    tv_address.setText(jsonObject.getJSONObject("data").getString("address"));
                    tv_dob.setText(jsonObject.getJSONObject("data").getString("dob"));
                    if (type.equals("0")){
                        String claassName=jsonObject.getJSONObject("data").getString("class");
                        tvClass.setText(claassName);
                        linClass.setVisibility(View.VISIBLE);
                        linClass.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_grey));
                        linAddress.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
                    }else{
                        linClass.setVisibility(View.GONE);
                        //linClass.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_gray));
                        linAddress.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.light_grey));
                    }
                   status=jsonObject.getJSONObject("data").getString("status");

                    showStatus(status);

                }
//                              }
////
                break;
            case ApiCode.SCHOOL_CHANGE_USER_STATUS:
                Log.e("sc_status", response.toString());
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                    showStatus(action_type);
                    status=action_type;
                }

                break;

        }
    }

    private void showStatus(String status) {

        switch (status){
            case "1":
                //show alll
                iv_accept.setVisibility(View.VISIBLE);
                iv_block.setVisibility(View.VISIBLE);
                iv_cancel.setVisibility(View.VISIBLE);
                break;
            case "2":
            case "4":
                //for decline/cancel
                iv_accept.setVisibility(View.GONE);
                iv_block.setVisibility(View.GONE);
                iv_cancel.setVisibility(View.GONE);
                break;
            case "3":
                //block
                iv_accept.setVisibility(View.GONE);
                iv_block.setVisibility(View.GONE);
                iv_cancel.setVisibility(View.GONE);
                break;
//            case "4":
//                //accept
//                iv_accept.setVisibility(View.GONE);
//                iv_block.setVisibility(View.GONE);
//                iv_cancel.setVisibility(View.GONE);
//                break;
        }
    }
}
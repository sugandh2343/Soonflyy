package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_MOBILE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.AllSubjectAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllSubjectModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolAllSubjectsFragment extends Fragment implements VolleyResponseListener {

    SwipeRefreshLayout swipe;
    ImageView arrow_back_img;
    TextView tv_title;
    private FloatingActionButton feb_addsubject;
    RecyclerView rec_allsubject;
    AllSubjectAdapter subjectAdapter;
    ArrayList<AllSubjectModel> list = new ArrayList<>();
    String get_fragment = "";
    String class_id, school_id, section_id, pageTitle = "", subject_name, subject_id, itutor_id,student_type;
    FirebaseAuth firebaseAuth;

    public SchoolAllSubjectsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_subjects, container, false);
        initView(view);
        getArgumentData();
        if (CommonMethods.checkInternetConnection(getActivity())) {
            if (get_fragment.equals(SCHOOL_TUTOR)){
                sendRequest(ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR);
            }else {
                sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
            }
        }
        // initRecyclerview();
        init_swipe_method();

        return view;
    }

    private void getArgumentData() {
        get_fragment = getArguments().getString("from");
        if (get_fragment.equals(INDEPENDENT_TUTOR)) {
            itutor_id = getArguments().getString("itutor_id");
            pageTitle="Subjects";
            feb_addsubject.setVisibility(View.VISIBLE);
        } else if (get_fragment.equals(SCHOOL_STUDENT)){
            student_type=getArguments().getString("student_type");
            if (student_type.equals("school")) {
                section_id = getArguments().getString("section_id");
                class_id = getArguments().getString("class_id");
                school_id = getArguments().getString("school_id");
                pageTitle = getArguments().getString("school_name");
            }else if (student_type.equals("itutor")){
                itutor_id = getArguments().getString("itutor_id");
                pageTitle="Subjects";
            }

            feb_addsubject.setVisibility(View.GONE);
        } else {
            section_id = getArguments().getString("section_id");
            class_id = getArguments().getString("class_id");
            school_id = getArguments().getString("school_id");
            pageTitle = getArguments().getString("section_name");

            if (get_fragment.equals(SCHOOL_TUTOR)) {
                //manage for school tutor
                feb_addsubject.setVisibility(View.GONE);
            } else if (get_fragment.equals(SCHOOL_COACHING)) {
                //manage for school coaching
                feb_addsubject.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
        feb_addsubject = view.findViewById(R.id.feb_addsubject);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("Class Section with class");
        arrow_back_img = view.findViewById(R.id.arrow_back_img);
        rec_allsubject = view.findViewById(R.id.rec_allsubject);
        rec_allsubject.hasFixedSize();
        rec_allsubject.setHasFixedSize(true);
        firebaseAuth=FirebaseAuth.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_allsubject.setLayoutManager(layoutManager);
        rec_allsubject.setKeepScreenOn(true);
        feb_addsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSubjectDailoge("add");
            }
        });

    }

    private void ShowSubjectDailoge(String type) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_school_addsubject);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        TextView tv_sub_title = dialog.findViewById(R.id.tv_sub_title);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        Button btn_upload = dialog.findViewById(R.id.btn_save);
        if (type.equals("update")) {
            tv_sub_title.setText("Update Subject");
            et_name.setText(subject_name);
            btn_upload.setText("Update");

        }


        dialog.show();

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    et_name.setError("Enter subject name");
                    et_name.requestFocus();
                } else {
                    subject_name = et_name.getText().toString().trim();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        if (type.equals("add"))
                            sendRequest(ApiCode.SCHOOL_ADD_SUBJECT);
                        else
                            sendRequest(ApiCode.SCHOOL_UPDATE_SUBJECT);
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);


    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    if (get_fragment.equals(SCHOOL_TUTOR)){
                        sendRequest(ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR);
                    }else {
                        sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                    }
                }
                //initRecyclerview();

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
//        list.add(new AllSubjectModel("Subject-01","Mathematics"));
//        list.add(new AllSubjectModel("Subject-02","Physics"));
//        list.add(new AllSubjectModel("Subject-03","Chemistry"));
//        list.add(new AllSubjectModel("Subject-04","English"));

        subjectAdapter = new AllSubjectAdapter("all_subject", getActivity(), get_fragment,list, new AllSubjectAdapter.OnSelectSubjectClickListener() {
            @Override
            public void onItemClick(int postion) {
                AllSubjectModel model = list.get(postion);
                if (get_fragment.equals("ys_sc")) {
//                    SchoolCoachingTeacherNameFragment fragment_sc_teachername = new SchoolCoachingTeacherNameFragment();
//
//                    // SwitchFragment (fragment_sc_teachername);
//                    //comment by swati---- comment switchfragment method because its crash on this fragment
//                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment_sc_teachername);

                } else {
                    SchoolTutorAllChapterFragment fragment = new SchoolTutorAllChapterFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("subject_id", model.getSubject_id());
                    bundle.putString("class_id", class_id);
                    bundle.putString("section_id", section_id);
                    bundle.putString("school_id", school_id);
                    bundle.putString("subject_name", model.getSubject_name());

                    bundle.putString("from", get_fragment);
                    if (get_fragment.equals(SCHOOL_STUDENT)) {
                        bundle.putString("itutor_id",itutor_id);
                        bundle.putString("student_type", student_type);
                    }
                    fragment.setArguments(bundle);
                    // SwitchFragment(fragment);
                    //comment by swati---- comment switchfragment method because its crash on this fragment
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);


                }


            }

            @Override
            public void onDelete(int position) {
                subject_id=list.get(position).getSubject_id();
                showDeleteDailog();
//                if (get_fragment.equals(INDEPENDENT_TUTOR)){
//                    if (CommonMethods.checkInternetConnection(getActivity())){
//                        sendRequest(ApiCode.SCHOOL_DELETE_SUBJECT);
//                    }
//                }


            }

            @Override
            public void onEdit(int position) {
                subject_id=list.get(position).getSubject_id();
                subject_name=list.get(position).getSubject_name();
                ShowSubjectDailoge("update");
            }
        });
        rec_allsubject.setAdapter(subjectAdapter);
        subjectAdapter.notifyDataSetChanged();


    }
    private void showDeleteDailog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethods.checkInternetConnection(getActivity())){
                            sendRequest(ApiCode.SCHOOL_DELETE_SUBJECT);
                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setCancelable(false)
                .show();
    }
    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).setActionBarTitle(pageTitle);

    }

    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_GET_SUBJECT:
                if (get_fragment.equals(SCHOOL_COACHING)||get_fragment.equals(SCHOOL_TUTOR)
                ||(get_fragment.equals(SCHOOL_STUDENT) && student_type.equals("school"))) {
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("school_id", school_id);
                    params.put("type", "0");
                    reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section")
                            .child(section_id).child("Subjects").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.clear();
                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        AllSubjectModel allSubjectModel=new AllSubjectModel();
                                        allSubjectModel.setSubject_name(ds.child("subject_name").getValue(String.class));
                                        allSubjectModel.setSubject_id(ds.child("subject_name").getValue(String.class));
                                        list.add(allSubjectModel);
                                    }
                                    initRecyclerview();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                } else if (get_fragment.equals(INDEPENDENT_TUTOR)
                        ||(get_fragment.equals(SCHOOL_STUDENT) && student_type.equals("itutor"))) {
                    params.put("teacher_id", itutor_id);
                    params.put("type", "1");
                    reference.child(firebaseAuth.getUid()).child("Subjects").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot ds:snapshot.getChildren()){
                                AllSubjectModel allSubjectModel=new AllSubjectModel();
                                allSubjectModel.setSubject_name(ds.child("subject_name").getValue(String.class));
                                allSubjectModel.setSubject_id(ds.child("subject_name").getValue(String.class));
                                list.add(allSubjectModel);
                            }
                            initRecyclerview();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//                callApi(ApiCode.SCHOOL_GET_SUBJECT, params);
                break;
            case ApiCode.SCHOOL_ADD_SUBJECT:
                if (get_fragment.equals(SCHOOL_COACHING)) {
                    params.put("school_id", school_id);
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("subject_name", subject_name);
                    params.put("type", "0");
                    Log.e("Check","1"+firebaseAuth.getUid());
                    Log.e("Check","2"+class_id);
                    Log.e("Check","3"+section_id);

                    reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section")
                            .child(section_id).child("Subjects").child(subject_name).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        CommonMethods.showSuccessToast(getActivity(),"Subject Already added");
                                    }else{
                                        reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section")
                                                .child(section_id).child("Subjects").child(subject_name).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        CommonMethods.showSuccessToast(getActivity(),"Subject Added Successfully");
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                } else if (get_fragment.equals(INDEPENDENT_TUTOR)) {
                    params.put("teacher_id", itutor_id);
                    params.put("subject_name", subject_name);
                    params.put("type", "1");
                    reference.child(firebaseAuth.getUid()).child("Subjects").child(subject_name).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        CommonMethods.showSuccessToast(getActivity(),"Subject Already added");
                                    }else{
                                        reference.child(firebaseAuth.getUid()).child("Subjects").child(subject_name).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        CommonMethods.showSuccessToast(getActivity(),"Subject Added Successfully");
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
//                callApi(ApiCode.SCHOOL_ADD_SUBJECT, params);
                break;
            case ApiCode.SCHOOL_UPDATE_SUBJECT:
                if (get_fragment.equals(SCHOOL_COACHING)) {
                    params.put("school_id", school_id);
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("subject_id", subject_id);
                    params.put("subject_name", subject_name);
                    params.put("type", "0");
                } else if (get_fragment.equals(INDEPENDENT_TUTOR)) {
                    params.put("teacher_id", itutor_id);
                    params.put("subject_name", subject_name);
                    params.put("subject_id", subject_id);
                    params.put("type", "1");
                }
                callApi(ApiCode.SCHOOL_UPDATE_SUBJECT, params);
                break;
            case ApiCode.SCHOOL_DELETE_SUBJECT:
                if (get_fragment.equals(SCHOOL_COACHING)) {
                    params.put("section_id", section_id);
                    params.put("class_id", class_id);
                    params.put("subject_id", subject_id);
                    params.put("type", "0");
                    params.put("school_id", school_id);

                } else if (get_fragment.equals(INDEPENDENT_TUTOR)) {
                    params.put("teacher_id", itutor_id);
                    params.put("subject_id", subject_id);
                    params.put("type", "1");
                }
                callApi(ApiCode.SCHOOL_DELETE_SUBJECT, params);
                break;
            case ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR:
                SessionManagement session_management=new SessionManagement(getActivity());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
//                                if(ds.child("SchoolId").getValue(String.class)!=null){
//                                    Log.e("mgfhgfuyjfuyjmfuky",ds.child("SchoolId").getValue(String.class));
//                                }
//                                Log.e("mgfhgfuyjfuyjmfuky","ghcghcgh:::::::"+school_id);

                            if(ds.child("SchoolId").getValue(String.class)!=null && ds.child("SchoolId").getValue(String.class).equals(school_id)){

                                for(DataSnapshot ds1:ds.child("Tutors").child(session_management.getString(SCHOOL_T_MOBILE)).child("Assigned").child("Class").child(class_id)
                                        .child("Section").child(section_id).child("Subject").getChildren()){
                                    AllSubjectModel allClassesModel=new AllSubjectModel();
                                    allClassesModel.setSubject_id(ds1.getKey());
                                    allClassesModel.setSubject_name(ds1.getKey());
                                    list.add(allClassesModel);
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

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_GET_SUBJECT,
                        BaseUrl.URL_SCHOOL_GET_SUBJECT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SUBJECT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_ADD_SUBJECT,
                        BaseUrl.URL_SCHOOL_ADD_SUBJECT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_SUBJECT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_SUBJECT,
                        BaseUrl.URL_SCHOOL_UPDATE_SUBJECT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_SUBJECT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_SUBJECT,
                        BaseUrl.URL_SCHOOL_DELETE_SUBJECT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_SUBJECT);
                Log.e("params", params.toString());
                break;
            case ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR:
                service.postDataVolley(ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR,
                        BaseUrl.URL_GET_SUBJECT_BY_SCHOOL_TUTOR, params);
                Log.e("api", BaseUrl.URL_GET_SUBJECT_BY_SCHOOL_TUTOR);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_SUBJECT:
            case ApiCode.GET_SUBJECT_BY_SCHOOL_TUTOR:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllSubjectModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllSubjectModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                    } else {
                        list.clear();
                        initRecyclerview();
                    }
                } else {
                    list.clear();
                    initRecyclerview();
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_SUBJECT:
                Log.e("sc_add_sec", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_SUBJECT:
                Log.e("sc_up_sec", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_SUBJECT:
                Log.e("delete_sec", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));

                break;
        }
    }

}
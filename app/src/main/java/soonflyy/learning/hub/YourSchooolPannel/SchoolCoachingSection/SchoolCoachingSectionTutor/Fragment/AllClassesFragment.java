package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_NAME;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.AllClassesAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AllClassesFragment extends Fragment implements VolleyResponseListener {
    SwipeRefreshLayout swipe;
    RecyclerView rec_class;
    FirebaseAuth firebaseAuth;

    private FloatingActionButton feb_addclass;
    //    ImageView arrow_back_img;
//    TextView tv_title;
    AllClassesAdapter classesAdapter;
    ArrayList<AllClassesModel> classlist = new ArrayList<>();
    String class_name, class_id, school_id, from;
    SessionManagement session_management;

    public AllClassesFragment() {
        // Required empty public constructor
    }


    public static AllClassesFragment newInstance(String param1, String param2) {
        AllClassesFragment fragment = new AllClassesFragment();

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
        View view = inflater.inflate(R.layout.fragment_all_classes, container, false);
        initView(view);

        session_management = new SessionManagement(getActivity());
        getArgumentData();
        // initRecyclerview();
        if (CommonMethods.checkInternetConnection(getContext())) {
            sendRequest(ApiCode.SCHOOL_GET_CLASS);
        }
        init_swipe_method();
        return view;
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        school_id = getArguments().getString("school_id");
        if (from.equals("s_tutor")) {
            feb_addclass.setVisibility(View.GONE);

        } else if (from.equals("school")) {
            feb_addclass.setVisibility(View.VISIBLE);
        }
    }


    private void initView(View view) {
        swipe = view.findViewById(R.id.swipe);
//        tv_title = view.findViewById(R.id.tv_title);
//        tv_title.setText("School Name");
//        arrow_back_img = view.findViewById(R.id.arrow_back_img);
        feb_addclass = view.findViewById(R.id.feb_addclass);
        rec_class = view.findViewById(R.id.rec_class);
        firebaseAuth=FirebaseAuth.getInstance();
        rec_class.hasFixedSize();
        rec_class.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_class.setLayoutManager(layoutManager);
        rec_class.setKeepScreenOn(true);
        feb_addclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////show list
                showAddClassDialog("add");
            }
        });

    }

    private void showAddClassDialog(String type) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_update_chapter);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        TextView tv_back = dialog.findViewById(R.id.tv_back);
        TextView tv_title = dialog.findViewById(R.id.tv_udpate_title);
        TextView tv_et_box_title = dialog.findViewById(R.id.tv_edit_title);
        tv_title.setText("Add Class");
        tv_et_box_title.setText("Class Name*");
        btn_save.setText("Add");
        et_name.setHint("Enter class name");

        if (type.equals("add")) {


        } else if (type.equals("update")) {
            tv_title.setText("Update Class");
            et_name.setText(class_name);
            btn_save.setText("Update");
        }
        dialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    et_name.setError("Enter class name");
                    et_name.requestFocus();
                } else {
                    class_name = et_name.getText().toString().trim();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        if (type.equals("add"))
                            sendRequest(ApiCode.SCHOOL_ADD_CLASS);
                        else
                            sendRequest(ApiCode.SCHOOL_UPDATE_CLASS);
                        dialog.dismiss();
                    }
                }

            }

        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);


    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                if (CommonMethods.checkInternetConnection(getContext())) {
                    sendRequest(ApiCode.SCHOOL_GET_CLASS);
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

        classesAdapter = new AllClassesAdapter("all_classes", getActivity(), from, classlist, new AllClassesAdapter.OnSelectClassClickListener() {
            @Override
            public void onItemClick(int postion) {
                AllClassesModel model = classlist.get(postion);
                ClassSectionFragment fragment = new ClassSectionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("class_id", model.getClass_id());
                bundle.putString("class_name", model.getName());
                bundle.putString("school_id", school_id);
                bundle.putString("from", from);
                fragment.setArguments(bundle);
                //comment by swati---- comment switchfragment method because its crash on this fragment
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                // SwitchFragment (fragment);
            }

            @Override
            public void onDelete(int position) {
                AllClassesModel model = classlist.get(position);
                class_id = model.getClass_id();
                showDeleteDailog();

            }

            @Override
            public void onEdit(int position) {
                AllClassesModel model = classlist.get(position);
                class_name = model.getName();
                class_id = model.getClass_id();
                showAddClassDialog("update");

            }
        });
        rec_class.setAdapter(classesAdapter);
        classesAdapter.notifyDataSetChanged();


    }

    private void showDeleteDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethods.checkInternetConnection(getActivity())) {
                            sendRequest(ApiCode.SCHOOL_DELETE_CLASS);
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
        if (from.equals("school")) {
            ((SchoolMainActivity) getActivity()).setActionBarTitle(new SessionManagement(getActivity()).getString(SCHOOL_NAME));
        } else if (from.equals("s_tutor")) {
            ((SchoolMainActivity) getActivity()).setActionBarTitle(getArguments().getString("school_name"));

        }
    }

    ///---api call--//

    private void sendRequest(int request) {
        SessionManagement management=new SessionManagement(getActivity());
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS:
                if(from.equals("school")){
                    reference.child(firebaseAuth.getUid()).child("Class").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            classlist.clear();

                            for (DataSnapshot ds:snapshot.getChildren()){
                                AllClassesModel allClassesModel=new AllClassesModel();
                                allClassesModel.setClass_id(ds.child("class_name").getValue(String.class));
                                allClassesModel.setName(ds.child("class_name").getValue(String.class));
                                classlist.add(allClassesModel);

                            }


                            initRecyclerview();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else if(from.equals("s_tutor")){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            classlist.clear();
                            for(DataSnapshot ds:snapshot.getChildren()){
//                                if(ds.child("SchoolId").getValue(String.class)!=null){
//                                    Log.e("mgfhgfuyjfuyjmfuky",ds.child("SchoolId").getValue(String.class));
//                                }
//                                Log.e("mgfhgfuyjfuyjmfuky","ghcghcgh:::::::"+school_id);

                                if(ds.child("SchoolId").getValue(String.class)!=null && ds.child("SchoolId").getValue(String.class).equals(school_id)){

                                    for(DataSnapshot ds1:ds.child("Tutors").child(session_management.getString(SCHOOL_T_MOBILE)).child("Assigned").child("Class").getChildren()){
                                        AllClassesModel allClassesModel=new AllClassesModel();
                                        allClassesModel.setClass_id(ds1.getKey());
                                        allClassesModel.setName(ds1.getKey());
                                        classlist.add(allClassesModel);
                                    }

                                }
                            }
                            initRecyclerview();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                break;
            case ApiCode.SCHOOL_ADD_CLASS:
                params.put("school_id", school_id);
                params.put("class_name", class_name);
                params.put("name", class_name);

                reference.child(firebaseAuth.getUid()).child("Class").child(class_name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CommonMethods.showSuccessToast(getActivity(),"Class Name Already Exists");
                        }else{
                            reference.child(firebaseAuth.getUid()).child("Class").child(class_name).setValue(params)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            sendRequest(ApiCode.SCHOOL_GET_CLASS);
                                            CommonMethods.showSuccessToast(getActivity(),"Class Added Successfully");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.SCHOOL_UPDATE_CLASS:
                params.put("class_id", class_id);
                params.put("class_name", class_name);
                callApi(ApiCode.SCHOOL_UPDATE_CLASS, params);
                break;
            case ApiCode.SCHOOL_DELETE_CLASS:
                params.put("class_id", class_id);
                callApi(ApiCode.SCHOOL_DELETE_CLASS, params);
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
            case ApiCode.SCHOOL_ADD_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_ADD_CLASS,
                        BaseUrl.URL_SCHOOL_ADD_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_CLASS,
                        BaseUrl.URL_SCHOOL_UPDATE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_CLASS,
                        BaseUrl.URL_SCHOOL_DELETE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_CLASS);
                Log.e("params", params.toString());
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
                    JSONArray jsonArray = jsonObject.getJSONArray("classes");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        classlist.clear();
                        classlist.addAll(psearch);
                        initRecyclerview();
                    } else {
                        classlist.clear();
                        initRecyclerview();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    classlist.clear();
                    initRecyclerview();
                    // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_CLASS:
                Log.e("sc_add_class", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_CLASS:
                Log.e("sc_up_class", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_CLASS:
                Log.e("delete_class", response);
                if (jsonObject.getBoolean("response")) {
                    sendRequest(ApiCode.SCHOOL_GET_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(), jsonObject.getString("message"));

                break;
        }
    }

}
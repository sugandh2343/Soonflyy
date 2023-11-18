package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.SchoolCoachingAsignTutorFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter.Indp_TutorManageStudentAdapter;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorManageStudentModel;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Scl_Indp_TutorManageStudentFragment extends Fragment implements VolleyResponseListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    RelativeLayout rel_no_live, rel_showclass;
    RecyclerView rec_manage_student;
    LinearLayout lin_filter;


    EditText et_name, et_mobile, et_email, et_father, et_address;
    TextView tv_class, tv_section, tv_cancel, tv_dob, f_tv_class, f_tv_section;
    Spinner section_spinner, class_spinner, f_class_spinner, f_section_spinner;


    Button dialogButton;

    private FloatingActionButton feb_manage_student;
    SwipeRefreshLayout swipe;
    Indp_TutorManageStudentAdapter liveClassAdapter;
    ArrayList<Indp_TutorManageStudentModel> livelist = new ArrayList<>();
    ArrayList<AllClassesModel> classList = new ArrayList<>();
    ArrayList<AllClassesModel> sectionList = new ArrayList<>();
    ArrayList<AllClassesModel> f_classList = new ArrayList<>();
    ArrayList<AllClassesModel> f_sectionList = new ArrayList<>();
    ArrayAdapter<AllClassesModel> class_adapter, section_adapter, f_class_adapter, f_section_adapter;


    String from, school_id, class_id, section_id, f_class_id, f_section_id, f_type, itutor_id;
    String blockStudentId,blockStatus,deleteStudentId;
    boolean isShowingExistData=false;
    //-----------------//
    String fSectionName="";
    String fSectionId="";
    //----------------//


    public Scl_Indp_TutorManageStudentFragment() {
        // Required empty public constructor
    }


    public static Scl_Indp_TutorManageStudentFragment newInstance(String param1, String param2) {
        Scl_Indp_TutorManageStudentFragment fragment = new Scl_Indp_TutorManageStudentFragment();
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
        View view = inflater.inflate(R.layout.fragment_scl__indp__tutor_manage_student, container, false);
        init_View(view);
        getArgumentData();
//        setSpinnerData();
        if (TextUtils.isEmpty(fSectionName)) {
            callApiRequest();
        }
        init_swipe_method();
        //  initRecyclerview();
        return view;
    }

    private void setSpinnerData() {
        //for class spinner
        classList.add(new AllClassesModel());
//
//        class_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, classList);
//        class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        f_class_spinner.setAdapter(class_adapter);


        //for section spinner
        sectionList.add(new AllClassesModel());
        section_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, sectionList);
        section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        f_section_spinner.setAdapter(section_adapter);

        //for class spinner
        f_classList.add(new AllClassesModel());
        f_class_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, f_classList);
        f_class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        f_class_spinner.setAdapter(f_class_adapter);


        //for section spinner
        f_sectionList.add(new AllClassesModel());
        f_section_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, f_sectionList);
        f_section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);
        f_section_spinner.setAdapter(f_section_adapter);
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        if (from.equals(SCHOOL_COACHING)) {
            school_id = getArguments().getString("school_id");
            lin_filter.setVisibility(View.VISIBLE);
        } else if (from.equals(INDEPENDENT_TUTOR)) {
            itutor_id = getArguments().getString("itutor_id");
            lin_filter.setVisibility(View.GONE);
        }
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callApiRequest();
                initControl();
                //initRecyclerview();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void initControl() {
    }


    private void init_View(View view) {
        lin_filter = view.findViewById(R.id.lin_filter);
        f_tv_class = view.findViewById(R.id.f_tv_s_class);
        f_tv_section = view.findViewById(R.id.f_tv_section);
        f_section_spinner = view.findViewById(R.id.f_section_spinner);
        f_class_spinner = view.findViewById(R.id.f_class_spinner);

        swipe = view.findViewById(R.id.swipe);
        rel_no_live = view.findViewById(R.id.rel_no_live);
        rel_showclass = view.findViewById(R.id.rel_showclass);
        rec_manage_student = view.findViewById(R.id.rec_manage_student);
        rec_manage_student.hasFixedSize();
        rec_manage_student.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_manage_student.setLayoutManager(layoutManager);
        rec_manage_student.setKeepScreenOn(true);

        feb_manage_student = view.findViewById(R.id.feb_manage_student);
        feb_manage_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_AddNewStudent();
            }
        });

        f_tv_section.setOnClickListener(this);
        f_tv_class.setOnClickListener(this);
        f_class_spinner.setOnItemSelectedListener(this);
        f_section_spinner.setOnItemSelectedListener(this);

    }

    private void initRecyclerview() {

//        livelist = new ArrayList<>();
//
//        livelist.add(new Indp_TutorManageStudentModel());
//        livelist.add(new Indp_TutorManageStudentModel());


        liveClassAdapter = new Indp_TutorManageStudentAdapter(getActivity(), from, livelist, new Indp_TutorManageStudentAdapter.OnSelectClickListener() {
            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onPerformanceClik(int postion) {
                fSectionName=f_tv_section.getText().toString();

                fSectionId=f_section_id;


                Indp_TutorManageStudentModel model = livelist.get(postion);
                Scl_Indp_TutorStudentPerformanceFragment fragment = new Scl_Indp_TutorStudentPerformanceFragment();

                Bundle bundle = new Bundle();
                bundle.putString("from", from);
                bundle.putString("name", model.getName());
                bundle.putString("student_id", model.getStudent_id());
                bundle.putString("school_id", school_id);
                fragment.setArguments(bundle);
                // SwitchFragment (fragment);
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }

            @Override
            public void onBlock(int position,String statusValue) {
               blockStatus=statusValue;
               blockStudentId=livelist.get(position).getStudent_id();
//               if (CommonMethods.checkInternetConnection(getActivity()))
//                   sendRequest(ApiCode.SCHOOL_BLOCK_UNBLOCK_USER);
               if (statusValue.equals("1")) {
                   showBlockConfirmation("Are you sure to block?");
               }else{
                   showBlockConfirmation("Are you sure to unblock?");
               }


            }

            @Override
            public void onEdit(int position) {

            }

            @Override
            public void onDelete(int position, Indp_TutorManageStudentModel model) {
                deleteStudentId=model.getStudent_id();
                showDeleteAlert();
            }
        });
//        if (livelist.size()==0){
//            rel_no_live.setVisibility(View.VISIBLE);
//            rel_showclass.setVisibility(View.GONE);
//
//        }
//        else {
        // rel_no_live.setVisibility(View.GONE);
        //  rel_showclass.setVisibility(View.VISIBLE);
        rec_manage_student.setAdapter(liveClassAdapter);
        liveClassAdapter.notifyDataSetChanged();
        //  }


    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethods.checkInternetConnection(getActivity()))
                            sendRequest(ApiCode.DELETE_STUDENT_BY_SCHOOL);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }

    private void showBlockConfirmation(String msg) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethods.checkInternetConnection(getActivity()))
                            sendRequest(ApiCode.SCHOOL_BLOCK_UNBLOCK_USER);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
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

    private void show_AddNewStudent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_new_student);
        dialog.getWindow();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();


        et_name = dialog.findViewById(R.id.et_name);
        et_mobile = dialog.findViewById(R.id.et_mobile);
        et_email = dialog.findViewById(R.id.et_email);
        et_father = dialog.findViewById(R.id.et_father_name);
        tv_dob = dialog.findViewById(R.id.tv_dob);
        et_address = dialog.findViewById(R.id.et_address);
        tv_class = dialog.findViewById(R.id.tv_class);
        tv_section = dialog.findViewById(R.id.tv_section);
        section_spinner = dialog.findViewById(R.id.section_spinner);
        class_spinner = dialog.findViewById(R.id.class_spinner);
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        LinearLayout lin_class = dialog.findViewById(R.id.lin_class);
        LinearLayout lin_section = dialog.findViewById(R.id.lin_section);
        dialogButton = (Button) dialog.findViewById(R.id.btn_add);

        if (from.equals(INDEPENDENT_TUTOR)) {
            lin_class.setVisibility(View.GONE);
            lin_section.setVisibility(View.GONE);
        }

        //for class spinner
        class_spinner.setAdapter(class_adapter);

        //for section spinner
        section_spinner.setAdapter(section_adapter);

        class_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);

        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mString=s.toString();
                setFieldEnable(true);
                if (!TextUtils.isEmpty(mString)){
                    if (mString.length()==10){
                        if (Integer.parseInt(String.valueOf(mString.charAt(0))) < 6) {
                            et_mobile.setError("Invalid mobile number");
                            et_mobile.requestFocus();
                        }else{
                            if (CommonMethods.checkInternetConnection(getActivity())){
                                sendRequest(ApiCode.GET_STUDENT_DATA_BY_MOBILE);
                            }
                        }
                    }else{
                        if (isShowingExistData){
                            clearFieldData();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        tv_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonMethods.showDatePicker(getActivity(), tv_dob);
//            }
//        });


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmptyField()) {
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        //call add api
                        sendRequest(ApiCode.SCHOOL_ADD_STUDENT);
                        dialog.dismiss();
                    }
                }

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (class_adapter.getCount() > 1)
                    class_spinner.performClick();
            }
        });
        tv_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (section_adapter.getCount() > 1)
                    section_spinner.performClick();
            }
        });

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

       // dialog.show();
    }

    private void showDatePicker() {
        boolean isValidDate=false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_divider_color));

        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month, int day, int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());

                tv_dob.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                String d=tv_dob.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tv_dob.setText("");
//                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"Please select a date before  current date");
                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidDOB(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateEmptyField() {
        String name = et_name.getText().toString().trim();
        String mobile_num = et_mobile.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String father=et_father.getText().toString().trim();
        String dob=tv_dob.getText().toString().trim();
        String address=et_address.getText().toString().trim();


        if (TextUtils.isEmpty(mobile_num)) {
            et_mobile.setError("Enter mobile number");
            et_mobile.requestFocus();
            return false;
        }
        if (mobile_num.length() != 10) {
            et_mobile.setError("Invalid mobile number");
            et_mobile.requestFocus();
            return false;
        }

        if (Integer.parseInt(String.valueOf(mobile_num.charAt(0))) < 6) {
            et_mobile.setError("Invalid mobile number");
            et_mobile.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(name)){
            et_name.setError("Enter name");
            et_name.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(email)){
            et_email.setError("Enter email-ID");
            et_email.requestFocus();
            return false;
        }
//
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Invalid email-ID");
            et_email.requestFocus();
            return false;
        }
//
        if(TextUtils.isEmpty(father)){
            et_father.setError("Enter father name");
            et_father.requestFocus();
            return false; }
        if(TextUtils.isEmpty(dob)){
            CommonMethods.showSuccessToast(getContext(),"Choose date of birth");
            return false; }

        if (from.equals(SCHOOL_COACHING)) {
            if (TextUtils.isEmpty(class_id)) {
                CommonMethods.showSuccessToast(getContext(), "Choose Class");
                return false;
            }
            if (TextUtils.isEmpty(section_id)) {
                CommonMethods.showSuccessToast(getContext(), "Choose Section");
                return false;
            }
        }
        if(TextUtils.isEmpty(address)){
            et_address.setError("Enter address");
            et_address.requestFocus();
            return false; }

        return true;
    }


    ///---api call--//
    public void callGetStudentApi() {
        if (!TextUtils.isEmpty(f_class_id) && !TextUtils.isEmpty(f_section_id)) {
            if (CommonMethods.checkInternetConnection(getActivity())) {
                sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);
            }
        }
    }

    private void callApiRequest() {
        if (from.equals(SCHOOL_COACHING)) {
            if (CommonMethods.checkInternetConnection(getActivity())) {
//                if (f_class_id!=null && f_section_id!=null && school_id!=null){
//                    sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);
//                }else {
                    sendRequest(ApiCode.SCHOOL_GET_CLASS);
              //  }

            }
        }
        if (from.equals(INDEPENDENT_TUTOR)) {
            sendRequest(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS);
        }
    }

    private void sendRequest(int request) {
        SessionManagement sessionManagement=new SessionManagement(getContext());
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        switch (request) {
            case ApiCode.SCHOOL_GET_CLASS:
                params.put("school_id", school_id);
                params.put("teacher_id", "");
//                callApi(ApiCode.SCHOOL_GET_CLASS, params);
                reference.child(firebaseAuth.getUid()).child("Class").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classList.clear();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            AllClassesModel allClassesModel = new AllClassesModel();
                            allClassesModel.setClass_id(ds.child("class_name").getValue(String.class));
                            allClassesModel.setName(ds.child("class_name").getValue(String.class));
                            classList.add(allClassesModel);

                        }
                        classList.add(0,new AllClassesModel("Select Class","",""));
//                        setSpinnerData();
                        SpinnerClassAdapter classAdapter = new SpinnerClassAdapter();
//
                        f_class_spinner.setAdapter(classAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                String classId="";
                params.put("school_id", school_id);
                params.put("teacher_id", "");
                if (f_type.equals("add_student")) {
                    classId=class_id;
                } else {
                    classId=f_class_id;
                }
//                callApi(ApiCode.SCHOOL_GET_SECTION, params);
                reference.child(firebaseAuth.getUid()).child("Class").child(classId).child("Section").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sectionList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            AllClassesModel allClassesModel = new AllClassesModel();
                            allClassesModel.setSection_id(ds.child("section_name").getValue(String.class));
                            allClassesModel.setName(ds.child("section_name").getValue(String.class));
                            sectionList.add(allClassesModel);

                        }
                        sectionList.add(0,new AllClassesModel("Select Section","Select Section","Select Section"));
                        SpinnerSectionAdapter sectionAdapter = new SpinnerSectionAdapter();
                        section_spinner.setAdapter(sectionAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                ///---for school coaching--///
                if (from.equals(SCHOOL_COACHING)) {
                    params.put("class_id", f_class_id);
                    params.put("section_id", f_section_id);
                    params.put("school_id", school_id);
                    params.put("type", "0");
                } else if (from.equals(INDEPENDENT_TUTOR)) {
                    ////--------for independent tutor---/////
                    params.put("teacher_id", itutor_id);
                    params.put("type", "1");
                }

                callApi(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS, params);
                break;

            case ApiCode.SCHOOL_ADD_STUDENT:
                ///---for school coaching--///
                params.put("name",et_name.getText().toString().trim());
                params.put("email",et_email.getText().toString().trim());
                params.put("father_name",et_father.getText().toString().trim());
                params.put("dob",tv_dob.getText().toString().trim());
                params.put("address",et_address.getText().toString().trim());
                params.put("mobile", et_mobile.getText().toString());
                if (from.equals(SCHOOL_COACHING)) {
                    params.put("class_id", class_id);
                    params.put("section_id", section_id);
                    params.put("school_id", school_id);
                } else if (from.equals(INDEPENDENT_TUTOR)) {
                    ////--------for independent tutor---/////
                    params.put("teacher_id", itutor_id);
                   // params.put("mobile", et_mobile.getText().toString());
                }

                callApi(ApiCode.SCHOOL_ADD_STUDENT, params);
                break;
            case ApiCode.SCHOOL_BLOCK_UNBLOCK_USER:
                ///---for school coaching--///
                if (from.equals(SCHOOL_COACHING)) {

                    params.put("school_id", school_id);
                    params.put("type", "0");
                } else if (from.equals(INDEPENDENT_TUTOR)) {
                    ////--------for independent tutor---/////
                    params.put("teacher_id", itutor_id);
                    params.put("type","1");

                }
                params.put("id",blockStudentId);
                params.put("status",blockStatus);

                callApi(ApiCode.SCHOOL_BLOCK_UNBLOCK_USER, params);
                break;
            case ApiCode.DELETE_STUDENT_BY_SCHOOL:
                ///---for school coaching--///
                params.put("student_id", deleteStudentId);
                if (from.equals(SCHOOL_COACHING)) {
                    params.put("type", "0");
                    params.put("school_id", school_id);
                } else if (from.equals(INDEPENDENT_TUTOR)) {
                    ////--------for independent tutor---/////
                    params.put("type", "1");
                    params.put("teacher_id", itutor_id);
                }
                callApi(ApiCode.DELETE_STUDENT_BY_SCHOOL, params);
                break;

            case ApiCode.GET_STUDENT_DATA_BY_MOBILE:
                params.put("mobile", et_mobile.getText().toString().trim());
                if (from.equals(SCHOOL_COACHING)){
                    params.put("type","0");
                }else if (from.equals(INDEPENDENT_TUTOR)){
                    params.put("type","1");
                }
                callApi(ApiCode.GET_STUDENT_DATA_BY_MOBILE, params);
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
            case ApiCode.SCHOOL_GET_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION,
                        BaseUrl.URL_SCHOOL_GET_SECTION, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SECTION);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                service.postDataVolley(ApiCode.SCHOOL_GET_SCHOOL_STUDENTS,
                        BaseUrl.URL_SCHOOL_GET_SCHOOL_STUDENTS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_SCHOOL_STUDENTS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_ADD_STUDENT,
                        BaseUrl.URL_SCHOOL_ADD_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_BLOCK_UNBLOCK_USER:
                service.postDataVolley(ApiCode.SCHOOL_BLOCK_UNBLOCK_USER,
                        BaseUrl.URL_SCHOOL_BLOCK_UNBLOCK_USER, params);
                Log.e("api", BaseUrl.URL_SCHOOL_BLOCK_UNBLOCK_USER);
                Log.e("params", params.toString());
                break;
            case ApiCode.DELETE_STUDENT_BY_SCHOOL:
                service.postDataVolley(ApiCode.DELETE_STUDENT_BY_SCHOOL,
                        BaseUrl.URL_DELETE_STUDENT_BY_SCHOOL, params);
                Log.e("api", BaseUrl.URL_DELETE_STUDENT_BY_SCHOOL);
                Log.e("params", params.toString());
                break;
            case ApiCode.GET_STUDENT_DATA_BY_MOBILE:
                service.postDataVolley(ApiCode.GET_STUDENT_DATA_BY_MOBILE,
                        BaseUrl.URL_GET_STUDENT_DATA_BY_MOBILE, params);
                Log.e("api", BaseUrl.URL_GET_STUDENT_DATA_BY_MOBILE);
                Log.e("params", params.toString());
                break;
//


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

                        classList.clear();
                        f_classList.clear();
                        classList.add(new AllClassesModel());
                        classList.addAll(psearch);
                        f_classList.addAll(classList);
                        class_adapter.notifyDataSetChanged();
                        f_class_adapter.notifyDataSetChanged();
                        if (TextUtils.isEmpty(f_tv_class.getText().toString().trim())){
                          if (f_classList.size()>1) {
                              f_class_spinner.setSelection(1);
                          }
                        }

                    } else {
                        classList.clear();
                        f_classList.clear();
                        classList.add(new AllClassesModel());
                        f_classList.addAll(classList);
                        class_adapter.notifyDataSetChanged();
                        f_class_adapter.notifyDataSetChanged();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        if (f_type.equals("add_student")) {
                            sectionList.clear();
                            sectionList.add(new AllClassesModel());
                            sectionList.addAll(psearch);
                            section_adapter.notifyDataSetChanged();
                        } else {
                            f_sectionList.clear();
                            f_sectionList.add(new AllClassesModel());
                            f_sectionList.addAll(psearch);
                            f_section_adapter.notifyDataSetChanged();
                            if (!TextUtils.isEmpty(fSectionName)){
                                f_tv_section.setText(fSectionName);
                            }else if (TextUtils.isEmpty(f_tv_section.getText().toString().trim())){
                                if (f_sectionList.size()>1) {
                                    f_section_spinner.setSelection(1);
                                }
                            }
                        }
                    } else {
                        if (f_type.equals("add_student")) {
                            sectionList.clear();
                            sectionList.add(new AllClassesModel());
                            section_adapter.notifyDataSetChanged();
                        } else {
                            f_sectionList.clear();
                            f_sectionList.add(new AllClassesModel());
                            f_section_adapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    if (f_type.equals("add_student")) {
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        section_adapter.notifyDataSetChanged();
                    } else {
                        f_sectionList.clear();
                        f_sectionList.add(new AllClassesModel());
                        f_section_adapter.notifyDataSetChanged();
                    }
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;

            case ApiCode.SCHOOL_GET_SCHOOL_STUDENTS:
                Log.e("sc_students", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Indp_TutorManageStudentModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Indp_TutorManageStudentModel>>() {
                                        }.getType());
                        livelist.clear();
                        livelist.addAll(psearch);
                        initRecyclerview();
                    } else {
                        livelist.clear();
                        initRecyclerview();

                    }
                } else {
                    livelist.clear();
                    initRecyclerview();
                   // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;
            case ApiCode.SCHOOL_ADD_STUDENT:
                Log.e("add_student", "" + response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Student Added Successfully");

                    if (from.equals(SCHOOL_COACHING)){
                        callGetStudentApi();
                    }else{
                        callApiRequest();
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;

            case ApiCode.SCHOOL_BLOCK_UNBLOCK_USER:
                Log.e("balockStudent", "" + response);
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                   if (from.equals(INDEPENDENT_TUTOR))
                    callApiRequest();
                   else
                       callGetStudentApi();
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;
            case ApiCode.DELETE_STUDENT_BY_SCHOOL:
                Log.e("delete", "" + response);
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                    if (from.equals(INDEPENDENT_TUTOR))
                        callApiRequest();
                    else
                        callGetStudentApi();
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
                break;

            case ApiCode.GET_STUDENT_DATA_BY_MOBILE:
                Log.e("studentData", "" + response);
                if(jsonObject.getBoolean("response")){
                    setRegisterdData(jsonObject.getJSONObject("data"));
                }
                break;



        }
    }

    private void setRegisterdData(JSONObject data) {
        if (data.length()>0){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                       // et_mobile.setText(data.getString("mobile"));
                        isShowingExistData=true;
                        et_name.setText(data.getString("name"));
                        et_email.setText(data.getString("email"));
                        et_father.setText(data.getString("father_name"));
                        tv_dob.setText(data.getString("dob"));
                        et_address.setText(data.getString("address"));
//                        if (from.equals(SCHOOL_COACHING)) {
//                            tv_class.setText(data.getString("class_name"));
//                            tv_section.setText(data.getString("section_name"));
//                            section_id=data.getString("section_id");
//                            class_id=data.getString("class_id");
//                            sendRequest(ApiCode.SCHOOL_GET_SECTION);
//                        }
                        setFieldEnable(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
    private void setFieldEnable(boolean value){
        et_name.setEnabled(value);
        et_email.setEnabled(value);
        et_father.setEnabled(value);
        tv_dob.setEnabled(value);
        et_address.setEnabled(value);
    }
    private void clearFieldData(){
        isShowingExistData=false;
        et_name.getText().clear();
        et_email.getText().clear();
        et_father.getText().clear();
        tv_dob.setText("");
       tv_class.setText("");
       tv_section.setText("");
       section_id="";
       class_id="";
        et_address.getText().clear();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            switch (parent.getId()) {
                case R.id.class_spinner:
                    AllClassesModel model = classList.get(position);
                    tv_class.setText(model.getName());
                    class_id = model.getClass_id();
                    section_id = "";
                    f_type = "add_student";
                    sectionList.clear();
                    sectionList.add(new AllClassesModel());
                    tv_section.setText("");
                    section_adapter.notifyDataSetChanged();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.section_spinner:
                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    break;
                case R.id.f_class_spinner:
                    AllClassesModel model1 = f_classList.get(position);
                    f_tv_class.setText(model1.getName());
                    f_class_id = model1.getClass_id();
                    f_section_id = "";
                    f_type = "filter";
                    f_sectionList.clear();
                    f_sectionList.add(new AllClassesModel());
                    f_tv_section.setText("");
                    f_section_adapter.notifyDataSetChanged();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    }
                    break;
                case R.id.f_section_spinner:
                    fSectionName="";
                    fSectionId="";
                    AllClassesModel smodel1 = f_sectionList.get(position);
                    f_tv_section.setText(smodel1.getName());
                    f_section_id = smodel1.getSection_id();
                    callGetStudentApi();
                    break;


            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).setActionBarTitle("Manage Students");

        Log.e("SectionPosition",""+fSectionName);


        if (!TextUtils.isEmpty(fSectionName)){
            f_tv_section.setText(fSectionName);
            f_section_id=getSectinId(fSectionName);
            Log.e("Selectioniddfdfd",""+f_section_id);
            //f_section_id=getSectinId(fSectionName);
            callGetStudentApi();
        }

    }

    private String getSectinId(String setion) {
        String sectionId="";
        if(f_sectionList.size()>0){
            for (int i=0;i<f_sectionList.size();i++){
                AllClassesModel model=f_sectionList.get(i);
                if(model.getName().equals(setion)){
                    sectionId=model.getSection_id();
                    break;
                }
            }
        }
        return sectionId;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_tv_s_class:
                if (classList.size() > 0) {
                    f_class_spinner.performClick();
                }
                break;
            case R.id.f_tv_section:

                if (f_section_adapter.getCount() > 1)
                    f_section_spinner.performClick();

                break;
        }

    }
    class SpinnerClassAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return classList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position , View convertView , ViewGroup parent) {
            LayoutInflater inf = getLayoutInflater();
            View row = inf.inflate(R.layout.layout_category_single_row , null);
            TextView txt_category_name;

            txt_category_name = row.findViewById(R.id.txt_category_name);
            txt_category_name.setText(classList.get(position).getName());


            return row;
        }
    }

    class SpinnerSectionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sectionList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position , View convertView , ViewGroup parent) {
            LayoutInflater inf = getLayoutInflater();
            View row = inf.inflate(R.layout.layout_category_single_row , null);
            TextView txt_category_name;

            txt_category_name = row.findViewById(R.id.txt_category_name);
            txt_category_name.setText(sectionList.get(position).getName());


            return row;
        }
    }
}
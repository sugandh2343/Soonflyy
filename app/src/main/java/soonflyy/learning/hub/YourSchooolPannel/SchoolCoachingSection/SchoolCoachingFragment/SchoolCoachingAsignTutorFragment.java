package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter.SchoolCoachingTutorAsginAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolTutorAllChapterFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllSubjectModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginClassModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.adapter.MultiDayAdapter;
import soonflyy.learning.hub.model.DayModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SchoolCoachingAsignTutorFragment extends Fragment implements VolleyResponseListener, PopupWindow.OnDismissListener {
    FloatingActionButton btnAddTutor;
    RecyclerView rec_school_asigntutor;

    EditText et_name, et_mobile, et_email, et_father, et_address;
    TextView tv_dob;

    SwipeRefreshLayout swipe;
    RelativeLayout rel_no_live, rel_showlist;
    SchoolCoachingTutorAsginAdapter adapter;
    ArrayList<SchoolCoachingTutorAsginModel> modellist = new ArrayList<>();
    ArrayList<AllClassesModel> classList = new ArrayList<>();
    ArrayList<AllClassesModel> sectionList = new ArrayList<>();
    ArrayList<AllSubjectModel> subject_list = new ArrayList<>();
    ArrayAdapter<AllClassesModel> class_adapter, section_adapter;
    ArrayAdapter<AllSubjectModel> subjectAdapter;

    String from, school_id, class_id, subject_id, section_id, date, sTime, eTime, period, teacher_id,teacher_name, period_id;
    String class_name, section_name, subject_name, tutor_id, blockStatus;

    String dateType = "";
    boolean isShowingExistData = false;
    Spinner section_spinner;
    Spinner subject_spinner;
    ProgressDialog progressDialog;

    //------day model----//
    ArrayList<DayModel> dayList = new ArrayList<>();
    MultiDayAdapter multiDayAdapter;
    PopupWindow dayPopupWindow;
    ArrayList<String> selectedDayIdList = new ArrayList<>();
    TextView tvAttachedDayPopup;
    FirebaseAuth firebaseAuth;

    Dialog addUpdateDialog;


    public SchoolCoachingAsignTutorFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SchoolCoachingAsignTutorFragment newInstance(String param1 , String param2) {
        SchoolCoachingAsignTutorFragment fragment = new SchoolCoachingAsignTutorFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school_coaching_asign_tutor , container , false);
        initView(view);
        getArgumentData();
        setSpinner();
        callApiRequest();
        init_swipe_method();
        return view;
    }

    private void getArgumentData() {
        from = getArguments().getString("from");
        school_id = getArguments().getString("school_id");
    }

    private void initView(View view) {
        btnAddTutor = view.findViewById(R.id.feb_add_tutor);
        swipe = view.findViewById(R.id.swipe);
        firebaseAuth = FirebaseAuth.getInstance();
        rec_school_asigntutor = view.findViewById(R.id.rec_school_asigntutor);
        //  rel_no_live= view.findViewById(R.id.rel_no_live);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        btnAddTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTutorDialog();
            }
        });


    }


    private void setSpinner() {
        //for class spinner
        classList.add(new AllClassesModel());
        class_adapter = new ArrayAdapter<>(getActivity() , R.layout.spinner_item_holder , classList);
        class_adapter.setDropDownViewResource(R.layout.spinner_item_holder);


        //for section spinner
        sectionList.add(new AllClassesModel());
        section_adapter = new ArrayAdapter<>(getActivity() , R.layout.spinner_item_holder , sectionList);
        section_adapter.setDropDownViewResource(R.layout.spinner_item_holder);

        //for subject spinner
        subject_list.add(new AllSubjectModel(" " , ""));
        subjectAdapter = new ArrayAdapter<>(getActivity() , R.layout.spinner_item_holder , subject_list);
        subjectAdapter.setDropDownViewResource(R.layout.spinner_item_holder);


    }


    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);

                callApiRequest();
                initControl();
                // initRecyclerView();
                //swipe.setRefreshing(false);
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red , R.color.green , R.color.orange , R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void initRecyclerView() {

//        modellist = new ArrayList<>();
//        modellist.clear();
        rec_school_asigntutor.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_school_asigntutor.setLayoutManager(linearLayoutManager);
        rec_school_asigntutor.setKeepScreenOn(true);

//        modellist.add(new SchoolCoachingTutorAsginModel());
//        modellist.add(new SchoolCoachingTutorAsginModel());
//        modellist.add(new SchoolCoachingTutorAsginModel());

        adapter = new SchoolCoachingTutorAsginAdapter(getContext() , modellist , new SchoolCoachingTutorAsginAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {
//                SchoolTutorAllChapterFragment fragment = new SchoolTutorAllChapterFragment();
//                SwitchFragment(fragment);
            }

            @Override
            public void onAsignTutor_Click(int postion) {
                teacher_id = modellist.get(postion).getId();
                teacher_name = modellist.get(postion).getName();
                Log.e("teacsfdd",teacher_id);
                showAsignClassDailoge("add");
            }


            @Override
            public void onViewClass(int position , SchoolCoachingTutorAsginClassModel assignModel) {
                SchoolTutorAllChapterFragment fragment = new SchoolTutorAllChapterFragment();
                Bundle bundle = new Bundle();
                bundle.putString("subject_id" , assignModel.getSubject_id());
                bundle.putString("class_id" , assignModel.getClass_id());
                bundle.putString("section_id" , assignModel.getSection_id());
                bundle.putString("school_id" , school_id);
                bundle.putString("subject_name" , assignModel.getSubject_name());
                bundle.putString("from" , from);
                fragment.setArguments(bundle);
                // SwitchFragment(fragment);
                //comment by swati---- comment switchfragment method because its crash on this fragment
                ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
            }

            @Override
            public void onUpdate(int position , SchoolCoachingTutorAsginClassModel assignModel) {
                SchoolCoachingTutorAsginModel model = modellist.get(position);
                teacher_id = model.getId();
                class_id = assignModel.getClass_id();
                class_name = assignModel.getClass_name();
                section_id = assignModel.getSection_id();
                section_name = assignModel.getSection_name();
                subject_id = assignModel.getSubject_id();
                subject_name = assignModel.getSubject_name();
                period = assignModel.getPeroid();
                period_id = assignModel.getPeroid_id();
                date = assignModel.getDate();
                sTime = assignModel.getStart_time();
                eTime = assignModel.getEnd_time();
                selectedDayIdList = getDayArray(assignModel.getDays());
                showAsignClassDailoge("update");
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    sendRequest(ApiCode.SCHOOL_GET_SECTION);
                    sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                }
            }

            @Override
            public void onDelete(int position , SchoolCoachingTutorAsginModel model) {
                tutor_id = model.getId();
                showDeleteAlert();
            }

            @Override
            public void onBlockUnblock(int position , SchoolCoachingTutorAsginModel model , String blockValue) {
                blockStatus = blockValue;
                tutor_id = model.getId();
                if (blockValue.equals("1")) {
                    showBlockConfirmation("Are you sure to block?");
                } else {
                    showBlockConfirmation("Are you sure to unblock?");
                }
            }

            @Override
            public void onRemove(int position , SchoolCoachingTutorAsginClassModel assignModel) {
                SchoolCoachingTutorAsginModel model = modellist.get(position);
                tutor_id = model.getId();
                class_id = assignModel.getClass_id();
                section_id = assignModel.getSection_id();
                subject_id = assignModel.getSubject_id();
                period_id = assignModel.getPeroid_id();
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    sendRequest(ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL);
                }
            }


        });

//        if (modellist.size() < 0) {
//            rel_no_live.setVisibility(View.VISIBLE);
//            rel_showlist.setVisibility(View.GONE);
//
//        } else {
//            rel_no_live.setVisibility(View.GONE);
        rec_school_asigntutor.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //}


    }

    private ArrayList<String> getDayArray(String days) {
        ArrayList<String> list = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(days);
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.get(i).getAsString());
            }
        }
        return list;
    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure to delete?")
                .setPositiveButton("Yes" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog , int which) {
                        if (CommonMethods.checkInternetConnection(getActivity()))
                            sendRequest(ApiCode.DELETE_TUTOR_BY_SCHOOL);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog , int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }

    private void showBlockConfirmation(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage(msg)
                .setPositiveButton("Yes" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog , int which) {
                        if (CommonMethods.checkInternetConnection(getActivity()))
                            sendRequest(ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog , int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }


    private void showAsignClassDailoge(String type) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_school_tutotasign);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);

        //EditText et_name= dialog.findViewById(R.id.et_name);
        TextView tv_close = dialog.findViewById(R.id.tv_cancel);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_create);
        TextView tv_class = dialog.findViewById(R.id.tv_class);
        TextView tv_section = dialog.findViewById(R.id.tv_section);
        Spinner class_spinner = dialog.findViewById(R.id.class_spinner);
        section_spinner = dialog.findViewById(R.id.section_spinner);
        TextView tv_subject = dialog.findViewById(R.id.tv_subject);
        subject_spinner = dialog.findViewById(R.id.subject_spinner);
        EditText et_period = dialog.findViewById(R.id.et_period);
        TextView tv_date = dialog.findViewById(R.id.tv_date);
        TextView tv_sTime = dialog.findViewById(R.id.tv_time1);
        TextView tv_eTime = dialog.findViewById(R.id.tv_time2);

        updateDay(type , selectedDayIdList);
        SpinnerClassAdapter classAdapter = new SpinnerClassAdapter();

        class_spinner.setAdapter(classAdapter);


        if (type.equals("update")) {
            tv_class.setText(class_name);
            tv_section.setText(section_name);
            tv_subject.setText(subject_name);
            et_period.setText(period);
            tv_date.setText(date);
            tv_sTime.setText(sTime);
            tv_eTime.setText(eTime);
            dialogButton.setText("Update");

        }

        tv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class_spinner.performClick();
            }
        });
        tv_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section_spinner.performClick();
            }
        });
        tv_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject_spinner.performClick();
            }
        });
        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                if(position>0){
                    Log.e("ClassPosition" , classList.get(position).getName());
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(firebaseAuth.getUid()).child("Class")
                            .child(classList.get(position).getName()).child("Section")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    sectionList.clear();
                                    AllClassesModel model = classList.get(position);
                                    tv_class.setText(model.getName());
                                    class_id = model.getClass_id();
                                    section_id = "";
                                    tv_section.setText("");
                                    subject_id = "";
                                    tv_subject.setText("");
                                    sectionList.clear();
                                    sectionList.add(new AllClassesModel());
                                    section_adapter.notifyDataSetChanged();

                                    subject_list.clear();
                                    subject_list.add(new AllSubjectModel(" " , ""));
                                    subjectAdapter.notifyDataSetChanged();

                                    if (CommonMethods.checkInternetConnection(getActivity())) {
                                        sendRequest(ApiCode.SCHOOL_GET_SECTION);
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                if(position>0) {


                    AllClassesModel smodel = sectionList.get(position);
                    tv_section.setText(smodel.getName());
                    section_id = smodel.getSection_id();
                    subject_id = "";
                    tv_subject.setText("");

                    subject_list.clear();
                    subject_list.add(new AllSubjectModel(" " , ""));
                    subjectAdapter.notifyDataSetChanged();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_GET_SUBJECT);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view , int position , long id) {
                if(position>0) {


                    AllSubjectModel model = subject_list.get(position);
                    tv_subject.setText(model.getSubject_name());
                    subject_id = model.getSubject_id();
                    subject_name=model.getSubject_name();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* dateType="timeTable";
                showDatePicker(tv_date);

                */
                showDayPopupDialog(tv_date , dayList);

                //CommonMethods.showDatePicker(getActivity(),tv_date,true,false);
            }
        });
        tv_sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.showTimePicker(getActivity() , "Start Time" , tv_sTime);
            }
        });
        tv_eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.showTimePicker(getActivity() , "End Time" , tv_eTime);
            }
        });

        //
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                period = et_period.getText().toString().trim();
                date = tv_date.getText().toString().trim();
                sTime = tv_sTime.getText().toString().trim();
                eTime = tv_eTime.getText().toString().trim();
                if (TextUtils.isEmpty(class_id)) {
                    CommonMethods.showSuccessToast(getActivity() , "Select Class");
                } else if (TextUtils.isEmpty(section_id)) {
                    CommonMethods.showSuccessToast(getActivity() , "Select Section");
                } else if (TextUtils.isEmpty(subject_id)) {
                    CommonMethods.showSuccessToast(getActivity() , "Select Subject");
                } else if (TextUtils.isEmpty(period)) {
                    et_period.setError("Enter period");
                    et_period.requestFocus();
                } else if (selectedDayIdList.size() == 0) {
                    CommonMethods.showSuccessToast(getActivity() , "Select day");
                }
//                else if (TextUtils.isEmpty(date)){
//                    CommonMethods.showSuccessToast(getActivity(),"Select Date");
//                }
                else if (TextUtils.isEmpty(sTime)) {
                    CommonMethods.showSuccessToast(getActivity() , "Select Start Time");
                } else if (TextUtils.isEmpty(eTime)) {
                    CommonMethods.showSuccessToast(getActivity() , "Select End Time");
                } else if (!CommonMethods.isValidTimePeriod(sTime , eTime)) {
                    CommonMethods.showSuccessToast(getContext() , "Invalid time");
                } else {
//                    addUpdateDialog = dialog;
//                    new ValidateDatabase().execute(type);
//                    if (CommonMethods.checkInternetConnection(getActivity())){
//                        if (type.equals("add")){
//                            sendRequest(ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL);
//                        }else{
//                            sendRequest(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL);
//                        }
//                        dialog.dismiss();
//                    }
                    progressDialog.setMessage("Assigning Subject");
                    progressDialog.show();
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid())
                            .child("Class").child(class_id).child("Section")
                            .child(section_id);
                    Boolean alreadyAssigned=false;

                        reference.child("Subjects").child(subject_name).child("AssignTo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final Boolean[] AssignResult = {false};
                                if(snapshot.exists()){

                                    if(!snapshot.child("teacher_id").getValue(String.class).equals(teacher_id)){
                                        progressDialog.dismiss();
                                        CommonMethods.showSuccessToast(getActivity(),subject_name+" in this section is already assigned to "+snapshot.child("teacher_name").getValue(String.class));
                                    } for(int i=0;i<selectedDayIdList.size();i++) {

                                        Log.e("DAYUFUJC" , selectedDayIdList.get(i));
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("dayId" , selectedDayIdList.get(i));
                                        hashMap.put("period" , period);
                                        hashMap.put("subject" , subject_name);
                                        hashMap.put("assignedToId" , teacher_id);
                                        hashMap.put("assignedToName" , teacher_name);
                                        int finalI = i;
                                        reference.child("TimeTable").child(selectedDayIdList.get(i)).child(period).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
                                                if(snapshot.exists()){
                                                    CommonMethods.showSuccessToast(getActivity(),period+" Period Already assigned"+" on "+CommonMethods.getDayList().get(Integer.parseInt(selectedDayIdList.get(finalI))-1).getName());
                                                }else{
                                                    AssignResult[0] =true;
                                                    reference1.child("Tutors").child(teacher_id).child("TimeTable").child(selectedDayIdList.get(finalI)).child(period)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        CommonMethods.showSuccessToast(getActivity(),"Tutor is not free on "+CommonMethods.getDayList().get(Integer.parseInt(selectedDayIdList.get(finalI))-1).getName()+
                                                                                " in "+period+" period");
                                                                    }else{
                                                                        reference1.child("Tutors").child(teacher_id).child("TimeTable").child(selectedDayIdList.get(finalI)).child(period).
                                                                                setValue(hashMap);
                                                                        reference.child("TimeTable").child(selectedDayIdList.get(finalI)).child(period).setValue(hashMap);
                                                                        HashMap<String,Object>hashMap1=new HashMap<>();
                                                                        hashMap1.put("teacher_id",teacher_id);
                                                                        hashMap1.put("teacher_name",teacher_name);
                                                                        hashMap1.put("subject",subject_name);
                                                                        reference.child("Subjects").child(subject_name).child("AssignTo")
                                                                                .setValue(hashMap1);
                                                                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
                                                                        Log.e("TUTTCGHYDYGHCGHDHGDGHC",teacher_id);
                                                                        reference1.child("Tutors").child(teacher_id).child("Assigned").child("Class").child(class_id).child("Section")
                                                                                .child(section_id).child("Subject").child(subject_name).updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        CommonMethods.showSuccessToast(getActivity(),"Subject Assigned to "+teacher_name);
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    progressDialog.dismiss();
                                    if(AssignResult[0]){

                                    }

                                }else{
                                    for(int i=0;i<selectedDayIdList.size();i++) {

                                        Log.e("DAYUFUJC" , selectedDayIdList.get(i));
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("dayId" , selectedDayIdList.get(i));
                                        hashMap.put("period" , period);
                                        hashMap.put("subject" , subject_name);
                                        hashMap.put("assignedToId" , teacher_id);
                                        hashMap.put("assignedToName" , teacher_name);
                                        int finalI = i;
                                        reference.child("TimeTable").child(selectedDayIdList.get(i)).child(period).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
                                                if(snapshot.exists()){
                                                    CommonMethods.showSuccessToast(getActivity(),period+" Period Already assigned"+" on "+CommonMethods.getDayList().get(Integer.parseInt(selectedDayIdList.get(finalI))-1).getName());
                                                }else{
                                                    AssignResult[0] =true;
                                                    reference1.child("Tutors").child(teacher_id).child("TimeTable").child(selectedDayIdList.get(finalI)).child(period)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        CommonMethods.showSuccessToast(getActivity(),"Tutor is not free on "+CommonMethods.getDayList().get(Integer.parseInt(selectedDayIdList.get(finalI))-1).getName()+
                                                                                " in "+period+" period");
                                                                    }else{
                                                                        reference1.child("Tutors").child(teacher_id).child("TimeTable").child(selectedDayIdList.get(finalI)).child(period).
                                                                                setValue(hashMap);
                                                                        reference.child("TimeTable").child(selectedDayIdList.get(finalI)).child(period).setValue(hashMap);
                                                                        HashMap<String,Object>hashMap1=new HashMap<>();
                                                                        hashMap1.put("teacher_id",teacher_id);
                                                                        hashMap1.put("teacher_name",teacher_name);
                                                                        hashMap1.put("subject",subject_name);
                                                                        reference.child("Subjects").child(subject_name).child("AssignTo")
                                                                                .setValue(hashMap1);
                                                                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
                                                                        Log.e("TUTTCGHYDYGHCGHDHGDGHC",teacher_id);
                                                                        reference1.child("Tutors").child(teacher_id).child("Assigned").child("Class").child(class_id).child("Section")
                                                                                .child(section_id).child("Subject").child(subject_name).updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    progressDialog.dismiss();
                                    if(AssignResult[0]){
                                        CommonMethods.showSuccessToast(getActivity(),"Subject Assigned to "+teacher_name);
                                    }


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }



        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void updateDay(String type , ArrayList<String> idList) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dayList.clear();
                dayList = CommonMethods.getDayList();
                if (type.equals("update")) {
                    if (idList.size() > 0) {
                        for (int i = 0; i < idList.size(); i++) {
                            dayList.get(Integer.parseInt(idList.get(i)) - 1).setSelected(true);
                        }
                    }
                }
                try {
                    if (dayPopupWindow != null && dayPopupWindow.isShowing()) {
                        if (multiDayAdapter != null) {
                            multiDayAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void showDayPopupDialog(TextView attachedToView , ArrayList<DayModel> dList) {
        CommonMethods.hideSoftKeyboard(getActivity());
        // int width = LinearLayout.LayoutParams.MATCH_PARENT;
        tvAttachedDayPopup = attachedToView;
        int width = attachedToView.getMeasuredWidth();
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_day_popup , null);
        RecyclerView recItem = view.findViewById(R.id.rec_item);
        recItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        attachedToView.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.ic_baseline_arrow_drop_up_gray_24 , 0);

//
        dayPopupWindow = new PopupWindow(view , width , height);
        //window.setContentView(view);
        dayPopupWindow.setFocusable(true);
        dayPopupWindow.setOutsideTouchable(true);
        dayPopupWindow.setOnDismissListener(this);
        dayPopupWindow.showAsDropDown(attachedToView);
        multiDayAdapter = new MultiDayAdapter(getActivity() , dList , new MultiDayAdapter.OnChangeSelectionListener() {
            @Override
            public void onChangeSelection(int position , boolean checked) {
                multiDayAdapter.notifyItemChanged(position);
                selectedDayIdList.clear();
                selectedDayIdList = multiDayAdapter.getSelectedDay();
                //dayPopupWindow.dismiss();
            }
        });
        recItem.setAdapter(multiDayAdapter);

    }

    @Override
    public void onDismiss() {
        //on day popup listener
        tvAttachedDayPopup.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , R.drawable.ic_baseline_arrow_drop_down_24 , 0);
    }


    private void showDatePicker(TextView tvView) {
//
        boolean isValidDate = false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getContext() ,
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getContext() ,
                R.color.calendar_divider_color));


        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month , int day , int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year , month , day);
                String date = sdf.format(c.getTime());


                tvView.setText(date);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                if (dateType.equals("timeTable")) {
                    //validation for timetable date
                    String d = tvView.getText().toString();
                    if (!TextUtils.isEmpty(d)) {
                        if (!validateDate(d)) {
                            tvView.setText("");
//                            spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
                            CommonMethods.showSuccessToast(getActivity() , "You can't select a date earlier than the current date");
                        }
                    }
                } else {
                    //date for add tutor dob
                    String dt = tvView.getText().toString();
                    try {
                        if (!TextUtils.isEmpty(dt)) {
                            if (!CommonMethods.isValidDOB(dt)) {
                                tvView.setText("");
//                                spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
                                CommonMethods.showSuccessToast(getActivity() , "Please select a date before  current date");
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private boolean validateDate(String dt) {
        try {
            return CommonMethods.isValidLiveDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left , R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout , fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {

        }
    }

    private void showAddTutorDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_school_tutor);
        dialog.getWindow();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
//
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
        Button addTutorBtn = (Button) dialog.findViewById(R.id.btn_add);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);

        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count) {
                String mString = s.toString();
                setFieldEnable(true);
                if (!TextUtils.isEmpty(mString)) {
                    if (mString.length() == 10) {
                        if (Integer.parseInt(String.valueOf(mString.charAt(0))) < 6) {
                            et_mobile.setError("Invalid mobile number");
                            et_mobile.requestFocus();
                        } else {
                            if (CommonMethods.checkInternetConnection(getActivity())) {
                                //call get data by tutor mobile
                                sendRequest(ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE);
                            }
                        }
                    } else {
                        if (isShowingExistData) {
                            clearFieldData();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateType = "dob";
                showDatePicker(tv_dob);
            }
        });
        addTutorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmptyField()) {
                    //call pi haree
                    dialog.dismiss();
                    if (CommonMethods.checkInternetConnection(getActivity())) {
                        sendRequest(ApiCode.SCHOOL_ADD_TUTOR);
                    }
                }
            }
        });

    }


    public boolean validateEmptyField() {
        String name = et_name.getText().toString().trim();
        String mobile_num = et_mobile.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String father = et_father.getText().toString().trim();
        String dob = tv_dob.getText().toString().trim();
        String address = et_address.getText().toString().trim();


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
        if (TextUtils.isEmpty(name)) {
            et_name.setError("Enter name");
            et_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Enter email-ID");
            et_email.requestFocus();
            return false;
        }
//
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Invalid email-ID");
            et_email.requestFocus();
            return false;
        }
//
        if (TextUtils.isEmpty(father)) {
            et_father.setError("Enter father name");
            et_father.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dob)) {
            CommonMethods.showSuccessToast(getContext() , "Choose date of birth");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            et_address.setError("Enter address");
            et_address.requestFocus();
            return false;
        }

        return true;
    }

    private void setFieldEnable(boolean value) {
        et_name.setEnabled(value);
        et_email.setEnabled(value);
        et_father.setEnabled(value);
        tv_dob.setEnabled(value);
        et_address.setEnabled(value);
    }

    private void clearFieldData() {
        isShowingExistData = false;
        et_name.getText().clear();
        et_email.getText().clear();
        et_father.getText().clear();
        tv_dob.setText("");
        section_id = "";
        class_id = "";
        et_address.getText().clear();

    }

    private void setRegisteredData(JSONObject data) {
        if (data.length() > 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        // et_mobile.setText(data.getString("mobile"));
                        isShowingExistData = true;
                        et_name.setText(data.getString("name"));
                        et_email.setText(data.getString("email"));
                        et_father.setText(data.getString("father_name"));
                        tv_dob.setText(data.getString("dob"));
                        et_address.setText(data.getString("address"));
//
                        setFieldEnable(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity) getActivity()).setActionBarTitle("Tutors");
    }

    ///---api call--//
    private void callApiRequest() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            sendRequest(ApiCode.SCHOOL_GET_CLASS);
            sendRequest(ApiCode.SCHOOL_GET_TUTOR);
        }
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {
            case ApiCode.SCHOOL_GET_TUTOR:
//                params.put("school_id", school_id);
//                callApi(ApiCode.SCHOOL_GET_TUTOR, params);
                reference.child(firebaseAuth.getUid()).child("Tutors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modellist.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.child("status").getValue(String.class).equals("Approved")) {
                                SchoolCoachingTutorAsginModel schoolCoachingTutorAsginModel = new SchoolCoachingTutorAsginModel();
                                schoolCoachingTutorAsginModel.setId(ds.child("mobile").getValue(String.class));
                                schoolCoachingTutorAsginModel.setMobile(ds.child("mobile").getValue(String.class));
                                schoolCoachingTutorAsginModel.setName(ds.child("name").getValue(String.class));
                                schoolCoachingTutorAsginModel.setPercantage("");
                                schoolCoachingTutorAsginModel.setBlock_status("");

                                modellist.add(schoolCoachingTutorAsginModel);


                            }
                        }
                        initRecyclerView();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case ApiCode.SCHOOL_GET_CLASS:
//                params.put("school_id", school_id);
//                params.put("teacher_id","");
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


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section").addListenerForSingleValueEvent(new ValueEventListener() {
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
            case ApiCode.SCHOOL_GET_SUBJECT:
//                    params.put("class_id", class_id);
//                    params.put("section_id", section_id);
//                    params.put("school_id", school_id);
//                    params.put("type", "0");
//                callApi(ApiCode.SCHOOL_GET_SUBJECT, params);
                reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section")
                        .child(section_id).child("Subjects").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                subject_list.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    AllSubjectModel allSubjectModel = new AllSubjectModel();
                                    allSubjectModel.setSubject_name(ds.child("subject_name").getValue(String.class));
                                    allSubjectModel.setSubject_id(ds.child("subject_name").getValue(String.class));
                                    subject_list.add(allSubjectModel);
                                }
                                subject_list.add(0,new AllSubjectModel("Select Subject","Select Subject"));
                                SpinnerSubjectAdapter spinnerSubjectAdapter = new SpinnerSubjectAdapter();
                                subject_spinner.setAdapter(spinnerSubjectAdapter);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                break;
            case ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL:
                ArrayList<Integer> dayIdList = getSelectId(selectedDayIdList);
                params.put("class_id" , class_id);
                params.put("section_id" , section_id);
                params.put("subject_id" , subject_id);
                params.put("school_id" , school_id);
                params.put("peroid" , period);
                params.put("date" , date);
                params.put("days" , new Gson().toJson(dayIdList));
                params.put("start_time" , sTime);
                params.put("end_time" , eTime);
                params.put("teacher_id" , teacher_id);
                callApi(ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL , params);
                break;
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL:
                ArrayList<Integer> dIdList = getSelectId(selectedDayIdList);
                params.put("class_id" , class_id);
                params.put("section_id" , section_id);
                params.put("subject_id" , subject_id);
                params.put("school_id" , school_id);
                params.put("peroid" , period);
                params.put("date" , date);
                params.put("days" , new Gson().toJson(dIdList));
                params.put("start_time" , sTime);
                params.put("end_time" , eTime);
                params.put("teacher_id" , teacher_id);
                params.put("peroid_id" , period_id);
                callApi(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL , params);
                break;
            case ApiCode.DELETE_TUTOR_BY_SCHOOL:
                params.put("school_id" , school_id);
                params.put("teacher_id" , tutor_id);
                callApi(ApiCode.DELETE_TUTOR_BY_SCHOOL , params);
                break;
            case ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL:
                params.put("school_id" , school_id);
                params.put("teacher_id" , tutor_id);
                params.put("block_status" , blockStatus);
                callApi(ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL , params);
                break;
            case ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL:
                params.put("school_id" , school_id);
                params.put("tutor_id" , tutor_id);
                params.put("class_id" , class_id);
                params.put("section_id" , section_id);
                params.put("subject_id" , subject_id);
                params.put("period_id" , period_id);
                callApi(ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL , params);
                break;

            case ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE:
                params.put("mobile" , et_mobile.getText().toString().trim());
                callApi(ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE , params);
                break;
            case ApiCode.SCHOOL_ADD_TUTOR:

                params.put("school_id" , school_id);
                params.put("mobile" , et_mobile.getText().toString().trim());
                params.put("name" , et_name.getText().toString().trim());
                params.put("email" , et_email.getText().toString().trim());
                params.put("father_name" , et_father.getText().toString().trim());
                params.put("dob" , tv_dob.getText().toString().trim());
                params.put("address" , et_address.getText().toString().trim());
                callApi(ApiCode.SCHOOL_ADD_TUTOR , params);
                break;

        }
    }

    private ArrayList<Integer> getSelectId(ArrayList<String> idList) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String value : idList) {
            list.add(Integer.parseInt(value));
        }
        return list;
    }

    private void callApi(int request , HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback , getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_GET_TUTOR ,
                        BaseUrl.URL_SCHOOL_GET_TUTOR , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_GET_TUTOR);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_GET_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_GET_CLASS ,
                        BaseUrl.URL_SCHOOL_GET_CLASS , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_GET_CLASS);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                service.postDataVolley(ApiCode.SCHOOL_GET_SECTION ,
                        BaseUrl.URL_SCHOOL_GET_SECTION , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_GET_SECTION);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_GET_SUBJECT:
                service.postDataVolley(ApiCode.SCHOOL_GET_SUBJECT ,
                        BaseUrl.URL_SCHOOL_GET_SUBJECT , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_GET_SUBJECT);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL:
                service.postDataVolley(ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL ,
                        BaseUrl.URL_SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL ,
                        BaseUrl.URL_SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL);
                Log.e("params" , params.toString());
                break;
            case ApiCode.DELETE_TUTOR_BY_SCHOOL:
                service.postDataVolley(ApiCode.DELETE_TUTOR_BY_SCHOOL ,
                        BaseUrl.URL_DELETE_TUTOR_BY_SCHOOL , params);
                Log.e("api" , BaseUrl.URL_DELETE_TUTOR_BY_SCHOOL);
                Log.e("params" , params.toString());
                break;
            case ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL:
                service.postDataVolley(ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL ,
                        BaseUrl.URL_BLOCK_UNBLOCK_TUTOR_BY_SCHOOL , params);
                Log.e("api" , BaseUrl.URL_BLOCK_UNBLOCK_TUTOR_BY_SCHOOL);
                Log.e("params" , params.toString());
                break;
            case ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL:
                service.postDataVolley(ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL ,
                        BaseUrl.URL_REMOVE_TIME_TABLE_BY_SCHOOL , params);
                Log.e("api" , BaseUrl.URL_REMOVE_TIME_TABLE_BY_SCHOOL);
                Log.e("params" , params.toString());
                break;

            case ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE:
                service.postDataVolley(ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE ,
                        BaseUrl.URL_SCHOOL_GET_TUTOR_DATA_BY_MOBILE , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_GET_TUTOR_DATA_BY_MOBILE);
                Log.e("params" , params.toString());
                break;
            case ApiCode.SCHOOL_ADD_TUTOR:
                service.postDataVolley(ApiCode.SCHOOL_ADD_TUTOR ,
                        BaseUrl.URL_SCHOOL_ADD_TUTOR , params);
                Log.e("api" , BaseUrl.URL_SCHOOL_ADD_TUTOR);
                Log.e("params" , params.toString());
                break;


        }
    }

    @Override
    public void onResponse(int requestType , String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_TUTOR:
                Log.e("sc_tutor" , response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<SchoolCoachingTutorAsginModel> psearch = new Gson().
                                fromJson(jsonArray.toString() ,
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolCoachingTutorAsginModel>>() {
                                        }.getType());
                        modellist.clear();

                        modellist.addAll(psearch);
                        initRecyclerView();


                    } else {
                        modellist.clear();
                        initRecyclerView();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    modellist.clear();
                    initRecyclerView();
                    CommonMethods.showSuccessToast(getContext() , jsonObject.getString("message"));
                }
////
                break;

            case ApiCode.SCHOOL_GET_CLASS:
                Log.e("sc_login" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("classes");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString() ,
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        classList.clear();
                        classList.add(new AllClassesModel());
                        classList.addAll(psearch);
                        class_adapter.notifyDataSetChanged();

                    } else {
                        classList.clear();
                        classList.add(new AllClassesModel());
                        class_adapter.notifyDataSetChanged();
                    }
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                } else {
                    classList.clear();
                    classList.add(new AllClassesModel());
                    class_adapter.notifyDataSetChanged();
                    CommonMethods.showSuccessToast(getContext() , jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_SECTION:
                Log.e("sc_section" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllClassesModel> psearch = new Gson().
                                fromJson(jsonArray.toString() ,
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllClassesModel>>() {
                                        }.getType());
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        sectionList.addAll(psearch);
                        section_adapter.notifyDataSetChanged();
                    } else {
                        sectionList.clear();
                        sectionList.add(new AllClassesModel());
                        section_adapter.notifyDataSetChanged();

                    }
                } else {
                    sectionList.clear();
                    sectionList.add(new AllClassesModel());
                    section_adapter.notifyDataSetChanged();
                    // CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_GET_SUBJECT:
                Log.e("sc_section" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("section");
                    if (jsonArray.length() > 0) {
                        ArrayList<AllSubjectModel> psearch = new Gson().
                                fromJson(jsonArray.toString() ,
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllSubjectModel>>() {
                                        }.getType());
                        subject_list.clear();
                        subject_list.add(new AllSubjectModel("," , ""));
                        subject_list.addAll(psearch);
                        subjectAdapter.notifyDataSetChanged();
                    } else {
                        subject_list.clear();
                        subject_list.add(new AllSubjectModel("," , ""));
                        subjectAdapter.notifyDataSetChanged();

                    }
                } else {
                    subject_list.clear();
                    subject_list.add(new AllSubjectModel("," , ""));
                    subjectAdapter.notifyDataSetChanged();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL:
                Log.e("sc_add_time" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    if (addUpdateDialog != null) {
                        addUpdateDialog.dismiss();
                    }
                    callApiRequest();
                    subject_id = "";
                    class_id = "";
                    section_id = "";
                    date = "";
                    sTime = "";
                    eTime = "";

                }
                CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL:
                Log.e("sc_update" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    if (addUpdateDialog != null) {
                        addUpdateDialog.dismiss();
                    }
                    callApiRequest();
                    subject_id = "";
                    class_id = "";
                    section_id = "";
                    date = "";
                    sTime = "";
                    eTime = "";

                }
                CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));

                break;
            case ApiCode.DELETE_TUTOR_BY_SCHOOL:
                Log.e("DELETE" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    callApiRequest();
                }
                CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));
                break;
            case ApiCode.BLOCK_UNBLOCK_TUTOR_BY_SCHOOL:
                Log.e("Block_unblock" , response.toString());
                if (jsonObject.getBoolean("response")) {
                    callApiRequest();
                }
                CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));
                break;
            case ApiCode.REMOVE_TIME_TABLE_BY_SCHOOL:
                if (jsonObject.getBoolean("response")) {
                    CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));
                    callApiRequest();
                }
                break;

            case ApiCode.SCHOOL_GET_TUTOR_DATA_BY_MOBILE:
                if (jsonObject.getBoolean("response")) {
                    ///----------------
                    setRegisteredData(jsonObject.getJSONObject("data"));
                }
                break;
            case ApiCode.SCHOOL_ADD_TUTOR:
                CommonMethods.showSuccessToast(getActivity() , jsonObject.getString("message"));
                if (jsonObject.getBoolean("status")) {
                    callApiRequest();
                }
                break;

        }

    }

    //--------------/
    private class ValidateDatabase extends AsyncTask<String, Boolean, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String error = "";
        String type = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();


        }

        @Override
        protected Boolean doInBackground(String... strings) {
            type = strings[0];
            boolean result = true;
            ArrayList<SchoolCoachingTutorAsginModel> assignedTutorList = new ArrayList<>();
            assignedTutorList.addAll(modellist);

            for (int i = 0; i < assignedTutorList.size(); i++) {
                SchoolCoachingTutorAsginModel model = assignedTutorList.get(i);
                String teacherId = model.getId();
                ArrayList<SchoolCoachingTutorAsginClassModel> tableList = model.getAssign_class();
                if (tableList.size() > 0) {
                    if (type.equals("update")) {
                        for (int u = 0; u < tableList.size(); u++) {
                            if (period_id.equals(tableList.get(u).getPeroid_id())) {
                                tableList.remove(u);
                                break;
                            }
                            Log.e("removeID" , "" + period_id);
                            Log.e("removeList" , "" + new Gson().toJson(tableList));
                        }
                    }
                    if (tableList.size() > 0) {
                        for (int j = 0; j < tableList.size(); j++) {
                            SchoolCoachingTutorAsginClassModel
                                    tableModel = tableList.get(j);
                            if (class_id.equals(tableModel.getClass_id())) {
                                if (section_id.equals(tableModel.getSection_id())) {
                                    if (subject_id.equals(tableModel.getSubject_id())) {
                                        if (teacherId.equals(tutor_id)) {
                                            //assign to self
                                            if (period.equals(tableModel.getPeroid())) {
                                                //period is same so check days
                                                ArrayList<String> dList = getDayArray(tableModel.getDays());
                                                Log.e("dayFromDb" , "" + dList);
                                                Log.e("selectedDay" , "" + selectedDayIdList);

                                                dList.retainAll(selectedDayIdList);
                                                Log.e("commonDay" , "" + dList);
                                                if (dList.size() > 0) {
                                                    error = "Day in selected day already assigned";
                                                    result = false;
                                                    break;
                                                }
                                            }

                                        } else {
                                            //assigned to other
                                            error = "Subject already assigned to other teacher";
                                            result = false;
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                    }

                }
                if (!result) {
                    break;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            progressDialog.dismiss();
            if (status) {
//                if (addDialog!=null) {
//                    addDialog.dismiss();
//                }
                if (CommonMethods.checkInternetConnection(getActivity())) {
                    if (type.equals("add")) {
                        sendRequest(ApiCode.SCHOOL_ADD_ASSIGN_PERIOD_BY_SCHOOL);
                    } else {
                        sendRequest(ApiCode.SCHOOL_UPDATE_ASSIGN_PERIOD_BY_SCHOOL);
                    }
                }
            } else {
                CommonMethods.showSuccessToast(getActivity() , error);
            }
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

    class SpinnerSubjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return subject_list.size();
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
            txt_category_name.setText(subject_list.get(position).getSubject_name());


            return row;
        }
    }


}
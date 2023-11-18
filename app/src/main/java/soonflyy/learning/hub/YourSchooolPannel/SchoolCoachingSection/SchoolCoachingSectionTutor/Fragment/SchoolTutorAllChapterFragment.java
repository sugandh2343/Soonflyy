package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

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
import soonflyy.learning.hub.Student_Pannel.Subject_Chapter_Details_Fragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.AllSubjectAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllSubjectModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SchoolTutorAllChapterFragment extends Fragment implements VolleyResponseListener {
private FloatingActionButton feb_chapter;
TextView tv_teacher_name;
    SwipeRefreshLayout swipe;
    RecyclerView rec_chapter;
    AllSubjectAdapter subjectAdapter;
    View linTutorName;
    ArrayList<AllSubjectModel> list=new ArrayList<>();

    String chapter_name,chapter_id,teacher_id,class_id,section_id,teacher_name,student_type;
    String subject_id,pageTitle,from,schoo_id;
    FirebaseAuth firebaseAuth;
    public SchoolTutorAllChapterFragment() {
        // Required empty public constructor
    }


    public static SchoolTutorAllChapterFragment newInstance(String param1, String param2) {
        SchoolTutorAllChapterFragment fragment = new SchoolTutorAllChapterFragment();
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
        View view= inflater.inflate(R.layout.fragment_school_all_chapter, container, false);
        initView(view);
        getArgumentData();
       callRefreshApi();
      //  initRecyclerview();
        init_swipe_method();
        return  view;
    }

    private void getArgumentData() {
        subject_id=getArguments().getString("subject_id");
        pageTitle=getArguments().getString("subject_name");
        class_id=getArguments().getString("class_id");
        from=getArguments().getString("from");
        section_id=getArguments().getString("section_id");
        schoo_id=getArguments().getString("school_id");

        if (from.equals(SCHOOL_TUTOR)){
          //  tv_teacher_name.setVisibility(View.GONE);
            linTutorName.setVisibility(View.GONE);
            teacher_id=new SessionManagement(getActivity()).getString(SCHOOL_TEACHER_ID);
            feb_chapter.setVisibility(View.VISIBLE);
        }else if (from.equals(SCHOOL_COACHING)){
            feb_chapter.setVisibility(View.GONE);
            //tv_teacher_name.setVisibility(View.VISIBLE);
            linTutorName.setVisibility(View.VISIBLE);
        }else if (from.equals(INDEPENDENT_TUTOR)){
            pageTitle="Chapters";
           // tv_teacher_name.setVisibility(View.GONE);
            linTutorName.setVisibility(View.GONE);
            teacher_id=new SessionManagement(getActivity()).getString(SCHOOL_IT_ID);
            feb_chapter.setVisibility(View.VISIBLE);

        }else if (from.equals(SCHOOL_STUDENT)){
           // tv_teacher_name.setVisibility(View.GONE);
            linTutorName.setVisibility(View.GONE);
            feb_chapter.setVisibility(View.GONE);
            student_type=getArguments().getString("student_type");
            if (student_type.equals("itutor")){
                teacher_id=getArguments().getString("itutor_id");
            }
        }

    }

    private void initView(View view) {
        linTutorName=view.findViewById(R.id.lin_name);
        swipe = view.findViewById(R.id.swipe);
        tv_teacher_name=view.findViewById(R.id.tv_teacher_name);
        feb_chapter= view.findViewById(R.id.feb_chapter);
        rec_chapter = view.findViewById(R.id.rec_chapter);
        rec_chapter.hasFixedSize();
        firebaseAuth=FirebaseAuth.getInstance();
        rec_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_chapter.setLayoutManager(layoutManager);
        rec_chapter.setKeepScreenOn(true);
        feb_chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddChapterDailoge("add");

            }
        });

    }
    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                callRefreshApi();
               // initRecyclerview();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }
    private  void callRefreshApi(){
        if (CommonMethods.checkInternetConnection(getActivity())){
            if (from.equals(SCHOOL_COACHING)|| from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT);
            }else {
                sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
            }
        }
    }




    private void showAddChapterDailoge(String type) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailoge_school_addchapter);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
      //  TextView tv_c_title = dialog.findViewById(R.id.tv_c_title);
        EditText et_name = dialog.findViewById(R.id.et_name);
        Button btn_save = (Button) dialog.findViewById(R.id.btn_add);

        if (type.equals("update")){
            btn_save.setText("Update");
            et_name.setText(chapter_name);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    et_name.setError("Enter chapter name");
                    et_name.requestFocus();
                } else {
                    chapter_name = et_name.getText().toString().trim();
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        if (type.equals("add"))
                            sendRequest(ApiCode.SCHOOL_ADD_CHAPTER);
                        else
                            sendRequest(ApiCode.SCHOOL_UPDATE_CHAPTER);
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
        dialog.show();

    }


    private void initRecyclerview() {

//        list = new ArrayList<>();
//
//        list.add(new AllSubjectModel("Chapter-01","Polynomials"));
//        list.add(new AllSubjectModel("Chapter-02","Quadratic Equations"));
//        list.add(new AllSubjectModel("Chapter-03","Arithmetic Progressions"));
//        list.add(new AllSubjectModel("Chapter-04","Coordinate Geometry"));

        subjectAdapter= new AllSubjectAdapter("all_chapter",getActivity(), from,list, new AllSubjectAdapter.OnSelectSubjectClickListener() {
            @Override
            public void onItemClick(int postion) {
                Fragment fragment=null;
                if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                    //fragment = new Scl_StudentChapterDetailsFragment();
                    fragment=new Subject_Chapter_Details_Fragment();

                }
                else{
                     fragment = new Scl_SelectChpterDeatailFragment ();
                }

                Bundle bundle=new Bundle();
                bundle.putString("teacher_id",teacher_id);
                bundle.putString("from",from);
                bundle.putString("subject_id",subject_id);
                bundle.putString("class_id",class_id);
                bundle.putString("section_id",section_id);
                bundle.putString("school_id",schoo_id);
                bundle.putString("teacher_name",teacher_name);
                bundle.putString("chapter_id",list.get(postion).getChapter_id());
                bundle.putString("chapter_name",list.get(postion).getChapter_name());
                fragment.setArguments(bundle);
                Log.e("teacher_id",""+teacher_id);
               // SwitchFragment (fragment);
                //comment by swati---- comment switchfragment method because its crash on this fragment

                ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);

            }

            @Override
            public void onDelete(int position) {
                AllSubjectModel model=list.get(position);
                chapter_id=model.getChapter_id();
                showDeleteDailog();
            }

            @Override
            public void onEdit(int position) {
                AllSubjectModel model=list.get(position);
                chapter_id=model.getChapter_id();
                chapter_name=model.getChapter_name();
                showAddChapterDailoge("update");


            }
        });
        rec_chapter.setAdapter(subjectAdapter);
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
                            sendRequest(ApiCode.SCHOOL_DELETE_CHAPTER);
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
        ((SchoolMainActivity)getActivity()).setActionBarTitle(pageTitle);
    }

    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request) {

            case ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT:
                Log.e("Callkkhukb",firebaseAuth.getUid());
//                params.put("subject_id", subject_id);
//                callApi(ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT, params);
                reference.child(firebaseAuth.getUid()).child("Class").child(class_id).child("Section")
                        .child(section_id).child("Subjects").child(subject_id).child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            AllSubjectModel allSubjectModel = new AllSubjectModel();
                            allSubjectModel.setChapter_id(ds.child("chapter_name").getValue(String.class));
                            allSubjectModel.setChapter_name(ds.child("chapter_name").getValue(String.class));
                            list.add(allSubjectModel);







                        }
                        Log.e("SubjectList",""+list.size());
                        initRecyclerview();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case ApiCode.SCHOOL_GET_CHAPTER:
                if (from.equals(SCHOOL_COACHING)){
                    teacher_id="101";
                }
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                if(from.equals("s_tutor")){
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("SchoolId").getValue(String.class)!=null&&ds.child("SchoolId").getValue(String.class).equals(schoo_id)){
                                    Log.e("DASFXS",ds.getKey());
                                    Log.e("DASFXS",class_id);
                                    Log.e("DASFXS",section_id);
                                    Log.e("DASFXS",subject_id);

                                    reference.child(ds.getKey()).child("Class").child(class_id).child("Section")
                                            .child(section_id).child("Subjects").child(subject_id).child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    Log.e("Snapshobjibfjkb",""+snapshot.getChildrenCount());
                                                    list.clear();
                                                    for(DataSnapshot ds:snapshot.getChildren()){
                                                        AllSubjectModel allSubjectModel = new AllSubjectModel();
                                                        allSubjectModel.setChapter_id(ds.child("chapter_name").getValue(String.class));
                                                        allSubjectModel.setChapter_name(ds.child("chapter_name").getValue(String.class));
                                                        list.add(allSubjectModel);

                                                    }
                                                    Log.e("SubjectList",""+list.size());
                                                    initRecyclerview();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else if(from.equals("i_tutor")){
                    reference.child(firebaseAuth.getUid()).child("Subjects").child(subject_id)
                            .child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list.clear();
                                    for(DataSnapshot ds:snapshot.getChildren()){
                                        AllSubjectModel allSubjectModel = new AllSubjectModel();
                                        allSubjectModel.setChapter_id(ds.child("chapter_name").getValue(String.class));
                                        allSubjectModel.setChapter_name(ds.child("chapter_name").getValue(String.class));
                                        list.add(allSubjectModel);
                                    }
                                    initRecyclerview();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }


//                callApi(ApiCode.SCHOOL_GET_CHAPTER, params);
//                break;
                break;
            case ApiCode.SCHOOL_ADD_CHAPTER:
                if(from.equals("s_tutor")){
                    params.put("teacher_id", teacher_id);
                    params.put("subject_id", subject_id);
                    params.put("chapter_name", chapter_name);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(ds.child("SchoolId").getValue(String.class)!=null&&ds.child("SchoolId").getValue(String.class).equals(schoo_id)){
                                    Log.e("DASFXS",ds.getKey());
                                    Boolean chapterResult=false;
                                    for(DataSnapshot ds1:ds.child("Class").child(class_id).child("Section")
                                            .child(section_id).child("Subjects").child(subject_id).child("Chapters").getChildren()){
                                        if(ds1.child("chapter_name").getValue(String.class).equals(chapter_name)){
                                            chapterResult=true;
                                        }
                                    }
                                    if(chapterResult){

                                        CommonMethods.showSuccessToast(getActivity(),"Chapter with same name already exist ");
                                        sendRequest(ApiCode.SCHOOL_GET_CHAPTER);

                                    }else{
                                        reference.child(ds.getKey()).child("Class").child(class_id).child("Section")
                                                .child(section_id).child("Subjects").child(subject_id).child("Chapters")
                                                .child(String.valueOf(ds.child("Class").child(class_id).child("Section").child(section_id).child("Subjects").child(subject_id).child("Chapters").getChildrenCount()+1))
                                                .setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
                                                    }
                                                });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(from.equals("i_tutor")){
                    params.put("teacher_id", firebaseAuth.getUid());
                    params.put("subject_id", subject_id);
                    params.put("chapter_name", chapter_name);
                    reference.child(firebaseAuth.getUid()).child("Subjects").child(subject_id).child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean chapterValue=false;
                            for(DataSnapshot ds:snapshot.getChildren()){
                                if(ds.child("chapter_name").getValue(String.class).equals(chapter_name)){
                                    chapterValue=true;
                                }

                            }
                            if(chapterValue){
                                CommonMethods.showSuccessToast(getActivity(),"Chapter with same name already exist");
                            }else{
                                reference.child(firebaseAuth.getUid()).child("Subjects").child(subject_id).child("Chapters")
                                        .child(chapter_name).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                CommonMethods.showSuccessToast(getActivity(),"Chapter Added Successfully");
                                                sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


                break;
            case ApiCode.SCHOOL_UPDATE_CHAPTER:
                params.put("chapter_id",chapter_id);
                 params.put("chapter_name", chapter_name);
                callApi(ApiCode.SCHOOL_UPDATE_CHAPTER, params);
                break;
            case ApiCode.SCHOOL_DELETE_CHAPTER:

                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_DELETE_CHAPTER, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT:
                service.postDataVolley(ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT,
                        BaseUrl.URL_SCHOOL_GET_CHAPTER_BY_STUDENT, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CHAPTER_BY_STUDENT);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_GET_CHAPTER:
                service.postDataVolley(ApiCode.SCHOOL_GET_CHAPTER,
                        BaseUrl.URL_SCHOOL_GET_CHAPTER, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_CHAPTER);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_CHAPTER:
                service.postDataVolley(ApiCode.SCHOOL_ADD_CHAPTER,
                        BaseUrl.URL_SCHOOL_ADD_CHAPTER, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_CHAPTER);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_CHAPTER:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_CHAPTER,
                        BaseUrl.URL_SCHOOL_UPDATE_CHAPTER, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_CHAPTER);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_CHAPTER:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_CHAPTER,
                        BaseUrl.URL_SCHOOL_DELETE_CHAPTER, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_CHAPTER);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_CHAPTER:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("section");
                    if (jsonArray.length()>0){
                        ArrayList<AllSubjectModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllSubjectModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                        teacher_name=jsonObject.getString("teacher_name");
                        tv_teacher_name.setText("Teacher - "+teacher_name);
                        teacher_id=jsonObject.getString("teacher_id");

                    }else{
                        list.clear();
                        initRecyclerview();
                    }
                } else {
                    list.clear();
                    initRecyclerview();
                    //CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
            case ApiCode.SCHOOL_GET_CHAPTER_BY_STUDENT:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("response")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<AllSubjectModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<AllSubjectModel>>() {
                                        }.getType());
                        list.clear();
                        list.addAll(psearch);
                        initRecyclerview();
                        teacher_name=jsonObject.getString("teacher_name");
                        tv_teacher_name.setText("Teacher - "+teacher_name);
                        teacher_id=jsonObject.getString("teacher_id");

                    }else{
                        list.clear();
                        initRecyclerview();
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
//
                break;
            case ApiCode.SCHOOL_ADD_CHAPTER:
                Log.e("sc_add_sec", response);
                if (jsonObject.getBoolean("response")){
                    //sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
                    callRefreshApi();
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_CHAPTER:
                Log.e("sc_up_sec", response);
                if (jsonObject.getBoolean("response")){
                    //sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
                    callRefreshApi();
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_CHAPTER:
                Log.e("delete_sec", response);
                if (jsonObject.getBoolean("response")){
                   // sendRequest(ApiCode.SCHOOL_GET_CHAPTER);
                    callRefreshApi();
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
        }
    }



}
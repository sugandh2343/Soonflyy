package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.TopicFragment;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.TeacherSubject_Adapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.Model.SubjectModel;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.AssigntTProfileDetailsFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.model.TeacherSubject_Model;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCourseSubjectFragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener {
    float dX;
    float dY;
    int lastAction;
    private FloatingActionButton chat_btn;


    RecyclerView rec_subject;
    FloatingActionButton add_sub_btn;
    NestedScrollView scrollView;
    SwipeRefreshLayout refreshLayout;
    TeacherSubject_Adapter adapter;
    ArrayList<TeacherSubject_Model> clist = new ArrayList<>();
    String course_id, sub_title, subImageString, section_id;

    ImageView choose_image, cover_image;
    RelativeLayout rel_image;
    CircleImageView cancel_image;
    TextView tv_imageName;
    String title, imageString = "",updateImgUrl="";
    ArrayList<SubjectModel> subjectList = new ArrayList<>();
    String url;
    boolean bottomFocus=false;
    SessionManagement sessionManagement;
    FirebaseAuth firebaseAuth;
    Uri imgUri;


    public MyCourseSubjectFragment() {
        // Required empty public constructor
    }


    public static MyCourseSubjectFragment newInstance(String param1, String param2) {
        MyCourseSubjectFragment fragment = new MyCourseSubjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_course_subject, container, false);
      sessionManagement=new SessionManagement(getActivity());
      firebaseAuth=FirebaseAuth.getInstance();
        initView(view);
        getArgumentData();
        sendApiRequest();
        initControl();
       // setSubjectData();

        refreshLayout.setOnRefreshListener(this);
        add_sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUpdaeDialog("add");
            }
        });
        return view;

    }

    private void sendApiRequest() {
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_TEACHER_SUBJECTS);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void getArgumentData() {
        course_id = getArguments().getString("course_id");
        title=getArguments().getString("course_title");
    }

    private void setSubjectData() {

        // rec_subject.setHasFixedSize(true);
        Log.e("scount",""+clist.size());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_subject.setLayoutManager(linearLayoutManager);
        rec_subject.setKeepScreenOn(true);

        adapter = new TeacherSubject_Adapter(getContext(),"assignTo", clist, new TeacherSubject_Adapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                TeacherSubject_Model model = clist.get(postion);
               // Mycourse_deailFragment fragment = new Mycourse_deailFragment();
                Fragment fragment=null;
                Bundle bundle = new Bundle();
                String assignedValue= model.getAssigned_value();
                if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)
                || assignedValue.equals("-1")){
                    fragment=new T_Subject_ChaptersFragment();
                    bundle.putString("course_id", model.getCourse_id());
                    bundle.putString("subject_id", model.getId());
                    bundle.putString("course_title",title );//title+
                    bundle.putString("subName",model.getTitle());
                    if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
                        AssignProfile profileData= (AssignProfile) getArguments().getSerializable("profileData");
                        bundle.putSerializable("profileData",profileData);
                    }
                } else{
                        //assign to case
                    fragment=new TopicFragment();
                    bundle.putString("from",SIMPLEE_HOME_TUTOR);
                    bundle.putString("subject_id",model.getId());
                    bundle.putString("course_id",model.getCourse_id());
                    bundle.putString("course_name",title);
                    bundle.putString("subject_name",model.getTitle());
                    bundle.putInt("subjectPosition",postion+1);
                    //-----------for assign to profile-----//
                    AssignProfile profile=model.getAssigned_to();
                    String mobile="+91-"+ profile.getMobile();
                    String image=BaseUrl.BASE_URL_MEDIA+profile.getImage();
                   AssignProfile profiledata=new AssignProfile(profile.getId(),profile.getName(),mobile,image);
                    bundle.putSerializable("profileData",profiledata);
                }

                if (fragment!=null) {
                    fragment.setArguments(bundle);
                    ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                }
            }

            @Override
            public void onDelete(int position) {

                section_id = clist.get(position).getId();
              showAlertForDelete();
            }

            @Override
            public void onEdit(int position) {
                TeacherSubject_Model model = clist.get(position);

                section_id = model.getId();
                sub_title = model.getTitle();
                url = model.getCover_image();
                showAddUpdaeDialog("update");
//                if (ConnectivityReceiver.isConnected()){
//                    sendRequest(ApiCode.UPDATE_TEACHER_SUBJECTS);
//                }else{
//                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
//                }

            }

            @Override
            public void onProfileClick(int position) {
                //------
                String assignValue="";
                //------------
                Bundle bundle=new Bundle();
                AssignProfile profile=null;
                if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
                  profile=clist.get(position).getAssigned_by();
                    assignValue="1";
                }else{
                    profile=clist.get(position).getAssigned_to();
                    assignValue="0";
                }
                if (profile!=null) {
                    AssigntTProfileDetailsFragment fragment=new AssigntTProfileDetailsFragment();
                    bundle.putString("id", profile.getId());
                    bundle.putString("name", profile.getName());
                    bundle.putString("assignValue",assignValue);
                    fragment.setArguments(bundle);
                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
                }
            }
        });
        rec_subject.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.e("scount",""+adapter.getItemCount());

    }

    private void showAlertForDelete() {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setCancelable(false)
                .setMessage("Are you sure to delete ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ConnectivityReceiver.isConnected()) {
                    sendRequest(ApiCode.DELETE_TEACHER_SUBJECTS);
                } else {
                    CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                }
            }
        }).show();
    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    private void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.frame_layout_container, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.frame_layout_container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void initView(View view) {
        scrollView=view.findViewById(R.id.nested_scroll);
        chat_btn = view.findViewById(R.id.floatingActionButton);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        rec_subject = view.findViewById(R.id.rec_subject);
        add_sub_btn = view.findViewById(R.id.floatingActionButton);
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.ADD_SECTION:
                String data=new Gson().toJson(subjectList);
                String timestamp = "" + System.currentTimeMillis();
                String currentDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentDate = new android.icu.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(new Date());
                }

                String currentTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentTime = new android.icu.text.SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date());
                }
                String[] separataed=currentDate.split("-");
                String filePathAndName = "UserDetails/" +firebaseAuth.getUid()+"/" + "Course" + "/" +  course_id;
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
                String finalCurrentDate = currentDate;
                String finalCurrentTime = currentTime;
                if(imgUri!=null) {
                    String finalCurrentDate1 = currentDate;
                    storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();
                            HashMap<String,Object> params=new HashMap<>();
                            params.put("course_id",course_id);
                            params.put("sub_id",timestamp);
                            params.put("title",subjectList.get(0).getTitle());
                            params.put("section_thumbnail",""+downloadImageUri);
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
                            reference.child(firebaseAuth.getUid()).child(course_id).child("Subject").child(timestamp)
                                    .setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            sub_title="";
                                            imageString="";
                                            subjectList.clear();
                                            bottomFocus=true;
                                            sendApiRequest();

                                        }
                                    });
                        }
                    });
                }

//                callApi(ApiCode.ADD_SECTION, params);
                break;
            case ApiCode.GET_TEACHER_SUBJECTS:
                // params.put("type","1");
//                params.put("course_id", course_id);
//                if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)) {
//                    params.put("type","2");
//                }else{
//                    params.put("type","-1");
//                }
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Courses");
                reference.child(firebaseAuth.getUid()).child(course_id).child("Subject").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clist.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            TeacherSubject_Model model=new TeacherSubject_Model();
                            model.setCourse_id(ds.child("course_id").getValue(String.class));
                            model.setTitle(ds.child("title").getValue(String.class));
                            model.setCover_image(ds.child("section_thumbnail").getValue(String.class));
                            model.setId(ds.child("sub_id").getValue(String.class));
                            clist.add(model);
                        }
                        Log.e("CHFYDHTVJ",""+clist.size());
                        setSubjectData();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //params.put("user_id",new Session_management(getContext()).getString(USER_ID));
//                callApi(ApiCode.GET_TEACHER_SUBJECTS, params);
                break;
            case ApiCode.DELETE_TEACHER_SUBJECTS:
                params.put("section_id", section_id);
                //params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.DELETE_TEACHER_SUBJECTS, params);
                break;
            case ApiCode.UPDATE_TEACHER_SUBJECTS:
                params.put("section_id", section_id);
                params.put("title", sub_title);
                params.put("cover_image", imageString);
                //params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.UPDATE_TEACHER_SUBJECTS, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.ADD_SECTION:
                service.postDataVolley(ApiCode.ADD_SECTION,
                        BaseUrl.URL_ADD_SECTION, params);
                break;
            case ApiCode.GET_TEACHER_SUBJECTS:
                service.postDataVolley(ApiCode.GET_TEACHER_SUBJECTS,
                        BaseUrl.URL_GET_TEACHER_SUBJECTS, params);
                break;
            case ApiCode.DELETE_TEACHER_SUBJECTS:
                service.postDataVolley(ApiCode.DELETE_TEACHER_SUBJECTS,
                        BaseUrl.URL_DELETE_TEACHER_SUBJECTS, params);
                break;
            case ApiCode.UPDATE_TEACHER_SUBJECTS:
                service.postDataVolley(ApiCode.UPDATE_TEACHER_SUBJECTS,
                        BaseUrl.URL_UPDATE_TEACHER_SUBJECTS, params);
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.ADD_SECTION:
                Log.e("subject ",response);
                if (jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Subject Added Successfully");
                    sub_title="";
                    imageString="";
                    subjectList.clear();
                    bottomFocus=true;
                    sendApiRequest();
                }
                break;
            case ApiCode.GET_TEACHER_SUBJECTS:
                Log.e("subject_list", response);

                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<TeacherSubject_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<TeacherSubject_Model>>() {
                                        }.getType());
                        clist.clear();
                        clist.addAll(psearch);
                        Log.e("ssize",""+clist.size());
                        setSubjectData();
                        // testAdapter.notifyDataSetChanged();
                    } else {
                        clist.clear();
                        setSubjectData();
                        CommonMethods.showSuccessToast(getContext(), "No Subjects Available");
                    }

                }else{
                    clist.clear();
                    setSubjectData();
                    CommonMethods.showSuccessToast(getContext(), "No Subjects Available");
                }
                if (bottomFocus) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                            bottomFocus=false;
                        }
                    });
                }
                break;
            case ApiCode.DELETE_TEACHER_SUBJECTS:
                Log.e("delete_suject", response);
                if (jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Subject Deleted Successfully");
                    bottomFocus=true;
                    sendApiRequest();
                }
                break;
            case ApiCode.UPDATE_TEACHER_SUBJECTS:
                Log.e("update_sub", response);
                if (jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),"Subject Updated Successfully");
                    sub_title="";
                    imageString="";
                    bottomFocus=true;
                    sendApiRequest();
                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();

    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    //show dialog
                    showAddUpdaeDialog("add");

                break;

            default:
                return false;
        }
        return true;

    }

    private void showAddUpdaeDialog(String type) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_update_subject);
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
        TextView tv_title=dialog.findViewById(R.id.tv_title);
        tv_imageName = dialog.findViewById(R.id.tv_name_image);
        cover_image = dialog.findViewById(R.id.image_view);
        cancel_image = dialog.findViewById(R.id.cancel_image);
        rel_image = dialog.findViewById(R.id.rel_cover_image);
        choose_image = dialog.findViewById(R.id.iv_sub);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        TextView tv_back = dialog.findViewById(R.id.tv_back);
        if (type.equals("add")) {
            tv_title.setText("Add Subject");
            btn_save.setText("Add");
            choose_image.setVisibility(View.VISIBLE);
            rel_image.setVisibility(View.GONE);
            tv_imageName.setText("Upload your file here, choose file form your device");

        } else if (type.equals("update")) {
            tv_title.setText("Update Subject");
            btn_save.setText("Update");
            choose_image.setVisibility(View.GONE);
            rel_image.setVisibility(View.VISIBLE);
            et_name.setText(sub_title);
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + url)
                    .into(cover_image);
            tv_imageName.setText("cover_image.jpeg");
            updateImgUrl=url;

        }
        dialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("update")) {
                    if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                        et_name.setError("Enter subject title");
                        et_name.requestFocus();
                    }else if (TextUtils.isEmpty(imageString) && TextUtils.isEmpty(updateImgUrl)) {
                        CommonMethods.showSuccessToast(getContext(), "Choose cover image");
                    }else if (sub_title.equals(et_name.getText().toString().trim())
                            && TextUtils.isEmpty(imageString)&& !TextUtils.isEmpty(updateImgUrl)) {
                        CommonMethods.showSuccessToast(getContext(), "You have not change any data");
                    } else {
                        sub_title=et_name.getText().toString().trim();
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.UPDATE_TEACHER_SUBJECTS);
                            dialog.dismiss();
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                        }
                    }
                }else{
                    if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                        et_name.setError("Enter subject title");
                        et_name.requestFocus();
                    }else if (TextUtils.isEmpty(imageString)){
                        CommonMethods.showSuccessToast(getContext(),"Choose cover image");
                    }else{
                        sub_title=et_name.getText().toString().trim();
                        subjectList.add(new SubjectModel(""));
                        subjectList.get(0).setTitle(sub_title);
                        subjectList.get(0).setSection_thumbnail(imageString);
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.ADD_SECTION);
                            dialog.dismiss();
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                        }
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

        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateImgUrl="";
                rel_image.setVisibility(View.GONE);
                choose_image.setVisibility(View.VISIBLE);
                tv_imageName.setText("Upload your file here,Choose file form your device");
            }
        });
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        dialog.setCanceledOnTouchOutside(false);

    }

    private void chooseImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {
                imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);
                Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(cover_image);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                rel_image.setVisibility(View.VISIBLE);
                choose_image.setVisibility(View.GONE);
                tv_imageName.setText("cover_image.jpeg");
                //DynamicToast.make(getContext(), "Cover image selected for upload", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(title,false);
        ((Mycourse_deailFragment)getParentFragment()).showSubjectBg();
        ((Mycourse_deailFragment)getParentFragment()).showAssignProfile();
    }
}
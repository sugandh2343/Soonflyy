package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
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
import soonflyy.learning.hub.Teacher_Pannel.Adapter.AssignTeacherAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_ChatperAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_LiveAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignTutors;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_ChapterModel;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.live.MeetingActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class T_Subject_ChaptersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener, VolleyResponseListener, View.OnClickListener {
    float dX;
    float dY;
    int lastAction;
    RecyclerView rec_todayLive, rec_chapter;
    FloatingActionButton add_chapterBtn;
    SwipeRefreshLayout refreshLayout;
    NestedScrollView nestedScrollView;
    TextView tvTodayTitle,tvProfileName;
    ImageView tvMobileNo;
    CircleImageView ivProfileImg;

    ArrayList<T_LiveModel> liveList = new ArrayList<>();
    T_LiveAdapter liveAdapter;

    ArrayList<T_ChapterModel> chapterList = new ArrayList<>();
    T_ChatperAdapter chatperAdapter;

    String subject_id, course_id, chapter_title = "", chapter_id, course_title, subName,assignToId="",assignToName="";
    FirebaseAuth firebaseAuth;


    boolean bottomFocus = false;
    AssignProfile assignProfile;

    //----------new module-----//
    ArrayList<AssignTutors> teacherList = new ArrayList<>();
    TextView tvAssignSub;
    View includeAssignSub,profileLayoutView,noticeLayout;
    SessionManagement sessionManagement;
    //----------------------------//



    public T_Subject_ChaptersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t__subject__chapters, container, false);
       sessionManagement=new SessionManagement(getActivity());
       firebaseAuth=FirebaseAuth.getInstance();
        bindId(view);
        showAssignProfile();
        getArgumentData();
        sendApiCall();
        //  setTodayLiveList();
        setChapterList();

        return view;
    }

    private void getArgumentData() {
        subject_id = getArguments().getString("subject_id");
        Log.e("SubjectId",subject_id);
        course_id = getArguments().getString("course_id");
        course_title = getArguments().getString("course_title");
        subName = getArguments().getString("subName");
        if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)) {
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
            setProfileData(assignProfile);
        }
    }

    private void setProfileData(AssignProfile profile) {
        if (profile!=null) {
            String link = BaseUrl.BASE_URL_MEDIA + profile.getImage();
            Log.e("image", link);
            Picasso.get().load(link).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            tvProfileName.setText(profile.getName());
//            tvMobileNo.setText("+91-" + profile.getMobile());
        }
    }

    private void showAssignProfile(){
        String assignValue=sessionManagement.getString(ASSIGN);
        if (assignValue !=null){
            if (assignValue.equals(ASSIGN_BY)) {
                profileLayoutView.setVisibility(View.VISIBLE);
                includeAssignSub.setVisibility(View.GONE);
            }
            else
                profileLayoutView.setVisibility(View.GONE);
        }else{
            profileLayoutView.setVisibility(View.GONE);
        }
    }
    private void bindId(View view) {

        noticeLayout=view.findViewById(R.id.include_notice);
        tvProfileName=view.findViewById(R.id.assign_tv_name);
        tvMobileNo=view.findViewById(R.id.assign_tv_mobile);
        ivProfileImg=view.findViewById(R.id.assign_iv_profile_img);
        profileLayoutView=view.findViewById(R.id.include_assign_profile);
        profileLayoutView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white_smoke));

        tvAssignSub=view.findViewById(R.id.tv_assign);
        includeAssignSub=view.findViewById(R.id.include_assign_sub);
        tvAssignSub.setText("Assign Subject");

        tvTodayTitle = view.findViewById(R.id.tv_today_title);
        nestedScrollView = view.findViewById(R.id.nested_scroll);
        rec_todayLive = view.findViewById(R.id.rec_live);
        rec_chapter = view.findViewById(R.id.rec_chapter);
        add_chapterBtn = view.findViewById(R.id.add_chapter);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        rec_todayLive.setLayoutManager(new LinearLayoutManager(getContext()));
        rec_chapter.setLayoutManager(new LinearLayoutManager(getContext()));


        refreshLayout.setOnRefreshListener(this);
        includeAssignSub.setOnClickListener(this);
        noticeLayout.setOnClickListener(this);
        //  add_chapterBtn.setOnTouchListener(this);
        add_chapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("add");
            }
        });

    }

    private void setTodayLiveList() {
        //  liveList.add(new T_LiveModel());
        liveAdapter = new T_LiveAdapter(getContext(), liveList, "today", new T_LiveAdapter.OnLiveClickListener() {
            @Override
            public void onLiveClick(int position) {
                //
            }

            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onEdit(int position) {

            }

            @Override
            public void onGoLive(int position) {
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
                    T_LiveModel model = liveList.get(position);
//                    LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("live_type","chapter");
//                    bundle.putString("type","teacher");
//                    bundle.putString("slug",model.getSlug());
//                    bundle.putString("description",model.getDescription());
//                    fragment.setArguments(bundle);
//                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
                    Intent lIntent = new Intent(getActivity(), MeetingActivity.class);
                    lIntent.putExtra("title", model.getTitle());
                    lIntent.putExtra("sTime", model.getStart_time());
                    lIntent.putExtra("eTime", model.getEnd_time());
                    lIntent.putExtra("slug", model.getSlug());
                    lIntent.putExtra("live_type", "chapter");
                    lIntent.putExtra("from",SIMPLEE_HOME_TUTOR);
                    lIntent.putExtra("live_id",model.getId());
                    lIntent.putExtra("type", "teacher");
                    lIntent.putExtra("description", model.getDescription());
                    getActivity().startActivity(lIntent);

                } else {
                    CommonMethods.requestAudioCameraPermission(getActivity(), 333);
                }


            }
        });

        rec_todayLive.setAdapter(liveAdapter);
        if (liveAdapter.getItemCount() > 0) {
            tvTodayTitle.setVisibility(View.VISIBLE);
        } else {
            tvTodayTitle.setVisibility(View.GONE);
        }

    }

    private void setChapterList() {
        // chapterList.add(new T_ChapterModel());
        chatperAdapter = new T_ChatperAdapter(getContext(), chapterList, new T_ChatperAdapter.OnChapterListener() {
            @Override
            public void onChapterClick(int position) {
                T_ChapterDetailFragment fragment = new T_ChapterDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("course_id", course_id);
                bundle.putString("subject_id", subject_id);
                bundle.putString("title", course_title + "(" + chapterList.get(position).getTitle() + ")");
                bundle.putString("chapter_id", chapterList.get(position).getId());

                if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
                    bundle.putSerializable("profileData",assignProfile);
                }
                fragment.setArguments(bundle);
                ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
            }

            @Override
            public void onDelete(int position) {
                chapter_id = chapterList.get(position).getId();
                showDeleteAlert();
            }

            @Override
            public void onEdit(int position) {
                T_ChapterModel model = chapterList.get(position);
                chapter_id = model.getId();
                chapter_title = model.getTitle();
                showDialog("update");
            }
        });
        rec_chapter.setAdapter(chatperAdapter);

    }

    private void showDeleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setMessage("Delete")
                .setMessage("Are you sure to delete chapter?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendRequest(ApiCode.DELETE_CHAPTERS);
                        dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiCall();
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()) {
            //call api
            sendRequest(ApiCode.GET_LIVE_CLASSES);
            sendRequest(ApiCode.GET_SUBJECT_CHAPTERS);
            sendRequest(ApiCode.GET_TUTOR_FOR_ASSIGN);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(subName, false);
        // ((Mycourse_deailFragment)getParentFragment()).showSubjectBg();

        sendApiCall();
    }


    private void sendRequest(int request) {
        HashMap<String, Object> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_SUBJECT_CHAPTERS:
                params.put("section_id", subject_id);
                params.put("course_id", course_id);
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Courses");
                ref.child(firebaseAuth.getUid()).child(course_id).child("Subject").child(subject_id).child("Chapters").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chapterList.clear();


                        for(DataSnapshot ds:snapshot.getChildren()){
                            T_ChapterModel t_chapterModel=new T_ChapterModel();
                            t_chapterModel.setId(ds.child("chapterId").getValue(String.class));
                            t_chapterModel.setCourse_id(ds.child("course_id").getValue(String.class));
                            t_chapterModel.setSection_id(ds.child("section_id").getValue(String.class));
                            t_chapterModel.setTitle(ds.child("title").getValue(String.class));
                            chapterList.add(t_chapterModel);

                        }
                        setChapterList();
                        if (bottomFocus) {
                            nestedScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    bottomFocus = false;
                                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
//                callApi(ApiCode.GET_SUBJECT_CHAPTERS, params);
                break;
            case ApiCode.ADD_SUBJECT_CHAPTERS:
                String timestamp=""+System.currentTimeMillis();
                params.put("course_id", course_id);
                params.put("section_id", subject_id);
                params.put("title", chapter_title);
                params.put("chapterId", timestamp);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
//                jhfhgcmtdtmmtd
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
                reference.child(firebaseAuth.getUid()).child(course_id).child("Subject").child(subject_id)
                                .child("Chapters").child(timestamp)
                                .setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CommonMethods.showSuccessToast(getContext(), "Chapter added successfully");
                                chapter_title = "";
                                bottomFocus = true;
                                sendApiCall();
                            }
                        });

//                callApi(ApiCode.ADD_SUBJECT_CHAPTERS, params);
                break;
            case ApiCode.UPDATE_SUBJECT_CHAPTERS:
                params.put("id", chapter_id);
                params.put("course_id", course_id);
                params.put("section_id", subject_id);
                params.put("title", chapter_title);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
//                callApi(ApiCode.UPDATE_SUBJECT_CHAPTERS, params);
                break;
            case ApiCode.DELETE_CHAPTERS:
                params.put("id", chapter_id);
                //params.put("msg",message);
                //params.put("user_id",new Session_management(getContext()).getString(USER_ID));
//                callApi(ApiCode.DELETE_CHAPTERS, params);
                break;
            case ApiCode.GET_LIVE_CLASSES:
                params.put("chapter_id", " ");
                params.put("section_id", subject_id);
                params.put("course_id", course_id);
                params.put("type", "today");
                params.put("user_id", new SessionManagement(getActivity()).getString(USER_ID));
//                callApi(ApiCode.GET_LIVE_CLASSES, params);
                break;

            case ApiCode.GET_TUTOR_FOR_ASSIGN:
                //params.put("type", "course");
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
//                callApi(ApiCode.GET_TUTOR_FOR_ASSIGN, params);
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        teacherList.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Log.e("GHFDFD",ds.child("type").getValue(String.class));
                            if(ds.child("type").exists()&& ds.child("type").getValue(String.class).equals("1")){
                                if(!ds.child("uid").getValue(String.class).equals(firebaseAuth.getUid())){
                                    AssignTutors assignTutors=new AssignTutors();
                                    assignTutors.setImage(ds.child("image").getValue(String.class));
                                    assignTutors.setName(ds.child("name").getValue(String.class));
                                    assignTutors.setMobile(ds.child("mobile").getValue(String.class));
                                    assignTutors.setUid(ds.child("uid").getValue(String.class));
                                    teacherList.add(assignTutors);
                                }

                            }
                        }
                        Log.e("TEACHETRFDFHD",""+teacherList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.ASSIGN_COURSE_SUBJECT:
                params.put("assign_to_id", assignToId);
                params.put("assign_by_id",new SessionManagement(getContext()).getString(USER_ID) );
                params.put("type", "subject");
                params.put("course_id", course_id);
                params.put("subject_id", subject_id);
                params.put("status", "Pending");
                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Courses");
                reference2.child(firebaseAuth.getUid()).child(course_id).child("Subject").child(subject_id)
                        .updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CommonMethods.showSuccessToast(getActivity(),"Course Assigned to "+assignToName+" successfully");
                            }
                        });

//                callApi(ApiCode.ASSIGN_COURSE_SUBJECT, params);
                break;


        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_SUBJECT_CHAPTERS:
                service.postDataVolley(ApiCode.GET_SUBJECT_CHAPTERS,
                        BaseUrl.URL_GET_SUBJECT_CHAPTERS, params);
                break;
            case ApiCode.ADD_SUBJECT_CHAPTERS:
                service.postDataVolley(ApiCode.ADD_SUBJECT_CHAPTERS,
                        BaseUrl.URL_ADD_SUBJECT_CHAPTERS, params);
                break;

            case ApiCode.UPDATE_SUBJECT_CHAPTERS:
                service.postDataVolley(ApiCode.UPDATE_SUBJECT_CHAPTERS,
                        BaseUrl.URL_UPDATE_SUBJECT_CHAPTERS, params);
                break;

            case ApiCode.DELETE_CHAPTERS:
                service.postDataVolley(ApiCode.DELETE_CHAPTERS,
                        BaseUrl.URL_DELETE_CHAPTERS, params);
                break;
            case ApiCode.GET_LIVE_CLASSES:
                service.postDataVolley(ApiCode.GET_LIVE_CLASSES,
                        BaseUrl.URL_GET_LIVE_CLASSES, params);
                break;
            case ApiCode.ASSIGN_COURSE_SUBJECT:
                service.postDataVolley(ApiCode.ASSIGN_COURSE_SUBJECT,
                        BaseUrl.URL_ASSIGN_COURSE_SUBJECT, params);
                Log.e("url:", BaseUrl.URL_ASSIGN_COURSE_SUBJECT);
                Log.e("params:", "" + params);
                break;
            case ApiCode.GET_TUTOR_FOR_ASSIGN:
                service.postDataVolley(ApiCode.GET_TUTOR_FOR_ASSIGN,
                        BaseUrl.URL_GET_TUTOR_FOR_ASSIGN, params);
                Log.e("url:", BaseUrl.URL_GET_TUTOR_FOR_ASSIGN);
                Log.e("params:", "" + params);
                break;



        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.GET_SUBJECT_CHAPTERS:
                Log.e("chapter", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    if (array.length() > 0) {
                        ArrayList<T_ChapterModel> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<ArrayList<T_ChapterModel>>() {
                                        }.getType());
                        chapterList.clear();
                        chapterList.addAll(psearch);
                        setChapterList();
                        if (bottomFocus) {
                            nestedScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    bottomFocus = false;
                                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    } else {
                        chapterList.clear();
                        setChapterList();
                    }
                } else {
                    chapterList.clear();
                    setChapterList();
                }
                break;
            case ApiCode.ADD_SUBJECT_CHAPTERS:
                Log.e("chapter", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Chapter added successfully");
                    chapter_title = "";
                    bottomFocus = true;
                    sendApiCall();
                }
                break;
            case ApiCode.UPDATE_SUBJECT_CHAPTERS:
                Log.e("chapter", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Chapter Updated Successfully");
                    chapter_title = "";
                    bottomFocus = false;
                    sendApiCall();
                }
                break;
            case ApiCode.DELETE_CHAPTERS:
                Log.e("chapter", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Chapter Deleted Successfully");
                    // message="";
                    bottomFocus = false;
                    sendApiCall();
                }
                break;

            case ApiCode.GET_LIVE_CLASSES:
                Log.e("today_live", response);
                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<T_LiveModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<T_LiveModel>>() {
                                        }.getType());
                        liveList.clear();
                        if (psearch.size()>0) {
                            new TodayLiveAsyncTask().execute(psearch);
                        }

                    }

                } else {
                    liveList.clear();
                    setTodayLiveList();
                }

                break;
            case ApiCode.ASSIGN_COURSE_SUBJECT:
                Log.e("ASSI_SUBJ ",""+ response);
                if (jsonObject.getBoolean("status")) {
                    assignToId="";
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                    ((TeacherMainActivity)getActivity()).onBackPressed();

                }else{
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
            case ApiCode.GET_TUTOR_FOR_ASSIGN:
                Log.e("ASSI_TUTOR ",""+ response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<AssignTutors> list = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.common.reflect.TypeToken<List<AssignTutors>>() {
                                        }.getType());
                        teacherList.clear();
                        teacherList.addAll(list);
                        //setDataOnList();
                    }else{
                        teacherList.clear();
                    }
                }else{
                    teacherList.clear();
                }
                break;
        }

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
                    showDialog("add");

                break;

            default:
                return false;
        }
        return true;

    }

    private void showDialog(String type) {
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
        if (type.equals("add")) {

        } else if (type.equals("update")) {
            tv_title.setText("Update Chapter");
            et_name.setText(chapter_title);
            btn_save.setText("Update");
        }
        dialog.show();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    et_name.setError("Enter chapter title");
                    et_name.requestFocus();
                } else {
                    chapter_title = et_name.getText().toString().trim();
                    if (ConnectivityReceiver.isConnected()) {
                        if (type.equals("add"))
                            sendRequest(ApiCode.ADD_SUBJECT_CHAPTERS);
                        else
                            sendRequest(ApiCode.UPDATE_SUBJECT_CHAPTERS);

                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                    }
                }
                dialog.dismiss();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.include_assign_sub:
                showTeachersDialog();
                break;
            case R.id.include_notice:
                //subjec notice
                gotoNoticePage();
                break;

        }
    }

    private void gotoNoticePage() {
        MyNoticeFragment fragment=new MyNoticeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id", course_id);
        bundle.putString("subject_id", subject_id);
        bundle.putString("title",subName);
        bundle.putString("notice", "subject");

        if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)){
            bundle.putSerializable("profileData",assignProfile);
        }
        fragment.setArguments(bundle);
        ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
    }

    private void showTeachersDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_assign_tutor);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();
        ImageView ivCancel = dialog.findViewById(R.id.iv_tutor_cancel);
        EditText etSearch = dialog.findViewById(R.id.et_search);
        RecyclerView recTeachers = dialog.findViewById(R.id.rec_tutors);

        //_-----------set teacher list here----------------//
        recTeachers.setLayoutManager(new LinearLayoutManager(getActivity()));
        AssignTeacherAdapter teacherAdapter=new AssignTeacherAdapter(getActivity(), teacherList,
                new AssignTeacherAdapter.OnAssignListener() {
                    @Override
                    public void onAssign(int position,String asgId,String name,String image) {
                        //assignToId=teacherList.get(position).getId();
                        assignToId=asgId;
                        assignToName=name;
                        if (CommonMethods.checkInternetConnection(getContext())){
                            sendRequest(ApiCode.ASSIGN_COURSE_SUBJECT);
                            dialog.dismiss();
                        }

                    }
                });
        recTeachers.setAdapter(teacherAdapter);
        teacherAdapter.notifyDataSetChanged();
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //--------search feature--------------------
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (teacherAdapter!=null){
                    teacherAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //-----------------------------------------------------//
    }

    private class TodayLiveAsyncTask extends AsyncTask<ArrayList<T_LiveModel>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<T_LiveModel>... arrayLists) {
            ArrayList<T_LiveModel> list = new ArrayList<>();
            list.addAll(arrayLists[0]);
            for (T_LiveModel model:list){
                if (model.getIs_live().equals("1")) {
                    liveList.add(model);
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setTodayLiveList();
        }



    }


}
package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static org.webrtc.ContextUtils.getApplicationContext;
import static soonflyy.learning.hub.Common.Constant.ASSIGN;
import static soonflyy.learning.hub.Common.Constant.ASSIGN_BY;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.AssignTeacherAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_Course_provide_Adapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignTutors;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.LiveDemoClassesFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.TestCourseOfferAdapter;
import soonflyy.learning.hub.model.Course_Category_Model;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import soonflyy.learning.hub.model.T_Course_provide_Model;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateCourseFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {//AppCompatActivity


    View includeLiveDemo, includeAssign, includeCreator;
    ImageView ivEditCreator, ivDeleteCreator;
    LinearLayout linChangeCreator;

    ImageView iv_btn, iv_video, arrow_back_img, course_thumbnail_img, iv_image;
    ImageView upload_video_btn, iv_dropdwn, iv_dropup, iv_course_image;
    CheckBox cb_recommend;
    TextView tv_imageName, tv_expiry,assignTvProfileName;
    RelativeLayout rel_profile;
    RelativeLayout rel_coverImg, rel_assign;
    CircleImageView iv_removeImg,assignProfileImg;

    TextView tv_btn, tv_courseType, tv_title, course_title, tv_courseoffer, tv_updateVideo, tv_pdf, tv_live, tv_course, tv_free_btn, tv_paid_btn, tv_create, tv_sub_category;
    Spinner sub_cat_spinner, type_spinner;
    EditText et_name, et_detail, et_about, et_validity, et_price, et_discount, et_live_demo_count;
    RecyclerView rec_video, rec_uploaded_video, recv_course;
    LinearLayout lin_price, btn_text, lin_offer, lin_video_file, lin_uploaded_file, lin_upload, lin_pdf, lin_amount;
    RelativeLayout rel_create;
    SwipeRefreshLayout swipe;
    private SessionManagement management;
    private ArrayList<String> videoPathList = new ArrayList<>();
    ArrayList<MyCourseDetailModel> offerlist = new ArrayList<>();

    private String updateOrAdd;
    private String pageTitle;
    private T_Course_provide_Adapter course_provide_adapter;
    private ArrayList<T_Course_provide_Model> provide_modelList;
    private List<Course_Category_Model> subCategoryList = new ArrayList<>();
    private ArrayAdapter<Course_Category_Model> subCategoryArrayAdapter;
    private String sub_cat_id, free_paid, uFree_paid,courseCreatorId;
    private String course_id;
    String courseName, u_discount, u_expy_date, uCat_id, category_name, details, about, price, coverImgString = "", offer_price = "", live_dem_count = "0", validity;
    ArrayList<String> courseOffer = new ArrayList<>();
    ArrayList<Integer> uproviderList = new ArrayList<>();
    ArrayList<String> u_courseOffer = new ArrayList<>();

    ///
    //--------------new modeification-----//
    RoundedImageView ivCreatorImage;
    LinearLayout linCreatorImage;


    //-----profile View------------//
    TextView tvProfileName, tvType;
    ImageView tvMobile;
    Button btnProfile;
    CircleImageView ivProfileImg;
    RelativeLayout relProfileParent;
    //-----------------------------//
    String creatorImage = "", creatorName, creatorAbout,creatorId="",assignToId="",creatorImgUrl="",assignToName="",assignToImage="";
    String imageType = "";
    ArrayList<AssignTutors> teacherList = new ArrayList<>();

    FirebaseAuth firebaseAuth;

    Uri imgUri;
    //---------------------------------//


    public CreateCourseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_create_course, container, false);
        management = new SessionManagement(getContext());
        firebaseAuth=FirebaseAuth.getInstance();
        initview(view);
        getArgumentData();
        init_courseoffer();
        init_swipe_method();
        setCourseTypeSpinner();

        //tv_title.setText("Create Course");

        setSubCategorySpinner();
        sendApiCall();
        return view;
    }

    private void setCourseTypeSpinner() {
        List<String> typelist = new ArrayList<>();
        typelist.add("");
        typelist.add("Paid");
        typelist.add("Free");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, typelist);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        type_spinner.setAdapter(typeAdapter);
        typeAdapter.notifyDataSetChanged();
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // free_paid = parent.getSelectedItem().toString();
                    tv_courseType.setText(parent.getSelectedItem().toString());
                    if (position == 1) {
                        free_paid = "0";
                        et_price.setEnabled(true);
                        et_discount.setEnabled(true);
                    } else if (position == 2) {
                        free_paid = "1";
                        et_price.setEnabled(false);
                        et_discount.setEnabled(false);
                        et_price.getText().clear();
                        et_discount.getText().clear();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void init_courseoffer() {
        lin_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_offer_dailoge();
            }
        });

    }

    private void show_offer_dailoge() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_create_courseoffer);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        LinearLayout lin_offer = dialog.findViewById(R.id.lin_offer);
        ImageView iv_dropup = dialog.findViewById(R.id.iv_dropup);
        SearchView search_view = dialog.findViewById(R.id.search);
        RecyclerView rec_offerlist = dialog.findViewById(R.id.rec_offerlist);
        //TextView tv_courseoffer = dialog.findViewById(R.id.tv_courseoffer);
        TestCourseOfferAdapter offerAdapter;


       // dialog.show();


        //offerlist.clear();
        //rec_offerlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_offerlist.setLayoutManager(linearLayoutManager);
        //rec_offerlist.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        recv_course.setKeepScreenOn(true);
        offerAdapter = new TestCourseOfferAdapter(getContext(), offerlist);
        rec_offerlist.setAdapter(offerAdapter);
        offerAdapter.notifyDataSetChanged();

        iv_dropup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseOffer.clear();

                courseOffer.addAll(offerAdapter.getSelectedCourse());
                if (courseOffer.size() > 0) {
                    if (courseOffer.get(1).length() == 0) {
                        tv_courseoffer.setText("");
                        tv_courseoffer.setHint("Choose free course");
                    }
                    tv_courseoffer.setText(courseOffer.get(1));
                } else {
                    tv_courseoffer.setText("");
                    tv_courseoffer.setHint("Choose free course");
                }

                String price = offerAdapter.getOfferPrice();
                if (!TextUtils.isEmpty(price)) {
                    offer_price = price;
                }
                dialog.dismiss();
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                offerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                offerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        dialog.setCanceledOnTouchOutside(false);
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                sendApiCall();
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()) {
            if (updateOrAdd.equals("update")) {
                sendAddCourseRequest(ApiCode.GET_CATEGORY);
                sendAddCourseRequest(ApiCode.GET_COURSE);
                sendAddCourseRequest(ApiCode.GET_COURSE_BY_ID);
                sendAddCourseRequest(ApiCode.GET_TUTOR_FOR_ASSIGN);
            } else {
                sendAddCourseRequest(ApiCode.GET_CATEGORY);
                sendAddCourseRequest(ApiCode.GET_COURSE);
            }
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }


    private void initview(View view) {
        rel_assign = view.findViewById(R.id.rel_assign);
        includeCreator = view.findViewById(R.id.include_creator);
        includeAssign = view.findViewById(R.id.include_assign);
        includeLiveDemo = view.findViewById(R.id.include_livedemo);
        ivDeleteCreator = view.findViewById(R.id.iv_delete);
        linChangeCreator = view.findViewById(R.id.lin_chagne_creator);
        ivEditCreator = view.findViewById(R.id.iv_edit);

        assignTvProfileName=view.findViewById(R.id.assign_tv_name);


        iv_btn = view.findViewById(R.id.iv_btn);
        tv_expiry = view.findViewById(R.id.tv_expiry);
        tv_btn = view.findViewById(R.id.tv_btn);
        iv_course_image = view.findViewById(R.id.upload_course);
        swipe = view.findViewById(R.id.swipe);
        tv_courseoffer = view.findViewById(R.id.tv_courseoffer);
        iv_dropdwn = view.findViewById(R.id.iv_dropdwn);
        iv_dropup = view.findViewById(R.id.iv_dropup);
        lin_offer = view.findViewById(R.id.lin_offer);
        iv_video = view.findViewById(R.id.iv_video);
        iv_image = view.findViewById(R.id.min_thumbnail_img);
        lin_upload = view.findViewById(R.id.lin_upload);
        lin_pdf = view.findViewById(R.id.lin_pdf);
        tv_title = view.findViewById(R.id.tv_title);
        tv_create = view.findViewById(R.
                id.tv_create);
        tv_updateVideo = view.findViewById(R.id.tv_updateVideo);
        upload_video_btn = view.findViewById(R.id.upload_course_video);
        lin_video_file = view.findViewById(R.id.lin_video_file);
        lin_uploaded_file = view.findViewById(R.id.lin_uploaded_file);
        rec_video = view.findViewById(R.id.rc_select_video);
        rec_uploaded_video = view.findViewById(R.id.rc_uploaded_video);
        recv_course = view.findViewById(R.id.recv_course);
        course_thumbnail_img = view.findViewById(R.id.course_thumbnail);
        btn_text = view.findViewById(R.id.btn_text);

        tv_pdf = view.findViewById(R.id.tv_pdf);
        tv_live = view.findViewById(R.id.tv_liveclass);
        tv_course = view.findViewById(R.id.tv_course);
        tv_free_btn = view.findViewById(R.id.btn_free);
        tv_paid_btn = view.findViewById(R.id.btn_paid);
        course_title = view.findViewById(R.id.titel_tv);

        et_detail = view.findViewById(R.id.et_add_detail);
        et_name = view.findViewById(R.id.et_name);
        et_price = view.findViewById(R.id.et_amount);
        et_discount = view.findViewById(R.id.et_dis_amount);
        et_validity = view.findViewById(R.id.et_validity);
        et_about = view.findViewById(R.id.et_about_course);
        et_live_demo_count = view.findViewById(R.id.et_live_demo);

        rel_create = view.findViewById(R.id.rel_create);
        lin_amount = view.findViewById(R.id.amount_layout);
        lin_price = view.findViewById(R.id.lin_price);

        tv_sub_category = view.findViewById(R.id.sub_cat_tv);
        sub_cat_spinner = view.findViewById(R.id.sub_category_spinner);
        type_spinner = view.findViewById(R.id.type_spinner);
        tv_courseType = view.findViewById(R.id.tpe_tv);
        cb_recommend = view.findViewById(R.id.checkbox_recommend);

        tv_imageName = view.findViewById(R.id.tv_image_name);
        iv_removeImg = view.findViewById(R.id.cancel_image);
        rel_coverImg = view.findViewById(R.id.rel_cover_image);

        //------profile view initialize-----------//
        relProfileParent = view.findViewById(R.id.rel_profile);
        tvProfileName = view.findViewById(R.id.tv_name);
        ivProfileImg = view.findViewById(R.id.iv_profile_img);
        tvMobile = view.findViewById(R.id.assign_tv_mobile);
        assignProfileImg=view.findViewById(R.id.assign_iv_profile_img);

        tvType = view.findViewById(R.id.tv_type);
        btnProfile = view.findViewById(R.id.btn_profile);
        rel_profile = view.findViewById(R.id.rel_profile);
        rel_profile.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_smoke));
        //---------------------------------------//

        rel_create.setOnClickListener(this);
        // iv_pdf.setOnClickListener (this);
        arrow_back_img = view.findViewById(R.id.arrow_back_img);
        arrow_back_img.setOnClickListener(this);
        iv_video.setOnClickListener(this);
        tv_course.setOnClickListener(this);
        tv_paid_btn.setOnClickListener(this);
        tv_free_btn.setOnClickListener(this);
        upload_video_btn.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        course_thumbnail_img.setOnClickListener(this);
        tv_sub_category.setOnClickListener(this);
        btn_text.setOnClickListener(this);
        lin_upload.setOnClickListener(this);
        iv_removeImg.setOnClickListener(this);
        tv_courseType.setOnClickListener(this);
        tv_expiry.setOnClickListener(this);

        includeLiveDemo.setOnClickListener(this);
        includeAssign.setOnClickListener(this);
        linChangeCreator.setOnClickListener(this);
        ivEditCreator.setOnClickListener(this);
        ivDeleteCreator.setOnClickListener(this);


        init_course_recycler();


    }


    private void getArgumentData() {
        updateOrAdd = getArguments().getString("type");
        if (updateOrAdd.equals("update")) {
            course_id = getArguments().getString("course_id");
            pageTitle = getArguments().getString("course_name");
            courseCreatorId = getArguments().getString("course_creator_id");
            sendApiCall();
//            Log.e("jhvjcvgc",courseCreatorId);
//            Log.e("Assjhvhgdjyhgcvujgcjdyxm",course_id);

            ;
            // tv_create.setText("Update");
            //courseModel = getArguments().getParcelable("courseData");
            // initAndSetDataToView();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                  //  lin_price.setVisibility(View.VISIBLE);
                    tv_btn.setText("Update");
                    iv_btn.setVisibility(View.GONE);
                    rel_assign.setVisibility(View.VISIBLE);
                    includeCreator.setVisibility(View.GONE);
                    if (management.getString(ASSIGN).equals(ASSIGN_BY)) {
                        //manage for course assigne by other
                        includeCreator.setVisibility(View.GONE);
                        rel_profile.setVisibility(View.VISIBLE);
                        linChangeCreator.setVisibility(View.GONE);
                        includeAssign.setVisibility(View.GONE);
                        ((Mycourse_deailFragment) getParentFragment()).hideAssignProfile();
                    }

                }
            });


        } else {
            ((Create_Course_MainFragment) getParentFragment()).changeTrackerColor(1);
          //  lin_price.setVisibility(View.GONE);
            tv_btn.setText("Create");
           // iv_btn.setVisibility(View.VISIBLE);
            pageTitle = "Create Course";
            rel_assign.setVisibility(View.GONE);
//            tv_create.setText("Create");
        }
    }

    private void init_course_recycler() {
        provide_modelList = new ArrayList<>();
        provide_modelList.clear();
        recv_course.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        recv_course.setLayoutManager(linearLayoutManager);
        recv_course.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        recv_course.setKeepScreenOn(true);
        for (int i = 0; i < CommonMethods.getCourseProvide().length; i++) {
            provide_modelList.add(new T_Course_provide_Model(CommonMethods.getCourseProvide()[i],
                    false));
        }

        course_provide_adapter = new T_Course_provide_Adapter(getContext(), provide_modelList);
        recv_course.setAdapter(course_provide_adapter);
        course_provide_adapter.notifyDataSetChanged();


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_text:
                Log.e("create_btn ", "clicked");


                if (validateField(updateOrAdd)) {

                    if (ConnectivityReceiver.isConnected()) {
                        if (updateOrAdd.equals("add"))
                            sendAddCourseRequest(ApiCode.ADD_COURSE);
                        else
                            sendAddCourseRequest(ApiCode.UPDATE_COURSE);
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");

                    }
                }

                break;

            case R.id.upload_course_video:
                imageType = "course";
                chooseImage(v);
                break;
            case R.id.cancel_image:
                coverImgString = "";
                rel_coverImg.setVisibility(View.GONE);
                upload_video_btn.setVisibility(View.VISIBLE);
                tv_imageName.setText("Upload your file here,Choose file form your device");
                break;
            case R.id.sub_cat_tv:
                sub_cat_spinner.performClick();
                break;
            // chooseVideo();

            case R.id.tpe_tv:
                type_spinner.performClick();
                break;
            case R.id.tv_expiry:
              //  chooseDate(tv_expiry);
                showDatePicker(tv_expiry);
                break;

            case R.id.include_livedemo:
                //live demo click
                Log.e("click", "demo");
                gotoLiveDemoClass();
                break;
            case R.id.include_assign:
                ///assign course click
                Log.e("click", "assign");
                showTeachersDialog();
                break;
            case R.id.lin_chagne_creator:
                //creator change
                showChangeCreatorDialog("add");
                Log.e("click", "change");
                break;
            case R.id.iv_edit:
                //createor edit
                Log.e("click", "edit");
                showChangeCreatorDialog("update");
                break;
            case R.id.iv_delete:
                //createor delete
                Log.e("click", "delete");
                showDeletetMessage();
               // sendAddCourseRequest(ApiCode.DELETE_COURSE_CREATOR);
               // includeCreator.setVisibility(View.GONE);
                ///linChangeCreator.setVisibility(View.VISIBLE);
                break;


        }


    }

    private void showDeletetMessage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendAddCourseRequest(ApiCode.DELETE_COURSE_CREATOR);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void gotoLiveDemoClass() {
        LiveDemoClassesFragment fragment = new LiveDemoClassesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id",course_id);
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

        //setTeacherlist(teacherList);
        AssignTeacherAdapter teacherAdapter = new AssignTeacherAdapter(getActivity(), teacherList,
                new AssignTeacherAdapter.OnAssignListener() {
                    @Override
                    public void onAssign(int position,String asgid,String name,String image) {
                       // assignToId=teacherList.get(position).getId();
                        assignToId=asgid;
                        assignToName=name;
                        assignToImage=image;

                        Log.e("assignId",""+assignToId);
                        if (CommonMethods.checkInternetConnection(getContext())){
                            sendAddCourseRequest(ApiCode.ASSIGN_COURSE_SUBJECT);
                            dialog.dismiss();
                        }

                        //CommonMethods.showSuccessToast(getContext(), "Assign successfully");
                    }
                });
        recTeachers.setAdapter(teacherAdapter);
        //teacherAdapter.notifyDataSetChanged();
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



    private void showChangeCreatorDialog(String typ) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_course_creator);
        dialog.getWindow();
        dialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        //dialog.getWindow().setGravity(Gravity.CENTER);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
       // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

        ///----------initialize id ------------------------//
        linCreatorImage = dialog.findViewById(R.id.lin_image);
        ivCreatorImage = dialog.findViewById(R.id.img_upload_img);
        ImageView ivCancelImage = dialog.findViewById(R.id.iv_cancle);
        ImageView ivChooseImg = dialog.findViewById(R.id.iv_upload);
        EditText etName = dialog.findViewById(R.id.et_name);
        EditText etAbout = dialog.findViewById(R.id.et_about);
        Button btnSubmit = dialog.findViewById(R.id.btn_save);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        etAbout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_about) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        //---------------------------------------------------------//

        //----------set data according add or update------------------//
        if (typ.equalsIgnoreCase("update")) {
            //---write code to update data-----------//
            etName.setText(creatorName);
            etAbout.setText(creatorAbout);
            Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                    .into(ivCreatorImage);
            linCreatorImage.setVisibility(View.VISIBLE);
            //set image also here
            //------------------------------------//
        }
        ivChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose image
                imageType = "creator";
                chooseImage(v);
            }
        });
        ivCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linCreatorImage.setVisibility(View.GONE);
                creatorImage = "";
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatorName = etName.getText().toString().trim();
                creatorAbout = etAbout.getText().toString().trim();
                if (TextUtils.isEmpty(creatorName)) {
                    etName.setError("Enter name");
                    etName.requestFocus();
                }/*else if (TextUtils.isEmpty(creatorImage)){
                    CommonMethods.showSuccessToast(getContext(),"Choose Photo");
                } */ else if (TextUtils.isEmpty(creatorAbout)) {
                    etAbout.setError("Write here");
                    etAbout.requestFocus();
                } else {
                    //write code to call api for creat o
                    if (typ.equalsIgnoreCase("update")) {
                        sendAddCourseRequest(ApiCode.UPDATE_COURSE_CREATOR);
                    } else {
                        sendAddCourseRequest(ApiCode.CHANGE_COURSE_CREATOR);
                    }
                    dialog.dismiss();
                   // CommonMethods.showSuccessToast(getContext(), "Course creator changed Successfylly");
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //--------------------------------------------------------//


    }

    private void chooseDate(TextView view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());
                view.setText(date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    private  void showDatePicker(TextView tvView){

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

                tvView.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                String d=tvView.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tvView.setText("");
//                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"Please select a date after the current date");
                    }
                }
            }

        });
        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private boolean validateDate(String d) {
        try {
            return CommonMethods.isValidExpiryDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setSubCategorySpinner() {
        subCategoryList.add(new Course_Category_Model("0", "", ""));

        subCategoryArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, subCategoryList);
        subCategoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        sub_cat_spinner.setAdapter(subCategoryArrayAdapter);

        sub_cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tv_sub_category.setText(parent.getSelectedItem().toString());
                    sub_cat_id = subCategoryList.get(position).getId();
                    category_name = subCategoryList.get(position).getName();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        subCategoryArrayAdapter.notifyDataSetChanged();
        //--------------------//
    }


    private void chooseImage(View v) {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("courseimage", "" + requestCode);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            imgUri = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(imgUri.getPath()).getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            if(imageWidth<imageHeight){
                CommonMethods.showSuccessToast(getActivity(),"Invalid Image Configuration Selected");
            }else{
                try {


                    // Glide.with(this).load(imgUri).into(user_imge);

                    ContentResolver cr = getActivity().getContentResolver();
                    InputStream is = cr.openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    if (imageType.equals("course")) {
                        //for course image
                        Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(iv_course_image);
                        coverImgString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                        rel_coverImg.setVisibility(View.VISIBLE);
                        upload_video_btn.setVisibility(View.GONE);
                        tv_imageName.setText("cover_image.pdf");

                        DynamicToast.make(getContext(), "Cover image selected for upload", Toast.LENGTH_SHORT).show();

                    } else if (imageType.equals("creator")) {
                        //for  creator image
                        creatorImgUrl=imgUri.getPath();
                        Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(ivCreatorImage);
                        creatorImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                        linCreatorImage.setVisibility(View.VISIBLE);
                        DynamicToast.make(getContext(), "Creator Photo selected for upload", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
       /*if (requestCode == 104 && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);
                Picasso.get().load(imgUri).into(ivCreatorImage);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                creatorImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                linCreatorImage.setVisibility(View.VISIBLE);
                DynamicToast.make(getContext(), "Creator Photo selected for upload", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }


    private boolean validateField(String type) {
        if (type.equals("add")) {
            return validateField();
        } else {
            return validateFieldForUpdate();
        }
    }

    private boolean validateFieldForUpdate() {
        if (validateEmptyField()) {
            if (isDataChange())
                return true;
            else {
                CommonMethods.showSuccessToast(getContext(), "You have not change anything");
                return false;
            }

        } else {
            return false;
        }
    }

    private boolean isDataChange() {
        if (!courseName.equals(et_name.getText().toString().trim())) {
            return true;
        }
        if (!uCat_id.equals(sub_cat_id)) {
            return true;
        }
        if (!u_expy_date.equals(tv_expiry.getText().toString().trim())) {
            return true;
        }
        if (!details.equals(et_detail.getText().toString().trim())) {
            return true;
        }
        if (!about.equals(et_about.getText().toString().trim())) {
            return true;
        }
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(course_provide_adapter.getSelectedOption());

        if (list.size() != uproviderList.size()) {
            return true;
        }
        if (list.size() == uproviderList.size()) {
            if (!list.containsAll(uproviderList) && !uproviderList.containsAll(list)) {
                return true;
            }
        }

        if (!uFree_paid.equals(free_paid)) {
            return true;
        }
        if (free_paid.equals("0")) {
            if (Float.parseFloat(price) != Float.parseFloat(et_price.getText().toString())
                    || Float.parseFloat(u_discount) != Float.parseFloat(et_discount.getText().toString()))
                return true;
        }
        if (!TextUtils.isEmpty(coverImgString)) {
            return true;
        }
        if (!TextUtils.isEmpty(live_dem_count)) {
            if (!et_live_demo_count.getText().toString().trim().equals(live_dem_count)) {
                return true;
            }
        }

        return false;
    }

    private boolean validateEmptyField() {
        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
            et_name.setError("Enter course title");
            et_name.requestFocus();
            return false;
        }
        if (course_provide_adapter.getSelectedOption().size() == 0) {
            CommonMethods.showSuccessToast(getContext(), "Choose course provide option");
            return false;
        }
        if (!TextUtils.isEmpty(et_live_demo_count.getText().toString().trim())) {
            if (Integer.parseInt(et_live_demo_count.getText().toString().trim()) <= 0) {
                et_live_demo_count.setError("Value should be greater than 0");
                et_live_demo_count.requestFocus();
                return false;
            }
        }
        if (TextUtils.isEmpty(tv_expiry.getText().toString().trim())) {
            CommonMethods.showSuccessToast(getContext(), "Choose expiry date");
            return false;
        }
        if (TextUtils.isEmpty(details)) {
            et_detail.setError("Enter additional details");
            et_detail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(about)) {
            et_about.setError("Enter about course");
            et_about.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(free_paid)) {
            CommonMethods.showSuccessToast(getContext(), "Choose Course Type");
            return false;
        }
        if (free_paid.equals("0") && TextUtils.isEmpty(et_price.getText().toString().trim())) {
            CommonMethods.showSuccessToast(getContext(), "Enter amount");
            return false;
        }
        if (free_paid.equals("0") && Float.parseFloat(et_price.getText().toString().trim()) == 0) {
            CommonMethods.showSuccessToast(getContext(), "Amount should not be 0");
            return false;
        }
        if (free_paid.equals("0") && !TextUtils.isEmpty(et_discount.getText().toString().trim())) {
            if (Float.parseFloat(et_price.getText().toString().trim()) <=
                    Float.parseFloat(et_discount.getText().toString().trim())) {
                CommonMethods.showSuccessToast(getContext(), "Discount should be less than amount");
                return false;
            }
        }

        return true;
    }

    private boolean validateField() {
        boolean result = false;
        courseName = et_name.getText().toString().trim();
        //  validity = et_validity.getText().toString().trim();
        details = et_detail.getText().toString().trim();
        about = et_about.getText().toString().trim();
        String exp_date = tv_expiry.getText().toString().trim();
        if (TextUtils.isEmpty(courseName)) {
            et_name.setError("Enter course title");
            et_name.requestFocus();
        } else if (TextUtils.isEmpty(coverImgString)) {//updateOrAdd.equals("add") &&
            // Toast.makeText(this, "Please choose course cover image", Toast.LENGTH_SHORT).show();
            DynamicToast.make(getContext(), "Please choose course cover image", 3000).show();

        } else if (course_provide_adapter.getSelectedOption().size() == 0) {
            CommonMethods.showSuccessToast(getContext(), "Choose course provide option");
        } else if (TextUtils.isEmpty(exp_date)) {
            CommonMethods.showSuccessToast(getContext(), "Choose expiry date");
        } else if (TextUtils.isEmpty(details)) {
            et_detail.setError("Enter additional details");
            et_detail.requestFocus();
        } else if (TextUtils.isEmpty(about)) {
            et_about.setError("Enter about course");
            et_about.requestFocus();
        } else if (TextUtils.isEmpty(sub_cat_id)) {
            CommonMethods.showSuccessToast(getContext(), "Choose Category");
        }else if (TextUtils.isEmpty(free_paid)) {
            CommonMethods.showSuccessToast(getContext(), "Choose Course Type");
          //  return false;
        }else if (free_paid.equals("0") && TextUtils.isEmpty(et_price.getText().toString().trim())) {
            CommonMethods.showSuccessToast(getContext(), "Enter amount");
          //  return false;
        }else if (free_paid.equals("0") && Float.parseFloat(et_price.getText().toString().trim()) == 0) {
            CommonMethods.showSuccessToast(getContext(), "Amount should not be 0");
            //return false;
        }else if (free_paid.equals("0") && !TextUtils.isEmpty(et_discount.getText().toString().trim())) {
            if (Float.parseFloat(et_price.getText().toString().trim()) <=
                    Float.parseFloat(et_discount.getText().toString().trim())) {
                CommonMethods.showSuccessToast(getContext(), "Discount should be less than amount");
              //  return false;
            }else{
                result = true;
            }
        } else {
            result = true;
        }


        return result;
    }

    private void sendAddCourseRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        StorageReference storageReference=null;
        String filePathAndName="";

        switch (request) {
//
            case ApiCode.GET_COURSE:
                params.put("user_id", management.getString(USER_ID));
                if (course_id == null) {
                    course_id = " ";
                }
                params.put("course_id", course_id);
//                callApi(ApiCode.GET_COURSE, params);
                break;
            case ApiCode.GET_CATEGORY:
//                subCategoryList.clear();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Course");
                reference.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.e("Snapshot",""+snapshot.getChildrenCount());

                        for(DataSnapshot ds:snapshot.getChildren()){
                            Course_Category_Model course_category_model=ds.getValue(Course_Category_Model.class);
                            subCategoryList.add(course_category_model);
                        }
//                        subCategoryList.add(new Course_Category_Model("0", "", ""));
                        Log.e("SubCategory",""+subCategoryList.size());
//
//                        //-----------------//
//                        Collections.sort(subCategoryList, new Comparator<Course_Category_Model>() {
//                            @Override
//                            public int compare(Course_Category_Model o1, Course_Category_Model o2) {
//                                return o1.getName().compareToIgnoreCase(o2.getName());
//                            }
//                        });
                        subCategoryArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//
//                params.put("user_id", new SessionManagement(getActivity()).getString(USER_ID));
//                callApi(ApiCode.GET_CATEGORY, params);
                break;
            case ApiCode.ADD_COURSE:



                /*
                   param.put("discount_amount", String.valueOf(discount_price));
                param.put("amount", String.valueOf(price));
                param.put("is_free_course", free_paid);
                 */
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
                filePathAndName = "UserDetails/" +firebaseAuth.getUid()+"/" + "Course" + "/" +  timestamp;
                storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
                String finalCurrentDate = currentDate;
                String finalCurrentTime = currentTime;
                if(imgUri!=null){
                    String finalCurrentDate1 = currentDate;
                    storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();
                            HashMap<String,Object> params=new HashMap<>();
                            params.put("title", courseName);
                            params.put("course_offers", new Gson().toJson(courseOffer));//couserOffer_id
                            params.put("about_course", about);
                            params.put("validity", tv_expiry.getText().toString().trim());
                            params.put("additional_details", details);
                            params.put("user_id", management.getString(USER_ID));
                            params.put("course_provider", new Gson().toJson(course_provide_adapter.getSelectedOption()));
                            // Log.e("cprovider",""+new Gson().toJson(course_provide_adapter.getSelectedOption()));'
                            params.put("course_thumbnail", ""+downloadImageUri);
                            params.put("subcategory_id", sub_cat_id);
                            params.put("category_name", category_name);
                            params.put("offer_course_price", offer_price);
                            if (TextUtils.isEmpty(et_live_demo_count.getText().toString().trim()))
                                live_dem_count = "0";
                            else
                                live_dem_count = et_live_demo_count.getText().toString().trim();
                            params.put("live_demo_count", live_dem_count);
                            params.put("is_free_course", free_paid);
                            if (free_paid.equals("0")) {
                                String amt = et_price.getText().toString().trim();
                                String dis = et_discount.getText().toString().trim();
                                if (TextUtils.isEmpty(dis)) {
                                    dis = "0";
                                }
                                params.put("amount", String.valueOf(Float.parseFloat(amt)));
                                params.put("discount_amount", String.valueOf(Float.parseFloat(dis)));
                            } else {
                                params.put("amount", "0");
                                params.put("discount_amount", "0");
                            }
                            params.put("course_id",timestamp);
                            params.put("created_at", finalCurrentDate1);
                            params.put("creatorId", firebaseAuth.getUid());
                            params.put("assigned", false);
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                            reference1.child(firebaseAuth.getUid()).child(timestamp).setValue(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                        CommonMethods.showSuccessToast(getContext(), "Course Added Successfully");
                                        ((TeacherMainActivity)getActivity()).onBackPressed();





                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    CommonMethods.showSuccessToast(getContext(), "Something Went Wrong");
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext() , "Image Uploading Error :"+e.getMessage() , Toast.LENGTH_SHORT).show();
                                }
                            })      ;
                }else{
                    Toast.makeText(getContext() , "Select Cover Image" , Toast.LENGTH_SHORT).show();
                }


//
                callApi(ApiCode.ADD_COURSE, params);
                break;
            case ApiCode.UPDATE_COURSE:
                HashMap<String,Object> params1=new HashMap<>();

                params1.put("course_id", course_id);
                params1.put("subcategory_id", sub_cat_id);
                params1.put("title", et_name.getText().toString().trim());//courseName
                params1.put("validity", tv_expiry.getText().toString().trim());//
                params1.put("course_offers", new Gson().toJson(courseOffer));
                params1.put("about_course", et_about.getText().toString().trim());
                params1.put("additional_details", et_detail.getText().toString().trim());//details
                params1.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params1.put("course_provider", new Gson().toJson(course_provide_adapter.getSelectedOption()));
                params1.put("category_name", tv_sub_category.getText().toString().trim());
                params1.put("is_free_course", free_paid);


                if (free_paid.equals("0")) {
                    String amt = et_price.getText().toString().trim();
                    String dis = et_discount.getText().toString().trim();
                    if (TextUtils.isEmpty(dis)) {
                        dis = "0";
                    }
                    params.put("amount", String.valueOf(Float.parseFloat(amt)));
                    params.put("discount_amount", String.valueOf(Float.parseFloat(dis)));
                } else {
                    params.put("amount", "0");
                    params.put("discount_amount", "0");
                }
                params.put("course_thumbnail", coverImgString);

                params.put("offer_course_price", offer_price);
                if (TextUtils.isEmpty(et_live_demo_count.getText().toString().trim()))
                    live_dem_count = "0";
                else
                    live_dem_count = et_live_demo_count.getText().toString().trim();
                params.put("live_demo_count", live_dem_count);

//                callApi(ApiCode.UPDATE_COURSE, params);
                if(imgUri!=null){
                    filePathAndName = "UserDetails/" +firebaseAuth.getUid()+"/" + "Course" + "/" +  course_id;


                    storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

                    storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    Uri downloadImageUri = uriTask.getResult();
                                    params1.put("course_thumbnail", ""+downloadImageUri);
                                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                                    reference1.child(firebaseAuth.getUid()).child(course_id).updateChildren(params1);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    CommonMethods.showSuccessToast(getActivity(),e.getMessage());
                                }
                            });
                }else{
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                    reference1.child(firebaseAuth.getUid()).child(course_id).updateChildren(params1);
                }
                break;
            case ApiCode.GET_COURSE_BY_ID:
//                params.put("type", "1");
//                params.put("course_id", course_id);
//                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
//                callApi(ApiCode.GET_COURSE_BY_ID, params);
                Log.e("CourseCrearklnljkg",courseCreatorId);

                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Courses");
                reference1.child(courseCreatorId).child(course_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        courseName = snapshot.child("title").getValue(String.class);
                        details = snapshot.child("additional_details").getValue(String.class);
                        about = snapshot.child("about_course").getValue(String.class);
                        u_discount = snapshot.child("discount_amount").getValue(String.class);
                        price = snapshot.child("offer_course_price").getValue(String.class);
                        uFree_paid = snapshot.child("is_free_course").getValue(String.class);
                        uCat_id = snapshot.child("subcategory_id").getValue(String.class);
                        u_expy_date = snapshot.child("validity").getValue(String.class);
                        offer_price = snapshot.child("offer_course_price").getValue(String.class);
                        live_dem_count = snapshot.child("live_demo_count").getValue(String.class);
                        free_paid = uFree_paid;
                        sub_cat_id = uCat_id;
                        JSONArray providerJsonARray = null;
//                        try {
//                            providerJsonARray = new JSONArray(snapshot.child("course_provider").getValue(JSONArray.class));
//                            uproviderList.clear();
//                            for (int i = 0; i < providerJsonARray.length(); i++) {
//                                int value = providerJsonARray.getInt(i);
//                                provide_modelList.get(value).setChecked(true);
//                                uproviderList.add(value);
//                            }
//                            course_provide_adapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }





                        et_name.setText(courseName);
                        tv_sub_category.setText(snapshot.child("category_name").getValue(String.class));
                        Picasso.get().load(snapshot.child("course_thumbnail").getValue(String.class))
                                .into(iv_course_image);
                        coverImgString = "";
                        tv_expiry.setText(u_expy_date);
                        rel_coverImg.setVisibility(View.VISIBLE);
                        upload_video_btn.setVisibility(View.GONE);
                        tv_imageName.setText("cover_image.jpeg");

                        if (uFree_paid.equals("1")) {
                            tv_courseType.setText("Free");
                            et_price.setEnabled(false);
                            et_discount.setEnabled(false);

                        } else {
                            if (price == null || TextUtils.isEmpty(price)) {
                                price = "0";
                            } else {
                                if (price.equals("null")) {
                                    price = "0";
                                } else {
                                    et_price.setText(price);
                                }
                            }
                            tv_courseType.setText("Paid");
                            et_discount.setText(u_discount);
                        }
                        et_detail.setText(details);
                        et_about.setText(about);

                        if (!TextUtils.isEmpty(live_dem_count)) {
                            if (Integer.parseInt(live_dem_count) > 0) {
                                et_live_demo_count.setText(live_dem_count);
                            }
                        }

                        if(snapshot.child("assigned").getValue(Boolean.class)){



                            rel_profile.setVisibility(View.VISIBLE);
                            linChangeCreator.setVisibility(View.GONE);
                            includeAssign.setVisibility(View.GONE);

                            MyCourseDetailModel model = snapshot.getValue(MyCourseDetailModel.class);
                            Log.e("Creayflemf",model.getCreatorId());
                            Log.e("Creayflemf",firebaseAuth.getUid());
                            Log.e("Creayflemf",""+model.getCreatorId().equals(firebaseAuth.getUid()));


                            if(model.getCreatorId().equals(firebaseAuth.getUid())){
                                String imglink=model.getAssign_to_image();
                                if(imglink!=null){
                                    Picasso.get().load(imglink)
                                            .placeholder(R.drawable.logoo)
                                            .into(assignProfileImg);
                                }else{
                                    assignProfileImg.setImageResource(R.drawable.logoo);
                                }
                                tvType.setText("Assigned To");
                                tvMobile.setVisibility(View.VISIBLE);
                                assignTvProfileName.setText(model.getAssign_to_name());
                                if(model.getStatus().equals("Pending")){
                                    tvMobile.setImageResource(R.drawable.ic_pending);
                                    tvMobile.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext() , "The assigned tutor has not accepted the course request" , Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }else{
                                    tvMobile.setImageResource(R.drawable.ic_accepted);
                                    tvMobile.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext() , "The assigned tutor has accepted the course request" , Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


                            }
                            else{
                                tvType.setText("Assigned By");
                                tvMobile.setVisibility(View.GONE);
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(model.getAssign_by_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        assignTvProfileName.setText(snapshot.child("name").getValue(String.class));
                                        if(snapshot.child("image").exists()){
                                            String imglink=snapshot.child("image").getValue(String.class);
                                                Picasso.get().load(imglink)
                                                        .placeholder(R.drawable.logoo)
                                                        .into(assignProfileImg);

                                        }else{
                                            assignProfileImg.setImageResource(R.drawable.logoo);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

//
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;

            case ApiCode.CHANGE_COURSE_CREATOR:
                params.put("name", creatorName);
                params.put("change_by_tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("about", creatorAbout);
                params.put("course_id", course_id);
                params.put("image", creatorImage);
                callApi(ApiCode.CHANGE_COURSE_CREATOR, params);
                break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                params.put("name", creatorName);
                params.put("change_by_tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("about", creatorAbout);
                params.put("course_id", course_id);
                params.put("image", creatorImage);
                params.put("id", creatorId);
                callApi(ApiCode.UPDATE_COURSE_CREATOR, params);
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                params.put("id", creatorId);
                callApi(ApiCode.DELETE_COURSE_CREATOR, params);
                break;
            case ApiCode.GET_TUTOR_FOR_ASSIGN:
                //params.put("type", "course");
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
//                callApi(ApiCode.GET_TUTOR_FOR_ASSIGN, params);
                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Users");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        teacherList.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            AssignTutors assignTutors=ds.getValue(AssignTutors.class);
                            Log.e("Teacher",ds.child("uid").getValue(String.class)+"UID::::"+firebaseAuth.getUid());
                            if(ds.child("uid").getValue(String.class)!=null&&ds.child("uid").getValue(String.class).equals(firebaseAuth.getUid())){

                            }else{
                                teacherList.add(assignTutors);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case ApiCode.ASSIGN_COURSE_SUBJECT:
//                params.put("assign_to_id", assignToId);
//                params.put("assign_by_id",new SessionManagement(getContext()).getString(USER_ID) );
//                params.put("type", "course");
//                params.put("course_id", course_id);
//                callApi(ApiCode.ASSIGN_COURSE_SUBJECT, params);
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("assign_to_id",assignToId);
                hashMap.put("assign_to_name",assignToName);
                hashMap.put("assign_to_image",assignToImage);
                hashMap.put("assign_by_id",firebaseAuth.getUid());
                hashMap.put("type","course");
                hashMap.put("status","Pending");
                hashMap.put("assigned",true);

                DatabaseReference reference3=FirebaseDatabase.getInstance().getReference("Courses");
                reference3.child(firebaseAuth.getUid()).child(course_id).updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        CommonMethods.showSuccessToast(getActivity(),"Course Assigned Successfully");
                                        sendNotification();
//                                        hashMap.put("course_id",course_id);
//                                        reference2.child(assignToId).child("Course").child("Assigned").child(course_id)
//                                                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//
//
//                                                    }
//                                                });
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext() , e.getMessage() , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                Log.e("Assign",course_id);
                break;

        }
    }
    private void sendNotification() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(assignToId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token=snapshot.child("token").getValue(String.class);
                Log.e("ToKEN",token);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject js = new JSONObject();
                try {
                    JSONObject jsonobject_notification = new JSONObject();

                    jsonobject_notification.put("title", "You have course request");
                    jsonobject_notification.put("body", "A course creator has assigned a course");


                    JSONObject jsonobject_data = new JSONObject();
                    jsonobject_data.put("imgurl", "https://firebasestorage.googleapis.com/v0/b/haajiri-register.appspot.com/o/Notifications%2Fic_app_logo1.png?alt=media&token=d77857f1-b0e0-4b15-9934-3ec8a88903e9");

                    //JSONObject jsonobject = new JSONObject();

                    js.put("to", token);
                    js.put("notification", jsonobject_notification);
                    js.put("data", jsonobject_data);

                    //js.put("", jsonobject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "https://fcm.googleapis.com/fcm/send";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.e("response1234", String.valueOf(response));
//                        reference.child(hrUid).child("Industry").child(getSharedPreferences("UserDetails",MODE_PRIVATE).getString("industryName",""))
//                                .child("Site").child(String.valueOf(siteId))
//                                .child("Leaves").child(timestamp).updateChildren(hashMap);




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error :  ", String.valueOf(error));


                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer AAAAhfafC20:APA91bFfUwgpuJjofgA05BWhsrnfleqjrhpfTMBNtACD1JDBRZCXCsjcWmGL2yV2FZ_Rdpmz7vc_h_aVVIRGN6JOX0zc6ndqGEjTQfufHUgTF9Z9-SUa3jwEQxVBWGlm393Doz4QT9mj");//TODO add Server key here
                        //headers.put("Content-Type", "application/json");
                        return headers;
                    }

                };
                requestQueue.add(jsonObjReq);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_COURSE:
                service.postDataVolley(ApiCode.GET_COURSE,
                        BaseUrl.URL_GET_COURSE, params);
                break;
            case ApiCode.GET_CATEGORY:
                service.postDataVolley(ApiCode.GET_CATEGORY,
                        BaseUrl.URL_GET_CATEGORY, params);
                break;
            case ApiCode.ADD_COURSE:
                service.postDataVolley(ApiCode.ADD_COURSE,
                        BaseUrl.URL_ADD_COURSE, params);
                break;
//            case ApiCode.GET_SUB_CATEGORY:
//                service.postDataVolley(ApiCode.GET_SUB_CATEGORY,
//                        BaseUrl.URL_GET_SUB_CATEGORY, params);
//                break;
            case ApiCode.UPDATE_COURSE:
                service.postDataVolley(ApiCode.UPDATE_COURSE,
                        BaseUrl.URL_UPDATE_COURSE, params);
                break;
            case ApiCode.GET_COURSE_BY_ID:
                service.postDataVolley(ApiCode.GET_COURSE_BY_ID,
                        BaseUrl.URL_GET_COURSE_BY_ID, params);
                break;
            case ApiCode.CHANGE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.CHANGE_COURSE_CREATOR,
                        BaseUrl.URL_CHANGE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_CHANGE_COURSE_CREATOR);
                Log.e("params:", "" + params);
                break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.UPDATE_COURSE_CREATOR,
                        BaseUrl.URL_UPDATE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_UPDATE_COURSE_CREATOR);
                Log.e("params:", "" + params);
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.DELETE_COURSE_CREATOR,
                        BaseUrl.URL_DELETE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_DELETE_COURSE_CREATOR);
                Log.e("params:", "" + params);
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
            case ApiCode.ADD_COURSE:
                Log.e("add_course ", response);
//
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Course Added Successfully");
                    ((TeacherMainActivity)getActivity()).onBackPressed();
                    /*course_id = jsonObject.getJSONObject("data").getString("course_id");
//
                    Create_subjectFragment fragment = new Create_subjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("course_id", course_id);
                    bundle.putString("course_title", courseName);
                    fragment.setArguments(bundle);
                    ((Create_Course_MainFragment) getParentFragment()).switchFragment(fragment);

                     */

                } else {
                    CommonMethods.showSuccessToast(getContext(), "Something Went Wrong");
                }
//
                break;
            case ApiCode.GET_CATEGORY:
                Log.e("category_data", response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        List<Course_Category_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Course_Category_Model>>() {
                                        }.getType());
                        subCategoryList.clear();
                        subCategoryList.add(new Course_Category_Model("0", "", ""));
                        subCategoryList.addAll(psearch);
                        //-----------------//
                        Collections.sort(subCategoryList, new Comparator<Course_Category_Model>() {
                            @Override
                            public int compare(Course_Category_Model o1, Course_Category_Model o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                        subCategoryArrayAdapter.notifyDataSetChanged();
                        // setSubCategorySpinner();
                    }
                }
                break;
            case ApiCode.UPDATE_COURSE:
                Log.e("update", response);
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Course Updated Successfully");
//
                }
                break;
            case ApiCode.GET_COURSE_BY_ID:
                Log.e("course_details", response);
                if (jsonObject.getBoolean("status")) {
                    //set data for update
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    setAssignedProfile(jsonObject);
                    setCreatorProfile(jsonObject);
                    if (jsonArray.length() > 0) {
                        setDataToview(jsonArray.getJSONObject(0));
                    }
                }
                break;
            case ApiCode.GET_COURSE:
                Log.e("courses ", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<MyCourseDetailModel> list = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<MyCourseDetailModel>>() {
                                        }.getType());
                        offerlist.clear();
                        offerlist.addAll(list);
                        //setDataOnList();
                    }
                }
                break;
            case ApiCode.CHANGE_COURSE_CREATOR:
                Log.e("COURSE_CREATOR ", response);
                if (jsonObject.getBoolean("status")) {
                    creatorImage="";
                    linChangeCreator.setVisibility(View.GONE);
                    includeCreator.setVisibility(View.VISIBLE);
                    tvProfileName.setText(creatorName);
                    Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                                    .into(ivProfileImg);
                    sendApiCall();
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                Log.e("UPDATE_CREATOR ", ""+response);
                if (jsonObject.getBoolean("status")) {
                    creatorImage="";
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                    linChangeCreator.setVisibility(View.GONE);
                    includeCreator.setVisibility(View.VISIBLE);
                    sendApiCall();

                }
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                Log.e("DELETE_CREATOR ",""+ response);
                if (jsonObject.getBoolean("status")) {
                    linChangeCreator.setVisibility(View.VISIBLE);
                    includeCreator.setVisibility(View.GONE);
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
            case ApiCode.ASSIGN_COURSE_SUBJECT:
                Log.e("ASSI_COURSE ",""+ response);
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
                                        new TypeToken<List<AssignTutors>>() {
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

    private void setDataToview(JSONObject jsonObject) {
        try {

            courseName = jsonObject.getString("title");
            details = jsonObject.getString("short_description");
            about = jsonObject.getString("description");
            u_discount = jsonObject.getString("discounted_price");
            price = jsonObject.getString("amount");
            uFree_paid = jsonObject.getString("is_free_course");
            uCat_id = jsonObject.getString("category_id");
            u_expy_date = jsonObject.getString("validity");
            offer_price = jsonObject.getString("offer_course_price");
            live_dem_count = jsonObject.getString("live_demo_count");
            free_paid = uFree_paid;
            sub_cat_id = uCat_id;

            //corseOffer

            String str = jsonObject.getString("course_offers");
////
            JSONArray jsonArray = new JSONArray(str);
            u_courseOffer.clear();
            if (jsonArray.length() > 0) {
                u_courseOffer.add(jsonArray.getString(0));
                u_courseOffer.add(jsonArray.getString(1));
            }
            if (u_courseOffer.size() > 0) {
                courseOffer.clear();
                courseOffer.addAll(u_courseOffer);
                tv_courseoffer.setText(u_courseOffer.get(1));
            }
            JSONArray providerJsonARray = new JSONArray(jsonObject.getString("course_provider"));

            uproviderList.clear();
            for (int i = 0; i < providerJsonARray.length(); i++) {
                int value = providerJsonARray.getInt(i);
                provide_modelList.get(value).setChecked(true);
                uproviderList.add(value);
            }
            course_provide_adapter.notifyDataSetChanged();


            et_name.setText(courseName);
            tv_sub_category.setText(jsonObject.getString("category_name"));
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + jsonObject.getString("thumbnail"))
                    .into(iv_course_image);
            coverImgString = "";
            tv_expiry.setText(u_expy_date);
            rel_coverImg.setVisibility(View.VISIBLE);
            upload_video_btn.setVisibility(View.GONE);
            tv_imageName.setText("cover_image.jpeg");

            if (uFree_paid.equals("1")) {
                tv_courseType.setText("Free");
                et_price.setEnabled(false);
                et_discount.setEnabled(false);

            } else {
                if (price == null || TextUtils.isEmpty(price)) {
                    price = "0";
                } else {
                    if (price.equals("null")) {
                        price = "0";
                    } else {
                        et_price.setText(price);
                    }
                }
                tv_courseType.setText("Paid");
                et_discount.setText(u_discount);
            }
            et_detail.setText(details);
            et_about.setText(about);

            if (!TextUtils.isEmpty(live_dem_count)) {
                if (Integer.parseInt(live_dem_count) > 0) {
                    et_live_demo_count.setText(live_dem_count);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCreatorProfile(JSONObject jsonObject) {
        try {
                JSONObject object=jsonObject.getJSONObject("course_crator");
                if(object.length()>0){
                linChangeCreator.setVisibility(View.GONE);
                includeCreator.setVisibility(View.VISIBLE);
                creatorId=object.getString("id");
                creatorName=object.getString("name");
              creatorAbout=object.getString("about");
                creatorImgUrl=BaseUrl.BASE_URL_MEDIA+object.getString("image");
                Log.e("creatorImgLink",""+creatorImgUrl);

                tvProfileName.setText(creatorName);
                Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                        .into(ivProfileImg);

            }else{
                linChangeCreator.setVisibility(View.VISIBLE);
                includeCreator.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAssignedProfile(JSONObject jsonObject) {
        try {
            String assignValue=jsonObject.getString("assigned_value");
            if (assignValue !=null && !assignValue .equals("null")) {
                if (assignValue.equals("-1")) {
                    //not assigned to anyone
                    Log.e("asignValue","not assign");
                } else if (assignValue.equals("1")) {
                    //assigne to or by anyone
                    Log.e("asignValue","assign to");
                    if (management.getString(ASSIGN).equals(ASSIGN_BY)){
                        Log.e("asignValue","assign By");
                        JSONObject assignOb=jsonObject.getJSONObject("assign_by");
                        String imglink=BaseUrl.BASE_URL_MEDIA+assignOb.getString("image");
                        Log.e("imglink",imglink);
                        Picasso.get().load(imglink)
                                .placeholder(R.drawable.logoo)
                                .into(assignProfileImg);
                        assignTvProfileName.setText(assignOb.getString("name"));
//                        tvMobile.setText("+91-"+assignOb.getString("mobile"));
                    }else {
                        Log.e("asignValue","assign to");
                    }

                }
//                else if (assignValue.equals("2")) {
//                    //assigned by_anyone
//                    Log.e("asignValue","assign by");
//
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gotoHomeScreen() {
        getActivity().onBackPressed();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (updateOrAdd.equals("add")) {
            //((TeacherMainActivity) getActivity()).setChildActionBar(pageTitle);
            ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);
        } else {
            ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);
            ((Mycourse_deailFragment) getParentFragment()).setAboutBgColor();
        }
    }
}
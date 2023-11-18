package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.BaseUrl.URL_GET_LIVE_WATCH_HISTORY;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.Common_Package.Play_All_VideoFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Course_provider_adapter;
import soonflyy.learning.hub.Student_Pannel.Adapter.CreatorOtherCoursesAdapter;
import soonflyy.learning.hub.Student_Pannel.Adapter.DemoAdapter;
import soonflyy.learning.hub.Student_Pannel.Adapter.PurchaseAssignTutorAdapter;
import soonflyy.learning.hub.Student_Pannel.Model.Course_DetailsMOdel;
import soonflyy.learning.hub.Student_Pannel.Model.CreatorOtherCourse;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.live.LiveClassesActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Purchase_Course_Details extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = Purchase_Course_Details.class.getName();

    TextView tv_title, tv_course_desc, tv_subscriptions, tv_about, tv_course_offer, tv_demo_offer;
    ImageView iv_thumbnail, iv_bookmark;

    LinearLayout lin_curse_offer, lin_demo_offer, lin_offer_layout, lin_demo;
    LinearLayout linCreator, linTutor,linCreatorLayout,linCreatorOtherCourse;
    CircleImageView civCreatorImg;

    View lin_live_demo;
    TextView tv_live_time, tvCreatorName;
    Button btn_join_live;
    RoundedImageView iv_live;
    RelativeLayout rel_join_course;
    ConstraintLayout demoLiveLayout;
    RecyclerView recOtherCourses,recDemoVideo;
    private LinearLayout lin_thumbnail;


    RatingBar ratingBar;


    String content_id, content_name, course_id;
    String pageTitle;

    Course_DetailsMOdel model;
    SwipeRefreshLayout refresh_page;

    RecyclerView rec_provider, recTutors;
    ArrayList<String> providerList = new ArrayList<>();
    ArrayList<Integer> positionList = new ArrayList<>();
    Course_provider_adapter provider_adapter;

    JSONArray live_array;
    String history_count;

    PurchaseAssignTutorAdapter tutorAdapter;
    ArrayList<AssignProfile> tutorList = new ArrayList<>();

    CreatorOtherCoursesAdapter otherCoursesAdapter;
    ArrayList<CreatorOtherCourse>otherCourseList=new ArrayList<>();

    DemoAdapter demoAdapter;
    ArrayList<Video_Model>demoVideoList=new ArrayList<>();


    //-------creator data---//
    String creatorName = "", creatorImage = "", creatorAbout = "";
    //

    public Purchase_Course_Details() {
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
        View view = inflater.inflate(R.layout.fragment_purchase_course_details, container, false);
        CommonMethods.enableScreenshot(getActivity().getWindow());
        init(view);
        setProviderArray();
        getArgumentData();
      //  callCourseDetailsApi();
        initControl();
        gettutorList();
        getCreatorOtherCourse();
        return view;
    }

    private void getCreatorOtherCourse() {
        if (otherCourseList.size()>0){
            linCreatorOtherCourse.setVisibility(View.VISIBLE);
            otherCoursesAdapter=new CreatorOtherCoursesAdapter(getActivity(),
                    otherCourseList, new CreatorOtherCoursesAdapter.OnCreatorCourseClickListener() {
                @Override
                public void onItemClick(int position, String courseId) {
                    course_id=courseId;
                    callCourseDetailsApi();
                    pageTitle=otherCourseList.get(position).getTitle();
                    ((MainActivity)getActivity()).setStudentChildActionBar(pageTitle,false);
                }
            });
            recOtherCourses.setAdapter(otherCoursesAdapter);
        }else{
            linCreatorOtherCourse.setVisibility(View.GONE);
        }
    }

    private void setProviderArray() {
        providerList.clear();
        for (int i = 0; i < CommonMethods.getCourseProvide().length; i++) {
            providerList.add(CommonMethods.getCourseProvide()[i]);
        }
    }

    private void callCourseDetailsApi() {
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_COURSE_BY_ID);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void getArgumentData() {
        course_id = getArguments().getString("course_id");
        pageTitle = getArguments().getString("title");

    }


    private void init(View view) {
        recDemoVideo=view.findViewById(R.id.rec_video);
        demoLiveLayout=view.findViewById(R.id.live_layout);

        linCreatorOtherCourse=view.findViewById(R.id.lin_other_course);
        linCreatorLayout=view.findViewById(R.id.lin_created_course);
        linTutor = view.findViewById(R.id.lin_tutors);
        linCreator = view.findViewById(R.id.lin_creator);
        civCreatorImg = view.findViewById(R.id.iv_creator_img);
        tvCreatorName = view.findViewById(R.id.tv_creator_name);


        rec_provider = view.findViewById(R.id.rec_provider);
        iv_thumbnail = view.findViewById(R.id.img);
        recOtherCourses = view.findViewById(R.id.demo_video_rec_view);
        tv_title = view.findViewById(R.id.course_tittle);


        tv_course_desc = view.findViewById(R.id.course_details);


        rel_join_course = view.findViewById(R.id.rel_join_course);
        lin_thumbnail = view.findViewById(R.id.lin_course_thumbnail);
        iv_bookmark = view.findViewById(R.id.save_img);
        ratingBar = view.findViewById(R.id.rating_bar);
        tv_subscriptions = view.findViewById(R.id.tv_subscription);
        tv_about = view.findViewById(R.id.tv_liveclass);
        refresh_page = view.findViewById(R.id.refresh_course_details);

        tv_demo_offer = view.findViewById(R.id.tv_demo_offer);
        tv_course_offer = view.findViewById(R.id.tv_course_offer);
        lin_demo_offer = view.findViewById(R.id.lin_demo_offer);
        lin_curse_offer = view.findViewById(R.id.lin_course_offer);
        lin_offer_layout = view.findViewById(R.id.lin_offer_layout);
        lin_live_demo = view.findViewById(R.id.lin_demo_view);
        btn_join_live = view.findViewById(R.id.tv_join_btn);
        tv_live_time = view.findViewById(R.id.tv_live_time);
        iv_live = view.findViewById(R.id.live_imageview);
        lin_demo = view.findViewById(R.id.lin_demo_live);
        recTutors = view.findViewById(R.id.recTutors);

        recOtherCourses.setLayoutManager(new LinearLayoutManager(getActivity()));
        recTutors.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recDemoVideo.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));


    }


    private void initControl() {
        linCreator.setOnClickListener(this);
        refresh_page.setOnRefreshListener(this);
        rel_join_course.setOnClickListener(this);
        iv_bookmark.setOnClickListener(this);
        btn_join_live.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_creator:
                //show creator dialog detials
                showProfileDetails("creator", null);
                break;

            case R.id.rel_join_course:
                Log.e("click", "p click");

                gotoByeCourse();
                break;

            case R.id.video_tv:
                //  goToVideoFragment();
                break;
            case R.id.save_img:
                bookMarkCourse();
                break;
            case R.id.tv_join_btn:
                //join live demo class
                if (ConnectivityReceiver.isConnected()) {
                    sendRequest(ApiCode.GET_LIVE_WATCH_HISTORY);
                } else {
                    CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                }
                //goForDemoLive();
                break;
        }
    }

    private void goForDemoLive() {
        if (CommonMethods.checkAudioCameraPermission(getContext())) {
            if (live_array != null) {
                if (live_array.length() > 0) {
                    try {
                        JSONObject jsonObject = live_array.getJSONObject(0);
                        if (jsonObject.getString("is_live").equals("1")) {


                            Intent lIntent = new Intent(getActivity(), LiveClassesActivity.class);
                            lIntent.putExtra("title", pageTitle);
                            lIntent.putExtra("sTime", jsonObject.getString("start_time"));
                            lIntent.putExtra("eTime", jsonObject.getString("end_time"));
                            lIntent.putExtra("slug", jsonObject.getString("slug"));
                            lIntent.putExtra("live_type", "demo");
                            lIntent.putExtra("type", "student");
                            lIntent.putExtra("description", jsonObject.getString("description"));
                            lIntent.putExtra("live_id", jsonObject.getString("id"));
                            lIntent.putExtra("course_id", model.getId());
                            lIntent.putExtra("user_id", new SessionManagement(getContext()).getString(USER_ID));
                            lIntent.putExtra("demo_count", model.getLive_demo_count());
                            lIntent.putExtra("history_count", history_count);

                            lIntent.putExtra("token",jsonObject.getString("token"));
                            lIntent.putExtra("meeting_id",jsonObject.getString("meeting_id"));
                            lIntent.putExtra("from",SIMPLEE_HOME_STUDENT);
                            lIntent.putExtra("hostId",jsonObject.getString("user_id"));

                            getActivity().startActivity(lIntent);
                        } else {
                            CommonMethods.showSuccessToast(getActivity(), "Teacher is not Live");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        } else {
            CommonMethods.requestAudioCameraPermission(getActivity(), 333);
        }
    }

    private void gotoByeCourse() {
        if (model.getIs_purchased().equals("0")) {
            if (model.getIs_free_course().equals("1")) {
                if (ConnectivityReceiver.isConnected()) {
                    sendRequest(ApiCode.PURCHASE_COURSE);
                } else {
                    CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                }
            } else {
                BuyCourseFragment buyCourseFragment = new BuyCourseFragment();
                Bundle arg = new Bundle();
                arg.putParcelable("course", model);
                buyCourseFragment.setArguments(arg);
                ((MainActivity) getActivity()).SwitchFragment(buyCourseFragment);
            }
        } else {
            CommonMethods.showSuccessToast(getContext(), "You have already purchased this course");
        }
    }

    private void bookMarkCourse() {
        // if (course_type.equals("teacher_course")){
        ///check and save and remove form bookmark
        if (model.getIs_bookmark().equals("0")) {
            content_name = model.getTitle();
            content_id = course_id;
            if (ConnectivityReceiver.isConnected()) {
                sendRequest(ApiCode.ADD_BOOKMARK);
            }
        } else {
            showRemoveBookmarkAlert();
        }

        //  }
    }

    private void gettutorList() {
        if (tutorList.size() > 0) {
            tutorAdapter = new PurchaseAssignTutorAdapter(getActivity(), tutorList, new PurchaseAssignTutorAdapter.OnTestClickListener() {
                @Override
                public void onItemClick(int position, AssignProfile model) {
                    showProfileDetails("tutor", model);

                }
            });
            recTutors.setAdapter(tutorAdapter);
            tutorAdapter.notifyDataSetChanged();
            linTutor.setVisibility(View.VISIBLE);
        } else {
            linTutor.setVisibility(View.GONE);
        }


    }
    private void getDemoVideo(){
        if (demoVideoList.size()>0){
            demoAdapter=new DemoAdapter(getActivity(), demoVideoList, new DemoAdapter.OnVideoClickListener() {
                @Override
                public void onPlayVideo(int position, String videoUrl) {
                    Play_All_VideoFragment fragment = new Play_All_VideoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("videoList", demoVideoList);
                    bundle.putInt("position", position);
                    bundle.putString("from", SIMPLEE_HOME_STUDENT);
                    bundle.putString("courseName", pageTitle);
                    fragment.setArguments(bundle);
                    ((MainActivity) getActivity()).SwitchFragment(fragment);
                }
            });
            recDemoVideo.setAdapter(demoAdapter);
            lin_demo.setVisibility(View.VISIBLE);
            recDemoVideo.setVisibility(View.VISIBLE);
        }else{
            recDemoVideo.setVisibility(View.GONE);
        }
    }


    private void showProfileDetails(String type, AssignProfile model) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_purchasecourse_tutor_details);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        TextView tvProfileName = dialog.findViewById(R.id.tv_dailoge_tutor_name);
        TextView tvProfileType = dialog.findViewById(R.id.tv_dailoge_tutor);
        TextView tvProfileAbout = dialog.findViewById(R.id.tv_dailoge_user_detail);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        CircleImageView ivProfileImg = dialog.findViewById(R.id.iv_profile_image);
        if (type.equals("creator")) {
            //for creator details
            Picasso.get().load(creatorImage).placeholder(R.drawable.logoo)
                    .into(ivProfileImg);
            tvProfileName.setText(creatorName);
            tvProfileType.setText("Course Creator");
            tvProfileAbout.setText(creatorAbout);
        } else {
            //for tutor details
            if (model != null) {
                String link = BaseUrl.BASE_URL_MEDIA + model.getImage();
                Picasso.get().load(link).placeholder(R.drawable.logoo)
                        .into(ivProfileImg);
                tvProfileName.setText(model.getName());
                tvProfileType.setText("Tutor");
                tvProfileAbout.setText(model.getAbout());
            }
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setStudentChildActionBar(pageTitle, false);

        callCourseDetailsApi();
    }

    @Override
    public void onStop() {
        super.onStop();
//        try {
//            if (simpleExoPlayer!=null){
//                simpleExoPlayer.stop();
//                simpleExoPlayer.release();
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }

    }

    private void showRemoveBookmarkAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setCancelable(false)
                .setMessage("Are you sure to remove from bookmark ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.REMOVE_BOOKMARK);
                        } else {
                            DynamicToast.makeError(getContext(), "No Internet Connection", 3000).show();
                        }
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.ADD_BOOKMARK:
                params.put("type", "course");
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("content", content_id);
                params.put("name", content_name);
                callApi(ApiCode.ADD_BOOKMARK, params);
                break;
            case ApiCode.REMOVE_BOOKMARK:
                params.put("content", course_id);//content_id
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.REMOVE_BOOKMARK, params);
                break;
            case ApiCode.PURCHASE_COURSE:
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("course_id", course_id);
                params.put("price", "0");
                params.put("type", "free");//type means free or paid
                params.put("payment_type", "");//offline,upi,or online & others
                params.put("reciept_no", "");//when payment is offline
                params.put("details", "");//when payment is offline
                callApi(ApiCode.PURCHASE_COURSE, params);
                break;
            case ApiCode.GET_COURSE_BY_ID:
                params.put("type", "0");
                params.put("course_id", course_id);
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_COURSE_BY_ID, params);
                break;
            case ApiCode.GET_LIVE_WATCH_HISTORY:
                params.put("course_id", course_id);
                params.put("user_id", new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_LIVE_WATCH_HISTORY, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.ADD_BOOKMARK:
                service.postDataVolley(ApiCode.ADD_BOOKMARK,
                        BaseUrl.URL_ADD_BOOKMARK, params);
                break;

            case ApiCode.REMOVE_BOOKMARK:
                service.postDataVolley(ApiCode.REMOVE_BOOKMARK,
                        BaseUrl.URL_REMOVE_BOOKMARK, params);
                break;
            case ApiCode.PURCHASE_COURSE:
                service.postDataVolley(ApiCode.PURCHASE_COURSE,
                        BaseUrl.URL_PURCHASE_COURSE, params);
                break;
            case ApiCode.GET_COURSE_BY_ID:
                service.postDataVolley(ApiCode.GET_COURSE_BY_ID,
                        BaseUrl.URL_GET_COURSE_BY_ID, params);
                break;
            case ApiCode.GET_LIVE_WATCH_HISTORY:
                service.postDataVolley(ApiCode.GET_LIVE_WATCH_HISTORY,
                        URL_GET_LIVE_WATCH_HISTORY, params);
                Log.e("api", "" + URL_GET_LIVE_WATCH_HISTORY);
                Log.e("watch", "" + params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType) {
            case ApiCode.ADD_BOOKMARK:
            case ApiCode.REMOVE_BOOKMARK:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));

                        if (requestType == ApiCode.REMOVE_BOOKMARK) {
                            new CommonMethods().setImageIconTintColor(getContext(), iv_bookmark, R.color.light_gray, true);
                            model.setIs_bookmark("0");
                            //coursesModel.setBookmark_id("0");
                        } else {
                            String bookmarkId = jsonObject.getJSONObject("data").getString("bookmark_id");
                            new CommonMethods().setImageIconTintColor(getContext(), iv_bookmark, R.color.primary_color, true);

                            model.setIs_bookmark("1");
                            //coursesModel.setBookmark_id(bookmarkId);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ApiCode.PURCHASE_COURSE:
                Log.e("purchase: ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        CommonMethods.showSuccessToast(getContext(), "Subscribed successfully.");
                        // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        //((MainActivity)getActivity()).goToSubsriptionFragment();
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case ApiCode.GET_COURSE_BY_ID:
                Log.e("course_details", response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray tutorArray = jsonObject.getJSONArray("assign_to");
                    JSONArray otherCourseArray = jsonObject.getJSONArray("course_create_other");
                    JSONObject creatorObject = jsonObject.getJSONObject("course_crator");
                    setCreatorProfile(creatorObject);
                    setTutorData(tutorArray);
                    setOtherCourse(otherCourseArray);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        model = (Course_DetailsMOdel) VolleyService.response(jsonArray.getJSONObject(0).toString(), Course_DetailsMOdel.class);
                        setData();
                        live_array = jsonObject.getJSONArray("data_live");
                        setLiveData(live_array);
                        JSONArray demoArray=jsonArray.getJSONObject(0).getJSONArray("live_demo_video");
                        setDemoVideoData(demoArray);
                        if(live_array.length()==0 && demoArray.length()==0){
                            lin_demo.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case ApiCode.GET_LIVE_WATCH_HISTORY:
                Log.e("watch_histoyr", response);
                JSONObject jObject = new JSONObject(response);
                if (jObject.getBoolean("response")) {
                    history_count = jObject.getString("history_count");
                    int left = Integer.parseInt(history_count);
                    if (left < Integer.parseInt(model.getLive_demo_count())) {
                        goForDemoLive();
                    } else {
                        showLefLiveDialog();
                    }

                }

                break;


        }
    }

    private void setDemoVideoData(JSONArray demoArray) {
        demoVideoList.clear();
        if (demoArray.length() > 0) {
            ArrayList<Video_Model> psearch = new Gson().
                    fromJson(demoArray.toString(),
                            new com.google.gson.reflect.TypeToken<ArrayList<Video_Model>>() {
                            }.getType());
            demoVideoList.addAll(psearch);
        }
        getDemoVideo();
    }

    private void setOtherCourse(JSONArray otherCourseArray) {
        otherCourseList.clear();
        if (otherCourseArray.length() > 0) {
            ArrayList<CreatorOtherCourse> psearch = new Gson().
                    fromJson(otherCourseArray.toString(),
                            new com.google.gson.reflect.TypeToken<ArrayList<CreatorOtherCourse>>() {
                            }.getType());
            otherCourseList.addAll(psearch);
        }
        getCreatorOtherCourse();
    }

    private void setTutorData(JSONArray tutorArray) {
        tutorList.clear();
        if (tutorArray.length() > 0) {
            ArrayList<AssignProfile> psearch = new Gson().
                    fromJson(tutorArray.toString(),
                            new com.google.gson.reflect.TypeToken<ArrayList<AssignProfile>>() {
                            }.getType());
            tutorList.addAll(psearch);
        }
        gettutorList();
    }

    private void setCreatorProfile(JSONObject object) {
        if (object !=null && object.length()>0) {
            linCreatorLayout.setVisibility(View.VISIBLE);
            try {
                creatorName = object.getString("name");
                creatorImage = BaseUrl.BASE_URL_MEDIA + object.getString("image");
                creatorAbout = object.getString("about");
                Picasso.get().load(creatorImage).placeholder(R.drawable.logoo)
                        .into(civCreatorImg);
                tvCreatorName.setText(creatorName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            linCreatorLayout.setVisibility(View.GONE);
        }

    }

    private void setLiveData(JSONArray data_live) {

        if (data_live.length() > 0) {
            try {
                JSONObject jsonObject = data_live.getJSONObject(0);
                if (CommonMethods.isCurrentDate(jsonObject.getString("date"))) {
                    lin_demo.setVisibility(View.VISIBLE);
                    demoLiveLayout.setVisibility(View.VISIBLE);
                    // if (jsonObject.getString("is_live").equals("1")) {
//                        Picasso.get().load(BaseUrl.BASE_URL_MEDIA + jsonObject.getString("thumbnail"))
//                                .placeholder(R.color.black).into(iv_live);

                    tv_live_time.setText(jsonObject.getString("start_time") + " to " + jsonObject.getString("end_time"));
                    if (jsonObject.getString("is_live").equals("0")) {
                        btn_join_live.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.primary_color));
                        //btn_join_live.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.primary_color)));
                    }
                    // }
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }


        } else {
            //lin_demo.setVisibility(View.GONE);
            demoLiveLayout.setVisibility(View.GONE);
        }
    }

    private void setData() {
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA + model.getThumbnail()).placeholder(R.drawable.logoo)
                .into(iv_thumbnail);
        if (model.getIs_bookmark().equals("1")) {
            new CommonMethods().setImageIconTintColor(getContext(), iv_bookmark, R.color.primary_color, true);
        } else {
            new CommonMethods().setImageIconTintColor(getContext(), iv_bookmark, R.color.light_gray, true);
        }
        tv_subscriptions.setText("Subscriptions : " + model.getSubscription());
        tv_title.setText(model.getTitle());
        tv_course_desc.setText(model.getInstructor_name());
        tv_about.setText(model.getDescription());
        ratingBar.setRating(Float.parseFloat(model.getRating()));
        // setDemoVideo(model.getLive_demo_video());
        setCourseProviderList(model.getCourse_provider());

        try {
            setCourseOffer();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setCourseOffer() throws JSONException {
        JSONArray jsonArray = new JSONArray(model.getCourse_offers());
        ArrayList<String> u_courseOffer = new ArrayList<>();
        if (jsonArray.length() > 0) {
            u_courseOffer.add(jsonArray.getString(0));
            u_courseOffer.add(jsonArray.getString(1));
        }
        if (jsonArray.length() == 0 && Integer.parseInt(model.getLive_demo_count()) == 0) {
            lin_offer_layout.setVisibility(View.GONE);
        } else {
            lin_offer_layout.setVisibility(View.VISIBLE);
            if (u_courseOffer.size() > 0) {
                lin_curse_offer.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(model.getOffer_course_price())) {
                    if (Float.parseFloat(model.getOffer_course_price()) > 0) {
                        tv_course_offer.setText("Free " + u_courseOffer.get(1) + " course worth Rs. " + model.getOffer_course_price());
                    } else {
                        tv_course_offer.setText("Free " + u_courseOffer.get(1) + " course");
                    }
                } else {
                    tv_course_offer.setText("Free " + u_courseOffer.get(1) + " course");
                }

            } else {
                lin_curse_offer.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(model.getLive_demo_count())) {
                if (Float.parseFloat(model.getLive_demo_count()) > 0) {
                    lin_demo_offer.setVisibility(View.VISIBLE);
                    tv_demo_offer.setText("You'll get " + model.getLive_demo_count() + " Demo live classes with this course free");
                } else {
                    lin_demo_offer.setVisibility(View.GONE);
                }
            } else {
                lin_demo_offer.setVisibility(View.GONE);
            }
        }
    }

    private void setCourseProviderList(String course_provider) {
        rec_provider.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            JSONArray providerJsonARray = new JSONArray(course_provider);
            positionList.clear();
            for (int i = 0; i < providerJsonARray.length(); i++) {
                int value = providerJsonARray.getInt(i);
                positionList.add(value);
            }
            provider_adapter = new Course_provider_adapter(getContext(), providerList, positionList);
            rec_provider.setAdapter(provider_adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRefresh() {
        refresh_page.setRefreshing(false);
        callCourseDetailsApi();

    }

    private void showLefLiveDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_live_demo_left);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        Button btn_confirm_bye = dialog.findViewById(R.id.btn_confirm);
        TextView tv_msg = dialog.findViewById(R.id.tv_live_left);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        int left = Integer.parseInt(model.getLive_demo_count()) - Integer.parseInt(history_count);
        tv_msg.setText("You have only " + left + "/" + model.getLive_demo_count() + "\ndemo left");
        if (left == 0) {
            btn_confirm_bye.setText("Purchase Course");
            tv_cancel.setVisibility(View.VISIBLE);
        }
        dialog.show();
        btn_confirm_bye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (left == 0) {
                    gotoByeCourse();
                }
                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
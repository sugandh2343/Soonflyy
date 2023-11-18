package soonflyy.learning.hub.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static soonflyy.learning.hub.Common.Constant.COURSE_PRICE;
import static soonflyy.learning.hub.Common.Constant.ENROLL_ID;
import static soonflyy.learning.hub.Common.Constant.FIRST_NAME;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.LAST_NAME;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.PAYMENT_ID;
import static soonflyy.learning.hub.Common.Constant.PROFILE_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.Common_Package.Play_All_VideoFragment.fullscreen;
import static soonflyy.learning.hub.Common_Package.Play_All_VideoFragment.playFullScreen;
import static soonflyy.learning.hub.utlis.AppConstant.FROM_HOME_SECTION;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common.Constant;
import soonflyy.learning.hub.Common_Package.IDSection.IDSectionFragment;
import soonflyy.learning.hub.Common_Package.IDSection.IDUserProfileFragment;
import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.Common_Package.NotificationFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Downloaded_Videos_Fragment;
import soonflyy.learning.hub.Student_Pannel.HomeFragment;
import soonflyy.learning.hub.Student_Pannel.MyBookmarks_Fragment;
import soonflyy.learning.hub.Student_Pannel.MySubscriptionFragment;
import soonflyy.learning.hub.Student_Pannel.ResultFragment;
import soonflyy.learning.hub.Student_Pannel.Student_Profile_Fragment;
import soonflyy.learning.hub.Student_Pannel.Subscribed_Course_Details;
import soonflyy.learning.hub.Teacher_Pannel.AboutusFragment;
import soonflyy.learning.hub.Teacher_Pannel.StudentListFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolLoginMainActivity;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.bottomsheet.CustomHelpCenterBottomSheet;
import soonflyy.learning.hub.model.Course_Category_Model;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        CustomHelpCenterBottomSheet.ItemClickListener, VolleyResponseListener, PaymentResultWithDataListener {

    private Toolbar toolbar;
    private LinearLayout custom_item_layout;
    //AppBarLayout app_bar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView user_image;
    ImageView menuImageView,iv_setting;
    TextView tv_userName,tv_mobile,tv_toolbar_title;
    ImageView iv_tool_bar_back;
    LinearLayout lin_toolbar_title;

    SearchView et_searchview;
    RelativeLayout li_user_profile;
    LinearLayout li_subscription,li_home,li_bookmarks,li_help_center,li_assignment,li_about_us,li_contact_us,li_terms_condition,
            li_chat_session,lin_logout,lin_discussion,lin_download_video;
    String from;

    List<Course_Category_Model> subCategoryList=new ArrayList<>();
    ArrayAdapter<Course_Category_Model>serchAdapter;

   // ImageView filterTextView;
    SessionManagement session_management;
 public ActionBarDrawerToggle drawerToggle;
    BottomNavigationView bottomNavigationView;


    ArrayList<Notification_model>notificationList=new ArrayList<>();
    SearchView et_search;
    ImageView iv_bookmark,iv_notification;
    TextView tv_notification_count;

    Timer deviceInfoTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CommonMethods.disableScreenshot(getWindow());
        setContentView(R.layout.activity_main);
        session_management=new SessionManagement(MainActivity.this);
        /////// fragment home munu set krna....
        Fragment selectedFragment = new HomeFragment();
      //  SwitchFragment(selectedFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                selectedFragment).commit();
        session_management.setInt("selected_Cat",0);

        //----------//
       // startDeviceInfotask();
        //-------------//
        if (!CommonMethods.checkNotificationPermission(this)){
            CommonMethods.requestNotificationPermission(this,108);
        }
        init();
        initControl();
        myObserve();
        //checkStatus();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        drawerToggle = new ActionBarDrawerToggle (this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);

        drawerToggle.syncState();
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        /*Fragment fm = new HomeFragment ();
      //  getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,fm,"Home").commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,fm).commit();

         */
       setHomeProfile();
        if (ConnectivityReceiver.isConnected()){
            //sendRequest(ApiCode.GET_SUB_CATEGORY);
            //sendRequest(ApiCode.GET_USER_DETAILS);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount()>0){
                   onBackPressed();
                }else{
                    drawerLayout.addDrawerListener(drawerToggle);
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawerToggle.syncState();
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void startDeviceInfotask() {
        deviceInfoTimer=new Timer();
        deviceInfoTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (CommonMethods.checkInternetConnection(MainActivity.this)){
                    CommonMethods.callDeviceInfoTaskApi(MainActivity.this,session_management.getString(USER_ID),"0",deviceInfoTimer
                            );
                }
            }
        },5000,10000);
    }

    public void setHomeProfile() {
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+session_management.getString(PROFILE_IMAGE))
                .placeholder(R.drawable.profile).into(user_image);
        tv_userName.setText(session_management.getString(NAME));
        tv_mobile.setText("+91-"+session_management.getString(MOBILE));
    }

    private void checkStatus() {
        new HomeFragment();
        if(from != null){
            if(from.equalsIgnoreCase(HomeFragment.TAG)){
                showHomeFragment();
            }else if(from.equalsIgnoreCase(MySubscriptionFragment.TAG)){
                goToSubsriptionFragment();
            }
        }else{
            showHomeFragment();
        }
    }

    @Override
    public void init() {
        lin_toolbar_title=findViewById(R.id.lin_toolbar_action);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        iv_tool_bar_back = findViewById(R.id.img_toolbar_back);

        tv_notification_count=findViewById(R.id.tv_notifi_no);
        iv_notification=findViewById(R.id.iv_notification);
        et_search=findViewById (R.id.et_search);
        et_search.setQueryHint("Search courses");
        iv_bookmark=findViewById(R.id.iv_bookmark);

        custom_item_layout=findViewById(R.id.custom_tool_item);
        toolbar=findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuImageView = findViewById(R.id.menuImageView);
      //  app_bar = findViewById(R.id.app_bar);
        iv_setting = findViewById(R.id.iv_setting);
        user_image = findViewById(R.id.user_image);
        tv_mobile=findViewById(R.id.tv_number);
        tv_userName=findViewById(R.id.assign_tv_name);

        li_subscription = findViewById(R.id.li_subscription);
        li_user_profile = findViewById(R.id.li_user_profile);
        li_home = findViewById(R.id.li_home);
        li_bookmarks = findViewById(R.id.li_bookmarks);
        li_help_center = findViewById(R.id.li_help_center);
        li_contact_us= findViewById(R.id.li_contact_us);
        li_terms_condition= findViewById(R.id.li_terms_condition);
        li_assignment= findViewById(R.id.li_attendance);
        li_about_us=findViewById (R.id.li_about_us);
        li_chat_session=findViewById(R.id.li_chat_session);
        lin_logout=findViewById(R.id.lin_logout);
       // filterTextView=findViewById (R.id.iv_filterTextView);
        et_searchview=findViewById(R.id.et_search);
        lin_discussion=findViewById(R.id.li_discussion);
        lin_download_video=findViewById(R.id.li_download_video);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation_bottom);
        setProfile();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigation_bottom =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    Bundle bundle=new Bundle();
                    Log.e("namma", "onNavigationItemSelected: "+ item.getItemId());

                    switch (item.getItemId()) {
                       case R.id.bottom_home:
                            clearBackStack();
                           //Toast.makeText(getApplicationContext(), "texdrdtfyfg", Toast.LENGTH_SHORT).show();
                       //     selectedFragment = new HomeFragment();
//                                Intent intent_home= new Intent(TeacherMainActivity.this,)
                            break;

                        case R.id.bottom_school:
                          // selectedFragment = new YourSchoolChoiceFragment();
                            session_management.setString(LOGIN_PAGE_TYPE,FROM_HOME_SECTION);
                            startActivity(new Intent(MainActivity.this, SchoolLoginMainActivity.class));

                            break;

                        case R.id.bottom_idsection:
                            session_management.setString(ID_SECTION_USER_ID,session_management.getString(USER_ID));
                            if (session_management.getString(ID_SECTION).equals("0")) {
                                selectedFragment = new IDSectionFragment();
                                bundle.putString("type","add");
                            }else {
                                selectedFragment = new IDUserProfileFragment();
                            }
                            break;


                    }
                    if (selectedFragment != null) {
                        clearBackStack();
                        bundle.putString("user_type","student");
                        selectedFragment.setArguments(bundle);
                        SwitchFragment(selectedFragment);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
//                                selectedFragment).commit();
                    }
                    return true;
                }
            };

    public void clearBackStack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private void setProfile() {
        tv_userName.setText(session_management.getString(FIRST_NAME)+" "+session_management.getString(LAST_NAME));
       // tv_mobile.setText("+91"+session_management.getString(MOB));
    }

    @Override
    public void initControl() {
      //  setSearchItemList();
        iv_tool_bar_back.setOnClickListener(this);
        iv_notification.setOnClickListener(this);
        iv_bookmark.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        menuImageView.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        li_subscription.setOnClickListener(this);
        li_home.setOnClickListener(this);
        li_user_profile.setOnClickListener(this);
        li_bookmarks.setOnClickListener(this);
        li_help_center.setOnClickListener(this);
        li_assignment.setOnClickListener (this);
        li_contact_us.setOnClickListener (this);
        li_terms_condition.setOnClickListener (this);
        li_about_us.setOnClickListener (this);
        li_chat_session.setOnClickListener (this);
        lin_discussion.setOnClickListener (this);
        lin_logout.setOnClickListener (this);
      //  filterTextView.setOnClickListener (this);
        lin_download_video.setOnClickListener(this);
        lin_discussion.setOnClickListener(this);
    }



    @Override
    public void myObserve() {

    }



    public void checkDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.iv_filterTextView:
//                showItem();
//            break;

            case R.id.img_toolbar_back:
                onBackPressed();
                break;
            case R.id.iv_notification:
                gotoNotification();
                break;
            case R.id.iv_bookmark:
                gotoBookMarkScreen();
                break;
            case R.id.menuImageView:
                checkDrawerOpen();
                break;
            case R.id.iv_setting:
                goToProfileFragment();
               // app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_subscription:
                goToSubsriptionFragment();
             //   app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_home:
                drawerLayout.closeDrawers();
               // showHomeFragment();
                clearBackStack();
                break;
            case R.id.li_user_profile:
               // app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
              //  showStudentFragment();
                break;
            case R.id.li_bookmarks:
               // app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                showBookmarkFragment();
                break;
            case R.id.li_help_center:
                openHelpCenterBottomSheet();
                break;
            case R.id.li_contact_us:
                AboutusFragment abofragment = new AboutusFragment ();
                Bundle argss = new Bundle();
                argss.putString("title","contact");
                argss.putString ("type","student");
                abofragment.setArguments(argss);
                SwitchFragment(abofragment);
               // app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_about_us:
                AboutusFragment afragment = new AboutusFragment ();
                Bundle args = new Bundle();
                args.putString("title","About Us");
                args.putString ("type","student");
                afragment.setArguments(args);
                SwitchFragment(afragment);
              //  app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_terms_condition:
                AboutusFragment abfragment = new AboutusFragment ();
                Bundle arg = new Bundle();
                arg.putString("title","condition");
                arg.putString ("type","student");
                abfragment.setArguments(arg);
                SwitchFragment(abfragment);
              //  app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_attendance:

                //goToSAssignmentFragment ();
                //app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;

            case R.id.li_chat_session:
                chatFragment ();
                //app_bar.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                break;
            case R.id.lin_logout:
               // showLogoutAlert();
                CommonMethods.showLogoutDialog(this,SIMPLEE_HOME_STUDENT);
                break;
            case R.id.li_discussion:
                chatFragment ();
                drawerLayout.closeDrawers();
                break;
            case R.id.li_download_video:
                if (CommonMethods.checkReadPermission(this)) {
                    Downloaded_Videos_Fragment fragment = new Downloaded_Videos_Fragment();
                    SwitchFragment(fragment);
                }else{
                    CommonMethods.requestPermission(this,134);
                }
                drawerLayout.closeDrawers();
                break;



        }

    }
    public ImageView getToolbarBackBtn(){
        return iv_tool_bar_back;
    }
    public void setNotifications(ArrayList<Notification_model> nlist ){
        notificationList.clear();
        notificationList.addAll(nlist);
        int size=notificationList.size();
        if (size > 0) {
            tv_notification_count.setText(String.valueOf(size));
            tv_notification_count.setVisibility(VISIBLE);
        } else {
            tv_notification_count.setVisibility(GONE);
            tv_notification_count.setText("0");
        }

    }
    private void gotoNotification() {
        NotificationFragment fragment=new NotificationFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",SIMPLEE_HOME_STUDENT);
        bundle.putParcelableArrayList("notifications",notificationList);
        fragment.setArguments(bundle);
       SwitchFragment(fragment);
    }
    private void gotoBookMarkScreen() {
        SwitchFragment(new MyBookmarks_Fragment());
    }

    private void chatFragment() {
        StudentListFragment bookmarkFragment = new StudentListFragment();
        Bundle args = new Bundle();
        args.putString("type","student");
        args.putString("user_id",session_management.getString(Constant.USER_ID));
        bookmarkFragment.setArguments(args);
        SwitchFragment (bookmarkFragment);
    }


    private void showBookmarkFragment() {
        MyBookmarks_Fragment bookmarkFragment=new MyBookmarks_Fragment();
//        BookmarkFragment bookmarkFragment = new BookmarkFragment();
//        Bundle args = new Bundle();
//        args.putString(AppConstant.FROM,"from");
//        bookmarkFragment.setArguments(args);
        SwitchFragment (bookmarkFragment);
    }



    HomeFragment homeFragment;
    private void showHomeFragment() {
        //toolbar.setVisibility(View.GONE);
        homeFragment = new HomeFragment();
        //SwitchFragment(homeFragment);
    }

    public void goToSubsriptionFragment() {
        MySubscriptionFragment mySubscriptionFragment = new MySubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.FROM,"from");
        mySubscriptionFragment.setArguments(args);
        SwitchFragment (mySubscriptionFragment);
    }

    private void goToProfileFragment() {
       //ProfileFragment profileFragment = new ProfileFragment();
        Student_Profile_Fragment profile_fragment=new Student_Profile_Fragment();
        SwitchFragment (profile_fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        return false;
    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
      //  fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openHelpCenterBottomSheet() {
        CustomHelpCenterBottomSheet bottomSheet = new CustomHelpCenterBottomSheet();
        bottomSheet.show(getSupportFragmentManager(),CustomHelpCenterBottomSheet.TAG);
    }
    @Override
    public void onItemClick(String item, View view) {
        new CommonMethods().callUs(this);
    }



    public void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
//            case ApiCode.GET_DEVICE_INFO:
//                params.put("user_id",new SessionManagement(this).getString(Constant.USER_ID));
//                //params.put("type","0");
//                callApi(ApiCode.GET_DEVICE_INFO,params);
//                break;
            case ApiCode.GET_USER_DETAILS:
                params.put("user_id",new SessionManagement(this).getString(Constant.USER_ID));
                params.put("type","0");
                callApi(ApiCode.GET_USER_DETAILS,params);
                break;
            case ApiCode.GET_SUB_CATEGORY:
                params.put("user_id",new SessionManagement(this).getString(Constant.USER_ID));
                callApi(ApiCode.GET_SUB_CATEGORY,params);
                break;
            case ApiCode.PAYMENT_SUCCESS:
                params.put("user_id", new SessionManagement(this).getString(USER_ID));
                params.put("enrol_id", ENROLL_ID);
                params.put("type", "course");//type will be test
                params.put("amount",COURSE_PRICE);//price
                params.put("transaction_id",PAYMENT_ID );
                callApi(ApiCode.PAYMENT_SUCCESS, params);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, this);
        switch (request){
//            case ApiCode.GET_DEVICE_INFO:
//                service.postDataVolley(ApiCode.GET_DEVICE_INFO
//                        , URL_GET_DEVICE_INFO,params);
//                Log.e("apiurl",""+URL_GET_DEVICE_INFO);
//                Log.e("parmPost",""+params);
//                break;
            case ApiCode.GET_USER_DETAILS:
                service.postDataVolley(ApiCode.GET_USER_DETAILS
                        ,BaseUrl.URL_GET_USER_DETAILS,params);
                break;
            case ApiCode.GET_SUB_CATEGORY:
                service.postDataVolley(ApiCode.GET_SUB_CATEGORY
                        ,BaseUrl.URL_GET_SUB_CATEGORY,params);
                break;

            case ApiCode.PAYMENT_SUCCESS:
                service.postDataVolley(ApiCode.PAYMENT_SUCCESS,
                        BaseUrl.URL_PAYMENT_SUCCESS, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType) {
//            case ApiCode.GET_DEVICE_INFO:
//                Log.e("deviceInfo", response);
//                //setDataToView(response);
//                break;
            case ApiCode.GET_USER_DETAILS:
                Log.e(" profile", response);
                setDataToView(response);
                break;
            case ApiCode.GET_SUB_CATEGORY:
                Log.e(" subCat", response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getBoolean("status")){
                        JSONArray array = object.getJSONArray("data");
                        List<Course_Category_Model> psearch = new Gson().
                                fromJson(array.toString(),
                                        new TypeToken<List<Course_Category_Model>>() {
                                        }.getType());
                        subCategoryList.clear();
                        subCategoryList.addAll(psearch);
                        serchAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ApiCode.PAYMENT_SUCCESS:
                Log.e("success ", response);
                try {
                    JSONObject onObject = new JSONObject(response);
                    if (onObject.getBoolean("status")){
                        PAYMENT_ID="";
                        ENROLL_ID="";
                        COURSE_PRICE="";
                        showSucceesDailog();
//                        CommonMethods.showSuccessToast(this,"Course Subscribed Successfully");
//                       getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                        goToSubsriptionFragment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void showSucceesDailog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_payment_sucess);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();


        ImageView ivcancel = dialog.findViewById(R.id.iv_cancel);
        ivcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonMethods.showSuccessToast(MainActivity.this,"Course Subscribed Successfully");
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                goToSubsriptionFragment();
            }
        });

    }

    private void setDataToView(String response) {
        try {
            JSONObject object=new JSONObject(response);
            boolean res=object.getBoolean("status");
            if (res) {
                JSONObject job = object.getJSONObject("data");
                if (job.getString("status").equals("0")){
                    showAccountStatusAlertMessage();
                }
                String imageUrl = BaseUrl.BASE_URL_MEDIA + job.getString("image");
                Log.e("Profile_img_url", imageUrl);
                Picasso.get().load(imageUrl).placeholder(R.drawable.profile).error(R.drawable.prifile_error).into(user_image);
                String fullName = job.getString("first_name") + "" + job.getString("last_name");
                String mobileNo = job.getString("mobile");
                tv_userName.setText(fullName);
                tv_mobile.setText("+91-"+mobileNo);
                session_management.setString(FIRST_NAME,job.getString("first_name"));
                session_management.setString(LAST_NAME,job.getString("last_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showAccountStatusAlertMessage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Server Message")
                .setMessage("Your account is inactivated by admin. You haven't access to view contents of this app.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent ( MainActivity.this, SplashActivity.class);
                        session_management.logoutSession ();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        finish ();
                    }
                }).show();
    }


    private void showAllFragment() {
        AboutusFragment afragment = new AboutusFragment ();
        SwitchFragment(afragment);
    }



    public  void setStudentChildActionBar(String title,boolean show){
       // getSupportActionBar().setTitle(title);
        tv_toolbar_title.setText(title);
        if (show) {
            lin_toolbar_title.setVisibility(View.GONE);
            custom_item_layout.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(false);
        } else {
            custom_item_layout.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            lin_toolbar_title.setVisibility(View.VISIBLE);
        }
        //setBAckArrowColor();

        if (!getSupportActionBar().isShowing()){
            getSupportActionBar().show();
        }
        hideBottomNavigation(false);
    }
    private void setBAckArrowColor() {
        Drawable backArrow=getResources().getDrawable(R.drawable.ic_baseline_arrow_back_black_24dp);
        backArrow.setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    @Override
    public void onBackPressed() {
        // //swati  code
        iv_tool_bar_back.setOnClickListener(this);
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.container_layout);
        final String fm_name = fragment.getClass().getSimpleName();
        Log.e("main", ": " + fm_name);
        if (fm_name.contentEquals("Play_All_VideoFragment")) {
            if (isLand(getApplicationContext ())) {
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullscreen = true;
                playFullScreen (MainActivity.this, fullscreen);
            }
            else
            {
                if (getSupportFragmentManager ( ).getBackStackEntryCount ( ) > 0) {
                    sendRequest (ApiCode.GET_USER_DETAILS);
                    // Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.container_layout);
                    if (fragment instanceof Subscribed_Course_Details) {
                        Log.e ("child backup", "subcription ");
                        if (fragment.getChildFragmentManager ( ).getBackStackEntryCount ( ) > 1) {
                            Log.e ("child backup", "fragment.getTag()");
                            fragment.getChildFragmentManager ( ).popBackStackImmediate ( );
                        } else {
                            getSupportFragmentManager ( ).popBackStack ( );
                        }
                    }
                  /*  else if (fragment instanceof BookmarkFragment) {
                        if (fragment.getChildFragmentManager ( ).getBackStackEntryCount ( ) > 1) {
                            Log.e ("child backup", "bookmark");
                            fragment.getChildFragmentManager ( ).popBackStackImmediate ( );
                        } else {
                            getSupportFragmentManager ( ).popBackStack ( );
                        }
                    }

                   */
                    else if (fragment instanceof ResultFragment) {
                        getSupportFragmentManager ( ).popBackStack ( );
                        getSupportFragmentManager ( ).popBackStack ( );
                    } else {

                        getSupportFragmentManager ( ).popBackStack ( );
                    }

                } else {
                    //super.onBackPressed();
                    AlertDialog.Builder builder = new AlertDialog.Builder (this);
                    builder.setTitle ("Confirmation");
                    builder.setMessage ("Are you sure want to exit?");
                    builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss ( );
                            finishAffinity ( );


                        }
                    })
                            .setNegativeButton ("No", new DialogInterface.OnClickListener ( ) {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss ( );
                                }
                            });
                    final AlertDialog dialog = builder.create ( );
                    dialog.setOnShowListener (new DialogInterface.OnShowListener ( ) {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setTextColor (getResources ( ).getColor (R.color.primary_color));
                            dialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ( ).getColor (R.color.primary_color));
                        }
                    });
                    dialog.show ( );
                }

            }
        }
        //end swati  code


        else {

            //Sunil sir code
            if (getSupportFragmentManager ( ).getBackStackEntryCount ( ) > 0) {
                sendRequest (ApiCode.GET_USER_DETAILS);
                // Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.container_layout);
                if (fragment instanceof Subscribed_Course_Details) {
                    Log.e ("child backup", "subcription ");
                    if (fragment.getChildFragmentManager ( ).getBackStackEntryCount ( ) > 1) {
                        Log.e ("child backup", "fragment.getTag()");
                        fragment.getChildFragmentManager ( ).popBackStackImmediate ( );
                    } else {
                        getSupportFragmentManager ( ).popBackStack ( );
                    }
                }
               /* else if (fragment instanceof BookmarkFragment) {
                    if (fragment.getChildFragmentManager ( ).getBackStackEntryCount ( ) > 1) {
                        Log.e ("child backup", "bookmark");
                        fragment.getChildFragmentManager ( ).popBackStackImmediate ( );
                    } else {
                        getSupportFragmentManager ( ).popBackStack ( );
                    }
                }

                */
                else if (fragment instanceof ResultFragment) {
                    getSupportFragmentManager ( ).popBackStack ( );
                    getSupportFragmentManager ( ).popBackStack ( );
                } else {

                    getSupportFragmentManager ( ).popBackStack ( );
                }

            } else {
                //super.onBackPressed();
                AlertDialog.Builder builder = new AlertDialog.Builder (this);
                builder.setTitle ("Confirmation");
                builder.setMessage ("Are you sure want to exit?");
                builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss ( );
                        finishAffinity ( );


                    }
                })
                        .setNegativeButton ("No", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss ( );
                            }
                        });
                final AlertDialog dialog = builder.create ( );
                dialog.setOnShowListener (new DialogInterface.OnShowListener ( ) {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton (AlertDialog.BUTTON_NEGATIVE).setTextColor (getResources ( ).getColor (R.color.primary_color));
                        dialog.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ( ).getColor (R.color.primary_color));
                    }
                });
                dialog.show ( );
            }
        }

    }



    public void hideBottomNavigation(boolean value){
        if (value)
        bottomNavigationView.setVisibility(View.GONE);
        else
            bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void showLogoutAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to logout ?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent ( MainActivity.this, SplashActivity.class);
                session_management.logoutSession ();
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity (intent);
                //finish ();
            }
        }).show();
    }
    public static boolean isLand(@NonNull Context activity) {


        Resources resources = activity.getResources();
        assert resources != null;
        Configuration configuration = resources.getConfiguration();
        Assertions.checkState(configuration != null);
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        PAYMENT_ID=paymentData.getPaymentId();
        Log.e("pSuccess","success");
        sendRequest(ApiCode.PAYMENT_SUCCESS);
//
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e("paymentError ",s);
        CommonMethods.showSuccessToast(this,"Payment Failed");
    }

    @Override
    protected void onResume() {
        Log.e("resumeMain","resume");
        super.onResume();
        startDeviceInfotask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (deviceInfoTimer!=null){
            deviceInfoTimer.cancel();
        }
    }

}
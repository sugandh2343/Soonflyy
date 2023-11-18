package soonflyy.learning.hub.Teacher_Pannel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.INSTITUTE;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.PROFILE_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.utlis.AppConstant.FROM_HOME_SECTION;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common.Constant;
import soonflyy.learning.hub.Common_Package.IDSection.IDSectionFragment;
import soonflyy.learning.hub.Common_Package.IDSection.IDUserProfileFragment;
import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.Common_Package.NotificationFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.HomeFragment;
import soonflyy.learning.hub.Student_Pannel.MySubscriptionFragment;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.MyTeacherHomeFragment;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolLoginMainActivity;
import soonflyy.learning.hub.activity.SplashActivity;
import soonflyy.learning.hub.bottomsheet.CustomHelpCenterBottomSheet;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeacherMainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, CustomHelpCenterBottomSheet.ItemClickListener,
        VolleyResponseListener  {



    LinearLayout custom_toolbar;

    Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuImageView,iv_setting;

   // ImageView setting_icon;
    CircleImageView profile_image;
    TextView tv_user_name,tv_user_mobile,tv_toolbar_title;
    ImageView iv_tool_bar_back;
    LinearLayout lin_toolbar_title;
    RelativeLayout li_user_profile;
    LinearLayout li_subscription,lin_logout,li_home,lin_myteahcer,li_assignment,li_about_us,li_myTest,li_test_history,li_contact_us,li_terms_condition,li_help_center, li_chat_session;
    String from;

    SessionManagement session_management;
    public ActionBarDrawerToggle drawerToggle;
    BottomNavigationView  bottomNavigationView;

    RelativeLayout rel_search;
    TextView tv_actionbartitle;
    ImageView iv_bookmark,iv_notification;
    TextView tv_notification_count;
    ArrayList<Notification_model> notificationList = new ArrayList<>();

    String currentFragment;
    Timer deviceInfoTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView (R.layout.activity_teacher_main);

        /////// fragment home munu set krna....
        Fragment selectedFragment = new TeacherHomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                selectedFragment).commit();


        init();
        initControl();
        myObserve();
        sendRequest(ApiCode.GET_USER_DETAILS);

        //--------//
       // startDeviceInfotask();
        //------------//
        if (!CommonMethods.checkNotificationPermission(this)){
            CommonMethods.requestNotificationPermission(this,108);
        }
        View viewHeader=navigationView.getHeaderView(0);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation_bottom);


        setSupportActionBar(toolbar);

        //----------------
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        drawerToggle = new ActionBarDrawerToggle (this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount()>0){
                    TeacherMainActivity.super.onBackPressed();
                }else{
                    drawerLayout.addDrawerListener(drawerToggle);
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawerToggle.syncState();
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


       setProfile();
        if (ConnectivityReceiver.isConnected()){
            //sendRequest(ApiCode.GET_USER_DETAILS);
        }



    }

    private void startDeviceInfotask() {
        deviceInfoTimer=new Timer();
        deviceInfoTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (CommonMethods.checkInternetConnection(TeacherMainActivity.this)){
                    CommonMethods.callDeviceInfoTaskApi(TeacherMainActivity.this,session_management.getString(USER_ID),"1",deviceInfoTimer);
                }
            }
        },5000,10000);
    }
        private BottomNavigationView.OnNavigationItemSelectedListener navigation_bottom =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment selectedFragment = null;
                        Bundle bundle=new Bundle();
                        switch (item.getItemId()) {
                            case R.id.bottom_home:
                                clearBackStack();
                               // selectedFragment = new TeacherHomeFragment();
//                                Intent intent_home= new Intent(TeacherMainActivity.this,)
                                break;

                            case R.id.bottom_school:
                                ///toolbar.setVisibility(View.GONE);
                               // selectedFragment = new YourSchoolChoiceFragment();
                                session_management.setString(LOGIN_PAGE_TYPE,FROM_HOME_SECTION);
                                startActivity(new Intent(TeacherMainActivity.this, SchoolLoginMainActivity.class));

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
                            bundle.putString("user_type","teacher");
                            selectedFragment.setArguments(bundle);
                            SwitchFragment(selectedFragment);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
//                                    selectedFragment).commit();
                        }
                        return true;
                    }
                };


    private void checkStatus() {
        if(from != null){
            if(from.equalsIgnoreCase(HomeFragment.TAG)){
              //  showHomeFragment();
            }else if(from.equalsIgnoreCase(MySubscriptionFragment.TAG)){
                goToStudentAttenceFragment(null);
            }
        }else{
            //showHomeFragment();
        }
    }


    public void init() {
        rel_search = findViewById(R.id.rel_search);
        tv_actionbartitle =findViewById(R.id.tv_t_hometitle);
        iv_bookmark = findViewById(R.id.iv_bookmark);
        tv_notification_count =findViewById(R.id.tv_notifi_no);
        iv_notification=findViewById(R.id.iv_notification);

        iv_bookmark.setVisibility(View.GONE);
        tv_actionbartitle.setText("Let's provide knowledge");
        rel_search.setVisibility(GONE);
        iv_notification.setOnClickListener(this);

        lin_toolbar_title=findViewById(R.id.lin_toolbar_action);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        iv_tool_bar_back = findViewById(R.id.img_toolbar_back);

        custom_toolbar=findViewById(R.id.custom_tool_item);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuImageView = findViewById(R.id.menuImageView);
        //menuImageView.setVisibility(View.GONE);

      //  app_bar = findViewById(R.id.app_bar);
        toolbar=findViewById(R.id.toolbar);
      //  title=findViewById(R.id.page_title);
        tv_user_name=findViewById(R.id.assign_tv_name);
       tv_user_mobile=findViewById(R.id.tv_number);

        profile_image = findViewById(R.id.user_image);
        iv_setting=findViewById (R.id.iv_setting);
      // iv_setting.setOnClickListener (this);
        li_assignment=findViewById (R.id.li_attendance);
        li_subscription = findViewById(R.id.li_subscription);
        li_user_profile = findViewById(R.id.li_user_profile);
        li_about_us=findViewById (R.id.li_about_us);
        li_myTest=findViewById(R.id.li_my_test);
        li_test_history=findViewById(R.id.li_test_history);
        li_home = findViewById(R.id.li_home);
        lin_myteahcer=findViewById(R.id.li_my_teacher);

        li_contact_us= findViewById(R.id.li_contact_us);
        li_terms_condition= findViewById(R.id.li_terms_condition);
        li_help_center= findViewById(R.id.li_help_center);
        li_chat_session=findViewById(R.id.li_chat_session);
        lin_logout=findViewById(R.id.lin_logout);


        session_management=new SessionManagement(TeacherMainActivity.this);
      //  iv_filterTextView=findViewById (R.id.iv_filterTextView);
     //   filterTextView=findViewById (R.id.filterTextView);


    }


    public void initControl() {
        navigationView.setNavigationItemSelectedListener(this);
        menuImageView.setOnClickListener(this);
     //  user_image.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        li_subscription.setOnClickListener(this);
        li_assignment.setOnClickListener (this);
        li_about_us.setOnClickListener (this);
        li_test_history.setOnClickListener(this);
        li_myTest.setOnClickListener(this);
        li_home.setOnClickListener(this);
        lin_myteahcer.setOnClickListener(this);
        li_user_profile.setOnClickListener(this);
     //   filterTextView.setOnClickListener (this);
        li_contact_us.setOnClickListener (this);
        li_terms_condition.setOnClickListener (this);
        li_help_center.setOnClickListener (this);
                li_chat_session.setOnClickListener (this);
        lin_logout.setOnClickListener (this);
        iv_tool_bar_back.setOnClickListener(this);


    }


    public void myObserve() {

    }







    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_notification:
                gotoNotification();
                break;
            case R.id.menuImageView:
                checkDrawerOpen();
                break;
            case R.id.img_toolbar_back:
                onBackPressed();
                break;

            case R.id.iv_setting:
                goToProfileFragment();
                drawerLayout.closeDrawers();
                break;

            case R.id.li_home:
                drawerLayout.closeDrawers();
                clearBackStack();
//                Fragment fm = new TeacherHomeFragment ();
//                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,fm).commit();
                break;

            case R.id.li_my_teacher:
                //go for my teacher
                SwitchFragment(new MyTeacherHomeFragment());
                drawerLayout.closeDrawers();
                break;
            case R.id.li_subscription:
                //goToStudentAttenceFragment("course");
                SwitchFragment(new MyCourseFragment());
                drawerLayout.closeDrawers();
                break;
            case R.id.li_attendance:
                goToStudentAttenceFragment("assignment");
                drawerLayout.closeDrawers();
                break;
            case R.id.li_my_test:
                drawerLayout.closeDrawers();
                //SwitchFragment(new MyTestFragment());
                break;
            case R.id.li_test_history:
//                startActivity( new Intent(this, TestActivity.class));
//                drawerLayout.closeDrawers();
                break;
            case R.id.li_about_us:
                AboutusFragment afragment = new AboutusFragment ();
                Bundle args = new Bundle();
                args.putString("title","About Us");
                args.putString ("type","teacher");
                afragment.setArguments(args);
                SwitchFragment(afragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_contact_us:
                AboutusFragment abofragment = new AboutusFragment ();
                Bundle argss = new Bundle();
                argss.putString("title","contact");
                argss.putString ("type","teacher");
                abofragment.setArguments(argss);
                SwitchFragment(abofragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_terms_condition:
                AboutusFragment abfragment = new AboutusFragment ();
                Bundle arg = new Bundle();
                arg.putString("title","condition");
                arg.putString ("type","teacher");
                abfragment.setArguments(arg);
                SwitchFragment(abfragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.li_help_center:
                openHelpCenterBottomSheet();
                drawerLayout.closeDrawers();
                break;
            case R.id.li_chat_session:
                messageingFragment();
                drawerLayout.closeDrawers();
                break;
            case R.id.lin_logout:
                CommonMethods.showLogoutDialog(this,SIMPLEE_HOME_TUTOR);
            //   showLogoutAlert();
                //drawerLayout.closeDrawers();
                break;



        }

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
        bundle.putString("from",SIMPLEE_HOME_TUTOR);
        bundle.putParcelableArrayList("notifications",notificationList);
        fragment.setArguments(bundle);
       SwitchFragment(fragment);
    }

    private void messageingFragment() {
        StudentListFragment afragment = new StudentListFragment ();
        Bundle args = new Bundle();
        args.putString("type","teacher");//teacher
        args.putString("listType","chat");
        args.putString("user_id",session_management.getString(Constant.USER_ID));
        afragment.setArguments(args);
        SwitchFragment(afragment);
    }

    private void showStudentFragment() {
        StudentListFragment studentDetailsFragment = new StudentListFragment ();
        SwitchFragment(studentDetailsFragment);
    }

//

    private void goToStudentAttenceFragment(String type) {
        StudentAttendencdeFragment mycourse = new StudentAttendencdeFragment();
        Bundle args = new Bundle();
        args.putString("from",SIMPLEE_HOME_TUTOR);
//        args.putString("goto",type);
        mycourse.setArguments(args);
        SwitchFragment (mycourse);
    }


    private void goToProfileFragment() {
       // drawerLayout.closeDrawers();
       // TeacherProfileFragment profileFragment = new TeacherProfileFragment();
        T_Profile_Fragment profileFragment = new T_Profile_Fragment();
        SwitchFragment (profileFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
      /*  switch (item.getItemId()){
            case R.id.t_home:
                drawerLayout.closeDrawers();
                Fragment fm = new TeacherHomeFragment ();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,fm).commit();
                break;
            case R.id.t_my_course:
                goToCourseFragment("course");
                drawerLayout.closeDrawers();
                break;
            case R.id.t_assignment:
                goToCourseFragment("assignment");
                drawerLayout.closeDrawers();
                break;
            case R.id.t_my_test:
                drawerLayout.closeDrawers();
                SwitchFragment(new MyTestFragment());
                break;
            case R.id.t_test_history:
                startActivity( new Intent(this, TestActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.t_about_us:
                AboutusFragment afragment = new AboutusFragment ();
                Bundle args = new Bundle();
                args.putString("title","About Us");
                args.putString ("type","teacher");
                afragment.setArguments(args);
                SwitchFragment(afragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.t_contact_us:
                AboutusFragment abofragment = new AboutusFragment ();
                Bundle argss = new Bundle();
                argss.putString("title","contact");
                argss.putString ("type","teacher");
                abofragment.setArguments(argss);
                SwitchFragment(abofragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.t_term_condition:
                AboutusFragment abfragment = new AboutusFragment ();
                Bundle arg = new Bundle();
                arg.putString("title","condition");
                arg.putString ("type","teacher");
                abfragment.setArguments(arg);
                SwitchFragment(abfragment);
                drawerLayout.closeDrawers();
                break;
            case R.id.t_help_center:
                openHelpCenterBottomSheet();
                drawerLayout.closeDrawers();
                break;
            case R.id.t_chat_session:
                messageingFragment();
                drawerLayout.closeDrawers();
                break;
            case R.id.t_logout:
                Intent intent=new Intent ( TeacherMainActivity.this, SplashActivity.class);
                session_management.logoutSession ();
                startActivity (intent);
                finish ();
                //drawerLayout.closeDrawers();
                break;

        }

       */
        return true;
    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
       // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void checkDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    @Override
    public void onBackPressed() {
        Log.e("UIDYGCHGC",""+getSupportFragmentManager().getBackStackEntryCount());


    if (getSupportFragmentManager().getBackStackEntryCount()>0){

            sendRequest(ApiCode.GET_USER_DETAILS);

               getSupportFragmentManager().popBackStack();
//

        }else {
            //super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMainActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finishAffinity();


                }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_color));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_color));
                }
            });
            dialog.show();
        }

    }

    private void openHelpCenterBottomSheet() {
        CustomHelpCenterBottomSheet bottomSheet = new CustomHelpCenterBottomSheet();
        bottomSheet.show(getSupportFragmentManager(),CustomHelpCenterBottomSheet.TAG);
    }



    public  void setChildActionBar(String title){
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        drawerToggle.setDrawerIndicatorEnabled(false);
        ///// show own icon replce drawble icon start////
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_img);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_sort_24);
        drawerToggle.syncState();
        setBAckArrowColor();

    }
    public  void setTeacherActionBar(String title,boolean show){
       // getSupportActionBar().setTitle(title);
        tv_toolbar_title.setText(title);
        if (show) {
            lin_toolbar_title.setVisibility(View.GONE);
            custom_toolbar.setVisibility(View.VISIBLE);
       drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        } else {
            custom_toolbar.setVisibility(View.GONE);
          //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(false);
            lin_toolbar_title.setVisibility(View.VISIBLE);
        }
       // setBAckArrowColor();
        if (!getSupportActionBar().isShowing()){
            getSupportActionBar().show();
        }
    }
    private void setBAckArrowColor() {
        Drawable backArrow=getResources().getDrawable(R.drawable.ic_baseline_arrow_back_black_24dp);
        backArrow.setColorFilter(ContextCompat.getColor(this,R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }
//
    public void clearBackStack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onItemClick(String item, View view) {
        //click for open help center
        Log.e("string: ",item);
        Log.e("view id", String.valueOf(view.getId()));
        new CommonMethods().callUs(this);

    }

    public void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        switch (request){
            case ApiCode.GET_USER_DETAILS:
                params.put("user_id",new SessionManagement(TeacherMainActivity.this).getString(Constant.USER_ID));
                params.put("type","1");
                Log.e("UIDYGCHGC",new SessionManagement(TeacherMainActivity.this).getString(Constant.USER_ID));
                reference.child(new SessionManagement(TeacherMainActivity.this).getString(Constant.USER_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String image=snapshot.child("image").getValue(String.class);
                        if(image!=null&& !image.equals("")){
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(profile_image);
                        }

                        String fullName = snapshot.child("name").getValue(String.class);
                        String mobileNo =snapshot.child("mobile").getValue(String.class);
                        tv_user_name.setText(fullName);
                        tv_user_mobile.setText("+91"+mobileNo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                callApi(ApiCode.GET_USER_DETAILS,params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, this);
        switch (request){
            case ApiCode.GET_USER_DETAILS:
                service.postDataVolley(ApiCode.GET_USER_DETAILS
                        ,BaseUrl.URL_GET_USER_DETAILS,params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType) {
            case ApiCode.GET_USER_DETAILS:
                Log.e(" profile", response);

                setDataToView(response);
                break;
        }
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
                Picasso.get().load(imageUrl).placeholder(R.drawable.profile).into(profile_image);
               String fullName = job.getString("first_name") + "" + job.getString("last_name");
               String mobileNo = job.getString("mobile");
               tv_user_name.setText(fullName);
               tv_user_mobile.setText("+91"+mobileNo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setProfile() {
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+session_management.getString(PROFILE_IMAGE))
                .placeholder(R.drawable.profile).into(profile_image);
        tv_user_name.setText(session_management.getString(INSTITUTE));
        tv_user_mobile.setText("+91-"+session_management.getString(MOBILE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDeviceInfotask();

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
              //  Intent intent=new Intent ( TeacherMainActivity.this, SplashActivity.class);
                session_management.logoutSession ();
//                startActivity (intent);
//                finish ();
            }
        }).show();
    }

    private void showAccountStatusAlertMessage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Server Message")
                .setMessage("Your account is inactivated by admin. You haven't access to view contents of this app.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent ( TeacherMainActivity.this, SplashActivity.class);
                        session_management.logoutSession ();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        finish ();
                    }
                }).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (deviceInfoTimer!=null) {
            deviceInfoTimer.cancel();
        }
    }
}
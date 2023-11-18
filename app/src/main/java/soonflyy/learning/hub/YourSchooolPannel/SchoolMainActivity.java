package soonflyy.learning.hub.YourSchooolPannel;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.ITUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGGED_IN_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common_Package.Play_All_VideoFragment.fullscreen;
import static soonflyy.learning.hub.Common_Package.Play_All_VideoFragment.playFullScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.IDSection.IDSectionFragment;
import soonflyy.learning.hub.Common_Package.IDSection.IDUserProfileFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment.SchoolCoachingHomeFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.SchoolCoachingTutorHomeFragment;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Fragment.SchoolIndependentTutorHomeFragment;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Fragment.SchoolStudentHomeFragment;
import soonflyy.learning.hub.utlis.SessionManagement;

import java.util.Timer;
import java.util.TimerTask;


public class SchoolMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    ImageView arrow_back_img;
    TextView tv_title;
    View actionbar_view;
    String login_type;
    SessionManagement sessionManagement;
    Timer deviceInfoTimer;

    public  static  String SCHOOL_ID_SECTION_USER_TYPE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_main);
        sessionManagement =new SessionManagement(this);
        findView();
        getIntentData();
        inItControl();
       // startDeviceInfotask();
    }

    private void getIntentData() {
        login_type=getIntent().getStringExtra("login_type");
       // if (login_type!=null){
        Log.e("type",""+login_type);
         setHomePage();
      //  }
    }

    private void startDeviceInfotask() {
        deviceInfoTimer=new Timer();
        deviceInfoTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (CommonMethods.checkInternetConnection(SchoolMainActivity.this)){
                    String userType="";
                    String id="";
                    switch (login_type){
                        case SCHOOL_COACHING:
                            id=sessionManagement.getString(SCHOOL_ID);
                            userType="2";
                            break;
                        case SCHOOL_TUTOR:
                            id=sessionManagement.getString(SCHOOL_TEACHER_ID);
                            userType="5";
                            break;
                        case SCHOOL_STUDENT:
                            id=sessionManagement.getString(SCHOOL_LOGGED_IN_STUDENT_ID);//SCHOOL_STUDENT_ID
                            userType="4";
                            break;
                        case INDEPENDENT_TUTOR:
                            id=sessionManagement.getString(SCHOOL_IT_ID);
                            userType="3";
                            break;
                    }
                    CommonMethods.callDeviceInfoTaskApi(SchoolMainActivity.this,id,userType,deviceInfoTimer);
                }
            }
        },5000,10000);
    }

   private void setHomePage(){
       if (login_type.equals("school")){
           //manage home for school
           getSupportFragmentManager().beginTransaction().add(R.id.school_container_layout,
                   new SchoolCoachingHomeFragment()).commit();
       }else if (login_type.equals("s_tutor")){
           getSupportFragmentManager().beginTransaction().add(R.id.school_container_layout,
                   new SchoolCoachingTutorHomeFragment()).commit();
//               getSupportFragmentManager().beginTransaction().add(R.id.school_container_layout,
//                       new SchoolStudentHomeFragment()).commit();

       }else if (login_type.equals("i_tutor")){
           getSupportFragmentManager().beginTransaction().add(R.id.school_container_layout,
                   new SchoolIndependentTutorHomeFragment()).commit();
       }else if (login_type.equals(SCHOOL_STUDENT)){
           getSupportFragmentManager().beginTransaction().add(R.id.school_container_layout,
                   new SchoolStudentHomeFragment()).commit();
       }
    }

    private void inItControl() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        arrow_back_img.setOnClickListener(this);
    }

    private void findView() {
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        tv_title = findViewById(R.id.tv_title);
        arrow_back_img = findViewById(R.id.arrow_back_img);
        actionbar_view=findViewById(R.id.include);
    }

    public void switchFragmentOnSchoolMainActivity(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.school_container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setActionBarTitle(String title){
        actionbar_view.setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }
    public void showHideHomeActionBar(boolean b){
        if (b)
            actionbar_view.setVisibility(View.VISIBLE);
        else
            actionbar_view.setVisibility(View.GONE);
    }
    public void clearBackstack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment selectedFragment = null;
//        Bundle bundle=new Bundle();
//        Log.e("namma", "onNavigationItemSelected: "+ item.getItemId());
//
        switch (item.getItemId()) {
            case R.id.bottom_home:
                clearBackstack();
                break;


            case R.id.bottom_idsection:
                String idSection=getIDSectionValue();

                setIdSectionUserId();
                Fragment fragment=null;
                Bundle bundle=new Bundle();
                Log.e("schoolIdvalue",""+idSection);

                if (idSection==null|| TextUtils.isEmpty(idSection) ||idSection.equals("0")) {
                    fragment = new IDSectionFragment();
                    bundle.putString("type","add");
                    sessionManagement.setString(SCHOOL_ID_SECTION,"0");

                }else {
                    sessionManagement.setString(SCHOOL_ID_SECTION,"1");
                    fragment = new IDUserProfileFragment();
                }
                if (fragment!=null){
                    clearBackstack();
                    bundle.putString("user_type",SCHOOL_COACHING);
                    SCHOOL_ID_SECTION_USER_TYPE=login_type;
                    //setSchoolIdSectionUserType();
                    fragment.setArguments(bundle);
                    switchFragmentOnSchoolMainActivity(fragment);
                }

                break;


        }
//        if (selectedFragment != null) {
//            bundle.putString("user_type","student");
//            selectedFragment.setArguments(bundle);
//            SwitchFragment(selectedFragment);
////                        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,
////                                selectedFragment).commit();
//        }
        return true;
    }



    private void setIdSectionUserId() {
        if (login_type.equals("school")){
            //for school coaching
            sessionManagement.setString(ID_SECTION_USER_ID,
                    sessionManagement.getString(SCHOOL_ID));

        }else if (login_type.equals("i_tutor")){
            //for independent tutor
            sessionManagement.setString(ID_SECTION_USER_ID,
                    sessionManagement.getString(SCHOOL_IT_ID));

        }else if (login_type.equals("s_tutor")){
            //for school tutor
            sessionManagement.setString(ID_SECTION_USER_ID,
                    sessionManagement.getString(SCHOOL_TEACHER_ID));

        }else {
            //for student
            sessionManagement.setString(ID_SECTION_USER_ID,
                    sessionManagement.getString(SCHOOL_LOGGED_IN_STUDENT_ID));//SCHOOL_STUDENT_ID
        }
    }
    public void setIdSectionStatus(String value){
        sessionManagement.setString(SCHOOL_ID_SECTION,value);
        switch (login_type){
            case SCHOOL_COACHING:
                sessionManagement.setString(SCHOOL_COACHING_ID_SECTION,value);
                break;
            case SCHOOL_TUTOR:
                sessionManagement.setString(SCHOOL_TUTOR_ID_SECTION,value);
                break;
            case INDEPENDENT_TUTOR:
                sessionManagement.setString(ITUTOR_ID_SECTION,value);
                break;
            case SCHOOL_STUDENT:
                sessionManagement.setString(SCHOOL_STUDENT_ID_SECTION,value);
                break;
        }
    }
    public String getIDSectionValue(){
        if (login_type.equals(SCHOOL_COACHING)){
            return sessionManagement.getString(SCHOOL_COACHING_ID_SECTION);
        }
        if (login_type.equals(SCHOOL_TUTOR)){
            return sessionManagement.getString(SCHOOL_TUTOR_ID_SECTION);
        }
        if (login_type.equals(INDEPENDENT_TUTOR)){
            return sessionManagement.getString(ITUTOR_ID_SECTION);
        }
        if (login_type.equals(SCHOOL_STUDENT)){
            return sessionManagement.getString(SCHOOL_STUDENT_ID_SECTION);
        }
        return "";
    }


    /////----------------my code-----------------//

  /*  @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    //finishAffinity();
                    finish();


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

   */

        //---------------------------------------//

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        arrow_back_img.setOnClickListener(this);
        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.school_container_layout);

        final String fm_name = fr.getClass().getSimpleName();
        Log.e("backstack: ", ": " + fm_name);


//my code
        if (fm_name.contentEquals("Play_All_VideoFragment")) {
            if (isLand(getApplicationContext ())) {
                setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullscreen = true;
                playFullScreen (SchoolMainActivity.this, fullscreen);
            }
            else {
                if (getSupportFragmentManager ( ).getBackStackEntryCount ( ) > 0) {
                    getSupportFragmentManager ( ).popBackStack ( );
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder (this);
                    builder.setTitle ("Confirmation");
                    builder.setMessage ("Are you sure want to exit?");
                    builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener ( ) {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss ( );
                            //finishAffinity();
                            finish ( );


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
        //end my
//sunil sir
        else {
            if (getSupportFragmentManager ( ).getBackStackEntryCount ( ) > 0) {
                getSupportFragmentManager ( ).popBackStack ( );
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder (this);
                builder.setTitle ("Confirmation");
                builder.setMessage ("Are you sure want to exit?");
                builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss ( );
                        //finishAffinity();
                        finish ( );


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


    public static boolean isLand(@NonNull Context activity) {
        Resources resources = activity.getResources();
        assert resources != null;
        Configuration configuration = resources.getConfiguration();
        Assertions.checkState(configuration != null);
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_back_img:
                onBackPressed();
                break;

    }
}

public ImageView getToolbarBackBtn(){
        return arrow_back_img;
}

    public void showHideBottomNavigation(boolean status){

        if (status)
            bottomNavigationView.setVisibility(View.VISIBLE);
        else
            bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
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
package soonflyy.learning.hub.YourSchooolPannel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.R;

public class SchoolLoginMainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lnr_top, lin_school, lin_tutor,lin_bottom_ss;
    View view_tutor, view_school;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login_main);
        initView();
        //Fragment selectedFragment = new SchoolLoginFragment();
        Fragment selectedFragment = new YourSchoolChoiceFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                selectedFragment).commit();
//        makeBottom_gone("gone");

    }

    public void initView() {
        lin_bottom_ss=findViewById (R.id.lin_bottom_ss);

        view_tutor = findViewById(R.id.view_tutor);
        view_school = findViewById(R.id.view_school);
        lnr_top = findViewById(R.id.lnr_top);
        lin_school = findViewById(R.id.lin_school);
        lin_tutor = findViewById(R.id.lin_tutor);

        lnr_top.setOnClickListener(this);
        lin_school.setOnClickListener(this);
        lin_tutor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Fragment selectedFragment = null;
          Bundle bundle=new Bundle();
//        Intent i = null;
        switch (id) {
            case R.id.lin_school:
                clearSchoolBackstack();
                selectedFragment = new SchoolLoginFragment();
                bundle.putString("login_type","school");
                selectedFragment.setArguments(bundle);
//                view_tutor.setVisibility(View.GONE);
//                view_school.setVisibility(View.VISIBLE);
                makeBottom_gone("note_gone","school");
                break;
            case R.id.lin_tutor:
                makeBottom_gone("note_gone","s_tutor");
                clearSchoolBackstack();
                selectedFragment = new SchoolStudentLoginFragment();
                bundle.putString("login_type","s_tutor");
                selectedFragment.setArguments(bundle);
//                view_tutor.setVisibility(View.VISIBLE);
//                view_school.setVisibility(View.GONE);
                break;

        }
//        if (i != null) {
//            startActivity(i);
//        }

//        selectedFragment.setArguments(bundle);
        SwitchFragment(selectedFragment);
    }

    private void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void makeBottom_gone(String value,String loginType)
    {

      if(value.equals ("gone"))
      {
          lin_bottom_ss.setVisibility (View.GONE);
      }
        else
        {
            if (loginType.equals("s_tutor")){
                view_tutor.setVisibility(View.VISIBLE);
                view_school.setVisibility(View.GONE);
            }else if (loginType.equals("school")){
                view_tutor.setVisibility(View.GONE);
                view_school.setVisibility(View.VISIBLE);
            }
            lin_bottom_ss.setVisibility (View.VISIBLE);
        }
    }
    public void clearSchoolBackstack(){

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


//   @Override
//        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (resultCode== RESULT_OK && requestCode==1234){
//                if (data !=null){
//
//                    String type=data.getStringExtra("regType");
//                    Log.e("reg",""+type);
//                    if (type.equals("school")){
//                        clearSchoolBackstack();
//                        Fragment selectedFragment = new SchoolLoginFragment();
//                        Bundle bundle=new Bundle();
//                       bundle.putString("login_type","school");
//                        selectedFragment.setArguments(bundle);
//                        makeBottom_gone("note_gone","school");
//                        SwitchFragment(selectedFragment);
//
//                    }else if (type.equals("s_tutor")){
//                        makeBottom_gone("note_gone","s_tutor");
//                        clearSchoolBackstack();
//                       Fragment  selectedFragment = new SchoolStudentLoginFragment();
//                       Bundle bundle=new Bundle();
//                        bundle.putString("login_type","s_tutor");
//                        selectedFragment.setArguments(bundle);
//                        SwitchFragment(selectedFragment);
//                    }
//
//                }
//            }
//        }
//


    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // your operation....
                        Log.e("result","get");
                        if (data!=null){
                            String type=data.getStringExtra("regType");
                    Log.e("reg",""+type);

                            if (type.equals("school")){
                        clearSchoolBackstack();
                        Fragment selectedFragment = new SchoolLoginFragment();
                        Bundle bundle=new Bundle();
                       bundle.putString("login_type","school");
                        selectedFragment.setArguments(bundle);
                        makeBottom_gone("note_gone","school");
                        SwitchFragment(selectedFragment);

                    }else if (type.equals("s_tutor")){
                        makeBottom_gone("note_gone","s_tutor");
                        clearSchoolBackstack();
                       Fragment  selectedFragment = new SchoolStudentLoginFragment();
                       Bundle bundle=new Bundle();
                        bundle.putString("login_type","s_tutor");
                        selectedFragment.setArguments(bundle);
                        SwitchFragment(selectedFragment);
                    }
                        }
                    }
                }
            });


//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount()>0){
//            getSupportFragmentManager().popBackStack();
//        }else {
//            super.onBackPressed();
//        }
//    }
}


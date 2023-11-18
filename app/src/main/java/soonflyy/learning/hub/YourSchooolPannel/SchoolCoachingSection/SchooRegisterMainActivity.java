package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolRegisterFragment;
import soonflyy.learning.hub.YourSchooolPannel.SchoolStudentSignupFragment;

public class SchooRegisterMainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lnr_top, lin_school, lin_tutor,lin_bottom_ss;
    View view_tutor, view_school;
    String register_type,selected_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoo_register_main);
        initView();
        getIntentData();

    }

    private void getIntentData() {

        register_type=getIntent().getStringExtra("register_type");
        if (register_type.equals("school") || register_type.equals("s_tutor")){
            selected_type=getIntent().getStringExtra("selected_tab");
            makeBottom_gone("visible");
            if (register_type.equals("s_tutor")){
                Fragment selectedFragment = new SchoolStudentSignupFragment();
                Bundle bundle=new Bundle();
                bundle.putString("register_type","s_tutor");
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                        selectedFragment).commit();
                view_tutor.setVisibility(View.VISIBLE);
                view_school.setVisibility(View.GONE);
            }else{
                Fragment selectedFragment = new SchoolRegisterFragment();
                Bundle bundle=new Bundle();
                bundle.putString("register_type","school");
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                        selectedFragment).commit();
                view_tutor.setVisibility(View.GONE);
                view_school.setVisibility(View.VISIBLE);
            }

        }else if (register_type.equals("i_tutor")){
            makeBottom_gone("gone");
            Fragment selectedFragment = new SchoolRegisterFragment();
            Bundle bundle=new Bundle();
            bundle.putString("register_type","i_tutor");
            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                    selectedFragment).commit();
        }else if (register_type.equals("student")){
            makeBottom_gone("gone");
            Fragment selectedFragment = new SchoolStudentSignupFragment();
            Bundle bundle=new Bundle();
            bundle.putString("register_type","student");
            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container_layout,
                    selectedFragment).commit();
        }
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
        Intent i = null;
        switch (id) {
//
            case R.id.lin_school:
                clearSchoolBackstack();
                selectedFragment = new SchoolRegisterFragment();
                bundle.putString("register_type","school");
                selectedFragment.setArguments(bundle);
                view_tutor.setVisibility(View.GONE);
                view_school.setVisibility(View.VISIBLE);
                selected_type="school";
                register_type="school";
                break;
            case R.id.lin_tutor:
                clearSchoolBackstack();
                selectedFragment = new SchoolStudentSignupFragment();
                bundle.putString("register_type","s_tutor");
                selectedFragment.setArguments(bundle);
                view_tutor.setVisibility(View.VISIBLE);
                view_school.setVisibility(View.GONE);
                selected_type="tutor";
                register_type="tutor";
                break;
//
        }
        if (i != null) {
            startActivity(i);
        }

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
    public void makeBottom_gone(String value)
    {

        if(value.equals ("gone"))
        {
            lin_bottom_ss.setVisibility (View.GONE);
        }
        else
        {
            lin_bottom_ss.setVisibility (View.VISIBLE);
        }
    }
    public void clearSchoolBackstack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }




}
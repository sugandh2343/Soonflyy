package soonflyy.learning.hub.activity;

import static soonflyy.learning.hub.utlis.AppConstant.FROM_CHOICE_PAGE;
import static soonflyy.learning.hub.utlis.AppConstant.SCHOOL_SECTION_TYPE;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolLoginMainActivity;
import soonflyy.learning.hub.base.BaseActivity;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.SessionManagement;

public class ChoiceActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout lin_tutor,lin_student;
    Button btn_teacher,btn_student;
    LinearLayout lin_school;
    SessionManagement sessionManagement;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        sessionManagement=new SessionManagement(this);
        init();
        initControl();
        myObserve();
    }

    public void init(){
        lin_student= findViewById(R.id.lin_student);
        lin_tutor = findViewById(R.id.lin_tutor);
        btn_teacher= findViewById(R.id.btn_teacher);
        btn_student= findViewById(R.id.btn_student);
        lin_school=findViewById(R.id.lin_school);


    }

    @Override
    public void initControl() {
        btn_teacher.setOnClickListener(this);
        btn_student.setOnClickListener(this);
        lin_school.setOnClickListener(this);

    }

    @Override
    public void myObserve() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_student:
                btn_student.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.primary_color)));
              //  CommonMethods.showSuccessToast(this,"You have select student");
                goToLoinScreen("Student");
                break;
            case R.id.btn_teacher:
                btn_teacher.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.primary_color)));
                goToLoinScreen("Teacher");
               // CommonMethods.showSuccessToast(this,"You have select teacher");
                break;
            case R.id.lin_school:
                sessionManagement.setString(SCHOOL_SECTION_TYPE,FROM_CHOICE_PAGE);
                startActivity(new Intent(ChoiceActivity.this, SchoolLoginMainActivity.class));
                break;
        }
    }

    private void goToLoinScreen(String user_type) {
        Intent intent = new Intent(ChoiceActivity.this, LoginActivity.class);
        intent.putExtra(AppConstant.USER_TYPE,user_type);
        startActivity(intent);
        finish();

    }
}
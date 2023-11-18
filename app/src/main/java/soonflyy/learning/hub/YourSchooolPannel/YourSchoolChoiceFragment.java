package soonflyy.learning.hub.YourSchooolPannel;

import static soonflyy.learning.hub.utlis.AppConstant.FROM_RESET_PASSWORD;
import static soonflyy.learning.hub.utlis.AppConstant.LOGIN_PAGE_TYPE;
import static soonflyy.learning.hub.utlis.AppConstant.SCHOOL_SECTION_TYPE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.SessionManagement;


public class YourSchoolChoiceFragment extends Fragment implements View.OnClickListener {
RelativeLayout rel_student,rel_school_coaching,rel_maintutor;
    public YourSchoolChoiceFragment() {
        // Required empty public constructor
    }
    public static YourSchoolChoiceFragment newInstance(String param1, String param2) {
        YourSchoolChoiceFragment fragment = new YourSchoolChoiceFragment();

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
        View view= inflater.inflate(R.layout.fragment_your_choice_school, container, false);
        initView(view);
        setLoginPage();
        return  view;
    }

    private void setLoginPage() {

        SessionManagement sessionManagement=new SessionManagement(getActivity());
        String fromPage=sessionManagement.getString(LOGIN_PAGE_TYPE);
        if (fromPage.equalsIgnoreCase(FROM_RESET_PASSWORD)){
            String userType=sessionManagement.getString(SCHOOL_SECTION_TYPE);
            if (userType.equalsIgnoreCase("school")){
                //from school page
                setSchoolPage( );
            }else{
                //from independent tutor reset page
                setItutorPage();
            }
        }



    }

    private void initView(View view) {
        rel_student= view.findViewById(R.id.rel_student);
        rel_school_coaching= view.findViewById(R.id.rel_school_coaching);
        rel_maintutor= view.findViewById(R.id.rel_maintutor);
        rel_student.setOnClickListener(this);
        rel_school_coaching.setOnClickListener(this);
        rel_maintutor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Fragment selectedFragment = null;

        Bundle bundle=new Bundle();

        switch (id)
        {
            case R.id.rel_student:
             //   selectedFragment = new SchoolStudentHomeFragment();
                selectedFragment = new SchoolStudentLoginFragment();
                bundle.putString("login_type","student");
                selectedFragment.setArguments(bundle);
                SwitchFragment(selectedFragment);
                ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("gone","student");
//                Intent   i_student= new Intent(getActivity(), SchoolLoginMainActivity.class);
//                startActivity(i_student);
////;

                break;
            case R.id.rel_school_coaching:
                setSchoolPage();
               // ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("not_visible");
             //   selectedFragment = new School();
//              Intent   i_login= new Intent(getActivity(), SchoolLoginMainActivity.class);
//                      startActivity(i_login);
//;
                break;
            case R.id.rel_maintutor:
                //selectedFragment = new SchoolTutorHomeFragment();
                setItutorPage();
               //((SchoolLoginMainActivity)getActivity()).makeBottom_gone("gone");
                break;

        }

//        selectedFragment.setArguments(bundle);

    }

    private void setItutorPage() {
      Fragment  selectedFragment = new SchoolLoginFragment();
      Bundle bundle=new Bundle();
        bundle.putString("login_type","i_tutor");
        selectedFragment.setArguments(bundle);
        SwitchFragment(selectedFragment);
    }

    private void setSchoolPage() {
       Fragment selectedFragment = new SchoolLoginFragment();
        Bundle bundle=new Bundle();
        bundle.putString("login_type","school");
        selectedFragment.setArguments(bundle);
        SwitchFragment(selectedFragment);
    }

    private void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((SchoolLoginMainActivity)getActivity()).makeBottom_gone("gone","choice");
    }
}
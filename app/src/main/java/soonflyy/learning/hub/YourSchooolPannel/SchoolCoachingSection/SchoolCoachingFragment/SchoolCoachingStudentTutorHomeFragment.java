 package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingFragment;

 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.FrameLayout;
 import android.widget.LinearLayout;
 import android.widget.RelativeLayout;

 import androidx.fragment.app.Fragment;
 import androidx.fragment.app.FragmentManager;
 import androidx.fragment.app.FragmentTransaction;

 import soonflyy.learning.hub.R;
 import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment.Scl_SelectChpNoticeFragment;

 public class SchoolCoachingStudentTutorHomeFragment extends Fragment implements View.OnClickListener {
    LinearLayout lnr_top, lin_school, lin_tutor;
    View view_tutor, view_school;
    FrameLayout homeies;
    RelativeLayout rel_bordersss;
    public SchoolCoachingStudentTutorHomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SchoolCoachingStudentTutorHomeFragment newInstance(String param1, String param2) {
        SchoolCoachingStudentTutorHomeFragment fragment = new SchoolCoachingStudentTutorHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_school_coaching_student_tutor_home, container, false);
        rel_bordersss=view.findViewById (R.id.rel_bordersss);
        initView(view);
       // makeRelGon("visible");

        Fragment selectedFragment = new Scl_SelectChpNoticeFragment();
        getFragmentManager().beginTransaction().add(R.id.homeies,
                selectedFragment).commit();


        return view;
    }
     private void initView(View view) {
         homeies=view.findViewById (R.id.homeies);
         view_tutor = view.findViewById(R.id.view_tutor);
         view_school = view.findViewById(R.id.view_school);
         lnr_top = view.findViewById(R.id.lnr_top);
         lin_school = view.findViewById(R.id.lin_school);
         lin_tutor = view.findViewById(R.id.lin_tutor);
         lnr_top.setOnClickListener(this);
         lin_school.setOnClickListener(this);
         lin_tutor.setOnClickListener(this);
     }

     @Override
     public void onClick(View v) {

         int id = v.getId();
         Fragment selectedFragment = null;

         switch (id) {
             case R.id.lin_school:
                 selectedFragment = new Scl_SelectChpNoticeFragment();
                 view_tutor.setVisibility(View.GONE);
                 view_school.setVisibility(View.VISIBLE);
                 SwitchFragment(selectedFragment);
                 break;
             case R.id.lin_tutor:
                 selectedFragment = new SchoolCoachingTutorNewProfileFragment () ;
                 view_tutor.setVisibility(View.VISIBLE);
                 view_school.setVisibility(View.GONE);
                 SwitchFragment(selectedFragment);
                 break;

         }


     }

     private void SwitchFragment(Fragment fragment) {
         FragmentManager fragmentManager = getFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
         //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
         // fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
         fragmentTransaction.replace(R.id.homeies, fragment);
         fragmentTransaction.addToBackStack(null);
         fragmentTransaction.commit();
     }
//     public void makeRelGon(String val)
//     {
//         if(val.equals ("gone"))
//         {
//             rel_bordersss.setVisibility (GONE);
//         }
//         else
//         {
//             rel_bordersss.setVisibility (VISIBLE);
//         }
//     }
 }


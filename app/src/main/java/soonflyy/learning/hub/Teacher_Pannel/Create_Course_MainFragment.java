package soonflyy.learning.hub.Teacher_Pannel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.R;


public class Create_Course_MainFragment extends Fragment implements View.OnClickListener {
FrameLayout container_layout;
    TextView tv_tracker1,tv_tracker2,tv_tracker3;
    LinearLayout lin_1,lin_2,lin_3;


    public Create_Course_MainFragment() {
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
        View view= inflater.inflate(R.layout.fragment_create__course__main, container, false);
        init(view);
        if (getChildFragmentManager().getBackStackEntryCount()==0){
            showCourseFragment();
        }
        return view;
    }

    private void init(View view) {
        tv_tracker1=view.findViewById(R.id.tv_no_1);
        tv_tracker2=view.findViewById(R.id.tv_tracker2);
        tv_tracker3=view.findViewById(R.id.tv_tracker3);
        lin_1=view.findViewById(R.id.lin_1);
        lin_2=view.findViewById(R.id.lin_2);
        lin_3=view.findViewById(R.id.lin_3);
    }


    public void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.course_container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

        }
}

    private void showCourseFragment() {

        CreateCourseFragment fragment=new CreateCourseFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type","add");
        fragment.setArguments(bundle);
        switchFragment(fragment);
    }
    public void changeTrackerColor(int value){
        Log.e("trackercolor", "changeTrackerColor: "+value );
        switch (value){

            case 1:
                changeCloreFirst();
                break;
            case 2:
                changeColorSEcond();
                break;
            case 3:
                chageColorThird();
                break;
        }

    }

    private void chageColorThird() {
        tv_tracker3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.graient2));
        tv_tracker3.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        tv_tracker2.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.tracker_bg_circle));
        //tv_tracker2.setTextColor(ContextCompat.getColor(getContext(),R.color.graient2));
        tv_tracker1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.tracker_bg_circle));
       // tv_tracker1.setTextColor(ContextCompat.getColor(getContext(),R.color.graient2));



    }

    private void changeColorSEcond() {
        tv_tracker2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.graient2));
        tv_tracker2.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
        tv_tracker1.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.tracker_bg_circle));
        //tv_tracker1.setTextColor(ContextCompat.getColor(getContext(),R.color.graient2));
    }

    private void changeCloreFirst() {
        tv_tracker1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.graient2));
        tv_tracker1.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar("Create Course",false);
    }
}
package soonflyy.learning.hub.Common_Package.Common_Package.IDSection;

import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID_SECTION;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.SessionManagement;

public class IDSectionFragment extends Fragment implements View.OnClickListener {
    TextView tv_basic, tv_other, tv_activity, tv_achievement;
    LinearLayout lin_basicdetail, lin_achievement, lin_other, lin_activity;
    ImageView iv_basic, iv_activity, iv_achievement, iv_other;
    String user_type,type;
    SessionManagement management;
    //--------new modification------//
    String from;
    //-------------------//
  //  boolean  has_id_section=false;


    public static IDSectionFragment newInstance(String param1, String param2) {
        IDSectionFragment fragment = new IDSectionFragment();

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
        View view = inflater.inflate(R.layout.fragment_i_d_section, container, false);
        management=new SessionManagement(getActivity());
//        if (management.getString(ID_SECTION).equals("1")){
//            has_id_section=true;
//        }
        initView(view);
        getArgumentData();
        initControl();
        return view;
    }
    private void getArgumentData() {
        user_type=getArguments().getString("user_type");
        type=getArguments().getString("type");

    }

    private void initView(View view) {

        lin_basicdetail =view.findViewById(R.id.lin_basicdetail);
        iv_basic = view.findViewById(R.id.iv_basic);
        tv_basic =view.findViewById(R.id.tv_basic);
        lin_activity =view.findViewById(R.id.lin_activity);
        iv_activity =view.findViewById(R.id.iv_activity);
        tv_activity =view.findViewById(R.id.tv_activity);

        lin_achievement =view.findViewById(R.id.lin_achievement);
        iv_achievement =view.findViewById(R.id.iv_achievement);
        tv_achievement =view.findViewById(R.id.tv_achievement);

        lin_other =view.findViewById(R.id.lin_other);
        iv_other =view.findViewById(R.id.iv_other);
        tv_other =view.findViewById(R.id.tv_other);

        //chat_btn.setOnTouchListener(this);

        lin_basicdetail.setOnClickListener(this);
        lin_achievement.setOnClickListener(this);
        lin_activity.setOnClickListener(this);
        lin_other.setOnClickListener(this);

    }


    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount()==0){
            showIDBsicDetailFragment();
        }

    }
    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.frame_layout_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String hasId="";
        if (user_type.equals(SCHOOL_COACHING)){
            //for school coaching
            hasId=management.getString(SCHOOL_ID_SECTION);
        }else{
            hasId=management.getString(ID_SECTION);
        }
        switch (v.getId()){
            case R.id.lin_basicdetail:
                getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                showIDBsicDetailFragment();
                break;
            case R.id.lin_activity:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
               // if (has_id_section)


                if (hasId.equals("1")) {
                    getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    showActivityFragment();
                }
                else {
                    CommonMethods.showSuccessToast(getContext(), "Complete your basic details");
                }
                break;
            case R.id.lin_achievement:
                // getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
               // if (has_id_section)
                if (hasId.equals("1")) {
                    getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    showaAchievmentFragment();
                }
                 else
                CommonMethods.showSuccessToast(getContext(),"Complete your basic details");
                break;
            case R.id.lin_other:
                //  getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
              //  if (has_id_section)
                if (hasId.equals("1")) {
                    getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    showOtherFragment();
                }
                 else
                CommonMethods.showSuccessToast(getContext(),"Complete your basic details");
                break;
        }
    }

    private void showIDBsicDetailFragment() {
        setBasicdetailBgColor();
        IDBasicdetailFragment fragment=new IDBasicdetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("user_type",user_type);
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }


    private void showOtherFragment() {
        setotherColor();
        // chat_btn.setVisibility(View.VISIBLE);

        IDOtherFragment fragment=new IDOtherFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("user_type",user_type);
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }
    private void showaAchievmentFragment() {
        setAchievmentBgColor();
        IDAchievementFragment fragment=new IDAchievementFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("user_type",user_type);
        fragment.setArguments(bundle);
        SwitchFragment(fragment);
    }
    private void showActivityFragment() {
        showActivityBg();
        IDActivityFragment fragment=new IDActivityFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("user_type",user_type);
        fragment.setArguments(bundle);
            SwitchFragment(fragment);
    }

    public void setotherColor() {
        lin_basicdetail.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_basic.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_basic.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_activity.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_activity.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_activity.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_achievement.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_achievement.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_achievement.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_other.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_other.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_other.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_other.requestFocus();
    }

    public void setBasicdetailBgColor() {

        lin_basicdetail.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_basic.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_basic.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_activity.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_activity.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_activity.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_achievement.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_achievement.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_achievement.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_other.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_other.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_other.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }

    public void setAchievmentBgColor() {

        lin_basicdetail.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_basic.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_basic.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_activity.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_activity.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_activity.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_achievement.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_achievement.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_achievement.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_other.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_other.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_other.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
    }


    public void showActivityBg() {
        lin_basicdetail.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_basic.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_basic.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_activity.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.graient2));
        iv_activity.setColorFilter(ContextCompat.getColor(getContext(),R.color.white));
        tv_activity.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        lin_achievement.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_achievement.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_achievement.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        lin_other.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        iv_other.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        tv_other.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (user_type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).setTeacherActionBar("ID Section", false);
            } else if (user_type.equals("student")) {
                ((MainActivity) getActivity()).setStudentChildActionBar("ID Section", false);
            }else if (user_type.equals(SCHOOL_COACHING)){
                //for school section
                ((SchoolMainActivity)getActivity()).setActionBarTitle("ID Section");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
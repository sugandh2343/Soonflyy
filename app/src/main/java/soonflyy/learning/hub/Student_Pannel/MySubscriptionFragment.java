package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.SubscriptionAdapter;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MySubscriptionFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = MySubscriptionFragment.class.getName();
    RecyclerView rcy_subscription;
    RelativeLayout rel_no_course;
    SwipeRefreshLayout refresh_layout;
    Button btn_explore;
    String from;
    LinearLayout rel_show_reclist;
    private SessionManagement sessionManagement;
    private String user_id;

    private List<SubscribedCourse>courseList=new ArrayList<>();
    private SubscriptionAdapter adapter;
    private String course_id,rating_value="";
    int adapterPosition=-1;

    public MySubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_subscriptionk, container, false);
        init(view);
        sessionManagement=new SessionManagement(getContext());
        user_id=sessionManagement.getString(USER_ID);
        initControl();
      ///  setDataOnList();
       requestApiCall();
       btn_explore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((MainActivity)getActivity()).getSupportFragmentManager()
                       .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
               Fragment fm = new HomeFragment();
               ((MainActivity)getActivity()).getSupportFragmentManager().
                       beginTransaction().replace(R.id.container_layout,fm).commit();
           }
       });
        return  view;
    }

    private void requestApiCall() {
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.SUBSCRIBED_COURSES);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void init(View view) {
       // tv_title = view.findViewById(R.id.tv_title);
       btn_explore = view.findViewById(R.id.explore_btn);
        rcy_subscription = view.findViewById(R.id.rcy_subscription);
        refresh_layout=view.findViewById(R.id.refresh_subscription);
        rel_no_course=view.findViewById(R.id.rel_no_subscription);
        rel_show_reclist=view.findViewById(R.id.rel_show_reclist);
    }


    private void initControl() {
        refresh_layout.setOnRefreshListener(this);

       // arrow_back_img.setOnClickListener(this);
    }

    private void setDataOnList() {

        adapter = new SubscriptionAdapter(getContext(),courseList,MySubscriptionFragment.this);
        rcy_subscription.setLayoutManager(new LinearLayoutManager(getContext()));
        rcy_subscription.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount()>0){
            rel_no_course.setVisibility(View.GONE);
            rel_show_reclist.setVisibility(View.VISIBLE);
        }else{
            rel_no_course.setVisibility(View.VISIBLE);
            rel_show_reclist.setVisibility(View.GONE);
        }
    }

    public void onRateClick(int position,SubscribedCourse course){
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_rate_course);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        Button btn_submit=dialog.findViewById(R.id.btn_submit_rate);
        RatingBar ratingBar=dialog.findViewById(R.id.rating_bar);
        TextView btn_cancel=dialog.findViewById(R.id.tv_back);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_id=course.getCourse_id();
                rating_value=String.valueOf(ratingBar.getRating());
                if (!TextUtils.isEmpty(rating_value)){
                    if (ConnectivityReceiver.isConnected()){
                        sendRequest(ApiCode.GIVE_COURSE_RATING);
                        adapterPosition=position;
                        dialog.dismiss();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }else{
                    CommonMethods.showSuccessToast(getContext(),"Please rate us");
                }
              //  CommonMethods.showSuccessToast(getContext(),"Rating : "+ratingBar.getRating());
                dialog.dismiss();
               // CommonMethods.showSuccessToast(getContext(),"Thanks For Rate Us");
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
       // tv_title.setText("My Subscription");
        ((MainActivity)getActivity()).setStudentChildActionBar("My Subscriptions",false);
       }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.arrow_back_img:
              //  goToMainScreen();
                //break;
        }
    }

    public void onclick(int position){
        goToRailwayFragment(position);


    }

    private void goToRailwayFragment(int position) {
        Subscribed_Course_Details railwayCompletedFragment = new Subscribed_Course_Details();

        SubscribedCourse course=courseList.get(position);
        try {
            if (!CommonMethods.isExpired(course.getExpiry_date())) {
                Bundle args = new Bundle();
                // args.putString(AppConstant.FROM,from);
               args.putString("from",SIMPLEE_HOME_STUDENT);
                args.putParcelable("sCourse", course);
                railwayCompletedFragment.setArguments(args);
                ((MainActivity) getActivity()).SwitchFragment(railwayCompletedFragment);
            }else{
               //showDialogToRenewal(course);
                CommonMethods.showSuccessToast(getActivity(),"This course has been expired");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //SwitchFragment (railwayCompletedFragment);
    }

    private void showDialogToRenewal(SubscribedCourse course) {
        //write code to purchase
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("This course has been expired. Do you want to renew ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go to purchase
                        Purchase_Course_Details fragment=new Purchase_Course_Details();
                        Bundle bundle=new Bundle();
//                        bundle.putString("course_type","renewal");
//                        bundle.putParcelable("sCourse",course);
                        bundle.putString("course_id",course.getCourse_id());
                        bundle.putString("title",course.getTitle());
                        fragment.setArguments(bundle);
                        ((MainActivity)getActivity()).SwitchFragment(fragment);
                       dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.SUBSCRIBED_COURSES :
                params.put("user_id",user_id);
                callApi(ApiCode.SUBSCRIBED_COURSES, params);
                break;

            case ApiCode.GIVE_COURSE_RATING :
                params.put("user_id",user_id);
                params.put("course_id",course_id);
                params.put("rating_value",rating_value);
                callApi(ApiCode.GIVE_COURSE_RATING, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.SUBSCRIBED_COURSES:
                service.postDataVolley(ApiCode.SUBSCRIBED_COURSES,
                        BaseUrl.URL_SUBSCRIBED_COURSES, params);
                break;
            case ApiCode.GIVE_COURSE_RATING:
                service.postDataVolley(ApiCode.GIVE_COURSE_RATING,
                        BaseUrl.URL_GIVE_COURSE_RATING, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.SUBSCRIBED_COURSES:
                Log.e("sub coruses",response);
                try {
                    JSONObject object=new JSONObject(response);
                    boolean res=object.getBoolean("status");
                    if (res){
                        JSONArray array = object.getJSONArray("data");
                      if (array.length()>0) {
                            List<SubscribedCourse> psearch = new Gson().
                                    fromJson(array.toString(),
                                            new TypeToken<List<SubscribedCourse>>() {
                                            }.getType());
                            courseList.clear();
                            courseList.addAll(psearch);
                          setDataOnList();
                        }else{
                          CommonMethods.showSuccessToast(getContext(),"You have not subscribed any course");
                      }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case ApiCode.GIVE_COURSE_RATING:
                Log.e("rating",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getBoolean("status")){
                        CommonMethods.showSuccessToast(getContext(),"Thanks for rating us");
                        notifyData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void notifyData() {
        courseList.get(adapterPosition).setRating_value(rating_value);
        adapter.notifyItemChanged(adapterPosition);
        adapterPosition=-1;
        rating_value="";
    }

    @Override
    public void onRefresh() {
        refresh_layout.setRefreshing(false);
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.SUBSCRIBED_COURSES);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

}
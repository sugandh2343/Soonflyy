package soonflyy.learning.hub.Student_Pannel;

import static androidx.appcompat.widget.SearchView.OnClickListener;
import static androidx.appcompat.widget.SearchView.OnQueryTextListener;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.Common_Package.NotificationFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Course_model;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.Category_adapter_student;
import soonflyy.learning.hub.adapter.RecommendCourseAdapter;
import soonflyy.learning.hub.studentModel.Category_student_model;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements OnClickListener, VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = HomeFragment.class.getName();
    LinearLayout lin_subcription;


    TextView tv_cat_title;
    RecommendCourseAdapter adapter;
    RecyclerView rv_recommended_corse;
    RecyclerView rec_category;
    ArrayList<Notification_model>notificationList=new ArrayList<>();
  SearchView et_search;

    Category_adapter_student category_adapter;
   List<Category_student_model>  category_models=new ArrayList<>();
    List<Course_model> courseList =new ArrayList<>();
    SwipeRefreshLayout refresh_home;
    String category_id="";
    int selectedCatPosition=0;
    SessionManagement sessionManagement;
    NestedScrollView nestedScrollView;
    int scrollPosition=0;

   // ImageView img_1;


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("cat_position",);
//    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
       sessionManagement=new SessionManagement(getActivity());
       selectedCatPosition=sessionManagement.getInt("selected_Cat");
        CommonMethods.enableScreenshot(getActivity().getWindow());
        init(view);
        initControl();

        init_category();

        Log.e("callOnCreate","true");

            callRequrestedApi();


        et_search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(courseList.size ()>0) {
                    adapter.getFilter().filter(query.trim());
                }

                Log.e ("cv", "onQueryTextChange: "+query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if(courseList.size ()>0) {
                    adapter.getFilter().filter(query.trim());
                }

                //recentExamAdapter.getFilter().filter(query);
                Log.e ("cvbn", "onQueryTextChange: "+query );
                return false;
            }
        });

        refresh_home.setOnRefreshListener(this);




        return view;
    }


    @Override
    public void onRefresh() {
        scrollPosition=0;
        refresh_home.setRefreshing(false);
        callRequrestedApi();
        init_category();



    }


    private void callRequrestedApi(){

        if (ConnectivityReceiver.isConnected()){

            sendRequest(ApiCode.GET_CATEGORY);
            sendRequest(ApiCode.GET_COURSE_CAT_WISE);
            sendRequest(ApiCode.GET_NOTIFICATION);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }

    }

    private void init_category() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rec_category.setLayoutManager(linearLayoutManager);
        rec_category.setKeepScreenOn(true);
        rec_category.setHasFixedSize(true);
        category_adapter = new Category_adapter_student(getActivity(), category_models, new Category_adapter_student.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int position, Category_student_model catModel) {
                selectedCatPosition=position;
                scrollPosition=0;
             sessionManagement.setInt("selected_Cat",selectedCatPosition);
                tv_cat_title.setText(catModel.getName());
                courseList.clear();
                setOhterCatSelection(position);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged();
                }
                if (ConnectivityReceiver.isConnected()){
                    category_id=catModel.getId();
                    sendRequest(ApiCode.GET_COURSE_CAT_WISE);
                }
            }
        });
        rec_category.setAdapter(category_adapter);
        category_adapter.notifyDataSetChanged();

    }


    private void init(View view) {


        nestedScrollView=view.findViewById(R.id.nested_scroll);
        lin_subcription=view.findViewById(R.id.lin_subscription);
//
        et_search=view.findViewById (R.id.et_search);
        et_search.setQueryHint("Search courses");

        refresh_home=view.findViewById(R.id.refresh_home);
        rec_category = view.findViewById(R.id.rec_category);
        tv_cat_title=view.findViewById(R.id.tv_cat_title);





        rv_recommended_corse = view.findViewById(R.id.rv_recommended_corse);

        rv_recommended_corse.setLayoutManager(new LinearLayoutManager(getContext()));



    }


    private void initControl() {
//
        lin_subcription.setOnClickListener(this);
       // iv_notification.setOnClickListener(this);
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_CATEGORY:
                params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                callApi(ApiCode.GET_CATEGORY, params);
                break;
            case ApiCode.GET_NOTIFICATION:
                params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                callApi(ApiCode.GET_NOTIFICATION, params);
                break;
            case ApiCode.GET_COURSE_CAT_WISE:
                params.put("type","course");
                params.put("cat_id",category_id);
                params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));

                callApi(ApiCode.GET_COURSE_CAT_WISE, params);
                break;
//
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_CATEGORY:
                service.postDataVolley(ApiCode.GET_CATEGORY,
                        BaseUrl.URL_GET_CATEGORY, params);
                break;
            case ApiCode.GET_NOTIFICATION:
                service.postDataVolley(ApiCode.GET_NOTIFICATION,
                        BaseUrl.URL_GET_NOTIFICATION, params);
                break;
            case ApiCode.GET_COURSE_CAT_WISE:
                service.postDataVolley(ApiCode.GET_COURSE_CAT_WISE,
                        BaseUrl.URL_GET_COURSE_CAT_WISE, params);
                Log.e("callURL",""+BaseUrl.URL_GET_COURSE_CAT_WISE);
                Log.e("pramas",""+params);
                break;
//

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.iv_bookmark:
                gotoBookMarkScreen();
                break;
            case R.id.lin_subscription:
                goToSubscriptionPage();
                break;
            case R.id.iv_notification:
                gotoNotification();
                break;
        }
    }

    private void gotoNotification() {
        NotificationFragment fragment=new NotificationFragment();
        Bundle bundle=new Bundle();
        bundle.putString("from",SIMPLEE_HOME_STUDENT);
        bundle.putParcelableArrayList("notifications",notificationList);
        fragment.setArguments(bundle);
        ((MainActivity)getActivity()).SwitchFragment(fragment);
    }

    private void goToSubscriptionPage() {
        MySubscriptionFragment mySubscriptionFragment = new MySubscriptionFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.FROM,"from");
        mySubscriptionFragment.setArguments(args);
        ((MainActivity)getActivity()). SwitchFragment (mySubscriptionFragment);
    }

    private void gotoBookMarkScreen() {
        ((MainActivity)getActivity()).SwitchFragment(new MyBookmarks_Fragment());
    }




    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment, HomeFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_CATEGORY:
                Log.e("category_data",response);
                if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0) {
                            List<Category_student_model> catList = new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<Category_student_model>>() {
                                            }.getType());
                            category_models.clear();
                            Category_student_model model=new Category_student_model("","All Course");
                            if (selectedCatPosition==0) {
                                model.setSelected(true);
                            }
                            category_models.add(model);
                            category_models.addAll(catList);
                            init_category();
                            if (selectedCatPosition>0 && selectedCatPosition<category_models.size()){
//                               new Handler().post(new Runnable() {
//                                   @Override
//                                   public void run() {
                                       rec_category.scrollToPosition(selectedCatPosition);
                                      // category_adapter.setCategoryColor(rec_category.findViewHolderForAdapterPosition(selectedCatPosition),selectedCatPosition);
                                       setOhterCatSelection(selectedCatPosition);
                                       category_adapter.notifyDataSetChanged();
//                                   }
//                               });
                                category_id=category_models.get(selectedCatPosition).getId();
                                sendRequest(ApiCode.GET_COURSE_CAT_WISE);
                            }


                        }
                    }
                break;
            case ApiCode.GET_COURSE_CAT_WISE:
                Log.e("course_catewise",response);
                if (jsonObject.getBoolean("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        List<Course_model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Course_model>>() {
                                        }.getType());
                        courseList.clear();
                        courseList.addAll(psearch);

                        //------Arrange list according rating----//
                        Collections.sort(courseList, new Comparator<Course_model>() {
                        @Override
                            public int compare(Course_model o1, Course_model o2) {
                                float rating1= Float.parseFloat(o1.getRating());
                                float rating2= Float.parseFloat(o2.getRating());
//                                if (rating1>rating2){
//                                    return 1;
//                                }else {
//                                    return 0;
//                                }
                            return Float.compare(rating2, rating1);
                            }
                        });
                        //---------------//

                       // SnapHelper snapHelper = new PagerSnapHelper();
                        adapter = new RecommendCourseAdapter(getActivity(), courseList, new RecommendCourseAdapter.OnReCommendCourseClickListener() {
                            @Override
                            public void onItemClick(int position, Course_model course) {
                                scrollPosition=position;
//                                if(course.getIs_subscribed().equals("1")){
//                                    MySubscriptionFragment fragment=new MySubscriptionFragment();
//                                    ((MainActivity)getActivity()).SwitchFragment(fragment);
//                                }else {
                                    Purchase_Course_Details fragment = new Purchase_Course_Details();
                                    Bundle arg = new Bundle();
                                    arg.putString("course_id",course.getId());
                                    arg.putString("title",course.getTitle());
//                                    arg.putString("course_type", "teacher_course");
//                                    arg.putParcelable("course", course);
                                    fragment.setArguments(arg);
                                    ((MainActivity) getActivity()).SwitchFragment(fragment);
                               /// }
                            }});
                       // snapHelper.attachToRecyclerView(rv_recommended_corse);
                        rv_recommended_corse.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if(scrollPosition>0){
                            if (adapter.getItemCount()>=scrollPosition){
                               rv_recommended_corse.smoothScrollToPosition(scrollPosition);
                            }
                        }

                    }
                    else{
                        try {
                            courseList.clear();
                            adapter.notifyDataSetChanged();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case ApiCode.GET_NOTIFICATION:
                Log.e("notification",response);
                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        List<Notification_model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Notification_model>>() {
                                        }.getType());
                        notificationList.clear();
                        notificationList.addAll(psearch);
                       // setNotificationCount(notificationList.size());
                        try {
                            ((MainActivity) getActivity()).setNotifications(notificationList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        notificationList.clear();
                        //setNotificationCount(notificationList.size());
                        try {
                            ((MainActivity) getActivity()).setNotifications(notificationList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    notificationList.clear();
                    //setNotificationCount(notificationList.size());
                    try {
                        ((MainActivity) getActivity()).setNotifications(notificationList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;

//
        }
    }

    private void setOhterCatSelection(int sPosition) {
        Log.e("positions",""+sPosition);
        for (int i=0;i<category_models.size();i++){
            Category_student_model model=category_models.get(i);
            if (sPosition==i){
                model.setSelected(true);
                tv_cat_title.setText(model.getName());
            }
            else{
                model.setSelected(false);
            }
        }
        category_adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).drawerToggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).setStudentChildActionBar("",true);

        Log.e("selectedValue",""+selectedCatPosition);
        Log.e("scrollValue",""+scrollPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("homefragment","destroyed");
    }

    @Override
    public void onPause() {
        super.onPause();
      //  isCreated=false;
    }
}
package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import soonflyy.learning.hub.Student_Pannel.Model.Course_model;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.RecommendCourseAdapter;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyBookmarks_Fragment extends Fragment implements VolleyResponseListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rec_bookmark;
    RelativeLayout rel_no_bookmark;
    LinearLayout lin_bookmark;
    Button btn_explore;
    //BookmarkCourseAdapter bookmarkAdapter;
    ArrayList<Course_model>bookmarkList=new ArrayList<>();
    RecommendCourseAdapter bookmarkAdapter;
    SwipeRefreshLayout refresh_bookmark;


    public MyBookmarks_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_bookmarks_, container, false);
        bindViewId(view);
        setBookmark();
        setApiRequest();
        btn_explore.setOnClickListener(this);
        refresh_bookmark.setOnRefreshListener(this);
        return  view;
    }

    private void setApiRequest() {
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.GET_COURSE_CAT_WISE);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void setBookmark() {
        bookmarkAdapter=new RecommendCourseAdapter(getContext(), bookmarkList,
                new RecommendCourseAdapter.OnReCommendCourseClickListener() {
                    @Override
                    public void onItemClick(int position, Course_model course) {
                        Purchase_Course_Details fragment = new Purchase_Course_Details();
                        Bundle arg = new Bundle();
                        arg.putString("course_id",course.getId());
                        arg.putString("title",course.getTitle());
//                                    arg.putString("course_type", "teacher_course");
//                                    arg.putParcelable("course", course);
                        fragment.setArguments(arg);
                        ((MainActivity) getActivity()).SwitchFragment(fragment);
                    }
        });
        rec_bookmark.setAdapter(bookmarkAdapter);
        bookmarkAdapter.notifyDataSetChanged();
        if (bookmarkAdapter.getItemCount()>0){
            rel_no_bookmark.setVisibility(View.GONE);
            lin_bookmark.setVisibility(View.VISIBLE);
        }else{

            lin_bookmark.setVisibility(View.GONE);
            rel_no_bookmark.setVisibility(View.VISIBLE);
        }

    }

    private void bindViewId(View view) {
        rec_bookmark=view.findViewById(R.id.rec_bookmark_course);
        rel_no_bookmark=view.findViewById(R.id.rel_no_videos);
        lin_bookmark=view.findViewById(R.id.lin_bookmark);
        btn_explore=view.findViewById(R.id.btn_explore);
        refresh_bookmark=view.findViewById(R.id.refresh_bookmark);
        rec_bookmark.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_COURSE_CAT_WISE:
                params.put("type", "bookmark");
                params.put("cat_id", "");
                 params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                callApi(ApiCode.GET_COURSE_CAT_WISE, params);
                break;
        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_COURSE_CAT_WISE:
                service.postDataVolley(ApiCode.GET_COURSE_CAT_WISE,
                        BaseUrl.URL_GET_COURSE_CAT_WISE, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.GET_COURSE_CAT_WISE:
                Log.e("course_catewise", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        List<Course_model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Course_model>>() {
                                        }.getType());
                        bookmarkList.clear();
                        bookmarkList.addAll(psearch);
                        setBookmark();
                    }else{
                        bookmarkList.clear();
                        setBookmark();
                    }
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_explore:
                gotoHomePage();
                break;
        }
    }

    private void gotoHomePage() {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fm = new HomeFragment ();
        ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container_layout,fm).commit();
    }

    @Override
    public void onRefresh() {
        refresh_bookmark.setRefreshing(false);
        setApiRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setStudentChildActionBar("My Bookmark",false);
    }
}
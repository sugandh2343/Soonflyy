package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.Video_Adapter;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.Common_Package.Play_All_VideoFragment;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Video_Fragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rec_video;
    ArrayList<Video_Model> videoList = new ArrayList<>();
    Video_Adapter video_adapter;
    String chapter_id, subject_id, course_id,from;
    SwipeRefreshLayout refreshLayout;
    LinearLayout lin_video_show;
    RelativeLayout rel_no_video;
    TextView tv_download_watch;
    String courseName;


    public Video_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_, container, false);
        bindId(view);
        getArgumentData();
        sendApiRequest();

        //setVideo();


        return view;
    }

    private void sendApiRequest() {
        if (ConnectivityReceiver.isConnected()) {
            if (from.equals(SCHOOL_COACHING) || from.equals(SCHOOL_STUDENT)) {
                sendRequest(ApiCode.SCHOOL_GET_VIDEO);
            } else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)){
                sendRequest(ApiCode.GET_CHAPTER_VIDEO);
            }
        }  else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }


    }

    private void getArgumentData() {
        subject_id = getArguments().getString("subject_id");
        chapter_id = getArguments().getString("chapter_id");
        course_id = getArguments().getString("course_id");
        courseName=getArguments().getString("courseName");
        from=getArguments().getString("from");
    }

    private void setVideo() {

        video_adapter = new Video_Adapter(getContext(), videoList, new Video_Adapter.OnVideoClickListener() {
            @Override
            public void onVideoClick(int position) {
                Play_All_VideoFragment fragment = new Play_All_VideoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoList", videoList);
                bundle.putInt("position", position);
                bundle.putString("from", from);
                bundle.putString("courseName", courseName);
                fragment.setArguments(bundle);
                if (from.equals(SCHOOL_COACHING) || from.equals(SCHOOL_STUDENT)) {
                    ((SchoolMainActivity) getActivity()).switchFragmentOnSchoolMainActivity(fragment);
                }else{
                    ((MainActivity) getActivity()).SwitchFragment(fragment);
                }
            }
        });
        rec_video.setAdapter(video_adapter);
        video_adapter.notifyDataSetChanged();
        if (video_adapter.getItemCount()>0){
          //  rel_no_video.setVisibility(View.GONE);
            lin_video_show.setVisibility(View.VISIBLE);
        }else{

            lin_video_show.setVisibility(View.GONE);
           // rel_no_video.setVisibility(View.VISIBLE);
        }


    }

    private void bindId(View view) {
        tv_download_watch=view.findViewById(R.id.tv_download_watch);
        rec_video = view.findViewById(R.id.rec_video);
        refreshLayout = view.findViewById(R.id.refresh_video);
        lin_video_show = view.findViewById(R.id.lin_video_show);
        rel_no_video = view.findViewById(R.id.rel_no_video);
        rel_no_video.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(this);
        rec_video.setLayoutManager(new GridLayoutManager(getContext(), 3));
        tv_download_watch.setVisibility(View.GONE);
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_CHAPTER_VIDEO:
                // if (liveType.equals("course_wise")) {
                params.put("chapter_id", chapter_id);
                params.put("section_id", subject_id);
//                params.put("course_id", course_id);
//                params.put("lesson_id", chapter_id);
//                params.put("type", "video");
                //  params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_CHAPTER_VIDEO, params);
                break;
            case ApiCode.SCHOOL_GET_VIDEO:
                params.put("chapter_id", chapter_id);
                params.put("subject_id", subject_id);
//
                callApi(ApiCode.SCHOOL_GET_VIDEO, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_CHAPTER_VIDEO:
                service.postDataVolley(ApiCode.GET_CHAPTER_VIDEO,
                        BaseUrl.URL_GET_CHAPTER_VIDEO, params);
                break;
            case ApiCode.SCHOOL_GET_VIDEO:
                service.postDataVolley(ApiCode.SCHOOL_GET_VIDEO,
                        BaseUrl.URL_SCHOOL_GET_VIDEO, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType) {
            case ApiCode.GET_CHAPTER_VIDEO:
            case ApiCode. SCHOOL_GET_VIDEO:
                Log.e("video ", response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Video_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<Video_Model>>() {
                                        }.getType());
                        videoList.clear();
                        videoList.addAll(psearch);
                        setVideo();
                        //video_adapter.notifyDataSetChanged();
                    } else {
                        videoList.clear();
                        setVideo();
                        // video_adapter.notifyDataSetChanged();
                        CommonMethods.showSuccessToast(getContext(), "Video not available");
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((Subject_Chapter_Details_Fragment)getParentFragment()).setVideoBackground();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).showAssignToProfile();

    }
}
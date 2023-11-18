package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.utlis.AppConstant.FROM;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.Subject_Chapter_Adapter;
import soonflyy.learning.hub.Student_Pannel.Adapter.TodayLive_Adapter;
import soonflyy.learning.hub.Student_Pannel.Model.S_LiveModel;
import soonflyy.learning.hub.Student_Pannel.Model.Subject_Chapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.live.LiveClassesActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TopicFragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView rv_topic, rec_todayLive;
    Subject_Chapter_Adapter chapter_adapter;
    private ArrayList<Subject_Chapter> topicList = new ArrayList<>();

    private SwipeRefreshLayout refreshLayout;
    ArrayList<S_LiveModel> todayLiveList = new ArrayList<>();
    TodayLive_Adapter todayLiveAdapter;
    TextView tv_today;

    private SessionManagement sessionManagement;
    String sub_name, subject_id, course_id, courseName,from;
    int subPosition;


    ///------------profile Assign to-----------//
    View assignToLayoutView,noticeLayout;
    TextView tvType,tvProfileName,tvMobile;
    CircleImageView profileImg;
    AssignProfile assignProfile;
    //RelativeLayout relProfile;

    //--------------------------------//


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        // sessionManagement-=new SE

        initView(view);

        getArgumentData();
        //setTopic();
        //sendApiRequest();


        refreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void sendApiRequest() {
        if (ConnectivityReceiver.isConnected()) {
            if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_SUBJECT_CHAPTER);
            }
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void initView(View view) {

        noticeLayout=view.findViewById(R.id.include_notice);


        tv_today=view.findViewById(R.id.tv_topic);
        rv_topic = view.findViewById(R.id.rv_chapter);
        rec_todayLive = view.findViewById(R.id.rec_today_live);
        refreshLayout = view.findViewById(R.id.refresh_chapter);


        //-------------------------------------//
        // relProfile=getView().findViewById(R.id.);
        tvProfileName=view.findViewById(R.id.assign_tv_name);
        tvMobile=view.findViewById(R.id.assign_tv_mobile);
        tvType=view.findViewById(R.id.tv_type);
        profileImg=view.findViewById(R.id.assign_iv_profile_img);
        assignToLayoutView=view.findViewById(R.id.include_assign_sub);

        //---------listener---------//
        noticeLayout.setOnClickListener(this);

        //-------------------------------------//


    }

    private void getArgumentData() {
        from=getArguments().getString(FROM);
        subject_id = getArguments().getString("subject_id");
        sub_name = getArguments().getString("subject_name");
        course_id = getArguments().getString("course_id");
        courseName = getArguments().getString("course_name");
        subPosition = getArguments().getInt("subjectPosition");
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
            setProfileData();
        }
    }

    private void setProfileData() {
        if (assignProfile!=null){
            tvType.setText("Assigned to");
            String imaglink= assignProfile.getImage();
            Log.e("image",""+imaglink);
            Picasso.get().load(imaglink).placeholder(R.drawable.logoo)
                    .into(profileImg);
            tvProfileName.setText(assignProfile.getName());
            tvMobile.setText(assignProfile.getMobile());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sendApiRequest();
        if (from.equals(SIMPLEE_HOME_STUDENT))
        ((MainActivity) getActivity()).setStudentChildActionBar("S-" + subPosition + " (" + sub_name + ")", false);
        else if (from.equals(SIMPLEE_HOME_TUTOR)) {
            ((TeacherMainActivity) getActivity()).setTeacherActionBar("S-" + subPosition + " (" + sub_name + ")", false);
            assignToLayoutView.setVisibility(View.VISIBLE);
            assignToLayoutView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_smoke));
        }

        // ((Subscribed_Course_Details)getParentFragment()).showChapterBg();
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_SUBJECT_CHAPTER:
                params.put("subject_id", subject_id);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_SUBJECT_CHAPTER, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_SUBJECT_CHAPTER:
                service.postDataVolley(ApiCode.GET_SUBJECT_CHAPTER,
                        BaseUrl.URL_GET_SUBJECT_CHAPTER, params);
                Log.e("url",""+BaseUrl.URL_GET_SUBJECT_CHAPTER);
                Log.e("params",""+params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType) {
            case ApiCode.GET_SUBJECT_CHAPTER:
                Log.e("sub_chapters ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("chapters");
                        JSONArray jsonLiveArray = jsonObject.getJSONObject("data").getJSONArray("live");
                        if (jsonArray.length() > 0) {
                            ArrayList<Subject_Chapter> psearch = new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<Subject_Chapter>>() {
                                            }.getType());
                            topicList.clear();
                            topicList.addAll(psearch);
                            setChapterTopic();
                            // setChapterTopic();
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Chapter");
                        }

                        if (jsonLiveArray.length() > 0) {
                            ArrayList<S_LiveModel> psearch = new Gson().
                                    fromJson(jsonLiveArray.toString(),
                                            new TypeToken<List<S_LiveModel>>() {
                                            }.getType());
                            todayLiveList.clear();
                           // todayLiveList.addAll(psearch);
                            if (psearch.size()>0) {
                                new TodayLiveAsyncTask().execute(psearch);
                            }
                            //  setChapterTopic();
                            // setChapterTopic();
                           // setTodayLive();
                        } else {
                            todayLiveList.clear();
                            setTodayLive();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    private void setTodayLive() {
        rec_todayLive.setLayoutManager(new LinearLayoutManager(getContext()));
        todayLiveAdapter = new TodayLive_Adapter(getContext(), todayLiveList, new TodayLive_Adapter.OnTodayLiveListener() {
            @Override
            public void onLiveJoin(int position, S_LiveModel model) {
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
//                    LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("type","student");
//                    bundle.putString("slug",model.getSlug());
//                    bundle.putString("description",model.getDescription());
//                    fragment.setArguments(bundle);
//                    ((MainActivity)getActivity()).SwitchFragment(fragment);

                   Intent lIntent = new Intent(getActivity(), LiveClassesActivity.class);

                    lIntent.putExtra("title", model.getTitle());
                    lIntent.putExtra("sTime", model.getStart_time());
                    lIntent.putExtra("eTime", model.getEnd_time());
                    lIntent.putExtra("slug", model.getSlug());
                    lIntent.putExtra("live_type", "chapter");
                    lIntent.putExtra("type", "student");
                    lIntent.putExtra("token",model.getToken());
                    lIntent.putExtra("meeting_id",model.getMeeting_id());
                    lIntent.putExtra("from",from);
                    lIntent.putExtra("hostId",model.getUser_id());
                    lIntent.putExtra("description", model.getDescription());
                    getActivity().startActivity(lIntent);

                } else {
                    CommonMethods.requestAudioCameraPermission(getActivity(), 333);
                }
            }
        });
        rec_todayLive.setAdapter(todayLiveAdapter);
        todayLiveAdapter.notifyDataSetChanged();
        if (todayLiveAdapter.getItemCount()>0){
            tv_today.setVisibility(View.VISIBLE);
        }else{
            tv_today.setVisibility(View.GONE);
        }
    }

    private void setChapterTopic() {
        chapter_adapter = new Subject_Chapter_Adapter(getContext(), topicList, new Subject_Chapter_Adapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(int position) {
                Subject_Chapter_Details_Fragment fragment = new Subject_Chapter_Details_Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", from);//SIMPLEE_HOME_STUDENT
                bundle.putString("course_id", course_id);
                bundle.putString("chapter_id", topicList.get(position).getLesson_id());
                bundle.putString("subject_id", subject_id);
                bundle.putString("course_name", courseName);
                bundle.putString("subName", sub_name);
                bundle.putString("chapterName", topicList.get(position).getChapter_title());
                if (from.equals(SIMPLEE_HOME_TUTOR)){
                    bundle.putSerializable("profileData",assignProfile);
                }
                fragment.setArguments(bundle);
                if (from.equals(SIMPLEE_HOME_TUTOR)){
                    ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
                }else {
                    ((MainActivity) getActivity()).SwitchFragment(fragment);
                }
                // Log.e("click","click");
            }
        });
        rv_topic.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_topic.setAdapter(chapter_adapter);


    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
        sendApiRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.include_notice:
                gotoNoticePage();
                break;
        }

    }

    private void gotoNoticePage() {
        Student_Notice_Fragment fragment=new Student_Notice_Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("course_id", course_id);
        bundle.putString("subject_id", subject_id);
        bundle.putString("from",from);
        bundle.putString("notice", "subject");
        bundle.putString("subTitle",sub_name);

        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            bundle.putString("type","AssignTo");
          //  if (sessionManagement.getString(ASSIGN).equals(ASSIGN_BY)) {
                bundle.putSerializable("profileData", assignProfile);
           // }
        }
        fragment.setArguments(bundle);
        if (from.equals(SIMPLEE_HOME_TUTOR))
        ((TeacherMainActivity) getActivity()).SwitchFragment(fragment);
        else
            ((MainActivity)getActivity()).SwitchFragment(fragment);
    }


    private class TodayLiveAsyncTask extends AsyncTask<ArrayList<S_LiveModel>, Void, Void> {

        @Override
        protected Void doInBackground(ArrayList<S_LiveModel>... arrayLists) {
            ArrayList<S_LiveModel> list = new ArrayList<>();
            list.addAll(arrayLists[0]);
            for (S_LiveModel model : list) {
                if (model.getLive_status().equals("1")) {
                    todayLiveList.add(model);
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setTodayLive();
        }

    }
}
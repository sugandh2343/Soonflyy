package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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
import soonflyy.learning.hub.adapter.LiveAdapter;
import soonflyy.learning.hub.live.LiveClassesActivity;
import soonflyy.learning.hub.studentModel.Live;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Live_Classes_Fragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rec_live;
    private String liveType,course_id;
    private List<Live>liveList=new ArrayList<>();
    private LiveAdapter liveAdapter;
    String chapter_id,subject_id;
    String teacher_id,teacher_name,class_id,section_id,from;
    SwipeRefreshLayout refreshLayout;


    public Live_Classes_Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_live_list, container, false);
        initView(view);
       getArgData();
        //setLiveData();
       // sendApiRequest();
        //setLiveData();
//        rec_live.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_live, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
        return view;
    }

    private void sendApiRequest() {
        if(ConnectivityReceiver.isConnected())
            if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
            }else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_LIVE_CLASSES);
            }
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");

    }

    private void setLiveData() {
//
        liveAdapter=new LiveAdapter(getContext(), from, liveList, new LiveAdapter.OnLiveClickListener() {
            @Override
            public void goLive(int position, Live liveModel) {
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
                    String type="";
                  //  LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("live_type","chapter");
                    if (from.equals(SIMPLEE_HOME_STUDENT)) {
                        //bundle.putString("type", "student");
                        type="student";
                    }else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                        //bundle.putString("type", SCHOOL_COACHING);
                        type="school";
                    }
//                    bundle.putString("slug",liveModel.getSlug());
//                    bundle.putString("description"," ");
//                    fragment.setArguments(bundle);
//                    if (from.equals(SIMPLEE_HOME_STUDENT))
//                    ((MainActivity)getActivity()).SwitchFragment(fragment);
//                    else if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)) {
//                        ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);
//                    }

                 Intent lIntent=new Intent(getActivity(), LiveClassesActivity.class);
                  //  Intent lIntent=new Intent(getActivity(), ParticipaintLiveActivity.class);
                    lIntent.putExtra("title",liveModel.getTitle());
                    lIntent.putExtra("sTime",liveModel.getStart_time());
                    lIntent.putExtra("eTime",liveModel.getEnd_time());
                    lIntent.putExtra("slug",liveModel.getSlug());
                    lIntent.putExtra("live_id",liveModel.getId());
                    lIntent.putExtra("live_type","chapter");
                    lIntent.putExtra("token",liveModel.getToken());
                    lIntent.putExtra("meeting_id",liveModel.getMeeting_id());
                    lIntent.putExtra("type",type);
                    lIntent.putExtra("from",from);
                    Log.e("fromValue",""+from);
                    lIntent.putExtra("hostId",liveModel.getUser_id());
                    lIntent.putExtra("description",liveModel.getDescription());
                    getActivity().startActivity(lIntent);

                }else{
                    CommonMethods.requestAudioCameraPermission(getActivity(),333);
                }


            }
        });
//        liveAdapter=new LiveAdapter(getContext(), liveList, new LiveAdapter.OnLiveClickListener() {
//            @Override
//            public void goLive(int position, Live liveModel) {
//
//                LiveSessionFragment fragment=new LiveSessionFragment();
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("live",liveModel);
//                fragment.setArguments(bundle);
//                ((MainActivity)getActivity()).SwitchFragment(fragment);
//            }
//        });
        rec_live.setAdapter(liveAdapter);
        liveAdapter.notifyDataSetChanged();


    }

    private void getArgData() {
        from=getArguments().getString("from");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");

        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            teacher_name=getArguments().getString("teacher_name");
            class_id =getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");
            teacher_id=getArguments().getString("teacher_id");
        }else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
            course_id = getArguments().getString("course_id");
        }
//        liveType=getArguments().getString("liveType");
//        if (liveType.equals("all")){
//            //write code for all live list
//        }else if (liveType.equals("course_wise")){
//            course_id=getArguments().getString("course_id");
//        }
    }

    private void initView(View view) {
        rec_live=view.findViewById(R.id.rec_live_list);
        rec_live.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout=view.findViewById(R.id.refresh_live);
        refreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        sendApiRequest();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).setLiveBackground();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).showAssignToProfile();
      //  ((MainActivity)getActivity()).setStudentChildActionBar("Live List",false);
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_LIVE_CLASSES:
                params.put("chapter_id",chapter_id);
                params.put("section_id",subject_id);
                params.put("course_id",course_id);
                params.put("is_student","1");
                params.put("type","all");
               // if (liveType.equals("course_wise")) {
//                    params.put("course_id", course_id);
//                    params.put("chapter_id", chapter_id);
//                    Log.e("course_id",course_id);
                //}
                params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
               // Log.e("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_LIVE_CLASSES, params);
                break;
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_LIVE_CLASS, params);
                break;

        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_LIVE_CLASSES:
                service.postDataVolley(ApiCode.GET_LIVE_CLASSES,
                        BaseUrl.URL_GET_LIVE_CLASSES, params);
                break;
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_GET_LIVE_CLASS,
                        BaseUrl.URL_SCHOOL_GET_LIVE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LIVE_CLASS);
                Log.e("params", params.toString());
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType){
            case ApiCode.GET_LIVE_CLASSES:
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                Log.e("livelist ",response);
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.getBoolean("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        List<Live> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Live>>() {
                                        }.getType());
                        liveList.clear();
                        //liveList.addAll(psearch);
                       // setLiveData();
                        if (from.equals(SCHOOL_COACHING)){
                            liveList.addAll(psearch);
                             setLiveData();
                        }else {

                            if (psearch.size() > 0) {
                                new FilterLiveAsyncTask().execute(psearch);
                            }
                        }
                       // liveAdapter.notifyDataSetChanged();
                    }else{
                        liveList.clear();
                        setLiveData();
                       // liveAdapter.notifyDataSetChanged();
                        CommonMethods.showSuccessToast(getContext(),"Teacher is not live");
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

    private class FilterLiveAsyncTask extends AsyncTask<List<Live>, Void, Void> {

        @Override
        protected Void doInBackground(List<Live>... arrayLists) {
            List<Live> list = new ArrayList<>();
            list.addAll(arrayLists[0]);
            for (Live model : list) {
                if (model.getIs_live().equals("1")) {
                    liveList.add(model);
                    list.remove(model);
                    break;
                }
            }
            for (Live model : list) {
                try {
                    if (CommonMethods.isTodayDate(model.getDate())) {
                        if (!CommonMethods.isEventExpired(model.getDate()+" | "+model.getEnd_time())) {
                            liveList.add(model);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setLiveData();
        }

    }
}
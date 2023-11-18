package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.utlis.AppConstant.FROM;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.ChapterAdapter;
import soonflyy.learning.hub.model.Chapter;
import soonflyy.learning.hub.studentModel.BookMarkCourse;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SubjectFragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView rv_chapter;
    SwipeRefreshLayout refresh_subject;

    private ChapterAdapter chapterAdapter;
    private ArrayList<Chapter>chapterList=new ArrayList<>();
    private SubscribedCourse subscribedCourse;
    private BookMarkCourse bookMarkCourse;
    private String cType;
    String course_id,course_name;
    String from;
    AssignProfile assignProfile;


    public SubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chapter, container, false);
        bindId(view);
        from=getArguments().getString(FROM);
        course_id=getArguments().getString("course_id");
        course_name=getArguments().getString("course_name");
        if (from.equals(SIMPLEE_HOME_TUTOR)){
            assignProfile= (AssignProfile) getArguments().getSerializable("profileData");
        }
        requestApi();
        refresh_subject.setOnRefreshListener(this);
       //cType=getArguments().getString("type");
     //   setChapter();
        return  view;
    }

    private void requestApi() {
        if (ConnectivityReceiver.isConnected()){
            if ( from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_SUBJECT_LIST);
            }

        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }


    private void setChapter() {
//        if (cType.equals("subscription")){
//                subscribedCourse=getArguments().getParcelable("sCourse");
//            chapterList.clear();
//            chapterList.addAll(subscribedCourse.getChapter());
//        }else if (cType.equals("bookmark")){
//            bookMarkCourse=getArguments().getParcelable("sCourse");
//            chapterList.clear();
//            chapterList.addAll(bookMarkCourse.getChapter());
//        }
        chapterAdapter=new ChapterAdapter(getContext(), chapterList,"student", new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(int position, String text, String chapterId) {

                //((MainActivity)getActivity()).getSupportActionBar().setTitle(text);
                TopicFragment fragment=new TopicFragment();
                //Subject_Chapter_Details_Fragment fragment=new Subject_Chapter_Details_Fragment();
              //  FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
                Bundle arg=new Bundle();
                arg.putString("from",from);
                arg.putString("subject_id",chapterId);
                arg.putString("course_id",course_id);
                arg.putString("course_name",course_name);
                arg.putString("subject_name",text);
                arg.putInt("subjectPosition",position+1);
                //-----------for assign to profile-----//
                //home tutor case only
                arg.putSerializable("profileData",assignProfile);
                //----------------------------------//
                fragment.setArguments(arg);
                if (from.equals(SIMPLEE_HOME_STUDENT))
                ((MainActivity)getActivity()).SwitchFragment(fragment);
                else if (from.equals(SIMPLEE_HOME_TUTOR))
                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);

            }

            @Override
            public void onEdit(int positon) {

            }

            @Override
            public void onDelete(int position) {

            }
        });
        rv_chapter.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_chapter.setAdapter(chapterAdapter);
    }

    private void bindId(View view) {
         rv_chapter=view.findViewById(R.id.rv_chapter);
         refresh_subject=view.findViewById(R.id.refresh_subject);
    }


    @Override
    public void onResume() {
        super.onResume();
       // ((MainActivity)getActivity()).setStudentChildActionBar(subscribedCourse.getTitle(),false);
        ((Subscribed_Course_Details)getParentFragment()).showSubjectBg();
        ((Subscribed_Course_Details)getParentFragment()).showAssignToProfile();
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_SUBJECT_LIST:
                params.put("type","0");
                params.put("course_id",course_id);
                ///params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_SUBJECT_LIST, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_SUBJECT_LIST:
                service.postDataVolley(ApiCode.GET_SUBJECT_LIST,
                        BaseUrl.URL_GET_SUBJECT_LIST, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_SUBJECT_LIST:
                Log.e("subject_list",response);

                if(jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<Chapter> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<ArrayList<Chapter>>() {
                                        }.getType());
                        chapterList.clear();
                        chapterList.addAll(psearch);
                        setChapter();
                        // testAdapter.notifyDataSetChanged();
                    } else {
                        chapterList.clear();
                        setChapter();
                        CommonMethods.showSuccessToast(getContext(), "No Subjects Available");
                    }

                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        refresh_subject.setRefreshing(false);
        requestApi();
    }
}
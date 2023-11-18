package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Interface.ClickListener;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.adapter.StudentAdpter;
import soonflyy.learning.hub.adapter.StudentChatAdapter;
import soonflyy.learning.hub.model.StudentAll;
import soonflyy.learning.hub.model.StudentModel;
import soonflyy.learning.hub.studentModel.StudentChatModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentListFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    RecyclerView rec_student;
    ImageView arrow_back_img;
    TextView tv_title;
    SearchView searchView;

    StudentAdpter adapter;
    String type;
    ArrayList<StudentModel> slist=new ArrayList<>();
    ArrayList<StudentAll>allStudentList=new ArrayList<>();
    List<StudentChatModel>teacherList=new ArrayList<>();
    StudentChatAdapter chatAdapter;
    private String userId;
    private String courseId;
    private String assign_id;
    String listType;

    public StudentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        initview(view);
        getArgumentData(view);

       // getList();
        if (ConnectivityReceiver.isConnected()) {
            if (type.equals("teacher")) {
                if (listType.equals("assignment")) {
                    assign_id = getArguments().getString("assign_id");
                    sendRequest(ApiCode.GET_STUDENT_LIST_BY_ASSIGN_ID);
                } else if (listType.equals("chat")) {
                    sendRequest(ApiCode.STUDENT_LIST_BY_TEACHER);
                } else if (listType.equals("subscription")) {
                    courseId = getArguments().getString("course_id");
                    sendRequest(ApiCode.STUDENT_LIST_BY_COURSE_ID);

                }
            }
            else if (type.equals("student")){
                //write call api for teacher list
                sendRequest(ApiCode.TEACHER_LIST_STUDENT_CHAT);
            }
        }else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
       // if (type.equals("student")){}

        rec_student.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_student, new ClickListener() {

            public void onClick(View view, final int position) {
                if (type.equals("student")) {
                    StudentChatModel model=teacherList.get(position);
                    MessageFragment courseFragment = new MessageFragment();
                    Bundle args = new Bundle();
                    args.putString("from",SIMPLEE_HOME_STUDENT);
                    Log.e ("student_Passdata", "onClick"+type+"--"+model.getUser_image()+"--"+model.getFirst_name() );
                    args.putString("type", type);
                    args.putString("student_id",userId);
                    args.putString("teacher_id",model.getUser_id());
                    args.putString("name",model.getFirst_name());
                    args.putString("profile_image",model.getUser_image());
                    courseFragment.setArguments(args);
                    SwitchFragment(courseFragment);
                } else {
                    StudentChatModel model=teacherList.get(position);
                    Fragment fragment = new MessageFragment ();
                    Bundle args = new Bundle();
                    args.putString("from",SIMPLEE_HOME_TUTOR);
                    args.putString("type",type);
                   // args.putParcelable("studentData", allStudentList.get(position));
                    args.putString("teacher_id", userId);
                    args.putString("student_id",model.getUser_id());
                    args.putString("name",model.getFirst_name());
                    args.putString("profile_image",model.getUser_image());
                    fragment.setArguments(args);
                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);


                }

            }

            @Override
            public void onLongClick(View view, int position) {
                return;
            }
        }));

        return view;
    }



    private void getArgumentData(View view) {
        type = getArguments().getString("type");
        userId = getArguments().getString("user_id");

        if (type.equals("teacher")) {
            listType = getArguments().getString("listType");

        }

    }

    private void getStudentList() {
        if (listType !=null && (listType.equals("subscription")  || listType.equals("chat"))){
            adapter = new StudentAdpter(getActivity(),null, allStudentList, type,listType);
        }else{
            adapter = new StudentAdpter(getActivity(),slist, null, type,listType);
        }

        rec_student.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initview(View view) {
        slist = new ArrayList<>();
        searchView=view.findViewById(R.id.search_view);
        CommonMethods.setSearchViewColor(getActivity(),searchView);
        rec_student = view.findViewById(R.id.rec_discussions);
        rec_student.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_title = view.findViewById(R.id.tv_title);
        arrow_back_img = view.findViewById(R.id.iv_back);
        arrow_back_img.setOnClickListener(this);
        tv_title.setText("Student List");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (chatAdapter!=null){
                    chatAdapter.getFilter().filter(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (chatAdapter!=null){
                    chatAdapter.getFilter().filter(newText.trim());
                }
                return false;
            }
        });
searchView.requestFocus();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                gotoBack();
                break;
        }

    }

    private void gotoBack() {
        try {
            if (type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).onBackPressed();

            } else if (type.equals("student")){
                ((MainActivity) getActivity()).onBackPressed();
            }
        }catch (Exception e){

        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment);//, ProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){

            case ApiCode.STUDENT_LIST_BY_COURSE_ID :
                params.put("course_id",courseId);
                callApi(ApiCode.STUDENT_LIST_BY_COURSE_ID, params);
                break;
            case ApiCode.STUDENT_LIST_BY_TEACHER :
                params.put("user_id",userId);
                callApi(ApiCode.STUDENT_LIST_BY_TEACHER, params);
                break;
            case ApiCode.TEACHER_LIST_STUDENT_CHAT :
                params.put("user_id",userId);
                callApi(ApiCode.TEACHER_LIST_STUDENT_CHAT, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){

            case ApiCode.STUDENT_LIST_BY_COURSE_ID:
                service.postDataVolley(ApiCode.STUDENT_LIST_BY_COURSE_ID,
                        BaseUrl.URL_STUDENT_LIST_BY_COURSE_ID, params);
                break;
            case ApiCode.STUDENT_LIST_BY_TEACHER:
                service.postDataVolley(ApiCode.STUDENT_LIST_BY_TEACHER,
                        BaseUrl.URL_STUDENT_LIST_BY_TEACHER, params);
                break;
            case ApiCode.TEACHER_LIST_STUDENT_CHAT:
                service.postDataVolley(ApiCode.TEACHER_LIST_STUDENT_CHAT,
                        BaseUrl.URL_TEACHER_LIST_STUDENT_CHAT, params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) {
        switch (requestType){
            case ApiCode.STUDENT_LIST_BY_COURSE_ID:
            //case ApiCode.STUDENT_LIST_BY_TEACHER:
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                       if (jsonArray.length()>0) {
                            ArrayList<StudentAll>list=new Gson().
                                    fromJson(jsonArray.toString(),
                                            new TypeToken<List<StudentAll>>() {
                                            }.getType());
                            allStudentList.clear();
                            allStudentList.addAll(list);
                           getStudentList();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ApiCode.TEACHER_LIST_STUDENT_CHAT:
            case ApiCode.STUDENT_LIST_BY_TEACHER:
                Log.e("chat_teacher",response);
                try {
                    teacherList.clear();
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                StudentChatModel model = new StudentChatModel();
                                JSONObject OBJ = jsonArray.getJSONObject(i);
                                model.setUser_id(OBJ.getString("user_id"));
                                model.setFirst_name(OBJ.getString("first_name"));
                                model.setUser_image(OBJ.getString("user_image"));
                                JSONObject ob = OBJ.getJSONObject("chats");
                                if (ob.length() > 0) {
                                    model.setMessage_text(ob.getString("message_text"));
                                    model.setIs_read(ob.getString("is_read"));
                                    model.setUnread_count(ob.getString("unread_count"));
                                } else {
                                    model.setMessage_text("");
                                    model.setIs_read("");
                                    model.setUnread_count("0");
                                }
                                teacherList.add(model);
                            }
                            setTeacherList();


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setTeacherList(){
        rec_student.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter=new StudentChatAdapter(getContext(), teacherList);
        rec_student.setAdapter(chatAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (type.equals("teacher")) {
            ((TeacherMainActivity) getActivity()).getSupportActionBar().hide();
            //((TeacherMainActivity) getActivity()).setChildActionBar("Discussion");//student list
            //((TeacherMainActivity) getActivity()).setTeacherActionBar("Discussion",false);
        } else if (type.equals("student")){
            ((MainActivity) getActivity()).getSupportActionBar().hide();
            //((MainActivity)getActivity()).setStudentChildActionBar("Discussion",false);//teacher list
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (type.equals("teacher")) {
                ((TeacherMainActivity) getActivity()).getSupportActionBar().show();

            } else if (type.equals("student")){
                ((MainActivity) getActivity()).getSupportActionBar().show();
                     }
        }catch (Exception e){

        }
    }
}
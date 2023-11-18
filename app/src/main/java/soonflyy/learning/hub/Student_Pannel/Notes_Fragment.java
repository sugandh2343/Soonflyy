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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Adapters.Notes_Adapter;
import soonflyy.learning.hub.Common_Package.Models.Notes_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notes_Fragment extends Fragment implements VolleyResponseListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rec_notes;
    ArrayList<Notes_Model>noteList=new ArrayList<>();
    Notes_Adapter notesAdapter;

    String chapter_id,subject_id,course_id;

    String teacher_id,teacher_name,class_id,section_id,from;
    SwipeRefreshLayout refreshLayout;

    public Notes_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notes_, container, false);
        bindViewId(view);
        getArgumentData();
        sendApiRequest();
       // setNote();

        return view;
    }

    private void sendApiRequest() {
        if(ConnectivityReceiver.isConnected())
            if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
                sendRequest(ApiCode.SCHOOL_GET_NOTES);
            }else if (from.equals(SIMPLEE_HOME_STUDENT)||from.equals(SIMPLEE_HOME_TUTOR)) {
                sendRequest(ApiCode.GET_CHAPTER_NOTE);
            }
        else
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");

    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        subject_id=getArguments().getString("subject_id");
        chapter_id=getArguments().getString("chapter_id");
        if (from.equals(SCHOOL_COACHING)){
            teacher_name=getArguments().getString("teacher_name");
            class_id =getArguments().getString("class_id");
            section_id=getArguments().getString("section_id");
            teacher_id=getArguments().getString("teacher_id");
        }else if (from.equals(SIMPLEE_HOME_STUDENT)) {
            course_id = getArguments().getString("course_id");
        }else if (from.equals(SIMPLEE_HOME_TUTOR)){
            //code for tutor assign to
        }
    }


    private void setNote() {
        notesAdapter=new Notes_Adapter(getContext(), noteList, new Notes_Adapter.OnNoteClickListener() {
            @Override
            public void onClick(int position, String url) {
                String link=null;
                if (from.equals(SCHOOL_COACHING)|| from.equals(SCHOOL_STUDENT)) {
                    link=BaseUrl.SCHOOL_BASE_URL_MEDIA+noteList.get(position).getFile();
                }else if (from.equals(SIMPLEE_HOME_STUDENT)){
                    link=url;
                }
                new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(),link,"Note",
                        false,"");
            }
        });
        rec_notes.setAdapter(notesAdapter);
        notesAdapter.notifyDataSetChanged();
    }

    private void bindViewId(View view) {
        rec_notes=view.findViewById(R.id.rec_notes);
        refreshLayout=view.findViewById(R.id.refresh_note);
        refreshLayout.setOnRefreshListener(this);
        rec_notes.setLayoutManager(new GridLayoutManager(getActivity(),3));
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_CHAPTER_NOTE:
                // if (liveType.equals("course_wise")) {
                params.put("chapter_id", chapter_id);
                params.put("section_id", subject_id);
//
                callApi(ApiCode.GET_CHAPTER_NOTE, params);
                break;
            case ApiCode.SCHOOL_GET_NOTES:
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_NOTES, params);
                break;
        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.GET_CHAPTER_NOTE:
                service.postDataVolley(ApiCode.GET_CHAPTER_NOTE,
                        BaseUrl.URL_GET_CHAPTER_NOTE, params);
                break;
            case ApiCode.SCHOOL_GET_NOTES:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTES,
                        BaseUrl.URL_SCHOOL_GET_NOTES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTES);
                Log.e("params", params.toString());
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        switch (requestType){
            case ApiCode.GET_CHAPTER_NOTE:
            case ApiCode.SCHOOL_GET_NOTES:
                Log.e("note ",response);
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.getBoolean("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        ArrayList<Notes_Model> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<Notes_Model>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        setNote();
                       // notesAdapter.notifyDataSetChanged();
                    }else{
                        noteList.clear();
                        setNote();
                        //notesAdapter.notifyDataSetChanged();
                        CommonMethods.showSuccessToast(getContext(),"Notes not available");
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
        ((Subject_Chapter_Details_Fragment)getParentFragment()).setNotesBackground();
        ((Subject_Chapter_Details_Fragment)getParentFragment()).showAssignToProfile();

    }
}
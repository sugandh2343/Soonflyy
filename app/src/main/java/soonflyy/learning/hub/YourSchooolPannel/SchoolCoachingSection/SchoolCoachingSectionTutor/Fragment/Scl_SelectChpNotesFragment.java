package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolChapterNotesAdapter;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolChapterNotesModel;
import soonflyy.learning.hub.utlis.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Scl_SelectChpNotesFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    RecyclerView rec_notes;
    private FloatingActionButton feb_notes;
    CardView cv_create;
    TextView tv_add,tvSelectedPath;
    SwipeRefreshLayout swipe;
    RelativeLayout rel_no_live, rel_showlist;

    ArrayList<SchoolChapterNotesModel> noteList = new ArrayList<>();
    SchoolChapterNotesAdapter notesAdapter;

    String teacher_id,subject_id,chapter_id,from,note_id;
    String pdfFileString = "",noteName="";


    public Scl_SelectChpNotesFragment() {
        // Required empty public constructor
    }

    
    public static Scl_SelectChpNotesFragment newInstance(String param1, String param2) {
        Scl_SelectChpNotesFragment fragment = new Scl_SelectChpNotesFragment();
        
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
        View view= inflater.inflate(R.layout.fragment_scl__select_chp_notes, container, false);
        initView(view);
        getArgumentData();
        initRecyclerView();
        sendAPicCAll();
        initControl();
        //initFloatingButton();
       // initRecyclerView();
        init_swipe_method();
        tv_add.setOnClickListener(this);
        feb_notes.setOnClickListener(this);
        rel_no_live.setOnClickListener(this);
        cv_create.setOnClickListener(this);

        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
        teacher_id=getArguments().getString("teacher_id");
        chapter_id=getArguments().getString("chapter_id");
        subject_id=getArguments().getString("subject_id");


    }

    private void initView(View view) {
        cv_create=view.findViewById(R.id.card_create_course);
        swipe = view.findViewById(R.id.swipe);
        rec_notes = view.findViewById(R.id.rec_notes);
        feb_notes = view.findViewById(R.id.feb_notes);
        tv_add = view.findViewById(R.id.tv_discription);
        rel_no_live=view.findViewById(R.id.rel_no_live);
        rel_showlist=view.findViewById(R.id.rel_showlist);

    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                sendAPicCAll();
               // initFloatingButton();
                initControl();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendAPicCAll() {
       if (CommonMethods.checkInternetConnection(getActivity())){
           sendRequest(ApiCode.SCHOOL_GET_NOTES);
       }
    }
    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

//    private void initFloatingButton() {
//        rel_no_live.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showNotesList();
//            }
//        });
//    }


    private void initRecyclerView() {

//        noteList.add(new SchoolChapterNotesModel());
//        noteList.add(new SchoolChapterNotesModel());
//        noteList.add(new SchoolChapterNotesModel());

        rec_notes.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rec_notes.setKeepScreenOn(true);
        notesAdapter = new SchoolChapterNotesAdapter(getContext(), noteList, new SchoolChapterNotesAdapter.OnNoteClickListener() {
            @Override
            public void onItemClick(int position) {
                String link=BaseUrl.BASE_URL_MEDIA+noteList.get(position).getFile();
                Log.e("link",""+link);
                new CommonMethods().viewAndDownLoadPdfFrormUrl(getActivity(),link,"Note",
                        false,"");
            }

            @Override
            public void onDelete(int position) {
                note_id=noteList.get(position).getId();
                showDeleteDialog();

            }
        });
        rec_notes.setAdapter(notesAdapter);
        if (noteList.size() == 0) {
            rel_no_live.setVisibility(View.VISIBLE);
            rel_showlist.setVisibility(View.GONE);

        } else {
            rel_no_live.setVisibility(View.GONE);
            rel_showlist.setVisibility(View.VISIBLE);
            rec_notes.setAdapter(notesAdapter);
            notesAdapter.notifyDataSetChanged();
        }


    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setTitle("Delete")
                .setMessage("Are you sure to delete?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if (CommonMethods.checkInternetConnection(getActivity())){
                   sendRequest(ApiCode.SCHOOL_DELETE_NOTES);
               }
                dialog.dismiss();
            }
        }).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.feb_notes:
            case R.id.rel_no_live:
            case R.id.card_create_course:
            case R.id.tv_discription:
                showNotesList();
                break;
        }
    }
    private void showNotesList() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_teacher_notes);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        tvSelectedPath=dialog.findViewById(R.id.tv_seleted_path);
        ImageView iv_sub = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        Button btn_upload = dialog.findViewById(R.id.btn_upload);

        dialog.show();
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePdf();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteName=et_name.getText().toString().trim();
                if (TextUtils.isEmpty(noteName)) {
                    et_name.setError("Enter note name");
                    et_name.requestFocus();
                }else
                if (TextUtils.isEmpty(pdfFileString)){
                    CommonMethods.showSuccessToast(getContext(),"Select Pdf");
                }else{
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        sendRequest(ApiCode.SCHOOL_ADD_NOTES);
                        dialog.dismiss();
                    }
                }
                dialog.dismiss();

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }

    private void choosePdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setType("application/pdf");//*/*
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 222);//intent
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 222:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    pdfFileString = CommonMethods.convertToBase64String(uri, getContext());
                    String path = FileHelper.getRealPathFromURI(getActivity(),uri);
                    Log.e("pdfPath",""+path);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(path);
                    Log.e("Name",""+fileName);
                    tvSelectedPath.setText("Pdf: "+fileName);
                    //CommonMethods.showSuccessToast(getActivity(),"File Selected");
                }
                break;
        }

    }



    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_NOTES:
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_NOTES, params);
                break;
            case ApiCode.SCHOOL_ADD_NOTES:
                params.put("title","notes");
                params.put("file",pdfFileString);
                params.put("teacher_id", teacher_id);
                params.put("chapter_id", chapter_id);
                params.put("subject_id", subject_id);
                params.put("file_name",noteName);
                callApi(ApiCode.SCHOOL_ADD_NOTES, params);
                break;
            case ApiCode.SCHOOL_UPDATE_NOTES:
                params.put("title","notes");
                params.put("file",pdfFileString);
                params.put("teacher_id", teacher_id);
                params.put("chapter_id", chapter_id);
                params.put("subject_id", subject_id);
                params.put("notes_id", note_id);
                callApi(ApiCode.SCHOOL_UPDATE_NOTES, params);
                break;
            case ApiCode.SCHOOL_DELETE_NOTES:
                params.put("notes_id", note_id);
                callApi(ApiCode.SCHOOL_DELETE_NOTES, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_NOTES:
                service.postDataVolley(ApiCode.SCHOOL_GET_NOTES,
                        BaseUrl.URL_SCHOOL_GET_NOTES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_NOTES);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_NOTES:
                service.postDataVolley(ApiCode.SCHOOL_ADD_NOTES,
                        BaseUrl.URL_SCHOOL_ADD_NOTES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_NOTES);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_NOTES:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_NOTES,
                        BaseUrl.URL_SCHOOL_UPDATE_NOTES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_NOTES);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_NOTES:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_NOTES,
                        BaseUrl.URL_SCHOOL_DELETE_NOTES, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_NOTES);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_NOTES:
                Log.e("sc_get_note", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<SchoolChapterNotesModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<SchoolChapterNotesModel>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        initRecyclerView();
                    }else{
                        noteList.clear();
                        initRecyclerView();
                    }
                } else {
                    noteList.clear();
                    initRecyclerView();
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_NOTES:
                Log.e("sc_add_note", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTES);

                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_NOTES:
                Log.e("sc_up_note", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTES);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_NOTES:
                Log.e("delete_note", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_NOTES);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((Scl_SelectChpterDeatailFragment)getParentFragment()).setNoteBgColor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
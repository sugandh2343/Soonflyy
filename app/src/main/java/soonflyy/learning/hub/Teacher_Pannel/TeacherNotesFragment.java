package soonflyy.learning.hub.Teacher_Pannel;

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
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_NotesAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_Notes;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.FileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TeacherNotesFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {
    RecyclerView rec_notes;
    private FloatingActionButton feb_notes;
    TextView tv_add,tvSelectedPath;
    SwipeRefreshLayout swipe;
    CardView cv_card_create;
    RelativeLayout rel_no_live, rel_showlist;
    ArrayList<T_Notes> noteList = new ArrayList<>();
    T_NotesAdapter notesAdapter;


    String course_id, chapter_id, section_id, note_id;
    String pageTitle;
    String pdfFileString = "";
    String noteName="";


    public TeacherNotesFragment() {
        // Required empty public constructor
    }

    public static TeacherNotesFragment newInstance(String param1, String param2) {
        TeacherNotesFragment fragment = new TeacherNotesFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_teacher_notes, container, false);
        initView(view);
        getArgumentData();
        sendAPicCAll();
        init_swipe_method();
        tv_add.setOnClickListener(this);
        feb_notes.setOnClickListener(this);
        cv_card_create.setOnClickListener(this);
        return view;
    }

    private void getArgumentData() {
        course_id = getArguments().getString("course_id");
        chapter_id = getArguments().getString("chapter_id");
        section_id = getArguments().getString("section_id");
        pageTitle = getArguments().getString("course_title");
    }

    private void initView(View view) {
        cv_card_create=view.findViewById(R.id.card_create_course);
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

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendAPicCAll() {
        if (ConnectivityReceiver.isConnected()) {
            sendRequest(ApiCode.GET_CHAPTER_NOTE);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void initRecyclerView() {


        rec_notes.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rec_notes.setKeepScreenOn(true);
        notesAdapter = new T_NotesAdapter(getContext(), noteList, new T_NotesAdapter.OnNoteClickListener() {
            @Override
            public void onItemClick(int position) {
                String link=BaseUrl.BASE_URL_MEDIA+noteList.get(position).getFile();
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
            ///rec_notes.setAdapter(adapter);
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
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.DELETE_NOTES);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
                dialog.dismiss();
            }
        }).show();
    }

    private void initControl() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            ///showAboutFragment();
        }
    }

    private void initFloatingButton() {
        rel_no_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotesList();
            }
        });
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
                }else if (TextUtils.isEmpty(pdfFileString)){
                    CommonMethods.showSuccessToast(getContext(),"Select Pdf");
                }else{
                    if (ConnectivityReceiver.isConnected()){
                        sendRequest(ApiCode.ADD_NOTES);
                        dialog.dismiss();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }
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
                }
                break;
        }

    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.GET_CHAPTER_NOTE:
                params.put("chapter_id", chapter_id);
                params.put("section_id", section_id);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_CHAPTER_NOTE, params);
                break;
            case ApiCode.ADD_NOTES:
                params.put("chapter_id", chapter_id);
                params.put("section_id", section_id);
                params.put("course_id", course_id);
                params.put("file", pdfFileString);
                params.put("file_name",noteName);
                callApi(ApiCode.ADD_NOTES, params);
                break;
            case ApiCode.DELETE_NOTES:
                params.put("note_id", note_id);
                callApi(ApiCode.DELETE_NOTES, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_CHAPTER_NOTE:
                service.postDataVolley(ApiCode.GET_CHAPTER_NOTE,
                        BaseUrl.URL_GET_CHAPTER_NOTE, params);
                break;
            case ApiCode.ADD_NOTES:
                service.postDataVolley(ApiCode.ADD_NOTES,
                        BaseUrl.URL_ADD_NOTES, params);
                break;
            case ApiCode.DELETE_NOTES:
                service.postDataVolley(ApiCode.DELETE_NOTES,
                        BaseUrl.URL_DELETE_NOTES, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.ADD_NOTES:
                Log.e("note_add ", response);
                if (jsonObject.getBoolean("status")) {
                    pdfFileString = "";
                    CommonMethods.showSuccessToast(getContext(), "Note Added Successfully");
                    sendAPicCAll();
                }
                break;
            case ApiCode.DELETE_NOTES:
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Note Deleted Successfully");
                    sendAPicCAll();
                }
                Log.e("delete_note ", response);
                break;

            case ApiCode.GET_CHAPTER_NOTE:
                Log.e("note ", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<T_Notes> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<T_Notes>>() {
                                        }.getType());
                        noteList.clear();
                        noteList.addAll(psearch);
                        initRecyclerView();
                        // notesAdapter.notifyDataSetChanged();
                    } else {
                        noteList.clear();
                        initRecyclerView();
                        //notesAdapter.notifyDataSetChanged();
                        //  CommonMethods.showSuccessToast(getContext(),"Notes not available");
                    }
                }else{
                    noteList.clear();
                    initRecyclerView();
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_discription:
            case R.id.feb_notes:
            case R.id.card_create_course:
                showNotesList();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle,false);
        ((T_ChapterDetailFragment)getParentFragment()).setNoteBgColor();
        ((T_ChapterDetailFragment)getParentFragment()).showAssignProfile();

    }
}
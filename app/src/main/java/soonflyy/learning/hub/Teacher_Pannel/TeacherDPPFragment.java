package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.TeacherDPPAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.TeacherDPPModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.FileHelper;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class TeacherDPPFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {
    RecyclerView rec_dpp;
    private FloatingActionButton feb_dpp;
    CardView cv_create;
    TextView tv_add,tvSelectedPath;
    SwipeRefreshLayout swipe;
    RelativeLayout rel_no_live,rel_showlist;
    TeacherDPPAdapter adapter;
    ArrayList<TeacherDPPModel> modellist=new ArrayList<>();

    String fileString,dpp_title,dpp_date,dpp_mark;
    String course_id,section_id,chapter_id,dpp_id,pageTitle;




    public TeacherDPPFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teacher_d_p_p, container, false);
        initView(view);
        getArgumentData();
        init_swipe_method();
        sendApiCall();

        //initRecyclerView();

        feb_dpp.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        cv_create.setOnClickListener(this);
        return view;
    }

    private void getArgumentData() {
        course_id=getArguments().getString("course_id");
        chapter_id=getArguments().getString("chapter_id");
        section_id=getArguments().getString("section_id");
        pageTitle = getArguments().getString("course_title");
    }

    private void initView(View view) {
        cv_create=view.findViewById(R.id.card_create_course);
        swipe=view.findViewById(R.id.swipe);
        rec_dpp=view.findViewById(R.id.rec_dpp);
        feb_dpp= view.findViewById(R.id.feb_dpp);
        tv_add=view.findViewById(R.id.tv_discription);
        rel_no_live=view.findViewById(R.id.rel_no_live);
        rel_showlist=view.findViewById(R.id.rel_showlist);
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                sendApiCall();
                // initControl();
//                initRecyclerView();
//                initFloatingButton();
//                swipe.setRefreshing(false);
            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange,R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendApiCall() {
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.GET_DPP_BY_ID);
        }else{
            CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
        }
    }

    private void initRecyclerView() {


      //  rec_dpp.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_dpp.setLayoutManager(linearLayoutManager);
        rec_dpp.setKeepScreenOn(true);

        adapter = new TeacherDPPAdapter(getContext(), modellist, new TeacherDPPAdapter.OnCourseClickListener() {
            @Override
            public void onItemClick(int postion) {
                T_DPPTopicDeatilFragment fragment=new T_DPPTopicDeatilFragment();
                Bundle bundle=new Bundle();
                bundle.putString("course_id",course_id);
                bundle.putString("section_id",section_id);
                bundle.putString("chapter_id",chapter_id);
                bundle.putString("title",modellist.get(postion).getTopic());
                bundle.putString("dpp_id",modellist.get(postion).getDpp_id());
                bundle.putString("course_title",pageTitle);
                fragment.setArguments(bundle);
                ((T_ChapterDetailFragment)getParentFragment()).SwitchFragment(fragment);

            }


            @Override
            public void onDelete(int position) {
                dpp_id=modellist.get(position).getDpp_id();
                showAlertDialog();

            }

            @Override
            public void onEdit(int position) {

            }


        });

        if (modellist.size()==0){
            rel_no_live.setVisibility(View.VISIBLE);
            rel_showlist.setVisibility(View.GONE);

        } else {
            rel_no_live.setVisibility(View.GONE);
            rel_showlist.setVisibility(View.VISIBLE);
            rec_dpp.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Delete")
                .setCancelable(false)
                .setMessage("Are you sure to delete ?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.DELETE_DPP);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
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
                showDPPDailoge();
            }
        });
    }

    private void showDPPDailoge() {

        Dialog dialog = new Dialog (getActivity());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.dailoge_teacherdpp);
        dialog.getWindow ();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
        dialog.show ( );


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        ImageView iv_sub = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_back);
        tvSelectedPath = dialog.findViewById(R.id.tv_selected_path);
        EditText et_mark=dialog.findViewById(R.id.et_marks);
        TextView tv_last_date=dialog.findViewById(R.id.et_lastdate);
        Button btn_upload =dialog.findViewById(R.id.btn_upload);



        dialog.show();
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpp_title=et_name.getText().toString().trim();
                dpp_date=tv_last_date.getText().toString().trim();
                dpp_mark=et_mark.getText().toString().trim();
                if (validateField()){
                    if (ConnectivityReceiver.isConnected()){
                        //call api
                        sendRequest(ApiCode.ADD_DPP);
                        dialog.dismiss();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }

            }
        });
        tv_last_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // chooseDate(tv_last_date);
                showDatePicker(tv_last_date);
            }
        });
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePdf();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside (false);
    }

    private boolean validateField() {
        if (TextUtils.isEmpty(dpp_title)) {
            CommonMethods.showSuccessToast(getContext(),"Enter title");
            return false;
        }
        if (TextUtils.isEmpty(fileString)) {
            CommonMethods.showSuccessToast(getContext(),"Choose file");
            return false;
        }
        if (TextUtils.isEmpty(dpp_mark)) {
            CommonMethods.showSuccessToast(getContext(),"Enter mark");
            return false;
        }
        if (TextUtils.isEmpty(dpp_date)) {
            CommonMethods.showSuccessToast(getContext(),"Choose date");
            return false;
        }
        return true;
    }


    private void chooseDate(TextView tv_date) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
               /* String month= String.valueOf(i1+1);
                if (month.length()==1){
                    month="0"+month;
                }
                String day=String.valueOf(i2);
                if (day.length()==1){
                    day="0"+day;
                }
                String date =day+"/"+month+"/"+i;
                et_date.setText(date);

                */
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());
                tv_date.setText(date);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }
    private void showDatePicker(TextView tv_date) {
        boolean isValidDate=false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_divider_color));

        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month, int day, int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());

                tv_date.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                String d=tv_date.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tv_date.setText("");
//                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"Please select a date after current date");
                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidExpiryDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
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
                    fileString = CommonMethods.convertToBase64String(uri, getContext());
                    String path = FileHelper.getRealPathFromURI(getActivity(),uri);
                    Log.e("pdfPath",""+path);
                    String fileName=CommonMethods.getLastSegmentOfFilePath(path);
                    Log.e("Name",""+fileName);
                    tvSelectedPath.setText("Pdf: "+fileName);
                   // CommonMethods.showSuccessToast(getActivity(),"File Selected");
                }
                break;
        }

    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.ADD_DPP:
                params.put("course_id", course_id);
                params.put("lesson_id", chapter_id);
                params.put("section_id", section_id);
                params.put("topic", dpp_title);
                params.put("last_date", dpp_date+" | 08:00 pm");
                params.put("marks", dpp_mark);
                params.put("paper", fileString);
                 params.put("teacher_id",new SessionManagement(getContext()).getString(USER_ID));
                callApi(ApiCode.ADD_DPP, params);
                break;
            case ApiCode.DELETE_DPP:
                params.put("dpp_id", dpp_id);
                callApi(ApiCode.DELETE_DPP, params);
                break;
            case ApiCode.GET_DPP_BY_ID:
                params.put("course_id", course_id);
                params.put("lesson_id", chapter_id);
                params.put("section_id", section_id);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.GET_DPP_BY_ID, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.ADD_DPP:
                service.postDataVolley(ApiCode.ADD_DPP,
                        BaseUrl.URL_ADD_DPP, params);
                break;
            case ApiCode.DELETE_DPP:
                service.postDataVolley(ApiCode.DELETE_DPP,
                        BaseUrl.URL_DELETE_DPP, params);
                break;
            case ApiCode.GET_DPP_BY_ID:
                service.postDataVolley(ApiCode.GET_DPP_BY_ID,
                        BaseUrl.URL_GET_DPP_BY_ID, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.ADD_DPP:
                Log.e("dpp_add ", response);
                if (jsonObject.getBoolean("response")) {
                    fileString = "";
                    dpp_title="";
                    dpp_date=" ";
                    CommonMethods.showSuccessToast(getContext(), "DPP Added Successfully");
                   sendApiCall();
                }
                break;
            case ApiCode.DELETE_DPP:
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "DPP Deleted Successfully");
                    sendApiCall();
                }
                Log.e("delete_note ", response);
                break;

            case ApiCode.GET_DPP_BY_ID:
                Log.e("Dpplist", response);

                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        ArrayList<TeacherDPPModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<TeacherDPPModel>>() {
                                        }.getType());
                        modellist.clear();
                        modellist.addAll(psearch);
                        initRecyclerView();
                        // notesAdapter.notifyDataSetChanged();
                    } else {
                        modellist.clear();
                        initRecyclerView();
                        //notesAdapter.notifyDataSetChanged();
                        //  CommonMethods.showSuccessToast(getContext(),"Notes not available");
                    }
                }else{
                    modellist.clear();
                    initRecyclerView();
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feb_dpp:
            case R.id.tv_discription:
            case R.id.card_create_course:
                Log.e("dppclick","click");
                showDPPDailoge();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle,false);
        ((T_ChapterDetailFragment)getParentFragment()).setDppBgColor();
        ((T_ChapterDetailFragment)getParentFragment()).showAssignProfile();

    }
}
package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.CommonMethods.getCurrentTime;
import static soonflyy.learning.hub.Common.CommonMethods.isBeforeDate;
import static soonflyy.learning.hub.Common.CommonMethods.isCurrentDate;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_LiveAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.live.MeetingActivity;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ScheduleLiveFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    //    private RecyclerView rv_live;
    private RelativeLayout bnt_scheduleLive, rel_live;
    TextView tv_live;

    //    private CardView card_live_list;
    private SessionManagement management;
    MyCourseDetailModel courseModel;

    private String startTime, endTime, liveDate;
    private String liveClassId;
    private FloatingActionButton feb_shedule;
    SwipeRefreshLayout swipe;
    RelativeLayout rel_no_live, rel_showclass;
    RecyclerView rec_shedule;
    CardView live_create_card;

    ///
    ImageView choose_image, cover_image;
    RelativeLayout rel_image;
    CircleImageView cancel_image;
    TextView tv_imageName;
    String imageString = "";
    String url;

    ArrayList<T_LiveModel> liveArrayList = new ArrayList<>();
    T_LiveAdapter tLiveAdapter;
    String lTitle, lDate, lSTime, lETime, lDescription, is_demo_live = "1";
    String course_id, chapter_id, section_id, live_id;

    String pageTitle;


    public ScheduleLiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shedule_live, container, false);
        bindId(view);
        getArgumentsData();
        init_swipe_method();
        management = new SessionManagement(getActivity());
       // sendApiRequest();

        bnt_scheduleLive.setOnClickListener(this);
        feb_shedule.setOnClickListener(this);
//        card_live_list.setOnClickListener(this);
        tv_live.setOnClickListener(this);
        rel_live.setOnClickListener(this);
        live_create_card.setOnClickListener(this);

        return view;
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                sendApiRequest();

            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void sendApiRequest() {
        rec_shedule.setLayoutManager(new LinearLayoutManager(getContext()));
        if (ConnectivityReceiver.isConnected()) {
            //call api
            sendRequest(ApiCode.GET_LIVE_CLASSES);
        } else {
            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
        }
    }

    private void setLive() {
        tLiveAdapter = new T_LiveAdapter(getContext(), liveArrayList, "all", new T_LiveAdapter.OnLiveClickListener() {
            @Override
            public void onLiveClick(int position) {
                //golive
//                if (CommonMethods.checkAudioCameraPermission(getContext())) {
//                    T_LiveModel model=liveArrayList.get(position);
//                    LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("live_type","chapter");
//                    bundle.putString("type","teacher");
//                    bundle.putString("slug",model.getSlug());
//                    bundle.putString("description",model.getDescription());
//                    fragment.setArguments(bundle);
//                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
//
//                }else{
//                    CommonMethods.requestAudioCameraPermission(getActivity(),333);
//                }

            }

            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onDelete(int position) {
                liveClassId = liveArrayList.get(position).getId();
                showDeleteDialog();

            }

            @Override
            public void onEdit(int position) {
                T_LiveModel model = liveArrayList.get(position);
                url = model.getCover_image();
                live_id = model.getId();
                lTitle = model.getTitle();
                lDate = model.getDate();
                lSTime = model.getStart_time();
                lETime = model.getEnd_time();
                is_demo_live = model.getIs_demo();
                lDescription = model.getDescription();
                scheduleDialog("update");

            }

            @Override
            public void onGoLive(int position) {
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
                    T_LiveModel model = liveArrayList.get(position);
//                    LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("live_type","chapter");
//                    bundle.putString("type","teacher");
//                    bundle.putString("slug",model.getSlug());
//                    bundle.putString("description",model.getDescription());
//                    fragment.setArguments(bundle);
//                    ((TeacherMainActivity)getActivity()).SwitchFragment(fragment);
                   // Intent lIntent = new Intent(getActivity(), LiveClassesActivity.class);
                    Intent lIntent=new Intent(getActivity(), MeetingActivity.class);
                    lIntent.putExtra("title", model.getTitle());
                    lIntent.putExtra("sTime", model.getStart_time());
                    lIntent.putExtra("eTime", model.getEnd_time());
                    lIntent.putExtra("slug", model.getSlug());
                    lIntent.putExtra("live_type", "chapter");
                    lIntent.putExtra("type", "teacher");
                    lIntent.putExtra("live_id",model.getId());
                    lIntent.putExtra("from",SIMPLEE_HOME_TUTOR);
                    lIntent.putExtra("description", model.getDescription());
                    getActivity().startActivity(lIntent);

                } else {
                    CommonMethods.requestAudioCameraPermission(getActivity(), 333);
                }

            }
        });
        rec_shedule.setAdapter(tLiveAdapter);
        if (tLiveAdapter.getItemCount() > 0) {
            rel_showclass.setVisibility(View.VISIBLE);
            bnt_scheduleLive.setVisibility(View.GONE);
        } else {
            rel_showclass.setVisibility(View.GONE);
            bnt_scheduleLive.setVisibility(View.VISIBLE);
        }
    }


    private void scheduleDialog(String type) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_live_class_shedule);
        dialog.getWindow();
        dialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_title = dialog.findViewById(R.id.et_name);
        EditText et_description = dialog.findViewById(R.id.et_discription);
        choose_image = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_date = dialog.findViewById(R.id.et_date);
        TextView tv_sTime = dialog.findViewById(R.id.et_time1);
        TextView tv_eTime = dialog.findViewById(R.id.et_time2);
        Button save_btn = dialog.findViewById(R.id.btn_save);
        rel_image = dialog.findViewById(R.id.rel_cover_image);
        cancel_image = dialog.findViewById(R.id.cancel_image);
        cover_image = dialog.findViewById(R.id.image_view);
        tv_imageName = dialog.findViewById(R.id.tv_img_name);
        Switch btn_switch = dialog.findViewById(R.id.demo_switch);

        if (type.equals("update")) {
            et_title.setText(lTitle);
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + url)
                    .placeholder(R.drawable.logoo).into(cover_image);
            tv_imageName.setText("cover_image.jpeg");
            choose_image.setVisibility(View.GONE);
            rel_image.setVisibility(View.VISIBLE);
            tv_date.setText(lDate);
            tv_sTime.setText(lSTime);
            tv_eTime.setText(lETime);
            et_description.setText(lDescription);
            if (is_demo_live.equals("0")) {
                btn_switch.setChecked(false);
            } else {
                btn_switch.setChecked(true);
            }

        } else {
            live_id = "";
        }

        dialog.show();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lTitle = et_title.getText().toString();
                lDate = tv_date.getText().toString().trim();
                lSTime = tv_sTime.getText().toString().trim();
                lETime = tv_eTime.getText().toString().trim();
                lDescription = et_description.getText().toString().trim();

                if (validateLiveField(type)) {
                    //cal api
                    if (ConnectivityReceiver.isConnected()) {
                        if (type.equals("add"))
                            sendRequest(ApiCode.ADD_LIVE_CLASS);
                        else
                            //update
                            sendRequest(ApiCode.UPDATE_LIVE_CLASS);
                        dialog.dismiss();
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");

                    }
                }

                //dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_image.setVisibility(View.GONE);
                choose_image.setVisibility(View.VISIBLE);
                tv_imageName.setText("Upload your file here,Choose file form your device");
            }
        });
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        tv_sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoTimePicker(1, tv_sTime);
            }
        });
        tv_eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoTimePicker(0, tv_eTime);
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDatePicker(tv_date);
               //chooseDate(tv_date);
            }
        });
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_switch.isChecked()) {
                    is_demo_live = "1";
                } else {
                    is_demo_live = "0";
                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
    }

    private boolean validateLiveField(String type) {
        if (TextUtils.isEmpty(lTitle)) {
            CommonMethods.showSuccessToast(getContext(), "Enter Live title");
            return false;
        }
        if (type.equals("add") && TextUtils.isEmpty(imageString)) {
            CommonMethods.showSuccessToast(getContext(), "Choose cover image");
            return false;
        }
        if (TextUtils.isEmpty(lDate)) {
            CommonMethods.showSuccessToast(getContext(), "Choose date");
            return false;
        }
        if (TextUtils.isEmpty(lSTime)) {
            CommonMethods.showSuccessToast(getContext(), "Choose start time");
            return false;
        }
        if (TextUtils.isEmpty(lETime)) {
            CommonMethods.showSuccessToast(getContext(), "Choose end time");
            return false;
        }
        if (TextUtils.isEmpty(lDescription)) {
            CommonMethods.showSuccessToast(getContext(), "Enter Live description");
            return false;
        }
        if (!compareTime(lSTime, lETime)) {
            CommonMethods.showSuccessToast(getContext(), "Invalid live time");
            return false;
        }
        if (!validateExistTime()) {
            return false;
        }

        return true;
    }

    private boolean validateDate(String dt){
        try {
           return CommonMethods.isValidLiveDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean validateExistTime() {
        boolean result = true;
        Dialog dialog = null;
        if (liveArrayList.size() > 0) {
            dialog = CommonMethods.showProgress(getActivity(), "Please wait..",
                    true, 0);
            for (int i = 0; i < liveArrayList.size(); i++) {
                T_LiveModel model = liveArrayList.get(i);
                if (TextUtils.isEmpty(live_id)) {
                    if (!checkTime(model)) {
                        result = false;
                        break;
                    }
                } else if (!live_id.equals(model.getId())) {
                    if (!checkTime(model)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        if (dialog != null) {
            Dialog finalDialog = dialog;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalDialog.dismiss();
                }
            },2000);
          //  dialog.dismiss();
        }
        if (!result) {
            CommonMethods.showSuccessToast(getActivity(), "Live class already exist at the selected time");
        }
        return result;
    }


    private boolean checkTime(T_LiveModel model) {
        boolean result = true;
        String start = model.getStart_time();
        String end = model.getEnd_time();
        if (model.getDate().equals(lDate)) {
            if (start.equals(lSTime) || end.equals(lETime)) {
                Log.e("TimeValidation", "Equal time");
                result = false;

            } else {
                if (isTimeBetweenRange(start, end, lSTime)) {
                    Log.e("TimeValidation", "Start ime between range");
                    result = false;

                } else if (isTimeBetweenRange(start, end, lETime)) {
                    Log.e("TimeValidation", "End time between range");
                    result = false;

                }
            }
        }
        return result;
    }

    private static boolean isTimeBetweenRange(String startTime, String endTime, String checkTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime startLocalTime = LocalTime.parse(startTime, formatter);
        LocalTime endLocalTime = LocalTime.parse(endTime, formatter);
        LocalTime checkLocalTime = LocalTime.parse(checkTime, formatter);

        boolean isInBetween = false;
        if (endLocalTime.isAfter(startLocalTime)) {
            if (startLocalTime.isBefore(checkLocalTime) && endLocalTime.isAfter(checkLocalTime)) {
                isInBetween = true;
            }
        } else if (checkLocalTime.isAfter(startLocalTime) || checkLocalTime.isBefore(endLocalTime)) {
            isInBetween = true;
        }
        return isInBetween;
    }


    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete ?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ConnectivityReceiver.isConnected()) {
                            sendRequest(ApiCode.DELETE_LIVE_CLASS);
                        } else {
                            CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                        }

                        dialog.dismiss();
                    }
                }).show();
    }


    private void bindId(View view) {
        //Spinner
//        chapter_spinner=view.findViewById(R.id.chapter_spinner);
//        topic_spinner=view.findViewById(R.id.topic_spinner);

        //layout
//        lin_live=view.findViewById(R.id.live_list_layout);
//        lin_quiz=view.findViewById(R.id.lin_qiz);
//        card_live_list=view.findViewById(R.id.card_live_list);
        rel_live = view.findViewById(R.id.rel_create_live);

        //ImageView
//        iv_live_list=view.findViewById(R.id.iv_live);
//        iv_quiz_list=view.findViewById(R.id.iv_quiz);
//        add_live=view.findViewById(R.id.add_live);
//        add_quiz=view.findViewById(R.id.iv_add_quiz);
        //Recycler view
//        rv_live=view.findViewById(R.id.rv_live_list);
//        rv_quiz=view.findViewById(R.id.rv_quiz_quetion_list);
        swipe = view.findViewById(R.id.swipe);
        //rec_chapter=view.findViewById(R.id.rec_chapter);
        feb_shedule = view.findViewById(R.id.feb_shedule);

        bnt_scheduleLive = view.findViewById(R.id.rel_no_live);
        rel_showclass = view.findViewById(R.id.rel_showclass);
        live_create_card = view.findViewById(R.id.card_create_course);
        tv_live = view.findViewById(R.id.tv_test2);
        rec_shedule = view.findViewById(R.id.rec_shedule);


    }

    private void getArgumentsData() {
//        courseModel=getArguments().getParcelable("courseData");
        course_id = getArguments().getString("course_id");
        chapter_id = getArguments().getString("chapter_id");
        section_id = getArguments().getString("section_id");
        pageTitle = getArguments().getString("course_title");
    }

    @Override
    public void onResume() {
        super.onResume();
        // ((TeacherMainActivity)getActivity()).setChildActionBar("Schedule Live");
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle, false);
        ((T_ChapterDetailFragment) getParentFragment()).setLiveBgColor();
        ((T_ChapterDetailFragment) getParentFragment()).showAssignProfile();

        sendApiRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // management.setBoolean("isFromCreateQuiz",false);
    }

    //
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (position>0) {
//            switch (parent.getId()) {
//                case R.id.chapter_spinner:
//                    Chapter chapter = chapterList.get(parent.getSelectedItemPosition());
//                    Log.e("chapter : ", chapter.getId() + "  " + chapter.getTitle());
//                    chapterId=chapter.getId();
//
//                  management.setInt("chapterPosition",position);
//                    topicId=null;
//                    if (ConnectivityReceiver.isConnected()){
//                        topicList.clear();
//                        topicList.add(new TopicsModel("Select topic"));
//                        topicAdapter.notifyDataSetChanged();
//                        liveList.clear();
//                        liveAdapter.notifyDataSetChanged();
//                        quizQuestionList.clear();
//                        quizAdapter.notifyDataSetChanged();
//                        topic_spinner.setSelection(0);
//                        sendRequest(ApiCode.GET_TOPIC_BY_CHAPTER);
//                    }
//
//                    break;
//                case R.id.topic_spinner:
//                    TopicsModel model=topicList.get(parent.getSelectedItemPosition());
//                    Log.e("topic ",model.getTopic_id()+" "+model.getTopic_title());
//                    topicId=model.getTopic_id();
//                    management.setInt("topicPosition",position);
//                    if (ConnectivityReceiver.isConnected()){
//                        if (topicId!=null && chapterId!=null) {
//                            liveList.clear();
//                            liveAdapter.notifyDataSetChanged();
//                            quizQuestionList.clear();
//                            quizAdapter.notifyDataSetChanged();
//                            sendRequest(ApiCode.GET_LIVE_CLASS);
//                            sendRequest(ApiCode.GET_QUIZ_QUESTION);
//                        }
//                    }
//                    break;
//            }
//
//        }else if (position==0){
//            switch (parent.getId()) {
//                case R.id.chapter_spinner:
//                    chapterId=null;
//                    DynamicToast.make(getContext(),"Please select chapter",3000).show();
//                    break;
//                case R.id.topic_spinner:
//                    topicId=null;
//                    DynamicToast.make(getContext(),"Please select topic",3000).show();
//                    break;
//            }
//
//
//        }
//    }
//
//
//
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//    private void setTopicSpinner() {
//        topicList.clear();
//        topicList.add(new TopicsModel("Select topic"));
//        topicAdapter=new ArrayAdapter<TopicsModel>(getContext(), R.layout.spinner_item_holder,topicList);
//        topicAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
//        topic_spinner.setAdapter(topicAdapter);
//        topicAdapter.notifyDataSetChanged();
//
//
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_create_live:
            case R.id.feb_shedule:
            case R.id.card_create_course:
            case R.id.tv_test2:
                Log.e("live_click", "clicked");
                scheduleDialog("add");
                break;
//

//
        }
    }

    private void chooseImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);
                Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(cover_image);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

                rel_image.setVisibility(View.VISIBLE);
                choose_image.setVisibility(View.GONE);
                tv_imageName.setText("cover_image.jpeg");
                //DynamicToast.make(getContext(), "Cover image selected for upload", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private  void showDatePicker(TextView tvView){
//        final Calendar cd = Calendar.getInstance();
//        int mYear = cd.get(Calendar.YEAR);
//        int mMonth = cd.get(Calendar.MONTH);
//        int mDay = cd.get(Calendar.DAY_OF_MONTH);
        boolean isValidDate=false;
        final SpinnerPickerDialog spinnerPickerDialog = new SpinnerPickerDialog();
        spinnerPickerDialog.setContext(getActivity());
        spinnerPickerDialog.setArrowButton(true);
        spinnerPickerDialog.setAllColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_all_txt_color));
        spinnerPickerDialog.setmDividerColor(ContextCompat.getColor(getActivity(),
                R.color.calendar_divider_color));
      //---------------------
//        Calendar maxCalendar = Calendar.getInstance();
//        maxCalendar.add(Calendar.YEAR, -mYear);
//        maxCalendar.add(Calendar.MONTH, -mMonth);
//        maxCalendar.add(Calendar.DAY_OF_MONTH, -mDay);
//        spinnerPickerDialog.setMaxCalendar(maxCalendar);
        //--------------------

        spinnerPickerDialog.setOnDialogListener(new SpinnerPickerDialog.OnDialogListener() {

            @Override
            public void onSetDate(int month, int day, int year) {
                // "  (Month selected is 0 indexed {0 == January})"
                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());

                    tvView.setText(date);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {
                String d=tvView.getText().toString();
                if (!TextUtils.isEmpty(d)) {
                    if (!validateDate(d)) {
                        tvView.setText("");
//                        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(),"");
                        CommonMethods.showSuccessToast(getActivity(),"You can't select a date earlier than the current date");
                    }
                }
            }

        });
//        spinnerPickerDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    private void chooseDate(TextView view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth
                ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());
                view.setText(date);
            }
        }, mYear, mMonth, mDay);
       // datePickerDialog.getDatePicker().setSpinnersShown(true);
        //datePickerDialog.getDatePicker().setCalendarViewShown(false);
        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = new Date();
            Date d = f.parse(f.format(date));
            long milliseconds = d.getTime();
            datePickerDialog.getDatePicker().setMinDate(milliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();


    }

    private void shoTimePicker(int value, TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinut = c.get(Calendar.MINUTE);
        int mSec = c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity()
                , android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        Calendar cl = Calendar.getInstance();
                        cl.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cl.set(Calendar.MINUTE, minute);
                        String time = sdf.format(cl.getTime());

                        textView.setText(time);

                    }
                }, mHour, mMinut, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (value == 1) {
            timePickerDialog.setTitle("Start Time");
        } else {
            timePickerDialog.setTitle("End Time");
        }
        timePickerDialog.show();

    }

    private boolean compareTime(String sTime, String eTime) {
        boolean result = false;
        //SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        try {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(sTime);
            Date t2 = df.parse(eTime);
            if (t1.before(t2)) {
                if (!isBeforeDate(lDate)) {
                    if (isCurrentDate(lDate)) {
                        String d = lDate + " " + sTime;
                        SimpleDateFormat dfmt = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
                        Date d_start_time = dfmt.parse(d);
                        Date c_time = dfmt.parse(getCurrentTime("dd-MMM-yyyy h:mm a"));
                        if (d_start_time.after(c_time) && t1.before(t2)) {
                            result = true;
                        }
                    } else {
                        if (t1.before(t2)) {
                            result = true;

                        }
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.ADD_LIVE_CLASS:
                params.put("user_id", management.getString(USER_ID));
                params.put("chapter_id", chapter_id);
                params.put("course_id", course_id);
                params.put("section_id", section_id);
                params.put("date", lDate);
                params.put("start_time", lSTime);
                params.put("end_time", lETime);
                params.put("title", lTitle);
                params.put("description", lDescription);
                params.put("is_demo", is_demo_live);
                params.put("cover_image", imageString);
                callApi(ApiCode.ADD_LIVE_CLASS, params);
                break;
            case ApiCode.GET_LIVE_CLASSES:
                params.put("chapter_id", chapter_id);
                params.put("section_id", section_id);
                params.put("course_id", course_id);
                params.put("type", "all");
                params.put("user_id", management.getString(USER_ID));
                callApi(ApiCode.GET_LIVE_CLASSES, params);
                break;
            case ApiCode.DELETE_LIVE_CLASS:
                params.put("liveclass_id", liveClassId);
                callApi(ApiCode.DELETE_LIVE_CLASS, params);
                break;
            case ApiCode.UPDATE_LIVE_CLASS:
                params.put("liveclass_id", live_id);
                params.put("user_id", management.getString(USER_ID));
                params.put("chapter_id", chapter_id);
                params.put("course_id", course_id);
                params.put("section_id", section_id);
                params.put("date", lDate);
                params.put("start_time", lSTime);
                params.put("end_time", lETime);
                params.put("title", lTitle);
                params.put("description", lDescription);
                params.put("is_demo", is_demo_live);
                params.put("cover_image", imageString);
                callApi(ApiCode.UPDATE_LIVE_CLASS, params);
                break;


        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {

            case ApiCode.ADD_LIVE_CLASS:
                service.postDataVolley(ApiCode.ADD_LIVE_CLASS,
                        BaseUrl.URL_ADD_LIVE_CLASS, params);
                break;
            case ApiCode.GET_LIVE_CLASSES:
                service.postDataVolley(ApiCode.GET_LIVE_CLASSES,
                        BaseUrl.URL_GET_LIVE_CLASSES, params);
                break;

            case ApiCode.DELETE_LIVE_CLASS:
                service.postDataVolley(ApiCode.DELETE_LIVE_CLASS,
                        BaseUrl.URL_DELETE_LIVE_CLASS, params);
                break;
            case ApiCode.UPDATE_LIVE_CLASS:
                service.postDataVolley(ApiCode.UPDATE_LIVE_CLASS,
                        BaseUrl.URL_UPDATE_LIVE_CLASS, params);
                break;

        }
    }


    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.ADD_LIVE_CLASS:
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Live Added Successfully");
                    lTitle = "";
                    lDate = "";
                    imageString = "";
                    lSTime = "";
                    lETime = "";
                    lDescription = "";
                    is_demo_live = "0";
                    sendApiRequest();

                }
                break;
            case ApiCode.DELETE_LIVE_CLASS:
                if (jsonObject.getBoolean("status")) {

                    CommonMethods.showSuccessToast(getContext(), "Live Deleted Successfully");
                    sendApiRequest();
                }
                break;
            case ApiCode.UPDATE_LIVE_CLASS:
                if (jsonObject.getBoolean("status")) {
                    CommonMethods.showSuccessToast(getContext(), "Live Updated Successfully");
                    lTitle = "";
                    lDate = "";
                    imageString = "";
                    lSTime = "";
                    lETime = "";
                    lDescription = "";
                    is_demo_live = "0";
                    sendApiRequest();
                }

                break;
            case ApiCode.GET_LIVE_CLASSES:
                Log.e("live class", response);
                if (jsonObject.getBoolean("status")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        List<T_LiveModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new TypeToken<List<T_LiveModel>>() {
                                        }.getType());
                        liveArrayList.clear();
                        liveArrayList.addAll(psearch);
                        setLive();

                    } else {
                        liveArrayList.clear();
                        setLive();
                    }

                }

                break;

        }

    }


}
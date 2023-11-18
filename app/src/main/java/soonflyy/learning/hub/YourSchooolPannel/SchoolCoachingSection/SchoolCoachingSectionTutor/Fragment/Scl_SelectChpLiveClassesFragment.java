package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Fragment;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.CommonMethods.getCurrentTime;
import static soonflyy.learning.hub.Common.CommonMethods.isBeforeDate;
import static soonflyy.learning.hub.Common.CommonMethods.isCurrentDate;

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anggastudio.spinnerpickerdialog.SpinnerPickerDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.T_LiveAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter.SchoolLiveClassAdapter;
import soonflyy.learning.hub.live.MeetingActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class Scl_SelectChpLiveClassesFragment extends Fragment implements VolleyResponseListener {
    RelativeLayout rel_no_live, rel_showclass;
    RecyclerView rec_liveclass;
    private FloatingActionButton feb_addclass;
    SwipeRefreshLayout swipe;
SchoolLiveClassAdapter liveClassAdapter;
//ArrayList<SchoolLiveClassModel> livelist;
ArrayList<T_LiveModel>liveArrayList=new ArrayList<>();
    T_LiveAdapter tLiveAdapter;

    ImageView choose_image, cover_image;
    RelativeLayout rel_image;
    CircleImageView cancel_image;
    TextView tv_imageName;
    String  imageString = "";
    String url;

    String lTitle,lDate,lSTime,lETime,lDescription,live_id;

String from,teacher_id,chapter_id,subject_id,pageTitle;
    public Scl_SelectChpLiveClassesFragment() {
        // Required empty public constructor
    }


    public static Scl_SelectChpLiveClassesFragment newInstance(String param1, String param2) {
        Scl_SelectChpLiveClassesFragment fragment = new Scl_SelectChpLiveClassesFragment();

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
        View view = inflater.inflate(R.layout.fragment_scl_live_classes, container, false);
        init_View(view);
        getIntentData();
        init_swipe_method();
//        if (CommonMethods.checkInternetConnection(getActivity())){
//            sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
//        }

       // initRecyclerview();
        return view;
    }

    private void getIntentData() {
        from=getArguments().getString("from");
        teacher_id=getArguments().getString("teacher_id");
        chapter_id=getArguments().getString("chapter_id");
        pageTitle=getArguments().getString("chapter_name");
        subject_id=getArguments().getString("subject_id");
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                if (CommonMethods.checkInternetConnection(getActivity())){
                    sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
                }
                 initControl();
               // initRecyclerview();


            }
        });

        swipe.setProgressBackgroundColorSchemeResource(R.color.white);
        // Set refresh indicator color to red.
        int indicatorColorArr[] = {R.color.red, R.color.green, R.color.orange, R.color.cyan};
        swipe.setColorSchemeResources(indicatorColorArr);
    }

    private void initControl() {
    }


    private void init_View(View view) {
        swipe = view.findViewById(R.id.swipe);
        rel_no_live = view.findViewById(R.id.rel_no_live);
        rel_showclass = view.findViewById(R.id.rel_showclass);
        rec_liveclass = view.findViewById(R.id.rec_liveclass);
        rec_liveclass.hasFixedSize();
        rec_liveclass.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) ;
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_liveclass.setLayoutManager(layoutManager);
        rec_liveclass.setKeepScreenOn(true);

        feb_addclass = view.findViewById(R.id.feb_addclass);
        feb_addclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // show_LiveSchedule();
                scheduleDialog("add");
            }
        });
        rel_no_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleDialog("add");
            }
        });

    }

//    private void initRecyclerview() {
//
//        livelist = new ArrayList<>();
//
//        livelist.add(new SchoolLiveClassModel(R.drawable.img_4,"Geometry","40min"));
//        livelist.add(new SchoolLiveClassModel(R.drawable.school,"Geometry","40min"));
//
//
//        liveClassAdapter= new SchoolLiveClassAdapter(getActivity(), livelist, new SchoolLiveClassAdapter.OnSelectClassClickListener() {
//            @Override
//            public void onItemClick(int postion) {
//                SchoolAllSubjectsFragment fragment = new SchoolAllSubjectsFragment ();
//
//                SwitchFragment (fragment);
//            }
//
//            @Override
//            public void onDelete(int position) {
//
//            }
//
//            @Override
//            public void onEdit(int position) {
//
//            }
//        });
//        rec_liveclass.setAdapter(liveClassAdapter);
//        liveClassAdapter.notifyDataSetChanged();
//        if (livelist.size()==0){
//            rel_no_live.setVisibility(View.VISIBLE);
//            rel_showclass.setVisibility(View.GONE);
//
//        }
//        else {
//            rel_no_live.setVisibility(View.GONE);
//            rel_showclass.setVisibility(View.VISIBLE);
//
//        }
//
//
//
//
//    }

    private void setLive(){
        tLiveAdapter =new T_LiveAdapter(getContext(), liveArrayList, "all",new T_LiveAdapter.OnLiveClickListener() {
            @Override
            public void onLiveClick(int position) {
                //golive
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
                    T_LiveModel model=liveArrayList.get(position);
//                    LiveFragment fragment=new LiveFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("live_type","chapter");
//                    bundle.putString("type",SCHOOL_TUTOR);
//                    bundle.putString("slug",model.getSlug());
//                    bundle.putString("description",model.getDescription());
//                    fragment.setArguments(bundle);
//                    ((SchoolMainActivity)getActivity()).switchFragmentOnSchoolMainActivity(fragment);


                }else{
                    CommonMethods.requestAudioCameraPermission(getActivity(),333);
                }

            }

            @Override
            public void onItemClick(int postion) {

            }

            @Override
            public void onDelete(int position) {
                live_id=liveArrayList.get(position).getLive_id();
                showDeleteDialog();

            }

            @Override
            public void onEdit(int position) {
                T_LiveModel model=liveArrayList.get(position);
                url=model.getCover_image();
                live_id=model.getLive_id();
                lTitle=model.getTitle();
                lDate=model.getDate();
                lSTime=model.getStart_time();
                lETime=model.getEnd_time();
                lDescription=model.getDescription();
                scheduleDialog("update");

            }

            @Override
            public void onGoLive(int position) {
                if (CommonMethods.checkAudioCameraPermission(getContext())) {
                    T_LiveModel model=liveArrayList.get(position);

               // Intent lIntent=new Intent(getActivity(), LiveClassesActivity.class);
                Intent lIntent=new Intent(getActivity(), MeetingActivity.class);
                lIntent.putExtra("title",model.getTitle());
                lIntent.putExtra("sTime",model.getStart_time());
                lIntent.putExtra("eTime",model.getEnd_time());
                lIntent.putExtra("slug",model.getSlug());
                lIntent.putExtra("live_type","chapter");
                lIntent.putExtra("type","teacher");
                lIntent.putExtra("description",model.getDescription());
                    lIntent.putExtra("live_id",model.getLive_id());
                    lIntent.putExtra("from",from);
                getActivity().startActivity(lIntent);

                }else{
                    CommonMethods.requestAudioCameraPermission(getActivity(),333);
                }

            }
        });
        rec_liveclass.setAdapter(tLiveAdapter);
        if (tLiveAdapter.getItemCount()>0){
            rel_no_live.setVisibility(View.GONE);
            rel_showclass.setVisibility(View.VISIBLE);
        }else{
            rel_no_live.setVisibility(View.VISIBLE);
            rel_showclass.setVisibility(View.GONE);
        }
    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        //fragmentTransaction.replace(R.id.container_layout, fragment, ProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void show_LiveSchedule() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailoge_live_class_shedule);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_save);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void scheduleDialog(String type) {

        Dialog dialog = new Dialog (getActivity());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.dailoge_live_class_shedule);
        dialog.getWindow ();
        dialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
        dialog.show ( );


        LinearLayout lin_demo_live = dialog.findViewById(R.id.lin_demo_live);
        EditText et_title = dialog.findViewById(R.id.et_name);
        EditText et_description = dialog.findViewById(R.id.et_discription);
        choose_image = dialog.findViewById(R.id.iv_sub);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_date = dialog.findViewById(R.id.et_date);
        TextView tv_sTime = dialog.findViewById(R.id.et_time1);
        TextView tv_eTime = dialog.findViewById(R.id.et_time2);
        Button save_btn =dialog.findViewById(R.id.btn_save);
        rel_image=dialog.findViewById(R.id.rel_cover_image);
        cancel_image=dialog.findViewById(R.id.cancel_image);
        cover_image =dialog.findViewById(R.id.image_view);
        tv_imageName=dialog.findViewById(R.id.tv_img_name);
        lin_demo_live.setVisibility(View.GONE);
        Switch btn_switch=dialog.findViewById(R.id.demo_switch);

        if (type.equals("update")){
            et_title.setText(lTitle);
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA+url)
                    .placeholder(R.drawable.logoo).into(cover_image);
            tv_imageName.setText("cover_image.jpeg");
            choose_image.setVisibility(View.GONE);
            rel_image.setVisibility(View.VISIBLE);
            tv_date.setText(lDate);
            tv_sTime.setText(lSTime);
            tv_eTime.setText(lETime);
            et_description.setText(lDescription);

        }

        dialog.show();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lTitle=et_title.getText().toString();
                lDate=tv_date.getText().toString().trim();
                lSTime=tv_sTime.getText().toString().trim();
                lETime=tv_eTime.getText().toString().trim();
                lDescription=et_description.getText().toString().trim();

                if (validateLiveField(type)){
                    //cal api
                 if (CommonMethods.checkInternetConnection(getActivity())){
                     if(type.equals("add"))
                         sendRequest(ApiCode.SCHOOL_ADD_LIVE_CLASS);
                     else
                         //update
                         sendRequest(ApiCode.SCHOOL_UPDATE_LIVE_CLASS);
                     dialog.dismiss();
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
                shoTimePicker(1,tv_sTime);
            }
        });
        tv_eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoTimePicker(0,tv_eTime);
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // chooseDate(tv_date);
                showDatePicker(tv_date);
            }
        });

        dialog.setCanceledOnTouchOutside (false);
    }

    private boolean validateLiveField(String type) {
        if (TextUtils.isEmpty(lTitle)){
            CommonMethods.showSuccessToast(getContext(),"Enter Live title");
            return false;
        }
        if (type.equals("add") && TextUtils.isEmpty(imageString)){
            CommonMethods.showSuccessToast(getContext(),"Choose cover image");
            return false;
        }
        if (TextUtils.isEmpty(lDate)){
            CommonMethods.showSuccessToast(getContext(),"Choose date");
            return false;
        }
        if (TextUtils.isEmpty(lSTime)){
            CommonMethods.showSuccessToast(getContext(),"Choose start time");
            return false;
        }
        if (TextUtils.isEmpty(lETime)){
            CommonMethods.showSuccessToast(getContext(),"Choose end time");
            return false;
        }
        if (TextUtils.isEmpty(lDescription)){
            CommonMethods.showSuccessToast(getContext(),"Enter Live description");
            return false;
        }
        if(!compareTime(lSTime,lETime)){
            CommonMethods.showSuccessToast(getContext(),"Invalid live time");
            return false;
        }
        if (!validateExistTime()) {
            return false;
        }

        return true;
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
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.SCHOOL_DELETE_LIVE_CLASS);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }

                dialog.dismiss();
            }
        }).show();
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
    private void chooseDate(TextView view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                c.set(year, month, day);
                String date = sdf.format(c.getTime());
                view.setText(date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();


    }
    private  void showDatePicker(TextView tvView){
//
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
    private boolean validateDate(String dt){
        try {
            return CommonMethods.isValidLiveDate(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void shoTimePicker(int value ,TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinut = c.get(Calendar.MINUTE);
        int mSec = c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity()
                , android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                        Calendar cl= Calendar.getInstance();
                        cl.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cl.set(Calendar.MINUTE,minute);
                        String time=sdf.format(cl.getTime());

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
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            Date t1 = df.parse(sTime);
            Date t2 = df.parse(eTime);
            if (t1.before(t2)) {
                if (!isBeforeDate(lDate)) {
                    if (isCurrentDate(lDate)) {
                        String d = lDate + " " + sTime;
                        SimpleDateFormat dfmt = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                        Date d_start_time = dfmt.parse(d);
                        Date c_time = dfmt.parse(getCurrentTime("dd-MMM-yyyy hh:mm a"));
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


    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_GET_LIVE_CLASS, params);
                break;
            case ApiCode.SCHOOL_ADD_LIVE_CLASS:
                params.put("title", lTitle);
                params.put("cover_image", imageString);
                params.put("date", lDate);
                params.put("start_time", lSTime);
                params.put("end_time", lETime);
                params.put("description", lDescription);
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                callApi(ApiCode.SCHOOL_ADD_LIVE_CLASS, params);
                break;
            case ApiCode.SCHOOL_UPDATE_LIVE_CLASS:
                params.put("title", lTitle);
                params.put("cover_image", imageString);
                params.put("date", lDate);
                params.put("start_time", lSTime);
                params.put("end_time", lETime);
                params.put("description", lDescription);
                params.put("teacher_id", teacher_id);
                params.put("subject_id", subject_id);
                params.put("chapter_id", chapter_id);
                params.put("liveclass_id", live_id);
                callApi(ApiCode.SCHOOL_UPDATE_LIVE_CLASS, params);
                break;
            case ApiCode.SCHOOL_DELETE_LIVE_CLASS:

                params.put("liveclass_id", live_id);
                callApi(ApiCode.SCHOOL_DELETE_LIVE_CLASS, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_GET_LIVE_CLASS,
                        BaseUrl.URL_SCHOOL_GET_LIVE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_GET_LIVE_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_ADD_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_ADD_LIVE_CLASS,
                        BaseUrl.URL_SCHOOL_ADD_LIVE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_ADD_LIVE_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_UPDATE_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_LIVE_CLASS,
                        BaseUrl.URL_SCHOOL_UPDATE_LIVE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_LIVE_CLASS);
                Log.e("params", params.toString());
                break;
            case ApiCode.SCHOOL_DELETE_LIVE_CLASS:
                service.postDataVolley(ApiCode.SCHOOL_DELETE_LIVE_CLASS,
                        BaseUrl.URL_SCHOOL_DELETE_LIVE_CLASS, params);
                Log.e("api", BaseUrl.URL_SCHOOL_DELETE_LIVE_CLASS);
                Log.e("params", params.toString());
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_GET_LIVE_CLASS:
                Log.e("sc_section", response.toString());
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        ArrayList<T_LiveModel> psearch = new Gson().
                                fromJson(jsonArray.toString(),
                                        new com.google.gson.reflect.TypeToken<ArrayList<T_LiveModel>>() {
                                        }.getType());
                        liveArrayList.clear();
                        liveArrayList.addAll(psearch);
                        setLive();
                    }else{
                        liveArrayList.clear();
                        setLive();
                    }
                } else {
                    CommonMethods.showSuccessToast(getContext(), jsonObject.getString("message"));
                }
////
                break;
            case ApiCode.SCHOOL_ADD_LIVE_CLASS:
                Log.e("sc_add_live", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                break;
            case ApiCode.SCHOOL_UPDATE_LIVE_CLASS:
                Log.e("sc_up_live", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
            case ApiCode.SCHOOL_DELETE_LIVE_CLASS:
                Log.e("delete_live", response);
                if (jsonObject.getBoolean("status")){
                    sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
                }
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ((Scl_SelectChpterDeatailFragment)getParentFragment()).setLiveBgColor();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (CommonMethods.checkInternetConnection(getActivity())){
            sendRequest(ApiCode.SCHOOL_GET_LIVE_CLASS);
        }


    }


}


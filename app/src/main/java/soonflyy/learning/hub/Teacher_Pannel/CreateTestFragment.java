package soonflyy.learning.hub.Teacher_Pannel;

import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.adapter.CreateTestAdapter;
import soonflyy.learning.hub.model.CreateTestModel;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CreateTestFragment extends Fragment implements View.OnClickListener, VolleyResponseListener, View.OnTouchListener {//AppCompatActivity
    float dX;
    float dY;
    int lastAction;

    RecyclerView rec_main;
    TextView create_test_btn;
    CreateTestAdapter testAdapter;
    ImageView arrow_back_img;
    TextView tv_title;

    ArrayList<CreateTestModel> tlist = new ArrayList<>();
    ImageButton add_Question_btn;
    SessionManagement session_management;
    ArrayList<CreateTestModel> testmodel;




    String  pageTitle;//,passingMark
    String  courseId;




    private boolean isCheckedEmptyField = false;


    ///
   TextView tv_duration, tv_date, tv_start_time, tv_end_time;
    EditText et_title;
    int dMin;
    String course_title;
    String free_paid;
    float price;
    float discount_price;


    public void CreateTestFragment() {
        //def// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_test, container, false);
        initview(view);
        getArgumenttData();

        ((Create_Course_MainFragment) getParentFragment()).changeTrackerColor(3);
//
        return view;


    }



    private void initview(View view) {

        et_title = view.findViewById(R.id.et_name);
        tv_duration = view.findViewById(R.id.et_duration);
        tv_date = view.findViewById(R.id.et_date);
        tv_start_time = view.findViewById(R.id.et_time1);
        tv_end_time = view.findViewById(R.id.et_time2);

        testmodel = new ArrayList<>();
        rec_main = view.findViewById(R.id.rec_main);
        create_test_btn = view.findViewById(R.id.create_test_btn);
        create_test_btn.setOnClickListener(this);
        tv_title = view.findViewById(R.id.tv_title);

        arrow_back_img = view.findViewById(R.id.arrow_back_img);


        session_management = new SessionManagement(getContext());
        arrow_back_img.setOnClickListener(this);

        tv_date.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        tv_duration.setOnClickListener(this);

        rec_main.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void getArgumenttData() {
        courseId = getArguments().getString("course_id");
        course_title = getArguments().getString("course_title");
//
//
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_test_btn:
                if (validate()) {
                    if (ConnectivityReceiver.isConnected()) {
                        sendRequestCreateTest(ApiCode.ADD_TEST);
                    } else {
                        CommonMethods.showSuccessToast(getContext(), "No Internet Connection");
                    }
                }

                break;
            case R.id.et_date:
                chooseDate();
                break;
            case R.id.et_time1:
                shoTimePicker(1);
                break;
            case R.id.et_time2:
                shoTimePicker(0);
                break;
            case R.id.et_duration:
                chooseDuration();
                break;

        }

    }

    private void show_button_dailoge() {
        List<String> typelist = new ArrayList<>();
        typelist.add("");
        typelist.add("Paid");
        typelist.add("Free");
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoge_finish_test);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        LinearLayout liner = dialog.findViewById(R.id.liner);
        EditText et_name = dialog.findViewById(R.id.et_name);
        //EditText et_paid_amount = dialog.findViewById(R.id.et_paid_amount);
        EditText et_amount = dialog.findViewById(R.id.et_amount);
        EditText et_disamount = dialog.findViewById(R.id.et_disamount);
        Button btn_save = dialog.findViewById(R.id.btn_save);
        TextView tv_back = dialog.findViewById(R.id.tv_back);
        TextView tv_type = dialog.findViewById(R.id.tpe_tv);
        Spinner type_spinner = dialog.findViewById(R.id.type_spinner);

        et_name.setText(course_title);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_holder, typelist);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item_holder);
        type_spinner.setAdapter(typeAdapter);
        typeAdapter.notifyDataSetChanged();
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    free_paid=parent.getSelectedItem().toString();
                    tv_type.setText(free_paid);
                    if (position==1){
                        et_amount.setEnabled(true);
                        et_disamount.setEnabled(true);
                    }else{
                        et_amount.setEnabled(false);
                        et_disamount.setEnabled(false);
                        et_amount.getText().clear();
                        et_disamount.getText().clear();
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.show();
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_spinner.performClick();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // float dis=0;
               if (TextUtils.isEmpty(et_disamount.getText().toString().trim())){
                   discount_price=0;
               }else{
                   discount_price=Float.parseFloat(et_disamount.getText().toString().trim());
               }
                if (TextUtils.isEmpty(et_amount.getText().toString().trim())){
                    price=0;
                }else{
                    price=Float.parseFloat(et_amount.getText().toString().trim());
                }

                if (TextUtils.isEmpty(free_paid)){
                    CommonMethods.showSuccessToast(getContext(),"Choose Course Type");
                }else if (free_paid.equals("Paid") && TextUtils.isEmpty(et_amount.getText().toString().trim())){
                    CommonMethods.showSuccessToast(getContext(),"Enter amount");
                }else if (free_paid.equals("Paid") && price==0){
                    CommonMethods.showSuccessToast(getContext(),"Amount should not be 0");
                }else if (free_paid.equals("Paid") && (price<=discount_price)){
                    CommonMethods.showSuccessToast(getContext(),"Discount should be less than amount");
                }else {
                    //send request
                    if (ConnectivityReceiver.isConnected()){
                        sendRequestCreateTest(ApiCode.CREATE_COURSE_PRICE);
                        dialog.dismiss();
                    }else{
                        CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                    }
                }


            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
    }


    private void chooseDuration() {
        View view = getLayoutInflater().inflate(R.layout.dialog_test_duration_chooser, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        EditText min = view.findViewById(R.id.min_edit);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView ok = view.findViewById(R.id.tv_ok);
        cancel.setOnClickListener(v12 -> dialog.dismiss());
        ok.setOnClickListener(v1 -> {
            String m = min.getText().toString().trim();
            if (TextUtils.isEmpty(m)) {
                min.setError("Invalid");
                min.requestFocus();
            } else {
                dMin = Integer.parseInt(m);
                if (dMin > 0) {
                    if (m.length() == 1) {
                        m = "0" + m;
                    }
                    tv_duration.setText(m + " Min");
                    dialog.dismiss();
                } else {
                    min.setError("Invalid");
                    min.requestFocus();
                }
            }

        });

    }

    private void chooseDate() {
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
                tv_date.setText(date);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    private void shoTimePicker(int value) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinut = c.get(Calendar.MINUTE);
        int mSec = c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity()
                , android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        Calendar cl = Calendar.getInstance();
                        cl.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cl.set(Calendar.MINUTE, minute);
                        String time = sdf.format(cl.getTime());
                        if (value == 1) {
                            tv_start_time.setText(time);
                        } else {
                            tv_end_time.setText(time);
                        }

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

    private boolean validate() {
        if (TextUtils.isEmpty(et_title.getText().toString())) {
            et_title.setError("Enter title");
            et_title.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(tv_duration.getText().toString())) {
            CommonMethods.showSuccessToast(getContext(), "Choose duration");
            return false;
        }
        if (TextUtils.isEmpty(tv_date.getText().toString())) {
            CommonMethods.showSuccessToast(getContext(), "Choose date");
            return false;
        }
        if (CommonMethods.isBeforeDate(tv_date.getText().toString().trim())){
            CommonMethods.showSuccessToast(getContext(),"Invalid Date");
            return  false;
        }

        if (TextUtils.isEmpty(tv_start_time.getText().toString())) {
            CommonMethods.showSuccessToast(getContext(), "Choose start time");
            return false;
        }
        if (TextUtils.isEmpty(tv_end_time.getText().toString())) {
            CommonMethods.showSuccessToast(getContext(), "Choose end time");
            return false;
        }

        String time1=tv_start_time.getText().toString().trim();
        String time2=tv_end_time.getText().toString().trim();
        if (!CommonMethods.isValidTime(time1, time2,tv_date.getText().toString().trim(),dMin)){
            CommonMethods.showSuccessToast(getContext(),"Invalid time");

            return false;
        }
        return true;
    }


    private void sendRequestCreateTest(int request) {//, ArrayList<CreateTestModel> questionList
        HashMap<String, String> param = new HashMap<>();
        switch (request) {

            case ApiCode.ADD_TEST:
                param.put("title", et_title.getText().toString().trim());
                param.put("duration", String.valueOf(dMin));
                param.put("start_time", tv_start_time.getText().toString().trim());
                param.put("end_time", tv_end_time.getText().toString().trim());//endTime
                param.put("date", tv_date.getText().toString().trim());
                param.put("type", "course");//chapter/course
                param.put("is_paid", "0");//String.valueOf(isPaid)
                param.put("amount", "0");//paidAmount
                param.put("subject_id", "");
                param.put("chapter_id", "");
                param.put("test_description", "test");
                param.put("course_id", courseId);
                param.put("user_id", session_management.getString(USER_ID));
                callApi(ApiCode.ADD_TEST, param);
                break;
            case ApiCode.CREATE_COURSE_PRICE:
                if (free_paid.equals("Free")){
                    discount_price=0;
                    price=0;
                    free_paid="1";
                }
                if (free_paid.equals("Paid")){
                    free_paid="0";
                }
                param.put("course_id", courseId);
                param.put("discount_amount", String.valueOf(discount_price));
                param.put("amount", String.valueOf(price));
                param.put("is_free_course", free_paid);
                callApi(ApiCode.CREATE_COURSE_PRICE, param);
                break;

        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.ADD_TEST:
                service.postDataVolley(ApiCode.ADD_TEST,
                        BaseUrl.URL_ADD_TEST, params);
                break;
            case ApiCode.CREATE_COURSE_PRICE:
                service.postDataVolley(ApiCode.CREATE_COURSE_PRICE,
                        BaseUrl.URL_CREATE_COURSE_PRICE, params);
                break;
//

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
//

            case ApiCode.ADD_TEST:
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        //Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        CommonMethods.showSuccessToast(getContext(), "Test Added Successfully");
                        show_button_dailoge();
                    }
                break;
            case ApiCode.CREATE_COURSE_PRICE:

                    boolean result = jsonObject.getBoolean("status");
                    if (result) {
                        //Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        CommonMethods.showSuccessToast(getContext(), " Added Successfully");
                        ((TeacherMainActivity)getActivity()).onBackPressed();


                    }
                break;


        }
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment);// TeacherProfileFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
       // ((TeacherMainActivity) getActivity()).setChildActionBar(pageTitle);
        ((TeacherMainActivity) getActivity()).setTeacherActionBar(pageTitle,false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    tlist.add(new CreateTestModel());
                testAdapter.notifyDataSetChanged();
                //  testAdapter.notifyItemInserted(tlist.size() - 1);
                Log.e("size ", "" + tlist.size());
                rec_main.scrollToPosition(tlist.size() - 1);
                break;

            default:
                return false;
        }
        return true;

    }
}
package soonflyy.learning.hub.Student_Pannel;

import static soonflyy.learning.hub.Common.Constant.COURSE_PRICE;
import static soonflyy.learning.hub.Common.Constant.EMAIL;
import static soonflyy.learning.hub.Common.Constant.ENROLL_ID;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.USER_ID;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.makeramen.roundedimageview.RoundedImageView;
import com.razorpay.Checkout;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Course_DetailsMOdel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BuyCourseFragment extends Fragment implements View.OnClickListener,
        VolleyResponseListener {//, PaymentResultWithDataListener
    public static final String TAG = BuyCourseFragment.class.getName();
    ImageView iv_thumbnail;
    RoundedImageView riv_course;
    TextView tv_courseName,tv_tutorName;


    TextView offline_payment_tv,online_payment_tv,tv_name,
            tv_occupation,tv_course_fee,tv_total,tv_course_code,tv_discount,tv_offer;

    LinearLayout lin_offer;

    private String type,course_id,price;

    Course_DetailsMOdel model;
    private String merchantKey,light_logo,payment_id,enrol_id;
    private String paymentKey="";

    SessionManagement management;
    public BuyCourseFragment() {
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
        View view =inflater.inflate(R.layout.fragment_buy_course, container, false);
        init(view);
        management=new SessionManagement(getContext());
        if (ConnectivityReceiver.isConnected()){
            sendRequest(ApiCode.FRONTEND_CODE);
        }
        getArgumentsData();
        initControl();
        return  view;
    }

    private void getArgumentsData() {
        try {
            model=getArguments().getParcelable("course");
            setData();

//
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setData() {


        String name="Course Name: "+model.getTitle();
        String tutor="Course By: "+model.getInstructor_name();

        String imageUrl=BaseUrl.BASE_URL_MEDIA+model.getThumbnail();
        Picasso.get().load(imageUrl).placeholder(R.drawable.logoo).into(iv_thumbnail);
        Picasso.get().load(imageUrl).placeholder(R.drawable.logoo).into(riv_course);
        tv_courseName.setText(name);
        tv_tutorName.setText(tutor);

        tv_course_fee.setText("Rs."+model.getAmount());

        tv_discount.setText(String.valueOf("Rs."+model.getDiscounted_price()));
         price= String.valueOf(Float.parseFloat(model.getAmount())-
                Float.parseFloat(model.getDiscounted_price()));
        tv_total.setText("Rs."+price);
        online_payment_tv.setText("Buy Course@ Rs."+price);

        try {
            setCourseOffer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setCourseOffer() throws JSONException {
        Float amt=(Math.abs(Float.parseFloat(model.getAmount())-
                Float.parseFloat(model.getDiscounted_price())));
        JSONArray jsonArray=new JSONArray(model.getCourse_offers());
        ArrayList<String> u_courseOffer=new ArrayList<>();
        if (jsonArray.length()>0){
            u_courseOffer.add(jsonArray.getString(0));
            u_courseOffer.add(jsonArray.getString(1));
        }
        if (jsonArray.length()==0){
            lin_offer.setVisibility(View.GONE);
        }else {
            lin_offer.setVisibility(View.VISIBLE);
            if (u_courseOffer.size() > 0) {
                lin_offer.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(model.getOffer_course_price())){
                    if (Float.parseFloat(model.getOffer_course_price())>0){
                        tv_offer.setText("By this course @Rs "+amt+" and get 1 course free worth Rs. "+model.getOffer_course_price());
                    }else{
                        tv_offer.setText("By this course @Rs "+amt+" and get 1 course free");
                    }
                }else{
                    tv_offer.setText("By this course @Rs "+amt+" and get 1 course free");
                }

            }else{
                lin_offer.setVisibility(View.GONE);
            }

        }
    }





    private void init(View view) {
        riv_course=view.findViewById(R.id.riv_course);
        tv_courseName=view.findViewById(R.id.name_tv);
        tv_tutorName=view.findViewById(R.id.occupation_tv);

        online_payment_tv = view.findViewById(R.id.online_payment_tv);
        offline_payment_tv = view.findViewById(R.id.offline_payment_tv);

        iv_thumbnail=view.findViewById(R.id.img);
        tv_name=view.findViewById(R.id.name_tv);
    //    tv_course_code=view.findViewById(R.id.rating);
        tv_occupation=view.findViewById(R.id.occupation_tv);
        tv_course_fee=view.findViewById(R.id.course_fee);
        tv_total=view.findViewById(R.id.tv_total_amount);
        tv_discount=view.findViewById(R.id.tv_discount);
        tv_offer=view.findViewById(R.id.tv_offer);
        lin_offer=view.findViewById(R.id.lin_offer);


    }


    private void initControl() {


        online_payment_tv.setOnClickListener(this);
        offline_payment_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.online_payment_tv:
               // goToOnlineFragment();
                if (ConnectivityReceiver.isConnected()){
                    sendRequest(ApiCode.PURCHASE_COURSE);
                }else{
                    CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                }
                break;

        }
    }




    @Override
    public void onResume() {
        super.onResume();
       // tv_title.setText("Buy Course");
        ((MainActivity)getActivity()).setStudentChildActionBar("Buy Course",false);

    }

    public void SwitchFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.container_layout, fragment);//, OnlinePaymentFragment.TAG
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.FRONTEND_CODE:
                callApi(ApiCode.FRONTEND_CODE, params);
                break;
            case ApiCode.PURCHASE_COURSE:
                params.put("user_id",new SessionManagement(getContext()).getString(USER_ID));
                params.put("course_id",model.getId());
                params.put("price",price);//model.getDiscounted_price()
                params.put("type","paid");//type means free or paid
                params.put("payment_type","online");//offline,upi,or online & others
                params.put("reciept_no","");//when payment is offline
                params.put("details","");//when payment is offline
                callApi(ApiCode.PURCHASE_COURSE, params);
                break;



        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){

            case ApiCode.FRONTEND_CODE:
                service.postDataVolley(ApiCode.FRONTEND_CODE,
                        BaseUrl.URL_FRONTEND_SETTING, params);
                break;
            case ApiCode.PURCHASE_COURSE:
                service.postDataVolley(ApiCode.PURCHASE_COURSE,
                        BaseUrl.URL_PURCHASE_COURSE, params);
                break;
          /*  case ApiCode.PAYMENT_SUCCESS:
                service.postDataVolley(ApiCode.PAYMENT_SUCCESS,
                        BaseUrl.URL_PAYMENT_SUCCESS, params);
                break;

           */

        }
    }

    @Override
    public void onResponse(int requestType, String response)  {
        switch (requestType){
            case ApiCode.FRONTEND_CODE:
                Log.e("frontend: ",response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject object=jsonObject.getJSONObject("data");
                        String course_prefix=object.getString("course_prefix");
                        //tv_course_code.setText("Course Code : "+course_prefix+course_id);
                       paymentKey=object.getString("payment_key_id");
                        merchantKey=object.getString("merchant_key");
                        light_logo=object.getString("light_logo");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                break;

            case ApiCode.PURCHASE_COURSE:
                Log.e("purchase: ",response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getBoolean("status")) {
                        JSONObject object=jObject.getJSONObject("data");
                        enrol_id=object.getString("enrol_id");
                        ENROLL_ID=enrol_id;
                        Log.e("enroll",enrol_id);

                        if (paymentKey!=null && enrol_id !=null){
                            if (ConnectivityReceiver.isConnected())
                                try {
                                    payWithRZP();
                                }catch (Exception e) {
                                    //  Log.e("paymentException",e.getMessage());
                                    e.printStackTrace();
                                }
                            else
                                CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                        }else{
                            CommonMethods.showSuccessToast(getContext(),"Something Went Wrong");
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;



        }
    }


    //Payment
    private void payWithRZP()  {
        Checkout checkout=new Checkout();
        checkout.setKeyID(paymentKey);

        checkout.setImage(R.drawable.logoo);
        final Activity activity=(MainActivity)getActivity();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "SimpleExclusive;");
            options.put("description", "Payment to purchase course");
            options.put("image", BaseUrl.BASE_URL_MEDIA+light_logo);
            // options.put("order_id", orderId);//from response of step 3.
            options.put("theme.color", "#BC0032");
            options.put("currency", "INR");

           Float price=(Math.abs(Float.parseFloat(model.getAmount())-
                   Float.parseFloat(model.getDiscounted_price())));
           COURSE_PRICE= String.valueOf(price);

           // double amount=Float.parseFloat(model.getDiscounted_price())*100;

            options.put("amount",(price*100));//pass amount in currency subunits
            JSONObject prefill=new JSONObject();
            prefill.put("email",management.getString(EMAIL));
            prefill.put("contact",management.getString(MOBILE));
            options.put("prefill",prefill);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }




}
package soonflyy.learning.hub.Student_Pannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.utlis.AppConstant.FROM;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Adapter.CourseProvideOfferAdapter;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutCourseFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    public static final String TAG = AboutCourseFragment.class.getName();

    RoundedImageView iv_thumbnail;
    TextView tv_title, tv_instructor, tv_description,tvPrice,tvOfferTitle,tvProviderTitle;

    private LinearLayout lin_title;

    private SubscribedCourse sCourse;


    private String from, courseId, courseName,tutorId="";


    //----------------//
    RoundedImageView ivCreatorImage;
    LinearLayout linCreatorImage;

    View includeCreator;
    ImageView ivEditCreator, ivDeleteCreator;
    LinearLayout linChangeCreator, linCourseDetails;
    RecyclerView recOffers, recProvides;
    TextView tvProfileName;
    CircleImageView ivProfileImg;


    String creatorImage = "", creatorName, creatorAbout,creatorId="",creatorImgUrl="";

    ArrayList<String>providerList=new ArrayList<>();
    ArrayList<String>offerList=new ArrayList<>();
    //------------------//


    public AboutCourseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        init(view);
        GridLayoutManager lm = new GridLayoutManager(requireActivity(), 2);
        lm.setOrientation(GridLayoutManager.VERTICAL);
        getArugmentData();
        return view;
    }

    private void getArugmentData() {

        from = getArguments().getString(FROM);
        if (from.equals(SIMPLEE_HOME_STUDENT)) {
            sCourse = getArguments().getParcelable("sCourse");
            setData();
        } else if (from.equals(SIMPLEE_HOME_TUTOR)) {
            //for tutor assign t0
            courseName = getArguments().getString("course_name");
            courseId = getArguments().getString("course_id");
            tutorId = getArguments().getString("tutor_id");
            setCourseOffer();
            setCourseProvide();
            linChangeCreator.setVisibility(View.VISIBLE);
            linCourseDetails.setVisibility(View.VISIBLE);
            sendApiCall();
        }

    }

    private void setData() {
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA + sCourse.getThumbnail())
                .placeholder(R.drawable.logoo).into(iv_thumbnail);
        tv_title.setText(sCourse.getTitle());
        tv_instructor.setText(sCourse.getTeacher_name());
        tv_description.setText(sCourse.getDecription());
        tvOfferTitle.setVisibility(View.GONE);
        tvProviderTitle.setVisibility(View.GONE);
       // recProvides.setVisibility(View.GONE);
    }


    private void init(View view) {

        tvOfferTitle=view.findViewById(R.id.tv_title_offer);
        tvProviderTitle=view.findViewById(R.id.tv_course);
        lin_title = view.findViewById(R.id.lin_title);
        tvPrice=view.findViewById(R.id.price_tv);

        iv_thumbnail = view.findViewById(R.id.iv_img);
        tv_title = view.findViewById(R.id.tv_title);
        tv_instructor = view.findViewById(R.id.tv_instructor);
        tv_description = view.findViewById(R.id.tv_liveclass);


        includeCreator = view.findViewById(R.id.include_creator);
        ivDeleteCreator = view.findViewById(R.id.iv_delete);
        linChangeCreator = view.findViewById(R.id.lin_chagne_creator);
        ivEditCreator = view.findViewById(R.id.iv_edit);
        tvProfileName = view.findViewById(R.id.tv_name);
        ivProfileImg = view.findViewById(R.id.iv_profile_img);

        recOffers = view.findViewById(R.id.rec_offers);
        recProvides = view.findViewById(R.id.rec_provide);
        linCourseDetails = view.findViewById(R.id.lin_course_details);

        linChangeCreator.setOnClickListener(this);
        ivDeleteCreator.setOnClickListener(this);
        ivEditCreator.setOnClickListener(this);
        recOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        recProvides.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void setCourseOffer() {
        CourseProvideOfferAdapter adapter = new CourseProvideOfferAdapter(getActivity(),
                offerList);
        recOffers.setAdapter(adapter);
        if (offerList.size()>0){
            tvOfferTitle.setVisibility(View.VISIBLE);
        }else{
            tvOfferTitle.setVisibility(View.GONE);
        }
    }

    private void setCourseProvide() {
        CourseProvideOfferAdapter adapter = new CourseProvideOfferAdapter(getActivity(),
                providerList);
        recProvides.setAdapter(adapter);
        if (providerList.size()>0){
            tvProviderTitle.setVisibility(View.VISIBLE);
        }else{
            tvProviderTitle.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //if (video_type.equals("subscription")) {
        // ((MainActivity) getActivity()).setStudentChildActionBar(sCourse.getTitle(), false);
        ((Subscribed_Course_Details) getParentFragment()).setAboutBgColor();
        ((Subscribed_Course_Details) getParentFragment()).showAssignToProfile();

        //} else if (video_type.equals("bookmark")) {
        // ((MainActivity) getActivity()).setStudentChildActionBar("My Bookmarks", false);
        //  ((BookmarkFragment) getParentFragment()).setbackgroundColor();
        // }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_chagne_creator:
                showChangeCreatorDialog("add");
                break;
            case R.id.iv_edit:
                showChangeCreatorDialog("update");
                break;
            case R.id.iv_delete:
                showDeletetMessage();
                //setApiRequest(ApiCode.DELETE_COURSE_CREATOR);
//                CommonMethods.showSuccessToast(getContext(), "deleted");
//                linChangeCreator.setVisibility(View.VISIBLE);
//                includeCreator.setVisibility(View.GONE);
                break;
        }
    }
    private void showDeletetMessage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setApiRequest(ApiCode.DELETE_COURSE_CREATOR);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void chooseImage(View v) {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("courseimage", "" + requestCode);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {
                Uri imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);

                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                //for  creator image
                Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(ivCreatorImage);
                creatorImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                linCreatorImage.setVisibility(View.VISIBLE);
                DynamicToast.make(getContext(), "Creator Photo selected for upload", Toast.LENGTH_SHORT).show();
                creatorImgUrl=imgUri.getPath();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showChangeCreatorDialog(String typ) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_course_creator);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.show();

        ///----------initialize id ------------------------//
        linCreatorImage = dialog.findViewById(R.id.lin_image);
        ivCreatorImage = dialog.findViewById(R.id.img_upload_img);
        ImageView ivCancelImage = dialog.findViewById(R.id.iv_cancle);
        ImageView ivChooseImg = dialog.findViewById(R.id.iv_upload);
        EditText etName = dialog.findViewById(R.id.et_name);
        EditText etAbout = dialog.findViewById(R.id.et_about);
        Button btnSubmit = dialog.findViewById(R.id.btn_save);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        etAbout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.et_about) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        //---------------------------------------------------------//

        //----------set data according add or update------------------//
        if (typ.equalsIgnoreCase("update")) {
            //---write code to update data-----------//
            etName.setText(creatorName);
            etAbout.setText(creatorAbout);
            Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                    .into(ivCreatorImage);
            linCreatorImage.setVisibility(View.VISIBLE);
            //------------------------------------//
        }
        ivChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose image
                chooseImage(v);
            }
        });
        ivCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linCreatorImage.setVisibility(View.GONE);
                creatorImage = "";
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatorName = etName.getText().toString().trim();
                creatorAbout = etAbout.getText().toString().trim();
                if (TextUtils.isEmpty(creatorName)) {
                    etName.setError("Enter name");
                    etName.requestFocus();
                }/*else if (TextUtils.isEmpty(creatorImage)){
                    CommonMethods.showSuccessToast(getContext(),"Choose Photo");
                } */ else if (TextUtils.isEmpty(creatorAbout)) {
                    etAbout.setError("Write here");
                    etAbout.requestFocus();
                } else {
                    //write code to call api for creat o
                    if (typ.equalsIgnoreCase("update")) {
                        setApiRequest(ApiCode.UPDATE_COURSE_CREATOR);
                    } else {
                        setApiRequest(ApiCode.CHANGE_COURSE_CREATOR);
                    }
                    dialog.dismiss();

                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //--------------------------------------------------------//


    }


    private void setApiRequest(int request) {
        HashMap<String,String>params=new HashMap<>();
        switch (request) {
            case ApiCode.GET_COURSE_BY_ID:
                params.put("type", "1");
                params.put("course_id", courseId);
                params.put("user_id",tutorId);
                callApi(ApiCode.GET_COURSE_BY_ID, params);
                break;

            case ApiCode.CHANGE_COURSE_CREATOR:
                params.put("name", creatorName);
                params.put("change_by_tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("about", creatorAbout);
                params.put("course_id", courseId);
                params.put("image", creatorImage);
                callApi(ApiCode.CHANGE_COURSE_CREATOR, params);
                break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                params.put("name", creatorName);
                params.put("change_by_tutor_id", new SessionManagement(getContext()).getString(USER_ID));
                params.put("about", creatorAbout);
                params.put("course_id", courseId);
                params.put("image", creatorImage);
                params.put("id", creatorId);
                callApi(ApiCode.UPDATE_COURSE_CREATOR, params);
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                params.put("id", creatorId);
                callApi(ApiCode.DELETE_COURSE_CREATOR, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_COURSE_BY_ID:
                service.postDataVolley(ApiCode.GET_COURSE_BY_ID,
                        BaseUrl.URL_GET_COURSE_BY_ID, params);
                break;
            case ApiCode.CHANGE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.CHANGE_COURSE_CREATOR,
                        BaseUrl.URL_CHANGE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_CHANGE_COURSE_CREATOR);
                Log.e("params:", "" + params);
                break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.UPDATE_COURSE_CREATOR,
                        BaseUrl.URL_UPDATE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_UPDATE_COURSE_CREATOR);
                Log.e("params:", "" + params);
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                service.postDataVolley(ApiCode.DELETE_COURSE_CREATOR,
                        BaseUrl.URL_DELETE_COURSE_CREATOR, params);
                Log.e("url:", BaseUrl.URL_DELETE_COURSE_CREATOR);
                Log.e("params:", "" + params);
                break;

        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.GET_COURSE_BY_ID:
                Log.e("course_details", response);
                if (jsonObject.getBoolean("status")) {
                    //set data for update
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    setCreatorProfile(jsonObject);
                    if (jsonArray.length() > 0) {
                        setDataToview(jsonArray.getJSONObject(0));
                    }
                }
                break;
            case ApiCode.CHANGE_COURSE_CREATOR:
            Log.e("COURSE_CREATOR ", response);
            if (jsonObject.getBoolean("status")) {
                creatorImage="";
                linChangeCreator.setVisibility(View.GONE);
                includeCreator.setVisibility(View.VISIBLE);
                tvProfileName.setText(creatorName);
                Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                        .into(ivProfileImg);
                sendApiCall();
                CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
            }
            break;
            case ApiCode.UPDATE_COURSE_CREATOR:
                Log.e("UPDATE_CREATOR ", ""+response);
                if (jsonObject.getBoolean("status")) {
                    creatorImage="";
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                    linChangeCreator.setVisibility(View.GONE);
                    includeCreator.setVisibility(View.VISIBLE);
                    sendApiCall();

                }
                break;
            case ApiCode.DELETE_COURSE_CREATOR:
                Log.e("DELETE_CREATOR ",""+ response);
                if (jsonObject.getBoolean("status")) {
                    linChangeCreator.setVisibility(View.VISIBLE);
                    includeCreator.setVisibility(View.GONE);
                    CommonMethods.showSuccessToast(getActivity(),jsonObject.getString("message"));
                }
                break;
        }



    }

    private void sendApiCall() {
        if (CommonMethods.checkInternetConnection(getActivity())) {
            setApiRequest(ApiCode.GET_COURSE_BY_ID);
        }
    }

    private void setCreatorProfile(JSONObject jsonObject) {
        try {
            JSONObject object=jsonObject.getJSONObject("course_crator");
            if(object.length()>0){
                linChangeCreator.setVisibility(View.GONE);
                includeCreator.setVisibility(View.VISIBLE);
                creatorId=object.getString("id");
                creatorName=object.getString("name");
                creatorAbout=object.getString("about");
                creatorImgUrl=BaseUrl.BASE_URL_MEDIA+object.getString("image");
                Log.e("creatorImgLink",""+creatorImgUrl);
                tvProfileName.setText(creatorName);
                Picasso.get().load(creatorImgUrl).placeholder(R.drawable.logoo)
                        .into(ivProfileImg);

            }else{
                linChangeCreator.setVisibility(View.VISIBLE);
                includeCreator.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setDataToview(JSONObject jsonObject) {
        try {

            courseName = jsonObject.getString("title");
            tv_title.setText(courseName);
//            details = jsonObject.getString("short_description");
           String about = jsonObject.getString("description");
           tv_description.setText(about);
            String price = jsonObject.getString("amount");
            String uFree_paid = jsonObject.getString("is_free_course");
//            uCat_id = jsonObject.getString("category_id");
//            u_expy_date = jsonObject.getString("validity");
//            offer_price = jsonObject.getString("offer_course_price");
           String live_dem_count = jsonObject.getString("live_demo_count");
           offerList.clear();
           if (Integer.parseInt(live_dem_count)>0){
               offerList.add("You'll get "+live_dem_count+" Demo live classes with this course free");
           }
           setCourseOffer();
//            //corseOffer
//
//            String str = jsonObject.getString("course_offers");
//////
//            JSONArray jsonArray = new JSONArray(str);
//            u_courseOffer.clear();
//            if (jsonArray.length() > 0) {
//                u_courseOffer.add(jsonArray.getString(0));
//                u_courseOffer.add(jsonArray.getString(1));
//            }
//            if (u_courseOffer.size() > 0) {
//                courseOffer.clear();
//                courseOffer.addAll(u_courseOffer);
//                tv_courseoffer.setText(u_courseOffer.get(1));
//            }
            //----------for course provider---//
          JSONArray providerJsonARray = new JSONArray(jsonObject.getString("course_provider"));
           providerList.clear();
            for (int i = 0; i < providerJsonARray.length(); i++) {
                int value = providerJsonARray.getInt(i);
                providerList.add(CommonMethods.getCourseProvide()[value]);
            }
            setCourseProvide();
            //--------------------------//
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + jsonObject.getString("thumbnail"))
                    .into(iv_thumbnail);
//    -------------set price-----------//
            tvPrice.setVisibility(View.VISIBLE);
            if (uFree_paid.equals("1")) {
                tvPrice.setText("Free");
            } else {
                if (price == null || TextUtils.isEmpty(price)) {
                    price = "--";
                } else {
                    if (price.equals("null")) {
                        price = "--";
                    }

                }
                tvPrice.setText(getString(R.string.Rs_symbol)+" "+price);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
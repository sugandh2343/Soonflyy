package soonflyy.learning.hub.Common_Package.IDSection;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION_USER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.STORAGE_READ_WRIT_REQUEST_CODE;
import static soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity.SCHOOL_ID_SECTION_USER_TYPE;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.YourSchooolPannel.SchoolMainActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class IDBasicdetailFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {
    Button btn_submit;
    SwipeRefreshLayout swipe;
    EditText et_name, et_education, et_interest, et_workplace, et_work, et_address;
    ImageView iv_upload;
    CircleImageView iv_cancel_image;
    RelativeLayout rel_image;
    RoundedImageView iv_profile;
    ActivityResultLauncher<Intent> activityResultLauncher;

    String type="add";
    String name,education,interest,workplace,work,address,profilePhotoString="";
    String uName,uEducation,uInterest,uWorkplace,uWork,uAddress;
   String user_type;






    public IDBasicdetailFragment() {
        // Required empty public constructor
    }

    public static IDBasicdetailFragment newInstance(String param1, String param2) {
        IDBasicdetailFragment fragment = new IDBasicdetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_i_d_basicdetail, container, false);


        initview(view);
      getArgumentData();
      createActivityLauncher();
        init_swipe_method();
       // if (type.equals("update")) {
            sendApiRequest();
       // }
        clickListener();
        return view;
    }
    private void createActivityLauncher() {
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //for android 11 and above
                        if (result.getResultCode()==RESULT_OK) {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager())
                                    CommonMethods.showSuccessToast(getActivity(),"Permission Granted");
                                    //  Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                                else
                                    CommonMethods.showSuccessToast(getActivity(),"Permission Denied");
                                //Toast.makeText(getActivity().getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    private void getArgumentData() {
        user_type=getArguments().getString("user_type");
        type=getArguments().getString("type");
        if (type.equals("add")){
            btn_submit.setText("Submit");
        }else{
            btn_submit.setText("Update");
        }
    }

    private void clickListener() {
        btn_submit.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
        iv_cancel_image.setOnClickListener(this);
    }

    private void initview(View view) {
        swipe = view.findViewById(R.id.swipe);
        btn_submit = view.findViewById(R.id.btn_submit);
        et_name = view.findViewById(R.id.et_name);
        et_education = view.findViewById(R.id.et_education);
        et_interest = view.findViewById(R.id.et_interest);
        et_workplace = view.findViewById(R.id.et_workplace);
        et_address = view.findViewById(R.id.et_address);
        et_work = view.findViewById(R.id.et_work);
        iv_upload = view.findViewById(R.id.iv_sub);

        iv_cancel_image=view.findViewById(R.id.cancel_image);
        rel_image=view.findViewById(R.id.rel_image);
        iv_profile=view.findViewById(R.id.img_upload_img);
    }

    private void init_swipe_method() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipe.setRefreshing(true);
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
        if (ConnectivityReceiver.isConnected()){
         //  if (type.equals("update")){
                sendRequest(ApiCode.GET_BASIC_DETAILS);
        //  }
            //call api
        }else{
            CommonMethods.showSuccessToast(getActivity(),"No Internet Connection");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
              submit();
                break;
            case R.id.iv_sub:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //for android 11 and above
                    if (CommonMethods.checkStoragePermission(getActivity())){
                        chooseProfileImage();
                    }else{
                        CommonMethods.requestStoragePermission(getActivity(),STORAGE_READ_WRIT_REQUEST_CODE,activityResultLauncher);
                    }
                } else {
                    //for android 10 or bellow 10
                    if (CommonMethods.checkReadPermission(getContext())) {
                        Log.e("permissionChecked", "trye");
                        chooseProfileImage();

                    } else
                        CommonMethods.requestPermission(getActivity(), 123);
                }
                break;
              case  R.id.cancel_image:
                  profilePhotoString="";
                  rel_image.setVisibility(View.GONE);
                  break;
        }
    }

    private void submit() {
        if (validateField()){
            if (ConnectivityReceiver.isConnected()){
                //call update and add methods
              //  if (type.equals("add")){
                    //call add api
                    sendRequest(ApiCode.ADD_BASIC_DETAILS);
               // }else{
                    //call update api
              //  }
            }else{
                CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
            }
        }
    }

    private boolean validateField() {
        if (validateEmptyField()){
            if (type.equals("update")){
                if (checkDataChange()){
                    return true;
                }else{
                    CommonMethods.showSuccessToast(getContext(),"You have not change anything");
                    return false;
                }
            }else {
                return true;
            }
        }
        return false;
    }

    private boolean checkDataChange() {
        if (!name.equals(uName)){
            return true;
        }
        if (!education.equals(uEducation)){
            return true;
        }
        if (!interest.equals(uInterest)){
            return true;
        }
        if (!workplace.equals(uWorkplace)){
            return true;
        }
        if (!work.equals(uWork)){
            return true;
        }
        if (!address.equals(uAddress)){
            return true;
        }
        if (!TextUtils.isEmpty(profilePhotoString)){
            return true;
        }
        return false;
    }

    private boolean validateEmptyField() {
        name=et_name.getText().toString().trim();
       education= et_education.getText().toString().trim();
        interest=et_interest.getText().toString().trim();
        workplace=et_workplace.getText().toString().trim();
        work=et_work.getText().toString().trim();
        address=et_address.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            et_name.setError("Enter your name");
            et_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(education)){
            et_education.setError("Enter your education");
            et_education.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(interest)){
            et_interest.setError("Enter your interest");
            et_interest.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(workplace)){
            et_workplace.setError("Enter your workplace");
            et_workplace.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(work)){
            et_work.setError("Enter your work");
            et_work.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)){
            et_address.setError("Enter your address");
            et_address.requestFocus();
            return false;
        }
//        if (TextUtils.isEmpty(profilePhotoString)){
//            CommonMethods.showSuccessToast(getContext(),"Choose your photo");
//            return false;
//        }
        return true;
    }

    private void chooseProfileImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==999 && resultCode==RESULT_OK){
            try {
                Uri imgUri = data.getData();
                Glide.with(getActivity()).load(imgUri).into(iv_profile);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                profilePhotoString= Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
                rel_image.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendRequest(int request){
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.GET_USER_DETAILS:
               // params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                callApi(ApiCode.GET_USER_DETAILS,params);
                break;
            case ApiCode.ADD_BASIC_DETAILS:
               // params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));
                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                params.put("name",name);
                params.put("education",education);
                params.put("interest",interest);
                params.put("workplace",workplace);
                params.put("work",work);
                params.put("address",address);
                params.put("photo",profilePhotoString);

                setTypeParameter(params);

                callApi(ApiCode.ADD_BASIC_DETAILS,params);
                break;
            case ApiCode.GET_BASIC_DETAILS:
               // params.put("user_id",new SessionManagement(getActivity()).getString(USER_ID));

                params.put("user_id",new SessionManagement(getActivity()).getString(ID_SECTION_USER_ID));
                setTypeParameter(params);
                callApi(ApiCode.GET_BASIC_DETAILS,params);
                break;
//            case ApiCode.UPDATE_PROFILE_IMAGE:
//                params.put("user_id",new Session_management(getActivity()).getString(USER_ID));
//                params.put("name",name);
//                params.put("education",education);
//                params.put("interest",interest);
//                params.put("workplace",workplace);
//                params.put("work",work);
//                params.put("address",address);
//                params.put("photo",profilePhotoString);
//               // callApi(ApiCode.UPDATE_PROFILE_IMAGE,params);
//                break;
        }

    }

    private void setTypeParameter(HashMap<String, String> params) {
        if (user_type.equals(SCHOOL_COACHING)){
            if (SCHOOL_ID_SECTION_USER_TYPE.equals(SCHOOL_COACHING)){
                params.put("type","0");
            }else{
                params.put("type","1");
            }

        }else {
            params.put("type","1");
        }
    }

    public void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.GET_USER_DETAILS:
                service.postDataVolley(ApiCode.GET_USER_DETAILS
                        , BaseUrl.URL_GET_USER_DETAILS, params);
                break;
            case ApiCode.ADD_BASIC_DETAILS:
                service.postDataVolley(ApiCode.ADD_BASIC_DETAILS
                        , BaseUrl.URL_ADD_BASIC_DETAILS, params);
                break;
            case ApiCode.GET_BASIC_DETAILS:
                service.postDataVolley(ApiCode.GET_BASIC_DETAILS
                        , BaseUrl.URL_GET_BASIC_DETAILS, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.GET_USER_DETAILS:
                Log.e("profile_details",response);
                break;
            case ApiCode.ADD_BASIC_DETAILS:
                Log.e("add_bdetials",response);
                if(jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                    if (user_type.equals(SCHOOL_COACHING)){
                        //for school user
                        //new SessionManagement(getActivity()).setString(SCHOOL_ID_SECTION,"1");
                        ((SchoolMainActivity)getActivity()).setIdSectionStatus("1");
                    }else{
                        //for home user
                        new SessionManagement(getActivity()).setString(ID_SECTION,"1");
                    }


                    sendApiRequest();
                }else{
                    CommonMethods.showSuccessToast(getContext(),"Something Went Wrong");
                }
                break;
            case ApiCode.GET_BASIC_DETAILS:
                Log.e("b_details",response);
                if(jsonObject.getBoolean("status")){
                    JSONObject object=jsonObject.getJSONObject("userdatails");
                    et_name.setText(object.getString("name"));
                    et_education.setText(object.getString("education"));
                    et_interest.setText(object.getString("interest"));
                    et_workplace.setText(object.getString("workplace"));
                    et_work.setText(object.getString("work"));
                    et_address.setText(object.getString("address"));
                    Picasso.get().load(BaseUrl.BASE_URL_MEDIA+object.getString("photo"))
                            .into(iv_profile);
                    rel_image.setVisibility(View.VISIBLE);

                }
//
                break;
        }
    }


}
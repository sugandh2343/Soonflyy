package soonflyy.learning.hub.YourSchooolPannel;

import static android.app.Activity.RESULT_OK;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_S_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_IMAGE;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ScholProfileFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

CircleImageView profile_image;
Button update_btn,logout_btn;
String from,id,imageString="",type;
ImageView iv_edit;
SessionManagement management;

    public ScholProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schol_profile, container, false);
        management=new SessionManagement(getActivity());
        bindView(view);
        getArgumentData();

        update_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        return view;
    }

    private void getArgumentData() {
        from=getArguments().getString("from");
      id=getArguments().getString("id");
      type=getArguments().getString("type");
      setImage(from);
    }

    private void setImage(String from) {
        String profileUrl=BaseUrl.BASE_URL_MEDIA;
        switch (from){
            case SCHOOL_STUDENT:
                profileUrl=profileUrl+management.getString(SCHOOL_S_IMAGE);
                break;
            case SCHOOL_TUTOR:
                profileUrl=profileUrl+management.getString(SCHOOL_T_IMAGE);
                break;
            case SCHOOL_COACHING:
                profileUrl=profileUrl+management.getString(SCHOOL_IMAGE);
                break;
            case INDEPENDENT_TUTOR:
                profileUrl=profileUrl+management.getString(SCHOOL_IT_IMAGE);
                break;
        }
        Picasso.get().load(profileUrl).placeholder(R.drawable.profile).into(profile_image);
    }

    private void bindView(View view) {
        logout_btn=view.findViewById(R.id.btn_logout);
        profile_image=view.findViewById(R.id.user_imge);
        update_btn=view.findViewById(R.id.update_btn);
        iv_edit=view.findViewById(R.id.edit_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
              // showLogoutAlert();
                CommonMethods.showLogoutDialog(getActivity(),from);
                break;
            case R.id.user_imge:
            case R.id.edit_img:
                Log.e("image","click");
                if (CommonMethods.checkReadPermission(getContext())) {
                    chooseImage();
                }else{
                    CommonMethods.requestPermission(getActivity(),234);
                }
                break;
            case R.id.update_btn:
                Log.e("image","click");
                if (TextUtils.isEmpty(imageString)){
                    CommonMethods.showSuccessToast(getActivity(),"Choose profile Image");
                }else{
                    if (CommonMethods.checkInternetConnection(getActivity())){
                        //call api
                        sendRequest(ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE);
                    }
                }
                break;
        }
    }

    private void chooseImage() {
        Log.e("image","choose");
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(111);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==111 && resultCode==RESULT_OK){
            try {
                Uri imgUri = data.getData();
                Glide.with(getActivity()).load(imgUri).into(profile_image);

                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                imageString= Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SchoolMainActivity)getActivity()).setActionBarTitle("Profile");
    }
    ///---api call--//

    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request) {
            case ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE:
                params.put("image",imageString);
                params.put("type", type);
                params.put("user_id",id);
                callApi(ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE, params);
                break;
        }
    }

    private void callApi(int request, HashMap<String, String> params) {

        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request) {
            case ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE:
                service.postDataVolley(ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE,
                        BaseUrl.URL_SCHOOL_UPDATE_PROFILE_IMAGE, params);
                Log.e("api", BaseUrl.URL_SCHOOL_UPDATE_PROFILE_IMAGE);
                Log.e("params", params.toString());
                break;


        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        switch (requestType) {
            case ApiCode.SCHOOL_UPDATE_PROFILE_IMAGE:
                Log.e("sc_profile", response.toString());
                if (jsonObject.getBoolean("status")){
                    CommonMethods.showSuccessToast(getContext(),jsonObject.getString("message"));
                   updateSession(jsonObject.getString("image"));
                }
                break;


        }
    }

    private void updateSession(String imageurl) {
            String profileUrl=BaseUrl.BASE_URL_MEDIA;
            switch (from){
                case SCHOOL_STUDENT:
                    management.setString(SCHOOL_S_IMAGE,imageurl);
                  // profileUrl=profileUrl+management.getString(SCHOOL_S_IMAGE);
                    break;
                case SCHOOL_TUTOR:
                    management.setString(SCHOOL_T_IMAGE,imageurl);
                  //  profileUrl=profileUrl+management.getString(SCHOOL_T_IMAGE);
                    break;
                case SCHOOL_COACHING:
                    management.setString(SCHOOL_IMAGE,imageurl);
                   // profileUrl=profileUrl+management.getString(SCHOOL_IMAGE);
                    break;
                case INDEPENDENT_TUTOR:
                    management.setString(SCHOOL_IT_IMAGE,imageurl);
                    //profileUrl=profileUrl+management.getString(SCHOOL_IT_IMAGE);
                    break;
            }
          //  Picasso.get().load(profileUrl).placeholder(R.drawable.profile).into(profile_image);
        }



    private void showLogoutAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to logout ?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new SessionManagement(getActivity()).clearLogoutSession();
                getActivity().finish ();
            }
        }).show();
    }
}
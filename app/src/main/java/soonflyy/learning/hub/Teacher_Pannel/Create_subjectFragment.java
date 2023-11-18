package soonflyy.learning.hub.Teacher_Pannel;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.CreateSubjectAdapter;
import soonflyy.learning.hub.Teacher_Pannel.Model.SubjectModel;
import soonflyy.learning.hub.Volley.VolleyResponseListener;
import soonflyy.learning.hub.Volley.VolleyService;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Create_subjectFragment extends Fragment implements View.OnClickListener, VolleyResponseListener {

    LinearLayout btn_text;
    ImageView iv_addbutton;
    RecyclerView rv_subject;
    NestedScrollView nestedScrollView;
    ArrayList<SubjectModel> subjectList = new ArrayList<>();
    CreateSubjectAdapter subjectAdapter;
    String imageString;
    int adapterPosition;
    //

    //EditText et_name;
    ImageView choose_image, cover_image;
    RelativeLayout rel_image;
    CircleImageView cancel_image;
    TextView tv_imgName;
    String course_id,course_title;

    public Create_subjectFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_subject, container, false);
        initview(view);
        course_id=getArguments().getString("course_id");
        course_title=getArguments().getString("course_title");
        createSubject();
        ((Create_Course_MainFragment) getParentFragment()).changeTrackerColor(2);
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (getSubjectList()){
                   if (ConnectivityReceiver.isConnected()){
                       sendRequest(ApiCode.ADD_SECTION);
                   }else{
                       CommonMethods.showSuccessToast(getContext(),"No Internet Connection");
                   }
               }

            }
        });
        return view;
    }

    private void createSubject() {
        subjectList.add(new SubjectModel(""));
        subjectAdapter = new CreateSubjectAdapter(getContext(), subjectList, new CreateSubjectAdapter.OnImageChoose() {
            @Override
            public void onChooseImage(int position) {
                adapterPosition = position;
                initializeView(position);
                chooseImage();
            }

            @Override
            public void onCancelImage(int position) {
                Log.e("cancel","yes");
                adapterPosition=position;
                initializeView(position);
                setViewOnRemoveImage();
            }

            @Override
            public void onRemoveSubject(int position) {
                subjectList.remove(position);
                subjectAdapter.notifyDataSetChanged();

            }
        });
        rv_subject.setAdapter(subjectAdapter);
        subjectAdapter.notifyDataSetChanged();


    }

    private void setViewOnRemoveImage() {
        rel_image.setVisibility(View.GONE);
        choose_image.setVisibility(View.VISIBLE);
        tv_imgName.setText("Upload your file here,Choose file form your device");
        imageString=" ";
        subjectList.get(adapterPosition).setSection_thumbnail(imageString);
        subjectList.get(adapterPosition).setImgUri(null);
        subjectAdapter.notifyDataSetChanged();
    }

    private void initializeView(int position) {
        RecyclerView.ViewHolder viewHolder = rv_subject.findViewHolderForAdapterPosition(position);
        //  assert viewHolder != null;
        View view = viewHolder.itemView;
        choose_image = view.findViewById(R.id.iv_sub);
        rel_image = view.findViewById(R.id.rel_cover_image);
        cancel_image = view.findViewById(R.id.cancel_image);
        tv_imgName = view.findViewById(R.id.tv_name_image);
        cover_image = view.findViewById(R.id.image_view);
    }

    private void initview(View view) {
        nestedScrollView=view.findViewById(R.id.subject_nested);
        btn_text = view.findViewById(R.id.btn_text);
        iv_addbutton = view.findViewById(R.id.iv_addbutton);
        rv_subject = view.findViewById(R.id.rv_subject);
        rv_subject.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_text.setOnClickListener(this);
        iv_addbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_addbutton:
                //showAddDailoge();
                if (subjectAdapter.getItemCount()<3){
                subjectList.add(new SubjectModel(""));
                subjectAdapter.notifyDataSetChanged();
                }else{
                    CommonMethods.showSuccessToast(getContext(),"You can't create more than 3 subject at a time");
                }
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                
                break;
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
                subjectList.get(adapterPosition).setImgUri(imgUri);
                // Glide.with(this).load(imgUri).into(user_imge);
                Picasso.get().load(imgUri).placeholder(R.drawable.image_gallery_24px).into(cover_image);
                ContentResolver cr = getActivity().getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                setViewForAdapter();

//                //course_thumbnail_img.setVisibility(View.VISIBLE);
//                rel_coverImg.setVisibility(View.VISIBLE);
//                upload_video_btn.setVisibility(View.GONE);
//                tv_imageName.setText("cover_image.pdf");
                DynamicToast.make(getContext(), "Cover image selected for upload", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void setViewForAdapter() {
        choose_image.setVisibility(View.GONE);
        rel_image.setVisibility(View.VISIBLE);
        tv_imgName.setText("sub_img.jpeg");
        subjectList.get(adapterPosition).setSection_thumbnail(imageString);
        subjectAdapter.notifyDataSetChanged();
    }
    
    private boolean getSubjectList(){
        boolean result=true;
        ArrayList<SubjectModel> list=new ArrayList<>();
        if (subjectAdapter.getItemCount()>0) {
            for (int i = 0; i < subjectAdapter.getItemCount(); i++) {
                RecyclerView.ViewHolder viewHolder = rv_subject.findViewHolderForAdapterPosition(i);
                if (viewHolder == null) {
                    viewHolder = subjectAdapter.holderHasMap.get(i);
                }
                //  assert viewHolder != null;
                View view = viewHolder.itemView;

                EditText et_name = view.findViewById(R.id.et_name);
                String name = et_name.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    CommonMethods.showSuccessToast(getContext(), "Subject title require");
                    result = false;
                    break;
                } else if (TextUtils.isEmpty(subjectList.get(i).getSection_thumbnail())) {
                    CommonMethods.showSuccessToast(getContext(), "Subject thumbnail require");
                    result = false;
                    break;
                } else {
                    subjectList.get(i).setTitle(name);
                }
            }
        }else{
            result=false;
            CommonMethods.showSuccessToast(getActivity(),"Please add at least one subject");
        }
        return result;
    }



    private void sendRequest(int request) {
        HashMap<String, String> params = new HashMap<>();
        switch (request){
            case ApiCode.ADD_SECTION:
                String data=new Gson().toJson(subjectList);
                params.put("data",""+data);
               // Log.e("data", data);
                // if (liveType.equals("course_wise")) {
               params.put("course_id",course_id);
                // params.put("user_id",new Session_management(getContext()).getString(USER_ID));
                callApi(ApiCode.ADD_SECTION, params);
                break;
        }
    }
    private void callApi(int request, HashMap<String, String> params) {
        VolleyResponseListener callback = this;
        VolleyService service = new VolleyService(callback, getActivity());
        switch (request){
            case ApiCode.ADD_SECTION:
                service.postDataVolley(ApiCode.ADD_SECTION,
                        BaseUrl.URL_ADD_SECTION, params);
                break;
        }
    }

    @Override
    public void onResponse(int requestType, String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        switch (requestType){
            case ApiCode.ADD_SECTION:
                Log.e("subject ",response);
                if (jsonObject.getBoolean("status")){
                    CreateTestFragment fragment=new CreateTestFragment();
                    Bundle bundle=new Bundle( );
                    bundle.putString("course_id",course_id);
                    bundle.putString("course_title",course_title);
                    fragment.setArguments(bundle);
                    ((Create_Course_MainFragment) getParentFragment()).switchFragment(fragment);

                }
                break;
        }
    }
}
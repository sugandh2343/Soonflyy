package soonflyy.learning.hub.adapter;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.MyCourseDetailModel;

public class PendingCourseAdapter extends RecyclerView.Adapter<PendingCourseAdapter.HolderPendingCourse> {
    private Context context;
    ArrayList<MyCourseDetailModel> list;

    public PendingCourseAdapter(Context context , ArrayList<MyCourseDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PendingCourseAdapter.HolderPendingCourse onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pending_course_single_row , parent , false);
        return new PendingCourseAdapter.HolderPendingCourse(view);    }

    @Override
    public void onBindViewHolder(@NonNull PendingCourseAdapter.HolderPendingCourse holder , int position) {
        MyCourseDetailModel model=list.get(position);
        Log.e("MINONJB",model.getCourse_id());
        Picasso.get().load(model.getCourse_thumbnail())
                .placeholder(R.drawable.logoo)
                .into(holder.imgCourse);
//        Log.e("course_thumbnail ",BaseUrl.BASE_URL_MEDIA +model.getThumbnail());
        holder.txtTitle.setText(model.getTitle());
        holder.tv_subscription.setText("Subscription: " + model.getIs_subscriptions());
        if (model.getIs_free_course()!=null && model.getIs_free_course().equals("0")) {
            if (model.getAmount() == null || TextUtils.isEmpty(model.getAmount()))
                holder.tv_amount.setText("--");
            else
                holder.tv_amount.setText(context.getResources().getString(R.string.Rs_symbol) + " " + model.getAmount());
        } else
            holder.tv_amount.setText("Free");
//        String creadteDate = CommonMethods.changeDateTimeFmt("yyyy-MM-dd" , "dd-MMM-yyyy" , model.getCreated_at());
        holder.tv_date.setText("Created on: " + model.getCreated_at());
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("status","Accepted");
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");

                reference.child(model.getAssign_by_id()).child(model.getCourse_id())
                        .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                sendNotification(model.getAssign_by_id());
                            }
                        });


            }
        });
        holder.btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void sendNotification(String assignToId) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.child(assignToId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token=snapshot.child("token").getValue(String.class);
                Log.e("ToKEN",token);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject js = new JSONObject();
                try {
                    JSONObject jsonobject_notification = new JSONObject();

                    jsonobject_notification.put("title", "Course Accepted");
                    jsonobject_notification.put("body", "Tutor has accepted your assigned course request");


                    JSONObject jsonobject_data = new JSONObject();
                    jsonobject_data.put("imgurl", "https://firebasestorage.googleapis.com/v0/b/haajiri-register.appspot.com/o/Notifications%2Fic_app_logo1.png?alt=media&token=d77857f1-b0e0-4b15-9934-3ec8a88903e9");

                    //JSONObject jsonobject = new JSONObject();

                    js.put("to", token);
                    js.put("notification", jsonobject_notification);
                    js.put("data", jsonobject_data);

                    //js.put("", jsonobject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "https://fcm.googleapis.com/fcm/send";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.e("response1234", String.valueOf(response));
//                        reference.child(hrUid).child("Industry").child(getSharedPreferences("UserDetails",MODE_PRIVATE).getString("industryName",""))
//                                .child("Site").child(String.valueOf(siteId))
//                                .child("Leaves").child(timestamp).updateChildren(hashMap);




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error :  ", String.valueOf(error));


                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer AAAAhfafC20:APA91bFfUwgpuJjofgA05BWhsrnfleqjrhpfTMBNtACD1JDBRZCXCsjcWmGL2yV2FZ_Rdpmz7vc_h_aVVIRGN6JOX0zc6ndqGEjTQfufHUgTF9Z9-SUa3jwEQxVBWGlm393Doz4QT9mj");//TODO add Server key here
                        //headers.put("Content-Type", "application/json");
                        return headers;
                    }

                };
                requestQueue.add(jsonObjReq);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderPendingCourse extends RecyclerView.ViewHolder {
        ImageView img_edit, iv_forward;//courseImage,
        TextView txtTitle, tv_subscription, tv_amount, tv_date;
        RoundedImageView imgCourse;
        ProgressBar progressBar;
        CardView cv_1, cv_edi;
        RelativeLayout rel_img, rel_btn_live, relProfile, linParent;
        View viewLine;
        TextView tvProfileName, tvType;
        ImageView tvMobile;
        Button btnProfile,btn_accept,btn_deny;
        CircleImageView ivProfileImg;
        TextView tv_opendailoge, txt_upi_id, dailog_delete;
        LinearLayout lin_main;

        public HolderPendingCourse(@NonNull View itemView) {
            super(itemView);
            linParent = itemView.findViewById(R.id.parent);
            lin_main = itemView.findViewById(R.id.lnr_main);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            iv_forward = itemView.findViewById(R.id.iv_forward);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            img_edit = itemView.findViewById(R.id.img_edit);
            imgCourse = itemView.findViewById(R.id.imgCourse);
            rel_img = itemView.findViewById(R.id.rel_img);
            rel_btn_live = itemView.findViewById(R.id.rel_btn_live);
            img_edit = itemView.findViewById(R.id.img_edit);
            txtTitle = itemView.findViewById(R.id.tv_c_title);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_subscription = itemView.findViewById(R.id.tv_subcriber);
            tv_date = itemView.findViewById(R.id.tv_date);
            progressBar = itemView.findViewById(R.id.progressBar);

            relProfile = itemView.findViewById(R.id.rel_profile);
            tvProfileName = itemView.findViewById(R.id.assign_tv_name);
            ivProfileImg = itemView.findViewById(R.id.assign_iv_profile_img);
            tvMobile = itemView.findViewById(R.id.assign_tv_mobile);
            tvType = itemView.findViewById(R.id.tv_type);
            btnProfile = itemView.findViewById(R.id.btn_profile);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_deny = itemView.findViewById(R.id.btn_deny);
            viewLine = itemView.findViewById(R.id.view1);
            btnProfile.setVisibility(View.VISIBLE);
        }
    }
}

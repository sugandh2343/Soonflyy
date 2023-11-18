package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.model.TeacherSubject_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherSubject_Adapter extends RecyclerView.Adapter<TeacherSubject_Adapter.ViewHolder> {

    Context context;
    ArrayList<TeacherSubject_Model> list;
    OnCourseClickListener listener;
    int count = -1;
    String type;

    public interface OnCourseClickListener {
        void onItemClick(int postion);

        //  void onSubscriptionClick(int position);

        void onDelete(int position);

        void onEdit(int position);

        void onProfileClick(int position);
    }

    public TeacherSubject_Adapter(Context context, String type, ArrayList<TeacherSubject_Model> list, OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_teacher_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;
        TeacherSubject_Model model = list.get(adapterPosition);
        Log.e("imgLInk",BaseUrl.BASE_URL_MEDIA + model.getCover_image());
        Picasso.get().load(model.getCover_image()).placeholder(R.drawable.logoo).into(holder.iv_thumnail);
        if (adapterPosition + 1 < 10)
            holder.tv_sno.setText("Subject 0" + (adapterPosition + 1));
        else
            holder.tv_sno.setText("Subject " + (adapterPosition + 1));
        holder.tv_title.setText(model.getTitle());
        Log.e("Asknafn",model.getAssigm_to_id());
        Log.e("Asknafn",FirebaseAuth.getInstance().getUid());
        if(model.getStatus().equals("Accepted")){
            holder.lin_accept_deny.setVisibility(View.GONE);

        }else{
            holder.lin_accept_deny.setVisibility(View.VISIBLE);
        }
        if(model.getAssigm_to_id()!=null){
            holder.assign_profile.setVisibility(View.VISIBLE);
            if(FirebaseAuth.getInstance().getUid().equals(model.getAssign_by_id())){
                holder.tvType.setText("Assign To");
                holder.ivProfileImg.setImageResource(R.drawable.logoo);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                reference.child(model.getAssigm_to_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tvProfileName.setText(snapshot.child("name").getValue(String.class));

                        Picasso.get().load(snapshot.child("image").getValue(String.class)).placeholder(R.drawable.logoo)
                                .into(holder.ivProfileImg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            else{
                holder.tvType.setText("Assign By");
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                reference.child(model.getAssign_by_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tvProfileName.setText(snapshot.child("name").getValue(String.class));

                        Picasso.get().load(snapshot.child("image").getValue(String.class)).placeholder(R.drawable.logoo)
                                .into(holder.ivProfileImg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }else{
            holder.assign_profile.setVisibility(View.GONE);


        }

        //_----------set assigned------------//
//        if (type.equals("assignTo")) {
//            holder.img_edit.setVisibility(View.VISIBLE);
//            if (model.getAssigned_value().equals("1")) {//1 for assign to
//                holder.linProfile.setVisibility(View.VISIBLE);
//                holder.tvType.setText("Assigned to");
//                setAssignProfile(holder, model.getAssigned_to());
//            } else if (model.getAssigned_value().equals("-1")) {//0 for not assigned
//                holder.linProfile.setVisibility(View.GONE);
//
//            }
//        }
//        else if (type.equals("AssignSub")) {// for assign subject by
//            holder.tv_sno.setText("Course: " + model.getCourse_name());
//            holder.tv_title.setText("Subject: " + model.getTitle());
//            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + model.getCover_image())
//                    .placeholder(R.drawable.logoo).into(holder.iv_thumnail);
//            holder.linProfile.setVisibility(View.VISIBLE);
//            holder.tvType.setText("Assigned by");
//            setAssignProfile(holder, model.getAssigned_by());
//            holder.img_edit.setVisibility(View.GONE);
//        }
        //----------------------------------//

        holder.tv_opendailoge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });

        holder.card_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count == position) {
            holder.cv_edi.setVisibility(View.VISIBLE);
        } else {
            holder.cv_edi.setVisibility(View.GONE);
        }
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = adapterPosition;
                if (holder.cv_edi.getVisibility() == View.VISIBLE) {
                    holder.cv_edi.setVisibility(View.GONE);
                } else {
                    holder.cv_edi.setVisibility(View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Courses");
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("status","Accepted");
                reference.child(model.getAssign_by_id()).child(model.getCourse_id()).child("Subject")
                        .child(model.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });
        holder.btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Courses");
                reference.child(model.getAssigned_by().getId()).child(model.getCourse_id()).child("Subject")
                        .child(model.getId()).child("assign_by_id").removeValue();
                reference.child(model.getAssigned_by().getId()).child(model.getCourse_id()).child("Subject")
                        .child(model.getId()).child("assign_to_id").removeValue();
                reference.child(model.getAssigned_by().getId()).child(model.getCourse_id()).child("Subject")
                        .child(model.getId()).child("assign_to_name").removeValue();

            }
        });

        holder.dailog_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEdit(adapterPosition);
            }
        });

        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition);
            }
        });

        holder.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileClick(adapterPosition);
            }
        });

    }

    private void setAssignProfile(ViewHolder holder, AssignProfile model) {
        holder.tvProfileName.setText(model.getName());

        String link = BaseUrl.BASE_URL_MEDIA + model.getImage();
        Log.e("imgLink", "" + link);
        Picasso.get().load(link).placeholder(R.drawable.logoo)
                .into(holder.ivProfileImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sno, tv_title;
        TextView tv_opendailoge, txt_upi_id, dailog_edit, dailog_delete;
        CardView cv_edi;
        LinearLayout card_main;
        RadioButton redio1;
        ImageButton btn_edit, btn_delet;
        ImageView img_edit;
        RoundedImageView iv_thumnail;

        LinearLayout linProfile,lin_accept_deny;
        TextView tvProfileName, tvType;
        ImageView tvMobile;
        Button btnProfile,btn_accept,btn_deny;
        CircleImageView ivProfileImg;
        View assign_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_opendailoge = itemView.findViewById(R.id.tv_opendailoge);
            dailog_edit = itemView.findViewById(R.id.dailog_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            card_main = itemView.findViewById(R.id.card_main);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            img_edit = itemView.findViewById(R.id.img_edit);
            tv_sno = itemView.findViewById(R.id.header_tittle_tv);
            tv_title = itemView.findViewById(R.id.header_tv_discription);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_deny = itemView.findViewById(R.id.btn_deny);
            iv_thumnail = itemView.findViewById(R.id.img);

            //-------profile view------------//
            linProfile = itemView.findViewById(R.id.lin_profile);
            tvProfileName = itemView.findViewById(R.id.assign_tv_name);
            ivProfileImg = itemView.findViewById(R.id.assign_iv_profile_img);
            tvMobile = itemView.findViewById(R.id.assign_tv_mobile);
            assign_profile = itemView.findViewById(R.id.assign_profile);
            lin_accept_deny = itemView.findViewById(R.id.lin_accept_deny);
            tvType = itemView.findViewById(R.id.tv_type);
            btnProfile = itemView.findViewById(R.id.btn_profile);
            btnProfile.setVisibility(View.VISIBLE);
            tvType.setText("Assigned to");
            //-------------------------//
        }
    }

}

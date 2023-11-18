package soonflyy.learning.hub.adapter;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import soonflyy.learning.hub.model.MyCourseDetailModel;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder> {
    Context context;
    ArrayList<MyCourseDetailModel> list;
    OnCourseClickListener listener;
    int count = -1;
    String type;


    public interface OnCourseClickListener {
        void onItemClick(int postion);

        void onSubscriptionClick(int position);

        void onDelete(int position);

        void onEdit(int position);

        void onGoLive(int position);

        void onProfileClick(int position);
    }

    public MyCourseAdapter(Context context , String type , ArrayList<MyCourseDetailModel> list ,
                           OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type = type;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_mycourse , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder , int position) {
        int adapterPosition = position;
        MyCourseDetailModel model = list.get(position);
//        Log.e("Thumbnail",model.getCourse_thumbnail());
        Picasso.get().load(model.getCourse_thumbnail())
                .placeholder(R.drawable.logoo)
                .into(holder.imgCourse);
//        Log.e("course_thumbnail ",BaseUrl.BASE_URL_MEDIA +model.getThumbnail());
        holder.txtTitle.setText(model.getTitle());
        holder.tv_subscription.setText("Subscription: " + model.getIs_subscriptions());
        if (model.is_free_course.equals("0")) {
            if (model.getAmount() == null || TextUtils.isEmpty(model.getAmount()))
                holder.tv_amount.setText("--");
            else
                holder.tv_amount.setText(context.getResources().getString(R.string.Rs_symbol) + " " + model.getAmount());
        } else
            holder.tv_amount.setText("Free");
//        String creadteDate = CommonMethods.changeDateTimeFmt("yyyy-MM-dd" , "dd-MMM-yyyy" , model.getCreated_at());
        holder.tv_date.setText("Created on: " + model.getCreated_at());

//        holder.tv_description.setText(model.getDecription());
//        holder.tv_sub_num.setText(model.getIs_subscriptions());
//
//        holder.rel_sub_layou.setOnClickListener(v -> {
//            listener.onSubscriptionClick(position);
//        });
//        holder.iv_delete.setOnClickListener(v -> {
//            listener.onDelete(position);
//        });
//        holder.iv_edit.setOnClickListener(v -> {
//            listener.onEdit(position);
//        });
        holder.rel_btn_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoLive(adapterPosition);

            }
        });

        holder.rel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);

            }
        });
        holder.iv_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });
        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });

        if (count == adapterPosition) {
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

        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition);
            }
        });

        switch (adapterPosition % 2) {
            case 0:
                holder.linParent.setBackgroundColor(ContextCompat.getColor(context , R.color.white_smoke));

                break;
            case 1:
                holder.linParent.setBackgroundColor(ContextCompat.getColor(context , R.color.white));
                break;
            default:
                holder.linParent.setBackgroundColor(ContextCompat.getColor(context , R.color.white_smoke));
                break;

        }

        //___--------------mangae view acording type--//
        if (type.equalsIgnoreCase("MyCourse")) {

            if (model.getAssigned()) {

                holder.tvProfileName.setText(model.getAssign_to_name());
                if(model.getAssign_to_image()!=null){
                    Picasso.get().load(model.getAssign_to_image()).placeholder(R.drawable.logoo)
                            .into(holder.ivProfileImg);
                }else{
                    holder.ivProfileImg.setImageResource(R.drawable.logoo);
                }
                if(model.getStatus().equals("Pending")){
                    holder.tvMobile.setImageResource(R.drawable.ic_pending);
                }else{
                    holder.tvMobile.setImageResource(R.drawable.ic_accepted);
                }



//

                holder.relProfile.setVisibility(View.VISIBLE);
                holder.tvType.setText("Assigned to");
                holder.viewLine.setBackgroundColor(ContextCompat.getColor(context , R.color.graient2));
                ///set profile data
            } else {
                holder.relProfile.setVisibility(View.GONE);
                holder.viewLine.setBackgroundColor(ContextCompat.getColor(context , R.color.light_gray));
            }

        } else if (type.equalsIgnoreCase("AssignCourse")) {
            setProfileData(holder , model.getAssign_by());
            holder.relProfile.setVisibility(View.VISIBLE);
            holder.tvType.setText("Assigned by");
            holder.img_edit.setVisibility(View.GONE);
            holder.viewLine.setBackgroundColor(ContextCompat.getColor(context , R.color.graient2));
            //set profile here;

        }
        //profile click button handler

        holder.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileClick(adapterPosition);
            }
        });


    }

    private void setProfileData(ViewHolder holder , AssignProfile assignModel) {
        if (assignModel != null) {
            holder.tvProfileName.setText(assignModel.getName());
            String imageLink = BaseUrl.BASE_URL_MEDIA + assignModel.getImage();
            Log.e("profileImg" , "" + imageLink);
            Picasso.get().load(imageLink).placeholder(R.drawable.logoo)
                    .into(holder.ivProfileImg);
//            holder.tvMobile.setText("+91-" + assignModel.getMobile());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ViewHolder(@NonNull View itemView) {
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
            viewLine = itemView.findViewById(R.id.view1);
            btnProfile.setVisibility(View.VISIBLE);
            btn_accept=itemView.findViewById(R.id.btn_accept);
            btn_deny=itemView.findViewById(R.id.btn_deny);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }

}

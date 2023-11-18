package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.ScreenshotPoliyModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScreenshotPoliyAdapter extends RecyclerView.Adapter<ScreenshotPoliyAdapter.PoliciyHolder> {
    Context context;
    ArrayList<ScreenshotPoliyModel>list;
    OnCourseClickListener listener;
    public interface  OnCourseClickListener {
        void onItemClick(int postion, String value, String user_id, SwitchCompat switchBtn);
    }

    public ScreenshotPoliyAdapter(Context context, ArrayList<ScreenshotPoliyModel> list, OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public PoliciyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_screenshotpolicy,null);
        return new PoliciyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoliciyHolder holder, int position) {
        int adapterPosition=position;
        ScreenshotPoliyModel model=list.get(adapterPosition);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getImage())
                .placeholder(R.drawable.profile)
                .into(holder.iv_profile);
        holder.tvName.setText(model.getFirst_name());
        if (model.getIs_enable().equals("1")){
            holder.policy_switch.setChecked(true);
            holder.policy_switch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.track_color)));
            holder.policy_switch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.graient2)));
        }else{
            holder.policy_switch.setChecked(false);
            holder.policy_switch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.light_gray)));
            holder.policy_switch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.gray)));
        }
        holder.policy_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="0";
                if (holder.policy_switch.isChecked()){
                    status="1";
                    holder.policy_switch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.track_color)));
                    holder.policy_switch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.graient2)));
                }else{
                    status="0";
                    holder.policy_switch.setTrackTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.light_gray)));
                    holder.policy_switch.setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.gray)));
                }
                listener.onItemClick(adapterPosition,status,model.getStudent_id(),holder.policy_switch);
            }
        });
        //=============background color change======//
        switch (adapterPosition%2){
            case 0:
                holder.relMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white_smoke));
                break;
            case 1:
                holder.relMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                break;
            default:
                holder.relMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white_smoke));
                break;
        }
        //==================================//
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PoliciyHolder extends RecyclerView.ViewHolder {
       CircleImageView iv_profile;
        SwitchCompat policy_switch;
        RelativeLayout relMain;
        TextView tvName;
        public PoliciyHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile=itemView.findViewById(R.id.iv_profile);
            policy_switch=itemView.findViewById(R.id.policy_swich);
            relMain=itemView.findViewById(R.id.rel_main);
            tvName=itemView.findViewById(R.id.assign_tv_name);

        }
    }
}

package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Confiq.Common;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolHomeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SchoolHomeAdpter extends RecyclerView.Adapter<SchoolHomeAdpter.ViewHolder> {
    Context context;
    ArrayList<SchoolHomeModel> list;
    OnSchoolClickListener listener;


    public interface OnSchoolClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }


    public SchoolHomeAdpter(Context context, ArrayList<SchoolHomeModel> list,OnSchoolClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_home,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
SchoolHomeModel model = list.get(adapterPosition);
        Picasso.get().load(BaseUrl.SCHOOL_BASE_URL_MEDIA+model.getImage())
                .placeholder(R.drawable.logoo)
                .into(holder.img_school);
        if(model.getBlock_status().equals("Pending")){
            holder.tv_pending.setVisibility(View.VISIBLE);
            holder.tv_subcriber.setVisibility(View.GONE);
        }else if(model.getBlock_status().equals("Approved")){
            holder.tv_pending.setVisibility(View.GONE);
            holder.tv_subcriber.setVisibility(View.VISIBLE);
        }
holder.tv_school_title.setText(model.getSchool_name());
        holder.rel_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getBlock_status().equals("Approved")){
                    listener.onItemClick(adapterPosition);
                }else{
                    CommonMethods.showSuccessToast(context,"Please wait for school to approve your request");
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_school;
        RelativeLayout rel_open;
        TextView tv_school_title,tv_pending,tv_subcriber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_school= itemView.findViewById(R.id.img_school);
            rel_open= itemView.findViewById(R.id.rel_open);
            tv_school_title= itemView.findViewById(R.id.tv_tuotr_name);
            tv_pending= itemView.findViewById(R.id.tv_pending);
            tv_subcriber= itemView.findViewById(R.id.tv_subcriber);
        }
    }
}

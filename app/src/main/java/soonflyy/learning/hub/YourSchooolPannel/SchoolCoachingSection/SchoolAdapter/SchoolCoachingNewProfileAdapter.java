package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingNewProfileModel;

import java.util.ArrayList;
import java.util.Locale;

public class SchoolCoachingNewProfileAdapter extends RecyclerView.Adapter<SchoolCoachingNewProfileAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingNewProfileModel> list;
    OnClickListener listener;
    int count = -1;

    public interface  OnClickListener {
        void onItemClick(int postion);
        void onViewProfile_Click(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public SchoolCoachingNewProfileAdapter(Context context, ArrayList<SchoolCoachingNewProfileModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_newprofile,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition = position;
        SchoolCoachingNewProfileModel model=list.get(adapterPosition);
        holder.tv_profile_text.setText(model.getName().substring(0,1).toUpperCase(Locale.ROOT));
        holder.header_tittle_tv.setText(model.getName());
        if (adapterPosition+1<=4){
            switch (adapterPosition+1){
                case 1:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 2:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 3:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 4:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }else{
            switch ((adapterPosition+1)%4){
                case 0:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 1:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 2:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 3:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }
        switch (adapterPosition % 2) {
            case 0:
                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
            case 1:
                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            default:
                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
        }
        holder.tv_viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewProfile_Click(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        TextView header_tittle_tv,tv_viewprofile,tv_profile_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            tv_viewprofile=itemView.findViewById(R.id.tv_viewprofile);
            header_tittle_tv=itemView.findViewById(R.id.header_tittle_tv);
            tv_profile_text=itemView.findViewById(R.id.tv_profile_name);

        }
    }
}

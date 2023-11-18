package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolLiveClassModel;

import java.util.ArrayList;

public class SchoolLiveClassAdapter extends RecyclerView.Adapter<SchoolLiveClassAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolLiveClassModel> list;
    OnSelectClassClickListener listener;


    public interface OnSelectClassClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public SchoolLiveClassAdapter(Context context, ArrayList<SchoolLiveClassModel> list, OnSelectClassClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_liveclasses,null);
        return new ViewHolder (view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
holder.lin_topic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        listener.onItemClick(position);
    }
});
        holder.thumbnail_image.setImageResource(list.get(position).getImage());
        holder.tv_title.setText(list.get(position).getLivechaptername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_go_live,tv_title,tv_duration;
        LinearLayout lin_topic;
        ImageView thumbnail_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_go_live=itemView.findViewById(R.id.tv_go_live);
            lin_topic=itemView.findViewById(R.id.lin_topic);
            thumbnail_image=itemView.findViewById(R.id.thumbnail_image);
           tv_title=itemView.findViewById(R.id.tv_title);
           // tv_duration=itemView.findViewById(R.id.tv_duration);
        }
    }
}

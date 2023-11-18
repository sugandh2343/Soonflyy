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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.ClassRoomModel;

import java.util.ArrayList;

public class ClassRoomAdapter  extends RecyclerView.Adapter<ClassRoomAdapter.ViewHolder> {
   Context context;
   ArrayList<ClassRoomModel>list;
    OnClassClickListener listener;

    public interface OnClassClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);
    }

    public ClassRoomAdapter(Context context, ArrayList<ClassRoomModel> list, OnClassClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_classroom,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ClassRoomModel model = list.get(position);
        holder.tv_class_title.setText(model.getTitle());
       // Picasso.get().load(model.getImage()).into(holder.img_edit);
        holder.img_edit.setImageResource(list.get(position).getImage());


        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_edit;
        LinearLayout lin_main;
        TextView tv_class_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main= itemView.findViewById(R.id.lin_main);
            img_edit= itemView.findViewById(R.id.img_edit);
            tv_class_title= itemView.findViewById(R.id.tv_class_title);
        }
    }
}

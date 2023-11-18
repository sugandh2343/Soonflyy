package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.CreatorOtherCourse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatorOtherCoursesAdapter extends RecyclerView.Adapter<CreatorOtherCoursesAdapter.ViewHolder> {
    Context context;
    ArrayList<CreatorOtherCourse> list;
    OnCreatorCourseClickListener listener;

    public interface OnCreatorCourseClickListener{
        void onItemClick(int position,String courseId);
    }

    public CreatorOtherCoursesAdapter(Context context, ArrayList<CreatorOtherCourse> list,
                                      OnCreatorCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_puchase_course_creator,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        CreatorOtherCourse model=list.get(adapterPosition);
        holder.tvName.setText(model.getTitle());
        Picasso.get().load(model.getImage()).placeholder(R.drawable.logoo)
                .into(holder.ivCourseImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition,model.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivCourseImg;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseImg=itemView.findViewById(R.id.iv_other_img);
            tvName=itemView.findViewById(R.id.tv_other_name);
        }
    }
}

package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.CoursesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.ViewHolder> {
    Context context;
    List<CoursesModel>courseList;
    OnCourseClickListener listener;

    public TeacherCourseAdapter(Context context, List<CoursesModel> courseList, OnCourseClickListener listener) {
        this.context = context;
        this.courseList = courseList;
        this.listener = listener;
    }

    public  interface OnCourseClickListener{
        void onCourseClick(int position,CoursesModel course);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subscription_single_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoursesModel model=courseList.get(position);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getThumbnail()).into(holder.img);
        holder.header_tittle_tv.setText(model.getTitle());
        holder.header_sub_tittle_tv.setText(model.getDecription());
        holder.expire_date.setText("Validity "+model.getValidity()+" days" );
        if (model.getIs_subscribed().equals("1")){
            holder.play_img.setVisibility(View.VISIBLE);
        }else {

            if (model.getIs_free_course().equals("1")) {
                holder.play_img.setVisibility(View.GONE);
            } else {
                holder.play_img.setVisibility(View.VISIBLE);
            }
        }

        holder.cl_subsription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCourseClick(position,model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header_tittle_tv,header_sub_tittle_tv,expire_date;
        CircleImageView img;
        ImageView play_img;
        ConstraintLayout cl_subsription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            header_tittle_tv = itemView.findViewById(R.id.header_tittle_tv);
            play_img = itemView.findViewById(R.id.play_img);
            header_sub_tittle_tv = itemView.findViewById(R.id.header_sub_tittle_tv);
            cl_subsription = itemView.findViewById(R.id.cl_subsription);
            expire_date=itemView.findViewById(R.id.exp_date_tv);

        }
    }
}

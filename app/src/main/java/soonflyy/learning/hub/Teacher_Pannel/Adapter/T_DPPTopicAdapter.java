package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_DppStudent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class T_DPPTopicAdapter extends RecyclerView.Adapter<T_DPPTopicAdapter.ViewHolder> {
    Context context;
    ArrayList<T_DppStudent> list;
    OnCourseClickListener listener;


    public interface OnCourseClickListener {
        void onItemClick(int postion);
    }

    public T_DPPTopicAdapter(Context context, ArrayList<T_DppStudent> list, OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_tdpp_topic, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;
        T_DppStudent model=list.get(adapterPosition);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getImage())
                .placeholder(R.drawable.profile).into(holder.profile_img);
        holder.tv_name.setText(model.getFirst_name());
        holder.tv_open_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });

        Log.e("position_color", "onBindViewHolder: " + position % 2);

        switch (position % 2) {
            case 0:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
        }


        holder.lin_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_edit;
        LinearLayout lin_title;
        RelativeLayout lin_main;
        TextView tv_open, dailog_delete;
        CardView cv_1, cv_edi;
        TextView tv_name, tv_open_details;
        CircleImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_open = itemView.findViewById(R.id.tv_manage);
            lin_title = itemView.findViewById(R.id.lin_title);
            img_edit = itemView.findViewById(R.id.img_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            lin_main = itemView.findViewById(R.id.lin_main);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            tv_name = itemView.findViewById(R.id.tv_studentName);
            profile_img = itemView.findViewById(R.id.profile_image);
            tv_open_details = itemView.findViewById(R.id.tv_open);

        }
    }
}

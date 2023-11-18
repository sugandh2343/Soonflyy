package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.TeacherDPPModel;

import java.util.ArrayList;

public class TeacherDPPAdapter extends RecyclerView.Adapter<TeacherDPPAdapter.ViewHolder> {
    Context context;
    ArrayList<TeacherDPPModel> list;
    OnCourseClickListener listener;

    int count = -1;

    public interface OnCourseClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);
    }

    public TeacherDPPAdapter(Context context, ArrayList<TeacherDPPModel> list, OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_teacher_dpp, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;

        TeacherDPPModel model=list.get(adapterPosition);
        holder.tv_title.setText(model.getTopic());
        holder.tv_date.setText("Date: "+CommonMethods.changeDateTimeFmt("dd-MMM-yyyy | hh:mm a","dd-MM-yy",model.getLast_date()));


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

        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count == adapterPosition) {

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

        holder.lin_manage.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout lin_manage, lin_main;
        TextView tv_manage, dailog_delete,tv_title,tv_date;
        CardView cv_1, cv_edi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lin_main = itemView.findViewById(R.id.lin_main);
            tv_manage = itemView.findViewById(R.id.tv_manage);
            lin_manage = itemView.findViewById(R.id.lin_manage);
            img_edit = itemView.findViewById(R.id.img_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            tv_title=itemView.findViewById(R.id.tv_dpp_title);
            tv_date=itemView.findViewById(R.id.tv_date);

            cv_edi = itemView.findViewById(R.id.cv_edi);
        }
    }
}

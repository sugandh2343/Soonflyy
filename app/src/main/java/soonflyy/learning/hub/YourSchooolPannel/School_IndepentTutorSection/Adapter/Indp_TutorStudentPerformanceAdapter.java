package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorStudentPerformanceModel;

import java.util.ArrayList;

public class Indp_TutorStudentPerformanceAdapter extends RecyclerView.Adapter<Indp_TutorStudentPerformanceAdapter.ViewHolder> {
    Context context;
    ArrayList<Indp_TutorStudentPerformanceModel> list;
    OnPerformanceClickListener listener;


    public interface OnPerformanceClickListener {
        void onItemClick(int postion);

        void onAddMarkClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public Indp_TutorStudentPerformanceAdapter(Context context, ArrayList<Indp_TutorStudentPerformanceModel> list, OnPerformanceClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ind_teacher_performence,null);
        return new ViewHolder (view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Indp_TutorStudentPerformanceModel model=list.get(position);
        holder.tv_title.setText(model.getTitle());
        holder.tv_total.setText(model.getTotal_mark());
        holder.tv_obtained.setText(model.getObtain_mark());
        if (model.getRemark().equals("0")){
            holder.tv_remark.setText("Add Remark");
        }else{
            holder.tv_remark.setText("View Remark");
        }

        switch (position%2)
        {
            case 0: holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1: holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default: holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;        }


        holder.tv_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddMarkClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_remark,tv_title,tv_obtained,tv_total;
        LinearLayout lin_main;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_remark=itemView.findViewById(R.id.tv_remark);
            lin_main=itemView.findViewById(R.id.lin_main);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_obtained=itemView.findViewById(R.id.tv_obtained);
            tv_total=itemView.findViewById(R.id.tv_total);

        }
    }
}

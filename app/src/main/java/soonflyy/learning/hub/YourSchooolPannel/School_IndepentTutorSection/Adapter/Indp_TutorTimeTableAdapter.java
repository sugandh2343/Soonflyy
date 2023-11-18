package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter;

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
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorTimeTableModel;

import java.util.ArrayList;

public class Indp_TutorTimeTableAdapter extends RecyclerView.Adapter<Indp_TutorTimeTableAdapter.ViewHolder> {
    Context context;
    ArrayList<Indp_TutorTimeTableModel> list;
    OnClickListener listener;


    public interface OnClickListener {
        void onSubjectClick(int postion);

        void onDelete(int position);

        void onEdit(int position);
        void onItemClick(int position);


    }

    public Indp_TutorTimeTableAdapter(Context context, ArrayList<Indp_TutorTimeTableModel> list,  OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ind_timetable,null);
        return new ViewHolder (view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        Indp_TutorTimeTableModel model=list.get(adapterPosition);
        holder.tv_period.setText(model.getPeroid());
        holder.tv_subject.setText(model.getSubject());
        holder.tv_stitme.setText(model.getStart_time());
        holder.tv_etime.setText(model.getEnd_time());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });

        if (adapterPosition%2==0){
            holder.linHome.setBackgroundColor(ContextCompat.getColor(context,R.color.time_table_1));
        }else{
            holder.linHome.setBackgroundColor(ContextCompat.getColor(context,R.color.time_table_2));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_period,tv_subject,tv_stitme,tv_etime;
        LinearLayout linHome;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_period=itemView.findViewById(R.id.tv_period);
            tv_subject=itemView.findViewById(R.id.tv_subject);
            tv_stitme=itemView.findViewById(R.id.tv_s_time);
            tv_etime=itemView.findViewById(R.id.tv_e_time);
            linHome=itemView.findViewById(R.id.lin_main);

        }

    }
}

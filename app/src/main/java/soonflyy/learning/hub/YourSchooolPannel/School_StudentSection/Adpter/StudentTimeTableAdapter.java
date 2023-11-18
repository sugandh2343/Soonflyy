package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter;

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
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.StudentTimeTable;

import java.util.ArrayList;

public class StudentTimeTableAdapter extends RecyclerView.Adapter<StudentTimeTableAdapter.TimeTableHolder> {
   Context context;
   ArrayList<StudentTimeTable>timeList;

    public StudentTimeTableAdapter(Context context, ArrayList<StudentTimeTable> timeList) {
        this.context = context;
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public TimeTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeTableHolder(LayoutInflater.from(context).inflate(R.layout.row_student_time_table,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableHolder holder, int position) {
        StudentTimeTable model=timeList.get(position);
        holder.tv_period.setText(model.getPeroid());
        holder.tv_subject.setText(model.getSubject());
        holder.tv_tutor.setText(model.getTeacher_name());
        holder.tv_time.setText(model.getStart_time() +" to "+model.getEnd_time());

        if (position%2==0){
            holder.lin_main.setBackgroundColor(ContextCompat.getColor(context,R.color.time_table_1));
        }else{
            holder.lin_main.setBackgroundColor(ContextCompat.getColor(context,R.color.time_table_2));
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class TimeTableHolder extends RecyclerView.ViewHolder {
        TextView tv_period,tv_subject,tv_tutor,tv_time;
        LinearLayout lin_main;
        public TimeTableHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            tv_period=itemView.findViewById(R.id.tv_period);
            tv_subject=itemView.findViewById(R.id.tv_subject);
            tv_tutor=itemView.findViewById(R.id.tv_tutor);
            tv_time=itemView.findViewById(R.id.tv_time);



        }
    }
}

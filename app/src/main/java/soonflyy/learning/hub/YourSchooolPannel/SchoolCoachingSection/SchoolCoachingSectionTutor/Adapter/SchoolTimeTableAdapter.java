package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolTimeTableModel;

import java.util.ArrayList;

public class SchoolTimeTableAdapter  extends RecyclerView.Adapter<SchoolTimeTableAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolTimeTableModel> list;
    OnClickListener listener;
    String type,from;

    public interface OnClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);
    }

    public SchoolTimeTableAdapter(Context context, String from,String type,ArrayList<SchoolTimeTableModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type=type;
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_time_table,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SchoolTimeTableModel model = list.get(position);
        if (type.equals("0")){
            //fro school student
            holder.tv_s_period.setText(model.getPeriod());
            holder.tv_s_subject.setText(model.getSubject());
            holder.tv_s_tutor.setText(model.getTeacher_name());
            holder.tv_s_time.setText(model.getStart_time()+" to "+model.getEnd_time());

        }else if (type.equals("1")){
            //fro school teacher
            if (from.equals(SCHOOL_TUTOR)){
                //from schoo tutro side
                holder.tv_st_period.setText(model.getPeroid());
                holder.tv_st_class.setText(model.getClass_name());
                holder.tv_st_section.setText(model.getSection());
                holder.tv_st_subject.setText(model.getSubject());
                holder.tv_st_time.setText(model.getStart_time()+" to "+model.getEnd_time());
            }else {
                //from school side
                holder.tv_period.setText(model.getPeriod());
                holder.tv_class.setText(model.getClass_name());
                holder.tv_section.setText(model.getSection_name());
                holder.tv_subject.setText(model.getSubject());
                holder.tv_time.setText(model.getStart_time()+" to "+model.getEnd_time());
                holder.tv_tutor.setText(model.getTeacher_name());
            }
        }


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_period,tv_class,tv_section,tv_subject,tv_time,tv_tutor;
        TextView tv_st_period,tv_st_class,tv_st_section,tv_st_subject,tv_st_time;
        TextView tv_s_period,tv_s_subject,tv_s_time,tv_s_tutor;

        ImageView img_edit;
        LinearLayout lin_main,lin_sTabel,lin_tTable,lin_st_table;
        TextView tv_class_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lin_sTabel= itemView.findViewById(R.id.lin_s_table);
            lin_st_table= itemView.findViewById(R.id.lin_st_table);
            lin_tTable= itemView.findViewById(R.id.lin_t_table);
            lin_main= itemView.findViewById(R.id.lin_main);
            img_edit= itemView.findViewById(R.id.img_edit);
            tv_class_title= itemView.findViewById(R.id.tv_class_title);

            tv_period= itemView.findViewById(R.id.tv_period);
            tv_class= itemView.findViewById(R.id.tv_class);
            tv_section= itemView.findViewById(R.id.tv_section);
            tv_subject= itemView.findViewById(R.id.tv_subject);
            tv_time= itemView.findViewById(R.id.tv_time);
            tv_tutor= itemView.findViewById(R.id.tv_tutor);

            tv_st_period= itemView.findViewById(R.id.tv_st_period);
            tv_st_class= itemView.findViewById(R.id.tv_st_class);
            tv_st_section= itemView.findViewById(R.id.tv_st_section);
            tv_st_subject= itemView.findViewById(R.id.tv_st_subject);
            tv_st_time= itemView.findViewById(R.id.tv_st_time);

            tv_s_period= itemView.findViewById(R.id.tv_s_period);
            tv_s_subject= itemView.findViewById(R.id.tv_s_subject);
            tv_s_tutor= itemView.findViewById(R.id.tv_s_tutor);
            tv_s_time= itemView.findViewById(R.id.tv_s_time);


            if (type.equals("1")){
                if (from.equals(SCHOOL_TUTOR)){
                    lin_st_table.setVisibility(View.VISIBLE);
                    lin_tTable.setVisibility(View.GONE);
                }else {
                    lin_st_table.setVisibility(View.GONE);
                    lin_tTable.setVisibility(View.VISIBLE);
                }
             //   lin_sTabel.setVisibility(View.GONE);

            }else if (type.equals("0")){
                lin_tTable.setVisibility(View.GONE);
                lin_st_table.setVisibility(View.GONE);
                lin_sTabel.setVisibility(View.VISIBLE);

            }
        }
    }
}

package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolApplyForLeaveModel;

import java.util.ArrayList;

public class SchoolApplyForLeaveAdapter extends RecyclerView.Adapter<SchoolApplyForLeaveAdapter.ViewHolder> {
    protected static Object OnClickListener;
    Context context;
   ArrayList<SchoolApplyForLeaveModel> list;

    SchoolApplyForLeaveAdapter.OnClickListener listener;
    public interface  OnClickListener {
        void onItemClick(int postion);


        void onDelete(int position);

        void onEdit(int position);
    }
    public SchoolApplyForLeaveAdapter(Context context, ArrayList<SchoolApplyForLeaveModel> list,SchoolApplyForLeaveAdapter.OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener= listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_leave_apply,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchoolApplyForLeaveModel model=list.get(position);
        holder.tv_date.setText(model.getDate());
        String status=model.getLeave_status();
        if (status.equals("0")){
            holder.tv_status.setText("Pending");
            holder.tv_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
        }else if (status.equals("1")){
            holder.tv_status.setText("Granted");
            holder.tv_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
        }else if (status.equals("2")){
            holder.tv_status.setText("Cancelled");
            holder.tv_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
        }


        if (position%2==0){
            holder.relMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white_smoke));
        }else{
            holder.relMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_status;
        LinearLayout relMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relMain=itemView.findViewById(R.id.rel_main);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_status=itemView.findViewById(R.id.tv_leave_status);
        }
    }
}

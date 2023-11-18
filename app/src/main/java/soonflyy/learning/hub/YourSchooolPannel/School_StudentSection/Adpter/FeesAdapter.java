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
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.StudentFees;

import java.util.ArrayList;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.FeesHolder> {
    Context context;
    ArrayList<StudentFees>feesList;

    public FeesAdapter(Context context, ArrayList<StudentFees> feesList) {
        this.context = context;
        this.feesList = feesList;
    }

    @NonNull
    @Override
    public FeesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeesHolder(LayoutInflater.from(context).inflate(R.layout.row_student_fees,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull FeesHolder holder, int position) {
        StudentFees model=feesList.get(position);
        holder.tv_month.setText(model.getMonth());
        holder.tv_fees.setText("Rs "+model.getAmount());
        String status=model.getFees_status();

        if (status.equalsIgnoreCase("unpaid")){
            holder.tv_status.setText("Unpaid");
            holder.tv_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
        }else if(status.equalsIgnoreCase("paid")){
            holder.tv_status.setText("Paid");
            holder.tv_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));

        }

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

    }

    @Override
    public int getItemCount() {
        return feesList.size();
    }

    public class FeesHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        TextView tv_month,tv_fees,tv_status;

        public FeesHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            tv_month=itemView.findViewById(R.id.tv_month);
            tv_fees=itemView.findViewById(R.id.tv_fees);
            tv_status=itemView.findViewById(R.id.tv_status);


        }
    }
}

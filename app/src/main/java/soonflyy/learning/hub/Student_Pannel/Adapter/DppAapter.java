package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Dpp_Model;

import java.util.ArrayList;

public class DppAapter extends RecyclerView.Adapter<DppAapter.DppHolder> {
    Context context;
    ArrayList<Dpp_Model>dppList;
    OnDppClickListener listener;

    public DppAapter(Context context, ArrayList<Dpp_Model> dppList, OnDppClickListener listener) {
        this.context = context;
        this.dppList = dppList;
        this.listener = listener;
    }

    public interface OnDppClickListener{
        void onDppClick(int position);
    }
    @NonNull
    @Override
    public DppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DppHolder(LayoutInflater.from(context).inflate(R.layout.row_dpp_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DppHolder holder, @SuppressLint("RecyclerView") int position) {

        int adapterPosition=position;
      Dpp_Model model=dppList.get(position);
      holder.tv_title.setText(model.getTopic());
      holder.tv_date.setText("Date: "+ CommonMethods.changeDateTimeFmt("dd-MMM-yyyy | hh:mm a","dd-MM-yy",model.getDate()));
        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDppClick(position);
            }
        });
        switch (adapterPosition % 2) {
            case 1:
                holder.lin_main.setBackgroundColor((ContextCompat.getColor(context,R.color.white)));
                break;
            case 0:
            default:
                holder.lin_main.setBackgroundColor((ContextCompat.getColor(context,R.color.white_smoke)));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dppList.size();
    }

    public class DppHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_date;
        LinearLayout lin_main;
        public DppHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            tv_title=itemView.findViewById(R.id.tv_dpp_title);
            tv_date=itemView.findViewById(R.id.tv_date);

        }
    }
}

package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTestModel;

import java.util.ArrayList;

public class SchoolCoachingTestAdapter extends RecyclerView.Adapter<SchoolCoachingTestAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingTestModel> list;
    OnClickListener listener;
    int count = -1;

    public interface  OnClickListener {
        void onItemClick(int postion);
        void onViewTest_Click(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public SchoolCoachingTestAdapter(Context context, ArrayList<SchoolCoachingTestModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_schoolcoaching_test,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;
        switch (adapterPosition % 2) {
            case 0:
                holder.card_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
            case 1:
                holder.card_main.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            default:
                holder.card_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
        }
        holder.tv_view_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewTest_Click(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_main;
        TextView header_tittle_tv,tv_view_test;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_main=itemView.findViewById(R.id.card_main);
            tv_view_test=itemView.findViewById(R.id.tv_view_test);
            header_tittle_tv=itemView.findViewById(R.id.header_tittle_tv);

        }
    }
}

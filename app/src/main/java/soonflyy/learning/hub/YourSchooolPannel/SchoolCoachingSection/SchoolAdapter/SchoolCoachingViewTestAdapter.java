package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingViewTestModel;

import java.util.ArrayList;

public class SchoolCoachingViewTestAdapter  extends RecyclerView.Adapter<SchoolCoachingViewTestAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingViewTestModel> list;
    OnClickListener listener;
    int count = -1;

    public interface  OnClickListener {
        void onItemClick(int postion);
        void onViewTest_Click(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public SchoolCoachingViewTestAdapter(Context context, ArrayList<SchoolCoachingViewTestModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_view_test,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;

        switch (adapterPosition%2)
        {
            case 0: holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
            case 1: holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            default: holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);

        }
    }
}

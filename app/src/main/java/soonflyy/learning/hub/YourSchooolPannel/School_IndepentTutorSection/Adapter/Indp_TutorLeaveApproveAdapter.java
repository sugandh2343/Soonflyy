package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorLeaveApproveModel;

import java.util.ArrayList;

public class Indp_TutorLeaveApproveAdapter extends RecyclerView.Adapter<Indp_TutorLeaveApproveAdapter.ViewHolder> {
   Context context;
   ArrayList<Indp_TutorLeaveApproveModel> list;
    OnClickListener listener;


    public interface OnClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public Indp_TutorLeaveApproveAdapter(Context context, ArrayList<Indp_TutorLeaveApproveModel> list,  OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leave_approve,null);
        return new ViewHolder (view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

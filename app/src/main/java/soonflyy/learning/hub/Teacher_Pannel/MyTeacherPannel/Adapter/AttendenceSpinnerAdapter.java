package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AttendenceSpinnerModel;

import java.util.ArrayList;

public class AttendenceSpinnerAdapter extends RecyclerView.Adapter<AttendenceSpinnerAdapter.ViewHolder> {

    Context context;
    ArrayList<AttendenceSpinnerModel> list;
    OnClickListener listener;
    int lastPosition;

    public interface  OnClickListener{
        public void onClick(int position);
    }

    public AttendenceSpinnerAdapter(Context context, ArrayList<AttendenceSpinnerModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_attence_spinner,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        lastPosition=list.size();
        int adapterPosition =position;
        AttendenceSpinnerModel model=list.get(adapterPosition);

        holder.radio_text.setText(model.getTitle());
        if (adapterPosition==list.size()-1)
            holder.bottom_line.setVisibility(View.GONE);
        else
            holder.bottom_line.setVisibility(View.VISIBLE);

        if (model.isChecked()){
            holder.radio_text.setChecked(true);
        }else {
            holder.radio_text.setChecked(false);
        }
        holder.radio_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radio_text;
        View bottom_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radio_text=itemView.findViewById(R.id.radio_text);
            bottom_line=itemView.findViewById(R.id.bottom_line);
        }
    }
}

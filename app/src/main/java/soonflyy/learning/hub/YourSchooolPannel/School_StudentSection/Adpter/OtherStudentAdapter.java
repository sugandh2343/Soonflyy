package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter;

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

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.OtherStudent;

import java.util.ArrayList;
import java.util.Locale;

public class OtherStudentAdapter extends RecyclerView.Adapter<OtherStudentAdapter.OtherHolder> {
    Context context;
    ArrayList<OtherStudent>studentList;
    OnItemClickListener listener;

    public OtherStudentAdapter(Context context, ArrayList<OtherStudent> studentList,
                               OnItemClickListener listener) {
        this.context = context;
        this.studentList = studentList;
        this.listener = listener;
    }

    public interface  OnItemClickListener{
       void onItemClick(int position,OtherStudent model);
    }

    @NonNull
    @Override
    public OtherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OtherHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_toher_student,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OtherHolder holder, @SuppressLint("RecyclerView") int position) {
//--change color
        OtherStudent model= studentList.get(position);
        holder.tvName.setText(model.getStudent_name());
        switch (position%2){
            case 0:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(holder.linParent.getContext(), R.color.white_smoke));
                break;
            case 1:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(holder.linParent.getContext(),R.color.white));
                break;
        }
        holder.tvProfileText.setText(model.getStudent_name().substring(0,1).toUpperCase(Locale.ROOT));
        Context context=holder.tvProfileText.getContext();
        if (position+1<=4){
            switch (position+1){
                case 1:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 2:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 3:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 4:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }else{
            switch ((position+1)%4){
                case 0:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 1:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 2:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 3:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class OtherHolder extends RecyclerView.ViewHolder {
        LinearLayout linParent;
        public TextView tvName,tvProfileText;
        public OtherHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvProfileText = itemView.findViewById(R.id.tv_profile_text);
            linParent = itemView.findViewById(R.id.lin_parent);
        }
    }
}

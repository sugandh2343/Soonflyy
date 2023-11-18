package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorStudentPerformanceModel;

import java.util.ArrayList;

public class StudentPerformanceAdapter extends RecyclerView.Adapter<StudentPerformanceAdapter.ViewHolder> {
    Context context;
   // ArrayList<StudentPerformanceModel> list;
    OnSchoolDetailClickListener listener;
    ArrayList<Indp_TutorStudentPerformanceModel> list;


    public interface OnSchoolDetailClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }


    public StudentPerformanceAdapter(Context context, ArrayList<Indp_TutorStudentPerformanceModel> list, OnSchoolDetailClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_student_performance,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Indp_TutorStudentPerformanceModel model = list.get(position);
        holder.tv_title.setText(model.getTitle());
       // Log.e("position_color", "onBindViewHolder: "+position%2 );

        switch (position%2) {
            case 0:
                holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;
        }
            holder.card_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_main;
        TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_main= itemView.findViewById(R.id.card_main);
            tv_title=itemView.findViewById(R.id.header_tittle_tv);

        }
    }
}

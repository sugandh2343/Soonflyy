package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginClassModel;

import java.util.ArrayList;

public class SchoolCoachingTutorAsginClassAdapter extends RecyclerView.Adapter<SchoolCoachingTutorAsginClassAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingTutorAsginClassModel> list;
    OnClickListener listener;
    int count = -1;

    public interface  OnClickListener {
        void onItemClick(int postion);
        void onAsignTutor_Click(int postion);

        void onView(int position);

        void onUpdateSchedule(int position);
        void onRemovePeriod(int position);



    }

    public SchoolCoachingTutorAsginClassAdapter(Context context, ArrayList<SchoolCoachingTutorAsginClassModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_asigntutor_classlist,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition = position;
        SchoolCoachingTutorAsginClassModel model=list.get(position);
        Log.e("class",""+model.getClass_name());
        Log.e("section",""+model.getSection_name());
        holder.tv_class.setText("Class: "+model.getClass_name());
        holder.tv_section.setText("Section: "+model.getSection_name());


//        switch (adapterPosition % 2) {
//            case 0:
//                // holder.profile_image.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
//                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
//                break;
//            case 1:
//
//                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white));
//                break;
//            default:
//                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
//                break;
//        }

//        holder.rel_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(position);
//            }
//        });

        holder.tv_view_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onView(position);
            }
        });
        holder.tv_update_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpdateSchedule(position);
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertMessage(position);
            }
        });

    }

    private void showAlertMessage(int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setCancelable(false)
                .setMessage("Are you sure to remove?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onRemovePeriod(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
//        LinearLayout rel_main;
//        LinearLayout lin_main;
        ImageView ivRemove;
        TextView tv_chapter_title,tv_chaper_no,tv_asign;
        TextView tv_class,tv_section,tv_view_class,tv_update_schedule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRemove=itemView.findViewById(R.id.iv_remove_period);
            tv_asign= itemView.findViewById(R.id.tv_asign);
//            lin_main= itemView.findViewById(R.id.lin_main);
//            rel_main = itemView.findViewById(R.id.rel_main);
            tv_chapter_title= itemView.findViewById(R.id.tv_chapter_title);
            tv_chaper_no= itemView.findViewById(R.id.tv_chaper_no);
//            rel_main = itemView.findViewById(R.id.rel_main);
            ///
            tv_class=itemView.findViewById(R.id.tv_class);
            tv_section=itemView.findViewById(R.id.tv_section);
            tv_update_schedule=itemView.findViewById(R.id.tv_update_schedule);
            tv_view_class=itemView.findViewById(R.id.tv_view_class);



        }
    }
}

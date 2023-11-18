package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllClassesModel;

import java.util.ArrayList;

public class AllClassesAdapter extends RecyclerView.Adapter<AllClassesAdapter.ViewHolder> {
    Context context;
    ArrayList<AllClassesModel> list;
    OnSelectClassClickListener listener;
    String type;
    String from;
    int count=-1;

    public interface OnSelectClassClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }

    public AllClassesAdapter(String type, Context context,String from, ArrayList<AllClassesModel> list, OnSelectClassClickListener listener) {
        this.context = context;
        this.list = list;
        this.type=type;
        this.listener = listener;
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_allclass, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AllClassesModel model = list.get(position);
        int adapterPositino=position;
        if (type.equals("class_section")){
            holder.tv_class_name.setText("Section - " + model.getName());
            holder.tv_chapterTitle.setVisibility(View.GONE);
        }else if (type.equals("all_classes")){
            holder.tv_class_name.setText("Class - " + model.getName());
            holder.tv_chapterTitle.setVisibility(View.GONE);
        }
        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
        holder.rel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count==adapterPositino){
            holder.cv_edi.setVisibility(View.VISIBLE);
        }else {
            holder.cv_edi.setVisibility(View.GONE);
        }


        holder.img_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapterPositino;
                if(holder.cv_edi.getVisibility ()==View.VISIBLE)
                {
                    holder.cv_edi.setVisibility (View.GONE);
                }
                else
                {
                    holder.cv_edi.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });
        holder.dailog_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onEdit(adapterPositino);
            }
        });

        holder.dailog_delete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPositino);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_main,rel_edit;
        TextView tv_class_name,tv_chapterTitle;
        ImageView img_edit;
        TextView dailog_delete,dailog_edit;
        //LinearLayout lin_chapter;

        CardView cv_edi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rel_main = itemView.findViewById(R.id.rel_main);
            tv_class_name = itemView.findViewById(R.id.tv_class_name);
            rel_main=itemView.findViewById(R.id.rel_main);
            cv_edi=itemView.findViewById(R.id.cv_edi);
            img_edit= itemView.findViewById(R.id.img_edit);
            rel_edit = itemView.findViewById(R.id.rel_edit);
             dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete=itemView.findViewById(R.id.dailog_delete);
            tv_chapterTitle=itemView.findViewById(R.id.tv_chapter_title);

            if (from.equals(SCHOOL_TUTOR)||from.equals(SCHOOL_STUDENT)){
                img_edit.setVisibility(View.GONE);
            }
        }
    }
}

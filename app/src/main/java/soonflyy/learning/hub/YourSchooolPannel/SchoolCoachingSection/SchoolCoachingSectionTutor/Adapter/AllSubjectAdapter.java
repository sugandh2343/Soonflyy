package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.AllSubjectModel;

import java.util.ArrayList;

public class AllSubjectAdapter extends RecyclerView.Adapter<AllSubjectAdapter.ViewHolder> {
        Context context;
        ArrayList<AllSubjectModel>  list;
    OnSelectSubjectClickListener listener;
    String type;
    int count=-1;
    String from;


public interface OnSelectSubjectClickListener {
    void onItemClick(int postion);

    void onDelete(int position);

    void onEdit(int position);


}

    public AllSubjectAdapter(String type, Context context, String from,ArrayList<AllSubjectModel> list, OnSelectSubjectClickListener listener) {
        this.context = context;
        this.list = list;
        this.type=type;
        this.listener = listener;
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_allsubject,null);
        return new ViewHolder (view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    int adapter_position=position;
    AllSubjectModel model = list.get(position);
        if (type.equals("all_subject")){
            holder.tv_chapter_title.setText(model.getSubject_name());
            if (adapter_position+1<10)
            holder.tv_chaper_no.setText("Subject - 0"+(adapter_position+1));
            else
                holder.tv_chaper_no.setText("Subject - "+(adapter_position+1));
        }else if (type.equals("all_chapter")){
            holder.tv_chapter_title.setText(model.getChapter_name());
            if (adapter_position+1<10)
                holder.tv_chaper_no.setText("Chapter - 0"+(adapter_position+1));
            else
                holder.tv_chaper_no.setText("Chapter - "+(adapter_position+1));
        }



        holder.rel_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
//        holder.rel_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.cv_edi.setVisibility(View.GONE);
//            }
//        });
        if (count==adapter_position){

            holder.cv_edi.setVisibility(View.VISIBLE);
        }else {
            holder.cv_edi.setVisibility(View.GONE);
        }


        holder.img_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapter_position;
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
                listener.onEdit(adapter_position);
            }
        });

        holder.dailog_delete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapter_position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout rel_main,rel_edit,rel_sub;
    TextView tv_chapter_title,tv_chaper_no;
    ImageView img_edit,iv_circle;
    CardView cv_edi;
    TextView dailog_delete,dailog_edit;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
      //  rel_main = itemView.findViewById(R.id.rel_main);
        iv_circle=itemView.findViewById(R.id.iv_circle);
        tv_chapter_title= itemView.findViewById(R.id.tv_chapter_title);
        tv_chaper_no= itemView.findViewById(R.id.tv_chaper_no);
       rel_sub = itemView.findViewById(R.id.rel_sub);
        rel_main=itemView.findViewById(R.id.rel_main);

        cv_edi=itemView.findViewById(R.id.cv_edi);
        img_edit= itemView.findViewById(R.id.img_edit);
        rel_edit = itemView.findViewById(R.id.rel_edit);
        dailog_edit=itemView.findViewById(R.id.dailog_edit);
        dailog_delete=itemView.findViewById(R.id.dailog_delete);



        if (from.equals(SCHOOL_STUDENT)){
            iv_circle.setVisibility(View.GONE);
            rel_main.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_studentchap));
           img_edit.setVisibility(View.GONE);
        }else if (from.equals(SCHOOL_TUTOR)){
            if (type.equals("all_subject")){
                img_edit.setVisibility(View.GONE);
            }

        }else if (from.equals(SCHOOL_COACHING)){
            if (type.equals("all_chapter")) {
                img_edit.setVisibility(View.GONE);
            }

        }else if (from.equals(INDEPENDENT_TUTOR)){
            iv_circle.setVisibility(View.GONE);
            rel_main.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_studentchap));
        }

    }
}
}

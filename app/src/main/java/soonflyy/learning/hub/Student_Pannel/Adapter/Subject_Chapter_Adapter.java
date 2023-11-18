package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Subject_Chapter;

import java.util.ArrayList;

public class Subject_Chapter_Adapter extends RecyclerView.Adapter<Subject_Chapter_Adapter.ChapterHolder> {

    Context context;
    private ArrayList<Subject_Chapter> chapterList;
    OnChapterClickListener listener;



    public Subject_Chapter_Adapter(Context context, ArrayList<Subject_Chapter> chapterList,OnChapterClickListener listener) {
        this.context = context;
        this.chapterList = chapterList;
        this.listener=listener;
    }

    public interface OnChapterClickListener{
        void onChapterClick(int position);
    }

    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterHolder(LayoutInflater.from(context).inflate(R.layout.row_subject_chapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
        int sno=position+1;
        if (sno>10) {
            holder.tv_chapterNO.setText("Chapter - "+sno);
        }else{
            holder.tv_chapterNO.setText("Chapter - 0"+sno);
        }
        holder.tv_chapterTitle.setText(chapterList.get(position).getChapter_title());
        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChapterClick(holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterHolder extends RecyclerView.ViewHolder {
        TextView tv_chapterNO,tv_chapterTitle;
        RelativeLayout rel_main;
        public ChapterHolder(@NonNull View itemView) {
            super(itemView);
            tv_chapterNO=itemView.findViewById(R.id.tv_chaper_no);
            tv_chapterTitle=itemView.findViewById(R.id.tv_chapter_title);
            rel_main=itemView.findViewById(R.id.rel_main);
        }
    }
}

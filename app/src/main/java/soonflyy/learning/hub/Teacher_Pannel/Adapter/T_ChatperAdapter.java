package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_ChapterModel;

import java.util.ArrayList;

public class T_ChatperAdapter extends RecyclerView.Adapter<T_ChatperAdapter.ChapterHolder> {

    Context context;
    ArrayList<T_ChapterModel>chapterList;
    OnChapterListener listener;
    int count=-1;
    public T_ChatperAdapter(Context context, ArrayList<T_ChapterModel> chapterList, OnChapterListener listener) {
        this.context = context;
        this.chapterList = chapterList;
        this.listener = listener;
    }

    public interface OnChapterListener{
        void onChapterClick(int position);
        void onDelete(int position);
        void onEdit(int position);
    }

    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterHolder(LayoutInflater.from(context).inflate(R.layout.row_teacher_test,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
        int adapterPosition=position;

        holder.tv_title.setText(chapterList.get(adapterPosition).getTitle());
        holder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChapterClick(adapterPosition);

            }
        });
        Log.e("position_color_chapter", "onBindViewHolder: "+adapterPosition%2 );

        switch (position%2)
        {
            case 0: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                break;
            default: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;        }

        holder.card_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edit.setVisibility(View.GONE);
            }
        });
        if (count==adapterPosition){
            holder.cv_edit.setVisibility(View.VISIBLE);
        }else {
           holder.cv_edit.setVisibility(View.GONE);
        }
        holder.imgEdit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {

                count=adapterPosition;
                if(holder.cv_edit.getVisibility ()==View.VISIBLE)
                {
                    holder.cv_edit.setVisibility (View.GONE);
                }
                else
                {
                    holder.cv_edit.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });
        holder.dailog_delete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition);
            }
        });
        holder.dailog_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(adapterPosition);
            }
        });


    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterHolder extends RecyclerView.ViewHolder {
        CardView card_main,cv_edit;
        ImageView imgEdit,iconImg;
        TextView dailog_edit,dailog_delete,tv_title,tv_open;
        LinearLayout lin_manage;
        public ChapterHolder(@NonNull View itemView) {
            super(itemView);
            cv_edit=itemView.findViewById(R.id.cv_edi);
            iconImg=itemView.findViewById(R.id.img);
            imgEdit= itemView.findViewById(R.id.img_edit);
            lin_manage= itemView.findViewById(R.id.lin_manage);
            dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete=itemView.findViewById(R.id.dailog_delete);
            tv_title=itemView.findViewById(R.id.header_tittle_tv);
            tv_open=itemView.findViewById(R.id.tv_message);
            tv_open.setText("Open");

            card_main=itemView.findViewById(R.id.card_main);
            iconImg.setImageResource(R.drawable.s_chapter_icon);

        }
    }
}

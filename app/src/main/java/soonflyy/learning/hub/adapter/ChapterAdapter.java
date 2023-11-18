package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.Chapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    Context context;
    List<Chapter> list;
    OnChapterClickListener chapterListener;
    String type;


    public ChapterAdapter(Context context, List<Chapter> list, String type, OnChapterClickListener chapterListener) {
        this.context = context;
        this.list = list;
        this.chapterListener = chapterListener;
        this.type = type;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chapter, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        if ((adapterPosition+1)<10){
            holder.tv_chapterno.setText("Subject - 0"+(adapterPosition+1));
        }else{
            holder.tv_chapterno.setText("Subject - "+adapterPosition+1);
        }
        Chapter model=list.get(adapterPosition);
        holder.tv_name.setText(model.getTitle());
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getCover_image())
                .placeholder(R.drawable.logoo).into(holder.iv_image);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapterListener.onDelete(adapterPosition);
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapterListener.onEdit(adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name,tv_chapterno;

        ImageView iv_delete, iv_edit;
        RoundedImageView iv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.chapter_name_tv);
            tv_chapterno=itemView.findViewById(R.id.tv_chapter_no);
            iv_edit = itemView.findViewById(R.id.iv_edit_item);
            iv_delete = itemView.findViewById(R.id.iv_delete_item);
            iv_image=itemView.findViewById(R.id.iv_image);

            if (type.equals("student")) {
                iv_edit.setVisibility(View.GONE);
                iv_delete.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            chapterListener.onChapterClick(getAdapterPosition(), tv_name.getText().toString(), list.get(getAdapterPosition()).getId());
        }
    }

    public interface OnChapterClickListener {
        void onChapterClick(int position, String text, String chapterId);

        void onEdit(int positon);

        void onDelete(int position);
    }
}

package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.VideoThumbnailTask;

import java.util.ArrayList;

public class LiveVedioAdapter extends RecyclerView.Adapter<LiveVedioAdapter.Viewholder> {
    Context context;
    ArrayList<Video_Model> list;
    OnCourseClickListener listener;
int count=-1;

    public interface  OnCourseClickListener{
        void onItemClick(int postion);
        void onDelete(int position);
    }

    public LiveVedioAdapter(Context context, ArrayList<Video_Model> list, OnCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_single_video,parent,false);
        return new Viewholder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        int adapterPositino=position;
        Video_Model model=list.get(adapterPositino);
        holder.tv_title.setText(model.getTitle());

        String videoUrl= BaseUrl.BASE_URL_MEDIA+model.getFile();
        Log.e("videoPath",""+videoUrl);
        VideoThumbnailTask task=new VideoThumbnailTask(holder.iv_image,context);
        task.execute(videoUrl);


        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count==adapterPositino){

        }else {
            holder.cv_edi.setVisibility(View.GONE);
        }
        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());

            }
        });


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
//        holder.dailog_edit.setOnClickListener (new View.OnClickListener ( ) {
//            @Override
//            public void onClick(View view) {
//            }
//        });

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

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RoundedImageView iv_image;
        ImageView img_edit;
        TextView dailog_delete;//dailog_edit,
        //LinearLayout lin_chapter;
        RelativeLayout rel_main;
        CardView cv_edi;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_title= itemView.findViewById(R.id.tv_title);
            iv_image=itemView.findViewById(R.id.thumbnail_image);
            rel_main=itemView.findViewById(R.id.rel_main);
            cv_edi=itemView.findViewById(R.id.cv_edi);
            img_edit= itemView.findViewById(R.id.img_edit);
          //  lin_chapter = itemView.findViewById(R.id.lin_chapter);
           // dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete=itemView.findViewById(R.id.dailog_delete);


        }
    }
}

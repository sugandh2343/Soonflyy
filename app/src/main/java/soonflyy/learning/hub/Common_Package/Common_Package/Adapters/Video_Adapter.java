package soonflyy.learning.hub.Common_Package.Common_Package.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.VideoThumbnailTask;

public class Video_Adapter extends RecyclerView.Adapter<Video_Adapter.VideoHolder> {
    Context context;
    ArrayList<Video_Model>videoList;
    OnVideoClickListener listener;



    public Video_Adapter(Context context, ArrayList<Video_Model> videoList,OnVideoClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener=listener;
    }

    public interface OnVideoClickListener{
        void onVideoClick(int position);
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(context).inflate(R.layout.row_single_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        int adapterPosition=position;
        holder.tv_title.setText(videoList.get(position).getTitle());
//        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+videoList.get(position).getVideo_thumbnail())
//                .placeholder(R.color.black).into(holder.iv_image);

//        Glide.with(context).load(BaseUrl.BASE_URL_MEDIA+videoList.get(adapterPosition).getFile())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.iv_image);

        String videoUrl=BaseUrl.BASE_URL_MEDIA+videoList.get(adapterPosition).getFile();
        Log.e("videoPath",""+videoUrl);
        VideoThumbnailTask task=new VideoThumbnailTask(holder.iv_image,context);
        task.execute(videoUrl);
//        try {
//            Glide.with(context).load(CommonMethods.retriveVideoFrameFromVideo(videoUrl))
//            .into(holder.iv_image);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onVideoClick(adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RoundedImageView iv_image;
        RelativeLayout rel_edit;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            tv_title= itemView.findViewById(R.id.tv_title);
            iv_image=itemView.findViewById(R.id.thumbnail_image);
            rel_edit=itemView.findViewById(R.id.rel_edit);
            rel_edit.setVisibility(View.GONE);
        }
    }
}

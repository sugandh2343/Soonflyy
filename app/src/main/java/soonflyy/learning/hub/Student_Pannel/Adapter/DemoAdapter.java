package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.VideoThumbnailTask;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.VideoHolder> {

    Context context;
    ArrayList<Video_Model>videoList;
    OnVideoClickListener listener;

    public DemoAdapter(Context context, ArrayList<Video_Model> videoList,
                       OnVideoClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    public  interface  OnVideoClickListener{
        void onPlayVideo(int position,String videoUrl);
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_demo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        int adapterPosition=position;
        Video_Model model=videoList.get(adapterPosition);
        String videoUrl= BaseUrl.BASE_URL_MEDIA+model.getFile();
        Log.e("videoPath",""+videoUrl);
        VideoThumbnailTask task=new VideoThumbnailTask(holder.thumbnail,context);
        task.execute(videoUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlayVideo(adapterPosition,videoUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        RoundedImageView thumbnail;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.video_thumbnail);
        }
    }
}

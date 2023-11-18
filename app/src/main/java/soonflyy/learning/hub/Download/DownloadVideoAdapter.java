package soonflyy.learning.hub.Download;

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
import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class DownloadVideoAdapter extends RecyclerView.Adapter<DownloadVideoAdapter.VideoHolder> {
    Context context;
    ArrayList<DownloadVideo>videoList;
    OnPlayDownloadListener listener;

    public DownloadVideoAdapter(Context context, ArrayList<DownloadVideo> videoList, OnPlayDownloadListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    public interface OnPlayDownloadListener{
      void  onPlayVideo(int position,DownloadVideo downloadVideo);
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(context).inflate(R.layout.row_single_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        int adapterPosition=position;
        DownloadVideo model=videoList.get(adapterPosition);

//        try{
////MICRO_KIND, size: 96 x 96 thumbnail
//            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(model.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//            Glide.with(context).load(bmThumbnail)
//                    .placeholder(R.color.black)
//                    .into(holder.iv_image);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        VideoThumbnailTask task=new VideoThumbnailTask(holder.iv_image,context);
//        task.execute(model.getPath());
       // Log.e("dispName",""+model.getDisplayName());
        Log.e("titleName",""+model.getTitle());
        holder.tv_title.setText(model.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPlayVideo(adapterPosition,model);
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

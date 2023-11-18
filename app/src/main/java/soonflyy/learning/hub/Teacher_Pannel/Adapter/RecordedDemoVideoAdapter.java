package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
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

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Video_Model;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.VideoThumbnailTask;

import java.util.ArrayList;

public class RecordedDemoVideoAdapter extends RecyclerView.Adapter<RecordedDemoVideoAdapter.VideoHolder> {

    Context context;
    ArrayList<Video_Model>videoList;
    OnVide0Listener listener;
    int count=-1;

    public RecordedDemoVideoAdapter(Context context, ArrayList<Video_Model> videoList,
                                    OnVide0Listener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    public  interface OnVide0Listener{
        void onItemClick(int position,Video_Model model);
        void onDelete(int position,Video_Model model);
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new VideoHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_live_claess,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        int adapterPosition=position;
        Video_Model model=videoList.get(position);
        holder.tvVideoTitle.setText(model.getTitle());

        String videoUrl= BaseUrl.BASE_URL_MEDIA+model.getFile();
        Log.e("videoPath",""+videoUrl);
        VideoThumbnailTask task=new VideoThumbnailTask(holder.rivThumbnail,context);
        task.execute(videoUrl);

        //-----------listnere------------//
        if (count==adapterPosition){
        }else {
            holder.cvEdit.setVisibility(View.GONE);
        }
        holder.ivEdit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapterPosition;
                if(holder.cvEdit.getVisibility ()==View.VISIBLE)
                {
                    holder.cvEdit.setVisibility (View.GONE);
                }
                else
                {
                    holder.cvEdit.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition,model);

            }
        });
        //------------------------//

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivThumbnail;
        TextView tvDuration,tvTitle,tvVideoTitle,tvDelete;
        LinearLayout linTime;
        ImageView playIcon,ivEdit;
        CardView cvEdit;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            rivThumbnail=itemView.findViewById(R.id.thumbnail_image);
            tvDuration=itemView.findViewById(R.id.tv_duration);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvVideoTitle=itemView.findViewById(R.id.tv_video_name);
            linTime=itemView.findViewById(R.id.lin_time);
            playIcon=itemView.findViewById(R.id.play_icon);

            tvDelete=itemView.findViewById(R.id.dailog_delete);
            cvEdit=itemView.findViewById(R.id.cv_edi);
            ivEdit=itemView.findViewById(R.id.img_edit);

            playIcon.setVisibility(View.VISIBLE);
            linTime.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            tvDuration.setVisibility(View.GONE);
            tvVideoTitle.setVisibility(View.VISIBLE);


        }
    }
}

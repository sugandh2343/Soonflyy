package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.Video;

import java.util.List;

public class CourseDemoVideoAdapter extends RecyclerView.Adapter<CourseDemoVideoAdapter.ViewHolder> {
    Context context;
    List<Video> list;
    OnDemoVideoClickListener listener;


    public interface OnDemoVideoClickListener{
        void onVideoClick(int position,String videoUrl);
    }
    public CourseDemoVideoAdapter(Context context, List<Video> list,OnDemoVideoClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_demo_live_video,null);//row_mycoursedetailt
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video=list.get(position);
       // Picasso.get().load(BaseUrl.BASE_URL_MEDIA+video.getVideo_thumbnail())
         //       .resize(120,90)
        //.placeholder(R.drawable.cover_video)
        //.into(holder.video_thumbnail);
        //holder.tv_video_title.setText("Video_"+(position+1));

    }


    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView video_thumbnail;
        //TextView tv_video_title;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            video_thumbnail= itemView.findViewById(R.id.live_imageview);
            //tv_video_title=itemView.findViewById(R.id.demo_video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            listener.onVideoClick(position,list.get(position).getVideo_file());
        }
    }
}

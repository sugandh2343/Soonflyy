package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private Context context;
    private List<Video> list;
    private String userType;
    String courseName;
   OnDemoVideoClickListener listener;
    /* private onItemClickListener clickListener;*/

    public interface  OnDemoVideoClickListener{
        void onItemClick(int position,Video video);
        void addToBookmarkClick(int position,Video video,View bookmark_view);
    }
    public VideoAdapter(Context context,List<Video> list,String courseName,String userType,OnDemoVideoClickListener listener){
        this.context= context;
        this.list = list;
        this.courseName=courseName;
        this.listener=listener;
        this.userType=userType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_single_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Video video=list.get(position);
        String url= BaseUrl.BASE_URL_MEDIA+video.getVideo_thumbnail();
        Picasso.get().load(url).placeholder(R.drawable.lecture_img).into(holder.img);
        if (courseName!=null){
            if (courseName.length()>3){
                courseName=courseName.substring(0,3);
                Log.e("name: ",courseName);
            }
            holder.lecture_tv.setText(courseName+" Demo Video "+(position+1));
        }

        if (!userType.equals("teacher")) {
            if (video.getIs_bookmark()!=null){
                if (video.getIs_bookmark().equals("1")) {
                    ImageViewCompat.setImageTintList(holder.save_img, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_color)));
                } else {
                    ImageViewCompat.setImageTintList(holder.save_img, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_gray)));
                }
            }
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,video);
            }
        });
        holder.save_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addToBookmarkClick(position,video,holder.save_img);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView lecture_tv;
        ImageView save_img,img,play_img;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            lecture_tv = itemView.findViewById(R.id.lecture_tv);
            save_img = itemView.findViewById(R.id.save_img);
            play_img = itemView.findViewById(R.id.play_img);

            if(userType.equals("teacher")){
                save_img.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* if(clickListener!=null){
                clickListener.onClickItem(v,getAdapterPosition());
            }*/
        }
    }
}

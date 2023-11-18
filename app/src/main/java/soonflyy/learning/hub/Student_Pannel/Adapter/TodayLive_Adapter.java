package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.S_LiveModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TodayLive_Adapter extends RecyclerView.Adapter<TodayLive_Adapter.LiveHolder> {

    Context context;
    ArrayList<S_LiveModel>liveList;
    OnTodayLiveListener listener;

    public TodayLive_Adapter(Context context, ArrayList<S_LiveModel> liveList, OnTodayLiveListener listener) {
        this.context = context;
        this.liveList = liveList;
        this.listener = listener;
    }

    public interface OnTodayLiveListener{
        void onLiveJoin(int position,S_LiveModel model);
    }

    @NonNull
    @Override
    public LiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LiveHolder(LayoutInflater.from(context).inflate(R.layout.row_student_today_live,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveHolder holder, int position) {
        int adapterPosition=position;
        S_LiveModel model=liveList.get(adapterPosition);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getThumbnail()).placeholder(R.drawable.logoo).into(holder.thumbnail_image);
        holder.tv_title.setText("Live Class on "+model.getTitle());
        if (model.getLive_status().equals("1")){
            holder.tv_join.setText("Join");

        }else{
            holder.tv_join.setText(model.getStart_time());
        }

        holder.tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getLive_status().equals("1")){
                    listener.onLiveJoin(adapterPosition,model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveList.size();
    }

    public class LiveHolder extends RecyclerView.ViewHolder {
        RoundedImageView thumbnail_image;
        TextView tv_join,tv_title;
        public LiveHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail_image=itemView.findViewById(R.id.thumbnail_image);
            tv_join=itemView.findViewById(R.id.tv_see_details);
            tv_title=itemView.findViewById(R.id.tv_title);
        }
    }
}

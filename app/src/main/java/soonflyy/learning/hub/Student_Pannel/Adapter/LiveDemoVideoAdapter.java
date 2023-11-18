package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;

import java.util.ArrayList;

public class LiveDemoVideoAdapter extends RecyclerView.Adapter<LiveDemoVideoAdapter.DemoAdapter> {

    Context context;
    ArrayList<T_LiveModel> liveArrayList;
    OnLiveJoinListener listener;

    public LiveDemoVideoAdapter(Context context, ArrayList<T_LiveModel> liveArrayList, OnLiveJoinListener listener) {
        this.context = context;
        this.liveArrayList = liveArrayList;
        this.listener = listener;
    }

    public interface OnLiveJoinListener{
        void onLiveJoin(int position,T_LiveModel model);
    }
    @NonNull
    @Override
    public DemoAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemoAdapter(LayoutInflater.from(context).inflate(R.layout.row_demo_live_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemoAdapter holder, int position) {
        int adapterPosition=position;
        T_LiveModel model=liveArrayList.get(adapterPosition);
        if (model.getIs_live().equals("1")){
            holder.btn_join.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.light_green));

        }else{
            holder.btn_join.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
        }
        holder.tv_time.setText(model.getStart_time()+" to "+model.getEnd_time());

        holder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getIs_live().equals("1")){
                    listener.onLiveJoin(adapterPosition,model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveArrayList.size();
    }

    public class DemoAdapter extends RecyclerView.ViewHolder {
        RoundedImageView iv_thumnail;
        TextView tv_time;
        Button btn_join;
        public DemoAdapter(@NonNull View itemView) {
            super(itemView);
            iv_thumnail=itemView.findViewById(R.id.live_imageview);
            tv_time=itemView.findViewById(R.id.tv_join_btn);
            btn_join=itemView.findViewById(R.id.tv_live_time);
        }
    }
}

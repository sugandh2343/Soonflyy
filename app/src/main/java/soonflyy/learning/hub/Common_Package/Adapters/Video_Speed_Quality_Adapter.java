package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Models.Video_Quality_Speed;
import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class Video_Speed_Quality_Adapter extends RecyclerView.Adapter<Video_Speed_Quality_Adapter.Holder>{

    Context context;
    ArrayList<Video_Quality_Speed>list;
    OnItemSelectionListener listener;

    public Video_Speed_Quality_Adapter(Context context,
                                       ArrayList<Video_Quality_Speed> list,
                                       OnItemSelectionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemSelectionListener{
        void onItemSelect(int position,Video_Quality_Speed model);
    }
    @NonNull
    @Override

    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.row_video_speed_quality,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int adapterPosition=position;
        Video_Quality_Speed model=list.get(adapterPosition);

        if (model.isChecked()){
            holder.iv_checked.setVisibility(View.VISIBLE);
        }else{
            holder.iv_checked.setVisibility(View.INVISIBLE);
        }

        holder.tv_text.setText(model.getText());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelect(adapterPosition,model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout lin_parent;
        TextView tv_text;
        ImageView iv_checked;
        public Holder(@NonNull View itemView) {
            super(itemView);
            lin_parent=itemView.findViewById(R.id.lin_parent);
            tv_text=itemView.findViewById(R.id.tv_text);
            iv_checked=itemView.findViewById(R.id.iv_check);

        }
    }
}

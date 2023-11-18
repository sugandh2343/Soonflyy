package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.TopicsModel;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {
    Context context;
    ArrayList<TopicsModel>list;
    OnTopicListener onTopicListener;
    String type;


    public interface OnTopicListener{
        void onTopicItemClick(int pistion,String topic,String topicId);
        void onEdit(int positon);
        void onDelete(int position);
    }
    public TopicsAdapter(Context context, ArrayList<TopicsModel> list,String type,OnTopicListener onTopicListener) {
        this.context = context;
        this.list = list;
        this.type=type;
       this.onTopicListener=onTopicListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chapter,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getTopic_title());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopicListener.onDelete(holder.getAdapterPosition());
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopicListener.onEdit(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        ImageView iv_delete,iv_edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.chapter_name_tv);
            iv_edit=itemView.findViewById(R.id.iv_edit_item);
            iv_delete=itemView.findViewById(R.id.iv_delete_item);
           // tv_name.setText("Trigonometry");
            if (type.equals("student")){
                iv_edit.setVisibility(View.GONE);
                iv_delete.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int aposition=getAdapterPosition();
            onTopicListener.onTopicItemClick(aposition,list.get(aposition).getTopic_title(),list.get(aposition).getTopic_id());
        }
    }
}

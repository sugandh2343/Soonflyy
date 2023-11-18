package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Models.IDOtherModel;
import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class ID_Other_Adapter extends RecyclerView.Adapter<ID_Other_Adapter.OtherHolder> {
    Context context;
    ArrayList<IDOtherModel>otherList;
    OnLinkClickListener listener;

    public ID_Other_Adapter(Context context, ArrayList<IDOtherModel> otherList, OnLinkClickListener listener) {
        this.context = context;
        this.otherList = otherList;
        this.listener = listener;
    }

    public  interface OnLinkClickListener{
        void onLinkClick(int position,String link);
    }
    @NonNull
    @Override
    public OtherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OtherHolder(LayoutInflater.from(context).inflate(R.layout.row_other_item_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OtherHolder holder, int position) {
        int adapterPosition=position;
        IDOtherModel model=otherList.get(adapterPosition);
        holder.tv_title.setText(model.getTitle());
        holder.tv_link.setText(model.getLink());

        holder.tv_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  listener.onLinkClick();

            }
        });

    }

    @Override
    public int getItemCount() {
        return otherList.size();
    }

    public class OtherHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_title,tv_link;
        public OtherHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.iv_icon);
            tv_link=itemView.findViewById(R.id.tv_link);
            tv_title=itemView.findViewById(R.id.tv_title);
        }
    }
}

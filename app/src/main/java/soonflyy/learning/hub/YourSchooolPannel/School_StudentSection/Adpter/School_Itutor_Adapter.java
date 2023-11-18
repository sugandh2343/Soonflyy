package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.School_ITutor_Search;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class School_Itutor_Adapter extends RecyclerView.Adapter<School_Itutor_Adapter.ItemHolder> {
    Context context;
    ArrayList<School_ITutor_Search>list;
    OnAddListener listener;

    public School_Itutor_Adapter(Context context, ArrayList<School_ITutor_Search> list, OnAddListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public  interface  OnAddListener{
        void onAdd(int position);
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.row_school_itutor_search,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        int adapterPosition=position;
        School_ITutor_Search searchModel=list.get(adapterPosition);
        Picasso.get().load(BaseUrl.SCHOOL_BASE_URL_MEDIA+searchModel.getImage())
                .placeholder(R.drawable.logoo)
                .into(holder.iv_profile);
        holder.tv_name.setText(searchModel.getName());
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAdd(adapterPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_profile;
        TextView tv_name,tv_add;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile=itemView.findViewById(R.id.profile_image);
            tv_name=itemView.findViewById(R.id.assign_tv_name);
            tv_add=itemView.findViewById(R.id.tv_add);


        }
    }
}

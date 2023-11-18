package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.ShowMessageModel;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.utlis.SessionManagement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowMessageAdapter  extends RecyclerView.Adapter<ShowMessageAdapter.ViewHolder>
implements Filterable {
   Context context;
   ArrayList<ShowMessageModel>list;
    ArrayList<ShowMessageModel>filterableList;
   OnChatListItemClickListener listener;
   SessionManagement sessionManagement;


    public ShowMessageAdapter(Context context, ArrayList<ShowMessageModel> list, OnChatListItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.filterableList=list;
        this.listener = listener;
        sessionManagement=new SessionManagement(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if (charString.isEmpty()){
                    filterableList=list;
                }else{
                    ArrayList<ShowMessageModel> dataFilteredList=new ArrayList<>();
                    for (ShowMessageModel row: list){
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())){
                            dataFilteredList.add(row);
                        }
                    }
                    filterableList=dataFilteredList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filterableList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterableList=(ArrayList<ShowMessageModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface  OnChatListItemClickListener{
        void onItemClick(int postion,ShowMessageModel model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_show_message,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.e("position_color", "onBindViewHolder: " + position % 2);

        int adapterPosition=position;
        ShowMessageModel model=filterableList.get(adapterPosition);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getPhoto())
                .placeholder(R.drawable.profile).into(holder.profile_image);
        holder.tv_Name.setText(model.getName());
        String blockById= model.getBlocked_by();
        String blockToId=model.getBlocked_to();
        if (blockById.equals("1") && blockToId.equals("1")){
            // both block each other, so first priority is sender
            holder.tv_title.setText("You blocked this account");
            holder.tv_countnumber.setVisibility(View.GONE);
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.red));
        }else if (blockById.equals("1")){
            holder.tv_title.setText("You blocked this account");
            holder.tv_countnumber.setVisibility(View.GONE);
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.red));
            //receiver is block by sender
        }else if (blockToId.equals("1")){
            //sender is block by receiver
            holder.tv_title.setText("You have blocked");
            holder.tv_countnumber.setVisibility(View.GONE);
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.red));
        }else {
            holder.tv_title.setText(model.getLast_msg());
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_countnumber.setVisibility(View.VISIBLE);
        }
        /*String userId=sessionManagement.getString(ID_SECTION_USER_ID);
        if (model.getBlock_status()!=null) {
            if (model.getBlock_status().equals("1")
                    && (blockById.equals("")|| blockById.equals(userId))) {
                holder.tv_title.setText("You blocked this account");
                holder.tv_countnumber.setVisibility(View.GONE);
                holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.red));
           }else if (model.getBlock_status().equals("1")
            && (!blockById.equals("") && !blockById.equals(userId))){

           // } else if (!blockById.equals("") && !blockById.equals(userId)){
                holder.tv_title.setText("You have blocked");
                holder.tv_countnumber.setVisibility(View.GONE);
                holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                holder.tv_title.setText(model.getLast_msg());
                holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.tv_countnumber.setVisibility(View.VISIBLE);
            }
        }else{
            holder.tv_title.setText(model.getLast_msg());
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_countnumber.setVisibility(View.VISIBLE);
        }*/
        String count=model.getUnseen_count();
        if (count !=null) {
            if (count.equals("0")) {
                holder.tv_countnumber.setVisibility(View.GONE);
            } else {
                holder.tv_countnumber.setText(count);
                holder.tv_countnumber.setVisibility(View.VISIBLE);
            }
        }else{
            holder.tv_countnumber.setVisibility(View.GONE);
        }

        switch (position % 2) {
            case 0:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition,model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        ImageView profile_image;
        TextView tv_Name,tv_title,tv_countnumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main = itemView.findViewById(R.id.lin_main);
            profile_image = itemView.findViewById(R.id.profile_image);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_countnumber = itemView.findViewById(R.id.tv_countnumber);
        }
    }
}

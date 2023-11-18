package soonflyy.learning.hub.adapter;

import static android.graphics.Typeface.BOLD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.StudentChatModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentChatAdapter extends RecyclerView.Adapter<StudentChatAdapter.ChatHolder>  implements Filterable {
    Context context;
    List<StudentChatModel>list;
    List<StudentChatModel>filteredList;
   // OnChatListener listener;

    public StudentChatAdapter(Context context, List<StudentChatModel> list) {//, OnChatListener listener
        this.context = context;
        this.list = list;
        this.filteredList = list;
        //this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if (charString.isEmpty()){
                    filteredList=list;
                }else{
                    ArrayList<StudentChatModel> dataFilteredList=new ArrayList<>();
                    for (StudentChatModel row: list){
                        if (row.getFirst_name().toLowerCase().contains(charString.toLowerCase())){
                            dataFilteredList.add(row);
                        }
                    }
                    filteredList=dataFilteredList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList=(ArrayList<StudentChatModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnChatListener{
        void onChatItemClick(int position,StudentChatModel model);
    }
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(context).inflate(R.layout.row_studentlist,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        StudentChatModel model=filteredList.get(position);
        holder.tv_name.setText(model.getFirst_name());


//        try {
//            JSONObject jsonObject=new JSONObject (String.valueOf (model.getChats ()));
//            Log.e ("jso", "onBindViewHolder: "+jsonObject+"--"+(model.getChats ()) );
//
//
//            Log.e ("countt", "onBindViewHolder: "+jsonObject.getString ("unread_count") );
//        } catch (JSONException e) {
//            e.printStackTrace ( );
//        }

        holder.tv_lstmsg.setText (model.getMessage_text ());
        holder.tv_count.setText (model.getUnread_count ());

        if(model.getIs_read ().equals ("0"))
        {
            holder.tv_lstmsg.setTypeface (null,BOLD);
        }
        if(model.getUnread_count ().equals ("0"))
        {
            holder.tv_count.setVisibility (View.GONE);
        }

        if (model.getUser_image()!=null){
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getUser_image()).placeholder(R.drawable.logoo).into(holder.profile_image);
        }
      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                listener.onChatItemClick(position,model);
            }
        });

       */

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        ImageView iv_message;
        CircleImageView profile_image;
        TextView tv_name,tv_lstmsg,tv_count;
        CardView cardView;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            iv_message=itemView.findViewById (R.id.iv_message);
            tv_name=itemView.findViewById(R.id.header_tittle_tv);
            profile_image=itemView.findViewById(R.id.img);
            cardView=itemView.findViewById(R.id.card_main);
            tv_count=itemView.findViewById (R.id.tv_count);
            tv_lstmsg=itemView.findViewById (R.id.tv_lstmsg);

        }
    }
}

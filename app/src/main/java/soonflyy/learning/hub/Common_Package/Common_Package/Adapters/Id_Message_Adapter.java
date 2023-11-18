package soonflyy.learning.hub.Common_Package.Common_Package.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.Common_Package.Models.Chat_Message;
import soonflyy.learning.hub.R;


public class Id_Message_Adapter extends RecyclerView.Adapter<Id_Message_Adapter.MessageHolder> {

    Context context;
    ArrayList<Chat_Message>messageList;
    String previous_date="";
    String fromId;

    public Id_Message_Adapter(Context context, ArrayList<Chat_Message> messageList,String fromId) {
        this.context = context;
        this.messageList = messageList;
        this.fromId=fromId;

    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(context).inflate(R.layout.row_messagechat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        int adapterPosition=position;
        Chat_Message model=messageList.get(adapterPosition);
        String date=model.getDate();


            if (TextUtils.isEmpty(previous_date)) {
                if (CommonMethods.getCurrentDate().equals(date)) {
                    holder.tv_date.setText("Today");
                    previous_date = date;
                } else {
                    holder.tv_date.setText(date);
                    previous_date = date;
                }
            }else{
                if (date.equals(previous_date)){
                    holder.tv_date.setVisibility(View.GONE);
                }else{
                    if (CommonMethods.getCurrentDate().equals(date)) {
                        holder.tv_date.setText("Today");
                       // previous_date = date;
                    }else{
                        holder.tv_date.setText(date);
                    }
                    holder.tv_date.setVisibility(View.VISIBLE);
                    previous_date = date;
                }
            }
        holder.tv_teacher_msg.setText(model.getMessage());
        if (model.getFrom_id().equals(fromId)){
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.msg_layout.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.msg_layout.setLayoutParams(params);
            holder.tv_teacher_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            //  holder.tv_teacher_msg.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            holder.tv_teacher_msg.setTextColor(Color.WHITE);
           // holder.tv_teacher_msg.setText(model.getMessage_text());

        }else{

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.msg_layout.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.msg_layout.setLayoutParams(params);

            holder.tv_teacher_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.tv_teacher_msg.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            holder.tv_teacher_msg.setTextColor(Color.BLACK);
           // holder.tv_teacher_msg.setText(model.getMessage_text());

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView tv_teacher_msg,tv_date;//,tv_student_msg
        ImageView image_view;
        LinearLayout msg_layout;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            tv_date= itemView.findViewById (R.id.tv_date);
            //  tv_student_msg=itemView.findViewById(R.id.tv_student_msg);
            tv_teacher_msg=itemView.findViewById(R.id.tv_teacher_msg);
            image_view=itemView.findViewById(R.id.image);
            msg_layout=itemView.findViewById(R.id.msg_layout);
        }
    }
}

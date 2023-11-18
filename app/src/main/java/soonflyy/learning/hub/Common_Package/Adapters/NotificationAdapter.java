package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Models.Notification_model;
import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    ArrayList<Notification_model>notificationList;
    OnNotificationClickListener listener;

    public NotificationAdapter(Context context, ArrayList<Notification_model> notificationList,
                               OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    public interface OnNotificationClickListener{
        void onNotificationClick(int position,Notification_model model);
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(context).inflate(R.layout.row_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        int adapterPosition=position;
        Notification_model model=notificationList.get(position);
        int sno=position+1;
        if (sno<10){
            holder.tv_sno.setText("0"+sno);
        }else{
            holder.tv_sno.setText(String.valueOf(sno));
        }
        String msg=model.getTitle();
        Log.e("msg",""+msg);
        if (msg.length()>30){
            String m= msg.substring(0,28)+"...";
            holder.tv_notice.setText(m);
            Log.e("msg",""+msg);
        }else{
            holder.tv_notice.setText(msg+"...");
            Log.e("msg",""+msg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotificationClick(adapterPosition,model);
            }
        });

        //-----------set color-//

        switch (adapterPosition%2){
            case 0:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(context,R.color.time_table_1));
                break;
            case 1:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(context,R.color.time_table_2));
                break;


        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        TextView tv_sno,tv_notice,tv_date_time;
        LinearLayout linParent;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno=itemView.findViewById(R.id.tv_notice_sno);
            tv_notice=itemView.findViewById(R.id.tv_note);
            linParent=itemView.findViewById(R.id.lin_parent);
//            tv_date_time=itemView.findViewById(R.id.tv_date_time);
//            tv_date_time.setVisibility(View.GONE);
        }
    }
}

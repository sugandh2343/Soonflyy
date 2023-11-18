package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolChapterNoticeModel;

import java.util.ArrayList;

public class SchoolChapterNoticeAdapter extends RecyclerView.Adapter<SchoolChapterNoticeAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolChapterNoticeModel> noticeList;
    OnNoticeClickListener listener;
    public SchoolChapterNoticeAdapter(Context context, ArrayList<SchoolChapterNoticeModel> noticeList, OnNoticeClickListener listener) {
        this.context = context;
        this.noticeList = noticeList;
        this.listener= listener;
    }

    public  interface OnNoticeClickListener{
        void onItemClick(int position);
        void onDelete(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_notice,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchoolChapterNoticeModel model=noticeList.get(position);
        int sno=position+1;
        if (sno<10){
            holder.tv_sno.setText("0"+sno);
        }else{
            holder.tv_sno.setText(String.valueOf(sno));
        }
//        String msg=model.getMsg();
//        if (msg.length()>30){
//            String m= msg.substring(0,28)+"...";
//            holder.tv_notice.setText(m);
//        }else{
//            holder.tv_notice.setText(msg+"...");
//        }

        holder.tv_date_time.setText(model.getDate());


    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sno,tv_notice,tv_date_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno=itemView.findViewById(R.id.tv_notice_sno);
            tv_notice=itemView.findViewById(R.id.tv_note);
            tv_date_time=itemView.findViewById(R.id.tv_date_time);

        }
    }
}

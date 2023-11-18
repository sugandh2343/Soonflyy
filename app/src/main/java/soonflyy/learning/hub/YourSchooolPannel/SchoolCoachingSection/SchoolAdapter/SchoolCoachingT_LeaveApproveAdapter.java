package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingT_LeaveApproveModel;

import java.util.ArrayList;

public class SchoolCoachingT_LeaveApproveAdapter extends RecyclerView.Adapter<SchoolCoachingT_LeaveApproveAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingT_LeaveApproveModel> list;
    OnClickListener listener;
    String type,from;


    public interface OnClickListener {
        void onViewDownloadReason(int postion);

        void onTeacherAccept(int position);

        void onTeacherCancel(int position);
        void showMsgDetails(int position);



    }

    public SchoolCoachingT_LeaveApproveAdapter(Context context, String type,String from,ArrayList<SchoolCoachingT_LeaveApproveModel> list,  OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.type=type;
        this.from=from;
    }
////row_s_tutorleave_approve
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_s_tutorleave_approve,null);
        return new ViewHolder (view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
SchoolCoachingT_LeaveApproveModel model = list.get(position);
        int adapterPosition=position;
        String dateTime=model.getDate_time().substring(0,10)+"\n"+model.getDate_time().substring(13,21);
        if (type.equals("1")){
            holder.tv_t_name.setText(model.getName());

            holder.tv_t_time.setText(dateTime);//model.getDate_time()
            if (from.equals(INDEPENDENT_TUTOR)){
                holder.tv_t_reason.setVisibility(View.GONE);
                holder.tv_download_file.setVisibility(View.VISIBLE);
              // holder.tv_t_reason.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.ic_baseline_download_24),null);
            }else if (from.equals(SCHOOL_COACHING)){
                holder.tv_t_reason.setVisibility(View.VISIBLE);
                holder.tv_download_file.setVisibility(View.GONE);
                String msg=model.getReason_message();
               // String reasonMsg=msg;
                if (msg.length() > 6) {
                   // reasonMsg=msg.substring(0,4)+"...";
                    holder.tv_t_reason.setText(msg.substring(0,4)+"...");
                }

               // setMsgColor(reasonMsg,holder.tv_t_reason);
                else{
                    holder.tv_t_reason.setText(msg);
                }

                //----set text color//

            }
            switch (model.getLeave_status()){
                case "0":
                    holder.iv_t_action_accept.setVisibility(View.VISIBLE);
                    holder.iv_t_action_cancel.setVisibility(View.VISIBLE);
                    break;
                case "1":
                    holder.iv_t_action_accept.setVisibility(View.VISIBLE);
                    holder.iv_t_action_cancel.setVisibility(View.GONE);
                    break;
                case "2":
                    holder.iv_t_action_accept.setVisibility(View.GONE);
                    holder.iv_t_action_cancel.setVisibility(View.VISIBLE);
                    break;
            }
        }else if (type.equals("0")){
            holder.tv_s_name.setText(model.getName());
            holder.tv_class.setText(model.getS_class());//model.getClass_name
            holder.tv_section.setText(model.getSection());


            holder.tv_date_time.setText(dateTime);

            switch (model.getLeave_status()){
                case "0":
                    holder.iv_s_action_accept.setVisibility(View.VISIBLE);
                    holder.iv_s_action_cancel.setVisibility(View.VISIBLE);
                    break;
                case "1":
                    holder.iv_s_action_accept.setVisibility(View.VISIBLE);
                    holder.iv_s_action_cancel.setVisibility(View.GONE);
                    break;
                case "2":
                    holder.iv_s_action_accept.setVisibility(View.GONE);
                    holder.iv_s_action_cancel.setVisibility(View.VISIBLE);
                    break;
            }

        }


        holder.iv_t_action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getLeave_status().equals("0")) {
                    listener.onTeacherCancel(adapterPosition);
                }else{
                    CommonMethods.showSuccessToast(context,"You have already taken action");
                }
            }
        });
        holder.iv_t_action_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model.getLeave_status().equals("0")) {
                    listener.onTeacherAccept(adapterPosition);
                }else{
                    CommonMethods.showSuccessToast(context,"You have already taken action");
                }
            }
        });
        holder.tv_download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals(INDEPENDENT_TUTOR)){
                    listener.onViewDownloadReason(adapterPosition);
                }
            }
        });
        holder.iv_s_action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getLeave_status().equals("0")) {
                    listener.onTeacherCancel(adapterPosition);
                }else{
                    CommonMethods.showSuccessToast(context,"You have already taken action");
                }
            }
        });

        holder.iv_s_action_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getLeave_status().equals("0")) {
                    listener.onTeacherAccept(adapterPosition);
                }else{
                    CommonMethods.showSuccessToast(context,"You have already taken action");
                }
            }
        });
        holder.tv_s_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewDownloadReason(adapterPosition);
            }
        });
        holder.tv_t_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showMsgDetails(adapterPosition);
            }
        });

        switch (position%2)
        {
            case 0:
                holder.lin_t_table.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                holder.lin_s_table.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.lin_t_table.setBackgroundColor((context.getResources().getColor(R.color.white)));
                holder.lin_s_table.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.lin_t_table.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                holder.lin_s_table.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
        }


    }

    private void setMsgColor(String reasonMsg, TextView tv_msg) {
        String rMessage= "\nView File";
        SpannableString spannableString = new SpannableString(rMessage);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(reasonMsg);
//        SpannableStringBuilder spannableStringBuilder
//                = new SpannableStringBuilder(text2);
        ForegroundColorSpan blue = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(blue,
               0, rMessage.length(), 0);
        builder.append(spannableString);
        tv_msg.setText(builder, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_s_table,lin_t_table,lin_main;
        TextView tv_t_name,tv_t_reason,tv_t_time,tv_download_file;
        ImageView iv_t_action_accept,iv_t_action_cancel,iv_s_action_accept,iv_s_action_cancel;
        TextView tv_s_name,tv_class,tv_section,tv_date_time,tv_s_reason;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            lin_s_table=itemView.findViewById(R.id.lin_s_table);
            lin_t_table=itemView.findViewById(R.id.lin_t_table);

            tv_t_name=itemView.findViewById(R.id.tv_s_name);
            tv_t_reason=itemView.findViewById(R.id.tv_reason);
            tv_t_time=itemView.findViewById(R.id.tv_time);
            iv_t_action_accept=itemView.findViewById(R.id.iv_action_accept);
            iv_t_action_cancel=itemView.findViewById(R.id.iv_action_cancel);
            tv_download_file=itemView.findViewById(R.id.tv_file_download);

            tv_s_name=itemView.findViewById(R.id.assign_tv_name);
            tv_class=itemView.findViewById(R.id.tv_class);
            tv_section=itemView.findViewById(R.id.tv_secion);
            tv_date_time=itemView.findViewById(R.id.tv_date_time);
            tv_s_reason=itemView.findViewById(R.id.tv_s_file_reason);
            iv_s_action_accept=itemView.findViewById(R.id.iv_s_accept);
            iv_s_action_cancel=itemView.findViewById(R.id.iv_s_cancel);




            if (type.equals("0")){
                lin_s_table.setVisibility(View.VISIBLE);
                lin_t_table.setVisibility(View.GONE);


            }else if (type.equals("1")){
                lin_s_table.setVisibility(View.GONE);
                lin_t_table.setVisibility(View.VISIBLE);

            }
        }
    }
}

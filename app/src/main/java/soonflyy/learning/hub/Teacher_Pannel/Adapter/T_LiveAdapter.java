package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.T_LiveModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

public class T_LiveAdapter extends RecyclerView.Adapter<T_LiveAdapter.LiveHolder> {

    Context context;
    ArrayList<T_LiveModel>liveList;
    OnLiveClickListener listener;
    int count=-1;
    String type="all";
    public T_LiveAdapter(Context context, ArrayList<T_LiveModel> liveList,String type, OnLiveClickListener listener) {
        this.context = context;
        this.liveList = liveList;
        this.listener = listener;
        this.type=type;
    }

    public interface OnLiveClickListener{
        void onLiveClick(int position);
        void onItemClick(int postion);
            void onDelete(int position);
            void  onEdit(int position);
            void onGoLive(int position);
    }
    @NonNull
    @Override
    public LiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LiveHolder(LayoutInflater.from(context).inflate(R.layout.row_teacher_live,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveHolder holder, int position) {
        int adapterPosition=position;
        T_LiveModel model=liveList.get(adapterPosition);



        Log.e("start",""+model.getStart_time());
        Log.e("end",""+model.getEnd_time());
        int liveDuration= CommonMethods.getDurationBetweenTime(model.getStart_time(),model.getEnd_time());
        holder.tv_livetitle.setText("Live Class on "+ model.getTitle()+"\nDuration : "+liveDuration+" min");
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getCover_image())
                .placeholder(R.drawable.logoo).into(holder.iv_thumbnail);

        if(model.getIs_live().equals("1")){
            holder.tv_golive.setVisibility(View.VISIBLE);
            holder.lin_liveTime.setVisibility(View.GONE);
           // holder.tv_golive.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.light_green));
        }else{
            holder.tv_golive.setVisibility(View.GONE);
            holder.lin_liveTime.setVisibility(View.VISIBLE);
            if (CommonMethods.isToday(model.getDate())){
                holder.tv_liveDate.setText("Today");
            }else{
                holder.tv_liveDate.setText(model.getDate());
            }
            holder.tv_liveTime.setText(model.getStart_time());
           // holder.tv_golive.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.primary_color));
        }
        holder.tv_golive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make validation for active live or not
                if(model.getIs_live().equals("1")) {
                    listener.onGoLive(adapterPosition);
                }
            }
        });

        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count==adapterPosition){

        }else {
            holder.cv_edi.setVisibility(View.GONE);
        }
        holder.img_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapterPosition;
                if(holder.cv_edi.getVisibility ()==View.VISIBLE)
                {
                    holder.cv_edi.setVisibility (View.GONE);
                }
                else
                {
                    holder.cv_edi.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });

        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition);

            }
        });
        holder.dailog_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(adapterPosition);
            }
        });



        //------------------------timer-----------///
        if (!model.getIs_live().equals("1")) {
            try {
                if (CommonMethods.isTodayDate(model.getDate())) {
                if (!CommonMethods.isEventExpired(model.getDate()+" | "+model.getEnd_time())) {

//                        TimerAsyncTask timerTask = new TimerAsyncTask(context, holder.tv_golive, holder.lin_liveTime);
//                        timerTask.execute(new String[]{model.getDate(), model.getStart_time(), model.getEnd_time()});
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                long milSecond = CommonMethods.getTimeGapBetweenTime(CommonMethods.getCurrentTime("h:mm:ss a"),
                                        CommonMethods.changeDateTimeFmt("h:mm a", "h:mm:ss a", model.getStart_time()));
                                Log.e("left", "" + milSecond);
                                if (milSecond > 0) {
                                    new CountDownTimer(milSecond, 1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            try {
                                               holder.lin_liveTime.setVisibility(View.GONE);
                                                holder.tv_golive.setVisibility(View.VISIBLE);
                                                liveList.get(adapterPosition).setIs_live("1");
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                }
                            }
                        });
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //-------timer--------------------//

    }

    @Override
    public int getItemCount() {
        return liveList.size();
    }







    public class LiveHolder extends RecyclerView.ViewHolder {
        TextView tv_livetitle,tv_golive,tv_liveDate,tv_liveTime;
        RoundedImageView iv_thumbnail;
        ImageView img_edit;
        TextView tv_manage,dailog_delete,dailog_edit;
        CardView cv_edi;
        RelativeLayout rel_manage;
LinearLayout lin_main,lin_liveTime;
        public LiveHolder(@NonNull View itemView) {
            super(itemView);
            tv_livetitle=itemView.findViewById(R.id.tv_title);
            tv_golive=itemView.findViewById(R.id.tv_see_details);
            iv_thumbnail=itemView.findViewById(R.id.thumbnail_image);
            tv_manage= itemView.findViewById(R.id.tv_manage);
            img_edit= itemView.findViewById(R.id.img_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            lin_main= itemView.findViewById(R.id.lin_main);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            rel_manage=itemView.findViewById(R.id.rel_manage);
            dailog_edit=itemView.findViewById(R.id.dailog_edit);

            tv_liveDate=itemView.findViewById(R.id.tv_live_date);
            tv_liveTime=itemView.findViewById(R.id.tv_live_time);
            lin_liveTime=itemView.findViewById(R.id.lin_time);

            if (type.equals("all")){
                rel_manage.setVisibility(View.VISIBLE);
            }else{
                rel_manage.setVisibility(View.GONE);
            }
        }
    }
}

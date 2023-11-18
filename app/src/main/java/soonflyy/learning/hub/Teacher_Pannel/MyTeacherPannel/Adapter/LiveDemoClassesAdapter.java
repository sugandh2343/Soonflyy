package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class LiveDemoClassesAdapter extends RecyclerView.Adapter<LiveDemoClassesAdapter.ViewHolder> {
    Context context;
    ArrayList<T_LiveModel> list;
    OnClickListener listener;
    int count=-1;

    public interface  OnClickListener{
         void onClick(int position);
         void onGoLive(int position,T_LiveModel model);
         void onDelete(int position,String liveId);
    }

    public LiveDemoClassesAdapter(Context context, ArrayList<T_LiveModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_live_claess,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        T_LiveModel model =list.get(adapterPosition);
        String thumbnail= BaseUrl.BASE_URL_MEDIA+model.getCover_image();
        Picasso.get().load(thumbnail).placeholder(R.drawable.logoo)
                .into(holder.rivThumbnail);
        holder.tvTitle.setText("Live Class on "+model.getTitle());
        Log.e("start",""+model.getStart_time());
        Log.e("end",""+model.getEnd_time());
        int liveDuration= CommonMethods.getDurationBetweenTime(model.getStart_time(),model.getEnd_time());
        holder.tvDuration.setText("Duration : "+liveDuration+" min");
        //--------set time and date----------------//
        if(model.getIs_live().equals("1")){
            holder.tvDate.setVisibility(View.GONE);
            holder.tvTime.setText("Go");
            // holder.tv_golive.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.light_green));
        }else{
            holder.tvDate.setVisibility(View.VISIBLE);
            if (CommonMethods.isToday(model.getDate())){
                holder.tvDate.setText("Today");
            }else{
                holder.tvDate.setText(model.getDate());
            }
            holder.tvTime.setText(model.getStart_time());
            // holder.tv_golive.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.primary_color));
        }
        //-----------------------------------//
        //----timer------------//
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
                                                holder.tvDate.setVisibility(View.GONE);
                                                holder.tvTime.setText("Go");
                                              //  holder.tv_golive.setVisibility(View.VISIBLE);
                                                list.get(adapterPosition).setIs_live("1");
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
        //-----------//

        //-----------listnere------------//
        if (count==adapterPosition){
        }else {
            holder.cvEdit.setVisibility(View.GONE);
        }
        holder.ivEdit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapterPosition;
                if(holder.cvEdit.getVisibility ()==View.VISIBLE)
                {
                    holder.cvEdit.setVisibility (View.GONE);
                }
                else
                {
                    holder.cvEdit.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition,model.getId());

            }
        });
        //------------------------//

        holder.linTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make validation for active live or not
                if(model.getIs_live().equals("1")) {
                    listener.onGoLive(adapterPosition,model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivThumbnail;
        TextView tvDuration,tvTitle,tvVideoTitle,tvDate,tvTime,tvDelete;
        LinearLayout linTime;
        ImageView playIcon,ivEdit;
        CardView cvEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rivThumbnail=itemView.findViewById(R.id.thumbnail_image);
            tvDuration=itemView.findViewById(R.id.tv_duration);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvVideoTitle=itemView.findViewById(R.id.tv_video_name);
            linTime=itemView.findViewById(R.id.lin_time);
            playIcon=itemView.findViewById(R.id.play_icon);
            tvDate=itemView.findViewById(R.id.tv_live_date);
            tvTime=itemView.findViewById(R.id.tv_live_time);
            tvDelete=itemView.findViewById(R.id.dailog_delete);
            cvEdit=itemView.findViewById(R.id.cv_edi);
            ivEdit=itemView.findViewById(R.id.img_edit);


            playIcon.setVisibility(View.GONE);
            linTime.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            tvDuration.setVisibility(View.VISIBLE);
            tvVideoTitle.setVisibility(View.GONE);
        }
    }
}

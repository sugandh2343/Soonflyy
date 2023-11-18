package soonflyy.learning.hub.adapter;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.Live;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveHolder> {
    Context context;
    List<Live>liveList;
   OnLiveClickListener listener;
   String from;

    public LiveAdapter(Context context, String from,List<Live> liveList, OnLiveClickListener listener) {
        this.context = context;
        this.liveList = liveList;
        this.listener = listener;
        this.from=from;
    }


//    public LiveAdapter(Context context, List<Live> liveList) {
//        this.context = context;
//        this.liveList = liveList;
//    }

        public interface OnLiveClickListener{
        void goLive(int position,Live liveModel);
    }
    @NonNull
    @Override
    public LiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LiveHolder(LayoutInflater.from(context).inflate(R.layout.row_live,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveHolder holder, int position) {
        int adapterPosition=position;
        Live liveModel=liveList.get(position);
        if (from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_STUDENT)){
            Picasso.get().load(BaseUrl.SCHOOL_BASE_URL_MEDIA + liveModel.getCover_image())//getThumbnail()
                    .placeholder(R.drawable.logoo).into(holder.iv_thumbnail);
        }else {
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + liveModel.getThumbnail())
                    .placeholder(R.drawable.logoo).into(holder.iv_thumbnail);
        }
        holder.tv_title.setText("Live Class on "+liveModel.getTitle());
        if (liveModel.getIs_live().equals("0"))
        holder.tv_join_time.setText(liveModel.getStart_time());
        else
            holder.tv_join_time.setText("Join");

        holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveModel.getIs_live().equals("1")) {
                    listener.goLive(adapterPosition, liveModel);
                }else{
                    CommonMethods.showSuccessToast(context,"Teacher is not live");
                }
            }
        });

        if (!liveModel.getIs_live().equals("1")) {
            try {
                if (CommonMethods.isTodayDate(liveModel.getDate())) {
                    if (!CommonMethods.isEventExpired(liveModel.getDate()+" | "+liveModel.getEnd_time())) {

//
                        new Handler(context.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                long milSecond = CommonMethods.getTimeGapBetweenTime(CommonMethods.getCurrentTime("hh:mm:ss a"),
                                        CommonMethods.changeDateTimeFmt("hh:mm a","hh:mm:ss a",liveModel.getStart_time()));
                                Log.e("left", "" + milSecond);
                                if (milSecond > 0) {
                                    new CountDownTimer(milSecond, 1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            try {
                                                liveList.get(adapterPosition).setIs_live("1");
                                                holder.tv_join_time.setText("Join");

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

//        holder.tv_course.setText(liveModel.getCourse_name());
//        holder.tv_chapter.setText(liveModel.getChapter_name());
//        holder.tv_topic.setText(liveModel.getTopic_name());
//        holder.tv_teacher.setText(liveModel.getTeacher_name());
//        holder.tv_time.setText(liveModel.getStart_time()+ " to "+liveModel.getEnd_time());
//        holder.tv_date.setText(liveModel.getDate());
//       // Log.e("livedate",liveModel.getDate());
//
//        if (liveModel.getIs_live().equals("1"))
//            holder.lin_live.setVisibility(View.VISIBLE);
//        else
//            holder.lin_live.setVisibility(View.GONE);

//        holder.live_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (liveModel.getIs_live().equals("1")) {
//                    listener.goLive(position, liveModel);
//                }else{
//                    CommonMethods.showSuccessToast(context,"Teacher is not live");
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return liveList.size();
    }

    public class LiveHolder extends RecyclerView.ViewHolder {
//        TextView tv_course,tv_chapter,tv_topic,tv_teacher,tv_time,tv_date;
//        LinearLayout lin_live;
//        CardView live_card;

        RoundedImageView iv_thumbnail;
        TextView tv_title,tv_join_time;



        public LiveHolder(@NonNull View itemView) {
            super(itemView);
//            live_card=itemView.findViewById(R.id.live_card);
//            lin_live=itemView.findViewById(R.id.lin_live);
//            tv_course=itemView.findViewById(R.id.tv_course);
//            tv_chapter=itemView.findViewById(R.id.tv_chapter);
//            tv_topic=itemView.findViewById(R.id.tv_topic);
//            tv_teacher=itemView.findViewById(R.id.tv_teacher);
//            tv_time=itemView.findViewById(R.id.tv_time);
//            tv_date=itemView.findViewById(R.id.tv_date);
            iv_thumbnail= itemView.findViewById(R.id.thumbnail_image);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_join_time= itemView.findViewById(R.id.tv_join_live);
        }
    }
}

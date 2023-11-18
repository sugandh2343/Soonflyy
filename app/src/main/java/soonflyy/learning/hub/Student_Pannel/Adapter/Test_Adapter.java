package soonflyy.learning.hub.Student_Pannel.Adapter;

import static soonflyy.learning.hub.Common.CommonMethods.getCurrentDateTime;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Test_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Test_Adapter extends RecyclerView.Adapter<Test_Adapter.TestHolder> {

    Context context;
    ArrayList<Test_Model>testList;
    OnTestClickListener listener;
    String from;

    public interface OnTestClickListener{
        void onItemClick(int position);
    }

    public Test_Adapter(Context context,String from,  ArrayList<Test_Model> testList,OnTestClickListener listener) {
        this.context = context;
        this.testList = testList;
        this.listener=listener;
        this.from=from;
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestHolder(LayoutInflater.from(context).inflate(R.layout.test_single_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        if (position+1%2==0){
            holder.lin_layout.setBackgroundColor(ContextCompat.getColor(context, in.aabhasjindal.otptextview.R.color.grey));
        }else{
            holder.lin_layout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }

        Test_Model model=testList.get(position);
        holder.tv_title.setText(model.getTitle());

        //---------------------//
        String testStatus=model.getAttempted();
        if (testStatus.equals("1")){
            //attempted case
            holder.btn_start.setText("View Result");
            holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));
            if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SCHOOL_COACHING)) {
                //only view test
                holder.btn_start.setText("Details");
            }
           // holder.btn_start.setAlpha(0.4f);
        }else{
            if (from.equals(SIMPLEE_HOME_TUTOR)||from.equals(SCHOOL_COACHING)){
                //only view test
                holder.btn_start.setText("Details");
                holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));
               // holder.btn_start.setAlpha(0.4f);
            }else{
              //set test validation


        try {
            if (isTestExpired(model.getDate()+" | "+model.getEnd_time())){
                holder.btn_start.setVisibility(View.VISIBLE);
               // holder.btn_start.setEnabled(false);
                holder.btn_start.setText("Expired");
                holder.btn_start.setTextColor(ContextCompat.getColor(context,R.color.red));
                holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.white));
                holder.btn_start.setAlpha(1f);

            }else{
                if (CommonMethods.isCurrentDate(model.getDate())) {
                    holder.btn_start.setEnabled(true);
                    if (isTestTime(model.getStart_time(),model.getEnd_time())) {
                        holder.btn_start.setText("Start now");
                        holder.btn_start.setAlpha(1f);
                        holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                        holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));

                    }else{
                        holder.btn_start.setText("Start now");
                        holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                        holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));
                        holder.btn_start.setAlpha(0.4f);
                    }
                }else{
                    holder.btn_start.setText("Start now");
                    holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                    holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));
                    holder.btn_start.setAlpha(0.4f);
                }
           }
        } catch (ParseException e) {
            e.printStackTrace();
        }
            }
        }
        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("testclick","click");
               // if (model.getDate().equals(CommonMethods.getCurrentDate()))
              //  listener.onItemClick(holder.getAdapterPosition());
                if (from.equals(SCHOOL_COACHING)||from.equals(SIMPLEE_HOME_TUTOR)){
                    listener.onItemClick(holder.getAdapterPosition());
                }else {
                    try {
                        if (CommonMethods.isCurrentDate(model.getDate())) {
                            if (isTestTime(model.getStart_time(), model.getEnd_time()))
                                listener.onItemClick(holder.getAdapterPosition());
                            else {
                                if (isTestExpired(model.getDate() + " | " + model.getStart_time())) {
                                    if (model.getAttempted().equals("1")){
                                        listener.onItemClick(holder.getAdapterPosition());
                                    }

                                }else {
                                    CommonMethods.showSuccessToast(context,
                                            "Test will be start on " + model.getDate() + " at " + model.getStart_time());
                                }
                            }
                        } else if (isTestExpired(model.getDate()+" | "+model.getStart_time())) {
                            if (model.getAttempted().equals("1"))
                                listener.onItemClick(holder.getAdapterPosition());
                            else
                            CommonMethods.showSuccessToast(context, "Test expired");
                        }else{
                            CommonMethods.showSuccessToast(context,"You can join test on "+ model.getDate());
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

       if (!from.equals(SCHOOL_COACHING) && ! from.equals(SIMPLEE_HOME_TUTOR)) {
           try {
               String startTime = model.getStart_time();
               if (isCurrentDate(model.getDate())) {
                   if (!isTestTime(model.getStart_time(), model.getEnd_time())) {
                       if (!isTestExpired(model.getDate() + " | " + model.getEnd_time())) {
                           //int leftTime = CommonMethods.getDurationBetweenTime(CommonMethods.getCurrentTime("hh:mm a"), startTime);
                           // long milSecond = leftTime * 60 * 1000;

                           long milSecond = CommonMethods.getTimeGapBetweenTime(CommonMethods.getCurrentTime("h:mm:ss a"),
                                   CommonMethods.changeDateTimeFmt("h:mm a", "h:mm:ss a", startTime));
                           Log.e("left", "" + milSecond);
                           if (milSecond > 0) {
                               new CountDownTimer(milSecond, 1000) {

                                   @Override
                                   public void onTick(long millisUntilFinished) {

                                   }

                                   @Override
                                   public void onFinish() {
                                       holder.btn_start.setText("Start now");
                                       holder.btn_start.setAlpha(1f);
                                       holder.btn_start.setTextColor(ContextCompat.getColor(context, R.color.white));
                                       holder.btn_start.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.graient2));


                                   }
                               }.start();
                           }
                       }
                   }
               }

           } catch (ParseException e) {
               e.printStackTrace();
           }
       }



    }

    public boolean isTestTime(String time1,String time2){
        boolean status = false;
        try {
            String current_time=CommonMethods.getCurrentTime("h:mm a");
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(time1);
            Date t2 = df.parse(time2);
            Date current=df.parse(current_time);
            if ((t1.equals(current)||current.after(t1)) && (current.before(t2))){
                status=true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;

    }

    public static boolean isTestExpired(String assignDate) throws ParseException {
        Log.e("time",assignDate);
        String currentDate = getCurrentDateTime();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy | hh:mm a");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.after(d1))
            return true;
        else
            return false;

    }
    public static boolean isCurrentDate(String testDate) throws ParseException {
        String currentDate=CommonMethods.getCurrentTime("dd-MMM-yyyy");
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(testDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.equals(d1))
            return true;
        else
            return false;
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView btn_start;
        LinearLayout lin_layout;
        public TestHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.header_tittle_tv);
            btn_start=itemView.findViewById(R.id.btn_start);
            lin_layout=itemView.findViewById(R.id.cl_subsription);
        }
    }


}

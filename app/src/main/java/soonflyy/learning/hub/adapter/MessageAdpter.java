package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common.CommonMethods;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.MessageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdpter  extends RecyclerView.Adapter<MessageAdpter.ViewHolder> {
    Context context;
    ArrayList<MessageModel> list;
    String fromId;
    String sssc="",da="",todayy="";
    String formattedDate="",yesterday="";

    String previous_date="";


    public MessageAdpter(Context context, ArrayList<MessageModel> list,String fromId) {
        this.context = context;
        this.list = list;
        this.fromId=fromId;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_messagechat,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        MessageModel model=list.get(position);
//        Date date = new Date();  // to get the date
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
//        formattedDate = df.format(date.getTime());
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//       yesterday =df.format(cal.getTime());
//
//        String api_date=model.getCreated_at ();
//        String[] sep=api_date.split (" ");
//        String dates= sep[0];
//
//
////        for(int i=0;i<list.size ();i++){
////            if(list.get (0).getTemp_date ().equals (list.get (0).getTemp_date ()))
////            {
////                holder.tv_date.setVisibility(View.GONE);
////            }
////        }
//
//    //
////               if(dates.equals (formattedDate))
////               {
////                   holder.tv_date.setText ("Today");
////               }
//////               else if(dates.equals (yesterday))
//////        {
//////            holder.tv_date.setText ("Yesterday");
//////        }
////               else {
//                  holder.tv_date.setText (dates);
////               }
//
//
//        Log.e ("date", "onBindViewHolder: "+formattedDate );
//
//
//
//        if(position>1){
//            String ss= list.get (position-1).getCreated_at ( );
//                   Log.e ("mmmmmm", "onResponse: "+ model.getCreated_at ( ));
//                   String[] sss=ss.split (" ");
//                    sssc= sss[0];
//            todayy="Today";
//        }
//        setTimeTextVisibility(sssc, holder.tv_date);

        ///set time--//
        int adapterPosition=position;
    //    Log.e("psition","ps "+adapterPosition);
        MessageModel model=list.get(adapterPosition);
        String dvalue=model.getCreated_at().substring(0,10);
      // Log.e("dvalue",""+dvalue);
        String date=CommonMethods.changeDateTimeFmt("yyyy-MM-dd","dd-MMM-yyyy",dvalue);
        Log.e("c_value",""+date);


        //--set today and date wise message together--//

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

        //----------//



//               for(int ii=0;ii<list.size ()-1;ii++){
//                   String ss= list.get (ii).getCreated_at ( );
//                   Log.e ("mmmmmm", "onResponse: "+ model.getCreated_at ( ));
//                   String[] sss=ss.split (" ");
//                    sssc= sss[0];
//
//                   String ate=list.get (ii+1).getCreated_at ();
//                   String[] s=ate.split (" ");
//                  da= s[0];
//                   Log.e ("check_date", " "+da+"--::--"+ sssc);
//
//                   Log.e ("djdjjd", " "+da+"----"+ sssc);
//
////                   if(da.equals (sssc))
////                   {
////                       holder.tv_date.setVisibility(View.GONE);
////                   }
////                   else
////                   {
////                       holder.tv_date.setVisibility(View.VISIBLE);
////                   }
//               }




//        char[] strArray = holder.tv_date.getText().toString().toCharArray();
//        for(char charItem:strArray) {
//            if(charCountMap.containsKey(charItem)) {
//                charCountMap.put(charItem,charCountMap.get(charItem)+1);
//            } else {
//                charCountMap.put(charItem,1);
//            }
//        if(model.getCreated_at ().equals ("")){
//
//        }
//        else{
//
//        }

        if (model.getMessage_type().equals("Text")){
            holder.tv_teacher_msg.setVisibility(View.VISIBLE);
            holder.image_view.setVisibility(View.GONE);

        }else if (model.getMessage_type().equals("File")){
            holder.tv_teacher_msg.setVisibility(View.GONE);
            holder.image_view.setVisibility(View.VISIBLE);
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getMessage_file())
                    .placeholder(R.drawable.image_gallery_24px)
                    .into(holder.image_view);
        }

        if (model.getFrom().equals(fromId)){
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.msg_layout.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.msg_layout.setLayoutParams(params);
            holder.tv_teacher_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
           //  holder.tv_teacher_msg.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            holder.tv_teacher_msg.setTextColor(Color.WHITE);
            holder.tv_teacher_msg.setText(model.getMessage_text());

        }else{

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.msg_layout.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.msg_layout.setLayoutParams(params);

            holder.tv_teacher_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.tv_teacher_msg.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            holder.tv_teacher_msg.setTextColor(Color.BLACK);
            holder.tv_teacher_msg.setText(model.getMessage_text());

        }
    }


    private void setTimeTextVisibility(String sssc, TextView tv_date) {

        if (sssc.equals (tv_date.getText ().toString ())) {
            tv_date.setVisibility (View.GONE);
            tv_date.setText ("");
        } else {
            if(tv_date.getText ().toString ().equals (formattedDate))
               {
                   tv_date.setText ("Today");
               }
             if(tv_date.getText ().toString ().equals (yesterday))
        {
            tv_date.setText ("Yesterday");
        }
            tv_date.setVisibility (View.VISIBLE);



        }
    }


    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_teacher_msg,tv_date;//,tv_student_msg
        RoundedImageView image_view;
        LinearLayout msg_layout;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_date= itemView.findViewById (R.id.tv_date);
          //  tv_student_msg=itemView.findViewById(R.id.tv_student_msg);
            tv_teacher_msg=itemView.findViewById(R.id.tv_teacher_msg);
            image_view=itemView.findViewById(R.id.image);
            msg_layout=itemView.findViewById(R.id.msg_layout);
        }
    }
}

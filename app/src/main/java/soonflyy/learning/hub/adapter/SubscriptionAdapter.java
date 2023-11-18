package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.MySubscriptionFragment;
import soonflyy.learning.hub.studentModel.SubscribedCourse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder> {
    private Context context;
    private List<SubscribedCourse> list;
    Fragment fragment;
    public SubscriptionAdapter(Context context,List<SubscribedCourse> list,Fragment fragment){
        this.context= context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subscription_single_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        int adapterPosition=position;
        SubscribedCourse course=list.get(adapterPosition);
        String url= BaseUrl.BASE_URL_MEDIA+course.getThumbnail();
        Picasso.get().load(url).placeholder(R.drawable.logoo).into(holder.img);
        holder.header_tittle_tv.setText(course.getTitle());
        holder.header_sub_tittle_tv.setText(course.getTeacher_name());
        holder.expire_date.setText("Expires on "+course.getExpiry_date());
        //-------------------//
        String rating=course.getRating_value();
        if (rating.equals("0")){
            holder.btn_rate.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
        }else{
            holder.btn_rate.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.parseFloat(rating));
        }
        //----------------------//

     holder.cl_subsription.setOnClickListener(v -> {
         ((MySubscriptionFragment)fragment).onclick(adapterPosition);
     });

     holder.btn_rate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ((MySubscriptionFragment)fragment).onRateClick(adapterPosition,course);

         }
     });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView header_tittle_tv,header_sub_tittle_tv,expire_date;
        RatingBar ratingBar;
        RoundedImageView img;
        ImageView play_img;
        ConstraintLayout cl_subsription;
        Button btn_rate;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            ratingBar=itemView.findViewById(R.id.rating_bar);
            header_tittle_tv = itemView.findViewById(R.id.header_tittle_tv);
            play_img = itemView.findViewById(R.id.play_img);
            header_sub_tittle_tv = itemView.findViewById(R.id.header_sub_tittle_tv);
            cl_subsription = itemView.findViewById(R.id.cl_subsription);
            expire_date=itemView.findViewById(R.id.exp_date_tv);
            btn_rate=itemView.findViewById(R.id.btn_rate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* if(clickListener!=null){
                clickListener.onClickItem(v,getAdapterPosition());
            }*/
        }
    }
}

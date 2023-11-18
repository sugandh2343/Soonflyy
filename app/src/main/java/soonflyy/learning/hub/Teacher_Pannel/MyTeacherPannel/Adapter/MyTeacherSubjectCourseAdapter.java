package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.MyTeacherSubjectCourseModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTeacherSubjectCourseAdapter extends RecyclerView.Adapter<MyTeacherSubjectCourseAdapter.Viewholder> {
    Context context;
    ArrayList<MyTeacherSubjectCourseModel> list,listFilter;
    OnCourseClickListener listener;
    int count=-1;
    String type;

    public interface  OnCourseClickListener {
        void onItemClick(int postion,MyTeacherSubjectCourseModel model);

    }
    public MyTeacherSubjectCourseAdapter(Context context, String type, ArrayList<MyTeacherSubjectCourseModel> list, OnCourseClickListener listener) {
        this.context=context;
        this.list =list;
        this.listener=listener;
        this.listFilter=list;
        this.type=type;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_myteacher_course_subject,null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        int adapterPosition=position;
        MyTeacherSubjectCourseModel model= list.get(adapterPosition);
        holder.tvName.setText(model.getCourse_name());
       setProfileData(holder,model);
        switch (position%2)
        {
            case 0: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                break;
            default: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;        }
        holder.lin_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition,model);

            }
        });






    }

    private void setProfileData(Viewholder holder, MyTeacherSubjectCourseModel model) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Log.e("Model>><<Get",model.getId());
        reference.child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imglink= "";
                if(snapshot.child("image").exists()){
                    imglink=snapshot.child("image").getValue(String.class);
                }
                Log.e("imgLink",""+imglink);
                if(!imglink.equals("")){
                    Picasso.get().load(imglink).placeholder(R.drawable.logoo)
                            .into(holder.ivProfileImg);
                }else{
                    holder.ivProfileImg.setImageResource(R.drawable.logoo);
                }

                holder.tvCourseSub.setText(snapshot.child("name").getValue(String.class));
                holder.tvMobile.setText("+91-"+snapshot.child("mobile").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String cSubtitle="";

//        if (type.equals("course")){
//            cSubtitle="Course: ";
//            ArrayList<String>courseList=model.getAssignCourses();
//            for (int i=0;i<courseList.size();i++){
//                if (i==0){
//                    cSubtitle= cSubtitle+ courseList.get(i);
//                }else{
//                    cSubtitle= cSubtitle+" | "+ courseList.get(i);
//                }
//            }
//          //  cSubtitle="Course: "+model.getCourse_name();
//        }
//        else if (type.equals("subject")){
//            cSubtitle="Subject: ";
//            ArrayList<String>subjList=model.getAssignSubjects();
//            for (int i=0;i<subjList.size();i++) {
//                if (i == 0) {
//                    cSubtitle = cSubtitle + subjList.get(i);
//                } else {
//                    cSubtitle = cSubtitle + " | " + subjList.get(i);
//                }
//            }
//        }


    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    ArrayList<MyTeacherSubjectCourseModel> filteredList = new ArrayList<>();
                    for (MyTeacherSubjectCourseModel row : listFilter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName ().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
//                        else
//                        {
//                            Toast.makeText (context, "No Information Found", Toast.LENGTH_SHORT).show ( );
//                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<MyTeacherSubjectCourseModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tvName,tvMobile,tvCourseSub;
        CircleImageView ivProfileImg;

        CardView card_main;
        LinearLayout lin_click;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            card_main=itemView.findViewById(R.id.card_main);
            lin_click=itemView.findViewById(R.id.lin_click);
            tvName=itemView.findViewById(R.id.tittle_tv);
            tvMobile=itemView.findViewById(R.id.tv_mobile_number);
            tvCourseSub=itemView.findViewById(R.id.tv_course_subject);
            ivProfileImg=itemView.findViewById(R.id.img);

        }
    }
}

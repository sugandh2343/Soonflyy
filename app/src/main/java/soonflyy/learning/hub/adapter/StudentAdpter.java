package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.StudentAll;
import soonflyy.learning.hub.model.StudentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdpter extends RecyclerView.Adapter<StudentAdpter.ViewHolder> {
    Context context;
    ArrayList<StudentModel> list;
    ArrayList<StudentAll>studentList;
   // OnStudentListItemClick listItemClick;
    String type;
    String listType;


    public  interface OnStudentListItemClick{
        void onItemClick(int position,String courseId);
    }

    public StudentAdpter(Context context, ArrayList<StudentModel> list,ArrayList<StudentAll>studentList
            ,String type,String listType) {//,OnStudentListItemClick listItemClick
        this.context = context;
        this.list = list;
        this.studentList=studentList;
       /// this.listItemClick=listItemClick;
        this.type=type;
        this.listType=listType;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_studentlist,null);
        return new ViewHolder (view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listType !=null && (listType.equals("subscription")  || listType.equals("chat"))){
            StudentAll model=studentList.get(position);
            holder.tv_name.setText(model.getFirst_name());

            if (model.getUser_image() !=null){
                Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getUser_image())
                        .placeholder(R.drawable.men_img).into(holder.profile_image);
            }
        }else {
            StudentModel studentModel = list.get(position);
            holder.tv_name.setText(studentModel.getFirst_name());
            holder.tv_count.setText ("");
            holder.tv_lstmsg.setText ("");
           if (studentModel.getUser_image() !=null){
                Picasso.get().load(BaseUrl.BASE_URL_MEDIA+studentModel.getUser_image())
                        .placeholder(R.drawable.men_img).into(holder.profile_image);

            }

        }
    }

    @Override
    public int getItemCount() {
        if (listType !=null && (listType.equals("subscription")  || listType.equals("chat")))
            return studentList.size();
        else
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_message;
        CircleImageView profile_image;
        TextView tv_name,tv_count,tv_lstmsg;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tv_count=itemView.findViewById (R.id.tv_count);
            iv_message=itemView.findViewById (R.id.iv_message);
            tv_name=itemView.findViewById(R.id.header_tittle_tv);
            tv_lstmsg=itemView.findViewById(R.id.tv_lstmsg);
            profile_image=itemView.findViewById(R.id.img);

            if (type.equals("teacher") &&(listType.equals("assignment"))){
                iv_message.setVisibility(View.GONE);
            }
            iv_message.setOnClickListener (v -> {
               /* Fragment fragment = new MessageFragment ();
                FragmentManager fragmentManager = ((TeacherMainActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_layout,fragment).addToBackStack(null).commit();

                */
            });
        }

    }

}

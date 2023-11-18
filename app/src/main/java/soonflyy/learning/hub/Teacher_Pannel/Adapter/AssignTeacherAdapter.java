package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignTutors;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignTeacherAdapter extends RecyclerView.Adapter<AssignTeacherAdapter.TeacherHolder> {

    Context context;
    ArrayList<AssignTutors>teacherList;
    ArrayList<AssignTutors>filterList;
    OnAssignListener onAssignListener;

    public AssignTeacherAdapter(Context context, ArrayList<AssignTutors> teacherList,
                                OnAssignListener onAssignListener) {
        this.context = context;
        this.teacherList = teacherList;
        this.filterList=teacherList;
        this.onAssignListener = onAssignListener;
    }

    public interface OnAssignListener{
        void onAssign(int position,String assignId,String name,String image);
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString().trim();
                if (charString.isEmpty()){
                    teacherList=filterList;
                }else{
                    ArrayList<AssignTutors> dataFilteredList=new ArrayList<>();
                    for (AssignTutors row: filterList){
                        String name=row.getName();
                        if (name!=null && !TextUtils.isEmpty(name)) {
                            if (row.getName().toLowerCase()
                                    .contains(charString.toLowerCase())) {
                                dataFilteredList.add(row);
                            }
                        }
                    }
                    teacherList=dataFilteredList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=teacherList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                teacherList=(ArrayList<AssignTutors>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherHolder(LayoutInflater.from(context).inflate(R.layout.row_assign_profile_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        int adapterPosition=position;
        AssignTutors model=teacherList.get(adapterPosition);
        setData(holder,model);

        //-------btn listener----------------------//
        holder.btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignListener.onAssign(adapterPosition,model.getUid(),model.getName(), model.getImage());
            }
        });
        //--------------------------------------------//
        //----------set list color----------------//
        switch (adapterPosition%2){
            case 0:
                holder.relProfile.setBackgroundColor(ContextCompat.getColor(context,R.color.white_smoke));

                break;
            case 1:
                holder.relProfile.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
                break;
            default:
                holder.relProfile.setBackgroundColor(ContextCompat.getColor(context,R.color.white_smoke));
                break;

        }
        //----------------------------------------//

    }

    private void setData(TeacherHolder holder, AssignTutors model) {
        holder.tvProfileName.setText(model.getName());

        holder.tvMobile.setVisibility(View.GONE);
        String imgUrl= model.getImage();
        Log.e("imgLInk",""+imgUrl);
        if(model.getImage()==null){
            holder.ivProfileImg.setImageResource(R.drawable.logoo);
        }else{
            Picasso.get().load(imgUrl).placeholder(R.drawable.logoo).into(holder.ivProfileImg);
        }

    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }


    public class TeacherHolder extends RecyclerView.ViewHolder {
        TextView tvProfileName,tvType;
        ImageView tvMobile;
        Button btnAssign;
        CircleImageView ivProfileImg;
        RelativeLayout relProfile;
        public TeacherHolder(@NonNull View itemView) {
            super(itemView);
            relProfile=itemView.findViewById(R.id.rel_profile);
            tvProfileName=itemView.findViewById(R.id.assign_tv_name);
            ivProfileImg=itemView.findViewById(R.id.assign_iv_profile_img);
            tvMobile=itemView.findViewById(R.id.assign_tv_mobile);
            tvType=itemView.findViewById(R.id.tv_type);
            btnAssign=itemView.findViewById(R.id.btn_profile);

            tvType.setVisibility(View.GONE);
            btnAssign.setText("Assign");
            btnAssign.setVisibility(View.VISIBLE);

        }
    }
}

package soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Adapter;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_IndepentTutorSection.Model.Indp_TutorManageStudentModel;

import java.util.ArrayList;
import java.util.Locale;

public class Indp_TutorManageStudentAdapter extends RecyclerView.Adapter<Indp_TutorManageStudentAdapter.ViewHolder> {
    Context context;
    ArrayList<Indp_TutorManageStudentModel> list;
    OnSelectClickListener listener;
    int count = -1;
    String from;

    public interface OnSelectClickListener {
        void onItemClick(int postion);

        void onPerformanceClik(int postion);

        void onBlock(int position,String statusValue);

        void onEdit(int position);
        void onDelete(int position,Indp_TutorManageStudentModel model);


    }

    public Indp_TutorManageStudentAdapter(Context context,String from, ArrayList<Indp_TutorManageStudentModel> list, OnSelectClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.from=from;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_managestudent, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPositino = position;
        Indp_TutorManageStudentModel model=list.get(adapterPositino);

        if (!TextUtils.isEmpty(model.getName())) {
            holder.tv_profile_text.setText(model.getName().substring(0, 1).toUpperCase(Locale.ROOT));
        }else{
            holder.tv_profile_text.setText("");
        }
        if (!TextUtils.isEmpty(model.getName())){
            if (!model.getName().equalsIgnoreCase("null")){
                holder.tv_name.setText(model.getName());
            }else{
                holder.tv_name.setText("Name : ");
            }
        }else{
            holder.tv_name.setText("Name : ");
        }

        //father name--------------//
        if (!TextUtils.isEmpty(model.getFather_name())){
            if (!model.getFather_name().equalsIgnoreCase("null")){
                holder.tvFatherName.setText("Father : "+model.getFather_name());
            }else{
                holder.tvFatherName.setText("Father : ");
            }
        }else{
            holder.tvFatherName.setText("Father : ");
        }
        //--------------------//

      //  holder.tv_name.setText(model.getName());
        holder.tv_mob_no.setText("Mobile : "+model.getMobile());
        if (!TextUtils.isEmpty(model.getEmail())) {
            if (!model.getEmail().equalsIgnoreCase("null")) {
                holder.tv_emailid.setText("Email-ID : " + model.getEmail());
            }else{
                holder.tv_emailid.setText("Email-ID : ");
            }
        }else{
            holder.tv_emailid.setText("Email-ID : ");
        }
        try {
           // holder.tv_percentage.setText(Integer.parseInt(model.getPercantage())+"%");
            holder.tv_percentage.setText(Double.valueOf(model.getPercantage()).intValue() + "%");
        }catch (Exception e){
            holder.tv_percentage.setText("0 %");
            e.printStackTrace();
        }


        /*if (model.getPercantage().equals("0")){
            holder.tv_performance.setVisibility(View.GONE);
        }else{
            holder.tv_performance.setVisibility(View.VISIBLE);
        }

         */

        if (!TextUtils.isEmpty(model.getAddress())) {
            if (!model.getAddress().equalsIgnoreCase("null")) {
                holder.tv_address.setText("Address : " + model.getAddress());
            }else{
                holder.tv_address.setText("Address : ");
            }
        }else{
            holder.tv_address.setText("Address : ");
        }
        //holder.tv_address.setText("Address : "+model.getAddress());
        if (from.equals(SCHOOL_COACHING)){
            holder.tv_class.setText("Class : "+model.getClass_name());
            holder.tv_section.setText("Section : "+model.getSection());
           // holder.tv_class.setVisibility(View.VISIBLE);
            //holder.tv_section.setVisibility(View.VISIBLE);
            holder.lin_class_section.setVisibility(View.VISIBLE);
            holder.tv_admnum.setText("Admin No.: "+model.getStudent_id());
            holder.tv_admnum.setVisibility(View.VISIBLE);
        }
        if (from.equals(INDEPENDENT_TUTOR)){
            //holder.tv_class.setVisibility(View.GONE);
            //holder.tv_section.setVisibility(View.GONE);
            holder.lin_class_section.setVisibility(View.GONE);
            holder.tv_admnum.setVisibility(View.GONE);
        }
        if (model.getIs_block().equals("1")){
            holder.dailog_delete.setText("Unblock");
        }else{
            holder.dailog_delete.setText("Block");
        }

        if (adapterPositino+1<=4){
            switch (adapterPositino+1){
                case 1:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 2:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 3:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 4:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }else{
            switch ((adapterPositino+1)%4){
                case 0:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 1:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 2:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 3:
                    holder.tv_profile_text.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }

        switch (position % 2) {
            case 0:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
        }


        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
        holder.tv_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPerformanceClik(position);
            }
        });


        holder.rel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count == adapterPositino) {
            holder.cv_edi.setVisibility(View.VISIBLE);
        } else {
            holder.cv_edi.setVisibility(View.GONE);
        }


        ///---------temporary hold-------/
      holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click","edit");
                count = adapterPositino;
                if (holder.cv_edi.getVisibility() == View.VISIBLE) {
                    holder.cv_edi.setVisibility(View.GONE);
                } else {
                    holder.cv_edi.setVisibility(View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });
        //-------------------------------------------//

//         */
//        holder.dailog_edit.setOnClickListener (new View.OnClickListener ( ) {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getIs_block().equals("1")) {
                    listener.onBlock(adapterPositino, "0");
                }else{
                    listener.onBlock(adapterPositino, "1");
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPositino,model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_manage;
        RelativeLayout rel_edit;
        ImageView img_edit;
        TextView dailog_delete, dailog_edit;
        LinearLayout lin_class_section;

        CardView cv_edi;
        LinearLayout lin_main;
        ImageView img_student;
        TextView tv_profile_text, tv_admnum,tv_percentage, tv_name,tvFatherName,tv_mob_no, tv_emailid, tv_class, tv_section, tv_address, tv_performance,tvDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            img_edit = itemView.findViewById(R.id.img_edit);
            rel_edit = itemView.findViewById(R.id.rel_edit);
            // dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            lin_main = itemView.findViewById(R.id.lin_main);
            lin_manage = itemView.findViewById(R.id.lin_manage);
            tv_performance = itemView.findViewById(R.id.tv_performance);
            img_student = itemView.findViewById(R.id.img_student);
            tv_admnum = itemView.findViewById(R.id.tv_admnum);
            tv_mob_no = itemView.findViewById(R.id.tv_mob_no);
            tv_emailid = itemView.findViewById(R.id.tv_emailid);
            tv_class = itemView.findViewById(R.id.tv_class);
            tv_section = itemView.findViewById(R.id.tv_section);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_name=itemView.findViewById(R.id.assign_tv_name);
            tv_percentage=itemView.findViewById(R.id.tv_percentage);
            lin_class_section=itemView.findViewById(R.id.lin_class_section);
            tv_profile_text=itemView.findViewById(R.id.tv_profile_text);
            tvDelete=itemView.findViewById(R.id.tv_delete);
            tvFatherName=itemView.findViewById(R.id.tv_father);

        }
    }
}

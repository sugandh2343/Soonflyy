package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginClassModel;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolModel.SchoolCoachingTutorAsginModel;

import java.util.ArrayList;
import java.util.Locale;

public class SchoolCoachingTutorAsginAdapter extends RecyclerView.Adapter<SchoolCoachingTutorAsginAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolCoachingTutorAsginModel> list;
    OnClickListener listener;
    int count = -1;
    int adapter_positon=-1;
      SchoolCoachingTutorAsginClassAdapter asginClassAdapter;
      ArrayList<SchoolCoachingTutorAsginClassModel> asginClassModels;
    public interface  OnClickListener {
        void onItemClick(int postion);
        void onAsignTutor_Click(int postion);

        void onViewClass(int position,SchoolCoachingTutorAsginClassModel assignClass);

        void onUpdate(int position,SchoolCoachingTutorAsginClassModel assignModel);
        void onDelete(int position,SchoolCoachingTutorAsginModel assignModel);
        void onBlockUnblock(int position,SchoolCoachingTutorAsginModel assignModel,String blockValue);
        void onRemove(int position,SchoolCoachingTutorAsginClassModel assignModel);
    }

    public SchoolCoachingTutorAsginAdapter(Context context, ArrayList<SchoolCoachingTutorAsginModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_asign_tutor,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition = position;
        SchoolCoachingTutorAsginModel model=list.get(position);
        holder.tv_profile_text.setText(model.getName().substring(0,1).toUpperCase(Locale.ROOT));
        holder.tv_name.setText(model.getName());
        holder.tv_mobile.setText("Mobile: "+model.getMobile());

        String blockStatus= model.getBlock_status();
        if (blockStatus.equals("0")){
            holder.tvBlockUnblock.setText("Block");
        }else {
            holder.tvBlockUnblock.setText("Unblock");
        }
        try {
          //  holder.tv_percentage.setText(Integer.parseInt(model.getPercantage()) + "%");Double.valueOf(s).intValue();
            holder.tv_percentage.setText(Double.valueOf(model.getPercantage()).intValue() + "%");
        }catch (Exception e){
            holder.tv_percentage.setText("-");
            e.printStackTrace();
        }



//        if (model.getAssign_class()==null||model.getAssign_class().size()>0) {
//            holder.lin_assign_class.setVisibility(View.VISIBLE);
//            asignClassRecyclerView(holder.rcv_asign_class, model.getAssign_class(),position);
//        }else{
//            holder.lin_assign_class.setVisibility(View.GONE);
//        }

        //profile with diffrent color
        if (adapterPosition+1<=4){
            switch (adapterPosition+1){
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
            switch ((adapterPosition+1)%4){
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

        switch (adapterPosition % 2) {
            case 0:
                // holder.profile_image.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
            case 1:

                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
            default:
                holder.lin_main.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                break;
        }
        holder.tv_asign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAsignTutor_Click(position);
            }
        });

        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });

        //------------------3 dot functinality----//
       holder.rel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count==adapterPosition){
            holder.cv_edi.setVisibility(View.VISIBLE);
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
        holder.tvDelete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition,model);
            }
        });

        holder.tvBlockUnblock.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                String value="";
                if (blockStatus.equals("1")) {
                  value="0";
                }else{
                   value="1";
                }
                listener.onBlockUnblock(adapterPosition,model,value);
            }
        });

        //--------------------------------------------//

    }

    private void asignClassRecyclerView(RecyclerView rcv_asign_class,ArrayList<SchoolCoachingTutorAsginClassModel> assignClassList,int parentAdapterPosition)  {

//        asginClassModels = new ArrayList<>();
//        asginClassModels.clear();

        for (int i=0;i<assignClassList.size();i++){
            assignClassList.get(i).setParentPosition(parentAdapterPosition);
        }

        rcv_asign_class.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv_asign_class.setLayoutManager(new LinearLayoutManager(context));
        rcv_asign_class.setKeepScreenOn(true);

//        asginClassModels.add(new SchoolCoachingTutorAsginClassModel());
//        asginClassModels.add(new SchoolCoachingTutorAsginClassModel());
//        asginClassModels.add(new SchoolCoachingTutorAsginClassModel());

        asginClassAdapter = new SchoolCoachingTutorAsginClassAdapter(context, assignClassList, new SchoolCoachingTutorAsginClassAdapter.OnClickListener() {
            @Override
            public void onItemClick(int postion) {

            }
            @Override
            public void onAsignTutor_Click(int postion) {

            }
            @Override
            public void onView(int position) {
                SchoolCoachingTutorAsginClassModel model=assignClassList.get(position);
                listener.onViewClass(model.getParentPosition(),model);


            }
            @Override
            public void onUpdateSchedule(int position) {
                SchoolCoachingTutorAsginClassModel model=assignClassList.get(position);
                listener.onUpdate(model.getParentPosition(),model);

            }

            @Override
            public void onRemovePeriod(int position) {
                SchoolCoachingTutorAsginClassModel model=assignClassList.get(position);
                listener.onRemove(model.getParentPosition(),model);
            }


        });
        rcv_asign_class.setAdapter(asginClassAdapter);
        asginClassAdapter.notifyDataSetChanged();
//        if (rec.equals("0")){
//           rcv_asign_class.setVisibility(View.VISIBLE);
//        }
//
//        else {
//        rcv_asign_class.setAdapter(asginClassAdapter);
//        asginClassAdapter.notifyDataSetChanged();
//        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_main,rel_edit;
        LinearLayout lin_main;
        TextView tv_chapter_title,tv_chaper_no,tv_asign;
        ImageView img_edit;
        CardView cv_edi;
        TextView tvBlockUnblock,tvDelete;
        RecyclerView rcv_asign_class;

        TextView tv_name,tv_mobile,tv_profile_text,tv_percentage;
        CardView lin_assign_class;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_asign_class= itemView.findViewById(R.id.rcv_asign_class);
            tv_asign= itemView.findViewById(R.id.tv_asign);
            lin_main= itemView.findViewById(R.id.lin_main);
            rel_main = itemView.findViewById(R.id.rel_main);
            tv_chapter_title= itemView.findViewById(R.id.tv_chapter_title);
            tv_chaper_no= itemView.findViewById(R.id.tv_chaper_no);
            rel_main = itemView.findViewById(R.id.rel_main);
            rel_main=itemView.findViewById(R.id.rel_main);
            cv_edi=itemView.findViewById(R.id.cv_edi);
            img_edit= itemView.findViewById(R.id.img_edit);
            rel_edit = itemView.findViewById(R.id.rel_edit);
            tvDelete=itemView.findViewById(R.id.tv_delete);
            tvBlockUnblock=itemView.findViewById(R.id.tv_block);

            tv_name=itemView.findViewById(R.id.assign_tv_name);
            tv_mobile=itemView.findViewById(R.id.assign_tv_mobile);
            tv_profile_text=itemView.findViewById(R.id.tv_profile_text);
            tv_percentage=itemView.findViewById(R.id.tv_percentage);
            lin_assign_class=itemView.findViewById(R.id.lin_assign);

        }
    }


}

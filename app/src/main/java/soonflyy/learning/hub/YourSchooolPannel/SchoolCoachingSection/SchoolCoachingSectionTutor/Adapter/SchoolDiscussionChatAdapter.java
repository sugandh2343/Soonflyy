package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolDiscussionChatModel;

import java.util.ArrayList;
import java.util.Locale;

public class SchoolDiscussionChatAdapter extends RecyclerView.Adapter<SchoolDiscussionChatAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<SchoolDiscussionChatModel> list;
    ArrayList<SchoolDiscussionChatModel> filterList;
    OnChatClickListener listener;
    int count = -1;

    public interface OnChatClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);
    }

    public SchoolDiscussionChatAdapter(Context context, ArrayList<SchoolDiscussionChatModel> list, OnChatClickListener listener) {
        this.context = context;
        this.list = list;
        this.filterList=list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_discustion_chat,null);
        return new ViewHolder (view);    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition = position;

        SchoolDiscussionChatModel model=filterList.get(adapterPosition);
        holder.tv_Name.setText(model.getName ());
        holder.tv_title.setText(model.getLast_msg ());

        if (TextUtils.isEmpty(model.getUn_seen_count())){
            holder.tv_countnumber.setVisibility(View.GONE);
        }else {
            if (model.getUn_seen_count().equals("0")) {
                holder.tv_countnumber.setVisibility(View.GONE);
            } else {
                holder.tv_countnumber.setText(model.getUn_seen_count());
                holder.tv_countnumber.setVisibility(View.VISIBLE);
            }
        }

        if (!TextUtils.isEmpty(model.getName())) {
            holder.tv_profile_txt.setText(model.getName().substring(0, 1).toUpperCase(Locale.ROOT));
        }else{
            holder.tv_profile_txt.setText("U");
            holder.tv_Name.setText("Unknown");
        }
        if (adapterPosition+1<=4){
            switch (adapterPosition+1){
                case 1:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 2:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 3:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 4:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }else{
            switch ((adapterPosition+1)%4){
                case 0:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 1:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 2:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 3:
                    holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
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

        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick (position);
               // holder.cv_edi.setVisibility(View.GONE);
            }
        });
//        if (count == adapterPosition) {
//
//        } else {
//            holder.cv_edi.setVisibility(View.GONE);
//        }
//        holder.img_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                count = adapterPosition;
//                if (holder.cv_edi.getVisibility() == View.VISIBLE) {
//                    holder.cv_edi.setVisibility(View.GONE);
//                } else {
//                    holder.cv_edi.setVisibility(View.VISIBLE);
//
//                }
//                notifyDataSetChanged();
//            }
//        });
//
//
//        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onDelete(adapterPosition);
//            }
//        });
//
//        holder.lin_manage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(adapterPosition);
//            }
//        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterList = list;
                } else {
                    ArrayList<SchoolDiscussionChatModel> dataFilteredList = new ArrayList<>();
                    for (SchoolDiscussionChatModel row : list) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            dataFilteredList.add(row);
                        }
                    }
                    filterList = dataFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<SchoolDiscussionChatModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lin_main;
        TextView tv_profile_txt;
        TextView tv_Name,tv_title,tv_countnumber;
        ImageView img_edit;
        LinearLayout lin_manage;
        TextView tv_manage, dailog_delete,tv_date;
        CardView cv_1, cv_edi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_manage = itemView.findViewById(R.id.lin_manage);
            img_edit = itemView.findViewById(R.id.img_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            tv_date=itemView.findViewById(R.id.tv_date);

            cv_edi = itemView.findViewById(R.id.cv_edi);
           lin_main=itemView.findViewById (R.id.lin_main);
              tv_profile_txt=itemView.findViewById (R.id.profile_text);
            tv_Name=itemView.findViewById (R.id.tv_Name);
            tv_title =itemView.findViewById (R.id.tv_title);
        tv_countnumber=itemView.findViewById (R.id.tv_countnumber);
        }
    }
}

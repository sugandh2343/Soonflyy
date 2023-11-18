package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.TManageTestModel;

import java.util.ArrayList;

public class TManageTestAdapter extends RecyclerView.Adapter<TManageTestAdapter.manageholder> {
    Context context ;
    ArrayList<TManageTestModel> list;

    OnCourseClickListener listener;
    public interface  OnCourseClickListener {
        void onItemClick(int postion);

        void onSubscriptionClick(int position);

        void onDelete(int position);

        void onEdit(int position);
    }
    public TManageTestAdapter(Context context, ArrayList<TManageTestModel> list, OnCourseClickListener listener){
        this.context= context;
        this.list = list;
        this.listener=listener;

    }

    @NonNull
    @Override
    public manageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_teacher_test,null);
        return new manageholder (view);    }

    @Override
    public void onBindViewHolder(@NonNull manageholder holder, int position) {

        TManageTestModel model=list.get(position);
        switch (position%2)
        {
            case 0: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                break;
            default: holder.card_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
                break;        }


        holder.tv_title.setText(model.getTitle());
        holder.lin_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());

            }
        });
        holder.img_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if(holder.cv_edi.getVisibility ()==View.VISIBLE)
                {
                    holder.cv_edi.setVisibility (View.GONE);
                }
                else
                {
                    holder.cv_edi.setVisibility (View.VISIBLE);

                }
            }
        });
        holder.dailog_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });

        holder.dailog_delete.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class manageholder extends RecyclerView.ViewHolder {
        CardView card_main,cv_edi;
        ImageView img_edit;
        TextView dailog_edit,dailog_delete,tv_title;
LinearLayout lin_manage;

        public manageholder(@NonNull View itemView) {
            super(itemView);
            cv_edi=itemView.findViewById(R.id.cv_edi);
            img_edit= itemView.findViewById(R.id.img_edit);
            lin_manage= itemView.findViewById(R.id.lin_manage);
            dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete=itemView.findViewById(R.id.dailog_delete);
            tv_title=itemView.findViewById(R.id.header_tittle_tv);

            card_main=itemView.findViewById(R.id.card_main);
            dailog_delete.setVisibility(View.GONE);
        }
    }
}

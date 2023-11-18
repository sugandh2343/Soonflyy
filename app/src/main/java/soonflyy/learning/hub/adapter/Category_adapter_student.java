package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.Category_student_model;

import java.util.List;

public class Category_adapter_student extends RecyclerView.Adapter<Category_adapter_student.LiveHolder> {
    Context context;
    List<Category_student_model> modelList;
    OnCategoryClickListener listener;
    int index=0;


    public Category_adapter_student(Context context, List<Category_student_model> modelList,OnCategoryClickListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener=listener;

    }

    public interface OnCategoryClickListener{
        void onCategoryClick(int position,Category_student_model catModel);
    }

    @NonNull
    @Override
    public LiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LiveHolder(LayoutInflater.from(context).inflate(R.layout.row_categroies, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveHolder holder, int position) {

        Category_student_model model = modelList.get(position);
        holder.tv_category.setText(model.getName());
        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                index=holder.getAdapterPosition();
//                notifyDataSetChanged();
                listener.onCategoryClick(holder.getAdapterPosition(),model);
            }
        });

        if (model.isSelected()){//index==holder.getAdapterPosition()
            holder.rel_main.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
            holder.tv_category.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else{
             holder.rel_main.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.rec_bg));
            holder.tv_category.setTextColor(ContextCompat.getColor(context,R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public  void setCategoryColor(RecyclerView.ViewHolder holder,int position){
       index=position;
        View view = holder.itemView;

        TextView tvCatName = view.findViewById(R.id.tv_category);
        RelativeLayout relCatMain = view.findViewById(R.id.rel_cat_main);
        relCatMain.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
        tvCatName.setTextColor(ContextCompat.getColor(context,R.color.white));
    }

public class LiveHolder extends RecyclerView.ViewHolder {
    TextView tv_category;
    RelativeLayout rel_main;

    public LiveHolder(@NonNull View itemView) {
        super(itemView);

        tv_category = itemView.findViewById(R.id.tv_category);
        rel_main=itemView.findViewById(R.id.rel_cat_main);

    }
}
}


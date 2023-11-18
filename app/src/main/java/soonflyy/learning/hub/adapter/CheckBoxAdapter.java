package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.Course_Category_Model;

import java.util.List;


public class CheckBoxAdapter extends RecyclerView.Adapter<soonflyy.learning.hub.adapter.CheckBoxAdapter.ViewHolder> {
    Context context;
   // ArrayList<CheckBoxModel> list;
    List<Course_Category_Model> list;
    OnSubCategorySelectedListener listener;


    public CheckBoxAdapter(Context context, List<Course_Category_Model> list, OnSubCategorySelectedListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    public interface OnSubCategorySelectedListener{
       void onSelection(int position, Course_Category_Model course);
    }

    @NonNull
    @Override
    public soonflyy.learning.hub.adapter.CheckBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_checkbox_items,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(soonflyy.learning.hub.adapter.CheckBoxAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelection(position,list.get(position));
            }
        });

      //  holder.checkbox.isChecked();
       /* holder.lin_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (holder.checkbox.isChecked())
               {
                   holder.checkbox.setChecked(false);
               }
               else {
                   holder.checkbox.setChecked(true);
               }
            }
        });

        */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_select;
        CheckBox checkbox;
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_select = itemView.findViewById(R.id.lin_select);
            checkbox = itemView.findViewById(R.id.checkbox);
            tv_name = itemView.findViewById(R.id.assign_tv_name);

        }
    }
}

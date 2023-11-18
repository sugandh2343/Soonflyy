package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.T_Course_provide_Model;

import java.util.ArrayList;

public class T_Course_provide_Adapter extends RecyclerView.Adapter<T_Course_provide_Adapter.myholder> {
    Context context;
    ArrayList<T_Course_provide_Model> provide_modelArrayList;

    public T_Course_provide_Adapter(Context context, ArrayList<T_Course_provide_Model> best_deal_models) {
        this.context = context;
        this.provide_modelArrayList = best_deal_models;

    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_tcourse_provide, viewGroup, false);
        return new myholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, @SuppressLint("RecyclerView") int position) {
        T_Course_provide_Model model1 = provide_modelArrayList.get(position);
        holder.title.setText(model1.getTitle());
        if (model1.isChecked()){
            holder.title.setChecked(true);
        }else{
            holder.title.setChecked(false);
        }

        holder.title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                provide_modelArrayList.get(holder.getAdapterPosition())
                        .setChecked(isChecked);
            }
        });


    }

    @Override
    public int getItemCount() {
        return provide_modelArrayList.size();
    }

    public class myholder extends RecyclerView.ViewHolder {
        CheckBox title;


        public myholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);


        }
    }

    public ArrayList<Integer>getSelectedOption(){
        ArrayList<Integer> list=new ArrayList<>();
        for (int i=0;i<provide_modelArrayList.size();i++){
            if (provide_modelArrayList.get(i).isChecked()){
                list.add(i);
            }
        }
        return list;
    }
}


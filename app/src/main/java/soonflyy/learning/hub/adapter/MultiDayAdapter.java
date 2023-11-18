package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.DayModel;

import java.util.ArrayList;

public class MultiDayAdapter extends RecyclerView.Adapter<MultiDayAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DayModel> list;
   OnChangeSelectionListener listener;

    public MultiDayAdapter(Context context, ArrayList<DayModel> list,
                           OnChangeSelectionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnChangeSelectionListener{
        void onChangeSelection(int position,boolean checked);
    }
    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vi = LayoutInflater.from(context).inflate(R.layout.row_multi_day_selector,null);
        ViewHolder holder = new ViewHolder(vi);
        return holder ;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        DayModel model=list.get (position);

        holder.itemCheckbox.setText (model.getName ());
        if (model.isSelected()){
            holder.itemCheckbox.setChecked(true);
        }else {
            holder.itemCheckbox.setChecked(false);
        }

        holder.itemCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean value;
                if (holder.itemCheckbox.isChecked()){
                    list.get(adapterPosition).setSelected(true);
                    value=true;
                }else{
                    list.get(adapterPosition).setSelected(false);
                    value=false;
                }
                listener.onChangeSelection(adapterPosition,value);
            }
        });

    }

    public  ArrayList<String> getSelectedDay(){
        ArrayList<String>idList=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            DayModel model=list.get(i);
            if (model.isSelected()){
                idList.add(model.getId());
            }
        }
        return idList;
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox itemCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            itemCheckbox =itemView.findViewById (R.id.item_checkbox);

        }
    }


}

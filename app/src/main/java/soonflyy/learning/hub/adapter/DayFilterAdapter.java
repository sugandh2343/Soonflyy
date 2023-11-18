package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.DayModel;

import java.util.ArrayList;

public class DayFilterAdapter extends RecyclerView.Adapter<DayFilterAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DayModel> list;
   OnChangeSelectionListener listener;

    public DayFilterAdapter(Context context, ArrayList<DayModel> list,
                            OnChangeSelectionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnChangeSelectionListener{
        void onChangeSelection(int position,DayModel dayModel);
    }
    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vi = LayoutInflater.from(context).inflate(R.layout.row_day_filter,null);
        ViewHolder holder = new ViewHolder(vi);
        return holder ;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        DayModel model=list.get (position);

        holder.tvItem.setText (model.getName ());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onChangeSelection(adapterPosition,model);
            }
        });

    }

//    public  ArrayList<String> getSelectedDay(){
//        ArrayList<String>idList=new ArrayList<>();
//        for (int i=0;i<list.size();i++){
//            DayModel model=list.get(i);
//            if (model.isSelected()){
//                idList.add(model.getId());
//            }
//        }
//        return idList;
//    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            tvItem =itemView.findViewById (R.id.tv_item);

        }
    }


}

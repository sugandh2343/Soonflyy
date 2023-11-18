package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolDiscustionModel;

import java.util.ArrayList;

public class SchoolDiscustionAdapter extends RecyclerView.Adapter<SchoolDiscustionAdapter.ViewHolder>
implements Filterable {

    Context context;
    ArrayList<SchoolDiscustionModel> classList;
    ArrayList<SchoolDiscustionModel>filterClassList;
    OnClickListener listener;
    int count = -1;
    public SchoolDiscustionAdapter(Context context, ArrayList<SchoolDiscustionModel> classList, OnClickListener listener) {
        this.context = context;
        this.classList = classList;
        this.filterClassList=classList;
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        String charString = constraint.toString();
                        if (charString.isEmpty()) {
                            filterClassList = classList;
                        } else {
                            ArrayList<SchoolDiscustionModel> dataFilteredList = new ArrayList<>();
                            for (SchoolDiscustionModel row : classList) {
                                if (row.getClass_name().toLowerCase().contains(charString.toLowerCase())) {
                                    dataFilteredList.add(row);
                                }
                            }
                            filterClassList = dataFilteredList;
                        }
                        FilterResults filterResults = new FilterResults();
                        filterResults.values = filterClassList;
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        filterClassList = (ArrayList<SchoolDiscustionModel>) results.values;
                        notifyDataSetChanged();
                    }
                };
        }

    public  interface OnClickListener{
        void onItemClick(int position);
        void onDelete(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new
                ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_discussion,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition = position;

        SchoolDiscustionModel model=classList.get(adapterPosition);
        holder.tv_class_name.setText("Class - "+model.getClass_name ());
        holder.tv_countnumber.setText(model.getSection_count ());
        holder.lin_main.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onItemClick (adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        CardView cv_main;
        TextView tv_class_name,tv_countnumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_main=itemView.findViewById (R.id.cv_main);
            lin_main=itemView.findViewById (R.id.lin_main);
            tv_class_name=itemView.findViewById (R.id.tv_class_name);
            tv_countnumber =itemView.findViewById (R.id.tv_countnumber);
        }
    }
}

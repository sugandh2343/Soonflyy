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
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SectionForDiscussion;

import java.util.ArrayList;


public class SectionForDiscussionAdaper extends RecyclerView.Adapter<SectionForDiscussionAdaper.SectionHolder>
implements Filterable {

   Context context;
    ArrayList<SectionForDiscussion>sectionList;
    ArrayList<SectionForDiscussion>filterSectionList;
    OnSectionClickListener listener;

    public SectionForDiscussionAdaper(Context context, ArrayList<SectionForDiscussion> sectionList, OnSectionClickListener listener) {
        this.context = context;
        this.sectionList = sectionList;
        this.filterSectionList=sectionList;
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterSectionList = sectionList;
                } else {
                    ArrayList<SectionForDiscussion> dataFilteredList = new ArrayList<>();
                    for (SectionForDiscussion row : sectionList) {
                        if (row.getSection_name().toLowerCase().contains(charString.toLowerCase())) {
                            dataFilteredList.add(row);
                        }
                    }
                    filterSectionList = dataFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterSectionList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterSectionList = (ArrayList<SectionForDiscussion>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface  OnSectionClickListener{
        void onSectionClick(int position);
    }


    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionHolder(LayoutInflater.from(context).inflate(R.layout.row_discussion,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionHolder holder, int position) {
        int adapterPosition = position;

        SectionForDiscussion model=sectionList.get(adapterPosition);
        holder.tv_class_name.setText("Section - "+model.getSection_name ());
        holder.tv_countnumber.setText(model.getStudent_count ());
        holder.lin_main.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                listener.onSectionClick (adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterSectionList.size();
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        CardView cv_main;
        TextView tv_class_name,tv_countnumber;
        public SectionHolder(@NonNull View itemView) {
            super(itemView);
            cv_main=itemView.findViewById (R.id.cv_main);
            lin_main=itemView.findViewById (R.id.lin_main);
            tv_class_name=itemView.findViewById (R.id.tv_class_name);
            tv_countnumber =itemView.findViewById (R.id.tv_countnumber);
        }
    }
}

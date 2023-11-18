package soonflyy.learning.hub.Common_Package.Common_Package.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import soonflyy.learning.hub.Common_Package.Models.IDViewProfileModel;
import soonflyy.learning.hub.R;

public class IDViewProfileAdapter extends RecyclerView.Adapter<IDViewProfileAdapter.ViewHolder>
implements Filterable {
   Context context;
   ArrayList<IDViewProfileModel>list;
    ArrayList<IDViewProfileModel>filtrableList;
    OnProfileClickListener listener;


    public IDViewProfileAdapter(Context context, ArrayList<IDViewProfileModel> list, OnProfileClickListener listener) {
        this.context = context;
        this.list = list;
        this.filtrableList=list;
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if (charString.isEmpty()){
                  list=filtrableList;
                }else{
                    ArrayList<IDViewProfileModel> dataFilteredList=new ArrayList<>();

                    for (IDViewProfileModel row: filtrableList){
                        Log.e("row",row.getName().toLowerCase());
                        Log.e("filter",charString);
                        if ((row.getName().toLowerCase()).contains(charString.toLowerCase())){
                            dataFilteredList.add(row);
                            Log.e("added",row.getName());
                        }
                    }
                   list=dataFilteredList;
//                    Log.e("fSize",""+filtrableList.size());
//                    if (filtrableList.size()>0){
//                        Log.e("contain",filtrableList.get(0).getName());
//                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
              //  filtrableList.clear();
                list=(ArrayList<IDViewProfileModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    public  interface OnProfileClickListener{
        void OnProfileClick( String  userId,String  isSchool);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_id_view_profile,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
       IDViewProfileModel model = list.get(adapterPosition);
        Picasso.get().load(model.getPhoto())
                .placeholder(R.drawable.profile).into(holder.profile_image);
       holder.tv_Name.setText(model.getName());
       holder.tv_user_title.setText(model.getWork());
        Log.e("position_color", "onBindViewHolder: " + adapterPosition % 2);

        switch (adapterPosition % 2) {
            case 0:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
            case 1:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white)));
                break;
            default:
                holder.lin_main.setBackgroundColor((context.getResources().getColor(R.color.white_smoke)));
                break;
        }
         holder.tv_viewprofile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.OnProfileClick(model.getUser_id(),model.getIs_school());
             }
         });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_main;
        ImageView profile_image;
        TextView tv_Name,tv_user_title,tv_viewprofile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_main= itemView.findViewById(R.id.lin_main);
                    profile_image= itemView.findViewById(R.id.profile_image);
            tv_Name= itemView.findViewById(R.id.tv_Name);
                    tv_user_title= itemView.findViewById(R.id.tv_user_title);
            tv_viewprofile= itemView.findViewById(R.id.tv_viewprofile);
        }
    }
}

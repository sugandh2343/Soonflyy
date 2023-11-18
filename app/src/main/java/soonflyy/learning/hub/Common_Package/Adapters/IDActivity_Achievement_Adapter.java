package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Models.IDAchievementModel;
import soonflyy.learning.hub.Common_Package.Models.IDActivityModel;
import soonflyy.learning.hub.R;

import java.util.ArrayList;
import java.util.HashMap;

public class IDActivity_Achievement_Adapter extends RecyclerView.Adapter<IDActivity_Achievement_Adapter.ItemHolder> {
    public HashMap<Integer,RecyclerView.ViewHolder> holderHasMap=new HashMap<>();
    Context context;
    ArrayList<IDActivityModel>activityList;
    ArrayList<IDAchievementModel>achievementList;
    String type;

    public IDActivity_Achievement_Adapter(Context context, ArrayList<IDActivityModel> activityList,
                                          ArrayList<IDAchievementModel> achievementList, String type) {
        this.context = context;
        this.activityList = activityList;
        this.achievementList = achievementList;
        this.type = type;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.id_activity_achevement_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        int adapterPosition=position;

        if (type.equals("activity")){
            holder.tv_title.setText(activityList.get(adapterPosition).getActivity_name());
        }else{
            //for achievement
            holder.tv_title.setText(achievementList.get(adapterPosition).getAchievement_name());

        }
    }

    @Override
    public int getItemCount() {
        if (type.equals("activity"))
            return activityList.size();
        else
        return achievementList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ItemHolder holder) {
        holderHasMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ItemHolder holder) {
        holderHasMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);

    }
}

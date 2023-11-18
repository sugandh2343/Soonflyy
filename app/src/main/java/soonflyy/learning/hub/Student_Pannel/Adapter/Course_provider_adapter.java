package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class Course_provider_adapter extends RecyclerView.Adapter<Course_provider_adapter.ProviderHolder> {

    Context context;
    ArrayList<String>providerList;
    ArrayList<Integer>positionList;


    public Course_provider_adapter(Context context, ArrayList<String> providerList, ArrayList<Integer> positionList) {
        this.context = context;
        this.providerList = providerList;
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public ProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProviderHolder(LayoutInflater.from(context).inflate(R.layout.row_course_provider,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderHolder holder, int position) {
        holder.tv_provder.setText(providerList.get(positionList.get(position)));

    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public class ProviderHolder extends RecyclerView.ViewHolder {
        TextView tv_provder;
        public ProviderHolder(@NonNull View itemView) {
            super(itemView);
            tv_provder=itemView.findViewById(R.id.tv_provider);
        }
    }
}

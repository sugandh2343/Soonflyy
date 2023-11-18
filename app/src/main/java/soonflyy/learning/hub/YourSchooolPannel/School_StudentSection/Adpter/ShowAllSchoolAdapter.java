package soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.School_StudentSection.Model.ShowAllSchoolModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowAllSchoolAdapter extends RecyclerView.Adapter<ShowAllSchoolAdapter.ViewHolder> {
    Context context;
    ArrayList<ShowAllSchoolModel> list;
    OnSchoolClickListener listener;


    public interface OnSchoolClickListener {
        void onItemClick(int postion);

        void onDelete(int position);

        void onEdit(int position);


    }


    public ShowAllSchoolAdapter(Context context, ArrayList<ShowAllSchoolModel> list, OnSchoolClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_home,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ShowAllSchoolModel model = list.get(position);
        Picasso.get().load(BaseUrl.SCHOOL_BASE_URL_MEDIA+model.getImage())
                .placeholder(R.drawable.logoo)
                .into(holder.img_school);
        Log.e("sc_url",""+BaseUrl.SCHOOL_BASE_URL+model.getImage());
        holder.tv_school_title.setText(model.getName());

        holder.rel_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_school;
        RelativeLayout rel_open;
        TextView tv_school_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_school= itemView.findViewById(R.id.img_school);
            rel_open= itemView.findViewById(R.id.rel_open);
            tv_school_title= itemView.findViewById(R.id.tv_tuotr_name);
        }
    }
}

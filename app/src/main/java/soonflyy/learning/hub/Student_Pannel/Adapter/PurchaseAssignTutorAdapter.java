package soonflyy.learning.hub.Student_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.AssignProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PurchaseAssignTutorAdapter extends RecyclerView.Adapter<PurchaseAssignTutorAdapter.ViewHolder> {
    Context context;
    //ArrayList<PurchaseCourseTutorModel> list;
    ArrayList<AssignProfile> list;
    OnTestClickListener listener;

    public interface OnTestClickListener{
        void onItemClick(int position,AssignProfile model);
    }

    public PurchaseAssignTutorAdapter(Context context, ArrayList<AssignProfile> list, OnTestClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.row_purchasecourse_tutor,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        AssignProfile model=list.get(adapterPosition);
        String img= BaseUrl.BASE_URL_MEDIA+model.getImage();
        Picasso.get().load(img).placeholder(R.drawable.logoo)
                .into(holder.ivImage);
        holder.tvName.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition,model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivImage;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.iv_image);
            tvName=itemView.findViewById(R.id.tv_tutor_name);
        }
    }
}

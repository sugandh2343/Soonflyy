package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common_Package.Models.IDAchievementModel;
import soonflyy.learning.hub.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class IDAchievemenAdapter extends RecyclerView.Adapter<IDAchievemenAdapter.ViewHolder> {
    public HashMap<Integer,RecyclerView.ViewHolder> holderHasMap=new HashMap<>();
    Context context;
    ArrayList<IDAchievementModel> list;
   OnAchievementListener listener;

    public IDAchievemenAdapter(Context context, ArrayList<IDAchievementModel> list, OnAchievementListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public  interface OnAchievementListener{
        void OnRemove(int position, IDAchievementModel model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_idactivity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        IDAchievementModel model=list.get(adapterPosition);
        // if (hasData>0){

        holder.tv_title.setText("Achievement-"+(adapterPosition+1));

//        if (TextUtils.isEmpty(model.getAchievement_name()))
//            holder.et_name.setHint("Enter name");
//        else
            holder.et_name.setText(model.getAchievement_name());


        //}
        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnRemove(adapterPosition,model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText tv_title;
        EditText et_name;
        CircleImageView iv_cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            et_name=itemView.findViewById(R.id.et_name);
            iv_cancel=itemView.findViewById(R.id.cancel_image);
            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(getAdapterPosition()).setAchievement_name(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
}

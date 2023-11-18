package soonflyy.learning.hub.Common_Package.Common_Package.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import soonflyy.learning.hub.Common_Package.Models.IDOtherModel;
import soonflyy.learning.hub.R;

public class IDOtherAdapter extends RecyclerView.Adapter<IDOtherAdapter.ViewHolder> {
    public HashMap<Integer,RecyclerView.ViewHolder> holderHasMap=new HashMap<>();
    Context context;
    ArrayList<IDOtherModel> list;
    OnOtherListener listener;

    public IDOtherAdapter(Context context, ArrayList<IDOtherModel> list, OnOtherListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public  interface OnOtherListener{
        void onRemove( int position,IDOtherModel model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_idactivity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        IDOtherModel model=list.get(adapterPosition);
        // if (hasData>0){


        if (TextUtils.isEmpty(model.getTitle())) {
            holder.tv_title.setText("");
            holder.tv_title.setHint("Social media title");
        }
        else
            holder.tv_title.setText(model.getTitle());

        if (TextUtils.isEmpty(model.getLink())) {
            holder.et_name.getText().clear();
            holder.et_name.setHint("Enter link here");
        }
        else
            holder.et_name.setText(model.getLink());


        //}
        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemove(adapterPosition,model);
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
            tv_title.setEnabled(true);
            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(getAdapterPosition()).setLink(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tv_title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(getAdapterPosition()).setTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        holderHasMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holderHasMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);

    }
}

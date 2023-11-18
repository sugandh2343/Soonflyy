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
import soonflyy.learning.hub.Common_Package.Models.IDActivityModel;
import soonflyy.learning.hub.R;

public class IDActivityAdapter extends RecyclerView.Adapter<IDActivityAdapter.ViewHolder> {
    public HashMap<Integer,RecyclerView.ViewHolder> holderHasMap=new HashMap<>();
    Context context;
    ArrayList<IDActivityModel>list;
    OnActivityClickListener listener;
    int hasData;

    public IDActivityAdapter(Context context,int hasData, ArrayList<IDActivityModel> list,
                             OnActivityClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.hasData=hasData;
    }

    public  interface OnActivityClickListener{
     void OnActivityRemove( int position,IDActivityModel model);
     void onActivityChange(int position ,String name);
 }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_idactivity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        IDActivityModel model=list.get(adapterPosition);
       // if (hasData>0){

                holder.tv_title.setText("Activity-"+(adapterPosition+1));

            if (TextUtils.isEmpty(model.getActivity_name())) {
                holder.et_name.setHint("Enter name");
                holder.et_name.getText().clear();
            }
            else
            holder.et_name.setText(model.getActivity_name());


        //}
        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnActivityRemove(adapterPosition,model);
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
                    String value=s.toString().trim();
                    if (value.length()==0 || TextUtils.isEmpty(value)){

                        list.get(getAdapterPosition()).setActivity_name("");

                    }else{
                         list.get(getAdapterPosition()).setActivity_name(value);

                    }

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

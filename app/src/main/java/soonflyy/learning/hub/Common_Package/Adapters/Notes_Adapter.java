package soonflyy.learning.hub.Common_Package.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.Common_Package.Models.Notes_Model;
import soonflyy.learning.hub.R;

import java.util.ArrayList;

public class Notes_Adapter extends RecyclerView.Adapter<Notes_Adapter.NoteHolder> {
    Context context;
    ArrayList<Notes_Model>noteList;
    OnNoteClickListener listener;

    public Notes_Adapter(Context context, ArrayList<Notes_Model> noteList,OnNoteClickListener listener) {
        this.context = context;
        this.noteList = noteList;
        this.listener=listener;
    }

    public interface OnNoteClickListener{
        void onClick(int position,String url);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(context).inflate(R.layout.row_notes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        int adapterposition=position;
        Notes_Model model=noteList.get(position);
        if(TextUtils.isEmpty(model.getFile_name())) {
            if (adapterposition+1<10){
                holder.tv_title.setText("Note_0"+(adapterposition+1));
            }else{
                holder.tv_title.setText("Note_"+(adapterposition+1));
            }
        }else {
            holder.tv_title.setText(model.getFile_name());
        }

        //holder.tv_title.setText(model.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getFile()!=null){
                    listener.onClick(adapterposition, BaseUrl.BASE_URL_MEDIA+model.getFile());
                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_image;
        RelativeLayout rel_edit;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tv_title= itemView.findViewById(R.id.tv_title);
            iv_image=itemView.findViewById(R.id.thumbnail_image);
            rel_edit=itemView.findViewById(R.id.rel_edit);
            rel_edit.setVisibility(View.GONE);
        }
    }
}

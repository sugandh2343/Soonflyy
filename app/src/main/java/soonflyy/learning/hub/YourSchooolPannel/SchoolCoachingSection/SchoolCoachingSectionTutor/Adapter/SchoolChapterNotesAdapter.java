package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolChapterNotesModel;

import java.util.ArrayList;

public class SchoolChapterNotesAdapter extends RecyclerView.Adapter<SchoolChapterNotesAdapter.NoteHolder> {
    Context context;
    ArrayList<SchoolChapterNotesModel> notList;
    OnNoteClickListener listener;
    int count = -1;
    public SchoolChapterNotesAdapter(Context context, ArrayList<SchoolChapterNotesModel> notList, OnNoteClickListener listener) {
        this.context = context;
        this.notList = notList;
        this.listener = listener;
    }

    public  interface OnNoteClickListener{
        void onItemClick(int position);
        void onDelete(int position);
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(context).inflate(R.layout.row_notes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        int adapterPosition=position;
        SchoolChapterNotesModel model=notList.get(adapterPosition);
        if(TextUtils.isEmpty(model.getFile_name())) {
            holder.tv_title.setText("Note_" + adapterPosition + 1);
        }else {
            holder.tv_title.setText(model.getFile_name());
        }

        holder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cv_edi.setVisibility(View.GONE);
            }
        });
        if (count==adapterPosition){

        }else {
            holder.cv_edi.setVisibility(View.GONE);
        }


        holder.img_edit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                count=adapterPosition;
                if(holder.cv_edi.getVisibility ()==View.VISIBLE)
                {
                    holder.cv_edi.setVisibility (View.GONE);
                }
                else
                {
                    holder.cv_edi.setVisibility (View.VISIBLE);

                }
                notifyDataSetChanged();
            }
        });
        holder.dailog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(adapterPosition);
            }
        });
        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition);
            }
        });
    }
    @Override
    public int getItemCount() {
        return notList.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_image;
        ImageView img_edit;
        TextView tv_manage,dailog_delete;
        CardView cv_edi;
        CardView rel_main;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tv_title= itemView.findViewById(R.id.tv_title);
            iv_image=itemView.findViewById(R.id.thumbnail_image);
            tv_manage= itemView.findViewById(R.id.tv_manage);
            img_edit= itemView.findViewById(R.id.img_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            rel_main = itemView.findViewById(R.id.rel_main);
            cv_edi = itemView.findViewById(R.id.cv_edi);
        }
    }
}

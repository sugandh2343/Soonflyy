package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AssignedSubjects;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignedSubjectAdapter extends RecyclerView.Adapter<AssignedSubjectAdapter.Viewholder> {
    Context context;
    ArrayList<AssignedSubjects> modellist;
    OnCourseClickListener listener;
    int count = -1;
    PopupWindow settingWindow;

    public interface  OnCourseClickListener {
        void onItemClick(int position,AssignedSubjects model);
        void onUnassignSubject(int position,AssignedSubjects model);

    }
    public AssignedSubjectAdapter(Context context, ArrayList<AssignedSubjects> modellist, OnCourseClickListener listener) {
        this.context=context;
        this.modellist=modellist;
        this.listener=listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_myteacher_profile_subjectlist,null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition =position;
        AssignedSubjects model=modellist.get(adapterPosition);
        setSubjectData(holder,model);

        if (adapterPosition==modellist.size()-1)
            holder.bottom_line.setVisibility(View.GONE);
        else
            holder.bottom_line.setVisibility(View.VISIBLE);


//        switch (position%2)
//        {
//            case 0: holder.lin_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
//                break;
//            case 1: holder.lin_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
//                break;
//            default: holder.lin_main.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white_smoke)));
//                break;        }
        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingWindow!=null){
                    if (settingWindow.isShowing()){
                        settingWindow.dismiss();
                        count=-1;
                    }else {
                        listener.onItemClick(adapterPosition, model);
                    }
                }else {
                    listener.onItemClick(adapterPosition, model);
                }

            }
        });

//
//        holder.rel_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.cv_edi.setVisibility(View.GONE);
//            }
//        });
//        if (count == adapterPositino) {
//
//        } else {
//            holder.cv_edi.setVisibility(View.GONE);
//        }

//        holder.img_edit.setOnClickListener( new View.OnClickListener() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingWindow !=null){
                    if (count!=adapterPosition){
                        if (settingWindow.isShowing()) {
                            settingWindow.dismiss();
                            showPopupMenu(holder.img_edit, adapterPosition);

                        }else{
                            showPopupMenu(holder.img_edit,adapterPosition);
                        }
                    }else if (count==adapterPosition){
                        if (settingWindow.isShowing())
                            settingWindow.dismiss();
                        count=-1;
                    }
                }else{
                    showPopupMenu(holder.img_edit, adapterPosition);

                }

            }
        });


    }

    private void setSubjectData(Viewholder holder, AssignedSubjects model) {
        holder.tvSubjectName.setText(model.getSubject_name());
        String link= BaseUrl.BASE_URL_MEDIA+model.getImage();
        Log.e("imgLink",""+link);
        Picasso.get().load(link).placeholder(R.drawable.logoo)
                .into(holder.ivImage);
    }
    private void showPopupMenu(ImageView img_edit, int adapterPosition) {
        count=adapterPosition;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.dialog_assign_unassign_popup, null);
        LinearLayout linMain = customView.findViewById(R.id.lin_main);

        settingWindow = new PopupWindow(customView, 300, 80);//ViewGroup.LayoutParams.WRAP_CONTENT
        //settingWindow.showAtLocation(holder.img_edit,Gravity.BOTTOM,0,0);
        settingWindow.showAsDropDown(img_edit, -20, -10, Gravity.END);
        settingWindow.setOutsideTouchable(false);
        linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnassignDailoge(adapterPosition);
                settingWindow.dismiss();
            }
        });
    }


    private void showUnassignDailoge(int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity( Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailoge_unassign);
        TextView tvCancel=dialog.findViewById(R.id.tv_cancel);
        Button btn_unassign = dialog.findViewById(R.id.btn_unassign);


        btn_unassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                notifyItemRemoved(po);
                listener.onUnassignSubject(position,modellist.get(position));

                dialog.dismiss();

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public  PopupWindow getPopUpWindow(){
        return settingWindow;
    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView ivImage;
        LinearLayout lin_main;
        ImageView iv_arrowback;
        RelativeLayout rel_edit;
        ImageView img_edit;
        TextView dailog_delete,tvSubjectName;

        CardView cv_edi;
        View bottom_line;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ivImage=itemView.findViewById(R.id.iv_image);
            tvSubjectName=itemView.findViewById(R.id.tv_title);
            lin_main=itemView.findViewById(R.id.lin_main);
            iv_arrowback=itemView.findViewById(R.id.iv_arrowback);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            img_edit = itemView.findViewById(R.id.img_edit);
            rel_edit = itemView.findViewById(R.id.rel_edit);
            // dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            bottom_line = itemView.findViewById(R.id.bottom_line);


        }
    }
}

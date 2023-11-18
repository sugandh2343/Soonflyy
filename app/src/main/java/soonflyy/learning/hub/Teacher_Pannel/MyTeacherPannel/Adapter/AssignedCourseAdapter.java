package soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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
import soonflyy.learning.hub.Teacher_Pannel.MyTeacherPannel.Model.AssignedCourses;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignedCourseAdapter extends RecyclerView.Adapter<AssignedCourseAdapter.Viewholder> {
    Context context;
    ArrayList<AssignedCourses> modellist;
    OnCourseClickListener listener;
    int count = -1;
    PopupWindow  settingWindow;

    public interface  OnCourseClickListener {
        void onItemClick(int position);
        void onUnassignCourse(int position,AssignedCourses model);

    }
    public AssignedCourseAdapter(Context context, ArrayList<AssignedCourses> modellist, OnCourseClickListener listener) {
        this.context=context;
        this.modellist=modellist;
        this.listener=listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_myteacher_profile_coursedetails,null);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        int adapterPosition =position;
        AssignedCourses model=modellist.get(adapterPosition);
        setCourseData(holder,model);
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
                    }else{
                        listener.onItemClick(adapterPosition);
                    }
                }else {
                    listener.onItemClick(adapterPosition);
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
//                Context vv = new ContextThemeWrapper(context, R.style.PopupMenu);
//
//                PopupMenu popupMenu = new PopupMenu(vv,holder.img_edit);
//                popupMenu.getMenuInflater().inflate(R.menu.popup_delete_only,popupMenu.getMenu());
//
//                //----------------//
//               MenuItem mItem= popupMenu.getMenu().findItem(R.id.popup_remove);
//               String title=mItem.getTitle().toString();
//                SpannableString s=new SpannableString(title);
//                s.setSpan(new ForegroundColorSpan(Color.parseColor("#F11414")),0,s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                mItem.setTitle(s);
//               //-------------//
//               // insertMenuItemIcons(context, popupMenu);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @SuppressLint("NonConstantResourceId")
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()){
//                            case R.id.popup_remove:
//                                showUnassignDailoge();
//
//
//
//
//                                break;
//                        }
//                        return true;
//                    }
//                });
//
//                popupMenu.show();
////                MenuPopupHelper helper = new MenuPopupHelper(context, (MenuBuilder) popupMenu.getMenu());
////                helper.setForceShowIcon(true);
////                helper.setGravity( Gravity.END);
////                helper.show();
//
//            }
//            private  boolean hasIcon(Menu menu) {
//                for (int i = 0; i < menu.size(); i++) {
//                    if (menu.getItem(i).getIcon() != null)
//                        return true;
//                }
//                return false;
//            }
//            private void insertMenuItemIcons(Context context, PopupMenu popupMenu) {
//                Menu menu = popupMenu.getMenu();
//                if (hasIcon(menu)) {
//                    for (int i = 0; i < menu.size(); i++) {
//                        insertMenuItemIcon(context, menu.getItem(i));
//                    }
//                }
//            }
//
//            private void insertMenuItemIcon(Context context, MenuItem item) {
//                Drawable icon = item.getIcon();
//
//                // If there's no icon, we insert a transparent one to keep the title aligned with the items
//                // which do have icons.
//                if (icon == null) icon = new ColorDrawable( Color.TRANSPARENT);
//
//                int iconSize = context.getResources().getDimensionPixelSize(R.dimen.tv_bold_12pt);
//                icon.setBounds(0, 0, iconSize, iconSize);
//                ImageSpan imageSpan = new ImageSpan(icon);
//
//                // Add a space placeholder for the icon, before the title.
//                SpannableStringBuilder ssb = new SpannableStringBuilder("       " + item.getTitle());
//
//                // Replace the space placeholder with the icon.
//                ssb.setSpan(imageSpan, 1, 2, 0);
//                item.setTitle(ssb);
//                // Set the icon to null just in case, on some weird devices, they've customized Android to display
//                // the icon in the menu... we don't want two icons to appear.
//                item.setIcon(null);
//
//            }
//
//        });
//


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

    private void setCourseData(Viewholder holder, AssignedCourses model) {
        String imgLInk= BaseUrl.BASE_URL_MEDIA+model.getImage();
        Log.e("courseImg",""+imgLInk);
        Picasso.get().load(imgLInk).placeholder(R.drawable.logoo)
                .into(holder.ivCourseImg);
        holder.tvCourseName.setText(model.getCourse_name());
        String price=model.getPrice();
        if (TextUtils.isEmpty(price)){
            holder.tvPrice.setText("--");
        }else if (Float.parseFloat(price)==0f){
            holder.tvPrice.setText("Free");
        }else{
            holder.tvPrice.setText(context.getResources().getString(R.string.Rs_symbol)+price);
        }
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
                listener.onUnassignCourse(position,modellist.get(position));

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

        CircleImageView ivCourseImg;
        TextView tvCourseName,tvPrice;
        LinearLayout lin_main;
        ImageView iv_arrowback;
        RelativeLayout rel_edit;
        ImageView img_edit;
        TextView dailog_delete;

        CardView cv_edi;
        View bottom_line;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            lin_main=itemView.findViewById(R.id.lin_main);
            iv_arrowback=itemView.findViewById(R.id.iv_arrowback);
            cv_edi = itemView.findViewById(R.id.cv_edi);
            img_edit = itemView.findViewById(R.id.img_edit);
            rel_edit = itemView.findViewById(R.id.rel_edit);
            // dailog_edit=itemView.findViewById(R.id.dailog_edit);
            dailog_delete = itemView.findViewById(R.id.dailog_delete);
            bottom_line = itemView.findViewById(R.id.bottom_line);

            ivCourseImg=itemView.findViewById(R.id.iv_course_img);
            tvCourseName=itemView.findViewById(R.id.tv_course_name);
            tvPrice=itemView.findViewById(R.id.tv_price);


        }
    }
}

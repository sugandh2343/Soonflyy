package soonflyy.learning.hub.Student_Pannel.Adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Notice;
import soonflyy.learning.hub.Teacher_Pannel.Adapter.MyNoticeNoteImageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Notice_Adapter extends RecyclerView.Adapter<Notice_Adapter.NoticeHolder> {
    Context context;
    ArrayList<Notice>noticeList;
    OnNoticeMsgClickListener listener;

//    ArrayList<MyNoticeNoteImageModel> imageModels= new ArrayList<>();
//    MyNoticeNoteImageAdapter imageAdapter;


    public Notice_Adapter(Context context, ArrayList<Notice> noticeList, OnNoticeMsgClickListener listener) {
        this.context = context;
        this.noticeList = noticeList;
        this.listener = listener;
    }

    public interface  OnNoticeMsgClickListener{
        public void onClick(int position);
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeHolder(LayoutInflater.from(context).inflate(R.layout.row_notice,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {


        int adapterps=position;
        Notice model=noticeList.get(position);
        int sno=position+1;
        if (sno<10){
            holder.tv_sno.setText("0"+sno);
        }else{
            holder.tv_sno.setText(String.valueOf(sno));
        }
        String msg=model.getMsg();
        Log.e("msg",""+msg);
        if (msg.length()>30){
           String m= msg.substring(0,28)+"...";
            holder.tv_notice.setText(m);
            Log.e("msg",""+msg);
        }else{
            holder.tv_notice.setText(msg+"...");
            Log.e("msg",""+msg);
        }
        iniImageRecycler(holder,model) ;

        holder.tv_date_time.setText(model.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(adapterps);
            }
        });



    }

    private void iniImageRecycler(NoticeHolder holder, Notice model) {
        ArrayList<String>imageList=new ArrayList<>();
        if (!TextUtils.isEmpty(model.getImage())){
            imageList.add(model.getImage());
        }
        if (imageList.size()>0){
            MyNoticeNoteImageAdapter imageAdapter = new MyNoticeNoteImageAdapter(context, imageList,
                    new MyNoticeNoteImageAdapter.OnNoticeImageClickListener() {
                @Override
                public void onClick(int position,String link) {
                    dialogFullscreen(link);
                }

            } );
            holder.rec_note_image.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,false));
            holder.rec_note_image.setAdapter( imageAdapter );
            Log.e("imageItem",""+imageAdapter.getItemCount());
            holder.rec_note_image.setVisibility(View.VISIBLE);
        }else{
            holder.rec_note_image.setVisibility(View.GONE);
        }

    }

    private void dialogFullscreen(String link) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_view_full_screen);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show ( );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.setCancelable(false);
        dialog.show();
        PhotoView photoView =dialog.findViewById(R.id.photo_view);
        ImageView iv_cancel=dialog.findViewById(R.id.iv_close_dialog);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Picasso.get().load(link).into(photoView);

    }

   /* private void iniImageRecycler(RecyclerView rec_note_image) {
        ArrayList<MyNoticeNoteImageModel> imageModels= new ArrayList<>();
        imageModels.add( new MyNoticeNoteImageModel() );
        imageModels.add( new MyNoticeNoteImageModel() );


        if (imageModels.size() > 0) {
           MyNoticeNoteImageAdapter imageAdapter = new MyNoticeNoteImageAdapter(context, imageModels, new MyNoticeNoteImageAdapter.OnNoticeImageClickListener() {
                @Override
                public void onClick(int position,String link) {

                }

            } );
            rec_note_image.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rec_note_image.setAdapter( imageAdapter );
            Log.e("imageItem",""+imageAdapter.getItemCount());

            // lin_shimmer.setVisibility(View.GONE);
            //  lin_rec.setVisibility(View.VISIBLE);
        } else {

        }


    }

    */

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class NoticeHolder extends RecyclerView.ViewHolder {
        TextView tv_sno,tv_notice,tv_date_time;
        RecyclerView rec_note_image;
        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno=itemView.findViewById(R.id.tv_notice_sno);
            tv_notice=itemView.findViewById(R.id.tv_note);
            tv_date_time=itemView.findViewById(R.id.tv_date_time);
            rec_note_image=itemView.findViewById(R.id.rec_note_image);
            rec_note_image.setLayoutManager( new GridLayoutManager( context,3 ) );



        }
    }
}

package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyNoticeNoteImageAdapter extends RecyclerView.Adapter<MyNoticeNoteImageAdapter.ViewHolder> {
    Context context;
  //  ArrayList<MyNoticeNoteImageModel> list;
    ArrayList<String>list;
    OnNoticeImageClickListener listener;

    public interface  OnNoticeImageClickListener{
         void onClick(int position,String imgLink);
    }

    public MyNoticeNoteImageAdapter(Context context, ArrayList<String> list, OnNoticeImageClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_notice_imagee,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      String imgLink= BaseUrl.BASE_URL_MEDIA+list.get(position);
      Picasso.get().load(imgLink).placeholder(R.drawable.logoo).into(holder.rivNoticeImg);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              listener.onClick(position,imgLink);
          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivNoticeImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rivNoticeImg=itemView.findViewById(R.id.riv_notice_img);
        }
    }
}

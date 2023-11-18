package soonflyy.learning.hub.Teacher_Pannel.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.Model.SubjectModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateSubjectAdapter extends RecyclerView.Adapter<CreateSubjectAdapter.SubjectHolder> {
    public HashMap<Integer,RecyclerView.ViewHolder> holderHasMap=new HashMap<>();
    Context context;
    ArrayList<SubjectModel>subjectList;
    OnImageChoose listener;

    public CreateSubjectAdapter(Context context, ArrayList<SubjectModel> subjectList, OnImageChoose listener) {
        this.context = context;
        this.subjectList = subjectList;
        this.listener = listener;
    }

    public interface OnImageChoose{
        void onChooseImage(int position);
        void onCancelImage(int position);
         void onRemoveSubject(int position);
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectHolder(LayoutInflater.from(context).inflate(R.layout.row_create_subject,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        int ps=position;
        SubjectModel model=subjectList.get(ps);
        holder.et_name.setText(model.getTitle());
        try {
           if (model.getImgUri()!=null) {
               Picasso.get().load(model.getImgUri()).into(holder.cover_image);
               holder.choose_image.setVisibility(View.GONE);
               holder.rel_image.setVisibility(View.VISIBLE);
               holder.tv_imgName.setText("sub_img.jpeg");
           }else{
               holder.choose_image.setVisibility(View.VISIBLE);
               holder.rel_image.setVisibility(View.GONE);
               holder.tv_imgName.setText("Upload your file here,Choose file form your device");
           }
//            String bas64Img=subjectList.get(ps).getSection_thumbnail();
//            if (!TextUtils.isEmpty(bas64Img)){
//                Log.e("image",""+bas64Img);
//                byte[] image =Base64.getDecoder().decode(bas64Img);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
//                Glide.with(context).load(decodedByte).into(holder.cover_image);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }

      holder.choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.onChooseImage(ps);
            }
        });
      holder.cancel_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              listener.onCancelImage(ps);
              Log.e("cancel","click");
          }
      });
      holder.lin_remove.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //call for remove
              listener.onRemoveSubject(ps);
          }
      });
    }


    @Override
    public int getItemCount() {
        return subjectList.size() ;
    }

    public class SubjectHolder extends RecyclerView.ViewHolder {
        EditText et_name;
        ImageView choose_image,cover_image;
        RelativeLayout rel_image;
        CircleImageView cancel_image;
        TextView tv_imgName;
        LinearLayout lin_remove;
        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            et_name=itemView.findViewById(R.id.et_name);
            choose_image=itemView.findViewById(R.id.iv_sub);
            rel_image=itemView.findViewById(R.id.rel_cover_image);
            cancel_image=itemView.findViewById(R.id.cancel_image);
            tv_imgName=itemView.findViewById(R.id.tv_name_image);
            cover_image=itemView.findViewById(R.id.image_view);
            lin_remove=itemView.findViewById(R.id.lin_remove);
            lin_remove.setVisibility(View.VISIBLE);

            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subjectList.get(getAdapterPosition()).setTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }
    @Override
    public void onViewAttachedToWindow(@NonNull SubjectHolder holder) {
        holderHasMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SubjectHolder holder) {
        holderHasMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);

    }

}

package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Student_Pannel.Model.Course_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecommendCourseAdapter extends RecyclerView.Adapter<RecommendCourseAdapter.RecommendeCourseViewHolder> {
    Context context;
    List<Course_model> list,listFilter;
    OnReCommendCourseClickListener listener;

    public RecommendCourseAdapter(Context context, List<Course_model> list, OnReCommendCourseClickListener listener) {
        this.context = context;
        this.list = list;
        this.listFilter=list;
        this.listener = listener;
    }

    public interface OnReCommendCourseClickListener{
        void onItemClick(int position, Course_model courses);
    }
    @Override
    public RecommendeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_recommended_courses,parent,false);
       // ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //layoutParams.width = (int) (parent.getWidth() * 0.8);
       // view.setLayoutParams(layoutParams);
        return new RecommendeCourseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecommendeCourseViewHolder holder, int position) {
        int adapterPosition=position;
        Course_model model=list.get(adapterPosition);
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .priority(Priority.HIGH);
//        new GlideImageLoader(holder.courseImage,
//                holder.progressBar).load(BaseUrl.BASE_URL_MEDIA+model.getThumbnail(),options);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getThumbnail())
                .placeholder(R.drawable.logoo)
                .into(holder.courseImage);
        holder.txtTitle.setText(model.getTitle());
        holder.tv_name.setText(model.getName());

        if (model.getIs_purchased()!=null && model.getIs_purchased().equals("1")){
            holder.imgSubcribed.setVisibility(View.VISIBLE);
        }else {
            holder.imgSubcribed.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(adapterPosition,model);
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecommendeCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView txtTitle,tv_name;
        ProgressBar progressBar;
        ImageView imgSubcribed;
        public RecommendeCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.imgCourse);
            txtTitle = itemView.findViewById(R.id.tv_category);//txtTitle
            tv_name=itemView.findViewById(R.id.tv_discriptin);
            progressBar = itemView.findViewById(R.id.progressBar);
            imgSubcribed=itemView.findViewById(R.id.img_subcribed);
        }
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = listFilter;
                } else {
                    ArrayList<Course_model> filteredList = new ArrayList<>();
                    for (Course_model row : listFilter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getTitle ().toLowerCase().contains(charString.toLowerCase())
                        ||row.getName ().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
//                        else
//                        {
//                            Toast.makeText (context, "No Information Found", Toast.LENGTH_SHORT).show ( );
//                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<Course_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

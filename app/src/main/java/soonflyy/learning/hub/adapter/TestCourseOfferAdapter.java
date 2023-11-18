package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.MyCourseDetailModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TestCourseOfferAdapter extends RecyclerView.Adapter<TestCourseOfferAdapter.myholder>
        implements Filterable {
    Context context;
    ArrayList<MyCourseDetailModel> modellist;
    ArrayList<MyCourseDetailModel> filterModelList;
    int lastCheckedPosition=-1;
    String id="", name="",offer_price="";
//   OnSelectionCourseOfferListener listener;

    public TestCourseOfferAdapter(Context context, ArrayList<MyCourseDetailModel> modellist
                                ) {//  ,OnSelectionCourseOfferListener listener
        this.context = context;
        this.modellist = modellist;
        this.filterModelList=modellist;
        //this.listener = listener;
    }

//    public  interface  OnSelectionCourseOfferListener{
//        void onSelection(int position,String name);
//    }
    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_dailogeoffer,null);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        int ps=position;
        MyCourseDetailModel model=filterModelList.get(position);
        Picasso.get().load(BaseUrl.BASE_URL_MEDIA+model.getCourse_thumbnail())
                .placeholder(R.drawable.logoo).into(holder.iv_thumbnail);
        holder.radioChoice.setText(model.getTitle());
        if (model.isSelected()){
            holder.radioChoice.setChecked(true);
            lastCheckedPosition=ps;
            id=model.getId();
            name=model.getTitle();
            offer_price=model.getAmount();
        }else{
            holder.radioChoice.setChecked(false);
        }

     holder.radioChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    model.setSelected(true);
                if (holder.radioChoice.isChecked()){
                    holder.radioChoice.setChecked(false);
                    model.setSelected(true);


                }else{
                    holder.radioChoice.setChecked(true);
                    model.setSelected(false);
                }
              holder.radioChoice.setChecked(true);
                if(lastCheckedPosition!=-1) {
                    modellist.get(lastCheckedPosition).setSelected(false);
                }
                try {
                    if (lastCheckedPosition!=-1) {
                        notifyItemChanged(lastCheckedPosition);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                lastCheckedPosition=ps;
                if (model.isSelected()) {
                id=model.getId();
                name=model.getTitle();
                offer_price=model.getAmount();
                Log.e("offer","running true");
                }else{
                    id="";
                    name="";
                    offer_price="";
                    Log.e("offer","runningfalse");
                }
            }
        });


     //  holder.radioChoice.setChecked(position==lastCheckedPosition);


//        holder.radioChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if(lastCheckedPosition!=-1) {
//                        modellist.get(lastCheckedPosition).setSelected(false);
//                    }
//                 model.setSelected(isChecked);
//
//                    if (isChecked) {
//                        id = model.getId();
//                        name = model.getTitle();
//                        offer_price = model.getAmount();
//                        Log.e("offer", "running");
//                    }else{
//                        id="";
//                        name="";
//                        offer_price="";
//                    }
////                try {
////                    if (lastCheckedPosition!=-1) {
////                        notifyItemChanged(lastCheckedPosition);
////                    }
////
////                }catch (Exception e){
////                    e.printStackTrace();
////                }
//                lastCheckedPosition=ps;
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if (charString.isEmpty()){
                    filterModelList=modellist;
                }else{
                    ArrayList<MyCourseDetailModel> dataFilteredList=new ArrayList<>();
                    for (MyCourseDetailModel row: modellist){
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())){
                            dataFilteredList.add(row);
                        }
                    }
                    filterModelList=dataFilteredList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filterModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterModelList=(ArrayList<MyCourseDetailModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class myholder extends RecyclerView.ViewHolder {
        CardView cardView;
        RadioButton radioChoice;
        RoundedImageView iv_thumbnail;

        public myholder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_main);
            radioChoice=itemView.findViewById(R.id.radio_title_btn);
            iv_thumbnail=itemView.findViewById(R.id.img);

        }
    }
    public ArrayList<String> getSelectedCourse(){
        ArrayList<String>arrayList=new ArrayList<>();
        Log.e("offer",""+id+name);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)){
            arrayList.add(id);
            arrayList.add(name);
        }
        Log.e("offerSize",""+arrayList.size());
        return arrayList;
    }
    public String getOfferPrice(){
        return offer_price;
    }

}

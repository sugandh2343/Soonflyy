package soonflyy.learning.hub.adapter;

import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.Common.BaseUrl;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.model.StudentAttendenceModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAttendenceAdapter  extends RecyclerView.Adapter<StudentAttendenceAdapter.ViewHolder> {
    Context context;
    ArrayList<StudentAttendenceModel> modellist;
    OnCourseClickListener listener;

    ArrayList<String> spiner_attendenceList;
    String from,type="",assignValue="";


    public interface  OnCourseClickListener {
        void onItemClick(int postion);

        void onStudentAttendenceClick(int position,int spostion);


    }

    public StudentAttendenceAdapter(Context context,String[] typeArray,
                                    ArrayList<StudentAttendenceModel> modellist, OnCourseClickListener listener) {
        this.context = context;
        this.modellist = modellist;
        this.listener = listener;
        //this.from=from;
        from=typeArray[0];
        type=typeArray[1];
        assignValue=typeArray[2];
        spiner_attendenceList = new ArrayList<>();
        spiner_attendenceList.add("");
        spiner_attendenceList.add("Present");
        spiner_attendenceList.add("Absent");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_studentattendence,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int adapterPosition=position;
        StudentAttendenceModel model=modellist.get(adapterPosition);
        if (from.equals(SIMPLEE_HOME_TUTOR)) {
            holder.tv_profile_txt.setVisibility(View.GONE);
            Picasso.get().load(BaseUrl.BASE_URL_MEDIA + model.getImage())
                    .into(holder.img_student);
            if (type.equals("course")){
                if (assignValue.equals("1")) {
                    holder.tvAttendanceInfo.setVisibility(View.VISIBLE);
                    String info = "Total Student: " + model.getTotalStudent()
                            + " | Present: " + model.getPresent()
                            + " | Absent: " + model.getAbsent();
                    holder.tvAttendanceInfo.setText(info);
                }

            }else if (type.equals("subject")){
                holder.tvAttendanceInfo.setVisibility(View.GONE);
            }
        }
        else if (from.equals(INDEPENDENT_TUTOR) || from.equals(SCHOOL_COACHING)||from.equals(SCHOOL_TUTOR)){
            holder.img_student.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(model.getFirst_name())) {
                holder.tv_profile_txt.setText(model.getFirst_name().substring(0, 1));
            }else{
                holder.tv_profile_txt.setText("-");
            }
            if (adapterPosition+1<=4){
                switch (adapterPosition+1){
                    case 1:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                        break;
                    case 2:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                        break;
                    case 3:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                        break;
                    case 4:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                        break;
                }
            }else{
                switch ((adapterPosition+1)%4){
                    case 0:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                        break;
                    case 1:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                        break;
                    case 2:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                        break;
                    case 3:
                        holder.tv_profile_txt.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                        break;
                }
            }
            holder.tv_profile_txt.setVisibility(View.VISIBLE);

        }
        if (!TextUtils.isEmpty(model.getFirst_name())) {
            holder.tittle_tv.setText(model.getFirst_name());
        }else{
            holder.tittle_tv.setText("Unknown");
        }

     /*   if (model.isAttendanceStatus()){
//            holder.spiner_attendence.setSelection(1);
            Log.e("present",""+model.isAttendanceStatus());
            holder.tv_select.setText("Present");
        }else{
            Log.e("absent",""+model.isAttendanceStatus());
           // holder.spiner_attendence.setSelection(2);
            holder.tv_select.setText("Absent");
        }

      */
        String attendanceValue= model.getAttenadnce_value();
        if (TextUtils.isEmpty(attendanceValue)){
            holder.tv_select.setHint("Select");
            holder.tv_select.setText("");
        }else if (attendanceValue.equals("A")) {
            holder.tv_select.setText("Absent");
        }else if (attendanceValue.equals("P")){
            holder.tv_select.setText("Present");
        }

//        holder.tittle_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.tv_option.performClick();
//            }
//        });
//      holder.tv_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//          @Override
//          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//              String selectedItem=parent.getItemAtPosition(position).toString();
//              Toast.makeText(context,
//                      ""+selectedItem, Toast.LENGTH_SHORT).show();
//          }
//      });

    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_student;
        TextView tv_profile_txt,tvAttendanceInfo;
        Spinner spiner_attendence;
        TextView tittle_tv;
        AutoCompleteTextView tv_option;
        TextView tv_select;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAttendanceInfo=itemView.findViewById(R.id.tv_attendance_info);
            tv_select=itemView.findViewById(R.id.tv_selected);
            tv_option= itemView.findViewById(R.id.tv_option);
            tittle_tv=itemView.findViewById(R.id.tittle_tv);
            spiner_attendence=itemView.findViewById(R.id.spiner_attendence);
            img_student=itemView.findViewById(R.id.img_student);
            tv_profile_txt=itemView.findViewById(R.id.tittle_profile_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.spinner_item_holder,spiner_attendenceList);
           spiner_attendence.setAdapter(adapter);
//          spiner_attendence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int sposition, long id) {
//
//                  //  listener.onStudentAttendenceClick(getAdapterPosition(),sposition);
////            if (sposition>0) {
////                if (sposition == 1)
////                    listener.onStudentAttendenceClick(adapterPosition, "P");
////                else if (sposition == 2) {
////                    listener.onStudentAttendenceClick(adapterPosition, "A");
////                }
////            }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


//            ArrayAdapter<String> optionAdapter=new ArrayAdapter(context,R.layout.spinner_item_holder,spiner_attendenceList);
//            tv_option.setAdapter(optionAdapter);

          tv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   spiner_attendence.performClick();
                }
            });
           spiner_attendence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int sposition, long id) {

                    if (sposition>0){
                       tv_select.setText(parent.getSelectedItem().toString());
                        listener.onStudentAttendenceClick(getAdapterPosition(),sposition);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
}

package soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Adapter;

import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.YourSchooolPannel.SchoolCoachingSection.SchoolCoachingSectionTutor.Model.SchoolT_FreeStatusModel;

import java.util.ArrayList;
import java.util.List;

public class SchoolT_FreeStatusAdapter  extends RecyclerView.Adapter<SchoolT_FreeStatusAdapter.ViewHolder> {
    Context context;
    ArrayList<SchoolT_FreeStatusModel> list;
    OnClickListener listener;
    ArrayAdapter<String>  st_adapter;
    List<String> statusList;
    String type,from;


    public interface OnClickListener {
        void onItemClick(int postion);

        void onDelete(int position);
        void onEdit(int position);
        void onFeStatusChange(int pisition,int spinnerPistion);
    }

    public SchoolT_FreeStatusAdapter(Context context,String from,String type, ArrayList<SchoolT_FreeStatusModel> list, OnClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        statusList= new ArrayList<>();
        this.type=type;
        statusList.add("");
        statusList.add("Paid");
        statusList.add("Unpaid");
        this.from=from;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_school_fee,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SchoolT_FreeStatusModel model = list.get(position);
        holder.tv_st_student.setText(model.getStudent_name());


        String status=model.getFees_status();
        if(!TextUtils.isEmpty(status)) {
            if (status.equals("paid")) {
                if (from.equals(SCHOOL_TUTOR)){
                    holder.tv_fee_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    holder.tv_fee_status.setText("Paid");
                }else {


                   // holder.st_spiner.setSelection(1);
                    model.setPaidOrunpaid(true);
                    holder.tv_selected.setText("Paid");


                }
            } else if (status.equals("unpaid")) {
                if (from.equals(SCHOOL_TUTOR)){
                    holder.tv_fee_status.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    holder.tv_fee_status.setText("Unpaid");
                }else {

                   // holder.st_spiner.setSelection(2);
                    model.setPaidOrunpaid(false);
                    holder.tv_selected.setText("Unpaid");
                }

            }
        }


        switch (position % 2) {
            case 0:
                holder.lin_s_t_row.setBackgroundColor((ContextCompat.getColor(context,R.color.white_smoke)));
                break;
            case 1:
                holder.lin_s_t_row.setBackgroundColor((ContextCompat.getColor(context,R.color.white)));
                break;
            default:
                holder.lin_s_t_row.setBackgroundColor((ContextCompat.getColor(context,R.color.white_smoke)));
                break;
        }


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_edit;
        LinearLayout lin_main;
        Spinner st_spiner;
        TextView tv_class_title,tv_st_student,tv_fee_status,tv_selected;
        LinearLayout lin_spinner;

        LinearLayout lin_s_t_row,lin_student_row;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_spinner=itemView.findViewById(R.id.lin_spinner);
            tv_selected=itemView.findViewById(R.id.tv_selected);
            tv_st_student=itemView.findViewById(R.id.tv_st_student);
            lin_s_t_row= itemView.findViewById(R.id.lin_s_t_row);
            lin_student_row= itemView.findViewById(R.id.lin_student_row);
            st_spiner= itemView.findViewById(R.id.st_spiner);
            lin_main= itemView.findViewById(R.id.lin_main);
            img_edit= itemView.findViewById(R.id.img_edit);
            tv_class_title= itemView.findViewById(R.id.tv_class_title);
            tv_fee_status=itemView.findViewById(R.id.tv_fee_status);


            if (type.equals("0")){
                lin_s_t_row.setVisibility(View.GONE);
                lin_student_row.setVisibility(View.VISIBLE);

            } else if (type.equals("1")) {
                lin_s_t_row.setVisibility(View.VISIBLE);
                lin_student_row.setVisibility(View.GONE);
            }

            if (from.equals(SCHOOL_TUTOR)) {
               // st_spiner.setVisibility(View.GONE);
                lin_spinner.setVisibility(View.GONE);
                tv_fee_status.setVisibility(View.VISIBLE);
            }

            st_adapter = new ArrayAdapter<>(context,R.layout.spinner_item_holder,statusList);
           st_spiner.setAdapter(st_adapter);
           st_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int sposition, long id) {

                    if (sposition>0){

                        tv_selected.setText(parent.getSelectedItem().toString());
                        listener.onFeStatusChange(getAdapterPosition(),sposition);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
           tv_selected.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   st_spiner.performClick();
               }
           });
        }
    }
}


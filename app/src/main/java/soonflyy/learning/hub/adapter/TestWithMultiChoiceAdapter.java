package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.TestQuestionModel;

import java.util.List;

public class TestWithMultiChoiceAdapter extends RecyclerView.Adapter<TestWithMultiChoiceAdapter.MyViewHolder> {
    private Context context;
    private List<TestQuestionModel> list;

    public TestWithMultiChoiceAdapter(Context context, List<TestQuestionModel> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.test_multichoice_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if ((position+1)%2==0){
            holder.cl.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));
        }else{
            holder.cl.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }

        TestQuestionModel model = list.get(position);
        holder.question_tv.setText((position + 1) + ". " + model.getTitle());
       // holder.tv_mark.setText(model.getMarks());
        String[] options = model.getOptions();
        holder.option_1_btn.setText(options[0]);
        holder.option_2_btn.setText(options[1]);
        holder.option_3_btn.setText(options[2]);
        holder.option_4_btn.setText(options[3]);
        if (model.getSelected_option().equals("-1")) {
            setAnswer(holder, "-1");
        } else {
            setAnswer(holder, model.getSelected_option());
        }

    }

    private void setAnswer(MyViewHolder holder, String selected_option) {
        switch (selected_option) {
            case "-1":
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                break;
            case "0":
                holder.option_1_btn.setChecked(true);
                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                break;
            case "1":
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(true);
                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                break;
            case "2":
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(true);
                holder.option_4_btn.setChecked(false);
                break;
            case "3":
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(true);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<TestQuestionModel>getStudentTestData(){
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView question_tv, tv_mark;
        RadioButton option_1_btn, option_2_btn, option_3_btn, option_4_btn;
        ConstraintLayout cl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question_tv = itemView.findViewById(R.id.question_tv);
            option_1_btn = itemView.findViewById(R.id.opton_1_btn);
            option_2_btn = itemView.findViewById(R.id.opton_2_btn);
            option_3_btn = itemView.findViewById(R.id.opton_3_btn);
            option_4_btn = itemView.findViewById(R.id.opton_4_btn);
            cl=itemView.findViewById(R.id.cl1);

            tv_mark = itemView.findViewById(R.id.tv_mark);
            option_1_btn.setOnClickListener(this);
            option_2_btn.setOnClickListener(this);
            option_3_btn.setOnClickListener(this);
            option_4_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition=getAdapterPosition();
            switch (v.getId()) {
                case R.id.opton_1_btn:
                    option_1_btn.setChecked(true);
                    option_2_btn.setChecked(false);
                    option_3_btn.setChecked(false);
                    option_4_btn.setChecked(false);
                    setSelection(0,adapterPosition);
                    break;
                case R.id.opton_2_btn:
                    setSelection(1,adapterPosition);
                    option_1_btn.setChecked(false);
                    option_2_btn.setChecked(true);
                    option_3_btn.setChecked(false);
                    option_4_btn.setChecked(false);
                    break;
                case R.id.opton_3_btn:
                    option_1_btn.setChecked(false);
                    option_2_btn.setChecked(false);
                    option_3_btn.setChecked(true);
                    option_4_btn.setChecked(false);
                    setSelection(2,adapterPosition);
                    break;
                case R.id.opton_4_btn:
                    option_1_btn.setChecked(false);
                    option_2_btn.setChecked(false);
                    option_3_btn.setChecked(false);
                    option_4_btn.setChecked(true);
                    setSelection(3,adapterPosition);
                    break;


            }
        }
    }

    private void setSelection(int option,int position) {
        list.get(position).setSelected_option(String.valueOf(option));
    }
}

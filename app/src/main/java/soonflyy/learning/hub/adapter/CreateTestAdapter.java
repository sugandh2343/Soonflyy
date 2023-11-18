package soonflyy.learning.hub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.TestQuestionModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTestAdapter extends RecyclerView.Adapter<CreateTestAdapter.TestViewHolder> {
   public HashMap<Integer,RecyclerView.ViewHolder>holderHasMap=new HashMap<>();
    Context context;
    ArrayList<TestQuestionModel> list;//CreateTestModel
    OnSelectAnswerListener listener;

//    int preCheckBtn=0;
    int hasData;
    String qType;
    public interface OnSelectAnswerListener{
        void onAnswerSelect(String option);
        void onRemoveQuestion(int position,String id);
    }

public CreateTestAdapter(Context context, ArrayList<TestQuestionModel> list,int hasData,String qType,
                         OnSelectAnswerListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
        this.hasData=hasData;
        this.qType=qType;
        }



    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_createtest,parent,false);
       TestViewHolder viewHolder=new TestViewHolder(view);
        return viewHolder;//new CreateTestAdapter.ViewHolder (view)
    }


    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        int adapterPOsition=position;
    int questionNo=position+1;
        holder.question_num_tv.setText("Question " + questionNo);
       // if (hasData>0){
            TestQuestionModel model=list.get(position);
           // if (qType.equals("test")) {

            Log.e("Qs","-----------------");
                holder.mark_edit.setText(model.getMarks());
                Log.e("mark",""+model.getMarks());
            //}
            Log.e("optionlength",""+model.getOptions().length);
         //   if(model.getOptions().length!=0) {
                holder.question_edit.setText(model.getTitle());
                holder.option_1_edit.setText(model.getOptions()[0]);
                holder.option_2_edit.setText(model.getOptions()[1]);
                holder.option_3_edit.setText(model.getOptions()[2]);
                holder.option_4_edit.setText(model.getOptions()[3]);

                Log.e("op1",""+model.getOptions()[0]);
                Log.e("op2",""+model.getOptions()[1]);
                Log.e("op3",""+model.getOptions()[2]);
                Log.e("op4",""+model.getOptions()[3]);
                Log.e("answer",""+model.getAnswer());
                if (model.getAnswer().equals("-1"))
                    holder.answer_key_tv.setText("");
                else
                holder.answer_key_tv.setText(model.getOptions()[Integer.parseInt(model.getAnswer())]);
                // if (qType.equals("test")) {
                //   holder.description_edit.setText(model.getDetails());
                //}

                switch (model.getAnswer()) {
                    case"-1":
                        //preCheckBtn=0;
                        resetSelectedField(holder,0);
                        break;
                    case "0":
                        holder.option_1_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        holder.option_1_edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_icon, 0);
                        holder.option_1_btn.setChecked(true);
                        //preCheckBtn = 1;
                        changeOtherOptionColor(holder,1);
                        break;
                    case "1":
                        holder.option_2_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        holder.option_2_edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_icon, 0);
                        holder.option_2_btn.setChecked(true);
                        //preCheckBtn = 2;
                        changeOtherOptionColor(holder,2);
                        break;
                    case "2":
                        holder.option_3_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        holder.option_3_edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_icon, 0);
                        holder.option_3_btn.setChecked(true);
                        //preCheckBtn = 3;
                        changeOtherOptionColor(holder,3);
                        break;
                    case "3":
                        holder.option_4_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        holder.option_4_edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_icon, 0);
                        holder.option_4_btn.setChecked(true);
                        //preCheckBtn = 4;
                        changeOtherOptionColor(holder,4);
                        break;
                }
           // }

       // }

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=list.get(adapterPOsition).getId();
               // String
                removeItem(adapterPOsition);
                listener.onRemoveQuestion(adapterPOsition,id);
            }
        });




    }
    private void changeOtherOptionColor(TestViewHolder holder,int checked) {
        switch (checked){
            case 1:

                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                holder.option_2_edit.setBackgroundColor(Color.WHITE);
                holder.option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_2_edit.setCompoundDrawables(null,null,null,null);
                holder.option_3_edit.setBackgroundColor(Color.WHITE);
                holder.option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_3_edit.setCompoundDrawables(null,null,null,null);
                holder.option_4_edit.setBackgroundColor(Color.WHITE);
                holder.option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_4_edit.setCompoundDrawables(null,null,null,null);

                break;
            case 2:
                holder.option_1_btn.setChecked(false);

                holder.option_3_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                holder. option_1_edit.setBackgroundColor(Color.WHITE);
                holder.option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_1_edit.setCompoundDrawables(null,null,null,null);
                holder.option_3_edit.setBackgroundColor(Color.WHITE);
                holder.option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_3_edit.setCompoundDrawables(null,null,null,null);
                holder.option_4_edit.setBackgroundColor(Color.WHITE);
                holder.option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_4_edit.setCompoundDrawables(null,null,null,null);

                break;
            case 3:
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(false);
                holder.option_4_btn.setChecked(false);
                holder. option_1_edit.setBackgroundColor(Color.WHITE);
                holder.option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_1_edit.setCompoundDrawables(null,null,null,null);
                holder.option_2_edit.setBackgroundColor(Color.WHITE);
                holder.option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_2_edit.setCompoundDrawables(null,null,null,null);

                holder.option_4_edit.setBackgroundColor(Color.WHITE);
                holder. option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_4_edit.setCompoundDrawables(null,null,null,null);

                break;
            case 4:
                holder.option_1_btn.setChecked(false);
                holder.option_2_btn.setChecked(false);
                holder.option_3_btn.setChecked(false);

                holder.option_1_edit.setBackgroundColor(Color.WHITE);
                holder.option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_1_edit.setCompoundDrawables(null,null,null,null);
                holder. option_2_edit.setBackgroundColor(Color.WHITE);
                holder.option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_2_edit.setCompoundDrawables(null,null,null,null);
                holder.option_3_edit.setBackgroundColor(Color.WHITE);
                holder.option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                holder.option_3_edit.setCompoundDrawables(null,null,null,null);


                break;



        }
    }
    private void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    private void resetSelectedField(TestViewHolder holder, int preCheckBtn) {

        holder.option_1_btn.setChecked(false);
        holder.option_2_btn.setChecked(false);
        holder.option_3_btn.setChecked(false);
        holder.option_4_btn.setChecked(false);
        holder.option_1_edit.setBackgroundColor(Color.WHITE);
        holder.option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
        holder. option_1_edit.setCompoundDrawables(null,null,null,null);
        holder.option_2_edit.setBackgroundColor(Color.WHITE);
        holder.option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
        holder.option_2_edit.setCompoundDrawables(null,null,null,null);
        holder.option_3_edit.setBackgroundColor(Color.WHITE);
        holder.option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
        holder.option_3_edit.setCompoundDrawables(null,null,null,null);
        holder.option_4_edit.setBackgroundColor(Color.WHITE);
        holder.option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
        holder.option_4_edit.setCompoundDrawables(null,null,null,null);

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ///RecyclerView rec_option;
        LinearLayout tv_remove;
        TextView question_num_tv,answer_key_tv;
        EditText option_1_edit,option_2_edit,option_3_edit,option_4_edit,mark_edit,question_edit,description_edit;
        RadioButton option_1_btn,option_2_btn,option_3_btn,option_4_btn;
        LinearLayout mark_layout,comment_layout;
        public TestViewHolder(@NonNull View itemView) {
            super (itemView);
//            rec_option=itemView.findViewById (R.id.rec_option);
//            rec_option.setLayoutManager(new LinearLayoutManager (context));
            question_num_tv=itemView.findViewById(R.id.question_no_tv);
            answer_key_tv=itemView.findViewById(R.id.answer_key_tv);
            tv_remove=itemView.findViewById(R.id.lin_remove);

            mark_edit=itemView.findViewById(R.id.mark_edit);
            question_edit=itemView.findViewById(R.id.question_edit);
            description_edit=itemView.findViewById(R.id.description_edit);
            option_1_edit=itemView.findViewById(R.id.option_1_edit);
            option_2_edit=itemView.findViewById(R.id.option_2_edit);
            option_3_edit=itemView.findViewById(R.id.option_3_edit);
            option_4_edit=itemView.findViewById(R.id.option_4_edit);

            option_1_btn=itemView.findViewById(R.id.option_1_radio_btn);
            option_2_btn=itemView.findViewById(R.id.option_2_radio_btn);
            option_3_btn=itemView.findViewById(R.id.option_3_radio_btn);
            option_4_btn=itemView.findViewById(R.id.option_4_radio_btn);

            mark_layout=itemView.findViewById(R.id.mark_layout);
            comment_layout=itemView.findViewById(R.id.comment_layout);
            comment_layout.setVisibility(View.GONE);
            tv_remove.setVisibility(View.VISIBLE);

//            if (qType.equals("quiz")){
//                mark_layout.setVisibility(View.GONE);
//                comment_layout.setVisibility(View.GONE);
//            }


            option_1_btn.setOnClickListener(this);
            option_2_btn.setOnClickListener(this);
            option_3_btn.setOnClickListener(this);
            option_4_btn.setOnClickListener(this);

            question_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(getAdapterPosition()).setTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mark_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(getAdapterPosition()).setMarks(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            option_1_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String []options=list.get(getAdapterPosition()).getOptions();
                    options[0]=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            option_2_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String []options=list.get(getAdapterPosition()).getOptions();
                    options[1]=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            option_3_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String []options=list.get(getAdapterPosition()).getOptions();
                    options[2]=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            option_4_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String []options=list.get(getAdapterPosition()).getOptions();
                    options[3]=s.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            boolean isChecked=((RadioButton)v).isChecked();
            switch (v.getId()){
                case R.id.option_1_radio_btn:
                   if (isChecked){
                       option_1_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                       option_1_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_icon,0);
                       option_2_btn.setChecked(false);
                       option_3_btn.setChecked(false);
                       option_4_btn.setChecked(false);
                       setOtherOptionColor(0,1);
                       setAnswerKey(1);
                       //preCheckBtn=1;
                       listener.onAnswerSelect("0");
                       list.get(getAdapterPosition()).setAnswer("0");
                   }
                    break;
                case R.id.option_2_radio_btn:
                    if (isChecked){
                        //option_2_btn.setHighlightColor(Color.parseColor("#3DD45B"));
                        option_2_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        option_2_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_icon,0);
                        option_1_btn.setChecked(false);
                        option_3_btn.setChecked(false);
                        option_4_btn.setChecked(false);
                        setOtherOptionColor(0,2);
                        setAnswerKey(2);
                      //  preCheckBtn=2;
                        listener.onAnswerSelect("1");
                        list.get(getAdapterPosition()).setAnswer("1");
                    }
                    break;
                case R.id.option_3_radio_btn:
                    if (isChecked){
                      //  option_3_btn.setHighlightColor(Color.parseColor("#3DD45B"));
                        option_3_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        option_3_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_icon,0);
                        option_1_btn.setChecked(false);
                        option_2_btn.setChecked(false);
                        option_4_btn.setChecked(false);
                        setOtherOptionColor(0,3);
                        //preCheckBtn=3;
                        setAnswerKey(3);
                        listener.onAnswerSelect("2");
                        list.get(getAdapterPosition()).setAnswer("2");

                    }
                    break;
                case R.id.option_4_radio_btn:
                    if (isChecked){
                        option_4_edit.setBackgroundColor(Color.parseColor("#76FB91"));
                        option_4_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_icon,0);
                        option_1_btn.setChecked(false);
                        option_2_btn.setChecked(false);
                        option_3_btn.setChecked(false);
                        setAnswerKey(4);
                        setOtherOptionColor(0,4);
                        listener.onAnswerSelect("3");
                      //  preCheckBtn=4;
                        list.get(getAdapterPosition()).setAnswer("3");
                    }
                    break;
            }

        }

//        private void setOtherOptionColor(int preCheckBtn,int checked) {
//            switch (preCheckBtn){
//                case 1:
//                    option_1_edit.setBackgroundColor(Color.WHITE);
//                    option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
//                    option_1_edit.setCompoundDrawables(null,null,null,null);
//
//                    break;
//                case 2:
//                    option_2_edit.setBackgroundColor(Color.WHITE);
//                    option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
//                    option_2_edit.setCompoundDrawables(null,null,null,null);
//
//                    break;
//                case 3:
//                    option_3_edit.setBackgroundColor(Color.WHITE);
//                    option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
//                    option_3_edit.setCompoundDrawables(null,null,null,null);
//
//                    break;
//                case 4:
//                    option_4_edit.setBackgroundColor(Color.WHITE);
//                    option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
//                    option_4_edit.setCompoundDrawables(null,null,null,null);
//
//                    break;
//
//
//
//            }
//        }
        private void setOtherOptionColor(int preCheckBtn,int checked) {
            switch (checked){
                case 1:
                    option_2_edit.setBackgroundColor(Color.WHITE);
                    option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_2_edit.setCompoundDrawables(null,null,null,null);
                    option_3_edit.setBackgroundColor(Color.WHITE);
                    option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_3_edit.setCompoundDrawables(null,null,null,null);
                    option_4_edit.setBackgroundColor(Color.WHITE);
                    option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_4_edit.setCompoundDrawables(null,null,null,null);

                    break;
                case 2:
                    option_1_edit.setBackgroundColor(Color.WHITE);
                    option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_1_edit.setCompoundDrawables(null,null,null,null);
                    option_3_edit.setBackgroundColor(Color.WHITE);
                    option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_3_edit.setCompoundDrawables(null,null,null,null);
                    option_4_edit.setBackgroundColor(Color.WHITE);
                    option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_4_edit.setCompoundDrawables(null,null,null,null);

                    break;
                case 3:
                    option_1_edit.setBackgroundColor(Color.WHITE);
                    option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_1_edit.setCompoundDrawables(null,null,null,null);
                    option_2_edit.setBackgroundColor(Color.WHITE);
                    option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_2_edit.setCompoundDrawables(null,null,null,null);

                    option_4_edit.setBackgroundColor(Color.WHITE);
                    option_4_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_4_edit.setCompoundDrawables(null,null,null,null);

                    break;
                case 4:
                    option_1_edit.setBackgroundColor(Color.WHITE);
                    option_1_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_1_edit.setCompoundDrawables(null,null,null,null);
                    option_2_edit.setBackgroundColor(Color.WHITE);
                    option_2_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_2_edit.setCompoundDrawables(null,null,null,null);
                    option_3_edit.setBackgroundColor(Color.WHITE);
                    option_3_edit.setBackground(context.getDrawable(R.drawable.drawable_edit_text_shadow_bg));
                    option_3_edit.setCompoundDrawables(null,null,null,null);


                    break;



            }
        }

        private void setAnswerKey(int option) {
            switch (option){
                case 1:
                    answer_key_tv.setText(option_1_edit.getText().toString().trim());
                    break;
                case 2:
                    answer_key_tv.setText(option_2_edit.getText().toString().trim());
                    break;
                case 3:
                    answer_key_tv.setText(option_3_edit.getText().toString().trim());
                    break;
                case 4:
                    answer_key_tv.setText(option_4_edit.getText().toString().trim());
                    break;

            }

        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TestViewHolder holder) {
        holderHasMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TestViewHolder holder) {
        holderHasMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);

    }
}

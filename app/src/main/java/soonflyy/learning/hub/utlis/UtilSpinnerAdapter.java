package soonflyy.learning.hub.utlis;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import soonflyy.learning.hub.R;
import soonflyy.learning.hub.studentModel.SubscribedCourse;

import java.util.List;

public class UtilSpinnerAdapter extends ArrayAdapter implements android.widget.SpinnerAdapter {
    Activity context;
//    List<String> list;
  List<SubscribedCourse>list;
    LayoutInflater inflate;
    public UtilSpinnerAdapter(Activity context, int resouceId, int textviewId, List<SubscribedCourse> list){
        super(context,resouceId,textviewId, list);
        inflate = context.getLayoutInflater();
        this.context=context;
        this.list=list;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        if(convertView == null){
            convertView = inflate.inflate(R.layout.spinner_drop_down_single_row,parent, false);
        }

        view.setBackgroundColor(context.getResources().getColor(R.color.white));
        TextView tv = (TextView) convertView.findViewById(R.id.name);
        tv.setTextSize(14.0f);
        /*if (position == 0) {
            tv.setTextColor(context.getResources().getColor(R.color.gray_color));
        } else {
            tv.setTextColor(context.getResources().getColor(android.R.color.black));
        }*/
        tv.setTextColor(context.getResources().getColor(android.R.color.black));
        tv.setText(list.get(position).getTitle ());
        Log.e ("name_course", "getDropDownView: "+list.get(position).getTitle () );
        return convertView;
    }
}

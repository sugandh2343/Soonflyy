package soonflyy.learning.hub.Confiq;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.activity.ChoiceActivity;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;



public class Common {
    Context context;
    SessionManagement session_management;
    DatePickerDialog.OnDateSetListener setListener;

    public Common(Context context) {
        this.context = context;
        session_management=new SessionManagement(context);
    }


    public String checkNull(String s) {
        String str = "";
        if (s == null || s.isEmpty ( ) || s.equalsIgnoreCase ("null")) {
            str = "";
        } else {
            str = s;
        }
        return str;
    }
    public String getRandomKey(int i) {
        final String characters = "0123456789";
        StringBuilder stringBuilder = new StringBuilder ( );
        while (i > 0) {
            Random ran = new Random ( );
            stringBuilder.append (characters.charAt (ran.nextInt (characters.length ( ))));
            i--;
        }
        return stringBuilder.toString ( );
    }


    public String VolleyErrorMessage(VolleyError error) {
        String str_error = "";
        if (error instanceof TimeoutError) {
            str_error = "Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error = "Session Timeout";

        } else if (error instanceof ServerError) {
            str_error = "Server not responding please try again later";

        } else if (error instanceof NetworkError) {
            str_error = "Server not responding please try again later";

        } else if (error instanceof ParseError) {

            str_error = "An Unknown error occur";
        } else if (error instanceof NoConnectionError) {
            str_error = "No Internet Connection";
        }

        return str_error;
    }

    public void showToast(String s) {
        Toast.makeText (context, "" + s, Toast.LENGTH_SHORT).show ( );
    }

    public String showVolleyError(VolleyError volleyError) {
        String msg = VolleyErrorMessage (volleyError);
        if (!msg.isEmpty ( )) {
            showToast ("" + msg);
        }
        return msg;
    }

    public void whatsapp(String phone, String message) {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent (Intent.ACTION_VIEW);

        try {
            String url = "whatsapp://send?phone="+phone+"&text="+ URLEncoder.encode(message, "UTF-8");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void openWhatsapp(String message) {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent (Intent.ACTION_VIEW);

        try {
            String url = "whatsapp://send?phone="+"&text="+ URLEncoder.encode(message, "UTF-8");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void noInternet() {

        final Dialog dialog = new Dialog (context);
       // dialog.getWindow ( ).setLayout (WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();

       // dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.setCanceledOnTouchOutside (false);
        dialog.setContentView (R.layout.no_internet_layout);

        dialog.show ( );
        Button btn_again = (Button) dialog.findViewById (R.id.btn_again);



        btn_again.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                if (ConnectivityReceiver.isConnected()) {
                    Intent intent = new Intent (context, ChoiceActivity.class);
                    context.startActivity(intent);

                }
                else
                {
                    noInternet ();
                }

            }
        });

    }

    public void datePicker(TextView tv_date) {
        Calendar calendar = Calendar.getInstance ( );
        final int year = calendar.get (Calendar.YEAR);
        final int month = calendar.get (Calendar.MONTH);
        final int day = calendar.get (Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog (context, android.R.style.Theme_Holo_Light_Panel, setListener, year, month, day);
        datePickerDialog.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        datePickerDialog.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
        datePickerDialog.show ( );
        datePickerDialog.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
        datePickerDialog.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


        setListener = new DatePickerDialog.OnDateSetListener ( ) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                tv_date.setText (date);
            }
        };
    }


}

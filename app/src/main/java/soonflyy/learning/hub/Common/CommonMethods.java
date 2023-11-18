package soonflyy.learning.hub.Common;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.WindowManager.LayoutParams.FLAG_SECURE;
import static soonflyy.learning.hub.Common.Constant.DEVICE_ID;
import static soonflyy.learning.hub.Common.Constant.INDEPENDENT_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_STUDENT;
import static soonflyy.learning.hub.Common.Constant.SIMPLEE_HOME_TUTOR;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.rajat.pdfviewer.PdfViewerActivity;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Teacher_Pannel.TeacherMainActivity;
import soonflyy.learning.hub.activity.ChoiceActivity;
import soonflyy.learning.hub.activity.MainActivity;
import soonflyy.learning.hub.activity.SplashActivity;
import soonflyy.learning.hub.model.DayModel;
import soonflyy.learning.hub.utlis.AppConstant;
import soonflyy.learning.hub.utlis.AppController;
import soonflyy.learning.hub.utlis.ConnectivityReceiver;
import soonflyy.learning.hub.utlis.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class CommonMethods {

    private static ProgressDialog progressDialog;
    private static Context context;
    private static String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};


    public static void toPrettyFormat(String jsonString, int type) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(jsonString).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(json);
            switch (type) {
                case 0:
                    Log.e("Response:%n %s", prettyJson);
                    break;
                case 1:
                    Log.e("Request:%n %s", prettyJson);
                    break;
                default:
                    Log.e("Data:%n %s", prettyJson);
                    break;
            }
        } catch (JsonSyntaxException e) {
            Log.e("Data:%n %s", jsonString);
            e.printStackTrace();
        }
    }

    public static void generalAlert(Context context, String msg) {
        AlertDialog.Builder builder1;
        builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setTitle("Alert");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static <T> void intentAlert(Context context, String arg, String msg, Class<T> tClass) {
        AlertDialog.Builder builder1;
        builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setTitle("Alert");
        builder1.setCancelable(true);
        builder1.setNeutralButton("No",
                (dialog, id) -> dialog.cancel());
        builder1.setPositiveButton("yes", (dialog, which) -> {
            Intent intent = new Intent(context, tClass);
            intent.putExtra(AppConstant.USER_TYPE, arg);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void requestQueue(Context context, StringRequest request) {
        RequestQueue requestQueue1 = Volley.newRequestQueue(context);
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue1.add(request);
    }

    public static void commonToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showSuccessToast(Context context, String message) {
     /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          Log.e("running","toast");
            DynamicToast.Config.getInstance().setTextSize(16)
                    .setTextTypeface(context.getResources().getFont(R.font.spartan))
            //.setDefaultBackgroundColor(Color.WHITE)
                    .setToastBackground(context.getDrawable(R.drawable.bg_round_red_outline))
                    .setDisableIcon(true)

                    .apply();
        }
        DynamicToast.makeSuccess(context,message,3000).show();

      */
        View toastLayout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        final Toast toast = new Toast(context);
        ((TextView) toastLayout.findViewById(R.id.tv_toast_message)).setText(message);
        //   toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();

      /*  Toast toast=Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        View view=toast.getView();
        view.setBackground(context.getDrawable(R.drawable.bg_round_red_outline));
        TextView tview=(TextView) view.findViewById(android.R.id.message);
        tview.setTextColor(context.getColor(R.color.primary_color));
        tview.setPadding(32,20,32,20);
        tview.setTextSize(16);
        tview.setShadowLayer(0,0,0,Color.TRANSPARENT);
        //Typeface font=Typeface.createFromAsset(context.getAssets(),"font/lexend.ttf");

      /// tview.setTypeface(font);
        toast.setView(view);
        toast.show();

       */


    }

    public static boolean checkReadPermission(Context context) {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("permissionChecked","true");
            return true;
        } else {
            Log.e("permissionChecked","false");
            return false;
        }
    }

    public static void requestPermission(Activity activity, int requestCode) {
        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
    }
    public static boolean checkNotificationPermission(Context contextPm){
        if (contextPm.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("permissionChecked","true");
            return true;
        } else {
            Log.e("permissionChecked","false");
            return false;
        }
    }
    public static void requestNotificationPermission(Activity activity, int requestCode) {
        activity.requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                requestCode);
    }

    public static boolean checkStoragePermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //permission check for android 11 and above,here check external storage manager
            return Environment.isExternalStorageManager();
        } else {
            //check code for android 10 and below version
            int read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
            int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
            return read == PackageManager.PERMISSION_GRANTED
                    && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static void requestStoragePermission(Activity activity, int requestCode, ActivityResultLauncher<Intent> activityResultLauncher) {
//

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //request permission for android 11 and above
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{activity.getPackageName()})));
                activityResultLauncher.launch(intent);

            } catch (Exception exception) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }

        } else {
            //request permission for android 10 and bellow
            ActivityCompat.requestPermissions(activity, permissions, requestCode);

        }
    }


    public static void requestAudioCameraPermission(Activity activity, int requestCode) {
        activity.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                requestCode);
    }

    public static boolean checkAudioCameraPermission(Context context) {
        if (context.checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //video and pdf base64 conversion methods
    public static ArrayList<String> getBase64StringList(ArrayList<Uri> uriList, Context context) {
        ArrayList<String> list = new ArrayList<>();
        Log.e("uri size ", String.valueOf(uriList.size()));
        if (uriList.size() > 0) {
            for (int i = 0; i < uriList.size(); i++) {
                list.add(convertToBase64String(uriList.get(i), context));
            }
        }
        return list;
    }

    public static String convertToBase64String(Uri uri, Context context) {
        String convertedString = null;
        String uriString = uri.toString();
        Log.e("base_data", "onActivityResult: uri" + uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            Log.e("base64_data", " bytes size=" + bytes.length);
            // Log.e("base64_data", " Base64string="+ Base64.encodeToString(bytes,Base64.DEFAULT));
            convertedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            // Document=Base64.encodeToString(bytes,Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
        return convertedString;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void viewAndDownLoadPdfFrormUrl(Context context, String url, String title, boolean enableDownload, String downlDir) {
        if (downlDir == null) {
            downlDir = "";
//            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+"SImpleeExVideo");
//
//            if (!mediaStorageDir.exists()) {
//                if (!mediaStorageDir.mkdirs()) {
//                    Log.d("App", "failed to create directory");
//                }else{
//                    Log.d("App", " created directory");
//                }
//            }else{
//                Log.d("App", " directory exist already");
//            }
//            Log.e("createdPath",""+mediaStorageDir.getPath());
//            downlDir=mediaStorageDir.getPath();
        }
        context.startActivity(
                PdfViewerActivity.Companion.launchPdfFromUrl(  //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                        context,
                        url,                                // PDF URL in String format
                        title,                        // PDF Name/Title in String format
                        downlDir,                  // If nothing specific, Put "" it will save to Downloads
                        enableDownload                    // This param is true by defualt.
                ));
    }

    public void setImageIconTintColor(Context context, ImageView imageView, int color, boolean isResourceColor) {
        if (isResourceColor)
            imageView.setColorFilter(ContextCompat.getColor(context, color));
            //  ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(ContextCompat.getColor(context,color)));
        else
            //  ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
            imageView.setColorFilter(color);
    }

    public static void setTextViewDrawerColor(Context context, TextView view, int color, boolean isResourceColor) {
        for (Drawable drawable : view.getCompoundDrawables()) {
            if (drawable != null) {
                if (isResourceColor) {
                    Log.e("tint", "running");
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, color),
                            PorterDuff.Mode.SRC_IN));
                } else
                    drawable.setColorFilter(new PorterDuffColorFilter(color,
                            PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static void setEditTextDrawerColor(Context context, EditText view, int color, boolean isResourceColor) {
        for (Drawable drawable : view.getCompoundDrawables()) {
            if (drawable != null) {
                if (isResourceColor) {
                    Log.e("tint", "running");
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, color),
                            PorterDuff.Mode.SRC_IN));
                } else
                    drawable.setColorFilter(new PorterDuffColorFilter(color,
                            PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public void callUs(Context context) {
        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + 1234567890));
            context.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void postRequest(String url, HashMap<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Log.e("postmethod", "postRequest: " + url + "\n" + params);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Log.e("params", "check" + params);
                return params;
                // return super.getParams ( );
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(Constant.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        AppController.getInstance().addToRequestQueue(stringRequest, "tag");

    }

    public static boolean validatePinCode(String pinCode) {
        if (pinCode.length() == 6)
            return true;
        else
            return false;

    }

    public static boolean isDateExpired(String date, String format) throws ParseException {
        String currentDate = getCurrentTime(format);
        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        Date d1 = sdformat.parse(date);
        Date d2 = sdformat.parse(currentDate);
        if (d2.after(d1))
            return true;
        else
            return false;
    }


    public static boolean isExpired(String assignDate) throws ParseException {
        String currentDate = getCurrentDate();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.after(d1))
            return true;
        else
            return false;

    }

    public static boolean isValidDOB(String assignDate) throws ParseException {
        String currentDate = getCurrentDate();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d1.before(d2))
            return true;
        else
            return false;
    }

    public static boolean isValidLiveDate(String assignDate) throws ParseException {
        String currentDate = getCurrentDate();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d1.after(d2) || d1.equals(d2))
            return true;
        else
            return false;
    }

    public static boolean isValidExpiryDate(String assignDate) throws ParseException {
        String currentDate = getCurrentDate();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d1.after(d2))
            return true;
        else
            return false;
    }


    public static boolean isCurrentDate(String assignDate) throws ParseException {
        String currentDate = getCurrentDate();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.equals(d1))
            return true;
        else
            return false;
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getCurrentDayName() {
        SimpleDateFormat f = new SimpleDateFormat("EEEE");
        return f.format(new Date());
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy | hh:mm a");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTime(String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        return format.format(Calendar.getInstance().getTime());
    }


    public static String[] getCourseProvide() {
        String[] cProvide = new String[]{
                "Daily practice papers",
                "Live class demo",
                "Test",
                "Recorded videos",
                "Live sessions",
                "Notes"
        };
        return cProvide;

    }

    public static void rotateScreen(Activity activity, String status, String user_type) {
        // ViewGroup.LayoutParams params=cl_video.getLayoutParams();
        if (activity.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            // params.height=0;
            if (status.equals("fullScreen")) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                // cl_video.setLayoutParams(params);
                if (user_type.equals("teacher")) {
                    ((TeacherMainActivity) activity).getSupportActionBar().hide();
                } else {
                    ((MainActivity) activity).getSupportActionBar().hide();
                }
                // lin_related.setVisibility(View.GONE);
            }
        } else {
            //   params.height=300;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // cl_video.setLayoutParams(params);
            if (user_type.equals("teacher")) {
                ((TeacherMainActivity) activity).getSupportActionBar().show();
            } else {
                ((MainActivity) activity).getSupportActionBar().show();
            }
            // lin_related.setVisibility(View.VISIBLE);
        }
    }

    public static int getTotalDaysOfMonth(int month, int year) {
        int value = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                value = 31;
                break;
            case 2:
                if (year % 4 == 0)
                    value = 29;
                else
                    value = 28;

                break;
            case 4:
            case 6:
            case 9:
            case 11:
                value = 30;
                break;

        }
        return value;
    }

    public static int remainDaysInMonth(int month, int year, int totalDay) {
        int total = 0;
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        if (year == currentYear) {
            if (month == currentMonth) {
                if (totalDay == currentDay) {
                    total = totalDay;
                } else {
                    total = currentDay;
                }
            } else if (month < currentMonth) {
                total = totalDay;
            }

        } else if (year < currentYear) {
            total = totalDay;
        }
        return total;
    }

    public static int getDurationBetweenTime(String time1, String time2) {
        int duration = 0;
        SimpleDateFormat df = new SimpleDateFormat("h:mm a");

        try {
            Date t1 = df.parse(time1);

            Date t2 = df.parse(time2);
            long difInMilliSeconds =
                    Math.abs(t2.getTime() - t1.getTime());
            long diff_hour = (difInMilliSeconds / (60 * 60 * 1000)) % 24;
            long diff_minutes = (difInMilliSeconds / (60 * 1000)) % 60;

            duration = (int) ((diff_hour * 60) + diff_minutes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("cal_duration", "" + duration);
        return duration;
    }

    public static int getLeftDurationLiveTime(String time1, String time2) {
        int duration = 0;
        SimpleDateFormat df = new SimpleDateFormat("h:mm a");

        try {
            Date t1 = df.parse(time1);

            Date t2 = df.parse(time2);
            long difInMilliSeconds =
                    Math.abs(t2.getTime() - t1.getTime());
            long diff_hour = (difInMilliSeconds / (60 * 60 * 1000)) % 24;
            long diff_minutes = (difInMilliSeconds / (60 * 1000)) % 60;

            duration = (int) ((diff_hour * 60) + diff_minutes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("cal_duration", "" + duration);
        return duration;
    }
    public static long getTimeGapBetweenTime(String time1, String time2) {
        long duration = 0;
        SimpleDateFormat df = new SimpleDateFormat("h:mm:ss a");

        try {
            Date t1 = df.parse(time1);

            Date t2 = df.parse(time2);
            duration = Math.abs(t2.getTime() - t1.getTime());
//            long diff_hour = (difInMilliSeconds / (60 * 60 * 1000)) % 24;
//            long diff_minutes = (difInMilliSeconds / (60 * 1000)) % 60;
//            long differenceSeconds = difInMilliSeconds / 1000 % 60;
//
//            int  = (int) ((diff_hour * 60) + diff_minutes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("cal_duration", "" + duration);
        return duration;
    }


    public static boolean isValidTimePeriod(String time1, String time2, String date) {
        Boolean status = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(time1);
            Date t2 = df.parse(time2);
            if (t1.before(t2)) {
                if (isCurrentDate(date)) {
                    String d = date + " " + time1;
                    SimpleDateFormat dfmt = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
                    Date d_start_time = dfmt.parse(d);
                    Date c_time = dfmt.parse(getCurrentTime("dd-MMM-yyyy h:mm a"));
                    if (d_start_time.after(c_time)) {
                        status = true;
                    }
                } else {
                    status = true;
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static boolean isValidTimePeriod(String time1, String time2) {
        Boolean status = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(time1);
            Date t2 = df.parse(time2);
            if (t1.before(t2)) {

                status = true;
            } else {
                status = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static boolean isValidTime(String time1, String time2, String date, int selectedDuration) {
        boolean status = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(time1);
            Date t2 = df.parse(time2);
            if (t1.before(t2)) {
                if (!isBeforeDate(date)) {
                    if (isCurrentDate(date)) {
                        String d = date + " " + time1;
                        SimpleDateFormat dfmt = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
                        Date d_start_time = dfmt.parse(d);
                        Date c_time = dfmt.parse(getCurrentTime("dd-MMM-yyyy h:mm a"));
                        if (d_start_time.after(c_time)) {
                            if (selectedDuration == getDurationBetweenTime(time1, time2)) {
                                status = true;
                            }
                        }
                    } else {
                        if (selectedDuration == getDurationBetweenTime(time1, time2)) {
                            status = true;

                        }
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static boolean isToday(String date) {
        boolean status = false;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date dd = df.parse(date);
            Date d2 = df.parse(getCurrentDate());
            if (dd.equals(d2)) {
                status = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static boolean isBeforeDate(String date) {
        boolean status = false;
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            Date dd = df.parse(date);
            Date d2 = df.parse(getCurrentDate());
            if (dd.before(d2)) {
                status = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String changeDateTimeFmt(String orgFmt, String tgtFmt, String date_time) {
        String convertedValue = "";
        try {
            DateFormat originalFmt = new SimpleDateFormat(orgFmt, Locale.getDefault());
            DateFormat targetFmt = new SimpleDateFormat(tgtFmt);
            Date date = originalFmt.parse(date_time);
            convertedValue = targetFmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedValue;

    }


    public static void setSearchViewColor(Context context, SearchView searchView) {
        EditText searchEdit = (EditText) searchView.findViewById(com.google.android.material.R.id.search_src_text);
        searchEdit.setTextColor(ContextCompat.getColor(context, R.color.white));
        searchEdit.setHintTextColor(ContextCompat.getColor(context, R.color.white));
        ImageView icon = searchView.findViewById(com.google.android.material.R.id.search_button);
        Drawable whiteIcon = icon.getDrawable();
        whiteIcon.setTint(Color.WHITE);
        icon.setImageDrawable(whiteIcon);


    }

    public static boolean checkInternetConnection(Context context) {
        if (ConnectivityReceiver.isConnected()) {
            return true;
        } else {
            showSuccessToast(context, "No Internet Connection");
            return false;
        }
    }

    public static ArrayList<String> getFeeStatusList() {
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("All");
        monthList.add("Paid");
        monthList.add("Unpaid");
        return monthList;


    }

    public static ArrayList<String> getMonth() {
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");
        return monthList;


    }

    public static HashMap<String, String> getSelectedMonth(String monthName) {
        HashMap<String, String> valuMap = new HashMap<>();
        switch (monthName) {
            case "January":
                valuMap.put("name", "Jan");
                valuMap.put("value", "1");
                break;
            case "February":
                valuMap.put("name", "Feb");
                valuMap.put("value", "2");
                break;
            case "March":
                valuMap.put("name", "Mar");
                valuMap.put("value", "3");
                break;
            case "April":
                valuMap.put("name", "Apr");
                valuMap.put("value", "4");
                break;
            case "May":
                valuMap.put("name", "May");
                valuMap.put("value", "5");
                break;
            case "June":
                valuMap.put("name", "Jun");
                valuMap.put("value", "6");
                break;
            case "July":
                valuMap.put("name", "Jul");
                valuMap.put("value", "7");
                break;
            case "August":
                valuMap.put("name", "Aug");
                valuMap.put("value", "8");
                break;
            case "September":
                valuMap.put("name", "Sep");
                valuMap.put("value", "9");
                break;
            case "October":
                valuMap.put("name", "Oct");
                valuMap.put("value", "10");
                break;
            case "November":
                valuMap.put("name", "Nov");
                valuMap.put("value", "11");
                break;
            case "December":
                valuMap.put("name", "Dec");
                valuMap.put("value", "12");
                break;

        }
        return valuMap;
    }

    public static void showDatePicker(Context context, TextView tv_date) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String month = String.valueOf(i1 + 1);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = String.valueOf(i2);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String date = day + "/" + month + "/" + i;

                tv_date.setText(changeDateTimeFmt("dd/MM/yyyy", "dd-MMM-yyyy", date));
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public static void showDatePicker(Context context, TextView tv_date, boolean enableMin, boolean enabelMax) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String month = String.valueOf(i1 + 1);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = String.valueOf(i2);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String date = day + "/" + month + "/" + i;

                tv_date.setText(changeDateTimeFmt("dd/MM/yyyy", "dd-MMM-yyyy", date));
            }
        }, mYear, mMonth, mDay);
        if (enableMin) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        if (enabelMax) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        datePickerDialog.show();


    }

    public static void showTimePicker(Context context, String title, TextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinut = c.get(Calendar.MINUTE);
        int mSec = c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context
                , android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        Calendar cl = Calendar.getInstance();
                        cl.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cl.set(Calendar.MINUTE, minute);
                        String time = sdf.format(cl.getTime());
                        textView.setText(time);

                    }
                }, mHour, mMinut, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.setTitle(title);
        timePickerDialog.show();
    }


    public static void enableScreenshot(Window window) {
        window.clearFlags(FLAG_SECURE);
    }

    public static void disableScreenshot(Window window) {
        window.setFlags(FLAG_SECURE, FLAG_SECURE);

    }

    public static void showLogoutDialog(Activity activity, String from) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_logout);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.setCancelable(false);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.tv_no);
        TextView tv_yes = dialog.findViewById(R.id.tv_yes);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement management = new SessionManagement(activity);
                dialog.dismiss();
                switch (from) {
                    case SIMPLEE_HOME_STUDENT:
                    case SIMPLEE_HOME_TUTOR:
                        //  Intent intent=new Intent ( activity, SplashActivity.class);
                        //management.clearLogoutSession ();
                        management.clearSimpleeHomeLoginSession();
                        Intent intent = new Intent(activity, ChoiceActivity.class);
                        // management.logoutSession ();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        break;
                    case SCHOOL_COACHING:
                        management.clearSchoolLoginSession();
                        activity.finish();
                        break;
                    case SCHOOL_TUTOR:
                        management.clearSchoolTutorLoginSession();
                        activity.finish();
                        break;
                    case SCHOOL_STUDENT:
                        management.clearSchoolStudentLoginSession();
                        activity.finish();
                        break;
                    case INDEPENDENT_TUTOR:
                        management.clearItutorLoginSession();
                        // management.clearLogoutSession();
                        activity.finish();
                        break;
                }


            }
        });


    }


    public static void setFileName(Context context, Uri uri, TextView tv) {
        Cursor returnCursor = context.getContentResolver().query(uri, null,
                null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        Log.e("sFileName", "" + name);
        tv.setText(name);

    }

    public static void changeImageViewTintColor(ImageView imageView, int tintcolor, int bgTintColor) {//R.color.COLOR_YOUR_COLOR
        imageView.setBackgroundTintList(ContextCompat.getColorStateList(imageView.getContext(), bgTintColor));
        if (tintcolor != 0) {
            imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), tintcolor), PorterDuff.Mode.MULTIPLY);
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public boolean isEventTime(String time1, String time2) {
        boolean status = false;
        try {
            String current_time = CommonMethods.getCurrentTime("h:mm a");
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            Date t1 = df.parse(time1);
            Date t2 = df.parse(time2);
            Date current = df.parse(current_time);
            if ((t1.equals(current) || current.after(t1)) && (current.before(t2))) {
                status = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;

    }

    public static boolean isEventExpired(String assignDate) throws ParseException {
        Log.e("time", assignDate);
        String currentDate = getCurrentDateTime();
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy | hh:mm a");
        Date d1 = sdformat.parse(assignDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.after(d1))
            return true;
        else
            return false;

    }

    public static boolean isTodayDate(String testDate) throws ParseException {
        String currentDate = CommonMethods.getCurrentTime("dd-MMM-yyyy");
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MMM-yyyy");
        Date d1 = sdformat.parse(testDate);
        Date d2 = sdformat.parse(currentDate);
        if (d2.equals(d1))
            return true;
        else
            return false;
    }

    public void SwitchFragment(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        //  fragmentTransaction.replace(R.id.frame_layout_container, fragment, ProfileFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public static Dialog showProgress(Context context, String title, boolean indMode, int progressValue) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        TextView tvTitle = dialog.findViewById(R.id.tv_msg);
        CircularProgressBar circularProgressBar = dialog.findViewById(R.id.circularProgressBar);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (indMode) {
            circularProgressBar.setIndeterminateMode(true);
        }

        return dialog;
    }

    public static String getLastSegmentOfFilePath(String path) {

        String[] segments = path.split("/");
        return segments[segments.length - 1];

    }

    public static String getMimeType(Context mContext, String path) {

        Uri uri = Uri.fromFile(new File(path));
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;

    }

    public static String getMimeTypeFromLastSegement(String lastSeg) {

        int dotIndex = lastSeg.lastIndexOf(".");
        //String name = fileName.substring(0, dotIndex); // MyFile
        String extension = lastSeg.substring(dotIndex + 1);
        Log.e("mimelast", "" + extension);
        return extension;
    }

    public static ArrayList<DayModel> getDayList() {
        ArrayList<DayModel> list = new ArrayList<>();

        list.add(new DayModel("1", "Monday"));
        list.add(new DayModel("2", "Tuesday"));
        list.add(new DayModel("3", "Wednesday"));
        list.add(new DayModel("4", "Thursday"));
        list.add(new DayModel("5", "Friday"));
        list.add(new DayModel("6", "Saturday"));
        list.add(new DayModel("7", "Sunday"));
        return list;
    }

    public static String getDayIdFromName(String dayFullName) {
        String dId = "";
        switch (dayFullName) {
            case "Sunday":
                dId = "1";
                break;
            case "Monday":
                dId = "2";
                break;
            case "Tuesday":
                dId = "3";
                break;
            case "Wednesday":
                dId = "4";
                break;
            case "Thursday":
                dId = "5";
                break;
            case "Friday":
                dId = "6";
                break;
            case "Saturday":
                dId = "7";
                break;
        }
        return dId;

    }


    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void callDeviceInfoTaskApi(Activity contxt, String userId, String type, Timer timer) {
        // progressBar.setVisibility(View.VISIBLE);
        SessionManagement smgt=new SessionManagement(contxt);
        String runningDeviceId=smgt.getString(DEVICE_ID);
        String getUrl = BaseUrl.URL_GET_DEVICE_INFO;
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("type", type);
        if (getUrl != null) {
            CommonMethods.postRequest(getUrl, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("deviceInforamtion ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")) {
                               //progressBar.setVisibility(View.GONE);
                            String loginStatus=jsonObject.getJSONObject("data").getString("login_status");
                            String deviceId=jsonObject.getJSONObject("data").getString("device_id");
                            Log.d("loginSTatus",""+loginStatus);
                            Log.d("runningDeviceId",""+runningDeviceId);
                            Log.d("otherLoginDeviceId",""+deviceId);
                           try {
                               if (loginStatus.equals("1")) {
                                   if (!deviceId.equals(runningDeviceId)) {
                                       smgt.clearLogoutSession();
                                       Intent intent = new Intent(contxt, SplashActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       contxt.startActivity(intent);
                                       timer.cancel();

                                   }
                               }else{

                                 logout(contxt,type);
                               }
                           }catch (Exception e){
                               e.printStackTrace();
                           }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //progressBar.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //CommonMethods.showSuccessToast(getContext(), error.getMessage());
                    //progressBar.setVisibility(View.GONE);
                }
            });
        }


    }

    private static void logout(Activity activity,String type) {
        SessionManagement sessionManagement=new SessionManagement(activity);
        switch (type) {
            case "0":
            case "1":
                //home student logout
                //home tutor
                sessionManagement.clearLogoutSession();
               Intent intent=new Intent(activity,ChoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();

                break;
            case "2":
                //school
                sessionManagement.clearSchoolLoginSession();
                activity.finish();
                break;
            case "3":
                //independent tutor
                sessionManagement.clearItutorLoginSession();
                activity.finish();
                break;
            case "4":
                //school student
                sessionManagement.clearSchoolStudentLoginSession();
                activity.finish();
                break;
            case "5":
                //school tutor
                sessionManagement.clearSchoolTutorLoginSession();
                activity.finish();
                break;
        }
        showSuccessToast(activity,"Access is denied by admin.");

    }
}

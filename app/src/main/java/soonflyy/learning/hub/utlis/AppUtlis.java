package soonflyy.learning.hub.utlis;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtlis {
    public static final String TAG = "AppUtlis";

    public static final String[] countryList = {"","India", "America", "USA", "Dubai", "Japan", "China", "Bangladesh", "Pakistan", "England", "London"};
    public static final String[] cityList = {"","Mumbai", "Jaipur", "Jhansi", "Delhi","Bareilly","Kolkata","Rampur"};
    public static final String[] stateList = {"","Delhi", "Uttar Pradesh", "Uttrakhand", "Gujarat","Kerala","Tamil Nadu","Odisha"};
    public static final String[] genderList = {"","Male","Female","Other"};
    public static final String[] examList = {"","UPSC","STATE PCS","CAT EXAM","SSC","RAILWAY","SCHOOL","OTHER"};


    public static boolean isValidMobile(String number){
        if(!Pattern.matches("[a-zA-Z]+",number)){
        if(number.length()>6 && number.length()!=10){ return true; } }
        return false;
    }

    public static boolean isValidPassword(final String password){
    Pattern pattern;
    Matcher matcher;
    final String  PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_])(?=\\S+$).{6,15}$";
    pattern = Pattern.compile(PASSWORD_PATTERN);
    matcher = pattern.matcher(password);
    return matcher.matches();
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

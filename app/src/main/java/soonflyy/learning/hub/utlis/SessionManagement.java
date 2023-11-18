package soonflyy.learning.hub.utlis;

import static soonflyy.learning.hub.Common.Constant.CITY;
import static soonflyy.learning.hub.Common.Constant.DISTRICT;
import static soonflyy.learning.hub.Common.Constant.DOB;
import static soonflyy.learning.hub.Common.Constant.EMAIL;
import static soonflyy.learning.hub.Common.Constant.FATHER;
import static soonflyy.learning.hub.Common.Constant.GENDER;
import static soonflyy.learning.hub.Common.Constant.ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.INSTITUTE;
import static soonflyy.learning.hub.Common.Constant.IS_INSTRUCTOR;
import static soonflyy.learning.hub.Common.Constant.ITUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.MOBILE;
import static soonflyy.learning.hub.Common.Constant.NAME;
import static soonflyy.learning.hub.Common.Constant.PROFILE_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_CITY;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_COACHING_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_CITY;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_PINCODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_STATE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_IT_UNIQUE_CODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGIN;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_PINCODE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STATE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ADDRESS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_DOB;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_FATHER;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_STUDENT_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_S_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_S_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TEACHER_ID;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_TUTOR_ID_SECTION;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_ADDRESS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_DOB;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_EMAIL;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_FATHER;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_IMAGE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_LOGIN_STATUS;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_MOBILE;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_T_NAME;
import static soonflyy.learning.hub.Common.Constant.SCHOOL_UNIQUE_CODE;
import static soonflyy.learning.hub.Common.Constant.STATE;
import static soonflyy.learning.hub.Common.Constant.USER_ID;
import static soonflyy.learning.hub.Common.Constant.WORKPLACE;
import static soonflyy.learning.hub.utlis.AppConstant.IS_LOGIN;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_ACCOUNNO;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_ADDRESS;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_BANK_NAME;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_CITY;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_DOB;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_EMAIL;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_GENDER;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_HOLDER;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_ID;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_IFSC;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_MOBILE;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_NAME;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_PAYTM;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_PHONEPAY;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_PINCODE;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_TEZ;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_USER_NAME;
import static soonflyy.learning.hub.utlis.AppConstant.KEY_WALLET;
import static soonflyy.learning.hub.utlis.AppConstant.PREFS_NAME;
import static soonflyy.learning.hub.utlis.AppConstant.PUSH_DEVICE_ID;
import static soonflyy.learning.hub.utlis.AppConstant.PUSH_TOKEN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import soonflyy.learning.hub.activity.ChoiceActivity;

import java.util.HashMap;


public class SessionManagement {

    SharedPreferences prefs;

    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public SessionManagement(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void setString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int value){
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getInt(String key){
        return prefs.getInt(key, -1);
    }
    public String getString(String key){
        return prefs.getString(key, "");
    }
    public void setBoolean(String key, boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }
    public boolean getBoolean(String key){
        return prefs.getBoolean(key, false);
    }

    public void createLoginSession(String id, String name, String username
            , String mobile, String email, String address, String city, String pincode, String accountno,
                                   String bank_name, String ifsc, String holder, String paytm, String tez, String phonepay,
                                   String dob, String wallet, String gender) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_ACCOUNNO, accountno);
        editor.putString(KEY_BANK_NAME, bank_name);
        editor.putString(KEY_IFSC, ifsc);
        editor.putString(KEY_HOLDER, holder);
        editor.putString(KEY_PAYTM, paytm);
        editor.putString(KEY_TEZ, tez);
        editor.putString(KEY_PHONEPAY, phonepay);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_WALLET, wallet);
        editor.putString(KEY_GENDER, gender);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String> ();

        user.put(KEY_ID, prefs.getString(KEY_ID, ""));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, ""));
        user.put(KEY_USER_NAME, prefs.getString(KEY_USER_NAME, ""));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, ""));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, ""));
        user.put(KEY_ADDRESS, prefs.getString(KEY_ADDRESS, ""));
        user.put(KEY_CITY, prefs.getString(KEY_CITY, ""));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, ""));
        user.put(KEY_ACCOUNNO, prefs.getString(KEY_ACCOUNNO, ""));
        user.put(KEY_BANK_NAME, prefs.getString(KEY_BANK_NAME, ""));
        user.put(KEY_IFSC, prefs.getString(KEY_IFSC, ""));
        user.put(KEY_HOLDER, prefs.getString(KEY_HOLDER, ""));
        user.put(KEY_PAYTM, prefs.getString(KEY_PAYTM, ""));
        user.put(KEY_TEZ, prefs.getString(KEY_TEZ, ""));
        user.put(KEY_PHONEPAY, prefs.getString(KEY_PHONEPAY, ""));
        user.put(KEY_WALLET, prefs.getString(KEY_WALLET, ""));
        user.put(KEY_DOB, prefs.getString(KEY_DOB, ""));
        user.put(KEY_GENDER, prefs.getString(KEY_GENDER, ""));
        return user;
    }

    public void updateAccSection(String acc_no, String bank_name, String ifsc, String holder)
    {
        editor.putString(KEY_ACCOUNNO, acc_no);
        editor.putString(KEY_BANK_NAME, bank_name);
        editor.putString(KEY_IFSC, ifsc);
        editor.putString(KEY_HOLDER, holder);
        editor.commit();
    }
    public void updateAddressSection(String address, String city, String pincode)
    {
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PINCODE, pincode);
        editor.apply();
    }


    public void updateEmailSection(String email, String mobile, String name)
    {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public void updateItem(String key , String value)
    {
        editor.putString(key, value);
        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
        Intent logout = new Intent (context, ChoiceActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }
    public void clearLogoutSession(){
        editor.clear();
        editor.commit();
    }
    public void clearSchoolLoginSession(){
        editor.remove(SCHOOL_ID);
        editor.remove(SCHOOL_NAME);
        editor.remove(SCHOOL_MOBILE);
        editor.remove(SCHOOL_EMAIL);
        editor.remove(SCHOOL_STATE);
        editor.remove(SCHOOL_CITY);
        editor.remove(SCHOOL_IMAGE);
        editor.remove(SCHOOL_PINCODE);
        editor.remove(SCHOOL_UNIQUE_CODE);
        editor.remove(SCHOOL_LOGIN_STATUS);
        editor.remove(SCHOOL_LOGIN);
        editor.remove(SCHOOL_COACHING_ID_SECTION);
        editor.commit();

    }
    public void clearSchoolTutorLoginSession(){
        editor.remove(SCHOOL_TEACHER_ID);
        editor.remove(SCHOOL_T_NAME);
        editor.remove(SCHOOL_T_MOBILE);
        editor.remove(SCHOOL_T_EMAIL);
        editor.remove(SCHOOL_T_FATHER);
        editor.remove(SCHOOL_T_DOB);
        editor.remove(SCHOOL_T_ADDRESS);
        editor.remove(SCHOOL_T_IMAGE);
        editor.remove(SCHOOL_T_LOGIN_STATUS);
        editor.remove(SCHOOL_TUTOR_ID_SECTION);
        editor.commit();


    }
    public void clearItutorLoginSession(){
        editor.remove(SCHOOL_IT_ID);
        editor.remove(SCHOOL_IT_NAME);
        editor.remove(SCHOOL_IT_MOBILE);
        editor.remove(SCHOOL_IT_EMAIL);
        editor.remove(SCHOOL_IT_CITY);
        editor.remove(SCHOOL_IT_STATE);
        editor.remove(SCHOOL_IT_PINCODE);
        editor.remove(SCHOOL_IT_IMAGE);
        editor.remove(SCHOOL_IT_UNIQUE_CODE);
        editor.remove(SCHOOL_IT_LOGIN_STATUS);
        editor.remove(ITUTOR_ID_SECTION);
        editor.commit();
    }
    public void clearSchoolStudentLoginSession(){
        editor.remove(SCHOOL_STUDENT_ID);
        editor.remove(SCHOOL_STUDENT_NAME);
        editor.remove(SCHOOL_STUDENT_MOBILE);
        editor.remove(SCHOOL_STUDENT_EMAIL);
        editor.remove(SCHOOL_STUDENT_FATHER);
        editor.remove(SCHOOL_STUDENT_DOB);
        editor.remove(SCHOOL_STUDENT_ADDRESS);
        editor.remove(SCHOOL_S_IMAGE);
        editor.remove(SCHOOL_S_LOGIN_STATUS);
        editor.remove(SCHOOL_STUDENT_ID_SECTION);

        editor.commit();
    }
    public void clearSimpleeHomeLoginSession(){
        editor.remove(USER_ID);
        editor.remove(EMAIL);
        editor.remove(MOBILE);
        editor.remove(CITY);
        editor.remove(STATE);
        editor.remove(DISTRICT);
        editor.remove(INSTITUTE);
        editor.remove(DOB);
        editor.remove(NAME);
        editor.remove(GENDER);
        editor.remove(WORKPLACE);
        editor.remove(PROFILE_IMAGE);
        editor.remove(FATHER);
        editor.remove(ID_SECTION);
        editor.remove(LOGIN_STATUS);
        //editor.remove(LOGIN_STATUS);
        editor.remove(IS_INSTRUCTOR);
        editor.commit();
    }

//    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }



    public void addPushToken(String dId,String token) {
        editor.putString(PUSH_DEVICE_ID,dId);
        editor.putString(PUSH_TOKEN,token);
        editor.commit();
    }

    public String getPushToken() {
        return prefs.getString(PUSH_TOKEN,"");
    }
    public String getPushDeviceId() {
        return prefs.getString(PUSH_DEVICE_ID,"");
    }

}

package soonflyy.learning.hub.YourSchooolPannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import soonflyy.learning.hub.Common.ApiCode;
import soonflyy.learning.hub.R;
import soonflyy.learning.hub.Volley.VolleyResponseListener;

import java.util.HashMap;
import java.util.Random;


public class SchoolMobileVerificationFragment extends Fragment implements VolleyResponseListener, View.OnClickListener {
    EditText et_mobile_num;
    String user_type;
    Button btn_submit;
    public String otp="";

    public SchoolMobileVerificationFragment() {
        // Required empty public constructor
    }

    public static SchoolMobileVerificationFragment newInstance(String param1, String param2) {
        SchoolMobileVerificationFragment fragment = new SchoolMobileVerificationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_school_mobile_verification, container, false);
        initView(view);

        return  view;
    }

    private void initView(View view) {
        et_mobile_num=view.findViewById (R.id.et_mobile_num);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        otp=getRandomKey(4);

    }

    private String getRandomKey(int i) {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.btn_submit){
            String m = et_mobile_num.getText().toString();

            if (m.isEmpty())
            {
                Toast.makeText (getActivity(), "Mobile Number is Required", Toast.LENGTH_SHORT).show ( );
                // et_mobile_num.setError("Mobile Number is Required");

            }
            else if (m.length()!=10) {
                Toast.makeText (getActivity(), "Invalid Mobile No.", Toast.LENGTH_SHORT).show ( );
                // et_mobile_num.setError ("Invalid Mobile No.");
                et_mobile_num.requestFocus ( );
            }
            else if (Integer.parseInt (String.valueOf (m.charAt (0))) < 6) {
                //et_mobile_num.setError ("Mobile number should be start 6 or greater 6");
                Toast.makeText(getActivity(),"Mobile number should be start 6 or greater 6",Toast.LENGTH_LONG).show();
                et_mobile_num.requestFocus ( );
            }
            else {
                sendRequest(ApiCode.MOBILEVARIFICATION);
            }
        }
    }

    private void sendRequest(int request) {
    }

    private void callApi(int request, HashMap<String, String> params) {

    }

    @Override
    public void onResponse(int requestType, String response){


    }
}
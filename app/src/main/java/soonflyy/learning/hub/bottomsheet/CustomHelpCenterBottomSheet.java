package soonflyy.learning.hub.bottomsheet;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import soonflyy.learning.hub.R;

public class CustomHelpCenterBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = CustomHelpCenterBottomSheet.class.getName();
    public ItemClickListener mlistner;
    Button callUs_btn;
    TextView description_tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_help_center_bottom_sheet,container,false);


    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callUs_btn = view.findViewById(R.id.callUs_btn);
        description_tv = view.findViewById(R.id.description_tv);
        callUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED){
                 mlistner.onItemClick("1234567890",v);
                }else{
                    getActivity().requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                            901);
                }
            }
        });

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ItemClickListener){
            mlistner = (ItemClickListener) context;
        }else {
            throw new RuntimeException(context.toString()+"must implement ItemClickListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mlistner = null;
    }




    public interface ItemClickListener{
        void onItemClick(String item,View view);

    }
}

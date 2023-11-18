package soonflyy.learning.hub.utlis;

import android.text.Editable;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class TextWatcher implements android.text.TextWatcher {

    TextInputLayout inputLayout;
    EditText editText;

    public TextWatcher(EditText editText){
        this.editText = editText;
        addListener();
    }

    private void addListener() {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

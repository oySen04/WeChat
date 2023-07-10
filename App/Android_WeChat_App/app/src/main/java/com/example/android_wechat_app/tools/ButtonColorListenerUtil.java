package com.example.android_wechat_app.tools;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class ButtonColorListenerUtil {

    static IEditTextChangeListener eChangeListener;

    public static void setChangeListener(IEditTextChangeListener changeListener) {
        eChangeListener = changeListener;
    }

    //检测输入框里是否有内容,根据检测结果改变按钮是否可点击
    public static class textChangeListener {
        private Button button;
        private EditText[] editTexts;

        public textChangeListener(Button button) {
            this.button = button;
        }

        public textChangeListener addAllEditText(EditText... editTexts) {
            this.editTexts = editTexts;
            initEditListener();
            return this;
        }

        private void initEditListener() {
            for (EditText editText: editTexts) {
                editText.addTextChangedListener(new textChang());
            }
        }

        //判断edit的输入变化按钮是否可点击
        private class textChang implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkAllEdit()) {
                    //有值
                    eChangeListener.textChange(true);
                    button.setEnabled(true);
                }else {
                    //无值
                    button.setEnabled(false);
                    eChangeListener.textChange(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }
        //检查所有edit是否都输入了数据
        private boolean checkAllEdit() {
            for (EditText e: editTexts) {
                if (!TextUtils.isEmpty(e.getText() + "")) {
                    continue;
                }else {
                    return false;
                }
            }
            return true;
        }
    }
}

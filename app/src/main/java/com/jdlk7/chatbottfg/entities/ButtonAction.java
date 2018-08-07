package com.jdlk7.chatbottfg.entities;

import android.view.View.OnClickListener;

public class ButtonAction extends Action {

    private OnClickListener mOnClickListener = null;

    public ButtonAction(String text, String value) {
        mText = text;
        mValue = value;
        mType = "button";
    }

    public void setActionListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public OnClickListener getActionListener() {
        return mOnClickListener;
    }
}

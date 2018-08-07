package com.jdlk7.chatbottfg;

import android.view.View.OnClickListener;

public class Action {

    private String mText;
    private String mValue;
    private String mType = "button";
    private OnClickListener mOnClickListener = null;

    public Action(String text, String value) {
        mText = text;
        mValue = value;
    }

    public String getText() {
        return mText;
    }

    public String getValue() {
        return mValue;
    }

    public void setOnClickAction(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public OnClickListener getOnClickAction() {
        return mOnClickListener;
    }
}

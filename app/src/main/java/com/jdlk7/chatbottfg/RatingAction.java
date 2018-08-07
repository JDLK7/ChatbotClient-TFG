package com.jdlk7.chatbottfg;

import android.widget.RatingBar.OnRatingBarChangeListener;

public class RatingAction extends Action {

    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public RatingAction(String value) {
        mValue = value;
        mType = "rating";
    }

    public void setActionListener(OnRatingBarChangeListener onRatingBarChangeListener) {
        mOnRatingBarChangeListener = onRatingBarChangeListener;
    }

    public OnRatingBarChangeListener getActionListener() {
        return mOnRatingBarChangeListener;
    }

}

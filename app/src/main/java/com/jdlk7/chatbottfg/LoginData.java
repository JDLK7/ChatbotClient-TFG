package com.jdlk7.chatbottfg;

import android.content.Context;

import com.jdlk7.chatbottfg.entities.User;

import org.json.JSONObject;

public class LoginData {

    private static LoginData mInstance;
    private static Context mContext;
    private SharedPrefManager mSharedPrefManager;
    private User mUser;
    private String mAccessToken;

    private LoginData(Context context) {
        mContext = context;
        mSharedPrefManager = SharedPrefManager.getInstance(context);
    }

    public static LoginData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LoginData(context);
        }

        return mInstance;
    }

    public User getUser() {
        if (mUser == null) {
            mUser = User.fromJson(mSharedPrefManager.getString(SharedPrefManager.Key.USER));
        }

        return mUser;
    }

    public String getAccessToken() {
        if (mAccessToken == null) {
            mAccessToken = mSharedPrefManager.getString(SharedPrefManager.Key.ACCESS_TOKEN);
        }

        return mAccessToken;
    }
}

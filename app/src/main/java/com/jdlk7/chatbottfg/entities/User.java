package com.jdlk7.chatbottfg.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String mId;
    private String mName;
    private String mEmail;
    private String mCreatedAt;
    private String mUpdatedAt;

    public User(String id, String name, String email, String createdAt, String updatedAt) {
        mId = id;
        mName = name;
        mEmail = email;
        mCreatedAt = createdAt;
        mUpdatedAt = updatedAt;
    }

    public static User fromJson(JSONObject jsonUser) {
        try {
            return new User(
                    jsonUser.getString("id"),
                    jsonUser.getString("name"),
                    jsonUser.getString("email"),
                    jsonUser.getString("created_at"),
                    jsonUser.getString("updated_at")
            );
        } catch (JSONException e) {
            return null;
        }
    }

    public static User fromJson(String json) {
        try {
            return fromJson(new JSONObject(json));
        } catch (JSONException e) {
            return null;
        }
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }
}

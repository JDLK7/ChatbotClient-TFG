package com.jdlk7.chatbottfg;

import com.jdlk7.chatbottfg.entities.Action;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private String message;
    private boolean isUser;
    private List<Action> mActions;

    public Message(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        mActions = new ArrayList<Action>();
    }

    public Message(String message, boolean isUser, List<Action> actions) {
        this.message = message;
        this.isUser = isUser;
        mActions = actions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msgText) {
        this.message = msgText;
    }

    public boolean isUserMsg() {
        return isUser;
    }

    public void setIsUserMsg(boolean isUser) {
        this.isUser = isUser;
    }

    public List<Action> getActions() {
        return mActions;
    }

    public boolean hasActions() {
        return mActions.size() != 0;
    }
}

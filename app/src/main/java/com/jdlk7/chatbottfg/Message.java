package com.jdlk7.chatbottfg;

public class Message {

    private String message;
    private boolean isUser;

    public Message(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
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
}

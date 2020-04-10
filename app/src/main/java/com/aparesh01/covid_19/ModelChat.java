package com.aparesh01.covid_19;

public class ModelChat {

    String message,receiver,sender,timestamp;
    boolean isSeen;
    public ModelChat(){

    }

    public ModelChat(String message,String receiver,String sender,String timestamp,boolean isSeen){
        this.isSeen = isSeen;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;

    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiver(){
        return receiver;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public String getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}

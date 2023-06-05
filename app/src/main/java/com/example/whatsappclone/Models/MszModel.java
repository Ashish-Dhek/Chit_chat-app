package com.example.whatsappclone.Models;


public class MszModel {


    private  String messageId ,message,senderId;
    private long timeStamp;
    private int emoji;


    public MszModel() {

    }

    public MszModel(String message, String senderId, long timeStamp,int emoji) {

        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.emoji=emoji;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }
}

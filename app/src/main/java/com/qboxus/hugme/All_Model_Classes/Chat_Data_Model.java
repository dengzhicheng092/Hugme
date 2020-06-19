package com.qboxus.hugme.All_Model_Classes;

public class Chat_Data_Model {

    String msg;
    String receiver_id;
    String sender_id;
    String date_time;


    public Chat_Data_Model() {

    }


    public Chat_Data_Model(String msg, String receiver_id, String sender_id, String date_time) {
        this.msg = msg;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.date_time = date_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}

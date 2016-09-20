package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 15-09-2016.
 */
public class NotificationSPCard {

    public String user_message, generated_datetime, sp_accepted, service_name, reqid, userid;

    public NotificationSPCard(String user_message, String generated_datetime,
                              String sp_accepted, String service_name, String reqid, String userid) {

        this.user_message = user_message;
        this.generated_datetime = generated_datetime;
        this.sp_accepted = sp_accepted;
        this.service_name = service_name;
        this.reqid = reqid;
        this.userid = userid;
    }
}
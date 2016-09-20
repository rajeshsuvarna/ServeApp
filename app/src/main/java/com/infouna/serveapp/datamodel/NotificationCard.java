package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 15-09-2016.
 */

public class NotificationCard {

    public String user_message, user_generated_datetime, user_sp_accepted, user_service_name, user_reqid, user_userid;
    public String sp_message, sp_generated_time, sp_request_from, sp_service_name, sp_spid, sp_reqid;
    public int sp_type;

    public NotificationCard(String user_message, String generated_datetime,
                            String sp_accepted, String service_name, String reqid, String userid) {

        this.user_message = user_message;
        this.user_generated_datetime = generated_datetime;
        this.user_sp_accepted = sp_accepted;
        this.user_service_name = service_name;
        this.user_reqid = reqid;
        this.user_userid = userid;
    }

    public NotificationCard(String sp_message, String sp_generated_time, String sp_request_from,
                            String sp_service_name, String sp_spid, String sp_reqid, int type) {

        this.sp_message = sp_message;
        this.sp_generated_time = sp_generated_time;
        this.sp_request_from = sp_request_from;
        this.sp_service_name = sp_service_name;
        this.sp_spid = sp_spid;
        this.sp_reqid = sp_reqid;
        this.sp_type = type;
    }

}
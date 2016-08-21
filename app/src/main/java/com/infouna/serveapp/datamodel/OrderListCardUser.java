package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 11-08-2016.
 */
public class OrderListCardUser {

    public String reqid,
            service_name,
            location,
            requested_date_time,
            accepted;

    public OrderListCardUser(String reqid, String service_name,
                             String location, String requested_date_time, String accepted) {
        this.reqid = reqid;
        this.service_name = service_name;
        this.location = location;
        this.requested_date_time = requested_date_time;
        this.accepted = accepted;
    }
}
package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 11-08-2016.
 */
public class OrderListCardSP {

    public String reqid,
            spid,
            service_name,
            location,
            username,
            requested_date_time,
            accepted;

    public OrderListCardSP(String reqid, String spid, String service_name,
                           String location, String username, String requested_date_time, String accepted) {
        this.reqid = reqid;
        this.spid = spid;
        this.service_name = service_name;
        this.location = location;
        this.username = username;
        this.requested_date_time = requested_date_time;
        this.accepted = accepted;

    }
}
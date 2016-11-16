package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 24-08-2016.
 */
public class ServiceListCard {

    public String userid;
    public String service_providerid;
    public String service_name;
    public String sub_service_name;
    public String banner_picture;
    public String service_tag;
    public String service_location;
    public String service_price;
    public String confirmed;
    public String total_ratings;
    public String total_reviews;
    public String favourite;
    public String service_title;



    public ServiceListCard(String userid, String service_providerid, String service_name,String service_title, String sub_service_name,
                           String banner_picture,
                           String confirmed, String total_ratings, String total_reviews,
                           String favourite) {
        this.userid = userid;
        this.service_providerid = service_providerid;
        this.service_name = service_name;
        this.sub_service_name = sub_service_name;
        this.banner_picture = banner_picture;
        this.confirmed = confirmed;
        this.total_ratings = total_ratings;
        this.total_reviews = total_reviews;
        this.favourite = favourite;
        this.service_title = service_title;
    }
}


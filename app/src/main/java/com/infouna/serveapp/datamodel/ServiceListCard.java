package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 24-08-2016.
 */
public class ServiceListCard {

    public String
            userid,
            service_providerid,
            service_name,
            banner_picture,
            service_tag,
            service_location,
            service_price,
            confirmed,
            total_ratings,
            total_reviews,
            favourite;

    public ServiceListCard(String userid, String service_providerid, String service_name,
                           String banner_picture, String service_tag, String service_location,
                           String service_price, String confirmed, String total_ratings, String total_reviews, String favourite) {
        this.userid = userid;
        this.service_providerid = service_providerid;
        this.service_name = service_name;
        this.banner_picture = banner_picture;
        this.service_tag = service_tag;
        this.service_location = service_location;
        this.service_price = service_price;
        this.confirmed = confirmed;
        this.total_ratings = total_ratings;
        this.total_reviews = total_reviews;
        this.favourite = favourite;
    }
}

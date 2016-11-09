package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 06-08-2016.
 */
public class ServiceProfile {

    public String first_name, last_name, email, profile_pic, address, logo_path,
            shop_photos, website, location, service_id, service_name, sub_service_name, tags, service_price;

    public ServiceProfile( String email,
                          String profile_pic, String address, String logo_path,
                          String shop_photos, String website, String location,
                          String service_id, String service_name, String sub_service_name,
                          String tags, String service_price) {

        this.email = email;
        this.profile_pic = profile_pic;
        this.address = address;
        this.logo_path = logo_path;
        this.shop_photos = shop_photos;
        this.website = website;
        this.location = location;
        this.service_id = service_id;
        this.service_name = service_name;
        this.sub_service_name = sub_service_name;
        this.tags = tags;
        this.service_price = service_price;
    }

}

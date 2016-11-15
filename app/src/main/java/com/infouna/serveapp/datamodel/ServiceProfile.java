package com.infouna.serveapp.datamodel;

/**
 * Created by Darshan on 06-08-2016.
 */
public class ServiceProfile {

    public String service_address,service_banner,service_shop_photo,service_website,service_location,service_id,service_name,sub_service_name,service_title,service_price,service_description;

    public ServiceProfile(String service_address, String service_banner, String service_shop_photo, String service_website,String service_location, String service_id,String service_name, String sub_service_name, String service_title, String service_price, String service_description) {


        this.service_address = service_address;
        this.service_banner = service_banner;
        this.service_shop_photo = service_shop_photo;
        this.service_website = service_website;
        this.service_location = service_location;
        this.service_id = service_id;
        this.service_name = service_name;
        this.sub_service_name = sub_service_name;
        this.service_price = service_price;
        this.service_title = service_title;
        this.service_description = service_description;
    }
}
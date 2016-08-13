package com.infouna.serveapp.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshan on 14-06-2016.
 */
public class HomeCardData {

    public int service_id;
    public String servicename;
    public String service_image_url;
    public int has_sub_service;

    public HomeCardData() {

    }

    public HomeCardData(int service_id, String servicename, String service_image_url, int has_sub_service) {
        this.service_id = service_id;
        this.servicename = servicename;
        this.service_image_url = service_image_url;
        this.has_sub_service = has_sub_service;
    }

}

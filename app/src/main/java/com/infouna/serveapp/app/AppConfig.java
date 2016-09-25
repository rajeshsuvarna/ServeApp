package com.infouna.serveapp.app;

public class AppConfig {

    //Key
    public static String KEY = "fd0e5f476a68c73bba35f3ee71ff3b4a";

    // Server user login url
    public static String URL_LOGIN = "http://www.serveapp.in/assets/api/login.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&mobile=";

    // Server user register url
    public static String URL_REGISTER = "http://www.serveapp.in/assets/api/registeruser.php";

    // Server user register url
    public static String URL_CHECK_NUMBER = "http://www.serveapp.in/assets/api/checknumber.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&mobileno=";

    // Dashboard services / categories home page
    public static String URL_DASHBOARD_SERVICES_HOME = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=dashboard_services";

    // Dashboard sub services
    public static String URL_SUB_SEVICES_HOME = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=dashboard_sub_service";

    //Rate service'
    public static String URL_RATE_SERVICE = "http://www.serveapp.in/assets/api/rate_service.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";

    // URL_REPORT_SERVICE
    public static String URL_REPORT_SERVICE = "http://www.serveapp.com/assets/api/user_report.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";

    // URL_GET_SERVICE_PROFILE
    public static String URL_GET_SERVICE_PROFILE = "http://www.serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=sp_profile&userid=";

    //Add my profile
    public static String URL_ADD_SERVICE = "";

    //URL_ORDER_LISTING_USER
    public static String ORDER_LISTING_USER = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=myorders&userid= ";

    // Order details user
    public static String ORDER_DETAILS_USER = "http://www.serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=order_details";

    // Cancel service request
    public static String CANCEL_SERVICE_REQUEST = "http://www.serveapp.com/assets/api/cancle_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";

    // Order listing service provider
    public static String ORDER_LISTING_SP = "http://www.serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=sp_orders&spid=";

    //Order details Service Provider
    public static String ORDER_DETAILS_SP = "http://serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act= sp_orders_details";

    // Accept service request
    public static String ACCEPT_SERVICE_REQUEST = "http://www.serveapp.in/assets/api/accept_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";

    // decline request SP
    public static String DECLINE_SERVICE_REQUEST_SP = "http://www.serveapp.in/assets/api/accept_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";

    //Service listing URL
    public static String SERVICE_LISTING_URL = "http://www.serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=search&keyword=";

    //Service details
    public static String SERVICE_DETAILS_URL = "http://www.serveapp.in/assets/api/getData.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act= service_details&";

    //Check favourite service listing
    public static String CHECK_FAVOURITE = "http://www.serveapp.in/assets/api/ accept_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=check_user_favourites";

    //User notification
    public static String NOTIFICATION_USER = "http://www.serveapp.in/assets/api/ accept_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=get_user_notification&userid=";

    //SP notification
    public static String NOTIFICATION_SP = "http://www.serveapp.in/assets/api/ accept_request.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a&act=get_sp_notification&spid=";

    //Request Service
    public static String SERVICE_REQUEST = "http://www.serveapp.in/assets/api/request_service.php?key=fd0e5f476a68c73bba35f3ee71ff3b4a";
}
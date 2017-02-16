package com.sparken.parking.constant;

/**
 * Created by Anil Sharma on 1/2/16.
 */
public interface IUrls {
    String BASE_URL = "http://103.229.5.131/parking_app/web-service/";
    String URL_LOGIN = BASE_URL + "Login.php";
    String URL_UPDATE_PARKING = BASE_URL + "updateParkingDetails.php";
    //String BASE_URL = "http://192.168.0.64/hotel_system_webservices/";
    String URL_ADD_PARKING = BASE_URL + "insertParkingDetails.php";
    String URL_GET_CAPACITY = BASE_URL + "getCapacityCount.php";
    String URL_SEARCH_VEHI = BASE_URL + "searchVehicleNo.php";
    String URL_SEARCH_MOBILE = BASE_URL + "searchMobileNo.php";
    String URL_CHANGE_PASS = BASE_URL + "changePassword.php";

}

package com.sparken.parking.constant;

/**
 * Created by root on 16/7/16.
 */
public interface VolleyResponseListener<T> {


    void onResponse(T[] object,String message);

    void onError(String message);


    interface PostResponse {

        void onResponse(String id);

        void onError(String message);

    }

}

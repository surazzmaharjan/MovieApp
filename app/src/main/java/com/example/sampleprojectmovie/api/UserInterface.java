package com.example.sampleprojectmovie.api;

import com.example.sampleprojectmovie.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserInterface {


    /**
     * This method is to create user record
     *
     */

    @POST("register")
    Call<Void> addUser(@Body User user);

    /**
     * This method to login
     *
     */

    @FormUrlEncoded
    @POST("userlogin")
    Call<Void> loginUser(@Field("email")String e, @Field("password") String p);

}

package com.example.sampleprojectmovie;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class Retro {

//    private static final String BASE_URL = "http://172.26.8.184:4000/";
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    private static final String IMG_URL = "http://10.0.2.2:3000/";

    public static String token="Bearer ";

    public static Retrofit getInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit;
    }

}

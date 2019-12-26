package com.example.sampleprojectmovie;

import com.example.sampleprojectmovie.api.UserInterface;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class LoginTest {

    @Test
    public void testLogin(){


        UserInterface fb = Retro.getInstance().create(UserInterface.class);

        Call<Void> userCall = fb.loginUser("class@gmail.com","65465");


        try{
            Response<Void> response = userCall.execute();
            assertTrue(response.isSuccessful());

        }catch(IOException e){
        e.printStackTrace();


        }

    }
}

package com.example.sampleprojectmovie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sampleprojectmovie.R;
import com.example.sampleprojectmovie.api.UserInterface;
import com.example.sampleprojectmovie.model.User;
import com.example.sampleprojectmovie.validation.InputValidation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Retrofit retrofit;
    UserInterface userInterface;

    private NestedScrollView nestedScrollView;


    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;



    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getInstance();
        initViews();
        setListeners();
    }

    private void getInstance(){

//        http://sujitg.com.np/api/
//        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:4000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);
    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutName =findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword =  findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail =findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword =  findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink =  findViewById(R.id.appCompatTextViewLoginLink);
        appCompatTextViewLoginLink.setPaintFlags(appCompatTextViewLoginLink.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);





    }



    @Override
    public void onClick(View view) {
        if (R.id.appCompatButtonRegister == view.getId()) {


                String n =textInputEditTextName.getText().toString();
                String e =textInputEditTextEmail.getText().toString();
                String p =textInputEditTextPassword.getText().toString();
                User ur = new User(n,e,p);
//                addUserInt(ur);
               addUserDataToApi(ur);

        }

        if(R.id.appCompatTextViewLoginLink == view.getId()){
            finish();

        }
    }

    private void addUserInt(User user){

        Call<Void> addU = userInterface.addUser(user);

        addU.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {


                if (response.isSuccessful()) {

                    // Snack Bar to show success message that record saved successfully
                    Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
                    emptyInputEditText();

                } else {

                    switch (response.code()) {
                        case 500:
                            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();

                            break;

                    }
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Ex",t.getMessage());

            }
        });
    }


    // Set Listeners
    private void setListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        inputValidation = new InputValidation(this);
    }

    private void addUserDataToApi(User user) {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        addUserInt(user);



    }

        /**
         * This method is to empty all input edit text
         */
        private void emptyInputEditText() {
            textInputEditTextName.setText(null);
            textInputEditTextEmail.setText(null);
            textInputEditTextPassword.setText(null);
            textInputEditTextConfirmPassword.setText(null);
        }
}

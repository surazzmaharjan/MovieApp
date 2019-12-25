package com.example.sampleprojectmovie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.example.sampleprojectmovie.R;
import com.example.sampleprojectmovie.api.UserInterface;
import com.example.sampleprojectmovie.database.DatabaseHelper;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Retrofit retrofit;
    private UserInterface userInterface;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatCheckBox appCompatCheckBox;

    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;




    private final AppCompatActivity activity = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);
        initViews();
        setListeners();
        getInstance();
    }


    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        appCompatCheckBox = findViewById(R.id.show_hide_password);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
        textViewLinkRegister.setPaintFlags(textViewLinkRegister.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatButtonLogin:
                String e =textInputEditTextEmail.getText().toString();
                String p =textInputEditTextPassword.getText().toString();
                verifyDataFromApi(e,p);
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    // Set Listeners
    private void setListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        appCompatCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            appCompatCheckBox.setText("Hide Password");// change
                            // checkbox
                            // text

                            textInputEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            textInputEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            appCompatCheckBox.setText("Show Password");// change
                            // checkbox
                            // text

                            textInputEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });

    }


    private void getInstance(){


        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);
    }


    private void loginUser(String email,String password){
        Call<Void> loginU = userInterface.loginUser(email,password);

        loginU.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Intent accountsIntent = new Intent(activity, NavigationActivity.class);
                    accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
                    emptyInputEditText();
                    startActivity(accountsIntent);


                } else {

                    switch (response.code()) {
                        case 300:
                            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
                            break;

                        case 200:
                            Snackbar.make(nestedScrollView, "Password Invalid", Snackbar.LENGTH_LONG).show();
                            break;
                        case 400:
                            Snackbar.make(nestedScrollView, "Email doesn't exist", Snackbar.LENGTH_LONG).show();
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

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyDataFromApi(String em,String pas) {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail,
                getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        loginUser(em,pas);

//        if (databaseHelper.checkUserByEmailPassword(textInputEditTextEmail.getText().toString().trim()
//                , textInputEditTextPassword.getText().toString().trim())) {
//
//
//            Intent accountsIntent = new Intent(activity, NavigationActivity.class);
//            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
//            emptyInputEditText();
//            startActivity(accountsIntent);
//
//
//        } else {
//            // Snack Bar to show success message that record is wrong
//            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
//        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}

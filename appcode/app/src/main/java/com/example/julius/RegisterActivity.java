package com.example.julius;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.julius.R;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName,editTextEmail,editTextPassword,editTextPasswordConfirm,editTextDate;
    Button buttonSignUp, buttonSignIn;
    String gender;
    String language;
    RadioGroup radioGroup,radioGroupLang;
    RadioButton radioButtonMale, radioButtonFemale,radioButtonJava,radioButtonC,radioButtonPython;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonSignUp = findViewById(R.id.btnSignUp);
        buttonSignIn = findViewById(R.id.btnSignIn);

        editTextName = findViewById(R.id.firstnameTxt);
        editTextEmail = findViewById(R.id.emailTxt);
        editTextPassword = findViewById(R.id.passwordTxt);
        editTextPasswordConfirm = findViewById(R.id.cpasswordTxt);
        editTextDate = findViewById(R.id.dateTxt);

        //Initialize radioGroups
        radioGroup = findViewById(R.id.radioGroup);
        radioGroupLang = findViewById(R.id.radioGroupLang);

        radioButtonMale = findViewById(R.id.radioMale);
        radioButtonFemale = findViewById(R.id.radioFemale);
        radioButtonJava = findViewById(R.id.radioJava);
        radioButtonC = findViewById(R.id.radioC);
        radioButtonPython = findViewById(R.id.radioPython);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioMale:
                       gender = getString(R.string.male_option);
                       break;
                    case R.id.radioFemale:
                        gender = getString(R.string.female_option);
                        break;
                }
            }
        });

        radioGroupLang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioJava:
                        language = getString(R.string.java_option);
                        break;
                    case R.id.radioC:
                        language = getString(R.string.c_option);
                        break;
                    case R.id.radioPython:
                        language = getString(R.string.python_option);
                        break;
                }
            }
        });

        //user registration
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String cpassword = editTextPasswordConfirm.getText().toString().trim();

                if(firstname.isEmpty() || date.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this,
                            getString(R.string.enter_all_credentials),
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(password.matches(cpassword)){
                        //This is where we will make our request
                        Register(firstname,email,gender,language,date,password);
                    }else{
                        Toast.makeText(RegisterActivity.this,
                                getString(R.string.password_no_match),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        //Opens intent to Login activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //Registers user to the db
    private void Register(final String firstname,
                          final String email,
                          final String gender,
                          final String language,
                          final String dob,
                          final String password){
        Connector.connect(AppConfig.URL_REGISTER);

        //tag used to add request to queue and cancel request
        String tag_request = getString(R.string.request_register_string);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(RegisterActivity.this,
                                s,
                                Toast.LENGTH_SHORT).show();
                        if(s.matches(getString(R.string.success_string))){
                            Intent intent = new Intent(RegisterActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }},
                            new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put(getString(R.string.db_column_language), language);
                            params.put(getString(R.string.db_column_dob), dob);
                            params.put(getString(R.string.db_column_email), email);
                            params.put(getString(R.string.db_column_password), password);
                            params.put(getString(R.string.db_column_gender), gender);
                            params.put(getString(R.string.db_column_firstname), firstname);
                            return params;
                        }
                    };

        //Add request to queue
        AppController.getInstance(this).
                addToRequestQueue(stringRequest,tag_request);
    }
}

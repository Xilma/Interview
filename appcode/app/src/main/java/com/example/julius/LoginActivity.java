package com.example.julius;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.julius.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button buttonLogin,buttonSignUp;
    private EditText editTextEmail,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.btnSignIn);
        buttonSignUp = findViewById(R.id.btnRegister);
        editTextEmail = findViewById(R.id.emailTxt);
        editTextPassword = findViewById(R.id.passwordTxt);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.enter_all_credentials),
                            Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.prompt_enter_email),
                            Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.prompt_enter_password),
                            Toast.LENGTH_SHORT).show();
                }else{
                    //Make db request
                    try {
                        Login(email,password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login(final String email, final String password) throws IOException {
        Connector.connect(AppConfig.URL_LOGIN);

        //tag used to add request to queue and cancel request
        String tag_request = getString(R.string.request_login_string);
        StringRequest  stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(LoginActivity.this,
                                s,
                                Toast.LENGTH_SHORT).show();
                        if(s.matches("success")){
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(getString(R.string.db_column_password), password);
                params.put(getString(R.string.db_column_email), email);
                return params;
            }
        };

        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_request);
    }
}

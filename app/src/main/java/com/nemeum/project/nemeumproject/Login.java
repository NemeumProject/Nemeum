package com.nemeum.project.nemeumproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import url.UrlServer;

public class Login extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn = (Button) findViewById(R.id.button0);
        Button register_btn = (Button) findViewById(R.id.button1);
        TextView Forgotpass_btn = findViewById(R.id.forgetpassbtn);

        Email = (EditText)findViewById(R.id.EmailSpace);
        Password = (EditText)findViewById(R.id.PasswordSpace);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LoginValidation(Email.getText().toString(), Password.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        final TextView Forgetpass = findViewById(R.id.forgetpassbtn);
        String forgetpasstext0 = "Forgot Password?";
        SpannableString forgetpasstext1 = new SpannableString(forgetpasstext0);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                openForgetPassActivity();
            }
        };

        forgetpasstext1.setSpan(clickableSpan,0,16,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Forgetpass.setText(forgetpasstext1);
        Forgetpass.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void openRegisterActivity(){
        Intent intent2 = new Intent(this,RegisterActivity.class);
        startActivity(intent2);
    }

    private void LoginValidation(String Email, String UserPassword) throws IOException, JSONException {
        final String email = Email;
        final String password = UserPassword;
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UrlServer.url + "/login");

                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email);
                    postData.put("password", password);

                    StringEntity se = null;
                    se = new StringEntity(postData.toString());
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");
                    httppost.setEntity(se);
                    HttpResponse response = httpclient.execute(httppost);
                    if(response.getStatusLine().getStatusCode() == 200){
                        Intent intent1 = new Intent(Login.this, UserLoginActivity.class);
                        startActivity(intent1);
                    }else{
                        Login.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Login.this,"Your Input Email or Input Password is incorrect, Please Try Again!",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openForgetPassActivity(){
        Intent intent3 = new Intent(this,ForgetPassActivity.class);
        startActivity( intent3);
    }
}

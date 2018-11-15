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
                LoginValidation(Email.getText().toString(), Password.getText().toString());
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

    private void LoginValidation(String UserEmail,String UserPassword) {
        if((UserEmail.equals("admin")) && (UserEmail.equals("admin"))){
            Intent intent1 = new Intent(this, UserActivity.class);
            startActivity(intent1);
        }
        else{
            Toast.makeText(Login.this,"Your Input Email or Input Password is incorrect, Please Try Again!",Toast.LENGTH_LONG).show();
        }
    }

    private void openForgetPassActivity(){
        Intent intent3 = new Intent(this,ForgetPassActivity.class);
        startActivity( intent3);
    }

}

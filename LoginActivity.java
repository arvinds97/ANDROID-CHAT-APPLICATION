package com.arvindsudheer.letschat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;
    private Button LoginButton,  PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView   NeedNewAccountLink, ForgotPassowrdLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        InitializeFields();

        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();

            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });
        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneLoginIntent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
                startActivity(phoneLoginIntent);
            }
        });
    }

    private void AllowUserToLogin() {
        String email=UserEmail.getText().toString();
        String password=UserPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter an email", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter a password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Sign In");
            loadingBar.setMessage(" Only a little while ..)");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            SendUserToMainActivity();
                            Toast.makeText(LoginActivity.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this,"Error"+message , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
        }
    }

    public void InitializeFields() {

        LoginButton = (Button)findViewById(R.id.login_button);
        PhoneLoginButton = (Button)findViewById(R.id.phone_login_button);
        UserEmail= (EditText) findViewById(R.id.login_email);
        UserPassword= (EditText)findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        ForgotPassowrdLink = (TextView)findViewById(R.id.forgot_password_link);
        loadingBar = new ProgressDialog(this);


    }



    public void SendUserToMainActivity() {
        Intent mainIntent;
        mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainIntent);
        finish();

    }
    private void SendUserToRegisterActivity() {
     Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
       System.out.println("Reached!");
       startActivity(registerIntent);
   }



}
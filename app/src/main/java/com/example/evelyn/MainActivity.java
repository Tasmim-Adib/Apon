package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText loginEmailEditText,loginPasswordEditText;
    private Button loginButton;
    private TextView needAccountTextView,have_an_account;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            sendUsertoFirstActivity();
        }

        loginEmailEditText = (EditText)findViewById(R.id.Login_email_text);
        loginPasswordEditText = (EditText)findViewById(R.id.Login_password_text);
        loginButton = (Button)findViewById(R.id.login_button);
        needAccountTextView = (TextView)findViewById(R.id.log_textview_id);
        have_an_account = (TextView)findViewById(R.id.textView2);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        needAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignUpActivity();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedIn();
            }
        });
    }

    private void loggedIn() {
        String email = loginEmailEditText.getText().toString();
        String password = loginPasswordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            loginEmailEditText.setError("please enter an valid email");
            loginEmailEditText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            loginPasswordEditText.setError("please give the valid passsword");
            loginPasswordEditText.requestFocus();
        }

        else{
            loadingBar.setTitle("Log In");
            loadingBar.setMessage("Please Wait...");

            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUsertoFirstActivity();
                                Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                               String message = task.getException().toString();
                                Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }


                        }
                    });
        }
    }

    private void sendUsertoFirstActivity() {
        Intent ActualIntent = new Intent(getApplicationContext(),FirstActivity.class);
        startActivity(ActualIntent);
    }

    private void sendUserToSignUpActivity() {
        Intent signUpIntent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(signUpIntent);
    }
}

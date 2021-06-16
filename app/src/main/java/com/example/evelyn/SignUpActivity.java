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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmailEditText,signupPasswordEditText;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpEmailEditText = (EditText)findViewById(R.id.Signup_email_text);
        signupPasswordEditText = (EditText)findViewById(R.id.Signup_password_text);

        signupButton = (Button)findViewById(R.id.Signup_button);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        String email = signUpEmailEditText.getText().toString();
        String password = signupPasswordEditText.getText().toString();


        if(TextUtils.isEmpty(email)){
            signUpEmailEditText.requestFocus();
            signUpEmailEditText.setError("please enter an email");
            return;
        }
        if(TextUtils.isEmpty(password)){
            signupPasswordEditText.requestFocus();
            signupPasswordEditText.setError("please give a password");
            return;
        }

        else{

            loadingBar.setTitle("Sign Up");
            loadingBar.setMessage("Please wait, while your account is creating...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUsertoActualActivity();
                                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }
    }

    private void sendUsertoActualActivity() {
        Intent actualIntent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(actualIntent);
    }
}

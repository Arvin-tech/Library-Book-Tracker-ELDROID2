package com.example.saguisa_librarybooktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {
    EditText email, password;
    Button login;
    TextView signUp;
    FirebaseAuth mAuth;
    String inputtedEmail, inputtedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        //references to ui elements
        findViews();

        //click login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputtedEmail = email.getText().toString();
                inputtedPassword = password.getText().toString();
                if(!inputtedEmail.isEmpty() && !inputtedPassword.isEmpty()){
                    login(); //invoke method
                }else if(inputtedEmail.isEmpty()){
                    email.requestFocus();
                    email.setError("Please enter an email");
                }
                else if(inputtedPassword.isEmpty()){
                    password.requestFocus();
                    password.setError("Please enter a password");
                }
            }
        });

        //click sign up text view
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpActivity(); //proceed to sign up
            }
        });
    }

    public void findViews(){
        email = findViewById(R.id.emailEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        login = findViewById(R.id.buttonLogin);
        signUp = findViewById(R.id.textViewSignup);
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(inputtedEmail, inputtedPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show();
                            homeActivity(); //open homepage or dashboard
                            finish(); //close login activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void homeActivity(){
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent); //proceed to homepage
    }

    private void signUpActivity(){
        Intent intent = new Intent(getApplication(),Signup.class); //click sign up textview
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            homeActivity();
        }
    }

}
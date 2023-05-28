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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {
    EditText email, password;
    Button signUp;
    TextView login;
    FirebaseFirestore database;
    FirebaseAuth mAuth;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize fire store
        database = FirebaseFirestore.getInstance();

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

        //reference to ui elements
        findViews();

        //click
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputtedEmail = email.getText().toString(); //holds inputted email @ xml
                String inputtedPass = password.getText().toString(); //holds inputted password @ xml
                //validations
                if(!inputtedEmail.isEmpty() || !inputtedPass.isEmpty()){
                    createUser(inputtedEmail, inputtedPass); //invoke method, pass declared variables
                }
                else if(inputtedEmail.isEmpty()){
                    email.requestFocus();
                    email.setError("Please enter an email");
                }
                else if(inputtedPass.isEmpty()){
                    password.requestFocus();
                    password.setError("Please enter a password");
                }
            }
        });

        //click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity(); //invoke method
            }
        });
    }

    //create account using firebase auth
    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Signup.this, "Successfully Created!", Toast.LENGTH_SHORT).show();
                    loginActivity(); //proceed to login
                    finish(); //close registration activity
                }
                else{
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void findViews(){
        email = findViewById(R.id.emailEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        signUp = findViewById(R.id.buttonSignup);
        login = findViewById(R.id.textViewLogin);
    }

    private void loginActivity(){
        Intent intent = new Intent(getApplication(), Login.class); //already have an account?
        startActivity(intent);
    }
}
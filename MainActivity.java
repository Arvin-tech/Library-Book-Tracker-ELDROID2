package com.example.saguisa_librarybooktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    final String TAG = "FIRESTORE";
    EditText searchCode, bookCode, numDays, title, author, totalPrice, outputPrice;
    Button borrow;
    FirebaseFirestore database;
    FirebaseUser currentUser;
    TextView logoutTextView;
    String inputtedCode, inputtedNumOfDays;
    String updatedPrice;
    CollectionReference regularBooksRef, premiumBooksRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize fire store
        database = FirebaseFirestore.getInstance();

        //instance of logged in user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //reference to fire store regular books collection
        regularBooksRef = database.collection("RegularBooks");

        //reference to fire store premium books collection
        premiumBooksRef = database.collection("PremiumBooks");

        //reference to ui elements
        findViews();

        String email = currentUser.getEmail(); //get email of current user
        String logoutText = "Logout " + email; //set logout text and concatenate email

        //if logged out on start of the app
        if(currentUser == null){
            loginActivity();
            finish();
        }
        else{
            logoutTextView.setText(logoutText); //display
        }

        //search code edit text listener for auto searching (if not using borrow button to query data)
        searchCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputtedCode = s.toString();
                if (inputtedCode.isEmpty()) {
                    clearEditTxt(); //clear edit text values
                } else {
                    searchBook(inputtedCode);
                }
            }
        });

        //book code edit text listener
        bookCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputtedCode = s.toString();
                if (inputtedCode.isEmpty()) {
                    clearEditTxt(); //clear edit text values
                }
            }
        });


        //click
        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputtedCode = bookCode.getText().toString(); //holds inputted book code @ xml
                inputtedNumOfDays = numDays.getText().toString(); //holds inputted num of days @

                if (inputtedCode.isEmpty() || inputtedNumOfDays.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Please enter book code and number of days borrowed.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(inputtedCode.startsWith("R")){
                        borrowRegularBook(); //invoke
                    }
                    else if (inputtedCode.startsWith("P")) {
                        borrowPremiumBook(); //invoke
                    }
                }
            }
        });

        //click
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //logout user
                loginActivity();
                finish();
            }
        });
    }

    private void findViews(){
        searchCode = findViewById(R.id.editTextSearchBookCode);
        bookCode = findViewById(R.id.editTextBookCode);
        numDays = findViewById(R.id.editTextNumDays);
        title = findViewById(R.id.editTextTitle);
        author = findViewById(R.id.editTextAuthor);
        totalPrice = findViewById(R.id.editTextTotalPrice);
        borrow = findViewById(R.id.buttonBorrow);
        logoutTextView = findViewById(R.id.textViewLogout);
        outputPrice = findViewById(R.id.editTextOutputPrice);
    }

    //query data from fire store and display in edit text
    private void searchBook(String inputtedCode) {
        if(inputtedCode.startsWith("P")){
            Query query = premiumBooksRef.whereEqualTo("bookCode", inputtedCode);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String queriedTitle = documentSnapshot.getString("bookTitle");
                        String queriedAuthor = documentSnapshot.getString("bookAuthor");
                        double queriedPrice = documentSnapshot.getDouble("bookPrice");
                        // Display the retrieved data in the EditText fields
                        title.setText(queriedTitle);
                        author.setText(queriedAuthor);
                        totalPrice.setText(String.valueOf(queriedPrice));
                    } else {
                        Toast.makeText(MainActivity.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error searching for book", Toast.LENGTH_SHORT).show();
                }
            });
        } else if(inputtedCode.startsWith("R")){
            Query query = regularBooksRef.whereEqualTo("bookCode", inputtedCode);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String queriedTitle = documentSnapshot.getString("bookTitle");
                        String queriedAuthor = documentSnapshot.getString("bookAuthor");
                        double queriedPrice = documentSnapshot.getDouble("bookPrice");
                        // Display the retrieved data in the EditText fields
                        title.setText(queriedTitle);
                        author.setText(queriedAuthor);
                        totalPrice.setText(String.valueOf(queriedPrice));
                    } else {
                        Toast.makeText(MainActivity.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error searching for book", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid book code!", Toast.LENGTH_SHORT).show();
        }
    }
    //if the inputted book code in edit text starts with p then call the borrowPremiumBook() method
    private void borrowRegularBook(){
          borrow.setOnClickListener(new View.OnClickListener(){
                      @Override
                      public void onClick(View view){
                         database.collection("RegularBooks")
                                  .whereEqualTo("bookCode", inputtedCode)
                                  .get()
                                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                          if (task.isSuccessful()) {
                                              for (QueryDocumentSnapshot document : task.getResult()) {
                                                  String queriedBookTitle = document.get("bookTitle").toString();
                                                  String queriedAuthor = document.get("bookAuthor").toString();
                                                  double queriedPrice = document.getDouble("bookPrice");
                                                  int numOfDays = Integer.parseInt(inputtedNumOfDays);

                                                  //regular book object
                                                  RegularBook regularBook = new RegularBook(inputtedCode, queriedBookTitle, queriedAuthor, numOfDays, true);
                                                  double totalPriceValue = regularBook.computeCost();
                                                  outputPrice.setText(String.valueOf(totalPriceValue)); //display total borrow price according to num of days borrowed

                                                  //display all queried data from fire store in an edittext
                                                  title.setText(queriedBookTitle);
                                                  author.setText(queriedAuthor);
                                                  totalPrice.setText(String.valueOf(queriedPrice));

                                              }
                                          } else {
                                              Log.d(TAG, "Error getting documents: ", task.getException());
                                          }
                                      }
                                  });
                      }
          });

    }
    private void borrowPremiumBook(){
        database.collection("PremiumBooks")
                .whereEqualTo("bookCode", inputtedCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //store the queried data from firebase inside these variables
                                String queriedBookTitle = document.get("bookTitle").toString();
                                String queriedAuthor = document.get("bookAuthor").toString();
                                double queriedPrice = document.getDouble("bookPrice");
                                int numOfDays = Integer.parseInt(inputtedNumOfDays);

                                PremiumBook premiumBook = new PremiumBook(inputtedCode, queriedBookTitle, queriedAuthor, numOfDays, true);
                                double totalPriceValue = premiumBook.computeCost();
                                outputPrice.setText(String.valueOf(totalPriceValue)); //display total borrow price according to num of days borrowed

                                //display all queried data from fire store in an edittext
                                title.setText(queriedBookTitle);
                                author.setText(queriedAuthor);
                                totalPrice.setText(String.valueOf(queriedPrice));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void clearEditTxt(){
        //Clear EditText fields
        numDays.setText("");
        title.setText("");
        author.setText("");
        totalPrice.setText("");
    }
    private void loginActivity(){
        Intent intent = new Intent(getApplication(), Login.class); //already have an account?
        startActivity(intent);
    }
}
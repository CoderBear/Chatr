package com.udemy.sbsapps.chatr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersDB = database.getReference();

    EditText passwordET, emailET;
    TextView loginTV;
    Button signupButton;
    boolean isLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginTV = findViewById(R.id.loginTextView);
        signupButton = findViewById(R.id.loginButton);

        currentUser = auth.getCurrentUser();
    }

    public void signInClicked(View view){
        Log.i("MainActivity", "signClicked Pressed");

        if(currentUser == null && !isLogin) {
            auth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.i("createUserWithEmail: ", "succesful");
                                currentUser = auth.getCurrentUser();
                                usersDB.child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailET.getText().toString());
                                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                                startActivity(intent);
                            } else {
                                Log.i("createUserWithEmail: ", "failed");
                                Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            auth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.i("signInWithEmail: ", "succesful");
                                currentUser = auth.getCurrentUser();
                                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                                startActivity(intent);
                            } else {
                                Log.i("signInWithEmail: ", "failed");
                                Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void loginTextClicked(View view) {
        if(isLogin) {
            signupButton.setText("Sign Up");
            loginTV.setText("Login");
            isLogin = false;
        } else {
            signupButton.setText("Login");
            loginTV.setText("or, Sign Up");
            isLogin = true;
        }
    }
}

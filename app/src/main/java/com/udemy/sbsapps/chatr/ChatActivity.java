package com.udemy.sbsapps.chatr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String activeUser = "";
    EditText chatEditText;
    ListView chatListView;
    List<String> messages = new ArrayList<>();
    ArrayAdapter<String> adapter;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase chatDB = FirebaseDatabase.getInstance();
    DatabaseReference userRef = chatDB.getReference().child("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        activeUser = intent.getStringExtra("username");

        setTitle( "Chat with " + activeUser);

        chatEditText = findViewById(R.id.chatEditText);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);

        
    }

    public void sendChat(View view) {

        userRef.child(user.getEmail()).child("recipient").setValue(activeUser);
        userRef.child(user.getEmail()).child("recipient").child("message").setValue(chatEditText.getText().toString());
    }
}

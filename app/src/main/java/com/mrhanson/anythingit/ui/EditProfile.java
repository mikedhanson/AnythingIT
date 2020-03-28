package com.mrhanson.anythingit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mrhanson.anythingit.MainActivity;
import com.mrhanson.anythingit.Models.Ticket;
import com.mrhanson.anythingit.R;
import com.mrhanson.anythingit.SignInActivity;
import com.mrhanson.anythingit.Ticket.EditTicket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EditProfile extends AppCompatActivity {

    private TextView lbl_title;
    private TextView lbl_details;
    private TextView lbl_saved_at;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_profile);

        FirebaseAuth mFirebaseAuth;
        final FirebaseUser mFirebaseUser;
        String mUsername;
        final String ANONYMOUS = "anonymous";

        // Initialize Firebase Auth
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

        EditText txt_title = findViewById(R.id.txt_ticket_title);
        EditText txt_details = findViewById(R.id.txt_ticket_details);


        FloatingActionButton fab = findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View view){
                   Toast toast = Toast.makeText(getApplicationContext(), "An error occurred while opening the ticket. Error: " , Toast.LENGTH_LONG);
                   toast.show();

               }
            });
        }
    }
}


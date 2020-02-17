package com.mrhanson.anythingit.Ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mrhanson.anythingit.Models.Ticket;
import com.mrhanson.anythingit.R;
import com.mrhanson.anythingit.SignInActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTicket extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tickets);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Not signed in, launch the Sign In activity
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

        FloatingActionButton fab = findViewById(R.id.fab_save);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgressBar pb = findViewById(R.id.prg_saving);
                    EditText txt_ticket_title = findViewById(R.id.txt_ticket_title);
                    EditText txt_ticket_details = findViewById(R.id.txt_ticket_details);

                    //Error handling for fields
                    if (txt_ticket_title.getText().toString().matches("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddTicket.this)
                                .setMessage("You have not entered a title").setCancelable(false).setPositiveButton("OK", null);
                        AlertDialog ad = builder.create();
                        ad.setTitle("Incomplete Info"); ad.show();
                    } else {
                        pb.setVisibility(View.VISIBLE);

                        //gives each note a key ID
                        String key = mDatabase.child("User Tickets").push().getKey();
                        Ticket ticket = new Ticket();
                        ticket.setDetails(txt_ticket_details.getText().toString());
                        ticket.setTitle(txt_ticket_title.getText().toString());
                        ticket.setDateSaved(new Date());
                        ticket.setTicketId(key);

                        Map<String, Object> ticketValue = ticket.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/root/" + mFirebaseUser.getUid() + "/" + "tickets" + "/" + key, ticketValue);

                        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Ticket saved", Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(),"Error: " + databaseError.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                    finish();
                                }

                            }
                        });
                    }
                }
            });
        }
    }

}

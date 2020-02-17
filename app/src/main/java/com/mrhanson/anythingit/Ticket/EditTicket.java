package com.mrhanson.anythingit.Ticket;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EditTicket extends AppCompatActivity {
    private TextView lbl_title;
    private TextView lbl_details;
    private TextView lbl_saved_at;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);

        FirebaseAuth mFirebaseAuth;
        final FirebaseUser mFirebaseUser;
        String mUsername;
        final String ANONYMOUS = "anonymous";

        // Initialize Firebase Auth
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

        EditText txt_title = findViewById(R.id.txt_ticket_title);
        EditText txt_details = findViewById(R.id.txt_ticket_details);

        Intent intent = getIntent();
        final Ticket ticket = (Ticket) intent.getSerializableExtra("Ticket");
        final String ticketId = ticket.getTicketId();

        FloatingActionButton fab = findViewById(R.id.fab_edit);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    EditText txt_ticket_title = findViewById(R.id.txt_ticket_title);
                    EditText txt_ticket_details = findViewById(R.id.txt_ticket_details);
                    String title = txt_ticket_title.getText().toString();

                    //Basic error checking for fields
                    if (title.matches("") /*|| title.matches(note.getTitle())*/) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditTicket.this)
                                .setMessage("Title is blank").setCancelable(false).setPositiveButton("OK", null);
                        AlertDialog ad = builder.create();
                        ad.setTitle("Incomplete Info");
                        ad.show();
                    } else {
                        txt_ticket_details.setEnabled(false);
                        txt_ticket_title.setEnabled(false);

                        final Ticket ticket = new Ticket();
                        ticket.setDetails(txt_ticket_details.getText().toString());
                        ticket.setTitle(txt_ticket_title.getText().toString());
                        ticket.setDateSaved(new Date());

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("/root/" + mFirebaseUser.getUid() + "/" + "tickets" + "/");

                        String key = mDatabase.child("User Tickets").push().getKey();
                        Map<String, Object> childUpdates = new HashMap<>();
                        ticket.setTicketId(key);
                        childUpdates.put(key, ticket.toMap());

                        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    /* Delete older ticket */
                                    DatabaseReference redundantTicket;
                                    redundantTicket = FirebaseDatabase.getInstance().getReference().child("/root/" + mFirebaseUser.getUid() + "/" + "tickets" + "/").child(ticketId);
                                    redundantTicket.removeValue();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Ticket saved successfully", Toast.LENGTH_LONG); toast.show();
                                    finish();
                                    startActivity(new Intent(EditTicket.this, MainActivity.class));
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "An error occurred while saving the ticket. Error: "
                                            + databaseError.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            txt_title.setText(ticket.getTitle());
            txt_details.setText(ticket.getDetails());
        } catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "An error occurred while opening the ticket. Error: " + e.getMessage() , Toast.LENGTH_LONG);
            toast.show();
        }
    }

}


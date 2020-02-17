package com.mrhanson.anythingit.Ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mrhanson.anythingit.Models.Ticket;
import com.mrhanson.anythingit.R;

public class ViewTicket extends AppCompatActivity {
    private TextView lbl_title;
    private TextView lbl_details;
    private TextView lbl_saved_at;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        final Intent intent = getIntent();
        final Ticket ticket = (Ticket) intent.getSerializableExtra("Ticket");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String ticketId = ticket.getTicketId();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("/root/" + mFirebaseUser.getUid() + "/" + "tickets" + "/").child(ticketId);


        lbl_details = findViewById(R.id.txt_ticket_details);
        lbl_title = findViewById(R.id.txt_ticket_title);
        lbl_saved_at = findViewById(R.id.lbl_saved_at);

        FloatingActionButton fab = findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ViewTicket.this, "TODO: Edit ticket", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),EditTicket.class);
                intent.putExtra("Ticket", ticket);
                startActivity(intent);
            }
        });

        try {
            lbl_title.setText(ticket.getTitle());
            lbl_details.setText(ticket.getDetails());
            lbl_saved_at.setText(ticket.getDateSaved().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        lbl_title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditTicket.class);
                intent.putExtra("Ticket", ticket);
                startActivity(intent);
            }
        });
        lbl_details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditTicket.class);
                intent.putExtra("Ticket", ticket);
                startActivity(intent);
            }
        });

        //shows the menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}

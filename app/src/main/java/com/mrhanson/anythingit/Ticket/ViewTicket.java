package com.mrhanson.anythingit.Ticket;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private TextView lbl_delete_ticket;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        final Intent intent = getIntent();
        final Ticket ticket = (Ticket) intent.getSerializableExtra("Ticket");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String ticketId = ticket.getTicketId();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("/root/" + mFirebaseUser.getUid() + "/" + "tickets" + "/").child(ticketId);


        lbl_details = findViewById(R.id.txt_ticket_details);
        lbl_title = findViewById(R.id.txt_ticket_title);
        lbl_saved_at = findViewById(R.id.lbl_saved_at);
        lbl_delete_ticket = findViewById(R.id.tv_deleteTicket);

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

        lbl_delete_ticket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewTicket.this);
                alert.setMessage("Do you want to delete this ticket? ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mDatabase.removeValue();
                            Toast.makeText(ViewTicket.this, "Deleted Successfully.", Toast.LENGTH_SHORT).show();

                           /* Fragment homeFrag = new Fragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.homeFrag, homeFrag ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            transaction.commit();*/

                        } catch (Exception e) {
                            Toast.makeText(ViewTicket.this, "Failed to delete...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel" , null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
        //shows the menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}

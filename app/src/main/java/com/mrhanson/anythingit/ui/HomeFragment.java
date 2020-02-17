package com.mrhanson.anythingit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mrhanson.anythingit.Models.Ticket;
import com.mrhanson.anythingit.Models.TicketAdapter;
import com.mrhanson.anythingit.R;
import com.mrhanson.anythingit.Ticket.AddTicket;
import com.mrhanson.anythingit.Ticket.TicketsList;
import com.mrhanson.anythingit.Ticket.ViewTicket;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private List<Ticket> ticketList  = new ArrayList<>();
    private DatabaseReference ticketDatabaseRef, mDeleteUserTicketDB;
    private RecyclerView lstTicket;
    private TicketAdapter nAdapter;
    private String TAG;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_ticketslist, container, false); //r.layout.fragment_home
        onDestroyView();

        FloatingActionButton fab = root.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTicket.class);
                startActivity(intent);
            }

        });

        lstTicket = root.findViewById(R.id.lst_ticket);
        nAdapter = new TicketAdapter(ticketList);
        lstTicket.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstTicket.setAdapter(nAdapter);

        final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFireBaseUser = mFirebaseAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        ticketDatabaseRef = database.getReference("/root/" + mFireBaseUser.getUid() + "/" + "tickets" + "/");

        ticketDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null) {
                    String title = dataSnapshot.child("title").getValue().toString();
                    String details = (String) dataSnapshot.child("details").getValue();
                    String date = (String) dataSnapshot.child("dateSaved").getValue();
                    String ticketsId = (String) dataSnapshot.child("ticketId").getValue();
                    Ticket ticket = new Ticket(title, details, date, ticketsId);
                    ticketList.add(ticket);
                    nAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "No Tickets, add one now!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        lstTicket.addOnItemTouchListener(new TicketsList.RecyclerTouchListener(getActivity(), lstTicket, new TicketsList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Ticket ticket = ticketList.get(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), ViewTicket.class);
                intent.putExtra("Ticket", ticket);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final View view, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setMessage("Do you want to delete this ticket? ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Ticket ticket = ticketList.get(position);
                            String ticketId = ticket.getTicketId();
                            mDeleteUserTicketDB = FirebaseDatabase.getInstance().getReference().child("/root/" + mFireBaseUser.getUid() + "/" + "tickets" + "/").child(ticketId);
                            mDeleteUserTicketDB.removeValue();
                            ticketList.remove(position);
                            nAdapter.notifyItemRemoved(position);
                            Toast.makeText(view.getContext(), "Deleted Successfully ", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(view.getContext(), "Failed to delete...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancel" , null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        }));

        return root;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.printf("Failed to connect to firebase");
        //Toast.makeText(root.getContext(), "Failed to conntec to firebase", Toast.LENGTH_SHORT).show();
    }
}
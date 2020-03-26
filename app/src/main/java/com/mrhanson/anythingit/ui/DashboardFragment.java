package com.mrhanson.anythingit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mrhanson.anythingit.ChatActivity;
import com.mrhanson.anythingit.R;
import com.mrhanson.anythingit.Ticket.AddTicket;

public class DashboardFragment extends Fragment {

    CardView cardView_chat, cardView_newTicket, cardView_anythingIt, cardView_mhdev, cardView_admin, cardView_profile;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        /*Get help */

        cardView_chat = root.findViewById(R.id.get_help);
        cardView_newTicket = root.findViewById(R.id.newTicketID);
        cardView_anythingIt = root.findViewById(R.id.anythingitID);
        cardView_mhdev = root.findViewById(R.id.michaelhansondevID);
        cardView_profile = root.findViewById(R.id.profileID);
        //cardView_admin = root.findViewById(R.id.cardView_admin);

        cardView_chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), ChatActivity.class);
                startActivity(in);
            }
        });

        cardView_newTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddTicket.class);
                startActivity(in);
            }
        });

        cardView_anythingIt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://anythingit.michaelhanson.dev"));
                startActivity(browserIntent);
            }
        });
        cardView_mhdev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://michaelhanson.dev"));
                startActivity(browserIntent);
            }
        });

        cardView_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click on profile on the bottom", Toast.LENGTH_LONG).show();
            }
        });

        //cardView_admin.

        return root;
    }
}
package com.mrhanson.anythingit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrhanson.anythingit.R;
import com.mrhanson.anythingit.Ticket.AddTicket;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Edit coming soon...", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getActivity(), EditProfile.class);
               // startActivity(intent);
            }

        });




        Toast.makeText(getContext(), "Profile is coming soon...", Toast.LENGTH_LONG).show();
        return root;
    }
}
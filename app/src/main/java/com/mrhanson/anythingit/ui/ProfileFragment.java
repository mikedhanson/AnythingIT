package com.mrhanson.anythingit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mrhanson.anythingit.R;

public class ProfileFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Toast.makeText(getContext(), "Profile is coming soon...", Toast.LENGTH_LONG).show();

        return root;
    }
}
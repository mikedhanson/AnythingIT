package com.mrhanson.anythingit.SettingsActivites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mrhanson.anythingit.R;

public class DeleteAccountDialogPreference extends DialogPreference {

    private FirebaseAuth auth;

    public DeleteAccountDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set the layout here
        setDialogLayoutResource(R.xml.preferences);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onClick() {

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Delete Account?");
        dialog.setMessage("This action will delete all your data. Are you sure you want to continue?");
        dialog.setCancelable(true);

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Deleted " + user, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Unable to delete account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        AlertDialog al = dialog.create();
        al.show();
    }

}
package com.mrhanson.anythingit.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mrhanson.anythingit.ChatActivity;
import com.mrhanson.anythingit.Models.FriendlyMessage;
import com.mrhanson.anythingit.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE = 2;
    private static final String TAG = "ProfileFragment";
    public static final String MESSAGES_CHILD = "messages"; /* database shit*/
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";


    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, ChatActivity.MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference ticketRef;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView img_profile = root.findViewById(R.id.profilePic);

        TextView accountInfo = root.findViewById(R.id.tv_accountInfo);
        TextView lastLoginDate = root.findViewById(R.id.tv_lastLoginDateValue);
        TextView accountCreationDate = root.findViewById(R.id.tv_accountCreationDateValue);
        TextView emailAccount = root.findViewById(R.id.tv_email);
        final TextView numTickets = root.findViewById(R.id.tv_numOfTickets);
        TextView numConversations = root.findViewById(R.id.tv_numOfConversations);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            Glide.with(getActivity().getApplicationContext()).load(mPhotoUrl).into(img_profile);
        }


        /*Last Login*/
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String reportDate = df.format(mFirebaseAuth.getCurrentUser().getMetadata().getLastSignInTimestamp());
        lastLoginDate.setText(reportDate);

        /*Account Creation*/
        DateFormat df_account = new SimpleDateFormat("MM/dd/yyyy");
        String reportDateCreation = df_account.format(mFirebaseAuth.getCurrentUser().getMetadata().getCreationTimestamp());
        accountCreationDate.setText(reportDateCreation);

        /*Account email address*/
        emailAccount.setText(mFirebaseUser.getEmail());

        /*account info*/
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "todo", Toast.LENGTH_SHORT).show();
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ticketRef = database.getReference("/root/" + mFirebaseUser.getUid() + "/" + "ticket");
        ticketRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    numTickets.setText((int) snap.getChildrenCount()); //getting the number of tickets
                    System.out.printf("***************NUMBER OF TICKETS************"+snap.getKey(),snap.getChildrenCount() + "");
                    Log.e(snap.getKey(),snap.getChildrenCount() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImgPicker();
                //mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();

            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Edit coming soon...", Toast.LENGTH_LONG).show();
            }

        });
        return root;
    }


    public void openImgPicker(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());
                    mUsername = mFirebaseUser.getDisplayName();

                    mFirebaseDatabaseReference.child("/root/" + mFirebaseUser.getUid() + "/" + MESSAGES_CHILD).push().setValue("", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                String key = databaseReference.getKey();
                                StorageReference storageReference = FirebaseStorage.getInstance()
                                        .getReference(mFirebaseUser.getUid())
                                        .child(key)
                                        .child(uri.getLastPathSegment());
                                putImageInStorage(storageReference, uri, key);
                            } else {
                                Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                            }
                        }
                    });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //todo
                        }
                    });
                } else {
                    //Log.w(TAG, "Image upload task was not successful.", task.getException());
                }
            }
        });
    }

}
package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {
    private static final String LOCATIONS_REF = "Locations";
    private static final String USERS_REF = "Users";
    private static final String POSTS_REF = "Posts";

    private FirebaseDatabase db;
    private DatabaseReference locationsRef;
    private DatabaseReference usersRef;

    public Database() {
        db = FirebaseDatabase.getInstance();
        locationsRef = db.getReference(LOCATIONS_REF);
        usersRef = db.getReference(USERS_REF);
        handleUpdates();
    }

    private void handleUpdates() {
        locationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Location result = dataSnapshot.getValue(Location.class);
                Log.d("onLocationChange", "New Location");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("onLocationChange", "Canceled: " + databaseError.getMessage());

            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.e("onUserChange", "User added: " + user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onUserChange", "Canceled: " + databaseError.getMessage());
            }
        });
    }

    public void addLocation(Location location) {
        locationsRef.child(location.getId()).setValue(location);
    }

    public void addPost(String locationId, Post newPost, OnCompleteListener listener) {
        locationsRef
                .child(locationId)
                .child(POSTS_REF)
                .child(newPost.getId())
                .setValue(newPost)
                .addOnCompleteListener(listener);
    }

    public void addUser(User user) {
        usersRef.child(user.getId()).setValue(user);
    }

    public void getUser(String id, ValueEventListener listener) {
        usersRef.child(id).addListenerForSingleValueEvent(listener);
    }
}

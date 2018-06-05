package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Database extends FirebaseWrapper {
    private static final String LOCATIONS_REF = "Locations";
    private static final String USERS_REF = "Users";
    private static final String POSTS_REF = "Posts";
    private static final String VISITED_REF = "VisitedLocations";

    private FirebaseDatabase db;
    private DatabaseReference locationsRef;
    private DatabaseReference usersRef;

    private static Database instance;

    private Database() {
        super();
        db = FirebaseDatabase.getInstance();
        locationsRef = db.getReference(LOCATIONS_REF);
        usersRef = db.getReference(USERS_REF);
        handleUpdates();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
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

    public void addPost(String locationId, final Post newPost) {
        locationsRef
            .child(locationId)
            .child(POSTS_REF)
            .child(newPost.getId())
            .setValue(newPost)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_ADD_POST, true, null, newPost));
                    } else {
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_ADD_POST, false, task.getException().getMessage(), newPost));
                    }
                }
            });
    }

    public void addUser(User user) {
        usersRef.child(user.getId()).setValue(user);
    }

    public void getUser(String id, ValueEventListener listener) {
        usersRef.child(id).addListenerForSingleValueEvent(listener);
    }

    public void checkInUser(final Location location) {
        Authentification auth = Authentification.getInstance();
        final User user = auth.getUser();
        usersRef.child(user.getId())
            .child(VISITED_REF)
            .child(location.getId())
            .setValue(location)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.checkIn(location);
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_CHECK_IN, true, null, user));
                    } else {
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_CHECK_IN, false, task.getException().getMessage(), user));
                    }
                }
        });
    }

    public void getUser(String id) {
        usersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> result = (Map<String, Object>) dataSnapshot.getValue();
                if (result != null) {
                    User user;
                    String username = result.get("username").toString();
                    String id = result.get("id").toString();
                    String email = result.get("email").toString();

                    if (result.containsKey(VISITED_REF)) {
                        ArrayList<Location> mappedLocations = new ArrayList<>();
                        Map<String, Object> locations = (Map<String, Object>) result.get(VISITED_REF);
                        for(Map.Entry<String, Object> entry : locations.entrySet()) {
                            Map singleLocation = (Map) entry.getValue();
                            mappedLocations.add(getLocationFromMap(singleLocation));
                        }
                        user = new User(id, username, email, mappedLocations);
                    } else {
                        user = new User(id, username, email);
                    }
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, true, null, user));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, false, "Nutzer nicht gefunden", null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, false, databaseError.getMessage(), null));
            }
        });
    }

    public void getLocation(String id) {
        locationsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> result = (Map<String, Object>) dataSnapshot.getValue();
                if (result != null) {
                    Location location = getLocationFromMap(result);
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_LOCATION, true, null, location));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_LOCATION, false, "Ort nicht gefunden", null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_LOCATION, false, databaseError.getMessage(), null));
            }
        });
    }

    public void getAllLocations() {
        locationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> result = (Map<String, Object>) dataSnapshot.getValue();
                if (result != null) {
                    ArrayList<Location> locations = new ArrayList<>();
                    for(Map.Entry<String, Object> entry : result.entrySet()) {
                        Map singleLocation  = (Map) entry.getValue();
                        Location l = getLocationFromMap(singleLocation);
                        locations.add(l);
                    }
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_ALL_LOCATIONS, true, null, locations));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_ALL_LOCATIONS, false, "Keine Orte gefunden", null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_ALL_LOCATIONS, false, databaseError.getMessage(), null));
            }
        });
    }

    private Location getLocationFromMap(Map<String, Object> map) {
        Location location;
        double latitude = Double.parseDouble(map.get("latitude").toString());
        double longitude = Double.parseDouble(map.get("longitude").toString());
        String name = map.get("name").toString();
        location = new Location(latitude, longitude, name);

        if (map.containsKey(POSTS_REF)) {
            Map<String, Object> posts = (Map<String, Object>) map.get(POSTS_REF);
            for(Map.Entry<String, Object> entry : posts.entrySet()) {
                Map singlePost = (Map) entry.getValue();
                location.addPost(getPostFromMap(singlePost));
            }
        }
        return location;
    }

    private Post getPostFromMap(Map post) {
        String id = post.get("id").toString();
        String header = post.get("header").toString();
        String content = post.get("content").toString();
        String author = post.get("authorUsername").toString();
        return new Post(id, header, content, author);
    }
}

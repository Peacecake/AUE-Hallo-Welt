package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Database extends FirebaseWrapper {
    private static final String LOCATIONS_REF = "Locations";
    private static final String USERS_REF = "Users";
    private static final String POSTS_REF = "Posts";
    private static final String VISITED_REF = "visitedLocations";
    private static final String ACHIEVEMENTS_REF = "achievements";

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

    public void addLocation(final Location location) {
        locationsRef.child(location.getId()).setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_LOCATION_ADDED, true ,null, location));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_LOCATION_ADDED, false, task.getException().getMessage(), location));
                }
            }
        });
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

    public void addPostCount(final User user) {
        int postsCount = user.getPostsCount();
        postsCount++;
        final int finalPostsCount = postsCount;
        usersRef.child(user.getId()).child("postsCount").setValue(postsCount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Achievement a = AchievementFactory.checkAuthorAchievement(finalPostsCount);
                    if (a != null) {
                        a.setNew(true);
                        user.addAchievement(a);
                        usersRef.child(user.getId()).child(ACHIEVEMENTS_REF).child(a.getId()).setValue(a);
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_ACHIEVEMENT_UNLOCKED, true, null, user));
                    }
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

    public void checkInUser(final User user, final Location location) {
        usersRef.child(user.getId())
            .child(VISITED_REF)
            .child(location.getId())
            .setValue(location)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (user.checkIn(location)) {
                            Achievement a = AchievementFactory.checkTravelAchievement(user.getVisitedLocations());
                            if (a != null) {
                                a.setNew(true);
                                user.addAchievement(a);
                                usersRef.child(user.getId()).child(ACHIEVEMENTS_REF).child(a.getId()).setValue(a);
                                // usersRef.child(user.getId()).setValue(user);
                                listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_ACHIEVEMENT_UNLOCKED, true, null, user));
                            }
                        }
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_CHECK_IN, true, null, user));
                    } else {
                        listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_CHECK_IN, false, task.getException().getMessage(), user));
                    }
                }
        });
    }

    public void updateAchievement(final User user, Achievement achievement) {
        usersRef.child(user.getId()).child(ACHIEVEMENTS_REF).child(achievement.getId()).setValue(achievement).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_UPDATE, true, null, user));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_USER_UPDATE, false, task.getException().getMessage(), null));
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
                    int postCount = result.containsKey("postsCount") ? Integer.parseInt(result.get("postsCount").toString()) : 0;

                    ArrayList<Location> mappedLocations = new ArrayList<>();
                    ArrayList<Achievement> mappedAchievements = new ArrayList<>();

                    if (result.containsKey(VISITED_REF)) {
                        try {
                            Map<String, Object> allLocations = (Map<String,Object>) result.get(VISITED_REF);
                            for(Map.Entry<String, Object> mLocation : allLocations.entrySet()) {
                                Map singleLocation  = (Map) mLocation.getValue();
                                mappedLocations.add(getLocationFromMap(singleLocation));
                            }
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                    if (result.containsKey(ACHIEVEMENTS_REF)) {
                        try {
                            Map<String, Object> allAchievements = (Map<String, Object>) result.get(ACHIEVEMENTS_REF);
                            for(Map.Entry<String, Object> mAchievement : allAchievements.entrySet()) {
                                Map singleAchievement = (Map) mAchievement.getValue();
                                mappedAchievements.add(getAchievementFromMap(singleAchievement));
                            }
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                    user = new User(id, username, email, postCount, mappedLocations, mappedAchievements);
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, true, null, user));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, false, "Nutzerprofil nicht gefunden", null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_GET_USER, false, databaseError.getMessage(), null));
            }
        });
    }

    public void deletePost(String locationId, String postId) {
        locationsRef.child(locationId).child(POSTS_REF).child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_POST_DELETE, true, null, null));
                } else {
                    listener.onDatabaseEvent(new DatabaseResult(FirebaseResult.DB_POST_DELETE, false, task.getException().getMessage(), null));
                }
            }
        });
    }

    private Achievement getAchievementFromMap(Map<String, Object> map) {
        Achievement achievement;

        String id = map.get("id").toString();
        String title = map.get("title").toString();
        String description = map.get("description").toString();
        boolean isNew = Boolean.parseBoolean(map.get("new").toString());
        achievement = new Achievement(id, title, description, isNew);

        return achievement;
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
        String description = map.containsKey("description") ? map.get("description").toString() : "";
        location = new Location(latitude, longitude, name, description);

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
        if (post.containsKey("date")) {
            Map<String, Object> dateMap = (Map<String, Object>) post.get("date");
            String timeInMillis = dateMap.get("time").toString();
            Long millis = Long.parseLong(timeInMillis);
            Date date = new Date(millis);
            return new Post(id ,header, content,author, date);
        }
        return new Post(id, header, content, author, new Date());
    }
}

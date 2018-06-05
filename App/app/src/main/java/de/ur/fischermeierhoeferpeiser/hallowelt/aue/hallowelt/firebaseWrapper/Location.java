package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import java.util.ArrayList;

public class Location {
    private String id;
    private long latitude;
    private long longitude;
    private String name;
    private ArrayList<Post> posts;

    public Location(String id, long latitude, long longitude, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        posts = new ArrayList<>();
    }

    public void addPost(Post newPost) {
        posts.add(newPost);
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }
}

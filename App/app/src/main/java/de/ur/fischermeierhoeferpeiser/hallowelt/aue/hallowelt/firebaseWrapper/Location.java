package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Location {
    private double latitude;
    private double longitude;
    private String name;
    private ArrayList<Post> posts;

    public Location() {}

    public Location(double latitude, double longitude, String name) {
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
        return Double.toString(latitude).replace(".", "") + "" + Double.toString(longitude).replace(".", "");
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

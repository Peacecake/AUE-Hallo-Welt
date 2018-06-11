package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Location {
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private ArrayList<Post> posts;

    public Location() {}

    public Location(double latitude, double longitude, String name, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.id = Double.toString(latitude).replace(".", "") + "" + Double.toString(longitude).replace(".", "");
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

    public String getDescription() {
        return description;
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

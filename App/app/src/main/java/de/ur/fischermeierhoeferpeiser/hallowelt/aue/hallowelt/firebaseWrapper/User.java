package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import java.util.ArrayList;

public class User {
    private String id;
    private String username;
    private String email;
    private ArrayList<Location> visitedLocations;
    private Location checkedInLocation;

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        visitedLocations = new ArrayList<>();
        checkedInLocation = null;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Location> getVisitedLocations() {
        return visitedLocations;
    }

    public Location getCurrentLocation() {
        return checkedInLocation;
    }

    public void checkIn(Location location) {
        if (isNewLocation(location)) {
            visitedLocations.add(location);
        }
        checkedInLocation = location;
    }

    public void checkOut() {
        checkedInLocation = null;
    }

    private boolean isNewLocation(Location location) {
        for(Location l : visitedLocations) {
            if (l.getId().equals(location.getId())) {
                return false;
            }
        }
        return true;
    }
}

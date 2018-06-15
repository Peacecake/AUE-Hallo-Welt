package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    private String id;
    private String username;
    private String email;
    private ArrayList<Location> visitedLocations;
    private ArrayList<Achievement> achievements;
    private Location checkedInLocation;

    public User() {}

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        visitedLocations = new ArrayList<>();
        achievements = new ArrayList<>();
        checkedInLocation = null;
    }

    public User(String id, String username, String email, ArrayList<Location> locations, ArrayList<Achievement> achievements) {
        this.id = id;
        this.username = username;
        this.email = email;
        checkedInLocation = null;
        this.visitedLocations = locations != null ? locations : new ArrayList<Location>();
        this.achievements = achievements != null ? achievements : new ArrayList<Achievement>();
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

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public void setAchievementsOld() {
        for(Achievement a : achievements) {
            a.setNew(false);
        }
    }

    @Exclude
    public Location getCurrentLocation() {
        return checkedInLocation;
    }

    public boolean checkIn(Location location) {
        boolean isNewLocation = false;
        if (isNewLocation(location)) {
            visitedLocations.add(location);
            isNewLocation = true;
        }
        checkedInLocation = location;
        return isNewLocation;
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

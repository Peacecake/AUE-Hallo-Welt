package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import java.util.ArrayList;

public class AchievementFactory {
    private static final int TRAVEL_ACHIEVEMENT_THRESHOLD = 3;

    public static Achievement checkTravelAchievement(ArrayList<Location> visitedLocations) {
        if (visitedLocations.size() % TRAVEL_ACHIEVEMENT_THRESHOLD == 0) {
            Achievement a = new Achievement("Viel-Reiser", "Du hast drei neue Orte besucht, Glückwunsch!");
            return  a;
        }
        return null;
    }
}

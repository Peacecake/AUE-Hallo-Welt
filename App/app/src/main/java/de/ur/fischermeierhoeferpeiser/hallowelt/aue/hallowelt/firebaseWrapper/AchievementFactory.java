package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import java.util.ArrayList;

public class AchievementFactory {
    private static final int TRAVEL_ACHIEVEMENT_THRESHOLD = 3;

    public static Achievement checkTravelAchievement(ArrayList<Location> visitedLocations) {
        if (visitedLocations.size() == 1) {
            return new Achievement("Neu hier?", "Du hast die erste Sehenswürdigkeit besucht, willkommen in Regensburg!");
        }
        if (visitedLocations.size() % TRAVEL_ACHIEVEMENT_THRESHOLD == 0) {
            return new Achievement("Erkunder", "Du hast drei neue Orte besucht, Glückwunsch!");
        }
        return null;
    }
}

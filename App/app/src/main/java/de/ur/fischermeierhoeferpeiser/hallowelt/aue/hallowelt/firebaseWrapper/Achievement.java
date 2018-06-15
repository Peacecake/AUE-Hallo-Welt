package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Achievement {
    private String id;
    private String title;
    private String description;
    private boolean isNew;

    public Achievement() {}

    public Achievement(String title, String description) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.description = description;
        this.isNew = false;
    }

    public Achievement(String id, String title, String description, boolean isNew) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isNew = isNew;
    }

    public String getId() {
        return id;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}

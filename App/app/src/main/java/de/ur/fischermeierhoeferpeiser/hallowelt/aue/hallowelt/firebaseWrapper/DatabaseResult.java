package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public class DatabaseResult extends FirebaseResult {

    private Object databaseObject;

    public DatabaseResult(int type, boolean isSuccessful, String errorMessage, Object databaseObject) {
        super(type, isSuccessful, errorMessage);
        this.databaseObject = databaseObject;
    }

    public Object getDatabaseObject() {
        return databaseObject;
    }
}

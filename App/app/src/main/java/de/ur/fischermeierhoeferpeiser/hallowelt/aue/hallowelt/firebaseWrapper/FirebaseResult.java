package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public class FirebaseResult {
    public static final int AUTH_REGISTER = 0;
    public static final int AUTH_LOGIN = 1;
    public static final int AUTH_LOGOUT = 2;
    public static final int DB_GET_LOCATION = 3;
    public static final int DB_GET_USER = 4;
    public static final int DB_USER_CHECK_IN = 5;
    public static final int DB_GET_ALL_LOCATIONS = 6;
    public static final int DB_ADD_POST = 7;
    public static final int DB_LOCATION_ADDED = 8;

    private int type;
    private boolean isSuccessful;
    private String errorMessage;

    public FirebaseResult(int type, boolean isSuccessful, String errorMessage) {
        this.type = type;
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }

    public int getType() {
        return type;
    }

    public boolean wasSuccessful() {
        return isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.auth.FirebaseUser;

public class AuthentificationResult {
    public static final int AUTH_REGISTER = 0;
    public static final int AUTH_LOGIN = 1;
    public static final int AUTH_LOGOUT = 2;

    private FirebaseUser user;
    private int type;
    private String errorMessage;
    private boolean isSuccessful;

    public AuthentificationResult(int type, FirebaseUser user, boolean isSuccessful, String errorMessage) {
        this.type = type;
        this.user = user;
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean wasSuccessful() {
        return isSuccessful;
    }

    public int getType() {
        return type;
    }

    public FirebaseUser getUser() {
        return user;
    }
}

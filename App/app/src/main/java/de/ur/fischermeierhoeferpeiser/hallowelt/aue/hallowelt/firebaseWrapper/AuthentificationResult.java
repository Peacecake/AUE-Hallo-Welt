package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public class AuthentificationResult {
    public static final int AUTH_REGISTER = 0;
    public static final int AUTH_LOGIN = 1;
    public static final int AUTH_LOGOUT = 2;

    private User user;
    private int type;
    private String errorMessage;
    private boolean isSuccessful;

    public AuthentificationResult(int type, User user, boolean isSuccessful, String errorMessage) {
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

    public User getUser() {
        return user;
    }
}

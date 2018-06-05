package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public class AuthentificationResult extends FirebaseResult {

    private User user;

    public AuthentificationResult(int type, User user, boolean isSuccessful, String errorMessage) {
        super(type, isSuccessful, errorMessage);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

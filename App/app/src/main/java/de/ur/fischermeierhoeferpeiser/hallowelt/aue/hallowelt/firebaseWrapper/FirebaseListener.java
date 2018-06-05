package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public interface FirebaseListener {
    void onAuthEvent(AuthentificationResult authentificationResult);
    void onDatabaseEvent(DatabaseResult databaseResult);
}

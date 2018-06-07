package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Database;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseListener;

public class HelloWorldActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, FirebaseListener {
    protected Database db;
    protected Authentification auth;
    private Loader loader;

    @Override
    protected void onStart() {
        super.onStart();
        db = Database.getInstance();
        db.setOnFirebaseListener(this);
        auth = Authentification.getInstance();
        auth.setContext(this);
        auth.setOnFirebaseListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.removeOnAuthStateChangeListener(this);
    }

    protected void initLoader(int loaderId) {
        loader = findViewById(loaderId);
    }

    protected void setLoginProtected() {
        if (auth.getUser() == null) {
            goToLogin();
        }

        auth.setOnAuthStateChangeListener(this);
    }

    private void goToLogin() {
        Intent goToLoginIntent = new Intent(this, MainActivity.class);
        startActivity(goToLoginIntent);
    }

    protected void setLoading(boolean isLoading, View... disableOnLoadViews) {
        if (loader != null) {
            loader.setLoading(isLoading);
        }

        if (disableOnLoadViews != null) {
            for (View v : disableOnLoadViews) {
                v.setEnabled(!isLoading);
            }
        }

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            goToLogin();
        }
    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {

    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {

    }
}

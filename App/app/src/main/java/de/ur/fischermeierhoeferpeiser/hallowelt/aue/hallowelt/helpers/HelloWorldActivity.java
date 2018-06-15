package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth.AchievementActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth.ProfileActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Achievement;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Database;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseListener;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseWrapper;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_ACHIEVEMENT_UNLOCKED;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_ADD_POST;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_ALL_LOCATIONS;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_LOCATION;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_USER;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_LOCATION_ADDED;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_USER_CHECK_IN;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_USER_UPDATE;

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
        if (auth != null)
            auth.removeOnAuthStateChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (auth.isLoggedIn()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.default_top_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.miSignOut:
                auth.signOut();
                return true;
            case R.id.miSetup:
                startActivity(new Intent(this, SetupActivity.class));
                return true;
            case R.id.miAchievements:
                startActivity(new Intent(this, AchievementActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * If you want to use the loader, run this in OnCreate or onStart of your Activity. The root layout of
     * your Activity should be a RelativeLayout. Put the Loader Component to the bottom of your Activity Layout,
     * so it overlaps properly.
     * @param loaderId Something like R.id.myFancyLoader
     */
    protected void initLoader(int loaderId) {
        loader = findViewById(loaderId);
    }

    /**
     * Starts MainActivity if user is currently not logged in or the authentification state changes to logged out.
     * Use this in the onStart method at the earliest, otherwise app will crash with NullPointerException.
     * @return true if user is currently logged in
     */
    protected boolean setLoginProtected() {
        if (auth.getUser() == null) {
            goToLogin();
            return false;
        }
        auth.setOnAuthStateChangeListener(this);
        return true;
    }

    private void goToLogin() {
        Intent goToLoginIntent = new Intent(this, MainActivity.class);
        startActivity(goToLoginIntent);
    }

    /**
     * Call this method at the beginning and the end of an async operation. If some View elements should be disabled while loading
     * (for example the button that triggered the operation) you can pass them as well.
     * @param isLoading true if loading starts, else false
     * @param disableOnLoadViews optional. Any number of View elements that should be disabled while loading
     */
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

    protected void onDatabaseError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    protected void onLocationRetrieved(Location location) {

    }

    protected void onAllLocationsRetrieved(ArrayList<Location> locations) {}

    protected void onUserRetrieved(User user) {
        ArrayList<Achievement> achievements = user.getAchievements();
        if (achievements != null) {
            for(Achievement a : achievements) {
                if (a.isNew()) {
                    AchievementDialog dialog = AchievementDialog.newInstance(a.getTitle(), a.getDescription());
                    dialog.show(getFragmentManager(), "tag");
                    user.setAchievementsOld();
                    db.updateAchievement(user, a);
                }
            }
        }
    }

    protected void onUserCheckedIn(User user) {}

    protected void onPostAdded(Post post) {}

    protected void onLocationAdded(Location location) {}

    protected void onAchievementUnlocked(User user, Achievement newAchievement) {

    }

    protected void onUserUpdated(User user) {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            goToLogin();
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {

    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {
        if (!databaseResult.wasSuccessful()) onDatabaseError(databaseResult.getErrorMessage());
        else {
            switch(databaseResult.getType()) {
                case DB_GET_LOCATION:
                    onLocationRetrieved((Location) databaseResult.getDatabaseObject());
                    break;
                case DB_GET_USER:
                    onUserRetrieved((User) databaseResult.getDatabaseObject());
                    break;
                case DB_USER_CHECK_IN:
                    onUserCheckedIn((User) databaseResult.getDatabaseObject());
                    break;
                case DB_GET_ALL_LOCATIONS:
                    onAllLocationsRetrieved((ArrayList<Location>) databaseResult.getDatabaseObject());
                    break;
                case DB_ADD_POST:
                    onPostAdded((Post) databaseResult.getDatabaseObject());
                    break;
                case DB_LOCATION_ADDED:
                    onLocationAdded((Location) databaseResult.getDatabaseObject());
                    break;
                case DB_ACHIEVEMENT_UNLOCKED:
                    User user = (User) databaseResult.getDatabaseObject();
                    Achievement newAchievement = null;
                    ArrayList<Achievement> achievements = user.getAchievements();
                    if (achievements != null) {
                        for (Achievement a : achievements) {
                            if (a.isNew()) {
                                newAchievement = a;
                            }
                        }
                    }
                    onAchievementUnlocked(user, newAchievement);
                    break;
                case DB_USER_UPDATE:
                    onUserUpdated((User) databaseResult.getDatabaseObject());
                    break;
            }
        }
    }
}

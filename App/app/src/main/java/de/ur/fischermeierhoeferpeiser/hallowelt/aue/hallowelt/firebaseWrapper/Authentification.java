package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult.AUTH_LOGIN;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult.AUTH_LOGOUT;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult.AUTH_REGISTER;

public class Authentification extends FirebaseWrapper{
    private FirebaseAuth auth;
    private Context context;
    private Database db;

    private User currentUser;

    private static Authentification instance;

    private Authentification() {
        super();
        auth = FirebaseAuth.getInstance();
        db = Database.getInstance();
        currentUser = null;
    }

    public static Authentification getInstance() {
        if (instance == null) {
            instance = new Authentification();
        }

        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public User getUser() {
        if (auth.getCurrentUser() != null)
            return currentUser;
        return null;
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public void signOut() {
        auth.signOut();
    }

    public void login(final String email, final String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    db.getUser(auth.getCurrentUser().getUid(), new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentUser = dataSnapshot.getValue(User.class);
                            listener.onAuthEvent(new AuthentificationResult(AUTH_LOGIN, currentUser, true, null));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            listener.onAuthEvent(new AuthentificationResult(AUTH_LOGIN, null, false, databaseError.getMessage()));
                        }
                    });
                } else {
                    listener.onAuthEvent(new AuthentificationResult(AUTH_LOGIN, null, task.isSuccessful(), task.getException().getMessage()));
                }
            }
        });
    }

    public void registerUser(final String username, final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())  {
                    FirebaseUser user = auth.getCurrentUser();
                    User u = new User(user.getUid(), username, email);
                    db.addUser(u);
                    listener.onAuthEvent(new AuthentificationResult(AUTH_REGISTER, u, task.isSuccessful(), null));
                } else {
                    listener.onAuthEvent(new AuthentificationResult(AUTH_REGISTER, null, task.isSuccessful(), task.getException().getMessage()));
                }
            }
        });
    }
}

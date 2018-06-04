package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult.AUTH_LOGIN;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult.AUTH_REGISTER;

public class Authentification {
    private FirebaseAuth auth;
    private Context context;
    private FirebaseListener listener;

    public Authentification(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
    }

    public void setOnFirebaseListener(FirebaseListener listener) {
        this.listener = listener;
    }

    public FirebaseUser getUser() {
        return auth.getCurrentUser();
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
                    listener.onAuthEvent(new AuthentificationResult(AUTH_LOGIN, auth.getCurrentUser(), task.isSuccessful(), null));
                } else {
                    listener.onAuthEvent(new AuthentificationResult(AUTH_LOGIN, auth.getCurrentUser(), task.isSuccessful(), task.getException().getMessage()));
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
                    UserProfileChangeRequest profileUpates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    user.updateProfile(profileUpates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                listener.onAuthEvent(new AuthentificationResult(AUTH_REGISTER, auth.getCurrentUser(), task.isSuccessful(), null));
                            } else {
                                listener.onAuthEvent(new AuthentificationResult(AUTH_REGISTER, auth.getCurrentUser(), task.isSuccessful(), task.getException().getMessage()));
                            }
                        }
                    });
                } else {
                    listener.onAuthEvent(new AuthentificationResult(AUTH_REGISTER, auth.getCurrentUser(), task.isSuccessful(), task.getException().getMessage()));
                }
            }
        });
    }
}

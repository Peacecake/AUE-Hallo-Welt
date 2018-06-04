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

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
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
                                listener.onRegister(auth.getCurrentUser(), null);
                            } else {
                                listener.onRegister(null, task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    listener.onRegister(null, task.getException().getMessage());
                }
            }
        });
    }

}

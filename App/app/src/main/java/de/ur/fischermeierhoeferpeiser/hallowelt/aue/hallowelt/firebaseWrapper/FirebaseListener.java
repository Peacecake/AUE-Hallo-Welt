package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.auth.FirebaseUser;

public interface FirebaseListener {
    void onRegister(FirebaseUser user, String errorMessage);
}

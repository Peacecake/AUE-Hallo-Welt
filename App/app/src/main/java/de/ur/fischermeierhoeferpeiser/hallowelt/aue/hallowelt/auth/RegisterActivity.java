package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseListener;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.Loader;

public class RegisterActivity extends AppCompatActivity implements FirebaseListener {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnRegister;
    private Loader loader;

    private Authentification auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initListeners();
    }

    private void initUi() {
        setContentView(R.layout.activity_register);
        loader = findViewById(R.id.registerLoader);
        etUsername = findViewById(R.id.etRegisterUsername);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etPasswordRepeat = findViewById(R.id.etRegisterPasswordRepeat);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setEnabled(true);
    }

    private void initListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    btnRegister.setEnabled(false);
                    loader.setLoading(true);
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    auth = Authentification.getInstance();
                    auth.setContext(RegisterActivity.this);
                    auth.setOnFirebaseListener(RegisterActivity.this);
                    auth.registerUser(username, email, password);
                }
            }
        });
    }

    private boolean validateInputs() {
        FormValidator validator = new FormValidator(this);
        validator.requiredFieldsAreEmpty(etUsername, etEmail, etPassword, etPasswordRepeat);
        validator.isValidEmail(etEmail.getText().toString());
        validator.passwortRepeatMatches(etPassword.getText().toString(), etPasswordRepeat.getText().toString());

        if (!validator.isValid()) {
            Toast.makeText(this, validator.getErrorsAsString(), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {
        if (authentificationResult.wasSuccessful()) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, authentificationResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
        btnRegister.setEnabled(true);
        loader.setLoading(false);
    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {

    }
}

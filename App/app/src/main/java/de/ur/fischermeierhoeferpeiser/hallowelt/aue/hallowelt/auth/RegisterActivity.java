package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.map.MapsActivity;

public class RegisterActivity extends HelloWorldActivity {
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initLoader(R.id.registerLoader);
        initListeners();
    }

    private void initUi() {
        setContentView(R.layout.activity_register);
        setTitle(getString(R.string.registerHeader));
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
                    setLoading(true, btnRegister);
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
        super.onAuthEvent(authentificationResult);
        setLoading(false, btnRegister);
        if (authentificationResult.wasSuccessful()) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, authentificationResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {
        super.onDatabaseEvent(databaseResult);
    }
}

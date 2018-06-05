package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth.ProfileActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth.RegisterActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseListener;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseListener {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    Authentification auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        initLayout();
        initListeners();
        initAuthentification();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;
            case R.id.tvRegister:
                registerUser();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private void initAuthentification() {
        auth = new Authentification(this);
        auth.setOnFirebaseListener(this);

        if (auth.isLoggedIn()) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void loginUser() {
        FormValidator validator = new FormValidator(this);
        validator.requiredFieldsAreEmpty(etEmail, etPassword);
        validator.isValidEmail(etEmail.getText().toString());
        if (!validator.isValid()) {
            Toast.makeText(this, validator.getErrorsAsString(), Toast.LENGTH_LONG).show();
            return;
        }

        auth.login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    private void registerUser() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);

    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {
        if (authentificationResult.wasSuccessful()) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, authentificationResult.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {

    }
}

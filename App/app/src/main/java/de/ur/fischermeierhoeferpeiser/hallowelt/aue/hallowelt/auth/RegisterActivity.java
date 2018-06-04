package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initListeners();
    }

    private void initUi() {
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etRegisterUsername);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etPasswordRepeat = findViewById(R.id.etRegisterPasswordRepeat);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void initListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();

                if (validateInputs()) {
                    Toast.makeText(RegisterActivity.this, "Register " + username + " with email " + email, Toast.LENGTH_SHORT).show();
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
}

package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;

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
        if (etUsername.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, getString(R.string.username) + " " + getString(R.string.msgRequiredField), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etEmail.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, getString(R.string.email) + " " + getString(R.string.msgRequiredField), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPassword.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password) + " " + getString(R.string.msgRequiredField), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPasswordRepeat.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, getString(R.string.passwordRepeat) + " " + getString(R.string.msgRequiredField), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etPassword.getText().toString().equals(etPasswordRepeat.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.msgPasswordMatch), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.msgInvalidEmail), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String target) {
        return target.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+");
    }
}

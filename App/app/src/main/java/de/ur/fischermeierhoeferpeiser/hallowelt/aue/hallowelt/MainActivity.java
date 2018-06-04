package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.validation.Validator;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth.RegisterActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initListeners();
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

    private void initLayout() {
        setContentView(R.layout.activity_main);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
    }

    private void initListeners() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void loginUser() {
        FormValidator validator = new FormValidator(this);
        if (validator.requiredFieldsAreEmpty(etUsername, etPassword)) {
            Toast.makeText(this, validator.getErrorsAsString(), Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Login "+etUsername.getText()+" with password " + etPassword.getText() + "", Toast.LENGTH_SHORT).show();
    }

    private void registerUser() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);

    }

}

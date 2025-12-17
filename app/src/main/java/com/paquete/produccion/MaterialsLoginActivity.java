package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MaterialsLoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    
    // Credenciales temporales - en producción usar Firebase Auth
    private static final String VALID_USERNAME = "materials";
    private static final String VALID_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials_login);

        initializeViews();
        setupLoginButton();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(username, password)) {
            return;
        }

        if (authenticateUser(username, password)) {
            navigateToDashboard();
        } else {
            showError(getString(R.string.incorrect_credentials));
        }
    }

    private boolean validateInputs(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("El usuario es requerido");
            etUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("La contraseña es requerida");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean authenticateUser(String username, String password) {
        return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(MaterialsLoginActivity.this, MaterialsDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
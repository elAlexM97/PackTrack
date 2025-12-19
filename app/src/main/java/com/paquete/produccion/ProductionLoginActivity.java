package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductionLoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupLoginButton();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etUsername); // Reutilizando el ID existente
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            verifyUserType(user.getUid(), "produccion");
                        }
                    } else {
                        showError("Error: " + task.getException().getMessage());
                        btnLogin.setEnabled(true);
                        btnLogin.setText(getString(R.string.login_button));
                    }
                });
    }

    private void verifyUserType(String userId, String expectedType) {
        db.collection("usuarios").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userType = document.getString("userType");
                            if (expectedType.equals(userType)) {
                                navigateToDashboard();
                            } else {
                                mAuth.signOut();
                                showError("Este usuario no pertenece al área de Producción");
                                btnLogin.setEnabled(true);
                                btnLogin.setText(getString(R.string.login_button));
                            }
                        } else {
                            showError("Usuario no encontrado");
                            btnLogin.setEnabled(true);
                            btnLogin.setText(getString(R.string.login_button));
                        }
                    } else {
                        showError("Error al verificar usuario");
                        btnLogin.setEnabled(true);
                        btnLogin.setText(getString(R.string.login_button));
                    }
                });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("El correo es requerido");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("La contraseña es requerida");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(ProductionLoginActivity.this, ProductionDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

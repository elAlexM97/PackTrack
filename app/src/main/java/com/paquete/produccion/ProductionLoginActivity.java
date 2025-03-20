package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductionLoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Simple validation - in a real app, you would check against a database
                if (username.equals("production") && password.equals("1234")) {
                    Intent intent = new Intent(ProductionLoginActivity.this, ProductionDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProductionLoginActivity.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

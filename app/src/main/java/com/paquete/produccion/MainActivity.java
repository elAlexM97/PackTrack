package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private Button btnMaterials, btnProduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnMaterials = findViewById(R.id.btnMaterials);
        btnProduction = findViewById(R.id.btnProduction);
    }

    private void setupClickListeners() {
        btnMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMaterialsLogin();
            }
        });

        btnProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductionLogin();
            }
        });
    }

    private void navigateToMaterialsLogin() {
        Intent intent = new Intent(MainActivity.this, MaterialsLoginActivity.class);
        startActivity(intent);
    }

    private void navigateToProductionLogin() {
        Intent intent = new Intent(MainActivity.this, ProductionLoginActivity.class);
        startActivity(intent);
    }
}
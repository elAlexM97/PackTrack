package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AnimatedBottomNavBar navBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupNavigationBar();
        setupCardButtons();
        checkUserSession();
    }

    private void initializeViews() {
        navBar = findViewById(R.id.navBarContainer);
    }

    private void setupNavigationBar() {
        // Crear items de navegación iniciales (Registro y Login)
        List<AnimatedBottomNavBar.NavItem> navItems = new ArrayList<>();
        navItems.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_menu_add, "Registrarse"));
        navItems.add(new AnimatedBottomNavBar.NavItem(android.R.drawable.ic_lock_lock, "Iniciar Sesión"));

        navBar.setNavItems(navItems);
        navBar.setOnNavItemSelectedListener((position, item) -> {
            if (position == 0) {
                // Registro
                navigateToRegister();
            } else if (position == 1) {
                // Login
                navigateToLogin();
            }
        });
    }

    private void setupCardButtons() {
        // Botones en la tarjeta principal
        View btnRegisterCard = findViewById(R.id.btnRegisterCard);
        View btnLoginCard = findViewById(R.id.btnLoginCard);

        if (btnRegisterCard != null) {
            btnRegisterCard.setOnClickListener(v -> navigateToRegister());
        }

        if (btnLoginCard != null) {
            btnLoginCard.setOnClickListener(v -> navigateToLogin());
        }
    }

    private void checkUserSession() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Usuario ya está logueado, ir directamente al dashboard
            navigateToUserDashboard();
        }
    }

    private void navigateToRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        // Mostrar diálogo o ir a actividad de selección de tipo de login
        Intent intent = new Intent(MainActivity.this, LoginSelectionActivity.class);
        startActivity(intent);
    }

    private void navigateToUserDashboard() {
        // Esto se implementará después de la autenticación
        // Por ahora, mantener la vista HOME
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar si el usuario se logueó en otra actividad
        checkUserSession();
    }
}
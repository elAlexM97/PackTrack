package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import javax.annotation.Nullable;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.EventListener;

import java.util.HashMap;

public class ProductionDashboardActivity extends AppCompatActivity {

    private EditText etMaterial, etMeasurements, etColor, etArea;
    private Button btnSendRequest;
    private TextView tvNotifications;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_dashboard);

        initializeViews();
        setupFirestore();
        setupSendRequestButton();
        setupRequestStatusListener();
    }

    private void initializeViews() {
        etMaterial = findViewById(R.id.etComponentNumber);
        etMeasurements = findViewById(R.id.etComponentNumber2);
        etColor = findViewById(R.id.etComponentNumber3);
        etArea = findViewById(R.id.etComponentNumber4);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        tvNotifications = findViewById(R.id.tvNotifications);
    }

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupSendRequestButton() {
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String material = etMaterial.getText().toString().trim();
        String measurements = etMeasurements.getText().toString().trim();
        String color = etColor.getText().toString().trim();
        String area = etArea.getText().toString().trim();

        if (!validateRequest(material, measurements, color, area)) {
            return;
        }

        HashMap<String, Object> request = new HashMap<>();
        request.put("material", material);
        request.put("medidas", measurements);
        request.put("color", color);
        request.put("area", area);
        request.put("status", "pendiente");
        request.put("timestamp", System.currentTimeMillis());

        db.collection("pedidos").add(request)
                .addOnSuccessListener(documentReference -> {
                    showSuccess(getString(R.string.request_sent));
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    showError(getString(R.string.error_sending_request));
                });
    }

    private boolean validateRequest(String material, String measurements, String color, String area) {
        boolean isValid = true;

        if (TextUtils.isEmpty(material)) {
            etMaterial.setError("El material es requerido");
            etMaterial.requestFocus();
            isValid = false;
        }

        if (TextUtils.isEmpty(measurements)) {
            etMeasurements.setError("Las medidas son requeridas");
            if (isValid) {
                etMeasurements.requestFocus();
            }
            isValid = false;
        }

        if (TextUtils.isEmpty(color)) {
            etColor.setError("El color es requerido");
            if (isValid) {
                etColor.requestFocus();
            }
            isValid = false;
        }

        if (TextUtils.isEmpty(area)) {
            etArea.setError("El área es requerida");
            if (isValid) {
                etArea.requestFocus();
            }
            isValid = false;
        }

        return isValid;
    }

    private void clearForm() {
        etMaterial.setText("");
        etMeasurements.setText("");
        etColor.setText("");
        etArea.setText("");
    }

    private void setupRequestStatusListener() {
        db.collection("pedidos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                    String status = dc.getDocument().getString("status");
                                    updateNotificationStatus(status);
                                }
                            }
                        }
                    }
                });
    }

    private void updateNotificationStatus(String status) {
        if ("aceptado".equals(status)) {
            tvNotifications.setText(getString(R.string.request_accepted_by_materials));
            tvNotifications.setVisibility(View.VISIBLE);
            tvNotifications.setBackgroundColor(getResources().getColor(R.color.success_green));
        } else if ("rechazado".equals(status)) {
            tvNotifications.setText(getString(R.string.request_rejected_by_materials));
            tvNotifications.setVisibility(View.VISIBLE);
            tvNotifications.setBackgroundColor(getResources().getColor(R.color.error_red));
        }
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import javax.annotation.Nullable;

public class MaterialsDashboardActivity extends AppCompatActivity {

    private LinearLayout layoutRequests;
    private TextView tvNoRequests;
    private TextView tvComponentRequest;
    private TextView tvMaterialDetails;
    private Button btnAccept, btnReject;
    private FirebaseFirestore db;
    private String componentId = null;
    private DocumentSnapshot currentRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials_dashboard);

        initializeViews();
        setupFirestore();
        setupButtons();
        setupRequestListener();
    }

    private void initializeViews() {
        layoutRequests = findViewById(R.id.layoutRequests);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        tvComponentRequest = findViewById(R.id.tvComponentRequest);
        tvMaterialDetails = findViewById(R.id.tvMaterialDetails);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
    }

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupButtons() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentId != null) {
                    updateRequestStatus("aceptado");
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentId != null) {
                    updateRequestStatus("rechazado");
                }
            }
        });
    }

    private void updateRequestStatus(String status) {
        db.collection("pedidos").document(componentId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    String message = status.equals("aceptado") 
                            ? getString(R.string.request_accepted) 
                            : getString(R.string.request_rejected);
                    showSuccess(message);
                    componentId = null;
                    currentRequest = null;
                    updateRequestsView();
                })
                .addOnFailureListener(e -> {
                    showError("Error al actualizar solicitud");
                });
    }

    private void setupRequestListener() {
        db.collection("pedidos")
                .whereEqualTo("status", "pendiente")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable com.google.firebase.firestore.FirebaseFirestoreException e) {
                        if (e != null) {
                            showError(getString(R.string.error_loading_requests));
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            // Tomar la primera solicitud pendiente
                            DocumentSnapshot firstDoc = snapshots.getDocuments().get(0);
                            componentId = firstDoc.getId();
                            currentRequest = firstDoc;
                            updateRequestsView();
                        } else {
                            componentId = null;
                            currentRequest = null;
                            updateRequestsView();
                        }
                    }
                });
    }

    private void updateRequestsView() {
        if (componentId != null && currentRequest != null) {
            tvNoRequests.setVisibility(View.GONE);
            layoutRequests.setVisibility(View.VISIBLE);
            
            // Mostrar información detallada de la solicitud
            String material = currentRequest.getString("material");
            String medidas = currentRequest.getString("medidas");
            String color = currentRequest.getString("color");
            String area = currentRequest.getString("area");
            
            StringBuilder details = new StringBuilder();
            details.append("ID: ").append(componentId).append("\n");
            
            if (material != null && !material.isEmpty()) {
                details.append("Material: ").append(material).append("\n");
            }
            if (medidas != null && !medidas.isEmpty()) {
                details.append("Medidas: ").append(medidas).append("\n");
            }
            if (color != null && !color.isEmpty()) {
                details.append("Color: ").append(color).append("\n");
            }
            if (area != null && !area.isEmpty()) {
                details.append("Área: ").append(area);
            }
            
            tvComponentRequest.setText(getString(R.string.component_request));
            tvMaterialDetails.setText(details.toString());
        } else {
            tvNoRequests.setVisibility(View.VISIBLE);
            layoutRequests.setVisibility(View.GONE);
        }
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
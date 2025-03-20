package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import javax.annotation.Nullable;

public class MaterialsDashboardActivity extends AppCompatActivity {

    private LinearLayout layoutRequests;
    private TextView tvNoRequests;
    private TextView tvComponentRequest;
    private Button btnAccept, btnReject;
    private FirebaseFirestore db;
    private String componentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials_dashboard);

        layoutRequests = findViewById(R.id.layoutRequests);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        tvComponentRequest = findViewById(R.id.tvComponentRequest);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        db = FirebaseFirestore.getInstance();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentId != null) {
                    db.collection("pedidos").document(componentId)
                            .update("status", "aceptado")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MaterialsDashboardActivity.this, "Solicitud aceptada", Toast.LENGTH_SHORT).show();
                                componentId = null;
                                updateRequestsView();
                            });
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentId != null) {
                    db.collection("pedidos").document(componentId)
                            .update("status", "rechazado")
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MaterialsDashboardActivity.this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
                                componentId = null;
                                updateRequestsView();
                            });
                }
            }
        });

        db.collection("pedidos")
                .whereEqualTo("status", "pendiente")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable com.google.firebase.firestore.FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(MaterialsDashboardActivity.this, "Error al obtener solicitudes", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                    case MODIFIED:
                                        componentId = dc.getDocument().getId();
                                        updateRequestsView();
                                        break;
                                    case REMOVED:
                                        componentId = null;
                                        updateRequestsView();
                                        break;
                                }
                            }
                        } else {
                            componentId = null;
                            updateRequestsView();
                        }
                    }
                });
    }

    private void updateRequestsView() {
        if (componentId != null) {
            tvNoRequests.setVisibility(View.GONE);
            layoutRequests.setVisibility(View.VISIBLE);
            tvComponentRequest.setText("Solicitud de componente: " + componentId);
        } else {
            tvNoRequests.setVisibility(View.VISIBLE);
            layoutRequests.setVisibility(View.GONE);
        }
    }
}
package com.paquete.produccion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText etComponentNumber;
    private Button btnSendRequest;
    private TextView tvNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_dashboard);

        etComponentNumber = findViewById(R.id.etComponentNumber);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        tvNotifications = findViewById(R.id.tvNotifications);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String componentNumber = etComponentNumber.getText().toString();

                if (componentNumber.isEmpty()) {
                    Toast.makeText(ProductionDashboardActivity.this, "Ingrese un número de componente", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Subir solicitud a Firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                HashMap<String, Object> request = new HashMap<>();
                request.put("component", componentNumber);
                request.put("status", "pendiente");

                db.collection("pedidos").add(request)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(ProductionDashboardActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                            etComponentNumber.setText("");
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ProductionDashboardActivity.this, "Error al enviar solicitud", Toast.LENGTH_SHORT).show());
            }
        });

        // Listener para recibir cambios de estado de las solicitudes
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pedidos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                String status = dc.getDocument().getString("status");

                                if ("aceptado".equals(status)) {
                                    tvNotifications.setText("Solicitud aceptada por Materiales");
                                    tvNotifications.setVisibility(View.VISIBLE);
                                } else if ("rechazado".equals(status)) {
                                    tvNotifications.setText("Solicitud rechazada por Materiales");
                                    tvNotifications.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
    }
}
package com.fourbytes.loc8teapp.adapter;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fourbytes.loc8teapp.DataPasser;
import com.fourbytes.loc8teapp.R;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceItem;
import com.fourbytes.loc8teapp.ratesprorecycler.ServiceViewHolder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolder> {
    private Context context;

    private List<ServiceItem> serviceItems;

    private LayoutInflater layoutInflater;

    private String username;

    private HashMap<String, Object> temp;

    private FirebaseFirestore db;

    public ServiceAdapter(Context context, List<ServiceItem> serviceItems, LayoutInflater layoutInflater, String username) {
        this.context = context;
        this.serviceItems = serviceItems;
        this.layoutInflater = layoutInflater;
        this.username = username;

        this.temp = new HashMap<>();
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(context).inflate(R.layout.service_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        int pos = position;

        String serviceId = serviceItems.get(pos).getServiceId();
        String serviceName = serviceItems.get(pos).getServiceName();
        String price = String.valueOf(serviceItems.get(pos).getPrice());
        String rateType = serviceItems.get(pos).getRateType();
        String description = serviceItems.get(pos).getDescription();

        holder.tvServiceName.setText(serviceName);
        holder.tvPrice.setText(price);
        holder.tvRateType.setText(rateType);
        holder.tvDescription.setText(description);

        if (DataPasser.getUsername1() != null) {
            holder.btnEdit.setVisibility(View.INVISIBLE);
        }
        
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final View addServicePopupView = layoutInflater.inflate(R.layout.add_service_popup, null);

                EditText edtServiceName = addServicePopupView.findViewById(R.id.edt_service_name);
                EditText edtPrice = addServicePopupView.findViewById(R.id.edt_price);
                EditText edtRate = addServicePopupView.findViewById(R.id.edt_rate);
                EditText edtDescription = addServicePopupView.findViewById(R.id.edt_description);
                AppCompatButton btnAddServicePopup = addServicePopupView.findViewById(R.id.btn_add);
                AppCompatButton btnCancelPopup = addServicePopupView.findViewById(R.id.btn_cancel);
                AppCompatButton btnDelete = addServicePopupView.findViewById(R.id.btn_delete);

                edtServiceName.setText(serviceName);
                edtPrice.setText(price);
                edtRate.setText(rateType);
                edtDescription.setText(description);

                btnAddServicePopup.setText("save");
                btnDelete.setVisibility(View.VISIBLE);

                dialogBuilder.setView(addServicePopupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                btnAddServicePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();

                        temp.put("service_name", edtServiceName.getText().toString());
                        temp.put("price", edtPrice.getText().toString());
                        temp.put("rate_type", edtRate.getText().toString());
                        temp.put("description", edtDescription.getText().toString());

                        db.collection("pro_profiles")
                                .document(username)
                                .collection("rate")
                                .document(serviceId)
                                .set(temp);

                        dialog.dismiss();
                    }
                });
                
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("pro_profiles")
                                .document(username)
                                .collection("rate")
                                .document(serviceId)
                                .delete();

                        dialog.dismiss();
                    }
                });

                btnCancelPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }
}

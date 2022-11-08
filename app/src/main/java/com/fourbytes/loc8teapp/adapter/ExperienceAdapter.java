package com.fourbytes.loc8teapp.adapter;

import android.app.AlertDialog;
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
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceItem;
import com.fourbytes.loc8teapp.experienceprorecycler.ExperienceViewHolder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceViewHolder> {
    private Context context;
    
    private List<ExperienceItem> experienceItems;
    
    private LayoutInflater layoutInflater;

    private String username;

    private HashMap<String, Object> temp;

    private FirebaseFirestore db;

    public ExperienceAdapter(Context context, List<ExperienceItem> experienceItems, LayoutInflater layoutInflater, String username) {
        this.context = context;
        this.experienceItems = experienceItems;
        this.layoutInflater = layoutInflater;
        this.username = username;

        this.temp = new HashMap<>();
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExperienceViewHolder(LayoutInflater.from(context).inflate(R.layout.experience_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        String experienceId = experienceItems.get(position).getExperienceId();
        String namePosition = experienceItems.get(position).getPosition();
        String company = experienceItems.get(position).getCompany();
        String description = experienceItems.get(position).getDescription();

        holder.tvPosition.setText(namePosition);
        holder.tvCompany.setText(company);
        holder.tvExpDescription.setText(description);

        if (DataPasser.getUsername1() != null) {
            holder.btnExpEdit.setVisibility(View.INVISIBLE);
        }
        
        holder.btnExpEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                final View addExperiencePopupView = layoutInflater.inflate(R.layout.add_experience_popup, null);

                EditText edtPosition = addExperiencePopupView.findViewById(R.id.edt_position);
                EditText edtCompany = addExperiencePopupView.findViewById(R.id.edt_company);
                EditText edtDescription = addExperiencePopupView.findViewById(R.id.edt_description);
                AppCompatButton btnAddExperiencePopup = addExperiencePopupView.findViewById(R.id.btn_add);
                AppCompatButton btnCancelPopup = addExperiencePopupView.findViewById(R.id.btn_cancel);
                AppCompatButton btnDelete = addExperiencePopupView.findViewById(R.id.btn_delete);

                edtPosition.setText(namePosition);
                edtCompany.setText(company);
                edtDescription.setText(description);

                btnAddExperiencePopup.setText("save");
                btnDelete.setVisibility(View.VISIBLE);

                dialogBuilder.setView(addExperiencePopupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                btnAddExperiencePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp.clear();

                        temp.put("position", edtPosition.getText().toString());
                        temp.put("company", edtCompany.getText().toString());
                        temp.put("description", edtDescription.getText().toString());

                        db.collection("pro_profiles")
                                .document(username)
                                .collection("experience")
                                .document(experienceId)
                                .set(temp);

                        dialog.dismiss();
                    }
                });
                
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("pro_profiles")
                                .document(username)
                                .collection("experience")
                                .document(experienceId)
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
        return experienceItems.size();
    }
}

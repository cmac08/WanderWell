package com.example.wanderwell.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.R;
import com.example.wanderwell.database.Repository;
import com.example.wanderwell.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private List<Excursion> excursions;
    private final String vacationStartDate;
    private final String vacationEndDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy", Locale.US);

    public ExcursionAdapter(Context context, List<Excursion> excursions, String vacationStartDate, String vacationEndDate) {
        this.context = context;
        this.excursions = excursions;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
        this.inflater = LayoutInflater.from(context);
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.activity_excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (excursions != null && position < excursions.size()) {
            Excursion current = excursions.get(position);
            holder.nameView.setText(current.getExcursionName());

            Date date = current.getExcursionDate();
            String formattedDate = (date != null) ? dateFormat.format(date) : "No date available";
            holder.dateView.setText(formattedDate);

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("excursionId", current.getExcursionId());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("vacationID", current.getVacationId());
                intent.putExtra("excursionDate", current.getExcursionDate());
                intent.putExtra("vacationStartDate", vacationStartDate);
                intent.putExtra("vacationEndDate", vacationEndDate);
                context.startActivity(intent);
            });

            holder.editButton.setOnClickListener(view -> {
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("excursionId", current.getExcursionId());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("vacationID", current.getVacationId());
                intent.putExtra("excursionDate", current.getExcursionDate());
                intent.putExtra("vacationStartDate", vacationStartDate);
                intent.putExtra("vacationEndDate", vacationEndDate);
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(view -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Excursion")
                        .setMessage("Are you sure you want to delete this excursion?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            new Thread(() -> {
                                Repository repo = new Repository(((android.app.Activity) context).getApplication());
                                repo.delete(current);
                                excursions.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Excursion deleted", Toast.LENGTH_SHORT).show();
                            }).start();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                        .show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return (excursions != null) ? excursions.size() : 0;
    }

    static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        final TextView dateView;
        final ImageButton editButton;
        final ImageButton deleteButton;

        ExcursionViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewExcursionName);
            dateView = itemView.findViewById(R.id.textViewExcursionDate);
            editButton = itemView.findViewById(R.id.buttonEditExcursion);
            deleteButton = itemView.findViewById(R.id.buttonDeleteExcursion);
        }
    }
}

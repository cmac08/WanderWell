package com.example.wanderwell.UI;

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
import com.example.wanderwell.entities.Vacation;
import com.example.wanderwell.model.VacationListViewModel;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacationList;
    private final Context context;
    private final LayoutInflater mInflater;
    private final Repository repository;
    private final VacationListViewModel viewModel;



    public VacationAdapter(Context context, Repository repository, VacationListViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.repository = repository;
        this.viewModel = viewModel;

    }

    // ViewHolder class
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        private final TextView vacationDateRangeView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.vacationDetails); //TextView ID
            ImageButton editButton = itemView.findViewById(R.id.editButton);
            ImageButton deleteButton = itemView.findViewById(R.id.deleteButton);
            vacationDateRangeView = itemView.findViewById(R.id.textViewVacationDateRange);


            // Click on Vacation Name
            vacationItemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vacationList != null) {
                    openVacationDetails(vacationList.get(position));
                }
            });

            // Click on Edit Button
            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vacationList != null) {
                    Toast.makeText(context, "Opening Your Dream Vacation...", Toast.LENGTH_SHORT).show();
                    openVacationDetails(vacationList.get(position));

                }
            });
            // Handle Delete Button click
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && vacationList != null) {
                    confirmDelete(vacationList.get(position));
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation current = vacationList.get(position);

        if (current != null && current.getVacationName() != null) {
            holder.vacationItemView.setText(current.getVacationName());
        } else {
            holder.vacationItemView.setText(R.string.no_vacation_name);
        }
        assert current != null;
        String start = current.getStartDate() != null ? current.getStartDate() : "?";
        String end = current.getEndDate() != null ? current.getEndDate() : "?";
        holder.vacationDateRangeView.setText("Vacation: " + start + " - " + end);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VacationDetails.class);
            intent.putExtra("vacationId", current.getVacationId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return (vacationList != null) ? vacationList.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        vacationList = vacations;
        notifyDataSetChanged();
    }

    private void openVacationDetails(Vacation vacation) {
        Intent intent = new Intent(context, VacationDetails.class);
        intent.putExtra("id", vacation.getVacationId());
        intent.putExtra("title", vacation.getVacationName());
        intent.putExtra("hotel", vacation.getHotelName());
        intent.putExtra("startDate", vacation.getStartDate());
        intent.putExtra("endDate", vacation.getEndDate());
        context.startActivity(intent);
    }

    private void confirmDelete(Vacation vacation) {
        Repository.databaseExecutor.execute(() -> {
            List<Excursion> relatedExcursions = repository.getRelatedExcursionsSync(vacation.getVacationId());
            ((VacationList) context).runOnUiThread(() -> {
                String message;
                if (relatedExcursions.isEmpty()) {
                    message = "Are you sure you want to delete this vacation?";
                    new android.app.AlertDialog.Builder(context)
                            .setTitle("Delete Vacation")
                            .setMessage(message)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                deleteVacation(vacation);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    message = "You are not permitted to delete a vacation with Excursions. Delete the excursions first then delete the vacation.";
                    new android.app.AlertDialog.Builder(context)
                            .setTitle("Delete Vacation Not Allowed")
                            .setMessage(message)
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
            });
        });
    }

    private void deleteVacation(Vacation vacation) {
        Repository.databaseExecutor.execute(() -> {
            // Delete related excursions first
            List<Excursion> relatedExcursions = repository.getRelatedExcursionsSync(vacation.getVacationId());
            for (Excursion excursion : relatedExcursions) {
                repository.delete(excursion);
            }
            // Then delete the vacation
            repository.delete(vacation);
            ((VacationList) context).runOnUiThread(() -> {
                vacationList.remove(vacation);
                notifyDataSetChanged();
                Toast.makeText(context, "Vacation deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }
}

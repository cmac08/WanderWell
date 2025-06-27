package com.example.wanderwell.Search;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderwell.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SearchResult result);
    }

    private List<SearchResult> results = new ArrayList<>();
    private final OnItemClickListener listener;

    public SearchResultAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setResults(List<SearchResult> newResults) {
        if (newResults == null || newResults.equals(this.results)) return;
        this.results = newResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_result, parent, false);
        return new SearchResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult result = results.get(position);
        holder.bind(result, listener);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private final TextView textTitle;
        private final TextView textType;
        private final TextView textDate;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textType = itemView.findViewById(R.id.textType);
            textDate = itemView.findViewById(R.id.textDate);
        }

        public void bind(SearchResult result, OnItemClickListener listener) {
            textTitle.setText(result.title);
            textType.setText(result.type);
            textDate.setText(result.dateInfo);

            itemView.setOnClickListener(v -> listener.onItemClick(result));
        }
    }
}

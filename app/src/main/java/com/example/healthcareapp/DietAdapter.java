package com.example.healthcareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder> {

    private List<DietItem> dietList;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public DietAdapter(List<DietItem> dietList, OnDeleteClickListener listener) {
        this.dietList = dietList;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diet, parent, false);
        return new DietViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder holder, int position) {
        holder.tvDietItem.setText("- " + dietList.get(position).itemName);
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }

    public static class DietViewHolder extends RecyclerView.ViewHolder {
        TextView tvDietItem;
        ImageButton btnDelete;

        public DietViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDietItem = itemView.findViewById(R.id.tvDietItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteDiet);
        }
    }
}
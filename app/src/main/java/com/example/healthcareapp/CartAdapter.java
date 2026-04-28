package com.example.healthcareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private CheckupTest[] cartItems;
    private OnRemoveClickListener listener;

    public interface OnRemoveClickListener {
        void onRemoveClicked(CheckupTest test);
    }

    public CartAdapter(CheckupTest[] cartItems, OnRemoveClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_test, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CheckupTest test = cartItems[position];
        holder.tvName.setText(test.getTestName());
        holder.tvPrep.setText("Prep: " + test.getTestPrep());
        holder.tvPrice.setText(test.getTestPrice());

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveClicked(test);
            }
        });
    }

    @Override
    public int getItemCount() { return cartItems.length; }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrep, tvPrice;
        MaterialButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartTestName);
            tvPrep = itemView.findViewById(R.id.tvCartTestPrep);
            tvPrice = itemView.findViewById(R.id.tvCartTestPrice);
            btnRemove = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }
}
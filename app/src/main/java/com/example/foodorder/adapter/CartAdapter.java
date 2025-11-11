package com.example.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.databinding.ItemCartBinding;
import com.example.foodorder.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems = new ArrayList<>();
    private OnCartItemActionListener listener;

    public interface OnCartItemActionListener {
        void onQuantityChanged(CartItem cartItem);
        void onRemoveItem(CartItem cartItem);
    }

    public CartAdapter(OnCartItemActionListener listener) {
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem cartItem) {
            binding.tvFoodName.setText(cartItem.getFoodName());
            binding.tvFoodPrice.setText(String.format(Locale.US, "$%.2f", cartItem.getFoodPrice()));
            binding.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", cartItem.getSubtotal()));

            binding.btnMinus.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    binding.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
                    binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", cartItem.getSubtotal()));
                    if (listener != null) {
                        listener.onQuantityChanged(cartItem);
                    }
                }
            });

            binding.btnPlus.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                binding.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
                binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", cartItem.getSubtotal()));
                if (listener != null) {
                    listener.onQuantityChanged(cartItem);
                }
            });

            binding.btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(cartItem);
                }
            });
        }
    }
}

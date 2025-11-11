package com.example.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.databinding.ItemOrderBinding;
import com.example.foodorder.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders = new ArrayList<>();
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Order order) {
            binding.tvOrderId.setText("Order #" + order.getId());
            binding.tvOrderStatus.setText(order.getStatus());
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);
            binding.tvOrderDate.setText(sdf.format(new Date(order.getTimestamp())));
            
            binding.tvOrderItems.setText(getOrderItemsSummary(order.getOrderItems()));
            binding.tvOrderTotal.setText(String.format(Locale.US, "$%.2f", order.getTotalAmount()));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        }

        private String getOrderItemsSummary(String orderItemsJson) {
            try {
                // Simple extraction from JSON - just return as is for now
                return orderItemsJson.length() > 50 ? orderItemsJson.substring(0, 50) + "..." : orderItemsJson;
            } catch (Exception e) {
                return "Order items";
            }
        }
    }
}

package com.example.foodorder.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.databinding.ItemMessageBinding;
import com.example.foodorder.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageBinding binding;

        public MessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.tvMessage.setText(message.getMessageText());
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            binding.tvTimestamp.setText(sdf.format(new Date(message.getTimestamp())));

            // Set message alignment based on sender
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.cvMessage.getLayoutParams();
            if (message.isSentByUser()) {
                // User messages on right
                params.gravity = Gravity.END;
                binding.cvMessage.setCardBackgroundColor(
                        binding.getRoot().getContext().getResources().getColor(R.color.purple_200, null));
            } else {
                // Store messages on left
                params.gravity = Gravity.START;
                binding.cvMessage.setCardBackgroundColor(
                        binding.getRoot().getContext().getResources().getColor(android.R.color.darker_gray, null));
            }
            binding.cvMessage.setLayoutParams(params);
        }
    }
}

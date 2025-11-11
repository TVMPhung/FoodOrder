package com.example.foodorder.ui.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.MessageAdapter;
import com.example.foodorder.databinding.ActivityChatBinding;
import com.example.foodorder.model.Message;
import com.example.foodorder.repository.MessageRepository;
import com.example.foodorder.utils.SessionManager;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private MessageRepository messageRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageRepository = new MessageRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupToolbar();
        setupRecyclerView();
        setupButtons();
        loadMessages();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chat with Store");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.rvMessages.setLayoutManager(layoutManager);
        binding.rvMessages.setAdapter(messageAdapter);
    }

    private void setupButtons() {
        binding.btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        int userId = sessionManager.getUserId();
        messageRepository.getMessagesByUserId(userId).observe(this, messages -> {
            if (messages != null) {
                messageAdapter.setMessages(messages);
                if (!messages.isEmpty()) {
                    binding.rvMessages.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    private void sendMessage() {
        String messageText = binding.etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        int userId = sessionManager.getUserId();
        Message message = new Message(userId, messageText, true);
        messageRepository.insert(message);

        binding.etMessage.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

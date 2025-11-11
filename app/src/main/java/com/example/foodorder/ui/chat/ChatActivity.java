package com.example.foodorder.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.MessageAdapter;
import com.example.foodorder.databinding.ActivityChatBinding;
import com.example.foodorder.model.Message;
import com.example.foodorder.repository.MessageRepository;
import com.example.foodorder.service.ChatbotService;
import com.example.foodorder.utils.SessionManager;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private MessageRepository messageRepository;
    private SessionManager sessionManager;
    private ChatbotService chatbotService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageRepository = new MessageRepository(getApplication());
        sessionManager = new SessionManager(this);
        chatbotService = ChatbotService.getInstance(this);
        handler = new Handler(Looper.getMainLooper());

        setupToolbar();
        setupRecyclerView();
        setupButtons();
        loadMessages();
        
        // Send welcome message if no messages exist
        sendWelcomeMessageIfNeeded();
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
    
    private void sendWelcomeMessageIfNeeded() {
        int userId = sessionManager.getUserId();
        messageRepository.getMessagesByUserId(userId).observe(this, messages -> {
            if (messages == null || messages.isEmpty()) {
                // Send welcome message from chatbot
                handler.postDelayed(() -> {
                    String welcomeText = "Hello! Welcome to our restaurant! 🍕 I'm your AI assistant. " +
                            "Ask me about our menu, fast food, healthy options, locations, or anything else!";
                    Message botMessage = new Message(userId, welcomeText, false);
                    messageRepository.insert(botMessage);
                }, 500);
            }
        });
    }

    private void sendMessage() {
        String messageText = binding.etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        int userId = sessionManager.getUserId();
        
        // Save user message
        Message userMessage = new Message(userId, messageText, true);
        messageRepository.insert(userMessage);

        binding.etMessage.setText("");
        
        // Process query with chatbot and respond after a short delay
        handler.postDelayed(() -> {
            String response = chatbotService.processQuery(messageText);
            Message botMessage = new Message(userId, response, false);
            messageRepository.insert(botMessage);
        }, 800);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

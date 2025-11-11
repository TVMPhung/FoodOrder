package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.MessageDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.Message;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageRepository {
    private MessageDao messageDao;
    private ExecutorService executorService;

    public MessageRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        messageDao = database.messageDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Message message) {
        executorService.execute(() -> messageDao.insert(message));
    }

    public LiveData<List<Message>> getMessagesByUserId(int userId) {
        return messageDao.getMessagesByUserId(userId);
    }

    public void updateMessageStatus(int messageId, String status) {
        executorService.execute(() -> messageDao.updateMessageStatus(messageId, status));
    }

    public void clearMessages(int userId) {
        executorService.execute(() -> messageDao.clearMessages(userId));
    }
}

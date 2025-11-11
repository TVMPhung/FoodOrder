package com.example.foodorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodorder.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    long insert(Message message);

    @Query("SELECT * FROM messages WHERE userId = :userId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesByUserId(int userId);

    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    void updateMessageStatus(int messageId, String status);

    @Query("DELETE FROM messages WHERE userId = :userId")
    void clearMessages(int userId);
}

package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.UserDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user, OnUserInsertedListener listener) {
        executorService.execute(() -> {
            long id = userDao.insert(user);
            if (listener != null) {
                listener.onUserInserted(id);
            }
        });
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void login(String email, String password, OnLoginListener listener) {
        executorService.execute(() -> {
            User user = userDao.login(email, password);
            if (listener != null) {
                listener.onLoginResult(user);
            }
        });
    }

    public void getUserByEmail(String email, OnUserFetchListener listener) {
        executorService.execute(() -> {
            User user = userDao.getUserByEmail(email);
            if (listener != null) {
                listener.onUserFetched(user);
            }
        });
    }

    public void getUserByUsername(String username, OnUserFetchListener listener) {
        executorService.execute(() -> {
            User user = userDao.getUserByUsername(username);
            if (listener != null) {
                listener.onUserFetched(user);
            }
        });
    }

    public void getLoggedInUser(OnUserFetchListener listener) {
        executorService.execute(() -> {
            User user = userDao.getLoggedInUser();
            if (listener != null) {
                listener.onUserFetched(user);
            }
        });
    }

    public void logoutAllUsers() {
        executorService.execute(() -> userDao.logoutAllUsers());
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public interface OnUserInsertedListener {
        void onUserInserted(long userId);
    }

    public interface OnLoginListener {
        void onLoginResult(User user);
    }

    public interface OnUserFetchListener {
        void onUserFetched(User user);
    }
}

package com.example.foodorder.ui.review;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.ReviewAdapter;
import com.example.foodorder.databinding.ActivityReviewBinding;
import com.example.foodorder.model.Review;
import com.example.foodorder.repository.ReviewRepository;
import com.example.foodorder.utils.SessionManager;

public class ReviewActivity extends AppCompatActivity {
    private ActivityReviewBinding binding;
    private ReviewAdapter reviewAdapter;
    private ReviewRepository reviewRepository;
    private SessionManager sessionManager;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reviewRepository = new ReviewRepository(getApplication());
        sessionManager = new SessionManager(this);

        foodId = getIntent().getIntExtra("FOOD_ID", -1);
        if (foodId == -1) {
            finish();
            return;
        }

        setupToolbar();
        setupRecyclerView();
        setupButtons();
        loadReviews();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reviews");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        reviewAdapter = new ReviewAdapter();
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);
    }

    private void setupButtons() {
        binding.btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void loadReviews() {
        reviewRepository.getReviewsByFoodId(foodId).observe(this, reviews -> {
            if (reviews != null && !reviews.isEmpty()) {
                reviewAdapter.setReviews(reviews);
                binding.rvReviews.setVisibility(View.VISIBLE);
                binding.tvEmptyReviews.setVisibility(View.GONE);
            } else {
                binding.tvEmptyReviews.setVisibility(View.VISIBLE);
            }
        });
    }

    private void submitReview() {
        String reviewText = binding.etReview.getText().toString().trim();
        float rating = binding.ratingBar.getRating();

        if (reviewText.isEmpty()) {
            binding.etReview.setError("Please write a review");
            binding.etReview.requestFocus();
            return;
        }

        if (rating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        String username = sessionManager.getUsername();

        Review review = new Review(userId, foodId, username, rating, reviewText);
        reviewRepository.insert(review);

        Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
        binding.etReview.setText("");
        binding.ratingBar.setRating(5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

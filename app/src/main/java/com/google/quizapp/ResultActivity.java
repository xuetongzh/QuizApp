package com.google.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView congratulations, score;
    private Button newQuiz, finish;
    private String username;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        username = getIntent().getStringExtra("userName");
        congratulations = findViewById(R.id.congratulations);
        congratulations.setText("Congratulations! " + username + "!");
        score = findViewById(R.id.score);
        score.setText(getIntent().getIntExtra("correct", 0) + "/5");

        newQuiz = findViewById(R.id.newQuiz);
        newQuiz.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            intent.putExtra("userName", username);
            startActivity(intent);
            finish();
        });
        finish = findViewById(R.id.finish);
        finish.setOnClickListener(view -> finish());
    }
}
package com.google.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.quizapp.adapter.QuestionAdapter;
import com.google.quizapp.api.Api;
import com.google.quizapp.api.ApiResult;
import com.google.quizapp.bean.Data;
import com.google.quizapp.bean.Question;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView quiz_title, number;
    ViewPager2 questionView;//News list control
    List<Question> questions = new ArrayList<>();
    QuestionAdapter questionAdapter;
    private Button next;
    private ProgressBar progressBar;
    private int answered = 1;
    private int correct = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        EventBus.getDefault().register(this);
        quiz_title = findViewById(R.id.quiz_title);
        quiz_title.setText("Welcome " + getIntent().getStringExtra("userName") + "!");

        questionView = findViewById(R.id.viewPager2);
        progressBar = findViewById(R.id.progressBar);
        number = findViewById(R.id.number);

        number.setText(answered + "/5");
        progressBar.setProgress(answered);

        next = findViewById(R.id.next);
        next.setOnClickListener(view -> {
            questionView.setCurrentItem(questionView.getCurrentItem() + 1);
            answered++;
            progressBar.setProgress(answered);
            if (answered > 5) {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                intent.putExtra("correct", correct);
                startActivity(intent);
                finish();
                return;
            }
            number.setText(answered + "/5");
        });

        intData();
    }

    private void intData() {

        for (int i = 0; i < 5; i++) {
            //Add five same question
//            questions.add(new Question("In 1768, Captain James Cook set out to explore which ocean?", "Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "A", "The first voyage of James Cook was a combined Royal Navy and Royal Society expedition to the south Pacific Ocean aboard HMS Endeavour, from 1768 to 1771."));

//            Get five questions from the interface
//            Api.INSTANCE.get((success, errCode, data) -> {
//                if (data != null){
//                    if (data.getData() != null){
//                        questions.add(data.getData());
//                    }
//                }
//            });
        }

//        Add five different question
        questions.add(new Question("In 1768, Captain James Cook set out to explore which ocean?", "Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "A", "The first voyage of James Cook was a combined Royal Navy and Royal Society expedition to the south Pacific Ocean aboard HMS Endeavour, from 1768 to 1771."));
        questions.add(new Question("What is the speed of sound?", "240 km/h", "1236 km/h", "120 km/h", "B", "The velocity of sound in air at 20oC is 343.2 m/s which translates to 1,236 km/h."));
        questions.add(new Question("What was the first country to use tanks in combat during World War I?", "France", "Germany", "Britain", "C", "Tanks were used in battle for the first time, by the British."));
        questions.add(new Question("Which of the following songs was the top-selling hit in 2019?", "Someone You Loved", "Old Town Road", "I Don’t Care", "B", "Lil Nas X's breakout blockbuster “Old Town Road” wraps 2019 as the bestselling song, with 1.536 million purchases."));
        questions.add(new Question("In which country is Transylvania?", "Bulgaria", "Romania", "Croatia", "B", "Transylvania, Romanian Transilvania, historic eastern European region, now in Romania."));

        questionAdapter = new QuestionAdapter(this, questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        questionView.setAdapter(questionAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLocation(Data data) {
        switch (data.getData()) {
            case "Correct":
                correct++;
                break;
            case "Wrong":
                break;
        }
    }
}
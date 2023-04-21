package com.google.quizapp.bean;

import androidx.annotation.NonNull;

public class Question {
    private String question;
    private String answer_a;
    private String answer_b;
    private String answer_c;
    private String answer;
    private String analytic;

    public Question(String question, String answer_a, String answer_b, String answer_c, String answer, String analytic) {
        this.question = question;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer = answer;
        this.analytic = analytic;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer_a='" + answer_a + '\'' +
                ", answer_b='" + answer_b + '\'' +
                ", answer_c='" + answer_c + '\'' +
                ", answer='" + answer + '\'' +
                ", analytic='" + analytic + '\'' +
                '}';
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_a() {
        return answer_a;
    }

    public void setAnswer_a(String answer_a) {
        this.answer_a = answer_a;
    }

    public String getAnswer_b() {
        return answer_b;
    }

    public void setAnswer_b(String answer_b) {
        this.answer_b = answer_b;
    }

    public String getAnswer_c() {
        return answer_c;
    }

    public void setAnswer_c(String answer_c) {
        this.answer_c = answer_c;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalytic() {
        return analytic;
    }

    public void setAnalytic(String analytic) {
        this.analytic = analytic;
    }
}

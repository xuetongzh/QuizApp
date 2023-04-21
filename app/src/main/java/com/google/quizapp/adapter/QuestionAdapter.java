package com.google.quizapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.quizapp.R;
import com.google.quizapp.bean.Data;
import com.google.quizapp.bean.Question;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    //Dataset
    private final List<Question> questions;
    //Context
    private final Context context;

    //Constructor
    public QuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    //Using LayoutInflater to get the layout, itemView is the obtained layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View itemView = LayoutInflater.from(context).inflate(R.layout.news_list, null);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        // We set the parent layout for the view
        return new ViewHolder(itemView);
    }

    //Bind data to each sub-item holder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.title.setText("Your " + (position + 1) + " question is：");
        holder.question.setText(question.getQuestion());
        holder.first.setText(question.getAnswer_a());
        holder.second.setText(question.getAnswer_b());
        holder.third.setText(question.getAnswer_c());

        holder.first.setOnClickListener(view -> {
            if ("A".equals(question.getAnswer())) {
                holder.first.setBackgroundResource(R.drawable.style_button_green);
                EventBus.getDefault().post(new Data("Correct"));
            } else {
                holder.first.setBackgroundResource(R.drawable.style_button_red);
                EventBus.getDefault().post(new Data("Wrong"));
            }
        });

        holder.second.setOnClickListener(view -> {
            if ("B".equals(question.getAnswer())) {
                holder.second.setBackgroundResource(R.drawable.style_button_green);
                EventBus.getDefault().post(new Data("Correct"));
            } else {
                holder.second.setBackgroundResource(R.drawable.style_button_red);
                EventBus.getDefault().post(new Data("Wrong"));
            }
        });

        holder.third.setOnClickListener(view -> {
            if ("C".equals(question.getAnswer())) {
                holder.third.setBackgroundResource(R.drawable.style_button_green);
                EventBus.getDefault().post(new Data("Correct"));
            } else {
                holder.third.setBackgroundResource(R.drawable.style_button_red);
                EventBus.getDefault().post(new Data("Wrong"));
            }
        });
    }

    //Get dataset size
    @Override
    public int getItemCount() {
        return questions.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView question;
        private final Button first, second, third;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.qTitle);
            question = (TextView) itemView.findViewById(R.id.question);
            first = (Button) itemView.findViewById(R.id.first);
            second = (Button) itemView.findViewById(R.id.second);
            third = (Button) itemView.findViewById(R.id.third);

            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(view, questions.get(getLayoutPosition()));
                }
            });
        }
    }

    /**
     * Set the interface of listener of item
     */
    public interface OnItemClickListener {
        /**
         *
         * @param view     The view of the clicked item
         * @param newsBean The data of the clicked item
         */
        public void OnItemClick(View view, Question newsBean);
    }

    //Require external access, so need to implement a set method that easy to invocate
    private QuestionAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(QuestionAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

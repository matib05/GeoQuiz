package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG  = "QuizActivity";
    private static final String KEY_INDEX = "INDEX";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPreviousButton;
    private Button mCheatButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                if (mCurrentIndex == -1) {
                    mCurrentIndex = mQuestionBank.length-1;
                }
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    //To start -> create, start, resume
    //To end -> pause, stop, destroy

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        if (mIsCheater) {
            Toast.makeText(QuizActivity.this, "Cheater", Toast.LENGTH_LONG).show();
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }
}
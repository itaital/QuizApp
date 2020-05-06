package com.example.koralzakai.quizapp;



import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static com.example.koralzakai.quizapp.StartingScreenActivity.EXTRA_CATEGORY_ID;

//java code for the quiz layout.
public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;// 30 seconds

    //keys to save when rotating.
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MUSIC_COUNT = "keyMusicCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    public static final String EXTRA_songNum = "extraSongNum";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewDifficulty;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;
    int categoryID;
    String difficulty;
    int numOfMusicQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        numOfMusicQuestion =1;
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();// get the activity that create this activity
        categoryID = intent.getIntExtra(EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(StartingScreenActivity.EXTRA_CATEGORY_NAME);
        difficulty = intent.getStringExtra(StartingScreenActivity.EXTRA_DIFFICULTY);

        textViewCategory.setText("Category: " + categoryName);
        textViewDifficulty.setText("Difficulty: " + difficulty);
        // if we have instance state we want to restore the previous questions and not to make a new data base
        if (savedInstanceState == null) {
            //if the app was destroyed and rebuild again
            //create the data base once
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCountTotal = questionList.size();
            //Collections.shuffle(questionList);
            showNextQuestion();
        } else {
            //restore the data.
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            //insert the all data base into a list.
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            numOfMusicQuestion = savedInstanceState.getInt(KEY_MUSIC_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                //our question has not answered yet.
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }
    }

    public void ConfimClicked(View view) //when clicked on confim
    {
        if (!answered) {
            //check if the user chose an answer.
            if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                checkAnswer();
            } else {
                Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        } else {
            showNextQuestion();
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        //if this is more questions in the database
        if (questionCounter < questionCountTotal) {
            stopService(new Intent(this, MyService.class));

            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            if(questionCounter>1)
            {
                numOfMusicQuestion = 4;
                onStart();
            }
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false; // before an answer was chosen
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;//initialize the timer to 30 sec
            startCountDown();

        } else {
            finishQuiz();// if there is no more questions, end the activity
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;//update the timer
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0; //out of time
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);//less than 10 seconds left
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1; //index +1

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        //if there is more questions in the data base show "next"
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        stopService(new Intent(this, MyService.class));

        Intent resultIntent = new Intent(); // for the first screen
        resultIntent.putExtra(EXTRA_SCORE, score); // return the score
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onStop()//press on home button
    {
        super.onStop();
        stopService(new Intent(this, MyService.class));
        // insert here your instructions
    }
    @Override
    protected void onStart() {
        super.onStart();
        //on start called right after onCreate
        if(categoryID ==3 && difficulty.equals("Easy"))
        {
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(EXTRA_songNum,numOfMusicQuestion);
            startService(intent);
        }
        if(categoryID ==3 && difficulty.equals("Hard"))
        {
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(EXTRA_songNum, 2);
            startService(intent);
        }
        if(categoryID ==3 && difficulty.equals("Medium"))
        {
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(EXTRA_songNum, 3);
            startService(intent);
        }

    }

    @Override
    public void onBackPressed() {
        // if the user pressed twice within one second (2000 = one second)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            //finishQuiz();
        }
        else {
            //check if the user indeed want to exit the quiz
           // Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            finishQuiz();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit the quiz?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        //updating the time of pressing.
        backPressedTime = System.currentTimeMillis();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        stopService(new Intent(this, MyService.class));
    }
    //when rotate the app, save the values first in the keys
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putInt(KEY_MUSIC_COUNT, numOfMusicQuestion);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

}

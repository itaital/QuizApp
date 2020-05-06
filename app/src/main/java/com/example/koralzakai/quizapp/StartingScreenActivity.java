package com.example.koralzakai.quizapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.koralzakai.quizapp.difficulty_adapter_activity.*;

public class StartingScreenActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    private static final int REQUEST_CODE_DIFFICULTY = 2;

    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private ImageButton ibPogramingCategory;
    private ImageButton ibMusicCategory;
    private ImageButton ibGeographyCategory;

    private TextView textViewHighscore;
    private TextView progText;
    private TextView geoText;
    private TextView musicText;

    private BroadcastReceiver mReceiver;

    public int categoryID = 0;
    public String categoryName;
    public String difficulty = "Easy";
    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starting_screen);
        ibPogramingCategory = findViewById(R.id.imageButtonPrograming);
        ibMusicCategory = findViewById(R.id.imageButtonMusic);
        ibGeographyCategory = findViewById(R.id.imageButtonGeography);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        progText = findViewById(R.id.Programing);
        geoText = findViewById(R.id.Geography);
        musicText = findViewById(R.id.Music);

        initImageButtons();
        loadCategories();
        loadDifficultyLevels();
        loadHighscore();

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        imageButtonsInit();
        mReceiver = new BatteryBroadcastReceiver();
    }

    private void initImageButtons()
    {
        ibPogramingCategory.setBackground(getDrawable(R.drawable.regular_border));
        ibMusicCategory.setBackground(getDrawable(R.drawable.regular_border));
        ibGeographyCategory.setBackground(getDrawable(R.drawable.regular_border));

        progText.setTextColor(getColor(R.color.colorBackground));
        geoText.setTextColor(getColor(R.color.colorBackground));
        musicText.setTextColor(getColor(R.color.colorBackground));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // the menu:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setDifficulty:     // settings button clicked.

                Intent intent = new Intent(this, difficulty_adapter_activity.class);
                startActivityForResult(intent, REQUEST_CODE_DIFFICULTY);


                //Dialog builder = onCreateDialog(this);
                //builder.show();

                return true;
            case R.id.exit:
                this.onDestroy();
                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void imageButtonsInit()
    {
        ibPogramingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibGeographyCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibPogramingCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorImageButon));
                geoText.setTextColor(getColor(R.color.colorBackground));
                musicText.setTextColor(getColor(R.color.colorBackground));
                categoryID = 1;
                categoryName = "Programming";
            }
        });
        progText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibGeographyCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibPogramingCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorImageButon));
                geoText.setTextColor(getColor(R.color.colorBackground));
                musicText.setTextColor(getColor(R.color.colorBackground));
                categoryID = 1;
                categoryName = "Programming";
            }
        });


        ibGeographyCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibPogramingCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibGeographyCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorBackground));
                geoText.setTextColor(getColor(R.color.colorImageButon));
                musicText.setTextColor(getColor(R.color.colorBackground));
                categoryID = 2;
                categoryName = "Geography";
            }
        });

        geoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPogramingCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibGeographyCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorBackground));
                geoText.setTextColor(getColor(R.color.colorImageButon));
                musicText.setTextColor(getColor(R.color.colorBackground));
                categoryID = 2;
                categoryName = "Geography";
            }
        });

        ibMusicCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibGeographyCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibPogramingCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorBackground));
                geoText.setTextColor(getColor(R.color.colorBackground));
                musicText.setTextColor(getColor(R.color.colorImageButon));
                categoryID = 3;
                categoryName = "Music";
            }
        });

        musicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibGeographyCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibPogramingCategory.setBackground(getDrawable(R.drawable.regular_border));
                ibMusicCategory.setBackground(getDrawable(R.drawable.border_for_imagebutton));

                progText.setTextColor(getColor(R.color.colorBackground));
                geoText.setTextColor(getColor(R.color.colorBackground));
                musicText.setTextColor(getColor(R.color.colorImageButon));
                categoryID = 3;
                categoryName = "Music";
            }
        });
    }


    private void startQuiz() {
        // get selected radio button from radioGroup
        //int selectedRadioButtonId = RadioGroupLevels.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        //RadioButtonSeleced = (RadioButton) findViewById(selectedRadioButtonId);
        //String difficulty = RadioButtonSeleced.getText().toString();
        if(categoryID==0)
        {
            Toast.makeText(this, "Please choose quiz category first." , Toast.LENGTH_SHORT).show();
        }
        //send question activity category and difficulty
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
        if (requestCode == REQUEST_CODE_DIFFICULTY) {
            if (resultCode == RESULT_OK) {
                difficulty = data.getStringExtra(difficulty_adapter_activity.EXTRA_DIFFICULTY);
            }
        }
    }

    private void loadCategories() {
       /* QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);*/
    }

    private void loadDifficultyLevels() {
       /* String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);*/
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        private final static String BATTERY_LEVEL = "level";
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BATTERY_LEVEL, 0);
            String batteryString = "Low battery " + level + "%";
            if(level <= 72)
            {
                Toast.makeText(context, batteryString , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }
}

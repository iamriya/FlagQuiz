package com.mobmaxime.flagquiz;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FlagQuiz";
    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Map<String, Boolean> regionsMap;
    private int guessRows = 1;
    private Random random;
    private Handler handler;
    private Animation shakeAnimation;

    private TextView questionNumberTextView;
    private TextView answerTextView;
    private ImageView flagImageView;
    private TableLayout buttonTableLayout;

    private int totalGuesses; // number of guesses made
    private int correctAnswers; // number of correct guesses
    private String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.actionbar_main);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (null != intent) {

            int numberData = intent.getIntExtra("level", 0);
            guessRows = numberData + guessRows;
            init();
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        // Options name

        fileNameList = new ArrayList<String>();
        // Quiz all countryname Here
        quizCountriesList = new ArrayList<String>();
        regionsMap = new HashMap<String, Boolean>();
        // Random Number generator
        random = new Random();
        // timeout
        handler = new Handler();
        // Use for shake animation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.incorrect_shake);
        // How many time you want to shake
        shakeAnimation.setRepeatCount(3);
        // List of all regiouns
        String[] regionNames = getResources().getStringArray(
                R.array.regionsList);

        for (String region : regionNames) {
            regionsMap.put(region, true);
        }
        // It shows the textview at which stage you selected
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        // Flag imageview
        flagImageView = (ImageView) findViewById(R.id.flagImageView);
        buttonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);
        questionNumberTextView.setText(getResources().getString(
                R.string.question)
                + " 1 " + getResources().getString(R.string.of) + " 10");
        resetQuiz();

    }

    private void resetQuiz() {
        // TODO Auto-generated method stub
        AssetManager assets = getAssets();
        fileNameList.clear();

        try {
            Set<String> regions = regionsMap.keySet();

            for (String region : regions) {
                if (regionsMap.get(region)) {
                    String[] paths = assets.list(region);

                    for (String path : paths)
                        fileNameList.add(path.replace(".png", ""));
                    // Toast.makeText(this, fileNameList.size() + "",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading image file names", e);
        }

        correctAnswers = 0;
        totalGuesses = 0;
        quizCountriesList.clear();

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        while (flagCounter <= 10) {
            int randomIndex = random.nextInt(numberOfFlags);
            String fileName = fileNameList.get(randomIndex);

            if (!quizCountriesList.contains(fileName)) {
                quizCountriesList.add(fileName);
                ++flagCounter;
            }
        }
        loadNextFlag();
    }

    private void loadNextFlag() {
        String nextImageName = quizCountriesList.remove(0);
        correctAnswer = nextImageName;

        answerTextView.setText("");
        questionNumberTextView.setText(getResources().getString(
                R.string.question)
                + " "
                + (correctAnswers + 1)
                + " "
                + getResources().getString(R.string.of) + " 10");

        String region = nextImageName.substring(0, nextImageName.indexOf('-'));
        AssetManager assets = getAssets(); // get app's AssetManager
        InputStream stream;
        try {
            stream = assets.open(region + "/" + nextImageName + ".png");

            Drawable flag = Drawable.createFromStream(stream, nextImageName);
            flagImageView.setImageDrawable(flag);
        } catch (IOException e) {
            Log.e(TAG, "Error loading " + nextImageName, e);
        }
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
            ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();

        Collections.shuffle(fileNameList);

        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < guessRows; row++) {
            TableRow currentTableRow = getTableRow(row);

            for (int column = 0; column < 3; column++) {
                /*Button newGuessButton = (Button) inflater.inflate(
                        R.layout.guess_button, null);
                String fileName = fileNameList.get((row * 3) + column);
                newGuessButton.setText(getCountryName(fileName));
                newGuessButton.setOnClickListener(guessButtonListener);
                currentTableRow.addView(newGuessButton);*/
                View newGuessView = (View) inflater.inflate(
                        R.layout.guess_button, null);
                Button newGuessButton = (Button) newGuessView.findViewById(R.id.newGuessButton);
                String fileName = fileNameList.get((row * 3) + column);
                newGuessButton.setText(getCountryName(fileName));
                newGuessButton.setOnClickListener(guessButtonListener);
                currentTableRow.addView(newGuessView);
            }


        }
        int row = random.nextInt(guessRows);
        int column = random.nextInt(3);
        TableRow randomTableRow = getTableRow(row);
        String countryName = getCountryName(correctAnswer);

        Button btn = randomTableRow.getChildAt(column).findViewById(R.id.newGuessButton);
        btn.setText(countryName);
        //((Button) randomTableRow.getChildAt(column)).setText(countryName);
    }

    private TableRow getTableRow(int row) {
        return (TableRow) buttonTableLayout.getChildAt(row);
    }

    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitGuess((Button) v);
        }
    };

    private void submitGuess(Button guessButton) {
        String guess = guessButton.getText().toString();
        String answer = getCountryName(correctAnswer);
        ++totalGuesses;
        if (guess.equals(answer)) {
            ++correctAnswers;
            answerTextView.setText(answer + "!");
            answerTextView.setTextColor(getResources().getColor(
                    R.color.correct_answer));

            disableButtons();
            if (correctAnswers == 10) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.reset_quiz);

                builder.setMessage(String.format("%d %s, %.02f%% %s",
                        totalGuesses, getResources()
                                .getString(R.string.guesses),
                        (1000 / (double) totalGuesses), getResources()
                                .getString(R.string.correct)));

                builder.setCancelable(false);
                builder.setPositiveButton(R.string.reset_quiz,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resetQuiz();
                            }
                        });
                AlertDialog resetDialog = builder.create();
                resetDialog.show();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextFlag();
                    }
                }, 1000);
            }
        } else {
            flagImageView.startAnimation(shakeAnimation);
            answerTextView.setText(R.string.incorrect_answer);
            answerTextView.setTextColor(getResources().getColor(
                    R.color.incorrect_answer));
            guessButton.setEnabled(false);
        }
    }

    private void disableButtons() {
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row) {
            TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
            for (int i = 0; i < tableRow.getChildCount(); ++i)
                tableRow.getChildAt(i).setEnabled(false);
        }
    }

    public void back_click(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        util.left_right(this);
        finish();
    }
}

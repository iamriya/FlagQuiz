package com.mobmaxime.flagquiz;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.actionbar_home);
        setContentView(R.layout.activity_home);
        //getActionBar().setTitle("GAME");

        init();

    }

    private void init() {
        // TODO Auto-generated method stub
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        View levelButton = findViewById(R.id.level_button);
        levelButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View rateButton = findViewById(R.id.rate_button);
        rateButton.setOnClickListener(this);
        View shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start_button:
                start_game(0);
                break;
            case R.id.level_button:
                open_level_dialog();
                break;
            case R.id.about_button:
                open_about_dialog();
                break;
            case R.id.share_button:
                share_click();
                break;
            case R.id.rate_button:
                rate_click();
                break;
            case R.id.exit_button:
                finish();
                break;
        }

    }

    private void start_game(int num) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("level", num);
        startActivity(i);
        util.right_left(this);
    }

    private void open_level_dialog() {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setTitle(R.string.level)
                .setItems(R.array.levels,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                // TODO Auto-generated method stub
                                start_game(i);
                            }
                        }).show();

    }

    private void open_about_dialog() {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this).setTitle(R.string.about_company)
                .setMessage(R.string.about_text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                    }
                }).show();

    }

    private void share_click() {
        // TODO Auto-generated method stub
//        Intent i = new Intent(getApplicationContext(),
//                Activity_SocialShare.class);
//        startActivity(i);
//        util.right_left(this);
    }

    private void rate_click() {
        // TODO Auto-generated method stub

        final String appPackageName = getPackageName(); // getPackageName() from
        // Context or Activity
        // object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + appPackageName)));
            util.right_left(this);
        }
    }
}

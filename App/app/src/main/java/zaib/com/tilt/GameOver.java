package zaib.com.tilt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.PipedReader;

public class GameOver extends AppCompatActivity
{

    // region Data Members

    // Text View
    private TextView mScoreText, mHighScoreText;

    // Int
    private int mScore, mHighScore;

    // SharedPreferences
    private SharedPreferences mScoreData;

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Setting it to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Don't show the tile bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game_over);

        // Find view by ID
        mScoreText = findViewById(R.id.scoreText);
        mHighScoreText = findViewById(R.id.HighScoreText);

        // Get the score that was passed from player class and store it to mScore
        mScore = getIntent().getIntExtra("Score", 0);

        // Set the score text to be the same as the score that was just passed through
        mScoreText.setText("Score: " + mScore);

        // Set the score data to have a file name called "Game_Data" and set the mode to private
        mScoreData = getSharedPreferences("Game_Data", Context.MODE_PRIVATE);

        // 0 is the default value
        mHighScore = mScoreData.getInt("High_Score", 0);

        // IF current score is greater than higher score
        if (mScore >= mHighScore)
        {
            // Set the high score to score
            mHighScoreText.setText("High Score: " + mScore);

            // Edit the "Game_Data" file
            SharedPreferences.Editor edit = mScoreData.edit();
            // Put the new high score
            edit.putInt("High_Score", mScore);
            // Commit changes
            edit.commit();
        }
        else
        {
            // Else IF score not greater than high score then show the high score
            mHighScoreText.setText("High Score: " + mHighScore);
        }

    }

    // Start Game Button
    public void retry(View view)
    {
        // When pressed load the game class
        startActivity(new Intent(GameOver.this, Game.class));
    }

    // Exit Game Button
    public void exit(View view)
    {
        // Load first activity
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        // Clear all activities on top and close it
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Put extra to pass to first activity true
        intent.putExtra("Exit", true);
        // Load first activity which will close app
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        // Do nothing when back button pressed
    }
}

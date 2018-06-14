package zaib.com.tilt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity
{

    // region Data Members

    // Shared Preferences
    private SharedPreferences mGameData;

    // Text View
    private TextView mHighScoreText;

    // Int
    private int mHighScore;

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

        setContentView(R.layout.activity_main);

        // Set to the view id
        mHighScoreText = findViewById(R.id.highScoreTxt);

        // Set the game data to have a file name called "Game_Data" and set the mode to private
        mGameData = getSharedPreferences("Game_Data", Context.MODE_PRIVATE);

        // 0 is the default value
        mHighScore = mGameData.getInt("High_Score", 0);

        // IF high score is less than 0
        if (mHighScore < 0)
        {
            // Set the high score to 0
            mHighScoreText.setText("High Score: " + 0);

            // Edit the "Game_Data" file
            SharedPreferences.Editor edit = mGameData.edit();
            // Put the high score to 0
            edit.putInt("High_Score", mHighScore);
            // Commit changes
            edit.commit();
        }
        else
        {
            // If High Score greater than 0 then show high score
            mHighScoreText.setText("High Score: " + mHighScore);
        }

        // IF get extra is not null and get bool is true
        if(getIntent().getExtras() != null && getIntent().getExtras().getBoolean("Exit", false))
        {
            // close the app
            finish();
            System.exit(0);
        }

    }

    // Start Game Button
    public void startGame(View view)
    {
        startActivity(new Intent(MainMenu.this, Game.class));
    }

    // Exit Game Button
    public void exitGame(View view)
    {
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        System.exit(0);
    }


}

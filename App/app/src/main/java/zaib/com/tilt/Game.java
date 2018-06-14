package zaib.com.tilt;

import android.app.ActionBar;
import android.hardware.camera2.params.BlackLevelPattern;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends AppCompatActivity
{

    // region Data Members

    // ImageView
    private ImageView mImageBall;
    private ImageView mImageLeftBlock;
    private ImageView mImageRightBlock;

    // List
    private List<ImageView> mListLeftBlock;
    private List<ImageView> mListRightBlock;

    // Classes
    private Player mPlayer;

    // Layout
    private ConstraintLayout mConstraintLayout;

    // Constants - Static
    public static int Screen_Width, Screen_Height;

    // Text
    private TextView mText;

    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // region Make Full Screen

        // Setting it to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Don't show the tile bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // endregion

        // leave screen on don't let it fall asleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // region Get screen height and width

        // Get the screen height and width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // Set height and width
        Screen_Width = displayMetrics.widthPixels;
        Screen_Height = displayMetrics.heightPixels;

        // endregion

        setContentView(R.layout.activity_game);

        // region Find view by id

        // Get the ImageView of the ball by id.
        mImageBall = findViewById(R.id.Ball);

        // Text get the score text by id
        mText = findViewById(R.id.Score);

        // Get the reference of the Constraint Layout by id
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        // endregion

        // Initialise List to Array List
        mListLeftBlock = new ArrayList<>();
        mListRightBlock = new ArrayList<>();

        // Layout params for the Image View
        Constraints.LayoutParams layoutParams = new Constraints.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        // region For Loop

        // Loop 10 times
        for (int i = 1; i < 11; i++)
        {
            // region Left Blocks

            // Create Image View
            mImageLeftBlock = new ImageView(getApplicationContext());

            // Set the image resource in ImageView to the block
            mImageLeftBlock.setImageResource(R.drawable.block);

            // Set the scale type to fix xy
            mImageLeftBlock.setScaleType(ImageView.ScaleType.FIT_XY);

            // Add ImageView to Constraint Layout
            mConstraintLayout.addView(mImageLeftBlock);

            // endregion

            // region Right Blocks
            mImageRightBlock = new ImageView(getApplicationContext());
            mImageRightBlock.setImageResource(R.drawable.block);
            mImageRightBlock.setScaleType(ImageView.ScaleType.FIT_XY);
            mConstraintLayout.addView(mImageRightBlock);
            // endregion

            // Add the left and right blocks to the list
            mListLeftBlock.add(mImageLeftBlock);
            mListRightBlock.add(mImageRightBlock);
        }

        // endregion

        // Initialise the player class and pass 5 parameters
        // Context: This
        // ImageView: of the ball
        // List: of the left blocks
        // List: of the right blocks
        // TextView: Score
        mPlayer = new Player(this, mImageBall, mListLeftBlock, mListRightBlock, mText);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // When application is paused
        // set mStop to true
        mPlayer.mStop = true;
        // Unregister sensor
        mPlayer.unRegister();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // when resume set back to false
        mPlayer.mStop = false;
        // register ball
        mPlayer.register();
    }

    @Override
    public void onBackPressed()
    {
        // Do nothing when back button is pressed
    }




}

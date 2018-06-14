package zaib.com.tilt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class Player implements SensorEventListener
{

    // region Reference

    /*
        Used this site to understand how the accelerometer works
        and how to implement the accelerometer.
        By: Mohit Deshpande
        https://androidkennel.org/android-sensors-game-tutorial/

        Looked at the java doc for android studio to understand how the vibrator works
        https://developer.android.com/reference/android/os/Vibrator
    */

    // endregion

    // region Data Members

    // Boolean
    private Boolean mActive = true;
    public Boolean mStop = false;

    // Context
    private Context mContext;

    // ImageView
    private ImageView mBall;

    // List <ImageView>
    private List<ImageView> mLeftBlock;
    private List<ImageView> mRightBlock;

    // TextView
    private TextView mScoreText;

    // Sensor
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // Vibrator
    private Vibrator mVibrator;

    // Float Ball
    private float mXPosBall;
    private float mXSpeedBall;
    private float mXAccelerationBall;

    // Float Blocks Position
    private float mYPosLeftBlock;
    private float mYPosRightBlock;

    // Float Blocks Speed
    private float mSpeedBlock = 5;
    private float mMaxSpeedBlock = 20;

    // Random
    private Random mRnd;

    // Rect for Collision
    private Rect mRectBall;
    private Rect mRectLeftBlock;
    private Rect mRectRightBlock;

    // Int Score
    private int mScore = 1;
    private int mGap = 150;
    private int mLeftWidth, mRightWidth;

    // endregion

    public Player(Context pContext, ImageView pImageView, List<ImageView> pLeftBlock, List<ImageView> pRightBlock, TextView pText)
    {
        this.mContext = pContext;
        this.mBall = pImageView;
        this.mLeftBlock = pLeftBlock;
        this.mRightBlock = pRightBlock;
        this.mScoreText = pText;

        // Initialise random
        mRnd = new Random();

        // Initialise Rect for collision
        mRectBall = new Rect();
        mRectLeftBlock = new Rect();
        mRectRightBlock = new Rect();

        // Set the blocks position
        setBlockPosition();

        // set xPos to ball xPos
        mXPosBall = mBall.getX();

        // region Initialise sensors / Vibrator

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        // endregion

        register();
    }

    // region Listeners

    public void register()
    {
        // Register listener
        mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_GAME);
    }


    public void unRegister()
    {
        // un register listener
        mSensorManager.unregisterListener(this);
    }

    // endregion

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // IF mStop is false then Carry on as normal
        if (mStop == false)
        {
            // If event sensor type is accelerometer
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                // Set it to the first event value
                mXAccelerationBall = event.values[0];
                // Call update method
                update();
            }
        }

    }

    // region Method not used

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Not using
    }

    // endregion

    // region Ball Related Methods

    // region Moving ball Method

    private void moveBall()
    {
        // SET mXPos to the the image X position
        mXPosBall = mBall.getX();

        // SET the speed to equal to acceleration times 2
        mXSpeedBall = mXAccelerationBall * 2;

        // move mXPos by calculating the speed and acceleration
        mXPosBall -= mXSpeedBall + mXAccelerationBall;

        // SET the image to its new X position
        mBall.setX(mXPosBall);
    }

    // endregion

    // region Ball Boundary Check

    private void wallBoundaryDetection()
    {
        // 0 is left
        // If ball position is less than 0
        if (mXPosBall <= 0)
        {
            // set position to 0
            mBall.setX(0);
        }
        // Screen Width is right
        // Else if ball position is greater than screen width - ball width
        else if (mXPosBall >= Game.Screen_Width - mBall.getWidth())
        {
            // set ball to the right of the screen
            mBall.setX(Game.Screen_Width - mBall.getWidth());
        }
    }

    // endregion

    // endregion

    // region Block Related Methods

    // region Set Block Positions

    public void setBlockPosition()
    {
        int leftPos = 1;
        int rightPos = 1;

        // Random gen between 0 - 9
        int random = mRnd.nextInt(10);

        // If random is less than 5
        if (random < 5)
        {
            // Set the left and right blocks width
            mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
            mRightWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4;
        }
        // else if greater than 5
        else if (random >= 5)
        {
            mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4;
            mRightWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
        }

        // Count the left width + right width + gap
        int count = mLeftWidth + mRightWidth + mGap;

        // If count is greater than screen width THEN do this
        if (count >= Game.Screen_Width)
        {
            mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
            mRightWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
        }

        // region Left Blocks

        // Loop depending on the list size
        for (int i = 0; i < mLeftBlock.size(); i++)
        {
            // Request the layout to be able to set the width
            mLeftBlock.get(i).requestLayout();
            // Set the blocks width to the new width
            mLeftBlock.get(i).getLayoutParams().width = mLeftWidth;
            // Set the Y position of the blocks to negative value above screen
            mLeftBlock.get(i).setY(-500 * leftPos);
            // Set the left block Y position to be the same as the left block
            mYPosLeftBlock = mLeftBlock.get(i).getY();
            // leftPos + 1
            leftPos++;
        }

        // endregion

        // region Right Blocks

        // Loop depending on the list size
        for (int i = 0; i < mRightBlock.size(); i++)
        {
            // Request the layout to be able to set the width
            mRightBlock.get(i).requestLayout();
            // Set the blocks width to the new width
            mRightBlock.get(i).getLayoutParams().width = mRightWidth;
            // Set the Y position of the blocks to negative value above screen
            mRightBlock.get(i).setY(-500 * rightPos);
            // Set the X value of the blocks to be on the right hand side
            mRightBlock.get(i).setX(Game.Screen_Width - mRightWidth);
            // Set the left block Y position to be the same as the left block
            mYPosRightBlock = mRightBlock.get(i).getY();
            // rightPos + 1
            rightPos++;
        }

        // endregion
    }

    // endregion

    // region Moving Blocks

    private void moveBlocks()
    {
        // region For loop Left Blocks

        // Loop depending on the list size
        for(int i = 0; i < mLeftBlock.size(); i++)
        {
            // Set Y pos of blocks to the same
            mYPosLeftBlock = mLeftBlock.get(i).getY();
            // Y pos + speed
            mYPosLeftBlock += mSpeedBlock;
            // Set Y position os block to be the same as the YPosLeftBlocks
            mLeftBlock.get(i).setY(mYPosLeftBlock);

            // If left block is greater than screen height + screen height THEN
            if (mLeftBlock.get(i).getY() > Game.Screen_Height + Game.Screen_Height)
            {
                // Call Methods
                repositionBlocks();
                increaseSpeed();
            }
        }
        // endregion

        // region For loop Right Blocks

        // Loop depending on the list size
        for(int i = 0; i < mRightBlock.size(); i++)
        {
            // Set Y pos of blocks to the same
            mYPosRightBlock = mRightBlock.get(i).getY();
            // Y pos + speed
            mYPosRightBlock += mSpeedBlock;
            // Set Y position os block to be the same as the YPosRightBlocks
            mRightBlock.get(i).setY(mYPosRightBlock);

            // If right block is greater than screen height + screen height THEN
            if (mRightBlock.get(i).getY() > Game.Screen_Height + Game.Screen_Height)
            {
                // Call Method
                repositionBlocks();
            }
        }

        // endregion
    }

    // endregion

    // region Repositioning blocks

    private void repositionBlocks()
    {
        // Random number gen between 0 - 9
        int random = mRnd.nextInt(10);

        // IF random less than or = 5 THEN do this
        if (random <= 5)
        {
            mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
            mRightWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4;
        }
        // Else if random greater than 5 THEN do this
        else if (random > 5)
        {
            mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4;
            mRightWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
        }

        // Count the leftwidth + rightWidth + gap
        int count = mLeftWidth + mRightWidth + mGap;

        // IF count is greater than screen width - gap THEN do this
        if (count >= Game.Screen_Width - mGap)
        {
            // get new random
            int newRnd = mRnd.nextInt(10);

            if (newRnd < 5)
            {
                mLeftWidth = mRnd.nextInt(Game.Screen_Width/4) + Game.Screen_Width/4 - mGap;
            }
            else
            {
                // Minus 100 from current rightWidth
                mRightWidth -= 100;
            }
        }

        // region Left Blocks

        // Loop on list size
        for (int i = 0; i <  mLeftBlock.size(); i++)
        {
            if (mLeftBlock.get(i).getY() > Game.Screen_Height + Game.Screen_Height)
            {
                // Change width to = new width
                mLeftBlock.get(i).requestLayout();
                mLeftBlock.get(i).getLayoutParams().width = mLeftWidth;
                // set X back to negative value of screen height
                mLeftBlock.get(i).setY(-Game.Screen_Height);
            }
        }

        // endregion

        // region Right Blocks

        // Loop on list size
        for (int ii = 0; ii < mRightBlock.size(); ii++)
        {
            if (mRightBlock.get(ii).getY() > Game.Screen_Height + Game.Screen_Height)
            {
                // Change width to = new width
                mRightBlock.get(ii).requestLayout();
                mRightBlock.get(ii).getLayoutParams().width = mRightWidth;
                // Set X to the right hand side
                mRightBlock.get(ii).setX(Game.Screen_Width - mRightWidth);
                // Set Y to the top
                mRightBlock.get(ii).setY(-Game.Screen_Height);
            }
        }

        // endregion
    }

    // endregion

    // endregion

    // region Collision Detection Method

    private void collision()
    {
        // Set mBall to rect
        mBall.getHitRect(mRectBall);

        // region Left Block Collision

        // For Loop
        for (int i = 0; i < mLeftBlock.size(); i++)
        {
            // Set left blocks as rect
            mLeftBlock.get(i).getHitRect(mRectLeftBlock);

            // IF ball and left block touch / hit THEN
            if (Rect.intersects(mRectBall, mRectLeftBlock))
            {
                // Testing
                System.out.println("Somebody Toucha My Left Spaghet");

                // IF phone has a vibrator THEN
                if (mVibrator.hasVibrator())
                {
                    // Vibrate 0.5 seconds
                    mVibrator.vibrate(500);
                }

                // Set mActive to FALSE
                mActive = false;
            }
        }

        // endregion


        // region Right Block Collision

        // For Loop
        for (int i = 0; i < mRightBlock.size(); i++)
        {
            // Set left blocks as rect
            mRightBlock.get(i).getHitRect(mRectRightBlock);

            // IF ball and left block touch / hit THEN
            if (Rect.intersects(mRectBall, mRectRightBlock))
            {
                // Testing
                System.out.println("Somebody Toucha My Right Spaghet");

                // IF phone has a vibrator THEN
                if (mVibrator.hasVibrator())
                {
                    // Vibrate 0.5 seconds
                    mVibrator.vibrate(200);
                }

                // Set mActive to FALSE
                mActive = false;
            }
        }

        // endregion
    }

    // endregion

    // region Increase Block Speed Method

    private void increaseSpeed()
    {
        // IF speed is less than Max THEN
        if (mSpeedBlock <= mMaxSpeedBlock)
        {
            // Increase by 0.5
            mSpeedBlock += 0.5;
        }
    }

    // endregion

    // region Score Method

    private void score()
    {
        // FOR LOOP
        for (int i = 0; i < mLeftBlock.size(); i++)
        {
            // If the Y block is greater then ball Y pos
            if (mLeftBlock.get(i).getY() >  mBall.getY())
            {
                // Score + 1
                mScore++;
                // Display score on screen
                mScoreText.setText("Score: " + mScore);
            }
        }
    }

    // endregion

    // region Update Method

    private void update()
    {
        // IF True
        if (mActive == true)
        {
            // Keep calling these methods
            moveBall();
            moveBlocks();
            wallBoundaryDetection();
            collision();
            score();
        }
        // Else Call Game Over
        else
        {
            gameOver();
        }

    }

    // endregion

    // region Game Over Method

    private void gameOver()
    {
        // Open Game Over Class
        Intent intent = new Intent(mContext, GameOver.class);

        // Pass the score value to the next activity
        intent.putExtra("Score", mScore);

        // Open next activity
        mContext.startActivity(intent);
    }

    // endregion
}

package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillFiveAndOneActivity extends AppCompatActivity {

    private JSONObject allData;
    private JSONArray things;
    private JSONArray numbers;
    private MediaPlayer mp;

    // private Handler handler;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private RelativeLayout objectsContainer;
    private RelativeLayout numbersContainer;
    private int[] positions;
    private int draggedItems = 0;
    private RelativeLayout itemsReceptacle;
    private int targetItems = 0;
    private int itemResId;
    private boolean isInReceptacle;
    private ImageView equationNumberOne;
    private ImageView equationNumberTwo;
    private ImageView equationAnswer;
    private ImageView equationSign;
    private ImageView equationEqualsSign;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_five_and_one);
        equationNumberOne = (ImageView)findViewById(R.id.equation_one);
        // equationNumberOne.setBackgroundColor(Color.argb(100, 255, 0, 0));
        equationNumberOne.setColorFilter(Color.argb(255, 255, 255, 255));
        equationNumberOne.setVisibility(View.VISIBLE);

        equationSign = (ImageView)findViewById(R.id.equation_sign);
        // equationSign.setBackgroundColor(Color.argb(100, 0, 0, 255));
        equationSign.setVisibility(View.VISIBLE);

        equationNumberTwo = (ImageView)findViewById(R.id.equation_two);
        // equationNumberTwo.setBackgroundColor(Color.argb(100, 255, 0, 0));
        equationNumberTwo.setColorFilter(Color.argb(255, 255, 255, 255));
        equationNumberTwo.setVisibility(View.VISIBLE);

        equationEqualsSign = (ImageView)findViewById(R.id.equation_equals);
        // equationEqualsSign.setBackgroundColor(Color.argb(100, 0, 0, 255));
        equationEqualsSign.setVisibility(View.VISIBLE);

        equationAnswer = (ImageView)findViewById(R.id.equation_answer);
        // equationAnswer.setBackgroundColor(Color.argb(100, 0, 0, 255));
        equationAnswer.setColorFilter(Color.argb(255, 255, 255, 255));
        equationAnswer.setVisibility(View.VISIBLE);

        numbersContainer = (RelativeLayout)findViewById(R.id.numbers_container);
        // numbersContainer.setBackgroundColor(Color.argb(100, 0, 255, 255));
        numbersContainer.setVisibility(View.VISIBLE);

        numberOne = (ImageView)findViewById(R.id.numeral_1);
        numberOne.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberOne.setVisibility(View.INVISIBLE);

        numberTwo = (ImageView)findViewById(R.id.numeral_2);
        numberTwo.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberTwo.setVisibility(View.INVISIBLE);

        numberThree = (ImageView)findViewById(R.id.numeral_3);
        numberThree.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberThree.setVisibility(View.INVISIBLE);

        objectsContainer = (RelativeLayout)findViewById(R.id.itemsContainer);
        // objectsContainer.setBackgroundColor(Color.argb(100, 0, 255, 0));
        objectsContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < objectsContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) objectsContainer.getChildAt(i);
            iv.setImageResource(FetchResource.imageId(this, "orange"));
            // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
            iv.setVisibility(View.VISIBLE);
        }

        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        // itemsReceptacle.setBackgroundColor(Color.argb(100, 255, 100, 0));
        itemsReceptacle.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams irParams = (RelativeLayout.LayoutParams) itemsReceptacle.getLayoutParams();
        irParams.topMargin = 850;
        irParams.leftMargin = 15;
        itemsReceptacle.setLayoutParams(irParams);

        for (int i = 0; i < itemsReceptacle.getChildCount(); i++) {
            ImageView iv = (ImageView) itemsReceptacle.getChildAt(i);
            iv.setImageResource(FetchResource.imageId(this, "banana"));
            // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
            iv.setVisibility(View.VISIBLE);
        }

        initializeData();
    }

    private void playSound(String sound, final Runnable action) {
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    private void initializeData() {
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            things = allData.getJSONArray("things");
            numbers = allData.getJSONArray("numbers");

            playSound(allData.getString("equation_sound"), null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
        mp = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}

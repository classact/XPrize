package classact.com.xprize.activity.drill.math;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndOneActivity extends AppCompatActivity {
    private ImageView largeShape;
    private ImageView smallShape;
    private MediaPlayer mp;
    private JSONObject allData;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_one);
        largeShape = (ImageView)findViewById(R.id.big_shape);
        smallShape = (ImageView)findViewById(R.id.small_shape);

        RelativeLayout.LayoutParams largeShapeLP = (RelativeLayout.LayoutParams) largeShape.getLayoutParams();
        RelativeLayout.LayoutParams smallShapeLP = (RelativeLayout.LayoutParams) smallShape.getLayoutParams();

        largeShapeLP.topMargin = 430;
        smallShapeLP.topMargin = 430;

        largeShapeLP.leftMargin = 350;

        largeShapeLP.width = 475;
        smallShapeLP.width = 475;

        largeShapeLP.height = 475;
        smallShapeLP.height = 475;

        largeShape.setLayoutParams(largeShapeLP);
        smallShape.setLayoutParams(smallShapeLP);

        largeShape.setImageResource(0);
        smallShape.setImageResource(0);

        initialise();
    }

    public void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);

            String objectImage = allData.getString("object_name");
            int objectImageId = FetchResource.imageId(THIS, objectImage);

            largeShape.setImageResource(objectImageId);
            smallShape.setImageResource(objectImageId);

            boolean morphBigger = false;
            String objectComparativeSound = allData.getString("object_comparative_sound");
            if (objectComparativeSound.contains("big")) {
                morphBigger = true;
            } else if (objectComparativeSound.contains("small")){
                morphBigger = false;
            } else {
                Globals.bugBar(this.findViewById(android.R.id.content), "data - cannot compare (big vs small?)", objectComparativeSound).show();
                throw new Exception("Unable to determine comparative measure (big vs. small) for sound: " + objectComparativeSound);
            }

            float bs = 1.5f;
            float mbs = 1.15f;
            float ss = 0.5f;
            float mss = 0.85f;

            boolean morphLeft = false;
            if (Math.random() < 0.5f) {
                morphLeft = true;
                if (morphBigger) {
                    largeShape.setScaleX(bs);
                    largeShape.setScaleY(bs);

                    smallShape.setScaleX(mss);
                    smallShape.setScaleY(mss);
                } else {
                    largeShape.setScaleX(ss);
                    largeShape.setScaleY(ss);

                    smallShape.setScaleX(mbs);
                    smallShape.setScaleY(mbs);
                }
            } else {
                if (morphBigger) {
                    smallShape.setScaleX(bs);
                    smallShape.setScaleY(bs);

                    largeShape.setScaleX(mss);
                    largeShape.setScaleY(mss);
                } else {
                    smallShape.setScaleX(ss);
                    smallShape.setScaleY(ss);

                    largeShape.setScaleX(mbs);
                    largeShape.setScaleY(mbs);
                }
            }

            String sound = allData.getString("lets_look_at_shapes");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayTheseArea();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void objectClicked(String touchedObject){
        try{
            String objectToTouch = allData.getString("object_to_touch");
            if (objectToTouch.equalsIgnoreCase(touchedObject)){
                playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                            public void run(){
                                finish();
                            }
                        },500);
                    }
                });
            }
            else{
                playSound(FetchResource.negativeAffirmation(THIS), null);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayTheseArea(){
        try {
            String sound = allData.getString("these_are_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObject();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObject(){
        try {
            String sound = allData.getString("object_plural_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayRepeat();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayRepeat(){
        try {
            String sound = allData.getString("repeat_afterme_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectAgain(){
        try {
            String sound = allData.getString("object_plural_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayTouch();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayTouch(){
        try{
            String sound = allData.getString("touch_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectToTouch();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectToTouch(){
        try {
            String sound = allData.getString("object_comparative_sound");
            playSound(sound, null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
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

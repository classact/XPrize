package classact.com.xprize.activity.drill.math;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndOneActivity extends AppCompatActivity {
    private ImageView largeShape;
    private ImageView smallShape;
    private MediaPlayer mp;
    private JSONObject allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_one);
        largeShape = (ImageView)findViewById(R.id.big_shape);
        largeShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("large");
            }
        });
        smallShape = (ImageView)findViewById(R.id.small_shape);
        smallShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("small");
            }
        });
        initialise();
    }

    private void objectClicked(String touchedObject){
        try{
            String objectToTouch = allData.getString("object_to_touch");
            if (objectToTouch.equalsIgnoreCase(touchedObject)){
                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                    public void run(){
                        finish();
                    }
                },500);
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            largeShape.setImageResource(allData.getInt("demo_object"));
            smallShape.setImageResource(allData.getInt("demo_object"));
            int sound = allData.getInt("lets_look_at_shapes");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayTheseArea();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTheseArea(){
        try {
            int sound = allData.getInt("these_are_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObject();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObject(){
        try {
            int sound = allData.getInt("objects_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayRepeat();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayRepeat(){
        try {
            int sound = allData.getInt("repeat_afterme_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectAgain();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectAgain(){
        try {
            int sound = allData.getInt("objects_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayTouch();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTouch(){
        try{
            int sound = allData.getInt("touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectToTouch();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayObjectToTouch(){
        try {
            int sound = allData.getInt("object_to_touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
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
            mp.stop();
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}

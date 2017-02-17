package classact.com.xprize.activity.drill.math;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixActivity extends AppCompatActivity {
    private RelativeLayout objectsContainer;
    private ImageView demoShape;
    private ImageView rectangle;
    private ImageView square;
    private ImageView circle;
    private JSONObject allData;
    private MediaPlayer mp;
    private boolean drillEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six);
        objectsContainer = (RelativeLayout)findViewById(R.id.objectsContainer);
        demoShape = (ImageView)findViewById(R.id.demo_shape);
        rectangle = (ImageView)findViewById(R.id.rectangle);
        rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("rectangle");
            }
        });
        square = (ImageView)findViewById(R.id.square);
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("square");
            }
        });
        circle = (ImageView)findViewById(R.id.circle);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClicked("circle");
            }
        });
        drillEnd = false;
        initialise();
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            demoShape.setImageResource(allData.getInt("demo_object"));
            int sound = allData.getInt("lets_look_at_shapes");
            mp = MediaPlayer.create(this, sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayThisIsA();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
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

                    if (drillEnd) {
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void objectClicked(String touchedObject){
        try{
            String objectToTouch = allData.getString("object_to_touch");
            if (objectToTouch.equalsIgnoreCase(touchedObject)){
                drillEnd = true;
                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayThisIsA(){
        try {
            int sound = allData.getInt("this_is_sound");
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayObject(){
        try {
            int sound = allData.getInt("object_sound");
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
            if (mp != null) {
                mp.release();
            }
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayObjectAgain(){
        try {
            int sound = allData.getInt("object_sound");
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayTouch(){
        try{
            demoShape.setVisibility(View.INVISIBLE);
            objectsContainer.setVisibility(View.VISIBLE);
            int sound = allData.getInt("touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayObjectLastTime();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void sayObjectLastTime(){
        try {
            int sound = allData.getInt("object_sound");
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
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }
}

package classact.com.xprize.activity.drill.math;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixActivity extends AppCompatActivity {
    private RelativeLayout objectsContainer;
    private ImageView demoShape;
    private ImageView shape1;
    private ImageView shape2;
    private ImageView shape3;
    private JSONObject allData;
    private MediaPlayer mp;
    private boolean touchEnabled;
    private Handler handler;
    private ImageView[] shapeImageViews;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six);
        objectsContainer = (RelativeLayout)findViewById(R.id.objectsContainer);
        demoShape = (ImageView)findViewById(R.id.demo_shape);

        shape1 = (ImageView)findViewById(R.id.rectangle);
        shape2 = (ImageView)findViewById(R.id.circle);
        shape3 = (ImageView)findViewById(R.id.square);

        shape1.setImageResource(0);
        shape2.setImageResource(0);
        shape3.setImageResource(0);

        handler = new Handler();
        touchEnabled = false;
        initialise();
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            String objectImage = allData.getString("demo_object");
            int objectImageId = FetchResource.imageId(THIS, objectImage);
            demoShape.setImageResource(objectImageId);
            demoShape.setBackgroundColor(Color.argb(100, 0, 0, 255));

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int screenW = displayMetrics.widthPixels;
            int screenH = displayMetrics.heightPixels;
            float density = displayMetrics.density;

            Drawable demoDrawable = demoShape.getDrawable();
            int demoW = demoDrawable.getIntrinsicWidth();
            int demoH = demoDrawable.getIntrinsicHeight();

            System.out.println("Dr: w: " + demoW +
                    ", h: " + demoH);

            int topOffset = ((screenH - (int) ((float) 550 * density)) / 2) - 50;

            RelativeLayout.LayoutParams demoLayout = (RelativeLayout.LayoutParams) demoShape.getLayoutParams();
            // demoLayout.addRule(RelativeLayout.CENTER_VERTICAL);

            System.out.println("De: w: " + demoLayout.width +
                    ", h: " + demoLayout.height);

            demoLayout.topMargin = topOffset;
            demoShape.setLayoutParams(demoLayout);

            System.out.println("w: " + demoW + ", h: " + demoH);

            // Get object sound
            // Will use sound as comparator when object is clicked
            // Reason being, the image of object isn't necessarily that of sound
            // Ie., a 'table' may be used to represent a 'rectangle'
            // Hence, using 'table' is unreliable as equivalently comparing shape vs shape
            String objectSound = allData.getString("object_sound");

            // Set array that carries 'ordered' (#1-3 image views)
            ImageView[] orderedShapeImageViews = {shape1, shape2, shape3};
            int numberOfShapes = orderedShapeImageViews.length;

            // Init shapes and shape_sounds
            // Do this using default shapes: rectangle, circle or square)
            String[] shapes = {"rectangle", "circle", "square"};
            String[] shape_sounds = new String[shapes.length];
            if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
                shape_sounds[0] = "rectangle";
                shape_sounds[1] = "circle";
                shape_sounds[2] = "square";
            } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                shape_sounds[0] = "s_rectangle";
                shape_sounds[1] = "s_circle";
                shape_sounds[2] = "s_square";
            }

            // See if new shape duplicates existing one
            // If it doesn't, replace an existing shape
            // Choose the shape via random index
            int rndIndex;
            if (!(objectSound.equalsIgnoreCase(shape_sounds[0]) ||
                    objectSound.equalsIgnoreCase(shape_sounds[1]) ||
                    objectSound.equalsIgnoreCase(shape_sounds[2]))) {
                Random rnd = new Random();
                rndIndex = rnd.nextInt(numberOfShapes);
                shapes[rndIndex] = objectImage;
                shape_sounds[rndIndex] = objectSound;
            } else {
                for (int i = 0; i < shape_sounds.length; i++) {
                    if (objectSound.equalsIgnoreCase(shape_sounds[i])) {
                        // Override default shape's image, as the image might be different (despite the same sound)
                        shapes[i] = objectImage;
                    }
                }
            }

            // Shuffle shapes
            shapeImageViews = new ImageView[numberOfShapes];
            int[] s = FisherYates.shuffle(numberOfShapes);

            for (int i = 0; i < numberOfShapes; i++) {
                int si = s[i]; // shuffled index
                shapeImageViews[i] = orderedShapeImageViews[si];
                ImageView iv = shapeImageViews[i];

                final String shapeName = shapes[i];
                int imageId = FetchResource.imageId(THIS, shapeName);
                iv.setImageResource(imageId);

                // Now here is where shape sound is used / passed in as a comparator
                final String shapeSound = shape_sounds[i];

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(shapeSound);
                    }
                });
            }

            String sound = allData.getString("lets_look_at_shapes");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayThisIsA();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void objectClicked(String touchedObject){
        if (touchEnabled) {
            try {
                String objectToTouch = allData.getString("object_sound");
                if (objectToTouch.equalsIgnoreCase(touchedObject)) {
                    touchEnabled = false;
                    playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                        @Override
                        public void run() {
                            if (mp != null) {
                                mp.release();
                            }
                            finish();
                        }
                    });
                } else {
                    playSound(FetchResource.negativeAffirmation(THIS), null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sayThisIsA(){
        try {
            String sound = allData.getString("this_is_sound");
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
            String sound = allData.getString("object_sound");
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
            String sound = allData.getString("object_sound");
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
            demoShape.setVisibility(View.INVISIBLE);
            objectsContainer.setVisibility(View.VISIBLE);
            String sound = allData.getString("touch_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectLastTime();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectLastTime(){
        try {
            String sound = allData.getString("object_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    touchEnabled = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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

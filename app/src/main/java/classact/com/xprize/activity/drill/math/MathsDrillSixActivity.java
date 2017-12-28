package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;

public class MathsDrillSixActivity extends DrillActivity {
    private RelativeLayout objectsContainer;
    private ImageView demoShape;
    private ImageView shape1;
    private ImageView shape2;
    private ImageView shape3;
    private JSONObject allData;
    private boolean touchEnabled;
    private ImageView[] shapeImageViews;

    private final Context THIS = this;

    private MathDrill06AViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06AViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        objectsContainer = (RelativeLayout)findViewById(R.id.objectsContainer);
        demoShape = (ImageView)findViewById(R.id.demo_shape);

        shape1 = (ImageView)findViewById(R.id.rectangle);
        shape2 = (ImageView)findViewById(R.id.circle);
        shape3 = (ImageView)findViewById(R.id.square);

        shape1.setImageResource(0);
        shape2.setImageResource(0);
        shape3.setImageResource(0);

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
            // demoShape.setBackgroundColor(Color.argb(100, 0, 0, 255));

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

            /*shape1.setBackgroundColor(Color.argb(100, 255, 0, 0));
            shape2.setBackgroundColor(Color.argb(100, 0, 255, 0));
            shape3.setBackgroundColor(Color.argb(100, 0, 0, 255));*/

            Drawable s1d = shape1.getDrawable();
            Drawable s2d = shape2.getDrawable();
            Drawable s3d = shape3.getDrawable();

            int s1IW = s1d.getIntrinsicWidth();
            int s2IW = s2d.getIntrinsicWidth();
            int s3IW = s3d.getIntrinsicWidth();

            int s1IH = s1d.getIntrinsicHeight();
            int s2IH = s2d.getIntrinsicHeight();
            int s3IH = s3d.getIntrinsicHeight();

            int s1SW = (int) ((float) s1IW / density);
            int s2SW = (int) ((float) s2IW / density);
            int s3SW = (int) ((float) s3IW / density);

            int s1SH = (int) ((float) s1IH / density);
            int s2SH = (int) ((float) s2IH / density);
            int s3SH = (int) ((float) s3IH / density);

            int s1Max = Math.max(s1SW, s1SH);
            int s2Max = Math.max(s2SW, s2SH);
            int s3Max = Math.max(s3SW, s3SH);

            double s1Ratio = (double) 250 / s1Max;
            double s2Ratio = (double) 250 / s2Max;
            double s3Ratio = (double) 250 / s3Max;

            int s1RSW = (int) ((double) s1SW * s1Ratio);
            int s2RSW = (int) ((double) s2SW * s2Ratio);
            int s3RSW = (int) ((double) s3SW * s3Ratio);

            int s1RSH = (int) ((double) s1SH * s1Ratio);
            int s2RSH = (int) ((double) s2SH * s2Ratio);
            int s3RSH = (int) ((double) s3SH * s3Ratio);

            int s1PW = 100 + 250 - (int) (((double) 250 - (double) s1RSW)/2);
            int s2PW = 100 + 250 + (int) (((double) 250 - (double) s2RSW)/2);
            System.out.println("S1PW: " + s1PW + ", S2PW: " + s2PW);

            System.out.println("S1 - sw: " + s1RSW + ", sh: " + s1RSH);
            System.out.println("S2 - sw: " + s2RSW + ", sh: " + s2RSH);
            System.out.println("S3 - sw: " + s3RSW + ", sh: " + s3RSH);

            int s2s1DB = s2PW - s1PW;
            System.out.println("Distance between: " + s2s1DB);

            RelativeLayout.LayoutParams shape1Layout = (RelativeLayout.LayoutParams) shape1.getLayoutParams();
            RelativeLayout.LayoutParams shape2Layout = (RelativeLayout.LayoutParams) shape2.getLayoutParams();
            RelativeLayout.LayoutParams shape3Layout = (RelativeLayout.LayoutParams) shape3.getLayoutParams();

            shape1Layout.topMargin = 160;
            shape2Layout.topMargin = 60;

            if (s2s1DB < 54) {
                shape2Layout.leftMargin += (int) ((float) (54 - s2s1DB));
            }

            // shape2Layout.leftMargin = (int) ((float) 110 / density);
            shape3Layout.topMargin = 30;

            shape1.setLayoutParams(shape1Layout);
            shape2.setLayoutParams(shape2Layout);
            shape3.setLayoutParams(shape3Layout);

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
                    playSound(FetchResource.positiveAffirmation(THIS), () -> {
                        mediaPlayer.reset();
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
}

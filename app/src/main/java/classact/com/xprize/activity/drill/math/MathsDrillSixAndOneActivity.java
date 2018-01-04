package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
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
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillSixAndOneActivity extends DrillActivity {
    private ImageView largeShape;
    private ImageView smallShape;
    private JSONObject allData;

    private boolean touchEnabled;

    private MathDrill06BViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_one);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06BViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

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
            int objectImageId = FetchResource.imageId(context, objectImage);

            largeShape.setImageResource(objectImageId);
            smallShape.setImageResource(objectImageId);

            boolean morphBigger;

            final String objectSingularSound = allData.getString("object_singular_sound");
            final String objectComparativeSound = allData.getString("object_comparative_sound");

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

                    RelativeLayout.LayoutParams largeShapeLP = (RelativeLayout.LayoutParams) largeShape.getLayoutParams();
                    largeShapeLP.leftMargin += 40;
                    largeShape.setLayoutParams(largeShapeLP);

                    RelativeLayout.LayoutParams smallShapeLP = (RelativeLayout.LayoutParams) smallShape.getLayoutParams();
                    smallShapeLP.leftMargin += 60;
                    smallShape.setLayoutParams(smallShapeLP);

                } else {
                    largeShape.setScaleX(ss);
                    largeShape.setScaleY(ss);

                    smallShape.setScaleX(mbs);
                    smallShape.setScaleY(mbs);

                    RelativeLayout.LayoutParams largeShapeLP = (RelativeLayout.LayoutParams) largeShape.getLayoutParams();
                    largeShapeLP.leftMargin -= 75;
                    largeShape.setLayoutParams(largeShapeLP);

                    RelativeLayout.LayoutParams smallShapeLP = (RelativeLayout.LayoutParams) smallShape.getLayoutParams();
                    smallShapeLP.leftMargin -= 25;
                    smallShape.setLayoutParams(smallShapeLP);
                }
            } else {
                if (morphBigger) {
                    smallShape.setScaleX(bs);
                    smallShape.setScaleY(bs);

                    largeShape.setScaleX(mss);
                    largeShape.setScaleY(mss);

                    RelativeLayout.LayoutParams largeShapeLP = (RelativeLayout.LayoutParams) largeShape.getLayoutParams();
                    largeShapeLP.leftMargin -= 130;
                    largeShape.setLayoutParams(largeShapeLP);

                    RelativeLayout.LayoutParams smallShapeLP = (RelativeLayout.LayoutParams) smallShape.getLayoutParams();
                    smallShapeLP.leftMargin += 60;
                    smallShape.setLayoutParams(smallShapeLP);
                } else {
                    smallShape.setScaleX(ss);
                    smallShape.setScaleY(ss);

                    largeShape.setScaleX(mbs);
                    largeShape.setScaleY(mbs);

                    RelativeLayout.LayoutParams largeShapeLP = (RelativeLayout.LayoutParams) largeShape.getLayoutParams();
                    largeShapeLP.leftMargin += 110;
                    largeShape.setLayoutParams(largeShapeLP);

                    RelativeLayout.LayoutParams smallShapeLP = (RelativeLayout.LayoutParams) smallShape.getLayoutParams();
                    smallShapeLP.leftMargin -= 25;
                    smallShape.setLayoutParams(smallShapeLP);
                }
            }

            if (morphLeft) {
                largeShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(objectComparativeSound);
                    }
                });
                smallShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(objectSingularSound);
                    }
                });
            } else {
                largeShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(objectSingularSound);
                    }
                });
                smallShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(objectComparativeSound);
                    }
                });
            }

            // Disable touch
            touchEnabled = false;

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
            if (touchEnabled) {
                String objectToTouch = allData.getString("object_comparative_sound");
                if (objectToTouch.equalsIgnoreCase(touchedObject)) {
                    touchEnabled = false;
                    playSound(FetchResource.positiveAffirmation(context), new Runnable() {
                        @Override
                        public void run() {
                            handler.delayed(() -> {
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }, 0);
                        }
                    });
                } else {
                    playSound(FetchResource.negativeAffirmation(context), null);
                }
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

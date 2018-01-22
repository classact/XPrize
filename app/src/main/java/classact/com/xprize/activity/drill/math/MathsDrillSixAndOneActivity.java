package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillSixAndOneActivity extends DrillActivity {

    @BindView(R.id.activity_maths_drill_six_and_one) ConstraintLayout rootView;

    @BindView(R.id.g_h_01) Guideline gh01;
    @BindView(R.id.g_v_01) Guideline gv01;
    @BindView(R.id.g_h_02) Guideline gh02;
    @BindView(R.id.g_v_02) Guideline gv02;



    private ImageView largeShape;
    private ImageView smallShape;
    private JSONObject allData;

    private boolean touchEnabled;

    private MathDrill06BViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06BViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        largeShape = (ImageView)findViewById(R.id.big_shape);
        smallShape = (ImageView)findViewById(R.id.small_shape);

        ViewGroup.MarginLayoutParams largeShapeLP = (ViewGroup.MarginLayoutParams) largeShape.getLayoutParams();
        ViewGroup.MarginLayoutParams smallShapeLP = (ViewGroup.MarginLayoutParams) smallShape.getLayoutParams();

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

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float screenWidth = displayMetrics.widthPixels;
            float screenHeight = displayMetrics.heightPixels;
            float density = displayMetrics.density;

            boolean morphLeft = false;
            if (Math.random() < 0.5f) {
                morphLeft = true;
                if (morphBigger) {

                    int largeWidth = (int) (475 * bs);
                    int largeHeight = (int) (475 * bs);
                    int smallWidth = (int) (475 * mss);
                    int smallHeight = (int) (475 * mss);

                    ez.size(largeShape, largeWidth, largeHeight);
                    ez.size(smallShape, smallWidth, smallHeight);

                    // LEFT = BIG
                    // RIGHT = SMALL

                    if (largeShape.getX() + ez.getWidth(largeShape) >= smallShape.getX()) {
                        Log.d("OVERLAP", "DETECTED");
                    } else {
                        Log.d("OVERLAP", "NOT DETECTED");
                    }

                    float addPercentage = 80f / (screenWidth / density);

                    ez.guide.setPercentage(gv01, 0.25f);
                    ez.guide.setPercentage(gv02, 0.45f + addPercentage);

//                    ImageView leftMarker = ez.rect(50, 50, Color.RED);
//                    ImageView rightMarker = ez.rect(50, 50, Color.CYAN);
//                    rootView.addView(leftMarker);
//                    rootView.addView(rightMarker);
//                    leftMarker.setX(ez.guide.getPos(gv01) + (ez.getWidth(largeShape) / density));
//                    leftMarker.setY(ez.guide.getPos(gh01));
//                    rightMarker.setX(ez.guide.getPos(gv02));
//                    rightMarker.setY(ez.guide.getPos(gh02));
//
//                    Log.d("Option", "A: " + (rightMarker.getX() - leftMarker.getX()));

                } else {
                    int largeWidth = (int) (475 * ss);
                    int largeHeight = (int) (475 * ss);
                    int smallWidth = (int) (475 * mbs);
                    int smallHeight = (int) (475 * mbs);

                    ez.size(largeShape, largeWidth, largeHeight);
                    ez.size(smallShape, smallWidth, smallHeight);

                    // LEFT = SMALL
                    // RIGHT = BIG

                    if (smallShape.getX() + ez.getWidth(smallShape) >= largeShape.getX()) {
                        Log.d("OVERLAP", "DETECTED");
                    } else {
                        Log.d("OVERLAP", "NOT DETECTED");
                    }

                    ez.guide.setPercentage(gv01, 0.23f);
                    ez.guide.setPercentage(gv02, 0.43f);

//                    ImageView leftMarker = ez.rect(50, 50, Color.RED);
//                    ImageView rightMarker = ez.rect(50, 50, Color.CYAN);
//                    rootView.addView(leftMarker);
//                    rootView.addView(rightMarker);
//                    leftMarker.setX(ez.guide.getPos(gv01) + (ez.getWidth(smallShape) / density));
//                    leftMarker.setY(ez.guide.getPos(gh01));
//                    rightMarker.setX(ez.guide.getPos(gv02));
//                    rightMarker.setY(ez.guide.getPos(gh02));
//
//                    Log.d("Option", "B: " + (rightMarker.getX() - leftMarker.getX()));
                }
            } else {
                if (morphBigger) {

                    int smallWidth = (int) (475 * bs);
                    int smallHeight = (int) (475 * bs);
                    int largeWidth = (int) (475 * mss);
                    int largeHeight = (int) (475 * mss);

                    ez.size(smallShape, smallWidth, smallHeight);
                    ez.size(largeShape, largeWidth, largeHeight);

                    // LEFT = SMALL
                    // RIGHT = BIG

                    if (smallShape.getX() + ez.getWidth(smallShape) >= largeShape.getX()) {
                        Log.d("OVERLAP", "DETECTED");
                    } else {
                        Log.d("OVERLAP", "NOT DETECTED");
                    }

                    float addPercentage = 80f / (screenWidth / density);

                    ez.guide.setPercentage(gv01, 0.18f);
                    ez.guide.setPercentage(gv02, 0.38f + addPercentage);

//                    ImageView leftMarker = ez.rect(50, 50, Color.RED);
//                    ImageView rightMarker = ez.rect(50, 50, Color.CYAN);
//                    rootView.addView(leftMarker);
//                    rootView.addView(rightMarker);
//                    leftMarker.setX(ez.guide.getPos(gv01) + (ez.getWidth(smallShape) / density));
//                    leftMarker.setY(ez.guide.getPos(gh01));
//                    rightMarker.setX(ez.guide.getPos(gv02));
//                    rightMarker.setY(ez.guide.getPos(gh02));
//
//                    Log.d("Option", "C: " + (rightMarker.getX() - leftMarker.getX()));

                } else {

                    int smallWidth = (int) (475 * ss);
                    int smallHeight = (int) (475 * ss);
                    int largeWidth = (int) (475 * mbs);
                    int largeHeight = (int) (475 * mbs);

                    ez.size(smallShape, smallWidth, smallHeight);
                    ez.size(largeShape, largeWidth, largeHeight);

                    // LEFT = BIG
                    // RIGHT = SMALL

                    ez.guide.setPercentage(gv01, 0.28f);
                    ez.guide.setPercentage(gv02, 0.48f);

//                    ImageView leftMarker = ez.rect(50, 50, Color.RED);
//                    ImageView rightMarker = ez.rect(50, 50, Color.CYAN);
//                    rootView.addView(leftMarker);
//                    rootView.addView(rightMarker);
//                    leftMarker.setX(ez.guide.getPos(gv01) + (ez.getWidth(largeShape) / density));
//                    leftMarker.setY(ez.guide.getPos(gh01));
//                    rightMarker.setX(ez.guide.getPos(gv02));
//                    rightMarker.setY(ez.guide.getPos(gh02));
//
//                    Log.d("Option", "D: " + (rightMarker.getX() - leftMarker.getX()));
                }
            }

            if (morphLeft) {
                largeShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(largeShape, objectComparativeSound);
                    }
                });
                smallShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(smallShape, objectSingularSound);
                    }
                });
            } else {
                largeShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(largeShape, objectSingularSound);
                    }
                });
                smallShape.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectClicked(smallShape, objectComparativeSound);
                    }
                });
            }

//            smallShape.setBackgroundColor(Color.argb(100, 255, 0, 0));
//            largeShape.setBackgroundColor(Color.argb(100, 0, 255, 0));

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

    private void objectClicked(View view, String touchedObject){
        try{
            if (touchEnabled) {
                String objectToTouch = allData.getString("object_comparative_sound");
                if (objectToTouch.equalsIgnoreCase(touchedObject)) {
                    touchEnabled = false;

                    ImageView dummyView = new ImageView(context);
                    ViewGroup.MarginLayoutParams dummyViewLayoutParmas = new ViewGroup.MarginLayoutParams(
                            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                            ViewGroup.MarginLayoutParams.WRAP_CONTENT
                    );
                    dummyView.setLayoutParams(dummyViewLayoutParmas);
                    dummyView.setX(view.getX() + (ez.getWidth(view)/2));
                    dummyView.setY(view.getY() + (ez.getHeight(view)/2));

                    rootView.addView(dummyView);
                    starWorks.play(this, dummyView);

                    playSound(FetchResource.positiveAffirmation(context), () -> {
                        handler.delayed(() -> {
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }, 0);
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

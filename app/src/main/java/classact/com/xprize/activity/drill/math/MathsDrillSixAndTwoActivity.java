package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
import classact.com.xprize.utils.FisherYates;

public class MathsDrillSixAndTwoActivity extends DrillActivity {
    private ImageView shapeContainerOne;
    private ImageView shapeContainerTwo;
    private ImageView shapeContainerThree;
    private ImageView shapeContainerFour;
    private JSONObject allData;

    private boolean touchEnabled;

    private MathDrill06CViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_two);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06CViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        shapeContainerOne = (ImageView)findViewById(R.id.big_shape_one);
        shapeContainerTwo = (ImageView)findViewById(R.id.small_shape_one);
        shapeContainerThree = (ImageView)findViewById(R.id.big_shape_two);
        shapeContainerFour = (ImageView)findViewById(R.id.small_shape_two);

        shapeContainerOne.setImageResource(0);
        shapeContainerTwo.setImageResource(0);
        shapeContainerThree.setImageResource(0);
        shapeContainerFour.setImageResource(0);

        RelativeLayout.LayoutParams shapeContainerOneLP = (RelativeLayout.LayoutParams) shapeContainerOne.getLayoutParams();
        RelativeLayout.LayoutParams shapeContainerTwoLP = (RelativeLayout.LayoutParams) shapeContainerTwo.getLayoutParams();
        RelativeLayout.LayoutParams shapeContainerThreeLP = (RelativeLayout.LayoutParams) shapeContainerThree.getLayoutParams();
        RelativeLayout.LayoutParams shapeContainerFourLP = (RelativeLayout.LayoutParams) shapeContainerFour.getLayoutParams();

        shapeContainerOneLP.topMargin = 240;
        shapeContainerTwoLP.topMargin = 240;
        shapeContainerThreeLP.topMargin = 150;
        shapeContainerFourLP.topMargin = 150;

        shapeContainerOneLP.leftMargin = 350;
        shapeContainerTwoLP.leftMargin = 150;
        shapeContainerThreeLP.leftMargin = 350;
        shapeContainerFourLP.leftMargin = 150;

        shapeContainerOneLP.width = 475;
        shapeContainerTwoLP.width = 475;
        shapeContainerThreeLP.width = 475;
        shapeContainerFourLP.width = 475;

        shapeContainerOneLP.height = 475;
        shapeContainerTwoLP.height = 475;
        shapeContainerThreeLP.height = 475;
        shapeContainerFourLP.height = 475;

        shapeContainerOne.setLayoutParams(shapeContainerOneLP);
        shapeContainerTwo.setLayoutParams(shapeContainerTwoLP);
        shapeContainerThree.setLayoutParams(shapeContainerThreeLP);
        shapeContainerFour.setLayoutParams(shapeContainerFourLP);

        initialise();
    }

    public void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);

            JSONObject shapeOne = allData.getJSONObject("object_one");
            JSONObject shapeTwo = allData.getJSONObject("object_two");

            String shapeOneImage = shapeOne.getString("image_name");
            String shapeTwoImage = shapeTwo.getString("image_name");

            int shapeOneImageId = FetchResource.imageId(context, shapeOneImage);
            int shapeTwoImageId = FetchResource.imageId(context, shapeTwoImage);

            ImageView[] orderedSquareContainers = {
                    shapeContainerOne,
                    shapeContainerTwo,
                    shapeContainerThree,
                    shapeContainerFour
            };
            int numOfContainers = orderedSquareContainers.length;
            int[] s = FisherYates.shuffle(numOfContainers);
            ImageView[] shuffledSquareContainers = new ImageView[numOfContainers];

            for (int i = 0; i < numOfContainers; i++) {
                shuffledSquareContainers[i] = orderedSquareContainers[s[i]];
            }

            final float BIG_SCALE = 1.25f;
            final float SMALL_SCALE = 0.75f;

            boolean morphBigger = false;
            boolean isShapeOne = false;

            final String SMALL_SIZE = "small";
            final String BIG_SIZE = "big";

            final String comparativeSound = allData.getString("object_comparative_sound");
            String strippedComparativeSound = comparativeSound.replace("s_", ""); // location friendly

            if (strippedComparativeSound.contains(shapeOneImage)) {
                isShapeOne = true;
            } else if (strippedComparativeSound.contains(shapeTwoImage)) {
                isShapeOne = false;
            } else {
                String errorMessage = "drill data - invalid comparative shape (no valid shape given)";
                Globals.bugBar(this.findViewById(android.R.id.content), errorMessage, comparativeSound).show();
                throw new Exception(errorMessage);
            }

            if (strippedComparativeSound.contains(BIG_SIZE)) {
                morphBigger = true;
            } else if (strippedComparativeSound.contains(SMALL_SIZE)) {
                morphBigger = false;
            } else {
                String errorMessage = "drill data - invalid comparative shape (no valid shape size given)";
                Globals.bugBar(this.findViewById(android.R.id.content), errorMessage, comparativeSound).show();
                throw new Exception(errorMessage);
            }

            // Get singular sounds
            final String shapeOneSingularSound = shapeOne.getString("singular_sound");
            final String shapeTwoSingularSound = shapeTwo.getString("singular_sound");

            // Big shape one
            ImageView iv1 = shuffledSquareContainers[0];
            loadImage(iv1, shapeOneImageId);
            iv1 = scale(iv1, BIG_SCALE);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectClicked(shapeOneSingularSound);
                }
            });

            // Small shape two
            ImageView iv2 = shuffledSquareContainers[1];
            loadImage(iv2, shapeTwoImageId);
            iv2 = scale(iv2, SMALL_SCALE);
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectClicked(shapeTwoSingularSound);
                }
            });

            // Big shape two
            ImageView iv3 = shuffledSquareContainers[2];
            loadImage(iv3, shapeTwoImageId);
            iv3 = scale(iv3, BIG_SCALE);
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectClicked(shapeTwoSingularSound);
                }
            });

            // Small shape one
            ImageView iv4 = shuffledSquareContainers[3];
            loadImage(iv4, shapeOneImageId);
            iv4 = scale(iv4, SMALL_SCALE);
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectClicked(shapeOneSingularSound);
                }
            });

            if (morphBigger) {
                if (isShapeOne) {
                    // Big and shape one
                    iv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objectClicked(comparativeSound);
                        }
                    });
                } else {
                    // Big and shape two
                    iv3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objectClicked(comparativeSound);
                        }
                    });
                }
            } else {
                if (isShapeOne) {
                    // Small and shape one
                    iv4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objectClicked(comparativeSound);
                        }
                    });
                } else {
                    // Small and shape two
                    iv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objectClicked(comparativeSound);
                        }
                    });
                }
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

    private ImageView scale(ImageView iv, float sv) {
        iv.setScaleX(sv);
        iv.setScaleY(sv);
        return iv;
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
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                    sayObjectOne();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectOne() {
        try {
            String sound = allData.getJSONObject("object_one").getString("plural_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayAnd();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayAnd(){
        try {
            String sound = allData.getString("and_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectTwo();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectTwo() {
        try {
            String sound = allData.getJSONObject("object_two").getString("plural_sound");
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
                    sayObjectOneAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectOneAgain(){
        try {
            String sound = allData.getJSONObject("object_one").getString("plural_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayAndAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayAndAgain(){
        try {
            String sound = allData.getString("and_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectTwoAgain();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayObjectTwoAgain(){
        try {
            String sound = allData.getJSONObject("object_two").getString("plural_sound");
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

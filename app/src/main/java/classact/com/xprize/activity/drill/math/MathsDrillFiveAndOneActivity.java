package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.RandomExcluding;

public class MathsDrillFiveAndOneActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    private JSONObject allData;
    private JSONArray things;
    private JSONArray numbers;
    private MediaPlayer mp;

    private Handler handler;
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

    private SparseArray<ImageView> objectContainerImageViews;
    private SparseArray<CustomObject> numberObjects;
    private SparseArray<CustomObject> objectContainerObjects;
    private SparseArray<CustomObject> receptacleObjects;

    private LinkedHashMap<String, Integer> collectedObjects;
    private LinkedHashMap<String, ImageView> equationNumbers;

    private ImageView equationNumberOne;
    private ImageView equationNumberTwo;
    private ImageView equationAnswer;
    private ImageView equationSign;
    private ImageView equationEqualsSign;

    private boolean dragEnabled;
    private boolean touchNumbersEnabled;

    private int currentDragIndex;

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
        // numberOne.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberOne.setVisibility(View.INVISIBLE);

        numberTwo = (ImageView)findViewById(R.id.numeral_2);
        // numberTwo.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberTwo.setVisibility(View.INVISIBLE);

        numberThree = (ImageView)findViewById(R.id.numeral_3);
        // numberThree.setBackgroundColor(Color.argb(100, 255, 0, 255));
        numberThree.setVisibility(View.INVISIBLE);

        objectsContainer = (RelativeLayout)findViewById(R.id.itemsContainer);
        // objectsContainer.setBackgroundColor(Color.argb(100, 0, 255, 0));
        objectsContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < objectsContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) objectsContainer.getChildAt(i);
            iv.setImageResource(0);
            // iv.setBackgroundColor(Color.argb(150, 0, 0, (255 * (i+1)/objectsContainer.getChildCount())));
            iv.setVisibility(View.VISIBLE);
        }

        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        // itemsReceptacle.setBackgroundColor(Color.argb(100, 255, 100, 0));
        itemsReceptacle.setVisibility(View.VISIBLE);
        itemsReceptacle.setOnDragListener(this);

        RelativeLayout.LayoutParams irParams = (RelativeLayout.LayoutParams) itemsReceptacle.getLayoutParams();
        irParams.topMargin = 850;
        irParams.leftMargin = 15;
        itemsReceptacle.setLayoutParams(irParams);

        for (int i = 0; i < itemsReceptacle.getChildCount(); i++) {
            ImageView iv = (ImageView) itemsReceptacle.getChildAt(i);
            iv.setImageResource(0);
            // iv.setBackgroundColor(Color.argb(150, 0, 0, (255 * (i+1)/itemsReceptacle.getChildCount())));
            iv.setVisibility(View.VISIBLE);
        }

        handler = new Handler();
        initializeData();
    }

    private void addItemReceptacleA(int[] indexes) {

    }

    private void addItemReceptacleB(int[] indexes) {

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

            int numOfThings = things.length();

            // Validate number of things passed through
            // Must be equal to 2 for this activity
            if (numOfThings != 2) {
                Globals.bugBar(this.findViewById(android.R.id.content), "JSON Builder data",
                        "Invalid things count (" + numOfThings + ")").show();
                return;
            }

            // Setup numbers
            numberObjects = new SparseArray<>();
            for (int i = 0; i < numbers.length(); i++) {
                JSONObject number = numbers.getJSONObject(i);
                int numberValue = number.getInt("value");

                CustomObject numberObject = new CustomObject(number.getString("image"), number.getString("sound"));
                numberObjects.put(numberValue, numberObject);
            }


            // Init collected objects
            // Will be used to count number of objects in receptacle
            collectedObjects = new LinkedHashMap<>();

            // Setup things
            // Set up A
            JSONObject objectA = things.getJSONObject(0);
            int aCount = objectA.getInt("count");
            String aObjectImage = objectA.getString("image");
            String aObjectSound = objectA.getString("sound");
            int aObjectImageId = FetchResource.imageId(this, aObjectImage);
            collectedObjects.put(aObjectImage, 0); // No 'a's collected so far

            // Set up B
            JSONObject objectB = things.getJSONObject(1);
            int bCount = objectB.getInt("count");
            String bObjectImage = objectB.getString("image");
            String bObjectSound = objectB.getString("sound");
            int bObjectImageId = FetchResource.imageId(this, bObjectImage);
            collectedObjects.put(bObjectImage, 0); // No 'b's collected so far

            // Setup equation numbers
            equationNumbers = new LinkedHashMap<>();
            // Set equation number values to '0' (no objects collected so far)
            int zeroImageId = FetchResource.imageId(this, numberObjects.get(0).getImage());
            equationNumberOne.setImageResource(zeroImageId);
            equationNumberTwo.setImageResource(zeroImageId);
            // Add equation numbers to equation numbers hash map
            equationNumbers.put(aObjectImage, equationNumberOne);
            equationNumbers.put(bObjectImage, equationNumberTwo);
            // Set equation answer to question mark
            int questionMarkImageId = FetchResource.imageId(this, "questionmark_black");
            equationAnswer.setImageResource(questionMarkImageId);
            LinearLayout.LayoutParams equationAnswerLP = (LinearLayout.LayoutParams) equationAnswer.getLayoutParams();
            float sd = getResources().getDisplayMetrics().density;
            equationAnswerLP.width = (int) (sd * 125);
            equationAnswerLP.height = (int) (sd * 125);
            equationAnswerLP.leftMargin = equationAnswerLP.leftMargin - 25;
            equationAnswer.setLayoutParams(equationAnswerLP);

            // Setup object container for A and B
            int k = 0;
            int abCount = aCount + bCount;
            objectContainerImageViews = new SparseArray<>();
            objectContainerObjects = new SparseArray<>();

            for (int i = 0; i < abCount; i++) {

                // index formula
                int index = (i / 3) + (k++ * 8);

                // Get image view
                ImageView iv = (ImageView) objectsContainer.getChildAt(index);
                CustomObject co = null;

                if (i < aCount) {
                    co = new CustomObject(aObjectImage, aObjectSound);
                    iv.setImageResource(aObjectImageId);
                } else if (i < abCount) {
                    co = new CustomObject(bObjectImage, bObjectSound);
                    iv.setImageResource(bObjectImageId);
                }
                objectContainerObjects.put(index, co);
                iv.setTag("" + index);

                // add listener to iv
                iv.setOnTouchListener(this);

                // Add iv to list
                objectContainerImageViews.put(index, iv);

                // reset k if required
                if (k >= 3) {
                    k = 0;
                }
            }

            // Init receptacle images
            receptacleObjects = new SparseArray<>();

            // Set current drag index to -1
            currentDragIndex = -1;

            // Disable drag
            dragEnabled = false;

            // Disable touch
            touchNumbersEnabled = false; // stage 2

            handler.post(helpDamaWithMathsPrompt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Runnable helpDamaWithMathsPrompt = new Runnable() {
        @Override
        public void run() {
            try {
                String sound = allData.getString("help_dama_with_maths");
                playSound(sound, dragItemsPrompt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private Runnable dragItemsPrompt = new Runnable() {
        @Override
        public void run() {
            try {
                String sound = allData.getString("drag_items_onto_table");
                playSound(sound, new Runnable() {
                    @Override
                    public void run() {
                        dragEnabled = true;
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                String tag = (String) v.getTag();
                currentDragIndex = Integer.parseInt(tag);
                ClipData.Item item = new ClipData.Item(tag);
                ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                v.startDragAndDrop(dragData, dragShadow, v, 0);
                v.setVisibility(View.INVISIBLE);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        final int action = event.getAction();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DROP:
                if (dragEnabled) {
                    currentDragIndex = -1;

                    RelativeLayout container = (RelativeLayout) v;
                    ImageView iv = (ImageView) container.getChildAt(receptacleObjects.size());

                    String tag = (String) event.getClipDescription().getLabel();
                    int index = Integer.parseInt(tag);
                    CustomObject co = objectContainerObjects.get(index);

                    String image = co.getImage();
                    int imageId = FetchResource.imageId(this, image);
                    iv.setImageResource(imageId);

                    int nextIndex = receptacleObjects.size();

                    receptacleObjects.put(nextIndex, co);
                    objectContainerObjects.remove(index);

                    v.invalidate();

                    return true;
                } else {
                    return false;
                }
            case DragEvent.ACTION_DRAG_ENDED:
                if (event.getResult()) {
                    // Get index of latest added object
                    int objectIndex = receptacleObjects.size() - 1;

                    // Get image of latest object
                    String objectImage = receptacleObjects.get(objectIndex).getImage();

                    // Get sound
                    final String objectSound = receptacleObjects.get(objectIndex).getSound();

                    // Get current number of collected objects (type A or B specific)
                    int numOfCollectedObjects = collectedObjects.get(objectImage) + 1;
                    collectedObjects.put(objectImage, numOfCollectedObjects);

                    // Update image view for respective equation number
                    ImageView equationNumber = equationNumbers.get(objectImage);
                    int numberImageId = FetchResource.imageId(this, numberObjects.get(numOfCollectedObjects).getImage());
                    equationNumber.setImageResource(numberImageId);

                    // Get the number sound to play
                    String numberSound = numberObjects.get(numOfCollectedObjects).getSound();

                    // Play the sound
                    playSound(numberSound, new Runnable() {
                        @Override
                        public void run() {
                            playSound(objectSound, checkIfAllItemsCollected);
                        }
                    });
                } else {
                    if (currentDragIndex != -1) {
                        ImageView iv = objectContainerImageViews.get(currentDragIndex);
                        iv.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private Runnable checkIfAllItemsCollected = new Runnable() {
        @Override
        public void run() {
            try {
                if (objectContainerObjects.size() == 0) {
                    playSound(allData.getString("equation_sound"), new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nextStage();
                                }
                            }, 500);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private void endingSequence() {
        try {
            String equationSound = allData.getString("equation_sound");
            int answerValue = allData.getJSONObject("answer").getInt("value");
            final String answerSound = numberObjects.get(answerValue).getSound();
            playSound(equationSound, new Runnable() {
                @Override
                public void run() {
                    playSound(answerSound, new Runnable() {
                        @Override
                        public void run() {
                            if (mp != null) {
                                mp.release();
                            }
                            finish();
                        }
                    });
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void nextStage() {
        try {
            final int answerValue = allData.getJSONObject("answer").getInt("value");
            List<Integer> rndValues = RandomExcluding.nextInt(new ArrayList<Integer>(), 9, answerValue, 21, 2);
            rndValues.add(answerValue);

            int[] s = FisherYates.shuffle(rndValues.size());

            for (int i = 0; i < s.length; i++) {
                int si = s[i];
                final int chosenValue = rndValues.get(si);
                ImageView iv = (ImageView) numbersContainer.getChildAt(i);
                CustomObject number = numberObjects.get(chosenValue); // scrambled order
                int numberImageId = FetchResource.imageId(this, number.getImage());
                iv.setImageResource(numberImageId);

                final String numberSound = number.getSound();
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (touchNumbersEnabled) {
                            if (chosenValue == answerValue) {
                                touchNumbersEnabled = false;

                                LinearLayout.LayoutParams equationAnswerLP = (LinearLayout.LayoutParams) equationAnswer.getLayoutParams();
                                float sd = getResources().getDisplayMetrics().density;
                                equationAnswerLP.width = (int) (sd * 100);
                                equationAnswerLP.height = (int) (sd * 100);
                                equationAnswerLP.leftMargin = equationAnswerLP.leftMargin + 25;
                                equationAnswer.setLayoutParams(equationAnswerLP);

                                String answerImage = numberObjects.get(answerValue).getImage();
                                int answerImageId = FetchResource.imageId(THIS, answerImage);
                                equationAnswer.setImageResource(answerImageId);

                                playSound(numberSound, new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                                            @Override
                                            public void run() {
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        endingSequence();
                                                    }
                                                }, 250);
                                            }
                                        });
                                    }
                                });

                            } else {
                                playSound(numberSound, new Runnable() {
                                    @Override
                                    public void run() {
                                        playSound(FetchResource.negativeAffirmation(THIS), null);
                                    }
                                });
                            }
                        }
                    }
                });
                iv.setVisibility(View.VISIBLE);
            }

            String promptSound = allData.getString("can_you_find_and_touch");
            playSound(promptSound, new Runnable() {
                @Override
                public void run() {
                    touchNumbersEnabled = true;
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class CustomObject {

        private String image;
        private String sound;

        public CustomObject(String image, String sound) {
            this.image = image;
            this.sound = sound;
        }

        private String getImage() {
            return image;
        }

        private String getSound() {
            return sound;
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

package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.RandomExcluding;

public class MathsDrillFiveAndOneActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.numbers_container) RelativeLayout numbersContainer;
    @BindView(R.id.itemsContainer) RelativeLayout objectsContainer;
    @BindView(R.id.itemsReceptacle) RelativeLayout itemsReceptacle;
    @BindView(R.id.equation_one) ImageView equationNumberOne;
    @BindView(R.id.equation_two) ImageView equationNumberTwo;
    @BindView(R.id.equation_answer) ImageView equationAnswer;
    @BindView(R.id.equation_sign) ImageView equationSign;
    @BindView(R.id.equation_equals) ImageView equationEqualsSign;
    @BindView(R.id.numeral_1) ImageView numberOne;
    @BindView(R.id.numeral_2) ImageView numberTwo;
    @BindView(R.id.numeral_3) ImageView numberThree;

    private JSONObject allData;
    private JSONArray things;
    private JSONArray numbers;

    private SparseArray<ImageView> objectContainerImageViews;
    private SparseArray<CustomObject> numberObjects;
    private SparseArray<CustomObject> objectContainerObjects;
    private SparseArray<CustomObject> receptacleObjects;

    private LinkedHashMap<String, Integer> collectedObjects;
    private LinkedHashMap<String, ImageView> equationNumbers;

    private boolean dragEnabled;
    private boolean touchNumbersEnabled;

    private int currentDragIndex;

    private MathDrill05BViewModel vm;

    private View lastWrongNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_five_and_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill05BViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        equationNumberOne.setColorFilter(Color.argb(255, 255, 255, 255));
        equationNumberOne.setVisibility(View.VISIBLE);

        loadImage(equationSign, R.drawable.w_plus);
        equationSign.setVisibility(View.VISIBLE);

        equationNumberTwo.setColorFilter(Color.argb(255, 255, 255, 255));
        equationNumberTwo.setVisibility(View.VISIBLE);

        loadImage(equationEqualsSign, R.drawable.w_equals);
        equationEqualsSign.setVisibility(View.VISIBLE);

        equationAnswer.setColorFilter(Color.argb(255, 255, 255, 255));
        equationAnswer.setVisibility(View.VISIBLE);

        numbersContainer.setVisibility(View.VISIBLE);

//        numberOne.setVisibility(View.INVISIBLE);
//        numberTwo.setVisibility(View.INVISIBLE);
//        numberThree.setVisibility(View.INVISIBLE);

        objectsContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < objectsContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) objectsContainer.getChildAt(i);
            iv.setImageResource(0);
            iv.setVisibility(View.VISIBLE);
        }

        itemsReceptacle.setVisibility(View.VISIBLE);
        itemsReceptacle.setOnDragListener(this);

        RelativeLayout.LayoutParams irParams = (RelativeLayout.LayoutParams) itemsReceptacle.getLayoutParams();
        irParams.topMargin = 850;
        irParams.leftMargin = 15;
        itemsReceptacle.setLayoutParams(irParams);

        for (int i = 0; i < itemsReceptacle.getChildCount(); i++) {
            ImageView iv = (ImageView) itemsReceptacle.getChildAt(i);
            iv.setImageResource(0);
            iv.setVisibility(View.VISIBLE);
        }

        float density = getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams numbersContainerLP = (ViewGroup.MarginLayoutParams) numbersContainer.getLayoutParams();
        numbersContainerLP.topMargin = (int) (50 * density);
        numbersContainerLP.leftMargin = (int) ((940 + 16) * density);
        numbersContainer.setLayoutParams(numbersContainerLP);

        initializeData();
        prepareNumbers();
        handler.post(helpDamaWithMathsPrompt);
    }

    private void addItemReceptacleA(int[] indexes) {

    }

    private void addItemReceptacleB(int[] indexes) {

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
            loadImage(equationNumberOne, zeroImageId);
            loadImage(equationNumberTwo, zeroImageId);
            // Add equation numbers to equation numbers hash map
            equationNumbers.put(aObjectImage, equationNumberOne);
            equationNumbers.put(bObjectImage, equationNumberTwo);
            // Set equation answer to question mark
            int questionMarkImageId = FetchResource.imageId(this, "questionmark_black");
            loadImage(equationAnswer, questionMarkImageId);
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
//                    loadImage(iv, aObjectImageId);
                    iv.setImageResource(aObjectImageId);
                } else if (i < abCount) {
                    co = new CustomObject(bObjectImage, bObjectSound);
//                    loadImage(iv, bObjectImageId);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void prepareNumbers() {
        try {
            ez.unclickable(numberOne, numberTwo, numberThree);
            int answerValue = allData.getJSONObject("answer").getInt("value");
            List<Integer> rndValues = RandomExcluding.nextInt(new ArrayList<>(), 9, answerValue, 21, 2);
            rndValues.add(answerValue);

            int[] s = FisherYates.shuffle(rndValues.size());

            for (int i = 0; i < s.length; i++) {
                int si = s[i];
                final int chosenValue = rndValues.get(si);
                ImageView iv = (ImageView) numbersContainer.getChildAt(i);
                CustomObject number = numberObjects.get(chosenValue); // scrambled order
                int numberImageId = FetchResource.imageId(this, number.getImage());
                loadImage(iv, numberImageId);

                final String numberSound = number.getSound();
                iv.setOnClickListener((v) -> {
                    if (touchNumbersEnabled) {

                        // Uncolor last wrong number
                        if (lastWrongNumber != null && v != lastWrongNumber) {
                            unHighlight(lastWrongNumber);
                        }

                        if (chosenValue == answerValue) {
                            touchNumbersEnabled = false;
                            ez.unclickable(numberOne, numberTwo, numberThree);

                            LinearLayout.LayoutParams equationAnswerLP = (LinearLayout.LayoutParams) equationAnswer.getLayoutParams();
                            float sd = getResources().getDisplayMetrics().density;
                            equationAnswerLP.width = (int) (sd * 100);
                            equationAnswerLP.height = (int) (sd * 100);
                            equationAnswerLP.leftMargin = equationAnswerLP.leftMargin + 25;
                            equationAnswer.setLayoutParams(equationAnswerLP);

                            String answerImage = numberObjects.get(answerValue).getImage();
                            int answerImageId = FetchResource.imageId(context, answerImage);
                            loadImage(equationAnswer, answerImageId);

                            highlightCorrect(v);
                            playSound(numberSound, () -> {
                                starWorks.play(this, v);
                                playSound(FetchResource.positiveAffirmation(context), () -> {
                                    handler.delayed(() -> endingSequence(v), 250);
                                });
                            });

                        } else {
                            lastWrongNumber = v;
                            highlightWrong(v);
                            playSound(numberSound,
                                    () -> playSound(FetchResource.negativeAffirmation(context),
                                    () -> unHighlight(lastWrongNumber)));
                        }
                    }
                });
                iv.setAlpha(0.2f);
                iv.setVisibility(View.INVISIBLE);
            }
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
                    loadImage(iv, imageId);

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
                    loadImage(equationNumber, numberImageId);

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
                            handler.delayed(new Runnable() {
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

    private void endingSequence(View view) {
        try {
            String equationSound = allData.getString("equation_sound");
            int answerValue = allData.getJSONObject("answer").getInt("value");
            final String answerSound = numberObjects.get(answerValue).getSound();
            playSound(equationSound, () -> {
                starWorks.play(this, view);
                playSound(answerSound, () -> {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void nextStage() {
        try {
            ez.show(numberOne, numberTwo, numberThree);
            String promptSound = allData.getString("can_you_find_and_touch");
            playSound(promptSound, () -> {
                numberOne.setAlpha(1.0f);
                numberTwo.setAlpha(1.0f);
                numberThree.setAlpha(1.0f);
                ez.clickable(numberOne, numberTwo, numberThree);
                touchNumbersEnabled = true;
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
}

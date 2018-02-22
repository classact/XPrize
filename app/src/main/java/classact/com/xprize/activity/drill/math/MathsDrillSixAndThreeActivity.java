package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillSixAndThreeActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.monkeyEnclosure) RelativeLayout monkeyMouth;
    @BindView(R.id.itemsContainer) RelativeLayout objectsContainer;
    @BindView(R.id.numeral_1) ImageView numberOne;
    @BindView(R.id.numeral_2) ImageView numberTwo;
    @BindView(R.id.numeral_3) ImageView numberThree;

    private JSONObject allData;
    private JSONArray numbers;
    private int[] positions;
    private int draggedItems = 0;
    private int targetItems = 0;

    private SparseArray<NumberObject> numberObjects;

    private boolean dragEnabled;
    private boolean touchEnabled;

    private View lastWrongNumber;

    private MathDrill06DViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_three);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06DViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        numberOne.setOnClickListener((v) -> numberClicked(v,1));
        numberTwo.setOnClickListener((v) -> numberClicked(v,2));
        numberThree.setOnClickListener((v) -> numberClicked(v,3));

        monkeyMouth.setOnDragListener(this);
        int density = (int) getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams monkeyMouthLP = (ViewGroup.MarginLayoutParams) monkeyMouth.getLayoutParams();
        monkeyMouthLP.leftMargin = 570 * density;
        monkeyMouthLP.topMargin = 260 * density;
        monkeyMouthLP.width = 150 * density;
        monkeyMouthLP.height = 100 * density;
        monkeyMouth.setLayoutParams(monkeyMouthLP);
        initialise();
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("drill_specific_numbers");
            setupNumbers();
            setupNumberObjects();
            targetItems = allData.getInt("number_of_eaten_objects");

            touchEnabled = false;
            dragEnabled = false;

            String sound = allData.getString("monkey_is_hungry");
            playSound(sound, this::sayMonkeyEats);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupObjects(){
        try{
            dragEnabled = false; // disable drag
            draggedItems = 0;
            int count = allData.getInt("number_of_objects");
            String res = allData.getString("objects_image");
            int resId = FetchResource.imageId(context, res);
            for(int i = 0; i < count;i++){
                ImageView image = (ImageView) objectsContainer.getChildAt(i);
                loadImage(image, resId);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(this);
            }
        }
        catch(Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupNumbers(){
        try {
            ez.hide(numberOne, numberTwo, numberThree);
            positions = new int[3];
            Arrays.fill(positions, -1);
            Random rand = new Random();
            for (int i = 0; i < 3; i++) {
                int pos = rand.nextInt(3);
                if (positions[pos] == -1) {
                    positions[pos] = i;
                } else {
                    boolean done = false;
                    for (int j = 2; j > -1; j--) {
                        if (positions[j] == -1 && !done) {
                            positions[j] = i;
                            done = true;
                            pos = j;
                        }
                    }
                }
                switch (pos) {
                    case 0:
                        JSONObject number = numbers.getJSONObject(i);
                        String imageName = number.getString("image");
                        int imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberOne, imageId);
                        numberOne.setAlpha(0.2f);
                        break;
                    case 1:
                        number = numbers.getJSONObject(i);
                        imageName = number.getString("image");
                        imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberTwo, imageId);
                        numberTwo.setAlpha(0.2f);
                        break;
                    case 2:
                        number = numbers.getJSONObject(i);
                        imageName = number.getString("image");
                        imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberThree, imageId);
                        numberThree.setAlpha(0.2f);
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupNumberObjects() {
        try {
            numberObjects = new SparseArray<>();
            JSONArray numberObjectsArray = allData.getJSONArray("count_numbers");
            for (int i = 0; i < numberObjectsArray.length(); i++) {
                JSONObject number = numberObjectsArray.getJSONObject(i);
                String image = number.getString("image");
                String sound = number.getString("sound");
                int value = number.getInt("value");
                numberObjects.put(value, new NumberObject(image, sound, value));
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void numberClicked(View view, int position){
        if (touchEnabled) {
            try {

                // Uncolor last wrong number
                if (lastWrongNumber != null && view != lastWrongNumber) {
                    unHighlight(lastWrongNumber);
                }

                JSONObject number = numbers.getJSONObject(positions[position - 1]);
                int numberValue = number.getInt("value");
                String numberSound = numberObjects.get(numberValue).getSound();

                if (numberValue == targetItems) {
                    touchEnabled = false;

                    highlightCorrect(view);
                    playSound(numberSound, () -> end(view));
                } else {

                    lastWrongNumber = view;
                    highlightWrong(lastWrongNumber);

                    playSound(numberSound,
                            () -> playSound(FetchResource.negativeAffirmation(context),
                            () -> unHighlight(lastWrongNumber)));
                }
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void sayMonkeyEats(){
        try{
            String sound = allData.getString("he_eats_sound");
            playSound(sound, this::sayItemsEaten);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayItemsEaten(){
        try{
            String sound = allData.getString("objects_eaten");
            playSound(sound, this::sayDrag);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void sayDrag(){
        try{
            String sound = allData.getString("drag_sound");
            playSound(sound, this::sayobjectsEatenAgain);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayobjectsEatenAgain(){
        try{
            String sound = allData.getString("objects_eaten");
            playSound(sound, this::sayToMonkeysMouth);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkeysMouth(){
        try{
            String sound = allData.getString("to_the_monkey_sound");
            playSound(sound, () -> dragEnabled = true);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void nextSequence() {
        try {
            starWorks.play(this, monkeyMouth);
            playSound(FetchResource.positiveAffirmation(context),
                    () ->handler.delayed(this::sayTouchTheNumberThatMonkeyHasNow, 200));
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayTouchTheNumberThatMonkeyHasNow(){
        try{
            ez.show(numberOne, numberTwo, numberThree);
            String sound = allData.getString("touch_sound");
            playSound(sound, () -> {
                numberOne.setAlpha(1.0f);
                numberTwo.setAlpha(1.0f);
                numberThree.setAlpha(1.0f);
                touchEnabled = true;
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void end(View view) {
        starWorks.play(this, view);
        playSound(FetchResource.positiveAffirmation(context), () -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, dragShadow, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    if (dragEnabled) {
                        if (draggedItems < targetItems) {
                            draggedItems++;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        String numberSound = numberObjects.get(draggedItems).getSound();

                        if (draggedItems >= targetItems) {
                            dragEnabled = false;
                            playSound(numberSound, this::nextSequence);
                        } else {
                            playSound(numberSound, null);
                        }
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }

    private class NumberObject {
        private String image;
        private String sound;
        private int value;

        private NumberObject(String image, String sound, int value) {
            this.image = image;
            this.sound = sound;
            this.value = value;
        }

        public String getImage() {
            return image;
        }

        public String getSound() {
            return sound;
        }

        public int getValue() {
            return value;
        }
    }
}

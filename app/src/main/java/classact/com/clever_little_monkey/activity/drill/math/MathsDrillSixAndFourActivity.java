package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.utils.FetchResource;

public class MathsDrillSixAndFourActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.activity_maths_drill_six_and_four) ConstraintLayout parentView;
    @BindView(R.id.monkey) ImageView monkey;
    @BindView(R.id.monkeys_items) RelativeLayout itemsReceptacle;
    @BindView(R.id.damas_items) RelativeLayout objectsContainer;
    @BindView(R.id.equation) LinearLayout equation;
    @BindView(R.id.equation_one) ImageView equationNumberOne;
    @BindView(R.id.equation_two) ImageView equationNumberTwo;
    @BindView(R.id.equation_answer) ImageView equationAnswer;
    @BindView(R.id.equation_sign) ImageView equationSign;
    @BindView(R.id.equation_equals) ImageView equationEqualsSign;
    @BindView(R.id.number_01) ImageView numberOne;
    @BindView(R.id.number_02) ImageView numberTwo;
    @BindView(R.id.number_03) ImageView numberThree;

    @BindView(R.id.g_h_monkey) Guideline ghMonkey;
    @BindView(R.id.g_v_monkey) Guideline gvMonkey;
    @BindView(R.id.g_h_damas_items) Guideline ghDamasItems;
    @BindView(R.id.g_v_damas_items) Guideline gvDamasItems;
    @BindView(R.id.g_v_monkeys_items) Guideline gvMonkeysItems;
    @BindView(R.id.g_h_monkeys_items) Guideline ghMonkeysItems;
    @BindView(R.id.g_v_equation) Guideline gvEquation;
    @BindView(R.id.g_h_equation) Guideline ghEquation;

    @BindView(R.id.g_v_number_01) Guideline gvNumber01;
    @BindView(R.id.g_v_number_02) Guideline gvNumber02;
    @BindView(R.id.g_v_number_03) Guideline gvNumber03;
    @BindView(R.id.g_h_number_01) Guideline ghNumber01;
    @BindView(R.id.g_h_number_02) Guideline ghNumber02;
    @BindView(R.id.g_h_number_03) Guideline ghNumber03;

    private JSONObject allData;
    private JSONArray numbers;

    private int[] positions;
    private int draggedItems = 0;
    private int targetItems = 0;
    private int itemResId;
    private int segment = 0;

    private SparseArray<NumberObject> numberObjects;

    private boolean dragEnabled;
    private boolean touchEnabled;

    private MathDrill06EViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_four);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06EViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        ez.hide(equationNumberOne, equationSign, equationNumberTwo, equationEqualsSign, equationAnswer);
        ez.hide(numberOne, numberTwo, numberThree);

//        ez.guide.setPercentage(ghDamasItems, 0.775f);
//        ez.guide.setPercentage(gvDamasItems, 0.185f);
//        ez.guide.setPercentage(ghMonkeysItems, 0.735f);
//        ez.guide.setPercentage(gvMonkeysItems, 0.55f);
//        ez.guide.setPercentage(ghMonkey, 0.425f);
//        ez.guide.setPercentage(gvMonkey, 0.535f);
//        ez.guide.setPercentage(ghEquation, 0.8925f);
//        ez.guide.setPercentage(gvEquation, 0.4195f);
//
//        ez.guide.setPercentage(ghNumber01, 0.675f);
//        ez.guide.setPercentage(gvNumber01, 0.75f);
//        ez.guide.setPercentage(ghNumber02, 0.85f);
//        ez.guide.setPercentage(gvNumber02, 0.835f);
//        ez.guide.setPercentage(ghNumber03, 0.65f);

        numberOne.setOnClickListener((v) -> numberClicked(1));
        numberTwo.setOnClickListener((v) -> numberClicked(2));
        numberThree.setOnClickListener((v) -> numberClicked(3));

//        float density = getResources().getDisplayMetrics().density;
//        ViewGroup.MarginLayoutParams monkeyLP = (ViewGroup.MarginLayoutParams) monkey.getLayoutParams();
//        monkeyLP.width = (int) (240 * density);
//        monkeyLP.height = (int) (260 * density);
//        monkey.setLayoutParams(monkeyLP);
//
//        ez.sizeDpi(equationNumberOne, 90, 90);
//        ez.sizeDpi(equationNumberTwo, 90, 90);
//        ez.sizeDpi(equationAnswer, 90, 90);
//
//        equation.setGravity(Gravity.CENTER_VERTICAL);

        monkey.setOnDragListener(this);
        initialiseData();
    }

    private void initialiseData(){
        try {
            segment = 1;
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            setupNumberObjects();

            // Set equation sign
            loadImage(equationEqualsSign, R.drawable.equals);
            loadImage(equationSign, R.drawable.minus);

            String answerImage = allData.getString("answer_image");
            int answerImageId = FetchResource.imageId(context, answerImage);
            loadImage(equationAnswer, answerImageId);

            touchEnabled = false;
            dragEnabled = false;

            String sound = allData.getString("dama_has_sound");
            playSound(sound, this::sayNumber);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupObjects(){
        try{
            draggedItems = 0;
            int count = allData.getInt("number_of_objects");
//            int count = 15;
            targetItems = allData.getInt("number_of_given_objects");
//            targetItems = 10;

            String numOfObjectsImage = allData.getString("number_of_objects_image");
            int numOfObjectsImageId = FetchResource.imageId(context, numOfObjectsImage);

            String numOfObjectsGivenImage = allData.getString("number_of_given_objects_image");
            int numOfObjectsGivenImageId = FetchResource.imageId(context, numOfObjectsGivenImage);

            loadImage(equationNumberOne, numOfObjectsImageId);
            loadImage(equationNumberTwo, numOfObjectsGivenImageId);

            String objectsImage = allData.getString("objects_image");
            itemResId = FetchResource.imageId(context, objectsImage);
            Log.d("Count", String.valueOf(count));

            for(int i = 0; i < count; i++){
                ImageView iv = (ImageView) objectsContainer.getChildAt(i);
                loadImage(iv, itemResId);
                iv.setOnTouchListener(this);
            }
        }
        catch(Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupNumbers(){
        try {
            System.out.println("NUMBERS SETUP"+ numbers.length());
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
                        String numberImage = number.getString("image");
                        int numberImageId = FetchResource.imageId(context, numberImage);
                        loadImage(numberOne, numberImageId);
                        break;
                    case 1:
                        number = numbers.getJSONObject(i);
                        numberImage = number.getString("image");
                        numberImageId = FetchResource.imageId(context, numberImage);
                        loadImage(numberTwo, numberImageId);
                        break;
                    case 2:
                        number = numbers.getJSONObject(i);
                        numberImage = number.getString("image");
                        numberImageId = FetchResource.imageId(context, numberImage);
                        loadImage(numberThree, numberImageId);
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

    public void numberClicked(int position){
        if (touchEnabled) {
            try {
                JSONObject number = numbers.getJSONObject(positions[position - 1]);
                String numberSound = number.getString("sound");
                int numberValue = number.getInt("value");
                int count = allData.getInt("number_of_objects");
                if (numberValue == (count - targetItems)) {
                    touchEnabled = false;
                    equationAnswer.setVisibility(View.VISIBLE);
                    playSound(numberSound, () ->
                            playSound(FetchResource.positiveAffirmation(context), () -> {
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }));
                } else {
                    playSound(numberSound,
                            () -> playSound(FetchResource.negativeAffirmation(context), null));
                }
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void sayNumber(){
        try{
            String sound = allData.getString("number_of_objects_sound");
            playSound(sound, this::sayHeGives);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void sayHeGives(){
        try{
            String sound = allData.getString("he_gives_sound");
            playSound(sound, () -> {
                segment = 2;
                sayNumberToGive();
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayNumberToGive(){
        try{
            String sound = allData.getString("number_of_given_object_sound");
            playSound(sound, () -> {
                if (segment == 2) {
                    sayToMonkey();
                } else {
                    sayToMonkeySpace();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkey(){
        try{
            String sound = allData.getString("to_monkey_sound");
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
            playSound(sound, () -> {
                segment = 3;
                sayNumberToGive();
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkeySpace(){
        try{
            String sound = allData.getString("to_the_monkey_sound");
            playSound(sound, () -> dragEnabled = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void placeOnTable(){
        ImageView destination = (ImageView) itemsReceptacle.getChildAt(draggedItems - 1);
        loadImage(destination, itemResId);
        destination.setVisibility(View.VISIBLE);
    }

    private void showEquation (){
        try{
            equationNumberOne.setVisibility(View.VISIBLE);
            equationNumberTwo.setVisibility(View.VISIBLE);
            equationSign.setVisibility(View.VISIBLE);
            equationEqualsSign.setVisibility(View.VISIBLE);
            String sound = allData.getString("equation_sound");
            playSound(sound, this::playTouch);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playTouch(){
        try{
            ez.show(numberOne, numberTwo, numberThree);
            String sound = allData.getString("touch_sound");
            playSound(sound, () -> touchEnabled = true);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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
//                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                        return true;
//                    }
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

                        if ( draggedItems <= targetItems) {
                            placeOnTable();
                        }

                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        String numberSound = numberObjects.get(draggedItems).getSound();

                        if (draggedItems >= targetItems) {
                            dragEnabled = false;
                            playSound(numberSound, () -> playSound(FetchResource.positiveAffirmation(context), this::showEquation));
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

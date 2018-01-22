package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.RandomExcluding;

public class MathsDrillSevenAndOneActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.activity_maths_drill_seven_and_one) ConstraintLayout rootView;

    @BindView(R.id.beads) ImageView beads;

    @BindView(R.id.numeral_01) ImageView filler1;
    @BindView(R.id.numeral_02) ImageView filler2;
    @BindView(R.id.numeral_03) ImageView filler3;

    @BindView(R.id.number_01) ImageView item1;
    @BindView(R.id.number_02) ImageView item2;
    @BindView(R.id.number_03) ImageView item3;
    @BindView(R.id.number_04) ImageView item4;
    @BindView(R.id.number_05) ImageView item5;
    @BindView(R.id.number_06) ImageView item6;

    @BindView(R.id.g_h_numeral_01) Guideline ghNumeral01;
    @BindView(R.id.g_h_numeral_02) Guideline ghNumeral02;
    @BindView(R.id.g_h_numeral_03) Guideline ghNumeral03;

    @BindView(R.id.g_h_numbers) Guideline ghNumbers;
    @BindView(R.id.g_v_numbers) Guideline gvNumbers;

    private JSONObject allData;

    private int missingValue;
    private int currentValue;

    private LinkedHashMap<ImageView, Integer> draggableViewValues;
    private SparseArray<ImageView> staticNumberViews;

    private SparseArray<NumberObject> numberObjects;

    private boolean dragEnabled;
    private boolean isDragging;

    private MathDrill07BViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven_and_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill07BViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        loadImage(beads, R.drawable.counting_beads);

//        ez.highlight(
//                Color.argb(50, 0, 100, 100),
//                item1,
//                item2,
//                item3,
//                item4,
//                item5,
//                item6);

        float density = getResources().getDisplayMetrics().density;

        ez.guide.setPercentage(ghNumeral01, 0.7f);
        ez.guide.setPercentage(ghNumeral02, 0.7f);
        ez.guide.setPercentage(ghNumeral03, 0.7f);
        ez.guide.setPercentage(ghNumbers, 0.22f);

        ez.topEndMargin((int) (40 * density), (int) (350 * density), item1);
        ez.topEndMargin((int) (12 * density), (int) (190 * density), item2);
        ez.topEndMargin((int) (42 * density), (int) (30 * density), item3);
        ez.topStartMargin((int) (100 * density), (int) (40 * density), item4);
        ez.topStartMargin((int) (120 * density), (int) (185 * density), item5);
        ez.topStartMargin((int) (70 * density), (int) (340 * density), item6);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();
        initialiseData();
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupNumberObjects();
            initialiseObjects();

            dragEnabled = false;
            isDragging = false;

            String sound = allData.getString("pattern_introduction_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayHelpMonkey();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void initialiseObjects(){
        try {

            staticNumberViews = new SparseArray<>();
            JSONArray items = allData.getJSONArray("completion_pieces");
            ImageView[] itemViews = {item1, item2, item3, item4, item5, item6};

            missingValue = -1;

            int numOfItems = items.length();
            int numOfItemViews = itemViews.length;

            int indexShift = 0;
            int lengthShift = numOfItemViews - numOfItems;

            if (numOfItems > numOfItemViews) {
                int midPointIndex = numOfItemViews/2;
                if (numOfItemViews % 2 == 0) {
                    midPointIndex -= 1;
                }

                for (int i = 0; i < numOfItems; i++) {
                    JSONObject item = items.getJSONObject(i);
                    if (item.getInt("isRight") == 1) {
                        if (i > midPointIndex) {
                            indexShift = numOfItems - numOfItemViews;
                            lengthShift = 0;
                        }
                        break;
                    }
                }

            }

            for (int i = indexShift; i < numOfItems + lengthShift; i++) {
                JSONObject item = items.getJSONObject(i);

                String itemImage = item.getString("image");
                int itemImageId = FetchResource.imageId(context, itemImage);
                final int itemValue = item.getInt("value");

                boolean isMissing = item.getInt("isRight") == 1;

                ImageView itemView = itemViews[i - indexShift];
                // itemView.setBackgroundColor(Color.argb(100, 255, 0, 0));
                staticNumberViews.put(itemValue, itemView);

                if (isMissing) {
                    itemView.setOnDragListener(this);
                    missingValue = itemValue;
                } else {
                    loadImage(itemView, itemImageId);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dragEnabled) {
                                String numberSound = numberObjects.get(itemValue).getSound();
                                playSound(numberSound, null);
                            }
                        }
                    });
                }
            }

            if (missingValue == -1) {
                throw new Exception("No missing value found for pattern");
            }

            List<Integer> possibleNumbers = new ArrayList<>();
            possibleNumbers = RandomExcluding.nextInt(possibleNumbers, 0, missingValue, 21, 2);
            possibleNumbers.add(missingValue);

            draggableViewValues = new LinkedHashMap<>();

            ImageView[] fillerViews = {filler1, filler2, filler3};

            int numOfFillers = possibleNumbers.size();

            int[] s = FisherYates.shuffle(numOfFillers);

            for (int i = 0; i < numOfFillers; i++) {
                int si = s[i];
                int rndVal = possibleNumbers.get(si);
                String rndValImage = numberObjects.get(rndVal).getImage();
                int rndValImageId = FetchResource.imageId(context, rndValImage);

                ImageView fillerView = fillerViews[i];
                loadImage(fillerView, rndValImageId);
                fillerView.setOnTouchListener(this);

                draggableViewValues.put(fillerView, rndVal);
            }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private boolean isCorrectItem(){
        boolean isCorrectItem = false;
        try{
            if (currentValue == missingValue) {
                isCorrectItem = true;
            }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return isCorrectItem;
    }

    public void placeItem() {
        try {
            ImageView iv = staticNumberViews.get(missingValue);
            String missingValueImage = numberObjects.get(missingValue).getImage();
            int missingValueImageId = FetchResource.imageId(context, missingValueImage);
            loadImage(iv, missingValueImageId);
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayHelpMonkey() {
        try {
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            String sound = allData.getString("help_monkey_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayDrag();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayDrag(){
        try{
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragEnabled = true;
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void end() {
        try{
            playSound(FetchResource.positiveAffirmation(context), new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
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
                    currentValue = draggableViewValues.get(v);
//                    String tag = (String) v.getTag();
//                    ClipData.Item item = new ClipData.Item(tag);
//                    ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, dragShadow, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    System.out.println("Turn invisible");
                    if (dragEnabled) {
                        String numberSound = numberObjects.get(currentValue).getSound();
                        playSound(numberSound, null);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!isDragging) {
                        v.setVisibility(View.VISIBLE);
                        System.out.println("Turning back visible (Touch-based)");
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

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    isDragging = true;
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    if (dragEnabled) {
                        if (!isCorrectItem()) {
                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {
                                    int timeLeft = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                                    handler.delayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            playSound(FetchResource.negativeAffirmation(context), null);
                                        }
                                    }, timeLeft);
                                } else {
                                    playSound(FetchResource.negativeAffirmation(context), null);
                                }
                            }
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    isDragging = false;
                    if (event.getResult()) {
                        dragEnabled = false;
                        placeItem();
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        starWorks.play(this, v);
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                int timeLeft = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                                handler.delayed(this::end, timeLeft);
                            } else {
                                end();
                            }
                        }
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        System.out.println("Turning back visible");
                        return false;
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

package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;

public class MathsDrillSevenActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {

    @BindView(R.id.activity_maths_drill_seven) ConstraintLayout rootView;
    @BindView(R.id.pattern_container) LinearLayout itemsContainer;
    @BindView(R.id.shape_container) LinearLayout shapeContainer;
    @BindView(R.id.shape_01) ImageView filler1;
    @BindView(R.id.shape_02) ImageView filler2;
    @BindView(R.id.shape_03) ImageView filler3;
    @BindView(R.id.shape_04) ImageView filler4;
    @BindView(R.id.shape_05) ImageView filler5;
    @BindView(R.id.missing_shape) ImageView itemToFill;
    @BindView(R.id.pattern) ImageView pattern;

    private JSONObject allData;
    private boolean isInReceptacle;
    private int draggedItemIndex;
    private int currentItem = 0;
    private boolean endDrill;
    private ImageView[] fillerViews;
    private ImageView[] visibleFillerViews;

    private LinkedHashMap<ImageView, Integer> draggableViewIndexes;

    private boolean dragEnabled;

    private final int DRAG_TAG = 0;

    private MathDrill07AViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill07AViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        // itemsContainer.setBackgroundColor(Color.argb(100, 0, 255, 0));
        itemsContainer.setOnDragListener(this);

        fillerViews = new ImageView[5];
        fillerViews[0] = filler1;
        fillerViews[1] = filler2;
        fillerViews[2] = filler3;
        fillerViews[3] = filler4;
        fillerViews[4] = filler5;

        visibleFillerViews = new ImageView[fillerViews.length];

        dragEnabled = false;
        endDrill = false;

        initialiseData();
    }

    private void initialiseData() {
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            String patternImage = allData.getString("demo_pattern");
            int patternImageId = FetchResource.imageId(context, patternImage);
            loadImage(pattern, patternImageId);

            dragEnabled = false;

            String sound = allData.getString("pattern_introduction_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayPattern();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayPattern() {
        try {
            String sound = allData.getString("pattern_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayDrag();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayDrag() {
        try{
            setUpExercise();
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayObjectToDrag();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayObjectToDrag() {
        try{
            String sound = allData.getString("object_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayIntoTheSpace();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayIntoTheSpace() {
        try{
            String sound = allData.getString("into_the_space_sound");
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

    private void placeItem() {
        try{
            JSONObject item = allData.getJSONArray("completion_pieces").getJSONObject(draggedItemIndex);
            // Log.d("PLACE ITEM", "Index: " + draggedItemIndex);
            String image = item.getString("image");
            int imageId = FetchResource.imageId(context, image);
            itemToFill.setScaleType(ImageView.ScaleType.CENTER);
            loadImage(itemToFill, imageId);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private boolean isCorrectItem() {
        boolean isCorrectItem = false;
        try{
            JSONObject item = allData.getJSONArray("completion_pieces").getJSONObject(draggedItemIndex);
            if (item.getInt("isRight") == 1)
                isCorrectItem = true;
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return isCorrectItem;
    }

    private void setUpExercise() {
        try {

            JSONArray fillers = allData.getJSONArray("completion_pieces");

            int max = 5;
            int n = fillers.length();

            if (n > max) {
                throw new Exception("Too many shapes (Must be <= " + max + ")");
            }

            int[] s = FisherYates.shuffle(n);
            draggableViewIndexes = new LinkedHashMap<>();

            for (int i = 0; i < max; i++) {

                if (i < n) {
                    int si = s[i];
                    JSONObject obj = fillers.getJSONObject(si);
                    String objImage = obj.getString("image");
                    int objImageId = FetchResource.imageId(context, objImage);

                    ImageView fillerView = fillerViews[i];
                    loadImage(fillerView, objImageId);

                    draggableViewIndexes.put(fillerView, si);
                    fillerView.setOnTouchListener(this);
                    fillerView.setVisibility(View.VISIBLE);
                    visibleFillerViews[i] = fillerView;
                } else {
                    shapeContainer.removeView(fillerViews[i]);
                }
            }
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
                    draggedItemIndex = draggableViewIndexes.get(v);
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
                        if (!isCorrectItem()) {
                            playSound(FetchResource.negativeAffirmation(context), null);
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        placeItem();
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        handler.delayed(() -> starWorks.play(this, itemToFill), 100);
                        dragEnabled = false;
                        playSound(FetchResource.positiveAffirmation(context), new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });
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
}
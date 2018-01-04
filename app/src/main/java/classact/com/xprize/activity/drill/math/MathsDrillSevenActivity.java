package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.LinkedHashMap;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;

public class MathsDrillSevenActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {
    private LinearLayout itemsContainer;
    private ImageView filler1;
    private ImageView filler2;
    private ImageView filler3;
    private ImageView filler4;
    private ImageView filler5;
    private ImageView itemToFill;
    private JSONObject allData;
    private boolean isInReceptacle;
    private int draggedItemIndex;
    private int currentItem = 0;
    private ImageView pattern;
    private boolean endDrill;
    private ImageView[] fillerViews;
    private ImageView[] visibleFillerViews;

    private ImageView itemToFillSub;

    private LinkedHashMap<ImageView, Integer> draggableViewIndexes;

    private boolean dragEnabled;

    private final int DRAG_TAG = 0;

    private RelativeLayout parentView;

    private MathDrill07AViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill07AViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        parentView = (RelativeLayout) findViewById(R.id.activity_maths_drill_seven);

        itemsContainer = (LinearLayout)findViewById(R.id.itemsContainer);
        itemsContainer.setBackgroundColor(Color.argb(100, 0, 255, 0));
        itemsContainer.setOnDragListener(this);
        itemToFill = (ImageView)findViewById(R.id.missing);

        RelativeLayout.LayoutParams itemsContainerLayout = (RelativeLayout.LayoutParams) itemsContainer.getLayoutParams();
        itemsContainerLayout.leftMargin += 70;
        itemsContainer.setLayoutParams(itemsContainerLayout);
        itemsContainer.setPadding(0, 0, 0, 0);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int itemFillWidth = (int) ((float) 140 * density);

        LinearLayout.LayoutParams itemFillLayout = (LinearLayout.LayoutParams) itemToFill.getLayoutParams();
        itemFillLayout.leftMargin += 20;
        itemFillLayout.width = itemFillWidth;
        itemFillLayout.height = itemFillWidth;
        itemToFill.setLayoutParams(itemFillLayout);

        int ivFillWidth = (int) ((float) 175 * density);

        itemToFillSub = new ImageView(context);
        parentView.addView(itemToFillSub);
        ViewGroup.MarginLayoutParams itemToFillSubLP = (ViewGroup.MarginLayoutParams) itemToFillSub.getLayoutParams();
        itemToFillSubLP.width = ivFillWidth;
        itemToFillSubLP.height = ivFillWidth;
        itemToFillSub.setLayoutParams(itemToFillSubLP);
        itemToFillSub.setBackgroundColor(Color.argb(100, 255, 0, 0));
        itemToFillSub.setX(1670f);
        itemToFillSub.setY(430f);

        pattern = (ImageView)findViewById(R.id.pattern);

        filler1 = (ImageView)findViewById(R.id.filler1);
        filler2 = (ImageView)findViewById(R.id.filler2);
        filler3 = (ImageView)findViewById(R.id.filler3);
        filler4 = (ImageView)findViewById(R.id.filler4);
        filler5 = (ImageView)findViewById(R.id.filler5);

        fillerViews = new ImageView[5];
        fillerViews[0] = filler1;
        fillerViews[1] = filler2;
        fillerViews[2] = filler3;
        fillerViews[3] = filler4;
        fillerViews[4] = filler5;

        visibleFillerViews = new ImageView[fillerViews.length];

        dragEnabled = false;
        endDrill = false;
        resetFillers();
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
            String image = item.getString("image");
            int imageId = FetchResource.imageId(context, image);
            itemToFill.setImageResource(0);
            loadImage(itemToFillSub, imageId);
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

    private void resetFillers() {
        filler1.setVisibility(View.INVISIBLE);
        filler2.setVisibility(View.INVISIBLE);
        filler3.setVisibility(View.INVISIBLE);
        filler4.setVisibility(View.INVISIBLE);
        filler5.setVisibility(View.INVISIBLE);
    }

    private void setUpExercise() {
        try {
            itemToFill.setVisibility(View.VISIBLE);

            JSONArray fillers = allData.getJSONArray("completion_pieces");

            int numberOfItems = fillers.length();
            int[] s = FisherYates.shuffle(numberOfItems);
            draggableViewIndexes = new LinkedHashMap<>();

            for (int i = 0; i < numberOfItems; i++) {
                JSONObject obj = fillers.getJSONObject(i);
                String objImage = obj.getString("image");
                int objImageId = FetchResource.imageId(context, objImage);

                int si = s[i];
                ImageView fillerView = fillerViews[si];
                loadImage(fillerView, objImageId);
                draggableViewIndexes.put(fillerView, i);
                fillerView.setOnTouchListener(this);
                fillerView.setVisibility(View.VISIBLE);
                visibleFillerViews[si] = fillerView;
            }
            respaceFillers();
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void respaceFillers() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int screenWidth = displayMetrics.widthPixels - 450;

        int screenPadding = 16;
        int fillerWidth = 100;
        int fillerGapWidth = 50;

        int numOfFillers = 0;
        for (int i = 0; i < visibleFillerViews.length; i++) {
            if (visibleFillerViews[i] != null) {
                numOfFillers += 1;
            }
        }

        int fillerTotalWidth = (numOfFillers * fillerWidth) + ((numOfFillers - 1) * fillerGapWidth);

        screenWidth = (int) ((float) screenWidth / density);
        screenWidth -= 2 * screenPadding;

        int newWidth = (screenWidth - fillerTotalWidth) / 2;
        newWidth = (int) ((float) newWidth * density);
        boolean firstFillerView = false;

        for (int i = 0; i < visibleFillerViews.length; i++) {
            ImageView fillerView = visibleFillerViews[i];
            if (fillerView != null) {
                RelativeLayout.LayoutParams fillerViewLayout = (RelativeLayout.LayoutParams) fillerView.getLayoutParams();
                if (!firstFillerView) {
                    fillerViewLayout.leftMargin = newWidth;
                    firstFillerView = true;
                } else {
                    fillerViewLayout.leftMargin = ((int) (density * fillerGapWidth));
                }
                fillerView.setLayoutParams(fillerViewLayout);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    draggedItemIndex = draggableViewIndexes.get(v);
                    String tag = (String) v.getTag();
                    ClipData.Item item = new ClipData.Item(tag);
                    ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(dragData, dragShadow, v, 0);
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
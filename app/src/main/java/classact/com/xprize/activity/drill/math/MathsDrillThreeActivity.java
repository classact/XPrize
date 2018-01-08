package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.Square;
import classact.com.xprize.utils.SquarePacker;

public class MathsDrillThreeActivity extends DrillActivity {

    @BindView(R.id.activity_maths_drill_three) RelativeLayout rootView;

    @BindView(R.id.itemsReceptacle) RelativeLayout itemsReceptacle;
    @BindView(R.id.itemsContainer) RelativeLayout itemsContainer;

    private JSONObject allData;
    private int segment = 1;
    private int draggedItems = 0;
    private int targetItems = 0;
    private int itemResId;
    private boolean isInReceptacle;
    private boolean dragEnabled;
    private boolean drillComplete;
    private boolean endDrill;

    private MathDrill03ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_three);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill03ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        itemsReceptacle.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dragEnabled && !drillComplete) {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        isInReceptacle = true;
                        return true;
                    } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        isInReceptacle = false;
                        return true;
                    } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                        try {
                            draggedItems++;
                            if (draggedItems <= targetItems) {
                                return true;
                            }
                            return false;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                        try {
                            if (event.getResult()) {
                                ImageView view = (ImageView) event.getLocalState();
                                view.setVisibility(View.INVISIBLE);
                                placeOnTable();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
        dragEnabled = false;
        drillComplete = false;
        endDrill = false;
        initialiseData();
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            targetItems = allData.getInt("number_of_items");
            placeObjects();
            final String sound = allData.getString("monkey_wants_to_eat");
            handler.delayed(new Runnable() {
                @Override
                public void run() {
                    playSound(sound, new Runnable() {
                        @Override
                        public void run() {
                            sayNumber();
                        }
                    });
                }
            }, 500);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String getNumberSound(){
        String sound = "";
        try{
            switch (draggedItems){
                case 1:
                    sound = allData.getString("one_sound");
                    break;
                case 2:
                    sound = allData.getString("two_sound");
                    break;
                case 3:
                    sound = allData.getString("three_sound");
                    break;
                case 4:
                    sound = allData.getString("four_sound");
                    break;
                case 5:
                    sound = allData.getString("five_sound");
                    break;
                case 6:
                    sound = allData.getString("six_sound");
                    break;
                case 7:
                    sound = allData.getString("seven_sound");
                    break;
                case 8:
                    sound = allData.getString("eight_sound");
                    break;
                case 9:
                    sound = allData.getString("nine_sound");
                    break;
                case 10:
                    sound = allData.getString("ten_sound");
                    break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return sound;
    }

    private void placeOnTable(){
        ImageView item = (ImageView)itemsReceptacle.getChildAt(draggedItems - 1);
        loadImage(item, itemResId);
        item.setVisibility(View.VISIBLE);
        playSound(getNumberSound(), placementRunnable);
        if (draggedItems == targetItems){
            drillComplete = true;
            dragEnabled = false;
        }
    }

    private Runnable placementRunnable = new Runnable() {
        @Override
        public void run() {
            if (drillComplete && !endDrill) {
                endDrill = true;
                handler.delayed(new Runnable() {
                    @Override
                    public void run() {
                        playSound(FetchResource.positiveAffirmation(context), placementRunnable);
                    }
                }, 300);
            } else if (endDrill) {
                handler.removeCallbacks(placementRunnable);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    };

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            if (dragEnabled && !drillComplete) {
                isInReceptacle = false;
            }
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    private void placeObjects(){
        try {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;

            // itemsContainer.setBackgroundColor(Color.argb(100, 255, 0, 0));
            MarginLayoutParams itemsLayout = (MarginLayoutParams) itemsContainer.getLayoutParams();
            itemsLayout.width = (int) (395 * density);
            itemsLayout.height = (int) (790 * density);
            itemsContainer.setLayoutParams(itemsLayout);

            itemsContainer.removeAllViews();

            int totalItems = allData.getInt("total_items");

            int n = allData.getInt("total_items");
            int w = itemsLayout.width;
            int h = itemsLayout.height;
            int imageId = FetchResource.imageId(context, allData, "item");
            itemResId = imageId;

            SquarePacker squarePacker = new SquarePacker(w, h);
            Square[] squares = squarePacker.get(n);

            for (int i = 0; i < squares.length; i++) {
                // Get square
                Square square = squares[i];
                // Get drawable
                // Drawable d = getResources().getDrawable(imageId, null);
                // Create image view
                ImageView iv = new ImageView(context);
                // iv.setImageDrawable(d);
                loadImage(iv, imageId);
                iv.setScaleX(0.8f);
                iv.setScaleY(0.8f);
                // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                // Add image view to numbers layout
                itemsContainer.addView(iv);
                // Edit image view layout params
                RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                ivParams.leftMargin = 0;
                ivParams.topMargin = 0;
                ivParams.width = square.w;
                ivParams.height = square.w;
                iv.setLayoutParams(ivParams);
                // Set coordinates
                iv.setX((float) square.x);
                iv.setY((float) square.y);

                iv.setVisibility(View.VISIBLE);
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return dragItem(v,event);
                    }
                });
            }

            /*
            itemResId = FetchResource.imageId(context, allData, "item");
            ImageView item;
            for(int i=0; i < totalItems;i++){
                item = (ImageView)itemsContainer.getChildAt(i);
                item.setImageResource(itemResId);
                item.setVisibility(View.VISIBLE);
                item.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return dragItem(v,event);
                    }
                });
            }
            */
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayNumber(){
        try{
            String sound = allData.getString("number_of_items_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    if (segment == 1)
                        sayDrag();
                    else
                        sayToTheTable();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void sayDrag(){
        try{
            segment = 2;
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayToTheTable(){
        try{
            String sound = allData.getString("to_the_table_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragEnabled = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
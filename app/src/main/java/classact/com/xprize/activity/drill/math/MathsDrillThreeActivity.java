package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillThreeActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    private int segment = 1;
    private RelativeLayout itemsContainer;
    private int draggedItems = 0;
    private RelativeLayout itemsReceptacle;
    private int targetItems = 0;
    private int itemResId;
    private boolean isInReceptacle;
    private boolean dragEnabled;
    private boolean drillComplete;
    private boolean endDrill;
    private Handler handler;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_three);
        itemsContainer = (RelativeLayout) findViewById(R.id.itemsContainer);
        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        itemsReceptacle.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dragEnabled && !drillComplete) {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        isInReceptacle = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        isInReceptacle = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                        try {
                            draggedItems++;
                            System.out.println("DRAGGED ITEMS:::::: " + draggedItems);
                            if (draggedItems <= targetItems) {
                                placeOnTable();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            finish();
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                        try {
                            if (draggedItems > targetItems) {
                                playSound(FetchResource.negativeAffirmation(THIS), placementRunnable);
                            } else {
                                ImageView view = (ImageView) event.getLocalState();
                                view.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            finish();
                        }
                    }
                }
                return true;
            }
        });
        dragEnabled = false;
        drillComplete = false;
        endDrill = false;
        handler = new Handler();
        initialiseData();
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

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            targetItems = allData.getInt("number_of_items");
            placeObjects();
            final String sound = allData.getString("monkey_wants_to_eat");
            handler.postDelayed(new Runnable() {
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
        item.setImageResource(itemResId);
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSound(FetchResource.positiveAffirmation(THIS), placementRunnable);
                    }
                }, 300);
            } else if (endDrill) {
                if (mp != null) {
                    mp.release();
                }
                finish();
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
        try{
            int totalItems = allData.getInt("total_items");
            itemResId = FetchResource.imageId(this, allData, "item");
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
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
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
            if (mp != null){
                mp.release();
            }
            finish();
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
            mp.stop();
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}

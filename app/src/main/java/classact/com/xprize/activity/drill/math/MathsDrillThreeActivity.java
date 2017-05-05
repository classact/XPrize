package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
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
                                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
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

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            targetItems = allData.getInt("number_of_items");
            placeObjects();
            int sound = allData.getInt("monkey_wants_to_eat");
            mp = MediaPlayer.create(this, sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayNumber();
                }
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.start();
                }
            }, 1000);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private int getNumberSound(){
        int sound = 0;
        try{

            System.out.println("DRAGGED ITEMS>>>>>> " + draggedItems);

            switch (draggedItems){
                case 1:
                    sound = allData.getInt("one_sound");
                    break;
                case 2:
                    sound = allData.getInt("two_sound");
                    break;
                case 3:
                    sound = allData.getInt("three_sound");
                    break;
                case 4:
                    sound = allData.getInt("four_sound");
                    break;
                case 5:
                    sound = allData.getInt("five_sound");
                    break;
                case 6:
                    sound = allData.getInt("six_sound");
                    break;
                case 7:
                    sound = allData.getInt("seven_sound");
                    break;
                case 8:
                    sound = allData.getInt("eight_sound");
                    break;
                case 9:
                    sound = allData.getInt("nine_sound");
                    break;
                case 10:
                    sound = allData.getInt("ten_sound");
                    break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
        return sound;
    }

    private void placeOnTable(){
        ImageView item = (ImageView)itemsReceptacle.getChildAt(draggedItems - 1);
        item.setImageResource(itemResId);
        item.setVisibility(View.VISIBLE);
        playSound(getNumberSound());
        if (draggedItems == targetItems){
            drillComplete = true;
            dragEnabled = false;
        }
    }

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (drillComplete && !endDrill) {
                        endDrill = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                            }
                        }, 300);
                    } else if (endDrill) {
                        mp.release();
                        finish();
                    }
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
            itemResId = allData.getInt("item");
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
            int sound = allData.getInt("number_of_items_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (segment == 1)
                        sayDrag();
                    else
                        sayToTheTable();
                }
            });
            mp.start();
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
            int sound = allData.getInt("drag_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayNumber();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayToTheTable(){
        try{
            int sound = allData.getInt("to_the_table_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    dragEnabled = true;
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
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

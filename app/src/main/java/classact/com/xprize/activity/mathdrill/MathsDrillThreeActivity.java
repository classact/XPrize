package classact.com.xprize.activity.mathdrill;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_three);
        itemsContainer = (RelativeLayout) findViewById(R.id.itemsContainer);
        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        itemsReceptacle.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED)
                    isInReceptacle = true;
                else if (action == DragEvent.ACTION_DRAG_EXITED)
                    isInReceptacle = false;
                else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                    try {
                        draggedItems ++;
                        if ( draggedItems <= targetItems) {
                            placeOnTable();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                    try {
                        if ( draggedItems > targetItems) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                        }
                        else{
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                return true;
            }
        });
        initialiseData();
    }

    private int getNumberSound(){
        int sound = 0;
        try{
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
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){public void run(){finish();}},3000);
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
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDrag(data, shadowBuilder, view, 0);
            isInReceptacle = false;
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
            finish();
        }
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            targetItems = allData.getInt("number_of_items");
            placeObjects();
            int sound = allData.getInt("monkey_wants_to_eat");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
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
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }
}

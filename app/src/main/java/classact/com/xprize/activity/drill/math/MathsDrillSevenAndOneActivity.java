package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSevenAndOneActivity extends AppCompatActivity {
    private RelativeLayout itemsReceptacle;
    private ImageView filler1;
    private ImageView filler2;
    private ImageView filler3;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView itemToFill;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private MediaPlayer mp;
    private JSONObject allData;
    private boolean isInReceptacle;
    private int draggedItemIndex;
    private int currentItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven_and_one);
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
                        if ( isCorrectItem()) {
                            placeItem();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                    try {
                        if ( !isCorrectItem()) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                        }
                        else{
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                            playSoundAndFinish(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
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

    private boolean isCorrectItem(){
        boolean isCorrectItem = false;
        try{
            JSONObject item = allData.getJSONArray("completion_pieces").getJSONObject(draggedItemIndex);
            if (item.getInt("isRight") == 1)
                isCorrectItem = true;
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
        return isCorrectItem;
    }

    private void placeItem(){
        try{
            JSONObject item = allData.getJSONArray("completion_pieces").getJSONObject(draggedItemIndex);
            itemToFill.setImageResource(item.getInt("image"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
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

    private void playSoundAndFinish(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    finish();
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

    private void initialiseObjects(){
        try{
            JSONArray numbers = allData.getJSONArray("pattern_to_complete");
            item1 = (ImageView)findViewById(R.id.item_one);
            if (numbers.getInt(0) > 0)
                item1.setImageResource(numbers.getInt(0));
            else
                itemToFill = item1;
            item2 = (ImageView)findViewById(R.id.item_two);
            if (numbers.getInt(1) > 0)
                item2.setImageResource(numbers.getInt(1));
            else
                itemToFill = item2;
            item3 = (ImageView)findViewById(R.id.item_three);
            if (numbers.getInt(2) > 0)
                item3.setImageResource(numbers.getInt(2));
            else
                itemToFill = item3;
            item4 = (ImageView)findViewById(R.id.item_four);
            if (numbers.getInt(3) > 0)
                item4.setImageResource(numbers.getInt(4));
            else
                itemToFill = item4;
            item5 = (ImageView)findViewById(R.id.item_five);
            if (numbers.getInt(4) > 0)
                item5.setImageResource(numbers.getInt(5));
            else
                itemToFill = item5;
            item6 = (ImageView)findViewById(R.id.item_six);
            if (numbers.getInt(5) > 0)
                item6.setImageResource(numbers.getInt(5));
            else
                itemToFill = item6;
            JSONArray fillers = allData.getJSONArray("completion_pieces");
            filler1 = (ImageView)findViewById(R.id.numeral_1);
            filler1.setImageResource(fillers.getJSONObject(0).getInt("image"));
            filler1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    draggedItemIndex = 0;
                    return dragItem(v,event);
                }
            });
            filler2 = (ImageView)findViewById(R.id.numeral_2);
            filler2.setImageResource(fillers.getJSONObject(1).getInt("image"));
            filler2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    draggedItemIndex = 1;
                    return dragItem(v,event);
                }
            });
            filler3 = (ImageView)findViewById(R.id.numeral_3);
            filler3.setImageResource(fillers.getJSONObject(2).getInt("image"));
            filler3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    draggedItemIndex = 2;
                    return dragItem(v,event);
                }
            });
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
            initialiseObjects();
            int sound = allData.getInt("pattern_introduction_sound");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayHelpMonkey();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayHelpMonkey() {
        try {
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            int sound = allData.getInt("help_monkey_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayDrag();
                }
            });
            mp.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void sayDrag(){
        try{
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            int sound = allData.getInt("drag_sound");
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

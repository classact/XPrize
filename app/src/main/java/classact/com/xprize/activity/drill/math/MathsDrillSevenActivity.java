package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSevenActivity extends AppCompatActivity {
    private LinearLayout itemsContainer;
    private ImageView filler1;
    private ImageView filler2;
    private ImageView filler3;
    private ImageView filler4;
    private ImageView filler5;
    private ImageView itemToFill;
    private MediaPlayer mp;
    private JSONObject allData;
    private boolean isInReceptacle;
    private int draggedItemIndex;
    private int currentItem = 0;
    private ImageView pattern;
    private Handler handler;
    private boolean dragEnabled;
    private boolean endDrill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven);
        itemsContainer = (LinearLayout)findViewById(R.id.itemsContainer);
        itemsContainer.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dragEnabled) {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        isInReceptacle = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        isInReceptacle = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                        try {
                            if (isCorrectItem()) {
                                placeItem();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            finish();
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                        try {
                            if (!isCorrectItem()) {
                                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                            } else {
                                ImageView view = (ImageView) event.getLocalState();
                                view.setVisibility(View.INVISIBLE);
                                dragEnabled = false;
                                playSoundAndFinish(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (mp != null) {
                                mp.release();
                            }
                            finish();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        itemToFill = (ImageView)findViewById(R.id.missing);
        pattern = (ImageView)findViewById(R.id.pattern);
        filler1 = (ImageView)findViewById(R.id.filler1);
        filler1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                draggedItemIndex = 0;
                return dragItem(v,event);
            }
        });
        filler2 = (ImageView)findViewById(R.id.filler2);
        filler2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                draggedItemIndex = 1;
                return dragItem(v,event);
            }
        });
        filler3 = (ImageView)findViewById(R.id.filler3);
        filler3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                draggedItemIndex = 2;
                return dragItem(v,event);
            }
        });
        filler4 = (ImageView)findViewById(R.id.filler4);
        filler4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                draggedItemIndex = 3;
                return dragItem(v,event);
            }
        });
        filler5 = (ImageView)findViewById(R.id.filler5);
        filler5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                draggedItemIndex = 4;
                return dragItem(v,event);
            }
        });
        handler = new Handler();
        dragEnabled = false;
        endDrill = false;
        resetFillers();
        initialiseData();
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            pattern.setImageResource(allData.getInt("demo_pattern"));
            int sound = allData.getInt("pattern_introduction_sound");
            mp = MediaPlayer.create(this, sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayPattern();
                }
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.start();
                }
            }, 500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayPattern(){
        try {
            int soundId = allData.getInt("pattern_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    sayDrag();
                }
            });
            mp.prepare();
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
            setUpExercise();
            int sound = allData.getInt("drag_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    sayObjectToDrag();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayObjectToDrag(){
        try{
            int sound = allData.getInt("object_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    sayIntoTheSpace();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }

    private void sayIntoTheSpace(){
        try{
            int sound = allData.getInt("into_the_space_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dragEnabled = true;
                        }
                    }, 500);
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
        }
    }



    private void placeItem(){
        try{
            JSONObject item = allData.getJSONArray("completion_pieces").getJSONObject(draggedItemIndex);
            itemToFill.setImageResource(item.getInt("image"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null){
                mp.release();
            }
            finish();
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
            if (mp != null){
                mp.release();
            }
            finish();
        }
        return isCorrectItem;
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
            if (mp != null){
                mp.release();
            }
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
            if (dragEnabled) {
                isInReceptacle = false;
            }
            return true;
        } else {
            return false;
        }
    }


    private void resetFillers(){
        filler1.setVisibility(View.INVISIBLE);
        filler2.setVisibility(View.INVISIBLE);
        filler3.setVisibility(View.INVISIBLE);
        filler4.setVisibility(View.INVISIBLE);
        filler5.setVisibility(View.INVISIBLE);
    }


    private void setUpExercise(){
        try {
            itemToFill.setVisibility(View.VISIBLE);
            JSONArray fillers = allData.getJSONArray("completion_pieces");
            for (int j = 0; j < fillers.length();j++){
                JSONObject obj = fillers.getJSONObject(j);
                switch (j){
                    case 0:
                        filler1.setVisibility(View.VISIBLE);
                        filler1.setImageResource(obj.getInt("image"));
                        break;
                    case 1:
                        filler2.setVisibility(View.VISIBLE);
                        filler2.setImageResource(obj.getInt("image"));
                        break;
                    case 2:
                        filler3.setVisibility(View.VISIBLE);
                        filler3.setImageResource(obj.getInt("image"));
                        break;
                    case 3:
                        filler4.setVisibility(View.VISIBLE);
                        filler4.setImageResource(obj.getInt("image"));
                        break;
                    case 4:
                        filler5.setVisibility(View.VISIBLE);
                        filler5.setImageResource(obj.getInt("image"));
                        break;
                }
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
}

package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillSixActivity extends DrillActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;
    private ImageView receptacleBox1;
    private ImageView receptacleBox2;
    private ImageView demo_Item;
    private LinearLayout demoItemContainer;
    private ImageView demo_Item_one;
    private ImageView demo_Item_two;
    private RelativeLayout receptacles;
    private RelativeLayout items;
    private LinearLayout demo_letters;
    private int currentItem;
    private String drillData;
    public float x;
    public float y;
    public int image1;
    public int image2;
    public int [] positions;
    public String drillSound;
    public boolean isInReceptacle1;
    public boolean isInReceptacle2;
    private int correctItems = 0;
    private JSONObject data;
    private boolean itemsEnabled;
    private Runnable mRunnable;

    private final Context THIS = this;

    private SoundDrill06ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_six);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill06ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        itemsEnabled = false;
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item4 = (ImageView)findViewById(R.id.item4);
        item5 = (ImageView)findViewById(R.id.item5);
        item6 = (ImageView)findViewById(R.id.item6);
        item7 = (ImageView)findViewById(R.id.item7);
        item8 = (ImageView)findViewById(R.id.item8);
        demo_Item = (ImageView)findViewById(R.id.lonely_letter);
        demoItemContainer = (LinearLayout) findViewById(R.id.lonely_letter_container);
        items = (RelativeLayout)findViewById(R.id.items);
        demo_Item_one = (ImageView)findViewById(R.id.demo_letter_one);
        demo_Item_two = (ImageView)findViewById(R.id.demo_letter_two);
        demo_letters = (LinearLayout) findViewById(R.id.demo_letters);
        receptacleBox1 = (ImageView)findViewById(R.id.receptacle1_label);
        receptacleBox2 = (ImageView)findViewById(R.id.receptacle2_label);
        receptacles= (RelativeLayout)findViewById(R.id.recetacles);
        drillData = getIntent().getExtras().getString("data");
        initialiseData();

        item1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 0;
                return dragItem(v,event);
            }
        });
        item2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 1;
                return dragItem(v,event);
            }
        });
        item3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 2;
                return dragItem(v,event);
            }
        });
        item4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 3;
                return dragItem(v,event);
            }
        });

        item5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 4;
                return dragItem(v,event);
            }
        });

        item6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 5;
                return dragItem(v,event);
            }
        });

        item7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 6;
                return dragItem(v,event);
            }
        });

        item8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 7;
                return dragItem(v,event);
            }
        });

        receptacleBox1.setOnDragListener((v, event) -> {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        isInReceptacle1 = true;
                    }else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        isInReceptacle1 = false;
                    } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle1) {
                        if ( positions[currentItem] == image1) {
                            Globals.playStarWorks(THIS, receptacleBox1);
                            reward();
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle1) {
                        if ( positions[currentItem] != image1) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
                        } else {
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        );

        receptacleBox2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED) {
                        isInReceptacle2 = true;
                    } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                        isInReceptacle2 = false;
                    } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle2 ) {
                        if ( positions[currentItem] == image2) {
                            Globals.playStarWorks(THIS, receptacleBox2);
                            reward();
                        }
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle2) {
                        if ( positions[currentItem] != image2) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
                        } else {
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        });
        playWeCanWriteTheLetter();
    }

    private void initialiseData(){
        try{
            data = new JSONObject(drillData);
            Random rand = new Random();
            drillSound = data.getString("letter_sound");
            if (rand.nextInt(2) == 0) {
                image1 = data.getInt("small_letter");
                image2 = data.getInt("big_letter");
            }
            else{
                image2 = data.getInt("small_letter");
                image1 = data.getInt("big_letter");
            }
            ((ImageView)findViewById(R.id.receptacle1_label)).setImageResource(image1);
            ((ImageView)findViewById(R.id.receptacle2_label)).setImageResource(image2);
            demo_Item_one.setImageResource(data.getInt("small_letter"));
            demo_Item_two.setImageResource(data.getInt("big_letter"));
            positions = new int[8];
            Arrays.fill(positions,0);
            int countOne = 0;
            int countTwo = 0;
            for(int i = 0; i < 8; i++){
                boolean assigned = false;
                while (!assigned) {
                    int aOrB = rand.nextInt(100);
                    if (aOrB % 2 == 0 && countOne < 4) {
                        countOne++;
                        positions[i] = image1;
                        assigned = true;
                    }
                    else if ( countTwo < 4) {
                        countTwo++;
                        positions[i] = image2;
                        assigned = true;
                    }
                    else if ((countOne == 4 )&& (countTwo == 4))
                        assigned = true;
                }
            }
            item1.setImageResource(positions[0]);
            item2.setImageResource(positions[1]);
            item3.setImageResource(positions[2]);
            item4.setImageResource(positions[3]);
            item5.setImageResource(positions[4]);
            item6.setImageResource(positions[5]);
            item7.setImageResource(positions[6]);
            item8.setImageResource(positions[7]);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playWeCanWriteTheLetter() {
        try {

            // In two ways
            mRunnable = null;
            mRunnable = () -> handler.delayed(this::playInTwoWays, 350);

            String sound = data.getString("we_can_write_the_letter");
            playSound(sound, () -> playDrillLetterAndRunnableAfterCompletion(mRunnable));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDrillLetterAndRunnableAfterCompletion(final Runnable runnable){
        try {
            playSound(drillSound, runnable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDrillLetter(){
        try {
            playSound(drillSound, () -> handler.delayed(this::playInTwoWays, 350));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playInTwoWays(){
        try {
            String sound = data.getString("in_two_ways");
            playSound(sound, () -> handler.delayed(this::playStartLowerCase, 650));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playStartLowerCase(){
        try {
            // Play upper case
            mRunnable = null;
            mRunnable = () -> handler.delayed(this::playUpperCase, 650);

            demoItemContainer.setVisibility(View.VISIBLE);
            demo_letters.setVisibility(View.GONE);
            demo_Item.setImageResource(data.getInt("small_letter"));

            String sound = data.getString("this_is_the_lower_case");
            playSound(sound, () -> playDrillLetterAndRunnableAfterCompletion(mRunnable));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playUpperCase(){
        try {
            // Play drag the letters
            mRunnable = null;
            mRunnable = () -> handler.delayed(this::playDragTheLetters, 610);

            demo_Item.setImageResource(data.getInt("big_letter"));
            String sound = data.getString("this_is_the_upper_case");
            playSound(sound, () -> playDrillLetterAndRunnableAfterCompletion(mRunnable));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDragTheLetters(){
        try {
            demoItemContainer.setVisibility(View.GONE);
            receptacles.setVisibility(View.VISIBLE);
            items.setVisibility(View.VISIBLE);
            String sound = data.getString("drag_the_letters");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp) -> {
                    mp.start();
                    handler.delayed(() -> itemsEnabled = true,mp.getDuration() - 100);
                });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mediaPlayer.stop();
            });
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void reward() {
        correctItems ++;
        if (correctItems == 8){
            mRunnable = null;
            mRunnable = () -> handler.delayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 450);
            playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
        } else {
            playSound(ResourceSelector.getPositiveAffirmationSound(this), null);
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (itemsEnabled) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                isInReceptacle1 = false;
                isInReceptacle2 = false;
                return true;
            }
        }
        return false;
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            playSound(soundId, mRunnable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
        item4.setEnabled(enable);
        item5.setEnabled(enable);
        item6.setEnabled(enable);
        item7.setEnabled(enable);
        item8.setEnabled(enable);
    }
}

package classact.com.xprize.activity.drill.math;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillFourActivity extends AppCompatActivity {
    private RelativeLayout rightContainer;
    private RelativeLayout leftContainer;
    private ImageView leftNumber;
    private ImageView rightNumber;
    private JSONObject allData;
    private MediaPlayer mp;
    private int segment = 1;
    private boolean touchEnabled;
    private boolean drillComplete;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_four);
        leftContainer = (RelativeLayout)findViewById(R.id.left_container);
        rightContainer = (RelativeLayout)findViewById(R.id.right_container);
        leftNumber = (ImageView)findViewById(R.id.leftNumber);
        leftNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(true);
            }
        });
        rightNumber = (ImageView)findViewById(R.id.rightNumber);
        rightNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(false);
            }
        });
        touchEnabled = false;
        drillComplete = false;
        initialise();
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

    private void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupItems();
            String sound = allData.getString("monkey_has");
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

    private void clicked(boolean left){
        if (touchEnabled && !drillComplete) {
            try {
                String checkBigger = allData.getString("check_bigger");
                int leftItems = allData.getInt("number_of_left_items");
                int rightItems = allData.getInt("number_of_right_items");
                boolean isRight = false;
                if (checkBigger.equalsIgnoreCase("yes")) {
                    if (left && leftItems >= rightItems)
                        isRight = true;
                    else if (!left && rightItems >= leftItems)
                        isRight = true;
                } else {
                    if (left && leftItems <= rightItems)
                        isRight = true;
                    else if (!left && rightItems <= leftItems)
                        isRight = true;
                }
                if (isRight) {
                    touchEnabled = false;
                    drillComplete = true;
                    playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                        @Override
                        public void run() {
                            if (mp != null) {
                                mp.release();
                            }
                            finish();
                        }
                    });
                } else {
                    playSound(FetchResource.negativeAffirmation(THIS), null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setupItems(){
        try{
            int totalItems = allData.getInt("number_of_left_items");
            int resId = FetchResource.imageId(THIS, allData, "left_items_item");
            int numberId = FetchResource.imageId(THIS, allData, "left_number_image");
            leftNumber.setImageResource(numberId);
            ImageView item;
            for(int i = 0; i < 20; i++){
                item = (ImageView)leftContainer.getChildAt(i);
                if (i < totalItems) {
                    item.setImageResource(resId);
                }
                else {
                    item.setVisibility(View.INVISIBLE);
                }
            }
            totalItems = allData.getInt("number_of_right_items");
            resId = FetchResource.imageId(THIS, allData, "right_items_item");
            numberId = FetchResource.imageId(THIS, allData, "right_number_image");
            rightNumber.setImageResource(numberId);
            for(int i = 0; i < 20; i++){
                item = (ImageView)rightContainer.getChildAt(i);
                if (i < totalItems) {
                    item.setImageResource(resId);
                }
                else {
                    item.setVisibility(View.INVISIBLE);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void sayNumber(){
        try{
            String sound = allData.getString("number_of_left_items_sound");
            if (segment == 2) {
                sound = allData.getString("number_of_right_items_sound");
            }
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    if (segment == 1) {
                        sayAnd();
                    } else {
                        sayWhich();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void sayAnd() {
        try {
            segment = 2;
            String sound = allData.getString("and_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayWhich() {
        try {
            segment = 2;
            String sound = allData.getString("more_of_question");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayTouch();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayTouch() {
        try {
            segment = 2;
            String sound = allData.getString("touch_the_number");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    touchEnabled = true;
                }
            });
        } catch (Exception ex) {
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
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}

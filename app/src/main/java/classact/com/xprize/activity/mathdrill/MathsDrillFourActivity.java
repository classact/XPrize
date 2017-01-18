package classact.com.xprize.activity.mathdrill;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillFourActivity extends AppCompatActivity {
    private RelativeLayout rightContainer;
    private RelativeLayout leftContainer;
    private ImageView leftNumber;
    private ImageView rightNumber;
    private JSONObject allData;
    private MediaPlayer mp;
    private int segment = 1;

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
        initialise();
    }

    private void clicked(boolean left){
        try{
            String checkBigger = allData.getString("check_bigger");
            int leftItems = allData.getInt("number_of_left_items");
            int rightItems = allData.getInt("number_of_right_items");
            boolean isRight = false;
            if (checkBigger.equalsIgnoreCase("yes")){
                if (left && leftItems > rightItems)
                    isRight = true;
                else if (!left && rightItems > leftItems)
                    isRight = true;
            }
            else{
                if (left && leftItems < rightItems)
                    isRight = true;
                else if (!left && rightItems < leftItems)
                    isRight = true;
            }
            if (isRight){
                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                finish();
            }
            else{
                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            }
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

    private void setupItems(){
        try{
            int totalItems = allData.getInt("number_of_left_items");
            int resId = allData.getInt("left_items_item");
            int numberId = allData.getInt("left_number_image");
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
            resId = allData.getInt("right_items_item");
            numberId = allData.getInt("right_number_image");
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
            finish();
        }
    }

    private void initialise(){
        try{
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupItems();
            int sound = allData.getInt("monkey_has");
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
            int sound = allData.getInt("number_of_left_items_sound");
            if (segment == 2)
                sound = allData.getInt("number_of_right_items_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (segment == 1)
                        sayAnd();
                    else
                        sayWhich();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }
    
    private void sayAnd() {
        try {
            segment = 2;
            int sound = allData.getInt("and_sound");
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
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void sayWhich() {
        try {
            segment = 2;
            int sound = allData.getInt("more_of_question");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayTouch();
                }
            });
            mp.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTouch() {
        try {
            segment = 2;
            int sound = allData.getInt("touch_the_number");
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
        } catch (Exception ex) {
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

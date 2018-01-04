package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;

public class SoundDrillOneActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_one) RelativeLayout rootView;

    private JSONObject drillData;
    private ImageView letter;
    private JSONArray objects;
    private int currentObject;
    private int letterType;

    private int mStage;

    private final Context THIS = this;

    private SoundDrill01ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_one);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill01ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        letter = (ImageView)findViewById(R.id.item1);
        // letter.setBackgroundColor(Color.argb(100, 255, 0, 0));
        RelativeLayout.LayoutParams letterLP = (RelativeLayout.LayoutParams) letter.getLayoutParams();
        letterLP.removeRule(RelativeLayout.CENTER_HORIZONTAL);
        letterLP.leftMargin = 765;
        letter.setLayoutParams(letterLP);

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        //this letter is a small

        pauseScreen = ez.frameFull();
        pauseScreen.setClickable(true);
        pauseScreen.setFocusable(true);
        pauseScreen.setBackgroundColor(Color.argb(100, 255, 0, 0));
        ez.gray(pauseScreen);
        rootView.addView(pauseScreen);

        playIntro();
    }

    public void stage1() {
        // This is the letter
        // "M"
        // It makes the sound "mmm"
        // Now you try
    }

    public void stage2() {
        // "M" mushroom

    }

    //
    // This method reads the JSON that is passed into the activity
    //
    private void initialiseData(String data){
        try {
            drillData = new JSONObject(data);
            int item = drillData.getInt("letter");
            letterType = drillData.getInt("letter_type");
            letter.setImageResource(item);
            objects = drillData.getJSONArray("objects");
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    //
    // Play the introduction sound.  This is the letter
    //
    private void playIntro(){
        String sound;
        try {
            sound = drillData.getString("intro");
            playSound(sound, this::playNormalLetterSound);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playNormalLetterSound(){
        String sound;
        try {
            if (letterType == 1) {
                sound = drillData.getString("letter_sound");
            } else {
                sound = drillData.getString("letter_phonic_sound");
            }
            playSound(sound, this::playPhonicSoundIntro);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playPhonicSoundIntro(){
        String sound;
        try {
            if (letterType == 1) {
                sound = drillData.getString("it_makes_sound");
            } else {
                sound = "m_phrase26";
            }
            playSound(sound, this::playletterSound);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playletterSound(){
        String sound;
        try {
            sound = drillData.getString("letter_phonic_sound");
            playSound(sound, this::nowYouTry);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void nowYouTry(){
        String sound;
        try {
            sound = drillData.getString("now_you_try");
            playSound(sound, this::intiatePrescence);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void intiatePrescence(){
        currentObject = 0;
        handler.delayed(this::soundAndObject,1000);

    }

    private void soundAndObject(){
        String sound;
        try {
            int image = objects.getJSONObject(currentObject).getInt("object");
            letter.setImageResource(image);
            if (letterType == 1) {
                // sound = drillData.getString("letter_sound");
                sound = drillData.getString("letter_phonic_sound");
            } else {
                sound = drillData.getString("letter_phonic_sound");
            }
            playSound(sound, this::playObject);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playObject(){
        String sound;
        try {
            sound = objects.getJSONObject(currentObject).getString("object_sound");
            playSound(sound, () -> {
                currentObject ++;
                if (currentObject < 3){
                    handler.delayed(this::soundAndObject,1000);
                } else {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
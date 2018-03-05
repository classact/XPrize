package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.utils.FetchResource;

public class SoundDrillOneActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_one) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;
    @BindView(R.id.letter) ImageView letter;
    @BindView(R.id.image) ImageView image;

    private int currentObject;

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

        initialize();
        //this letter is a small

        playIntro();
    }

    //
    // This method reads the JSON that is passed into the activity
    //
    private void initialize(){
        try {
            ViewGroup.MarginLayoutParams letterLayoutParams = (ViewGroup.MarginLayoutParams) letter.getLayoutParams();
            letterLayoutParams.height = (int) (400f * getResources().getDisplayMetrics().density);
            letterLayoutParams.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
            letter.setLayoutParams(letterLayoutParams);

            pauseScreen = ez.frameFull();
            pauseScreen.setClickable(true);
            pauseScreen.setFocusable(true);
            ez.gray(pauseScreen);
            rootView.addView(pauseScreen);

            int letterImageId = FetchResource.imageId(context, vm.getLetter().getLetterPictureLowerCaseBlackURI());
            letter.setImageResource(letterImageId);

            currentObject = 0;
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
            sound = (vm.isLetter()) ? "drill1drillsound1" : "this_is_the_sound";
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
            if (vm.isLetter()) {
                sound = vm.getLetter().getLetterSoundURI();
            } else {
                sound = vm.getLetter().getLetterSoundURI();
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
            sound = (vm.isLetter()) ? "drill1drillsound2" : "m_phrase26";
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
            sound = vm.getLetter().getPhonicSoundURI();
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
            sound = "drill1drillsound3";
            playSound(sound, () -> handler.delayed(this::soundAndObject,1000));
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void soundAndObject(){
        String sound;
        try {
            int image = FetchResource.imageId(context, vm.getWord(currentObject).getImagePictureURI());
            loadImage(letter, image);
            sound = vm.getLetter().getPhonicSoundURI();
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
            sound = vm.getWord(currentObject).getWordSoundURI();
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
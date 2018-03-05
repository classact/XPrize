package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.database.model.Letter;
import classact.com.clever_little_monkey.utils.FetchResource;

public class SoundDrillThreeActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_three) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;
    @BindView(R.id.target_letter) ImageView targetLetter;
    @BindView(R.id.left_letter) ImageView leftLetter;
    @BindView(R.id.right_letter) ImageView rightLetter;

    private Letter correctLetter;

    private int currentSet;
    private int correctItem;
    private String currentSound;
    private String currentPhonicSound;
    private boolean itemsEnabled;
    private Random rnd;

    private SoundDrill03ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_three);
        ButterKnife.bind(this);

        preloadImage(R.drawable.background_choosetheletter);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill03ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        leftLetter.setOnClickListener((v) -> clickedItem(0));
        rightLetter.setOnClickListener((v) -> clickedItem(1));

        initialiseData();
        showSet();
    }

    private void initialiseData(){
        rnd = new Random();
        itemsEnabled = false;
        currentSet = 0;
    }

    public void showSet(){
        background.setImageResource(0);
        targetLetter.setVisibility(View.VISIBLE);
        ez.hide(leftLetter, rightLetter);

        try{
            correctLetter = vm.getCorrectLetter(currentSet);
            Letter wrongLetter = vm.getWrongLetter(currentSet);

            String image = correctLetter.getLetterPictureLowerCaseBlackURI();
            loadImage(targetLetter, FetchResource.imageId(context, image));

            currentPhonicSound = correctLetter.getPhonicSoundURI();
            currentSound = (correctLetter.getIsLetter() == 1) ? correctLetter.getLetterSoundURI() : currentPhonicSound;

            // Get a random value between 0 and 1
            correctItem = rnd.nextInt(2);

            // Assign correct | wrong letter
            if (correctItem == 0) {

                loadImage(leftLetter, FetchResource.imageId(context, correctLetter.getLetterPictureLowerCaseBlackURI()));
                loadImage(rightLetter, FetchResource.imageId(context, wrongLetter.getLetterPictureLowerCaseBlackURI()));

            } else if (correctItem == 1) {

                // item1 is item2JSONObject
                loadImage(leftLetter, FetchResource.imageId(context, wrongLetter.getLetterPictureLowerCaseBlackURI()));
                loadImage(rightLetter, FetchResource.imageId(context, correctLetter.getLetterPictureLowerCaseBlackURI()));
            }

            thisIsTheLetter();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void thisIsTheLetter() {
        String sound = (correctLetter.getIsLetter() == 1) ?
                "drill3drillsound1" :
                "this_is_the_sound";
        playSound(sound, this::letterSound);
    }

    public void letterSound(){
        playSound(currentSound, this::itMakesTheSound);
    }

    private void itMakesTheSound(){
        String sound = (correctLetter.getIsLetter() == 1) ?
                "drill3drillsound2" :
                "m_phrase26";
        playSound(sound, this::phonicSound);
    }

    private void phonicSound(){
        playSound(currentPhonicSound, this::touch);
    }

    public void touch(){
        try {

            loadImage(background, R.drawable.background_choosetheletter, new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    ez.hide(targetLetter);
                    ez.show(leftLetter, rightLetter);
                    return false;
                }
            });

            String sound = "drill3drillsound3";
            playSound(sound, this::playSoundAgain);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playSoundAgain(){
        try {
            playSound(currentPhonicSound, () -> itemsEnabled = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item){
        if (itemsEnabled) {
            if (item == correctItem) {
                // setItemsEnabled(false);

                ImageView iv = null;
                if (item == 0) {
                    iv = leftLetter;
                } else if (item == 1) {
                    iv = rightLetter;
                }
                starWorks.play(this, iv); // Globals.playStarWorks(context, iv);

                itemsEnabled = false;
                playSound(FetchResource.positiveAffirmation(context), () -> {
                    if (currentSet < vm.getLetterCount() - 1) {
                        currentSet++;
                        // Next drill
                        handler.delayed(this::showSet, 350);
                    } else {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
            } else {
                playSound(FetchResource.negativeAffirmation(context), null);
            }
        }
    }
}
package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.database.model.Word;
import classact.com.clever_little_monkey.utils.FetchResource;
import classact.com.clever_little_monkey.utils.ResourceSelector;

public class SoundDrillFiveActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_five) ConstraintLayout rootView;

    @BindView(R.id.background) ImageView background;
    @BindView(R.id.target_image) ImageView targetImage;
    @BindView(R.id.image_NW) ImageView image1;
    @BindView(R.id.image_NE) ImageView image2;
    @BindView(R.id.image_SW) ImageView image3;
    @BindView(R.id.image_SE) ImageView image4;

    private int currentSet;

    private boolean itemsEnabled;

    private ImageView[] items;

    private SoundDrill05ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_five);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill05ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        initialiseData();
        showSet();
    }

    private void initialiseData(){
        try {
            itemsEnabled = false;

            image1.setOnClickListener((v) -> clickedItem(0));
            image2.setOnClickListener((v) -> clickedItem(1));
            image3.setOnClickListener((v) -> clickedItem(2));
            image4.setOnClickListener((v) -> clickedItem(3));

            items = new ImageView[4];
            items[0] = image1;
            items[1] = image2;
            items[2] = image3;
            items[3] = image4;

            toggleItemsVisibility(false);

            currentSet = 0;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showSet(){
        toggleItemsVisibility(false);
        try{
            Word targetWord = vm.getGivenWord(currentSet);
            Word[] otherWords = vm.getWords(currentSet);

            targetImage.setImageResource(FetchResource.imageId(context, targetWord.getImagePictureURI()));
            targetImage.setVisibility(View.VISIBLE);

            for (int i = 0; i < otherWords.length; i++) {
                Word otherWord = otherWords[i];
                loadImage(items[i], FetchResource.imageId(context, otherWord.getImagePictureURI()));
            }

            playBahatiHasA();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playBahatiHasA(){
        try {
            String sound = "drill5drillsound1";
            playSound(sound, this::playCurrentItemSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playCurrentItemSound(){
        try {
            playSound(vm.getGivenWord(currentSet).getWordSoundURI(), () ->
                    handler.delayed(this::playSheNeedsSomethingElse, 500));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playSheNeedsSomethingElse(){
        try {
            toggleItemsVisibility(true);
            String sound = "drill5drillsound2";
            playSound(sound, this::playCurrentSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playCurrentSound(){
        try {
            toggleItemsVisibility(true);
            String soundPath = FetchResource.sound(getApplicationContext(),
                    vm.getLetter().getPhonicSoundURI());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp) -> {
                mediaPlayer.start();
                    handler.delayed(() -> itemsEnabled = true, mp.getDuration() - 100);
                });
            mediaPlayer.setOnCompletionListener((mp) -> mediaPlayer.stop());
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clickedItem(int index) {
        if (itemsEnabled) {
            try {
                // Disable items here
                // Otherwise can continuously carry on tapping
                if (vm.isCorrect(currentSet, index)) {
                    itemsEnabled = false;

                    ImageView iv = getItem(index);
                    if (iv != null) {
                        starWorks.play(this, iv);
                    }
                }

                String sound = vm.getWord(currentSet, index).getWordSoundURI();
                playSound(sound, () -> checkProgress(index));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkProgress(int index){
        if (vm.isCorrect(currentSet, index)){
            playPositiveSound(ResourceSelector.getPositiveAffirmationSound(this), index);
        }
        else{
            playSound(ResourceSelector.getNegativeAffirmationSound(this), null);
        }
    }

    private void playPositiveSound(int soundId, int index){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, myUri);
            mediaPlayer.setOnPreparedListener((mp) -> {
                ImageView iv = items[index];
                int waitDuration = Math.min(mp.getDuration(), 200);
                fadeIncorrect(iv, waitDuration);
                mp.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mp.stop();
                handler.delayed(this::repeatCorrect, 500);
            });
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void repeatCorrect() {

        try {
            final String givenWord = vm.getGivenWord(currentSet).getWordSoundURI();
            final String matchingWordSound = vm.getMatchingWord(currentSet).getWordSoundURI();

            // Play current
            playSound(givenWord, () ->
                handler.delayed(() ->
                        playSound(matchingWordSound, () ->
                                handler.delayed(this::determineNextDrillItem,
                                    450)),
                    400)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void determineNextDrillItem() {
        try {
            currentSet++;
            if (currentSet < vm.getSetCount()) {
                handler.delayed(this::showSet, 1000);
            } else {
                // End drill
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ImageView getItem(int index) {
        ImageView iv = null;
        for (int i = 0; i < items.length; i++) {
            if (i == index) {
                iv = items[i];
                break;
            }
        }
        return iv;
    }

    public void fadeIncorrect(ImageView ivCorrect, long fadeDuration) {
        for (ImageView iv : items) {
            if (iv != ivCorrect) {
                iv.animate()
                        .alpha(0f)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(fadeDuration)
                        .start();
            }
        }
    }

    public void toggleItemsVisibility(boolean show){
        if (show){
            for (ImageView iv : items) {
                iv.setVisibility(View.VISIBLE);
                iv.setAlpha(1f);
            }
        }
        else{
            for (ImageView iv : items) {
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }
}

package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.utils.Fetch;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.WordLetterLayout;

public class SoundDrillSevenActivity extends DrillActivity {
    //private SegmetedWritingView segmentWritingView;

    @BindView(R.id.activity_sound_drill_seven) ConstraintLayout rootView;

    @BindView(R.id.writing_container) LinearLayout writingContainer;

    @BindView(R.id.letter_1) ImageView letter1;
    @BindView(R.id.letter_2) ImageView letter2;
    @BindView(R.id.letter_3) ImageView letter3;
    @BindView(R.id.letter_4) ImageView letter4;
    @BindView(R.id.letter_5) ImageView letter5;
    @BindView(R.id.letter_6) ImageView letter6;
    @BindView(R.id.letter_7) ImageView letter7;

    @BindView(R.id.objects_container) LinearLayout objectsContainer;

    @BindView(R.id.item1) ImageView item1;
    @BindView(R.id.item2) ImageView item2;
    @BindView(R.id.item3) ImageView item3;

    @BindView(R.id.letters_g_h) Guideline ghLetters;
    @BindView(R.id.letters_g_v) Guideline gvLetters;
    @BindView(R.id.images_g_v) Guideline gvImages;

    private ImageView[] items;
    private TextView letters;
    private Word correctWord;
    private boolean itemsEnabled;
    private boolean roundEnd;

    private int currentSet;

    private SoundDrill07ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_seven);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill07ViewModel.class)
                .register(getLifecycle())
                .prepare(context);
        
        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        initialiseData();
        showTripple();
    }

    private void initialiseData() {
        currentSet = 0;
        itemsEnabled = false;
        roundEnd = false;

        items = new ImageView[3];
        items[0] = item1;
        items[1] = item2;
        items[2] = item3;

        for (ImageView item : items) {
            item.setImageResource(0);
        }

        letters = new TextView(context);
        letters.setTextSize(150f);
        letters.setTextColor(Color.BLACK);
        letters.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

        ConstraintLayout.LayoutParams lettersLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        lettersLayoutParams.topToTop = ghLetters.getId();
        lettersLayoutParams.leftToRight = gvLetters.getId();
        lettersLayoutParams.bottomToBottom = ghLetters.getId();
        letters.setLayoutParams(lettersLayoutParams);

        rootView.addView(letters);
    }

    public void showTripple(){
        try {

            itemsEnabled = false;
            roundEnd = false;

            letters.setText("");
            ez.guide.setPercentage(gvLetters, 0.4f);

            for (int i = 0; i < vm.getSetWordCount(currentSet); i++) {
                ImageView item = items[i];
                Word word = vm.getWord(currentSet, i);
                int imageId = FetchResource.imageId(context, word.getImagePictureURI());
                // loadImage(item, imageId);
                item.setImageResource(imageId);
                item.setAlpha(1f);
                int wordIndex = i;
                item.setOnClickListener((v) -> clickedItem(wordIndex));
            }

            correctWord = vm.getCorrectWord(currentSet);

            // Play drill instructions
            playListenToWordAndTouch();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playListenToWordAndTouch() {
        try {
            String sound = "drill7drillsound1";
            playSound(sound, this::playSayWordSlowly);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playSayWordSlowly() {
        String sound;
        try{
            sound = correctWord.getWordSlowSoundURI();
            playSound(sound, () -> {
                if (!roundEnd) {
                    itemsEnabled = true;
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item) {
        if (itemsEnabled) {
            try {
                ImageView iv = items[item];
                Word word = vm.getWord(currentSet, item);

                if (vm.isCorrect(currentSet, item)) {
                    roundEnd = true;
                    itemsEnabled = false;
                    if (iv != null) {
                        starWorks.play(this, iv);
                    }
                    playSound(word.getWordSoundURI(), () -> playAffirmationSound(item));
                } else {
                    playSound(word.getWordSoundURI(), () -> playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()), null));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fadeIncorrect(ImageView ivCorrect, long fadeDuration) {
        for (int i = 0; i < items.length; i++) {
            ImageView iv = items[i];
            if (iv != ivCorrect) {
                iv.animate()
                        .alpha(0f)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(fadeDuration)
                        .start();
            }
        }
    }

    private void playAffirmationSound(final int item){
        try {
            int soundId = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, myUri);
            mediaPlayer.setOnPreparedListener((mp) -> {
                ImageView iv = items[item];
                int waitDuration = Math.min(mp.getDuration(), 200);
                fadeIncorrect(iv, waitDuration);
                mp.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mp.stop();
                handler.delayed(() -> showLettersAndPlayAndLetterSounds(0), 400);
            });
            mediaPlayer.prepare();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void showLettersAndPlayAndLetterSounds(final int i) {
        try{
            // base case
            if (i == correctWord.getWordName().length()) {
                handler.delayed(this::playFullWord, 700);
            } else {
                Letter letter = vm.getCorrectWordLetters(currentSet).get(i);

                String lettersText = letters.getText() + ((i == 0) ? "" : " ") + letter.getLetterName();
                letters.setText(lettersText);
                String sound = letter.getPhonicSoundURI();
                playSound(sound, () -> handler.delayed(() -> showLettersAndPlayAndLetterSounds(i + 1), 400));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playFullWord() {
        String sound;
        try{

            Word correctWord = vm.getCorrectWord(currentSet);
            sound = correctWord.getWordSoundURI();
            letters.setText(correctWord.getWordName());

            playSound(sound, () -> {
                currentSet++;
                if (currentSet < vm.getSetCount()) {
                    handler.delayed(this::showTripple, 1300);
                } else {
                    handler.delayed(this::theEnd, 1300);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
    }

    private void theEnd() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}


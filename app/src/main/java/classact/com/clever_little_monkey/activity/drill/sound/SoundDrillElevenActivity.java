package classact.com.clever_little_monkey.activity.drill.sound;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.activity.DrillActivity;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.utils.ResourceSelector;
import classact.com.clever_little_monkey.utils.TextShrinker;

public class SoundDrillElevenActivity extends DrillActivity {

    @BindView(R.id.button_word1) ImageView wordImage1;
    @BindView(R.id.button_word2)  ImageView wordImage2;
    @BindView(R.id.button_word3)  ImageView wordImage3;
    @BindView(R.id.button_word4)  ImageView wordImage4;
    @BindView(R.id.button_word5)  ImageView wordImage5;
    @BindView(R.id.button_word6)  ImageView wordImage6;
    @BindView(R.id.button_word7)  ImageView wordImage7;
    @BindView(R.id.button_word8)  ImageView wordImage8;
    @BindView(R.id.button_word9)  ImageView wordImage9;
    @BindView(R.id.button_word10)  ImageView wordImage10;

    private JSONArray words;
    private int correctSets;
    private int[] assignments;
    private int currentlyOpen;
    private int[] openPair;
    private boolean[] cardState;
    private int startPair;
    private JSONObject allData;

    private boolean gameStarted;

    private SoundDrill11ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_eleven);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill11ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        wordImage1.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage2.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage3.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage4.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage5.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage6.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage7.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage8.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage9.setBackgroundResource(R.drawable.cardsinglesml);
        wordImage10.setBackgroundResource(R.drawable.cardsinglesml);

        gameStarted = false;

        initialiseCards();

        enableCards(false);

        String drillData = getIntent().getExtras().getString("data");

        initialiseData(drillData);

        try {
            String sound = allData.getString("monkey_wants_two");

            AlphaAnimation animation1 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation2 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation3 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation4 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation5 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation6 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation7 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation8 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation9 = new AlphaAnimation(0f, 1.0f);
            AlphaAnimation animation10 = new AlphaAnimation(0f, 1.0f);

            animation1.setDuration(1100);
            animation2.setDuration(1100);
            animation3.setDuration(1100);
            animation4.setDuration(1100);
            animation5.setDuration(1100);
            animation6.setDuration(1100);
            animation7.setDuration(1100);
            animation8.setDuration(1100);
            animation9.setDuration(1100);
            animation10.setDuration(1100);

            animation1.setStartOffset(100);
            animation2.setStartOffset(200);
            animation3.setStartOffset(300);
            animation4.setStartOffset(400);
            animation5.setStartOffset(500);
            animation6.setStartOffset(600);
            animation7.setStartOffset(700);
            animation8.setStartOffset(800);
            animation9.setStartOffset(900);
            animation10.setStartOffset(1000);

            animation1.setFillAfter(true);
            animation2.setFillAfter(true);
            animation3.setFillAfter(true);
            animation4.setFillAfter(true);
            animation5.setFillAfter(true);
            animation6.setFillAfter(true);
            animation7.setFillAfter(true);
            animation8.setFillAfter(true);
            animation9.setFillAfter(true);
            animation10.setFillAfter(true);

            wordImage1.startAnimation(animation1);
            wordImage2.startAnimation(animation2);
            wordImage3.startAnimation(animation3);
            wordImage4.startAnimation(animation4);
            wordImage5.startAnimation(animation5);
            wordImage6.startAnimation(animation6);
            wordImage7.startAnimation(animation7);
            wordImage8.startAnimation(animation8);
            wordImage9.startAnimation(animation9);
            wordImage10.startAnimation(animation10);

            handler.delayed(() -> enableCards(true), 500);

            playSound(sound, this::completeIntro);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void completeIntro(){
        try {
            String sound = allData.getString("can_you_match");
            playSound(sound, () -> gameStarted = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initialiseCards(){
        wordImage1.setImageResource(0);
        wordImage1.setOnClickListener((v) -> turnCard(1));
        wordImage2.setImageResource(0);
        wordImage2.setOnClickListener((v) -> turnCard(2));
        wordImage3.setImageResource(0);
        wordImage3.setOnClickListener((v) -> turnCard(3));
        wordImage4.setImageResource(0);
        wordImage4.setOnClickListener((v) -> turnCard(4));
        wordImage5.setImageResource(0);
        wordImage5.setOnClickListener((v) -> turnCard(5));
        wordImage6.setImageResource(0);
        wordImage6.setOnClickListener((v) -> turnCard(6));
        wordImage7.setImageResource(0);
        wordImage7.setOnClickListener((v) -> turnCard(7));
        wordImage8.setImageResource(0);
        wordImage8.setOnClickListener((v) -> turnCard(8));
        wordImage9.setImageResource(0);
        wordImage9.setOnClickListener((v) -> turnCard(9));
        wordImage10.setImageResource(0);
        wordImage10.setOnClickListener((v) -> turnCard(10));
    }

    public void enableCards(boolean enable) {
        wordImage1.setEnabled(enable);
        wordImage2.setEnabled(enable);
        wordImage3.setEnabled(enable);
        wordImage4.setEnabled(enable);
        wordImage5.setEnabled(enable);
        wordImage6.setEnabled(enable);
        wordImage7.setEnabled(enable);
        wordImage8.setEnabled(enable);
        wordImage9.setEnabled(enable);
        wordImage10.setEnabled(enable);
    }

    private void initialiseData(String drillData){
        try{
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            currentlyOpen = 0;
            assignments = new int[10];
            Arrays.fill(assignments,-1);
            Random rand = new Random();
            for (int l = 1;  l < 3; l++){
                for (int i = 0; i < 5; i++) {
                    int word = rand.nextInt(10);
                    if (assignments[word] == -1) {
                        assignments[word] = i;
                    } else {
                        int j = 9;
                        boolean assigned = false;
                        while (!assigned  && j > - 1) {
                            if (assignments[j] == -1) {
                                assignments[j] = i;
                                assigned = true;
                            }
                            j--;
                        }
                    }
                }
            }
            correctSets = 0;
            startPair = 0;
            openPair = new int[2];
            Arrays.fill(openPair,0);
            cardState = new boolean[10];
            Arrays.fill(cardState,false);
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private ImageView getCard(int card){
        ImageView ImageView = null;
        switch (card){
            case 1:
                ImageView = wordImage1;
                break;
            case 2:
                ImageView = wordImage2;
                break;
            case 3:
                ImageView = wordImage3;
                break;
            case 4:
                ImageView = wordImage4;
                break;
            case 5:
                ImageView = wordImage5;
                break;
            case 6:
                ImageView = wordImage6;
                break;
            case 7:
                ImageView = wordImage7;
                break;
            case 8:
                ImageView = wordImage8;
                break;
            case 9:
                ImageView = wordImage9;
                break;
            case 10:
                ImageView = wordImage10;
                break;
        }
        return ImageView;
    }

    private void turnCard(int card){
        if (gameStarted) {
            try {
                if (!cardState[card - 1]) {
                    ImageView ImageView = getCard(card);
                    processCard(card, ImageView);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void processCard(int index, ImageView card ){
        try {
            if (startPair < 2) {
                cardState[index - 1] = true;
                openPair[startPair] = index;
                startPair++;
                String sound = words.getJSONObject(assignments[index - 1]).getString("sound");
                int image = words.getJSONObject(assignments[index - 1]).getInt("image");
                card.setBackgroundResource(R.drawable.cardsinglesmlback_empty);
                // loadImage(card, image);

                int cardWidth = 180;
                float percentage = 0.9f;
                TextShrinker.shrink(card, cardWidth, percentage, image, getResources());

                playThisSound(sound);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void playThisSound(String sound){
        try {
            playSound(sound, () -> {
                if (startPair == 2) {
                    handler.delayed(this::playStarWorksIfCorrect, 100);
                }
            }, 0, () -> {
                if (startPair == 2) {
                    handler.delayed(isCorrectPair, 200);
                }
            }, 0);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundAndEnd(int soundId){
        try {
            playSound(soundId, () -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void resetCard(ImageView card){
        card.setBackgroundResource(R.drawable.cardsinglesml);
        card.setImageResource(0);
    }

    private void hideCard(final ImageView card){
        card.setEnabled(false);
        card.animate()
                .alpha(0f)
                .setDuration(125L)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card.setImageResource(0);
                        card.setBackgroundResource(0);
                        card.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void reward(){
        switch (correctSets) {
            case 1:
                //reward1.setImageResource(R.drawable.rewardball1colour);
                break;
            case 2:
                //reward2.setImageResource(R.drawable.rewardball1colour);
                break;
            case 3:
                //reward3.setImageResource(R.drawable.rewardball1colour);
                break;
            case 4:
                //reward4.setImageResource(R.drawable.rewardball1colour);
                break;
            case 5:
                //reward5.setImageResource(R.drawable.rewardball1colour);
                break;
        }
    }

    public void playStarWorksIfCorrect() {
        if (assignments[openPair[0] - 1] == assignments[openPair[1] - 1]){
            ImageView card1 = getCard(openPair[0]);
            ImageView card2 = getCard(openPair[1]);
            Globals.playStarWorks(this, card1);
            Globals.playStarWorks(this, card2);
        }
    }

    Runnable isCorrectPair = new Runnable() {
        @Override
        public void run() {
            if (assignments[openPair[0] - 1] == assignments[openPair[1] - 1]){
                ImageView card1 = getCard(openPair[0]);
                ImageView card2 = getCard(openPair[1]);
                hideCard(card1);;
                hideCard(card2);
                correctSets ++;
                reward();
                if (correctSets < 5)
                    playSound(ResourceSelector.getPositiveAffirmationSound(context), null);
                else
                    playSoundAndEnd(ResourceSelector.getPositiveAffirmationSound(context));
            }
            else{
                ImageView card = getCard(openPair[0]);
                resetCard(card);
                card = getCard(openPair[1]);
                resetCard(card);
                cardState[openPair[0] - 1] = false;
                cardState[openPair[1] - 1] = false;
                playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
            }
            Arrays.fill(openPair,0);
            startPair = 0;
            handler.removeCallbacks(this);
        }
    };
}

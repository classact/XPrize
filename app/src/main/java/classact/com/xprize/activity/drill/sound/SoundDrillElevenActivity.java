package classact.com.xprize.activity.drill.sound;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.ResourceSelector;
import classact.com.xprize.utils.TextShrinker;

public class SoundDrillElevenActivity extends DrillActivity {

    @BindView(R.id.button_word1) ImageButton ImageButtonWord1;
    @BindView(R.id.button_word2)  ImageButton ImageButtonWord2;
    @BindView(R.id.button_word3)  ImageButton ImageButtonWord3;
    @BindView(R.id.button_word4)  ImageButton ImageButtonWord4;
    @BindView(R.id.button_word5)  ImageButton ImageButtonWord5;
    @BindView(R.id.button_word6)  ImageButton ImageButtonWord6;
    @BindView(R.id.button_word7)  ImageButton ImageButtonWord7;
    @BindView(R.id.button_word8)  ImageButton ImageButtonWord8;
    @BindView(R.id.button_word9)  ImageButton ImageButtonWord9;
    @BindView(R.id.button_word10)  ImageButton ImageButtonWord10;

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

        /*
        ImageButtonWord1.setAlpha(0f);
        ImageButtonWord2.setAlpha(0f);
        ImageButtonWord3.setAlpha(0f);
        ImageButtonWord4.setAlpha(0f);
        ImageButtonWord5.setAlpha(0f);
        ImageButtonWord6.setAlpha(0f);
        ImageButtonWord7.setAlpha(0f);
        ImageButtonWord8.setAlpha(0f);
        ImageButtonWord9.setAlpha(0f);
        ImageButtonWord10.setAlpha(0f);
        */

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

            ImageButtonWord1.startAnimation(animation1);
            ImageButtonWord2.startAnimation(animation2);
            ImageButtonWord3.startAnimation(animation3);
            ImageButtonWord4.startAnimation(animation4);
            ImageButtonWord5.startAnimation(animation5);
            ImageButtonWord6.startAnimation(animation6);
            ImageButtonWord7.startAnimation(animation7);
            ImageButtonWord8.startAnimation(animation8);
            ImageButtonWord9.startAnimation(animation9);
            ImageButtonWord10.startAnimation(animation10);

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
        ImageButtonWord1.setImageResource(0);
        ImageButtonWord1.setOnClickListener((v) -> turnCard(1));
        ImageButtonWord2.setImageResource(0);
        ImageButtonWord2.setOnClickListener((v) -> turnCard(2));
        ImageButtonWord3.setImageResource(0);
        ImageButtonWord3.setOnClickListener((v) -> turnCard(3));
        ImageButtonWord4.setImageResource(0);
        ImageButtonWord4.setOnClickListener((v) -> turnCard(4));
        ImageButtonWord5.setImageResource(0);
        ImageButtonWord5.setOnClickListener((v) -> turnCard(5));
        ImageButtonWord6.setImageResource(0);
        ImageButtonWord6.setOnClickListener((v) -> turnCard(6));
        ImageButtonWord7.setImageResource(0);
        ImageButtonWord7.setOnClickListener((v) -> turnCard(7));
        ImageButtonWord8.setImageResource(0);
        ImageButtonWord8.setOnClickListener((v) -> turnCard(8));
        ImageButtonWord9.setImageResource(0);
        ImageButtonWord9.setOnClickListener((v) -> turnCard(9));
        ImageButtonWord10.setImageResource(0);
        ImageButtonWord10.setOnClickListener((v) -> turnCard(10));
    }

    public void enableCards(boolean enable) {
        ImageButtonWord1.setEnabled(enable);
        ImageButtonWord2.setEnabled(enable);
        ImageButtonWord3.setEnabled(enable);
        ImageButtonWord4.setEnabled(enable);
        ImageButtonWord5.setEnabled(enable);
        ImageButtonWord6.setEnabled(enable);
        ImageButtonWord7.setEnabled(enable);
        ImageButtonWord8.setEnabled(enable);
        ImageButtonWord9.setEnabled(enable);
        ImageButtonWord10.setEnabled(enable);
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

    private ImageButton getCard(int card){
        ImageButton ImageButton = null;
        switch (card){
            case 1:
                ImageButton = ImageButtonWord1;
                break;
            case 2:
                ImageButton = ImageButtonWord2;
                break;
            case 3:
                ImageButton = ImageButtonWord3;
                break;
            case 4:
                ImageButton = ImageButtonWord4;
                break;
            case 5:
                ImageButton = ImageButtonWord5;
                break;
            case 6:
                ImageButton = ImageButtonWord6;
                break;
            case 7:
                ImageButton = ImageButtonWord7;
                break;
            case 8:
                ImageButton = ImageButtonWord8;
                break;
            case 9:
                ImageButton = ImageButtonWord9;
                break;
            case 10:
                ImageButton = ImageButtonWord10;
                break;
        }
        return ImageButton;
    }

    private void turnCard(int card){
        if (gameStarted) {
            try {
                if (!cardState[card - 1]) {
                    ImageButton ImageButton = getCard(card);
                    processCard(card, ImageButton);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void processCard(int card, ImageButton button ){
        try {
            if (startPair < 2) {
                cardState[card - 1] = true;
                openPair[startPair] = card;
                startPair++;
                String sound = words.getJSONObject(assignments[card - 1]).getString("sound");
                int image = words.getJSONObject(assignments[card - 1]).getInt("image");
                button.setBackgroundResource(R.drawable.cardsinglesmlback_empty);
                button.setImageResource(image);

                int cardWidth = 180;
                float percentage = 0.9f;
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                button = TextShrinker.shrink(button, cardWidth, percentage, getResources());

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
    private void resetCard(ImageButton card){
        card.setBackgroundResource(R.drawable.cardsinglesml);
        card.setImageResource(0);
    }

    private void hideCard(final ImageButton card){
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
            ImageButton card1 = getCard(openPair[0]);
            ImageButton card2 = getCard(openPair[1]);
            Globals.playStarWorks(this, card1);
            Globals.playStarWorks(this, card2);
        }
    }

    Runnable isCorrectPair = new Runnable() {
        @Override
        public void run() {
            if (assignments[openPair[0] - 1] == assignments[openPair[1] - 1]){
                ImageButton card1 = getCard(openPair[0]);
                ImageButton card2 = getCard(openPair[1]);
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
                ImageButton card = getCard(openPair[0]);
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

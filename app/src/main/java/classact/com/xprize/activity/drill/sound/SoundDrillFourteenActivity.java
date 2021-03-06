package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.WordLetterLayout;
import classact.com.xprize.view.WriteView;

public class SoundDrillFourteenActivity extends DrillActivity {

    @BindView(R.id.item1) ImageView item1;
    @BindView(R.id.item2) ImageView item2;
    @BindView(R.id.item3) ImageView item3;
    @BindView(R.id.item4) ImageView item4;
    @BindView(R.id.item5) ImageView item5;
    @BindView(R.id.item6) ImageView item6;
    @BindView(R.id.item7) ImageView item7;
    @BindView(R.id.item8) ImageView item8;
    @BindView(R.id.item9) ImageView item9;

    @BindView(R.id.loc1) ImageView receptable1;
    @BindView(R.id.loc2) ImageView receptable2;
    @BindView(R.id.loc3) ImageView receptable3;
    @BindView(R.id.loc4) ImageView receptable4;
    @BindView(R.id.loc5) ImageView receptable5;
    @BindView(R.id.loc6) ImageView receptable6;
    @BindView(R.id.loc7) ImageView receptable7;
    @BindView(R.id.loc8) ImageView receptable8;
    @BindView(R.id.loc9) ImageView receptable9;

    @BindView(R.id.activity_sound_drill_fourteen) RelativeLayout rootView;
    @BindView(R.id.writing_canvas_container) LinearLayout writingContainer;
    @BindView(R.id.layout1) LinearLayout displayContainer;

    private RelativeLayout letterContainer;

    private LinearLayout blanksContainer;
    private DrillFourteenWriteView writingView;
    private JSONArray words;
    private int currentWord;
    private int currentTries;
    private JSONObject allData;

    String textholder;

    private boolean mCanWrite;
    private boolean mIsWriting;
    private long mLastWrittenTime;
    private TextView mTimer;
    private int mTimerCounter;
    private boolean mTimerReset;
    private LinearLayout mItemsParent;
    private LinearLayout mReceptaclesParent;

    private final Context THIS = this;

    private final int TIMER_MAX = 5;
    private final int DRAW_WAIT_TIME = 1000;

    private final float TIMER_MID_X = 2065f;
    private final float TIMER_MID_Y = 425f;

    private SoundDrill14ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_fourteen);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill14ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        loadImage(receptable1, R.drawable.line);
        loadImage(receptable2, R.drawable.line);
        loadImage(receptable3, R.drawable.line);
        loadImage(receptable4, R.drawable.line);
        loadImage(receptable5, R.drawable.line);
        loadImage(receptable6, R.drawable.line);
        loadImage(receptable7, R.drawable.line);
        loadImage(receptable8, R.drawable.line);
        loadImage(receptable9, R.drawable.line);

        blanksContainer = (LinearLayout) rootView.getChildAt(1);

        mItemsParent = (LinearLayout) item1.getParent();
        mReceptaclesParent = (LinearLayout) receptable1.getParent();

        RelativeLayout parentLayout = (RelativeLayout) displayContainer.getParent();
        parentLayout.setBackgroundResource(R.drawable.background_drawapic3);

        // Add draw timer
        ImageView timerClock = new ImageView(THIS);
        timerClock.setImageResource(R.drawable.timer_clock_001);
        timerClock.setScaleX(0.75f);
        timerClock.setScaleY(0.75f);
        timerClock.setX(1775f);
        timerClock.setY(125f);
        rootView.addView(timerClock);

        mTimer = new TextView(getApplicationContext());
        mTimer.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()), Typeface.BOLD);
        mTimer.setPadding(16, 16, 16, 16);
        mTimerCounter = TIMER_MAX;
        mTimerReset = true;
        mLastWrittenTime = 0l;

        mTimer.setText(String.valueOf(mTimerCounter));
        mTimer.setTextSize(115.0f);
        mTimer.setAlpha(0.8f);
        mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        // timer.setBackgroundColor(Color.argb(100, 255, 0, 0));
        mTimer.setX(TIMER_MID_X);
        mTimer.setY(TIMER_MID_Y);
        rootView.addView(mTimer);

        Point textSize = Globals.TEXT_MEASURED_SIZE(mTimer, String.valueOf(mTimerCounter));
        mTimer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
        mTimer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
    }

    private void initialiseData(String drillData) {
        try {
            allData = new JSONObject(drillData);
            words = allData.getJSONArray("words");
            currentWord = 1;
            startDrill();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startDrill() {
        try {
            mCanWrite = false;
            mTimerReset = true;
            mIsWriting = false;
            mLastWrittenTime = 0;
            resetLetters();
            loadWord();
            displayContainer.setVisibility(View.INVISIBLE);
            writingContainer.removeAllViews();
            writingView = new DrillFourteenWriteView(this, 0, true);
            writingView.setThisActivity(this);
            writingView.setAlpha(0.95f);
            writingContainer.addView(writingView);

            mTimerCounter = TIMER_MAX;
            mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            Point textSize = Globals.TEXT_MEASURED_SIZE(mTimer, String.valueOf(mTimerCounter));
            mTimer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
            mTimer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

            String sound = allData.getString("write");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayWord();
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sayWord() {
        try {
            String sound =  words.getJSONObject(currentWord - 1).getString("sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    mCanWrite = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Runnable showWordRunnable = new Runnable() {
        @Override
        public void run() {
            showWord();
        }
    };

    private void resetLetters() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        for (int i = 0; i < displayContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) displayContainer.getChildAt(i);
            iv.setImageResource(0);
            LinearLayout.LayoutParams ivLayout = (LinearLayout.LayoutParams) iv.getLayoutParams();
            ivLayout.topMargin = 280;
            ivLayout.leftMargin = (int) ((float) 30 * density);
            iv.setLayoutParams(ivLayout);
            iv.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < blanksContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) blanksContainer.getChildAt(i);
            LinearLayout.LayoutParams ivLayout = (LinearLayout.LayoutParams) iv.getLayoutParams();
            ivLayout.leftMargin = (int) ((float) 30 * density);
            iv.setLayoutParams(ivLayout);
            iv.setVisibility(View.INVISIBLE);
        }
    }

    private void loadWord() {
        try {
            currentTries = 0;

            JSONArray word = words.getJSONObject(currentWord - 1).getJSONArray("letters");

            item1.setImageResource(word.getInt(0));
            item1.setVisibility(View.VISIBLE);
            receptable1.setVisibility(View.VISIBLE);

            if (word.length() > 1) {
                item2.setImageResource(word.getInt(1));
                item2.setVisibility(View.VISIBLE);
                receptable2.setVisibility(View.VISIBLE);
                System.out.println("Show R1");
            }
            else{
                item2.setVisibility(View.INVISIBLE);
                receptable2.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 2) {
                item3.setImageResource(word.getInt(2));
                item3.setVisibility(View.VISIBLE);
                receptable3.setVisibility(View.VISIBLE);
                System.out.println("Show R2");
            }
            else{
                item3.setVisibility(View.INVISIBLE);
                receptable3.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 3) {
                item4.setImageResource(word.getInt(3));
                item4.setVisibility(View.VISIBLE);
                receptable4.setVisibility(View.VISIBLE);
                System.out.println("Show R3");
            }
            else{
                item4.setVisibility(View.INVISIBLE);
                receptable4.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 4) {
                item5.setImageResource(word.getInt(4));
                item5.setVisibility(View.VISIBLE);
                receptable5.setVisibility(View.VISIBLE);
                System.out.println("Show R4");
            }
            else{
                item5.setVisibility(View.INVISIBLE);
                receptable5.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 5) {
                item6.setImageResource(word.getInt(5));
                item6.setVisibility(View.VISIBLE);
                receptable6.setVisibility(View.VISIBLE);
                System.out.println("Show R5");
            }
            else{
                item6.setVisibility(View.INVISIBLE);
                receptable6.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 6) {
                item7.setImageResource(word.getInt(6));
                item7.setVisibility(View.VISIBLE);
                receptable7.setVisibility(View.VISIBLE);
                System.out.println("Show R6");
            }
            else{
                item7.setVisibility(View.INVISIBLE);
                receptable7.setVisibility(View.INVISIBLE);
            }


            if (word.length() > 7) {
                item8.setImageResource(word.getInt(7));
                item8.setVisibility(View.VISIBLE);
                receptable8.setVisibility(View.VISIBLE);
                System.out.println("Show R7");
            }
            else{
                item8.setVisibility(View.INVISIBLE);
                receptable8.setVisibility(View.INVISIBLE);
            }

            if (word.length() > 8) {
                item9.setImageResource(word.getInt(8));
                item9.setVisibility(View.VISIBLE);
                receptable9.setVisibility(View.VISIBLE);
                System.out.println("Show R8");
            }
            else{
                item9.setVisibility(View.INVISIBLE);
                receptable9.setVisibility(View.INVISIBLE);
            }

            String wordString = words.getJSONObject(currentWord - 1).getString("word");

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;
            int screenWidth = displayMetrics.widthPixels;

            float letterWidth = 300f;
            float letterScale = letterWidth / 180f;
            int letterWidthSum = 0;
            int letterMarginSum = 0;
            ImageView firstLetter = null;

            List<ImageView> letterViews = new ArrayList<>();
            List<Integer> letterResources = new ArrayList<>();

            int letterCount = 0;
            for (int i = 0; i < displayContainer.getChildCount(); i++) {
                ImageView iv = (ImageView) displayContainer.getChildAt(i);
                letterViews.add(iv);
                if (iv.getDrawable() != null) {
                    letterResources.add(word.getInt(letterCount++));
                }
            }

            letterViews = WordLetterLayout.level(
                    THIS,
                    letterViews,
                    letterResources,
                    wordString,
                    displayMetrics,
                    letterWidth,
                    letterScale,
                    true
            );

            letterCount = 0;
            for (int i = 0; i < letterViews.size(); i++) {
                ImageView iv = letterViews.get(i);
                MarginLayoutParams ivLayout = (MarginLayoutParams) iv.getLayoutParams();
                ivLayout.leftMargin = 10;
                int width = ivLayout.width;
                int leftMargin = 10;
                Drawable d = iv.getDrawable();
                if (d != null) {
                    letterWidthSum += width;
                    if (letterCount > 0) {
                        letterMarginSum += leftMargin;
                    } else {
                        firstLetter = iv;
                    }
                    letterCount++;
                }
            }

            int lettersWidth = letterWidthSum + letterMarginSum;
            LinearLayout.LayoutParams firstLetterLayout = (LinearLayout.LayoutParams) firstLetter.getLayoutParams();
            firstLetterLayout.leftMargin = (screenWidth - lettersWidth) / 2;
            firstLetter.setLayoutParams(firstLetterLayout);

            float blankWidth = 250f;
            float blankScale = blankWidth / 180f;
            int blankWidthSum = 0;
            int blankMarginSum = 0;
            ImageView firstBlank = null;
            int blanksCount = 0;
            for (int i = 0; i < blanksContainer.getChildCount(); i++) {
                ImageView iv = (ImageView) blanksContainer.getChildAt(i);
                LinearLayout.LayoutParams ivLayout = (LinearLayout.LayoutParams) iv.getLayoutParams();
                iv.setScaleX(blankScale);
                ivLayout.width = (int) (blankWidth);
                int leftMargin = (int) ((float) ivLayout.leftMargin * blankScale * density);
                ivLayout.leftMargin = leftMargin;
                iv.setLayoutParams(ivLayout);
                if (iv.getVisibility() == View.VISIBLE) {
                    // int colorGrad = (int) ((float) 255 * ((float) (i + 1) / (float) blanksContainer.getChildCount()));
                    // iv.setBackgroundColor(Color.argb(colorGrad, 0, 255, 0));
                    blankWidthSum += (int) blankWidth;
                    if (blanksCount > 0) {
                        blankMarginSum += leftMargin;
                    } else {
                        firstBlank = iv;
                    }
                    blanksCount++;
                }
            }

            int blanksWidth = blankWidthSum + blankMarginSum;
            LinearLayout.LayoutParams firstBlankLayout = (LinearLayout.LayoutParams) firstBlank.getLayoutParams();
            firstBlankLayout.leftMargin = (screenWidth - blanksWidth) / 2;
            firstBlank.setLayoutParams(firstBlankLayout);

            RelativeLayout.LayoutParams blanksLayout = (RelativeLayout.LayoutParams) blanksContainer.getLayoutParams();
            blanksLayout.removeRule(RelativeLayout.BELOW);
            blanksLayout.topMargin = 950;
            blanksContainer.setLayoutParams(blanksLayout);

            displayContainer.setVisibility(View.VISIBLE);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showWord(){
        try {
            displayContainer.setVisibility(View.VISIBLE);
            String sound = allData.getString("this_is");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    repeatWord();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void repeatWord(){
        try {
            String sound =  words.getJSONObject(currentWord - 1).getString("sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    handler.delayed(wereYouCorrectRunnable,100);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Runnable wereYouCorrectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String sound = allData.getString("were_you_correct");
                playSound(sound, new Runnable() {
                    @Override
                    public void run() {
                        handler.delayed(continueRunnable, 2000);
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    public Runnable continueRunnable = () -> {
        currentWord++;
        if (currentWord <= words.length()) {
            startDrill();
        } else {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };


    private void reward(){
        switch (currentWord) {
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

    private class DrillFourteenWriteView extends WriteView {

        private SoundDrillFourteenActivity mThisActivity;

        private DrillFourteenWriteView(Context context, int background, boolean transparent) {
            super(context, background, transparent);
        }

        private void setThisActivity(SoundDrillFourteenActivity thisActivity) {
            mThisActivity = thisActivity;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if (mCanWrite) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
                        mThisActivity.setIsWriting(true);
                        mThisActivity.setLastWrittenTime(0);
                        System.out.println("Writing not complete!");
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mThisActivity.setIsWriting(false);
                        mThisActivity.setTimerReset(true);
                        mThisActivity.setLastWrittenTime(new Date().getTime());

                        handler.removeCallbacks(countDown);
                        handler.delayed(new Runnable() {
                            @Override
                            public void run() {
                                mTimer.setTextColor(Color.DKGRAY);
                                countDown.run();
                            }
                        }, DRAW_WAIT_TIME);

                        System.out.println("Writing complete!");
                        break;
                }
            }
            return true;
        }
    }

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            try {
                long currentTime = new Date().getTime();

                if (!(mIsWriting || mLastWrittenTime == 0l || (currentTime - mLastWrittenTime) < DRAW_WAIT_TIME )) {
                    if (mTimerReset) {
                        mTimerReset = false;
                        mTimerCounter = TIMER_MAX;
                    }

                    Point textSize = Globals.TEXT_MEASURED_SIZE(mTimer, String.valueOf(mTimerCounter));
                    mTimer.setX(TIMER_MID_X - ((float) (textSize.x) / 2));
                    mTimer.setY(TIMER_MID_Y - ((float) (textSize.y) / 2));

                    if (mTimerCounter > 0) {
                        mTimerCounter--;
                        handler.delayed(countDown, 1000);
                    } else {
                        mCanWrite = false;
                        mTimer.setTextColor(Color.parseColor("#33ccff"));
                        handler.delayed(showWordRunnable, 500);
                    }
                } else {
                    mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    public void setIsWriting(boolean isWriting) {
        mIsWriting = isWriting;
    }

    public void setTimerReset(boolean timerReset) {
        mTimerReset = timerReset;
    }

    public void setLastWrittenTime(long lastWrittenTime) {
        mLastWrittenTime = lastWrittenTime;
    }

    public boolean getIsWriting() {
        return mIsWriting;
    }

    public boolean getTimerReset() {
        return mTimerReset;
    }

    public long getLastWrittenTime() {
        return mLastWrittenTime;
    }
}

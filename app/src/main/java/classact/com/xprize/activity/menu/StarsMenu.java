package classact.com.xprize.activity.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.MenuActivity;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.Unit;

public class StarsMenu extends MenuActivity {

    @BindView(R.id.activity_stars_menu) ConstraintLayout mRootView;

    @BindView(R.id.monkey_01) ImageView monkey01;
    @BindView(R.id.monkey_02) ImageView monkey02;
    @BindView(R.id.monkey_03) ImageView monkey03;
    @BindView(R.id.monkey_04) ImageView monkey04;
    @BindView(R.id.monkey_05) ImageView monkey05;
    @BindView(R.id.monkey_06) ImageView monkey06;
    @BindView(R.id.monkey_07) ImageView monkey07;
    @BindView(R.id.monkey_08) ImageView monkey08;
    @BindView(R.id.monkey_09) ImageView monkey09;
    @BindView(R.id.monkey_10) ImageView monkey10;
    @BindView(R.id.monkey_11) ImageView monkey11;
    @BindView(R.id.monkey_12) ImageView monkey12;
    @BindView(R.id.monkey_13) ImageView monkey13;
    @BindView(R.id.monkey_14) ImageView monkey14;
    @BindView(R.id.monkey_15) ImageView monkey15;
    @BindView(R.id.monkey_16) ImageView monkey16;
    @BindView(R.id.monkey_17) ImageView monkey17;
    @BindView(R.id.monkey_18) ImageView monkey18;
    @BindView(R.id.monkey_19) ImageView monkey19;
    @BindView(R.id.monkey_20) ImageView monkey20;

    @BindView(R.id.star_01) ImageView star01;
    @BindView(R.id.star_02) ImageView star02;
    @BindView(R.id.star_03) ImageView star03;
    @BindView(R.id.star_04) ImageView star04;
    @BindView(R.id.star_05) ImageView star05;
    @BindView(R.id.star_06) ImageView star06;
    @BindView(R.id.star_07) ImageView star07;
    @BindView(R.id.star_08) ImageView star08;
    @BindView(R.id.star_09) ImageView star09;
    @BindView(R.id.star_10) ImageView star10;
    @BindView(R.id.star_11) ImageView star11;
    @BindView(R.id.star_12) ImageView star12;
    @BindView(R.id.star_13) ImageView star13;
    @BindView(R.id.star_14) ImageView star14;
    @BindView(R.id.star_15) ImageView star15;
    @BindView(R.id.star_16) ImageView star16;
    @BindView(R.id.star_17) ImageView star17;
    @BindView(R.id.star_18) ImageView star18;
    @BindView(R.id.star_19) ImageView star19;
    @BindView(R.id.star_20) ImageView star20;

    @BindView(R.id.chapter_book) ImageView mChapterBookButton;
    @BindView(R.id.chapter_text) TextView mChapterText;

    private List<ImageView> monkeys;
    private List<ImageView> stars;
    private int mMaxStars;
    private int mCurrentStarLimit;
    private int mCurrentStar;

    private SparseIntArray mStarLevelBackgrounds;

    Animation mBookAnimation;


    private boolean mStarsExist;
    private boolean mViewVisible;
    private boolean mFirstMonkeyPlaced;
    
    private Handler mHandler;
    private Intent mIntent;
    private boolean mFinishActivity;

    private boolean[] isGoldStar;
    
    @Inject DatabaseController mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_menu);
        ButterKnife.bind(this);

        setupStarGraphics();

        mHandler = new Handler();
        mIntent = getIntent();
        mFinishActivity = false;

        monkeys = new ArrayList<>();
        stars = new ArrayList<>();
        mStarLevelBackgrounds = new SparseIntArray();

        mChapterText.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterText.setTextColor(Color.argb(255, 255, 205, 0));

        monkeys.add(monkey01);
        monkeys.add(monkey02);
        monkeys.add(monkey03);
        monkeys.add(monkey04);
        monkeys.add(monkey05);
        monkeys.add(monkey06);
        monkeys.add(monkey07);
        monkeys.add(monkey08);
        monkeys.add(monkey09);
        monkeys.add(monkey10);
        monkeys.add(monkey11);
        monkeys.add(monkey12);
        monkeys.add(monkey13);
        monkeys.add(monkey14);
        monkeys.add(monkey15);
        monkeys.add(monkey16);
        monkeys.add(monkey17);
        monkeys.add(monkey18);
        monkeys.add(monkey19);
        monkeys.add(monkey20);

        stars.add(star01);
        stars.add(star02);
        stars.add(star03);
        stars.add(star04);
        stars.add(star05);
        stars.add(star06);
        stars.add(star07);
        stars.add(star08);
        stars.add(star09);
        stars.add(star10);
        stars.add(star11);
        stars.add(star12);
        stars.add(star13);
        stars.add(star14);
        stars.add(star15);
        stars.add(star16);
        stars.add(star17);
        stars.add(star18);
        stars.add(star19);
        stars.add(star20);

        LinkedHashMap<Integer, Unit> units = mDb.getUnits();
        int numberOfUnlockedChapters = 0;
        int currentChapterInProgress = 0;
        Log.d("TEST", "Size is " + ((units == null) ? "null" : units.size()));
        for (Map.Entry<Integer, Unit> unitEntry : units.entrySet()) {
            Unit unit = unitEntry.getValue();
            int unitId = unit.getUnitId();
            if (!(unitId == 0 || unitId == 21)) {
                if (unit.getUnitUnlocked() == 1) {
                    numberOfUnlockedChapters++;
                    if (unit.getUnitInProgress() == 1) {
                        currentChapterInProgress = unitId;
                    }
                }
            }
        }

        mStarsExist = true;
        mChapterBookButton.setAlpha(0f);
        loadAndLayoutImage(mChapterBookButton, R.drawable.book_closed, .9f);
        mChapterBookButton.setEnabled(false);
        mChapterText.setAlpha(0f);

        mMaxStars = 20;
        for (int i = 0; i < mMaxStars; i++) {
            ImageView monkeyButton = monkeys.get(i);
            monkeyButton.setAlpha(0f);
            loadAndLayoutImage(monkeyButton, R.drawable.star_monkey_face_button_up);
            setTouchListener(monkeyButton, R.drawable.star_monkey_face_button_up, R.drawable.star_monkey_face_button_down);
            monkeyButton.setVisibility(View.INVISIBLE);
        }

        mCurrentStarLimit = Math.min(numberOfUnlockedChapters, mMaxStars);
        mCurrentStar = Math.min(currentChapterInProgress, mCurrentStarLimit);

        String chapterText = "Chapter " + mCurrentStar;
        mChapterText.setText(chapterText);

        isGoldStar = new boolean[20];
        Arrays.fill(isGoldStar, false);
        for (int i = 0; i < mCurrentStarLimit; i++) {
            isGoldStar[i] = true;
            ImageView starButton = stars.get(i);
            final int chapter = i + 1;
            starButton.setOnClickListener((v) -> {
                    String text = "Chapter " + chapter;
                    mChapterText.setText(text);
                    placeMonkey(chapter);
            });
        }

        setupStarColours();

        mChapterBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishActivity = true;
                Intent intent = new Intent(context, SectionsMenu.class);
                intent.putExtra("selected_chapter", mCurrentStar);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mBookAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, -0.001f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.0175f);
        mBookAnimation.setDuration(900);
        mBookAnimation.setRepeatCount(Animation.INFINITE);
        mBookAnimation.setRepeatMode(Animation.REVERSE);
        mBookAnimation.setInterpolator(new OvershootInterpolator());
        mBookAnimation.setFillAfter(true);

        mViewVisible = false;
        mFirstMonkeyPlaced = false;

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mCurrentStar > 0) {
                    if (!mViewVisible) {
                        mViewVisible = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                placeMonkey(mCurrentStar);
                            }
                        }, 150);
                    }
                }
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setupStarGraphics() {

        preloadImage(
                R.drawable.star_blue_01,
                R.drawable.star_blue_02,
                R.drawable.star_blue_03,
                R.drawable.star_blue_04,
                R.drawable.star_blue_05,
                R.drawable.star_blue_06,
                R.drawable.star_blue_07,
                R.drawable.star_blue_08,
                R.drawable.star_blue_09,
                R.drawable.star_blue_10,
                R.drawable.star_blue_11,
                R.drawable.star_blue_12,
                R.drawable.star_blue_13,
                R.drawable.star_blue_14,
                R.drawable.star_blue_15,
                R.drawable.star_blue_16,
                R.drawable.star_blue_17,
                R.drawable.star_blue_18,
                R.drawable.star_blue_19,
                R.drawable.star_blue_20,
                R.drawable.star_gold_01,
                R.drawable.star_gold_02,
                R.drawable.star_gold_03,
                R.drawable.star_gold_04,
                R.drawable.star_gold_05,
                R.drawable.star_gold_06,
                R.drawable.star_gold_07,
                R.drawable.star_gold_08,
                R.drawable.star_gold_09,
                R.drawable.star_gold_10,
                R.drawable.star_gold_11,
                R.drawable.star_gold_12,
                R.drawable.star_gold_13,
                R.drawable.star_gold_14,
                R.drawable.star_gold_15,
                R.drawable.star_gold_16,
                R.drawable.star_gold_17,
                R.drawable.star_gold_18,
                R.drawable.star_gold_19,
                R.drawable.star_gold_20,
                R.drawable.book_closed,
                R.drawable.star_monkey_face_button_up,
                R.drawable.star_monkey_face_button_down
        );

        // Get drawables
        Drawable star01Drawable = getResources().getDrawable(R.drawable.star_gold_01, null);
        Drawable star02Drawable = getResources().getDrawable(R.drawable.star_gold_02, null);
        Drawable star03Drawable = getResources().getDrawable(R.drawable.star_gold_03, null);
        Drawable star04Drawable = getResources().getDrawable(R.drawable.star_gold_04, null);
        Drawable star05Drawable = getResources().getDrawable(R.drawable.star_gold_05, null);
        Drawable star06Drawable = getResources().getDrawable(R.drawable.star_gold_06, null);
        Drawable star07Drawable = getResources().getDrawable(R.drawable.star_gold_07, null);
        Drawable star08Drawable = getResources().getDrawable(R.drawable.star_gold_08, null);
        Drawable star09Drawable = getResources().getDrawable(R.drawable.star_gold_09, null);
        Drawable star10Drawable = getResources().getDrawable(R.drawable.star_gold_10, null);
        Drawable star11Drawable = getResources().getDrawable(R.drawable.star_gold_11, null);
        Drawable star12Drawable = getResources().getDrawable(R.drawable.star_gold_12, null);
        Drawable star13Drawable = getResources().getDrawable(R.drawable.star_gold_13, null);
        Drawable star14Drawable = getResources().getDrawable(R.drawable.star_gold_14, null);
        Drawable star15Drawable = getResources().getDrawable(R.drawable.star_gold_15, null);
        Drawable star16Drawable = getResources().getDrawable(R.drawable.star_gold_16, null);
        Drawable star17Drawable = getResources().getDrawable(R.drawable.star_gold_17, null);
        Drawable star18Drawable = getResources().getDrawable(R.drawable.star_gold_18, null);
        Drawable star19Drawable = getResources().getDrawable(R.drawable.star_gold_19, null);
        Drawable star20Drawable = getResources().getDrawable(R.drawable.star_gold_20, null);

        // Get layout params
        ViewGroup.MarginLayoutParams star01LayoutParams = (ViewGroup.MarginLayoutParams) star01.getLayoutParams();
        ViewGroup.MarginLayoutParams star02LayoutParams = (ViewGroup.MarginLayoutParams) star02.getLayoutParams();
        ViewGroup.MarginLayoutParams star03LayoutParams = (ViewGroup.MarginLayoutParams) star03.getLayoutParams();
        ViewGroup.MarginLayoutParams star04LayoutParams = (ViewGroup.MarginLayoutParams) star04.getLayoutParams();
        ViewGroup.MarginLayoutParams star05LayoutParams = (ViewGroup.MarginLayoutParams) star05.getLayoutParams();
        ViewGroup.MarginLayoutParams star06LayoutParams = (ViewGroup.MarginLayoutParams) star06.getLayoutParams();
        ViewGroup.MarginLayoutParams star07LayoutParams = (ViewGroup.MarginLayoutParams) star07.getLayoutParams();
        ViewGroup.MarginLayoutParams star08LayoutParams = (ViewGroup.MarginLayoutParams) star08.getLayoutParams();
        ViewGroup.MarginLayoutParams star09LayoutParams = (ViewGroup.MarginLayoutParams) star09.getLayoutParams();
        ViewGroup.MarginLayoutParams star10LayoutParams = (ViewGroup.MarginLayoutParams) star10.getLayoutParams();
        ViewGroup.MarginLayoutParams star11LayoutParams = (ViewGroup.MarginLayoutParams) star11.getLayoutParams();
        ViewGroup.MarginLayoutParams star12LayoutParams = (ViewGroup.MarginLayoutParams) star12.getLayoutParams();
        ViewGroup.MarginLayoutParams star13LayoutParams = (ViewGroup.MarginLayoutParams) star13.getLayoutParams();
        ViewGroup.MarginLayoutParams star14LayoutParams = (ViewGroup.MarginLayoutParams) star14.getLayoutParams();
        ViewGroup.MarginLayoutParams star15LayoutParams = (ViewGroup.MarginLayoutParams) star15.getLayoutParams();
        ViewGroup.MarginLayoutParams star16LayoutParams = (ViewGroup.MarginLayoutParams) star16.getLayoutParams();
        ViewGroup.MarginLayoutParams star17LayoutParams = (ViewGroup.MarginLayoutParams) star17.getLayoutParams();
        ViewGroup.MarginLayoutParams star18LayoutParams = (ViewGroup.MarginLayoutParams) star18.getLayoutParams();
        ViewGroup.MarginLayoutParams star19LayoutParams = (ViewGroup.MarginLayoutParams) star19.getLayoutParams();
        ViewGroup.MarginLayoutParams star20LayoutParams = (ViewGroup.MarginLayoutParams) star20.getLayoutParams();

        // Set widths
        star01LayoutParams.width = (int) (star01Drawable.getIntrinsicWidth() * 0.8);
        star02LayoutParams.width = (int) (star02Drawable.getIntrinsicWidth() * 0.8);
        star03LayoutParams.width = (int) (star03Drawable.getIntrinsicWidth() * 0.8);
        star04LayoutParams.width = (int) (star04Drawable.getIntrinsicWidth() * 0.8);
        star05LayoutParams.width = (int) (star05Drawable.getIntrinsicWidth() * 0.8);
        star06LayoutParams.width = (int) (star06Drawable.getIntrinsicWidth() * 0.8);
        star07LayoutParams.width = (int) (star07Drawable.getIntrinsicWidth() * 0.8);
        star08LayoutParams.width = (int) (star08Drawable.getIntrinsicWidth() * 0.8);
        star09LayoutParams.width = (int) (star09Drawable.getIntrinsicWidth() * 0.8);
        star10LayoutParams.width = (int) (star10Drawable.getIntrinsicWidth() * 0.8);
        star11LayoutParams.width = (int) (star11Drawable.getIntrinsicWidth() * 0.8);
        star12LayoutParams.width = (int) (star12Drawable.getIntrinsicWidth() * 0.8);
        star13LayoutParams.width = (int) (star13Drawable.getIntrinsicWidth() * 0.8);
        star14LayoutParams.width = (int) (star14Drawable.getIntrinsicWidth() * 0.8);
        star15LayoutParams.width = (int) (star15Drawable.getIntrinsicWidth() * 0.8);
        star16LayoutParams.width = (int) (star16Drawable.getIntrinsicWidth() * 0.8);
        star17LayoutParams.width = (int) (star17Drawable.getIntrinsicWidth() * 0.8);
        star18LayoutParams.width = (int) (star18Drawable.getIntrinsicWidth() * 0.8);
        star19LayoutParams.width = (int) (star19Drawable.getIntrinsicWidth() * 0.8);
        star20LayoutParams.width = (int) (star20Drawable.getIntrinsicWidth() * 0.8);

        // Set heights
        star01LayoutParams.height = (int) (star01Drawable.getIntrinsicHeight() * 0.8);
        star02LayoutParams.height = (int) (star02Drawable.getIntrinsicHeight() * 0.8);
        star03LayoutParams.height = (int) (star03Drawable.getIntrinsicHeight() * 0.8);
        star04LayoutParams.height = (int) (star04Drawable.getIntrinsicHeight() * 0.8);
        star05LayoutParams.height = (int) (star05Drawable.getIntrinsicHeight() * 0.8);
        star06LayoutParams.height = (int) (star06Drawable.getIntrinsicHeight() * 0.8);
        star07LayoutParams.height = (int) (star07Drawable.getIntrinsicHeight() * 0.8);
        star08LayoutParams.height = (int) (star08Drawable.getIntrinsicHeight() * 0.8);
        star09LayoutParams.height = (int) (star09Drawable.getIntrinsicHeight() * 0.8);
        star10LayoutParams.height = (int) (star10Drawable.getIntrinsicHeight() * 0.8);
        star11LayoutParams.height = (int) (star11Drawable.getIntrinsicHeight() * 0.8);
        star12LayoutParams.height = (int) (star12Drawable.getIntrinsicHeight() * 0.8);
        star13LayoutParams.height = (int) (star13Drawable.getIntrinsicHeight() * 0.8);
        star14LayoutParams.height = (int) (star14Drawable.getIntrinsicHeight() * 0.8);
        star15LayoutParams.height = (int) (star15Drawable.getIntrinsicHeight() * 0.8);
        star16LayoutParams.height = (int) (star16Drawable.getIntrinsicHeight() * 0.8);
        star17LayoutParams.height = (int) (star17Drawable.getIntrinsicHeight() * 0.8);
        star18LayoutParams.height = (int) (star18Drawable.getIntrinsicHeight() * 0.8);
        star19LayoutParams.height = (int) (star19Drawable.getIntrinsicHeight() * 0.8);
        star20LayoutParams.height = (int) (star20Drawable.getIntrinsicHeight() * 0.8);

        // Set layout params
        star01.setLayoutParams(star01LayoutParams);
        star02.setLayoutParams(star02LayoutParams);
        star03.setLayoutParams(star03LayoutParams);
        star04.setLayoutParams(star04LayoutParams);
        star05.setLayoutParams(star05LayoutParams);
        star06.setLayoutParams(star06LayoutParams);
        star07.setLayoutParams(star07LayoutParams);
        star08.setLayoutParams(star08LayoutParams);
        star09.setLayoutParams(star09LayoutParams);
        star10.setLayoutParams(star10LayoutParams);
        star11.setLayoutParams(star11LayoutParams);
        star12.setLayoutParams(star12LayoutParams);
        star13.setLayoutParams(star13LayoutParams);
        star14.setLayoutParams(star14LayoutParams);
        star15.setLayoutParams(star15LayoutParams);
        star16.setLayoutParams(star16LayoutParams);
        star17.setLayoutParams(star17LayoutParams);
        star18.setLayoutParams(star18LayoutParams);
        star19.setLayoutParams(star19LayoutParams);
        star20.setLayoutParams(star20LayoutParams);
    }

    public void setupStarColours() {
        loadFadeImage(star01, (isGoldStar[0]) ? R.drawable.star_gold_01 : R.drawable.star_blue_01, (int) (Math.random() * 1500));
        loadFadeImage(star02, (isGoldStar[1]) ? R.drawable.star_gold_02 : R.drawable.star_blue_02, (int) (Math.random() * 1500));
        loadFadeImage(star03, (isGoldStar[2]) ? R.drawable.star_gold_03 : R.drawable.star_blue_03, (int) (Math.random() * 1500));
        loadFadeImage(star04, (isGoldStar[3]) ? R.drawable.star_gold_04 : R.drawable.star_blue_04, (int) (Math.random() * 1500));
        loadFadeImage(star05, (isGoldStar[4]) ? R.drawable.star_gold_05 : R.drawable.star_blue_05, (int) (Math.random() * 1500));
        loadFadeImage(star06, (isGoldStar[5]) ? R.drawable.star_gold_06 : R.drawable.star_blue_06, (int) (Math.random() * 1500));
        loadFadeImage(star07, (isGoldStar[6]) ? R.drawable.star_gold_07 : R.drawable.star_blue_07, (int) (Math.random() * 1500));
        loadFadeImage(star08, (isGoldStar[7]) ? R.drawable.star_gold_08 : R.drawable.star_blue_08, (int) (Math.random() * 1500));
        loadFadeImage(star09, (isGoldStar[8]) ? R.drawable.star_gold_09 : R.drawable.star_blue_09, (int) (Math.random() * 1500));
        loadFadeImage(star10, (isGoldStar[9]) ? R.drawable.star_gold_10 : R.drawable.star_blue_10, (int) (Math.random() * 1500));
        loadFadeImage(star11, (isGoldStar[10]) ? R.drawable.star_gold_11 : R.drawable.star_blue_11, (int) (Math.random() * 1500));
        loadFadeImage(star12, (isGoldStar[11]) ? R.drawable.star_gold_12 : R.drawable.star_blue_12, (int) (Math.random() * 1500));
        loadFadeImage(star13, (isGoldStar[12]) ? R.drawable.star_gold_13 : R.drawable.star_blue_13, (int) (Math.random() * 1500));
        loadFadeImage(star14, (isGoldStar[13]) ? R.drawable.star_gold_14 : R.drawable.star_blue_14, (int) (Math.random() * 1500));
        loadFadeImage(star15, (isGoldStar[14]) ? R.drawable.star_gold_15 : R.drawable.star_blue_15, (int) (Math.random() * 1500));
        loadFadeImage(star16, (isGoldStar[15]) ? R.drawable.star_gold_16 : R.drawable.star_blue_16, (int) (Math.random() * 1500));
        loadFadeImage(star17, (isGoldStar[16]) ? R.drawable.star_gold_17 : R.drawable.star_blue_17, (int) (Math.random() * 1500));
        loadFadeImage(star18, (isGoldStar[17]) ? R.drawable.star_gold_18 : R.drawable.star_blue_18, (int) (Math.random() * 1500));
        loadFadeImage(star19, (isGoldStar[18]) ? R.drawable.star_gold_19 : R.drawable.star_blue_19, (int) (Math.random() * 1500));
        loadFadeImage(star20, (isGoldStar[19]) ? R.drawable.star_gold_20 : R.drawable.star_blue_20, (int) (Math.random() * 1500));
    }

    public void placeMonkey(int chapter) {

        if (!mFirstMonkeyPlaced) {
            mFirstMonkeyPlaced = true;

            final ImageView firstMonkey = monkeys.get(chapter-1);
            firstMonkey.setVisibility(View.VISIBLE);
            firstMonkey.animate()
                    .alpha(1)
                    .setDuration(250)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            firstMonkey.setAlpha(1f);
                        }
                    });

            ImageView newStar = stars.get(chapter-1);
            Globals.playStarWorks(this, newStar);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChapterBookButton.animate()
                            .alpha(1f)
                            .setDuration(1500)
                            .setInterpolator(new LinearInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mChapterBookButton.setAlpha(1f);
                                    mChapterBookButton.setEnabled(true);
                                    mChapterBookButton.startAnimation(mBookAnimation);
                                }
                            });
                    mChapterText.animate()
                            .alpha(1f)
                            .setDuration(1500)
                            .setInterpolator(new LinearInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mChapterBookButton.setAlpha(1f);
                                }
                            });
                }
            }, 1500);

            mCurrentStar = chapter;

        } else if (chapter != mCurrentStar && chapter <= monkeys.size()) {

            final ImageView currentMonkey = monkeys.get(mCurrentStar-1);
            currentMonkey.animate()
                    .alpha(0)
                    .setDuration(250)
                    .setInterpolator(new AccelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            currentMonkey.setAlpha(0f);
                            currentMonkey.setVisibility(View.INVISIBLE);
                        }
                    });

            final ImageView newMonkey = monkeys.get(chapter-1);
            newMonkey.setVisibility(View.VISIBLE);
            newMonkey.animate()
                    .alpha(1)
                    .setDuration(250)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            newMonkey.setAlpha(1f);
                        }
                    });

            ImageView newStar = stars.get(chapter-1);
            Globals.playStarWorks(this, newStar);
            // Globals.playStarWorks(THIS, mChapterText, 0, 360);
            mCurrentStar = chapter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("StarsMenu: " + requestCode + "," + resultCode);
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                finishActivity();
                break;
            case Code.DRILL_SPLASH:
            case Code.INTRO:
            case Code.TUTORIAL:
            case Code.MOVIE:
            case Code.RUN_DRILL:
            case Code.CHAPTER_END:
            case Code.FINALE:
                setResult(resultCode);
                finishActivity();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFinishActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mFinishActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFinishActivity) {
            Globals.PAUSE_BACKGROUND_MUSIC(context);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    public void finishActivity() {
        mFinishActivity = true;
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
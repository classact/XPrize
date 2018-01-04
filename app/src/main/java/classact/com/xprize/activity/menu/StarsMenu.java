package classact.com.xprize.activity.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.Unit;
import dagger.android.support.DaggerAppCompatActivity;

public class StarsMenu extends DaggerAppCompatActivity {

    @BindView(R.id.activity_stars_menu) ConstraintLayout mRootView;

    @BindView(R.id.monkey_button_01) ImageButton mMonkeyButton01;
    @BindView(R.id.monkey_button_02) ImageButton mMonkeyButton02;
    @BindView(R.id.monkey_button_03) ImageButton mMonkeyButton03;
    @BindView(R.id.monkey_button_04) ImageButton mMonkeyButton04;
    @BindView(R.id.monkey_button_05) ImageButton mMonkeyButton05;
    @BindView(R.id.monkey_button_06) ImageButton mMonkeyButton06;
    @BindView(R.id.monkey_button_07) ImageButton mMonkeyButton07;
    @BindView(R.id.monkey_button_08) ImageButton mMonkeyButton08;
    @BindView(R.id.monkey_button_09) ImageButton mMonkeyButton09;
    @BindView(R.id.monkey_button_10) ImageButton mMonkeyButton10;
    @BindView(R.id.monkey_button_11) ImageButton mMonkeyButton11;
    @BindView(R.id.monkey_button_12) ImageButton mMonkeyButton12;
    @BindView(R.id.monkey_button_13) ImageButton mMonkeyButton13;
    @BindView(R.id.monkey_button_14) ImageButton mMonkeyButton14;
    @BindView(R.id.monkey_button_15) ImageButton mMonkeyButton15;
    @BindView(R.id.monkey_button_16) ImageButton mMonkeyButton16;
    @BindView(R.id.monkey_button_17) ImageButton mMonkeyButton17;
    @BindView(R.id.monkey_button_18) ImageButton mMonkeyButton18;
    @BindView(R.id.monkey_button_19) ImageButton mMonkeyButton19;
    @BindView(R.id.monkey_button_20) ImageButton mMonkeyButton20;

    @BindView(R.id.star_button_01) ImageButton mStarButton01;
    @BindView(R.id.star_button_02) ImageButton mStarButton02;
    @BindView(R.id.star_button_03) ImageButton mStarButton03;
    @BindView(R.id.star_button_04) ImageButton mStarButton04;
    @BindView(R.id.star_button_05) ImageButton mStarButton05;
    @BindView(R.id.star_button_06) ImageButton mStarButton06;
    @BindView(R.id.star_button_07) ImageButton mStarButton07;
    @BindView(R.id.star_button_08) ImageButton mStarButton08;
    @BindView(R.id.star_button_09) ImageButton mStarButton09;
    @BindView(R.id.star_button_10) ImageButton mStarButton10;
    @BindView(R.id.star_button_11) ImageButton mStarButton11;
    @BindView(R.id.star_button_12) ImageButton mStarButton12;
    @BindView(R.id.star_button_13) ImageButton mStarButton13;
    @BindView(R.id.star_button_14) ImageButton mStarButton14;
    @BindView(R.id.star_button_15) ImageButton mStarButton15;
    @BindView(R.id.star_button_16) ImageButton mStarButton16;
    @BindView(R.id.star_button_17) ImageButton mStarButton17;
    @BindView(R.id.star_button_18) ImageButton mStarButton18;
    @BindView(R.id.star_button_19) ImageButton mStarButton19;
    @BindView(R.id.star_button_20) ImageButton mStarButton20;

    @BindView(R.id.chapter_book_button) ImageButton mChapterBookButton;
    @BindView(R.id.chapter_text) TextView mChapterText;

    private List<ImageButton> mMonkeyButtons;
    private List<ImageButton> mStarButtons;
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
    
    @Inject DatabaseController mDb;
    @Inject Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_menu);
        ButterKnife.bind(this);

        ImageView background = new ImageView(context);
        background.setBackgroundColor(Color.TRANSPARENT);
        background.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.MarginLayoutParams backgroundLayoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT
        );
        background.setLayoutParams(backgroundLayoutParams);
        mRootView.addView(background, 0);

        mHandler = new Handler();
        mIntent = getIntent();
        mFinishActivity = false;

        mMonkeyButtons = new ArrayList<>();
        mStarButtons = new ArrayList<>();
        mStarLevelBackgrounds = new SparseIntArray();

        mChapterText.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterText.setTextColor(Color.argb(255, 255, 205, 0));

        mMonkeyButtons.add(mMonkeyButton01);
        mMonkeyButtons.add(mMonkeyButton02);
        mMonkeyButtons.add(mMonkeyButton03);
        mMonkeyButtons.add(mMonkeyButton04);
        mMonkeyButtons.add(mMonkeyButton05);
        mMonkeyButtons.add(mMonkeyButton06);
        mMonkeyButtons.add(mMonkeyButton07);
        mMonkeyButtons.add(mMonkeyButton08);
        mMonkeyButtons.add(mMonkeyButton09);
        mMonkeyButtons.add(mMonkeyButton10);
        mMonkeyButtons.add(mMonkeyButton11);
        mMonkeyButtons.add(mMonkeyButton12);
        mMonkeyButtons.add(mMonkeyButton13);
        mMonkeyButtons.add(mMonkeyButton14);
        mMonkeyButtons.add(mMonkeyButton15);
        mMonkeyButtons.add(mMonkeyButton16);
        mMonkeyButtons.add(mMonkeyButton17);
        mMonkeyButtons.add(mMonkeyButton18);
        mMonkeyButtons.add(mMonkeyButton19);
        mMonkeyButtons.add(mMonkeyButton20);

        mStarButtons.add(mStarButton01);
        mStarButtons.add(mStarButton02);
        mStarButtons.add(mStarButton03);
        mStarButtons.add(mStarButton04);
        mStarButtons.add(mStarButton05);
        mStarButtons.add(mStarButton06);
        mStarButtons.add(mStarButton07);
        mStarButtons.add(mStarButton08);
        mStarButtons.add(mStarButton09);
        mStarButtons.add(mStarButton10);
        mStarButtons.add(mStarButton11);
        mStarButtons.add(mStarButton12);
        mStarButtons.add(mStarButton13);
        mStarButtons.add(mStarButton14);
        mStarButtons.add(mStarButton15);
        mStarButtons.add(mStarButton16);
        mStarButtons.add(mStarButton17);
        mStarButtons.add(mStarButton18);
        mStarButtons.add(mStarButton19);
        mStarButtons.add(mStarButton20);

        mStarLevelBackgrounds.put(0, R.drawable.star_level_0);
        mStarLevelBackgrounds.put(1, R.drawable.star_level_1);
        mStarLevelBackgrounds.put(2, R.drawable.star_level_2);
        mStarLevelBackgrounds.put(3, R.drawable.star_level_3);
        mStarLevelBackgrounds.put(4, R.drawable.star_level_4);
        mStarLevelBackgrounds.put(5, R.drawable.star_level_5);
        mStarLevelBackgrounds.put(6, R.drawable.star_level_6);
        mStarLevelBackgrounds.put(7, R.drawable.star_level_7);
        mStarLevelBackgrounds.put(8, R.drawable.star_level_8);
        mStarLevelBackgrounds.put(9, R.drawable.star_level_9);
        mStarLevelBackgrounds.put(10, R.drawable.star_level_10);
        mStarLevelBackgrounds.put(11, R.drawable.star_level_11);
        mStarLevelBackgrounds.put(12, R.drawable.star_level_12);
        mStarLevelBackgrounds.put(13, R.drawable.star_level_13);
        mStarLevelBackgrounds.put(14, R.drawable.star_level_14);
        mStarLevelBackgrounds.put(15, R.drawable.star_level_15);
        mStarLevelBackgrounds.put(16, R.drawable.star_level_16);
        mStarLevelBackgrounds.put(17, R.drawable.star_level_17);
        mStarLevelBackgrounds.put(18, R.drawable.star_level_18);
        mStarLevelBackgrounds.put(19, R.drawable.star_level_19);
        mStarLevelBackgrounds.put(20, R.drawable.star_level_20);
        
        LinkedHashMap<Integer, Unit> units = mDb.getUnits();
        int numberOfUnlockedChapters = 0;
        int currentChapterInProgress = 0;
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
        mChapterBookButton.setEnabled(false);
        mChapterText.setAlpha(0f);

        mMaxStars = 20;
        for (int i = 0; i < mMaxStars; i++) {
            ImageButton monkeyButton = mMonkeyButtons.get(i);
            monkeyButton.setAlpha(0f);
            monkeyButton.setVisibility(View.INVISIBLE);
        }

        mCurrentStarLimit = Math.min(numberOfUnlockedChapters, mMaxStars);
        mCurrentStar = Math.min(currentChapterInProgress, mCurrentStarLimit);
        mRootView.setBackgroundColor(Color.TRANSPARENT);
        Glide.with(this).load(mStarLevelBackgrounds.get(mCurrentStarLimit)).into(background);

        String chapterText = "Chapter " + mCurrentStar;
        mChapterText.setText(chapterText);

        for (int i = 0; i < mCurrentStarLimit; i++) {
            ImageButton starButton = mStarButtons.get(i);
            final int chapter = i + 1;
            starButton.setOnClickListener((v) -> {
                    String text = "Chapter " + chapter;
                    mChapterText.setText(text);
                    placeMonkey(chapter);
            });
        }

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

    public void placeMonkey(int chapter) {

        if (!mFirstMonkeyPlaced) {
            mFirstMonkeyPlaced = true;

            final ImageButton firstMonkey = mMonkeyButtons.get(chapter-1);
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

            ImageButton newStar = mStarButtons.get(chapter-1);
            Globals.playStarWorks(this, newStar);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChapterBookButton.animate()
                            .alpha(1f)
                            .setDuration(1000)
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
                            .setDuration(1000)
                            .setInterpolator(new LinearInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mChapterBookButton.setAlpha(1f);
                                }
                            });
                }
            }, 1000);

            mCurrentStar = chapter;

        } else if (chapter != mCurrentStar && chapter <= mMonkeyButtons.size()) {

            final ImageButton currentMonkey = mMonkeyButtons.get(mCurrentStar-1);
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

            final ImageButton newMonkey = mMonkeyButtons.get(chapter-1);
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

            ImageButton newStar = mStarButtons.get(chapter-1);
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
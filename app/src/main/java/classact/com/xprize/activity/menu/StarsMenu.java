package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import classact.com.xprize.R;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.locale.Languages;

public class StarsMenu extends AppCompatActivity {

    private ConstraintLayout mRootView;

    private ImageButton mMonkeyButton01;
    private ImageButton mMonkeyButton02;
    private ImageButton mMonkeyButton03;
    private ImageButton mMonkeyButton04;
    private ImageButton mMonkeyButton05;
    private ImageButton mMonkeyButton06;
    private ImageButton mMonkeyButton07;
    private ImageButton mMonkeyButton08;
    private ImageButton mMonkeyButton09;
    private ImageButton mMonkeyButton10;
    private ImageButton mMonkeyButton11;
    private ImageButton mMonkeyButton12;
    private ImageButton mMonkeyButton13;
    private ImageButton mMonkeyButton14;
    private ImageButton mMonkeyButton15;
    private ImageButton mMonkeyButton16;
    private ImageButton mMonkeyButton17;
    private ImageButton mMonkeyButton18;
    private ImageButton mMonkeyButton19;
    private ImageButton mMonkeyButton20;

    private ImageButton mStarButton01;
    private ImageButton mStarButton02;
    private ImageButton mStarButton03;
    private ImageButton mStarButton04;
    private ImageButton mStarButton05;
    private ImageButton mStarButton06;
    private ImageButton mStarButton07;
    private ImageButton mStarButton08;
    private ImageButton mStarButton09;
    private ImageButton mStarButton10;
    private ImageButton mStarButton11;
    private ImageButton mStarButton12;
    private ImageButton mStarButton13;
    private ImageButton mStarButton14;
    private ImageButton mStarButton15;
    private ImageButton mStarButton16;
    private ImageButton mStarButton17;
    private ImageButton mStarButton18;
    private ImageButton mStarButton19;
    private ImageButton mStarButton20;

    private ImageButton mChapterBookButton;
    private TextView mChapterText;

    private List<ImageButton> mMonkeyButtons;
    private List<ImageButton> mStarButtons;
    private int mMaxStars;
    private int mCurrentStarLimit;
    private int mCurrentStar;

    private SparseArray<Integer> mStarLevelBackgrounds;

    private DatabaseController mDb;
    private Intent mIntent;
    private boolean mFinishActivity;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_menu);
        mIntent = getIntent();
        mFinishActivity = false;

        mMonkeyButtons = new ArrayList<>();
        mStarButtons = new ArrayList<>();
        mStarLevelBackgrounds = new SparseArray<>();

        mRootView = (ConstraintLayout) findViewById(R.id.activity_stars_menu);
        mChapterBookButton = (ImageButton) findViewById(R.id.chapter_book_button);

        mChapterText = (TextView) findViewById(R.id.chapter_text);
        mChapterText.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterText.setTextColor(Color.argb(255, 255, 205, 0));

        mMonkeyButton01 = (ImageButton) findViewById(R.id.monkey_button_01);
        mMonkeyButton02 = (ImageButton) findViewById(R.id.monkey_button_02);
        mMonkeyButton03 = (ImageButton) findViewById(R.id.monkey_button_03);
        mMonkeyButton04 = (ImageButton) findViewById(R.id.monkey_button_04);
        mMonkeyButton05 = (ImageButton) findViewById(R.id.monkey_button_05);
        mMonkeyButton06 = (ImageButton) findViewById(R.id.monkey_button_06);
        mMonkeyButton07 = (ImageButton) findViewById(R.id.monkey_button_07);
        mMonkeyButton08 = (ImageButton) findViewById(R.id.monkey_button_08);
        mMonkeyButton09 = (ImageButton) findViewById(R.id.monkey_button_09);
        mMonkeyButton10 = (ImageButton) findViewById(R.id.monkey_button_10);
        mMonkeyButton11 = (ImageButton) findViewById(R.id.monkey_button_11);
        mMonkeyButton12 = (ImageButton) findViewById(R.id.monkey_button_12);
        mMonkeyButton13 = (ImageButton) findViewById(R.id.monkey_button_13);
        mMonkeyButton14 = (ImageButton) findViewById(R.id.monkey_button_14);
        mMonkeyButton15 = (ImageButton) findViewById(R.id.monkey_button_15);
        mMonkeyButton16 = (ImageButton) findViewById(R.id.monkey_button_16);
        mMonkeyButton17 = (ImageButton) findViewById(R.id.monkey_button_17);
        mMonkeyButton18 = (ImageButton) findViewById(R.id.monkey_button_18);
        mMonkeyButton19 = (ImageButton) findViewById(R.id.monkey_button_19);
        mMonkeyButton20 = (ImageButton) findViewById(R.id.monkey_button_20);

        mStarButton01 = (ImageButton) findViewById(R.id.star_button_01);
        mStarButton02 = (ImageButton) findViewById(R.id.star_button_02);
        mStarButton03 = (ImageButton) findViewById(R.id.star_button_03);
        mStarButton04 = (ImageButton) findViewById(R.id.star_button_04);
        mStarButton05 = (ImageButton) findViewById(R.id.star_button_05);
        mStarButton06 = (ImageButton) findViewById(R.id.star_button_06);
        mStarButton07 = (ImageButton) findViewById(R.id.star_button_07);
        mStarButton08 = (ImageButton) findViewById(R.id.star_button_08);
        mStarButton09 = (ImageButton) findViewById(R.id.star_button_09);
        mStarButton10 = (ImageButton) findViewById(R.id.star_button_10);
        mStarButton11 = (ImageButton) findViewById(R.id.star_button_11);
        mStarButton12 = (ImageButton) findViewById(R.id.star_button_12);
        mStarButton13 = (ImageButton) findViewById(R.id.star_button_13);
        mStarButton14 = (ImageButton) findViewById(R.id.star_button_14);
        mStarButton15 = (ImageButton) findViewById(R.id.star_button_15);
        mStarButton16 = (ImageButton) findViewById(R.id.star_button_16);
        mStarButton17 = (ImageButton) findViewById(R.id.star_button_17);
        mStarButton18 = (ImageButton) findViewById(R.id.star_button_18);
        mStarButton19 = (ImageButton) findViewById(R.id.star_button_19);
        mStarButton20 = (ImageButton) findViewById(R.id.star_button_20);

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

        mDb = DatabaseController.getInstance(THIS, Languages.ENGLISH);

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

        if (currentChapterInProgress == 0) {
            // DO SOMETHING!!!!
        }

        mMaxStars = 20;
        mCurrentStarLimit = Math.min(numberOfUnlockedChapters, mMaxStars);
        mCurrentStar = Math.min(currentChapterInProgress, mCurrentStarLimit);
        mRootView.setBackgroundResource(mStarLevelBackgrounds.get(mCurrentStarLimit));

        String chapterText = "Chapter " + mCurrentStar;
        mChapterText.setText(chapterText);

        for (int i = 0; i < mMaxStars; i++) {
            ImageButton monkeyButton = mMonkeyButtons.get(i);
            final int chapter = i+1;

            if (chapter != mCurrentStar) {
                monkeyButton.setVisibility(View.INVISIBLE);
            }
        }

        for (int i = 0; i < mCurrentStarLimit; i++) {
            ImageButton starButton = mStarButtons.get(i);
            final int chapter = i+1;
            starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chapterText = "Chapter " + chapter;
                    mChapterText.setText(chapterText);
                    placeMonkey(chapter);
                }
            });
        }

        mChapterBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinishActivity = true;
                Intent intent = new Intent(THIS, SectionsMenu.class);
                intent.putExtra("selected_chapter", mCurrentStar);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public void placeMonkey(int chapter) {
        if (chapter != mCurrentStar && chapter <= mMonkeyButtons.size()) {
            ImageButton currentMonkey = mMonkeyButtons.get(mCurrentStar-1);
            currentMonkey.setVisibility(View.INVISIBLE);

            ImageButton newMonkey = mMonkeyButtons.get(chapter-1);
            newMonkey.setVisibility(View.VISIBLE);
            mCurrentStar = chapter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("StarsMenu: " + requestCode + "," + resultCode);
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case Code.INTRO:
            case Code.TUTORIAL:
            case Code.MOVIE:
            case Code.RUN_DRILL:
            case Code.CHAPTER_END:
            case Code.FINALE:
                setResult(resultCode);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
            Globals.RESUME_BACKGROUND_MUSIC(THIS);
        } else {
            mFinishActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFinishActivity) {
            Globals.PAUSE_BACKGROUND_MUSIC(THIS);
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
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mFinishActivity = true;
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

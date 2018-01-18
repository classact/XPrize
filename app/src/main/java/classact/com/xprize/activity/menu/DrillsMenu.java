package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import classact.com.xprize.R;
import classact.com.xprize.activity.MenuActivity;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.locale.Languages;
import dagger.android.support.DaggerAppCompatActivity;

public class DrillsMenu extends MenuActivity {

    private TextView mChapterTitle;
    private TextView mChapterNumber;
    private TextView mChapterSection;
    private ImageView mChapterHeader;
    private TextView mDrillInstruction;

    private LinkedHashMap<Integer, Integer> mChapterHeaderImageResources;

    private int mCurrentDrill;
    private int mMaxUnlockedDrills;
    private int mMaxDrills;

    private List<ImageButton> mDrillButtons;
    private ImageButton mDrillButton01;
    private ImageButton mDrillButton02;
    private ImageButton mDrillButton03;
    private ImageButton mDrillButton04;
    private ImageButton mDrillButton05;
    private ImageButton mDrillButton06;
    private ImageButton mDrillButton07;
    private ImageButton mDrillButton08;
    private ImageButton mDrillButton09;
    private ImageButton mDrillButton10;

    private List<ImageButton> mDrillMonkeys;
    private ImageButton mDrillMonkey01;
    private ImageButton mDrillMonkey02;
    private ImageButton mDrillMonkey03;
    private ImageButton mDrillMonkey04;
    private ImageButton mDrillMonkey05;
    private ImageButton mDrillMonkey06;
    private ImageButton mDrillMonkey07;
    private ImageButton mDrillMonkey08;
    private ImageButton mDrillMonkey09;
    private ImageButton mDrillMonkey10;

    private LinkedHashMap<ImageButton, ImageButton> mButtonMonkeyMap;
    private LinkedHashMap<ImageButton, Integer> mButtonDrillMap;

    private SparseArray<UnitSectionDrill> mUnitSectionDrillMap;
    private LinkedHashMap<Integer, UnitSectionDrill> mUnitSectionDrillByDrillNumberMap;

    private Intent mIntent;
    private int mSelectedChapter;
    private int mSelectedSection;
    private int mSelectedUnitSection;
    private int mSelectedSubId;
    private String mSelectedLetter;
    private ConstraintLayout mRootView;
    private boolean mFinishActivity;

    @Inject DatabaseController mDb;
    @Inject DrillFetcher drillFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drills_menu);
        mIntent = getIntent();
        mSelectedChapter = mIntent.getIntExtra("selected_chapter", 0);
        mSelectedSection = mIntent.getIntExtra("selected_section", 1);
        mSelectedUnitSection = mIntent.getIntExtra("selected_unit_section", 1);
        mSelectedSubId = 0;
        mSelectedLetter = "";
        mRootView = (ConstraintLayout) findViewById(R.id.activity_drills_menu);
        mFinishActivity = false;

        mDrillButtons = new ArrayList<>();
        mDrillMonkeys = new ArrayList<>();
        mButtonMonkeyMap = new LinkedHashMap<>();
        mButtonDrillMap = new LinkedHashMap<>();

        mChapterHeaderImageResources = new LinkedHashMap<>();
        mChapterTitle = (TextView) findViewById(R.id.chapter_title);
        mChapterNumber = (TextView) findViewById(R.id.chapter_number);
        mChapterSection = (TextView) findViewById(R.id.chapter_section);
        mChapterHeader = (ImageView) findViewById(R.id.chapter_header);
        mDrillInstruction = (TextView) findViewById(R.id.drill_instruction);

        mDrillButton01 = (ImageButton) findViewById(R.id.drill_button_01);
        mDrillButton02 = (ImageButton) findViewById(R.id.drill_button_02);
        mDrillButton03 = (ImageButton) findViewById(R.id.drill_button_03);
        mDrillButton04 = (ImageButton) findViewById(R.id.drill_button_04);
        mDrillButton05 = (ImageButton) findViewById(R.id.drill_button_05);
        mDrillButton06 = (ImageButton) findViewById(R.id.drill_button_06);
        mDrillButton07 = (ImageButton) findViewById(R.id.drill_button_07);
        mDrillButton08 = (ImageButton) findViewById(R.id.drill_button_08);
        mDrillButton09 = (ImageButton) findViewById(R.id.drill_button_09);
        mDrillButton10 = (ImageButton) findViewById(R.id.drill_button_10);

        mDrillMonkey01 = (ImageButton) findViewById(R.id.drill_monkey_01);
        mDrillMonkey02 = (ImageButton) findViewById(R.id.drill_monkey_02);
        mDrillMonkey03 = (ImageButton) findViewById(R.id.drill_monkey_03);
        mDrillMonkey04 = (ImageButton) findViewById(R.id.drill_monkey_04);
        mDrillMonkey05 = (ImageButton) findViewById(R.id.drill_monkey_05);
        mDrillMonkey06 = (ImageButton) findViewById(R.id.drill_monkey_06);
        mDrillMonkey07 = (ImageButton) findViewById(R.id.drill_monkey_07);
        mDrillMonkey08 = (ImageButton) findViewById(R.id.drill_monkey_08);
        mDrillMonkey09 = (ImageButton) findViewById(R.id.drill_monkey_09);
        mDrillMonkey10 = (ImageButton) findViewById(R.id.drill_monkey_10);

        // Other
        mDrillButtons.add(mDrillButton01);
        mDrillButtons.add(mDrillButton02);
        mDrillButtons.add(mDrillButton03);
        mDrillButtons.add(mDrillButton04);
        mDrillButtons.add(mDrillButton05);
        mDrillButtons.add(mDrillButton06);
        mDrillButtons.add(mDrillButton07);
        mDrillButtons.add(mDrillButton08);
        mDrillButtons.add(mDrillButton09);
        mDrillButtons.add(mDrillButton10);

        mDrillMonkeys.add(mDrillMonkey01);
        mDrillMonkeys.add(mDrillMonkey02);
        mDrillMonkeys.add(mDrillMonkey03);
        mDrillMonkeys.add(mDrillMonkey04);
        mDrillMonkeys.add(mDrillMonkey05);
        mDrillMonkeys.add(mDrillMonkey06);
        mDrillMonkeys.add(mDrillMonkey07);
        mDrillMonkeys.add(mDrillMonkey08);
        mDrillMonkeys.add(mDrillMonkey09);
        mDrillMonkeys.add(mDrillMonkey10);

        mButtonMonkeyMap.put(mDrillButton01, mDrillMonkey01);
        mButtonMonkeyMap.put(mDrillButton02, mDrillMonkey02);
        mButtonMonkeyMap.put(mDrillButton03, mDrillMonkey03);
        mButtonMonkeyMap.put(mDrillButton04, mDrillMonkey04);
        mButtonMonkeyMap.put(mDrillButton05, mDrillMonkey05);
        mButtonMonkeyMap.put(mDrillButton06, mDrillMonkey06);
        mButtonMonkeyMap.put(mDrillButton07, mDrillMonkey07);
        mButtonMonkeyMap.put(mDrillButton08, mDrillMonkey08);
        mButtonMonkeyMap.put(mDrillButton09, mDrillMonkey09);
        mButtonMonkeyMap.put(mDrillButton10, mDrillMonkey10);

        mButtonDrillMap.put(mDrillButton01, 1);
        mButtonDrillMap.put(mDrillButton02, 2);
        mButtonDrillMap.put(mDrillButton03, 3);
        mButtonDrillMap.put(mDrillButton04, 4);
        mButtonDrillMap.put(mDrillButton05, 5);
        mButtonDrillMap.put(mDrillButton06, 6);
        mButtonDrillMap.put(mDrillButton07, 7);
        mButtonDrillMap.put(mDrillButton08, 8);
        mButtonDrillMap.put(mDrillButton09, 9);
        mButtonDrillMap.put(mDrillButton10, 10);

        mChapterHeaderImageResources.put(1, R.drawable.chapter_01_header);
        mChapterHeaderImageResources.put(2, R.drawable.chapter_02_header);
        mChapterHeaderImageResources.put(3, R.drawable.chapter_03_header);
        mChapterHeaderImageResources.put(4, R.drawable.chapter_04_header);
        mChapterHeaderImageResources.put(5, R.drawable.chapter_05_header);
        mChapterHeaderImageResources.put(6, R.drawable.chapter_06_header);
        mChapterHeaderImageResources.put(7, R.drawable.chapter_07_header);
        mChapterHeaderImageResources.put(8, R.drawable.chapter_08_header);
        mChapterHeaderImageResources.put(9, R.drawable.chapter_09_header);
        mChapterHeaderImageResources.put(10, R.drawable.chapter_10_header);
        mChapterHeaderImageResources.put(11, R.drawable.chapter_11_header);
        mChapterHeaderImageResources.put(12, R.drawable.chapter_12_header);
        mChapterHeaderImageResources.put(13, R.drawable.chapter_13_header);
        mChapterHeaderImageResources.put(14, R.drawable.chapter_14_header);
        mChapterHeaderImageResources.put(15, R.drawable.chapter_15_header);
        mChapterHeaderImageResources.put(16, R.drawable.chapter_16_header);
        mChapterHeaderImageResources.put(17, R.drawable.chapter_17_header);
        mChapterHeaderImageResources.put(18, R.drawable.chapter_18_header);
        mChapterHeaderImageResources.put(19, R.drawable.chapter_19_header);
        mChapterHeaderImageResources.put(20, R.drawable.chapter_20_header);

        // Set type face
        mChapterTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterNumber.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterSection.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mDrillInstruction.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

        // Get number of unit section drills
        mUnitSectionDrillMap = mDb.getUnitSectionDrills(mSelectedUnitSection);
        int numberOfDrills = mUnitSectionDrillMap.size();
        mMaxDrills = Math.min(numberOfDrills, mDrillButtons.size());

        // Setup unlocked and locked drills + current drill in progress
        // Also setup unit section drills by drill number
        mUnitSectionDrillByDrillNumberMap = new LinkedHashMap<>();
        int numberOfUnlockedDrills = 0;
        int currentDrillInProgress = 1;
        boolean currentDrillInProgressFound = false;
        for (int i = 0; i < mUnitSectionDrillMap.size(); i++) {
            int key = mUnitSectionDrillMap.keyAt(i);
            UnitSectionDrill unitSectionDrill = mUnitSectionDrillMap.get(key);
            if (unitSectionDrill.getUnlocked() == 1) {
                numberOfUnlockedDrills++;
                if (unitSectionDrill.getInProgress() == 1) {
                    currentDrillInProgressFound = true;
                    currentDrillInProgress = unitSectionDrill.getDrillOrder();
                }
            }
            mUnitSectionDrillByDrillNumberMap.put(unitSectionDrill.getDrillOrder(), unitSectionDrill);
        }
        if (numberOfUnlockedDrills < 1) {
            numberOfUnlockedDrills = 1;
        }
        mMaxUnlockedDrills = Math.min(numberOfUnlockedDrills, mMaxDrills);
        if (mMaxUnlockedDrills < mMaxDrills && !currentDrillInProgressFound) {
            currentDrillInProgress = mMaxUnlockedDrills;
        }
        mCurrentDrill = Math.min(currentDrillInProgress, mMaxUnlockedDrills);

        // Setup drill instruction
        // Or navigate to different intent
        String drillInstruction = "";
        switch (mSelectedSection) {
            case DatabaseController.STORY_SECTION:
                UnitSectionDrill unitSectionDrill = null;
                for (int i = 0; i < mUnitSectionDrillMap.size(); i++) {
                    int key = mUnitSectionDrillMap.keyAt(i);
                    unitSectionDrill = mUnitSectionDrillMap.get(key);
                    int unitSectionId = unitSectionDrill.getUnitSectionId();
                    UnitSection unitSection = mDb.getUnitSection(unitSectionId);
                    int sectionId = unitSection.getSectionId();
                    int drillId = unitSectionDrill.getDrillId();
                    if (mDb.getDrillTypes()
                            .get(mDb.getDrills().get(drillId).getDrillTypeId())
                            .getName().equalsIgnoreCase("Movie") &&
                            mDb.getSections()
                                    .get(sectionId)
                                    .getName().equalsIgnoreCase("Story")) {
                        break;
                    }
                }
                try {
                    if (unitSectionDrill == null) {
                        throw new Exception("DrillsMenu: Cannot play story movie");
                    }
                    mDb.playUnitSectionDrill(unitSectionDrill.getUnitSectionDrillId());

                    Object[] objectArray = new Object[2];
                    drillFetcher.fetch(objectArray, Languages.ENGLISH, unitSectionDrill);

                    Intent intent = (Intent) objectArray[0];
                    int resultCode = (int) objectArray[1];

                    mFinishActivity = true;
                    Globals.STOP_BACKGROUND_MUSIC(context);

                    startActivityForResult(intent, resultCode);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case DatabaseController.PHONICS_SECTION:
                // mRootView.setBackgroundResource(R.drawable.bg_yellow);
                mSelectedSubId = mIntent.getIntExtra("selected_sub_id", 1);

                LinkedHashMap<Integer, String> subIdLetterMap = new LinkedHashMap<>();
                SparseArray<UnitSection> unitSectionMap = mDb.getUnitSections(mSelectedChapter, mSelectedSection);
                for (int i = 0; i < unitSectionMap.size(); i++) {
                    int key = unitSectionMap.keyAt(i);
                    UnitSection unitSection = unitSectionMap.get(key);
                    int subId = unitSection.getSectionSubId();
                    String subject = unitSection.getSectionSubject();
                    subIdLetterMap.put(subId, subject);
                }

                if (mSelectedSubId < 1 || subIdLetterMap.size() < 1) {
                    mSelectedSubId = 1;
                } else if (mSelectedSubId > subIdLetterMap.size()) {
                    mSelectedSubId = subIdLetterMap.size();
                }
                mSelectedLetter = subIdLetterMap.get(mSelectedSubId);
                String letterType = "letter";
                if (mSelectedLetter.length() > 1) {
                    letterType = "sound";
                }
                drillInstruction = "Learn the " + letterType + ", “" + mSelectedLetter + "” !";
                break;
            case DatabaseController.WORDS_SECTION:
                // mRootView.setBackgroundResource(R.drawable.bg_pink);
                drillInstruction = "Learn to read and write words !";
                break;
            case DatabaseController.BOOKS_SECTION:
                // mRootView.setBackgroundResource(R.drawable.bg_green);
                drillInstruction = "Learn to read books !";
                break;
            case DatabaseController.MATHS_SECTION:
                drillInstruction = "Learn count numbers !";
                break;
            default:
                break;
        }
        mDrillInstruction.setText(drillInstruction);

        // Setup chapter header
        mChapterTitle.setText(R.string.Chapter);
        mChapterNumber.setText(String.valueOf(mSelectedChapter));
        mChapterSection.setText(mDb.getSections().get(mSelectedSection).getName());
        mChapterHeader.setImageResource(mChapterHeaderImageResources.get(mSelectedChapter));

        for (int i = 0; i < mDrillButtons.size(); i++) {
            ImageButton drillButton = mDrillButtons.get(i);

            if (i < mMaxDrills) {
                drillButton.setVisibility(View.VISIBLE);

                if (i < mMaxUnlockedDrills) {
                    enableButton(drillButton, true);

                    if (i == mCurrentDrill-1) {
                        showMonkey(mButtonMonkeyMap.get(drillButton));
                    }
                } else {
                    enableButton(drillButton, false);
                }
            } else {
                drillButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void showMonkey(ImageButton monkey) {
        for (Map.Entry<ImageButton, ImageButton> entry : mButtonMonkeyMap.entrySet()) {
            ImageView mapMonkey = entry.getValue();
            if (mapMonkey.equals(monkey)) {
                mapMonkey.setVisibility(View.VISIBLE);
            } else {
                mapMonkey.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void enableButton(final ImageButton button, boolean enable) {
        if (enable) {
            button.setEnabled(true);
            final ImageButton monkey = mButtonMonkeyMap.get(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show monkey
                    showMonkey(monkey);
                    // Get drill number
                    int drillNumber = mButtonDrillMap.get(button);
                    // Get unit section drill
                    UnitSectionDrill unitSectionDrill = mUnitSectionDrillByDrillNumberMap.get(drillNumber);
                    mDb.playUnitSectionDrill(unitSectionDrill.getUnitSectionDrillId());

                    try {
                        Object[] objectArray = new Object[2];
                        drillFetcher.fetch(objectArray, Languages.ENGLISH, unitSectionDrill);

                        Intent intent = (Intent) objectArray[0];
                        int resultCode = (int) objectArray[1];

                        mFinishActivity = true;
                        Globals.STOP_BACKGROUND_MUSIC(context);

                        startActivityForResult(intent, resultCode);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            if (monkey.getVisibility() == View.VISIBLE) {
                                monkey.setPressed(true);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (monkey.getVisibility() == View.VISIBLE) {
                                monkey.setPressed(false);
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        } else {
            button.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("DrillsMenu: " + requestCode + "," + resultCode);
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
        switch (requestCode) {
            case Code.DRILL_SPLASH:
            case Code.INTRO:
            case Code.TUTORIAL:
            case Code.MOVIE:
            case Code.RUN_DRILL:
            case Code.CHAPTER_END:
            case Code.FINALE:
                setResult(requestCode);
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
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mFinishActivity = true;
        // setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
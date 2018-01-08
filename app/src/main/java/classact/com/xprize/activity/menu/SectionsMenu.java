package classact.com.xprize.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
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
import classact.com.xprize.database.model.Section;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.UnitSection;

public class SectionsMenu extends MenuActivity {

    @BindView(R.id.chapter_title) TextView mChapterTitle;
    @BindView(R.id.chapter_number) TextView mChapterNumber;

    @BindView(R.id.intro_title) TextView mStoryTitle;
    @BindView(R.id.phonics_title) TextView mPhonicsTitle;
    @BindView(R.id.words_title) TextView mWordsTitle;
    @BindView(R.id.books_title) TextView mBooksTitle;
    @BindView(R.id.maths_title) TextView mMathsTitle;

    @BindView(R.id.intro_button) ImageButton mStoryButton;
    @BindView(R.id.phonics_button) ImageButton mPhonicsButton;
    @BindView(R.id.words_button) ImageButton mWordsButton;
    @BindView(R.id.books_button) ImageButton mBooksButton;
    @BindView(R.id.maths_button) ImageButton mMathsButton;

    @BindView(R.id.intro_monkey) ImageButton mStoryMonkey;
    @BindView(R.id.phonics_monkey) ImageButton mPhonicsMonkey;
    @BindView(R.id.words_monkey) ImageButton mWordsMonkey;
    @BindView(R.id.books_monkey) ImageButton mBooksMonkey;
    @BindView(R.id.maths_monkey) ImageButton mMathsMonkey;

    @BindView(R.id.hsv) HorizontalScrollView mHSV;
    @BindView(R.id.chapter_header) ImageView mChapterHeader;

    private LinkedHashMap<ImageButton, TextView> mButtonTitles;
    private LinkedHashMap<ImageButton, ImageButton> mButtonMonkeys;
    private LinkedHashMap<ImageButton, Integer> mButtonSections;

    private LinkedHashMap<Integer, TextView> mSectionTitles;
    private LinkedHashMap<Integer, ImageButton> mSectionMonkeys;
    private LinkedHashMap<Integer, ImageButton> mSectionButtons;

    private List<Integer> mChapterHeaderImageResources;

    private DisplayMetrics mDisplayMetrics;
    private float mScreenDensity;

    private final float MAX_SCN_WIDTH = 2560f;
    private final float MAX_CH_WIDTH = 2756f;
    private final float MAX_HSV_WIDTH = 5120f;
    private final float SCN_CH_DIFF = MAX_CH_WIDTH - MAX_SCN_WIDTH;

    private Intent mIntent;
    private int mSelectedChapter;
    private boolean mFinishActivity;

    @Inject DatabaseController mDb;
    @Inject Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_menu);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mSelectedChapter = mIntent.getIntExtra("selected_chapter", 0);
        mFinishActivity = false;

        mChapterHeaderImageResources = new ArrayList<>();

        mDisplayMetrics = getResources().getDisplayMetrics();
        mScreenDensity = mDisplayMetrics.density;

        mHSV.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            float fScrollX = (float) scrollX;
            float ratio = fScrollX/MAX_HSV_WIDTH;
            float translationX = SCN_CH_DIFF * ratio * -1f;

            mChapterHeader.setX(translationX);
        });

        mChapterTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mChapterNumber.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));

        mStoryTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mPhonicsTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mWordsTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mBooksTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));
        mMathsTitle.setTypeface(Globals.TYPEFACE_EDU_AID(getAssets()));


        mChapterHeaderImageResources.add(R.drawable.chapter_01_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_02_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_03_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_04_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_05_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_06_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_07_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_08_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_09_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_10_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_11_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_12_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_13_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_14_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_15_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_16_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_17_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_18_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_19_header);
        mChapterHeaderImageResources.add(R.drawable.chapter_20_header);

        SparseArray<Section> sectionMap = mDb.getSections();
        SparseArray<UnitSection> unitSectionMap = mDb.getUnitSections(mSelectedChapter);
        List<UnitSection> unitSections = new ArrayList<>();
        UnitSection lastUnlockedUnitSection = null;

        List<String> sectionHeadings = new ArrayList<>();
        for (int i = 0; i < unitSectionMap.size(); i++) {
            int key = unitSectionMap.keyAt(i);
            UnitSection unitSection = unitSectionMap.get(key);
            int sectionId = unitSection.getSectionId();
            Section section = sectionMap.get(sectionId);
            String sectionName = section.getName();
            if (!sectionName.equalsIgnoreCase("Chapter End")) {
                if (!sectionHeadings.contains(sectionName)) {
                    sectionHeadings.add(sectionName);
                    unitSections.add(unitSection);
                }
                if (unitSection.getUnlocked() == 1) {
                    lastUnlockedUnitSection = unitSection;
                }
                unitSections.add(unitSection);
            }
        }

        final int STORY_SECTION = DatabaseController.STORY_SECTION;
        final int PHONICS_SECTION = DatabaseController.PHONICS_SECTION;
        final int WORDS_SECTION = DatabaseController.WORDS_SECTION;
        final int BOOKS_SECTION = DatabaseController.BOOKS_SECTION;
        final int MATHS_SECTION = DatabaseController.MATHS_SECTION;

        int storyWidth = getMeasuredWidth(mStoryTitle, sectionHeadings.get(0));
        int phonicsWidth = getMeasuredWidth(mPhonicsTitle, sectionHeadings.get(1));
        int WordsWidth = getMeasuredWidth(mWordsTitle, sectionHeadings.get(2));
        int booksWidth = getMeasuredWidth(mBooksTitle, sectionHeadings.get(3));
        int mathsWidth = getMeasuredWidth(mMathsTitle, sectionHeadings.get(4));

        placeMonkey(mStoryMonkey, (int) ((float) storyWidth/1.5f));
        placeMonkey(mPhonicsMonkey, (int) ((float) phonicsWidth/1.5f));
        placeMonkey(mWordsMonkey, (int) ((float) WordsWidth/1.5f));
        placeMonkey(mBooksMonkey, (int) ((float) booksWidth/1.5f));
        placeMonkey(mMathsMonkey, (int) ((float) mathsWidth/1.5f));

        mButtonTitles = new LinkedHashMap<>();
        mButtonTitles.put(mStoryButton, mStoryTitle);
        mButtonTitles.put(mPhonicsButton, mPhonicsTitle);
        mButtonTitles.put(mWordsButton, mWordsTitle);
        mButtonTitles.put(mBooksButton, mBooksTitle);
        mButtonTitles.put(mMathsButton, mMathsTitle);

        mButtonMonkeys = new LinkedHashMap<>();
        mButtonMonkeys.put(mStoryButton, mStoryMonkey);
        mButtonMonkeys.put(mPhonicsButton, mPhonicsMonkey);
        mButtonMonkeys.put(mWordsButton, mWordsMonkey);
        mButtonMonkeys.put(mBooksButton, mBooksMonkey);
        mButtonMonkeys.put(mMathsButton, mMathsMonkey);

        mButtonSections = new LinkedHashMap<>();
        mButtonSections.put(mStoryButton, STORY_SECTION);
        mButtonSections.put(mPhonicsButton, PHONICS_SECTION);
        mButtonSections.put(mWordsButton, WORDS_SECTION);
        mButtonSections.put(mBooksButton, BOOKS_SECTION);
        mButtonSections.put(mMathsButton, MATHS_SECTION);

        mSectionTitles = new LinkedHashMap<>();
        mSectionTitles.put(STORY_SECTION, mStoryTitle);
        mSectionTitles.put(PHONICS_SECTION, mPhonicsTitle);
        mSectionTitles.put(WORDS_SECTION, mWordsTitle);
        mSectionTitles.put(BOOKS_SECTION, mBooksTitle);
        mSectionTitles.put(MATHS_SECTION, mMathsTitle);

        mSectionMonkeys = new LinkedHashMap<>();
        mSectionMonkeys.put(STORY_SECTION, mStoryMonkey);
        mSectionMonkeys.put(PHONICS_SECTION, mPhonicsMonkey);
        mSectionMonkeys.put(WORDS_SECTION, mWordsMonkey);
        mSectionMonkeys.put(BOOKS_SECTION, mBooksMonkey);
        mSectionMonkeys.put(MATHS_SECTION, mMathsMonkey);

        mSectionButtons = new LinkedHashMap<>();
        mSectionButtons.put(STORY_SECTION, mStoryButton);
        mSectionButtons.put(PHONICS_SECTION, mPhonicsButton);
        mSectionButtons.put(WORDS_SECTION, mWordsButton);
        mSectionButtons.put(BOOKS_SECTION, mBooksButton);
        mSectionButtons.put(MATHS_SECTION, mMathsButton);

        mChapterTitle.setText(R.string.Chapter);
        mChapterNumber.setText(String.valueOf(mSelectedChapter));
        loadImage(mChapterHeader, mChapterHeaderImageResources.get(mSelectedChapter-1));

        // Check if is latest chapter
        LinkedHashMap<Integer, Unit> units = mDb.getUnits();
        int numberOfUnlockedChapters = 0;
        for (Map.Entry<Integer, Unit> unitEntry : units.entrySet()) {
            Unit unit = unitEntry.getValue();
            int unitId = unit.getUnitId();
            if (!(unitId == 0 || unitId == 21)) {
                if (unit.getUnitUnlocked() == 1) {
                    numberOfUnlockedChapters++;
                }
            }
        }

        // Setup sections
        int sectionIdInProgress = STORY_SECTION; // Default section in progress

        List<Integer> parsedSectionIds = new ArrayList<>();

        boolean unlockedSectionFound = false;
        for (UnitSection unitSection : unitSections) {
            int sectionId = unitSection.getSectionId();

            // Check if section id already covered
            if (!parsedSectionIds.contains(sectionId)) {
                parsedSectionIds.add(sectionId);

                // Checked unlocked status
                int sectionUnlocked = unitSection.getUnlocked();
                boolean sectionUnlockedStatus = (sectionUnlocked == 1);
                enableButton(mSectionButtons.get(sectionId), sectionUnlockedStatus);
            }
            // Check unit in progress
            int unitSectionInProgress = unitSection.getInProgress();
            if (unitSectionInProgress == 1) {
                unlockedSectionFound = true;
                sectionIdInProgress = sectionId;
            } else if (!unlockedSectionFound &&
                    (mSelectedChapter == numberOfUnlockedChapters) &&
                    (unitSection == lastUnlockedUnitSection)) {
                sectionIdInProgress = sectionId;
            }
        }
        showMonkey(mSectionMonkeys.get(sectionIdInProgress));

    }

    public void placeMonkey(ImageButton monkey, int offset) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) monkey.getLayoutParams();
        layoutParams.rightMargin = offset;
        monkey.setLayoutParams(layoutParams);
    }

    public void showMonkey(ImageButton monkey) {
        for (Map.Entry<ImageButton, ImageButton> entry : mButtonMonkeys.entrySet()) {
            ImageButton mapMonkey = entry.getValue();
            if (mapMonkey == monkey) {
                mapMonkey.setVisibility(View.VISIBLE);
            } else {
                mapMonkey.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void enableButton(final ImageButton button, boolean enable) {
        TextView title = mButtonTitles.get(button);
        if (enable) {
            title.setTextColor(Color.argb(200, 0, 0, 0));
            button.setEnabled(true);
            final ImageButton monkey = mButtonMonkeys.get(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMonkey(monkey);
                    int selectedSectionId = mButtonSections.get(button);
                    Intent intent = null;
                    if (selectedSectionId == DatabaseController.PHONICS_SECTION) {
                        intent = new Intent(context, PhonicsSubMenu.class);
                    } else {
                        intent = new Intent(context, DrillsMenu.class);

                        UnitSection unitSection = mDb.getUnitSection(mSelectedChapter, selectedSectionId, 0);
                        intent.putExtra("selected_unit_section", unitSection.getUnitSectionId());
                    }
                    intent.putExtra("selected_chapter", mSelectedChapter);
                    intent.putExtra("selected_section", selectedSectionId);
                    mFinishActivity = true;
                    startActivityForResult(intent, 0);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            button.setOnTouchListener((v, event) -> {
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
                            v.performClick();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            });
        } else {
            title.setTextColor(Color.argb(200, 75, 75, 75));
            button.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("SectionsMenu: " + requestCode + "," + resultCode);
        switch (resultCode) {
            case Globals.TO_MAIN:
                setResult(Globals.TO_MAIN);
                mFinishActivity = true;
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case Code.DRILL_SPLASH:
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
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

package classact.com.xprize.activity.menu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;

import classact.com.xprize.MainActivity;
import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.locale.Languages;

public class LanguageSelect extends AppCompatActivity {

    Activity activity;
    RelativeLayout layoutContainer;
    int mNextBgCode;
    int mNextBgResource;
    int mSelectedLanguage;

    Button selectEnglishButton;
    Button selectKiswahiliButton;

    private String[] chapterNames, chapterActivityNames, chapterActivityDrillNames;
    private Spinner chapterSpinner, chapterActivitySpinner, chapterActivityDrillSpinner;
    private ArrayAdapter<String> chapterSpinnerAdapter, chapterActivitySpinnerAdapter, chapterActivityDrillSpinnerAdapter;
    private SpinnerListener chapterSpinnerListener, chapterActivitySpinnerListener, chapterActivityDrillSpinnerListener;
    private ButtonListener updateButtonerListener;
    private TextView chapterActivityDrillTotalTextView;
    private Button updateButton;

    private DbHelper mDbHelper;
    private BookController bookController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        activity = this;
        mSelectedLanguage = 1; // English by default

        Intent intent = getIntent();
        mNextBgCode = intent.getIntExtra(Code.NEXT_BG_CODE, -1);
        mNextBgResource = intent.getIntExtra(Code.NEXT_BG_RES, -1);

        // get buttons
        selectEnglishButton = (Button) findViewById(R.id.language_select_english_button);
        selectKiswahiliButton = (Button) findViewById(R.id.language_select_kiswahili_button);

        // get my layout
        layoutContainer = (RelativeLayout) findViewById(R.id.language_select_container);

        /* NAV MENU LOGIC START */

        if (dbEstablsh()) {

            // Get unit id
            int chapterId = 0;
            int chapterActivityId = 0;
            int chapterActivityDrillId = 0;

            // Get database data
            int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
            Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

            // Set chapter id
            chapterId = unitId;

            // Init book controller
            bookController = new BookController(new Book("Smart Little Monkey")).init();

            // Chapter Spinner
            // Get chapter names
            chapterNames = bookController.getChapterNames();
            // Init chapter spinner
            chapterSpinner = new Spinner(getApplicationContext());
            // Init chapter spinner adapter
            chapterSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_nav_menu, chapterNames);
            // Set chapter spinner adapter
            chapterSpinner.setAdapter(chapterSpinnerAdapter);
            // Set chapter spinner selected item
            chapterSpinner.setSelection(chapterId, true);
            // Init chapter spinner listener
            chapterSpinnerListener = new ChapterSpinnerListener(getApplicationContext(), 0);
            // Set chapter spinner listener
            chapterSpinner.setOnItemSelectedListener(chapterSpinnerListener);
            // Add chapter spinner to layout
            layoutContainer.addView(chapterSpinner);
            // Set chapter spinner layout params
            RelativeLayout.LayoutParams chapterSpinnerLayoutParams = (RelativeLayout.LayoutParams) chapterSpinner.getLayoutParams();
            chapterSpinnerLayoutParams.leftMargin = 20;
            chapterSpinnerLayoutParams.topMargin = 20;
            chapterSpinner.setLayoutParams(chapterSpinnerLayoutParams);

            // Determine chapter activity id
            if (unitId == Globals.INTRO_ID) {
                chapterActivityDrillId = 0; // No drills
                if (u.getUnitFirstTimeMovie() == 0) {
                    chapterActivityId = 0; // Movie activity
                } else if (u.getUnitFirstTime() == 0) {
                    chapterActivityId = 1; // Tutorial activity
                }
            } else if (unitId == Globals.FINALE_ID) {
                chapterActivityId = 0; // Movie activity
                chapterActivityDrillId = 0; // No drills
            } else {
                if (u.getUnitFirstTimeMovie() == 0) {
                    chapterActivityId = 0; // Movie activity
                    chapterActivityDrillId = 0; // No drills
                } else {
                    int drillLastPlayed = u.getUnitDrillLastPlayed();
                    int nextDrill = drillLastPlayed + 1;
                    int maxDrills = u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills();
                    if (unitId == 1) {
                        maxDrills += 3; // As unit 1 only has 3 word drills
                    }
                    if (nextDrill > maxDrills && u.getUnitCompleted() == 0) {
                        chapterActivityId = 5; // Chapter end activity
                        chapterActivityDrillId = 0; // No drills
                    } else {
                        if (nextDrill < Globals.WORDS_STARTING_ID) { // +1, need to determine current (the next) drill
                            chapterActivityId = 1; // Phonics activity
                            chapterActivityDrillId = drillLastPlayed;
                        } else if (nextDrill < Globals.STORY_STARTING_ID) {
                            if (unitId == 1) { // Check unit 1 as it has 3 less word drills (though db is updated)
                                if (nextDrill + 3 >= Globals.STORY_STARTING_ID) {
                                    chapterActivityId = 3; // Simple story activity
                                    chapterActivityDrillId = 0; // No other drills
                                } else {
                                    chapterActivityId = 2; // Words activity
                                    chapterActivityDrillId = nextDrill - Globals.WORDS_STARTING_ID;
                                }
                            } else {
                                chapterActivityId = 2; // Words activity
                                chapterActivityDrillId = nextDrill - Globals.WORDS_STARTING_ID;
                            }
                        } else if (nextDrill < Globals.MATHS_STARTING_ID) {
                            chapterActivityId = 3; // Simple story activity
                            chapterActivityDrillId = nextDrill - Globals.STORY_STARTING_ID;
                        } else {
                            chapterActivityId = 4; // Maths activity
                            chapterActivityDrillId = nextDrill - Globals.MATHS_STARTING_ID;
                        }
                    }
                }
                /* int maxChapterActivityDrills = u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills();
                if (unitId == 1) {
                    maxChapterActivityDrills += 3; // Unit 1 skips 3 word drills
                }*/
            }

            // Chapter Activity Spinner
            // Get chapter activity names
            chapterActivityNames = bookController.getChapterActivityNames((String) chapterSpinner.getSelectedItem());
            // Init chapter activity spinner
            chapterActivitySpinner = new Spinner(getApplicationContext());
            // Init chapter activity spinner adapter
            chapterActivitySpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_nav_menu, chapterActivityNames);
            // Set chapter activity spinner adapter
            chapterActivitySpinner.setAdapter(chapterActivitySpinnerAdapter);
            // Set chapter activity spinner selected item
            chapterActivitySpinner.setSelection(chapterActivityId, true);
            // Init chapter activity spinner listener
            chapterActivitySpinnerListener = new ChapterActivitySpinnerListener(getApplicationContext(), 0);
            // Set chapter activity spinner listener
            chapterActivitySpinner.setOnItemSelectedListener(chapterActivitySpinnerListener);
            // Add chapter activity spinner to layout
            layoutContainer.addView(chapterActivitySpinner);
            // Set chapter activity spinner layout params
            RelativeLayout.LayoutParams chapterActivitySpinnerLayoutParams =
                    (RelativeLayout.LayoutParams) chapterActivitySpinner.getLayoutParams();
            chapterActivitySpinnerLayoutParams.leftMargin = 20;
            chapterActivitySpinnerLayoutParams.topMargin = 180;
            chapterActivitySpinner.setLayoutParams(chapterActivitySpinnerLayoutParams);

            // Chapter Activity Drill Spinner
            // Get chapter names
            chapterActivityDrillNames = bookController.getChapterActivityDrillNames(
                    (String) chapterSpinner.getSelectedItem(),
                    (String) chapterActivitySpinner.getSelectedItem());
            // Check if no chapter activity drills exist
            if (chapterActivityDrillNames.length == 0) {
                // Tweak data
                chapterActivityDrillNames = new String[1];
                chapterActivityDrillNames[0] = " ";
            }
            // Init chapter activity drill spinner
            chapterActivityDrillSpinner = new Spinner(getApplicationContext());
            // Init chapter spinner adapter
            chapterActivityDrillSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_nav_menu, chapterActivityDrillNames);
            // Set chapter activity drill spinner adapter
            chapterActivityDrillSpinner.setAdapter(chapterActivityDrillSpinnerAdapter);
            // Set chapter activity drill spinner selected item
            chapterActivityDrillSpinner.setSelection(chapterActivityDrillId, true);
            // Init chapter activity drill spinner listener
            chapterActivityDrillSpinnerListener = new ChapterActivityDrillSpinnerListener(getApplicationContext(), 0);
            // Set chapter activity drill spinner listener
            chapterActivityDrillSpinner.setOnItemSelectedListener(chapterActivityDrillSpinnerListener);
            // Check if chapter activity drill spinner is blank
            if (chapterActivityDrillNames[0].equals(" ")) {
                // Disable chapter activity drill spinner
                chapterActivityDrillSpinner.setEnabled(false);
            }

            // Init custom relative Layout for chapter activity drill views
            LinearLayout ll = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            llParams.setLayoutDirection(LinearLayout.HORIZONTAL);
            llParams.leftMargin = 20;
            llParams.topMargin = 340;
            ll.setLayoutParams(llParams);

            // Add custom layout to main layout
            layoutContainer.addView(ll);

            // Add chapter activity drill spinner to custom relative layout
            LinearLayout llLeft = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams llLeftParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llLeft.setLayoutParams(llLeftParams);
            llLeft.addView(chapterActivityDrillSpinner);

            // Chapter activity drill total text view
            // Init chapter activity drill total text view
            chapterActivityDrillTotalTextView = new TextView(getApplicationContext());
            chapterActivityDrillTotalTextView.setTextSize(50f);
            chapterActivityDrillTotalTextView.setTextColor(Color.BLACK);
            // update chapter activity drill total text view text
            updateChapterActivityDrillTotalTextView();
            // Add chapter activity drill total text view to custom layout

            LinearLayout llRight = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams llRightParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llRight.setLayoutParams(llRightParams);
            llRight.addView(chapterActivityDrillTotalTextView);

            // Add llLeft and llRight to ll
            ll.addView(llLeft);
            ll.addView(llRight);

            // Update button
            updateButton = new Button(getApplicationContext());
            updateButton.setEnabled(false); // Nothing changed yet
            updateButton.setText("Update");
            updateButton.setTextSize(50f);
            updateButtonerListener = new UpdateButtonListener(getApplicationContext());
            updateButton.setOnClickListener(updateButtonerListener);
            layoutContainer.addView(updateButton);
            RelativeLayout.LayoutParams rlUpdateButtonParams = (RelativeLayout.LayoutParams) updateButton.getLayoutParams();
            rlUpdateButtonParams.leftMargin = 20;
            rlUpdateButtonParams.topMargin = 500;
            updateButton.setLayoutParams(rlUpdateButtonParams);

            // Close db
            dbClose();
        }
        /* NAV MENU LOGIC STOP */

        // add listeners
        selectEnglishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(Languages.ENGLISH);
                fadeOutCurrentUI();
            }
        });

        selectKiswahiliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedLanguage(Languages.SWAHILI);
                fadeOutCurrentUI();
            }
        });
    }

    public void setSelectedLanguage(int language) {
        if (language == Languages.SWAHILI) {
            Globals.SELECTED_LANGUAGE = Languages.SWAHILI;
        } else {
            Globals.SELECTED_LANGUAGE = Languages.ENGLISH;
        }
        updateNextBG(language);
    }

    public void updateNextBG(int language) {

        RelativeLayout bg = (RelativeLayout) findViewById(R.id.activity_language_select);

        if (language == Languages.SWAHILI) {
            // Update selected language
            mSelectedLanguage = Languages.SWAHILI;

            // Update bg 'illusory splash' image
            switch (mNextBgCode) {
                case Code.INTRO:
                    bg.setBackgroundResource(R.drawable.tutorial_intro_bg_swahili);
                    break;
                case Code.TUTORIAL:
                    bg.setBackgroundResource(R.drawable.tutorial_bg_empty);
                    break;
                case Code.MOVIE:
                    bg.setBackgroundResource(R.drawable.chapter_bg);
                    break;
                case Code.PHONICS_SPLASH:
                    bg.setBackgroundResource(R.drawable.phonics_link_bg);
                    break;
                case Code.WORDS_SPLASH:
                    bg.setBackgroundResource(R.drawable.sw_words_link_bg);
                    break;
                case Code.STORY_SPLASH:
                    bg.setBackgroundResource(R.drawable.sw_story_link_bg);
                    break;
                case Code.MATHS_SPLASH:
                    bg.setBackgroundResource(R.drawable.maths_link_bg);
                    break;
                case Code.CHAPTER_END_SPLASH:
                    if (mNextBgResource > 0) {
                        bg.setBackgroundResource(mNextBgResource);
                    } else {
                        bg.setBackgroundResource(R.drawable.language_select_bg);
                    }
                    break;
                default:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
            }
        } else {
            // Update selected language
            mSelectedLanguage = Languages.ENGLISH;

            // Update bg 'illusory splash' image
            switch (mNextBgCode) {
                case Code.INTRO:
                    bg.setBackgroundResource(R.drawable.tutorial_intro_bg_english);
                    break;
                case Code.TUTORIAL:
                    bg.setBackgroundResource(R.drawable.tutorial_bg_empty);
                    break;
                case Code.MOVIE:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
                case Code.PHONICS_SPLASH:
                    bg.setBackgroundResource(R.drawable.phonics_link_bg);
                    break;
                case Code.WORDS_SPLASH:
                    bg.setBackgroundResource(R.drawable.en_words_link_bg);
                    break;
                case Code.STORY_SPLASH:
                    bg.setBackgroundResource(R.drawable.en_story_link_bg);
                    break;
                case Code.MATHS_SPLASH:
                    bg.setBackgroundResource(R.drawable.maths_link_bg);
                    break;
                case Code.CHAPTER_END_SPLASH:
                    if (mNextBgResource > 0) {
                        bg.setBackgroundResource(mNextBgResource);
                    } else {
                        bg.setBackgroundResource(R.drawable.language_select_bg);
                    }
                    break;
                default:
                    bg.setBackgroundResource(R.drawable.language_select_bg);
                    break;
            }
        }
    }

    public void fadeOutCurrentUI() {
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                finishIntent();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        fadeOut.setFillAfter(true);
        layoutContainer.startAnimation(fadeOut);
    }

    public void finishIntent() {
        // Debug
        System.out.println("LanguageSelect.finishIntent > Debug: Executing Intent finish logic");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Code.SELECT_LANG, mSelectedLanguage);
        setResult(Code.LANG, intent);
        finishAfterTransition();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /* NAV MENU LOGIC START */

    /**
     * DB CONNECTION
     */

    protected boolean dbEstablsh() {
        try {
            // Initialize DbHelper
            mDbHelper = DbHelper.getDbHelper(getApplicationContext());

            // Create database (or connect to existing)
            mDbHelper.createDatabase();

            // Test opening database
            mDbHelper.openDatabase();

            // All good
            return true;

            // Otherwise
        } catch (IOException ioex) {
            System.err.println("MainActivity.dbEstablish > IOException: " + ioex.getMessage());
        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.dbEstablish > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.dbEstablish > Exception: " + ex.getMessage());
        }
        return false;
    }

    protected void dbClose() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     * UPDATE FUNCTIONS
     */

    private void updateChapterActivitySpinner(Context context, String[] data) {
        chapterActivityNames = data;
        chapterActivitySpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_nav_menu, chapterActivityNames);
        chapterActivitySpinner.setAdapter(chapterActivitySpinnerAdapter);
    }

    private void updateChapterActivityDrillSpinner(Context context, String[] data) {
        chapterActivityDrillNames = data;
        if (chapterActivityDrillNames.length == 0) {
            chapterActivityDrillNames = new String[1];
            chapterActivityDrillNames[0] = " ";
        }
        chapterActivityDrillSpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_nav_menu, chapterActivityDrillNames);
        chapterActivityDrillSpinner.setAdapter(chapterActivityDrillSpinnerAdapter);
        if (chapterActivityDrillNames[0].equals(" ")) {
            chapterActivityDrillSpinner.setEnabled(false);
        } else {
            chapterActivityDrillSpinner.setEnabled(true);
        }
        updateChapterActivityDrillTotalTextView();
    }

    private void updateChapterActivityDrillTotalTextView() {
        String chapterActivityDrillTotalTextViewText = " / ";
        if (chapterActivityDrillNames[0].equals(" ")) {
            chapterActivityDrillTotalTextViewText += "0";
            chapterActivityDrillTotalTextView.setTextColor(Color.argb(55, 0, 0, 0));
        } else {
            chapterActivityDrillTotalTextViewText += "" + chapterActivityDrillNames.length;
            chapterActivityDrillTotalTextView.setTextColor(Color.argb(200, 0, 0, 0));
        }
        chapterActivityDrillTotalTextView.setText(chapterActivityDrillTotalTextViewText);
    }

    private boolean spinnerInputsHaveChanged() {
        return (chapterSpinnerListener.getFirstPosition() != chapterSpinnerListener.getPreviousPosition() ||
                chapterActivitySpinnerListener.getFirstPosition() != chapterActivitySpinnerListener.getPreviousPosition() ||
                chapterActivityDrillSpinnerListener.getFirstPosition() != chapterActivityDrillSpinnerListener.getPreviousPosition());
    }

    /**
     * CUSTOM LISTENER CLASSES
     */

    private class ChapterSpinnerListener extends SpinnerListener {

        public ChapterSpinnerListener(Context context, int position) {
            super(context, position);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Check if it's a different position
            if (position != previousPosition) {
                // Get new chapter activity names
                String[] newChapterActivityNames = bookController.getChapterActivityNames(position);
                // Check if update for chapter activities must happen:
                // #1 If not same length AND
                if (!(newChapterActivityNames.length == chapterActivityNames.length &&
                        // #2 If values don't correspond (Note that DeMorgan's law is being used
                        newChapterActivityNames[chapterActivitySpinner.getSelectedItemPosition()]
                                .equals(chapterActivitySpinner.getSelectedItem()))) {
                    // Call update method
                    updateChapterActivitySpinner(context, newChapterActivityNames);
                }
                // Next, check if chapter activity drill spinner must be updated
                String[] newChapterActivityDrillNames = bookController.getChapterActivityDrillNames(
                        chapterSpinner.getSelectedItemPosition(),
                        chapterActivitySpinner.getSelectedItemPosition());
                // Check if update for chapter activity drills must happen:
                // #1 If not same length AND
                if (!(newChapterActivityDrillNames.length == chapterActivityDrillNames.length &&
                        // #2 If values don't correspond (Note that DeMorgan's law is being used
                        newChapterActivityDrillNames[chapterActivityDrillSpinner.getSelectedItemPosition()]
                                .equals(chapterActivityDrillSpinner.getSelectedItem()))) {
                    // Call update method
                    updateChapterActivityDrillSpinner(context, newChapterActivityDrillNames);
                }
                // Update previous position
                previousPosition = position;
            }
            // Enable or disable button accordingly
            updateButton.setEnabled(spinnerInputsHaveChanged());
        }
    }

    private class ChapterActivitySpinnerListener extends SpinnerListener {

        public ChapterActivitySpinnerListener(Context context, int position) {
            super(context, position);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Check if it's a different position
            if (position != previousPosition) {
                // Get new chapter activity drill names
                String[] newChapterActivityDrillNames = bookController.getChapterActivityDrillNames(
                        chapterSpinner.getSelectedItemPosition(), position);
                // Call update method (no need to validate)
                updateChapterActivityDrillSpinner(context, newChapterActivityDrillNames);
                // Update previous position
                previousPosition = position;
            }
            // Enable or disable button accordingly
            updateButton.setEnabled(spinnerInputsHaveChanged());
        }
    }

    private class ChapterActivityDrillSpinnerListener extends SpinnerListener {

        public ChapterActivityDrillSpinnerListener(Context context, int position) {
            super(context, position);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Check if it's a different position
            if (position != previousPosition) {
                // Update previous position
                previousPosition = position;
            }
            // Enable or disable button accordingly
            updateButton.setEnabled(spinnerInputsHaveChanged());
        }
    }

    public class UpdateButtonListener extends ButtonListener {

        public UpdateButtonListener(Context context) {
            super(context);
        }

        @Override
        public void onClick(View view) {
            if (spinnerInputsHaveChanged()) {

                // Disable button
                updateButton.setEnabled(false);

                // Update database
                if (dbEstablsh()) {
                    int chapterId = chapterSpinner.getSelectedItemPosition();
                    int chapterActivityId = chapterActivitySpinner.getSelectedItemPosition();
                    int chapterActivityDrillId = chapterActivityDrillSpinner.getSelectedItemPosition();

                    // Get unit
                    Unit u = UnitHelper.getUnitInfo(mDbHelper.getWritableDatabase(), chapterId);

                    if (chapterId == Globals.INTRO_ID) {
                        if (chapterActivityId == 0) {

                        } else if (chapterActivityId == 1) {

                        }
                    } else if (chapterId == Globals.FINALE_ID) {

                    } else {
                        if (chapterActivityId == 0) { // Movie

                        } else if (chapterActivityId == 5) { // Chapter End

                        } else if (chapterActivityId == 1) { // Phonics

                        } else if (chapterActivityId == 2) { // Words

                        } else if (chapterActivityId == 3) { // Simple Story

                        } else if (chapterActivityId == 4) { // Math

                        } else { // Chapter End

                        }
                    }

                    // Update unit model's data
                    u.setUnitUnlocked(1);
                    u.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date()));
                    u.setUnitInProgress(0);
                    u.setUnitSubIDInProgress(0);
                    u.setUnitCompleted(1);
                    u.setUnitDrillLastPlayed(0);
                    u.setUnitFirstTime(1);
                    u.setUnitFirstTimeMovie(1);

                    // Close database
                    dbClose();
                }

                // Update 'first position' for spinner listeners
                chapterSpinnerListener.setFirstPosition(chapterSpinnerListener.getPreviousPosition());
                chapterActivitySpinnerListener.setFirstPosition(chapterActivitySpinnerListener.getPreviousPosition());
                chapterActivityDrillSpinnerListener.setFirstPosition(chapterActivityDrillSpinnerListener.getPreviousPosition());
            }
        }
    }

    /**
     * ABSTRACT CLASSES
     */

    private abstract class SpinnerListener implements AdapterView.OnItemSelectedListener {

        protected Context context;
        protected int previousPosition;
        protected int firstPosition;

        protected SpinnerListener(Context context, int position) {
            this.context = context;
            this.previousPosition = position;
            this.firstPosition = position;
        }

        @Override
        public abstract void onItemSelected(AdapterView<?> parent, View view, int position, long id);

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

        public void setFirstPosition(int firstPosition) {
            this.firstPosition = firstPosition;
        }

        public int getFirstPosition() {
            return firstPosition;
        }

        public int getPreviousPosition() {
            return previousPosition;
        }
    }

    private abstract class ButtonListener implements Button.OnClickListener {

        protected Context context;

        public ButtonListener(Context context) {
            this.context = context;
        }

        @Override
        public abstract void onClick(View view);
    }

    /* NAV MENU LOGIC STOP */
}
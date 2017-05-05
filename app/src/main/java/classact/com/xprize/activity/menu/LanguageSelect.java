package classact.com.xprize.activity.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import classact.com.xprize.MainActivity;
import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.locale.Languages;

public class LanguageSelect extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Activity activity;
    RelativeLayout layoutContainer;
    int mNextBgCode;
    int mNextBgResource;
    int mSelectedLanguage;

    Button selectEnglishButton;
    Button selectKiswahiliButton;

    private String[] stageData, activityData, drillNoData;

    private Spinner stageSpinner;
    private Spinner activitySpinner;
    private Spinner drillNoSpinner;
    private Button updateButton;

    private DbHelper mDbHelper;

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

        try {
            mDbHelper = DbHelper.getDbHelper(getApplicationContext());
            mDbHelper.createDatabase();
            mDbHelper.openDatabase();

            int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
            Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

            // Stage Spinner

            // Set stages
            String[] stages = {"Intro", "Chapter 1", "Chapter 2", "Chapter 3",
                    "Chapter 4", "Chapter 5", "Chapter 6", "Chapter 7", "Chapter 8", "Chapter 9",
                    "Chapter 10", "Chapter 11", "Chapter 12", "Chapter 13", "Chapter 14", "Chapter 15",
                    "Chapter 16", "Chapter 17", "Chapter 18", "Chapter 19", "Chapter 20", "Finale"};
            stageData = stages;

            // Setup stage spinner
            stageSpinner = new Spinner(getApplicationContext());
            ArrayAdapter<String> stagesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_nav_menu, stageData);
            stageSpinner.setAdapter(stagesAdapter);

            // Set stage spinner selected item
            stageSpinner.setSelection(unitId, true);

            // Add listener to stage spinner
            stageSpinner.setOnItemSelectedListener(this);

            // Add stage spinner to layout container
            layoutContainer.addView(stageSpinner);

            // Set stage spinner layout params
            RelativeLayout.LayoutParams stageSpinnerLayoutParams = (RelativeLayout.LayoutParams) stageSpinner.getLayoutParams();
            stageSpinnerLayoutParams.leftMargin = 20;
            stageSpinnerLayoutParams.topMargin = 20;
            stageSpinner.setLayoutParams(stageSpinnerLayoutParams);

            // Activity Spinner

            // Determine activities
            if (unitId == Globals.INTRO_ID) {
                String[] activities = {"Movie", "Tutorial", " "};
                activityData = activities;
            } else if (unitId < Globals.FINALE_ID) {
                String[] activities = {
                        "Movie",
                        "Phonics Splash", "Phonics",
                        "Words Splash", "Words",
                        "Simple Story Splash", "Simple Story",
                        "Math Splash", "Math",
                        "Chapter End",
                        " "
                };
                activityData = activities;
            } else {
                String[] activities = {"Movie", " "};
                activityData = activities;
            }

            // Setup activity spinner
            activitySpinner = new Spinner(getApplicationContext());
            ArrayAdapter<String> activitiesAdapter = new ArrayAdapter<>(this, R.layout.spinner_nav_menu, activityData);
            activitySpinner.setAdapter(activitiesAdapter);

            // Set activity spinner selected item
            activitySpinner.setSelection(0, true);

            int drillLastPlayed = u.getUnitDrillLastPlayed();
            int sumOfDrillsPlayed = 0;

            // Add listener to activity spinner
            if (unitId == Globals.INTRO_ID) {
                // Check if Movie | Tutorial
                if (u.getUnitFirstTimeMovie() == 0) {
                    // Movie time
                    activitySpinner.setSelection(0, true);
                } else if (u.getUnitFirstTime() == 0) {
                    // Tutorial time
                    activitySpinner.setSelection(1, true);
                } else {
                    // Err ... go to next
                    activitySpinner.setSelection(2, true);
                }
            } else if (unitId < Globals.FINALE_ID) {
                if (u.getUnitFirstTimeMovie() == 0) {
                    // Movie time
                    activitySpinner.setSelection(0, true);
                } else if (u.getUnitFirstTime() == 0) {
                    // Phonics Splash time
                    activitySpinner.setSelection(1, true);
                } else {
                    // Check if splash again
                    if ((drillLastPlayed + 1) < Globals.WORDS_STARTING_ID) {
                        // Words Splash time
                        activitySpinner.setSelection(3, true);
                    } else if ((drillLastPlayed + 1) < Globals.STORY_STARTING_ID) {
                        // Simple Story Splash time
                        activitySpinner.setSelection(5, true);
                    } else if ((drillLastPlayed + 1) < Globals.MATHS_STARTING_ID) {
                        // Maths Splash time
                        activitySpinner.setSelection(7, true);
                    } else {
                        // Get sum of drills last played
                        if (unitId == 1) {
                            sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills() + 3); // Add 3 word drills that aren't played
                        } else {
                            sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills());
                        }
                        // Check if chapter end | within chapter
                        if (u.getUnitCompleted() == 0) {
                            // Check if chapter end
                            if (drillLastPlayed > sumOfDrillsPlayed) {
                                // It's chapter end time
                                activitySpinner.setSelection(9, true);
                            } else {
                                // It's still within chapter, check which activity
                                if (drillLastPlayed < Globals.WORDS_STARTING_ID) {
                                    // Phonics time
                                    activitySpinner.setSelection(2, true);
                                } else if (drillLastPlayed < Globals.STORY_STARTING_ID) {
                                    // Words time
                                    activitySpinner.setSelection(4, true);
                                } else if (drillLastPlayed < Globals.MATHS_STARTING_ID) {
                                    // Simple story time
                                    activitySpinner.setSelection(6, true);
                                } else {
                                    // Maths time
                                    activitySpinner.setSelection(8, true);
                                }
                            }
                        } else {
                            // Err ... go to next
                            activitySpinner.setSelection(10, true);
                        }
                    }
                }
            } else {
                activitySpinner.setSelection(0, true);
            }

            // Add activity spinner to layout container
            layoutContainer.addView(activitySpinner);

            // Set activity spinner layout params
            RelativeLayout.LayoutParams activitySpinnerLayoutParams = (RelativeLayout.LayoutParams) activitySpinner.getLayoutParams();
            activitySpinnerLayoutParams.leftMargin = 20;
            activitySpinnerLayoutParams.topMargin = 180;
            activitySpinner.setLayoutParams(activitySpinnerLayoutParams);

            // Drill No. Spinner

            // Determine drill nos
            // Check drill type
            String[] drillNos = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            drillNoSpinner = new Spinner(getApplicationContext());
            ArrayAdapter<String> drillNosAdapter = new ArrayAdapter<String>(this, R.layout.spinner_nav_menu, drillNos);
            drillNoSpinner.setAdapter(drillNosAdapter);
            drillNoSpinner.setSelection(0, true);
            drillNoSpinner.setOnItemSelectedListener(this);

            layoutContainer.addView(drillNoSpinner);

            RelativeLayout.LayoutParams drillNoSpinnerLayoutParams = (RelativeLayout.LayoutParams) drillNoSpinner.getLayoutParams();
            drillNoSpinnerLayoutParams.leftMargin = 20;
            drillNoSpinnerLayoutParams.topMargin = 340;
            drillNoSpinner.setLayoutParams(drillNoSpinnerLayoutParams);

            // Update Button
            updateButton = new Button(getApplicationContext());
            updateButton.setText("Update");

            layoutContainer.addView(updateButton);

            RelativeLayout.LayoutParams updateButtonLayoutParams = (RelativeLayout.LayoutParams) updateButton.getLayoutParams();
            updateButtonLayoutParams.leftMargin = 20;
            updateButtonLayoutParams.topMargin = 500;
            updateButton.setLayoutParams(updateButtonLayoutParams);

        } catch (Exception ex) {
            System.err.println("=============");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.err.println("=============");
        } finally {
            if (mDbHelper != null) {
                mDbHelper.close();
            }
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

    /* NAV MENU LOGIC START */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /* NAV MENU LOGIC STOP */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
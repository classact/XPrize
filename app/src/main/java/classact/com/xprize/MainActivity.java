package classact.com.xprize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Date;

import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.activity.link.LevelCompleteLink;
import classact.com.xprize.activity.link.MathsLink;
import classact.com.xprize.activity.link.PhonicsLink;
import classact.com.xprize.activity.link.StoryLink;
import classact.com.xprize.activity.link.WordsLink;
import classact.com.xprize.activity.menu.MusicMenu;
import classact.com.xprize.activity.menu.StarsMenu;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.activity.movie.Movie;
import classact.com.xprize.activity.movie.MoviePausable;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.model.Drill;
import classact.com.xprize.database.model.DrillType;
import classact.com.xprize.database.model.Section;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.locale.Languages;
import classact.com.xprize.utils.ResourceDecoder;

public class MainActivity extends AppCompatActivity {

    private final boolean ALLOW_DB_RECOPY = false;

    // Database hack related
    private final boolean HACK_NEXT_UNIT = false;
    private final int HACK_UNIT_ID = 1;
    private final int HACK_UNIT_SUB_ID_IN_PROGRESS = 0;
    private final int HACK_DRILL_LAST_PLAYED = 0;
    private final int HACK_UNIT_FIRST_TIME = 0;
    private final int HACK_UNIT_FIRST_TIME_MOVIE = 0;

    private boolean mInitialized;
    private DbHelper mDbHelper;

    private ConstraintLayout mRootView;

    private boolean mPhonicsStarted;
    private boolean mWordsStarted;
    private boolean mBooksStarted;
    private boolean mMathsStarted;

    private ImageButton mReadButton;
    private ImageButton mMusicButton;
    private ImageButton mStarsButton;
    private AudioManager mAudioManager;
    private DatabaseController mDb;
    private boolean mNewActivity;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mNewActivity = false;

        mRootView = (ConstraintLayout) findViewById(R.id.activity_main);

        // Read Button
        mReadButton = (ImageButton) findViewById(R.id.read_button);
        mReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextDrill();
            }
        });

        // Stars Button
        mStarsButton = (ImageButton) findViewById(R.id.stars_button);
        mStarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewActivity = true;
                Intent intent = new Intent(THIS, StarsMenu.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Music Button
        mMusicButton = (ImageButton) findViewById(R.id.music_button);
        mMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewActivity = true;
                Intent intent = new Intent(THIS, MusicMenu.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Globals.PLAY_BACKGROUND_MUSIC(this);

        // System.out.println("!!!!!!!!!!!!!!!!!!!! " + Build.VERSION.SECURITY_PATCH + ", " + Build.VERSION.RELEASE);

        mPhonicsStarted = false;
        mWordsStarted = false;
        mBooksStarted = false;
        mMathsStarted = false;

        /* mInitialized = false;

        Intent intent = new Intent(this, LanguageSelect.class);

        // Determine next splash bg
        int[] nextSplashBg = determineNextSplashBg();

        if (nextSplashBg != null) {
            if (nextSplashBg[0] != -1) {
                intent.putExtra(Code.NEXT_BG_CODE, nextSplashBg[0]);
            }
            if (nextSplashBg[1] != -1) {
                intent.putExtra(Code.NEXT_BG_RES, nextSplashBg[1]);
            }
        }

        startActivityForResult(intent, Code.LANG);
        overridePendingTransition(0, android.R.anim.fade_out); */
    }

    public void playNextDrill() {

        // Setup Database Controller
        mDb = DatabaseController.getInstance(THIS, Languages.ENGLISH);

        // Get unit section drill data
        UnitSectionDrill unitSectionDrill = mDb.getUnitSectionDrillInProgress();

        try {
            if (dbEstablsh(false)) {
                Object[] objectArray = DrillFetcher.fetch(THIS, mDbHelper, Languages.ENGLISH, unitSectionDrill);
                Intent intent = (Intent) objectArray[0];
                int resultCode = (int) objectArray[1];

                Globals.STOP_BACKGROUND_MUSIC(THIS);

                startActivityForResult(intent, resultCode);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            dbClose();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("MainActivity: " + requestCode + "," + resultCode);

        if (resultCode != Globals.TO_MAIN) {
            switch (requestCode) {
                case Code.DRILL_SPLASH:
                case Code.INTRO:
                case Code.TUTORIAL:
                case Code.MOVIE:
                case Code.RUN_DRILL:
                case Code.CHAPTER_END:
                case Code.FINALE:
                    processActivityResult(requestCode);
                    break;
                default:
                    break;
            }
            switch (resultCode) {
                case Code.DRILL_SPLASH:
                case Code.INTRO:
                case Code.TUTORIAL:
                case Code.MOVIE:
                case Code.RUN_DRILL:
                case Code.CHAPTER_END:
                case Code.FINALE:
                    processActivityResult(resultCode);
                    break;
                default:
                    break;
            }
        } else {
            DrillFetcher.mPhonicsStarted = false;
            DrillFetcher.mWordsStarted = false;
            DrillFetcher.mBooksStarted = false;
            DrillFetcher.mMathsStarted = false;
        }
    }

    protected void processActivityResult(int code) {
        switch (code) {
            case Code.DRILL_SPLASH:

                // Setup Database Controller
                mDb = DatabaseController.getInstance(THIS, Languages.ENGLISH);

                // Get unit section drill data
                UnitSectionDrill nextUnitSectionDrill = mDb.getUnitSectionDrillInProgress();

                try {
                    if (dbEstablsh(false)) {
                        Object[] objectArray = DrillFetcher.fetch(THIS, mDbHelper, Languages.ENGLISH, nextUnitSectionDrill);
                        Intent intent = (Intent) objectArray[0];
                        int resultCode = (int) objectArray[1];

                        Globals.STOP_BACKGROUND_MUSIC(THIS);

                        startActivityForResult(intent, resultCode);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    dbClose();
                }
                break;
            case Code.INTRO:
            case Code.TUTORIAL:
            case Code.MOVIE:
            case Code.RUN_DRILL:
            case Code.CHAPTER_END:
            case Code.FINALE:

                // Setup Database Controller
                mDb = DatabaseController.getInstance(THIS, Languages.ENGLISH);

                // Get unit section drill data
                nextUnitSectionDrill = mDb.moveToNextUnitSectionDrill();

                try {
                    if (dbEstablsh(false)) {
                        Object[] objectArray = DrillFetcher.fetch(THIS, mDbHelper, Languages.ENGLISH, nextUnitSectionDrill);
                        Intent intent = (Intent) objectArray[0];
                        int resultCode = (int) objectArray[1];

                        Globals.STOP_BACKGROUND_MUSIC(THIS);

                        startActivityForResult(intent, resultCode);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    dbClose();
                }
                break;
            default:
                break;
        }
    }

    public boolean determineNextItem() {

        // Initialize success boolean to see if everything is A-OK
        boolean success = false;
        boolean isFinale = false;

        // Declare intent
        Intent intent = null;
        int resultCode = 0;

        try {
            // Establish database connectivity
            if (!dbEstablsh(!mInitialized)) {
                throw new Exception("Db Connection unsuccessful");
            }
            System.out.println("MainActivity.determineNextItem > Debug: Db Connection successful");

            /* NORMAL LOGIC PAUSE */

            // :: HACK :: START for next unit to be played - development purposes
            if (!mInitialized && HACK_NEXT_UNIT) {
                System.out.println("=====================================================================");
                System.out.println(":: HACK :: MainActivity.determineNextItem > Debug: Hack section start");

                // Declare re-usable variables
                Unit u; // Unit to be updated
                int result; // Result (gives unitId) after database update

                System.out.println("---------------------------------------------------------------------");

                // Loop through each unit, completing them, until we get to unitToBePlayed (hacked)
                for (int i = 0; i < HACK_UNIT_ID; i++) {

                    // Get unit
                    u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), i);

                    // Update unit model's data
                    u.setUnitUnlocked(1);
                    u.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date()));
                    u.setUnitInProgress(0);
                    u.setUnitSubIDInProgress(0);
                    u.setUnitCompleted(1);
                    u.setUnitDrillLastPlayed(0);
                    u.setUnitFirstTime(1);
                    u.setUnitFirstTimeMovie(1);

                    // Update unit in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);

                    // Confirm result
                    System.out.println(":: HACK :: Unit #" + result + " completed via hack");
                }

                System.out.println("---------------------------------------------------------------------");

                // Now establish the next unit + drill to be played
                u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), HACK_UNIT_ID);

                // Update unit model's data
                u.setUnitUnlocked(1);
                u.setUnitDateLastPlayed("0");
                u.setUnitInProgress(1);
                u.setUnitSubIDInProgress(HACK_UNIT_SUB_ID_IN_PROGRESS);
                u.setUnitCompleted(0);
                u.setUnitDrillLastPlayed(HACK_DRILL_LAST_PLAYED);
                u.setUnitFirstTime(HACK_UNIT_FIRST_TIME);
                u.setUnitFirstTimeMovie(HACK_UNIT_FIRST_TIME_MOVIE);

                // Update unit in database
                result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);

                // Confirm result
                System.out.println(":: HACK :: == Unit #" + result + ", " +
                        "Drill # " + (HACK_DRILL_LAST_PLAYED + 1) + " hacked and ready ==");

                System.out.println(":: HACK :: MainActivity.determineNextItem > Debug: Hack section end");
                System.out.println("=====================================================================");
            } // :: HACK :: END

            /* NORMAL LOGIC RESUME */

            // Get current unit
            int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());

            // Finale override hack on unit 3
            /* if (unitId == 4) {

                // Just go to finale. We still need to figure out how to get 20 chapter
                // videos into the sparse image ... for now, only till chapter 2
                success = true;
                throw new Exception(":: HACK :: MainActivity.determineNextItem > Debug: " +
                        "Unit id is (" + unitId + "), auto moving to finale, as we don't have enough 'video space' (yet)");
            }*/

            Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

            // Debug
            System.out.println("MainActivity.determineNextItem > Debug: UnitId = " + unitId);

            if (unitId == Globals.INTRO_ID) {
            /* INTRO LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Intro Section");

                if (u.getUnitFirstTimeMovie() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Intro Movie");

                    // Play intro movie
                    intent = new Intent(THIS, Movie.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.NEXT_BG_CODE, Code.INTRO);
                    resultCode = Code.INTRO;

                } else if (u.getUnitFirstTime() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Intro Tutorial");

                    // Start tutorial
                    intent = new Intent(THIS, Tutorial.class);
                    resultCode = Code.TUTORIAL;
                }
            } else if (unitId < Globals.FINALE_ID) {
            /* CHAPTER LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Chapter Section");

                if (u.getUnitFirstTimeMovie() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Chapter Movie");

                    // Play chapter movie
                    intent = new Intent(this, MoviePausable.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.SHOW_MV_BUTTONS, true);
                    intent.putExtra(Code.NEXT_BG_CODE, Code.MOVIE);
                    resultCode = Code.MOVIE;

                } /* else if (u.getUnitFirstTime() == 0) {
                TUTORIAL LOGIC FOR THE FUTURE? Perhaps
            } */ else {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Drill section");

                    // Determine if drill splash should be displayed
                    // Splash is displayed when
                    // #1. restarting the application (onResume() method))
                    // #2. the beginning of each drill type section (ie. 'Phonics section', 'Words section')

                    // Determine type of splash by comparing drill last played
                    int drillLastPlayed = u.getUnitDrillLastPlayed();
                    int sumOfDrillsPlayed = 0;
                    if (unitId == 1) {
                        sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills() + 3); // Add 3 word drills that aren't played
                    } else {
                        sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills());
                    }

                    System.out.println("------------- Unit First Time? " + u.getUnitFirstTime());
                    if (u.getUnitFirstTime() == 0) {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Show drill splash");

                        // Determine drill splash code
                        Class drillSplashActivity;

                        // Phonics Splash
                        if (drillLastPlayed + 1 < Globals.WORDS_STARTING_ID) {
                            drillSplashActivity = PhonicsLink.class;
                        // Words Splash
                        } else if (drillLastPlayed + 1 < Globals.STORY_STARTING_ID) {
                            drillSplashActivity = WordsLink.class;
                        // Story Splash
                        } else if (drillLastPlayed + 1 < Globals.MATHS_STARTING_ID) {
                            drillSplashActivity = StoryLink.class;
                        // Maths Splash
                        } else {
                            drillSplashActivity = MathsLink.class;
                        }

                        // Show drill splash
                        intent = new Intent(this, drillSplashActivity);
                        resultCode = Code.DRILL_SPLASH;

                        // Determine if the chapter ending splash should be played
                        // Ending under the following scenarios:
                        // * Unit has not been completed
                        // * Last drill has been played
                        //   - Determine this by checking <drill last played> is equal to <sum of number of drills (language, maths) in unit >
                    } else if (u.getUnitCompleted() == 0 && drillLastPlayed > sumOfDrillsPlayed) {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Chapter End (" + drillLastPlayed + "/" + sumOfDrillsPlayed + ")");

                        // Show ending splash
                        intent = new Intent(this, LevelCompleteLink.class);
                        intent.putExtra(Code.RES_NAME, "level" + unitId);
                        intent.putExtra(Code.NEXT_BG_RES, "star_level_" + unitId);
                        resultCode = Code.CHAPTER_END;

                        // Let's roll with the drill
                    } else {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Determine drill");

                        int drillId = drillLastPlayed + 1; // next item to play

                        // Unit 1 skip Drill #13, 14, 15 hack
                        if (unitId == 1 && drillId >= 13 && drillId <= 15) {
                            drillId = 16;
                        }

                        int languageId = Globals.SELECTED_LANGUAGE;

                        /* Sub id logic START */
                        int subId = 0;
                        // Check drill type
                        if (drillId < Globals.WORDS_STARTING_ID) {
                            // It's a phonics drill
                            // Get sub id from database
                            subId = u.getUnitSubIDInProgress();
                            // Check sub id is 0 (no subs have been played yet)
                            if (subId == 0) {
                                // set the sub id to 1 programmatically
                                subId = 1;
                                // update sub id value in database model
                                u.setUnitSubIDInProgress(subId);
                                // Update unit in database
                                int result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                                // Check if database update unsuccessful
                                if (result == 0) {
                                    // Print the issue out ... but don't throw exception ... lol
                                    System.out.println("Could not update sub id :-( ...");
                                }
                            }
                            // Print out sub id
                            System.out.println("Next item's sub id is: " + subId);
                        } else if (drillId < Globals.STORY_STARTING_ID) {
                            // It's a word drill
                            subId = 0;
                        }
                        // NOTE: The 'next sub id' logic is determined by onActivityResult method.
                        /* Sub id logic END */

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Running drill for (" + unitId + ", " + drillId + ", " + languageId + ", " + subId + ")");

                        intent = DrillFetcher.fetch(getApplicationContext(), mDbHelper, unitId, drillId, languageId, subId);
                        resultCode = Code.RUN_DRILL;
                    }
                }
            } else if (unitId == Globals.FINALE_ID) {
            /* ENDING LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Finale Section ");

                if (u.getUnitFirstTimeMovie() == 0) {
                    // Play finale movie
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.NEXT_BG_CODE, Code.FINALE);
                    resultCode = Code.FINALE;
                    isFinale = true;
                }

            } else {
            /* "Whoopsie!" LOGIC */
                throw new Exception("... And why am I here?");
            }

            // Set initialized flag to true
            if (!mInitialized) {
                System.out.println("mInitialized!");
                mInitialized = true;
            }

            // All happy
            success = true;

        // Otherwise
        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.determineNextItem > Exception: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.determineNextItem > Exception: " + ex.getMessage());
        } finally {

            // Close database connection
            if (mDbHelper != null) {
                mDbHelper.close();
                mDbHelper = null;
            }

            // Launch new intent if success
            if (success) {
                if (intent != null) {
                    startActivityForResult(intent, resultCode);
                    if (isFinale) {
                        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out);
                    } else {
                        overridePendingTransition(0, android.R.anim.fade_out);
                    }
                } else {
                    // Debug error: force finale
                    System.err.println("MainActivity.determineNextItem > Error: Forcing finale");


                    // Force play finale movie ... for now
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, "finale_movie");
                    intent.putExtra(Code.NEXT_BG_CODE, Code.FINALE);
                    resultCode = Code.FINALE;
                    startActivityForResult(intent, resultCode);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }
        return success;
    }

    /**
     * Exists to reset splash
     */
    public void resetUnitFirstTIme() {
        try {
            // Establish database connectivity
            if (!dbEstablsh(!mInitialized)) {
                throw new Exception("Db Connection unsuccessful");
            }
            System.out.println("MainActivity.onDestroy > Debug: Db Connection successful");

            // Determine current unitId
            int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());

            if (unitId > Globals.INTRO_ID && unitId < Globals.FINALE_ID) {

                // Get unit u
                Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

                // Update unit u model
                u.setUnitFirstTime(0); // Reset all splash

                // Update unit u in database
                int result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                System.out.println("MainActivity.onStop - Unit u (" + u.getUnitId() + ") successfull updated in database");
            }

        } catch (SQLiteException sqlex) {
            System.out.println("MainActivity.onStop >  SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("MainActivity.onStop >  Exception: " + ex.getMessage());
        } finally {
            // Close database connection
            if (mDbHelper != null) {
                mDbHelper.close();
                mDbHelper = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // resetUnitFirstTIme();
    }

    /**
     * On Activity Result
     * Responsible for database updates after receiving response
     * param requestCode
     * param resultCode
     * param data
     */
    //// @Override
    /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Debug
        System.out.println("MainActivity.onActivityResult > Debug: Request (" + requestCode + ") and Result (" + resultCode + ")");
        System.out.println("-----------------------------------------------------------------------------");

        if (resultCode == Code.EXIT) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {

            // Flag if everything processed 100%
            boolean success = false;
            boolean finaleComplete = false;
            boolean showNavMenu = false;

            //// Resolve request completion
            //// ==========================
            //// - requestCode = finished activity's type (ie. Language-select, movie, drill)
            //// - resultCode = unitId of finished activity
            ////
            try {
                // Establish database connectivity
                if (!dbEstablsh(!mInitialized)) {
                    throw new Exception("Db Connection unsuccessful");
                }
                System.out.println("MainActivity.onActivityResult > Debug: Db Connection successful");

                // Get current unit
                int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
                Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);
                Unit u2; // next unit
                int result;

                if (resultCode == Code.NAV_MENU) {
                    showNavMenu = true;
                } else {
                    switch (requestCode) {
                        case Code.LANG:
                            // Get selected language
                            int selectedLanguage = data.getIntExtra(Code.SELECT_LANG, 1);

                            // Update Globals (if not already)
                            Globals.SELECTED_LANGUAGE = selectedLanguage;

                            // Update database
                            // TO DO
                            break;

                        case Code.INTRO:
                            // Validate that current unit vs request code
                            if (unitId != Globals.INTRO_ID) {
                                throw new Exception("Request Code (" + requestCode + ") - Intro request code detected, but unitId (" + unitId + ") does not match that");
                            }

                            // Update unit u model
                            u.setUnitFirstTimeMovie(1); // Intro/Chapter/Ending Movie has been watched

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.TUTORIAL:
                            // Validate that current unit vs request code
                            if (unitId != Globals.INTRO_ID) {
                                throw new Exception("Request Code (" + requestCode + ") - Intro request code detected, but unitId (" + unitId + ") does not match that");
                            }

                            // Update unit u model
                            u.setUnitFirstTime(1); // Mark tutorial as having completed
                            u.setUnitCompleted(1); // Mark unit as completed (as we don't have any drills - unless the tutorial is a drill)
                            u.setUnitInProgress(0); // Flag the unit as no longer being 'in progress'
                            u.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date())); // Flag last-played datetime

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");

                            // Get next unit u2
                            u2 = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId + 1);

                            // Validate unit u2
                            if (u2 == null) {
                                throw new Exception("Request Code (" + requestCode + ") - Could not retrieve next unit u2 (" + (unitId + 1) + ")");
                            }

                            // Update unit u2
                            u2.setUnitUnlocked(1); // Unlock unit
                            u2.setUnitCompleted(0); // Reset completion if previously completed
                            u2.setUnitInProgress(1); // Update unit to being in progress
                            u2.setUnitDrillLastPlayed(0); // Reset drill progress
                            u2.setUnitSubIDInProgress(0); // Reset subId progress
                            u2.setUnitFirstTimeMovie(0); // Reset chapter movie
                            u2.setUnitFirstTime(0); // Reset splash/tutorial

                            // Update unit u2 in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u2);
                            System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.MOVIE:
                            // Update unit u model
                            u.setUnitFirstTimeMovie(1); // Intro/Chapter/Ending Movie has been watched

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.DRILL_SPLASH:
                            // Update unit u model
                            u.setUnitFirstTime(1); // Splash/Tutorial has been watched

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.RUN_DRILL:

                            // Get drill data
                            int currentDrill = u.getUnitDrillLastPlayed() + 1;
                            int nextDrill = currentDrill + 1;
                            int subId = u.getUnitSubIDInProgress();

                            // Hax to avoid bugged drills
                            ArrayList<Integer> buggedDrills = new ArrayList<>();
                            if (unitId == 1) {
                                // No Word drills #13, 14 and 15 for unit 1
                                buggedDrills.add(13);
                                buggedDrills.add(14);
                                buggedDrills.add(15);
                            }
                            if (buggedDrills.size() > 0) {
                                for (Integer buggedDrill : buggedDrills) {
                                    if (buggedDrill >= nextDrill) {
                                        if (nextDrill == buggedDrill) {
                                            System.err.println("Skipping drill " + nextDrill);
                                            nextDrill++;
                                        } else {
                                            System.out.println("Next drill is " + nextDrill);
                                            break;
                                        }
                                    }
                                }
                            }

                            // Print out current sub id
                            System.out.println("Current sub id: " + subId);
                            //// Sub id logic START
                            // Check if next drill is a word drill (a.k.a, after phonics)
                            if (nextDrill == Globals.WORDS_STARTING_ID) {
                                // It will be a word drill
                                // Check subId to determine if I should 'go back' to phonics
                                // This is based on sub id
                                // Check if the max sub ids for phonics hasn't been reached
                                if (subId < Globals.PHONICS_MAX_SUB_ID) {
                                    // Not it hasn't
                                    // So increase the sub id
                                    u.setUnitSubIDInProgress(++subId);
                                    // set next drill to start of phonics
                                    // note that, as next drill is decremented later,
                                    // will result in a negative drill id
                                    // but ... determineNextItem method takes care of that
                                    nextDrill = Globals.PHONICS_STARTING_ID;
                                } else {
                                    // Set sub id back to 0, as phonics have been completed
                                    u.setUnitSubIDInProgress(0);
                                }
                            }
                            // Print out new sub id
                            System.out.println("New sub id: " + u.getUnitSubIDInProgress());
                            //// Sub id logic END

                            //// Drill splash logic START
                            // It should additionally apply to when sub id = 0 (no prior sub ids played)
                            // Update unit u model accordingly
                            if ((nextDrill == Globals.PHONICS_STARTING_ID && subId == 0) ||
                                    nextDrill == Globals.WORDS_STARTING_ID ||
                                    nextDrill == Globals.STORY_STARTING_ID ||
                                    nextDrill == Globals.MATHS_STARTING_ID) {
                                // Reset UnitFirstTime 'Splash' Flag
                                u.setUnitFirstTime(0);
                            }
                            //// Drill splash logic END

                            // Update unit u model
                            u.setUnitDrillLastPlayed(nextDrill - 1); // Update drill progress

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.CHAPTER_END:
                            // Update unit u model
                            u.setUnitCompleted(1); // Mark unit as completed (as we don't have any drills - unless the tutorial is a drill)
                            u.setUnitInProgress(0); // Flag the unit as no longer being 'in progress'
                            u.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date())); // Flag last-played datetime

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");

                            // Get next unit u2
                            u2 = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId + 1);

                            // Validate unit u2
                            if (u2 == null) {
                                throw new Exception("Request Code (" + requestCode + ") - Could not retrieve next unit u2 (" + (unitId + 1) + ")");
                            }

                            // Update unit u2
                            u2.setUnitUnlocked(1); // Unlock unit
                            u2.setUnitCompleted(0); // Reset completion if previously completed
                            u2.setUnitInProgress(1); // Update unit to being in progress
                            u2.setUnitDrillLastPlayed(0); // Reset drill progress
                            u2.setUnitSubIDInProgress(0); // Reset subId progress
                            u2.setUnitFirstTimeMovie(0); // Reset chapter movie
                            u2.setUnitFirstTime(0); // Reset splash/tutorial

                            // Update unit u2 in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u2);
                            System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() + ") successfull updated in database");
                            break;

                        case Code.FINALE:
                            // Validate that current unit vs request code
                        //// if (unitId != Globals.FINALE_ID) {
                        ////    throw new Exception("Request Code (" + requestCode + ") - Finale request code detected, but unitId (" + unitId + ") does not match that");
                        ////}

                            // Update unit u model
                            u.setUnitFirstTimeMovie(1); // Mark finale as having completed
                            u.setUnitCompleted(1); // Mark unit as completed (as we don't have any drills - unless any finale items are added in the future)
                            u.setUnitInProgress(0); // Flag the unit as no longer being 'in progress'
                            u.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date())); // Flag last-played datetime

                            // Update unit u in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                            System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() + ") successfull updated in database");

                            // Get next unit u2
                            u2 = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), Globals.INTRO_ID); // Go back to beginning

                            // Validate unit u2
                            if (u2 == null) {
                                throw new Exception("Request Code (" + requestCode + ") - Could not retrieve next unit u2 (" + (Globals.INTRO_ID) + ")");
                            }

                            // Update unit u2
                            u2.setUnitUnlocked(1); // Unlock unit
                            u2.setUnitCompleted(0); // Reset completion if previously completed
                            u2.setUnitInProgress(1); // Update unit to being in progress
                            u2.setUnitDrillLastPlayed(0); // Reset drill progress
                            u2.setUnitSubIDInProgress(0); // Reset subId progress
                            u2.setUnitFirstTimeMovie(0); // Reset chapter movie
                            u2.setUnitFirstTime(0); // Reset splash/tutorial

                            // Update unit u2 in database
                            result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u2);
                            System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() + ") successfull updated in database");

                            // Set finaleComplete to true
                            finaleComplete = true;
                            break;

                        default:
                            throw new Exception("Request Code (" + requestCode + ") - Invalid");
                    }

                //// // After all the above database updates, let's get the unitId of the next unit-to-play
                //// unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());

                    // Debug
                    System.out.println("-----------------------------------------------------------------------------");

                    // Flag success
                    success = true;
                }
            } catch (SQLiteException sqlex) {
                System.err.println("MainActivity.onActivityResult > SQLiteException: " + sqlex.getMessage());

            } catch (Exception ex) {
                System.err.println("MainActivity.onActivityResult > Exception: " + ex.getMessage());

            } finally {
                // Close database connection
                if (mDbHelper != null) {
                    mDbHelper.close();
                    mDbHelper = null;
                }

                if (showNavMenu) {

                    // Reset unit first time
                    resetUnitFirstTIme();

                    // Go to language select screen
                    Intent intent = new Intent(this, LanguageSelect.class);
                    startActivityForResult(intent, Code.LANG);
                    overridePendingTransition(0, android.R.anim.fade_out);

                } else if (finaleComplete) {

                    // Go to language select screen
                    Intent intent = new Intent(this, LanguageSelect.class);

                    // Determine next splash bg
                    int[] nextSplashBg = determineNextSplashBg();

                    if (nextSplashBg != null) {
                        if (nextSplashBg[0] != -1) {
                            intent.putExtra(Code.NEXT_BG_CODE, nextSplashBg[0]);
                        }
                        if (nextSplashBg[1] != -1) {
                            intent.putExtra(Code.NEXT_BG_RES, nextSplashBg[1]);
                        }
                    }

                    startActivityForResult(intent, Code.LANG);
                    overridePendingTransition(0, android.R.anim.fade_out);

                } else if (success) {
                    // Determine the next item (a.k.a. 'Activity / Intent') to play
                    determineNextItem();

                } else {
                    // Debug error: force finale
                    System.err.println("MainActivity.determineNextItem > Error: Forcing finale");

                    // Force play finale movie ... for now
                    Intent intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, "finale_movie");
                    intent.putExtra(Code.NEXT_BG_CODE, Code.FINALE);
                    resultCode = Code.FINALE;
                    startActivityForResult(intent, resultCode);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }
    } */

    public int[] determineNextSplashBg() {
        // Index 0: the Code according to commons.Code
        // Index 1: custom bg resource
        int[] nextSplashBg = new int[2];

        nextSplashBg[0] = -1;
        nextSplashBg[1] = -1;

        try {
            // Establish database connectivity
            if (!dbEstablsh(!mInitialized)) {
                throw new Exception("Db Connection unsuccessful");
            }
            System.out.println("MainActivity.determineNextSplashBg > Debug: Db Connection successful");

            // Declare re-usable unit-related variabels
            int unitId, unitCompleted, drillLastPlayed, unitFirstTime, unitFirstTimeMovie;
            Unit u;

            // Get current unit
            if (!mInitialized && HACK_NEXT_UNIT) {
                /* :: HACK :: APPLIED */
                unitId = HACK_UNIT_ID;
                u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);
                unitCompleted = 0;
                drillLastPlayed = HACK_DRILL_LAST_PLAYED;
                unitFirstTime = HACK_UNIT_FIRST_TIME;
                unitFirstTimeMovie = HACK_UNIT_FIRST_TIME_MOVIE;

                // Debug
                System.out.println("MainActivity.determineNextSplashBg > Debug: :: HACK :: APPLIED ");
            } else {
                /* :: HACK :: NOT APPLIED */
                unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
                u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);
                unitCompleted = u.getUnitCompleted();
                drillLastPlayed = u.getUnitDrillLastPlayed();
                unitFirstTime = u.getUnitFirstTime();
                unitFirstTimeMovie = u.getUnitFirstTimeMovie();

                // Debug
                System.out.println("MainActivity.determineNextSplashBg > Debug: :: HACK :: NOT APPLIED ");
            }

            // Unit 1 skip Drill #13, 14, 15 hack
            if (unitId == 1 && drillLastPlayed >= 12 && drillLastPlayed <= 14) {
                drillLastPlayed = 15;
            }

            // Debug
            System.out.println("MainActivity.determineNextSplashBg > Debug: UnitId = " + unitId);

            if (unitId == Globals.INTRO_ID) {
            /* INTRO LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextSplashBg > Debug: Intro Section");

                if (unitFirstTimeMovie == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextSplashBg > Debug: Select Intro Movie");

                    // Select Intro Movie
                    nextSplashBg[0] = Code.INTRO;

                } else if (unitFirstTime == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextSplashBg > Debug: Select Intro Tutorial");

                    // Select Intro Tutorial
                    nextSplashBg[0] = Code.TUTORIAL;
                }
            } else if (unitId < Globals.FINALE_ID) {
            /* CHAPTER LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextSplashBg > Debug: Chapter Section");

                if (unitFirstTimeMovie == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextSplashBg > Debug: Select Chapter Movie");

                    // Select Chapter Movie
                    nextSplashBg[0] = Code.MOVIE;

                } /* else if (u.getUnitFirstTime() == 0) {
                TUTORIAL LOGIC FOR THE FUTURE? Perhaps
            } */ else {

                    // Debug
                    System.out.println("MainActivity.determineNextSplashBg > Debug: Drill section");

                    // Determine type of splash by comparing drill last played
                    int sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills());

                    // Determine if the chapter ending splash should be played
                    // Ending under the following scenarios:
                    // * Unit has not been completed
                    // * Last drill has been played
                    //   - Determine this by checking <drill last played> is equal to <sum of number of drills (language, maths) in unit >
                    if (unitCompleted == 0 && drillLastPlayed == sumOfDrillsPlayed) {

                        // Debug
                        System.out.println("MainActivity.determineNextSplashBg > Debug: Chapter End (" + drillLastPlayed + "/" + sumOfDrillsPlayed + ")");

                        // Select Chapter End Splash
                        nextSplashBg[0] = Code.CHAPTER_END_SPLASH;

                        // Select Custom Resource
                        nextSplashBg[1] = ResourceDecoder.getIdentifier(getApplicationContext(), ("star_level_" + unitId), "drawable");

                        // Let's roll with the drill
                    } else {

                        // Debug
                        System.out.println("MainActivity.determineNextSplashBg > Select Drill Splash");

                        // Phonics Splash
                        if (drillLastPlayed + 1 < Globals.WORDS_STARTING_ID) {
                            nextSplashBg[0] = Code.PHONICS_SPLASH;

                            // Words Splash
                        } else if (drillLastPlayed + 1 < Globals.STORY_STARTING_ID) {
                            nextSplashBg[0] = Code.WORDS_SPLASH;

                            // Story Splash
                        } else if (drillLastPlayed + 1 < Globals.MATHS_STARTING_ID) {
                            nextSplashBg[0] = Code.STORY_SPLASH;

                            // Maths Splash
                        } else {
                            nextSplashBg[0] = Code.MATHS_SPLASH;
                        }
                    }
                }
            } else if (unitId == Globals.FINALE_ID) {
            /* ENDING LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextSplashBg > Debug: Finale Section ");

                nextSplashBg[0] = Code.FINALE;

            } else {
            /* "Whoopsie!" LOGIC */
                throw new Exception("... And why am I here?");
            }

        // Otherwise
        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.determineNextSplashBg > Exception: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.determineNextSplashBg > Exception: " + ex.getMessage());
        } finally {
            // Close database connection
            if (mDbHelper != null) {
                mDbHelper.close();
                mDbHelper = null;
            }
        }

        return nextSplashBg;
    }

    /**
     * Establish db connection
     * @return Returns true/false if db connection has been established
     */
    protected boolean dbEstablsh(boolean recopy) {
        try {
            // Initialize DbHelper
            mDbHelper = DbHelper.getDbHelper(getApplicationContext());

            // Try create database or connect to existing
            if (ALLOW_DB_RECOPY) {
                mDbHelper.createDatabase(recopy);
            } else {
                mDbHelper.createDatabase();
            }

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

    protected boolean dbEstablishWithRecopy() {
        try {
            // Initialize DbHelper
            mDbHelper = DbHelper.getDbHelper(getApplicationContext());

            // Try create database or connect to existing
            mDbHelper.createDatabase(true);

            // Test opening database
            mDbHelper.openDatabase();

            // All good
            return true;

            // Otherwise
        } catch (IOException ioex) {
            System.err.println("MainActivity.dbEstablish > IOException: " + ioex.getMessage());
        } catch (SQLiteException sqlex) {;
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

    @Override
    public void onResume() {
        super.onResume();
        if (!mNewActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(THIS);
        } else {
            mNewActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mNewActivity) {
            Globals.PAUSE_BACKGROUND_MUSIC(THIS);
        } else {
            mNewActivity = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hey, Smart Little Monkey!");
        builder.setMessage("Want to stop reading?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Globals.STOP_BACKGROUND_MUSIC(THIS);
                finishAndRemoveTask();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light, null));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light, null));
            }
        });
        alertDialog.show();
    }
}
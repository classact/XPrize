package classact.com.xprize;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.activity.link.LevelCompleteLink;
import classact.com.xprize.activity.link.MathsLink;
import classact.com.xprize.activity.link.PhonicsLink;
import classact.com.xprize.activity.link.StoryLink;
import classact.com.xprize.activity.link.WordsLink;
import classact.com.xprize.activity.menu.LanguageSelect;
import classact.com.xprize.activity.movie.Movie;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.model.Unit;

public class MainActivity extends AppCompatActivity {

    private boolean mActivitesInProgress;
    private boolean mInitialized;
    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInitialized = false;

        // Establish database connectivity
        if (!dbEstablsh()) {
            // Error handling with no db connection
            /* Error screen activity */
        }
        System.out.println("MainActivity.onCreate > Debug: Db Connection successful");

        Intent intent = new Intent(this, LanguageSelect.class);
        startActivityForResult(intent, Code.LANG);
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    public boolean determineNextItem(int unitId) {
        try {
            // Get current unit
            unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
            Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

            // Debug
            System.out.println("MainActivity.determineNextItem > Debug: UnitId = " + unitId);

            // Declare intent
            Intent intent;

            if (unitId == Globals.INTRO_ID) {
            /* INTRO LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Intro Section");

                if (u.getUnitFirstTimeMovie() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Intro Movie");

                    // Play intro movie
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.SHOW_MV_BUTTONS, false);
                    startActivityForResult(intent, Code.INTRO);
                    overridePendingTransition(android.R.anim.fade_in, 0);

                } else if (u.getUnitFirstTime() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Intro Tutorial");

                    // Start tutorial
                    intent = new Intent(this, Tutorial.class);
                    startActivityForResult(intent, Code.TUTORIAL);
                    overridePendingTransition(0, android.R.anim.fade_out);
                }
            } else if (unitId < Globals.FINALE_ID) {
            /* CHAPTER LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Chapter Section");

                if (u.getUnitFirstTimeMovie() == 0) {

                    // Debug
                    System.out.println("MainActivity.determineNextItem > Debug: Play Chapter Movie");

                    // Play chapter movie
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.SHOW_MV_BUTTONS, true);

                    startActivityForResult(intent, Code.MOVIE);
                    overridePendingTransition(0, android.R.anim.fade_out);

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
                    int sumOfDrillsPlayed = (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills());

                    System.out.println("------------- Unit First Time? " + u.getUnitFirstTime());
                    if (u.getUnitFirstTime() == 0) {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Show drill splash");

                        // Determine drill splash code
                        Class drillSplashActivity;

                        // Phonics Splash
                        if (drillLastPlayed + 1 < Globals.WORDS_STARTING_ID) {
                            drillSplashActivity = PhonicsLink.class;
                        // Words Splashh
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
                        startActivityForResult(intent, Code.DRILL_SPLASH);

                        // Determine if the chapter ending splash should be played
                        // Ending under the following scenarios:
                        // * Unit has not been completed
                        // * Last drill has been played
                        //   - Determine this by checking <drill last played> is equal to <sum of number of drills (language, maths) in unit >
                    } else if (u.getUnitCompleted() == 0 && drillLastPlayed == sumOfDrillsPlayed) {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Chapter End (" + drillLastPlayed + "/" + sumOfDrillsPlayed + ")");

                        // Show ending splash
                        intent = new Intent(this, LevelCompleteLink.class);
                        intent.putExtra(Code.RES_NAME, "star_level_" + unitId);
                        startActivityForResult(intent, Code.CHAPTER_END);
                        overridePendingTransition(0, android.R.anim.fade_out);

                        // Let's roll with the drill
                    } else {

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Determine drill");

                        int drillId = drillLastPlayed + 1; // next item to play
                        int languageId = Globals.SELECTED_LANGUAGE;
                        int subId = 0;

                        if (drillId < Globals.WORDS_STARTING_ID) {
                            subId = 1;
                        } else if (drillId < Globals.STORY_STARTING_ID) {
                            subId = 0;
                        }

                        // Debug
                        System.out.println("MainActivity.determineNextItem > Debug: Running drill for (" + unitId + ", " + drillId + ", " + languageId + ", " + subId + ")");

                        intent = DrillFetcher.fetch(getApplicationContext(), mDbHelper, unitId, drillId, languageId, subId);
                        startActivityForResult(intent, Code.RUN_DRILL);
                        overridePendingTransition(0, android.R.anim.fade_out);
                    }
                }
            } else if (unitId == Globals.FINALE_ID) {
            /* ENDING LOGIC */

                // Debug
                System.out.println("MainActivity.determineNextItem > Debug: Finale Section ");

                if (u.getUnitFirstTimeMovie() == 0) {
                    // Play ending movie
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.SHOW_MV_BUTTONS, false);
                    startActivityForResult(intent, Code.FINALE);
                    overridePendingTransition(0, android.R.anim.fade_out);
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
            return true;

        // Otherwise
        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.determineNextItem > Exception: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.determineNextItem > Exception: " + ex.getMessage());
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
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

                // Close database
                mDbHelper.close();
            }
        } catch (SQLiteException sqlex) {
            System.out.println("MainActivity.onStop >  SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("MainActivity.onStop >  Exception: " + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on Resume");
        try {
            // Determine current unitId
            if (!dbEstablsh()) {
                throw new Exception("Database connection could not be established");
            }
        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.onResume >  SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.onResume >  Exception: " + ex.getMessage());
        }
    }

    /**
     * On Activity Result
     * Responsible for database updates after receiving response
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Debug
        System.out.println("MainActivity.onActivityResult > Debug: Request (" + requestCode + ") and Result (" + resultCode + ")");
        System.out.println("-----------------------------------------------------------------------------");

        /* Resolve request completion
         * ==========================
         * - requestCode = finished activity's type (ie. Language-select, movie, drill)
         * - resultCode = unitId of finished activity
         */
        try {
            // Get current unit
            int unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
            Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);
            Unit u2; // next unit
            int result;

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
                        throw new Exception("Request Code ("+ requestCode +") - Intro request code detected, but unitId (" + unitId + ") does not match that");
                    }

                    // Update unit u model
                    u.setUnitFirstTimeMovie(1); // Intro/Chapter/Ending Movie has been watched

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");

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

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");

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
                    System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() +") successfull updated in database");
                    break;

                case Code.MOVIE:
                    // Update unit u model
                    u.setUnitFirstTimeMovie(1); // Intro/Chapter/Ending Movie has been watched

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");

                    break;
                case Code.DRILL_SPLASH:
                    // Update unit u model
                    u.setUnitFirstTime(1); // Splash/Tutorial has been watched

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");
                    break;

                case Code.RUN_DRILL:
                    // Hax to avoid bugged drills
                    int currentDrill = u.getUnitDrillLastPlayed() + 1;
                    int nextDrill = currentDrill + 1;
                    int[] buggedDrills = {5, 7, 9};

                    for (int buggedDrill : buggedDrills) {
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

                    // Update unit u model
                    u.setUnitDrillLastPlayed(nextDrill - 1); // Update drill progress. Note that subId update is handled separately

                    // Check if next drill requires splash
                    // Update unit u model accordingly
                    if (nextDrill == Globals.PHONICS_STARTING_ID ||
                        nextDrill == Globals.WORDS_STARTING_ID ||
                        nextDrill == Globals.STORY_STARTING_ID ||
                        nextDrill == Globals.MATHS_STARTING_ID) {
                        // Reset UnitFirstTime 'Splash' Flag
                        u.setUnitFirstTime(0);
                    }

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");
                    break;

                case Code.CHAPTER_END:
                    // Update unit u model
                    u.setUnitCompleted(1); // Mark unit as completed (as we don't have any drills - unless the tutorial is a drill)
                    u.setUnitInProgress(0); // Flag the unit as no longer being 'in progress'

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");

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
                    System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() +") successfull updated in database");

                    break;

                case Code.FINALE:
                    // Validate that current unit vs request code
                    if (unitId != Globals.FINALE_ID) {
                        throw new Exception("Request Code (" + requestCode + ") - Finale request code detected, but unitId (" + unitId + ") does not match that");
                    }

                    // Update unit u model
                    u.setUnitFirstTimeMovie(1); // Mark finale as having completed
                    u.setUnitCompleted(1); // Mark unit as completed (as we don't have any drills - unless any finale items are added in the future)
                    u.setUnitInProgress(0); // Flag the unit as no longer being 'in progress'

                    // Update unit u in database
                    result = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    System.out.println("Request Code (" + requestCode + ") - Unit u (" + u.getUnitId() +") successfull updated in database");

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
                    System.out.println("Request Code (" + requestCode + ") - Unit u2 (" + u2.getUnitId() +") successfull updated in database");
                    break;

                default:
                    throw new Exception("Request Code (" + requestCode + ") - Invalid");

            }

            // After all the above database updates, let's get the unitId of the next unit-to-play
            unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());

            // Debug
            System.out.println("-----------------------------------------------------------------------------");

            // Determine the next item (a.k.a. 'Activity / Intent') to play
            determineNextItem(unitId);

        } catch (SQLiteException sqlex) {
            System.err.println("MainActivity.onActivityResult > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("MainActivity.onActivityResult > SQLiteException: " + ex.getMessage());
        }
    }

    public int determineNextSplashBg() {
        return 0;
    }

    /**
     * Establish db connection
     * @return Returns true/false if db connection has been established
     */
    protected boolean dbEstablsh() {
        try {
            // Initialize DbHelper
            mDbHelper = new DbHelper(this);

            // Try create database or connect to existing
            mDbHelper.createDatabase(!mInitialized);

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
}
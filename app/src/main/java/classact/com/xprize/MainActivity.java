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
import classact.com.xprize.activity.menu.HelpMenu;
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
    private ImageButton mHelpButton;
    private ImageButton mStarsButton;
    private AudioManager mAudioManager;
    private DatabaseController mDb;
    private boolean mNewActivity;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = (ConstraintLayout) findViewById(R.id.activity_main);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mNewActivity = false;

        // Read Button
        mReadButton = (ImageButton) findViewById(R.id.read_button);
        mReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextDrill();
            }
        });

        // Help Button
        mHelpButton = (ImageButton) findViewById(R.id.help_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewActivity = true;
                Intent intent = new Intent(THIS, HelpMenu.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Globals.PLAY_BACKGROUND_MUSIC(this);

        // System.out.println("!!!!!!!!!!!!!!!!!!!! " + Build.VERSION.SECURITY_PATCH + ", " + Build.VERSION.RELEASE);

        mPhonicsStarted = false;
        mWordsStarted = false;
        mBooksStarted = false;
        mMathsStarted = false;
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
                    break;
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
package classact.com.xprize;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.activity.MenuActivity;
import classact.com.xprize.activity.menu.HelpMenu;
import classact.com.xprize.activity.menu.StarsMenu;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.DrillFetcher;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.locale.Languages;

public class MainActivity extends MenuActivity {

    @BindView(R.id.app_emblem) ImageView appEmblem;
    @BindView(R.id.read_button) ImageView readButton;
    @BindView(R.id.stars_button) ImageView starsButton;
    @BindView(R.id.help_button) ImageView helpButton;

    private final int READ_BUTTON = 0;
    private final int STARS_BUTTON = 1;
    private final int HELP_BUTTON = 2;

    private boolean mNewActivity;

    @Inject DatabaseController mDb;
    @Inject DrillFetcher drillFetcher;
    MainActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb.setLanguage(Languages.ENGLISH);

//        vm = ViewModelProviders.of(this, vmFactory).get(MainActivityViewModel.class);

        mNewActivity = false;

        setupGraphics();
        setupButtons();
        // preloadImages();
        addListeners();

        Globals.PLAY_BACKGROUND_MUSIC(context);
    }

    public void setupGraphics() {

        readButton.setScaleType(ImageView.ScaleType.FIT_XY);
        starsButton.setScaleType(ImageView.ScaleType.FIT_XY);
        helpButton.setScaleType(ImageView.ScaleType.FIT_XY);

        // App emblem
        loadImage(appEmblem, R.drawable.app_emblem);

        // Buttons
        loadAndLayoutImage(readButton, R.drawable.read_button_up);
        loadAndLayoutImage(starsButton, R.drawable.stars_button_up);
        loadAndLayoutImage(helpButton, R.drawable.help_button_up);
    }

    public void setupButtons() {
        setTouchListener(readButton, R.drawable.read_button_up, R.drawable.read_button_down);
        setTouchListener(starsButton, R.drawable.stars_button_up, R.drawable.stars_button_down);
        setTouchListener(helpButton, R.drawable.help_button_up, R.drawable.help_button_down);
    }

    public void preloadImages() {
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
                R.drawable.star_blue_20
        );
    }

    public void addListeners() {
        // Read Button
        readButton.setOnClickListener((v) -> {
            playNextDrill();
        });

        // Stars Button
        starsButton.setOnClickListener((v) -> {
            mNewActivity = true;
            Intent intent = new Intent(context, StarsMenu.class);
            startActivityForResult(intent, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Help Button
        helpButton.setOnClickListener((v) -> {
            mNewActivity = true;
            Intent intent = new Intent(context, HelpMenu.class);
            startActivityForResult(intent, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    public void playNextDrill() {
        // Get unit section drill data
        UnitSectionDrill unitSectionDrill = mDb.getUnitSectionDrillInProgress();

        try {
            System.out.println("About to play: " + unitSectionDrill.getUnitSectionDrillId());
            Object[] objectArray = new Object[2];
            drillFetcher.fetch(objectArray, Languages.ENGLISH, unitSectionDrill);

            Intent intent = (Intent) objectArray[0];
            int resultCode = (int) objectArray[1];

            Globals.STOP_BACKGROUND_MUSIC(context);

            startActivityForResult(intent, resultCode);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void autoProgressDrill() {
        // Get unit section drill data
        UnitSectionDrill nextUnitSectionDrill = mDb.moveToNextUnitSectionDrill();

        try {
            Object[] objectArray = new Object[2];
            drillFetcher.fetch(objectArray, Languages.ENGLISH, nextUnitSectionDrill);
            Intent intent = (Intent) objectArray[0];
            int resultCode = (int) objectArray[1];

            Globals.STOP_BACKGROUND_MUSIC(context);

            startActivityForResult(intent, resultCode);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
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
            drillFetcher.mPhonicsStarted = false;
            drillFetcher.mWordsStarted = false;
            drillFetcher.mBooksStarted = false;
            drillFetcher.mMathsStarted = false;
        }
    }

    protected void processActivityResult(int code) {
        switch (code) {
            case Code.FINALE:
                mDb.moveToNextUnitSectionDrill();
                readButton.setBackgroundColor(Color.TRANSPARENT);
                readButton.setImageResource(R.drawable.read_button);
                System.out.println("FINALE!!!!");
                break;
            case Code.DRILL_SPLASH:
                // Get unit section drill data
                UnitSectionDrill nextUnitSectionDrill = mDb.getUnitSectionDrillInProgress();

                try {
                    Object[] objectArray = new Object[2];
                    drillFetcher.fetch(objectArray, Languages.ENGLISH, nextUnitSectionDrill);
                    Intent intent = (Intent) objectArray[0];
                    int resultCode = (int) objectArray[1];

                    Globals.STOP_BACKGROUND_MUSIC(context);

                    startActivityForResult(intent, resultCode);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case Code.INTRO:
                System.out.println("!!!!! INTRO !!!!!!!");
                autoProgressDrill();
                break;
            case Code.TUTORIAL:
                System.out.println("!!!!! TUTORIAL !!!!!!!");
                autoProgressDrill();
                break;
            case Code.MOVIE:
                System.out.println("!!!!! MOVIE !!!!!!!");
                autoProgressDrill();
                break;
            case Code.RUN_DRILL:
                System.out.println("!!!!! RUN_DRILL !!!!!!!");
                autoProgressDrill();
                break;
            case Code.CHAPTER_END:
                System.out.println("!!!!! CHAPTER_END !!!!!!!");
                autoProgressDrill();
                break;
            default:
                System.out.println("!!!!! DEFAULT !!!!!!!");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mNewActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mNewActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mNewActivity) {
            Globals.PAUSE_BACKGROUND_MUSIC(context);
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
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            Globals.STOP_BACKGROUND_MUSIC(context);
            finishAndRemoveTask();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.cancel();
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener((dialog) -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light, null));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light, null));
        });
        alertDialog.show();
    }
}
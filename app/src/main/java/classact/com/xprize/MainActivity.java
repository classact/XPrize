package classact.com.xprize;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.ImageButton;

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

    @BindView(R.id.read_button) ImageButton mReadButton;
    @BindView(R.id.help_button) ImageButton mHelpButton;
    @BindView(R.id.stars_button) ImageButton mStarsButton;

    private boolean mNewActivity;

    @Inject DatabaseController mDb;
    @Inject DrillFetcher drillFetcher;
    @Inject Context context;
    @Inject ViewModelProvider.Factory vmFactory;
    MainActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb.setLanguage(Languages.ENGLISH);

//        vm = ViewModelProviders.of(this, vmFactory).get(MainActivityViewModel.class);

        // Check read button
        checkReadButton();

        mNewActivity = false;

        // Read Button
        mReadButton.setOnClickListener((v) -> {
            mReadButton.setOnClickListener(null);
            playNextDrill();
        });

        // Help Button
        mHelpButton.setOnClickListener((v) -> {
            mNewActivity = true;
//          Intent intent = new Intent(THIS, StoryActivity.class);
            Intent intent = new Intent(context, HelpMenu.class);
            startActivityForResult(intent, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Stars Button
        mStarsButton.setOnClickListener((v) -> {
            mNewActivity = true;
            Intent intent = new Intent(context, StarsMenu.class);
            startActivityForResult(intent, 0);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        Globals.PLAY_BACKGROUND_MUSIC(context);
    }

    public void checkReadButton() {
//        // Get current unit section drill in progress
//        UnitSectionDrill currentUnitSectionDrill = mDb.getUnitSectionDrillInProgress();
//
//        // Check if it should display "Read" or "Continue"
//        if (currentUnitSectionDrill.getUnitSectionDrillId() == 1) {
//            mReadButton.setBackgroundColor(Color.TRANSPARENT);
//            mReadButton.setImageResource(R.drawable.read_button);
//        } else {
//            mReadButton.setBackgroundColor(Color.TRANSPARENT);
//            mReadButton.setImageResource(R.drawable.continue_button);
//        }
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
                mReadButton.setBackgroundColor(Color.TRANSPARENT);
                mReadButton.setImageResource(R.drawable.read_button);
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
            checkReadButton();
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
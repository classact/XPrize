package classact.com.clever_little_monkey;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.clever_little_monkey.activity.MenuActivity;
import classact.com.clever_little_monkey.activity.menu.HelpMenu;
import classact.com.clever_little_monkey.activity.menu.StarsMenu;
import classact.com.clever_little_monkey.activity.menu.controller.DatabaseController;
import classact.com.clever_little_monkey.common.Code;
import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.controller.DrillFetcher;
import classact.com.clever_little_monkey.database.model.UnitSectionDrill;
import classact.com.clever_little_monkey.locale.Languages;
import classact.com.clever_little_monkey.utils.LiveObjectAnimator;

public class MainActivity extends MenuActivity implements SensorEventListener {

    @BindView(R.id.activity_main) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;
    @BindView(R.id.app_emblem) ImageView appEmblem;
    @BindView(R.id.read_button) ImageView readButton;
    @BindView(R.id.stars_button) ImageView starsButton;
    @BindView(R.id.help_button) ImageView helpButton;

    @BindView(R.id.g_h_mid) Guideline ghMid;
    @BindView(R.id.g_v_mid) Guideline gvMid;
    @BindView(R.id.gl_v2) Guideline gv2;
    @BindView(R.id.gl_v4) Guideline gv4;

    private final int READ_BUTTON = 0;
    private final int STARS_BUTTON = 1;
    private final int HELP_BUTTON = 2;

    private boolean mNewActivity;

    @Inject DatabaseController mDb;
    @Inject DrillFetcher drillFetcher;
    MainActivityViewModel vm;

//    private SensorManager mSensorManager;
//    private Sensor mAccelerometer;
//    private Sensor mMagnetometer;
//
//    private final float[] mAccelerometerReading = new float[3];
//    private final float[] mMagnetometerReading = new float[3];
//
//    private final float[] mRotationMatrix = new float[9];
//    private final float[] mOrientationAngles = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb.setLanguage(Languages.ENGLISH);
        appEmblem.setImageResource(R.drawable.app_emblem);
//        rootView.setBackgroundColor(Color.WHITE);
//        background.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        loadImage(background, R.drawable.bg_landscape);

        mNewActivity = false;

        setupGraphics();
        setupButtons();
        // preloadImages();
        addListeners();

        Globals.PLAY_BACKGROUND_MUSIC(context);
    }

    public void addSpecialSauce() {
        Guideline ghVillage = ez.guide.create(true, 0.75f);
        Guideline gvVillage = ez.guide.create(false, 0.5f);

        Guideline ghVege01 = ez.guide.create(true, .99f);
        Guideline gvVege01 = ez.guide.create(false, 0.1f);

        Guideline ghVege02 = ez.guide.create(true, 1f);
        Guideline gvVege02 = ez.guide.create(false, 0.95f);

//        Guideline ghClouds = ez.guide.create(true, 0.23f);
        Guideline ghClouds = ez.guide.create(true, 0.4f);

        Guideline ghCloud1 = ez.guide.create(true, 0.325f);
        Guideline gvCloud1 = ez.guide.create(false, 0.2f);

        Guideline ghCloud2 = ez.guide.create(true, 0.5f);
        Guideline gvCloud2 = ez.guide.create(false, 0.5f);

        Guideline ghCloud3 = ez.guide.create(true, 0.4f);
        Guideline gvCloud3 = ez.guide.create(false, 0.8f);

        Guideline ghWindmill = ez.guide.create(true, 0.594f);
        Guideline gvWindmill = ez.guide.create(false, 0.44f);

        rootView.addView(ghVillage);
        rootView.addView(gvVillage);
        rootView.addView(ghVege01);
        rootView.addView(gvVege01);
        rootView.addView(ghVege02);
        rootView.addView(gvVege02);
        rootView.addView(ghClouds);
        rootView.addView(ghCloud1);
        rootView.addView(gvCloud1);
        rootView.addView(ghCloud2);
        rootView.addView(gvCloud2);
        rootView.addView(ghCloud3);
        rootView.addView(gvCloud3);
        rootView.addView(ghWindmill);
        rootView.addView(gvWindmill);

        ez.guide.setPercentage(ghMid, 0.69f);
        ez.guide.setPercentage(gvMid, 0.5f);

        ImageView village = new ImageView(context);
        ImageView windmillTop = new ImageView(context);
        ImageView windmillBlades = new ImageView(context);
        ImageView clouds1 = new ImageView(context);
        ImageView clouds2 = new ImageView(context);
        ImageView clouds3 = new ImageView(context);
        ImageView vege01 = new ImageView(context);
        ImageView vege02 = new ImageView(context);

        village.setScaleType(ImageView.ScaleType.FIT_XY);
        windmillTop.setScaleType(ImageView.ScaleType.FIT_XY);
        windmillBlades.setScaleType(ImageView.ScaleType.FIT_XY);
        clouds1.setScaleType(ImageView.ScaleType.FIT_XY);
        clouds2.setScaleType(ImageView.ScaleType.FIT_XY);
        clouds3.setScaleType(ImageView.ScaleType.FIT_XY);
        vege01.setScaleType(ImageView.ScaleType.FIT_XY);
        vege02.setScaleType(ImageView.ScaleType.FIT_XY);

        ez.layoutWrapContent(village, windmillBlades, windmillTop, clouds1, clouds2, clouds3, vege01, vege02);

        rootView.addView(windmillTop, rootView.getChildCount() - 18);
        rootView.addView(windmillBlades, rootView.getChildCount() - 19);
        rootView.addView(village, rootView.getChildCount() - 20);
        rootView.addView(clouds1, rootView.getChildCount() - 21);
        rootView.addView(clouds2, rootView.getChildCount() - 22);
        rootView.addView(clouds3, rootView.getChildCount() - 23);

        rootView.addView(vege01);
        rootView.addView(vege02);

        loadImage(village, R.drawable.bg_landscape_foreground_02);
        loadImage(windmillBlades, R.drawable.windmill);
        loadImage(windmillTop, R.drawable.windmill_center);
        loadImage(clouds1, R.drawable.clouds_01);
        loadImage(clouds2, R.drawable.clouds_02);
        loadImage(clouds3, R.drawable.clouds_03);
        loadImage(vege01, R.drawable.vegetation_01);
        loadImage(vege02, R.drawable.vegetation_02);

        ez.guide.center(ghVillage, village);
        ez.guide.center(gvVillage, village);

        ez.guide.center(ghWindmill, windmillBlades);
        ez.guide.center(gvWindmill, windmillBlades);

        ez.guide.center(ghWindmill, windmillTop);
        ez.guide.center(gvWindmill, windmillTop);

        ez.guide.center(ghCloud1, clouds1);
        ez.guide.center(gvCloud1, clouds1);

        ez.guide.center(ghCloud2, clouds2);
        ez.guide.center(gvCloud2, clouds2);

        ez.guide.center(ghCloud3, clouds3);
        ez.guide.center(gvCloud3, clouds3);

        ez.guide.center(ghVege01, vege01);
        ez.guide.center(gvVege01, vege01);

        ez.guide.center(ghVege02, vege02);
        ez.guide.center(gvVege02, vege02);

        background.setScaleX(1.4f);
        background.setScaleY(1.4f);
        village.setScaleX(1.4f);
        village.setScaleY(1.4f);
        windmillBlades.setScaleX(1.4f);
        windmillBlades.setScaleY(1.4f);
        windmillTop.setScaleX(1.4f);
        windmillTop.setScaleY(1.4f);

        clouds1.setScaleX(2f);
        clouds1.setScaleY(2f);
        clouds2.setScaleX(1f);
        clouds2.setScaleY(1f);
        clouds3.setScaleX(1.65f);
        clouds3.setScaleY(1.65f);

        vege01.setScaleX(1.5f);
        vege01.setScaleY(1.5f);
        vege02.setScaleX(1.45f);
        vege02.setScaleY(1.45f);

        LiveObjectAnimator windmillAnimator = new LiveObjectAnimator(
                getLifecycle(),
                ObjectAnimator.ofFloat(windmillBlades, "rotation", 0f, 360f))
                .setDuration(60)
                .setRepeatCount(Animation.INFINITE).setInterpolator(new LinearInterpolator());

        windmillAnimator.start();
    }

    public void setupGraphics() {

        readButton.setScaleType(ImageView.ScaleType.FIT_XY);
        starsButton.setScaleType(ImageView.ScaleType.FIT_XY);
        helpButton.setScaleType(ImageView.ScaleType.FIT_XY);

        // App emblem
        // loadImage(appEmblem, R.drawable.app_emblem);

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
        // sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);


        if (!mNewActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mNewActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        mSensorManager.unregisterListener(this);

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

    @Override
    public void onSensorChanged(SensorEvent event) {

//        if (event.sensor == mAccelerometer) {
//            System.arraycopy(event.values, 0, mAccelerometerReading,
//                    0, mAccelerometerReading.length);
//        }
//        else if (event.sensor == mMagnetometer) {
//            System.arraycopy(event.values, 0, mMagnetometerReading,
//                    0, mMagnetometerReading.length);
//        }
        // updateOrientationAngles();
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
//    public void updateOrientationAngles() {
//        // Update rotation matrix, which is needed to update orientation angles.
//        SensorManager.getRotationMatrix(mRotationMatrix, null,
//                mAccelerometerReading, mMagnetometerReading);
//
//        // "mRotationMatrix" now has up-to-date information.
//
//        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
//
//        // "mOrientationAngles" now has up-to-date information.
//        Log.d("Orientation Angles",
//                "Azimuth: " + mOrientationAngles[0] + ", " +
//                        "Pitch: " + mOrientationAngles[1] + ", " +
//                        "Roll: " + mOrientationAngles[2]);
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
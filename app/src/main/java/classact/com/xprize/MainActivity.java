package classact.com.xprize;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
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
import classact.com.xprize.utils.LiveObjectAnimator;

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

    private SensorManager sensorManager;
    private Sensor sensor;
    private float[] gravity;
    private float[] linear_acceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb.setLanguage(Languages.ENGLISH);
        appEmblem.setImageResource(0);
        rootView.setBackgroundColor(Color.WHITE);
        background.setScaleType(ImageView.ScaleType.FIT_CENTER);
        loadImage(background, R.drawable.bg_landscape);

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

//        vm = ViewModelProviders.of(this, vmFactory).get(MainActivityViewModel.class);

        mNewActivity = false;

        setupGraphics();
        setupButtons();
        // preloadImages();
        addListeners();

        /* SENSORS */
        gravity = new float[3];
        linear_acceleration = new float[3];
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        LiveObjectAnimator windmillAnimator = new LiveObjectAnimator(
        getLifecycle(),
        ObjectAnimator.ofFloat(windmillBlades, "rotation", 0f, 360f))
            .setDuration(60)
            .setRepeatCount(Animation.INFINITE).setInterpolator(new LinearInterpolator());

//        LiveObjectAnimator cloud1Animator = new LiveObjectAnimator(
//                getLifecycle(),
//                ObjectAnimator.ofFloat(clouds1, "translationX", 2500).setA)
//                    .setDuration(60000)
//                    .setRepeatCount(Animation.INFINITE)
//                    .setRepeatMode(Animation.REVERSE);
//
//        LiveObjectAnimator cloud2Animator = new LiveObjectAnimator(
//                getLifecycle(),
//                ObjectAnimator.ofFloat(clouds2, "translationX", 1000f))
//                .setDuration(96000)
//                .setRepeatCount(Animation.INFINITE)
//                .setRepeatMode(Animation.REVERSE);
//
//        LiveObjectAnimator cloud3Animator = new LiveObjectAnimator(
//                getLifecycle(),
//                ObjectAnimator.ofFloat(clouds3, "translationX", -2500f))
//                .setDuration(75000)
//                .setRepeatCount(Animation.INFINITE)
//                .setRepeatMode(Animation.REVERSE);

//        cloud1Animator.start();
//        cloud2Animator.start();
//        cloud3Animator.start();

        windmillAnimator.start();

        Globals.PLAY_BACKGROUND_MUSIC(context);
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
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (!mNewActivity) {
            Globals.RESUME_BACKGROUND_MUSIC(context);
        } else {
            mNewActivity = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

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
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        float x = linear_acceleration[0] = event.values[0] - gravity[0];
        float y = linear_acceleration[1] = event.values[1] - gravity[1];
        float z = linear_acceleration[2] = event.values[2] - gravity[2];

        if (x > 1 || y > 1 || z > 1) {
            Log.d("Linear Acceleration",
                    "x: " + x + ", " +
                            "y:" + y + ", " +
                            "z: " + z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
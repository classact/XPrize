package classact.com.xprize.activity.drill.tutorial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.view.TouchView;

public class Tutorial extends AppCompatActivity {

    /* STATES */
    private String currentState;
    private boolean activityCreated;

    /* STATE suffixes */
    private final String READY = "_READY";
    private final String PLAY_COMPLETE = "_PLAY_COMPLETE";
    private final String ANIM_COMPLETE = "_ANIM_COMPLETE";
    private final String TASK_COMPLETE = "_TASK_COMPLETE";
    private final String SPLASH_VISIBLE = "_SPLASH_VISIBLE";
    private final String ACTIVITY_TRANSITION = "_ACTIVITY_TRANSITION";

    /* DEMO REPEAT CONSTANT */
    private final float DEMO_REPEAT = 2.5f; // 2 animations make up 1 loop demo

    /* DRAG & DRAW sensitivity threshold */
    private final int DRAG_SENSITIVITY_THRESHOLD = 5;
    private final int DRAW_SENSITIVITY_THRESHOLD = 5;

    /* VIEW KEYS */
    private final int INTRO = 0;
    private final int START = 1;
    private final int PLAY = 2;
    private final int STOP = 3;
    private final int TOUCH = 4;
    private final int DRAG = 5;
    private final int DRAW = 6;
    private final int PAUSE = 7;

    /* Screen dimensions */
    float screenWidth;
    float screenHeight;

    /* Views */
    View introView;
    View startView;
    View playView;
    View stopView;
    View touchView;
    View dragView;
    View drawView;
    View pauseView;

    /* Tutorial Splash */
    RelativeLayout chapter01SplashContainer;
    ImageView chapter01Splash;
    Animation chapter01SplashFadeIn;

    /* Subviews */
    // Intro
    // Start
    ImageView startDemo;
    ImageView startDemoCover;
    AnimationDrawable startDemoAnimation;
    Button startButton;
    // Play
    ImageView playDemo;
    ImageView playDemoCover;
    AnimationDrawable playDemoAnimation;
    Button playButton;
    // Stop
    ImageView stopDemo;
    ImageView stopDemoCover;
    AnimationDrawable stopDemoAnimation;
    Button stopButton;
    // Touch
    ImageView touchDemo;
    ImageView touchDemoCover;
    AnimationDrawable touchDemoAnimation;
    Button touchButton;
    // Drag
    ImageView banana;
    int bananaDeltaX;
    int bananaDeltaY;
    Point dragOrigin;
    boolean dragStarted;
    boolean dragging;
    boolean dragCaptured;
    ImageView dragDemo;
    RelativeLayout dragDemoContainer;
    TranslateAnimation dragDemoAnimation;
    int bananaTopMargin, bananaLeftMargin;
    boolean bananaMarginsCaptured;
    // Draw
    ImageView drawDemo;
    ImageView drawDemoCover;
    AnimationDrawable drawDemoAnimation;
    ImageView drawTick;
    TouchView drawSurface;
    View drawSurfaceShadow;
    Point drawOrigin;
    boolean drawStarted;
    boolean drawing;
    boolean drawCaptured;
    // Pause
    ImageView pauseDemo;
    ImageView pauseDemoCover;
    AnimationDrawable pauseDemoAnimation;
    Button pauseButton;

    /* MediaPlayer */
    MediaPlayer mediaPlayer;

    /* Handlers & Controllers */
    Handler delayHandler;
    private static TutorialController tutorialController;
    Handler handler;

    /* Random generator */
    Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_tutorial);

        // Instantiate views
        introView = findViewById(R.id.content_tutorial_intro);
        startView = findViewById(R.id.content_tutorial_start);
        playView = findViewById(R.id.content_tutorial_play);
        stopView = findViewById(R.id.content_tutorial_stop);
        touchView = findViewById(R.id.content_tutorial_touch);
        dragView = findViewById(R.id.content_tutorial_drag);
        drawView = findViewById(R.id.content_tutorial_draw);
        pauseView = findViewById(R.id.content_tutorial_pause);

        // Set drag view visible
        introView.setVisibility(View.VISIBLE);

        // Hide all other views
        startView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        stopView.setVisibility(View.INVISIBLE);
        touchView.setVisibility(View.INVISIBLE);
        dragView.setVisibility(View.INVISIBLE);
        drawView.setVisibility(View.INVISIBLE);
        pauseView.setVisibility(View.INVISIBLE);

        // Chapter 01 Splash setup
        chapter01SplashContainer = (RelativeLayout) findViewById(R.id.chapter_01_splash_container);
        chapter01Splash = (ImageView) findViewById(R.id.chapter_01_splash);
        chapter01SplashContainer.setVisibility(View.INVISIBLE);
        chapter01SplashFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        chapter01SplashFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                chapter01SplashContainer.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animation animation) {}

            @Override public void onAnimationRepeat(Animation animation) {}
        });
        chapter01SplashFadeIn.setFillAfter(true);

        // Screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // Instantiate subviews: Start
        startDemo = (ImageView) findViewById(R.id.tutorial_start_demo);
        startDemoCover = (ImageView) findViewById(R.id.tutorial_start_demo_cover);
        startDemoAnimation = (AnimationDrawable) startDemo.getDrawable();
        startButton = (Button) findViewById(R.id.tutorial_start_button);

        // Instantiate subviews: Play
        playDemo = (ImageView) findViewById(R.id.tutorial_play_demo);
        playDemoCover = (ImageView) findViewById(R.id.tutorial_play_demo_cover);
        playDemoAnimation = (AnimationDrawable) playDemo.getDrawable();
        playButton = (Button) findViewById(R.id.tutorial_play_button);

        // Instantiate subviews: Stop
        stopDemo = (ImageView) findViewById(R.id.tutorial_stop_demo);
        stopDemoCover = (ImageView) findViewById(R.id.tutorial_stop_demo_cover);
        stopDemoAnimation = (AnimationDrawable) stopDemo.getDrawable();
        stopButton = (Button) findViewById(R.id.tutorial_stop_button);

        // Instantiate subviews: Touch
        touchDemo = (ImageView) findViewById(R.id.tutorial_touch_demo);
        touchDemoCover = (ImageView) findViewById(R.id.tutorial_touch_demo_cover);
        touchDemoAnimation = (AnimationDrawable) touchDemo.getDrawable();

        // Instantiate subviews: Drag
        banana = (ImageView) findViewById(R.id.tutorial_banana);
        touchButton = (Button) findViewById(R.id.tutorial_banana_button);
        dragDemo = (ImageView) findViewById(R.id.tutorial_drag_demo);
        dragDemoContainer = (RelativeLayout) findViewById(R.id.tutorial_drag_demo_container);

        // Drag Demo and Banana margins captured
        bananaMarginsCaptured = false;

        // Drag bools
        dragStarted = false;
        dragging = false;
        dragCaptured = false;

        // Drag animation
        dragDemoAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.6f
        );
        dragDemoAnimation.setDuration(2500);
        dragDemoAnimation.setRepeatCount(0);
        dragDemoAnimation.setInterpolator(new DecelerateInterpolator());
        dragDemoAnimation.setFillAfter(true);

        // Instantiate subviews: Draw
        drawDemo = (ImageView) findViewById(R.id.tutorial_draw_demo);
        drawDemoCover = (ImageView) findViewById(R.id.tutorial_draw_demo_cover);
        drawTick = (ImageView) findViewById(R.id.tutorial_tick_background);
        drawSurface = (TouchView) findViewById(R.id.tutorial_draw_surface);
        drawSurfaceShadow = findViewById(R.id.tutorial_draw_surface_shadow);
        drawDemoAnimation = (AnimationDrawable) drawDemo.getDrawable();
        drawStarted = false;
        drawing = false;
        drawCaptured = false;

        // Instantiate subviews: Pause
        pauseDemo = (ImageView) findViewById(R.id.tutorial_pause_demo);
        pauseDemoCover = (ImageView) findViewById(R.id.tutorial_pause_demo_cover);
        pauseDemoAnimation = (AnimationDrawable) pauseDemo.getDrawable();
        pauseButton = (Button) findViewById(R.id.tutorial_pause_button);

        // Instantiate handler
        delayHandler = new Handler();
        handler = new Handler();
        tutorialController = new TutorialController(this);

        // Initialize Random Generator
        rnd = new Random();

        // Initialize audio resources
        TutorialAudioResources.init();

        // Ready R01 Intro section
        readyIntro(TutorialStates.TUT_R01_INTRO);
    }

    @Override
    public void onResume() {
        // Call superclass method first
        super.onResume();

        // Send message to controller
        if (!activityCreated) {
            activityCreated = true;
        } else {
            sendMessageToController("onResume", currentState + READY);
        }
    }

    /***************************************
     * SEND MESSAGE TO TUTORIAL CONTROLLER *
     ***************************************/
    /* public void sendMessageToController(String message) {
        Message msg = new Message();
        msg.obj = message;
        msg.setTarget(tutorialController);
        msg.sendToTarget();
    }*/

    public void sendMessageToController(String src, String message) {
        System.out.println("Sending message '" + message + "' from '" + src + "'");

        Message msg = new Message();
        msg.obj = message;
        msg.setTarget(tutorialController);
        msg.sendToTarget();
    }

    /*****************
     * INTRO SECTION *
     *****************/
    public void readyIntro(String state) {
        // Update current state
        currentState = state;

        // Show view
        showView(INTRO);

        // Send message to controller
        sendMessageToController("readyIntro", currentState + READY);
    }

    /*****************
     * START SECTION *
     *****************/
    public void readyStart(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        startDemoAnimation.stop();
        startDemoAnimation.selectDrawable(0);

        // Show demo content
        startDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        startButton.setVisibility(View.INVISIBLE);
        startDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(START);

        // Send message to controller
        sendMessageToController("readyStart", currentState + READY);
    }

    public void runStartDemo(String state) {
        // Update current state
        currentState = state;

        // Add runnable to stop animation
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                startDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                startDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runStartDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                startDemoAnimation.stop();
            }
        }, (long) (1600 * DEMO_REPEAT));

        // Play it ~ ♪♫♪
        startDemoAnimation.start();
    }

    public void readyStartPractice(String state) {
        // Update current state
        currentState = state;

        // Show practice content
        startButton.setVisibility(View.VISIBLE);

        // Hide demo content
        startDemo.setVisibility(View.INVISIBLE);
        startDemoCover.setVisibility(View.INVISIBLE);

        // Set on click listener to practice button
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Send message to controller
                sendMessageToController("readyStartPractice", currentState + TASK_COMPLETE);

                // Remove listener
                startButton.setOnClickListener(null);
            }
        });
    }

    /**
     * PLAY SECTION
     */
    public void readyPlay(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        playDemoAnimation.stop();
        playDemoAnimation.selectDrawable(0);

        // Show demo content
        playDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        playButton.setVisibility(View.INVISIBLE);
        playDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(PLAY);

        // Send message to controller
        sendMessageToController("readyPlay", currentState + READY);
    }

    public void runPlayDemo(String state) {
        // Update current state
        currentState = state;

        // Add runnable to stop animation
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                playDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                playDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runPlayDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                playDemoAnimation.stop();
            }
        }, (long) (1600 * DEMO_REPEAT));

        // Play it ~ ♪♫♪
        playDemoAnimation.start();
    }

    public void readyPlayPractice(String state) {
        // Update current state
        currentState = state;

        // Show practice content
        playButton.setVisibility(View.VISIBLE);

        // Hide demo content
        playDemo.setVisibility(View.INVISIBLE);
        playDemoCover.setVisibility(View.INVISIBLE);

        // Set on click listener to practice button
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send message to controller
                sendMessageToController("readyPlayPractice", currentState + TASK_COMPLETE);

                // Remove listener
                playButton.setOnClickListener(null);
            }
        });
    }

    /**
     * STOP SECTION
     */
    public void readyStop(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        stopDemoAnimation.stop();
        stopDemoAnimation.selectDrawable(0);

        // Show demo content
        stopDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        stopButton.setVisibility(View.INVISIBLE);
        stopDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(STOP);

        // Send message to controller
        sendMessageToController("readyStop", currentState + READY);
    }

    public void runStopDemo(String state) {
        // Update current state
        currentState = state;

        // Add runnable to stop animation
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                stopDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                stopDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runStopDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                stopDemoAnimation.stop();
            }
        }, (long) (1600 * DEMO_REPEAT));

        // Play it ~ ♪♫♪
        stopDemoAnimation.start();
    }

    public void readyStopPractice(String state) {
        // Update current state
        currentState = state;

        // Show practice content
        stopButton.setVisibility(View.VISIBLE);

        // Hide demo content
        stopDemo.setVisibility(View.INVISIBLE);
        stopDemoCover.setVisibility(View.INVISIBLE);

        // Set on click listener to practice button
        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send message to controller
                sendMessageToController("readyStopPractice", currentState + TASK_COMPLETE);

                // Remove listener
                stopButton.setOnClickListener(null);
            }
        });
    }

    /**
     * TOUCH SECTION
     */
    public void readyTouch(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        touchDemoAnimation.stop();
        touchDemoAnimation.selectDrawable(0);

        // Show demo content
        touchDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        touchButton.setVisibility(View.INVISIBLE);
        touchDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(TOUCH);

        // Send message to controller
        sendMessageToController("readyTouch", currentState + READY);
    }

    public void runTouchDemo(String state) {
        // Update current state
        currentState = state;

        // Add runnable to stop animation
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                touchDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                touchDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runTouchDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                touchDemoAnimation.stop();
            }
        }, (long) (1200 * DEMO_REPEAT));

        // Play it ~ ♪♫♪
        touchDemoAnimation.start();
    }

    public void readyTouchPractice(String state) {
        // Update current state
        currentState = state;

        // Show practice content
        touchButton.setVisibility(View.VISIBLE);

        // Hide demo content
        touchDemo.setVisibility(View.INVISIBLE);
        touchDemoCover.setVisibility(View.INVISIBLE);

        // Set on click listener to practice button
        touchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send message to controller
                sendMessageToController("readyTouchPractice", currentState + TASK_COMPLETE);

                // Remove listener
                touchButton.setOnClickListener(null);
            }
        });
    }

    /**
     * DRAG SECTION
     */
    public void readyDrag(String state) {
        // Update current state
        currentState = state;

        // Clear animation on dragDemo
        dragDemo.clearAnimation();

        // Show demo content
        dragDemoContainer.setVisibility(View.VISIBLE);

        // Hide practice content
        banana.setVisibility(View.INVISIBLE);

        // Show view
        showView(DRAG);

        // Send message to controller
        sendMessageToController("readyDrag", currentState + READY);
    }

    public void runDragDemo(String state) {
        // Update current state
        currentState = state;

        // Add listener to drag demo animation
        dragDemoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Send message to controller
                sendMessageToController("runDragDemo", currentState + ANIM_COMPLETE);

                // Remove listener
                dragDemoAnimation.setAnimationListener(null);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        // Play it ~ ♪♫♪
        dragDemo.startAnimation(dragDemoAnimation);
    }

    public void readyDragPractice(String state) {
        // Update current state
        currentState = state;

        // Reset drag booleans
        dragStarted = false;
        dragging = false;
        dragCaptured = false;

        // Reset banana margins (margins were previously captured)
        if (bananaMarginsCaptured) {
            RelativeLayout.LayoutParams bananaParams = (RelativeLayout.LayoutParams) banana.getLayoutParams();
            bananaParams.topMargin = bananaTopMargin;
            bananaParams.leftMargin = bananaLeftMargin;
            banana.setLayoutParams(bananaParams);
        }

        // Hide demo content
        dragDemoContainer.setVisibility(View.INVISIBLE);

        // Clear demo animation
        dragDemo.clearAnimation();

        // Show practice content
        banana.setVisibility(View.VISIBLE);

        // Capture banana margins if first time displayed
        if (!bananaMarginsCaptured) {
            bananaMarginsCaptured = true;

            RelativeLayout.LayoutParams bananaParams = (RelativeLayout.LayoutParams) banana.getLayoutParams();
            bananaParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
            bananaTopMargin = banana.getTop();
            bananaLeftMargin = banana.getLeft();
            bananaParams.topMargin = bananaTopMargin;
            bananaParams.leftMargin = bananaLeftMargin;
            banana.setLayoutParams(bananaParams);
        }

        // Add listener to banana
        banana.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean result = true;

                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        BitmapDrawable bd = (BitmapDrawable) ((ImageView) v).getDrawable();
                        Bitmap b = bd.getBitmap();

                        float sw = (float) b.getWidth()/(float) v.getWidth();
                        float sh = (float) b.getHeight()/(float) v.getHeight();

                        boolean canDrag = true;

                        int x = (int) (event.getX() * sw);
                        int y = (int) (event.getY() * sh);

                        if (x > b.getWidth() || x < 0) {
                            canDrag = false;
                        }

                        if (y > b.getHeight() || y < 0) {
                            canDrag = false;
                        }

                        if (canDrag && (b.getPixel(x, y) != Color.TRANSPARENT)) {
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                            bananaDeltaX = rawX - lParams.leftMargin;
                            bananaDeltaY = rawY - lParams.topMargin;
                            dragStarted = true;

                            // Capture original drag start position
                            dragOrigin = new Point((int) event.getRawX(), (int) event.getRawY());
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (dragStarted) {
                            if (!dragging) {
                                dragging = true;
                            }

                            if (dragging) {
                                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                                lParams.leftMargin = rawX - bananaDeltaX;
                                lParams.topMargin = rawY - bananaDeltaY;
                                v.setLayoutParams(lParams);

                                // Check current drag position against origin if no drag has been captured
                                // If there's no difference, nothing has been dragged
                                if (!dragCaptured) {
                                    if (Math.abs((int) event.getRawX() - dragOrigin.x) > DRAG_SENSITIVITY_THRESHOLD ||
                                        Math.abs((int) event.getRawY() - dragOrigin.y) > DRAG_SENSITIVITY_THRESHOLD) {
                                        dragCaptured = true;
                                    }
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Check if dragging has started
                        if (dragging) {

                            // Has banana been dragged?
                            if (dragCaptured) {

                                // Update drag booleans
                                dragCaptured = true;
                                dragging = false;
                                dragStarted = false;

                                // Send message to controller
                                sendMessageToController("readyDragPractice", currentState + TASK_COMPLETE);

                                // Remove listener
                                banana.setOnTouchListener(null);
                            } else {
                                // Banana has not been dragged yet, as dragCaptured is still false
                                dragging = false;
                                dragStarted = false;
                            }
                        }
                        break;
                    default:
                        result = false;
                        break;
                }
                return result;
            }
        });
    }

    /**
     * DRAW SECTION
     */
    public void readyDraw(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        drawDemoAnimation.stop();
        drawDemoAnimation.selectDrawable(0);

        // Clear draw surface
        drawSurface.clearCanvas();

        // Show demo content
        drawDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        drawTick.setVisibility(View.INVISIBLE);
        drawSurface.setVisibility(View.INVISIBLE);
        drawSurfaceShadow.setVisibility(View.INVISIBLE);
        drawDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(DRAW);

        // Send message to controller
        sendMessageToController("readyDraw", currentState + READY);
    }

    public void runDrawDemo(String state) {
        // Update current state
        currentState = state;

        // Show demo contents
        drawDemo.setVisibility(View.VISIBLE);

        // Hide practice contents
        drawTick.setVisibility(View.INVISIBLE);
        drawSurface.setVisibility(View.INVISIBLE);
        drawSurfaceShadow.setVisibility(View.INVISIBLE);

        // Add runnable to stop animation
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                drawDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                drawDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runDrawDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                drawDemoAnimation.stop();
            }
        }, 5000);

        // Play it ~ ♪♫♪
        drawDemoAnimation.start();
    }

    public void readyDrawPractice(String state) {
        // Update current state
        currentState = state;

        // Reset draw-related booleans
        drawStarted = false;
        drawing = false;
        drawCaptured = false;

        // Show practice content
        drawSurfaceShadow.setVisibility(View.VISIBLE);
        drawSurface.setVisibility(View.VISIBLE);
        drawTick.setVisibility(View.VISIBLE);

        // Hide demo content
        drawDemo.setVisibility(View.INVISIBLE);
        drawDemoCover.setVisibility(View.INVISIBLE);

        // Add listener to surface shadow to detect touch events
        // Note that surface shadow assigns its coordinates to surface below it
        drawSurfaceShadow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Update drawSurface so that, even if it is below drawSurfaceShadow,
                // it looks as if it is detecting touch events
                drawSurface.onTouchEvent(event);
                boolean result = true;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawStarted = true;

                        // Capture draw origin
                        drawOrigin = new Point((int) event.getRawX(), (int) event.getRawY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (drawStarted) {
                            drawing = true;

                            // Check if anything has been drawn
                            if (!drawCaptured) {
                                if (Math.abs((int) event.getRawX() - drawOrigin.x) > DRAW_SENSITIVITY_THRESHOLD ||
                                    Math.abs((int) event.getRawY() - drawOrigin.y) > DRAW_SENSITIVITY_THRESHOLD) {
                                    drawCaptured = true;
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (drawing) {
                            // Check if anything has been drawn
                            // Only finish task if draw surface is not blank
                            if (!drawSurface.isBlank() && drawCaptured) {
                                drawCaptured = true; // perhaps for future use?
                                drawing = false;
                                drawStarted = false;

                                // Send message to controller
                                sendMessageToController("readyDrawPractice", currentState + TASK_COMPLETE);

                                // Remove listener
                                drawSurfaceShadow.setOnTouchListener(null);

                            } else {
                                // Nothing has been drawn
                                // Don't send confirmation to controller yet
                                drawCaptured =  false;
                                drawing = false;
                                drawStarted = false;
                            }
                        }
                        break;
                    default:
                        result = false;
                        break;
                }
                return result;
            }
        });
    }

    /**
     * PAUSE SECTION
     */
    public void readyPause(String state) {
        // Update current state
        currentState = state;

        // Reset animation
        pauseDemoAnimation.stop();
        pauseDemoAnimation.selectDrawable(0);

        // Show demo content
        pauseDemo.setVisibility(View.VISIBLE);

        // Hide demo cover and practice button
        pauseButton.setVisibility(View.INVISIBLE);
        pauseDemoCover.setVisibility(View.INVISIBLE);

        // Show view
        showView(PAUSE);

        // Send message to controller
        sendMessageToController("readyPause", currentState + READY);
    }

    public void runPauseDemo(String state) {
        // Update current state
        currentState = state;

        // Add runnable to stop animation
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show demo cover
                pauseDemoCover.setVisibility(View.VISIBLE);

                // Hide demo
                pauseDemo.setVisibility(View.INVISIBLE);

                // Send message to controller
                sendMessageToController("runPauseDemo", currentState + ANIM_COMPLETE);

                // Stop animation
                pauseDemoAnimation.stop();
            }
        }, (long) (1600 * DEMO_REPEAT));

        // Play it ~ ♪♫♪
        pauseDemoAnimation.start();
    }

    public void readyPausePractice(String state) {
        // Update current state
        currentState = state;

        // Show practice content
        pauseButton.setVisibility(View.VISIBLE);

        // Hide demo content
        pauseDemo.setVisibility(View.INVISIBLE);
        pauseDemoCover.setVisibility(View.INVISIBLE);

        // Set on click listener to practice button
        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send message to controller
                sendMessageToController("pause", currentState + TASK_COMPLETE);

                // Remove listener
                pauseButton.setOnClickListener(null);
            }
        });
    }

    /****************
     * END TUTORIAL *
     ****************/
    public void showChapter01Splash(String state) {
        // Update current state
        currentState = state;

        // Clear animation listener
        chapter01SplashFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Send message to controller
                sendMessageToController("showChapter01Splash", SPLASH_VISIBLE);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        // Start splash animation
        chapter01SplashContainer.startAnimation(chapter01SplashFadeIn);
    }

    public void transitionToChapter01(String state) {
        // Debug
        System.out.println("Tutorial.transitionToChapter01 > Debug: Executing Intent finish logic");

        // Update current state
        currentState = state;

        // Send message to controller
        sendMessageToController("transitionToChapter01", ACTIVITY_TRANSITION);

        /*
        Intent intent = new Intent(this, Movie.class);
        startActivity(intent);
        overridePendingTransition(0, android.R.anim.fade_out);
        */

        // Release media player
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    /**************
     * PLAY AUDIO *
     **************/
    public void play(String state) {
        // Update current state
        currentState = state;

        /* OLD CODE
        // Retrieve audio resource
        int audioResourceId = TutorialAudioResources.get(state);

        // Validate audio resource
        if (audioResourceId == 0) {
            // TO DO: Handle this error
            System.err.println("Error retrieving tutorial audio resource id.");
            return;
        } else if (audioResourceId == -1) {
            audioResourceId = TutorialAudioResources.getAffirmation(rnd.nextInt(TutorialAudioResources.noOfAffirmations()));
        }
        */

        try {

            // Get resource name
            String name = TutorialAudioResources.get(state);

            // Validate resource name
            if (name == null) {

                // Throw error
                throw new Exception("Could not retrieve audio resource name");

            // Check if it's an affirmation
            } else if (name.equalsIgnoreCase("-1")) {

                // Get affirmation
                name = TutorialAudioResources.getAffirmation(rnd.nextInt(TutorialAudioResources.noOfAffirmations()));
            }

            // Get resource file descriptor, using name
            String soundPath = FetchResource.sound(getApplicationContext(), name);

            // Reset media player
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            // Reset media player
            mediaPlayer.reset();

            // Set data source of media player
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(soundPath));


            // Set on completion listener
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    // Release all previous media player instances
                    mp.reset();

                    // Send Message to controller
                    sendMessageToController("play", currentState + PLAY_COMPLETE);
                }
            });

            // Set on prepared listener
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    // Play the tune ~ ♪♫♪
                    mediaPlayer.start();
                }
            });

            // Prepare the media player
            mediaPlayer.prepare();

        } catch (IOException ioex) {
            System.err.println("Tutorial.play > IOException: " + ioex.getMessage());

        } catch (Exception ex) {
            System.err.println("Tutorial.play > Exception: " + ex.getMessage());
        }
    }

    /****************************
     * VIEW VISIBILITY CONTROLS *
     ****************************/
    public void showView(int viewKey) {

        switch (viewKey) {
            case INTRO:
                // Set drag view visible
                introView.setVisibility(View.VISIBLE);

                // Hide all other views
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case START:
                // Set drag view visible
                startView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case PLAY:
                // Set drag view visible
                playView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case STOP:
                // Set drag view visible
                stopView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case TOUCH:
                // Set drag view visible
                touchView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case DRAG:
                // Set drag view visible
                dragView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case DRAW:
                // Set drag view visible
                drawView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                pauseView.setVisibility(View.INVISIBLE);
                break;
            case PAUSE:
                // Set drag view visible
                pauseView.setVisibility(View.VISIBLE);

                // Hide all other views
                introView.setVisibility(View.INVISIBLE);
                startView.setVisibility(View.INVISIBLE);
                playView.setVisibility(View.INVISIBLE);
                stopView.setVisibility(View.INVISIBLE);
                touchView.setVisibility(View.INVISIBLE);
                dragView.setVisibility(View.INVISIBLE);
                drawView.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (delayHandler != null) {
            delayHandler.removeCallbacksAndMessages(null);
            delayHandler = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

/*
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);

FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
});
*/
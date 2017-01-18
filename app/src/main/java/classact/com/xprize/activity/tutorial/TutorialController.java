package classact.com.xprize.activity.tutorial;

import android.os.Handler;
import android.os.Message;

/**
 * Created by hyunchanjeong on 2017/01/13.
 */

public class TutorialController extends Handler {

    private final String READY = "_READY";
    private final String PLAY_COMPLETE = "_PLAY_COMPLETE";
    private final String ANIM_COMPLETE = "_ANIM_COMPLETE";
    private final String TASK_COMPLETE = "_TASK_COMPLETE";
    private final String SPLASH_VISIBLE = "_SPLASH_VISIBLE";
    private final String ACTIVITY_TRANSITION = "_ACTIVITY_TRANSITION";

    private static class DelayHandler extends Handler {}
    private final DelayHandler delayHandler = new DelayHandler();

    private final TutorialActivity t;

    public TutorialController(TutorialActivity a) {
        t = a;
    }

    @Override
    public void handleMessage(Message msg) {

        String message = "";

        try {
            message = (String) msg.obj;
        } catch (Exception ex) {
            System.err.println("Error handling tutorial message: " + ex.getMessage());
            return;
        }

        System.out.println("Handling message: " + message);

        switch (message) {

            /*****************
             * R01 SECTION *
             *****************/

            /**
             * Play tutorial R01 intro
             */
            /*
            case TutorialStates.TUT_R01_INTRO + READY:
                t.play(TutorialStates.TUT_R01_INTRO);
                break;
                */

            /**
             * Tutorial R01 intro complete
             * Ready start section
             */
            case TutorialStates.TUT_R01_INTRO + READY:
                t.readyStart(TutorialStates.TUT_R01_START_INTRO);
                break;

            /*****************
             * START SECTION *
             *****************/

            /**
             * Start section ready
             * Play start intro
             */
            case TutorialStates.TUT_R01_START_INTRO + READY:
                t.play(TutorialStates.TUT_R01_START_INTRO);
                break;

            /**
             * Start intro complete
             * Play start audio
             * Run start demo
             */
            case TutorialStates.TUT_R01_START_INTRO + PLAY_COMPLETE:
                t.runStartDemo(TutorialStates.TUT_R01_START_DEMO);
                break;

            /**
             * Start demo complete
             * Play start practice prompt
             */
            case TutorialStates.TUT_R01_START_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_START_PROMPT);
                break;

            /**
             * Start practice prompt complete
             * Ready start practice
             */
            case TutorialStates.TUT_R01_START_PROMPT + PLAY_COMPLETE:
                t.readyStartPractice(TutorialStates.TUT_R01_START_PRACTICE);
                break;

            /**
             * Start practice complete
             * Play start practice affirmation
             */
            case TutorialStates.TUT_R01_START_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_START_PRACTICE);
                break;

            /**
             * Start practice affirmation complete
             * Ready play section
             */
            case TutorialStates.TUT_R01_START_PRACTICE + PLAY_COMPLETE:
                t.readyPlay(TutorialStates.TUT_R01_PLAY_INTRO);
                break;

            /****************
             * PLAY SECTION *
             ****************/

            /**
             * Play section ready
             * Play play intro
             */
            case TutorialStates.TUT_R01_PLAY_INTRO + READY:
                t.play(TutorialStates.TUT_R01_PLAY_INTRO);
                break;

            /**
             * Play intro complete
             * Play play audio
             * Run play demo
             */
            case TutorialStates.TUT_R01_PLAY_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_PLAY_DEMO);
                t.runPlayDemo(TutorialStates.TUT_R01_PLAY_DEMO);
                break;

            /**
             * Play demo complete
             * Play play practice prompt
             */
            case TutorialStates.TUT_R01_PLAY_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_PLAY_PROMPT);
                break;

            /**
             * Play practice prompt complete
             * Ready play practice
             */
            case TutorialStates.TUT_R01_PLAY_PROMPT + PLAY_COMPLETE:
                t.readyPlayPractice(TutorialStates.TUT_R01_PLAY_PRACTICE);
                break;

            /**
             * Play practice complete
             * Play play practice affirmation
             */
            case TutorialStates.TUT_R01_PLAY_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_PLAY_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R01_PLAY_PRACTICE + PLAY_COMPLETE:
                t.readyStop(TutorialStates.TUT_R01_STOP_INTRO);
                break;

            /****************
             * STOP SECTION *
             ****************/

            /**
             * Play section ready
             * Play play intro
             */
            case TutorialStates.TUT_R01_STOP_INTRO + READY:
                t.play(TutorialStates.TUT_R01_STOP_INTRO);
                break;

            /**
             * Play intro complete
             * Play play audio
             * Run play demo
             */
            case TutorialStates.TUT_R01_STOP_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_STOP_DEMO);
                t.runStopDemo(TutorialStates.TUT_R01_STOP_DEMO);
                break;

            /**
             * Play demo complete
             * Play play practice prompt
             */
            case TutorialStates.TUT_R01_STOP_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_STOP_PROMPT);
                break;

            /**
             * Play practice prompt complete
             * Ready play practice
             */
            case TutorialStates.TUT_R01_STOP_PROMPT + PLAY_COMPLETE:
                t.readyStopPractice(TutorialStates.TUT_R01_STOP_PRACTICE);
                break;

            /**
             * Play practice complete
             * Play play practice affirmation
             */
            case TutorialStates.TUT_R01_STOP_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_STOP_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R01_STOP_PRACTICE + PLAY_COMPLETE:
                t.readyTouch(TutorialStates.TUT_R01_TOUCH_INTRO);
                break;

            /*****************
             * TOUCH SECTION *
             *****************/

            /**
             * Touch section ready
             * Play touch intro
             */
            case TutorialStates.TUT_R01_TOUCH_INTRO + READY:
                t.play(TutorialStates.TUT_R01_TOUCH_INTRO);
                break;

            /**
             * Touch intro complete
             * Play touch audio
             * Run touch demo
             */
            case TutorialStates.TUT_R01_TOUCH_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_TOUCH_DEMO);
                t.runTouchDemo(TutorialStates.TUT_R01_TOUCH_DEMO);
                break;

            /**
             * Touch demo complete
             * Play touch practice prompt
             */
            case TutorialStates.TUT_R01_TOUCH_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_TOUCH_PROMPT);
                break;

            /**
             * Touch practice prompt complete
             * Ready touch practice
             */
            case TutorialStates.TUT_R01_TOUCH_PROMPT + PLAY_COMPLETE:
                t.readyTouchPractice(TutorialStates.TUT_R01_TOUCH_PRACTICE);
                break;

            /**
             * Touch practice complete
             * Play touch practice affirmation
             */
            case TutorialStates.TUT_R01_TOUCH_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_TOUCH_PRACTICE);
                break;

            /**
             * Touch practice affirmation complete
             * Ready drag section
             */
            case TutorialStates.TUT_R01_TOUCH_PRACTICE + PLAY_COMPLETE:
                t.readyDrag(TutorialStates.TUT_R01_DRAG_INTRO);
                break;

            /*****************
             * DRAG SECTION *
             *****************/

            /**
             * Drag section ready
             * Play drag intro
             */
            case TutorialStates.TUT_R01_DRAG_INTRO + READY:
                t.play(TutorialStates.TUT_R01_DRAG_INTRO);
                break;

            /**
             * Drag intro complete
             * Play drag audio
             * Run drag demo
             */
            case TutorialStates.TUT_R01_DRAG_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAG_DEMO);
                t.runDragDemo(TutorialStates.TUT_R01_DRAG_DEMO);
                break;

            /**
             * Drag demo complete
             * Play drag practice prompt
             */
            case TutorialStates.TUT_R01_DRAG_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAG_PROMPT);
                break;

            /**
             * Drag practice prompt complete
             * Ready drag practice
             */
            case TutorialStates.TUT_R01_DRAG_PROMPT + PLAY_COMPLETE:
                t.readyDragPractice(TutorialStates.TUT_R01_DRAG_PRACTICE);
                break;

            /**
             * Drag practice complete
             * Play drag practice affirmation
             */
            case TutorialStates.TUT_R01_DRAG_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAG_PRACTICE);
                break;

            /**
             * Drag practice affirmation complete
             * Ready draw section
             */
            case TutorialStates.TUT_R01_DRAG_PRACTICE + PLAY_COMPLETE:
                t.readyDraw(TutorialStates.TUT_R01_DRAW_INTRO);
                break;

            /*****************
             * DRAW SECTION *
             *****************/

            /**
             * Draw section ready
             * Play draw intro
             */
            case TutorialStates.TUT_R01_DRAW_INTRO + READY:
                t.play(TutorialStates.TUT_R01_DRAW_INTRO);
                break;

            /**
             * Draw intro complete
             * Play draw audio
             * Run draw demo
             */
            case TutorialStates.TUT_R01_DRAW_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAW_DEMO);
                t.runDrawDemo(TutorialStates.TUT_R01_DRAW_DEMO);
                break;

            /**
             * Draw demo complete
             * Play draw practice prompt
             */
            case TutorialStates.TUT_R01_DRAW_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAW_PROMPT);
                break;

            /**
             * Draw practice prompt complete
             * Ready draw practice
             */
            case TutorialStates.TUT_R01_DRAW_PROMPT + PLAY_COMPLETE:
                t.readyDrawPractice(TutorialStates.TUT_R01_DRAW_PRACTICE);
                break;

            /**
             * Draw practice complete
             * Play draw practice affirmation
             */
            case TutorialStates.TUT_R01_DRAW_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_DRAW_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R01_DRAW_PRACTICE + PLAY_COMPLETE:
                t.readyPause(TutorialStates.TUT_R01_PAUSE_INTRO);
                break;

            /*****************
             * PAUSE SECTION *
             *****************/

            /**
             * Pause section ready
             * Play pause intro
             */
            case TutorialStates.TUT_R01_PAUSE_INTRO + READY:
                t.play(TutorialStates.TUT_R01_PAUSE_INTRO);
                break;

            /**
             * Pause intro complete
             * Play pause audio
             * Run pause demo
             */
            case TutorialStates.TUT_R01_PAUSE_INTRO + PLAY_COMPLETE:
                t.play(TutorialStates.TUT_R01_PAUSE_DEMO);
                t.runPauseDemo(TutorialStates.TUT_R01_PAUSE_DEMO);
                break;

            /**
             * Pause demo complete
             * Play pause practice prompt
             */
            case TutorialStates.TUT_R01_PAUSE_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R01_PAUSE_PROMPT);
                break;

            /**
             * Pause practice prompt complete
             * Ready pause practice
             */
            case TutorialStates.TUT_R01_PAUSE_PROMPT + PLAY_COMPLETE:
                t.readyPausePractice(TutorialStates.TUT_R01_PAUSE_PRACTICE);
                break;

            /**
             * Pause practice complete
             * Play pause practice affirmation
             */
            case TutorialStates.TUT_R01_PAUSE_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R01_PAUSE_PRACTICE);
                break;

            /**
             * Pause practice affirmation complete
             * Ready pause section
             */
            case TutorialStates.TUT_R01_PAUSE_PRACTICE + PLAY_COMPLETE:
                t.readyIntro(TutorialStates.TUT_R02_INTRO);
                break;

            /*****************
             * R02 SECTION *
             *****************/

            /**
             * Play tutorial  intro
             */
            case TutorialStates.TUT_R02_INTRO + READY:
                t.play(TutorialStates.TUT_R02_INTRO);
                break;

            /**
             * Tutorial R02 intro complete
             * Ready start section
             */
            case TutorialStates.TUT_R02_INTRO + PLAY_COMPLETE:
                t.readyStart(TutorialStates.TUT_R02_START_INTRO);
                break;

            /*****************
             * START SECTION *
             *****************/

            /**
             * Start section ready
             * Play start audio
             * Run start demo
             */
            case TutorialStates.TUT_R02_START_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_START_DEMO);
                        t.runStartDemo(TutorialStates.TUT_R02_START_DEMO);
                    }
                }, 500);
                break;

            /**
             * Start demo complete
             * Play start practice prompt
             */
            case TutorialStates.TUT_R02_START_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_START_PROMPT);
                break;

            /**
             * Start practice prompt complete
             * Ready start practice
             */
            case TutorialStates.TUT_R02_START_PROMPT + PLAY_COMPLETE:
                t.readyStartPractice(TutorialStates.TUT_R02_START_PRACTICE);
                break;

            /**
             * Start practice complete
             * Play start practice affirmation
             */
            case TutorialStates.TUT_R02_START_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_START_PRACTICE);
                break;

            /**
             * Start practice affirmation complete
             * Ready play section
             */
            case TutorialStates.TUT_R02_START_PRACTICE + PLAY_COMPLETE:
                t.readyPlay(TutorialStates.TUT_R02_PLAY_INTRO);
                break;

            /****************
             * PLAY SECTION *
             ****************/

            /**
             * Play section ready
             * Play play audio
             * Run play demo
             */
            case TutorialStates.TUT_R02_PLAY_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_PLAY_DEMO);
                        t.runPlayDemo(TutorialStates.TUT_R02_PLAY_DEMO);
                    }
                }, 500);
                break;

            /**
             * Play demo complete
             * Play play practice prompt
             */
            case TutorialStates.TUT_R02_PLAY_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_PLAY_PROMPT);
                break;

            /**
             * Play practice prompt complete
             * Ready play practice
             */
            case TutorialStates.TUT_R02_PLAY_PROMPT + PLAY_COMPLETE:
                t.readyPlayPractice(TutorialStates.TUT_R02_PLAY_PRACTICE);
                break;

            /**
             * Play practice complete
             * Play play practice affirmation
             */
            case TutorialStates.TUT_R02_PLAY_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_PLAY_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R02_PLAY_PRACTICE + PLAY_COMPLETE:
                t.readyStop(TutorialStates.TUT_R02_STOP_INTRO);
                break;

            /****************
             * STOP SECTION *
             ****************/

            /**
             * Play section ready
             * Play play audio
             * Run play demo
             */
            case TutorialStates.TUT_R02_STOP_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_STOP_DEMO);
                        t.runStopDemo(TutorialStates.TUT_R02_STOP_DEMO);
                    }
                }, 500);
                break;

            /**
             * Play demo complete
             * Play play practice prompt
             */
            case TutorialStates.TUT_R02_STOP_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_STOP_PROMPT);
                break;

            /**
             * Play practice prompt complete
             * Ready play practice
             */
            case TutorialStates.TUT_R02_STOP_PROMPT + PLAY_COMPLETE:
                t.readyStopPractice(TutorialStates.TUT_R02_STOP_PRACTICE);
                break;

            /**
             * Play practice complete
             * Play play practice affirmation
             */
            case TutorialStates.TUT_R02_STOP_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_STOP_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R02_STOP_PRACTICE + PLAY_COMPLETE:
                t.readyTouch(TutorialStates.TUT_R02_TOUCH_INTRO);
                break;

            /*****************
             * TOUCH SECTION *
             *****************/

            /**
             * Touch section ready
             * Play touch audio
             * Run touch demo
             */
            case TutorialStates.TUT_R02_TOUCH_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            t.play(TutorialStates.TUT_R02_TOUCH_DEMO);
                            t.runTouchDemo(TutorialStates.TUT_R02_TOUCH_DEMO);
                    }
                }, 500);
                break;

            /**
             * Touch demo complete
             * Play touch practice prompt
             */
            case TutorialStates.TUT_R02_TOUCH_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_TOUCH_PROMPT);
                break;

            /**
             * Touch practice prompt complete
             * Ready touch practice
             */
            case TutorialStates.TUT_R02_TOUCH_PROMPT + PLAY_COMPLETE:
                t.readyTouchPractice(TutorialStates.TUT_R02_TOUCH_PRACTICE);
                break;

            /**
             * Touch practice complete
             * Play touch practice affirmation
             */
            case TutorialStates.TUT_R02_TOUCH_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_TOUCH_PRACTICE);
                break;

            /**
             * Touch practice affirmation complete
             * Ready drag section
             */
            case TutorialStates.TUT_R02_TOUCH_PRACTICE + PLAY_COMPLETE:
                t.readyDrag(TutorialStates.TUT_R02_DRAG_INTRO);
                break;

            /*****************
             * DRAG SECTION *
             *****************/

            /**
             * Drag section ready
             * Play drag audio
             * Run drag demo
             */
            case TutorialStates.TUT_R02_DRAG_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_DRAG_DEMO);
                        t.runDragDemo(TutorialStates.TUT_R02_DRAG_DEMO);
                    }
                }, 500);
                break;

            /**
             * Drag demo complete
             * Play drag practice prompt
             */
            case TutorialStates.TUT_R02_DRAG_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_DRAG_PROMPT);
                break;

            /**
             * Drag practice prompt complete
             * Ready drag practice
             */
            case TutorialStates.TUT_R02_DRAG_PROMPT + PLAY_COMPLETE:
                t.readyDragPractice(TutorialStates.TUT_R02_DRAG_PRACTICE);
                break;

            /**
             * Drag practice complete
             * Play drag practice affirmation
             */
            case TutorialStates.TUT_R02_DRAG_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_DRAG_PRACTICE);
                break;

            /**
             * Drag practice affirmation complete
             * Ready draw section
             */
            case TutorialStates.TUT_R02_DRAG_PRACTICE + PLAY_COMPLETE:
                t.readyDraw(TutorialStates.TUT_R02_DRAW_INTRO);
                break;

            /*****************
             * DRAW SECTION *
             *****************/

            /**
             * Draw section ready
             * Play draw audio
             * Run draw demo
             */
            case TutorialStates.TUT_R02_DRAW_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_DRAW_DEMO);
                        t.runDrawDemo(TutorialStates.TUT_R02_DRAW_DEMO);
                    }
                }, 500);
                break;

            /**
             * Draw demo complete
             * Play draw practice prompt
             */
            case TutorialStates.TUT_R02_DRAW_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_DRAW_PROMPT);
                break;

            /**
             * Draw practice prompt complete
             * Ready draw practice
             */
            case TutorialStates.TUT_R02_DRAW_PROMPT + PLAY_COMPLETE:
                t.readyDrawPractice(TutorialStates.TUT_R02_DRAW_PRACTICE);
                break;

            /**
             * Draw practice complete
             * Play draw practice affirmation
             */
            case TutorialStates.TUT_R02_DRAW_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_DRAW_PRACTICE);
                break;

            /**
             * Play practice affirmation complete
             * Ready stop section
             */
            case TutorialStates.TUT_R02_DRAW_PRACTICE + PLAY_COMPLETE:
                t.readyPause(TutorialStates.TUT_R02_PAUSE_INTRO);
                break;

            /*****************
             * PAUSE SECTION *
             *****************/

            /**
             * Pause section ready
             * Play pause audio
             * Run pause demo
             */
            case TutorialStates.TUT_R02_PAUSE_INTRO + READY:
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t.play(TutorialStates.TUT_R02_PAUSE_DEMO);
                        t.runPauseDemo(TutorialStates.TUT_R02_PAUSE_DEMO);
                    }
                }, 500);
                break;

            /**
             * Pause demo complete
             * Play pause practice prompt
             */
            case TutorialStates.TUT_R02_PAUSE_DEMO + ANIM_COMPLETE:
                t.play(TutorialStates.TUT_R02_PAUSE_PROMPT);
                break;

            /**
             * Pause practice prompt complete
             * Ready pause practice
             */
            case TutorialStates.TUT_R02_PAUSE_PROMPT + PLAY_COMPLETE:
                t.readyPausePractice(TutorialStates.TUT_R02_PAUSE_PRACTICE);
                break;

            /**
             * Pause practice complete
             * Play pause practice affirmation
             */
            case TutorialStates.TUT_R02_PAUSE_PRACTICE + TASK_COMPLETE:
                t.play(TutorialStates.TUT_R02_PAUSE_PRACTICE);
                break;

            /**
             * Pause practice affirmation complete
             * Ready pause section
             */
            case TutorialStates.TUT_R02_PAUSE_PRACTICE + PLAY_COMPLETE:
                t.readyIntro(TutorialStates.TUT_END);
                break;

            /**
             * Tutorial end is nigh
             * Play end audio
             */
            case TutorialStates.TUT_END + READY:
                // Play tutorial ending audio
                t.play(TutorialStates.TUT_END);
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Show chapter 01 splash screen
                        t.showChapter01Splash(TutorialStates.TUT_END);
                    }
                }, 1300);
                break;

            /**
             * End audio ended
             */
            case TutorialStates.TUT_END + PLAY_COMPLETE:
                t.transitionToChapter01(TutorialStates.TUT_END);
                break;
            default:
                break;
        }
    }
}


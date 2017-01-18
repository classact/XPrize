package classact.com.xprize.activity.tutorial;

import java.util.HashMap;
import java.util.Map;

import classact.com.xprize.R;
import classact.com.xprize.common.Globals;
import classact.com.xprize.locale.Languages;

/**
 * Created by hyunchanjeong on 2017/01/13.
 */

public class TutorialAudioResources {

    private static boolean initialized = false;

    private static Map<String, Integer> englishResources = new HashMap<>();
    private static Map<String, Integer> swahiliResources = new HashMap<>();

    private static int[] englishAffirmations = {
            R.raw.amazing, R.raw.awesome, R.raw.cool, R.raw.excellent,
            R.raw.fantastic, R.raw.good_job, R.raw.goodeffort, R.raw.hooray,
            R.raw.i_like_it, R.raw.nice, R.raw.nicelydone, R.raw.nicework,
            R.raw.welldone, R.raw.whoohoo, R.raw.whoohoo_2, R.raw.wicked,
            R.raw.yeah, R.raw.yippeee
    };

    private static int[] swahiliAffirmations = {
            R.raw.s_amazing, R.raw.s_congrats1, R.raw.s_congrats2,
            R.raw.s_cool, R.raw.s_goodjob
    };

    public static void init() {
        if (!initialized) {
            /// MAP ENGLISH RESOURCES ///
            /// R01 ///
            englishResources.put("TUT_R01_INTRO", R.raw.intro5);
            englishResources.put("TUT_R01_START_INTRO", R.raw.intro6);
            englishResources.put("TUT_R01_START_DEMO", R.raw.intro7);
            englishResources.put("TUT_R01_START_PROMPT", R.raw.intro8);
            englishResources.put("TUT_R01_START_PRACTICE", -1);
            englishResources.put("TUT_R01_PLAY_INTRO", R.raw.intro9);
            englishResources.put("TUT_R01_PLAY_DEMO", R.raw.intro10);
            englishResources.put("TUT_R01_PLAY_PROMPT", R.raw.intro11);
            englishResources.put("TUT_R01_PLAY_PRACTICE", -1);
            englishResources.put("TUT_R01_STOP_INTRO", R.raw.intro12);
            englishResources.put("TUT_R01_STOP_DEMO", R.raw.intro13);
            englishResources.put("TUT_R01_STOP_PROMPT", R.raw.intro14);
            englishResources.put("TUT_R01_STOP_PRACTICE", -1);
            englishResources.put("TUT_R01_TOUCH_INTRO", R.raw.intro15);
            englishResources.put("TUT_R01_TOUCH_DEMO", R.raw.intro16);
            englishResources.put("TUT_R01_TOUCH_PROMPT", R.raw.intro17);
            englishResources.put("TUT_R01_TOUCH_PRACTICE", -1);
            englishResources.put("TUT_R01_DRAG_INTRO", R.raw.intro18);
            englishResources.put("TUT_R01_DRAG_DEMO", R.raw.intro19);
            englishResources.put("TUT_R01_DRAG_PROMPT", R.raw.intro20);
            englishResources.put("TUT_R01_DRAG_PRACTICE", -1);
            englishResources.put("TUT_R01_DRAW_INTRO", R.raw.intro21);
            englishResources.put("TUT_R01_DRAW_DEMO", R.raw.intro22);
            englishResources.put("TUT_R01_DRAW_PROMPT", R.raw.intro23);
            englishResources.put("TUT_R01_DRAW_PRACTICE", -1);
            englishResources.put("TUT_R01_PAUSE_INTRO", R.raw.intro24);
            englishResources.put("TUT_R01_PAUSE_DEMO", R.raw.intro25);
            englishResources.put("TUT_R01_PAUSE_PROMPT", R.raw.intro26);
            englishResources.put("TUT_R01_PAUSE_PRACTICE", -1);
            /// R02 ///
            englishResources.put("TUT_R02_INTRO", R.raw.intro27);
            englishResources.put("TUT_R02_START_DEMO", R.raw.intro28);
            englishResources.put("TUT_R02_START_PROMPT", R.raw.intro29);
            englishResources.put("TUT_R02_START_PRACTICE", -1);
            englishResources.put("TUT_R02_PLAY_DEMO", R.raw.intro30);
            englishResources.put("TUT_R02_PLAY_PROMPT", R.raw.intro31);
            englishResources.put("TUT_R02_PLAY_PRACTICE", -1);
            englishResources.put("TUT_R02_STOP_DEMO", R.raw.intro32);
            englishResources.put("TUT_R02_STOP_PROMPT", R.raw.intro33);
            englishResources.put("TUT_R02_STOP_PRACTICE", -1);
            englishResources.put("TUT_R02_TOUCH_DEMO", R.raw.intro34);
            englishResources.put("TUT_R02_TOUCH_PROMPT", R.raw.intro35);
            englishResources.put("TUT_R02_TOUCH_PRACTICE", -1);
            englishResources.put("TUT_R02_DRAG_DEMO", R.raw.intro36);
            englishResources.put("TUT_R02_DRAG_PROMPT", R.raw.intro37);
            englishResources.put("TUT_R02_DRAG_PRACTICE", -1);
            englishResources.put("TUT_R02_DRAW_DEMO", R.raw.intro38);
            englishResources.put("TUT_R02_DRAW_PROMPT", R.raw.intro39);
            englishResources.put("TUT_R02_DRAW_PRACTICE", -1);
            englishResources.put("TUT_R02_PAUSE_DEMO", R.raw.intro40);
            englishResources.put("TUT_R02_PAUSE_PROMPT", R.raw.intro41);
            englishResources.put("TUT_R02_PAUSE_PRACTICE", -1);
            /// END ///
            englishResources.put("TUT_END", R.raw.intro42);

            /// MAP SWAHILI RESOURCES ///
            /// R01 ///
            swahiliResources.put("TUT_R01_INTRO", R.raw.s_intro5);
            swahiliResources.put("TUT_R01_START_INTRO", R.raw.s_intro6);
            swahiliResources.put("TUT_R01_START_DEMO", R.raw.s_intro7);
            swahiliResources.put("TUT_R01_START_PROMPT", R.raw.s_intro8); // Fix
            swahiliResources.put("TUT_R01_START_PRACTICE", -1);
            swahiliResources.put("TUT_R01_PLAY_INTRO", R.raw.s_intro9);
            swahiliResources.put("TUT_R01_PLAY_DEMO", R.raw.s_intro10);
            swahiliResources.put("TUT_R01_PLAY_PROMPT", R.raw.s_intro11);
            swahiliResources.put("TUT_R01_PLAY_PRACTICE", -1);
            swahiliResources.put("TUT_R01_STOP_INTRO", R.raw.s_intro12);
            swahiliResources.put("TUT_R01_STOP_DEMO", R.raw.s_intro13);
            swahiliResources.put("TUT_R01_STOP_PROMPT", R.raw.s_intro14);
            swahiliResources.put("TUT_R01_STOP_PRACTICE", -1);
            swahiliResources.put("TUT_R01_TOUCH_INTRO", R.raw.s_intro15);
            swahiliResources.put("TUT_R01_TOUCH_DEMO", R.raw.s_intro16);
            swahiliResources.put("TUT_R01_TOUCH_PROMPT", R.raw.s_intro17);
            swahiliResources.put("TUT_R01_TOUCH_PRACTICE", -1);
            swahiliResources.put("TUT_R01_DRAG_INTRO", R.raw.s_intro18);
            swahiliResources.put("TUT_R01_DRAG_DEMO", R.raw.s_intro19);
            swahiliResources.put("TUT_R01_DRAG_PROMPT", R.raw.s_intro20);
            swahiliResources.put("TUT_R01_DRAG_PRACTICE", -1);
            swahiliResources.put("TUT_R01_DRAW_INTRO", R.raw.s_intro21);
            swahiliResources.put("TUT_R01_DRAW_DEMO", R.raw.s_intro22);
            swahiliResources.put("TUT_R01_DRAW_PROMPT", R.raw.s_intro23);
            swahiliResources.put("TUT_R01_DRAW_PRACTICE", -1);
            swahiliResources.put("TUT_R01_PAUSE_INTRO", R.raw.s_intro24);
            swahiliResources.put("TUT_R01_PAUSE_DEMO", R.raw.s_intro25);
            swahiliResources.put("TUT_R01_PAUSE_PROMPT", R.raw.s_intro26);
            swahiliResources.put("TUT_R01_PAUSE_PRACTICE", -1);
            /// R02 ///
            swahiliResources.put("TUT_R02_INTRO", R.raw.s_intro27);
            swahiliResources.put("TUT_R02_START_DEMO", R.raw.s_intro28);
            swahiliResources.put("TUT_R02_START_PROMPT", R.raw.s_intro29);
            swahiliResources.put("TUT_R02_START_PRACTICE", -1);
            swahiliResources.put("TUT_R02_PLAY_DEMO", R.raw.s_intro30);
            swahiliResources.put("TUT_R02_PLAY_PROMPT", R.raw.s_intro31);
            swahiliResources.put("TUT_R02_PLAY_PRACTICE", -1);
            swahiliResources.put("TUT_R02_STOP_DEMO", R.raw.s_intro32);
            swahiliResources.put("TUT_R02_STOP_PROMPT", R.raw.s_intro33);
            swahiliResources.put("TUT_R02_STOP_PRACTICE", -1);
            swahiliResources.put("TUT_R02_TOUCH_DEMO", R.raw.s_intro34);
            swahiliResources.put("TUT_R02_TOUCH_PROMPT", R.raw.s_intro35);
            swahiliResources.put("TUT_R02_TOUCH_PRACTICE", -1);
            swahiliResources.put("TUT_R02_DRAG_DEMO", R.raw.s_intro36);
            swahiliResources.put("TUT_R02_DRAG_PROMPT", R.raw.s_intro37);
            swahiliResources.put("TUT_R02_DRAG_PRACTICE", -1);
            swahiliResources.put("TUT_R02_DRAW_DEMO", R.raw.s_intro38);
            swahiliResources.put("TUT_R02_DRAW_PROMPT", R.raw.s_intro39);
            swahiliResources.put("TUT_R02_DRAW_PRACTICE", -1);
            swahiliResources.put("TUT_R02_PAUSE_DEMO", R.raw.s_intro40);
            swahiliResources.put("TUT_R02_PAUSE_PROMPT", R.raw.s_intro41);
            swahiliResources.put("TUT_R02_PAUSE_PRACTICE", -1);
            /// END ///
            swahiliResources.put("TUT_END", R.raw.s_intro42);

            /// Update initialized boolean ///
            initialized = true;
        }
    }

    public static int get(String state) {
        if (!initialized) {
            init();
        }

        if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
            try {
                return englishResources.get(state);
            } catch (Exception ex) {
                System.err.println("Error retrieving tutorial English audio resources: " + ex.getMessage());
            }
        } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            try {
                return swahiliResources.get(state);
            } catch (Exception ex) {
                System.err.println("Error retrieving tutorial Swahili audio resources: " + ex.getMessage());
            }
        } else {
            System.err.println("Error with Globals.selectedLanguage");
        }
        return 0;
    }

    public static int getAffirmation(int i) {
        try {
            if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
                return englishAffirmations[i];
            } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                return swahiliAffirmations[i];
            }
        } catch (Exception ex) {
            System.err.println("Error retrieving affirmations: " + ex.getMessage());
        }

        return 0;
    }

    public static int noOfAffirmations() {
        if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
            return englishAffirmations.length;
        } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
            return swahiliAffirmations.length;
        } else {
            return 0;
        }
    }
}
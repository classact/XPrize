package classact.com.clever_little_monkey.activity.drill.tutorial;

import java.util.HashMap;
import java.util.Map;

import classact.com.clever_little_monkey.common.Globals;
import classact.com.clever_little_monkey.locale.Languages;

/**
 * Created by hyunchanjeong on 2017/01/13.
 */

public class TutorialAudioResources {

    private static boolean initialized = false;

    /* OLD CODE
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
            englishResources.put("TUT_R01_START_PRACTICE", "-1");
            englishResources.put("TUT_R01_PLAY_INTRO", R.raw.intro9);
            englishResources.put("TUT_R01_PLAY_DEMO", R.raw.intro10);
            englishResources.put("TUT_R01_PLAY_PROMPT", R.raw.intro11);
            englishResources.put("TUT_R01_PLAY_PRACTICE", "-1");
            englishResources.put("TUT_R01_STOP_INTRO", R.raw.intro12);
            englishResources.put("TUT_R01_STOP_DEMO", R.raw.intro13);
            englishResources.put("TUT_R01_STOP_PROMPT", R.raw.intro14);
            englishResources.put("TUT_R01_STOP_PRACTICE", "-1");
            englishResources.put("TUT_R01_TOUCH_INTRO", R.raw.intro15);
            englishResources.put("TUT_R01_TOUCH_DEMO", R.raw.intro16);
            englishResources.put("TUT_R01_TOUCH_PROMPT", R.raw.intro17);
            englishResources.put("TUT_R01_TOUCH_PRACTICE", "-1");
            englishResources.put("TUT_R01_DRAG_INTRO", R.raw.intro18);
            englishResources.put("TUT_R01_DRAG_DEMO", R.raw.intro19);
            englishResources.put("TUT_R01_DRAG_PROMPT", R.raw.intro20);
            englishResources.put("TUT_R01_DRAG_PRACTICE", "-1");
            englishResources.put("TUT_R01_DRAW_INTRO", R.raw.intro21);
            englishResources.put("TUT_R01_DRAW_DEMO", R.raw.intro22);
            englishResources.put("TUT_R01_DRAW_PROMPT", R.raw.intro23);
            englishResources.put("TUT_R01_DRAW_PRACTICE", "-1");
            englishResources.put("TUT_R01_PAUSE_INTRO", R.raw.intro24);
            englishResources.put("TUT_R01_PAUSE_DEMO", R.raw.intro25);
            englishResources.put("TUT_R01_PAUSE_PROMPT", R.raw.intro26);
            englishResources.put("TUT_R01_PAUSE_PRACTICE", "-1");
            /// R02 ///
            englishResources.put("TUT_R02_INTRO", R.raw.intro27);
            englishResources.put("TUT_R02_START_DEMO", R.raw.intro28);
            englishResources.put("TUT_R02_START_PROMPT", R.raw.intro29);
            englishResources.put("TUT_R02_START_PRACTICE", "-1");
            englishResources.put("TUT_R02_PLAY_DEMO", R.raw.intro30);
            englishResources.put("TUT_R02_PLAY_PROMPT", R.raw.intro31);
            englishResources.put("TUT_R02_PLAY_PRACTICE", "-1");
            englishResources.put("TUT_R02_STOP_DEMO", R.raw.intro32);
            englishResources.put("TUT_R02_STOP_PROMPT", R.raw.intro33);
            englishResources.put("TUT_R02_STOP_PRACTICE", "-1");
            englishResources.put("TUT_R02_TOUCH_DEMO", R.raw.intro34);
            englishResources.put("TUT_R02_TOUCH_PROMPT", R.raw.intro35);
            englishResources.put("TUT_R02_TOUCH_PRACTICE", "-1");
            englishResources.put("TUT_R02_DRAG_DEMO", R.raw.intro36);
            englishResources.put("TUT_R02_DRAG_PROMPT", R.raw.intro37);
            englishResources.put("TUT_R02_DRAG_PRACTICE", "-1");
            englishResources.put("TUT_R02_DRAW_DEMO", R.raw.intro38);
            englishResources.put("TUT_R02_DRAW_PROMPT", R.raw.intro39);
            englishResources.put("TUT_R02_DRAW_PRACTICE", "-1");
            englishResources.put("TUT_R02_PAUSE_DEMO", R.raw.intro40);
            englishResources.put("TUT_R02_PAUSE_PROMPT", R.raw.intro41);
            englishResources.put("TUT_R02_PAUSE_PRACTICE", "-1");
            /// END ///
            englishResources.put("TUT_END", R.raw.intro42);

            /// MAP SWAHILI RESOURCES ///
            /// R01 ///
            swahiliResources.put("TUT_R01_INTRO", R.raw.s_intro5);
            swahiliResources.put("TUT_R01_START_INTRO", R.raw.s_intro6);
            swahiliResources.put("TUT_R01_START_DEMO", R.raw.s_intro7);
            swahiliResources.put("TUT_R01_START_PROMPT", R.raw.s_intro8); // Fix
            swahiliResources.put("TUT_R01_START_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_PLAY_INTRO", R.raw.s_intro9);
            swahiliResources.put("TUT_R01_PLAY_DEMO", R.raw.s_intro10);
            swahiliResources.put("TUT_R01_PLAY_PROMPT", R.raw.s_intro11);
            swahiliResources.put("TUT_R01_PLAY_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_STOP_INTRO", R.raw.s_intro12);
            swahiliResources.put("TUT_R01_STOP_DEMO", R.raw.s_intro13);
            swahiliResources.put("TUT_R01_STOP_PROMPT", R.raw.s_intro14);
            swahiliResources.put("TUT_R01_STOP_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_TOUCH_INTRO", R.raw.s_intro15);
            swahiliResources.put("TUT_R01_TOUCH_DEMO", R.raw.s_intro16);
            swahiliResources.put("TUT_R01_TOUCH_PROMPT", R.raw.s_intro17);
            swahiliResources.put("TUT_R01_TOUCH_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_DRAG_INTRO", R.raw.s_intro18);
            swahiliResources.put("TUT_R01_DRAG_DEMO", R.raw.s_intro19);
            swahiliResources.put("TUT_R01_DRAG_PROMPT", R.raw.s_intro20);
            swahiliResources.put("TUT_R01_DRAG_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_DRAW_INTRO", R.raw.s_intro21);
            swahiliResources.put("TUT_R01_DRAW_DEMO", R.raw.s_intro22);
            swahiliResources.put("TUT_R01_DRAW_PROMPT", R.raw.s_intro23);
            swahiliResources.put("TUT_R01_DRAW_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_PAUSE_INTRO", R.raw.s_intro24);
            swahiliResources.put("TUT_R01_PAUSE_DEMO", R.raw.s_intro25);
            swahiliResources.put("TUT_R01_PAUSE_PROMPT", R.raw.s_intro26);
            swahiliResources.put("TUT_R01_PAUSE_PRACTICE", "-1");
            /// R02 ///
            swahiliResources.put("TUT_R02_INTRO", R.raw.s_intro27);
            swahiliResources.put("TUT_R02_START_DEMO", R.raw.s_intro28);
            swahiliResources.put("TUT_R02_START_PROMPT", R.raw.s_intro29);
            swahiliResources.put("TUT_R02_START_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_PLAY_DEMO", R.raw.s_intro30);
            swahiliResources.put("TUT_R02_PLAY_PROMPT", R.raw.s_intro31);
            swahiliResources.put("TUT_R02_PLAY_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_STOP_DEMO", R.raw.s_intro32);
            swahiliResources.put("TUT_R02_STOP_PROMPT", R.raw.s_intro33);
            swahiliResources.put("TUT_R02_STOP_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_TOUCH_DEMO", R.raw.s_intro34);
            swahiliResources.put("TUT_R02_TOUCH_PROMPT", R.raw.s_intro35);
            swahiliResources.put("TUT_R02_TOUCH_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_DRAG_DEMO", R.raw.s_intro36);
            swahiliResources.put("TUT_R02_DRAG_PROMPT", R.raw.s_intro37);
            swahiliResources.put("TUT_R02_DRAG_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_DRAW_DEMO", R.raw.s_intro38);
            swahiliResources.put("TUT_R02_DRAW_PROMPT", R.raw.s_intro39);
            swahiliResources.put("TUT_R02_DRAW_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_PAUSE_DEMO", R.raw.s_intro40);
            swahiliResources.put("TUT_R02_PAUSE_PROMPT", R.raw.s_intro41);
            swahiliResources.put("TUT_R02_PAUSE_PRACTICE", "-1");
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
    */

    private static Map<String, String> englishResources = new HashMap<>();
    private static Map<String, String> swahiliResources = new HashMap<>();

    private static String[] englishAffirmations = {
            "amazing", "awesome", "cool", "excellent",
            "fantastic", "good_job", "goodeffort", "hooray",
            "i_like_it", "nice", "nicelydone", "nicework",
            "welldone", "whoohoo", "whoohoo_2", "wicked",
            "yeah", "yippeee"
    };

    private static String[] swahiliAffirmations = {
            "s_amazing", "s_congrats1", "s_congrats2",
            "s_cool", "s_goodjob"
    };

    public static void init() {
        if (!initialized) {
            /// MAP ENGLISH RESOURCES ///
            /// R01 ///
            englishResources.put("TUT_R01_INTRO", "intro5");
            englishResources.put("TUT_R01_START_INTRO", "intro6");
            englishResources.put("TUT_R01_START_DEMO", "intro7");
            englishResources.put("TUT_R01_START_PROMPT", "intro8");
            englishResources.put("TUT_R01_START_PRACTICE", "-1");
            englishResources.put("TUT_R01_PLAY_INTRO", "intro9");
            englishResources.put("TUT_R01_PLAY_DEMO", "intro10");
            englishResources.put("TUT_R01_PLAY_PROMPT", "intro11");
            englishResources.put("TUT_R01_PLAY_PRACTICE", "-1");
            englishResources.put("TUT_R01_STOP_INTRO", "intro12");
            englishResources.put("TUT_R01_STOP_DEMO", "intro13");
            englishResources.put("TUT_R01_STOP_PROMPT", "intro14");
            englishResources.put("TUT_R01_STOP_PRACTICE", "-1");
            englishResources.put("TUT_R01_TOUCH_INTRO", "intro15");
            englishResources.put("TUT_R01_TOUCH_DEMO", "intro16");
            englishResources.put("TUT_R01_TOUCH_PROMPT", "intro17");
            englishResources.put("TUT_R01_TOUCH_PRACTICE", "-1");
            englishResources.put("TUT_R01_DRAG_INTRO", "intro18");
            englishResources.put("TUT_R01_DRAG_DEMO", "intro19");
            englishResources.put("TUT_R01_DRAG_PROMPT", "intro20");
            englishResources.put("TUT_R01_DRAG_PRACTICE", "-1");
            englishResources.put("TUT_R01_DRAW_INTRO", "intro21");
            englishResources.put("TUT_R01_DRAW_DEMO", "intro22");
            englishResources.put("TUT_R01_DRAW_PROMPT", "intro23");
            englishResources.put("TUT_R01_DRAW_PRACTICE", "-1");
            englishResources.put("TUT_R01_PAUSE_INTRO", "intro24");
            englishResources.put("TUT_R01_PAUSE_DEMO", "intro25");
            englishResources.put("TUT_R01_PAUSE_PROMPT", "intro26");
            englishResources.put("TUT_R01_PAUSE_PRACTICE", "-1");
            /// R02 ///
            englishResources.put("TUT_R02_INTRO", "intro27");
            englishResources.put("TUT_R02_START_DEMO", "intro28");
            englishResources.put("TUT_R02_START_PROMPT", "intro29");
            englishResources.put("TUT_R02_START_PRACTICE", "-1");
            englishResources.put("TUT_R02_PLAY_DEMO", "intro30");
            englishResources.put("TUT_R02_PLAY_PROMPT", "intro31");
            englishResources.put("TUT_R02_PLAY_PRACTICE", "-1");
            englishResources.put("TUT_R02_STOP_DEMO", "intro32");
            englishResources.put("TUT_R02_STOP_PROMPT", "intro33");
            englishResources.put("TUT_R02_STOP_PRACTICE", "-1");
            englishResources.put("TUT_R02_TOUCH_DEMO", "intro34");
            englishResources.put("TUT_R02_TOUCH_PROMPT", "intro35");
            englishResources.put("TUT_R02_TOUCH_PRACTICE", "-1");
            englishResources.put("TUT_R02_DRAG_DEMO", "intro36");
            englishResources.put("TUT_R02_DRAG_PROMPT", "intro37");
            englishResources.put("TUT_R02_DRAG_PRACTICE", "-1");
            englishResources.put("TUT_R02_DRAW_DEMO", "intro38");
            englishResources.put("TUT_R02_DRAW_PROMPT", "intro39");
            englishResources.put("TUT_R02_DRAW_PRACTICE", "-1");
            englishResources.put("TUT_R02_PAUSE_DEMO", "intro40");
            englishResources.put("TUT_R02_PAUSE_PROMPT", "intro41");
            englishResources.put("TUT_R02_PAUSE_PRACTICE", "-1");
            /// END ///
            englishResources.put("TUT_END", "intro42");

            /// MAP SWAHILI RESOURCES ///
            /// R01 ///
            swahiliResources.put("TUT_R01_INTRO", "s_intro5");
            swahiliResources.put("TUT_R01_START_INTRO", "s_intro6");
            swahiliResources.put("TUT_R01_START_DEMO", "s_intro7");
            swahiliResources.put("TUT_R01_START_PROMPT", "s_intro8"); // Fix
            swahiliResources.put("TUT_R01_START_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_PLAY_INTRO", "s_intro9");
            swahiliResources.put("TUT_R01_PLAY_DEMO", "s_intro10");
            swahiliResources.put("TUT_R01_PLAY_PROMPT", "s_intro11");
            swahiliResources.put("TUT_R01_PLAY_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_STOP_INTRO", "s_intro12");
            swahiliResources.put("TUT_R01_STOP_DEMO", "s_intro13");
            swahiliResources.put("TUT_R01_STOP_PROMPT", "s_intro14");
            swahiliResources.put("TUT_R01_STOP_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_TOUCH_INTRO", "s_intro15");
            swahiliResources.put("TUT_R01_TOUCH_DEMO", "s_intro16");
            swahiliResources.put("TUT_R01_TOUCH_PROMPT", "s_intro17");
            swahiliResources.put("TUT_R01_TOUCH_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_DRAG_INTRO", "s_intro18");
            swahiliResources.put("TUT_R01_DRAG_DEMO", "s_intro19");
            swahiliResources.put("TUT_R01_DRAG_PROMPT", "s_intro20");
            swahiliResources.put("TUT_R01_DRAG_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_DRAW_INTRO", "s_intro21");
            swahiliResources.put("TUT_R01_DRAW_DEMO", "s_intro22");
            swahiliResources.put("TUT_R01_DRAW_PROMPT", "s_intro23");
            swahiliResources.put("TUT_R01_DRAW_PRACTICE", "-1");
            swahiliResources.put("TUT_R01_PAUSE_INTRO", "s_intro24");
            swahiliResources.put("TUT_R01_PAUSE_DEMO", "s_intro25");
            swahiliResources.put("TUT_R01_PAUSE_PROMPT", "s_intro26");
            swahiliResources.put("TUT_R01_PAUSE_PRACTICE", "-1");
            /// R02 ///
            swahiliResources.put("TUT_R02_INTRO", "s_intro27");
            swahiliResources.put("TUT_R02_START_DEMO", "s_intro28");
            swahiliResources.put("TUT_R02_START_PROMPT", "s_intro29");
            swahiliResources.put("TUT_R02_START_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_PLAY_DEMO", "s_intro30");
            swahiliResources.put("TUT_R02_PLAY_PROMPT", "s_intro31");
            swahiliResources.put("TUT_R02_PLAY_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_STOP_DEMO", "s_intro32");
            swahiliResources.put("TUT_R02_STOP_PROMPT", "s_intro33");
            swahiliResources.put("TUT_R02_STOP_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_TOUCH_DEMO", "s_intro34");
            swahiliResources.put("TUT_R02_TOUCH_PROMPT", "s_intro35");
            swahiliResources.put("TUT_R02_TOUCH_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_DRAG_DEMO", "s_intro36");
            swahiliResources.put("TUT_R02_DRAG_PROMPT", "s_intro37");
            swahiliResources.put("TUT_R02_DRAG_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_DRAW_DEMO", "s_intro38");
            swahiliResources.put("TUT_R02_DRAW_PROMPT", "s_intro39");
            swahiliResources.put("TUT_R02_DRAW_PRACTICE", "-1");
            swahiliResources.put("TUT_R02_PAUSE_DEMO", "s_intro40");
            swahiliResources.put("TUT_R02_PAUSE_PROMPT", "s_intro41");
            swahiliResources.put("TUT_R02_PAUSE_PRACTICE", "-1");
            /// END ///
            swahiliResources.put("TUT_END", "s_intro42");

            /// Update initialized boolean ///
            initialized = true;
        }
    }

    public static String get(String state) {
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
        return null;
    }

    public static String getAffirmation(int i) {
        try {
            if (Globals.SELECTED_LANGUAGE == Languages.ENGLISH) {
                return englishAffirmations[i];
            } else if (Globals.SELECTED_LANGUAGE == Languages.SWAHILI) {
                return swahiliAffirmations[i];
            }
        } catch (Exception ex) {
            System.err.println("Error retrieving affirmations: " + ex.getMessage());
        }

        return null;
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
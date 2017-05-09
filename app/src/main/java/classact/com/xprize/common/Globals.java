package classact.com.xprize.common;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import classact.com.xprize.locale.Languages;

public class Globals {
    // General constants
    public static final String RESOURCE_PREFIX = "android.resource://";

    // Unit/Drill-based constants
    public static final int INTRO_ID = 0;
    public static final int FINALE_ID = 21;

    public static final int PHONICS_STARTING_ID = 1;
    public static final int WORDS_STARTING_ID = 10;
    public static final int STORY_STARTING_ID = 16;
    public static final int MATHS_STARTING_ID = 17;

    public static final int PHONICS_MAX_SUB_ID = 2;
    public static final int WORDS_MAX_SUB_ID = 0;
    public static final int STORY_MAX_SUB_ID = 0;
    public static final int MATHS_MAX_SUB_ID = 0;

    public static final String[] EN_ALPHABET = {"A","B","C","D","E","F","G","H","I",
        "J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    // Determined values
    public static int SELECTED_LANGUAGE = Languages.ENGLISH;
    public static String ACTIVE_SESSION_TIME = "1900-01-01 00:00:00";

    // Methods
    public final static String STANDARD_DATE_TIME_STRING(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH-mm-ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}

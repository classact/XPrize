package classact.com.xprize;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import classact.com.xprize.activity.drill.math.MathsDrillFiveActivity;
import classact.com.xprize.activity.drill.math.MathsDrillFourActivity;
import classact.com.xprize.activity.drill.math.MathsDrillOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSevenAndOneActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixActivity;
import classact.com.xprize.activity.drill.math.MathsDrillSixAndThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillThreeActivity;
import classact.com.xprize.activity.drill.math.MathsDrillTwoActivity;
import classact.com.xprize.activity.drill.sound.SimpleStoryActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillEightActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillElevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFifteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFiveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillNineActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSixActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThirteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThreeActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwelveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwoActivity;
import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.activity.link.MathsLink;
import classact.com.xprize.activity.link.PhonicsLink;
import classact.com.xprize.activity.link.StoryLink;
import classact.com.xprize.activity.link.WordsLink;
import classact.com.xprize.activity.menu.LanguageSelect;
import classact.com.xprize.activity.movie.Movie;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.control.ComprehensionQuestion;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.MathDrillJsonBuilder;
import classact.com.xprize.control.Numeral;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.control.RightWrongPair;
import classact.com.xprize.control.Sentence;
import classact.com.xprize.control.SimpleStoryJsonBuilder;
import classact.com.xprize.control.SimpleStorySentence;
import classact.com.xprize.control.SoundDrillFiveObject;
import classact.com.xprize.control.SoundDrillJsonBuilder;
import classact.com.xprize.control.SoundDrillThreeObject;
import classact.com.xprize.control.SpelledWord;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.ComprehensionHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.LetterSequenceHelper;
import classact.com.xprize.database.helper.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helper.MathImageHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.helper.SentenceHelper;
import classact.com.xprize.database.helper.SentenceWordsHelper;
import classact.com.xprize.database.helper.SimpleStoriesHelper;
import classact.com.xprize.database.helper.SimpleStoryUnitFileHelper;
import classact.com.xprize.database.helper.SimpleStoryWordHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.Comprehension;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.MathDrillFlowWords;
import classact.com.xprize.database.model.MathImages;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.database.model.SentenceDB;
import classact.com.xprize.database.model.SentenceDBWords;
import classact.com.xprize.database.model.SimpleStories;
import classact.com.xprize.database.model.SimpleStoryUnitFiles;
import classact.com.xprize.database.model.SimpleStoryWords;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.Word;

public class MainActivity extends AppCompatActivity {

    private DbHelper mDbHelper;
    private int languageID;
    private int currentUnitId;
    private int drillID;
    private int mathDrillID;
    private int letterID;

    private boolean firstSectionFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Establish database connectivity
        if (!dbEstablsh()) {
            // Error handling with no db connection
            /* Error screen activity */
        }

        Intent intent = new Intent(this, LanguageSelect.class);
        // intent.putExtra("NEXT_BG_CODE", nextBgCode);
        startActivityForResult(intent, -1); // result code '-1' = initialization
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    public boolean determineCurrentItem(int unitId) {
        // Get current unit
        unitId = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
        Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitId);

        // Declare intent
        Intent intent;

        if (unitId == Globals.TUTORIAL_ID) {
            /* TUTORIAL LOGIC */

            if (u.getUnitFirstTimeMovie() == 0) {
                // Play intro movie
                intent = new Intent(this, Movie.class);
                intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                intent.putExtra(Code.SHOW_MV_BUTTONS, false);
                startActivityForResult(intent, unitId);
                overridePendingTransition(0, android.R.anim.fade_out);

            } else if (u.getUnitFirstTime() == 0) {
                // Start tutorial
                intent = new Intent(this, Tutorial.class);
                startActivityForResult(intent, unitId);
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        } else if (unitId < Globals.ENDING_ID) {
            /* CHAPTER LOGIC */

            if (u.getUnitFirstTimeMovie() == 0) {
                // Play chapter movie
                intent = new Intent(this, Movie.class);
                intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                intent.putExtra(Code.SHOW_MV_BUTTONS, true);

                startActivityForResult(intent, unitId);
                overridePendingTransition(0, android.R.anim.fade_out);

            } /* else if (u.getUnitFirstTime() == 0) {
                TUTORIAL LOGIC FOR THE FUTURE? Perhaps
            } */ else {
                // Determine if drill splash should be displayed
                // Splash is displayed when
                // #1. restarting the application (onRestart() method))
                // #2. the beginning of each drill type section (ie. 'Phonics section', 'Words section')

                // Determine type of splash by comparing drill last played
                int drillLastPlayed = u.getUnitDrillLastPlayed();

                if (drillLastPlayed == Globals.PHONICS_STARTING_ID ||
                    drillLastPlayed == Globals.WORDS_STARTING_ID ||
                    drillLastPlayed == Globals.STORY_STARTING_ID ||
                    drillLastPlayed == Globals.MATHS_STARTING_ID) {

                    // Determine drill splash code
                    Class drillSplashActivity = determineDrillSplash(u);

                    // Show drill splash
                    if (drillSplashActivity != null) {
                        intent = new Intent(this, drillSplashActivity);
                        startActivityForResult(intent, unitId);
                    }

                // Determine if the chapter ending splash should be played
                // Ending under the following scenarios:
                // * Unit has not been completed
                // * Last drill has been played
                //   - Determine this by checking <drill last played> is equal to <sum of number of drills (language, maths) in unit >
                } else if (u.getUnitCompleted() == 0 && drillLastPlayed == (u.getNumberOfLanguageDrills() + u.getNumberOfMathDrills())) {
                    // Show ending splash
                    intent = new Intent(this, Movie.class);
                    intent.putExtra(Code.RES_NAME, "star_level_" + unitId);
                    startActivityForResult(intent, unitId);
                    overridePendingTransition(0, android.R.anim.fade_out);

                // Let's roll with the drill
                } else {
                    letterID = LetterSequenceHelper.getLetterID(mDbHelper.getReadableDatabase(), Globals.SELECTED_LANGUAGE, currentUnitId, Globals.DEFAULT_UNIT_SUB_ID);
                    drillID = ++drillLastPlayed;
                    intent = prepDrill(drillID);
                    startActivityForResult(intent, unitId);
                    overridePendingTransition(0, android.R.anim.fade_out);
                }
            }
        } else if (unitId == Globals.ENDING_ID) {
            /* ENDING LOGIC */

            if (u.getUnitFirstTimeMovie() == 0) {
                // Play ending movie
                intent = new Intent(this, Movie.class);
                intent.putExtra(Code.RES_NAME, u.getUnitFirstTimeMovieFile());
                intent.putExtra(Code.SHOW_MV_BUTTONS, false);
                startActivityForResult(intent, Code.FINALE);
                overridePendingTransition(0, android.R.anim.fade_out);
            }

        } else {
            /* "Whoopsie!" LOGIC */
            System.err.println("And why am I here ...");
            return false;
        }

        return true;
    }

    /**
     * On Activity Result
     * Responsible for database updates after receiving response
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Resolve request completion
         * ==========================
         * - requestCode = finished activity's type (ie. Language-select, movie, drill)
         * - resultCode = unitId of finished activity
         */
        switch (requestCode) {
            case Code.LANG:
                // Store language id
                languageID = Globals.SELECTED_LANGUAGE;
                break;
            case Code.INTRO:
                break;
            case Code.TUTORIAL:
                break;
            case Code.MOVIE:
                break;
            case Code.PHONICS_SPLASH:
                break;
            case Code.WORDS_SPLASH:
                break;
            case Code.STORY_SPLASH:
                break;
            case Code.MATHS_SPLASH:
                break;
            case Code.RUN_DRILL:
                break;
            case Code.CHAPTER_END:
                break;
            case Code.FINALE:
                break;
            default:
                break;
        }

        // Determine current item based on resultCode (a.k.a 'unitId')
        determineCurrentItem(resultCode);
    }

    public Class determineDrillSplash(Unit u) {
        // Only show splash if we're busy with a unit (and not chapter movie/ending)
        if (u.getUnitFirstTimeMovie() == 0) {
            // Determine type of splash by comparing drill last played
            int drillLastPlayed = u.getUnitDrillLastPlayed();

            // Show phonics splash
            if (drillLastPlayed <= Globals.PHONICS_STARTING_ID) {
                return PhonicsLink.class;

            // Show words splash
            } else if (drillLastPlayed <= Globals.WORDS_STARTING_ID) {
                return WordsLink.class;

            // Show story splash
            } else if (drillLastPlayed <= Globals.STORY_STARTING_ID) {
                return StoryLink.class;

            // Show maths splash
            } else if (drillLastPlayed <= Globals.MATHS_STARTING_ID) {
                return MathsLink.class;

            // Otherwise .. err
            } else {
                // Don't show splash
                /* "Whoopsie!" Logic */
            }
        } else {
            // Don't show splash
            // Should be showing movie instead
            /* "Whoopsie!" Logic */
        }
        return null;
    }

    public int determineNextSplashBg() {
        return 0;
    }

    private Intent fetchDrill (int dbHelper, int unitId, int drillId, int languageId){
        try {
            // let's check which drill we are running and send through params for the json builder
            switch (drillId) {
                case 1: {

                    return playDrill1(limit, wordType);
                }
                case 2: {


                    return playDrill2(limit, wordType);
                }
                case 3: {


                    return playDrill3(limit);
                }
                case 4: {


                    return playDrill4(rightLimit, wrongLimit, wordType);
                }

                case 5: {


                    return playDrill5(rightLimit, wrongLimit, wordType);
                }
                case 6: {


                    return playDrill6(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3(),
                            drillFlowWord.getDrillSound4(),
                            drillFlowWord.getDrillSound5());
                }
                case 7: {


                    return playDrill7(mDbHelper,
                            languageId,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(4)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(5)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(6)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(7)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(8)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(9)),
                            drillFlowWord.getDrillSound1()
                    );
                }
                case 8: {
                    return playDrill8();
                }
                case 9: {
                    DrillFlowWords drillFlowWord;
                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillId, languageId);

                    return playDrill9(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3());
                }
                case 10: {


                    return playDrill10(WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2());
                }
                case 11: {
                    int wordType = 2; // drill 11 only uses sight words, which is WordType 2
                    int rightLimit = 5; // limit the words to 4 for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 3 random words based on the specific unit ID

                    ArrayList<Integer> drillWordIDs = new ArrayList();
                    drillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, rightLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillId, languageId);

                    return playDrill11(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2());
                }
                case 12: {
                    int wordType = 2; // drill 12 only uses sight words, which is WordType 2
                    int rightLimit = 4; // number of correct words
                    int wrongLimit = 8; // number of incorrect words
                    int numberLimit = 6; // Limit numbers to 8
                    int boyGirl = 1;
                    DrillFlowWords drillFlowWord;

                    ArrayList<Integer>  numerals = new ArrayList();
                    numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageId, numberLimit, boyGirl);

                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, rightLimit);

                    ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, wrongLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillId, languageId);

                    return playDrill12(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(4)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(5)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(6)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(7)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3(),
                            drillFlowWord.getDrillSound4(),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(0)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(1)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(2)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(3)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(4)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageId, numerals.get(5)));
                }
                case 13: {

                    int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // 5 words for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer>  rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillId, languageId);

                    return playDrill13(
                            languageId,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2()
                    );
                }
                case 14: {
                    int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // 5 words for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer>  rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillId, languageId);

                    return playDrill14(languageId,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageId, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3()
                    );
                }
                case 15: {
                    return playDrill15();
                }
                case 16: {
                    return playSimpleStory();
                }
                case 17: {
                    switch (unitId) {
                        case 1: {
                            int limit = 5; // Limit numbers to 5
                            int subId = 0; // this runs the subIDs for the drillflow table
                            int boyGirl;
                            if (languageId == 1)
                                boyGirl = 2;
                            else
                                boyGirl = 1;
                            return playMathDrill1(limit, boyGirl, subId);
                        }
                        case 2: {

                            int limit = 3; // Limit numbers to 3
                            int subId = 0; // this runs the subIDs for the drillflow table
                            int boyGirl;
                            if (languageId == 1)
                                boyGirl = 2;
                            else
                                boyGirl = 1;

                            return playMathDrill2(limit, subId, boyGirl);
                        }
                        case 3: {
                            int limit = 10; // Limit numbers to 10
                            int boyGirl = 1;
                            int subId = 0; // this runs the subIDs for the drillflow table

                            return playMathDrill3(limit, subId, boyGirl);
                        }
                        case 4: {
                            int limit = 5; // Limit numbers to 5
                            int subId = 0; // this runs the subIDs for the drillflow table
                            int boyGirl;
                            if (languageId == 1)
                                boyGirl = 2;
                            else
                                boyGirl = 1;

                            return playMathDrill4(limit, subId, boyGirl);
                        }
                        case 5: {
                            int subId = 0; // this runs the subIDs for the drillflow table
                            int boyGirl = 1;

                            if (unitId < 9) {
                                int limit = 10; // Limit numbers to 10
                                return playMathDrill5(limit, subId, boyGirl);
                            } else {
                                int limit = 20; // Limit numbers to 20
                                subId = 1; // this runs the subIDs for the drillflow table
                                return playMathDrill5And1(limit, subId, boyGirl);

                            }
                        }
                        case 6: {
                            int subId = 0; // this runs the subIDs for the drillflow table
                            int boyGirl;
                            if (languageId == 1)
                                boyGirl = 2;
                            else
                                boyGirl = 1;
                            if (unitId < 6) { // drill 1 - 5

                                if (languageId == 1)
                                    boyGirl = 2;
                                else
                                    boyGirl = 1;
                                return playMathDrill6(subId, boyGirl);

                            } else if ((unitId >= 6) && (unitId < 10)) { //six and one
                                subId=1;
                                return playMathDrill6And1(subId);

                            } else if (unitId == 10) { // six and two
                                subId=2;
                                return playMathDrill6And2(subId);

                            } else if ((unitId > 10) && (unitId < 16)) { // six and three
                                subId=3;
                                int limit = 3; // Limit numbers to 5
                                return playMathDrill6And3(subId, boyGirl);

                            } else if (unitId > 15) {  // six and four
                                subId=4;
                                int limit = 5; // Limit numbers to 5
                                return playMathDrill6And4(subId, boyGirl);
                            } else {
                                System.err.println("Error sdelecting maths drill");
                                break;
                            }
                        }

                        case 7: {
                            int subId = 0;
                            int boyGirl = 1;
                            if (unitId < 10) {
                                return playMathDrill7(subId);
                            } else {
                                subId=1;
                                return playMathDrill7And1(subId);
                            }
                        }
                        default: {
                            System.err.println("Wee Math drill is this?!");
                            break;
                        }
                    }
                    break;
                }
                default: {
                    System.err.println();
                    break;
                }
            }
        } catch (SQLiteException sqle) {
            throw sqle;
        }
        return null;
    }

    private void updateStatus () {
        Unit UnitInfo = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), currentUnitId);
        UnitInfo.setUnitDrillLastPlayed(drillID);
        int dbUpdateResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), UnitInfo);
    }

    /**
     * Establish db connection
     * @return Returns true/false if db connection has been established
     */
    protected boolean dbEstablsh() {
        // Try create database or connect to existing
        mDbHelper = new DbHelper(this);
        try {
            mDbHelper.createDatabase();
        } catch (IOException ioex) {
            System.err.println("Main: error connecting to database - " + ioex.getMessage());
            // Failed
            return false;
        }

        // Test opening database
        try {
            mDbHelper.openDatabase();
        } catch (SQLiteException sqlex) {
            System.err.println("Main: error opening database - " + sqlex.getMessage());
            // Failed
            return false;
        }

        // Success
        return true;
    }
}
package classact.com.xprize;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import classact.com.xprize.activity.language_select.LanguageSelectActivity;
import classact.com.xprize.activity.links.PhonicsLink;
import classact.com.xprize.activity.mathdrill.MathsDrillFiveActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillFiveAndOneActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillFourActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillOneActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSevenActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSevenAndOneActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSixActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSixAndFourActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSixAndOneActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSixAndThreeActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillSixAndTwoActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillThreeActivity;
import classact.com.xprize.activity.mathdrill.MathsDrillTwoActivity;
import classact.com.xprize.activity.movies.Chapter_01_Movie;
import classact.com.xprize.activity.movies.IntroActivity;
import classact.com.xprize.activity.sounddrill.SimpleStoryActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillEightActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillElevenActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillFifteenActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillFiveActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillFourActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillNineActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillOneActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillSevenActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillSixActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillTenActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillThirteenActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillThreeActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillTwelveActivity;
import classact.com.xprize.activity.sounddrill.SoundDrillTwoActivity;
import classact.com.xprize.activity.tutorial.TutorialActivity;
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
import classact.com.xprize.database.helpers.ComprehensionHelper;
import classact.com.xprize.database.helpers.DrillFlowWordsHelper;
import classact.com.xprize.database.helpers.DrillWordHelper;
import classact.com.xprize.database.helpers.LetterHelper;
import classact.com.xprize.database.helpers.LetterSequenceHelper;
import classact.com.xprize.database.helpers.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helpers.MathImageHelper;
import classact.com.xprize.database.helpers.NumeralHelper;
import classact.com.xprize.database.helpers.SentenceHelper;
import classact.com.xprize.database.helpers.SentenceWordsHelper;
import classact.com.xprize.database.helpers.SimpleStoriesHelper;
import classact.com.xprize.database.helpers.SimpleStoryUnitFileHelper;
import classact.com.xprize.database.helpers.SimpleStoryWordHelper;
import classact.com.xprize.database.helpers.UnitHelper;
import classact.com.xprize.database.helpers.WordHelper;
import classact.com.xprize.model.Comprehension;
import classact.com.xprize.model.DrillFlowWords;
import classact.com.xprize.model.Letter;
import classact.com.xprize.model.MathDrillFlowWords;
import classact.com.xprize.model.MathImages;
import classact.com.xprize.model.Numerals;
import classact.com.xprize.model.SentenceDB;
import classact.com.xprize.model.SentenceDBWords;
import classact.com.xprize.model.SimpleStories;
import classact.com.xprize.model.SimpleStoryUnitFiles;
import classact.com.xprize.model.SimpleStoryWords;
import classact.com.xprize.model.Unit;
import classact.com.xprize.model.Word;

public class MainActivity extends AppCompatActivity {

    private DbHelper mDbHelper;
    private int unitToBePlayed;
    private int unitSubID;
    private int drillID;
    private int mathDrillID;
    private int languageID;
    private int letterID;

    private boolean firstSectionFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!dbEstablsh()) {
            // Error handling with no db connection

        }

        // Otherwise :-)
        int u2p = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
        Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), u2p);

        // Set next bg 'splash' post language select screen
        int nextBgCode = -1;

        if (u.getUnitId() > 0) {
            nextBgCode = Code.PHON01;
        } else {
            if (u.getUnitFirstTimeMovie() == 0) {
                nextBgCode = Code.INTRO;
            } else if (u.getUnitFirstTime() == 0) {
                nextBgCode = Code.TUT;
            } else if (u.getUnitCompleted() == 0) {
                nextBgCode = Code.MV01;
            } else {
                nextBgCode = Code.PHON01;
            }
        }

        Intent intent = new Intent(this, LanguageSelectActivity.class);
        intent.putExtra("NEXT_BG_CODE", nextBgCode);
        startActivityForResult(intent, Code.LANG);
        overridePendingTransition(0, android.R.anim.fade_out);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int u2p = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
        Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), u2p);
        int queryResult = -1;

        if (!firstSectionFinished) {
            Intent intent;

            switch (requestCode) {
                case Code.LANG:
                    if (u.getUnitId() > 0) {
                        intent = new Intent(this, PhonicsLink.class);

                        startActivityForResult(intent, Code.PHON01);
                    } else {
                        if (u.getUnitFirstTimeMovie() == 0) {
                            intent = new Intent(this, IntroActivity.class);
                            startActivityForResult(intent, Code.INTRO);
                        } else if (u.getUnitFirstTime() == 0) {
                            intent = new Intent(this, TutorialActivity.class);
                            startActivityForResult(intent, Code.TUT);
                        } else if (u.getUnitCompleted() == 0) {
                            intent = new Intent(this, Chapter_01_Movie.class);
                            startActivityForResult(intent, Code.MV01);
                        } else {
                            intent = new Intent(this, PhonicsLink.class);
                            startActivityForResult(intent, Code.PHON01);
                        }
                    }
                    break;
                case Code.INTRO:
                    u.setUnitFirstTimeMovie(1);
                    queryResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    intent = new Intent(this, TutorialActivity.class);
                    startActivityForResult(intent, Code.TUT);
                    break;
                case Code.TUT:
                    u.setUnitFirstTime(1);
                    queryResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    intent = new Intent(this, Chapter_01_Movie.class);
                    startActivityForResult(intent, Code.MV01);
                    break;
                case Code.MV01:
                    u.setUnitCompleted(1);
                    u.setUnitInProgress(0);
                    queryResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), u.getUnitId() + 1);
                    queryResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    if (u != null && u.getUnitCompleted() != 1) {
                        u.setUnitInProgress(1);
                        if (u.getUnitUnlocked() == 0) {
                            u.setUnitUnlocked(1);
                        }
                        queryResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    }
                    intent = new Intent(this, PhonicsLink.class);
                    startActivityForResult(intent, Code.PHON01);
                    break;
                case Code.PHON01:
                    firstSectionFinished = true;
                    runUnits();
                    break;
                default:
                    break;
            }
        } else {

            switch (requestCode) {
                case (1): {
                    // Drill1
                    updateStatus();
                }
                break;
                case (2): {
                    // Drill2
                    updateStatus();
                }
                break;
                case (3): {
                    // Drill3
                }
                break;
                case (4): {
                    // Drill4
                }
                break;
                case (5): {
                    // Drill5
                }
                break;
                case (6): {
                    // Drill6
                }
                break;
                case (7): {
                    // Drill7
                }
                break;
                case (8): {
                    // Drill8
                }
                break;
                case (9): {
                    // Drill10
                }
                break;
                case (11): {
                    // Drill11
                }
                break;
                case (12): {
                    // Drill12
                }
                break;
                case (13): {
                    // Drill13
                }
                break;
                case (14): {
                    // Drill14
                }
                break;
                case (15): {
                    // Drill15
                }
                break;
                case (16): {
                    // Drill16 (Simple Stories)
                }
                break;
                case (17): {
                    // Math Drill 1

                }
                break;
                case (18): {
                    // Math Drill 2
                }
                break;
                case (19): {
                    // Math Drill 3
                }
                break;
                case (20): {
                    // Math Drill 4
                }
                break;
                case (21): {
                    // Math Drill 5
                }
                break;
                case (22): {
                    // Math Drill 6
                }
                break;
                case (23): {
                    // Math Drill 7
                }
                break;
            }
        }
    }

    private void runUnits () {
        int drillLastPlayed;
        languageID = Globals.SELECTED_LANGUAGE;

        unitToBePlayed = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
        Unit UnitInfo = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitToBePlayed);
        unitSubID = UnitInfo.getUnitSubIDInProgress();
        letterID = LetterSequenceHelper.getLetterID(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID);

        drillLastPlayed = UnitInfo.getUnitDrillLastPlayed();
        if (drillLastPlayed < 15)
            drillID = drillLastPlayed + 1;
        else {
            drillID = 1; // if this is true, all drills for this unit has been done.
        }
        mathDrillID = 0; // NEEDS TO BE FURTHER DEVELOPED

        runUnitAndDrill();
    }

    private void runUnitAndDrill (){
        try {

            // let's check which drill we are running and send through params for the json builder
            switch (drillID) {
                case 1: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // limit the words to 3 for this drill
                    playDrill1(limit, wordType);
                    break;
                }
                case 2: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 5; // 5 words for this drill

                    playDrill2(limit, wordType);

                    break;
                }
                case 3: {
                    int limit = 5; // 5 repeats for this drill so we choose 5 incorrect letters

                    playDrill3(limit);
                    break;
                }
                case 4: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 4; // limit the words to 4 for this drill
                    int wrongLimit = 2;

                    playDrill4(rightLimit, wrongLimit, wordType);
                    break;
                }

                case 5: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 3; // 5 words for this drill
                    int wrongLimit = 9;

                    playDrill5(rightLimit, wrongLimit, wordType);
                    break;
                }
                case 6: {
                    DrillFlowWords drillFlowWord;
                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill6(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3(),
                            drillFlowWord.getDrillSound4(),
                            drillFlowWord.getDrillSound5());
                    break;
                }
                case 7: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 6; // 5 words for this drill
                    int wrongWordLimit = 10;
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    ArrayList<Integer>  wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wrongWordLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill7(mDbHelper,
                            languageID,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
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
                    break;
                }
                case 8: {
                    playDrill8();
                    break;
                }
                case 9: {
                    DrillFlowWords drillFlowWord;
                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill9(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3());
                    break;
                }
                case 10: {
                    int wordType = 2; // drill 10 only uses sight words, which is WordType 2
                    int rightLimit = 5; // limit the words to 4 for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 3 random words based on the specific unit ID

                    ArrayList<Integer> drillWordIDs = new ArrayList();
                    drillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill10(WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2());
                    break;
                }
                case 11: {
                    int wordType = 2; // drill 11 only uses sight words, which is WordType 2
                    int rightLimit = 5; // limit the words to 4 for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 3 random words based on the specific unit ID

                    ArrayList<Integer> drillWordIDs = new ArrayList();
                    drillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill11(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2());
                    break;
                }
                case 12: {
                    int wordType = 2; // drill 12 only uses sight words, which is WordType 2
                    int rightLimit = 4; // number of correct words
                    int wrongLimit = 8; // number of incorrect words
                    int numberLimit = 6; // Limit numbers to 8
                    int boyGirl = 1;
                    DrillFlowWords drillFlowWord;

                    ArrayList<Integer>  numerals = new ArrayList();
                    numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, numberLimit, boyGirl);

                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightLimit);

                    ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wrongLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill12(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
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
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                            NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)));
                    break;
                }
                case 13: {

                    int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // 5 words for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer>  rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill13(
                            languageID,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2()
                    );
                    break;
                }
                case 14: {
                    int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // 5 words for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer>  rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill14(languageID,
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3()
                    );

                }
                break;
                case 15: {
                    playDrill15();
                    break;
                }
                case 16: {
                    playSimpleStory();
                    break;
                }


            }
            if (mathDrillID > 0) {
                switch (mathDrillID) {
                    case 1: {
                        int limit = 5; // Limit numbers to 5
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;
                        playMathDrill1(limit, boyGirl, subId);
                        break;
                    }
                    case 2: {

                        int limit = 3; // Limit numbers to 3
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;

                        playMathDrill2(limit, subId, boyGirl);
                        break;
                    }
                    case 3: {
                        int limit = 10; // Limit numbers to 10
                        int boyGirl = 1;
                        int subId = 0; // this runs the subIDs for the drillflow table

                        playMathDrill3(limit, subId, boyGirl);
                        break;
                    }
                    case 4: {
                        int limit = 5; // Limit numbers to 5
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;

                        playMathDrill4(limit, subId, boyGirl);
                        break;
                    }
                    case 5: {
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl = 1;

                        if (unitToBePlayed < 9) {
                            int limit = 10; // Limit numbers to 10
                            playMathDrill5(limit, subId, boyGirl);
                            break;
                        } else {
                            int limit = 20; // Limit numbers to 20
                            subId = 1; // this runs the subIDs for the drillflow table
                            playMathDrill5And1(limit, subId, boyGirl);

                        }
                        break;
                    }
                    case 6: {
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;
                        if (unitToBePlayed < 6) { // drill 1 - 5

                            if (languageID == 1)
                                boyGirl = 2;
                            else
                                boyGirl = 1;
                            playMathDrill6(subId, boyGirl);

                        } else if ((unitToBePlayed >= 6) && (unitToBePlayed < 10)) { //six and one
                            subId=1;
                            playMathDrill6And1(subId);

                        } else if (unitToBePlayed == 10) { // six and two
                            subId=2;
                            playMathDrill6And2(subId);

                        } else if ((unitToBePlayed > 10) && (unitToBePlayed < 16)) { // six and three
                            subId=3;
                            int limit = 3; // Limit numbers to 5
                            playMathDrill6And3(subId, boyGirl);

                        } else if (unitToBePlayed > 15) {  // six and four
                            subId=4;
                            int limit = 5; // Limit numbers to 5
                            playMathDrill6And4(subId, boyGirl);
                        }
                        break;
                    }

                    case 7: {
                        int subId = 0;
                        int boyGirl = 1;
                        if (unitToBePlayed < 10) {
                            playMathDrill7(subId);
                        } else {
                            subId=1;
                            playMathDrill7And1(subId);
                        }
                        break;
                    }
                }
            }

        } catch (SQLiteException sqle) {
            throw sqle;
        }

    }

    private void playDrill1(int limit, int wordType) {
        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);

        ObjectAndSound<String> letterObject = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        ArrayList<ObjectAndSound<String>> drillObjects = new ArrayList<ObjectAndSound<String>>();
        ArrayList<Integer> drillWordIDs = new ArrayList();
        drillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

        for (int i=0; i < drillWordIDs.size(); i++ ){
            Word word = WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(i));
            drillObjects.add(new ObjectAndSound<String>(word.getImagePictureURI(), word.getWordSoundURI(), word.getWordSlowSoundURI()));
        }
        String drillData = SoundDrillJsonBuilder.getSoundDrillOneJson(this, letterObject, drillFlowWord.getDrillSound1(), drillFlowWord.getDrillSound2(), drillFlowWord.getDrillSound3(), drillObjects);
        Intent intent = new Intent(this, SoundDrillOneActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
        //startActivity(intent);
        //int requestCode=-1;
        //startActivityForResult(intent, requestCode);
    }

    private void playDrill2(int limit, int wordType) {

        DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);
        ArrayList<Integer> rightDrillWordIDs = new ArrayList();
        rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);
        ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
        wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);
        ArrayList<RightWrongPair> pairs = new ArrayList<RightWrongPair>();
        for (int i=0; i < rightDrillWordIDs.size(); i++ ){ // we have the same amount of right and wrong words. So just loop over right words.
            Word rightWord = WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));
            Word wrongWord = WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));

            ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
            ObjectAndSound<String> wrongObject = new ObjectAndSound<>(wrongWord.getImagePictureURI(), wrongWord.getWordSoundURI(), "");
            RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
            pairs.add(pair);
        }
        String drillData = SoundDrillJsonBuilder.getSoundDrillTwoJson(this,
                letter.getPhonicSoundURI(),
                drillFlowWords.getDrillSound1(),
                drillFlowWords.getDrillSound2(),
                pairs
        );
        Intent intent = new Intent(this, SoundDrillTwoActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill3(int limit) {
        DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);
        ArrayList<SoundDrillThreeObject> sets = new ArrayList<SoundDrillThreeObject>();
        ArrayList<Integer> wrongLetters = new ArrayList();
        wrongLetters = LetterHelper.getWrongLetters(mDbHelper.getReadableDatabase(), languageID, letterID, limit);
        ObjectAndSound<String> objectAndSound = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());

        for (int i=0; i < wrongLetters.size(); i++ ) { //
            Letter wrongLetter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(i));
            ObjectAndSound<String> rightObject = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), "", "");
            ObjectAndSound<String> wrongObject = new ObjectAndSound<>(wrongLetter.getLetterPictureLowerCaseBlackURI(), "", "");
            RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
            SoundDrillThreeObject obj = new SoundDrillThreeObject(objectAndSound, pair);
            sets.add(obj);
        }

        String drillData = SoundDrillJsonBuilder.getSoundDrillThreeJson(this, drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), drillFlowWords.getDrillSound3(), sets);
        Intent intent = new Intent(this, SoundDrillThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill4(int rightlimit, int wronglimit, int wordType) {

        DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        ArrayList<DraggableImage<ObjectAndSound>> images = new ArrayList<DraggableImage<ObjectAndSound>>();
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);
        ArrayList<Integer> rightDrillWordIDs = new ArrayList();
        rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightlimit);
        int lastPosition=0;
        for (int i=0; i < rightDrillWordIDs.size(); i++ ){
            Word word = WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));

            ObjectAndSound obj = new ObjectAndSound(word.getImagePictureURI(), word.getWordSoundURI(), "");
            DraggableImage<ObjectAndSound> image = new DraggableImage<ObjectAndSound>(i+1, 1, obj);
            images.add(image);
            lastPosition=i;
        }

        ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
        wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wronglimit);
        for (int i=0; i < wrongDrillWordIDs.size(); i++ ){
            Word word = WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));

            ObjectAndSound obj = new ObjectAndSound(word.getImagePictureURI(), word.getWordSoundURI(), "");
            DraggableImage<ObjectAndSound> image = new DraggableImage<ObjectAndSound>(i+lastPosition+1, 0, obj);
            images.add(image);
        }

        String drillData = SoundDrillJsonBuilder.getSoundDrillFourActivity(this, letter.getLetterSoundURI(), drillFlowWords.getDrillSound3(), drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), images);
        Intent intent = new Intent(this, SoundDrillFourActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill5(int rightlimit, int wronglimit, int wordType) {

        ArrayList<SoundDrillFiveObject> soundDrillFiveObjects = new ArrayList<SoundDrillFiveObject>();
        ArrayList<ObjectAndSound> images = new ArrayList<ObjectAndSound>();
        ArrayList<ObjectAndSound> drillObjects = new ArrayList<ObjectAndSound>();

        DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);

        ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
        wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wronglimit);

        ArrayList<Integer> rightDrillWordIDs = new ArrayList();
        rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightlimit);

        int lastPosition=0;
        for (int i=0; i < rightDrillWordIDs.size(); i++ ){
            Word rightWord = WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));
            ObjectAndSound objectAndSound = new ObjectAndSound(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
            objectAndSound.setCustomData("1");
            images.add(objectAndSound);
            ObjectAndSound drillObject = new ObjectAndSound(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
            drillObject.setBeginningLetterSound(letter.getLetterSoundURI());
            drillObjects.add(drillObject);

            int jloopCount=0;
            for (int j=lastPosition; i < wrongDrillWordIDs.size(); i++ ){
                while (j < (lastPosition+3)) {
                    Word wrongWord = WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));
                    objectAndSound = new ObjectAndSound(wrongWord.getImagePictureURI(), wrongWord.getWordSoundURI(), "");
                    objectAndSound.setCustomData("0");
                    images.add(objectAndSound);
                    jloopCount = j;
                }
                lastPosition = jloopCount;
            }
            SoundDrillFiveObject obj = new SoundDrillFiveObject(drillObject, images);
            soundDrillFiveObjects.add(obj);
        }

        String drillData = SoundDrillJsonBuilder.getSoundDrillFiveJson(this, drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), soundDrillFiveObjects);
        Intent intent = new Intent(this, SoundDrillFiveActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill6(Letter letter,
                           String drillSound1,
                           String drillSound2,
                           String drillSound3,
                           String drillSound4,
                           String drillSound5) {
        String drillData = SoundDrillJsonBuilder.getSoundDrillSixJson(this, letter.getLetterPictureLowerCaseBlackURI(),
                letter.getLetterPictureUpperCaseBlackURI(),
                letter.getLetterSoundURI(), drillSound1, drillSound2, drillSound3, drillSound4, drillSound5);
        Intent intent = new Intent(this, SoundDrillSixActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill7(DbHelper mDbHelper,
                           int languageID,
                           Letter letter,
                           Word rightWord1,
                           Word rightWord2,
                           Word rightWord3,
                           Word wrongWord1,
                           Word wrongWord2,
                           Word wrongWord3,
                           Word wrongWord4,
                           Word wrongWord5,
                           Word wrongWord6,
                           Word wrongWord7,
                           Word wrongWord8,
                           Word wrongWord9,
                           Word wrongWord10,
                           String drillSound1) {

        ArrayList<SpelledWord> words = new ArrayList<>();
        //First Word
        SpelledWord spelledWord = new SpelledWord();
        ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord1.getImagePictureURI(), rightWord1.getWordSoundURI(), "");
        ObjectAndSound<String> word = new ObjectAndSound<>(rightWord1.getImagePictureURI(), rightWord1.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord1.getWordSlowSoundURI());
        ArrayList<String> letterSounds = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rightWord1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(rightWord1.getWordName().charAt(i)));
            if (i > 0)
                sb.append(",");
            sb.append(thisLetter.getLetterPictureLowerCaseBlackURI());
            sb.append("&");
            sb.append(thisLetter.getLetterPictureLowerCaseBlueURI());
            letterSounds.add(thisLetter.getLetterSoundURI());
        }
        word.setSpelling(sb.toString());
        spelledWord.setWord(word);
        spelledWord.setettersSound(letterSounds);

        ArrayList<DraggableImage<String>> letterImages = new ArrayList<>();
        letterImages.add(new DraggableImage<String>(0, 1, rightWord1.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord1.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord2.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);
        //Second Word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord2.getImagePictureURI(), rightWord2.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord2.getWordSlowSoundURI());
        sb = new StringBuilder();
        letterSounds = new ArrayList<>();
        for (int i = 0; i < rightWord2.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(rightWord2.getWordName().charAt(i)));
            if (i > 0)
                sb.append(",");
            sb.append(thisLetter.getLetterPictureLowerCaseBlackURI());
            sb.append("&");
            sb.append(thisLetter.getLetterPictureLowerCaseBlueURI());
            letterSounds.add(thisLetter.getLetterSoundURI());
        }

        word.setSpelling(sb.toString());
        spelledWord.setWord(word);
        spelledWord.setettersSound(letterSounds);
        letterImages = new ArrayList<>();
        letterImages.add(new DraggableImage<String>(0, 0, rightWord2.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord3.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 1, wrongWord4.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);

        //Third Word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord3.getImagePictureURI(), rightWord3.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord3.getWordSlowSoundURI());
        sb = new StringBuilder();
        letterSounds = new ArrayList<>();
        for (int i = 0; i < rightWord3.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(rightWord3.getWordName().charAt(i)));
            if (i > 0)
                sb.append(",");
            sb.append(thisLetter.getLetterPictureLowerCaseBlackURI());
            sb.append("&");
            sb.append(thisLetter.getLetterPictureLowerCaseBlueURI());
            letterSounds.add(thisLetter.getLetterSoundURI());
        }

        word.setSpelling(sb.toString());

        spelledWord.setWord(word);
        spelledWord.setettersSound(letterSounds);
        letterImages = new ArrayList<>();
        letterImages.add(new DraggableImage<String>(0, 1, rightWord3.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord5.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord6.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);
/*
        //Fourth word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord4.getImagePictureURI(), rightWord4.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord4.getWordSlowSoundURI());
        sb = null;
        letterSounds = null;
        for (int i = 0; i < rightWord1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(rightWord1.getWordName().charAt(i)));
            if (i > 0)
                sb.append(",");
            sb.append(thisLetter.getLetterPictureLowerCaseBlackURI());
            sb.append("&");
            sb.append(thisLetter.getLetterPictureLowerCaseBlueURI());
            letterSounds.add(thisLetter.getLetterSoundURI());
        }

        word.setSpelling(sb.toString());
        spelledWord.setWord(word);
        spelledWord.setettersSound(letterSounds);
        letterImages = new ArrayList<>();
        letterImages.add(new DraggableImage<String>(0, 1, rightWord4.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord7.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord8.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);

        //Fifth word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord5.getImagePictureURI(), rightWord1.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord5.getWordSlowSoundURI());
        sb = null;
        letterSounds = null;
        for (int i = 0; i < rightWord1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(rightWord1.getWordName().charAt(i)));
            if (i > 0)
                sb.append(",");
            sb.append(thisLetter.getLetterPictureLowerCaseBlackURI());
            sb.append("&");
            sb.append(thisLetter.getLetterPictureLowerCaseBlueURI());
            letterSounds.add(thisLetter.getLetterSoundURI());
        }

        word.setSpelling(sb.toString());
        spelledWord.setWord(word);
        spelledWord.setettersSound(letterSounds);
        letterImages = new ArrayList<>();
        letterImages.add(new DraggableImage<String>(0, 1, rightWord5.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord9.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord10.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);
        //
        */
        String drillData = SoundDrillJsonBuilder.getSoundDrillSevenJson(this, drillSound1, words);
        Intent intent = new Intent(this, SoundDrillSevenActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill8() {
        Letter letter = LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID);
        DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);
        String drillData = SoundDrillJsonBuilder.getSoundDrillEightJson(this,
                letter.getLetterLowerPath(),
                drillFlowWords.getDrillSound1(),
                drillFlowWords.getDrillSound4(),
                drillFlowWords.getDrillSound2(),
                drillFlowWords.getDrillSound3(),
                letter.getLetterPictureLowerCaseDotsURI(),
                letter.getLetterSoundURI(),
                letter.getLetterUpperPath(),
                letter.getLetterPictureUpperCaseDotsURI());
        Intent intent = new Intent(this, SoundDrillEightActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playDrill9(Letter letter,
                           String drillSound1,
                           String drillSound2,
                           String drillSound3) {
        String drillData = SoundDrillJsonBuilder.getSoundDrillNineJson(this, letter.getLetterSoundURI(),
                drillSound1,
                drillSound2,
                drillSound3);
        Intent intent = new Intent(this, SoundDrillNineActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }
    public void playDrill10( Word word1,
                             Word word2,
                             Word word3,
                             Word word4,
                             Word word5,
                             String drillSound1,
                             String drillSound2) {

        ArrayList<SpelledWord> words = new ArrayList<>();
        //One
        SpelledWord word = new SpelledWord();
        ObjectAndSound<String> object = new ObjectAndSound<>(word1.getWordPictureURI(), word1.getWordSoundURI(),"");
        object.setSpelling(word1.getWordName());
        word.setWord(object);
        words.add(word);
        //Two
        word = new SpelledWord();
        object = new ObjectAndSound<>(word2.getWordPictureURI(), word2.getWordSoundURI(),"");
        object.setSpelling(word2.getWordName());
        word.setWord(object);
        words.add(word);
        //Three
        word = new SpelledWord();
        object = new ObjectAndSound<>(word3.getWordPictureURI(), word3.getWordSoundURI(),"");
        object.setSpelling(word3.getWordName());
        word.setWord(object);
        words.add(word);
        //Four
        word = new SpelledWord();
        object = new ObjectAndSound<>(word4.getWordPictureURI(), word4.getWordSoundURI(),"");
        object.setSpelling(word4.getWordName());
        word.setWord(object);
        words.add(word);
        //Five
        word = new SpelledWord();
        object = new ObjectAndSound<>(word1.getWordPictureURI(), word5.getWordSoundURI(),"");
        object.setSpelling(word5.getWordName());
        word.setWord(object);
        words.add(word);
        //
        String drillData = SoundDrillJsonBuilder.getSoundDrillTenJson(this,drillSound1,drillSound2,words);
        Intent intent = new Intent(this,SoundDrillTenActivity.class);
        intent.putExtra("data",drillData);
        startActivityForResult(intent,10);
    }

    public void playDrill11(Letter letter,
                            Word word1,
                            Word word2,
                            Word word3,
                            Word word4,
                            Word word5,
                            String drillSound1,
                            String drillSound2) {
        ArrayList<ObjectAndSound<String>> words = new ArrayList<>();
        ObjectAndSound<String> word = new ObjectAndSound<>("", word1.getWordSoundURI(), "");
        word.setSpelling(word1.getWordName());
        words.add(word);
        word = new ObjectAndSound<>("", word2.getWordSoundURI(), "");
        word.setSpelling(word2.getWordName());
        words.add(word);
        word = new ObjectAndSound<>("", word3.getWordSoundURI(), "");
        word.setSpelling(word3.getWordName());
        words.add(word);
        word = new ObjectAndSound<>("", word4.getWordSoundURI(), "");
        word.setSpelling(word4.getWordName());
        words.add(word);
        word = new ObjectAndSound<>("", word5.getWordSoundURI(), "");
        word.setSpelling(word5.getWordName());
        words.add(word);

        String drillData = SoundDrillJsonBuilder.getSoundDrillElevenJson(this, drillSound1, drillSound2, NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, 1).getSound(), words);
        Intent intent = new Intent(this, SoundDrillElevenActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,11);
    }

    public void playDrill12(Letter letter,
                            Word rightWord1,
                            Word rightWord2,
                            Word rightWord3,
                            Word rightWord4,
                            Word wrongWord1,
                            Word wrongWord2,
                            Word wrongWord3,
                            Word wrongWord4,
                            Word wrongWord5,
                            Word wrongWord6,
                            Word wrongWord7,
                            Word wrongWord8,
                            String drillSound1,
                            String drillSound2,
                            String drillSound3,
                            String drillSound4,
                            Numerals numeral1,
                            Numerals numeral2,
                            Numerals numeral3,
                            Numerals numeral4,
                            Numerals numeral5,
                            Numerals numeral6) {

        ArrayList<SpelledWord> sets = new ArrayList<>();
        //Set 1
        SpelledWord set = new SpelledWord();
        ObjectAndSound<String> word = new ObjectAndSound<>("", rightWord1.getWordSoundURI(), "");
        set.setWord(word);
        ArrayList<DraggableImage<String>> items = new ArrayList<>();
        DraggableImage<String> item = new DraggableImage<>(0, 0, wrongWord1.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 1, rightWord1.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 0, wrongWord2.getWordName());
        items.add(item);
        set.setLettersImages(items);
        sets.add(set);
        //Two
        set = new SpelledWord();
        word = new ObjectAndSound<>("", rightWord2.getWordSoundURI(), "");
        set.setWord(word);
        items = new ArrayList<>();
        item = new DraggableImage<>(0, 1, rightWord2.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 0, wrongWord3.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 0, wrongWord4.getWordName());
        items.add(item);
        set.setLettersImages(items);
        sets.add(set);
        //Three
        set = new SpelledWord();
        word = new ObjectAndSound<>("", rightWord3.getWordSoundURI(), "");
        set.setWord(word);
        items = new ArrayList<>();
        item = new DraggableImage<>(0, 0, wrongWord5.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 0, wrongWord6.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 1, rightWord3.getWordName());
        items.add(item);
        set.setLettersImages(items);
        sets.add(set);
        //Four
        set = new SpelledWord();
        word = new ObjectAndSound<>("", rightWord4.getWordSoundURI(), "");
        set.setWord(word);
        items = new ArrayList<>();
        item = new DraggableImage<>(0, 0, wrongWord7.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 1, rightWord4.getWordName());
        items.add(item);
        item = new DraggableImage<>(0, 0, wrongWord8.getWordName());
        items.add(item);
        set.setLettersImages(items);
        sets.add(set);
        //
        String drillData = SoundDrillJsonBuilder.getSoundDrilTwelveJson(this,
                drillSound1, drillSound2, drillSound3, numeral1.getSound(),numeral2.getSound(),numeral3.getSound(),numeral4.getSound(),numeral5.getSound(),
                numeral6.getSound(), drillSound4, sets);
        Intent intent = new Intent(this, SoundDrillTwelveActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,12);
    }

    public void playDrill13(int languageID,
                            Letter letter,
                            Word word1,
                            Word word2,
                            Word word3,
                            Word word4,
                            String drillSound1,
                            String drillSound2) {

        ArrayList<SpelledWord> words = new ArrayList<>();
        SpelledWord set = new SpelledWord();
        ObjectAndSound<String> word = new ObjectAndSound<>("",word1.getWordSoundURI(),"");
        word.setSpelling(word1.getWordName());
        set.setWord(word);
        ArrayList<DraggableImage<String>> items = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < word1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word1.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word1.getWordName().length() - word1.getWordName().substring(0,word1.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
            int n=0;
            for (int j=-1; (j=word1.getWordName().indexOf(thisLetter.getLetterName(), j+1)) != -1;) {
                sb.append(String.valueOf(j+1));
                if (n<count)
                    sb.append(",");
                n++;
            }
            item.setExtraData(sb.toString());
            items.add(item);
        }

        set.setLettersImages(items);
        words.add(set);
        //Two
        set = new SpelledWord();
        word = new ObjectAndSound<>("",word2.getWordSoundURI(),"");
        word.setSpelling(word2.getWordName());
        set.setWord(word);
        items = new ArrayList<>();
        sb = new StringBuilder();
        count = 0;
        for (int i = 0; i < word2.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word2.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word2.getWordName().length() - word2.getWordName().substring(0,word2.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
            int n=0;
            for (int j=-1; (j=word2.getWordName().indexOf(thisLetter.getLetterName(), j+1)) != -1;) {
                sb.append(String.valueOf(j+1));
                if (n<count)
                    sb.append(",");
                n++;
            }
            item.setExtraData(sb.toString());
            items.add(item);
        }

        set.setLettersImages(items);
        words.add(set);
        //Three
        set = new SpelledWord();
        word = new ObjectAndSound<>("",word3.getWordSoundURI(),"");
        word.setSpelling(word3.getWordName());
        set.setWord(word);
        items = new ArrayList<>();
        sb = new StringBuilder();
        count = 0;
        for (int i = 0; i < word3.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word3.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word3.getWordName().length() - word3.getWordName().substring(0,word3.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
            int n=0;
            for (int j=-1; (j=word3.getWordName().indexOf(thisLetter.getLetterName(), j+1)) != -1;) {
                sb.append(String.valueOf(j+1));
                if (n<count)
                    sb.append(",");
                n++;
            }
            item.setExtraData(sb.toString());
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
        //Four
        /*
        set = new SpelledWord();
        word = new ObjectAndSound<>("",word4.getWordSoundURI(),"");
        word.setSpelling(word4.getWordName());
        set.setWord(word);
        items = new ArrayList<>();
        sb = new StringBuilder();

        for (int i = 0; i < word4.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word4.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word4.getWordName().length() - word4.getWordName().substring(0,word4.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
            int n=0;
            for (int j=-1; (j=word4.getWordName().indexOf(thisLetter.getLetterName(), j+1)) != -1;) {
                sb.append(String.valueOf(j+1));
                if (n<count)
                    sb.append(",");
                n++;
            }
            item.setExtraData(sb.toString());
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
        //*/
        String drillData =SoundDrillJsonBuilder.getSoundDrillThirteenJson(this,drillSound1,drillSound2,words);
        Intent intent = new Intent(this,SoundDrillThirteenActivity.class);
        intent.putExtra("data",drillData);
        startActivity(intent);
    }

    public void playDrill14(int languageID,
                            Letter letter,
                            Word word1,
                            Word word2,
                            Word word3,
                            Word word4,
                            String drillSound1,
                            String drillSound2,
                            String drillSound3) {

        ArrayList<SpelledWord> words = new ArrayList<>();
        SpelledWord set = new SpelledWord();
        ObjectAndSound<String> word = new ObjectAndSound<>("", word1.getWordSoundURI(), "");
        word.setSpelling(word1.getWordName());
        set.setWord(word);
        ArrayList<DraggableImage<String>> items = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < word1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word1.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word1.getWordName().length() - word1.getWordName().substring(0, word1.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }

        set.setLettersImages(items);
        words.add(set);
        //Two
        set = new SpelledWord();
        word = new ObjectAndSound<>("", word2.getWordSoundURI(), "");
        word.setSpelling(word2.getWordName());
        set.setWord(word);
        items = new ArrayList<>();

        count = 0;
        for (int i = 0; i < word2.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word2.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word2.getWordName().length() - word2.getWordName().substring(0, word2.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }

        set.setLettersImages(items);
        words.add(set);
        //Three
        set = new SpelledWord();
        word = new ObjectAndSound<>("", word3.getWordSoundURI(), "");
        word.setSpelling(word3.getWordName());
        set.setWord(word);
        items = new ArrayList<>();

        count = 0;
        for (int i = 0; i < word3.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word3.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word3.getWordName().length() - word3.getWordName().substring(0, word3.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
        /*
        //Four
        set = new SpelledWord();
        word = new ObjectAndSound<>("", word4.getWordSoundURI(), "");
        word.setSpelling(word4.getWordName());
        set.setWord(word);
        items = new ArrayList<>();


        for (int i = 0; i < word4.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word4.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word4.getWordName().length() - word4.getWordName().substring(0, word4.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
        // */
        String drillData = SoundDrillJsonBuilder.getSoundDrillFourteenJson(this, drillSound1, drillSound2, drillSound3, words);
        Intent intent = new Intent(this, SoundDrillThirteenActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);

    }


    public void playDrill15() {
        ArrayList<Sentence> sentences = new ArrayList<>();

        ArrayList<Integer>  sentenceIDs = new ArrayList();
        sentenceIDs = SentenceHelper.getSentences(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed);

        for (int i=0; i < sentenceIDs.size(); i++){
            SentenceDB sentenceFromDB = SentenceHelper.getSentence(mDbHelper.getReadableDatabase(), sentenceIDs.get(i));
            Sentence sentence = new Sentence(sentenceFromDB.getWordCount(), sentenceFromDB.getSentenceSoundFile());
            ArrayList<DraggableImage<String>> words = new ArrayList<>();
            ArrayList<Integer>  sentenceWordIDs = new ArrayList();
            sentenceWordIDs = SentenceWordsHelper.getSentenceWords(mDbHelper.getReadableDatabase(), sentenceIDs.get(i));
            for (int j=0; j< sentenceWordIDs.size(); j++){
                SentenceDBWords sentenceWord = SentenceWordsHelper.getSentenceWord(mDbHelper.getReadableDatabase(), sentenceWordIDs.get(j));
                DraggableImage<String> word = new DraggableImage(0,0,"");
                StringBuilder sb = new StringBuilder();
                sb.append(sentenceWord.getWordImage());
                sb.append(",");
                sb.append(sentenceWord.getWordSound());
                word.setContent(sb.toString());
                word.setExtraData(String.valueOf(sentenceWord.getWordNo()));
                words.add(word);
            }
            sentence.setWords(words);
            sentences.add(sentence);
        }
        //
        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

        String drillData = SoundDrillJsonBuilder.getSoundDrillFifteenJson(this,drillFlowWord.getDrillSound1(),drillFlowWord.getDrillSound2(),sentences);
        Intent intent = new Intent(this,SoundDrillFifteenActivity.class);
        intent.putExtra("data",drillData);
        startActivity(intent);
    }

    public void playSimpleStory() {
        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

        ArrayList<SimpleStorySentence> sentences = new ArrayList<>();

        ArrayList<Integer>  sentenceIDs = new ArrayList();
        sentenceIDs = SimpleStoriesHelper.getSentences(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed);

        for (int i=0; i < sentenceIDs.size(); i++){
            SimpleStories sentenceFromDB = SimpleStoriesHelper.getSentence(mDbHelper.getReadableDatabase(), sentenceIDs.get(i));
            SimpleStorySentence sentence = new SimpleStorySentence();
            sentence.setFullSound(sentenceFromDB.getSentenceSoundFile());
            ArrayList<classact.com.xprize.control.Word> words = new ArrayList<>();
            ArrayList<Integer>  sentenceWordIDs = new ArrayList();
            sentenceWordIDs = SimpleStoryWordHelper.getSentenceWords(mDbHelper.getReadableDatabase(), sentenceIDs.get(i));
            for (int j=0; j< sentenceWordIDs.size(); j++){
                SimpleStoryWords sentenceWord = SimpleStoryWordHelper.getSentenceWord(mDbHelper.getReadableDatabase(), sentenceWordIDs.get(j));
                classact.com.xprize.control.Word word = new classact.com.xprize.control.Word(1, sentenceWord.getBlackWord(), sentenceWord.getRedWord());
                word.setSound(sentenceWord.getWordSound());
                words.add(word);
            }
            sentence.setWords(words);
            sentences.add(sentence);
        }

        ArrayList<ComprehensionQuestion> questions = new ArrayList<>();
        ArrayList<DraggableImage<String>> images = new ArrayList<>();

        ArrayList<Integer>  comprehensionIDs = new ArrayList();
        comprehensionIDs = ComprehensionHelper.getComprehensionIDs(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed);
        int rightPicture=0;
        for (int i=0; i < comprehensionIDs.size(); i++){
            Comprehension comprehensionQA = ComprehensionHelper.getComprehensionQA(mDbHelper.getReadableDatabase(), comprehensionIDs.get(i));
            if (comprehensionQA.getQuestionHasSoundAnswer() == 0) {
                ComprehensionQuestion question = new ComprehensionQuestion(comprehensionQA.getQuestionSound(), "", 0);
                if (comprehensionQA.getCorrectPicture() == comprehensionQA.getPicture1()) {
                    DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture1());
                    images.add(image);
                } else {
                    DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture1());
                    images.add(image);
                }
                if (comprehensionQA.getCorrectPicture() == comprehensionQA.getPicture2()) {
                    DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture2());
                    images.add(image);
                } else {
                    DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture2());
                    images.add(image);
                }
                if (comprehensionQA.getCorrectPicture() == comprehensionQA.getPicture3()) {
                    DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture3());
                    images.add(image);
                } else {
                    DraggableImage<String> image = new DraggableImage<>(0, 0, comprehensionQA.getPicture3());
                    images.add(image);
                }
                question.setImages(images);
                questions.add(question);
            } else {
                ComprehensionQuestion question = new ComprehensionQuestion(comprehensionQA.getQuestionSound(), comprehensionQA.getAnswerSound(), 0);
                DraggableImage<String> image = new DraggableImage<>(0, 1, comprehensionQA.getPicture1());
                images.add(image);
                image = new DraggableImage<>(0, 1, comprehensionQA.getPicture2());
                images.add(image);
                question.setImages(images);
                questions.add(question);
            }
        }

        SimpleStoryUnitFiles simpleStoryUnitFiles = SimpleStoryUnitFileHelper.getSimpleStoryUnitFiles(mDbHelper.getReadableDatabase(), unitToBePlayed, languageID);

        String drillData = SimpleStoryJsonBuilder.getSimpleStoryJson(this,
                "story_link",
                drillFlowWord.getDrillSound1(),
                drillFlowWord.getDrillSound2(),
                drillFlowWord.getDrillSound3(),
                drillFlowWord.getDrillSound4(),
                drillFlowWord.getDrillSound5(),
                drillFlowWord.getDrillSound6(),
                drillFlowWord.getDrillSound7(),
                simpleStoryUnitFiles.getSimpleStoryUnitSoundFile(),
                drillFlowWord.getDrillSound8(),
                simpleStoryUnitFiles.getSimpleStoryUnitImage(),
                sentences,
                simpleStoryUnitFiles.getCompInstr1(),
                simpleStoryUnitFiles.getCompInstr2(),
                questions);
        Intent intent = new Intent(this, SimpleStoryActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill1(int limit, int boyGirl, int subId) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);
        ArrayList<Integer> numeralsFromDB = new ArrayList();
        numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);
        ArrayList<Numeral> numerals = new ArrayList<>();
        for (int i=0; i < numeralsFromDB.size(); i++ ){
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(i));
            Numeral numeral = new Numeral(numeralFromDB.getSound(), numeralFromDB.getBlackImage(), numeralFromDB.getSparklingImage());
            numerals.add(numeral);
        }
        String drillData = MathDrillJsonBuilder.getDrillOneJson(this,
                mathDrillFlowWord.getDrillSound1(),
                mathDrillFlowWord.getDrillSound2(),
                numerals);
        Intent intent = new Intent(this, MathsDrillOneActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill2(int limit, int subId, int boyGirl) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);

        ArrayList<Integer>  numeralsFromDB = new ArrayList();
        numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

        ArrayList<Integer>  mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);
        MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)); // drill 2 only has one record per unit. So we can select one at a time.

        ArrayList<ObjectAndSound<String>> numbers = new ArrayList<>();
        for (int i=0; i < numeralsFromDB.size(); i++ ) {
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(i));
            ObjectAndSound<String> number = new ObjectAndSound<>(numeralFromDB.getBlackImage(), numeralFromDB.getSound(), "");
            if (i==0){
                number.setCustomData("1");
            } else {
                number.setCustomData("0");
            }
            numbers.add(number);
        }
        String drillData = MathDrillJsonBuilder.getDrillTwoJson(this, mathDrillFlowWord.getDrillSound1(), mathImages.getNumberOfImagesSound(), mathImages.getNumberOfImages(), mathImages.getImageName(),  mathDrillFlowWord.getDrillSound2(), mathImages.getImageSound(), numbers);
        Intent intent = new Intent(this, MathsDrillTwoActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }
    public void playMathDrill3(int limit, int subId, int boyGirl) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId,  languageID);
        ArrayList<Integer>  numerals = new ArrayList();
        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

        ArrayList<Integer>  mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);


        String drillData = MathDrillJsonBuilder.getDrillThreeJson(this,
                mathDrillFlowWord.getDrillSound1(), MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getTestNumber(), MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound2(), mathDrillFlowWord.getDrillSound3(), MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(6)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(7)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(8)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(9)).getSound()
        );
        Intent intent = new Intent(this, MathsDrillThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }



    public void playMathDrill4(int limit, int subId, int boyGirl) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId,languageID);
        ArrayList<Integer>  numerals = new ArrayList();
        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        String drillData = MathDrillJsonBuilder.getDrillFourJson(this, "yes",
                mathDrillFlowWord.getDrillSound1(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound2(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numerals.get(0)).getBlackImage(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getImageName(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numerals.get(1)).getBlackImage()
        );
        Intent intent = new Intent(this, MathsDrillFourActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);

    }

    public void playMathDrill5(int limit, int subId, int boyGirl) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        ArrayList<ObjectAndSound<String>> items = new ArrayList<>();
        int correctTestNumber=0;
        for (int i=0; i < mathImageList.size(); i++ ) {
            MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(i));
            correctTestNumber = mathImages.getTestNumber();
            ObjectAndSound<String> item = new ObjectAndSound<>(mathImages.getImageName(), mathImages.getImageSound(), "");
            item.setCustomData(String.valueOf(mathImages.getNumberOfImages()));
            items.add(item);
        }

        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();

        ArrayList<Integer> numeralsFromDB_2_Only = new ArrayList();
        numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(mDbHelper.getReadableDatabase(), languageID,10, 2, correctTestNumber, boyGirl);
        for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
            numerals.add(new DraggableImage<>(0, 0, numeralFromDB.getBlackImage()));
        }
        Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, correctTestNumber);
        numerals.add(new DraggableImage<>(0, 1, numeralCorrectAnswer.getBlackImage()));

        ArrayList<Integer> numeralsFromDB = new ArrayList();
        numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);
        String drillData = MathDrillJsonBuilder.getDrillFiveJson(this, mathDrillFlowWord.getDrillSound1(), mathDrillFlowWord.getDrillSound2(),
                mathDrillFlowWord.getDrillSound3(), items, numerals,
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(0)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(1)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(2)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(3)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(4)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(5)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(6)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(7)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(8)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(9)).getSound()
        );
        Intent intent = new Intent(this, MathsDrillFiveActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill5And1(int limit, int subId, int boyGirl) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);
        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        ArrayList<ObjectAndSound<String>> items = new ArrayList<>();
        int correctTestNumber=0;
        String answerSound="";
        for (int i=0; i < mathImageList.size(); i++ ) {
            MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(i));
            correctTestNumber = mathImages.getTestNumber();
            if (mathImages.getImageName().contains("plus")) {
                answerSound=mathImages.getNumberOfImagesSound();
            }
            Numerals thisNumber = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, mathImages.getNumberOfImages());
            ObjectAndSound<String> item = new ObjectAndSound<>(mathImages.getImageName(), mathImages.getImageSound(), "");
            item.setCustomData(String.valueOf(mathImages.getNumberOfImages()));
            items.add(item);
        }

        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();

        ArrayList<Integer> numeralsFromDB_2_Only = new ArrayList();
        numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(mDbHelper.getReadableDatabase(), languageID,20, 2, correctTestNumber, boyGirl);
        for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
            numerals.add(new DraggableImage<>(0, 0, numeralFromDB.getBlackImage()));
        }
        Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, correctTestNumber);
        numerals.add(new DraggableImage<>(0, 1, numeralCorrectAnswer.getBlackImage()));

        ArrayList<Integer> numeralsFromDB = new ArrayList();
        numeralsFromDB = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);
        String drillData = MathDrillJsonBuilder.getDrillFiveAndOneJson(this, mathDrillFlowWord.getDrillSound1(), mathDrillFlowWord.getDrillSound2(),
                answerSound, mathDrillFlowWord.getDrillSound3(), items, numerals,
                numeralCorrectAnswer.getBlackImage(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(0)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(1)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(2)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(3)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(4)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(5)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(6)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(7)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(8)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(9)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(10)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(11)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(12)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(13)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(14)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(15)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(16)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(17)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(18)).getSound(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB.get(19)).getSound());
        Intent intent = new Intent(this, MathsDrillFiveActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);


    }

    public void playMathDrill6(int subId, int boyGirl) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId,languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        String drillData = MathDrillJsonBuilder.getDrillSixJson(this, "yes",
                mathDrillFlowWord.getDrillSound1(),
                mathDrillFlowWord.getDrillSound2(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageSound(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName());

        Intent intent = new Intent(this, MathsDrillSixActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill6And1(int subId) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId,languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        String drillData = MathDrillJsonBuilder.getDrillSixAndOneJson(this,
                mathDrillFlowWord.getDrillSound1(),
                mathDrillFlowWord.getDrillSound2(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImagesSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(), "large");
        ;

        Intent intent = new Intent(this, MathsDrillSixActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);

    }

    public void playMathDrill6And2(int subId) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId,languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        String drillData = MathDrillJsonBuilder.getDrillSixAndTwoJson(this,
                mathDrillFlowWord.getDrillSound1(),
                mathDrillFlowWord.getDrillSound2(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)).getNumberOfImagesSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(4)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),"and",
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(3)).getImageSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)).getImageName(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getImageName());
        ;

        Intent intent = new Intent(this, MathsDrillSixActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }


    public void playMathDrill6And3(int subId, int boyGirl) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0));
        int correctTestNumber = mathImages.getTestNumber();

        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();

        ArrayList<Integer> numeralsFromDB_2_Only = new ArrayList();
        numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(mDbHelper.getReadableDatabase(), languageID,20, 2, correctTestNumber, boyGirl);
        for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
            numerals.add(new DraggableImage<>(0, 0, numeralFromDB.getBlackImage()));
        }
        Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, correctTestNumber);
        numerals.add(new DraggableImage<>(0, 1, numeralCorrectAnswer.getBlackImage()));

        String drillData = MathDrillJsonBuilder.getDrillSixAndThreeJson(this,
                mathDrillFlowWord.getDrillSound1(),
                mathDrillFlowWord.getDrillSound2(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageSound(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),
                mathDrillFlowWord.getDrillSound5(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)).getImageName(),
                String.valueOf(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages()),
                String.valueOf(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages()),
                numerals);
        Intent intent = new Intent(this, MathsDrillSixAndThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill6And4(int subId, int boyGirl) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);

        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

        MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0));
        int correctTestNumber = mathImages.getTestNumber();

        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();

        ArrayList<Integer> numeralsFromDB_2_Only = new ArrayList();
        numeralsFromDB_2_Only = NumeralHelper.getNumeralsBelowLimitRandom(mDbHelper.getReadableDatabase(), languageID,20, 2, correctTestNumber, boyGirl);
        for (int i=0; i < numeralsFromDB_2_Only.size(); i++ ) {
            Numerals numeralFromDB = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), numeralsFromDB_2_Only.get(i));
            numerals.add(new DraggableImage<>(0, 0, numeralFromDB.getBlackImage()));
        }
        Numerals numeralCorrectAnswer = NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, correctTestNumber);
        numerals.add(new DraggableImage<>(0, 1, numeralCorrectAnswer.getBlackImage()));

        String drillData = MathDrillJsonBuilder.getDrillSixAndFourJson(this,
                mathDrillFlowWord.getDrillSound1(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound2(),
                mathDrillFlowWord.getDrillSound3(),
                mathDrillFlowWord.getDrillSound4(),
                mathDrillFlowWord.getDrillSound5(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound6(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getImageName(),
                String.valueOf(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages()),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImages()).getBlackImage(),
                String.valueOf(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages()),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)).getNumberOfImages()).getBlackImage(),
                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)).getNumberOfImages()).getBlackImage(),
                numerals);
        Intent intent = new Intent(this, MathsDrillSixAndThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    public void playMathDrill7(int subId) {

        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);
        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);
        int patternIndexNumber=0;
        ArrayList<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();
        for (int i=0; i < mathImageList.size(); i++ ) {
            MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(i));
            DraggableImage<String> item = new DraggableImage<>(0, mathImages.getTestNumber(), mathImages.getImageName());
            if (mathImages.getNumberOfImages() == 0)
                patternIndexNumber = i;
            itemsToCompletePattern.add(item);
        }
        String drillData = MathDrillJsonBuilder.getDrillSevenJson(this, mathDrillFlowWord.getDrillSound1(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(patternIndexNumber)).getImageName(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(patternIndexNumber)).getNumberOfImagesSound(),
                itemsToCompletePattern,
                mathDrillFlowWord.getDrillSound2(),
                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)).getNumberOfImagesSound(),
                mathDrillFlowWord.getDrillSound3());
        Intent intent = new Intent(this, MathsDrillSevenActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    private void playMathDrill7And1(int subId) {
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);
        ArrayList<String> patternToComplete = new ArrayList<String>();
        ArrayList<Integer> mathImageList = new ArrayList();
        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);
        int patternIndexNumber=0;
        ArrayList<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();

        for (int i=0; i < mathImageList.size(); i++ ) {
            MathImages mathImages = MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(i));
            DraggableImage<String> item = new DraggableImage<>(0, mathImages.getTestNumber(), mathImages.getImageName());
            if ((mathImages.getNumberOfImages() == 1) && (mathImages.getTestNumber() == 0)) {
                itemsToCompletePattern.add(item);
            }
            if (mathImages.getNumberOfImages() == 0) {
                patternToComplete.add(mathImages.getImageName());
            }
            if ((mathImages.getNumberOfImages() == 1) && (mathImages.getTestNumber() == 1)) {
                patternToComplete.add("blank");
                itemsToCompletePattern.add(item);
            }

        }
        String drillData = MathDrillJsonBuilder.getDrillSevenAndOneJson(this, mathDrillFlowWord.getDrillSound1(), mathDrillFlowWord.getDrillSound2(), mathDrillFlowWord.getDrillSound3(), patternToComplete, itemsToCompletePattern);
        Intent intent = new Intent(this, MathsDrillSevenAndOneActivity.class);
        intent.putExtra("data", drillData);
        startActivity(intent);
    }

    private void updateStatus () {
        Unit UnitInfo = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitToBePlayed);
        UnitInfo.setUnitDrillLastPlayed(drillID);
        int dbUpdateResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), UnitInfo);
    }
}
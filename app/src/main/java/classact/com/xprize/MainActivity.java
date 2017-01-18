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
import classact.com.xprize.database.helpers.DrillFlowWordsHelper;
import classact.com.xprize.database.helpers.DrillWordHelper;
import classact.com.xprize.database.helpers.LetterHelper;
import classact.com.xprize.database.helpers.LetterSequenceHelper;
import classact.com.xprize.database.helpers.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helpers.MathImageHelper;
import classact.com.xprize.database.helpers.NumeralHelper;
import classact.com.xprize.database.helpers.UnitHelper;
import classact.com.xprize.database.helpers.WordHelper;
import classact.com.xprize.model.DrillFlowWords;
import classact.com.xprize.model.Letter;
import classact.com.xprize.model.MathDrillFlowWords;
import classact.com.xprize.model.MathImages;
import classact.com.xprize.model.Numerals;
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

        firstSectionFinished = true;
        runUnitAndDrill();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int u2p = UnitHelper.getUnitToBePlayed(mDbHelper.getReadableDatabase());
        Unit u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), u2p);
        int mDbResult = -1;

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
                    mDbResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    intent = new Intent(this, TutorialActivity.class);
                    startActivityForResult(intent, Code.TUT);
                    break;
                case Code.TUT:
                    u.setUnitFirstTime(1);
                    mDbResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    intent = new Intent(this, Chapter_01_Movie.class);
                    startActivityForResult(intent, Code.MV01);
                    break;
                case Code.MV01:
                    u.setUnitCompleted(1);
                    u.setUnitInProgress(0);
                    mDbResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    u = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), u.getUnitId() + 1);
                    mDbResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    if (u != null && u.getUnitCompleted() != 1) {
                        u.setUnitInProgress(1);
                        if (u.getUnitUnlocked() == 0) {
                            u.setUnitUnlocked(1);
                        }
                        mDbResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), u);
                    }
                    intent = new Intent(this, PhonicsLink.class);
                    startActivityForResult(intent, Code.PHON01);
                    break;
                case Code.PHON01:
                    firstSectionFinished = true;
                    runUnits();
                    break;
            /*
            case Code.PHON02:
            case Code.PHON03:
            case Code.PHON04:
            case Code.PHON05:
            case Code.PHON06:
            case Code.PHON07:
            case Code.PHON08:
            case Code.PHON09:
            case Code.PHON10:
            case Code.PHON11:
            case Code.PHON12:
            case Code.PHON13:
            case Code.PHON14:
            case Code.PHON15:
            case Code.PHON16:
            case Code.PHON17:
            case Code.PHON18:
            case Code.PHON19:
            case Code.PHON20:
                intent = new Intent(this, PhonicsLink.class);
                intent.putExtra(Code.UNI, mUnitId);
                intent.putExtra(Code.REQ, mCurrItem);
                startActivityForResult(intent, mNextItem);
                break;
            case Code.WORD01:
            case Code.WORD02:
            case Code.WORD03:
            case Code.WORD04:
            case Code.WORD05:
            case Code.WORD06:
            case Code.WORD07:
            case Code.WORD08:
            case Code.WORD09:
            case Code.WORD10:
            case Code.WORD11:
            case Code.WORD12:
            case Code.WORD13:
            case Code.WORD14:
            case Code.WORD15:
            case Code.WORD16:
            case Code.WORD17:
            case Code.WORD18:
            case Code.WORD19:
            case Code.WORD20:
                intent = new Intent(this, WordsLink.class);
                intent.putExtra(Code.UNI, mUnitId);
                intent.putExtra(Code.REQ, mCurrItem);
                startActivityForResult(intent, mNextItem);
                break;
            case Code.STOR01:
            case Code.STOR02:
            case Code.STOR03:
            case Code.STOR04:
            case Code.STOR05:
            case Code.STOR06:
            case Code.STOR07:
            case Code.STOR08:
            case Code.STOR09:
            case Code.STOR10:
            case Code.STOR11:
            case Code.STOR12:
            case Code.STOR13:
            case Code.STOR14:
            case Code.STOR15:
            case Code.STOR16:
            case Code.STOR17:
            case Code.STOR18:
            case Code.STOR19:
            case Code.STOR20:
                intent = new Intent(this, StoryLink.class);
                intent.putExtra(Code.UNI, mUnitId);
                intent.putExtra(Code.REQ, mCurrItem);
                startActivityForResult(intent, mNextItem);
                break;
            case Code.MATH01:
            case Code.MATH02:
            case Code.MATH03:
            case Code.MATH04:
            case Code.MATH05:
            case Code.MATH06:
            case Code.MATH07:
            case Code.MATH08:
            case Code.MATH09:
            case Code.MATH10:
            case Code.MATH11:
            case Code.MATH12:
            case Code.MATH13:
            case Code.MATH14:
            case Code.MATH15:
            case Code.MATH16:
            case Code.MATH17:
            case Code.MATH18:
            case Code.MATH19:
            case Code.MATH20:
                intent = new Intent(this, MathsLink.class);
                intent.putExtra(Code.UNI, mUnitId);
                intent.putExtra(Code.REQ, mCurrItem);
                startActivityForResult(intent, mNextItem);
                break;*/
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

    private void runUnitAndDrill (){
        try {

            // let's check which drill we are running and send through params for the json builder
            switch (drillID) {
                case 1: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // limit the words to 3 for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 3 random words based on the specific unit ID

                    ArrayList<Integer> drillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill1(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3());
                    break;
                }
                case 2: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 5; // 5 words for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill2(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(4)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2()

                    );
                    break;
                }
                case 3: {
                    int limit = 5; // 5 repeats for this drill so we choose 5 incorrect letters
                    DrillFlowWords drillFlowWord;

                    ArrayList<Integer> wrongLetters = new ArrayList();
                    wrongLetters = LetterSequenceHelper.getWrongLetters(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, limit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill3(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(0)),
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(1)),
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(2)),
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(3)),
                            LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, wrongLetters.get(4)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3());
                    break;
                }
                case 4: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 4; // limit the words to 4 for this drill
                    int wrongLimit = 2;
                    DrillFlowWords drillFlowWord;

                    //This will get 4 random words based on the specific unit ID

                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightLimit);

                    ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wrongLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill4(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(0)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), wrongDrillWordIDs.get(1)),
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3());
                    break;
                }

                case 5: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 3; // 5 words for this drill
                    int wrongLimit = 9;
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, rightLimit);

                    ArrayList<Integer>  wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(mDbHelper.getReadableDatabase(), languageID, unitToBePlayed, unitSubID, drillID, wordType, wrongLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, languageID);

                    playDrill5(LetterHelper.getLetter(mDbHelper.getReadableDatabase(), languageID, letterID),
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
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2());
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
                    int limit = 5; // 5 words for this drill
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
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(3)),
                            WordHelper.getWord(mDbHelper.getReadableDatabase(), rightDrillWordIDs.get(4)),
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

            }
            if (mathDrillID > 0) {
                switch (mathDrillID) {
                    case 1: {
                        int limit = 5; // Limit numbers to 5
                        MathDrillFlowWords mathDrillFlowWord;
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;


                        ArrayList<Integer> numerals = new ArrayList();
                        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                        mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                        playMathDrill1(NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                mathDrillFlowWord.getDrillSound1(),
                                mathDrillFlowWord.getDrillSound2());
                        break;
                    }
                    case 2: {
                        int limit = 3; // Limit numbers to 3
                        int subId = 0; // this runs the subIDs for the drillflow table
                        MathDrillFlowWords mathDrillFlowWord;
                        int boyGirl;
                        if (languageID == 1)
                            boyGirl = 2;
                        else
                            boyGirl = 1;


                        ArrayList<Integer>  numerals = new ArrayList();
                        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                        ArrayList<Integer>  mathImageList = new ArrayList();
                        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                        mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), mathDrillID, subId, languageID);

                        playMathDrill2(NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                mathDrillFlowWord.getDrillSound1(),
                                mathDrillFlowWord.getDrillSound2());
                        break;
                    }
                    case 3: {
                        int limit = 10; // Limit numbers to 10
                        int boyGirl = 1;
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int drillTestNumber = 2; // number we are testing
                        MathDrillFlowWords mathDrillFlowWord;

                        ArrayList<Integer>  numerals = new ArrayList();
                        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                        ArrayList<Integer>  mathImageList = new ArrayList();
                        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                        mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId,  languageID);

                        playMathDrill3(NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(6)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(7)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(8)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(9)),
                                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                drillTestNumber,
                                mathDrillFlowWord.getDrillSound1(),
                                mathDrillFlowWord.getDrillSound2(),
                                mathDrillFlowWord.getDrillSound2());
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
                        int drillTestNumber = 2; // number we are testing

                        MathDrillFlowWords mathDrillFlowWord;

                        ArrayList<Integer>  numerals = new ArrayList();
                        numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                        ArrayList<Integer> mathImageList = new ArrayList();
                        mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                        mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId,languageID);

                        playMathDrill4(NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                mathDrillFlowWord.getDrillSound1(),
                                mathDrillFlowWord.getDrillSound2(),
                                mathDrillFlowWord.getDrillSound3(),
                                mathDrillFlowWord.getDrillSound4()
                        );
                        break;
                    }
                    case 5: {
                        int subId = 0; // this runs the subIDs for the drillflow table
                        int boyGirl = 1;

                        if (unitToBePlayed < 9) {
                            int limit = 10; // Limit numbers to 10
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  numerals = new ArrayList();
                            numerals = NumeralHelper.getNumeralsBelowLimit(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                            ArrayList<Integer> mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill5(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(6)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(7)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(8)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(9)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3());
                            break;
                        } else {
                            int limit = 20; // Limit numbers to 20
                            subId = 1; // this runs the subIDs for the drillflow table
                            String answerImage = null; //NEED TO SORT OUT
                            String answerImage2 = null; //NEED TO SORT OUT
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  numerals = new ArrayList();
                            numerals = NumeralHelper.getNumeralsBelowLimitFromZero(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                            ArrayList<Integer> mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill5And1(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(6)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(7)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(8)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(9)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(10)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(11)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(12)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(13)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(14)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(15)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(16)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(17)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(18)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(19)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(20)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4());

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
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer> mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId,languageID);

                            playMathDrill6(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4());

                        } else if ((unitToBePlayed >= 6) && (unitToBePlayed < 10)) { //six and one
                            subId=1;
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID,subId, languageID);

                            playMathDrill6And1(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4());

                        } else if (unitToBePlayed == 10) { // six and two
                            subId=2;
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill6And2(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(3)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(4)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4());

                        } else if ((unitToBePlayed > 10) && (unitToBePlayed < 16)) { // six and three
                            subId=3;
                            int limit = 3; // Limit numbers to 5
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  numerals = new ArrayList();
                            numerals = NumeralHelper.getNumeralsBelowLimit235(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                            ArrayList<Integer>  mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill6And3(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4(),
                                    mathDrillFlowWord.getDrillSound5());

                        } else if (unitToBePlayed > 15) {  // six and four
                            subId=4;
                            int limit = 5; // Limit numbers to 5
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  numerals = new ArrayList();
                            numerals = NumeralHelper.getNumeralsBelowLimit12358(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                            ArrayList<Integer>  mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill6And4(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(2)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3(),
                                    mathDrillFlowWord.getDrillSound4(),
                                    mathDrillFlowWord.getDrillSound5(),
                                    mathDrillFlowWord.getDrillSound6());
                        }
                        break;
                    }

                    case 7: {
                        int subId = 0;
                        int boyGirl = 1;
                        if (unitToBePlayed < 10) {
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  mathImageList = new ArrayList();
                            mathImageList = MathImageHelper.getMathImageList(mDbHelper.getReadableDatabase(), unitToBePlayed, mathDrillID, languageID);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill7(MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(0)),
                                    MathImageHelper.getMathImage(mDbHelper.getReadableDatabase(), mathImageList.get(1)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3());
                        } else {
                            subId=1;
                            int limit = 8; // Limit numbers to 8
                            MathDrillFlowWords mathDrillFlowWord;

                            ArrayList<Integer>  numerals = new ArrayList();
                            numerals = NumeralHelper.getNumeralsBelowLimit12358(mDbHelper.getReadableDatabase(), languageID, limit, boyGirl);

                            mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(mDbHelper.getReadableDatabase(), drillID, subId, languageID);

                            playMathDrill7And1(NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(0)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(1)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(2)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(3)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(4)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(5)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(6)),
                                    NumeralHelper.getNumeral(mDbHelper.getReadableDatabase(), languageID, numerals.get(7)),
                                    mathDrillFlowWord.getDrillSound1(),
                                    mathDrillFlowWord.getDrillSound2(),
                                    mathDrillFlowWord.getDrillSound3());
                        }
                        break;
                    }
                }
            }

        } catch (SQLiteException sqle) {
            throw sqle;
        }

    }

    private void playDrill1(Letter letter,
                            Word word1,
                            Word word2,
                            Word word3,
                            String drillSound1,
                            String drillSound2,
                            String drillSound3) {
        int requestCode=-1;
        System.out.println(0);
        ObjectAndSound<String> letterObject = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        System.out.println(1);
        ArrayList<ObjectAndSound<String>> drillObjects = new ArrayList<ObjectAndSound<String>>();
        System.out.println(2);
        drillObjects.add(new ObjectAndSound<String>(word1.getWordPictureURI(), word1.getWordSoundURI(), word1.getWordSlowSoundURI()));
        System.out.println(3);
        drillObjects.add(new ObjectAndSound<String>(word2.getWordPictureURI(), word2.getWordSoundURI(), word2.getWordSlowSoundURI()));
        System.out.println(4);
        drillObjects.add(new ObjectAndSound<String>(word3.getWordPictureURI(), word3.getWordSoundURI(), word3.getWordSlowSoundURI()));
        System.out.println(5);
        String drillData = SoundDrillJsonBuilder.getSoundDrillOneJson(this, letterObject, drillSound1, drillSound2, drillSound3, drillObjects);
        System.out.println(6);
        Intent intent = new Intent(this, SoundDrillOneActivity.class);
        System.out.println(7);
        intent.putExtra("data", drillData);
        System.out.println(8);
        startActivityForResult(intent,1);
        System.out.println(9);
        //startActivity(intent);
        //startActivityForResult(intent, requestCode);
    }

    private void playDrill2(Letter letter, Word rightWord1,
                            Word rightWord2,
                            Word rightWord3,
                            Word rightWord4,
                            Word rightWord5,
                            Word wrongWord1,
                            Word wrongWord2,
                            Word wrongWord3,
                            Word wrongWord4,
                            Word wrongWord5,
                            String drillSound1,
                            String drillSound2) {

        ArrayList<RightWrongPair> pairs = new ArrayList<RightWrongPair>();
        ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        ObjectAndSound<String> wrongObject = new ObjectAndSound<>(wrongWord1.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
        pairs.add(pair);

        rightObject = new ObjectAndSound<>(rightWord2.getWordPictureURI(), rightWord2.getWordSoundURI(), "");
        wrongObject = new ObjectAndSound<>(wrongWord2.getWordPictureURI(), wrongWord2.getWordSoundURI(), "");
        pair = new RightWrongPair(rightObject, wrongObject);
        pairs.add(pair);

        rightObject = new ObjectAndSound<>(rightWord3.getWordPictureURI(), rightWord3.getWordSoundURI(), "");
        wrongObject = new ObjectAndSound<>(wrongWord3.getWordPictureURI(), wrongWord3.getWordSoundURI(), "");
        pair = new RightWrongPair(rightObject, wrongObject);
        pairs.add(pair);

        rightObject = new ObjectAndSound<>(rightWord4.getWordPictureURI(), rightWord4.getWordSoundURI(), "");
        wrongObject = new ObjectAndSound<>(wrongWord4.getWordPictureURI(), wrongWord4.getWordSoundURI(), "");
        pair = new RightWrongPair(rightObject, wrongObject);
        pairs.add(pair);

        rightObject = new ObjectAndSound<>(rightWord5.getWordPictureURI(), rightWord5.getWordSoundURI(), "");
        wrongObject = new ObjectAndSound<>(wrongWord5.getWordPictureURI(), wrongWord5.getWordSoundURI(), "");
        pair = new RightWrongPair(rightObject, wrongObject);
        pairs.add(pair);

        String drillData = SoundDrillJsonBuilder.getSoundDrillTwoJson(this,
                letter.getPhonicSoundURI(),
                drillSound1,
                drillSound2,
                pairs
        );
        Intent intent = new Intent(this, SoundDrillTwoActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,2);
    }

    public void playDrill3(Letter letter,
                           Letter wrongLetter1,
                           Letter wrongLetter2,
                           Letter wrongLetter3,
                           Letter wrongLetter4,
                           Letter wrongLetter5,
                           String drillSound1,
                           String drillSound2,
                           String drillSound3) {
        ArrayList<SoundDrillThreeObject> sets = new ArrayList<SoundDrillThreeObject>();

        ObjectAndSound<String> objectAndSound = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        ObjectAndSound rightObject = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), "", "");
        ObjectAndSound wrongObject = new ObjectAndSound(wrongLetter1.getLetterPictureLowerCaseBlackURI(), "", "");
        RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
        SoundDrillThreeObject obj = new SoundDrillThreeObject(objectAndSound, pair);
        sets.add(obj);

        //Object 2
        objectAndSound = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        rightObject = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), "", "");
        wrongObject = new ObjectAndSound(wrongLetter2.getLetterPictureLowerCaseBlackURI(), "", "");
        pair = new RightWrongPair(rightObject, wrongObject);
        obj = new SoundDrillThreeObject(objectAndSound, pair);
        sets.add(obj);
        //Object 3
        objectAndSound = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        rightObject = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), "", "");
        wrongObject = new ObjectAndSound(wrongLetter3.getLetterPictureLowerCaseBlackURI(), "", "");
        pair = new RightWrongPair(rightObject, wrongObject);
        obj = new SoundDrillThreeObject(objectAndSound, pair);
        sets.add(obj);
        //Object 4
        objectAndSound = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        rightObject = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), "", "");
        wrongObject = new ObjectAndSound(wrongLetter4.getLetterPictureLowerCaseBlackURI(), "", "");
        pair = new RightWrongPair(rightObject, wrongObject);
        obj = new SoundDrillThreeObject(objectAndSound, pair);
        sets.add(obj);
        //

        objectAndSound = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
        rightObject = new ObjectAndSound(letter.getLetterPictureLowerCaseBlackURI(), "", "");
        wrongObject = new ObjectAndSound(wrongLetter5.getLetterPictureLowerCaseBlackURI(), "", "");
        pair = new RightWrongPair(rightObject, wrongObject);
        obj = new SoundDrillThreeObject(objectAndSound, pair);
        sets.add(obj);
        //

        String drillData = SoundDrillJsonBuilder.getSoundDrillThreeJson(this, drillSound1, drillSound2, drillSound3, sets);
        Intent intent = new Intent(this, SoundDrillThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,3);
    }

    public void playDrill4(Letter letter,
                           Word word1,
                           Word word2,
                           Word word3,
                           Word word4,
                           Word word5,
                           Word word6,
                           String drillSound1,
                           String drillSound2,
                           String drillSound3) {
        ArrayList<DraggableImage<ObjectAndSound>> images = new ArrayList<DraggableImage<ObjectAndSound>>();
        ObjectAndSound obj = new ObjectAndSound(word1.getWordPictureURI(), word1.getWordSoundURI(), "");
        DraggableImage<ObjectAndSound> image = new DraggableImage<ObjectAndSound>(1, 1, obj);
        images.add(image);
        obj = new ObjectAndSound(word2.getWordPictureURI(), word1.getWordSoundURI(), "");
        image = new DraggableImage<ObjectAndSound>(2, 0, obj);
        images.add(image);
        obj = new ObjectAndSound(word5.getWordPictureURI(), word1.getWordSoundURI(), "");
        image = new DraggableImage<ObjectAndSound>(3, 1, obj);
        images.add(image);
        obj = new ObjectAndSound(word4.getWordPictureURI(), word1.getWordSoundURI(), "");
        image = new DraggableImage<ObjectAndSound>(4, 0, obj);
        images.add(image);
        obj = new ObjectAndSound(word6.getWordPictureURI(), word1.getWordSoundURI(), "");
        image = new DraggableImage<ObjectAndSound>(5, 1, obj);
        images.add(image);
        obj = new ObjectAndSound(word3.getWordPictureURI(), word1.getWordSoundURI(), "");
        image = new DraggableImage<ObjectAndSound>(6, 1, obj);
        images.add(image);
        String drillData = SoundDrillJsonBuilder.getSoundDrillFourActivity(this, letter.getLetterSoundURI(), drillSound3, drillSound1, drillSound2, images);
        Intent intent = new Intent(this, SoundDrillFourActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,4);
    }

    public void playDrill5(Letter letter,
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
                           String drillSound1,
                           String drillSound2) {

        ArrayList<SoundDrillFiveObject> soundDrillFiveObjects = new ArrayList<SoundDrillFiveObject>();
        //One
        ArrayList<ObjectAndSound> images = new ArrayList<ObjectAndSound>();
        ObjectAndSound objectAndSound = new ObjectAndSound(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("1");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord1.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord2.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord3.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        ObjectAndSound drillObject = new ObjectAndSound(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        drillObject.setBeginningLetterSound(letter.getLetterSoundURI());
        SoundDrillFiveObject obj = new SoundDrillFiveObject(drillObject, images);
        soundDrillFiveObjects.add(obj);
        //Two
        images = new ArrayList<ObjectAndSound>();
        objectAndSound = new ObjectAndSound(rightWord2.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("1");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord4.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord5.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord6.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        drillObject = new ObjectAndSound(rightWord2.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        drillObject.setBeginningLetterSound(letter.getLetterSoundURI());
        obj = new SoundDrillFiveObject(drillObject, images);
        soundDrillFiveObjects.add(obj);
        //Three
        images = new ArrayList<ObjectAndSound>();
        objectAndSound = new ObjectAndSound(rightWord3.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("1");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord7.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord8.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        objectAndSound = new ObjectAndSound(wrongWord9.getWordPictureURI(), wrongWord1.getWordSoundURI(), "");
        objectAndSound.setCustomData("0");
        images.add(objectAndSound);
        drillObject = new ObjectAndSound(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        drillObject.setBeginningLetterSound(letter.getLetterSoundURI());
        obj = new SoundDrillFiveObject(drillObject, images);
        soundDrillFiveObjects.add(obj);
        //
        String drillData = SoundDrillJsonBuilder.getSoundDrillFiveJson(this, drillSound1, drillSound2, soundDrillFiveObjects);
        Intent intent = new Intent(this, SoundDrillFiveActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,5);
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
        startActivityForResult(intent,6);
    }

    public void playDrill7(DbHelper mDbHelper,
                           int languageID,
                           Letter letter,
                           Word rightWord1,
                           Word rightWord2,
                           Word rightWord3,
                           Word rightWord4,
                           Word rightWord5,
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
        ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        ObjectAndSound<String> word = new ObjectAndSound<>(rightWord1.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
        rightObject.setObjectSlowSound(rightWord1.getWordSlowSoundURI());
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
        rightObject.setSpelling(sb.toString());
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
        word = new ObjectAndSound<>(rightWord2.getWordPictureURI(), rightWord2.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord2.getWordSlowSoundURI());
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
        letterImages.add(new DraggableImage<String>(0, 0, rightWord2.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord3.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 1, wrongWord4.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);

        //Third Word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord3.getWordPictureURI(), rightWord3.getWordSoundURI(), "");
        word.setObjectSlowSound(rightWord3.getWordSlowSoundURI());
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
        letterImages.add(new DraggableImage<String>(0, 1, rightWord3.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord5.getWordPictureURI()));
        letterImages.add(new DraggableImage<String>(0, 0, wrongWord6.getWordPictureURI()));
        spelledWord.setLettersImages(letterImages);
        words.add(spelledWord);

        //Fourth word
        spelledWord = new SpelledWord();
        word = new ObjectAndSound<>(rightWord4.getWordPictureURI(), rightWord4.getWordSoundURI(), "");
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
        word = new ObjectAndSound<>(rightWord5.getWordPictureURI(), rightWord1.getWordSoundURI(), "");
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
        String drillData = SoundDrillJsonBuilder.getSoundDrillSevenJson(this, "touch", words);
        Intent intent = new Intent(this, SoundDrillSevenActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,7);
    }

    public void playDrill8() {
        String drillData = SoundDrillJsonBuilder.getSoundDrillEightJson(this,
                "a_lower_path",
                "lets_learn_how_to_write_upper",
                "lets_learn_how_to_write_lower",
                "watch",
                "now_you_write",
                "alower_dots",
                "letter_a",
                "a_upper_path",
                "aupper_dots");
        Intent intent = new Intent(this, SoundDrillEightActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,8);
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
        startActivityForResult(intent,9);
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
        String drillData = SoundDrillJsonBuilder.getSoundDrillElevenJson(this, drillSound1, drillSound2, "g_1", words);
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
        sb=null;
        count = 0;
        for (int i = 0; i < word2.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word2.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word2.getWordName().length() - word2.getWordName().substring(0,word2.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
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
        //Three
        set = new SpelledWord();
        word = new ObjectAndSound<>("",word3.getWordSoundURI(),"");
        word.setSpelling(word3.getWordName());
        set.setWord(word);
        items = new ArrayList<>();
        sb=null;
        count = 0;
        for (int i = 0; i < word1.getWordName().length(); i++) {
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
        set = new SpelledWord();
        word = new ObjectAndSound<>("",word4.getWordSoundURI(),"");
        word.setSpelling(word4.getWordName());
        set.setWord(word);
        items = new ArrayList<>();
        sb=null;

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
        //
        String drillData =SoundDrillJsonBuilder.getSoundDrillThirteenJson(this,drillSound1,drillSound2,words);
        Intent intent = new Intent(this,SoundDrillThirteenActivity.class);
        intent.putExtra("data",drillData);
        startActivityForResult(intent,13);
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
        for (int i = 0; i < word1.getWordName().length(); i++) {
            Letter thisLetter = LetterHelper.getLetterByName(mDbHelper.getReadableDatabase(), languageID, Character.toString(word3.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word3.getWordName().length() - word3.getWordName().substring(0, word3.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
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
        //
        String drillData = SoundDrillJsonBuilder.getSoundDrillFourteenJson(this, drillSound1, drillSound2, drillSound3, words);
        Intent intent = new Intent(this, SoundDrillThirteenActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,14);

    }


    public void playDrill15() {
        ArrayList<Sentence> sentences = new ArrayList<>();
        Sentence sentence = new Sentence(2,"sentence1");
        ArrayList<DraggableImage<String>> words = new ArrayList<>();
        DraggableImage<String> word = new DraggableImage(0,0,"");
        word.setContent("play_black,rat"); //Image + Sound o the word
        word.setExtraData("1"); //Positions it can occupy
        words.add(word);
        word = new DraggableImage(0,0,"\"play_black&rat\"");
        word.setContent("play_black,rat"); //Image + Sound o the word
        word.setExtraData("1"); //Positions it can occupy
        words.add(word);
        //
        sentence.setWords(words);
        sentences.add(sentence);
        //
        String drillData = SoundDrillJsonBuilder.getSoundDrillFifteenJson(this,"drill15drillsound1","drill15drillsound1",sentences);
        Intent intent = new Intent(this,SoundDrillFifteenActivity.class);
        intent.putExtra("data",drillData);
        startActivityForResult(intent,15);
    }

    public void playSimpleStory() {
        ArrayList<SimpleStorySentence> sentences = new ArrayList<>();
        //Sentence1
        SimpleStorySentence sentence = new SimpleStorySentence();
        sentence.setFullSound("ss1line1");
        ArrayList<classact.com.xprize.control.Word> words = new ArrayList<>();
        //--word
        classact.com.xprize.control.Word word = new classact.com.xprize.control.Word(1, "this_black_cap", "this_red_cap");
        word.setSound("this_sound");
        words.add(word);
        //--word
        word = new classact.com.xprize.control.Word(1, "is_black", "is_red");
        word.setSound("is_sound");
        words.add(word);
        //--word
        word = new classact.com.xprize.control.Word(1, "bahati_head1pic", "");
        word.setSound("bahati");
        words.add(word);
        //
        sentence.setWords(words);
        sentences.add(sentence);
        //Sentence 2
        sentence = new SimpleStorySentence();
        sentence.setFullSound("ss1line2");
        words = new ArrayList<>();
        //--word
        word = new classact.com.xprize.control.Word(1, "this_black_cap", "this_red_cap");
        word.setSound("this_sound");
        words.add(word);
        //--word
        word = new classact.com.xprize.control.Word(1, "is_black", "is_red");
        word.setSound("is_sound");
        words.add(word);
        //--word
        word = new classact.com.xprize.control.Word(1, "dama_head1pic", "");
        word.setSound("dama");
        words.add(word);
        //
        sentence.setWords(words);
        sentences.add(sentence);
        //
        ArrayList<ComprehensionQuestion> questions = new ArrayList<>();
        //Question 1
        ComprehensionQuestion question = new ComprehensionQuestion("who_sound", "ss1line1", 0);
        ArrayList<DraggableImage<String>> images = new ArrayList<>();
        DraggableImage<String> image = new DraggableImage<>(0, 1, "bahati_head1pic");
        images.add(image);
        image = new DraggableImage<>(0, 1, "bahati_head1pic");
        images.add(image);
        question.setImages(images);
        questions.add(question);
        //Question 2  -- Kids must touch one of 3 images
        question = new ComprehensionQuestion("who_sound", "ss1line2", 1);
        images = new ArrayList<>();
        image = new DraggableImage<>(0, 0, "bahati_head1pic");
        images.add(image);
        image = new DraggableImage<>(0, 1, "dama_head1pic");
        images.add(image);
        image = new DraggableImage<>(0, 0, "mother_head1pic");
        images.add(image);
        question.setImages(images);
        questions.add(question);
        //
        String drillData = SimpleStoryJsonBuilder.getSimpleStoryJson(this,
                "story_link",
                "ssi_1",
                "ssi_5",
                "ssi_6",
                "ssi_16",
                "ssi_10",
                "ssi_15",
                "ssi_13",
                "ss1_with_no_sounding",
                "ssi_17",
                "ss1_image",
                sentences,
                "comp5",
                "comp6",
                questions);
        Intent intent = new Intent(this, SimpleStoryActivity.class);

        intent.putExtra("data", drillData);
        startActivity(intent);
    }
    public void playMathDrill1(Numerals numeral1,
                               Numerals numeral2,
                               Numerals numeral3,
                               Numerals numeral4,
                               Numerals numeral5,
                               String drillSound1,
                               String drillSound2) {
        ArrayList<Numeral> numerals = new ArrayList<>();
        Numeral numeral = new Numeral(numeral1.getSound(), numeral1.getBlackImage(), numeral1.getSparklingImage());
        numerals.add(numeral);
        numeral = new Numeral(numeral2.getSound(), numeral2.getBlackImage(), numeral2.getSparklingImage());
        numerals.add(numeral);
        numeral = new Numeral(numeral3.getSound(), numeral3.getBlackImage(), numeral3.getSparklingImage());
        numerals.add(numeral);
        numeral = new Numeral(numeral4.getSound(), numeral4.getBlackImage(), numeral4.getSparklingImage());
        numerals.add(numeral);
        numeral = new Numeral(numeral5.getSound(), numeral5.getBlackImage(), numeral5.getSparklingImage());
        numerals.add(numeral);
        String drillData = MathDrillJsonBuilder.getDrillOneJson(this,
                drillSound1,
                drillSound2,
                numerals);
        Intent intent = new Intent(this, MathsDrillOneActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,17);
    }

    public void playMathDrill3(Numerals numeral1,
                               Numerals numeral2,
                               Numerals numeral3,
                               Numerals numeral4,
                               Numerals numeral5,
                               Numerals numeral6,
                               Numerals numeral7,
                               Numerals numeral8,
                               Numerals numeral9,
                               Numerals numeral10,
                               MathImages mathImages,
                               int drillTestNumber,
                               String drillSound1,
                               String drillSound2,
                               String drillSound3) {
        String drillData = MathDrillJsonBuilder.getDrillThreeJson(this,
                drillSound1, drillTestNumber, mathImages.getNumberOfImagesSound(), drillSound2, drillSound3, mathImages.getNumberOfImages(), mathImages.getImageName(),
                numeral1.getSound(), numeral2.getSound(), numeral3.getSound(), numeral4.getSound(), numeral5.getSound(), numeral6.getSound(), numeral7.getSound(), numeral8.getSound(), numeral9.getSound(), numeral10.getSound());
        Intent intent = new Intent(this, MathsDrillThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,18);
    }

    public void playMathDrill2(Numerals numeral1,
                               Numerals numeral2,
                               Numerals numeral3,
                               MathImages mathImages,
                               String drillSound1,
                               String drillSound2) {
        ArrayList<ObjectAndSound<String>> numbers = new ArrayList<>();
        ObjectAndSound<String> number = new ObjectAndSound<>(numeral1.getBlackImage(), numeral1.getSound(), "");
        number.setCustomData("1");
        numbers.add(number);
        number = new ObjectAndSound<>(numeral2.getBlackImage(), numeral2.getSound(), "");
        number.setCustomData("0");
        numbers.add(number);
        number = new ObjectAndSound<>(numeral3.getBlackImage(), numeral3.getSound(), "");
        number.setCustomData("0");
        numbers.add(number);
        String drillData = MathDrillJsonBuilder.getDrillTwoJson(this, drillSound1, mathImages.getNumberOfImagesSound(), mathImages.getNumberOfImages(), mathImages.getImageName(), drillSound2, mathImages.getImageSound(), numbers);
        Intent intent = new Intent(this, MathsDrillTwoActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,19);
    }

    public void playMathDrill4(Numerals numeral1,
                               Numerals numeral2,
                               MathImages mathImage1,
                               MathImages mathImage2,
                               String drillSound1,
                               String drillSound2,
                               String drillSound3,
                               String drillSound4) {
        String drillData = MathDrillJsonBuilder.getDrillFourJson(this, "yes",
                drillSound1, mathImage1.getNumberOfImagesSound(), mathImage2.getNumberOfImagesSound(), drillSound2, drillSound3, drillSound4,
                mathImage1.getNumberOfImages(), mathImage1.getImageName(), numeral1.getBlackImage(), mathImage2.getNumberOfImages(), mathImage2.getImageName(), numeral2.getBlackImage()
        );
        Intent intent = new Intent(this, MathsDrillFourActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,20);
    }

    public void playMathDrill5(MathImages mathImage1,
                               MathImages mathImage2,
                               Numerals numeral1,
                               Numerals numeral2,
                               Numerals numeral3,
                               Numerals numeral4,
                               Numerals numeral5,
                               Numerals numeral6,
                               Numerals numeral7,
                               Numerals numeral8,
                               Numerals numeral9,
                               Numerals numeral10,
                               String drillSound1,
                               String drillSound2,
                               String drillSound3) {
        ArrayList<ObjectAndSound<String>> items = new ArrayList<>();
        ObjectAndSound<String> item = new ObjectAndSound<>(mathImage1.getImageName(), mathImage1.getImageSound(), "");
        item.setCustomData("1");
        items.add(item);
        item = new ObjectAndSound<>(mathImage2.getImageName(), mathImage2.getImageSound(), "");
        item.setCustomData("2");
        items.add(item);
        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();
        numerals.add(new DraggableImage<>(0, 0, numeral1.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 1, numeral2.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 0, numeral3.getBlackImage()));
        String drillData = MathDrillJsonBuilder.getDrillFiveJson(this, drillSound1, drillSound2,
                drillSound3, items, numerals, numeral1.getSound(), numeral2.getSound(), numeral3.getSound(), numeral4.getSound(), numeral5.getSound(), numeral6.getSound(), numeral7.getSound(), numeral8.getSound(), numeral9.getSound(), numeral10.getSound()
        );
        Intent intent = new Intent(this, MathsDrillFiveActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,21);
    }

    public void playMathDrill5And1(MathImages mathImage1,
                                   MathImages mathImage2,
                                   Numerals numeral0,
                                   Numerals numeral1,
                                   Numerals numeral2,
                                   Numerals numeral3,
                                   Numerals numeral4,
                                   Numerals numeral5,
                                   Numerals numeral6,
                                   Numerals numeral7,
                                   Numerals numeral8,
                                   Numerals numeral9,
                                   Numerals numeral10,
                                   Numerals numeral11,
                                   Numerals numeral12,
                                   Numerals numeral13,
                                   Numerals numeral14,
                                   Numerals numeral15,
                                   Numerals numeral16,
                                   Numerals numeral17,
                                   Numerals numeral18,
                                   Numerals numeral19,
                                   Numerals numeral20,
                                   String drillSound1,
                                   String drillSound2,
                                   String drillSound3,
                                   String drillSound4) {
        ArrayList<ObjectAndSound<String>> items = new ArrayList<>();
        ObjectAndSound<String> item = new ObjectAndSound<>(mathImage1.getImageName(), mathImage1.getImageSound(), numeral10.getBlackImage());
        item.setCustomData("10");
        items.add(item);
        item = new ObjectAndSound<>(mathImage2.getImageName(), mathImage2.getNumberOfImagesSound(), numeral0.getBlackImage());
        item.setCustomData("0");
        items.add(item);
        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();
        numerals.add(new DraggableImage<>(0, 0, numeral1.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 1, numeral10.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 0, numeral8.getBlackImage()));
        String drillData = MathDrillJsonBuilder.getDrillFiveAndOneJson(this, drillSound1, drillSound2,
                drillSound3, drillSound4, items, numerals, numeral10.getBlackImage(), numeral1.getSound(), numeral2.getSound(), numeral3.getSound(), numeral4.getSound(), numeral5.getSound(), numeral6.getSound(), numeral7.getSound(), numeral8.getSound(), numeral9.getSound(),
                numeral10.getSound(), numeral11.getSound(), numeral12.getSound(), numeral13.getSound(), numeral14.getSound(), numeral15.getSound(), numeral16.getSound(), numeral17.getSound(), numeral18.getSound(), numeral19.getSound(), numeral20.getSound()
        );
        Intent intent = new Intent(this, MathsDrillFiveAndOneActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,21);
    }

    public void playMathDrill6(MathImages mathImage1,
                               String drillSound1,
                               String drillSound2,
                               String drillSound3,
                               String drillSound4) {
        String drillData = MathDrillJsonBuilder.getDrillSixJson(this, drillSound1, drillSound2, mathImage1.getImageSound(),
                drillSound3, drillSound4, mathImage1.getImageName(), mathImage1.getImageName());
        Intent intent = new Intent(this, MathsDrillSixActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,22);
    }

    public void playMathDrill6And1(MathImages mathImage1,
                                   MathImages mathImage2,
                                   String drillSound1,
                                   String drillSound2,
                                   String drillSound3,
                                   String drillSound4) {
        String drillData = MathDrillJsonBuilder.getDrillSixAndOneJson(this, drillSound1, drillSound2, mathImage1.getNumberOfImagesSound(),
                drillSound3, drillSound4, mathImage2.getNumberOfImagesSound(), mathImage1.getImageName(), "large");

        Intent intent = new Intent(this, MathsDrillSixAndOneActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,22);
    }

    public void playMathDrill6And2(MathImages mathImage1,
                                   MathImages mathImage2,
                                   MathImages mathImage3,
                                   MathImages mathImage4,
                                   MathImages mathImage5,
                                   String drillSound1,
                                   String drillSound2,
                                   String drillSound3,
                                   String drillSound4) {
        String drillData = MathDrillJsonBuilder.getDrillSixAndTwoJson(this, drillSound1, drillSound2,
                mathImage3.getNumberOfImagesSound(),
                mathImage5.getNumberOfImagesSound(), "and",
                drillSound3, drillSound4, mathImage4.getImageSound(), mathImage3.getImageName(), mathImage1.getImageName(), mathImage2.getImageName());
        Intent intent = new Intent(this, MathsDrillSixAndTwoActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,22);
    }

    public void playMathDrill6And3(MathImages mathImage1,
                                   Numerals numeral2,
                                   Numerals numeral3,
                                   Numerals numeral5,
                                   String drillSound1,
                                   String drillSound2,
                                   String drillSound3,
                                   String drillSound4,
                                   String drillSound5) {
        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();
        numerals.add(new DraggableImage<>(0, 0, numeral5.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 0, numeral2.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 1, numeral3.getBlackImage()));
        String drillData = MathDrillJsonBuilder.getDrillSixAndThreeJson(this, drillSound1, drillSound2, mathImage1.getNumberOfImagesSound(),
                mathImage1.getImageSound(), drillSound3,
                drillSound4, drillSound5, mathImage1.getImageName(), "5", "2", numerals);
        Intent intent = new Intent(this, MathsDrillSixAndThreeActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,22);
    }

    public void playMathDrill6And4(MathImages mathImage1,
                                   MathImages mathImage2,
                                   MathImages mathImage3,
                                   Numerals numeral1,
                                   Numerals numeral2,
                                   Numerals numeral3,
                                   Numerals numeral5,
                                   Numerals numeral8,
                                   String drillSound1,
                                   String drillSound2,
                                   String drillSound3,
                                   String drillSound4,
                                   String drillSound5,
                                   String drillSound6) {
        ArrayList<DraggableImage<String>> numerals = new ArrayList<>();
        numerals.add(new DraggableImage<>(0, 0, numeral1.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 1, numeral3.getBlackImage()));
        numerals.add(new DraggableImage<String>(0, 0, numeral8.getBlackImage()));
        String drillData = MathDrillJsonBuilder.getDrillSixAndFourJson(this, drillSound1,
                mathImage1.getNumberOfImagesSound(), mathImage2.getNumberOfImagesSound(),
                drillSound2, drillSound3, drillSound4, drillSound5, mathImage3.getNumberOfImagesSound(),
                drillSound6, mathImage1.getImageName(), "5", numeral5.getBlackImage(), "2", numeral2.getBlackImage(), numeral3.getBlackImage(), numerals);
        Intent intent = new Intent(this, MathsDrillSixAndFourActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,22);
    }

    public void playMathDrill7(MathImages mathImage2,
                               MathImages mathImage3,
                               String drillSound1,
                               String drillSound2,
                               String drillSound3) {
        ArrayList<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();
        DraggableImage<String> item = new DraggableImage<>(0, 0, mathImage2.getImageName());
        itemsToCompletePattern.add(item);
        item = new DraggableImage<>(0, 1, mathImage2.getImageName());
        itemsToCompletePattern.add(item);
        String drillData = MathDrillJsonBuilder.getDrillSevenJson(this, drillSound1,
                mathImage3.getImageName(),
                mathImage3.getNumberOfImagesSound(),
                itemsToCompletePattern,
                drillSound2,
                mathImage2.getImageSound(),
                drillSound3);
        Intent intent = new Intent(this, MathsDrillSevenActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,23);
    }

    private void playMathDrill7And1(Numerals numeral1,
                                    Numerals numeral2,
                                    Numerals numeral3,
                                    Numerals numeral4,
                                    Numerals numeral5,
                                    Numerals numeral6,
                                    Numerals numeral7,
                                    Numerals numeral8,
                                    String drillSound1,
                                    String drillSound2,
                                    String drillSound3) {
        ArrayList<String> patternToComplete = new ArrayList<String>();
        patternToComplete.add(numeral2.getBlackImage());
        patternToComplete.add(numeral3.getBlackImage());
        patternToComplete.add(numeral4.getBlackImage());
        patternToComplete.add(numeral5.getBlackImage());
        patternToComplete.add(numeral6.getBlackImage());
        patternToComplete.add(numeral7.getBlackImage());
        ArrayList<DraggableImage<String>> itemsToCompletePattern = new ArrayList<>();
        itemsToCompletePattern.add(new DraggableImage<>(0, 0, numeral7.getBlackImage()));
        itemsToCompletePattern.add(new DraggableImage<>(0, 0, numeral1.getBlackImage()));
        itemsToCompletePattern.add(new DraggableImage<>(0, 1, numeral4.getBlackImage()));
        String drillData = MathDrillJsonBuilder.getDrillSevenAndOneJson(this, drillSound1, drillSound2, drillSound3, patternToComplete, itemsToCompletePattern);
        Intent intent = new Intent(this, MathsDrillSevenAndOneActivity.class);
        intent.putExtra("data", drillData);
        startActivityForResult(intent,23);
    }

    private void updateStatus () {
        Unit UnitInfo = UnitHelper.getUnitInfo(mDbHelper.getReadableDatabase(), unitToBePlayed);
        UnitInfo.setUnitDrillLastPlayed(drillID);
        int dbUpdateResult = UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), UnitInfo);
    }
}
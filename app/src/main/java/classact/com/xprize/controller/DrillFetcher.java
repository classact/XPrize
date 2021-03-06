package classact.com.xprize.controller;


import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.activity.drill.books.StoryActivity;
import classact.com.xprize.activity.drill.math.MathsDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillEightActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFiveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillNineActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSixActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThreeActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwoActivity;
import classact.com.xprize.activity.drill.tutorial.Tutorial;
import classact.com.xprize.activity.link.LevelCompleteLink;
import classact.com.xprize.activity.link.MathsLink;
import classact.com.xprize.activity.link.PhonicsLink;
import classact.com.xprize.activity.link.StoryLink;
import classact.com.xprize.activity.link.WordsLink;
import classact.com.xprize.activity.menu.controller.DatabaseController;
import classact.com.xprize.activity.movie.MovieActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.catalogue.MathDrills;
import classact.com.xprize.controller.catalogue.StoryDrills;
import classact.com.xprize.controller.catalogue.WordDrills;
import classact.com.xprize.database.DbAccessor;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.LetterSequenceHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.Drill;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.DrillType;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.database.model.Section;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.locale.Languages;

public class DrillFetcher extends DbAccessor {

    public boolean mPhonicsStarted = false;
    public boolean mWordsStarted = false;
    public boolean mBooksStarted = false;
    public boolean mMathsStarted = false;

    private Context context;
    private DatabaseController mDb;
    private WordDrills wordDrills;
    private StoryDrills storyDrills;
    private MathDrills mathDrills;
    private LetterHelper letterHelper;

    @Inject
    public DrillFetcher(
            Context context,
            DatabaseController mDb,
            DbHelper dbHelper,
            WordDrills wordDrills,
            StoryDrills storyDrills, MathDrills mathDrills,
            LetterHelper letterHelper) {
        super(dbHelper);
        this.context = context;
        this.mDb = mDb;
        this.wordDrills = wordDrills;
        this.storyDrills = storyDrills;
        this.mathDrills = mathDrills;
        this.letterHelper = letterHelper;
    }

    public void fetch(Object[] objectArray, int languageId, UnitSectionDrill unitSectionDrill) {

        try {

            // Extract ids
            int unitSectionDrillId = unitSectionDrill.getUnitSectionDrillId();
            int unitSectionId = unitSectionDrill.getUnitSectionId();
            int drillId = unitSectionDrill.getDrillId();

            // Get unit section
            UnitSection unitSection = mDb.getUnitSection(unitSectionId);

            // Get section
            int sectionId = unitSection.getSectionId();
            Section section = mDb.getSections().get(sectionId);

            // Get drill and drill type name
            Drill drill = mDb.getDrills().get(drillId);
            int drillTypeId = drill.getDrillTypeId();
            DrillType drillType = mDb.getDrillTypes().get(drillTypeId);

            // Get unit
            int unitId = unitSection.getUnitId();
            Unit unit = mDb.getUnit(unitId);

            String sectionName = section.getName();
            String drillTypeName = drillType.getName();

            Intent intent = null;
            int resultCode = 0;

            System.out.println("DrillFetcher: " + drillTypeName + " -> " + unitSectionDrillId + ", " + unitSection.getUnitSectionId());

            if (drillTypeName.equalsIgnoreCase("Cinematic")) {
                if (sectionName.equalsIgnoreCase("Intro")) {
                    intent = new Intent(context, MovieActivity.class);
                    resultCode = Code.INTRO;
                } else if (sectionName.equalsIgnoreCase("Chapter End")) {
                    intent = new Intent(context, LevelCompleteLink.class);
                    intent.putExtra(Code.RES_NAME, "level" + unitId);
                    intent.putExtra(Code.NEXT_BG_RES, "star_level_" + unitId);
                    if (unitId > 0) {
                        intent.putExtra(Code.PREV_BG_RES, "star_level_" + (unitId-1));
                    }
                    resultCode = Code.CHAPTER_END;
                } else if (sectionName.equalsIgnoreCase("Finale")) {
                    intent = new Intent(context, MovieActivity.class);
                    intent.putExtra(Code.RES_NAME, unit.getUnitFirstTimeMovieFile());
                    intent.putExtra(Code.NEXT_BG_CODE, Code.FINALE);
                    resultCode = Code.FINALE;
                }
            } else if (drillTypeName.equalsIgnoreCase("Movie")) {
                if (sectionName.equalsIgnoreCase("Story")) {
                    intent = new Intent(context, MovieActivity.class);
                    resultCode = Code.MOVIE;
                }
            } else if (drillTypeName.equalsIgnoreCase("Tutorial")) {
                intent = new Intent(context, Tutorial.class);
                resultCode = Code.TUTORIAL;
            } else if (drillTypeName.equalsIgnoreCase("Phonic")) {
                if (!mPhonicsStarted) {
                    intent = new Intent(context, PhonicsLink.class);
                    resultCode = Code.DRILL_SPLASH;
                    mPhonicsStarted = true;
                } else {
                    intent = getPhonicsDrill(unitId, drillId, languageId, unitSection.getSectionSubId());
                    resultCode = Code.RUN_DRILL;
                }
            } else if (drillTypeName.equalsIgnoreCase("Word")) {
                if (!mWordsStarted) {
                    intent = new Intent(context, WordsLink.class);
                    resultCode = Code.DRILL_SPLASH;
                    mWordsStarted = true;
                } else {
                    intent = getWordDrill(unitId, drillId, languageId, unitSectionDrill.getDrillSubId());
                    resultCode = Code.RUN_DRILL;
                }
            } else if (drillTypeName.equalsIgnoreCase("Book")) {
                if (!mBooksStarted) {
                    intent = new Intent(context, StoryLink.class);
                    resultCode = Code.DRILL_SPLASH;
                    mBooksStarted = true;
                } else {
                    intent = new Intent(context, StoryActivity.class);
                    resultCode = Code.RUN_DRILL;
                }
            } else if (drillTypeName.equalsIgnoreCase("Math")) {
                if (!mMathsStarted) {
                    intent = new Intent(context, MathsLink.class);
                    resultCode = Code.DRILL_SPLASH;
                    mMathsStarted = true;
                } else {
                    intent = getMathDrill(unitId, drillId, languageId, unitSectionDrill.getDrillSubId());
                    resultCode = Code.RUN_DRILL;
                }
            }
            objectArray[0] = intent;
            objectArray[1] = resultCode;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Intent fetch(int unitId, int drillId, int languageId, int subId) {
        // fetch drill to return according to type
        // Type is based on generic, numeric drill value
        try {
            // Phonics drill
            if (drillId >= Globals.PHONICS_STARTING_ID && drillId < Globals.WORDS_STARTING_ID) {
                return getPhonicsDrill(unitId, drillId, languageId, subId);
            // Word Drill
            } else if (drillId < Globals.STORY_STARTING_ID) {
                return getWordDrill(unitId, drillId, languageId, subId);
                // Story Drill
            } else if (drillId < Globals.MATHS_STARTING_ID) {
                return getStoryDrill(unitId, drillId, languageId, subId);
            // Math Drill
            } else {
                return getMathDrill(unitId, drillId, languageId, subId);
            }
        } catch (Exception ex) {
            System.err.println("DrillFetcher.fetch." + ex.getMessage());
        }
        return null;
    }

    private Intent getPhonicsDrill(int unitId, int drillId, int languageId, int subId) throws Exception {
        Intent intent;
        switch (drillId) {
            case 1: {
                intent =  new Intent(context, SoundDrillOneActivity.class);
                break;
            }
            case 2: {
                intent = new Intent(context, SoundDrillTwoActivity.class);
                break;
            }
            case 3: {
                intent = new Intent(context, SoundDrillThreeActivity.class);
                break;
            }
            case 4: {
                intent = new Intent(context, SoundDrillFourActivity.class);
                break;
            }
            case 5: {
                intent = new Intent(context, SoundDrillFiveActivity.class);
                break;
            }
            case 6: {
                intent = new Intent(context, SoundDrillSixActivity.class);
                break;
            }
            case 7: {
                intent = new Intent(context, SoundDrillSevenActivity.class);
                break;
            }
            case 8: {
                intent = new Intent(context, SoundDrillEightActivity.class);
                break;
            }
            case 9: {
                intent = new Intent(context, SoundDrillNineActivity.class);
                break;
            }
            default: {
                throw new Exception("Drill #" + drillId + " is not catalogued as a Phonics Drill.");
            }
        }
        return intent;
    }

    private Intent getWordDrill(int unitId, int drillId, int languageId, int subId) throws Exception {
        Intent intent = null;
        try {
            if (dbOpen()) {
                // Get unit u
                Unit u = UnitHelper.getUnitInfo(dbHelper.getReadableDatabase(), unitId);

                // Get letterId using unit u
                int letterId = LetterSequenceHelper.getLetterID(dbHelper.getReadableDatabase(), languageId, unitId, subId);

                switch (drillId) {
                    case 10: {
                        int wordType = 2; // drill 10 only uses sight words, which is WordType 2
                        int rightLimit = 5; // limit the words to 4 for this drill
                        //This will get 3 random words based on the specific unit ID

                        ArrayList<Integer> drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightLimit);
                        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

                        // Fetch D1
                        intent = wordDrills.D1(context, dbHelper, unitId, drillId, languageId,
                                WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(0)),
                                WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(1)),
                                WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(2)),
                                WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(3)),
                                WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(4)),
                                drillFlowWord.getDrillSound1(),
                                drillFlowWord.getDrillSound2()
                        );
                        break;
                    }
                    case 11: {
                        int wordType = 2; // drill 11 only uses sight words, which is WordType 2
                        int rightLimit = 5; // limit the words to 4 for this drill
                        //This will get 3 random words based on the specific unit ID

                        List<Integer> drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightLimit);
                        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                        List<Word> wordList = new ArrayList<>();
                        for (int i = 0; i < drillWordIDs.size(); i++) {
                            Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(i));
                            wordList.add(word);
                        }

                        // Fetch D2
                        intent = wordDrills.D2(context, dbHelper, unitId, drillId, languageId,
                                wordList,
                                drillFlowWord.getDrillSound1(),
                                drillFlowWord.getDrillSound2()
                        );
                        break;
                    }
                    case 12: {
                        int wordType = 2; // drill 12 only uses sight words, which is WordType 2
                        int rightLimit = 4; // number of correct words
                        int wrongLimit = 8; // number of incorrect words
                        int numberLimit = 6; // Limit numbers to 8
                        int boyGirl = 2; // numbers (a.k.a number of correct words) spoken in english by a girl from g_0 to g_6

                        if (languageId == Languages.SWAHILI) {
                            boyGirl = 1; // numbers spoken by a guy/boy instead. Swahili doesn't have a girl version for this
                        }

                        ArrayList<Integer> numeralIds = NumeralHelper.getNumeralsBelowLimitFromZero(dbHelper.getReadableDatabase(), languageId, numberLimit, boyGirl);
                        ArrayList<Integer> rightDrillWordIds = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightLimit);
                        ArrayList<Integer> wrongDrillWordIds = DrillWordHelper.getWrongDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, wrongLimit);
                        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

                        for (int i = 0; i < numeralIds.size(); i++) {
                            System.out.println("DrillFetcher.getWordDrill() > case (" + drillId + "): " + "Using Numeral id (" + numeralIds.get(i) + "), index (" + i + ")");
                        }

                        // Populate right drill words
                        ArrayList<Word> rightDrillWords = new ArrayList<>();
                        for (int i = 0; i < rightDrillWordIds.size(); i++) {
                            rightDrillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIds.get(i)));
                        }

                        // Populate wrong drill words
                        ArrayList<Word> wrongDrillWords = new ArrayList<>();
                        for (int i = 0; i < wrongDrillWordIds.size(); i++) {
                            wrongDrillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), wrongDrillWordIds.get(i)));
                        }

                        // Populate drill sounds
                        ArrayList<String> drillSounds = new ArrayList<>();
                        drillSounds.add(drillFlowWord.getDrillSound1());
                        drillSounds.add(drillFlowWord.getDrillSound2());
                        drillSounds.add(drillFlowWord.getDrillSound3());
                        drillSounds.add(drillFlowWord.getDrillSound4());

                        // Populate numerals (for countdown)
                        ArrayList<Numerals> numerals = new ArrayList<>();
                        for (int i = 0; i < numeralIds.size(); i++) {
                            numerals.add(NumeralHelper.getNumeralById(dbHelper.getReadableDatabase(), numeralIds.get(i)));
                        }

                        // Fetch D3
                        intent = wordDrills.D3(context, dbHelper, unitId, drillId, languageId,
                                rightDrillWords,
                                wrongDrillWords,
                                drillSounds,
                                numerals
                        );
                        break;
                    }
                    case 13: {
                        int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                        int limit = 3; // 5 words for this drill

                        //This will get 5 random words based on the specific unit ID

                        List<Integer> rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
                        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                        Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                        List<Word> words = new ArrayList<>();
                        for (int i = 0; i < rightDrillWordIDs.size(); i++) {
                            words.add(WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i)));
                        }

                        System.out.println("DrillFetcher.getWordDrill.case 13 > Debug: languageId " +
                                languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                drillId + ", wordType " + wordType + ", limit " + limit);

                        // Fetch D4
                        intent = wordDrills.D4(context, dbHelper, unitId, drillId, languageId,
                                letter,
                                words,
                                drillFlowWord.getDrillSound1(),
                                drillFlowWord.getDrillSound2()
                        );
                        break;
                    }
                    case 14: {
                        int wordType = 2; // drill 1 only uses phonic words, which is WordType 1
                        int limit = 3; // 5 words for this drill

                        System.out.println("DrillFetcher.getWordDrill.case 14 > Debug: languageId " +
                                languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                drillId + ", wordType " + wordType + ", limit " + limit);

                        //This will get 5 random words based on the specific unit ID
                        ArrayList<Integer> rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
                        DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                        Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                        // Extract drill words
                        ArrayList<Word> drillWords = new ArrayList<>();
                        for (int i = 0; i < rightDrillWordIDs.size(); i++) {
                            drillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i)));
                        }

                        // Fetch D5
                        intent = wordDrills.D5(context, dbHelper, unitId, drillId, languageId,
                                letter,
                                drillWords,
                                drillFlowWord.getDrillSound1(),
                                drillFlowWord.getDrillSound2(),
                                drillFlowWord.getDrillSound3()
                        );
                        break;
                    }
                    case 15: {
                        // Fetch D6
                        intent = wordDrills.D6(context, dbHelper, unitId, drillId, languageId);
                        break;
                    }
                    default: {
                        throw new Exception("Drill #" + drillId + " is not catalogued as a Word Drill.");
                    }
                }
            }
            dbClose();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("getWordDrill: " + ex.getMessage());
        }
        return intent;
    }

    private Intent getStoryDrill(int unitId, int drillId, int languageId, int subId) throws Exception {
        Intent intent = null;
        try {
            if (dbOpen()) {
                switch (drillId) {
                    case 16: {
                        intent = storyDrills.D1(context, dbHelper, unitId, drillId, languageId);
                        break;
                    }
                    default: {
                        throw new Exception("Drill #" + drillId + " is not catalogued as a Story Drill.");
                    }
                }
            }
            dbClose();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("getStoryDrill." + ex.getMessage());
        }
        return intent;
    }

    private Intent getMathDrill(int unitId, int drillId, int languageId, int subId) throws Exception {
        Intent intent = null;

        try {
            if (dbOpen()) {

                // Get math drillId, as drillIds start from 1 to 7 with current solution
                // Ensures that drillId starts at one (if all values are correct in the first place ...)
                int mathDrillId = (drillId - Globals.MATHS_STARTING_ID) + 1;

                // Init other variables used with defaults
                // Override existing sub id values (changes for math drills)
                subId = 0; // runs the subIDs for the drillflow table
                int limit = 0; // Limits numbers to X
                int boyGirl = 1;

                switch (mathDrillId) {
                    case 1: {
                        intent = new Intent(context, MathsDrillOneActivity.class);
                        break;
                    }
                    case 2: {
                        limit = 3;
                        subId = 0;
                        if (languageId == Languages.ENGLISH) {
                            boyGirl = 2;
                        }

                        System.out.println("DrillFetcher.getMathDrill.case 2 > Debug: languageId " +
                                languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                drillId + ", boyGirl " + boyGirl + ", limit " + limit);

                        // Fetch D2
                        intent = mathDrills.D2(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        break;
                    }
                    case 3: {
                        limit = 10;
                        subId = 0;

                        System.out.println("DrillFetcher.getMathDrill.case 3 > Debug: languageId " +
                                languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                drillId + ", boyGirl " + boyGirl + ", limit " + limit);

                        // Fetch D3
                        intent = mathDrills.D3(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        break;
                    }
                    case 4: {
                        limit = 5;
                        subId = 0;
                        if (languageId == Languages.ENGLISH) {
                            boyGirl = 2;
                        }

                        System.out.println("DrillFetcher.getMathDrill.case 2 > Debug: languageId " +
                                languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                drillId + ", boyGirl " + boyGirl + ", limit " + limit);

                        // Fetch D4
                        intent = mathDrills.D4(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        break;
                    }
                    case 5: {
                        if (unitId < 10) {
                            limit = 10;
                            subId = 0;

                            System.out.println("DrillFetcher.getMathDrill.case 5 > Debug: languageId " +
                                    languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                                    drillId + ", boyGirl " + boyGirl + ", limit " + limit);

                            // Fetch D5A
                            intent = mathDrills.D5A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else {
                            limit = 20;
                            subId = 1;
                            // Fetch D5B
                            intent = mathDrills.D5B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        }
                        break;
                    }
                    case 6: {
                        if (languageId == Languages.ENGLISH) {
                            boyGirl = 2;
                        }
                        if (unitId < 6) {
                            subId = 0;
                            // Fetch D6A
                            intent = mathDrills.D6A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else if (unitId < 10) {
                            subId = 1;
                            // Fetch D6B
                            intent = mathDrills.D6B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else if (unitId == 10) {
                            subId = 2;
                            // Fetch D6C
                            intent = mathDrills.D6C(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else if (unitId < 16) {
                            limit = 3;
                            subId = 3;
                            // Fetch D6D
                            intent = mathDrills.D6D(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else if (unitId >= 16) {
                            limit = 5;
                            subId = 4;
                            // Fetch D6E
                            intent = mathDrills.D6E(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else {
                            throw new Exception("getMathDrill: Math drill #" + drillId + " has an invalid unitId (" + unitId + ")");
                        }
                        break;
                    }
                    case 7: {
                        if (unitId <= 10) {
                            subId = 0;
                            // Fetch D7A
                            intent = mathDrills.D7A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        } else {
                            subId = 1;
                            // Fetch D7B
                            intent = mathDrills.D7B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                        }
                        break;
                    }
                    default: {
                        throw new Exception("getMathDrill: Math Drill #" + drillId + " is not catalogued as a Math Drill.");
                    }

                }
            }
            dbClose();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("getMathDrill." + ex.getMessage());
        }
        return intent;
    }
}
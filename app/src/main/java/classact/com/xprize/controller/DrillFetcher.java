package classact.com.xprize.controller;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.catalogue.MathDrills;
import classact.com.xprize.controller.catalogue.PhonicsDrills;
import classact.com.xprize.controller.catalogue.StoryDrills;
import classact.com.xprize.controller.catalogue.WordDrills;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.LetterSequenceHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.DrillWords;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.locale.Languages;

public class DrillFetcher {

    public static Intent fetch(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int subId) {
        Intent intent = null;

        // fetch drill to return according to type
        // Type is based on generic, numeric drill value
        try {
            // Phonics drill
            if (drillId >= Globals.PHONICS_STARTING_ID && drillId < Globals.WORDS_STARTING_ID) {
                return getPhonicsDrill(context, dbHelper, unitId, drillId, languageId, subId);
            // Word Drill
            } else if (drillId < Globals.STORY_STARTING_ID) {
                return getWordDrill(context, dbHelper, unitId, drillId, languageId, subId);
                // Story Drill
            } else if (drillId < Globals.MATHS_STARTING_ID) {
                return getStoryDrill(context, dbHelper, unitId, drillId, languageId, subId);
            // Math Drill
            } else {
                return getMathDrill(context, dbHelper, unitId, drillId, languageId, subId);
            }
        } catch (SQLiteException sqlex) {
            System.err.println("DrillFetcher.fetch." + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("DrillFetcher.fetch." + ex.getMessage());
        }

        return intent;
    }

    private static Intent getPhonicsDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int subId) throws SQLiteException, Exception {
        Intent intent = null;
        try {

            // Get unit u
            Unit u = UnitHelper.getUnitInfo(dbHelper.getReadableDatabase(), unitId);

            // Get letterId using unit u
            int letterId = LetterSequenceHelper.getLetterID(dbHelper.getReadableDatabase(), languageId, unitId, subId);
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

            // Debug
            System.out.println("DrillFetcher.getPhonicsDrill > Debug: Letter :: " +
                    letter.getLetterName() + " :: for (" + languageId + ", " + unitId + ", " + subId + ", " + drillId + ") selected");

            switch (drillId) {
                case 1: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // limit the words to 3 for this drill

                    // Fetch D1
                    intent = PhonicsDrills.D1(context, dbHelper, unitId, drillId, languageId, subId, letterId, limit, wordType);
                    break;
                }
                case 2: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 5; // 5 words for this drill

                    // Fetch D2
                    intent = PhonicsDrills.D2(context, dbHelper, unitId, drillId, languageId, subId, letterId, limit, wordType);
                    break;
                }
                case 3: {
                    int limit = 5; // 5 cupcakes selectable

                    // Fetch D3
                    intent = PhonicsDrills.D3(context, dbHelper, unitId, drillId, languageId, letterId, limit);
                    break;
                }
                case 4: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 4; // limit the words to 4 for this drill
                    int wrongLimit = 2;

                    // Fetch D4
                    intent = PhonicsDrills.D4(context, dbHelper, unitId, drillId, languageId, subId, letterId, rightLimit, wrongLimit, wordType);
                    break;
                }
                case 5: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 6; // 3 right words, 3 other right words
                    int wrongLimit = 9;

                    // Fetch D5
                    intent = PhonicsDrills.D5(context, dbHelper, unitId, drillId, languageId, subId, letterId, rightLimit, wrongLimit, wordType);
                    break;
                }
                case 6: {
                    DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    // Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                    // Fetch D6
                    intent = PhonicsDrills.D6(context, dbHelper, unitId, drillId, languageId,
                            letter,
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3(),
                            drillFlowWord.getDrillSound4(),
                            drillFlowWord.getDrillSound5()
                    );
                    break;
                }
                case 7: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 6; // 5 words for this drill
                    int wrongWordLimit = 10;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer> rightDrillWordIds = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
                    ArrayList<Integer> wrongDrillWordIds = DrillWordHelper.getWrongDrillWordsByLetter(dbHelper.getReadableDatabase(), languageId, wordType, letter.getLetterName(), wrongWordLimit);
                    DrillFlowWords drillFlowWord = drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    // Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                    ArrayList<Word> rightDrillWords = new ArrayList<>();
                    for (Integer id: rightDrillWordIds) {
                        rightDrillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), id));
                    }

                    ArrayList<Word> wrongDrillWords = new ArrayList<>();
                    for (Integer id: wrongDrillWordIds) {
                        wrongDrillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), id));
                    }


                    // Fetch D7
                    intent = PhonicsDrills.D7(context, dbHelper, unitId, drillId, languageId,
                            letter,
                            rightDrillWords,
                            wrongDrillWords,
                            drillFlowWord.getDrillSound1()
                    );
                    break;
                }
                case 8: {
                    // Fetch D8
                    intent = PhonicsDrills.D8(context, dbHelper, unitId, drillId, languageId, letterId);
                    break;
                }
                case 9: {
                    DrillFlowWords drillFlowWord;
                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    // Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                    // Fetch D9
                    intent = PhonicsDrills.D9(context, dbHelper, unitId, drillId, languageId,
                            letter,
                            drillFlowWord.getDrillSound1(),
                            drillFlowWord.getDrillSound2(),
                            drillFlowWord.getDrillSound3()
                    );
                    break;
                }
                default: {
                    throw new Exception("Drill #" + drillId + " is not catalogued as a Phonics Drill.");
                }
            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getPhonicsDrill > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("getPhonicsDrill > Exception: " + ex.getMessage());
        }
        return intent;
    }

    private static Intent getWordDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int subId) throws SQLiteException, Exception {
        Intent intent;

        try {

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
                    intent = WordDrills.D1(context, dbHelper, unitId, drillId, languageId,
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

                    ArrayList<Integer> drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightLimit);
                    DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

                    // Fetch D2
                    intent = WordDrills.D2(context, dbHelper, unitId, drillId, languageId,
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
                    intent = WordDrills.D3(context, dbHelper, unitId, drillId, languageId,
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

                    ArrayList<Integer>  rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
                    DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                    System.out.println("DrillFetcher.getWordDrill.case 13 > Debug: languageId " +
                            languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                            drillId + ", wordType " + wordType + ", limit " + limit);

                    // Fetch D4
                    intent = WordDrills.D4(context, dbHelper, unitId, drillId, languageId,
                            letter,
                            WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(0)),
                            WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(1)),
                            WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(2)),
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
                    ArrayList<Integer>  rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
                    DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

                    // Extract drill words
                    ArrayList<Word> drillWords = new ArrayList<>();
                    for (int i = 0; i < rightDrillWordIDs.size(); i++) {
                        drillWords.add(WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i)));
                    }

                    // Fetch D5
                    intent = WordDrills.D5(context, dbHelper, unitId, drillId, languageId,
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
                    intent = WordDrills.D6(context, dbHelper, unitId, drillId, languageId);
                    break;
                }
                default: {
                    throw new Exception("Drill #" + drillId + " is not catalogued as a Word Drill.");
                }
            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getWordDrill: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("getWordDrill: " + ex.getMessage());
        }
        return intent;
    }

    private static Intent getStoryDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int subId) throws SQLiteException, Exception {
        Intent intent = null;
        try {
            switch (drillId) {
                case 16: {
                    intent = StoryDrills.D1(context, dbHelper, unitId, drillId, languageId);
                    break;
                }
                default: {
                    throw new Exception("Drill #" + drillId + " is not catalogued as a Story Drill.");
                }
            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getStoryDrill." + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("getStoryDrill." + ex.getMessage());
        }
        return intent;
    }

    private static Intent getMathDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId, int subId) throws SQLiteException, Exception {
        Intent intent = null;

        // Get math drillId, as drillIds start from 1 to 7 with current solution
        // Ensures that drillId starts at one (if all values are correct in the first place ...)
        int mathDrillId = (drillId - Globals.MATHS_STARTING_ID) + 1;

        // Init other variables used with defaults
        // Override existing sub id values (changes for math drills)
        subId = 0; // runs the subIDs for the drillflow table
        int limit = 0; // Limits numbers to X
        int boyGirl = 1;

        try {
            switch (mathDrillId) {
                case 1: {
                    limit = 5;
                    subId = 0;
                    if (languageId == Languages.ENGLISH) {
                        boyGirl = 2;
                    }
                    // Fetch D1
                    intent = MathDrills.D1(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
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
                    intent = MathDrills.D2(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    break;
                }
                case 3: {
                    limit = 10;
                    subId = 0;

                    System.out.println("DrillFetcher.getMathDrill.case 3 > Debug: languageId " +
                            languageId + ", unitId " + unitId + ", subId " + subId + ", drillId " +
                            drillId + ", boyGirl " + boyGirl + ", limit " + limit);

                    // Fetch D3
                    intent = MathDrills.D3(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
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
                    intent = MathDrills.D4(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
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
                        intent = MathDrills.D5A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else {
                        limit = 20;
                        subId = 1;
                        // Fetch D5B
                        intent = MathDrills.D5B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
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
                        intent = MathDrills.D6A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId < 10) {
                        subId = 1;
                        // Fetch D6B
                        intent = MathDrills.D6B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId == 10) {
                        subId = 2;
                        // Fetch D6C
                        intent = MathDrills.D6C(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId < 16) {
                        limit = 3;
                        subId = 3;
                        // Fetch D6D
                        intent = MathDrills.D6D(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId >= 16) {
                        limit = 5;
                        subId = 4;
                        // Fetch D6E
                        intent = MathDrills.D6E(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else {
                        throw new Exception("getMathDrill: Math drill #" + drillId + " has an invalid unitId (" + unitId + ")");
                    }
                    break;
                }
                case 7: {
                    if (unitId < 10) {
                        subId = 0;
                        // Fetch D7A
                        intent = MathDrills.D7A(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else {
                        subId = 1;
                        // Fetch D7B
                        intent = MathDrills.D7B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    }
                    break;
                }
                default: {
                    throw new Exception("getMathDrill: Math Drill #" + drillId + " is not catalogued as a Math Drill.");
                }

            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getMathDrill." + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("getMathDrill." + ex.getMessage());
        }
        return intent;
    }
}
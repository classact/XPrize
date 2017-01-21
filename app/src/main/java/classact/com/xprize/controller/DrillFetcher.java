package classact.com.xprize.controller;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.common.Globals;
import classact.com.xprize.controller.catalogue.MathDrills;
import classact.com.xprize.controller.catalogue.StoryDrills;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.locale.Languages;

public class DrillFetcher {

    public static Intent fetch(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) {
        Intent intent = null;

        // fetch drill to return according to type
        // Type is based on generic, numeric drill value
        try {
            // Phonics drill
            if (drillId >= Globals.PHONICS_STARTING_ID && drillId < Globals.WORDS_STARTING_ID) {
                return getPhonicsDrill(context, dbHelper, unitId, drillId, languageId);
            // Word Drill
            } else if (drillId < Globals.STORY_STARTING_ID) {
                return getWordDrill(context, dbHelper, unitId, drillId, languageId);
            // Story Drill
            } else if (drillId < Globals.MATHS_STARTING_ID) {
                return getStoryDrill(context, dbHelper, unitId, drillId, languageId);
            // Math Drill
            } else {
                return getMathDrill(context, dbHelper, unitId, drillId, languageId);
            }
        } catch (SQLiteException sqlex) {
            System.err.println("DrillFetcher.fetch." + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("DrillFetcher.fetch." + ex.getMessage());
        }

        return intent;
    }

    private static Intent getPhonicsDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {
        Intent intent = null;
        try {
            switch (drillId) {
                case 1: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 3; // limit the words to 3 for this drill
                    break;
                }
                case 2: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 5; // 5 words for this drill
                    break;
                }
                case 3: {
                    int limit = 5; // 5 repeats for this drill so we choose 5 incorrect letters
                    break;
                }
                case 4: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 4; // limit the words to 4 for this drill
                    int wrongLimit = 2;
                    break;
                }
                case 5: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int rightLimit = 3; // 5 words for this drill
                    int wrongLimit = 9;
                    break;
                }
                case 6: {
                    DrillFlowWords drillFlowWord;
                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

                    break;
                }
                case 7: {
                    int wordType = 1; // drill 1 only uses phonic words, which is WordType 1
                    int limit = 6; // 5 words for this drill
                    int wrongWordLimit = 10;
                    DrillFlowWords drillFlowWord;

                    //This will get 5 random words based on the specific unit ID
                    ArrayList<Integer> rightDrillWordIDs = new ArrayList();
                    rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);

                    ArrayList<Integer>  wrongDrillWordIDs = new ArrayList();
                    wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, wrongWordLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

                    break;
                }
                case 8: {
                    break;
                }
                case 9: {
                    break;
                }
                default: {
                    throw new Exception("Drill #" + drillId + " is not catalogued as a Phonics Drill.");
                }
            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getPhonicsDrill: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("getPhonicsDrill: " + ex.getMessage());
        }
        return intent;
    }

    private static Intent getWordDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {
        Intent intent = null;
        try {
            switch (drillId) {
                case 10: {
                    int wordType = 2; // drill 10 only uses sight words, which is WordType 2
                    int rightLimit = 5; // limit the words to 4 for this drill
                    DrillFlowWords drillFlowWord;

                    //This will get 3 random words based on the specific unit ID

                    ArrayList<Integer> drillWordIDs = new ArrayList();
                    drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, rightLimit);

                    drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
                    break;
                }
                case 11: {
                    break;
                }
                case 12: {
                    break;
                }
                case 13: {
                    break;
                }
                case 14: {
                    break;
                }
                case 15: {
                    break;
                }
                default: {
                    throw new Exception("Drill #" + drillId + " is not catalogued as a Word Drill.");
                }
            }
        } catch (SQLiteException sqlex) {
            throw new SQLiteException("getWordDrill: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("getWordDrill: " + ex.getMessage());
        }
        return intent;
    }

    private static Intent getStoryDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {
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

    private static Intent getMathDrill(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {
        Intent intent = null;

        // Get math drillId, as drillIds start from 1 to 7 with current solution
        // Ensures that drillId starts at one (if all values are correct in the first place ...)
        int mathDrillId = (drillId - Globals.MATHS_STARTING_ID) + 1;

        // Init other variables used with defaults
        int limit = 0; // Limits numbers to X
        int subId = 0; // this runs the subIDs for the drillflow table
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
                    // Fetch D2
                    intent = MathDrills.D2(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    break;
                }
                case 3: {
                    limit = 10;
                    subId = 0;
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
                    // Fetch D4
                    intent = MathDrills.D4(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    break;
                }
                case 5: {
                    if (unitId < 9) {
                        limit = 10;
                        subId = 0;
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
                    } else if ((unitId >= 6) && (unitId < 10)) {
                        subId = 1;
                        // Fetch D6B
                        intent = MathDrills.D6B(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId == 10) {
                        subId = 2;
                        // Fetch D6C
                        intent = MathDrills.D6C(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if ((unitId > 10) && (unitId < 16)) {
                        limit = 3;
                        subId = 3;
                        // Fetch D6D
                        intent = MathDrills.D6D(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else if (unitId > 15) {
                        limit = 5;
                        subId = 4;
                        // Fetch D6E
                        intent = MathDrills.D6E(context, dbHelper, unitId, drillId, languageId, mathDrillId, subId, limit, boyGirl);
                    } else {
                        throw new Exception("getMathDrill: Math drill #" + drillId + " has an invalid unitId (" + unitId + ")");
                    }
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
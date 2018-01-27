package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.activity.drill.sound.SoundDrillEightActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFiveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillNineActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSixActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThreeActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwoActivity;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.control.RightWrongPair;
import classact.com.xprize.control.RightWrongWordSet;
import classact.com.xprize.control.SoundDrillFiveObject;
import classact.com.xprize.control.SoundDrillJsonBuilder;
import classact.com.xprize.control.SoundDrillThreeObject;
import classact.com.xprize.control.SpelledWord;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.utils.FisherYates;

public class PhonicsDrills {

    private LetterHelper letterHelper;

    @Inject
    public PhonicsDrills(LetterHelper letterHelper) {
        this.letterHelper = letterHelper;
    }

    public Intent D6(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, String drillSound1, String drillSound2,
                            String drillSound3, String drillSound4, String drillSound5
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            String drillData = SoundDrillJsonBuilder.getSoundDrillSixJson(context, letter.getLetterPictureLowerCaseBlackURI(),
                    letter.getLetterPictureUpperCaseBlackURI(),
                    letter.getLetterSoundURI(), drillSound1, drillSound2, drillSound3, drillSound4, drillSound5);
            intent = new Intent(context, SoundDrillSixActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D6: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D6: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D8(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int letterId
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            String drillData = SoundDrillJsonBuilder.getSoundDrillEightJson(context,
                    letter.getLetterLowerPath(),
                    drillFlowWords.getDrillSound1(),
                    drillFlowWords.getDrillSound4(),
                    drillFlowWords.getDrillSound2(),
                    drillFlowWords.getDrillSound3(),
                    letter.getLetterPictureLowerCaseDotsURI(),
                    letter.getLetterSoundURI(),
                    letter.getLetterUpperPath(),
                    letter.getLetterPictureUpperCaseDotsURI());
            intent = new Intent(context, SoundDrillEightActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D8: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D8: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D9(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, String drillSound1, String drillSound2, String drillSound3
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            String drillData = SoundDrillJsonBuilder.getSoundDrillNineJson(context, letter.getLetterSoundURI(),
                    drillSound1,
                    drillSound2,
                    drillSound3);
            intent = new Intent(context, SoundDrillNineActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D9 > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D9 > Exception: " + ex.getMessage());
        }
        return intent;
    }

    private RightWrongWordSet getRightWrongWordSet(Word rightWord, ArrayList<Word> wrongWords) throws Exception {

        try {
            // Get shuffled indexes
            int[] shuffledIndexes = FisherYates.shuffle(wrongWords.size() + 1); // add one because of the 1 'right word'

            // Create list of right and wrong words (use Draggable Images object)
            ArrayList<DraggableImage<Word>> rightAndWrongWords = new ArrayList<>();

            // Add right words to list
            rightAndWrongWords.add(new DraggableImage<>(0, 1, rightWord));

            // Add wrong words to list
            for (int i = 0; i < wrongWords.size(); i++) {
                rightAndWrongWords.add(new DraggableImage<>(0, 0, wrongWords.get(i)));
            }

            // Now add the right and wrong words to a shuffled array
            ArrayList<DraggableImage<Word>> shuffledRightAndWrongWords = new ArrayList<>();
            for (int i = 0; i < shuffledIndexes.length; i++) {
                shuffledRightAndWrongWords.add(rightAndWrongWords.get(shuffledIndexes[i]));
            }

            // Return a newly created set of right and wrong words
            return new RightWrongWordSet(rightWord, shuffledRightAndWrongWords);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("getRightWrongWordSet > Exception: " + ex.getMessage());
        }
    }
}
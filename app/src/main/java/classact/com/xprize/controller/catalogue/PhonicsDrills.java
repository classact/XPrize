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

    public Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int limit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent;

        try {
            DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

            int letterType = letter.getIsLetter();

            ObjectAndSound<String> letterObject = new ObjectAndSound<>(
                    letter.getLetterPictureLowerCaseBlackURI(),
                    letter.getLetterSoundURI(),
                    letter.getPhonicSoundURI());
            List<ObjectAndSound<String>> drillObjects = new ArrayList<>();
            ArrayList<Integer> drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);

            for (int i=0; i < drillWordIDs.size(); i++ ){
                Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(i));
                drillObjects.add(new ObjectAndSound<String>(word.getImagePictureURI(), word.getWordSoundURI(), word.getWordSlowSoundURI()));
            }
            String drillData = SoundDrillJsonBuilder.getSoundDrillOneJson(context,
                    letterObject,
                    letterType,
                    drillFlowWord.getDrillSound1(),
                    drillFlowWord.getDrillSound2(),
                    drillFlowWord.getDrillSound3(),
                    drillObjects);
            intent = new Intent(context, SoundDrillOneActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D1: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D1: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D2(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int limit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent;

        try {
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
            ArrayList<Integer> rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
            ArrayList<Integer> wrongDrillWordIDs = DrillWordHelper.getWrongDrillWordsByLetter(dbHelper.getReadableDatabase(), languageId, wordType, letter.getLetterName(), limit);
            List<RightWrongPair> pairs = new ArrayList<>();
            for (int i=0; i < rightDrillWordIDs.size(); i++ ){ // we have the same amount of right and wrong words. So just loop over right words.
                Word rightWord = WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));
                Word wrongWord = WordHelper.getWord(dbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));

                ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
                ObjectAndSound<String> wrongObject = new ObjectAndSound<>(wrongWord.getImagePictureURI(), wrongWord.getWordSoundURI(), "");
                RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
                pairs.add(pair);
            }
            String drillData = SoundDrillJsonBuilder.getSoundDrillTwoJson(context,
                    letter.getPhonicSoundURI(),
                    drillFlowWords.getDrillSound1(),
                    drillFlowWords.getDrillSound2(),
                    pairs
            );
            intent = new Intent(context, SoundDrillTwoActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D2: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D2: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D5(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int rightlimit, int wronglimit, int wordType
    ) throws SQLiteException, Exception {

        // Debug
        System.out.println("PhonicsDrills.D5 > Debug: METHOD CALLED");

        Intent intent;

        try {
            System.out.println("===== A =====");
            ArrayList<SoundDrillFiveObject> soundDrillFiveObjects = new ArrayList<>();

            System.out.println("===== B =====");
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

            System.out.println("===== C =====");
            ArrayList<Integer> rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightlimit);
            ArrayList<Integer> wrongDrillWordIDs = DrillWordHelper.getWrongDrillWordsByLetter(dbHelper.getReadableDatabase(), languageId, wordType, letter.getLetterName(), wronglimit);

            System.out.println("===== D =====");
            System.out.println("rightDrillWordIDs.size(): " + rightDrillWordIDs.size());
            System.out.println("wrongDrillWordIDs.size(): " + wrongDrillWordIDs.size());


            int wrongWordCounter = 0;
            int maxWrongWordsPerItem = 3;
            int numberOfRightDrillWords = rightDrillWordIDs.size();
            if (numberOfRightDrillWords % 2 > 0) {
                numberOfRightDrillWords -= 1; // make it even
            }
            ArrayList<ObjectAndSound> images;

            for (int i = 0; i < numberOfRightDrillWords; i += 2) {
                Word rightWord = WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));
                images = new ArrayList<>();

                // Drill object (The reference object for the drill item)
                ObjectAndSound drillObject = new ObjectAndSound(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
                System.out.println("Processing Right Word: (id " + rightWord.getWordID() + ")" + rightWord.getWordName());

                drillObject.setBeginningLetterSound(letter.getPhonicSoundURI());

                // Get another right word
                Word anotherRightWord = WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i+1));
                System.out.println("Processing Another Right Word: (id " + anotherRightWord.getWordID() + ")" + anotherRightWord.getWordName());

                ObjectAndSound objectAndSound = new ObjectAndSound(anotherRightWord.getImagePictureURI(), anotherRightWord.getWordSoundURI(), "");
                objectAndSound.setCustomData("1");
                images.add(objectAndSound);

                // The wrong words/images
                for (int j = wrongWordCounter; j < maxWrongWordsPerItem; j++) {
                    Word wrongWord = WordHelper.getWord(dbHelper.getReadableDatabase(), wrongDrillWordIDs.get(j));

                    System.out.println("Processing Wrong Word: " + wrongWord.getWordName());

                    objectAndSound = new ObjectAndSound(wrongWord.getImagePictureURI(), wrongWord.getWordSoundURI(), "");
                    objectAndSound.setCustomData("0");
                    images.add(objectAndSound);
                }
                wrongWordCounter += 3;
                maxWrongWordsPerItem += 3;

                SoundDrillFiveObject obj = new SoundDrillFiveObject(drillObject, images);
                soundDrillFiveObjects.add(obj);

                System.out.println("===== E(" + i + ") =====");
                System.out.println("images.size(): " + images.size());
                System.out.println("soundDrillFiveObjects.size(): " + soundDrillFiveObjects.size());
            }

            // Debug
            System.out.println("PhonicsDrills.D5 > Debug: Length of sound drill five objects (" + soundDrillFiveObjects.size() + ")");

            String drillData = SoundDrillJsonBuilder.getSoundDrillFiveJson(context, drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), soundDrillFiveObjects);
            intent = new Intent(context, SoundDrillFiveActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D5: " + ex.getMessage());
        }
        return intent;
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
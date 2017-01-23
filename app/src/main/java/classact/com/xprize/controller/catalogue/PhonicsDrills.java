package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.activity.drill.sound.SoundDrillEightActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFiveActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillNineActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillOneActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillSixActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThreeActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwoActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.control.RightWrongPair;
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

public class PhonicsDrills {

    public static Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int limit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

            ObjectAndSound<String> letterObject = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());
            ArrayList<ObjectAndSound<String>> drillObjects = new ArrayList<ObjectAndSound<String>>();
            ArrayList<Integer> drillWordIDs = new ArrayList();
            drillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);

            for (int i=0; i < drillWordIDs.size(); i++ ){
                Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), drillWordIDs.get(i));
                drillObjects.add(new ObjectAndSound<String>(word.getImagePictureURI(), word.getWordSoundURI(), word.getWordSlowSoundURI()));
            }
            String drillData = SoundDrillJsonBuilder.getSoundDrillOneJson(context, letterObject, drillFlowWord.getDrillSound1(), drillFlowWord.getDrillSound2(), drillFlowWord.getDrillSound3(), drillObjects);
            intent = new Intent(context, SoundDrillOneActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D1: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D1: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D2(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int limit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
            ArrayList<Integer> rightDrillWordIDs = new ArrayList();
            rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
            ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
            wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, limit);
            ArrayList<RightWrongPair> pairs = new ArrayList<RightWrongPair>();
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
            throw new SQLiteException("D2: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D3(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int letterId, int limit
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
            ArrayList<SoundDrillThreeObject> sets = new ArrayList<SoundDrillThreeObject>();
            ArrayList<Integer> wrongLetters = LetterHelper.getWrongLetters(dbHelper.getReadableDatabase(), languageId, letterId, limit);
            ObjectAndSound<String> objectAndSound = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), letter.getLetterSoundURI(), letter.getPhonicSoundURI());

            for (int i=0; i < wrongLetters.size(); i++ ) { //
                Letter wrongLetter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, wrongLetters.get(i));
                ObjectAndSound<String> rightObject = new ObjectAndSound<>(letter.getLetterPictureLowerCaseBlackURI(), "", "");
                ObjectAndSound<String> wrongObject = new ObjectAndSound<>(wrongLetter.getLetterPictureLowerCaseBlackURI(), "", "");
                RightWrongPair pair = new RightWrongPair(rightObject, wrongObject);
                SoundDrillThreeObject obj = new SoundDrillThreeObject(objectAndSound, pair);
                sets.add(obj);
            }

            String drillData = SoundDrillJsonBuilder.getSoundDrillThreeJson(context, drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), drillFlowWords.getDrillSound3(), sets);
            intent = new Intent(context, SoundDrillThreeActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D3: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D3: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D4(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int subId, int letterId, int rightlimit, int wronglimit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            ArrayList<DraggableImage<ObjectAndSound>> images = new ArrayList<DraggableImage<ObjectAndSound>>();
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
            ArrayList<Integer> rightDrillWordIDs = new ArrayList();
            rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, rightlimit);
            int lastPosition=0;
            for (int i=0; i < rightDrillWordIDs.size(); i++ ){
                Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));

                ObjectAndSound obj = new ObjectAndSound(word.getImagePictureURI(), word.getWordSoundURI(), "");
                DraggableImage<ObjectAndSound> image = new DraggableImage<ObjectAndSound>(i+1, 1, obj);
                images.add(image);
                lastPosition=i;
            }

            ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
            wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, subId, drillId, wordType, wronglimit);
            for (int i=0; i < wrongDrillWordIDs.size(); i++ ){
                Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));

                ObjectAndSound obj = new ObjectAndSound(word.getImagePictureURI(), word.getWordSoundURI(), "");
                DraggableImage<ObjectAndSound> image = new DraggableImage<ObjectAndSound>(i+lastPosition+1, 0, obj);
                images.add(image);
            }

            String drillData = SoundDrillJsonBuilder.getSoundDrillFourActivity(context, letter.getLetterSoundURI(), drillFlowWords.getDrillSound3(), drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), images);
            intent = new Intent(context, SoundDrillFourActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D4: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D4: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D5(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int letterId, int rightlimit, int wronglimit, int wordType
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            ArrayList<SoundDrillFiveObject> soundDrillFiveObjects = new ArrayList<SoundDrillFiveObject>();
            ArrayList<ObjectAndSound> images = new ArrayList<ObjectAndSound>();
            ArrayList<ObjectAndSound> drillObjects = new ArrayList<ObjectAndSound>();

            DrillFlowWords drillFlowWords = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);

            ArrayList<Integer> wrongDrillWordIDs = new ArrayList();
            wrongDrillWordIDs = DrillWordHelper.getWrongDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, wronglimit);

            ArrayList<Integer> rightDrillWordIDs = new ArrayList();
            rightDrillWordIDs = DrillWordHelper.getDrillWords(dbHelper.getReadableDatabase(), languageId, unitId, Globals.DEFAULT_UNIT_SUB_ID, drillId, wordType, rightlimit);

            int lastPosition=0;
            for (int i=0; i < rightDrillWordIDs.size(); i++ ){
                Word rightWord = WordHelper.getWord(dbHelper.getReadableDatabase(), rightDrillWordIDs.get(i));
                ObjectAndSound objectAndSound = new ObjectAndSound(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
                objectAndSound.setCustomData("1");
                images.add(objectAndSound);
                ObjectAndSound drillObject = new ObjectAndSound(rightWord.getImagePictureURI(), rightWord.getWordSoundURI(), "");
                drillObject.setBeginningLetterSound(letter.getLetterSoundURI());
                drillObjects.add(drillObject);

                int jloopCount=0;
                for (int j=lastPosition; i < wrongDrillWordIDs.size(); i++ ){
                    while (j < (lastPosition+3)) {
                        Word wrongWord = WordHelper.getWord(dbHelper.getReadableDatabase(), wrongDrillWordIDs.get(i));
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

            String drillData = SoundDrillJsonBuilder.getSoundDrillFiveJson(context, drillFlowWords.getDrillSound1(), drillFlowWords.getDrillSound2(), soundDrillFiveObjects);
            intent = new Intent(context, SoundDrillFiveActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D5: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D6(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
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
            throw new SQLiteException("D6: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D7(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, Word rightWord1, Word rightWord2, Word rightWord3, Word wrongWord1,
                            Word wrongWord2, Word wrongWord3, Word wrongWord4, Word wrongWord5, Word wrongWord6,
                            Word wrongWord7, Word wrongWord8, Word wrongWord9, Word wrongWord10,
                            String drillSound1
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            ArrayList<SpelledWord> words = new ArrayList<>();
            //First Word
            SpelledWord spelledWord = new SpelledWord();
            ObjectAndSound<String> rightObject = new ObjectAndSound<>(rightWord1.getImagePictureURI(), rightWord1.getWordSoundURI(), "");
            ObjectAndSound<String> word = new ObjectAndSound<>(rightWord1.getImagePictureURI(), rightWord1.getWordSoundURI(), "");
            word.setObjectSlowSound(rightWord1.getWordSlowSoundURI());
            ArrayList<String> letterSounds = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rightWord1.getWordName().length(); i++) {
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(rightWord1.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(rightWord2.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(rightWord3.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(rightWord1.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(rightWord1.getWordName().charAt(i)));
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
            String drillData = SoundDrillJsonBuilder.getSoundDrillSevenJson(context, drillSound1, words);
            intent = new Intent(context, SoundDrillSevenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D7: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D7: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D8(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            int letterId
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            Letter letter = LetterHelper.getLetter(dbHelper.getReadableDatabase(), languageId, letterId);
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
            throw new SQLiteException("D8: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D9(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
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
            throw new SQLiteException("D9: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D9: " + ex.getMessage());
        }
        return intent;
    }
}

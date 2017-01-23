package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.activity.drill.sound.SoundDrillElevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFifteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThirteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwelveActivity;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.control.Sentence;
import classact.com.xprize.control.SoundDrillJsonBuilder;
import classact.com.xprize.control.SpelledWord;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillFlowWordsHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.helper.SentenceHelper;
import classact.com.xprize.database.helper.SentenceWordsHelper;
import classact.com.xprize.database.model.DrillFlowWords;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.database.model.SentenceDB;
import classact.com.xprize.database.model.SentenceDBWords;
import classact.com.xprize.database.model.Word;

public class WordDrills {

    public static Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Word word1, Word word2, Word word3, Word word4, Word word5,
                            String drillSound1, String drillSound2
    ) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            System.out.println("Word ids: " + word1.getWordID() + ", " + word2.getWordID() + ", " + word3.getWordID() + ", " + word4.getWordID() + ", " + word5.getWordID());
            System.out.println("Word picture1: " + word1.getWordPictureURI());
            System.out.println("Word picture2: " + word2.getWordPictureURI());
            System.out.println("Word picture3: " + word3.getWordPictureURI());
            System.out.println("Word picture4: " + word4.getWordPictureURI());
            System.out.println("Word picture5: " +  word5.getWordPictureURI());
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
            String drillData = SoundDrillJsonBuilder.getSoundDrillTenJson(context,drillSound1,drillSound2,words);
            intent = new Intent(context,SoundDrillTenActivity.class);
            intent.putExtra("data",drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D1: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D1: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D2(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, Word word1, Word word2, Word word3, Word word4, Word word5,
                            String drillSound1, String drillSound2)
            throws SQLiteException, Exception {
        Intent intent = null;

        try {
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

            String drillData = SoundDrillJsonBuilder.getSoundDrillElevenJson(context, drillSound1, drillSound2, NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, 1).getSound(), words);
            intent = new Intent(context, SoundDrillElevenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D2: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D2: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D3(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, Word rightWord1, Word rightWord2, Word rightWord3, Word rightWord4, Word wrongWord1, Word wrongWord2,
                            Word wrongWord3, Word wrongWord4, Word wrongWord5, Word wrongWord6, Word wrongWord7, Word wrongWord8,
                            String drillSound1, String drillSound2, String drillSound3, String drillSound4,
                            Numerals numeral1, Numerals numeral2, Numerals numeral3, Numerals numeral4, Numerals numeral5, Numerals numeral6)
            throws SQLiteException, Exception {
        Intent intent = null;

        try {
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
            String drillData = SoundDrillJsonBuilder.getSoundDrilTwelveJson(context,
                    drillSound1, drillSound2, drillSound3, numeral1.getSound(),numeral2.getSound(),numeral3.getSound(),numeral4.getSound(),numeral5.getSound(),
                    numeral6.getSound(), drillSound4, sets);
            intent = new Intent(context, SoundDrillTwelveActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D3: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D3: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D4(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, Word word1, Word word2, Word word3, Word word4,
                            String drillSound1, String drillSound2) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            ArrayList<SpelledWord> words = new ArrayList<>();
            SpelledWord set = new SpelledWord();
            ObjectAndSound<String> word = new ObjectAndSound<>("",word1.getWordSoundURI(),"");
            word.setSpelling(word1.getWordName());
            set.setWord(word);
            ArrayList<DraggableImage<String>> items = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int i = 0; i < word1.getWordName().length(); i++) {
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word1.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word2.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word3.getWordName().charAt(i)));
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
            Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word4.getWordName().charAt(i)));
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
            String drillData =SoundDrillJsonBuilder.getSoundDrillThirteenJson(context,drillSound1,drillSound2,words);
            intent = new Intent(context,SoundDrillThirteenActivity.class);
            intent.putExtra("data",drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D4: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D4: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D5(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, Word word1, Word word2, Word word3, Word word4,
                            String drillSound1, String drillSound2, String drillSound3) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            ArrayList<SpelledWord> words = new ArrayList<>();
            SpelledWord set = new SpelledWord();
            ObjectAndSound<String> word = new ObjectAndSound<>("", word1.getWordSoundURI(), "");
            word.setSpelling(word1.getWordName());
            set.setWord(word);
            ArrayList<DraggableImage<String>> items = new ArrayList<>();

            int count = 0;
            for (int i = 0; i < word1.getWordName().length(); i++) {
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word1.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word2.getWordName().charAt(i)));
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
                Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word3.getWordName().charAt(i)));
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
            Letter thisLetter = LetterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word4.getWordName().charAt(i)));
            DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
            count = word4.getWordName().length() - word4.getWordName().substring(0, word4.getWordName().length() - 1).replaceAll(thisLetter.getLetterName(), "").length();
            item.setExtraData(String.valueOf(i+1));
            items.add(item);
        }
        set.setLettersImages(items);
        words.add(set);
        // */
            String drillData = SoundDrillJsonBuilder.getSoundDrillFourteenJson(context, drillSound1, drillSound2, drillSound3, words);
            intent = new Intent(context, SoundDrillThirteenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D5: " + ex.getMessage());
        }
        return intent;
    }

    public static Intent D6(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {
        Intent intent = null;

        try {
            ArrayList<Sentence> sentences = new ArrayList<>();

            ArrayList<Integer>  sentenceIDs = new ArrayList();
            sentenceIDs = SentenceHelper.getSentences(dbHelper.getReadableDatabase(), languageId, unitId);

            for (int i=0; i < sentenceIDs.size(); i++){
                SentenceDB sentenceFromDB = SentenceHelper.getSentence(dbHelper.getReadableDatabase(), sentenceIDs.get(i));
                Sentence sentence = new Sentence(sentenceFromDB.getWordCount(), sentenceFromDB.getSentenceSoundFile());
                ArrayList<DraggableImage<String>> words = new ArrayList<>();
                ArrayList<Integer>  sentenceWordIDs = new ArrayList();
                sentenceWordIDs = SentenceWordsHelper.getSentenceWords(dbHelper.getReadableDatabase(), sentenceIDs.get(i));
                for (int j=0; j< sentenceWordIDs.size(); j++){
                    SentenceDBWords sentenceWord = SentenceWordsHelper.getSentenceWord(dbHelper.getReadableDatabase(), sentenceWordIDs.get(j));
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
            DrillFlowWords drillFlowWord = DrillFlowWordsHelper.getDrillFlowWords(dbHelper.getReadableDatabase(), drillId, languageId);

            String drillData = SoundDrillJsonBuilder.getSoundDrillFifteenJson(context,drillFlowWord.getDrillSound1(),drillFlowWord.getDrillSound2(),sentences);
            intent = new Intent(context,SoundDrillFifteenActivity.class);
            intent.putExtra("data",drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D6: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D6: " + ex.getMessage());
        }
        return intent;
    }
}
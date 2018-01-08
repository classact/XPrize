package classact.com.xprize.controller.catalogue;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.activity.drill.sound.SoundDrillElevenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFifteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillFourteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillThirteenActivity;
import classact.com.xprize.activity.drill.sound.SoundDrillTwelveActivity;
import classact.com.xprize.control.DraggableImage;
import classact.com.xprize.control.ObjectAndSound;
import classact.com.xprize.control.RightWrongWordSet;
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
import classact.com.xprize.utils.FisherYates;

public class WordDrills {

    private LetterHelper letterHelper;

    @Inject
    public WordDrills(LetterHelper letterHelper) {
        this.letterHelper = letterHelper;
    }

    public Intent D1(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Word word1, Word word2, Word word3, Word word4, Word word5,
                            String drillSound1, String drillSound2
    ) throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D1 > Debug: MC");

        Intent intent;

        try {
            // Create words list
            ArrayList<Word> words = new ArrayList<>();
            words.add(word1);
            words.add(word2);
            words.add(word3);
            words.add(word4);
            words.add(word5);
            // Create drill data
            String drillData = SoundDrillJsonBuilder.getSoundDrillTenJson(
                    context,
                    drillSound1,
                    drillSound2,
                    words
            );
            intent = new Intent(context,SoundDrillTenActivity.class);
            intent.putExtra("data",drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D1: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new SQLiteException("D1: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D2(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            List<Word> wordList,
                            String drillSound1, String drillSound2)
            throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D2 > Debug: MC");

        Intent intent;

        try {
            String drillData = SoundDrillJsonBuilder.getSoundDrillElevenJson(
                    context,
                    drillSound1,
                    drillSound2,
                    NumeralHelper.getNumeral(dbHelper.getReadableDatabase(), languageId, 1).getSound(),
                    wordList
            );
            intent = new Intent(context, SoundDrillElevenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D2: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D2: " + ex.getMessage());
        }
        return intent;
    }

    /**
     * D3 Word Drill
     *
     * Note that we expect 4 rightDrillWords and 8 wrongDrillWords
     * This would make 4 sets of (1 rightDrillWord + 2 wrongDrillWords)
     *
     * @param context
     * @param dbHelper
     * @param unitId
     * @param drillId
     * @param languageId
     * @param rightDrillWords
     * @param wrongDrillWords
     * @param drillSounds
     * @param numerals
     * @return
     * @throws SQLiteException
     * @throws Exception
     */
    public Intent D3(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            ArrayList<Word> rightDrillWords,
                            ArrayList<Word> wrongDrillWords,
                            ArrayList<String> drillSounds,
                            ArrayList<Numerals> numerals)
            throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D3 > Debug: MC");

        Intent intent;
        int wordsPerSet = 3;

        try {

            /* ==================== Debug ==================== */
            for (int i = 0; i < numerals.size(); i++) {
                System.out.println("WordDrills.D3 > Debug: " + "Using numerals (" + numerals.get(i).getNumber() + ")");
            }

            /* ==================== Init ==================== */

            // Initialize set to hold sets of Right Wrong Word Sets
            ArrayList<RightWrongWordSet> sets = new ArrayList<>();

            // Declare reusable variables
            Word rightWord;
            ArrayList<Word> wrongWords;
            RightWrongWordSet set;

            // Declare counters
            int rightWordCounter = 0;
            int wrongWordCounter = 0;

            /* ==================== Create set 1 ==================== */

            // Assign right word
            rightWord = rightDrillWords.get(rightWordCounter++);

            // Assing wrong words (to array list)
            wrongWords = new ArrayList<>();
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));

            set = getRightWrongWordSet(rightWord, wrongWords);

            // Add set to list of sets
            sets.add(set);

            /* ==================== Create set 2 ==================== */

            // Assign right word
            rightWord = rightDrillWords.get(rightWordCounter++);

            // Assing wrong words (to array list)
            wrongWords = new ArrayList<>();
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));

            set = getRightWrongWordSet(rightWord, wrongWords);

            // Add set to list of sets
            sets.add(set);

            /* ==================== Create set 3 ==================== */

            // Assign right word
            rightWord = rightDrillWords.get(rightWordCounter++);

            // Assing wrong words (to array list)
            wrongWords = new ArrayList<>();
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));

            set = getRightWrongWordSet(rightWord, wrongWords);

            // Add set to list of sets
            sets.add(set);

            /* ==================== Create set 4 ==================== */

            // Assign right word
            rightWord = rightDrillWords.get(rightWordCounter++);

            // Assing wrong words (to array list)
            wrongWords = new ArrayList<>();
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));
            wrongWords.add(wrongDrillWords.get(wrongWordCounter++));

            set = getRightWrongWordSet(rightWord, wrongWords);

            // Add set to list of sets
            sets.add(set);

            /* ==================== Setup drill data ==================== */

            // Create drill data
            String drillData = SoundDrillJsonBuilder.getSoundDrillTwelveJson(
                    context,
                    drillSounds.get(0), // Quick! Mother's coming ..
                    drillSounds.get(1), // You got
                    drillSounds.get(2), // No
                    numerals.get(0).getSound(), // 0
                    numerals.get(1).getSound(), // 1
                    numerals.get(2).getSound(), // 2
                    numerals.get(3).getSound(), // 3
                    numerals.get(4).getSound(), // 4
                    numerals.get(5).getSound(), // 5
                    numerals.get(6).getSound(), // 6
                    drillSounds.get(3), // Words
                    sets
            ); // List of Right Wrong Word Sets

            /* ==================== Create intent ==================== */

            intent = new Intent(context, SoundDrillTwelveActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D3: " + sqlex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("D3: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D4(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter,
                            List<Word> words,
                            String drillSound1,
                            String drillSound2) throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D4 > Debug: MC");

        Intent intent;

        try {
            List<SpelledWord> spelledWords = new ArrayList<>();
            SpelledWord set = null;
            ObjectAndSound<String> objNSound = null;
            List<DraggableImage<String>> items = null;

            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);

                set = new SpelledWord();
                objNSound = new ObjectAndSound<>("",word.getWordSoundURI(),"");
                objNSound.setSpelling(word.getWordName());
                set.setWord(objNSound);
                items = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                int count = 0;
                for (int j = 0; j < word.getWordName().length(); j++) {
                    Letter thisLetter = letterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word.getWordName().charAt(j)));
                    DraggableImage<String> item = new DraggableImage<>(0,0,thisLetter.getLetterPictureLowerCaseBlackURI());
                    count = word.getWordName().length() - word.getWordName().substring(0,word.getWordName().length()-1).replaceAll(thisLetter.getLetterName(),"").length();
                    int n = 0;
                    for (int k =- 1; (k = word.getWordName().indexOf(thisLetter.getLetterName(), k+1)) != -1;) {
                        sb.append(String.valueOf(k+1));
                        if (n<count)
                            sb.append(",");
                        n++;
                    }
                    String letterSound = thisLetter.getPhonicSoundURI();
                    String letterString = "" + word.getWordName().charAt(j);
                    item.setLetter(letterString);
                    item.setSound(letterSound);
                    item.setExtraData(sb.toString());
                    items.add(item);
                }
                set.setLettersImages(items);
                spelledWords.add(set);
            }
            String drillData =SoundDrillJsonBuilder.getSoundDrillThirteenJson(context,drillSound1,drillSound2,spelledWords);
            intent = new Intent(context,SoundDrillThirteenActivity.class);
            intent.putExtra("data",drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D4: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D4: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D5(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId,
                            Letter letter, ArrayList<Word> words,
                            String drillSound1, String drillSound2, String drillSound3) throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D5 > Debug: MC");

        Intent intent;

        try {
            ArrayList<SpelledWord> spelledWords = new ArrayList<>();

            for (int i = 0; i < words.size(); i++) {

                // Get word
                Word word = words.get(i);

                SpelledWord set = new SpelledWord();
                ObjectAndSound<String> onsWord = new ObjectAndSound<>("", word.getWordSoundURI(), "");
                onsWord.setSpelling(word.getWordName());
                set.setWord(onsWord);

                ArrayList<DraggableImage<String>> items = new ArrayList<>();

                for (int j = 0; j < word.getWordName().length(); j++) {
                    Letter thisLetter = letterHelper.getLetterByName(dbHelper.getReadableDatabase(), languageId, Character.toString(word.getWordName().charAt(j)));
                    DraggableImage<String> item = new DraggableImage<>(0, 0, thisLetter.getLetterPictureLowerCaseBlackURI());
                    item.setExtraData(String.valueOf(j+1));
                    items.add(item);
                }

                set.setLettersImages(items);
                spelledWords.add(set);
            }

            String drillData = SoundDrillJsonBuilder.getSoundDrillFourteenJson(context, drillSound1, drillSound2, drillSound3, spelledWords);
            intent = new Intent(context, SoundDrillFourteenActivity.class);
            intent.putExtra("data", drillData);

        } catch (SQLiteException sqlex) {
            throw new SQLiteException("D5: " + sqlex.getMessage());
        } catch (Exception ex) {
            throw new Exception("D5: " + ex.getMessage());
        }
        return intent;
    }

    public Intent D6(Context context, DbHelper dbHelper, int unitId, int drillId, int languageId) throws SQLiteException, Exception {

        // Debug
        System.out.println("WordDrills.D6 > Debug: MC");

        Intent intent;

        try {
            List<Sentence> sentences = new ArrayList<>();

            List<Integer>  sentenceIDs = new ArrayList();
            sentenceIDs = SentenceHelper.getSentences(dbHelper.getReadableDatabase(), languageId, unitId);

            for (int i=0; i < sentenceIDs.size(); i++){
                SentenceDB sentenceFromDB = SentenceHelper.getSentence(dbHelper.getReadableDatabase(), sentenceIDs.get(i));
                Sentence sentence = new Sentence(sentenceFromDB.getWordCount(), sentenceFromDB.getSentenceSoundFile());
                List<DraggableImage<String>> words = new ArrayList<>();
                List<Integer>  sentenceWordIDs = new ArrayList();
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
                String sentenceString = sentenceFromDB.getSentence();
                String punctuation = "" + sentenceString.charAt(sentenceString.length()-1);
                sentence.setPunctuation(punctuation);
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
            throw new Exception("D6: " + ex.getMessage());
        }
        return intent;
    }

    private RightWrongWordSet getRightWrongWordSet(Word rightWord, List<Word> wrongWords) throws Exception {

        try {
            // Get shuffled indexes
            int[] shuffledIndexes = FisherYates.shuffle(wrongWords.size() + 1); // add one because of the 1 'right word'

            // Create list of right and wrong words (use Draggable Images object)
            ArrayList<DraggableImage<Word>> rightAndWrongWords = new ArrayList<>();

            // Add right words to list
            rightAndWrongWords.add(new DraggableImage<Word>(0, 1, rightWord));

            // Add wrong words to list
            for (int i = 0; i < wrongWords.size(); i++) {
                rightAndWrongWords.add(new DraggableImage<Word>(0, 0, wrongWords.get(i)));
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
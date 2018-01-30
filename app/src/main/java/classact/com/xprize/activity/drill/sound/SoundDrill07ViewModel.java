package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 * View Model for Sound Drill Seven Activity
 */

public class SoundDrill07ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private Letter letter;
    private List<List<Letter>> correctWordLetters;
    private List<Word[]> wordSets;
    private List<Integer> checkList;

    @Inject
    SoundDrill07ViewModel(
            Bus bus, DbHelper dbHelper,
            LetterHelper letterHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.letterHelper = letterHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;
        correctWordLetters = new ArrayList<>();
        wordSets = new ArrayList<>();
        checkList = new ArrayList<>();
    }

    @Override
    public SoundDrill07ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill07ViewModel prepare(Context context) {

        // Open database
        dbHelper.openDatabase();

        // Get unit section drill
        // Get unit section
        UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrillInProgress(dbHelper.getReadableDatabase(), 1);
        UnitSection unitSection = unitSectionHelper.getUnitSection(dbHelper.getReadableDatabase(), unitSectionDrill.getUnitSectionId());

        // Get unit id
        // Get unit sub id
        int unitId = unitSection.getUnitId();
        int unitSubId = unitSection.getSectionSubId();

        // Get letter
        letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), 1, unitId, unitSubId);

        // Get correct word ids
        List<Word> correctWords = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, 5);

        // Get correct words
        int numCorrectWords = correctWords.size();

        // Get wrong word ids
        List<Word> wrongWords = (letter.getIsLetter() == 1) ?
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, letter.getLetterName(), numCorrectWords * 2) :
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, numCorrectWords * 2);

        // Prepare word positions and random generator
        int previousCorrectWordPosition = -1;
        int correctWordPosition;
        Random rnd = new Random();

        // Prepare indexes
        List<Integer> indexes = new ArrayList<>();
        indexes.add(0);
        indexes.add(1);
        indexes.add(2);

        // Populate word sets
        int startingWrongWordId = 0;
        for (int i = 0; i < numCorrectWords; i++) {

            // Get correct word position
            if (previousCorrectWordPosition == -1) { // No previous position exists
                correctWordPosition = rnd.nextInt(3);
            } else { // A previous position exists
                correctWordPosition = indexes.get(rnd.nextInt(indexes.size()));
            }

            // Get the index of the correct word position
            int correctWordIndex = indexes.indexOf(correctWordPosition);

            // Remove that index from the indexes array
            indexes.remove(correctWordIndex);

            // If a previous position exists, add that position back
            if (previousCorrectWordPosition != -1) {
                indexes.add(previousCorrectWordPosition);
            }

            // Update the previous word position to the current word position
            previousCorrectWordPosition = correctWordPosition;

            // Add the word position to the checklist
            checkList.add(correctWordPosition);

            // Declare shuffled words arrary
            Word[] shuffledWords = new Word[3];

            // Get correct word
            Word correctWord = correctWords.get(i);

            // Add word to shuffled words list, at found position
            shuffledWords[correctWordPosition] = correctWord;

            // Get correct word letter sounds
            String correctWordName = correctWord.getWordName();
            List<Letter> wordLetters = new ArrayList<>();
            for (int j = 0; j < correctWordName.length(); j++) {

                // Extract letter name
                char wordLetterName = correctWordName.charAt(j);

                // Get letter
                Letter wordLetter = letterHelper.getLetterByName(dbHelper.getReadableDatabase(), 1, "" + wordLetterName);

                // Add word letters
                wordLetters.add(wordLetter);
            }

            // Add word letters to correct word letters
            correctWordLetters.add(wordLetters);

            // Add wrong words to shuffled words array
            shuffledWords[indexes.get(0)] = wrongWords.get(startingWrongWordId++);
            shuffledWords[indexes.get(1)] = wrongWords.get(startingWrongWordId++);

            // Add shuffled words as a word set
            wordSets.add(shuffledWords);
        }

        // Close database
        dbHelper.close();

        return this;
    }

    Letter getLetter() {
        return letter;
    }

    List<Letter> getCorrectWordLetters(int setIndex) {
        return correctWordLetters.get(setIndex);
    }

    Word getCorrectWord(int setIndex) {
        return wordSets.get(setIndex)[checkList.get(setIndex)];
    }

    int getSetCount() {
        return wordSets.size();
    }

    int getSetWordCount(int setIndex) {
        return wordSets.get(setIndex).length;
    }

    boolean isCorrect(int setIndex, int wordIndex) {
        return checkList.get(setIndex) == wordIndex;
    }

    Word getWord(int setIndex, int wordIndex) {
        return wordSets.get(setIndex)[wordIndex];
    }
}
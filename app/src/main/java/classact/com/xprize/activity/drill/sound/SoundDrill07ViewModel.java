package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillWordHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.LetterSequenceHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.database.helper.WordHelper;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.database.model.Word;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.utils.FisherYates;
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
    private List<List<Word>> wordSets;
    private List<Integer> checkList;

    @Inject
    public SoundDrill07ViewModel(
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

        // Get letter id
        // Get letter
        int letterId = LetterSequenceHelper.getLetterID(
                dbHelper.getReadableDatabase(),
                1,
                unitSection.getUnitId(),
                unitSectionDrill.getDrillSubId());
        letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), 1, letterId);

        // Get correct word ids
        List<Word> correctWords = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(),
                1,
                unitSection.getUnitId(),
                unitSection.getSectionSubId(),
                1,
                5);

        // Get correct words
        int numCorrectWords = correctWords.size();

        // Get wrong word ids
        List<Word> wrongWords = (letter.getIsLetter() == 1) ?
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(),
                        1,
                        unitSection.getUnitId(),
                        unitSection.getSectionSubId(),
                        1,
                        letter.getLetterName(),
                        numCorrectWords * 2) :
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(),
                        1,
                        unitSection.getUnitId(),
                        unitSection.getSectionSubId(),
                        1,
                        numCorrectWords * 2);

        // Populate word sets
        int startingWrongWordId = 0;
        for (int i = 0; i < numCorrectWords; i++) {

            // Setup words
            List<Word> words = new ArrayList<>();

            // Add correct word to words list
            Word correctWord = correctWords.get(i);
            words.add(correctWord);

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

            // Add wrong words to words list
            words.add(wrongWords.get(startingWrongWordId++));
            words.add(wrongWords.get(startingWrongWordId++));

            //  Shuffle words and add
            List<Word> shuffledWords = new ArrayList<>();
            int n = words.size();
            int[] s = FisherYates.shuffle(n);
            for (int j = 0; j < n; j++) {
                int sj = s[j];

                // Check if correct word
                if (sj == 0) {

                    // Add correct word to check-list
                    checkList.add(j);
                }

                // Add word to shuffled word
                shuffledWords.add(words.get(sj));
            }

            // Add shuffled words to word set
            wordSets.add(shuffledWords);
        }

        // Close database
        dbHelper.close();

        return this;
    }

    public Letter getLetter() {
        return letter;
    }

    public List<Letter> getCorrectWordLetters(int setIndex) {
        return correctWordLetters.get(setIndex);
    }

    public Word getCorrectWord(int setIndex) {
        return wordSets.get(setIndex).get(checkList.get(setIndex));
    }

    public int getSetCount() {
        return wordSets.size();
    }

    public int getSetWordCount(int setIndex) {
        return wordSets.get(setIndex).size();
    }

    public boolean isCorrect(int setIndex, int wordIndex) {
        return checkList.get(setIndex) == wordIndex;
    }

    public Word getWord(int setIndex, int wordIndex) {
        return wordSets.get(setIndex).get(wordIndex);
    }
}
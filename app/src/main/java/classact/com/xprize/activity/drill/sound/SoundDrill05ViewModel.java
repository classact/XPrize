package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.util.Log;

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
 * Sound Drill 05 View Model
 */

public class SoundDrill05ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private Letter letter;
    private List<Word> givenWords;
    private List<Word> matchingWords;
    private List<Integer> checkList;
    List<Word[]> words;

    @Inject
    public SoundDrill05ViewModel(
            Bus bus, DbHelper dbHelper,
            LetterHelper letterHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.letterHelper = letterHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;

        givenWords = new ArrayList<>();
        matchingWords = new ArrayList<>();
        checkList = new ArrayList<>();
        words = new ArrayList<>();
    }

    @Override
    public SoundDrill05ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill05ViewModel prepare(Context context) {

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
        letter = letterHelper.getLetter(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId);

        // Get temp correct words
        List<Word> tempCorrectWords = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, 6);

        // Establish n
        int n = tempCorrectWords.size();

        // Verify words
        if (n <= 1) {
            Log.e("Sound Drill 05 View Model", "Preparation Failure: Not enough words");
            return this;
        }

        // Ensure n is even
        if (n % 2 != 0) {
            n -= 1; // Remove 1 from odd number to make even
        }

        // Get temp wrong words
        List<Word> tempWrongWords = (letter.getIsLetter() == 1) ?
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, letter.getLetterName(), n * 3) :
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, n * 3);

        // Setup random generator
        Random rnd = new Random();

        // Initialize starting wrongWordIndex
        int wrongWordIndex = 0;

        // Setup words
        for (int i = 0; i < n; i += 2) {

            // Get given word
            Word givenWord = tempCorrectWords.get(i);

            // Add given word to given words
            givenWords.add(givenWord);

            // Get matching word
            Word matchingWord = tempCorrectWords.get(i+1);

            // Add matching word to matching words
            matchingWords.add(matchingWord);

            // Generate matching word index
            int matchingWordIndex = rnd.nextInt(4);

            // Add matching word index to check list
            checkList.add(matchingWordIndex);

            // Setup words that will be added to array
            Word[] setWords = new Word[4];

            // Add matching word to words array
            setWords[matchingWordIndex] = matchingWord;

            // Fill set words
            for (int j = 0; j < 4; j++) {
                if (j != matchingWordIndex) {
                    setWords[j] = tempWrongWords.get(wrongWordIndex++);
                }
            }

            // Add set words to words array
            words.add(setWords);
        }

        // Close database
        dbHelper.close();

        return this;
    }

    public Letter getLetter() {
        return letter;
    }

    public int getSetCount() {
        return givenWords.size();
    }

    public Word getGivenWord(int setIndex) {
        return givenWords.get(setIndex);
    }

    public Word getMatchingWord(int setIndex) {
        return matchingWords.get(setIndex);
    }

    public Word getWord(int setIndex, int wordIndex) {
        return words.get(setIndex)[wordIndex];
    }

    public Word[] getWords(int setIndex) {
        return words.get(setIndex);
    }

    public boolean isCorrect(int setIndex, int wordIndex) {
        return (checkList.get(setIndex) == wordIndex);
    }
}
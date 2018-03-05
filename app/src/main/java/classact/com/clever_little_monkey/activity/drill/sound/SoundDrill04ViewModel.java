package classact.com.clever_little_monkey.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.clever_little_monkey.database.DbHelper;
import classact.com.clever_little_monkey.database.helper.LetterHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionDrillHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionHelper;
import classact.com.clever_little_monkey.database.helper.WordHelper;
import classact.com.clever_little_monkey.database.model.Letter;
import classact.com.clever_little_monkey.database.model.UnitSection;
import classact.com.clever_little_monkey.database.model.UnitSectionDrill;
import classact.com.clever_little_monkey.database.model.Word;
import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.utils.FisherYates;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 * View Model for Sound Drill Four
 */

public class SoundDrill04ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private Letter letter;
    private List<Word> correctWords;
    private List<Word> wrongWords;
    private List<Word> words;
    private List<Boolean> checkList;

    @Inject
    SoundDrill04ViewModel(
            Bus bus, DbHelper dbHelper,
            LetterHelper letterHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.letterHelper = letterHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;

        correctWords = new ArrayList<>();
        wrongWords = new ArrayList<>();
        words = new ArrayList<>();
        checkList = new ArrayList<>();
    }

    @Override
    public SoundDrill04ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill04ViewModel prepare(Context context) {

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

        // Prepare temp list
        List<Word> tempWords = new ArrayList<>();
        List<Boolean> tempCheckList = new ArrayList<>();

        // Get correct words
        correctWords = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, 4);

        // Add to temp words list
        for (int i = 0; i < correctWords.size(); i++) {
            Word word = correctWords.get(i);
            tempWords.add(word);
            tempCheckList.add(true);
        }

        // Get wrong words
        wrongWords = (letter.getIsLetter() == 1) ?
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, letter.getLetterName(), 2) :
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, 2);

        // Add to temp words list
        for (int i = 0; i < wrongWords.size(); i++) {
            Word word = wrongWords.get(i);
            tempWords.add(word);
            tempCheckList.add(false);
        }

        //  Shuffle temp words and add
        int n = tempWords.size();
        int[] s = FisherYates.shuffle(n);
        for (int i = 0; i < n; i++) {
            int si = s[i];
            words.add(tempWords.get(si));
            checkList.add(tempCheckList.get(si));
        }

        // Close database
        dbHelper.close();

        return this;
    }

    Letter getLetter() {
        return letter;
    }

    int getWordCount() {
        return words.size();
    }

    int getCorrectWordCount() {
        return correctWords.size();
    }

    Word getWord(int index) {
        return words.get(index);
    }

    boolean isCorrect(int index) {
        return checkList.get(index);
    }
}
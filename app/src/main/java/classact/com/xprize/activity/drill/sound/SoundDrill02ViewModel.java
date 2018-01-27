package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
 */

public class SoundDrill02ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private Letter letter;
    private List<Word> correctWords;
    private List<Word> wrongWords;
    private List<Integer> checkList;

    @Inject
    public SoundDrill02ViewModel(
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
        checkList = new ArrayList<>();
    }

    @Override
    public SoundDrill02ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill02ViewModel prepare(Context context) {

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

        // Get correct words
        correctWords = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, 5);

        // Establish n
        int n = correctWords.size();

        // Get wrong words
        wrongWords = (letter.getIsLetter() == 1) ?
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, letter.getLetterName(), n) :
                WordHelper.getAntiUnitWords(
                        dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 1, n);

        //
        for (int i = 0; i < n; i++) {
            int correctIndex = (Math.random() < 0.5) ? 0 : 1;
            checkList.add(correctIndex);
        }

        // Close database
        dbHelper.close();

        return this;
    }

    public Letter getLetter() {
        return letter;
    }

    public Word getLeftWord(int index) {
        return (checkList.get(index) == 0) ? correctWords.get(index) : wrongWords.get(index);
    }

    public Word getRightWord(int index) {
        return (checkList.get(index) == 0) ? wrongWords.get(index) : correctWords.get(index);
    }

    public Word getCorrectWord(int index) {
        return correctWords.get(index);
    }

    public int getWordCount() {
        return correctWords.size();
    }

    public boolean isCorrect(int setIndex, int wordIndex) {
        return (checkList.get(setIndex) == wordIndex);
    }
}
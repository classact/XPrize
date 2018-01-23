package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public SoundDrill04ViewModel(
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

        // Get letter id
        // Get letter
        int letterId = LetterSequenceHelper.getLetterID(
                dbHelper.getReadableDatabase(),
                1,
                unitSection.getUnitId(),
                unitSectionDrill.getDrillSubId());
        letter = letterHelper.getLetter(dbHelper.getReadableDatabase(), 1, letterId);

        // Prepare temp list
        List<Word> tempWords = new ArrayList<>();
        List<Boolean> tempCheckList = new ArrayList<>();

        // Get correct word ids
        List<Integer> correctWordIds = DrillWordHelper.getDrillWords(
                dbHelper.getReadableDatabase(),
                1,
                unitSection.getUnitId(),
                unitSection.getSectionSubId(),
                unitSectionDrill.getDrillId(),
                1,
                4);

        // Get correct words
        for (int i = 0; i < correctWordIds.size(); i++) {
            Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), correctWordIds.get(i));
            correctWords.add(word);
            tempWords.add(word);
            tempCheckList.add(true);
        }

        // Get wrong word ids
        List<Integer> wrongWordIds = DrillWordHelper.getWrongDrillWordsByLetter(
                dbHelper.getReadableDatabase(),
                1,
                1,
                letter.getLetterName(),
                2);

        // Get wrong words
        for (int i = 0; i < wrongWordIds.size(); i++) {
            Word word = WordHelper.getWord(dbHelper.getReadableDatabase(), wrongWordIds.get(i));
            wrongWords.add(word);
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

    public Letter getLetter() {
        return letter;
    }

    public int getWordCount() {
        return words.size();
    }

    public int getCorrectWordCount() {
        return correctWords.size();
    }

    public int getWrongWordCount() {
        return wrongWords.size();
    }

    public Word getWord(int index) {
        return words.get(index);
    }

    public boolean isCorrect(int index) {
        return checkList.get(index);
    }
}
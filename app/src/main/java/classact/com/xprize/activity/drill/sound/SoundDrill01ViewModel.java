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
 * ViewModel for SoundDrillOneActivity
 */

public class SoundDrill01ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private Letter letter;
    private List<Word> words;

    @Inject
    SoundDrill01ViewModel(
            Bus bus, DbHelper dbHelper,
            LetterHelper letterHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.letterHelper = letterHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;

        words = new ArrayList<>();
    }

    @Override
    public SoundDrill01ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill01ViewModel prepare(Context context) {

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
                dbHelper.getReadableDatabase(),
                1,
                unitId,
                unitSubId);

        // Get words
        words = WordHelper.getUnitWords(
                dbHelper.getReadableDatabase(),
                1,
                unitId,
                unitSubId,
                1,
                3);

        // Close database
        dbHelper.close();

        return this;
    }

    Letter getLetter() {
        return letter;
    }

    boolean isLetter() {
        return (letter.getIsLetter() == 1);
    }

    Word getWord(int index) {
        return words.get(index);
    }
}
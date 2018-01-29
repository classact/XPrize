package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.LetterHelper;
import classact.com.xprize.database.helper.LetterSequenceHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 * View Model for Sound Drill Three
 */

public class SoundDrill03ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final LetterHelper letterHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private List<Letter> correctLetters;
    private List<Letter> wrongLetters;

    @Inject
    SoundDrill03ViewModel(
            Bus bus, DbHelper dbHelper,
            LetterHelper letterHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.letterHelper = letterHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;
        correctLetters = new ArrayList<>();
        wrongLetters = new ArrayList<>();
    }

    @Override
    public SoundDrill03ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public SoundDrill03ViewModel prepare(Context context) {

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

        // Prepare excluded letter id list
        List<Integer> excludedLetterIds = new ArrayList<>();

        // Get primary letter id
        // Get primary letter
        int primaryLetterId = LetterSequenceHelper.getLetterID(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId);
        Letter primaryLetter = letterHelper.getLetter(dbHelper.getReadableDatabase(), 1, primaryLetterId);

        // Add primary letter to excluded letters list
        excludedLetterIds.add(primaryLetterId);

        // Get secondary letters
        List<Letter> secondaryLetters = letterHelper.getLettersBelow(
                dbHelper.getReadableDatabase(), 1, unitId, unitSubId, 2);

        // Add secondary letters to letters list
        // Ensure that secondary letters are 'excluded' from the 'comparison letters' found for each letter
        if (secondaryLetters != null) {
            for (int i = 0; i < secondaryLetters.size(); i++) {
                Letter secondaryLetter = secondaryLetters.get(i);
                excludedLetterIds.add(secondaryLetter.getLetterId());
                correctLetters.add(secondaryLetter);
            }
        }

        // Establish limit of 'wrong' letters + 'excluded' letters
        int wrongLettersLimit = (secondaryLetters == null) ? 0 : secondaryLetters.size();
        int excludedLettersLimit = 3 + wrongLettersLimit;

        // Get wrong letters
        wrongLetters = letterHelper.getLettersExcludingIds(
                dbHelper.getReadableDatabase(), excludedLetterIds, 1, excludedLettersLimit);

        // Add primary letter to wrong letters
        for (int i = correctLetters.size(); i < wrongLetters.size(); i++) {
            correctLetters.add(primaryLetter);
        }

        // Close database
        dbHelper.close();

        return this;
    }

    int getLetterCount() {
        return correctLetters.size();
    }

    Letter getCorrectLetter(int index) {
        if (index < correctLetters.size()) {
            return correctLetters.get(index);
        }
        return null;
    }

    Letter getWrongLetter(int index) {
        if (index < wrongLetters.size()) {
            return wrongLetters.get(index);
        }
        return null;
    }
}
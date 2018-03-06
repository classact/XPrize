package classact.com.clever_little_monkey.activity.drill.math;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.clever_little_monkey.database.DbHelper;
import classact.com.clever_little_monkey.database.helper.MathDrillFlowWordsHelper;
import classact.com.clever_little_monkey.database.helper.MathImageHelper;
import classact.com.clever_little_monkey.database.helper.NumeralHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionDrillHelper;
import classact.com.clever_little_monkey.database.helper.UnitSectionHelper;
import classact.com.clever_little_monkey.database.model.MathImages;
import classact.com.clever_little_monkey.database.model.Numerals;
import classact.com.clever_little_monkey.database.model.UnitSection;
import classact.com.clever_little_monkey.database.model.UnitSectionDrill;
import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 * ViewModel for Math Drill 06 E
 */

public class MathDrill06EViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private List<String> instructions;
    private MathImages largerNumber;
    private MathImages smallerNumber;
    private String equationSound;
    private Numerals correctNumber;
    private int correctIndex;
    private List<Numerals> numbers;
    private List<Numerals> numberPool;

    @Inject
    MathDrill06EViewModel(
            Bus bus, DbHelper dbHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;

        this.instructions = new ArrayList<>();
        this.numbers = new ArrayList<>();
        this.numberPool = new ArrayList<>();
    }

    @Override
    public MathDrill06EViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public MathDrill06EViewModel prepare(Context context) {

        // Open database
        dbHelper.openDatabase();

        // Get unit section drill
        // Get unit section
        UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrillInProgress(dbHelper.getReadableDatabase(), 1);
        UnitSection unitSection = unitSectionHelper.getUnitSection(dbHelper.getReadableDatabase(), unitSectionDrill.getUnitSectionId());

        // Get unit id
        // Get drill id (custom value atm) TODO: FIX TO BEING DB-BASED
        // Get language id (custom value atm) TODO: FIX TO BEING DB OR SETTINGS / META-DATA BASED
        int unitId = unitSection.getUnitId();
        int drillId = 6;
        int subId = 4; // TODO: FIX TO BEING DB-BASED
        int languageId = 1;

        instructions = MathDrillFlowWordsHelper.getInstructions(dbHelper.getReadableDatabase(), languageId, drillId, subId);

        List<MathImages> mathImages = MathImageHelper.getMathImages(dbHelper.getReadableDatabase(), languageId, unitId, drillId);
        int largestNumber = 0;
        for (MathImages mi : mathImages) {
            if (mi.getImageSound().equalsIgnoreCase("nothing")) {
                equationSound = mi.getNumberOfImagesSound();
            } else {
                if (mi.getNumberOfImages() >= largestNumber) {
                    if (largerNumber != null) {
                        smallerNumber = largerNumber;
                    }
                    largerNumber = mi;
                    largestNumber = mi.getNumberOfImages();
                } else {
                    smallerNumber = mi;
                }
            }
        }

        // Get correct number
        int boyGirl = 2;
        int ans = largerNumber.getTestNumber();
        correctNumber = NumeralHelper.getNumeralByNumber(dbHelper.getReadableDatabase(), languageId, boyGirl, ans);

        // Get incorrect number
        int lower = 0;
        int upper = 20;
        int limit = 2;
        numbers = NumeralHelper.getRandomNumeralsByInclusiveBoundsExcluding(
                dbHelper.getReadableDatabase(), languageId, boyGirl, lower, upper, limit, ans);

        correctIndex = (int) (Math.random() * 3);
        numbers.add(correctIndex, correctNumber);

        // Get number pool
        numberPool = NumeralHelper.getNumeralsBelowAndEqualToLimit(
                dbHelper.getReadableDatabase(), languageId, 20, boyGirl, true);

        // Close database
        dbHelper.close();

        return this;
    }

    List<String> getInstructions() { return this.instructions; }

    MathImages getLargerNumber() {
        return this.largerNumber;
    }

    MathImages getSmallerNumber() {
        return this.smallerNumber;
    }

    String getEquationSound() {
        return this.equationSound;
    }

    Numerals getCorrectNumber() {
        return this.correctNumber;
    }

    int getCorrectIndex() { return this.correctIndex; }

    List<Numerals> getNumbers() {
        return this.numbers;
    }

    Numerals getNumber(int index) { return this.numbers.get(index); }

    List<Numerals> getNumberPool() { return this.numberPool; }
}
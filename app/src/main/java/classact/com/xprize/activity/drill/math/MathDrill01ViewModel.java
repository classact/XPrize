package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helper.NumeralHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.database.model.MathDrillFlowWords;
import classact.com.xprize.database.model.Numerals;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.viewmodel.DrillViewModel;

/**
 * Created by hcdjeong on 2017/12/27.
 * ViewModel for MathsDrillOneActivity
 */

public class MathDrill01ViewModel extends DrillViewModel {

    private final DbHelper dbHelper;
    private final UnitSectionDrillHelper unitSectionDrillHelper;
    private final UnitSectionHelper unitSectionHelper;

    private List<Numerals> numbers;
    private List<String> instructions;

    @Inject
    MathDrill01ViewModel(
            Bus bus, DbHelper dbHelper,
            UnitSectionDrillHelper unitSectionDrillHelper,
            UnitSectionHelper unitSectionHelper) {
        super(bus);
        this.dbHelper = dbHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.unitSectionHelper = unitSectionHelper;

        this.numbers = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    @Override
    public MathDrill01ViewModel register(Lifecycle lifecycle) {
        super.register(lifecycle);
        return this;
    }

    @Override
    public MathDrill01ViewModel prepare(Context context) {

        // Open database
        dbHelper.openDatabase();

        // Get unit section drill
        // Get unit section
        UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrillInProgress(dbHelper.getReadableDatabase(), 1);
        UnitSection unitSection = unitSectionHelper.getUnitSection(dbHelper.getReadableDatabase(), unitSectionDrill.getUnitSectionId());

        // Get language id
        int languageId = 1; // TODO Get from DB or settings instead

        // Get unit id
        // Get drill sub id
        int unitId = unitSection.getUnitId();
        int drillId = unitSectionDrill.getDrillOrder();
        int subId = 0; // TODO Get from DB instead

        // Get and add instructions
        MathDrillFlowWords mathDrillFlowWord = MathDrillFlowWordsHelper.getMathDrillFlowWords(dbHelper.getReadableDatabase(), drillId, subId, languageId);
        if (!mathDrillFlowWord.getDrillSound1().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound1());
        }
        if (!mathDrillFlowWord.getDrillSound2().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound2());
        }
        if (!mathDrillFlowWord.getDrillSound3().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound3());
        }
        if (!mathDrillFlowWord.getDrillSound4().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound4());
        }
        if (!mathDrillFlowWord.getDrillSound5().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound4());
        }
        if (!mathDrillFlowWord.getDrillSound6().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound4());
        }
        if (!mathDrillFlowWord.getDrillSound7().equals("")) {
            instructions.add(mathDrillFlowWord.getDrillSound4());
        }

        // Get numerals
        int limit = 5 * (int) Math.ceil((double) unitId / 5); // Get limit TODO get from DB instead
        int boyGirl = 2; // 1 if other language TODO get from DB instead
        boolean includeZero = (limit > 5); // TODO get from DB instead
        numbers = NumeralHelper.getNumeralsBelowAndEqualToLimit(dbHelper.getReadableDatabase(), languageId, limit, boyGirl, includeZero);

        // Close database
        dbHelper.close();

        return this;
    }

    public int getNumberCount() {
        return this.numbers.size();
    }

    public Numerals getNumber(int index) {
        return this.numbers.get(index);
    }

    public int getInstructionCount() {
        return this.instructions.size();
    }

    public String getInstruction(int index) {
        return this.instructions.get(index);
    }
}
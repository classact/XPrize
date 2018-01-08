package classact.com.xprize.activity.menu.controller;

import android.util.SparseArray;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import classact.com.xprize.common.Globals;
import classact.com.xprize.database.DbAccessor;
import classact.com.xprize.database.DbHelper;
import classact.com.xprize.database.helper.DrillHelper;
import classact.com.xprize.database.helper.DrillTypeHelper;
import classact.com.xprize.database.helper.SectionHelper;
import classact.com.xprize.database.helper.UnitHelper;
import classact.com.xprize.database.helper.UnitSectionDrillHelper;
import classact.com.xprize.database.helper.UnitSectionHelper;
import classact.com.xprize.database.model.Drill;
import classact.com.xprize.database.model.DrillType;
import classact.com.xprize.database.model.Section;
import classact.com.xprize.database.model.Unit;
import classact.com.xprize.database.model.UnitSection;
import classact.com.xprize.database.model.UnitSectionDrill;

/**
 * Created by hcdjeong on 2017/07/24.
 * Database Controller for app navigation
 */

public class DatabaseController extends DbAccessor {

//    public final static int INTRO_SECTION = 1;
//    public final static int TUTORIAL_SECTION = 2;
    public final static int STORY_SECTION = 3;
    public final static int PHONICS_SECTION = 4;
    public final static int WORDS_SECTION = 5;
    public final static int BOOKS_SECTION = 6;
    public final static int MATHS_SECTION = 7;
//    public final static int CHAPTER_END_SECTION = 8;
//    public final static int FINALE_SECTION = 9;

    private SparseArray<Drill> mDrills;
    private SparseArray<DrillType> mDrillTypes;
    private SparseArray<Section> mSections;
    
    private int languageId;

    private DrillHelper drillHelper;
    private DrillTypeHelper drillTypeHelper;
    private SectionHelper sectionHelper;
    private UnitSectionHelper unitSectionHelper;
    private UnitSectionDrillHelper unitSectionDrillHelper;

    @Inject
    public DatabaseController(DbHelper dbHelper,
                              DrillHelper drillHelper, DrillTypeHelper drillTypeHelper,
                              SectionHelper sectionHelper,
                              UnitSectionHelper unitSectionHelper, UnitSectionDrillHelper unitSectionDrillHelper) {
        super(dbHelper);
        this.drillHelper = drillHelper;
        this.drillTypeHelper = drillTypeHelper;
        this.sectionHelper = sectionHelper;
        this.unitSectionHelper = unitSectionHelper;
        this.unitSectionDrillHelper = unitSectionDrillHelper;
        this.languageId = 1;
    }

    public void setLanguage(int languageId) {
        this.languageId = languageId;
    }

    public SparseArray<Section> getSections() {
        if (mSections == null) {
            try {
                // Open database connection
                if (dbOpen()) {
                    mSections = sectionHelper.getSections(dbHelper.getReadableDatabase(), languageId);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                // Close database connection
                dbClose();
            }
        }
        return mSections;
    }

    public SparseArray<Drill> getDrills() {
        if (mDrills == null) {
            try {
                // Open database connection
                if (dbOpen()) {
                    mDrills = drillHelper.getDrills(dbHelper.getReadableDatabase());
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } finally {
                // Close database connection
                dbClose();
            }
        }
        return mDrills;
    }

    public SparseArray<DrillType> getDrillTypes() {
        if (mDrillTypes == null) {
            try {
                // Open database connection
                if (dbOpen()) {
                    mDrillTypes = drillTypeHelper.getDrillTypes(dbHelper.getReadableDatabase(), languageId);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } finally {
                // Close database connection
                dbClose();
            }
        }
        return mDrillTypes;
    }

    public LinkedHashMap<Integer, Unit> getUnits() {
        LinkedHashMap<Integer, Unit> units = null;
        try {
            // Open database connection
            if (dbOpen()) {
                units = UnitHelper.getUnits(dbHelper.getReadableDatabase());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return units;
    }

    public Unit getUnit(int unitId) {
        Unit unit = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unit = UnitHelper.getUnitInfo(
                        dbHelper.getReadableDatabase(), unitId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unit;
    }

    public SparseArray<UnitSection> getUnitSections(int unitId) {
        SparseArray<UnitSection> unitSections = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSections = unitSectionHelper.getUnitSections(
                        dbHelper.getReadableDatabase(),
                        languageId,
                        unitId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSections;
    }

    public SparseArray<UnitSection> getUnitSections(int unitId, int sectionId) {
        SparseArray<UnitSection> unitSections = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSections = unitSectionHelper.getUnitSections(
                        dbHelper.getReadableDatabase(),
                        languageId,
                        unitId,
                        sectionId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSections;
    }

    public UnitSection getUnitSection(int unitSectionId) {
        UnitSection unitSection = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSection = unitSectionHelper.getUnitSection(
                        dbHelper.getReadableDatabase(), unitSectionId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSection;
    }

    public UnitSection getUnitSection(int unitId, int sectionId, int sectionSubId) {
        UnitSection unitSection = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSection = unitSectionHelper.getUnitSection(
                        dbHelper.getReadableDatabase(), unitId, sectionId, sectionSubId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSection;
    }


    public SparseArray<UnitSectionDrill> getUnitSectionDrills(int unitSectionId) {
        SparseArray<UnitSectionDrill> unitSectionDrills = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSectionDrills = unitSectionDrillHelper.getUnitSectionDrills(
                        dbHelper.getReadableDatabase(),
                        languageId,
                        unitSectionId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSectionDrills;
    }

//    public UnitSectionDrill getUnitSectionDrill(int unitSectionDrillId) {
//        UnitSectionDrill unitSectionDrill = null;
//        try {
//            // Open database connection
//            if (dbOpen()) {
//                unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
//                        dbHelper.getReadableDatabase(), unitSectionDrillId);
//            }
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//            ex.printStackTrace();
//        } finally {
//            // Close database connection
//            dbClose();
//        }
//        return unitSectionDrill;
//    }

    public UnitSectionDrill getUnitSectionDrillInProgress() {
        UnitSectionDrill unitSectionDrill = null;
        try {
            // Open database connection
            if (dbOpen()) {
                unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrillInProgress(
                        dbHelper.getReadableDatabase(), languageId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSectionDrill;
    }

    public void playUnitSectionDrill(int unitSectionDrillId) {
        try {
            // Open database connection
            if (dbOpen()) {

                // Get unit section drill
                UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrill(
                        dbHelper.getReadableDatabase(),
                        unitSectionDrillId);
                // Validate unit section drill
                if (unitSectionDrill == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#1");
                }
                // Clear in progress of all unlocked unit section drills
                unitSectionDrillHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup in progress for selected unit section drill
                unitSectionDrill.setInProgress(1);
                // Update selected unit section drill
                unitSectionDrillHelper.update(dbHelper.getWritableDatabase(), unitSectionDrill);

                // Get unit section
                UnitSection unitSection = unitSectionHelper.getUnitSection(
                        dbHelper.getReadableDatabase(),
                        unitSectionDrill.getUnitSectionId());
                // Validate unit section
                if (unitSection == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#2");
                }
                // Clear in progress for all unlocked unit sections
                unitSectionHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup in progress for selected unit section
                unitSection.setInProgress(1);
                // Update selected unit section
                unitSectionHelper.update(dbHelper.getWritableDatabase(), unitSection);

                // Get unit
                Unit unit = UnitHelper.getUnitInfo(
                        dbHelper.getReadableDatabase(),
                        unitSection.getUnitId());
                // Validate unit section
                if (unit == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#3");
                }
                // Clear in progress for all unlocked units
                UnitHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup unit details (includes in progress)
                unit = refactorUnit(unit, unitSection, unitSectionDrill);
                // Update selected unit
                UnitHelper.updateUnitInfo(dbHelper.getWritableDatabase(), unit);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
    }

//    public void playNextUnitSectionDrill() {
//        try {
//            // Open database connection
//            if (dbOpen()) {
//
//                // Get unit section drill
//                UnitSectionDrill unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrillInProgress(
//                        dbHelper.getReadableDatabase(), languageId);
//                // Validate unit section drill
//                if (unitSectionDrill == null) {
//                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#1");
//                }
//                // Clear in progress of all unlocked unit section drills
//                UnitSectionDrillHelper.clearInProgress(dbHelper.getWritableDatabase());
//                // Setup in progress for selected unit section drill
//                unitSectionDrill.setInProgress(1);
//                // Update selected unit section drill
//                UnitSectionDrillHelper.update(dbHelper.getWritableDatabase(), unitSectionDrill);
//
//                // Get unit section
//                UnitSection unitSection = UnitSectionHelper.getUnitSection(
//                        dbHelper.getReadableDatabase(),
//                        unitSectionDrill.getUnitSectionId());
//                // Validate unit section
//                if (unitSection == null) {
//                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#2");
//                }
//                // Clear in progress for all unlocked unit sections
//                UnitSectionHelper.clearInProgress(dbHelper.getWritableDatabase());
//                // Setup in progress for selected unit section
//                unitSection.setInProgress(1);
//                // Update selected unit section
//                UnitSectionHelper.update(dbHelper.getWritableDatabase(), unitSection);
//
//                // Get unit
//                Unit unit = UnitHelper.getUnitInfo(
//                        dbHelper.getReadableDatabase(),
//                        unitSection.getUnitId());
//                // Validate unit section
//                if (unit == null) {
//                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#3");
//                }
//                // Clear in progress for all unlocked units
//                UnitHelper.clearInProgress(dbHelper.getWritableDatabase());
//                // Setup unit details (includes in progress)
//                unit = refactorUnit(unit, unitSection, unitSectionDrill);
//                // Update selected unit
//                UnitHelper.updateUnitInfo(dbHelper.getWritableDatabase(), unit);
//            }
//        } catch (Exception ex) {
//            System.err.println(ex.getMessage());
//            ex.printStackTrace();
//        } finally {
//            // Close database connection
//            dbClose();
//        }
//    }

    private UnitSectionDrill getPreviousUnitSectionDrill(int unitSectionDrillId) {
        UnitSectionDrill previousUnitSectionDrill = null;
        try {
            // Open database connection
            if (dbOpen()) {
                // Get the given unit section drill
                UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrill(
                        dbHelper.getReadableDatabase(), unitSectionDrillId);
                // Validate given unit section drill
                if (unitSectionDrill == null) {
                    throw new Exception("Database Controller: getPreviousUnitSectionDrill Err#1");
                }
                // Decrement id
                int previousUnitSectionDrillId = unitSectionDrillId - 1;
                // Get the unit section drill if it exists
                previousUnitSectionDrill = unitSectionDrillHelper.getUnitSectionDrill(
                        dbHelper.getReadableDatabase(), previousUnitSectionDrillId);
                // Validate previous unit section drill
                if (previousUnitSectionDrill == null) {
                    throw new Exception("Database Controller: getPreviousUnitSectionDrill Err#2");
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return previousUnitSectionDrill;
    }

    public UnitSectionDrill moveToNextUnitSectionDrill() {
        UnitSectionDrill nextUnitSectionDrill = null;
        try {
            // Open database connection
            if (dbOpen()) {
                // Get the current unit section drill in progress
                UnitSectionDrill unitSectionDrill = unitSectionDrillHelper.getUnitSectionDrillInProgress(
                        dbHelper.getReadableDatabase(), languageId);

                // Get next unit section drill id
                int nextUnitSectionDrillId = unitSectionDrill.getUnitSectionDrillId() + 1;

                // Get next unit section drill
                nextUnitSectionDrill = unitSectionDrillHelper.getUnitSectionDrill(
                        dbHelper.getReadableDatabase(), nextUnitSectionDrillId);

                // Validate if it could be found
                if (nextUnitSectionDrill == null) {

                    // Go back to first unit section drill
                    nextUnitSectionDrill = unitSectionDrillHelper.getFirstUnitSectionDrill(
                            dbHelper.getReadableDatabase());
                }

                // Get Unlocked Date
                String unlockedDate = Globals.STANDARD_DATE_TIME_STRING(new Date());

                // Clear in progress of all unlocked unit section drills
                unitSectionDrillHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup in progress for next unit section drill
                nextUnitSectionDrill.setInProgress(1);
                // Setup unlocked + unlocked date for next unit section drill
                if (nextUnitSectionDrill.getUnlocked() == 0) {
                    nextUnitSectionDrill.setUnlocked(1);
                    nextUnitSectionDrill.setUnlockedDate(unlockedDate);
                }
                // Update next unit section drill
                unitSectionDrillHelper.update(dbHelper.getWritableDatabase(), nextUnitSectionDrill);

                // Get next unit section
                UnitSection nextUnitSection = unitSectionHelper.getUnitSection(
                        dbHelper.getReadableDatabase(),
                        nextUnitSectionDrill.getUnitSectionId());
                // Validate next unit section
                if (nextUnitSection == null) {
                    throw new Exception("Database Controller: moveToNextUnitSectionDrill Err#1");
                }
                // Clear in progress for all unlocked unit sections
                unitSectionHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup in progress for next unit section
                nextUnitSection.setInProgress(1);
                // Setup unlocked + unlocked date for next unit section
                if (nextUnitSection.getUnlocked() == 0) {
                    nextUnitSection.setUnlocked(1);
                    nextUnitSection.setUnlockedDate(unlockedDate);
                }
                // Update next unit section
                unitSectionHelper.update(dbHelper.getWritableDatabase(), nextUnitSection);

                // Get next unit
                Unit nextUnit = UnitHelper.getUnitInfo(
                        dbHelper.getReadableDatabase(),
                        nextUnitSection.getUnitId());
                // Validate next unit
                if (nextUnit == null) {
                    throw new Exception("Database Controller: moveToNextUnitSectionDrill Err#2");
                }
                // Clear in progress for all unlocked units
                UnitHelper.clearInProgress(dbHelper.getWritableDatabase());
                // Setup unlocked + unlocked date for next unit
                if (nextUnit.getUnitUnlocked() == 0) {
                    nextUnit.setUnitUnlocked(1);
                }
                // Setup unit details (includes in progress)
                nextUnit = refactorUnit(nextUnit, nextUnitSection, nextUnitSectionDrill);
                // Update next unit
                UnitHelper.updateUnitInfo(dbHelper.getWritableDatabase(), nextUnit);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return nextUnitSectionDrill;
    }

    private Unit refactorUnit(Unit unit, UnitSection unitSection, UnitSectionDrill unitSectionDrill) {

        int unitId = unit.getUnitId();
        // int unitSectionId = unitSection.getUnitSectionId();
        int unitSectionDrillId = unitSectionDrill.getUnitSectionDrillId();
        // int sectionId = unitSection.getSectionId();
        int drillId = unitSectionDrill.getDrillId();
        int sectionSubId = unitSection.getSectionSubId();
        // int drillSubId = unitSectionDrill.getDrillSubId();

        unit.setUnitDateLastPlayed(Globals.STANDARD_DATE_TIME_STRING(new Date()));
        unit.setUnitInProgress(1);
        unit.setUnitSubIDInProgress(sectionSubId);
        unit.setUnitCompleted(0);
        unit.setUnitDrillLastPlayed(drillId);

        // Check if intro cinematic
        if (unitId == 0 &&
                getDrillTypes().get(getDrills().get(drillId).getDrillTypeId()).getName().equalsIgnoreCase("Cinematic")) {
            unit.setUnitDrillLastPlayed(0);
            unit.setUnitFirstTimeMovie(1);
        }
        // Check if tutorial
        else if (unitId == 0 &&
                getDrillTypes().get(getDrills().get(drillId).getDrillTypeId()).getName().equalsIgnoreCase("Tutorial")) {
            unit.setUnitCompleted(1);
            unit.setUnitDrillLastPlayed(0);
            unit.setUnitFirstTime(1);
        }
        // Check if intro movie
        else if (unitId > 0 && unitId < 21 &&
                getDrillTypes().get(getDrills().get(drillId).getDrillTypeId()).getName().equalsIgnoreCase("Movie")) {
            unit.setUnitDrillLastPlayed(0);
            unit.setUnitFirstTimeMovie(1);
        }
        // Check if chapter end
        else if (unitId > 0 && unitId < 21 &&
                getDrillTypes().get(getDrills().get(drillId).getDrillTypeId()).getName().equalsIgnoreCase("Cinematic")) {
            unit.setUnitCompleted(1);
            UnitSectionDrill previousUnitSectionDrill = getPreviousUnitSectionDrill(unitSectionDrillId);
            if (previousUnitSectionDrill == null) {
                unit.setUnitDrillLastPlayed(23); // Assumed last maths drill
            } else {
                unit.setUnitDrillLastPlayed(previousUnitSectionDrill.getDrillId());
            }
        }
        // Check if finale cinematic
        else if (unitId == 21 &&
                getDrillTypes().get(getDrills().get(drillId).getDrillTypeId()).getName().equalsIgnoreCase("Cinematic")) {
            unit.setUnitCompleted(1);
            unit.setUnitDrillLastPlayed(0);
            unit.setUnitFirstTime(1);
            unit.setUnitFirstTimeMovie(1);
        }
        // ... It's a drill!

        return unit;
    }

    public Object run(DatabaseQuery databaseQuery) {
        Object result = null;
        try {
            // Open database connection
            if (dbOpen()) {
                // Get result
                result = databaseQuery.execute(dbHelper);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return result;
    }
}
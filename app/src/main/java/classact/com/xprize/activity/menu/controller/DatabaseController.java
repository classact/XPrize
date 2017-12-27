package classact.com.xprize.activity.menu.controller;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;

import classact.com.xprize.common.Globals;
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
 */

public class DatabaseController {

    public final static int INTRO_SECTION = 1;
    public final static int TUTORIAL_SECTION = 2;
    public final static int STORY_SECTION = 3;
    public final static int PHONICS_SECTION = 4;
    public final static int WORDS_SECTION = 5;
    public final static int BOOKS_SECTION = 6;
    public final static int MATHS_SECTION = 7;
    public final static int CHAPTER_END_SECTION = 8;
    public final static int FINALE_SECTION = 9;

    private LinkedHashMap<Integer, Drill> mDrills;
    private LinkedHashMap<Integer, DrillType> mDrillTypes;
    private LinkedHashMap<Integer, Section> mSections;

    private static DatabaseController mDatabaseController;
    private static Context mContext;
    private static int mLanguageId;
    private DbHelper mDbHelper;

    private DatabaseController() {}

    public static DatabaseController getInstance(Context context, int languageId) {
        if (mDatabaseController == null) {
            mDatabaseController = new DatabaseController();
        }
        mContext = context;
        mLanguageId = languageId;
        return mDatabaseController;
    }

    public LinkedHashMap<Integer, Section> getSections() {
        if (mSections == null) {
            try {
                // Establish database connection
                if (dbEstablsh()) {
                    mSections = SectionHelper.getSections(mDbHelper.getReadableDatabase(), mLanguageId);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } finally {
                // Close database connection
                dbClose();
            }
        }
        return mSections;
    }

    public LinkedHashMap<Integer, Drill> getDrills() {
        if (mDrills == null) {
            try {
                // Establish database connection
                if (dbEstablsh()) {
                    mDrills = DrillHelper.getDrills(mDbHelper.getReadableDatabase());
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

    public LinkedHashMap<Integer, DrillType> getDrillTypes() {
        if (mDrillTypes == null) {
            try {
                // Establish database connection
                if (dbEstablsh()) {
                    mDrillTypes = DrillTypeHelper.getDrillTypes(mDbHelper.getReadableDatabase(), mLanguageId);
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
            // Establish database connection
            if (dbEstablsh()) {
                units = UnitHelper.getUnits(mDbHelper.getReadableDatabase());
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
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
            // Establish database connection
            if (dbEstablsh()) {
                unit = UnitHelper.getUnitInfo(
                        mDbHelper.getReadableDatabase(), unitId);
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

    public LinkedHashMap<Integer, UnitSection> getUnitSections(int unitId) {
        LinkedHashMap<Integer, UnitSection> unitSections = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                unitSections = UnitSectionHelper.getUnitSections(
                        mDbHelper.getReadableDatabase(),
                        mLanguageId,
                        unitId);
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

    public LinkedHashMap<Integer, UnitSection> getUnitSections(int unitId, int sectionId) {
        LinkedHashMap<Integer, UnitSection> unitSections = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                unitSections = UnitSectionHelper.getUnitSections(
                        mDbHelper.getReadableDatabase(),
                        mLanguageId,
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
            // Establish database connection
            if (dbEstablsh()) {
                unitSection = UnitSectionHelper.getUnitSection(
                        mDbHelper.getReadableDatabase(), unitSectionId);
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
            // Establish database connection
            if (dbEstablsh()) {
                unitSection = UnitSectionHelper.getUnitSection(
                        mDbHelper.getReadableDatabase(), unitId, sectionId, sectionSubId);
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


    public LinkedHashMap<Integer, UnitSectionDrill> getUnitSectionDrills(int unitSectionId) {
        LinkedHashMap<Integer, UnitSectionDrill> unitSectionDrills = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                unitSectionDrills = UnitSectionDrillHelper.getUnitSectionDrills(
                        mDbHelper.getReadableDatabase(),
                        mLanguageId,
                        unitSectionId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSectionDrills;
    }

    public UnitSectionDrill getUnitSectionDrill(int unitSectionDrillId) {
        UnitSectionDrill unitSectionDrill = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
                        mDbHelper.getReadableDatabase(), unitSectionDrillId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSectionDrill;
    }

    public UnitSectionDrill getUnitSectionDrillInProgress() {
        UnitSectionDrill unitSectionDrill = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrillInProgress(
                        mDbHelper.getReadableDatabase(), mLanguageId);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
        return unitSectionDrill;
    }

    public void playUnitSectionDrill(int unitSectionDrillId) {
        try {
            // Establish database connection
            if (dbEstablsh()) {

                // Get unit section drill
                UnitSectionDrill unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
                        mDbHelper.getReadableDatabase(),
                        unitSectionDrillId);
                // Validate unit section drill
                if (unitSectionDrill == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#1");
                }
                // Clear in progress of all unlocked unit section drills
                UnitSectionDrillHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for selected unit section drill
                unitSectionDrill.setInProgress(1);
                // Update selected unit section drill
                UnitSectionDrillHelper.update(mDbHelper.getWritableDatabase(), unitSectionDrill);

                // Get unit section
                UnitSection unitSection = UnitSectionHelper.getUnitSection(
                        mDbHelper.getReadableDatabase(),
                        unitSectionDrill.getUnitSectionId());
                // Validate unit section
                if (unitSection == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#2");
                }
                // Clear in progress for all unlocked unit sections
                UnitSectionHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for selected unit section
                unitSection.setInProgress(1);
                // Update selected unit section
                UnitSectionHelper.update(mDbHelper.getWritableDatabase(), unitSection);

                // Get unit
                Unit unit = UnitHelper.getUnitInfo(
                        mDbHelper.getReadableDatabase(),
                        unitSection.getUnitId());
                // Validate unit section
                if (unit == null) {
                    throw new Exception("Database Controller: playUnitSectionDrill Err#3");
                }
                // Clear in progress for all unlocked units
                UnitHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup unit details (includes in progress)
                unit = refactorUnit(unit, unitSection, unitSectionDrill);
                // Update selected unit
                UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), unit);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
    }

    public void playNextUnitSectionDrill() {
        try {
            // Establish database connection
            if (dbEstablsh()) {

                // Get unit section drill
                UnitSectionDrill unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrillInProgress(
                        mDbHelper.getReadableDatabase(), mLanguageId);
                // Validate unit section drill
                if (unitSectionDrill == null) {
                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#1");
                }
                // Clear in progress of all unlocked unit section drills
                UnitSectionDrillHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for selected unit section drill
                unitSectionDrill.setInProgress(1);
                // Update selected unit section drill
                UnitSectionDrillHelper.update(mDbHelper.getWritableDatabase(), unitSectionDrill);

                // Get unit section
                UnitSection unitSection = UnitSectionHelper.getUnitSection(
                        mDbHelper.getReadableDatabase(),
                        unitSectionDrill.getUnitSectionId());
                // Validate unit section
                if (unitSection == null) {
                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#2");
                }
                // Clear in progress for all unlocked unit sections
                UnitSectionHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for selected unit section
                unitSection.setInProgress(1);
                // Update selected unit section
                UnitSectionHelper.update(mDbHelper.getWritableDatabase(), unitSection);

                // Get unit
                Unit unit = UnitHelper.getUnitInfo(
                        mDbHelper.getReadableDatabase(),
                        unitSection.getUnitId());
                // Validate unit section
                if (unit == null) {
                    throw new Exception("Database Controller: playNextUnitSectionDrill Err#3");
                }
                // Clear in progress for all unlocked units
                UnitHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup unit details (includes in progress)
                unit = refactorUnit(unit, unitSection, unitSectionDrill);
                // Update selected unit
                UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), unit);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Close database connection
            dbClose();
        }
    }

    private UnitSectionDrill getPreviousUnitSectionDrill(int unitSectionDrillId) {
        UnitSectionDrill previousUnitSectionDrill = null;
        try {
            // Establish database connection
            if (dbEstablsh()) {
                // Get the given unit section drill
                UnitSectionDrill unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
                        mDbHelper.getReadableDatabase(), unitSectionDrillId);
                // Validate given unit section drill
                if (unitSectionDrill == null) {
                    throw new Exception("Database Controller: getPreviousUnitSectionDrill Err#1");
                }
                // Decrement id
                int previousUnitSectionDrillId = unitSectionDrillId - 1;
                // Get the unit section drill if it exists
                previousUnitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
                        mDbHelper.getReadableDatabase(), previousUnitSectionDrillId);
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
            // Establish database connection
            if (dbEstablsh()) {
                // Get the current unit section drill in progress
                UnitSectionDrill unitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrillInProgress(
                        mDbHelper.getReadableDatabase(), mLanguageId);

                // Get next unit section drill id
                int nextUnitSectionDrillId = unitSectionDrill.getUnitSectionDrillId() + 1;

                // Get next unit section drill
                nextUnitSectionDrill = UnitSectionDrillHelper.getUnitSectionDrill(
                        mDbHelper.getReadableDatabase(), nextUnitSectionDrillId);

                // Validate if it could be found
                if (nextUnitSectionDrill == null) {

                    // Go back to first unit section drill
                    nextUnitSectionDrill = UnitSectionDrillHelper.getFirstUnitSectionDrill(
                            mDbHelper.getReadableDatabase());
                }

                // Get Unlocked Date
                String unlockedDate = Globals.STANDARD_DATE_TIME_STRING(new Date());

                // Clear in progress of all unlocked unit section drills
                UnitSectionDrillHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for next unit section drill
                nextUnitSectionDrill.setInProgress(1);
                // Setup unlocked + unlocked date for next unit section drill
                if (nextUnitSectionDrill.getUnlocked() == 0) {
                    nextUnitSectionDrill.setUnlocked(1);
                    nextUnitSectionDrill.setUnlockedDate(unlockedDate);
                }
                // Update next unit section drill
                UnitSectionDrillHelper.update(mDbHelper.getWritableDatabase(), nextUnitSectionDrill);

                // Get next unit section
                UnitSection nextUnitSection = UnitSectionHelper.getUnitSection(
                        mDbHelper.getReadableDatabase(),
                        nextUnitSectionDrill.getUnitSectionId());
                // Validate next unit section
                if (nextUnitSection == null) {
                    throw new Exception("Database Controller: moveToNextUnitSectionDrill Err#1");
                }
                // Clear in progress for all unlocked unit sections
                UnitSectionHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup in progress for next unit section
                nextUnitSection.setInProgress(1);
                // Setup unlocked + unlocked date for next unit section
                if (nextUnitSection.getUnlocked() == 0) {
                    nextUnitSection.setUnlocked(1);
                    nextUnitSection.setUnlockedDate(unlockedDate);
                }
                // Update next unit section
                UnitSectionHelper.update(mDbHelper.getWritableDatabase(), nextUnitSection);

                // Get next unit
                Unit nextUnit = UnitHelper.getUnitInfo(
                        mDbHelper.getReadableDatabase(),
                        nextUnitSection.getUnitId());
                // Validate next unit
                if (nextUnit == null) {
                    throw new Exception("Database Controller: moveToNextUnitSectionDrill Err#2");
                }
                // Clear in progress for all unlocked units
                UnitHelper.clearInProgress(mDbHelper.getWritableDatabase());
                // Setup unlocked + unlocked date for next unit
                if (nextUnit.getUnitUnlocked() == 0) {
                    nextUnit.setUnitUnlocked(1);
                }
                // Setup unit details (includes in progress)
                nextUnit = refactorUnit(nextUnit, nextUnitSection, nextUnitSectionDrill);
                // Update next unit
                UnitHelper.updateUnitInfo(mDbHelper.getWritableDatabase(), nextUnit);
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
        int unitSectionId = unitSection.getUnitSectionId();
        int unitSectionDrillId = unitSectionDrill.getUnitSectionDrillId();
        int sectionId = unitSection.getSectionId();
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
            // Establish database connection
            if (dbEstablsh()) {
                // Get result
                result = databaseQuery.execute(mDbHelper);
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

    private boolean dbEstablsh() {
        try {
            // Initialize DbHelper
            mDbHelper = DbHelper.getDbHelper(mContext);
            // Create database (or connect to existing)
            mDbHelper.createDatabase(false);
            // Test opening database
            mDbHelper.openDatabase();
            // All good
            return true;

            // Otherwise
        } catch (IOException ioex) {
            System.err.println("DatabaseController.dbEstablish > IOException: " + ioex.getMessage());
        } catch (SQLiteException sqlex) {
            System.err.println("DatabaseController.dbEstablish > SQLiteException: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.err.println("DatabaseController.dbEstablish > Exception: " + ex.getMessage());
        }
        return false;
    }

    private void dbClose() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
}

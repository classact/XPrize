package classact.com.clever_little_monkey.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;

import javax.inject.Inject;

import classact.com.clever_little_monkey.database.model.UnitSection;

/**
 * Created by hcdjeong on 2017/07/24.
 * Helper for {@link UnitSection}
 */

public class UnitSectionHelper {

    @Inject
    public UnitSectionHelper() {

    }

    public void clearInProgress(SQLiteDatabase db) throws SQLiteException {
        Cursor cursor = db.rawQuery(
                "UPDATE tbl_UnitSection " +
                        "SET InProgress = 0 " +
                        "WHERE Unlocked = 1", null);
        cursor.moveToFirst();
        cursor.close();
    }

    public int update(SQLiteDatabase db, UnitSection unitSection) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("UnitId", unitSection.getUnitId());
        contentValues.put("SectionId", unitSection.getSectionId());
        contentValues.put("LanguageId", unitSection.getLanguageId());
        contentValues.put("SectionOrder", unitSection.getSectionOrder());
        contentValues.put("SectionSubId", unitSection.getSectionSubId());
        contentValues.put("SectionSubject", unitSection.getSectionSubject());
        contentValues.put("Unlocked", unitSection.getUnlocked());
        contentValues.put("UnlockedDate", unitSection.getUnlockedDate());
        contentValues.put("InProgress", unitSection.getInProgress());
        int numOfUpdatedRows = db.update("tbl_UnitSection", contentValues, "_id = ? ",
                new String[] { Integer.toString(unitSection.getUnitSectionId()) });
        contentValues.clear();
        return numOfUpdatedRows;
    }

    public UnitSection getUnitSection(SQLiteDatabase db, int id) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "UnitId, " +
                        "SectionId, " +
                        "LanguageId, " +
                        "SectionOrder, " +
                        "SectionSubId, " +
                        "SectionSubject, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSection " +
                        "WHERE _id = " + id, null);

        UnitSection unitSection = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSection = new UnitSection();
            unitSection.setUnitSectionId(id);
            unitSection.setUnitId(cursor.getInt(cursor.getColumnIndex("UnitId")));
            unitSection.setSectionId(cursor.getInt(cursor.getColumnIndex("SectionId")));
            unitSection.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSection.setSectionOrder(cursor.getInt(cursor.getColumnIndex("SectionOrder")));
            unitSection.setSectionSubId(cursor.getInt(cursor.getColumnIndex("SectionSubId")));
            unitSection.setSectionSubject(cursor.getString(cursor.getColumnIndex("SectionSubject")));
            unitSection.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSection.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSection.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSection;
    }

    public UnitSection getUnitSection(SQLiteDatabase db, int unitId, int sectionId, int sectionSubId) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitId, " +
                        "SectionId, " +
                        "LanguageId, " +
                        "SectionOrder, " +
                        "SectionSubId, " +
                        "SectionSubject, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSection " +
                        "WHERE UnitId = " + unitId + " " +
                        "AND SectionId = " + sectionId + " " +
                        "AND SectionSubId = " + sectionSubId + " " +
                        "LIMIT 1", null);

        UnitSection unitSection = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSection = new UnitSection();
            unitSection.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("_id")));
            unitSection.setUnitId(cursor.getInt(cursor.getColumnIndex("UnitId")));
            unitSection.setSectionId(cursor.getInt(cursor.getColumnIndex("SectionId")));
            unitSection.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSection.setSectionOrder(cursor.getInt(cursor.getColumnIndex("SectionOrder")));
            unitSection.setSectionSubId(cursor.getInt(cursor.getColumnIndex("SectionSubId")));
            unitSection.setSectionSubject(cursor.getString(cursor.getColumnIndex("SectionSubject")));
            unitSection.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSection.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSection.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSection;
    }

    public UnitSection getUnitSectionInProgress(SQLiteDatabase db, int languageId) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitId, " +
                        "SectionId, " +
                        "LanguageId, " +
                        "SectionOrder, " +
                        "SectionSubId, " +
                        "SectionSubject, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSection " +
                        "WHERE LanguageId = " + languageId + " " +
                        "AND Unlocked = 1 " +
                        "AND InProgress = 1 " +
                        "ORDER BY _id ASC " +
                        "LIMIT 1", null);

        UnitSection unitSection = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSection = new UnitSection();
            unitSection.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("_id")));
            unitSection.setUnitId(cursor.getInt(cursor.getColumnIndex("UnitId")));
            unitSection.setSectionId(cursor.getInt(cursor.getColumnIndex("SectionId")));
            unitSection.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSection.setSectionOrder(cursor.getInt(cursor.getColumnIndex("SectionOrder")));
            unitSection.setSectionSubId(cursor.getInt(cursor.getColumnIndex("SectionSubId")));
            unitSection.setSectionSubject(cursor.getString(cursor.getColumnIndex("SectionSubject")));
            unitSection.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSection.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSection.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSection;
    }

    public SparseArray<UnitSection> getUnitSections(
            SQLiteDatabase db,
            int languageId,
            int unitId) throws SQLiteException {

        SparseArray<UnitSection> unitSections = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitId, " +
                        "SectionId, " +
                        "LanguageId, " +
                        "SectionOrder, " +
                        "SectionSubId, " +
                        "SectionSubject, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSection " +
                        "WHERE LanguageId = " + languageId + " " +
                        "AND UnitId = " + unitId + " " +
                        "ORDER BY SectionOrder ASC", null);

        if (cursor.getCount() > 0) {
            unitSections = new SparseArray<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                UnitSection unitSection = new UnitSection();
                unitSection.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("_id")));
                unitSection.setUnitId(cursor.getInt(cursor.getColumnIndex("UnitId")));
                unitSection.setSectionId(cursor.getInt(cursor.getColumnIndex("SectionId")));
                unitSection.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                unitSection.setSectionOrder(cursor.getInt(cursor.getColumnIndex("SectionOrder")));
                unitSection.setSectionSubId(cursor.getInt(cursor.getColumnIndex("SectionSubId")));
                unitSection.setSectionSubject(cursor.getString(cursor.getColumnIndex("SectionSubject")));
                unitSection.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
                unitSection.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
                unitSection.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
                unitSections.put(unitSection.getUnitSectionId(), unitSection);
            }
        }
        cursor.close();
        return unitSections;
    }

    public SparseArray<UnitSection> getUnitSections(SQLiteDatabase db, int languageId, int unitId, int sectionId) throws SQLiteException {

        SparseArray<UnitSection> unitSections = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitId, " +
                        "SectionId, " +
                        "LanguageId, " +
                        "SectionOrder, " +
                        "SectionSubId, " +
                        "SectionSubject, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSection " +
                        "WHERE LanguageId = " + languageId + " " +
                        "AND UnitId = " + unitId + " " +
                        "AND SectionId = " + sectionId + " " +
                        "ORDER BY SectionOrder ASC", null);

        if (cursor.getCount() > 0) {
            unitSections = new SparseArray<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                UnitSection unitSection = new UnitSection();
                unitSection.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("_id")));
                unitSection.setUnitId(cursor.getInt(cursor.getColumnIndex("UnitId")));
                unitSection.setSectionId(cursor.getInt(cursor.getColumnIndex("SectionId")));
                unitSection.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                unitSection.setSectionOrder(cursor.getInt(cursor.getColumnIndex("SectionOrder")));
                unitSection.setSectionSubId(cursor.getInt(cursor.getColumnIndex("SectionSubId")));
                unitSection.setSectionSubject(cursor.getString(cursor.getColumnIndex("SectionSubject")));
                unitSection.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
                unitSection.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
                unitSection.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
                unitSections.put(unitSection.getUnitSectionId(), unitSection);
            }
        }
        cursor.close();
        return unitSections;
    }
}
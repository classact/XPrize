package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;

import classact.com.xprize.database.model.UnitSectionDrill;

/**
 * Created by hcdjeong on 2017/07/24.
 * Helper for {@link UnitSectionDrill}
 */

public class UnitSectionDrillHelper {

    public void clearInProgress(SQLiteDatabase db) throws SQLiteException {
        Cursor cursor = db.rawQuery(
                "UPDATE tbl_UnitSectionDrill " +
                        "SET InProgress = 0 " +
                        "WHERE Unlocked = 1", null);
        cursor.moveToFirst();
        cursor.close();
    }

    public int update(SQLiteDatabase db, UnitSectionDrill unitSectionDrill) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("UnitSectionId", unitSectionDrill.getUnitSectionId());
        contentValues.put("DrillId", unitSectionDrill.getDrillId());
        contentValues.put("DrillSubId", unitSectionDrill.getDrillSubId());
        contentValues.put("LanguageId", unitSectionDrill.getLanguageId());
        contentValues.put("DrillOrder", unitSectionDrill.getDrillOrder());
        contentValues.put("DrillScore", unitSectionDrill.getDrillScore());
        contentValues.put("DrillScoreMax", unitSectionDrill.getDrillScoreMax());
        contentValues.put("Unlocked", unitSectionDrill.getUnlocked());
        contentValues.put("UnlockedDate", unitSectionDrill.getUnlockedDate());
        contentValues.put("InProgress", unitSectionDrill.getInProgress());
        int numOfUpdatedRows = db.update("tbl_UnitSectionDrill", contentValues, "_id = ? ",
                new String[] { Integer.toString(unitSectionDrill.getUnitSectionDrillId()) });
        contentValues.clear();
        return numOfUpdatedRows;
    }

    public UnitSectionDrill getUnitSectionDrill(SQLiteDatabase db, int id) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "UnitSectionId, " +
                        "DrillId, " +
                        "DrillSubId, " +
                        "LanguageId, " +
                        "DrillOrder, " +
                        "DrillScore, " +
                        "DrillScoreMax, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSectionDrill " +
                        "WHERE _id = " + id, null);

        UnitSectionDrill unitSectionDrill = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSectionDrill = new UnitSectionDrill();
            unitSectionDrill.setUnitSectionDrillId(id);
            unitSectionDrill.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("UnitSectionId")));
            unitSectionDrill.setDrillId(cursor.getInt(cursor.getColumnIndex("DrillId")));
            unitSectionDrill.setDrillSubId(cursor.getInt(cursor.getColumnIndex("DrillSubId")));
            unitSectionDrill.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSectionDrill.setDrillOrder(cursor.getInt(cursor.getColumnIndex("DrillOrder")));
            unitSectionDrill.setDrillScore(cursor.getInt(cursor.getColumnIndex("DrillScore")));
            unitSectionDrill.setDrillScoreMax(cursor.getInt(cursor.getColumnIndex("DrillScoreMax")));
            unitSectionDrill.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSectionDrill.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSectionDrill.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSectionDrill;
    }

    public UnitSectionDrill getFirstUnitSectionDrill(SQLiteDatabase db) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitSectionId, " +
                        "DrillId, " +
                        "DrillSubId, " +
                        "LanguageId, " +
                        "DrillOrder, " +
                        "DrillScore, " +
                        "DrillScoreMax, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSectionDrill " +
                        "ORDER BY _id ASC " +
                        "LIMIT 1", null);

        UnitSectionDrill unitSectionDrill = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSectionDrill = new UnitSectionDrill();
            unitSectionDrill.setUnitSectionDrillId(cursor.getInt(cursor.getColumnIndex("_id")));
            unitSectionDrill.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("UnitSectionId")));
            unitSectionDrill.setDrillId(cursor.getInt(cursor.getColumnIndex("DrillId")));
            unitSectionDrill.setDrillSubId(cursor.getInt(cursor.getColumnIndex("DrillSubId")));
            unitSectionDrill.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSectionDrill.setDrillOrder(cursor.getInt(cursor.getColumnIndex("DrillOrder")));
            unitSectionDrill.setDrillScore(cursor.getInt(cursor.getColumnIndex("DrillScore")));
            unitSectionDrill.setDrillScoreMax(cursor.getInt(cursor.getColumnIndex("DrillScoreMax")));
            unitSectionDrill.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSectionDrill.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSectionDrill.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSectionDrill;
    }

    public UnitSectionDrill getUnitSectionDrillInProgress(SQLiteDatabase db, int languageId) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitSectionId, " +
                        "DrillId, " +
                        "DrillSubId, " +
                        "LanguageId, " +
                        "DrillOrder, " +
                        "DrillScore, " +
                        "DrillScoreMax, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSectionDrill " +
                        "WHERE LanguageId = " + languageId + " " +
                        "AND InProgress = 1 " +
                        "ORDER BY _id ASC " +
                        "LIMIT 1", null);

        UnitSectionDrill unitSectionDrill = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unitSectionDrill = new UnitSectionDrill();
            unitSectionDrill.setUnitSectionDrillId(cursor.getInt(cursor.getColumnIndex("_id")));
            unitSectionDrill.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("UnitSectionId")));
            unitSectionDrill.setDrillId(cursor.getInt(cursor.getColumnIndex("DrillId")));
            unitSectionDrill.setDrillSubId(cursor.getInt(cursor.getColumnIndex("DrillSubId")));
            unitSectionDrill.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            unitSectionDrill.setDrillOrder(cursor.getInt(cursor.getColumnIndex("DrillOrder")));
            unitSectionDrill.setDrillScore(cursor.getInt(cursor.getColumnIndex("DrillScore")));
            unitSectionDrill.setDrillScoreMax(cursor.getInt(cursor.getColumnIndex("DrillScoreMax")));
            unitSectionDrill.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
            unitSectionDrill.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
            unitSectionDrill.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
        }
        cursor.close();
        return unitSectionDrill;
    }

    public SparseArray<UnitSectionDrill> getUnitSectionDrills(
            SQLiteDatabase db,
            int languageId,
            int unitSectionId) throws SQLiteException {

        SparseArray<UnitSectionDrill> unitSectionDrills = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitSectionId, " +
                        "DrillId, " +
                        "DrillSubId, " +
                        "LanguageId, " +
                        "DrillOrder, " +
                        "DrillScore, " +
                        "DrillScoreMax, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSectionDrill " +
                        "WHERE LanguageId = " + languageId + " " +
                        "AND UnitSectionId = " + unitSectionId + " " +
                        "ORDER BY DrillOrder ASC", null);

        if (cursor.getCount() > 0) {
            unitSectionDrills = new SparseArray<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                UnitSectionDrill unitSectionDrill = new UnitSectionDrill();
                unitSectionDrill.setUnitSectionDrillId(cursor.getInt(cursor.getColumnIndex("_id")));
                unitSectionDrill.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("UnitSectionId")));
                unitSectionDrill.setDrillId(cursor.getInt(cursor.getColumnIndex("DrillId")));
                unitSectionDrill.setDrillSubId(cursor.getInt(cursor.getColumnIndex("DrillSubId")));
                unitSectionDrill.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                unitSectionDrill.setDrillOrder(cursor.getInt(cursor.getColumnIndex("DrillOrder")));
                unitSectionDrill.setDrillScore(cursor.getInt(cursor.getColumnIndex("DrillScore")));
                unitSectionDrill.setDrillScoreMax(cursor.getInt(cursor.getColumnIndex("DrillScoreMax")));
                unitSectionDrill.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
                unitSectionDrill.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
                unitSectionDrill.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
                unitSectionDrills.put(unitSectionDrill.getUnitSectionDrillId(), unitSectionDrill);
            }
        }
        cursor.close();
        return unitSectionDrills;
    }

    public SparseArray<UnitSectionDrill> getUnitSectionDrills(
            SQLiteDatabase db,
            int languageId) throws SQLiteException {

        SparseArray<UnitSectionDrill> unitSectionDrills = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "UnitSectionId, " +
                        "DrillId, " +
                        "DrillSubId, " +
                        "LanguageId, " +
                        "DrillOrder, " +
                        "DrillScore, " +
                        "DrillScoreMax, " +
                        "Unlocked, " +
                        "UnlockedDate, " +
                        "InProgress " +
                        "FROM tbl_UnitSectionDrill " +
                        "WHERE LanguageId = " + languageId + " " +
                        "ORDER BY _id ASC", null);

        if (cursor.getCount() > 0) {
            unitSectionDrills = new SparseArray<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                UnitSectionDrill unitSectionDrill = new UnitSectionDrill();
                unitSectionDrill.setUnitSectionDrillId(cursor.getInt(cursor.getColumnIndex("_id")));
                unitSectionDrill.setUnitSectionId(cursor.getInt(cursor.getColumnIndex("UnitSectionId")));
                unitSectionDrill.setDrillId(cursor.getInt(cursor.getColumnIndex("DrillId")));
                unitSectionDrill.setDrillSubId(cursor.getInt(cursor.getColumnIndex("DrillSubId")));
                unitSectionDrill.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                unitSectionDrill.setDrillOrder(cursor.getInt(cursor.getColumnIndex("DrillOrder")));
                unitSectionDrill.setDrillScore(cursor.getInt(cursor.getColumnIndex("DrillScore")));
                unitSectionDrill.setDrillScoreMax(cursor.getInt(cursor.getColumnIndex("DrillScoreMax")));
                unitSectionDrill.setUnlocked(cursor.getInt(cursor.getColumnIndex("Unlocked")));
                unitSectionDrill.setUnlockedDate(cursor.getString(cursor.getColumnIndex("UnlockedDate")));
                unitSectionDrill.setInProgress(cursor.getInt(cursor.getColumnIndex("InProgress")));
                unitSectionDrills.put(unitSectionDrill.getUnitSectionDrillId(), unitSectionDrill);
            }
        }
        cursor.close();
        return unitSectionDrills;
    }
}
package classact.com.clever_little_monkey.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import classact.com.clever_little_monkey.database.model.Unit;
/**
 * Created by Tseliso on 5/8/2016.
 */

public class UnitHelper {

    public static void clearInProgress(SQLiteDatabase db) throws SQLiteException {
        Cursor cursor = db.rawQuery(
                "UPDATE tbl_UnitControl " +
                        "SET UnitInProgress = 0, " +
                        "UnitSubIDInProgress = 0 " +
                        "WHERE UnitUnlocked = 1", null);
        cursor.moveToFirst();
        cursor.close();
    }

    public static int updateUnitInfo (SQLiteDatabase db, Unit unit) throws SQLiteException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NumberOfLanguageDrills", unit.getNumberOfLanguageDrills());
        contentValues.put("NumberOfMathDrills", unit.getNumberOfMathDrills());
        contentValues.put("UnitUnlocked", unit.getUnitUnlocked());
        contentValues.put("UnitDateLastPlayed", unit.getUnitDateLastPlayed());
        contentValues.put("UnitCompleted", unit.getUnitCompleted());
        contentValues.put("UnitInProgress", unit.getUnitInProgress());
        contentValues.put("UnitSubIDInProgress", unit.getUnitSubIDInProgress());
        contentValues.put("UnitFirstTime", unit.getUnitFirstTime());
        contentValues.put("UnitFirstTimeMovie", unit.getUnitFirstTimeMovie());
        contentValues.put("DrillLastPlayed", unit.getUnitDrillLastPlayed());
        int id = db.update("tbl_UnitControl", contentValues, "_id = ? ", new String[] { Integer.toString(unit.getUnitId()) } );
        return id;
    }

    public static Unit getUnitInfo (SQLiteDatabase db, int ID) throws SQLiteException {
        // this will return only one row
        Cursor unitInfo = db.rawQuery(
                "SELECT " +
                        "NumberOfLanguageDrills, " +
                        "NumberOfMathDrills, " +
                        "UnitUnlocked, " +
                        "UnitDateLastPlayed, " +
                        "UnitInProgress, " +
                        "UnitSubIDInProgress, " +
                        "UnitCompleted, " +
                        "DrillLastPlayed, " +
                        "UnitFirstTime, " +
                        "UnitFirstTimeMovie, " +
                        "UnitFirstTimeMovieFile " +
                "FROM tbl_UnitControl " +
                "WHERE _id = " + ID, null);
        unitInfo.moveToFirst();
        Unit unit = new Unit();
        unit.setUnitId(ID);
        unit.setNumberOfLanguageDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfLanguageDrills")));
        unit.setNumberOfMathDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfMathDrills")));
        unit.setUnitUnlocked(unitInfo.getInt(unitInfo.getColumnIndex("UnitUnlocked")));
        unit.setUnitDateLastPlayed(unitInfo.getString(unitInfo.getColumnIndex("UnitDateLastPlayed")));
        unit.setUnitInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitInProgress")));
        unit.setUnitSubIDInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitSubIDInProgress")));
        unit.setUnitCompleted(unitInfo.getInt(unitInfo.getColumnIndex("UnitCompleted")));
        unit.setUnitDrillLastPlayed(unitInfo.getInt(unitInfo.getColumnIndex("DrillLastPlayed")));
        unit.setUnitFirstTime(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTime")));
        unit.setUnitFirstTimeMovie(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTimeMovie")));
        unit.setUnitFirstTimeMovieFile(unitInfo.getString(unitInfo.getColumnIndex("UnitFirstTimeMovieFile")));
        unitInfo.close();
        return unit;
    }

    public static ArrayList<Unit> getAllUnits (SQLiteDatabase db) throws SQLiteException {

        ArrayList<Unit> unitInfoList = new ArrayList<Unit>();
        Cursor unitInfo = db.rawQuery("select * from tbl_UnitControl", null);
        for(unitInfo.moveToFirst(); !unitInfo.isAfterLast(); unitInfo.moveToNext()) {
            Unit unit = new Unit();
            unit.setNumberOfLanguageDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfLanguageDrills")));
            unit.setNumberOfMathDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfMathDrills")));
            unit.setUnitUnlocked(unitInfo.getInt(unitInfo.getColumnIndex("UnitUnlocked")));
            unit.setUnitDateLastPlayed(unitInfo.getString(unitInfo.getColumnIndex("UnitDateLastPlayed")));
            unit.setUnitInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitProgress")));
            unit.setUnitCompleted(unitInfo.getInt(unitInfo.getColumnIndex("UnitCompleted")));
            unit.setUnitFirstTime(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTime")));
            unit.setUnitFirstTimeMovie(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTimeMovie")));
            unit.setUnitFirstTimeMovieFile(unitInfo.getString(unitInfo.getColumnIndex("UnitFirstTimeMovieFile")));
            unit.setUnitInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitInProgress")));
            unit.setUnitSubIDInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitSubIDInProgress")));
            unit.setUnitDrillLastPlayed(unitInfo.getInt(unitInfo.getColumnIndex("DrillLastPlayed")));
            unitInfoList.add(unit);
        }
        unitInfo.close();
        return unitInfoList;
    }

    public static int getUnitToBePlayed (SQLiteDatabase db) throws SQLiteException {

        Cursor unitInfo = db.rawQuery(
                "SELECT " +
                        "_id " +
                "FROM tbl_UnitControl " +
                "WHERE UnitUnlocked = 1 " +
                "AND UnitInProgress = 1", null);
        int unitID = 0;
        try {
            if (unitInfo.getCount()>0) {
                unitInfo.moveToFirst();
                unitID = unitInfo.getInt(unitInfo.getColumnIndex("_id"));
            }
            return unitID;
        }finally {
            unitInfo.close();
        }
    }

    public static Unit getUnitInProgress (SQLiteDatabase db) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "NumberOfLanguageDrills, " +
                        "NumberOfMathDrills, " +
                        "UnitUnlocked, " +
                        "UnitDateLastPlayed, " +
                        "UnitInProgress, " +
                        "UnitSubIDInProgress, " +
                        "UnitCompleted, " +
                        "DrillLastPlayed, " +
                        "UnitFirstTime, " +
                        "UnitFirstTimeMovie, " +
                        "UnitFirstTimeMovieFile " +
                "FROM tbl_UnitControl " +
                "WHERE UnitUnlocked = 1 " +
                "AND UnitInProgress = 1 " +
                "ORDER BY _id ASC " +
                "LIMIT 1", null);

        Unit unit = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            unit = new Unit();
            unit.setUnitId(cursor.getInt(cursor.getColumnIndex("_id")));
            unit.setNumberOfLanguageDrills(cursor.getInt(cursor.getColumnIndex("NumberOfLanguageDrills")));
            unit.setNumberOfMathDrills(cursor.getInt(cursor.getColumnIndex("NumberOfMathDrills")));
            unit.setUnitUnlocked(cursor.getInt(cursor.getColumnIndex("UnitUnlocked")));
            unit.setUnitDateLastPlayed(cursor.getString(cursor.getColumnIndex("UnitDateLastPlayed")));
            unit.setUnitInProgress(cursor.getInt(cursor.getColumnIndex("UnitInProgress")));
            unit.setUnitSubIDInProgress(cursor.getInt(cursor.getColumnIndex("UnitSubIDInProgress")));
            unit.setUnitCompleted(cursor.getInt(cursor.getColumnIndex("UnitCompleted")));
            unit.setUnitDrillLastPlayed(cursor.getInt(cursor.getColumnIndex("DrillLastPlayed")));
            unit.setUnitFirstTime(cursor.getInt(cursor.getColumnIndex("UnitFirstTime")));
            unit.setUnitFirstTimeMovie(cursor.getInt(cursor.getColumnIndex("UnitFirstTimeMovie")));
            unit.setUnitFirstTimeMovieFile(cursor.getString(cursor.getColumnIndex("UnitFirstTimeMovieFile")));
            cursor.close();
        }
        return unit;
    }

    public static LinkedHashMap<Integer, Unit> getUnits(SQLiteDatabase db) throws SQLiteException {

        LinkedHashMap<Integer, Unit> units = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "NumberOfLanguageDrills, " +
                        "NumberOfMathDrills, " +
                        "UnitUnlocked, " +
                        "UnitDateLastPlayed, " +
                        "UnitInProgress, " +
                        "UnitSubIDInProgress, " +
                        "UnitCompleted, " +
                        "DrillLastPlayed, " +
                        "UnitFirstTime, " +
                        "UnitFirstTimeMovie, " +
                        "UnitFirstTimeMovieFile " +
                "FROM tbl_UnitControl " +
                "ORDER BY _id ASC", null);

        if (cursor.getCount() > 0) {
            units = new LinkedHashMap<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Unit unit = new Unit();
                unit.setUnitId(cursor.getInt(cursor.getColumnIndex("_id")));
                unit.setNumberOfLanguageDrills(cursor.getInt(cursor.getColumnIndex("NumberOfLanguageDrills")));
                unit.setNumberOfMathDrills(cursor.getInt(cursor.getColumnIndex("NumberOfMathDrills")));
                unit.setUnitUnlocked(cursor.getInt(cursor.getColumnIndex("UnitUnlocked")));
                unit.setUnitDateLastPlayed(cursor.getString(cursor.getColumnIndex("UnitDateLastPlayed")));
                unit.setUnitInProgress(cursor.getInt(cursor.getColumnIndex("UnitInProgress")));
                unit.setUnitSubIDInProgress(cursor.getInt(cursor.getColumnIndex("UnitSubIDInProgress")));
                unit.setUnitCompleted(cursor.getInt(cursor.getColumnIndex("UnitCompleted")));
                unit.setUnitDrillLastPlayed(cursor.getInt(cursor.getColumnIndex("DrillLastPlayed")));
                unit.setUnitFirstTime(cursor.getInt(cursor.getColumnIndex("UnitFirstTime")));
                unit.setUnitFirstTimeMovie(cursor.getInt(cursor.getColumnIndex("UnitFirstTimeMovie")));
                unit.setUnitFirstTimeMovieFile(cursor.getString(cursor.getColumnIndex("UnitFirstTimeMovieFile")));
                units.put(unit.getUnitId(), unit);
            }
            cursor.close();
        }
        return units;
    }
}

package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.database.model.Unit;
/**
 * Created by Tseliso on 5/8/2016.
 */

public class UnitHelper {
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
        Cursor unitInfo = db.rawQuery("select NumberOfLanguageDrills, NumberOfMathDrills, UnitUnlocked, UnitDateLastPlayed, UnitCompleted, UnitFirstTime, UnitFirstTimeMovie, UnitFirstTimeMovieFile, " +
                "            UnitInProgress, UnitSubIDInProgress, DrillLastPlayed from tbl_UnitControl where _id="+ID+"", null);
        unitInfo.moveToFirst();
        Unit unit = new Unit();
        unit.setUnitId(ID);
        unit.setNumberOfLanguageDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfLanguageDrills")));
        unit.setNumberOfMathDrills(unitInfo.getInt(unitInfo.getColumnIndex("NumberOfMathDrills")));
        unit.setUnitUnlocked(unitInfo.getInt(unitInfo.getColumnIndex("UnitUnlocked")));
        unit.setUnitDateLastPlayed(unitInfo.getString(unitInfo.getColumnIndex("UnitDateLastPlayed")));
        unit.setUnitCompleted(unitInfo.getInt(unitInfo.getColumnIndex("UnitCompleted")));
        unit.setUnitFirstTime(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTime")));
        unit.setUnitFirstTimeMovie(unitInfo.getInt(unitInfo.getColumnIndex("UnitFirstTimeMovie")));
        unit.setUnitFirstTimeMovieFile(unitInfo.getString(unitInfo.getColumnIndex("UnitFirstTimeMovieFile")));
        unit.setUnitInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitInProgress")));
        unit.setUnitSubIDInProgress(unitInfo.getInt(unitInfo.getColumnIndex("UnitSubIDInProgress")));
        unit.setUnitDrillLastPlayed(unitInfo.getInt(unitInfo.getColumnIndex("DrillLastPlayed")));
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

        Cursor unitInfo = db.rawQuery("select _id from tbl_UnitControl where UnitInProgress = 1 and UnitCompleted = 0", null);
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


}

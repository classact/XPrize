package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.LinkedHashMap;

import classact.com.xprize.database.model.Drill;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class DrillHelper {

    public static int updateDrill(SQLiteDatabase db, Drill drill) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("DrillTypeId", drill.getDrillTypeId());
        int id = db.update("tbl_Drill", contentValues, "_id = ? ",
                new String[] { Integer.toString(drill.getDrillId()) });
        return id;
    }

    public static Drill getDrill(SQLiteDatabase db, int id) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "DrillTypeId " +
                        "FROM tbl_Drill " +
                        "WHERE _id = " + id + "", null);

        Drill drill = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            drill = new Drill();
            drill.setDrillId(id);
            drill.setDrillTypeId(cursor.getInt(cursor.getColumnIndex("DrillTypeId")));
            cursor.close();
        }
        return drill;
    }

    public static LinkedHashMap<Integer, Drill> getDrills(SQLiteDatabase db) throws SQLiteException {

        LinkedHashMap<Integer, Drill> drills = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "DrillTypeId " +
                        "FROM tbl_Drill", null);

        if (cursor.getCount() > 0) {
            drills = new LinkedHashMap<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Drill drill = new Drill();
                drill.setDrillId(cursor.getInt(cursor.getColumnIndex("_id")));
                drill.setDrillTypeId(cursor.getInt(cursor.getColumnIndex("DrillTypeId")));
                drills.put(drill.getDrillId(), drill);
            }
            cursor.close();
        }
        return drills;
    }
}

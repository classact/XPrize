package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.SparseArray;

import javax.inject.Inject;

import classact.com.xprize.database.model.Drill;

/**
 * Created by hcdjeong on 2017/07/24.
 * Helper for {@link Drill}
 */

public class DrillHelper {

    @Inject
    public DrillHelper() {

    }

    public int updateDrill(SQLiteDatabase db, Drill drill) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("DrillTypeId", drill.getDrillTypeId());
        int id = db.update("tbl_Drill", contentValues, "_id = ? ",
                new String[] { Integer.toString(drill.getDrillId()) });
        return id;
    }

    public Drill getDrill(SQLiteDatabase db, int id) throws SQLiteException {

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

    public SparseArray<Drill> getDrills(SQLiteDatabase db) throws SQLiteException {

        SparseArray<Drill> drills = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "DrillTypeId " +
                        "FROM tbl_Drill", null);

        if (cursor.getCount() > 0) {
            drills = new SparseArray<>();
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

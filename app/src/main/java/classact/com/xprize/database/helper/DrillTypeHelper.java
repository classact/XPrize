package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.LinkedHashMap;

import classact.com.xprize.database.model.DrillType;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class DrillTypeHelper {

    public static int updateDrillType(SQLiteDatabase db, DrillType drillType) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", drillType.getName());
        contentValues.put("LanguageId", drillType.getLanguageId());
        int id = db.update("tbl_DrillType", contentValues, "_id = ? ",
                new String[] { Integer.toString(drillType.getDrillTypeId()) });
        return id;
    }

    public static DrillType getDrillType(SQLiteDatabase db, int id) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "Name, " +
                        "LanguageId " +
                        "FROM tbl_DrillType " +
                        "WHERE _id = " + id + "", null);

        DrillType drillType = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            drillType = new DrillType();
            drillType.setDrillTypeId(id);
            drillType.setName(cursor.getString(cursor.getColumnIndex("Name")));
            drillType.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            cursor.close();
        }
        return drillType;
    }

    public static LinkedHashMap<Integer, DrillType> getDrillTypes(SQLiteDatabase db, int languageId) throws SQLiteException {

        LinkedHashMap<Integer, DrillType> drillTypes = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "Name, " +
                        "LanguageId " +
                        "FROM tbl_DrillType " +
                        "WHERE LanguageId = " + languageId, null);

        if (cursor.getCount() > 0) {
            drillTypes = new LinkedHashMap<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                DrillType drillType = new DrillType();
                drillType.setDrillTypeId(cursor.getInt(cursor.getColumnIndex("_id")));
                drillType.setName(cursor.getString(cursor.getColumnIndex("Name")));
                drillType.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                drillTypes.put(drillType.getDrillTypeId(), drillType);
            }
            cursor.close();
        }
        return drillTypes;
    }
}

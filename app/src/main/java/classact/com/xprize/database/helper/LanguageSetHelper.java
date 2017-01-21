package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Tseliso on 5/8/2016.
 */
public class LanguageSetHelper {
    public static int getLanguageSet(SQLiteDatabase db){
        int _id = 1;
        String[] columns = new String[] {"LanguageInd"};
        Cursor cursor = db.query("tbl_LanguageSet", columns," _id=?", new String[]{String.valueOf(_id)}, null, null, null);
        int languageSet = 0;
        try {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                languageSet = cursor.getInt(0);
            }
            return languageSet;
        }finally {
            cursor.close();
        }
    }

    public static int updateLanguageSet (SQLiteDatabase db, int languageInd) throws SQLiteException {
        ContentValues contentValues = new ContentValues();
        int _id = 1;
        contentValues.put("LanguageInd", languageInd);
        int id = db.update("tbl_LanguageSet", contentValues, "_id = ? ", new String[] { Integer.toString(_id) } );
        return languageInd;
    }
}


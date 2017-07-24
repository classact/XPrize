package classact.com.xprize.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.LinkedHashMap;

import classact.com.xprize.database.model.Section;

/**
 * Created by hcdjeong on 2017/07/24.
 */

public class SectionHelper {

    public static int updateSection(SQLiteDatabase db, Section section) throws SQLiteException {

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", section.getName());
        contentValues.put("LanguageId", section.getLanguageId());
        int id = db.update("tbl_Section", contentValues, "_id = ? ",
                new String[] { Integer.toString(section.getSectionId()) });
        return id;
    }

    public static Section getSection(SQLiteDatabase db, int id) throws SQLiteException {

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "Name, " +
                        "LanguageId " +
                        "FROM tbl_Section " +
                        "WHERE _id = " + id, null);

        Section section = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            section = new Section();
            section.setSectionId(id);
            section.setName(cursor.getString(cursor.getColumnIndex("Name")));
            section.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
            cursor.close();
        }
        return section;
    }

    public static LinkedHashMap<Integer, Section> getSections(SQLiteDatabase db, int languageId) throws SQLiteException {

        LinkedHashMap<Integer, Section> sections = null;

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        "_id, " +
                        "Name, " +
                        "LanguageId " +
                        "FROM tbl_Section " +
                        "WHERE LanguageId = " + languageId, null);

        if (cursor.getCount() > 0) {
            sections = new LinkedHashMap<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Section section = new Section();
                section.setSectionId(cursor.getInt(cursor.getColumnIndex("_id")));
                section.setName(cursor.getString(cursor.getColumnIndex("Name")));
                section.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageId")));
                sections.put(section.getSectionId(), section);
            }
            cursor.close();
        }
        return sections;
    }
}

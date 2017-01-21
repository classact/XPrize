package classact.com.xprize.database.helper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import classact.com.xprize.database.model.LetterSequence;

public class LetterSequenceHelper {
    public static ArrayList<LetterSequence> getLetterSequence(SQLiteDatabase db, int languageID, int unitId, int subId) {
        ArrayList<LetterSequence> letterSequences = new ArrayList<>();
        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "UnitSubID", "letterID"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_lettersequence", columns, "LanguageID=? AND UnitID=? AND UnitSubID=?", new String[]{String.valueOf(languageID), String.valueOf(unitId), String.valueOf(subId)}, null, null, OrderBy);
        LetterSequence sequence = new LetterSequence();
        if (cursor.moveToFirst()) {
            do {
                sequence = new LetterSequence();
                sequence.setLanguageID(cursor.getInt(0));
                sequence.setSequenceID(cursor.getInt(1));
                sequence.setUnitID(cursor.getInt(2));
                sequence.setSubUnitID(cursor.getInt(3));
                sequence.setLetterID(cursor.getInt(4));
                letterSequences.add(sequence);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return letterSequences;
    }

    public static int getLetterID (SQLiteDatabase db, int languageID, int unitId, int subId)  throws SQLiteException {
        String[] columns = new String[]{"LetterID"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_LetterSequence", columns, "LanguageID=? AND UnitID=? AND UnitSubID=?", new String[]{String.valueOf(languageID), String.valueOf(unitId), String.valueOf(subId)}, null, null, OrderBy);
        int letterID = 0;
        try {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                letterID = cursor.getInt(cursor.getColumnIndex("LetterID"));
            }
            return letterID;
        }finally {
            cursor.close();
        }

    }

    public static ArrayList getWrongLetters(SQLiteDatabase db, int languageID, int unitId, int limit){
        ArrayList letters = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT LetterID FROM tbl_LetterSequence where LanguageID = "+languageID+" and UnitID <> " + unitId + " ORDER BY RANDOM() LIMIT " + limit +";", null);
        //LetterSequence letterSequence = new LetterSequence();
        int letterSequence = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //letterSequence = new LetterSequence();
                    letterSequence = cursor.getInt(0);
                    letters.add(letterSequence);
                } while (cursor.moveToNext());
            }
            return letters;
        }finally {
            cursor.close();
        }
    }
}

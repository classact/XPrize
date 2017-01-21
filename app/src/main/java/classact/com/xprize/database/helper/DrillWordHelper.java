package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DrillWordHelper {
    public static int getDrillWord(SQLiteDatabase db, int languageID, int unitId, int subId, int drillId, int wordType){
        Cursor drillWords = db.rawQuery("SELECT WordID FROM tbl_DrillWords where LanguageID = "+languageID+" and WordType = " + wordType + " and UnitID = " + unitId + " and SubID = " + subId + " and DrillID = " + drillId + " ORDER BY RANDOM() LIMIT 1;", null);
        int wordID = 0;
        try {
            if (drillWords.getCount()>0) {
                drillWords.moveToFirst();
                wordID = drillWords.getInt(drillWords.getColumnIndex("WordID"));
            }
            return wordID;
        }finally {
            drillWords.close();
        }
    }

    public static ArrayList getDrillWords(SQLiteDatabase db, int languageID, int unitId, int subId, int drillId, int wordType, int limit){
        ArrayList drillWords = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT WordID FROM tbl_DrillWords where LanguageID = "+languageID+" and WordType = " + wordType + " and UnitID = " + unitId + " and SubID = " + subId + " and DrillID = " + drillId + " ORDER BY RANDOM() LIMIT " + limit +";", null);
        //DrillWords drillWord = new DrillWords();
        int drillWord=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    drillWord = cursor.getInt(0);
                    //drillWord.setDrillID(cursor.getInt(0));
                    drillWords.add(drillWord);
                } while (cursor.moveToNext());
            }
            return drillWords;
        }finally {
            cursor.close();
        }
    }

    public static int getWrongDrillWord(SQLiteDatabase db, int languageID, int unitId, int subId, int drillId, int wordType){
        Cursor drillWords = db.rawQuery("SELECT WordID FROM tbl_DrillWords where LanguageID = "+languageID+" and WordType = " + wordType + " and UnitID <> " + unitId + " ORDER BY RANDOM() LIMIT 1;", null);
        int wordID = 0;
        try {
            if (drillWords.getCount()>0) {
                drillWords.moveToFirst();
                wordID = drillWords.getInt(drillWords.getColumnIndex("WordID"));
            }
            return wordID;
        }finally {
            drillWords.close();
        }
    }

    public static ArrayList getWrongDrillWords(SQLiteDatabase db, int languageID, int unitId, int subId, int drillId, int wordType, int limit){
        ArrayList drillWords = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT WordID FROM tbl_DrillWords where LanguageID = "+languageID+" and WordType = " + wordType + " and UnitID <> " + unitId + " and SubID = "+ subId + " ORDER BY RANDOM() LIMIT " + limit +";", null);
        int drillWord=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    drillWord = cursor.getInt(0);
                    drillWords.add(drillWord);
                } while (cursor.moveToNext());
            }
            return drillWords;
        }finally {
            cursor.close();
        }
    }

}

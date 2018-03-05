package classact.com.clever_little_monkey.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.clever_little_monkey.database.model.SimpleStoryWord;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoryWordHelper {
    public static SimpleStoryWord getSimpleStoryWord(SQLiteDatabase db, int simpleStoryWordId) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "SentenceID", "SentenceNo", "WordNo", "BlackWord", "RedWord", "Sound", "SentenceSetNo"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SimpleStoryWords", columns, "_id=?", new String[]{String.valueOf(simpleStoryWordId)}, null, null, OrderBy);
        SimpleStoryWord simpleStoryWord = new SimpleStoryWord();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            simpleStoryWord = new SimpleStoryWord();
            simpleStoryWord.setSimpleStoryWordID(cursor.getInt(0));
            simpleStoryWord.setLanguageID(cursor.getInt(1));
            simpleStoryWord.setUnitID(cursor.getInt(2));
            simpleStoryWord.setSentenceID(cursor.getInt(3));
            simpleStoryWord.setSentenceNo(cursor.getInt(4));
            simpleStoryWord.setWordNo(cursor.getInt(5));
            simpleStoryWord.setBlackWord(cursor.getString(6));
            simpleStoryWord.setRedWord(cursor.getString(7));
            simpleStoryWord.setSound(cursor.getString(8));
            simpleStoryWord.setSentenceSetNo(cursor.getInt(9));
        }
        cursor.close();
        return simpleStoryWord;
    }

    public static ArrayList<Integer> getSimpleStoryWordIds(SQLiteDatabase db, int sentenceID){
        ArrayList<Integer> simpleStoryWordIds = new ArrayList<>();
        Cursor cursor = db.rawQuery("select _id from tbl_SimpleStoryWords where SentenceID = " + sentenceID+ ";", null);
        //DrillWords drillWord = new DrillWords();
        int simpleStoryWordId = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    simpleStoryWordId = cursor.getInt(0);
                    //drillWord.setDrillID(cursor.getInt(0));
                    simpleStoryWordIds.add(simpleStoryWordId);
                } while (cursor.moveToNext());
            }
            return simpleStoryWordIds;
        }finally {
            cursor.close();
        }
    }
}

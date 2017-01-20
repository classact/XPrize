package classact.com.xprize.database.helpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import classact.com.xprize.model.SimpleStories;
import classact.com.xprize.model.SimpleStoryWords;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoryWordHelper {
    public static SimpleStoryWords getSentenceWord(SQLiteDatabase db, int sentenceWordID) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "SentenceID", "SentenceNo", "WordNo", "BlackWprd", "RedWord", "Sound"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SentenceWords", columns, "_id=?", new String[]{String.valueOf(sentenceWordID)}, null, null, OrderBy);
        SimpleStoryWords sentenceWord = new SimpleStoryWords();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            sentenceWord = new SimpleStoryWords();
            sentenceWord.setSentenceID(cursor.getInt(0));
            sentenceWord.setLanguageID(cursor.getInt(1));
            sentenceWord.setUnitID(cursor.getInt(2));
            sentenceWord.setSentenceID(cursor.getInt(3));
            sentenceWord.setSentenceNo(cursor.getInt(4));
            sentenceWord.setWordNo(cursor.getInt(5));
            sentenceWord.setBlackWord(cursor.getString(6));
            sentenceWord.setRedWord(cursor.getString(7));
            sentenceWord.setWordSound(cursor.getString(8));
        }
        cursor.close();
        return sentenceWord;
    }

    public static ArrayList getSentenceWords(SQLiteDatabase db, int sentenceID){
        ArrayList sentences = new ArrayList();
        Cursor cursor = db.rawQuery("select _id from tbl_SentenceWords where SentenceID = " + sentenceID+ ";", null);
        //DrillWords drillWord = new DrillWords();
        int sentence=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    sentence = cursor.getInt(0);
                    //drillWord.setDrillID(cursor.getInt(0));
                    sentences.add(sentence);
                } while (cursor.moveToNext());
            }
            return sentences;
        }finally {
            cursor.close();
        }
    }
}

package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.xprize.database.model.SimpleStories;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoriesHelper {
    public static SimpleStories getSentence(SQLiteDatabase db, int sentenceID ) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "DrillID", "SentenceNo", "SentenceSoundFile"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SimpleStories", columns, "_id=?", new String[]{String.valueOf(sentenceID)}, null, null, null);
        SimpleStories sentence = new SimpleStories();
        if (cursor.moveToFirst()) {
            do {
                sentence = new SimpleStories();
                sentence.setSentenceID(cursor.getInt(0));
                sentence.setLanguageID(cursor.getInt(1));
                sentence.setUnitID(cursor.getInt(2));
                sentence.setDrillID(cursor.getInt(3));
                sentence.setSentenceNo(cursor.getInt(4));
                sentence.setSentenceSoundFile(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sentence;
    }

    public static ArrayList<SimpleStories> getSentenceAll(SQLiteDatabase db, int languageID, int unitID, int drillID) {
        ArrayList<SimpleStories> sentences = new ArrayList<>();
        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "DrillID", "SentenceNo", "SentenceSoundFile"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SimpleStories", columns, "LanguageID=? AND UnitID=? and DrillID=?", new String[]{String.valueOf(languageID), String.valueOf(unitID), String.valueOf(drillID)}, null, null, OrderBy);
        SimpleStories sentence = new SimpleStories();
        if (cursor.moveToFirst()) {
            do {
                sentence = new SimpleStories();
                sentence.setSentenceID(cursor.getInt(0));
                sentence.setLanguageID(cursor.getInt(1));
                sentence.setUnitID(cursor.getInt(2));
                sentence.setDrillID(cursor.getInt(3));
                sentence.setSentenceNo(cursor.getInt(4));
                sentence.setSentenceSoundFile(cursor.getString(5));
                sentences.add(sentence);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sentences;
    }

    public static ArrayList<Integer> getSentences(SQLiteDatabase db, int languageID, int unitId){
        ArrayList<Integer> sentences = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("select _id from tbl_SimpleStories where LanguageID = "+languageID+" and UnitID = " + unitId + ";", null);
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

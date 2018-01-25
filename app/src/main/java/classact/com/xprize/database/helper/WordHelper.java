package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import classact.com.xprize.database.model.Letter;
import classact.com.xprize.database.model.Word;

/**
 * Created by Tseliso on 5/10/2016.
 */
public class WordHelper {
    public static ArrayList<Word> getWords(SQLiteDatabase db){
        ArrayList<Word> words = new ArrayList<>();
        String[] columns = new String[] {"_id", "LanguageInd","WordName", "WordPicture", "ImagePicture", "WordSlowSound",
                "WordSound","English"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_word", columns, null, null, null, null, OrderBy);
        Word word = null;
            if (cursor.moveToFirst()) {
            do {
                word = new Word();
                word.setWordID(cursor.getInt(0));
                word.setLanguageID(cursor.getInt(1));
                word.setWordName(cursor.getString(2));
                word.setWordPictureURI(cursor.getString(3));
                word.setImagePictureURI(cursor.getString(4));
                word.setWordSlowSoundURI(cursor.getString(5));
                word.setWordSoundURI(cursor.getString(6));
                word.setEnglish(cursor.getString(7));
                words.add(word);
            } while (cursor.moveToNext());
        }
            cursor.close();
            return words;
    }

    public static List<Word> getUnitWords(SQLiteDatabase db, int languageId, int unitId, int subId, int wordType, int limit) {
        List<Word> words = null;
        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT w.* " +
                "FROM tbl_DrillWords dw " +
                "INNER JOIN tbl_Word w " +
                "ON w._id = dw.WordID " +
                "AND dw.LanguageId = " + languageId + " " +
                "AND dw.UnitId = " + unitId + " " +
                "AND dw.DrillId IN (1, 2, 3, 4, 5, 6, 7, 8, 9) " +
                "AND dw.SubId = " + subId + " " +
                "AND w.WordType = " + wordType + " " +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

            if (cursor.getCount() > 0) {
            words = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Word word = new Word();
                word.setWordID(cursor.getInt(cursor.getColumnIndex("_id")));
                word.setLanguageID(cursor.getInt(cursor.getColumnIndex("LanguageInd")));
                word.setWordName(cursor.getString(cursor.getColumnIndex("WordName")));
                word.setWordType(cursor.getInt(cursor.getColumnIndex("WordType")));
                word.setWordPictureURI(cursor.getString(cursor.getColumnIndex("WordPicture")));
                word.setImagePictureURI(cursor.getString(cursor.getColumnIndex("ImagePicture")));
                word.setWordSlowSoundURI(cursor.getString(cursor.getColumnIndex("WordSlowSound")));
                word.setWordSoundURI(cursor.getString(cursor.getColumnIndex("WordSound")));
                word.setEnglish(cursor.getString(cursor.getColumnIndex("English")));
                word.setIsPlural(cursor.getInt(cursor.getColumnIndex("IsPlural")));
                word.setIsVowel(cursor.getInt(cursor.getColumnIndex("IsVowel")));
                words.add(word);
            }
        }
        cursor.close();
        return words;
    }

    public static List<Word> getAntiUnitWords(SQLiteDatabase db, int languageId, int unitId, int subId, int wordType, int limit) {
        List<Word> words = null;
        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT tw.* " +
                "FROM tbl_Word tw " +
                "WHERE tw.LanguageInd = " + languageId + " " +
                "AND tw.WordType = " + wordType + " " +
                "AND tw._id NOT IN " +
                "(SELECT DISTINCT w._id " +
                "FROM tbl_DrillWords dw " +
                "INNER JOIN tbl_Word w " +
                "ON w._id = dw.WordID " +
                "AND dw.LanguageId = " + languageId + " " +
                "AND w.WordType = " + wordType + " " +
                "AND dw.UnitId = " + unitId + " " +
                "AND dw.DrillId IN (1, 2, 3, 4, 5, 6, 7, 8, 9)) " +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

        if (cursor.getCount() > 0) {
            words = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Word word = new Word();
                word.setWordID(cursor.getInt(cursor.getColumnIndex("_id")));
                word.setLanguageID(cursor.getInt(cursor.getColumnIndex("LanguageInd")));
                word.setWordName(cursor.getString(cursor.getColumnIndex("WordName")));
                word.setWordType(cursor.getInt(cursor.getColumnIndex("WordType")));
                word.setWordPictureURI(cursor.getString(cursor.getColumnIndex("WordPicture")));
                word.setImagePictureURI(cursor.getString(cursor.getColumnIndex("ImagePicture")));
                word.setWordSlowSoundURI(cursor.getString(cursor.getColumnIndex("WordSlowSound")));
                word.setWordSoundURI(cursor.getString(cursor.getColumnIndex("WordSound")));
                word.setEnglish(cursor.getString(cursor.getColumnIndex("English")));
                word.setIsPlural(cursor.getInt(cursor.getColumnIndex("IsPlural")));
                word.setIsVowel(cursor.getInt(cursor.getColumnIndex("IsVowel")));
                words.add(word);
            }
        }
        cursor.close();
        return words;
    }

    public static List<Word> getAntiUnitWords(SQLiteDatabase db, int languageId, int unitId, int subId, int wordType, String letterName, int limit) {
        List<Word> words = null;
        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT tw.* " +
                "FROM tbl_Word tw " +
                "WHERE tw.LanguageInd = " + languageId + " " +
                "AND tw.WordType = " + wordType + " " +
                "AND substr(tw.WordName, 0, 2) <> '" + letterName + "' " +
                "AND tw._id NOT IN " +
                "(SELECT DISTINCT w._id " +
                "FROM tbl_DrillWords dw " +
                "INNER JOIN tbl_Word w " +
                "ON w._id = dw.WordID " +
                "AND dw.LanguageId = " + languageId + " " +
                "AND w.WordType = " + wordType + " " +
                "AND dw.UnitId = " + unitId + " " +
                "AND dw.DrillId IN (1, 2, 3, 4, 5, 6, 7, 8, 9)) " +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

        if (cursor.getCount() > 0) {
            words = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Word word = new Word();
                word.setWordID(cursor.getInt(cursor.getColumnIndex("_id")));
                word.setLanguageID(cursor.getInt(cursor.getColumnIndex("LanguageInd")));
                word.setWordName(cursor.getString(cursor.getColumnIndex("WordName")));
                word.setWordType(cursor.getInt(cursor.getColumnIndex("WordType")));
                word.setWordPictureURI(cursor.getString(cursor.getColumnIndex("WordPicture")));
                word.setImagePictureURI(cursor.getString(cursor.getColumnIndex("ImagePicture")));
                word.setWordSlowSoundURI(cursor.getString(cursor.getColumnIndex("WordSlowSound")));
                word.setWordSoundURI(cursor.getString(cursor.getColumnIndex("WordSound")));
                word.setEnglish(cursor.getString(cursor.getColumnIndex("English")));
                word.setIsPlural(cursor.getInt(cursor.getColumnIndex("IsPlural")));
                word.setIsVowel(cursor.getInt(cursor.getColumnIndex("IsVowel")));
                words.add(word);
            }
        }
        cursor.close();
        return words;
    }

    public static Word getWord(SQLiteDatabase db,int wordId){

        String[] columns = new String[] {"_id", "LanguageInd","WordName", "WordPicture", "ImagePicture", "WordSlowSound",
                "WordSound","English"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Word", columns, "_id=?", new String[]{String.valueOf(wordId)}, null, null, OrderBy);
        Word word = new Word();
        if (cursor.moveToFirst()) {
            do {
                word = new Word();
                word.setWordID(cursor.getInt(cursor.getColumnIndex("_id")));
                word.setLanguageID(cursor.getInt(cursor.getColumnIndex("LanguageInd")));
                word.setWordName(cursor.getString(cursor.getColumnIndex("WordName")));
                word.setWordPictureURI(cursor.getString(cursor.getColumnIndex("WordPicture")));
                word.setImagePictureURI(cursor.getString(cursor.getColumnIndex("ImagePicture")));
                word.setWordSlowSoundURI(cursor.getString(cursor.getColumnIndex("WordSlowSound")));
                word.setWordSoundURI(cursor.getString(cursor.getColumnIndex("WordSound")));
                word.setEnglish(cursor.getString(cursor.getColumnIndex("English")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return word;
    }
}

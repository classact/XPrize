package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import classact.com.xprize.database.model.Letter;

/**
 * Created by Tseliso on 5/10/2016.
 */
public class LetterHelper {
    public static ArrayList<Letter> getLetters(SQLiteDatabase db, int languageId, int letterId){
        ArrayList<Letter> letters = new ArrayList<>();
        String[] columns = new String[] {"_id","LanguageID","letterName"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_letter", columns, "_id=? and LanguageID=?", new String[]{String.valueOf(letterId),String.valueOf(languageId)}, null, null, OrderBy);
        Letter letter = null;
        if (cursor.moveToFirst()) {
            do {
                letter = new Letter();
                letter.setLetterId(cursor.getInt(0));
                letter.setLanguageId(cursor.getInt(1));
                letter.setLetterName(cursor.getString(2));
                letters.add(letter);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return letters;
    }

    public static Letter getLetter(SQLiteDatabase db,int languageId, int letterId){
        String[] columns = new String[] {"_id","LanguageID","LetterName","LetterPictureLowerCaseBlack",
                "LetterPictureLowerCaseBlue","LetterPictureUpperCaseBlack","LetterPictureUpperCaseRed",
                "LetterPictureUpperCaseDots","LetterPictureLowerCaseDots","LetterSound", "PhonicSound", "LetterLowerPath", "LetterUpperPath"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Letter", columns, " _id=? AND LanguageID=?", new String[]{String.valueOf(letterId), String.valueOf(languageId)}, null, null, OrderBy);
        Letter letter = new Letter();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            letter.setLetterId(cursor.getInt(0));
            letter.setLanguageId(cursor.getInt(1));
            letter.setLetterName(cursor.getString(2));
            letter.setLetterPictureLowerCaseBlackURI(cursor.getString(3));
            letter.setLetterPictureLowerCaseBlueURI(cursor.getString(4));
            letter.setLetterPictureUpperCaseBlackURI(cursor.getString(5));
            letter.setLetterPictureUpperCaseRedURI(cursor.getString(6));
            letter.setLetterPictureUpperCaseDotsURI(cursor.getString(7));
            letter.setLetterPictureLowerCaseDotsURI(cursor.getString(8));
            letter.setLetterSoundURI(cursor.getString(9));
            letter.setPhonicSoundURI(cursor.getString(10));
            letter.setLetterLowerPath(cursor.getString(11));
            letter.setLetterUpperPath(cursor.getString(12));
        }
        cursor.close();
        return letter;
    }

    public static Letter getLetterByName(SQLiteDatabase db,int languageId, String letterName){
        String[] columns = new String[] {"_id","LanguageID","LetterName","LetterPictureLowerCaseBlack",
                "LetterPictureLowerCaseBlue","LetterPictureUpperCaseBlack","LetterPictureUpperCaseRed",
                "LetterPictureUpperCaseDots","LetterPictureLowerCaseDots","LetterSound", "PhonicSound", "LetterLowerPath", "LetterUpperPath"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Letter", columns, " LetterName=? AND LanguageID=?", new String[]{String.valueOf(letterName), String.valueOf(languageId)}, null, null, OrderBy);
        Letter letter = new Letter();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            letter.setLetterId(cursor.getInt(0));
            letter.setLanguageId(cursor.getInt(1));
            letter.setLetterName(cursor.getString(2));
            letter.setLetterPictureLowerCaseBlackURI(cursor.getString(3));
            letter.setLetterPictureLowerCaseBlueURI(cursor.getString(4));
            letter.setLetterPictureUpperCaseBlackURI(cursor.getString(5));
            letter.setLetterPictureUpperCaseRedURI(cursor.getString(6));
            letter.setLetterPictureUpperCaseDotsURI(cursor.getString(7));
            letter.setLetterPictureLowerCaseDotsURI(cursor.getString(8));
            letter.setLetterSoundURI(cursor.getString(9));
            letter.setPhonicSoundURI(cursor.getString(10));
            letter.setLetterLowerPath(cursor.getString(11));
            letter.setLetterUpperPath(cursor.getString(12));
        }
        cursor.close();
        return letter;
    }

    public static ArrayList getWrongLetters(SQLiteDatabase db, int languageID, int letterID, int limit){
        ArrayList letters = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Letter where LanguageID = "+languageID+" and _id <> " + letterID + " ORDER BY RANDOM() LIMIT " + limit +";", null);
        int letter=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    letter = cursor.getInt(0);
                    letters.add(letter);
                } while (cursor.moveToNext());
            }
            return letters;
        }finally {
            cursor.close();
        }
    }

    public static ArrayList<Integer> getWrongSingleLetters(SQLiteDatabase db, int languageID, int letterID, int limit){
        ArrayList<Integer> letters = new ArrayList<>();
        Cursor cursor = db.rawQuery("" +
                "SELECT _id FROM tbl_Letter " +
                "where LanguageID = "+languageID+" " +
                "and _id <> " + letterID + " " +
                "and length(letterName) = 1 " +
                "ORDER BY RANDOM() LIMIT " + limit +";", null);
        int letter=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    letter = cursor.getInt(0);
                    letters.add(letter);
                } while (cursor.moveToNext());
            }
            return letters;
        }finally {
            cursor.close();
        }
    }
}

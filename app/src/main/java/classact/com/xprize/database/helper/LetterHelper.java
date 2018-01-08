package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import classact.com.xprize.database.model.Letter;

/**
 * Created by Tseliso on 5/10/2016.
 * Helper for {@link Letter}
 */

public class LetterHelper {

    @Inject
    public LetterHelper() {

    }

    public List<Letter> getLetters(SQLiteDatabase db, int languageId, int letterId){
        List<Letter> letters = null;
        String[] columns = new String[] {"_id","LanguageID","letterName"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_letter", columns, "_id=? and LanguageID=?", new String[]{String.valueOf(letterId),String.valueOf(languageId)}, null, null, OrderBy);
        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Letter letter = new Letter();
                letter.setLetterId(cursor.getInt(0));
                letter.setLanguageId(cursor.getInt(1));
                letter.setLetterName(cursor.getString(2));
                letter.setIsLetter(cursor.getInt(3));
                letters.add(letter);
            }
        }
        cursor.close();
        return letters;
    }

    public Letter getLetter(SQLiteDatabase db,int languageId, int letterId){
        String[] columns = new String[] {"_id","LanguageID","LetterName","LetterPictureLowerCaseBlack",
                "LetterPictureLowerCaseBlue","LetterPictureUpperCaseBlack","LetterPictureUpperCaseRed",
                "LetterPictureUpperCaseDots","LetterPictureLowerCaseDots","LetterSound", "PhonicSound", "LetterLowerPath", "LetterUpperPath", "IsLetter"};
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
            letter.setIsLetter(cursor.getInt(13));
        }
        cursor.close();
        return letter;
    }

    public Letter getLetterByName(SQLiteDatabase db,int languageId, String letterName){
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

    public List<Integer> getWrongLetters(SQLiteDatabase db, int languageID, int letterID, int limit){
        List<Integer> letters = null;
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Letter where LanguageID = "+languageID+" and _id <> " + letterID + " ORDER BY RANDOM() LIMIT " + limit +";", null);

        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int letter = cursor.getInt(0);
                letters.add(letter);
            }
        }
        cursor.close();
        return letters;
    }

    public List<Letter> getLettersBelow(SQLiteDatabase db, int languageId, int unitId, int subId, int limit) {
        List<Letter> letters = null;
        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT " +
                "l._id, l.LanguageID, l.LetterName, " +
                "l.LetterPictureLowerCaseBlack, l.LetterPictureLowerCaseBlue, " +
                "l.LetterPictureUpperCaseBlack, l.LetterPictureUpperCaseRed, " +
                "l.LetterPictureUpperCaseDots, l.LetterPictureLowerCaseDots, " +
                "l.LetterSound, l.PhonicSound, " +
                "l.LetterLowerPath, l.LetterUpperPath, " +
                "l.IsLetter " +
                "FROM tbl_LetterSequence ls " +
                "INNER JOIN tbl_Letter l ON l._id = ls.LetterID " +
                "WHERE ls.LanguageID = " + languageId + " " +
                "AND " +
                "((ls.UnitID < " + unitId + ") OR (ls.UnitID = " + unitId + " AND ls.UnitSubID < " + subId + "))" +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Letter letter = new Letter();
                letter.setLetterId(cursor.getInt(cursor.getColumnIndex("_id")));
                letter.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageID")));
                letter.setLetterName(cursor.getString(cursor.getColumnIndex("LetterName")));
                letter.setLetterPictureLowerCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlack")));
                letter.setLetterPictureLowerCaseBlueURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlue")));
                letter.setLetterPictureUpperCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseBlack")));
                letter.setLetterPictureUpperCaseRedURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseRed")));
                letter.setLetterPictureUpperCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseDots")));
                letter.setLetterPictureLowerCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseDots")));
                letter.setLetterSoundURI(cursor.getString(cursor.getColumnIndex("LetterSound")));
                letter.setPhonicSoundURI(cursor.getString(cursor.getColumnIndex("PhonicSound")));
                letter.setLetterLowerPath(cursor.getString(cursor.getColumnIndex("LetterLowerPath")));
                letter.setLetterUpperPath(cursor.getString(cursor.getColumnIndex("LetterUpperPath")));
                letter.setIsLetter(cursor.getInt(cursor.getColumnIndex("IsLetter")));
                letters.add(letter);
            }
        }
        cursor.close();
        return letters;
    }

    public List<Letter> getLettersExcludingIds(SQLiteDatabase db, List<Integer> excludedLetterIds, int languageId, int limit) {

        List<Letter> letters = null;

        String excludedString = "";
        if (excludedLetterIds.size() == 0) {
            return null;
        }

        int numberOfExcludedLetterIds = excludedLetterIds.size();

        for (int i = 0; i < numberOfExcludedLetterIds; i++) {
            excludedString += excludedLetterIds.get(i);
            if (i < numberOfExcludedLetterIds - 1) {
                excludedString += ", ";
            }
        }

        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT " +
                "l._id, l.LanguageID, l.LetterName, " +
                "l.LetterPictureLowerCaseBlack, l.LetterPictureLowerCaseBlue, " +
                "l.LetterPictureUpperCaseBlack, l.LetterPictureUpperCaseRed, " +
                "l.LetterPictureUpperCaseDots, l.LetterPictureLowerCaseDots, " +
                "l.LetterSound, l.PhonicSound, " +
                "l.LetterLowerPath, l.LetterUpperPath, " +
                "l.IsLetter " +
                "FROM tbl_LetterSequence ls " +
                "INNER JOIN tbl_Letter l ON l._id = ls.LetterID " +
                "WHERE ls.LanguageID = " + languageId + " " +
                // "AND ls.UnitID <> " + unitId + " " +
                "AND l._id NOT IN (" + excludedString + ")" +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Letter letter = new Letter();
                letter.setLetterId(cursor.getInt(cursor.getColumnIndex("_id")));
                letter.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageID")));
                letter.setLetterName(cursor.getString(cursor.getColumnIndex("LetterName")));
                letter.setLetterPictureLowerCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlack")));
                letter.setLetterPictureLowerCaseBlueURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlue")));
                letter.setLetterPictureUpperCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseBlack")));
                letter.setLetterPictureUpperCaseRedURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseRed")));
                letter.setLetterPictureUpperCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseDots")));
                letter.setLetterPictureLowerCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseDots")));
                letter.setLetterSoundURI(cursor.getString(cursor.getColumnIndex("LetterSound")));
                letter.setPhonicSoundURI(cursor.getString(cursor.getColumnIndex("PhonicSound")));
                letter.setLetterLowerPath(cursor.getString(cursor.getColumnIndex("LetterLowerPath")));
                letter.setLetterUpperPath(cursor.getString(cursor.getColumnIndex("LetterUpperPath")));
                letter.setIsLetter(cursor.getInt(cursor.getColumnIndex("IsLetter")));
                letters.add(letter);
            }
            cursor.close();
        }
        return letters;
    }

    public List<Letter> getLettersExcludingLetterNames(SQLiteDatabase db, List<String> excludedLetterNames, int languageId, int limit) {

        List<Letter> letters = null;

        String excludedString = "";
        if (excludedLetterNames.size() == 0) {
            return null;
        }

        int numberOfExcludedLetterNames = excludedLetterNames.size();

        for (int i = 0; i < numberOfExcludedLetterNames; i++) {
            excludedString += "'" + excludedLetterNames.get(i) + "'";
            if (i < numberOfExcludedLetterNames - 1) {
                excludedString += ", ";
            }
        }

        Cursor cursor = db.rawQuery("" +
                "SELECT DISTINCT " +
                "l._id, l.LanguageID, l.LetterName, " +
                "l.LetterPictureLowerCaseBlack, l.LetterPictureLowerCaseBlue, " +
                "l.LetterPictureUpperCaseBlack, l.LetterPictureUpperCaseRed, " +
                "l.LetterPictureUpperCaseDots, l.LetterPictureLowerCaseDots, " +
                "l.LetterSound, l.PhonicSound, " +
                "l.LetterLowerPath, l.LetterUpperPath, " +
                "l.IsLetter " +
                "FROM tbl_LetterSequence ls " +
                "INNER JOIN tbl_Letter l ON l._id = ls.LetterID " +
                "WHERE ls.LanguageID = " + languageId + " " +
                "AND l.LetterName NOT IN (" + excludedString + ")" +
                "ORDER BY RANDOM() LIMIT " + limit + ";", null);

        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Letter letter = new Letter();
                letter.setLetterId(cursor.getInt(cursor.getColumnIndex("_id")));
                letter.setLanguageId(cursor.getInt(cursor.getColumnIndex("LanguageID")));
                letter.setLetterName(cursor.getString(cursor.getColumnIndex("LetterName")));
                letter.setLetterPictureLowerCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlack")));
                letter.setLetterPictureLowerCaseBlueURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseBlue")));
                letter.setLetterPictureUpperCaseBlackURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseBlack")));
                letter.setLetterPictureUpperCaseRedURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseRed")));
                letter.setLetterPictureUpperCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureUpperCaseDots")));
                letter.setLetterPictureLowerCaseDotsURI(cursor.getString(cursor.getColumnIndex("LetterPictureLowerCaseDots")));
                letter.setLetterSoundURI(cursor.getString(cursor.getColumnIndex("LetterSound")));
                letter.setPhonicSoundURI(cursor.getString(cursor.getColumnIndex("PhonicSound")));
                letter.setLetterLowerPath(cursor.getString(cursor.getColumnIndex("LetterLowerPath")));
                letter.setLetterUpperPath(cursor.getString(cursor.getColumnIndex("LetterUpperPath")));
                letter.setIsLetter(cursor.getInt(cursor.getColumnIndex("IsLetter")));
                letters.add(letter);
            }
        }
        cursor.close();
        return letters;
    }

    public List<Integer> getWrongSingleLetters(SQLiteDatabase db, int languageID, int letterID, int limit){
        List<Integer> letters = null;
        Cursor cursor = db.rawQuery("" +
                "SELECT _id FROM tbl_Letter " +
                "where LanguageID = "+languageID+" " +
                "and _id <> " + letterID + " " +
                "and length(letterName) = 1 " +
                "ORDER BY RANDOM() LIMIT " + limit +";", null);

        if (cursor.getCount() > 0) {
            letters = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int letter = cursor.getInt(0);
                letters.add(letter);
            }
        }
        cursor.close();
        return letters;
    }
}

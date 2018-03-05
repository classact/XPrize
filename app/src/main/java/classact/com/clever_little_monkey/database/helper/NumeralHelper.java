package classact.com.clever_little_monkey.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


import classact.com.clever_little_monkey.database.model.Numerals;

/**
 * Created by User on 1/8/2017.
 */

public class NumeralHelper {
    public static Numerals getNumeral(SQLiteDatabase db, int languageId, int number){
        String[] columns = new String[] {"_id","LanguageID","Number",
                "BoyGirl","NumberSound","NumberBlackPicture","NumberSparklePicture" };
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Numerals", columns, " Number=? AND LanguageID=?", new String[]{String.valueOf(number), String.valueOf(languageId)}, null, null, OrderBy);
        Numerals numeral = new Numerals();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            numeral.setLanguageID(cursor.getInt(1));
            numeral.setNumber(cursor.getInt(2));
            numeral.setBoyGirl(cursor.getInt(3));
            numeral.setSound(cursor.getString(4));
            numeral.setBlackImage(cursor.getString(5));
            numeral.setSparklingImage(cursor.getString(6));
        }
        cursor.close();
        return numeral;
    }

    public static Numerals getNumeral(SQLiteDatabase db, int numeralID){
        String[] columns = new String[] {"_id","LanguageID","Number",
                "BoyGirl","NumberSound","NumberBlackPicture","NumberSparklePicture" };
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Numerals", columns, "_id=?", new String[]{String.valueOf(numeralID),}, null, null, OrderBy);
        Numerals numeral = new Numerals();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            numeral.setLanguageID(cursor.getInt(1));
            numeral.setNumber(cursor.getInt(2));
            numeral.setBoyGirl(cursor.getInt(3));
            numeral.setSound(cursor.getString(4));
            numeral.setBlackImage(cursor.getString(5));
            numeral.setSparklingImage(cursor.getString(6));
        }
        cursor.close();
        return numeral;
    }

    /**
     * Get Numeral By Id
     * Duplicate of above method until we clean up everything
     * @param db
     * @param numeralID
     * @return
     */
    public static Numerals getNumeralById(SQLiteDatabase db, int numeralID){
        String[] columns = new String[] {"_id","LanguageID","Number",
                "BoyGirl","NumberSound","NumberBlackPicture","NumberSparklePicture" };
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Numerals", columns, "_id=?", new String[]{String.valueOf(numeralID),}, null, null, OrderBy);
        Numerals numeral = new Numerals();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            numeral.setLanguageID(cursor.getInt(1));
            numeral.setNumber(cursor.getInt(2));
            numeral.setBoyGirl(cursor.getInt(3));
            numeral.setSound(cursor.getString(4));
            numeral.setBlackImage(cursor.getString(5));
            numeral.setSparklingImage(cursor.getString(6));
        }
        cursor.close();
        return numeral;
    }

    public static ArrayList<Integer> getNumerals(SQLiteDatabase db, int languageID, int limit, int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " ORDER BY RANDOM() LIMIT " + limit +" order by _id;", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                   // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        }finally {
            cursor.close();
        }
    }
    public static ArrayList<Integer> getNumeralsBelowLimit(SQLiteDatabase db, int languageID, int limit, int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " AND Number > 0 AND Number <= " + limit +" order by _id;", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        } finally {
            cursor.close();
        }
    }

    public static List<Numerals> getNumeralsBelowAndEqualToLimit(SQLiteDatabase db, int languageID, int limit, int boyGirl, boolean includeZero) {

        List<Numerals> numerals = null;

        Cursor cursor = db.rawQuery("" +
                "SELECT _id, " +
                "LanguageID, " +
                "Number, " +
                "BoyGirl, " +
                "NumberSound, " +
                "NumberBlackPicture, " +
                "NumberSparklePicture " +
                "FROM tbl_Numerals " +
                "WHERE LanguageID = " + languageID + " " +
                "AND BoyGirl = " + boyGirl + " " +
                "AND Number " + ((includeZero) ? ">=" : ">") + " 0 " +
                "AND Number <= " + limit + " " +
                "ORDER by _id;", null);

        if (cursor.getCount() > 0) {
            numerals = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Numerals numeral = new Numerals();
                numeral.setNumeralID(cursor.getInt(cursor.getColumnIndex("_id")));
                numeral.setLanguageID(cursor.getInt(cursor.getColumnIndex("LanguageID")));
                numeral.setNumber(cursor.getInt(cursor.getColumnIndex("Number")));
                numeral.setBoyGirl(cursor.getInt(cursor.getColumnIndex("BoyGirl")));
                numeral.setSound(cursor.getString(cursor.getColumnIndex("NumberSound")));
                numeral.setBlackImage(cursor.getString(cursor.getColumnIndex("NumberBlackPicture")));
                numeral.setSparklingImage(cursor.getString(cursor.getColumnIndex("NumberSparklePicture")));
                numerals.add(numeral);
            }
        }
        cursor.close();
        return numerals;
    }

    public static ArrayList<Integer> getNumeralsBelowLimitRandom(SQLiteDatabase db, int languageID, int limitBelow, int limit, int numberToExclude,  int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " and number > 0 AND Number <= "+ limitBelow + " AND Number <> " + numberToExclude +" ORDER BY RANDOM() LIMIT "+ limit + ";", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        }finally {
            cursor.close();
        }
    }
    public static ArrayList<Integer> getNumeralsBelowLimitFromZero(SQLiteDatabase db, int languageID, int limit,  int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " AND Number <= " + limit + " order by Number;", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        }finally {
            cursor.close();
        }
    }

    public static ArrayList<Integer> getNumeralsBelowLimit235(SQLiteDatabase db, int languageID, int limit,  int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " AND Number in (2,3,5) order by _id;", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        }finally {
            cursor.close();
        }
    }
    public static ArrayList<Integer> getNumeralsBelowLimit12358(SQLiteDatabase db, int languageID, int limit,  int boyGirl){
        ArrayList<Integer> numerals = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_Numerals where LanguageID = "+languageID+ " and BoyGirl = " + boyGirl + " AND Number in (1,2,3,5,8) orderr by _id;", null);
        int numeral = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    // numeral = new Numerals();
                    numeral = cursor.getInt(0);
                    numerals.add(numeral);
                } while (cursor.moveToNext());
            }
            return numerals;
        }finally {
            cursor.close();
        }
    }
}

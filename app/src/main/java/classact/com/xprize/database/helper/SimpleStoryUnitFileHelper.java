package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.xprize.database.model.SimpleStoryUnitFiles;

/**
 * Created by JHB on 2016/12/16.
 */

public class SimpleStoryUnitFileHelper {
    public static SimpleStoryUnitFiles getSimpleStoryUnitFiles(SQLiteDatabase db, int simpleStoryUnitID ) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "FullSimpleStorySoundFile", "SimpleStoryImage", "CompInstr1", "CompInstr2"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SimpleStoryUnitFiles", columns, "_id=?", new String[]{String.valueOf(simpleStoryUnitID)}, null, null, null);
        SimpleStoryUnitFiles simpleStoryUnitFiles = new SimpleStoryUnitFiles();
        if (cursor.moveToFirst()) {
            do {
                simpleStoryUnitFiles = new SimpleStoryUnitFiles();
                simpleStoryUnitFiles.setSimpleStoryUnitID(cursor.getInt(0));
                simpleStoryUnitFiles.setLanguageID(cursor.getInt(1));
                simpleStoryUnitFiles.setUnitID(cursor.getInt(2));
                simpleStoryUnitFiles.setSimpleStoryUnitSoundFile(cursor.getString(3));
                simpleStoryUnitFiles.setSimpleStoryUnitImage(cursor.getString(4));
                simpleStoryUnitFiles.setCompInstr1(cursor.getString(5));
                simpleStoryUnitFiles.setCompInstr2(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return simpleStoryUnitFiles;
    }

    public static SimpleStoryUnitFiles getSimpleStoryUnitFiles(SQLiteDatabase db, int unitID, int languageID ) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "FullSimpleStorySoundFile", "SimpleStoryImage", "CompInstr1", "CompInstr2"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_SimpleStoryUnitFiles", columns, "LanguageID=? AND UnitID=?", new String[]{String.valueOf(languageID), String.valueOf(unitID)}, null, null, null);
        SimpleStoryUnitFiles simpleStoryUnitFiles = new SimpleStoryUnitFiles();
        if (cursor.moveToFirst()) {
            do {
                simpleStoryUnitFiles = new SimpleStoryUnitFiles();
                simpleStoryUnitFiles.setSimpleStoryUnitID(cursor.getInt(0));
                simpleStoryUnitFiles.setLanguageID(cursor.getInt(1));
                simpleStoryUnitFiles.setUnitID(cursor.getInt(2));
                simpleStoryUnitFiles.setSimpleStoryUnitSoundFile(cursor.getString(3));
                simpleStoryUnitFiles.setSimpleStoryUnitImage(cursor.getString(4));
                simpleStoryUnitFiles.setCompInstr1(cursor.getString(5));
                simpleStoryUnitFiles.setCompInstr2(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return simpleStoryUnitFiles;
    }
    public static ArrayList getSimpleStoryUnitFilesIDs(SQLiteDatabase db, int languageID, int unitId){
        ArrayList simpleStoryUnitIDs = new ArrayList();
        Cursor cursor = db.rawQuery("select _id from tbl_SimpleStoryUnitFiles where LanguageID = "+languageID+" and UnitID = " + unitId + ";", null);
        //DrillWords drillWord = new DrillWords();
        int simpleStoryUnitID=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    simpleStoryUnitID = cursor.getInt(0);
                    //drillWord.setDrillID(cursor.getInt(0));
                    simpleStoryUnitIDs.add(simpleStoryUnitID);
                } while (cursor.moveToNext());
            }
            return simpleStoryUnitIDs;
        }finally {
            cursor.close();
        }
    }
}

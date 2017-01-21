package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.xprize.database.model.Comprehension;

/**
 * Created by JHB on 2016/12/16.
 */

public class ComprehensionHelper {
    public static Comprehension getComprehensionQA(SQLiteDatabase db, int comprehensionID ) {

        String[] columns = new String[]{"_id", "LanguageID", "UnitID", "QA_No", "QuestionHasSoundAnswer", "QuestionSound", "AnswerSound", "NumberOfPictures", "CorrectPicture", "Picture1", "Picture2", "Picture3"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_Comprehension", columns, "_id=?", new String[]{String.valueOf(comprehensionID)}, null, null, null);
        Comprehension comprehensionQA = new Comprehension();
        if (cursor.moveToFirst()) {
            do {
                comprehensionQA = new Comprehension();
                comprehensionQA.setComprehensionID(cursor.getInt(0));
                comprehensionQA.setLanguageID(cursor.getInt(1));
                comprehensionQA.setUnitID(cursor.getInt(2));
                comprehensionQA.setqA_No(cursor.getString(3));
                comprehensionQA.setQuestionHasSoundAnswer(cursor.getInt(4));
                comprehensionQA.setQuestionSound(cursor.getString(5));
                comprehensionQA.setAnswerSound(cursor.getString(6));
                comprehensionQA.setNumberOfPictures(cursor.getInt(7));
                comprehensionQA.setCorrectPicture(cursor.getString(8));
                comprehensionQA.setPicture1(cursor.getString(9));
                comprehensionQA.setPicture2(cursor.getString(10));
                comprehensionQA.setPicture3(cursor.getString(11));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return comprehensionQA;
    }

    public static ArrayList getComprehensionIDs(SQLiteDatabase db, int languageID, int unitId){
        ArrayList comprehensionQAs = new ArrayList();
        Cursor cursor = db.rawQuery("select _id from tbl_Comprehension where LanguageID = "+languageID+" and UnitID = " + unitId + ";", null);
        //DrillWords drillWord = new DrillWords();
        int comprehensionID=0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //drillWord = new DrillWords();
                    comprehensionID = cursor.getInt(0);
                    //drillWord.setDrillID(cursor.getInt(0));
                    comprehensionQAs.add(comprehensionID);
                } while (cursor.moveToNext());
            }
            return comprehensionQAs;
        }finally {
            cursor.close();
        }
    }
}

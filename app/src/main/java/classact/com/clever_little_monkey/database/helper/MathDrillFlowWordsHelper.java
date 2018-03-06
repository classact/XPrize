package classact.com.clever_little_monkey.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import classact.com.clever_little_monkey.database.model.MathDrillFlowWords;
import classact.com.clever_little_monkey.database.model.Numerals;

/**
 * Created by JHB on 2016/12/21.
 */

public class MathDrillFlowWordsHelper {
    public static MathDrillFlowWords getMathDrillFlowWords(SQLiteDatabase db, int drillId, int subId, int languageID){
        String[] columns = new String[] {"_id","LanguageID","DrillID", "SubID",
                "DrillSound1", "DrillSound2",
                "DrillSound3", "DrillSound4", "DrillSound5", "DrillSound6", "DrillSound7"};
        String OrderBy = "drillID asc";
        Cursor cursor = db.query("tbl_Math_DrillFlowWords", columns, "DrillID=? and SubID=? and LanguageID=?", new String[]{String.valueOf(drillId),String.valueOf(subId),String.valueOf(languageID)}, null, null, OrderBy);
        MathDrillFlowWords drillFlowWord = new MathDrillFlowWords();
        try {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                drillFlowWord.setDrillFlowWordID(cursor.getInt(0));
                drillFlowWord.setLanguageID(cursor.getInt(1));
                drillFlowWord.setDrillID(cursor.getInt(2));
                drillFlowWord.setSubID(cursor.getInt(3));
                drillFlowWord.setDrillSound1(cursor.getString(4).trim());
                drillFlowWord.setDrillSound2(cursor.getString(5).trim());
                drillFlowWord.setDrillSound3(cursor.getString(6).trim());
                drillFlowWord.setDrillSound4(cursor.getString(7).trim());
                drillFlowWord.setDrillSound5(cursor.getString(8).trim());
                drillFlowWord.setDrillSound6(cursor.getString(9).trim());
                drillFlowWord.setDrillSound7(cursor.getString(10).trim());
            }
            return drillFlowWord;

        }finally {
            cursor.close();
        }
    }

    public static List<String> getInstructions(SQLiteDatabase db, int languageID, int drillId, int subId) {

        List<String> instructions = null;

        Cursor cursor = db.rawQuery("SELECT " +
                "DrillSound1, " +
                "DrillSound2, " +
                "DrillSound3, " +
                "DrillSound4, " +
                "DrillSound5, " +
                "DrillSound6, " +
                "DrillSound7 " +
                "FROM tbl_Math_DrillFlowWords " +
                "WHERE LanguageID = " + languageID + " " +
                "AND DrillID = " + drillId + " " +
                "AND SubID = " + subId + ";", null);

        if (cursor.getCount() > 0) {
            instructions = new ArrayList<>();
            cursor.moveToFirst();

            // Get trimmed instructions
            String instruction01 = cursor.getString(cursor.getColumnIndex("DrillSound1")).trim();
            String instruction02 = cursor.getString(cursor.getColumnIndex("DrillSound2")).trim();
            String instruction03 = cursor.getString(cursor.getColumnIndex("DrillSound3")).trim();
            String instruction04 = cursor.getString(cursor.getColumnIndex("DrillSound4")).trim();
            String instruction05 = cursor.getString(cursor.getColumnIndex("DrillSound5")).trim();
            String instruction06 = cursor.getString(cursor.getColumnIndex("DrillSound6")).trim();
            String instruction07 = cursor.getString(cursor.getColumnIndex("DrillSound7")).trim();

            // Add instructions if they exist
            if (instruction01.length() > 0) instructions.add(instruction01);
            if (instruction02.length() > 0) instructions.add(instruction02);
            if (instruction03.length() > 0) instructions.add(instruction03);
            if (instruction04.length() > 0) instructions.add(instruction04);
            if (instruction05.length() > 0) instructions.add(instruction05);
            if (instruction06.length() > 0) instructions.add(instruction06);
            if (instruction07.length() > 0) instructions.add(instruction07);
        }
        cursor.close();
        return instructions;
    }
}

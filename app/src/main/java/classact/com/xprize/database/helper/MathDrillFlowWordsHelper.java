package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import classact.com.xprize.database.model.MathDrillFlowWords;

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
}

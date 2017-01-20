package classact.com.xprize.database.helpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.xprize.model.DrillControl;
import classact.com.xprize.model.DrillFlowWords;

/**
 * Created by JHB on 2016/12/21.
 */

public class DrillFlowWordsHelper {
    public static DrillFlowWords getDrillFlowWords(SQLiteDatabase db, int drillId, int languageID){
        String[] columns = new String[] {"_id","LanguageID","DrillID", "PhonicSoundStart",
                "WordSoundStart", "StorySound", "MathSound", "CorrectSound",
                "WrongSound", "LevelCompleteSound", "DrillSound1", "DrillSound2",
                "DrillSound3", "DrillSound4", "DrillSound5", "DrillSound6", "DrillSound7", "DrillSound8", "DrillSound9"};
        String OrderBy = "drillID asc";
        Cursor cursor = db.query("tbl_DrillFlowWords", columns, "DrillID=? and LanguageID=?", new String[]{String.valueOf(drillId),String.valueOf(languageID)}, null, null, OrderBy);
        DrillFlowWords drillFlowWord = new DrillFlowWords();
        try {
            if (cursor.getCount()>0) {
                cursor.moveToFirst();
                drillFlowWord.setDrillFlowWordID(cursor.getInt(0));
                drillFlowWord.setLanguageID(cursor.getInt(1));
                drillFlowWord.setDrillID(cursor.getInt(2));
                drillFlowWord.setPhonicSoundStart(cursor.getString(3));
                drillFlowWord.setWordSoundStart(cursor.getString(4));
                drillFlowWord.setStorySound(cursor.getString(5));
                drillFlowWord.setMathSound(cursor.getString(6));
                drillFlowWord.setCorrectSound(cursor.getString(7));
                drillFlowWord.setWrongSound(cursor.getString(8));
                drillFlowWord.setLevelCompleteSound(cursor.getString(9));
                drillFlowWord.setDrillSound1(cursor.getString(10));
                drillFlowWord.setDrillSound2(cursor.getString(11));
                drillFlowWord.setDrillSound3(cursor.getString(12));
                drillFlowWord.setDrillSound4(cursor.getString(13));
                drillFlowWord.setDrillSound5(cursor.getString(14));
                drillFlowWord.setDrillSound6(cursor.getString(15));
                drillFlowWord.setDrillSound7(cursor.getString(16));
                drillFlowWord.setDrillSound8(cursor.getString(17));
                drillFlowWord.setDrillSound9(cursor.getString(18));
            }
            return drillFlowWord;

        }finally {
            cursor.close();
        }
    }
}

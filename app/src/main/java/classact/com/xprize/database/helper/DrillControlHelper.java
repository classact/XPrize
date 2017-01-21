package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import classact.com.xprize.database.model.DrillControl;

public class DrillControlHelper {
    public static ArrayList<DrillControl> getDrillControls(SQLiteDatabase db,int drillId){
        ArrayList<DrillControl> drillControls = new ArrayList<>();
        String[] columns = new String[] {"_id","WordAndLetter","LetterOnly","DrawDrill", "SentenceOnly", "WordOnly", "NoOfPhrases"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_DrillControl", columns, "_id=?", new String[]{String.valueOf(drillId)}, null, null, OrderBy);
        DrillControl drillControl = new DrillControl();
        if (cursor.moveToFirst()) {
            do {
                drillControl = new DrillControl();
                drillControl.setDrillID(cursor.getInt(cursor.getColumnIndex("_id")));
                drillControl.setWordAndLetter(cursor.getInt(cursor.getColumnIndex("WordAndLetter")));
                drillControl.setLetterOnly(cursor.getInt(cursor.getColumnIndex("LetterOnly")));
                drillControl.setDrawDrill(cursor.getInt(cursor.getColumnIndex("DrawDrill")));
                drillControl.setSentenceOnly(cursor.getInt(cursor.getColumnIndex("SentenceOnly")));
                drillControl.setWordOnly(cursor.getInt(cursor.getColumnIndex("WordOnly")));
                drillControl.setNoOfPhrases(cursor.getInt(cursor.getColumnIndex("NoOfPhrases")));
                drillControls.add(drillControl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return drillControls;
    }

}

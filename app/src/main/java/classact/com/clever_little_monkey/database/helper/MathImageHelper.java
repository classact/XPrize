package classact.com.clever_little_monkey.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classact.com.clever_little_monkey.database.model.MathImages;

public class MathImageHelper {
    public static MathImages getMathImage(SQLiteDatabase db, int mathImageID){
        String[] columns = new String[] {"_id","LanguageID","UnitID","DrillID","ImageName",
                "NumberOfImages", "ImageSound", "TestNumber", "NumberOfImagesSound" };
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_MathImages", columns, " _id=?" , new String[]{String.valueOf(mathImageID)}, null, null, OrderBy);
        MathImages mathImages = new MathImages();
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            mathImages.setMathImageID(cursor.getInt(0));
            mathImages.setLanguageID(cursor.getInt(1));
            mathImages.setUnitID(cursor.getInt(2));
            mathImages.setDrillID(cursor.getInt(3));
            mathImages.setImageName(cursor.getString(4));
            mathImages.setNumberOfImages(cursor.getInt(5));
            mathImages.setImageSound(cursor.getString(6));
            mathImages.setTestNumber(cursor.getInt(7));
            mathImages.setNumberOfImagesSound(cursor.getString(8));
        }
        cursor.close();
        return mathImages;
    }

    public static ArrayList<Integer> getMathImageList(SQLiteDatabase db, int unitID, int drillID, int languageID){
        ArrayList<Integer> mathImages = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT _id FROM tbl_MathImages where LanguageID = "+languageID+ " and UnitID = "+unitID+ " and DrillID = " + drillID +";", null);
        int mathImage = 0;
        try {
            if (cursor.moveToFirst()) {
                do {
                    //mathImage = new MathImages();
                    mathImage = cursor.getInt(0);
                    mathImages.add(mathImage);
                } while (cursor.moveToNext());
            }
            return mathImages;
        }finally {
            cursor.close();
        }
    }
}
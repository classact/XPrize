package classact.com.xprize.database.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import classact.com.xprize.database.model.Language;
import java.util.ArrayList;
/**
 * Created by Tseliso on 5/8/2016.
 */
public class LanguageHelper {
    public static ArrayList<Language> getLanguages(SQLiteDatabase db){
        ArrayList<Language> languages = new ArrayList<>();
        String[] columns = new String[] {"_id","Language"};
        String OrderBy = "_id asc";
        Cursor cursor = db.query("tbl_language", columns, null, null, null, null, OrderBy);
        Language language = new Language();
        if (cursor.moveToFirst()) {
            do {
                language = new Language();
                language.setLanguageId(cursor.getInt(0));
                language.setLanguageName(cursor.getString(1));
                languages.add(language);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return languages;
    }
}


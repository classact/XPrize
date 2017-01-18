package classact.com.xprize.database;

import android.content.Context;
import android.database.sqlite.*;


import classact.com.xprize.database.helpers.DrillControlHelper;
import classact.com.xprize.database.helpers.DrillFlowWordsHelper;
import classact.com.xprize.database.helpers.MathDrillFlowWordsHelper;
import classact.com.xprize.database.helpers.DrillWordHelper;
import classact.com.xprize.database.helpers.LanguageHelper;
import classact.com.xprize.database.helpers.LetterHelper;
import classact.com.xprize.database.helpers.LetterSequenceHelper;
import classact.com.xprize.database.helpers.SentenceHelper;
import classact.com.xprize.database.helpers.UnitHelper;
import classact.com.xprize.database.helpers.WordHelper;
import classact.com.xprize.model.*;
import java.util.*;
/**
 * Created by Tseliso on 5/8/2016.
 */
public class DB {
    private SQLiteDatabase db;
    private final Context context;
    private final DbHelper dbhelper;

    public DB(Context c){
        context = c;
        dbhelper = new DbHelper(context);
    }

    public void close(){
        db.close();
    }

    public void open() throws SQLiteException{
        try {
            if (db == null)
                db = dbhelper.getWritableDatabase();
            else if (!db.isOpen())
                db = dbhelper.getWritableDatabase();
        }
        catch(SQLiteException ex) {
            db = dbhelper.getReadableDatabase();
        }
    }

    public boolean isReadOnly()
    {
        try{
            return db.isReadOnly();
        }
        catch (SQLiteException ex)
        {
            return false;
        }
    }

    public boolean isOpen() {
        try {
            return db.isOpen();
        } catch (SQLiteException ex) {
            return false;
        }
    }

    public int updateUnit(Unit unit){
        return UnitHelper.updateUnitInfo(db,unit);
    }

    public Unit getUnitInfo(int ID){
        return UnitHelper.getUnitInfo(db, ID);
    }

    public ArrayList<Unit> getAllUnits(){
        return UnitHelper.getAllUnits(db);
    }

    public int getUnitToBePlayed(){
        return UnitHelper.getUnitToBePlayed(db);
    }

    public ArrayList<Language> getLanguages(){
        return LanguageHelper.getLanguages(db);
    }

    public List getDrillWords(int languageID, int unitId, int subId, int drillId, int wordType, int limit){
        return DrillWordHelper.getDrillWords(db, languageID, unitId, subId, drillId, wordType, limit);
    }

    public List getWrongDrillWords(int languageID, int unitId, int subId, int drillId, int wordType, int limit){
        return DrillWordHelper.getWrongDrillWords(db, languageID, unitId, subId, drillId, wordType, limit);
    }

    public DrillFlowWords getDrillFlowWords(int DFW, int languageID){
        return DrillFlowWordsHelper.getDrillFlowWords(db, DFW, languageID);
    }

    public MathDrillFlowWords getMathDrillFlowWords(int DFW, int subId, int languageID){
        return MathDrillFlowWordsHelper.getMathDrillFlowWords(db, DFW, subId, languageID);
    }

    public ArrayList<DrillControl> getDrillControl(int drillId){
        return DrillControlHelper.getDrillControls(db, drillId);
    }

    public Letter getLetter(int languageID, int id){
        return LetterHelper.getLetter(db, languageID, id);
    }

    public ArrayList<Letter> getLetters(int languageId, int id){
        return LetterHelper.getLetters(db, languageId, id);
    }

    public Word getWord(int id){
        return WordHelper.getWord(db, id);
    }

    public ArrayList<Word> getWords(){
        return WordHelper.getWords(db);
    }

    public ArrayList<LetterSequence> getLetterSequence(int languageId, int unitId, int subId){
        return LetterSequenceHelper.getLetterSequence(db, languageId, unitId, subId);
    }

    public SentenceDB getSentence(int sentenceId){
        return SentenceHelper.getSentence(db, sentenceId);
    }


}

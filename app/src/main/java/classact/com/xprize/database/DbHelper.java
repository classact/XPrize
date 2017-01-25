package classact.com.xprize.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import classact.com.xprize.database.helper.SQLiteAssetHelper;

/**
 * Created by Tseliso on 5/8/2016.
 */


public class DbHelper extends SQLiteAssetHelper {
    private static DbHelper myDbHelper;
	private static String DATABASE_NAME = "CA_XPrize.db";
	private static String DATABASE_PATH;
	private static int DATABASE_VERSION=1;
	private SQLiteDatabase myDatabase;
	private final Context myContext;

    public static DbHelper getDbHelper(Context c) {

        if (myDbHelper == null) {
            myDbHelper = new DbHelper(c);
            System.out.println("--------------------------------------------");
            System.out.println("DbHelper.getDbHelper > Debug: create new");
            System.out.println("--------------------------------------------");
        } else {
            System.out.println("--------------------------------------------");
            System.out.println("DbHelper.getDbHelper > Debug: re-use");
            System.out.println("--------------------------------------------");
        }

        return myDbHelper;
    }

	private DbHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = c;
		this.DATABASE_PATH = c.getApplicationInfo().dataDir + "/databases/";
	}

    public void createDatabase(boolean recopy) throws IOException {
        boolean dbExist = checkDatabase();
        if(dbExist){
            // do nothing here, the database exists already
            if (recopy) {
                System.out.println("------------ Recopy DB");
                copyDatabase();
            } else {
                System.out.println("------------ Use existing DB");
            }
        }else{
            //by doing this we will be able to copy our existing db over the blank db that is
            //created when the app first ran
            try {
                this.getReadableDatabase();
            } catch (SQLiteException sqle) {
            }

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

	public void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		if(dbExist){
			// do nothing here, the database exists already
			// copyDatabase();
		}else{
			//by doing this we will be able to copy our existing db over the blank db that is
			//created when the app first ran
			try {
				this.getReadableDatabase();
			} catch (SQLiteException sqle) {
			}

			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}
	private boolean checkDatabase() {
		// check if our db already exists. if so we don't want to copy over it every time the app runs
		// we return true if it exists and false if it doesn't

		SQLiteDatabase checkDB = null;
		try {
			String dbPath = DATABASE_PATH + DATABASE_NAME;
			File file = new File(dbPath);
			if (file.exists() && !file.isDirectory())
				checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e){
			//database does not exist yet
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDatabase() throws IOException{
		//Open our local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		//Path to the just created empty DB
		String outFilename = DATABASE_PATH + DATABASE_NAME;

		//open the empty DB as the output stream
		OutputStream myOutput = new FileOutputStream(outFilename);

		// transfer the bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer,0,length);
		}

		//close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDatabase() throws SQLException {
		//open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

	}
	@Override

	public synchronized void close(){
		if(myDatabase != null)
			myDatabase.close();

		super.close();

        System.out.println("--------------------------------------------");
		System.out.println("DbHelper.close > Debug: database closed");
        System.out.println("--------------------------------------------");
	}




	private void deleteTables(SQLiteDatabase db)
	{

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//deleteTables(db);
		//onCreate(db);
	}
}

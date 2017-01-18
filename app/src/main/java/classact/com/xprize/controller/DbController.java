package classact.com.xprize.controller;


import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import java.io.IOException;

import classact.com.xprize.database.DbHelper;
import classact.com.xprize.model.Unit;

public class DbController {

    private static boolean mInitialized = false;
    private static Context mContext;
    private static DbHelper mDb;

    // Unit Management
    private static Unit mCurrentUnit;

    /**
     *
     * @return
     */
    public static DbHelper DB(Context context) {

        // Return saved db if already existant
        if (mDb != null) {
            return mDb;
        }

        // If not existant, check if initialized
        if (!mInitialized) {
            if (context != null) {
                mContext = context;

                // Create new DbHelper as it's not been set yet
                mDb = new DbHelper(mContext);

                // Try create database
                try {
                    mDb.createDatabase();
                } catch (IOException ioex) {
                    Log.e("DbController.java", "getDB: cannot create database - " + ioex.getMessage());
                    return null;
                }

                // Try open the database
                try {
                    mDb.openDatabase();
                } catch (SQLException sqlex) {
                    Log.e("DbController.java", "getDB: cannot open database - " + sqlex.getMessage());
                    return null;
                }

                // Set initialized flag to true
                mInitialized = true;

                return mDb;
            } else {
                Log.e("DbController.java", "DB: invalid context");
                return null;
            }
        }

        // Should be impossible to reach
        Log.e("DbController.java", "DB: How in the world did I get here?");
        return null;
    }

    /**
     *
     * @param context
     */
    public static void init(Context context) {
        if (!mInitialized && context != null) {
            mInitialized = true;
            mContext = context;
            Log.d("DbController.java", "init: success");
        } else if (mInitialized) {
            Log.d("DbController.java", "init: already initialized");
        } else {
            Log.e("DbController.java", "init: failure");
        }
    }


}